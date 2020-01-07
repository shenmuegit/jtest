package com.ehualu.calabashandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.ehualu.calabashandroid.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by linjinfa on 2018/2/5.
 * email 331710168@qq.com
 */
public class ToastUtil {

    private static Object iNotificationManagerObj;

    /**
     * @param context
     * @param message
     */
    public static void show(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT, Gravity.BOTTOM);
    }

    /**
     * @param context
     * @param message
     */
    public static void showCenter(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT, Gravity.CENTER);
    }

    /**
     * @param context
     * @param message
     */
    public static void show(Context context, String message, int duration, int gravity) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        //后setText 兼容小米默认会显示app名称的问题
        Toast toast = Toast.makeText(context, null, duration);
        toast.setText(message);
        toast.setGravity(gravity, 0, 0);
        if (isNotificationEnabled(context)) {
            toast.show();
        } else {
            showSystemToast(toast);
        }
    }

    /**
     * 业务相关的toast
     *
     * @param context
     * @param message
     */
    public static void showCenterForBusiness(final Context context, final String message) {
        if (context != null) {
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        show(context, message, Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                });
            } else {
                show(context, message, Toast.LENGTH_SHORT, Gravity.CENTER);
            }
        }
    }

    /**
     * 显示系统Toast
     */
    private static void showSystemToast(Toast toast) {
        try {
            Method getServiceMethod = Toast.class.getDeclaredMethod("getService");
            getServiceMethod.setAccessible(true);
            //hook INotificationManager
            if (iNotificationManagerObj == null) {
                iNotificationManagerObj = getServiceMethod.invoke(null);

                Class iNotificationManagerCls = Class.forName("android.app.INotificationManager");
                Object iNotificationManagerProxy = Proxy.newProxyInstance(toast.getClass().getClassLoader(), new Class[]{iNotificationManagerCls}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //强制使用系统Toast
                        if ("enqueueToast".equals(method.getName()) || "enqueueToastEx".equals(method.getName())) {  //华为p20 pro上为enqueueToastEx
                            args[0] = "android";
                        }
                        return method.invoke(iNotificationManagerObj, args);
                    }
                });
                Field sServiceFiled = Toast.class.getDeclaredField("sService");
                sServiceFiled.setAccessible(true);
                sServiceFiled.set(null, iNotificationManagerProxy);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息通知是否开启
     *
     * @return
     */
    private static boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        boolean areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled();
        return areNotificationsEnabled;
    }

    /**
     * add by houxiansheng 2019年12月12日09:59:13 居中显示带图片和文字的Toast
     *
     * @param context
     * @param message 提示语
     */
    public static void showCenterHasImageToast(Context context, String message) {
        Toast toast = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_tip, null);
        ImageView imageView = view.findViewById(R.id.iv_toast);
        TextView textView = view.findViewById(R.id.tv_toast);
        textView.setText(message);
        imageView.setVisibility(View.VISIBLE);
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * add by houxiansheng 2019-12-12 22:13:27 居中显示只有文字，没有图片的Toast
     *
     * @param context
     * @param message 提示语
     */
    public static void showCenterNoImageToast(Context context, String message) {
        Toast toast = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_tip, null);
        ImageView imageView = view.findViewById(R.id.iv_toast);
        TextView textView = view.findViewById(R.id.tv_toast);
        textView.setText(message);
        imageView.setVisibility(View.GONE);
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
