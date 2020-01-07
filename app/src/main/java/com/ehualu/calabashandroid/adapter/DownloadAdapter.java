package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.activity.EditFileActivity;
import com.ehualu.calabashandroid.base.BaseRecyclerViewAdapter;
import com.ehualu.calabashandroid.base.BaseRecyclerViewHolder;
import com.ehualu.calabashandroid.model.DownloadTask;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.CapacityUtils;
import com.ehualu.calabashandroid.utils.TimeUtils;
import com.ehualu.calabashandroid.widget.SlideRecyclerView;

import java.util.List;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

public class DownloadAdapter extends BaseRecyclerViewAdapter<DownloadTask> {

    private OnDeleteClickLister mDeleteClickListener;
    private boolean isFinished;
    private Context context;
    private SlideRecyclerView recyclerView;

    public DownloadAdapter(Context context, List<DownloadTask> data, boolean isFinished) {
        super(context, data, R.layout.item_transfer_list);
        this.isFinished = isFinished;
        this.context = context;
    }

    public DownloadAdapter(Context context, List<DownloadTask> data, boolean isFinished, SlideRecyclerView recyclerView) {
        super(context, data, R.layout.item_transfer_list);
        this.isFinished = isFinished;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onBindData(BaseRecyclerViewHolder holder, DownloadTask bean, int position) {
        ImageView ivIcon = (ImageView) holder.getView(R.id.iv_icon_item);
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

        TextView fileSize = (TextView) holder.getView(R.id.tv_finished_size);
        TextView finishTime = (TextView) holder.getView(R.id.tv_finished_time);

        //        Glide.with(context).load(FileIconUtils.getFileIcon(bean.file)).into(ivIcon);
        //        ivIcon.setImageResource(FileIconUtils.getFileIcon(bean.file));
        Glide.with(context).load(bean.getDownLoadRecordEntity().getPath()).into(ivIcon);
        tvName.setText(bean.file.getFileName());
        progressbar.setProgress(bean.getProgress());
        tvCurrentProgress.setText(bean.getProgress() + "%");
        tvCurrentSpeed.setText(bean.getSpeed());
        tvSize.setText(CapacityUtils.bytesToHumanReadable(bean.file.getFileSize()));
        fileSize.setText(CapacityUtils.bytesToHumanReadable(bean.file.getFileSize()));

        if (isFinished) {
            llGoing.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
            tvCurrentProgress.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(v -> {
                //点击上传完成列表中的item,直接跳转到文件编辑页面
                Intent intent = new Intent(context, EditFileActivity.class);
                RemoteFile rf = new RemoteFile();
                rf.setFileName(bean.getDownLoadRecordEntity().getFileName());
                rf.setID(bean.getDownLoadRecordEntity().getFileId());
                rf.setThumbnail(bean.getDownLoadRecordEntity().getPath());
                intent.putExtra("isLocal", true);
                intent.putExtra("remoteFile", rf);
                context.startActivity(intent);
            });
        } else {
            tvStatus.setVisibility(View.GONE);
            llFinsihedSizeTime.setVisibility(View.GONE);
        }
        uploadLocation.setVisibility(View.GONE);
        backupExtractLocation.setVisibility(View.GONE);

        switch (bean.getDownLoadRecordEntity().getStatus()) {
            case 0:
                tvStatus.setText("等待下载");//根据bean的status 判断显示状态
                tvStatus.setVisibility(View.VISIBLE);
                tvCurrentSpeed.setVisibility(View.GONE);
                ivDownloadIcon.setVisibility(View.VISIBLE);
                break;
            case 1:
//                tvStatus.setText("下载中");//根据bean的status 判断显示状态
                break;
            case 2:
                tvStatus.setText("暂停中...");//根据bean的status 判断显示状态
                progressbar.setProgressColor(0xff5e5e5e);
                tvStatus.setVisibility(View.VISIBLE);
                tvCurrentSpeed.setVisibility(View.GONE);
                break;
            case 3:
                tvStatus.setText("下载失败");//根据bean的status 判断显示状态
                tvStatus.setVisibility(View.VISIBLE);
                tvCurrentSpeed.setVisibility(View.GONE);
                break;
            case 4:
                llGoing.setVisibility(View.GONE);
                progressbar.setVisibility(View.GONE);
                tvCurrentProgress.setVisibility(View.GONE);
                finishTime.setText(TimeUtils.dateToString(bean.getDownLoadRecordEntity().getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
                tvStatus.setVisibility(View.VISIBLE);
                llFinsihedSizeTime.setVisibility(View.VISIBLE);
                break;
        }

        //        if (recyclerView != null)
        //            recyclerView.showMenu(position);

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