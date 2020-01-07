package com.ehualu.calabashandroid.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.db.entity.UploadEntity;
import com.ehualu.calabashandroid.db.manager.EntityManager;
import com.ehualu.calabashandroid.model.TransferModel;
import com.ehualu.calabashandroid.responseBean.UploadNotifyResponse;
import com.ehualu.calabashandroid.upload.OnDatatransferProgressListener;
import com.ehualu.calabashandroid.upload.ProgressListener;
import com.ehualu.calabashandroid.upload.UploadChunk;
import com.ehualu.calabashandroid.upload.UploadRunnable;
import com.ehualu.calabashandroid.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 文件上传的服务
 */
public class FileUploader extends Service implements OnDatatransferProgressListener {

    private IBinder mBinder;
    private ExecutorService es;

    public static final String UPLOAD_COMPLETE = "upload_complete";

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new FileUploaderBinder();
        es = Executors.newFixedThreadPool(5);//5个固定线程进行文件上传
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return Service.START_NOT_STICKY;
        }
        int type = intent.getIntExtra("type", 0);
        if (type == 1) {
            createNotificationChannel(type);
        } else {
        }
        UploadChunk uploadChunk = (UploadChunk) intent.getSerializableExtra("uploadChunk");
        long originFileSize = intent.getLongExtra("originFileSize", 0);
        UploadRunnable runnable = new UploadRunnable(uploadChunk, originFileSize);
        runnable.addDataTransferProgressListener(this);
        runnable.addDataTransferProgressListener((OnDatatransferProgressListener) mBinder);
        es.submit(runnable);
        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("111", "上传文件", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(this, "111").build();
            startForeground(type, notification);
        } else {
        }
    }

    @Override
    public void notifyUpdateProgress(String taskId, long current, long total, long speed) {
        if (current == -10000) {
            //表示文件上传完成
            ApiRetrofit.getInstance().notifyUploadStatus(taskId).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UploadNotifyResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(UploadNotifyResponse uploadNotifyResponse) {
                            if (uploadNotifyResponse.isSuccess()) {
                                //传输完成，更新上传任务表的status
                                String dirID = EntityManager.getInstance().updateUploadEntity(taskId, 4, uploadNotifyResponse.getData().getFilePath(), 0);
                                //发送广播，更新界面
                                Log.e("传完了", taskId + "");
                                Intent intent = new Intent(UPLOAD_COMPLETE);
                                intent.putExtra("taskId", taskId);
                                intent.putExtra("dirID", dirID);
                                LocalBroadcastManager.getInstance(FileUploader.this).sendBroadcast(intent);

                                // ToastUtil.showCenterHasImageToast(MyApp.getAppContext(), "通知服务器成功" + uploadNotifyResponse.getMessage());
                            } else {
                                Log.e("传完了2", taskId + "--------------" + uploadNotifyResponse.getMessage());
                                //ToastUtil.showCenterHasImageToast(MyApp.getAppContext(), "通知服务器失败" + uploadNotifyResponse.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("传完了3", "--------------" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    /**
     * 上传服务binder实现进度监听接口
     */
    public class FileUploaderBinder extends Binder implements OnDatatransferProgressListener {

        private Map<String, OnDatatransferProgressListener> mBoundListeners = new HashMap<>();

        public boolean containTask(String taskId, int hashcode) {
            ProgressListener listener = (ProgressListener) mBoundListeners.get(taskId);
            if (listener != null && listener.getKkey().equals(hashcode + taskId)) {
                return true;
            }
            return false;
        }

        public void removeDatatransferProgressListener(String taskId) {
            ProgressListener listener = (ProgressListener) mBoundListeners.get(taskId);
            if (listener != null) {
                mBoundListeners.remove(listener);
            }
        }

        public void addDatatransferProgressListener(OnDatatransferProgressListener listener) {
            ProgressListener progressListener = (ProgressListener) listener;
            mBoundListeners.put(progressListener.getModel().getTaskId(), progressListener);
        }

        @Override

        public void notifyUpdateProgress(String taskId, long current, long total, long speed) {
            //分发所有的上传进度给对应的上传任务
            if (current >= 0) {
                OnDatatransferProgressListener listener = mBoundListeners.get(taskId);
                if (listener != null) {
                    listener.notifyUpdateProgress(taskId, current, total, speed);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        mBinder = null;
        es.shutdown();
        super.onDestroy();
    }

    /**
     * 上传请求
     */
    public static class UploadRequester {

        public void uploadFile(Context context, long originFileSize, UploadChunk uploadChunk) {
            Intent intent = new Intent(context, FileUploader.class);
            intent.putExtra("originFileSize", originFileSize);
            intent.putExtra("uploadChunk", uploadChunk);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                intent.putExtra("type", 1);
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }
}
