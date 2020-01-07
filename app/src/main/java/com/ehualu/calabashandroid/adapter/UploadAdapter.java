package com.ehualu.calabashandroid.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.base.BaseRecyclerViewAdapter;
import com.ehualu.calabashandroid.base.BaseRecyclerViewHolder;
import com.ehualu.calabashandroid.model.TransferModel;
import com.ehualu.calabashandroid.utils.CapacityUtils;
import com.ehualu.calabashandroid.utils.FileIconUtils;

import java.util.List;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

public class UploadAdapter extends BaseRecyclerViewAdapter<TransferModel> {

    private OnDeleteClickLister mDeleteClickListener;
    private boolean isFinished;
    private BaseActivity baseActivity;

    public UploadAdapter(BaseActivity baseActivity, List<TransferModel> data, boolean isFinished) {
        super(baseActivity, data, R.layout.item_transfer_list);
        this.isFinished = isFinished;
        this.baseActivity = baseActivity;
    }

    /**
     * 禁用item复用
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    protected void onBindData(BaseRecyclerViewHolder holder, TransferModel bean, int position) {
        ImageView ivIcon = (ImageView) holder.getView(R.id.iv_icon);
        TextView tvName = (TextView) holder.getView(R.id.tv_name);
        TextView tvSize = (TextView) holder.getView(R.id.tv_size);

        TextView tvCurrentProgress = (TextView) holder.getView(R.id.tv_currentProgress);
        ZzHorizontalProgressBar progressbar = (ZzHorizontalProgressBar) holder.getView(R.id.progressbar);
        ImageView ivDownloadIcon = (ImageView) holder.getView(R.id.iv_download_icon);
        TextView tvCurrentSpeed = (TextView) holder.getView(R.id.tv_currentSpeed);
        TextView tvStatus = (TextView) holder.getView(R.id.tv_status);
        TextView tvDelete = (TextView) holder.getView(R.id.tv_delete);
        LinearLayout llGoing = (LinearLayout) holder.getView(R.id.ll_going);
        LinearLayout backupExtractLocation = (LinearLayout) holder.getView(R.id.ll_location_backup_extract);
        LinearLayout llLocation = (LinearLayout) holder.getView(R.id.ll_location_upload);
        LinearLayout llFinsihedSizeTime = (LinearLayout) holder.getView(R.id.ll_finished_size_time);

        tvName.setText(bean.getName());
        tvSize.setText(CapacityUtils.bytesToHumanReadable(bean.getSize()));
        Glide.with(baseActivity).load(FileIconUtils.getFileIcon(bean.getName())).into(ivIcon);

        if (isFinished) {
            llGoing.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
            tvCurrentProgress.setVisibility(View.GONE);
        } else {
            llLocation.setVisibility(View.GONE);
            tvStatus.setVisibility(View.GONE);
        }
        llFinsihedSizeTime.setVisibility(View.GONE);
        backupExtractLocation.setVisibility(View.GONE);

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