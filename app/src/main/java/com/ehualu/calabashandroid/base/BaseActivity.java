package com.ehualu.calabashandroid.base;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.db.entity.UploadEntity;
import com.ehualu.calabashandroid.db.manager.EntityManager;
import com.ehualu.calabashandroid.responseBean.CreateTaskIdResponse;
import com.ehualu.calabashandroid.service.FileUploader;
import com.ehualu.calabashandroid.upload.ComponentsGetter;
import com.ehualu.calabashandroid.upload.UploadChunk;
import com.ehualu.calabashandroid.upload.UploadTool;
import com.ehualu.calabashandroid.utils.Constants;
import com.ehualu.calabashandroid.utils.FileUtils;
import com.ehualu.calabashandroid.utils.StatusBarUtil;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener, ComponentsGetter {

    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_TIME = 2;

    public static final int GRID_VIEW = 1;
    public static final int LIST_VIEW = 2;

    public int sortType = SORT_BY_NAME;
    public int viewType = GRID_VIEW;
    public boolean isSelectMode = false;

    private RxPermissions rxPermissions;

    private Handler mHandler = new Handler();

    public Handler getmHandler() {
        return mHandler;
    }


    //上传服务
    private ServiceConnection mUploadServiceConnection;
    private FileUploader.FileUploaderBinder mUploaderBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制所有界面竖屏
        ImmersionBar.with(this).fitsSystemWindows(false).init();
        //add by houxiansheng 2019-12-5 17:40:34 动态修改StatusBar的字体颜色：修改顶部时间颜色为黑色
        StatusBarUtil.setStatusBar(this, false, false);

        rxPermissions = new RxPermissions(this);
        mUploadServiceConnection = new UploadListServiceConnection();
        bindService(new Intent(this, FileUploader.class), mUploadServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (mUploadServiceConnection != null) {
            unbindService(mUploadServiceConnection);
            mUploadServiceConnection = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * add by  houxiansheng 2019-12-16 15:52:38 获取Activity显示的Fragment
     */
    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }

    @Override
    public FileUploader.FileUploaderBinder getFileUploaderBinder() {
        return mUploaderBinder;
    }

    private class UploadListServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof FileUploader.FileUploaderBinder) {
                if (mUploaderBinder == null) {
                    mUploaderBinder = (FileUploader.FileUploaderBinder) service;
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (name.equals(new ComponentName(BaseActivity.this, FileUploader.class))) {
                mUploaderBinder = null;
            }
        }
    }

    public void startUpload(File originFile, String dirID) {
        rxPermissions.request(Manifest.permission.FOREGROUND_SERVICE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //创建上传任务id
                            ApiRetrofit.getInstance().createTask()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.computation())
                                    .subscribe(new Observer<CreateTaskIdResponse>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(CreateTaskIdResponse createTaskIdResponse) {
                                            if (createTaskIdResponse.isSuccess()) {
                                                String taskId = createTaskIdResponse.getData().getTaskId();
                                                //切割文件
                                                List<File> files = UploadTool.splitFile(originFile);
                                                //向上传表插入一条记录
                                                UploadEntity entity = new UploadEntity();
                                                entity.setTaskId(taskId);
                                                entity.setStatus(0);//等待中
                                                entity.setUpdateTime(System.currentTimeMillis());
                                                entity.setFileSize(originFile.length());
                                                entity.setDirId(dirID);
                                                entity.setCreateTime(System.currentTimeMillis());
                                                entity.setFileName(originFile.getName());
                                                entity.setPath(originFile.getAbsolutePath());

                                                Observable.create(new ObservableOnSubscribe<Object>() {

                                                    @Override
                                                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                                        EntityManager.getInstance().getUploadEntityDao().insert(entity);
                                                        emitter.onNext("success");
                                                    }
                                                }).subscribe(new Consumer<Object>() {

                                                    @Override
                                                    public void accept(Object o) throws Exception {
                                                        Intent intent = new Intent(Constants.UPLOAD_TABLE_INSERT);
                                                        intent.putExtra("taskId", taskId);
                                                        LocalBroadcastManager.getInstance(BaseActivity.this).sendBroadcast(intent);

                                                        //给每个分片创建Runnable
                                                        for (int i = 0; i < files.size(); i++) {
                                                            File f = files.get(i);
                                                            UploadChunk chunk = createPiece(taskId, i, originFile, files.size(), f, dirID);
                                                            FileUploader.UploadRequester requester = new FileUploader.UploadRequester();
                                                            requester.uploadFile(BaseActivity.this, originFile.length(), chunk);
                                                        }
                                                    }
                                                });
                                            } else {
                                                ToastUtil.showCenterForBusiness(BaseActivity.this, "创建上传任务失败!");
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            ToastUtil.showCenterForBusiness(BaseActivity.this, "创建上传任务失败!");
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        }
                    }
                });
    }

    private UploadChunk createPiece(String taskId, long index, File originFile, long chunkTotal, File partFile, String dirID) {
        UploadChunk chunk = new UploadChunk();
        chunk.setChunkId(taskId + "_chunk" + index);
        chunk.setTaskId(taskId);
        chunk.setTargetDirId(dirID);
        String str = Base64.encodeToString((originFile.getName()).getBytes(), Base64.DEFAULT);
        chunk.setFileName(str.replace("\n", ""));
        chunk.setChunkNum(index);
        chunk.setChunkSize(UploadTool.UPLOAD_CHUNK_SIZE);
        chunk.setChunkTotal(chunkTotal);
        chunk.setMd5(FileUtils.getFileMD5(originFile));
        chunk.setPartFile(partFile);
        return chunk;
    }

}
