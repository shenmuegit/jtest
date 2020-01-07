package com.ehualu.calabashandroid.utils;

import android.content.Context;

import com.ehualu.calabashandroid.model.RemoteFile;

import java.text.Collator;
import java.util.Comparator;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-17 15:50:34
 * <p>
 * describe：按名称排序
 */
public class SortChineseName implements Comparator<RemoteFile> {
    private Context mContext;
    boolean isAllItemCanSelected;//是否所有条目都可以选中

    public SortChineseName(Context mContext, boolean isAllItemCanSelected) {
        this.mContext = mContext;
        this.isAllItemCanSelected = isAllItemCanSelected;
    }

    @Override
    public int compare(RemoteFile f1, RemoteFile f2) {
        String name1 = f1.getFileName();
        String name2 = f2.getFileName();
        Collator collator = Collator.getInstance(java.util.Locale.CHINA);

        if (!isAllItemCanSelected) {
            if (name1.equals("葫芦备份")) {
                return -1;
            }

            if (name2.equals("葫芦备份")) {
                return 1;
            }

            if (name1.equals("备份恢复")) {
                return -1;
            }

            if (name2.equals("备份恢复")) {
                return 1;
            }

            if (name1.equals("收到文件")) {
                return -1;
            }

            if (name2.equals("收到文件")) {
                return 1;
            }

            if (name1.equals("我的收藏")) {
                return -1;
            }

            if (name2.equals("我的收藏")) {
                return 1;
            }

            if (name1.equals("我的相册")) {
                return -1;
            }

            if (name2.equals("我的相册")) {
                return 1;
            }

            if (!f1.isCanSelected()) {
                return -1;
            }
            if (!f2.isCanSelected()) {
                return 1;
            }
        }

        if ("2".equals(f1.getCategory())) {
            //o1是文件夹
            if ("2".equals(f2.getCategory())) {
                //都是文件夹
                return collator.compare(f1.getFileName(), f2.getFileName());
            } else {
                return -1;
            }
        } else {
            //o1是文件
            if ("2".equals(f2.getCategory())) {
                return 1;
            } else {
                //都是文件
                return collator.compare(f1.getFileName(), f2.getFileName());
            }
        }
    }
}
