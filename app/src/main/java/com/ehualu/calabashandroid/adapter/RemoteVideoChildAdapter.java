package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.CapacityUtils;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.FileIconUtils;
import com.ehualu.calabashandroid.utils.GlideRoundTransform;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.ehualu.calabashandroid.utils.TimeUtils;

import java.util.List;

public class RemoteVideoChildAdapter extends BaseQuickAdapter<RemoteFile, BaseViewHolder> {

    private Context mContext;
    private int itemLayout;
    private RemoteVideoParentAdapter parentAdapter;

    public RemoteVideoChildAdapter(Context context, List<RemoteFile> data, int itemLayout,
                                   RemoteVideoParentAdapter parentAdapter) {
        super(itemLayout, data);
        this.mContext = context;
        this.itemLayout = itemLayout;
        this.parentAdapter = parentAdapter;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFile item) {
        ImageView ivChildVideo = helper.getView(R.id.ivChildVideo);
        ImageView ivSelect = helper.getView(R.id.ivSelect);
        helper.setGone(R.id.ivSelect, parentAdapter.isSelectMode());
        switch (itemLayout) {
            case R.layout.item_remote_video_child_grid:
                helper.setText(R.id.tvName, item.getFileName());
                helper.setText(R.id.tv_duration, TimeUtils.getVideoTime(item));
                RelativeLayout rlVideo = helper.getView(R.id.rlVideo);
                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) rlVideo.getLayoutParams();
                int width = (ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(13 * 2 + 44 * 2, mContext)) / 3;
                ll.width = width;
                ll.height = width;
                rlVideo.setLayoutParams(ll);
                FileIconUtils.getThumnailURL(mContext, item, ivChildVideo); //获取缩略图
                break;
            case R.layout.item_remote_video_child_list:
                RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(mContext, 4));
                helper.setText(R.id.tv_name, item.getFileName());
                helper.setText(R.id.tv_time, TimeUtils.longToString(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
                helper.setText(R.id.tv_size, CapacityUtils.bytesToHumanReadable(item.getFileSize()));
                FileIconUtils.getThumnailURL(mContext, item, ivChildVideo); //获取缩略图
                break;
        }

        if (parentAdapter.isChecked(item)) {
            ivSelect.setBackgroundResource(R.drawable.upload_photo_cb_checked);
        } else {
            ivSelect.setBackgroundResource(R.drawable.upload_photo_cb_unchecked);
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

            /**
             * item的长按事件
             */
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
