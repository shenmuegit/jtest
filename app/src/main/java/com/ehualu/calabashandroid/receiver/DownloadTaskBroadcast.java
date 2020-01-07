package com.ehualu.calabashandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.service.FileDownloader;

/**
 * Created by GaoTing on 2019/12/27.
 * <p>
 * Explain:下载任务广播监听
 */
public class DownloadTaskBroadcast extends BroadcastReceiver {
    public static final String TAG = "DownloadTaskBroadcast";
    private FileDownloader downloader;

    public DownloadTaskBroadcast(FileDownloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteFile file = (RemoteFile) intent.getSerializableExtra("file");
        if (downloader != null && file != null) {
            downloader.addTask(file);
        }
    }
}
