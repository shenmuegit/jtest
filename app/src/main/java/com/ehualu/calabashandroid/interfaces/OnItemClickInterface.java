package com.ehualu.calabashandroid.interfaces;

import com.ehualu.calabashandroid.model.RemoteFile;

import java.util.List;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-12 13:49:38
 * <p>
 * describe：文件条目的点击事件Interface
 */
public interface OnItemClickInterface {
    /**
     * 单个点击
     */
    void onItemClick(RemoteFile remoteFile);

    /**
     * 长按弹出多选框
     */
    void onItemLongCick(RemoteFile remoteFile);

    /**
     * 全选
     */
    void selectedAll();

    /**
     * 反选
     */
    void notSelectedAll();

    /**
     * 选中个数
     */
    void selectCount(List<RemoteFile> selectedList, int count);
}
