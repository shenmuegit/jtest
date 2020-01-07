package com.ehualu.calabashandroid.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseAdapter;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.CapacityUtils;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.FileIconUtils;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.ehualu.calabashandroid.utils.TimeUtils;

import java.util.List;

public class AudioAdapter extends BaseAdapter<RemoteFile, BaseViewHolder> {

    private ImageView cbSelectMode;//选择框

    public AudioAdapter(@Nullable List<RemoteFile> data, int itemLayout) {
        super(R.layout.item_audio_child, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFile item) {
        ImageView itemIcon = helper.getView(R.id.iv_audio);
        helper.setText(R.id.tv_name, item.getFileName());
        helper.setText(R.id.tv_time, TimeUtils.longToString(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
        helper.setText(R.id.tv_size, CapacityUtils.bytesToHumanReadable(item.getFileSize()));
        int width = (int) ((ScreenUtils.getScreenWidth(mContext) / 1080f) * DensityUtil.dip2px(46, mContext));
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) itemIcon.getLayoutParams();
        params2.width = width;
        params2.height = (width * 59 / 74);
        itemIcon.setLayoutParams(params2);

        cbSelectMode = helper.getView(R.id.iv_select_mode);
        cbSelectMode.setVisibility(isSelectMode() ? View.VISIBLE : View.GONE);
        FileIconUtils.getThumnailURL(mContext, item, itemIcon); //获取缩略图
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
                        onItemClickInterface.selectCount(checkedList,checkedList.size());
                    }
                    return true;
                }
            });
        }
    }
}
