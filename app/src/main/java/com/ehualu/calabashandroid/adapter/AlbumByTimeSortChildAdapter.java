package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.FileIconUtils;
import com.ehualu.calabashandroid.utils.ScreenUtils;

import java.util.List;

public class AlbumByTimeSortChildAdapter extends BaseQuickAdapter<RemoteFile, BaseViewHolder> {

    private Context mContext;
    private boolean isBitmapMode;
    private AlbumByTimeSortAdapter parentAdapter;

    public AlbumByTimeSortChildAdapter(Context context, @Nullable List<RemoteFile> data, boolean isBitmapMode,
                                       AlbumByTimeSortAdapter parentAdapter) {
        super(R.layout.item_album_by_time_sort_child, data);
        this.mContext = context;
        this.isBitmapMode = isBitmapMode;
        this.parentAdapter = parentAdapter;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFile item) {
        helper.setGone(R.id.ivSelect, parentAdapter.isSelectMode());
        ImageView ivSelect = helper.getView(R.id.ivSelect);
        ImageView ivPhoto = helper.getView(R.id.ivPhoto);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPhoto.getLayoutParams();
        if (isBitmapMode) {
            int width = (ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(13 * 2 + 4, mContext)) / 2;
            params.width = width;
            params.height = width;
            ivPhoto.setLayoutParams(params);
        } else {
            int width = (ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(13 * 2 + 2 * 3, mContext)) / 4;
            params.width = width;
            params.height = width;
            ivPhoto.setLayoutParams(params);
        }
        FileIconUtils.getThumnailURL(mContext, item, ivPhoto); //获取缩略图
        if (parentAdapter.isChecked(item)) {
            ivSelect.setImageResource(R.drawable.upload_photo_cb_checked);
        } else {
            ivSelect.setImageResource(R.drawable.upload_photo_cb_unchecked);
        }

        if (parentAdapter.isSelectMode()) {
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (parentAdapter.isChecked(item)) {
                        parentAdapter.removeCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                    } else {
                        parentAdapter.addCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                    }
                }
            });

            if (parentAdapter.checkedList.size() != parentAdapter.getFileNumber()) {
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

            helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (parentAdapter.onItemClickInterface != null) {
                        parentAdapter.onItemClickInterface.onItemLongCick(item);
                        parentAdapter.addCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                        parentAdapter.onItemClickInterface.selectCount(parentAdapter.checkedList,
                                parentAdapter.checkedList.size());
                    }
                    return true;
                }
            });
        }
    }
}
