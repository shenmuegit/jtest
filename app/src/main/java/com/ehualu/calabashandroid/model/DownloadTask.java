package com.ehualu.calabashandroid.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.db.entity.DownLoadRecordEntity;
import com.ehualu.calabashandroid.db.manager.DBManager;
import com.ehualu.calabashandroid.utils.BroadcastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static com.ehualu.calabashandroid.utils.Constants.PART_SIZE;
import static com.ehualu.calabashandroid.utils.Constants.DOWNLOAD_PATH;

/**
 * Created by GaoTing on 2019/12/27.
 * <p>
 * Explain:下载任务
 */
public class DownloadTask implements Serializable {
    public final String id;
    public final RemoteFile file;
    public final Date createTime;
    private int status;//任务状态   0:待下载,1:下载中,2,暂停,3:失败,4,完成,5：删除;
    private int progress;//下载进度
    private String speed;//下载速度

    volatile int loadSize;
    volatile int oldSize;

    private DownLoadRecordEntity downLoadRecordEntity;
    private List<DownLoadRecordEntity> parts = new ArrayList<>();

    private LinkedList<Integer> loadSizes = new LinkedList<>();

    public DownloadTask(RemoteFile file) {
        this.id = createId();
        this.file = file;
        this.createTime = new Date();
        if (null != file.getID())
            syncSQLite();
    }


    public DownloadTask(String id, RemoteFile file, Date createTime) {
        this.id = id;
        this.file = file;
        this.createTime = createTime;
    }

    private void syncSQLite() {
        //文件记录
        downLoadRecordEntity = new DownLoadRecordEntity();
        downLoadRecordEntity.setUserId(MyApp.userId);
        downLoadRecordEntity.setTaskId(id);
        downLoadRecordEntity.setFileType("1");
        downLoadRecordEntity.setStatus(status);
        downLoadRecordEntity.setPath(DOWNLOAD_PATH + file.getFileName());
        downLoadRecordEntity.setFileSize(file.getFileSize());
        downLoadRecordEntity.setFileId(file.getID());
        downLoadRecordEntity.setFileName(file.getFileName());
        downLoadRecordEntity.setCreateTime(createTime);
        downLoadRecordEntity.setUpdateTime(createTime);
        DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().insert(downLoadRecordEntity);

        //片段记录
        //总片数
        int partCount = (int) ((file.getFileSize() / PART_SIZE) + (file.getFileSize() % PART_SIZE > 0 ? 1 : 0));
        for (int i = 0; i < partCount; i++) {
            DownLoadRecordEntity downLoadRecordEntity = new DownLoadRecordEntity();
            downLoadRecordEntity.setUserId(MyApp.userId);
            downLoadRecordEntity.setTaskId(id);
            downLoadRecordEntity.setFileType("2");
            downLoadRecordEntity.setPartNum(i);
            downLoadRecordEntity.setFileId(file.getID());
            downLoadRecordEntity.setFileName(file.getFileName());
            downLoadRecordEntity.setFileSize(i == partCount - 1 ? file.getFileSize() % PART_SIZE - 1 : PART_SIZE);
            downLoadRecordEntity.setPath(DOWNLOAD_PATH + ".part/" + file.getFileName() + ".part" + i);
            downLoadRecordEntity.setStatus(status);
            downLoadRecordEntity.setCreateTime(createTime);
            downLoadRecordEntity.setUpdateTime(createTime);
            parts.add(downLoadRecordEntity);
        }
        DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().insertInTx(parts);
    }

    private String createId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public int getProgress() {
        return progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        if (status != 0) {
            downLoadRecordEntity.setStatus(status);
            downLoadRecordEntity.setUpdateTime(new Date());
            DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().update(downLoadRecordEntity);
        }
        if (status == 1) {
            //统计下载速度
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        loadSizes.add(loadSize);
                        double speedb = (loadSizes.getLast() - oldSize) / loadSizes.size();
                        double speedk = Math.floor((speedb) / 102.4) / 10;
                        double speedm = Math.floor(speedk / 102.4) / 10;
                        oldSize = loadSizes.getFirst();
                        if (loadSizes.size() >= 5) {
                            loadSizes.removeFirst();
                        }
                        if (speedb < 1024) {
                            setSpeed(speedb + " B/s");
                        } else if (speedk < 1024) {
                            setSpeed(speedk + " K/s");
                        } else {
                            setSpeed(speedm + " M/s");
                        }
                        BroadcastUtils.sendDownloadToastBroadcast(DownloadTask.this);
                    });
                    if (getStatus() != 1) {
                        this.cancel();
                    }
                }
            }, 1000, 1000);
        }
        if (status == 4) {
            progress = 100;
        }
    }

    public void setPartStatus(int status, int partNum) {
        DownLoadRecordEntity downLoadRecordEntity = parts.get(partNum);
        downLoadRecordEntity.setStatus(status);
        downLoadRecordEntity.setUpdateTime(new Date());
        DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().update(downLoadRecordEntity);
    }

    // size  当前已下载长度
    public void setProgress(long size) {
        setLoadSize((int) size);
        this.progress = (int) Math.floor((size - 1) * 100.0 / file.getFileSize());
    }

    public DownLoadRecordEntity getDownLoadRecordEntity() {
        return downLoadRecordEntity;
    }

    public List<DownLoadRecordEntity> getParts() {
        return parts;
    }

    public void setDownLoadRecordEntity(DownLoadRecordEntity entity) {
        this.downLoadRecordEntity = entity;
    }

    public void setParts(List<DownLoadRecordEntity> parts) {
        this.parts = parts;
    }


    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "DownloadTask{" +
                "id='" + id + '\'' +
                ", file=" + file +
                ", createTime=" + createTime +
                ", status=" + status +
                ", progress=" + progress +
                ", speed='" + speed + '\'' +
                '}';
    }

    private void setLoadSize(int loadSize) {
        synchronized (DownloadTask.class) {
            this.loadSize = loadSize;
        }
    }
}
