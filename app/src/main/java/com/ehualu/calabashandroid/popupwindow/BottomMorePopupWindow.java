package com.ehualu.calabashandroid.popupwindow;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.interfaces.BottomMoreInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.MyPopupWindowUtils;

import java.util.List;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-12 20:09:11
 * <p>
 * describe：底部更多弹框
 */
public class BottomMorePopupWindow extends PopupWindow implements View.OnClickListener {
    private PopupWindow popupWindow;
    private Activity mContext;
    private BottomMoreInterface bottomMoreInterface;
    private List<RemoteFile> list;
    private boolean isFromBackup;

    public BottomMorePopupWindow(Activity mContext, BottomMoreInterface bottomMoreInterface, List<RemoteFile> list,
                                 boolean isFromBackup) {
        this.mContext = mContext;
        this.bottomMoreInterface = bottomMoreInterface;
        this.list = list;
        this.isFromBackup = isFromBackup;
    }

    /**
     * 显示更多的底部弹窗
     */
    public void showMoreOperationPopup(View view) {
        popupWindow = new PopupWindow(mContext);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popup_botom_more, null);
        initView(contentView);
        popupWindow.setContentView(contentView);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frg_file_popup_bg));

        //设置背景透明
        MyPopupWindowUtils.addBackground(mContext, popupWindow);
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
    }

    private void initView(View contentView) {
        ImageView ivMove = contentView.findViewById(R.id.iv_move);
        TextView tvMove = contentView.findViewById(R.id.tv_move);
        LinearLayout llMove = contentView.findViewById(R.id.ll_move);

        ImageView ivRename = contentView.findViewById(R.id.iv_rename);
        TextView tvRename = contentView.findViewById(R.id.tv_rename);
        LinearLayout llRename = contentView.findViewById(R.id.ll_rename);

        ImageView ivInfo = contentView.findViewById(R.id.iv_info);
        TextView tvInfo = contentView.findViewById(R.id.tv_info);
        LinearLayout llInfo = contentView.findViewById(R.id.ll_info);

        if (list.size() > 1) {
            llRename.setOnClickListener(null);
            ivRename.setBackground(mContext.getResources().getDrawable(R.drawable.icon_bottom_rename_not));
            tvRename.setTextColor(Color.parseColor("#ffc1c1c0"));

            llInfo.setOnClickListener(null);
            ivInfo.setBackground(mContext.getResources().getDrawable(R.drawable.icon_bottom_info_not));
            tvInfo.setTextColor(Color.parseColor("#ffc1c1c0"));
        } else {
            llRename.setOnClickListener(this);
            ivRename.setBackground(mContext.getResources().getDrawable(R.drawable.icon_bottom_rename));
            tvRename.setTextColor(Color.parseColor("#ff000000"));

            llInfo.setOnClickListener(this);
            ivInfo.setBackground(mContext.getResources().getDrawable(R.drawable.icon_bottom_info));
            tvInfo.setTextColor(Color.parseColor("#ff000000"));
        }

        if (isFromBackup) {
            llMove.setVisibility(View.GONE);
        } else {
            llMove.setVisibility(View.VISIBLE);
            llMove.setOnClickListener(this);
            ivMove.setBackground(mContext.getResources().getDrawable(R.drawable.icon_bottom_move));
        }
    }

    @Override
    public void onClick(View view) {
        if (bottomMoreInterface == null) {
            return;
        }

        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }

        switch (view.getId()) {
            case R.id.ll_move:
                bottomMoreInterface.onMove(list);
                break;

            case R.id.ll_rename:
                bottomMoreInterface.onRename(list.get(0));
                break;
            case R.id.ll_info:
                bottomMoreInterface.onInfo(list.get(0));
                break;
        }
    }
}
