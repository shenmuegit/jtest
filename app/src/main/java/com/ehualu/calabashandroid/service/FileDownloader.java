package com.ehualu.calabashandroid.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.util.Log;

import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.db.DownLoadRecordEntityDao;
import com.ehualu.calabashandroid.db.entity.DownLoadRecordEntity;
import com.ehualu.calabashandroid.db.manager.DBManager;
import com.ehualu.calabashandroid.model.DownloadTask;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.receiver.DownloadTaskBroadcast;
import com.ehualu.calabashandroid.utils.BroadcastUtils;
import com.ehualu.calabashandroid.utils.ThreadPoolProxy;

import androidx.annotation.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ehualu.calabashandroid.utils.Constants.DOWNLOAD_PATH;
import static com.ehualu.calabashandroid.utils.Constants.PART_SIZE;

/**
 * 文件下载服务
 */
public class FileDownloader extends Service {

    public static final String TAG = "FileDownloader";

    private ThreadPoolProxy threadPool;
    private String partPath;

    //待下载任务
    private LinkedList<DownloadTask> waitTasks = new LinkedList<>();
    //下载中任务
    private LinkedList<DownloadTask> loadTasks = new LinkedList<>();

    Handler handler = new Handler(Looper.getMainLooper());


    @Override
    public void onCreate() {
        super.onCreate();
        threadPool = ThreadPoolProxy.newProxy(3);//固定5个子线程
        partPath = DOWNLOAD_PATH + ".part" + File.separator;
        File partFolder = new File(partPath);
        if (!partFolder.exists()) {
            partFolder.mkdirs();
        }
        DownloadTaskBroadcast broadcast = new DownloadTaskBroadcast(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastUtils.DOWNLOAD_TASK);
        registerReceiver(broadcast, intentFilter);


        List<DownLoadRecordEntity> loadings = DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().queryBuilder()
                .whereOr(DownLoadRecordEntityDao.Properties.Status.eq("1")
                        , DownLoadRecordEntityDao.Properties.Status.eq("0"))
                .where(DownLoadRecordEntityDao.Properties.FileType.eq("1"))
                .orderAsc(DownLoadRecordEntityDao.Properties.CreateTime)
                .list();

        for (DownLoadRecordEntity entity : loadings) {
            RemoteFile file = new RemoteFile();
            file.setFileSize(entity.getFileSize());
            file.setFileName(entity.getFileName());
            file.setID(entity.getFileId());
            DownloadTask task = new DownloadTask(entity.getTaskId(), file, entity.getCreateTime());
            task.setDownLoadRecordEntity(entity);
            List<DownLoadRecordEntity> parts = DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().queryBuilder()
                    .where(DownLoadRecordEntityDao.Properties.TaskId.eq(task.id))
                    .where(DownLoadRecordEntityDao.Properties.FileType.eq("2"))
                    .orderAsc(DownLoadRecordEntityDao.Properties.PartNum)
                    .list();
            task.setParts(parts);
            if (task.getStatus() == 1) {
                task.setStatus(0);
                loadTasks.add(task);
            } else {
                waitTasks.add(task);
            }
        }
        checkTasks();
        ThreadPoolProxy.getInstance().executeTask(() -> {
            while (true) {
                for (DownloadTask task : waitTasks) {
                    BroadcastUtils.sendDownloadToastBroadcast(task);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //添加下载任务到任务队列
    public void addTask(RemoteFile file) {
        waitTasks.add(new DownloadTask(file));
        checkTasks();
    }

    //校验正在下载队列是否是满任务执行;
    private void checkTasks() {
        Log.d(TAG, "checkTasks: wait:" + waitTasks.size() + "     loading:" + loadTasks.size());
        if (loadTasks.size() < 3) {
            if (waitTasks.size() > 0) {
                loadTasks.add(waitTasks.getFirst());
                waitTasks.removeFirst();
                checkLoadTask();
            } else {
                return;
            }
            checkTasks();
        }
    }

    private void checkLoadTask() {
        for (DownloadTask take : loadTasks) {
            if (take.getStatus() == 0) {
                threadPool.executeTask(() -> openDownTask(take));
            }
        }
    }

    private void openDownTask(DownloadTask task) {

        //改变任务状态
        task.setStatus(1);
        //将要下载的文件
        RemoteFile remoteFile = task.file;


        //总片数
        int partCount = (int) ((remoteFile.getFileSize() / PART_SIZE) + (remoteFile.getFileSize() % PART_SIZE > 0 ? 1 : 0));
        //下载完成片数
        AtomicInteger finishNum = new AtomicInteger(0);
        //当前已下载文件大小
        AtomicInteger loadFileSize = new AtomicInteger(0);

        for (int i = 0; i < partCount; i++) {
            final int finalI = i;
            long startIndex = PART_SIZE * finalI;
            long endIndex;
            long curPartSize;
            if (partCount == finalI + 1) {
                endIndex = remoteFile.getFileSize();
                curPartSize = remoteFile.getFileSize() % PART_SIZE ;
            } else {
                endIndex = PART_SIZE * finalI + PART_SIZE - 1;
                curPartSize = PART_SIZE;
            }
            task.setPartStatus(1, finalI);
            ApiRetrofit.getInstance().downloadFile(remoteFile.getID(), startIndex, endIndex)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ThreadPoolProxy.newProxy(3).executeTask(() -> {
                                InputStream is = null;
                                BufferedInputStream bis = null;
                                try {
                                    long loadSize = 0;
                                    String partName = remoteFile.getFileName() + ".part" + finalI;
                                    File part = new File(partPath + partName);
                                    FileOutputStream fos = new FileOutputStream(part);
                                    byte[] buff = new byte[20480];
                                    int len = 0;
                                    is = response.body().byteStream();
                                    bis = new BufferedInputStream(is);
                                    while ((len = bis.read(buff)) != -1) {
                                        fos.write(buff, 0, len);
                                        loadSize += len;
                                        final int length = len;
                                        handler.post(() -> {
                                            int asb = loadFileSize.addAndGet(length);
                                            task.setProgress(asb);
                                        });
                                    }

                                    //下载完成
                                    if (loadSize == curPartSize) {
                                        task.setPartStatus(4, finalI);
                                    } else {

                                    }

                                    int num = finishNum.incrementAndGet();
//                            loadTask(task, remoteFile, partCount, finishNum, loadCount, loadIndex, loadFileSize);
                                    //片段下载完成
                                    Log.e(TAG, "onResponse: Num=" + num + "  count:" + partCount);
                                    if (num == partCount) {
                                        // 合并文件并 更新数据库
                                        saveFile(task);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (is != null) {
                                            is.close();
                                        }
                                        if (bis != null) {
                                            bis.close();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            });
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("result", t.toString());
                        }
                    });

        }

    }


    //文件碎片整合成完整文件
    private void saveFile(DownloadTask task) {
        //查询所有part
        List<DownLoadRecordEntity> parts = task.getParts();

        //查询下载文件记录
        DownLoadRecordEntity recordEntity = task.getDownLoadRecordEntity();
        FileOutputStream fos = null;
        RandomAccessFile ra = null;
        FileInputStream is = null;
        int counts = parts.size();
        try {
            File file = new File(recordEntity.getPath());
            fos = new FileOutputStream(file);
            ra = new RandomAccessFile(file, "rwd");

            long startIndex = 0;
            for (int i = 0; i < parts.size(); i++) {
                File part = new File(parts.get(i).getPath());
                is = new FileInputStream(part);
                ra.seek(startIndex);
                startIndex += part.length();
                byte[] buffer = new byte[204800];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    ra.write(buffer, 0, len);
                }

                if (part.exists()) {
                    part.delete();
                }
                counts--;
                if (i == parts.size() - 1) {
                    Log.d("文件片段读取完成:", file.getAbsolutePath());
                }
                if (counts == 0) {
                    Log.d("文件保存完成:", file.getAbsolutePath());
                    task.setStatus(4);
                    loadTasks.remove(task);
                    checkTasks();
                    BroadcastUtils.sendDownloadToastBroadcast(task);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ra != null) {
                    ra.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }


}
