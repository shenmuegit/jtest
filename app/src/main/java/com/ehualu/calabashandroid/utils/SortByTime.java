package com.ehualu.calabashandroid.utils;

import com.ehualu.calabashandroid.model.RemoteFile;

import java.util.Comparator;
import java.util.Date;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-29 18:13:57
 * <p>
 * describe：按时间排序
 */
public class SortByTime implements Comparator<RemoteFile> {

    public SortByTime() {

    }

    @Override
    public int compare(RemoteFile remoteFile1, RemoteFile remoteFile2) {

        Date date1 = TimeUtils.longToDate(remoteFile1.getUpdateTime(), "yyyy-MM-dd HH:mm:ss");
        Date date2 = TimeUtils.longToDate(remoteFile2.getUpdateTime(), "yyyy-MM-dd HH:mm:ss");

        if ("2".equals(remoteFile1.getCategory())) {//remoteFile1是文件夹
            if ("2".equals(remoteFile2.getCategory())) { //都是文件夹
                if (date1.before(date2)) { // 对日期字段进行升序，如果欲降序可采用after方法
                    return 1;
                }
                return -1;
            } else {
                return -1;
            }
        } else {//remoteFile1是文件
            if ("2".equals(remoteFile2.getCategory())) {
                return 1;
            } else {
                //都是文件
                if (date1.before(date2)) {// 对日期字段进行升序，如果欲降序可采用after方法
                    return 1;
                }
                return -1;
            }
        }

    }

}
