package com.ehualu.calabashandroid.utils;

import android.content.Context;
import android.content.Intent;

import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.model.DownloadTask;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.service.FileDownloader;

/**
 * Created by GaoTing on 2019/12/30.
 * <p>
 * Explain:广播发送
 */
public class BroadcastUtils {
    public static final String DOWNLOAD_TASK = "com.ehualu.calabash:Task";
    public static final String DOWNLOAD_TOAST = "com.ehualu.calabash:Toast";

    //发送下载广播
    public static void sendDownloadBroadcast(RemoteFile remoteFile) {
        Context context = MyApp.getAppContext();
        Intent serverIntent=new Intent(context, FileDownloader.class);
        context.startService(serverIntent);
        Intent intent = new Intent(DOWNLOAD_TASK);
        intent.putExtra("file", remoteFile);
        context.sendBroadcast(intent);
    }

    //发送下载通知广播
    public static void sendDownloadToastBroadcast(DownloadTask task) {
        Context context = MyApp.getAppContext();
        Intent intent = new Intent(DOWNLOAD_TOAST);
        intent.putExtra("task", task);
        context.sendBroadcast(intent);
    }
}
