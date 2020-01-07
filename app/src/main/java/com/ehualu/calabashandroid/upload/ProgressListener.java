package com.ehualu.calabashandroid.upload;

import android.util.Log;

import com.ehualu.calabashandroid.model.TransferModel;

public class ProgressListener implements OnDatatransferProgressListener {

    private TransferModel model;
    private ProgressCallback callback;
    private long lastTime;
    private String key;

    public void setKkey(String key) {
        this.key = key;
    }

    public String getKkey() {
        return key;
    }

    public TransferModel getModel() {
        return model;
    }

    public ProgressListener(TransferModel model, ProgressCallback callback) {
        this.model = model;
        this.callback = callback;
    }

    @Override
    public void notifyUpdateProgress(String taskId, long current, long total, long speed) {
        if (current == total) {
            callback.complete(taskId, 100, speed);
        } else {
            if (lastTime == 0) {
                lastTime = System.currentTimeMillis();
            } else {
                long now = System.currentTimeMillis();
                long dur = now - lastTime;
                if (dur > 500) {
                    int percent = (int) (100.0 * ((double) current) / ((double) total));
                    callback.progress(percent, speed);
                    lastTime = now;
                }
            }
        }

    }

    public interface ProgressCallback {
        void progress(int percent, long speed);

        void complete(String taskId, int percent, long speed);

        void updateSpeed(long speed);
    }
}
