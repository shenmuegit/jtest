package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseRecyclerViewAdapter;
import com.ehualu.calabashandroid.base.BaseRecyclerViewHolder;
import com.ehualu.calabashandroid.model.TransferModel;

import java.util.List;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

/**
 * 传输列表：备份提取列表Adapter
 */
public class BackupExtractAdapter extends BaseRecyclerViewAdapter<TransferModel> {

    private OnDeleteClickLister mDeleteClickListener;
    private boolean isFinished;

    public BackupExtractAdapter(Context context, List<TransferModel> data, boolean isFinished) {
        super(context, data, R.layout.item_transfer_list);
        this.isFinished = isFinished;
    }

    @Override
    protected void onBindData(BaseRecyclerViewHolder holder, TransferModel bean, int position) {
        ImageView ivIcon = (ImageView) holder.getView(R.id.iv_icon);
        TextView tvName = (TextView) holder.getView(R.id.tv_name);
        TextView tvCurrentProgress = (TextView) holder.getView(R.id.tv_currentProgress);
        ZzHorizontalProgressBar progressbar = (ZzHorizontalProgressBar) holder.getView(R.id.progressbar);
        ImageView ivDownloadIcon = (ImageView) holder.getView(R.id.iv_download_icon);
        TextView tvCurrentSpeed = (TextView) holder.getView(R.id.tv_currentSpeed);
        TextView tvStatus = (TextView) holder.getView(R.id.tv_status);
        TextView tvSize = (TextView) holder.getView(R.id.tv_size);
        TextView tvDelete = (TextView) holder.getView(R.id.tv_delete);
        LinearLayout llGoing = (LinearLayout) holder.getView(R.id.ll_going);
        LinearLayout uploadLocation = (LinearLayout) holder.getView(R.id.ll_location_upload);
        LinearLayout backupExtractLocation = (LinearLayout) holder.getView(R.id.ll_location_backup_extract);
        LinearLayout llFinsihedSizeTime = (LinearLayout) holder.getView(R.id.ll_finished_size_time);

        if (isFinished) {
            llGoing.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
            tvCurrentProgress.setVisibility(View.GONE);
        } else {
            tvStatus.setVisibility(View.GONE);
            backupExtractLocation.setVisibility(View.GONE);
        }
        llFinsihedSizeTime.setVisibility(View.GONE);
        uploadLocation.setVisibility(View.GONE);

        tvDelete.setTag(position);
        if (!tvDelete.hasOnClickListeners()) {
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteClickListener != null) {
                        mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                    }
                }
            });
        }
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }

}