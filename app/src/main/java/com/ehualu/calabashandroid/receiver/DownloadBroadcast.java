package com.ehualu.calabashandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ehualu.calabashandroid.model.DownloadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GaoTing on 2019/12/27.
 * <p>
 * Explain:下载服务广播监听
 */
public class DownloadBroadcast extends BroadcastReceiver {

    List<DownloadTaskListener> listeners = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadTask task = (DownloadTask) intent.getSerializableExtra("task");
        if (task != null) {
            for (DownloadTaskListener listener : listeners) {
                listener.loading(task);
            }
        }
    }

    public void addListener(DownloadTaskListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DownloadTaskListener listener) {
        listeners.remove(listener);

    }

    public interface DownloadTaskListener {
        void loading(DownloadTask task);
    }
}
