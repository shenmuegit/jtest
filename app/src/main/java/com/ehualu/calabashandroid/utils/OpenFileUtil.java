package com.ehualu.calabashandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.ehualu.calabashandroid.app.MyApp;

import java.io.File;
import java.util.Locale;

/**
 * author: houxiansheng
 * <p>
 * time：
 * <p>
 * describe：
 */
public class OpenFileUtil {
    public static void openFile(Context mContext, String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return;
        /* 取得扩展名 */
        String end =
                file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            getAudioFileIntent(mContext, filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            getVideoFileIntent(mContext, filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals(
                "bmp")) {
            getImageFileIntent(mContext, filePath);
        } else if (end.equals("apk")) {
            getApkFileIntent(mContext, filePath);
        } else if (end.equals("ppt")) {
            getPptFileIntent(mContext, filePath);
        } else if (end.equals("xls")) {
            getExcelFileIntent(mContext, filePath);
        } else if (end.equals("doc")) {
            getWordFileIntent(mContext, filePath);
        } else if (end.equals("pdf")) {
            getPdfFileIntent(mContext, filePath);
//        } else if (end.equals("chm")) {
//            getChmFileIntent(mContext, filePath);
        } else if (end.equals("txt")) {
            getTextFileIntent(mContext, filePath);
        } else {
            getAllIntent(mContext, filePath);
        }
    }

    // Android获取一个用于打开APK文件的intent
    public static void getAllIntent(Context mContext, String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "*/*");
        mContext.startActivity(intent);
    }

    // Android获取一个用于打开APK文件的intent
    public static void getApkFileIntent(Context mContext, String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MyApp.getAppContext(), "com.ehualu.calabashandroid.FileProvider",
                    new File(filePath));
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    // Android获取一个用于打开VIDEO文件的intent
    public static void getVideoFileIntent(Context mContext, String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MyApp.getAppContext(), "com.ehualu.calabashandroid.FileProvider",
                    new File(filePath));
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "video/*");
        mContext.startActivity(intent);
    }

    // Android获取一个用于打开AUDIO文件的intent
    public static void getAudioFileIntent(Context mContext, String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MyApp.getAppContext(), "com.ehualu.calabashandroid.FileProvider",
                    new File(filePath));
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "audio/*");
        mContext.startActivity(intent);
    }

    // Android获取一个用于打开Html文件的intent
    public static void getHtmlFileIntent(Context mContext, String filePath) {
        Uri uri =
                Uri.parse(filePath).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(filePath).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        mContext.startActivity(intent);
    }

    // Android获取一个用于打开图片文件的intent
    public static void getImageFileIntent(Context mContext, String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MyApp.getAppContext(), "com.ehualu.calabashandroid.FileProvider",
                    new File(filePath));
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        mContext.startActivity(intent);
    }

    // Android获取一个用于打开PPT文件的intent
    public static void getPptFileIntent(Context mContext, String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MyApp.getAppContext(), "com.ehualu.calabashandroid.FileProvider",
                    new File(filePath));
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        mContext.startActivity(intent);
    }

    // Android获取一个用于打开Excel文件的intent
    public static void getExcelFileIntent(Context mContext, String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MyApp.getAppContext(), "com.ehualu.calabashandroid.FileProvider",
                    new File(filePath));
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        mContext.startActivity(intent);
    }

    // Android获取一个用于打开Word文件的intent
    public static void getWordFileIntent(Context mContext, String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MyApp.getAppContext(), "com.ehualu.calabashandroid.FileProvider",
                    new File(filePath));
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "application/msword");
        mContext.startActivity(intent);
    }

    // Android获取一个用于打开CHM文件的intent
    public static void getChmFileIntent(Context mContext, String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MyApp.getAppContext(), "com.ehualu.calabashandroid.FileProvider",
                    new File(filePath));
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "application/x-chm");
        mContext.startActivity(intent);
    }

    // Android获取一个用于打开文本文件的intent
    public static void getTextFileIntent(Context mContext, String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MyApp.getAppContext(), "com.ehualu.calabashandroid.FileProvider",
                    new File(filePath));
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "text/plain");
        mContext.startActivity(intent);
    }

    // Android获取一个用于打开PDF文件的intent
    public static void getPdfFileIntent(Context mContext, String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MyApp.getAppContext(), "com.ehualu.calabashandroid.FileProvider",
                    new File(filePath));
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "application/pdf");
        mContext.startActivity(intent);
    }
}
