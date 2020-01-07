package com.ehualu.calabashandroid.upload;

import com.ehualu.calabashandroid.activity.MainActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Source;

/**
 * 监听上传进度
 */
public class ProgressRequestBody extends RequestBody {

    private String taskId;
    private long originFileLength;
    private UploadRunnable uploadRunnable;
    private File partFile;
    private MediaType mediaType;
    long currSize = 0;//记录目前一共读了多少数据

    public long getCurrent() {
        return currSize;
    }

    public String getTaskId() {
        return taskId;
    }

    public ProgressRequestBody(String taskId, long originFileLength, UploadRunnable uploadRunnable, File partFile, MediaType mediaType) {
        this.taskId = taskId;
        this.originFileLength = originFileLength;
        this.uploadRunnable = uploadRunnable;
        this.partFile = partFile;
    }

    @Override
    public MediaType contentType() {
        return mediaType;
    }

    @Override
    public long contentLength() throws IOException {
        return partFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source;
        long len;//记录本次读了多少数据
        try {
            source = Okio.source(partFile);
            Buffer buffer = new Buffer();
            while ((len = source.read(buffer, 2048)) != -1) {
                sink.write(buffer, len);
                sink.flush();
                currSize += len;
                refreshProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void refreshProgress() {
        uploadRunnable.notifyUpdateProgress(getTaskId(), -1, originFileLength,0);
    }
}
