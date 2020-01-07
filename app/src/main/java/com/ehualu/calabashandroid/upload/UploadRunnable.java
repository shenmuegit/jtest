package com.ehualu.calabashandroid.upload;

import android.util.Log;

import com.ehualu.calabashandroid.activity.MainActivity;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.db.entity.UploadPieceEntity;
import com.ehualu.calabashandroid.db.manager.EntityManager;
import com.ehualu.calabashandroid.responseBean.UploadPieceResponse;

import java.util.HashSet;
import java.util.Set;
import java.util.UnknownFormatConversionException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UploadRunnable implements Runnable, OnDatatransferProgressListener {

    private final Set<OnDatatransferProgressListener> mDataTransferListeners = new HashSet<>();
    private UploadChunk chunk;
    private long originFileSize;

    private long lastCount = 0;
    private long lastTime = 0;

    public UploadRunnable(UploadChunk chunk, long originFileSize) {
        this.chunk = chunk;
        this.originFileSize = originFileSize;
    }

    @Override
    public void run() {
        //插入上传分片表
        UploadPieceEntity pieceEntity = new UploadPieceEntity();
        pieceEntity.setChunkId(chunk.getChunkId());
        pieceEntity.setChunkNum(chunk.getChunkNum());
        pieceEntity.setChunkSize(chunk.getChunkSize());
        pieceEntity.setChunkTotal(chunk.getChunkTotal());
        pieceEntity.setCurrent(0L);
        pieceEntity.setDirId(chunk.getTargetDirId());
        pieceEntity.setFileName(chunk.getFileName());
        pieceEntity.setStatus(1);
        pieceEntity.setTaskId(chunk.getTaskId());
        EntityManager.getInstance().getUploadPieceEntityDao().insert(pieceEntity);
        ApiRetrofit.getInstance().uploadFile(chunk.convert(), chunk.getPartFile(), originFileSize, this)
                .subscribe(new Observer<UploadPieceResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UploadPieceResponse o) {
                        if (o.isSuccess()) {
                            //更新分片表
                            EntityManager.getInstance().updatePieceStatus(chunk.getChunkId(), true);

                            //查询当前已经传输完成的分片是否等于该任务总的分片数，如果相等，调用服务器的通知接口
                            int finishedSize = EntityManager.getInstance().queryAllFinishPiece(chunk.getTaskId());
                            if (finishedSize == chunk.getChunkTotal()) {
                                //调用服务器的通知接口
                                Log.e("苏昊一号", "苏昊一号");
                                notifyUpdateProgress(chunk.getTaskId(), -10000, originFileSize, 0);
                            }
                        } else {
                            //分片上传失败，更新数据库
                            EntityManager.getInstance().updatePieceStatus(chunk.getChunkId(), true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //分片上传失败，更新数据库
                        EntityManager.getInstance().updatePieceStatus(chunk.getChunkId(), true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void addDataTransferProgressListener(OnDatatransferProgressListener listener) {
        mDataTransferListeners.add(listener);
    }

    @Override
    public void notifyUpdateProgress(String taskId, long current, long total, long spe) {
        if (current == -10000) {
            for (OnDatatransferProgressListener listener : mDataTransferListeners) {
                listener.notifyUpdateProgress(taskId, current, total, 0);
            }
        } else {
            long cur = 0;
            for (ProgressRequestBody body : MainActivity.requestBodies) {
                if (body.getTaskId().equals(taskId)) {
                    cur += body.getCurrent();
                }
            }

            /**
             * 计算上传的速度
             */
            long speed = 0;
            long nowTotal = cur;
            long nowTime = System.currentTimeMillis();
            if (lastTime == 0 || lastCount == 0) {
                lastTime = nowTime;
                lastCount = nowTotal;
            } else {
                speed = (nowTotal - lastCount) / (nowTime - lastTime);//每秒的KB数
            }

            //更新上传表，为了下次进来的时候，显示已经上传的进度
            EntityManager.getInstance().updateUploadPieceProgress(taskId, cur);
            for (OnDatatransferProgressListener listener : mDataTransferListeners) {
                listener.notifyUpdateProgress(taskId, cur, total, speed);
            }
        }
    }

}
