package com.ehualu.calabashandroid.app;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;

import com.ehualu.calabashandroid.activity.AlbumActivity;
import com.ehualu.calabashandroid.activity.AlbumListActivity;
import com.ehualu.calabashandroid.activity.PhotoPreviewUploadActivity;
import com.ehualu.calabashandroid.activity.SelectVideoActivity;
import com.ehualu.calabashandroid.activity.TakePhotoActivity;
import com.ehualu.calabashandroid.activity.UploadFileTypeActivity;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.receiver.DownloadBroadcast;
import com.ehualu.calabashandroid.utils.BroadcastUtils;
import com.ehualu.calabashandroid.utils.Constants;
import com.ehualu.calabashandroid.utils.LocalImageHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyApp extends Application {

    public static MyApp instance;
    private static Context mContext;
    private RxPermissions rxPermissions;
    private static DownloadBroadcast broadcast;

    public static List<BaseActivity> needDestoryActs = new ArrayList<>();

    public static void closeAllNeedDestoryActs() {
        for (BaseActivity activity : needDestoryActs) {
            if (activity != null && !activity.isFinishing() && activity instanceof UploadFileTypeActivity) {
                activity.finish();
            }

            if (activity != null && !activity.isFinishing() && activity instanceof AlbumListActivity) {
                activity.finish();
            }

            if (activity != null && !activity.isFinishing() && activity instanceof AlbumActivity) {
                activity.finish();
            }

            if (activity != null && !activity.isFinishing() && activity instanceof SelectVideoActivity) {
                activity.finish();
            }

            if (activity != null && !activity.isFinishing() && activity instanceof TakePhotoActivity) {
                activity.finish();
            }

            if (activity != null && !activity.isFinishing() && activity instanceof PhotoPreviewUploadActivity) {
                activity.finish();
            }
        }

        needDestoryActs.clear();
    }

    public static Integer aaa = 0;

    //登录id;(临时写定值,后续修改)
    public static String userId = "12345";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initPath();
        registerBroadcast();
    }

    //注册全局广播监听
    private void registerBroadcast() {
        broadcast = new DownloadBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastUtils.DOWNLOAD_TOAST);
        registerReceiver(broadcast, intentFilter);
    }

    //获取下载通知广播
    public static DownloadBroadcast getBroadcast() {
        return broadcast;
    }

    /**
     * 实例化下载和上传目录
     */
    private void initPath() {
        File downloadFolder = new File(Constants.DOWNLOAD_PATH);
        if (!downloadFolder.exists()) {
            downloadFolder.mkdirs();
        }

        File uploadFolder = new File(Constants.UPLOAD_PATH);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
    }

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initGlobalContext(this);
    }

    private static void initGlobalContext(Context context) {
        mContext = context;
    }

    public static Context getAppContext() {
        return MyApp.mContext;
    }
}
