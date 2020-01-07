package com.ehualu.calabashandroid.upload;

/**
 * 文件上传进度接口
 */
public interface OnDatatransferProgressListener {

    void notifyUpdateProgress(String taskId, long current, long total, long speed);

}
