package com.ehualu.calabashandroid.adapter;

import android.text.TextUtils;
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

public class FileAdapter extends BaseAdapter<RemoteFile, BaseViewHolder> {

    private int itemLayout;
    private ImageView cbSelectMode;//选择框
    private ImageView ivNormalMode;
    private int canEditFileTotal;

    public FileAdapter(@Nullable List<RemoteFile> data, int itemLayout) {
        super(itemLayout, data);
        this.itemLayout = itemLayout;
        for (RemoteFile rf : data) {
            if (rf.isCanSelected()) {
                canEditFileTotal++;
            }
        }
    }

    public int getCanEditFileTotal() {
        return canEditFileTotal;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFile item) {
        ImageView itemIcon = helper.getView(R.id.itemIcon);
        int width;
        switch (itemLayout) {
            case R.layout.item_file_grid:
                helper.setText(R.id.itemName, item.getFileName());
                width = (ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(2 * 16 + 2 * 53, mContext)) / 3;

                if ("2".equals(item.getCategory())) {
                    //如果是文件夹
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemIcon.getLayoutParams();
                    params.width = width;
                    params.height = (int) ((params.width / 222) * 177f);
                    itemIcon.setLayoutParams(params);
                } else {
                    //
                    if (!TextUtils.isEmpty(item.getThumbnail())) {
                        //有缩略图
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemIcon.getLayoutParams();
                        params.width = DensityUtil.dip2px(52, mContext);
                        params.height = params.width;
                        itemIcon.setLayoutParams(params);
                    } else {
                        //没有缩略图
                    }
                }

                cbSelectMode = helper.getView(R.id.cbSelectMode);
                cbSelectMode.setVisibility((isSelectMode() && item.isCanSelected()) ? View.VISIBLE : View.GONE);
                FileIconUtils.getThumnailURL(mContext, item, itemIcon); //获取缩略图
                break;
            case R.layout.item_file_list:
                helper.setText(R.id.itemName, item.getFileName());
                helper.setText(R.id.tv_time, TimeUtils.longToString(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
                width = (int) ((ScreenUtils.getScreenWidth(mContext) / 1080f) * DensityUtil.dip2px(46, mContext));

                if ("2".equals(item.getCategory())) {
                    //如果是文件夹
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemIcon.getLayoutParams();
                    params.width = width;
                    params.height = (int) ((params.width / 222) * 177f);
                    itemIcon.setLayoutParams(params);
                } else {
                    if (!TextUtils.isEmpty(item.getThumbnail())) {
                        //有缩略图
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemIcon.getLayoutParams();
                        params.width = DensityUtil.dip2px(40, mContext);
                        params.height = params.width;
                        itemIcon.setLayoutParams(params);
                    } else {
                        //没有缩略图
                    }
                }
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) itemIcon.getLayoutParams();
                params2.width = width;
                params2.height = (width * 59 / 74);
                itemIcon.setLayoutParams(params2);

                cbSelectMode = helper.getView(R.id.cbSelectMode);
                cbSelectMode.setVisibility((isSelectMode() && item.isCanSelected()) ? View.VISIBLE : View.GONE);
                ivNormalMode = helper.getView(R.id.ivNormalMode);
                ivNormalMode.setVisibility(!(isSelectMode() && item.isCanSelected()) ? View.VISIBLE : View.GONE);
                if (isSelectMode()) {
                    ivNormalMode.setVisibility(View.GONE);
                }
                if (!item.isCanSelected()) {
                    helper.setGone(R.id.tv_time, false);
                } else {
                    helper.setGone(R.id.tv_time, true);
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
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.isCanSelected()) {
                        return;
                    }
                    if (isChecked(item)) {
                        removeCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                    } else {
                        addCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                    }
                }
            });

            if (checkedList.size() != getData().size() - 5) {
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
                    if (!item.isCanSelected()) {
                        return true;
                    }
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
