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
import com.ehualu.calabashandroid.utils.TimeUtils;

import java.util.List;

public class RemoteDocumentChildAdapter extends BaseQuickAdapter<RemoteFile, BaseViewHolder> {

    private Context mContext;
    private int itemLayout;
    private RemoteDocumentParentAdapter parentAdapter;

    public RemoteDocumentChildAdapter(Context context, @Nullable List<RemoteFile> data, int itemLayout,
                                      RemoteDocumentParentAdapter parentAdapter) {
        super(itemLayout, data);
        this.mContext = context;
        this.itemLayout = itemLayout;
        this.parentAdapter = parentAdapter;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFile item) {
        ImageView ivIcon = helper.getView(R.id.ivIcon);
        ImageView cbSelectMode = helper.getView(R.id.cbSelectMode);
        helper.setGone(R.id.cbSelectMode, parentAdapter.isSelectMode());
        helper.setText(R.id.tvName, item.getFileName());
        switch (itemLayout) {
            case R.layout.item_remote_document_child_grid:
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivIcon.getLayoutParams();
                params.width = DensityUtil.dip2px(52, mContext);
                params.height = DensityUtil.dip2px(64, mContext);
                ivIcon.setLayoutParams(params);
                FileIconUtils.getThumnailURL(mContext, item, ivIcon); //获取缩略图
                break;
            case R.layout.item_remote_document_child_list:
                helper.setText(R.id.tvTime, TimeUtils.longToString(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) ivIcon.getLayoutParams();
                params2.width = DensityUtil.dip2px(45, mContext);
                params2.height = DensityUtil.dip2px(55, mContext);
                ivIcon.setLayoutParams(params2);
                FileIconUtils.getThumnailURL(mContext, item, ivIcon); //获取缩略图
                break;
        }

        if (parentAdapter.isChecked(item)) {
            cbSelectMode.setImageResource(R.drawable.icon_checked);
        } else {
            cbSelectMode.setImageResource(R.drawable.icon_no_checked);
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
