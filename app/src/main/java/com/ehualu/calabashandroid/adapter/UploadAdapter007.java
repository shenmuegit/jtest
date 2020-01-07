package com.ehualu.calabashandroid.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.activity.EditFileActivity;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.model.TransferModel;
import com.ehualu.calabashandroid.service.FileUploader;
import com.ehualu.calabashandroid.upload.ProgressListener;
import com.ehualu.calabashandroid.utils.CapacityUtils;
import com.ehualu.calabashandroid.utils.FileIconUtils;

import java.util.List;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

/**
 * 专属上传adapter
 */
public class UploadAdapter007 extends BaseQuickAdapter<TransferModel, BaseViewHolder> {

    private UploadAdapter.OnDeleteClickLister mDeleteClickListener;
    private BaseActivity baseActivity;
    private boolean isFinished;

    public UploadAdapter007(BaseActivity baseActivity, List<TransferModel> data, boolean isFinished) {
        super(R.layout.item_transfer_list, data);
        this.isFinished = isFinished;
        this.baseActivity = baseActivity;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    protected void convert(BaseViewHolder holder, TransferModel bean) {
        int position = holder.getAdapterPosition();
        ImageView ivIcon = (ImageView) holder.getView(R.id.iv_icon_item);
        TextView tvName = (TextView) holder.getView(R.id.tv_name);
        TextView tvSize = (TextView) holder.getView(R.id.tv_size);
        TextView tvPath = holder.getView(R.id.tvPath);

        if (!TextUtils.isEmpty(bean.getTargetPath())) {
            tvPath.setText(bean.getTargetPath());
        }

        TextView tvCurrentProgress = (TextView) holder.getView(R.id.tv_currentProgress);
        ZzHorizontalProgressBar progressbar = (ZzHorizontalProgressBar) holder.getView(R.id.progressbar);
        ImageView ivDownloadIcon = (ImageView) holder.getView(R.id.iv_download_icon);

        ivDownloadIcon.setBackgroundResource(R.drawable.icon_upload);
        TextView tvCurrentSpeed = (TextView) holder.getView(R.id.tv_currentSpeed);
        TextView tvStatus = (TextView) holder.getView(R.id.tv_status);
        TextView tvDelete = (TextView) holder.getView(R.id.tv_delete);
        LinearLayout llGoing = (LinearLayout) holder.getView(R.id.ll_going);
        LinearLayout backupExtractLocation = (LinearLayout) holder.getView(R.id.ll_location_backup_extract);
        LinearLayout llLocation = (LinearLayout) holder.getView(R.id.ll_location_upload);
        LinearLayout llFinsihedSizeTime = (LinearLayout) holder.getView(R.id.ll_finished_size_time);

        tvName.setText(bean.getName());
        tvSize.setText(CapacityUtils.bytesToHumanReadable(bean.getSize()));
        String myName = bean.getName();
        if (myName.endsWith(".mp4") || myName.endsWith(".3gp") || myName.endsWith("rmvb") || myName.endsWith("avi")) {
            Glide.with(MyApp.getAppContext()).load(bean.getPath()).placeholder(R.mipmap.file_icon_video).into(ivIcon);
        } else if (myName.endsWith(".jpg") || myName.endsWith(".jpeg") || myName.endsWith("png")) {
            Glide.with(MyApp.getAppContext()).load(bean.getPath()).placeholder(R.mipmap.file_icon_picture).into(ivIcon);
        } else {
            Glide.with(baseActivity).load(FileIconUtils.getFileIcon(bean.getName())).into(ivIcon);
        }

        FileUploader.FileUploaderBinder binder = baseActivity.getFileUploaderBinder();


        if (binder.containTask(bean.getTaskId(), this.hashCode())) {

        } else {
            binder.removeDatatransferProgressListener(bean.getTaskId());

            progressbar.setProgress(bean.getCurrentProgress());
            tvCurrentProgress.setText(bean.getCurrentProgress() + "%");
            ProgressListener progressListener = new ProgressListener(bean, new ProgressListener.ProgressCallback() {
                @Override
                public void progress(int percent, long speed) {
                    baseActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressbar.setProgress(percent);
                            tvCurrentProgress.setText(percent + "%");

                            //设置速度
                            if (speed > 1000) {
                                String result = String.format("%.1f", speed / 1000.0);
                                tvCurrentSpeed.setText(result + "m/s");
                            } else {
                                tvCurrentSpeed.setText(speed + "k/s");
                            }


                            notifyItemChanged(position, R.id.tv_currentSpeed);
                            notifyItemChanged(position, R.id.progressbar);
                            notifyItemChanged(position, R.id.tv_currentProgress);
                        }
                    });
                }

                @Override
                public void complete(String taksId, int percent, long speed) {
                    baseActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressbar.setProgress(percent);
                            tvCurrentProgress.setText(percent + "%");
                            tvCurrentSpeed.setText("");
                            notifyItemChanged(position, R.id.tv_currentSpeed);
                            notifyItemChanged(position, R.id.progressbar);
                            notifyItemChanged(position, R.id.tv_currentProgress);
                        }
                    });
                }

                @Override
                public void updateSpeed(long speed) {
                }
            });
            progressListener.setKkey(this.hashCode() + bean.getTaskId());
            binder.addDatatransferProgressListener(progressListener);
        }

        if (isFinished) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击上传完成列表中的item,直接跳转到文件编辑页面
                    Intent intent = new Intent(mContext, EditFileActivity.class);
                    RemoteFile rf = new RemoteFile();
                    rf.setFileName(bean.getName());
                    rf.setID(bean.getTaskId());
                    rf.setThumbnail(bean.getPath());
                    intent.putExtra("isLocal",true);
                    intent.putExtra("remoteFile", rf);
                    mContext.startActivity(intent);
                }
            });

            llGoing.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
            tvCurrentProgress.setVisibility(View.GONE);
        } else {
            llLocation.setVisibility(View.GONE);
            tvStatus.setVisibility(View.GONE);
        }
        llFinsihedSizeTime.setVisibility(View.GONE);
        backupExtractLocation.setVisibility(View.GONE);

        tvDelete.setTag(holder.getAdapterPosition());
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

    public void setOnDeleteClickListener(UploadAdapter.OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }

    public int getPositionByTaskId(String taskId) {
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).getTaskId().equals(taskId)) {
                return i;
            }
        }
        return -1;
    }
}
