package com.ehualu.calabashandroid.interfaces;

import com.ehualu.calabashandroid.model.RemoteFile;

import java.util.List;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-12 17:46:13
 * <p>
 * describe：底部更多操作弹窗Interface
 */
public interface BottomMoreInterface {
    /**
     * 移动
     */
    void onMove(List<RemoteFile> list);

    /**
     * 重命名
     */
    void onRename(RemoteFile remoteFile);

    /**
     * 查看详情
     */
    void onInfo(RemoteFile remoteFile);
}
