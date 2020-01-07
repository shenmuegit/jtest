package com.ehualu.calabashandroid.interfaces;

import com.ehualu.calabashandroid.model.RemoteFile;

import java.util.List;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-12 21:27:15
 * <p>
 * describe：弹框的确定取消按钮的Interface
 */
public interface ConfirmCancelInterface {
    /**
     * 取消
     */
    void onCancel(List<RemoteFile> list);

    /**
     * 确定
     */
    void onConfirm(List<RemoteFile> list);

}
