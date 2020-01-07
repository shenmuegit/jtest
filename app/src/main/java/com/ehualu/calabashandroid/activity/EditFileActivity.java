package com.ehualu.calabashandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.db.DownLoadRecordEntityDao;
import com.ehualu.calabashandroid.db.entity.DownLoadRecordEntity;
import com.ehualu.calabashandroid.db.manager.DBManager;
import com.ehualu.calabashandroid.dialog.CreateFolderAndReNameDialog;
import com.ehualu.calabashandroid.interfaces.BottomMoreInterface;
import com.ehualu.calabashandroid.interfaces.ConfirmCancelInterface;
import com.ehualu.calabashandroid.interfaces.NormalDialogInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.popupwindow.BottomDeletePopupWindow;
import com.ehualu.calabashandroid.popupwindow.BottomMorePopupWindow;
import com.ehualu.calabashandroid.popupwindow.SharePopupWindow;
import com.ehualu.calabashandroid.receiver.DownloadBroadcast;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;
import com.ehualu.calabashandroid.responseBean.ResponseFileInfoBean;
import com.ehualu.calabashandroid.utils.BroadcastUtils;
import com.ehualu.calabashandroid.utils.Constants;
import com.ehualu.calabashandroid.utils.FileIconUtils;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.OpenFileUtil;
import com.ehualu.calabashandroid.utils.OperationUtils;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.ehualu.calabashandroid.widget.ImageProgress;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author: houxiansheng
 * time：2019-12-6 14:17:17
 * describe：编辑文件
 */
