package com.ehualu.calabashandroid.utils;

import android.os.Environment;

import java.io.File;

public class Constants {

    /**
     * 下载路径，用来保存临时的下载文件分片，以及分片合成后的文件
     */
    public static final String DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hulu" + File.separator +
                    "Downloads" + File.separator;

    /**
     * 上传路径，用来保存临时的上传文件分片
     */
    public static final String UPLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hulu" + File.separator +
                    "Uploads" + File.separator;

    /**
     * 下载片段大小
     */
    public static final long PART_SIZE = 10 * 1024 * 1024;


    public static final String UPLOAD_TABLE_INSERT = "upload_table_insert";//用来更新上传界面

    /**
     * 拍照上传图片保存路径
     */
    public static final String CAMERA_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hulu" + File.separator +
                    "Cameras";


    /**
     * 以下为界面更新的广播
     */
    public static final String DELETE_COMPLETE = "delete_complete";

}
