package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.CapacityUtils;
import com.ehualu.calabashandroid.utils.FileIconUtils;
import com.ehualu.calabashandroid.utils.TimeUtils;

import java.util.List;

/**
 * add by houxiansheng 2019-12-18 15:01:38 音频子Adapter
 */
public class AudioChildAdapter extends BaseQuickAdapter<RemoteFile, BaseViewHolder> {

    private Context context;
    private AudioParentAdapter parentAdapter;

    public AudioChildAdapter(Context context, @Nullable List<RemoteFile> data, AudioParentAdapter parentAdapter) {
        super(R.layout.item_audio_child, data);
        this.context = context;
        this.parentAdapter = parentAdapter;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFile item) {
        ImageView ivAudio = helper.getView(R.id.iv_audio);
        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvTime = helper.getView(R.id.tv_time);
        TextView tvSize = helper.getView(R.id.tv_size);
        tvName.setText(item.getFileName());
        tvTime.setText(TimeUtils.longToString(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
        tvSize.setText(CapacityUtils.bytesToHumanReadable(item.getFileSize()));
        ImageView ivSelectMode = helper.getView(R.id.iv_select_mode);
        ivSelectMode.setVisibility(parentAdapter.isSelectMode() ? View.VISIBLE : View.GONE);
        FileIconUtils.getThumnailURL(mContext, item, ivAudio); //获取缩略图
        if (parentAdapter.isChecked(item)) {
            ivSelectMode.setImageResource(R.drawable.icon_checked);
        } else {
            ivSelectMode.setImageResource(R.drawable.icon_no_checked);
        }

        //选择模式
        if (parentAdapter.isSelectMode()) {
            helper.itemView.setOnLongClickListener(null);
            helper.itemView.setOnClickListener(null);
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
            if (parentAdapter.checkedList.size() != parentAdapter.getTotalSize()) {
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
