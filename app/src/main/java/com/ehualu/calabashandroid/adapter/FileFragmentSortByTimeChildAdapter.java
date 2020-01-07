package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.FileIconUtils;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.ehualu.calabashandroid.utils.TimeUtils;

import java.util.List;

public class FileFragmentSortByTimeChildAdapter extends BaseQuickAdapter<RemoteFile, BaseViewHolder> {

    private Context mContext;
    private FileFragmentSortByTimeAdapter parentAdapter;
    private int itemLayout;//按时间排序，共两种布局子布局，列表布局和网格布局

    public FileFragmentSortByTimeChildAdapter(Context context, int itemLayout, List<RemoteFile> data,
                                              FileFragmentSortByTimeAdapter parentAdapter) {
        super(itemLayout, data);
        this.mContext = context;
        this.itemLayout = itemLayout;
        this.parentAdapter = parentAdapter;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFile item) {
        ImageView itemIcon = helper.getView(R.id.itemIcon);
        int width;
        switch (itemLayout) {
            case R.layout.item_file_grid:
                helper.setText(R.id.itemName, item.getFileName());
                if (!item.isCanSelected()) {
                    helper.setGone(R.id.cbSelectMode, false);
                } else {
                    if (parentAdapter.isSelectMode()) {
                        helper.setGone(R.id.cbSelectMode, true);
                        if (parentAdapter.isChecked(item)) {
                            helper.setImageResource(R.id.cbSelectMode, R.drawable.icon_checked);
                        } else {
                            helper.setImageResource(R.id.cbSelectMode, R.drawable.icon_no_checked);
                        }
                    } else {
                        helper.setGone(R.id.cbSelectMode, false);
                    }
                }

                width = (ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(2 * 16 + 2 * 53, mContext)) / 3;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemIcon.getLayoutParams();
                params.width = width;
                params.height = (width * 59 / 74);
                itemIcon.setLayoutParams(params);
                FileIconUtils.getThumnailURL(mContext, item, itemIcon); //获取缩略图
                break;
            case R.layout.item_file_list:
                if (!item.isCanSelected()) {
                    helper.setGone(R.id.cbSelectMode, false);
                    if (parentAdapter.isSelectMode()) {
                        helper.setGone(R.id.ivNormalMode, false);
                    } else {
                        helper.setGone(R.id.ivNormalMode, true);
                    }
                    helper.setGone(R.id.tv_time, false);
                } else {
                    if (parentAdapter.isSelectMode()) {
                        helper.setGone(R.id.cbSelectMode, true)
                                .setGone(R.id.ivNormalMode, false);
                        if (parentAdapter.isChecked(item)) {
                            helper.setImageResource(R.id.cbSelectMode, R.drawable.icon_checked);
                        } else {
                            helper.setImageResource(R.id.cbSelectMode, R.drawable.icon_no_checked);
                        }
                    } else {
                        helper.setGone(R.id.cbSelectMode, false)
                                .setGone(R.id.ivNormalMode, true);
                    }
                }

                helper.setText(R.id.itemName, item.getFileName());
                helper.setText(R.id.tv_time, TimeUtils.longToString(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
                width = (int) ((ScreenUtils.getScreenWidth(mContext) / 1080f) * DensityUtil.dip2px(46, mContext));
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) itemIcon.getLayoutParams();
                params2.width = width;
                params2.height = (width * 59 / 74);
                itemIcon.setLayoutParams(params2);
                FileIconUtils.getThumnailURL(mContext, item, itemIcon); //获取缩略图
                break;
        }

        if (parentAdapter.isSelectMode()) {
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.isCanSelected()) {
                        return;
                    }
                    if (parentAdapter.isChecked(item)) {
                        parentAdapter.removeCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                    } else {
                        parentAdapter.addCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                    }
                }
            });

            if (parentAdapter.checkedList.size() != parentAdapter.getCanEditFileTotal()) {
                parentAdapter.onItemClickInterface.selectedAll();
            } else {
                parentAdapter.onItemClickInterface.notSelectedAll();
            }
        } else {
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentAdapter.onItemClickInterface.onItemClick(item);
                }
            });
        }
    }
}
