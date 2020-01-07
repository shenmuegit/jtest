package com.ehualu.calabashandroid.model;

import android.graphics.drawable.Drawable;

/**
 * add by houxiansheng 2019-12-13 14:54:25 分享的Model
 */
public class ShareModel {
    private Drawable drawable;//图标
    private CharSequence title;//标题
    private String packageName;//包名
    private String activityName;//类名

    public ShareModel(Drawable drawable, CharSequence title, String packageName, String activityName) {
        this.drawable = drawable;
        this.title = title;
        this.packageName = packageName;
        this.activityName = activityName;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public CharSequence getTitle() {
        return title;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    @Override
    public String toString() {
        return "ShareModel{" +
                "drawable=" + drawable +
                ", title=" + title +
                ", packageName='" + packageName + '\'' +
                ", activityName='" + activityName + '\'' +
                '}';
    }
}