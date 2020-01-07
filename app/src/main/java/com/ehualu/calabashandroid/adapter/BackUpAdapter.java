package com.ehualu.calabashandroid.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseAdapter;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.FileIconUtils;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.ehualu.calabashandroid.utils.TimeUtils;

import java.util.List;

public class BackUpAdapter extends BaseAdapter<RemoteFile, BaseViewHolder> {

    private int itemLayout;
    private ImageView cbSelectMode;//选择框
    private ImageView ivNormalMode;

    public BackUpAdapter(@Nullable List<RemoteFile> data, int itemLayout) {
        super(itemLayout, data);
        this.itemLayout = itemLayout;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFile item) {
        int width;
        ImageView itemIcon = null;
        switch (itemLayout) {
            case R.layout.item_file_grid:
                helper.setText(R.id.itemName, item.getFileName());
                itemIcon = helper.getView(R.id.itemIcon);

                width = (ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(2 * 16 + 2 * 53, mContext)) / 3;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemIcon.getLayoutParams();
                params.width = width;
                params.height = (width * 59 / 74);
                itemIcon.setLayoutParams(params);

                cbSelectMode = helper.getView(R.id.cbSelectMode);
                cbSelectMode.setVisibility((isSelectMode()) ? View.VISIBLE : View.GONE);
                FileIconUtils.getThumnailURL(mContext, item, itemIcon); //获取缩略图
                break;
            case R.layout.item_file_list:
                helper.setText(R.id.itemName, item.getFileName());
                helper.setText(R.id.tv_time, TimeUtils.longToString(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
                itemIcon = helper.getView(R.id.itemIcon);

                width = (int) ((ScreenUtils.getScreenWidth(mContext) / 1080f) * DensityUtil.dip2px(46, mContext));
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) itemIcon.getLayoutParams();
                params2.width = width;
                params2.height = (width * 59 / 74);
                itemIcon.setLayoutParams(params2);

                cbSelectMode = helper.getView(R.id.cbSelectMode);
                cbSelectMode.setVisibility((isSelectMode()) ? View.VISIBLE : View.GONE);
                ivNormalMode = helper.getView(R.id.ivNormalMode);
                ivNormalMode.setVisibility(!(isSelectMode()) ? View.VISIBLE : View.GONE);
                if (isSelectMode()) {
                    ivNormalMode.setVisibility(View.GONE);
                }
                FileIconUtils.getThumnailURL(mContext, item, itemIcon); //获取缩略图
                break;
        }

        if (isChecked(item)) {
            cbSelectMode.setImageResource(R.drawable.icon_checked);
        } else {
            cbSelectMode.setImageResource(R.drawable.icon_no_checked);
        }

        //选择模式
        if (isSelectMode()) {
            helper.itemView.setOnLongClickListener(null);
            helper.itemView.setOnClickListener(null);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isChecked(item)) {
                        removeCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                    } else {
                        addCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                    }
                }
            });

            if (checkedList.size() != getData().size()) {
                onItemClickInterface.selectedAll();
            } else {
                onItemClickInterface.notSelectedAll();
            }

        } else {
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickInterface.onItemClick(item);
                }
            });
            /**
             * item的长按事件
             */
            helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickInterface != null) {
                        onItemClickInterface.onItemLongCick(item);
                        addCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                        onItemClickInterface.selectCount(checkedList, checkedList.size());
                    }
                    return true;
                }
            });
        }
    }
}
