package com.ehualu.calabashandroid.popupwindow;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.interfaces.ConfirmCancelInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.MyPopupWindowUtils;
import com.ehualu.calabashandroid.utils.ScreenUtils;

import java.util.List;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-18 11:49:15
 * <p>
 * describe：备份提取弹窗
 */
public class BottomExtractPopupWindow extends PopupWindow implements View.OnClickListener {
    private PopupWindow popupWindow;
    private Activity mContext;
    private ConfirmCancelInterface bottomDeleteInterface;
    private List<RemoteFile> list;

    public BottomExtractPopupWindow(Activity mContext, ConfirmCancelInterface bottomDeleteInterface, List<RemoteFile> list) {
        this.mContext = mContext;
        this.bottomDeleteInterface = bottomDeleteInterface;
        this.list = list;
    }

    /**
     * 显示更多的底部弹窗
     */
    public void showMoreOperationPopup(View view) {
        popupWindow = new PopupWindow(mContext);
        popupWindow.setWidth(ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(5, mContext));
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popup_extract, null);
        popupWindow.setContentView(contentView);

        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_corners_10_fff));

        //设置背景透明
        MyPopupWindowUtils.addBackground(mContext, popupWindow);
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 8, 0);

        TextView tvDeleteCancel = contentView.findViewById(R.id.tv_delete_cancel);
        TextView tvDeleteConfirm = contentView.findViewById(R.id.tv_delete_confirm);
        tvDeleteCancel.setOnClickListener(this);
        tvDeleteConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (bottomDeleteInterface == null) {
            return;
        }

        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }

        switch (view.getId()) {
            case R.id.tv_delete_cancel:
                bottomDeleteInterface.onCancel(list);
                break;

            case R.id.tv_delete_confirm:
                bottomDeleteInterface.onConfirm(list);
                break;
        }
    }
}
