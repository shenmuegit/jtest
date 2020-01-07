package com.ehualu.calabashandroid.utils;

import android.util.Log;

/**
 * author: houxiansheng
 * <p>
 * time：2016-11-8 下午2:59:42
 * <p>
 * describe：日志类
 */
public class MyLog {

    public static String TAG = "ehualuLog";

    /**
     * 日志总开关
     */
    private static boolean sLogOn = true;

    public static final String COLON = ": ";

    public static final boolean DEBUGV = true;

    public static final boolean DEBUGD = true;

    public static final boolean DEBUGI = true;

    public static final boolean DEBUGW = true;

    /**
     * 设置日志总开关
     *
     * @param bOn 日志开关
     */
    public static void setLogOn(boolean bOn) {
        sLogOn = bOn;
    }

    public static void v(String tag, String msg) {
        if (DEBUGV) {
            Log.v(TAG, tag + COLON + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUGI) {
            Log.i(TAG, tag + COLON + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sLogOn && DEBUGD) {
            Log.d(TAG, tag + COLON + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUGW) {
            Log.w(TAG, tag + COLON + msg);
        }
    }

    public static void e(String tag, String msg) {
        Log.e(TAG, tag + COLON + msg);
    }

    public static void e(String tag, String msg, Exception e) {
        Log.e(TAG, tag + COLON + msg, e);
    }

    public static void v(String msg) {
        if (DEBUGV) {
            Log.v(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (DEBUGI) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (sLogOn && DEBUGD) {
            Log.d(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (DEBUGW) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

}