public class EditFileActivity extends BaseActivity implements BottomMoreInterface {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.ll_download)
    LinearLayout llDownload;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.ll_backup)
    LinearLayout llBackup;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    RemoteFile remoteFile;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.tv_share)
    TextView tvShare;
    List<RemoteFile> list = new ArrayList<>();
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    String collectFlag = "0";
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.tv_download)
    TextView tvDownload;

    @BindView(R.id.img_progress)
    ImageProgress progress;
    DownloadBroadcast.DownloadTaskListener listener;
    @BindView(R.id.iv_play)
    ImageView ivPlay;

    private boolean isLocal;//专门给上传传输完成列表使用的
    private String filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        ButterKnife.bind(this);
        initViewAndData();
        initListener();
    }

    private void initViewAndData() {
        ivShare.setBackgroundResource(R.drawable.icon_bottom_share);
        remoteFile = (RemoteFile) getIntent().getSerializableExtra("remoteFile");

        isLocal = getIntent().getBooleanExtra("isLocal", false);
        list.add(remoteFile);
        getInfo();
        ivDownload.setBackgroundResource(R.drawable.icon_bottom_download);
        tvTitle.setText(remoteFile.getFileName());
        progress.setVisibility(View.GONE);
        filePath = Constants.DOWNLOAD_PATH + remoteFile.getFileName();
        setPlayIconStatus();
        if (isLocal) {
            Glide.with(this).load(remoteFile.getThumbnail()).into(ivIcon);
        } else {
            FileIconUtils.getThumnailURL(this, remoteFile, ivIcon); //获取缩略图
        }
    }

    private void initListener() {
        llDownload.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llBackup.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        llMore.setOnClickListener(this);
        llBack.setOnClickListener(this);
        imgRight.setOnClickListener(this);
        ivPlay.setOnClickListener(this);

        listener = task -> {
            if (remoteFile.getID().equals(task.file.getID())) {
                if (progress.getVisibility() != View.VISIBLE) {
                    progress.setVisibility(View.VISIBLE);
                }
                long count = DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().queryBuilder()
                        .where(DownLoadRecordEntityDao.Properties.TaskId.eq(task.id)).count();
                if (count <= 0) {
                    progress.setVisibility(View.GONE);
                    return;
                }
                if (task.getProgress() == 100) {
                    ToastUtil.showCenterHasImageToast(this, "下载完成");
                    setPlayIconStatus();
                }
                progress.setProgress(task.getProgress());
            }
        };
        MyApp.getBroadcast().addListener(listener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_download:
                //开始文件下载
                BroadcastUtils.sendDownloadBroadcast(remoteFile);
                ToastUtil.showCenterHasImageToast(this, "文件已添加至传输列表");
                break;
            case R.id.ll_share:
                String path = Constants.DOWNLOAD_PATH + remoteFile.getFileName();
                if (new File(path).exists()) {
                    remoteFile.setPath(path);
                    new SharePopupWindow(this, remoteFile).showPopupWindow(llShare);
                } else {
                    ToastUtil.showCenterHasImageToast(this, "请先下载后再分享！");
                }
                break;
            case R.id.ll_backup:
                backup();
                break;
            case R.id.ll_delete:
                new BottomDeletePopupWindow(this, deleteInterface, list).showMoreOperationPopup(llDelete);
                break;
            case R.id.ll_more:
                new BottomMorePopupWindow(this, this, list, false).showMoreOperationPopup(llMore);
                break;
            case R.id.img_right:
                if (collectFlag.equals("0")) {
                    collectFlag = "1";
                } else {
                    collectFlag = "0";
                }
                ApiRetrofit.getInstance().postCollection(remoteFile.getID(), collectFlag)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<PublicResponseBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(PublicResponseBean responseFileSearchBean) {
                                if (responseFileSearchBean.isSuccess()) {
                                    getInfo();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showCenterForBusiness(EditFileActivity.this, "文件收藏失败！");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case R.id.iv_play:
                OpenFileUtil.openFile(this, filePath);
                break;
        }
    }

    /**
     * 获取详细信息
     */
    private void getInfo() {
        ApiRetrofit.getInstance().getFileInfo(remoteFile.getID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseFileInfoBean>() {
                    @Override
                    public void accept(ResponseFileInfoBean responseFileInfoBean) throws Exception {
                        MyLog.d(responseFileInfoBean.toString());
                        if (responseFileInfoBean.isSuccess()) {
                            setFileData(responseFileInfoBean);
                        } else {
                            ToastUtil.showCenterHasImageToast(EditFileActivity.this,
                                    responseFileInfoBean.getMessage());
                        }
                    }
                });
    }

    /**
     * 设置文件数据
     */
    private void setFileData(ResponseFileInfoBean infoBean) {
        collectFlag = infoBean.getData().getCollectStatus();
        if (infoBean.getData().getCollectStatus().equals("0")) {
            imgRight.setBackgroundResource(R.drawable.collect_not);
        } else {
            imgRight.setBackgroundResource(R.drawable.collect);
        }
        tvName.setText(infoBean.getData().getFileName());
        tvTitle.setText(infoBean.getData().getFileName());
        RemoteFile temp = new RemoteFile();
        temp.setFileName(infoBean.getData().getFileName());
        temp.setCategory("1");
        temp.setID(infoBean.getData().getFileId());
        temp.setThumbnail(infoBean.getData().getFileId());
    }

    @Override
    public void onMove(List<RemoteFile> list) {
        Intent intent = new Intent();
        intent.putExtra("moveList", (Serializable) list);
        intent.setClass(this, MoveActivity.class);
        //        startActivityForResult(intent, requestCodeMove);
        startActivity(intent);
    }

    @Override
    public void onRename(RemoteFile remoteFile) {
        new CreateFolderAndReNameDialog(this, "重命名", remoteFile.getFileName(), reNameInterface).show();
    }

    /**
     * 重命名
     */
    NormalDialogInterface reNameInterface = new NormalDialogInterface() {
        @Override
        public void onConfirm(String text) {
            ApiRetrofit.getInstance().postRename(text, remoteFile.getID(), remoteFile.getCategory())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<PublicResponseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(PublicResponseBean responseBean) {
                            MyLog.d(responseBean.toString());
                            if (responseBean.isSuccess()) {
                                Handler handler = new Handler();
                                getInfo();
                            } else {
                                ToastUtil.showCenterHasImageToast(EditFileActivity.this, responseBean.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.showCenterForBusiness(EditFileActivity.this, "重命名失败！");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    public void onInfo(RemoteFile remoteFile) {
        Intent intent = new Intent();
        intent.setClass(this, FileInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("remoteFile", remoteFile);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 删除操作
     */
    ConfirmCancelInterface deleteInterface = new ConfirmCancelInterface() {
        @Override
        public void onCancel(List<RemoteFile> list) {

        }

        @Override
        public void onConfirm(List<RemoteFile> list) {
            OperationUtils.deleteRemoteFile(EditFileActivity.this, list);
        }
    };

    /**
     * 葫芦备份
     */
    private void backup() {
        List<String> fileList = new ArrayList<>();//文件列表
        List<String> folderList = new ArrayList<>();//文件列表
        fileList.add(remoteFile.getID());
        ApiRetrofit.getInstance().postBackUp(folderList, fileList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PublicResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PublicResponseBean responseBean) {
                        MyLog.d(responseBean.toString());
                        if (responseBean.isSuccess()) {
                            ToastUtil.showCenterHasImageToast(EditFileActivity.this, "文件已添加至葫芦备份");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterForBusiness(EditFileActivity.this, "葫芦备份失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 设置播放按钮的状态
     */
    private void setPlayIconStatus() {
        String end = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase(Locale.getDefault());
        if (end.equals("3gp") || end.equals("mp4")) {
            if (new File(filePath).exists()) {
                ivPlay.setVisibility(View.VISIBLE);
            } else {
                ivPlay.setVisibility(View.GONE);
            }
        } else {
            ivPlay.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.getBroadcast().removeListener(listener);
    }
}
