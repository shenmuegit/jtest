package com.ehualu.calabashandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.responseBean.ResponseFileInfoBean;
import com.ehualu.calabashandroid.responseBean.ResponseFolderInfoBean;
import com.ehualu.calabashandroid.utils.CapacityUtils;
import com.ehualu.calabashandroid.utils.FileIconUtils;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.TimeUtils;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author: houxiansheng
 * time：2019-12-6 09:44:17
 * describe：文件详情
 */
public class FileInfoActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_createTime)
    TextView tvCreateTime;
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;
    @BindView(R.id.tv_geography_location)
    TextView tvGeographyLocation;
    @BindView(R.id.tv_width_height)
    TextView tvWidthHeight;
    RemoteFile remoteFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileinfo);
        ButterKnife.bind(this);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        initViewAndData();
        initListener();
    }

    private void initViewAndData() {
        remoteFile = (RemoteFile) getIntent().getSerializableExtra("remoteFile");
        if (remoteFile.getCategory().equals("1")) {//文件
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
                                ToastUtil.showCenterHasImageToast(FileInfoActivity.this,
                                        responseFileInfoBean.getMessage());
                            }
                        }
                    });
        } else if (remoteFile.getCategory().equals("2")) {//文件夹
            ApiRetrofit.getInstance().getFolderInfo(remoteFile.getID())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResponseFolderInfoBean>() {
                        @Override
                        public void accept(ResponseFolderInfoBean responseFolderInfoBean) throws Exception {
                            MyLog.d(responseFolderInfoBean.toString());
                            setFolderData(responseFolderInfoBean);
                            if (responseFolderInfoBean.isSuccess()) {
                                setFolderData(responseFolderInfoBean);
                            } else {
                                ToastUtil.showCenterHasImageToast(FileInfoActivity.this,
                                        responseFolderInfoBean.getMessage());
                            }
                        }
                    });
        }
    }

    /**
     * 设置文件夹数据
     */
    private void setFolderData(ResponseFolderInfoBean infoBean) {
        imgRight.setVisibility(View.GONE);
        tvTitle.setText("文件夹详情");
        ivIcon.setBackgroundResource(FileIconUtils.getFileIcon(remoteFile));
        tvName.setText(infoBean.getData().getDIRCONTENT().getDirName());
        tvType.setText("类型：文件夹");
        tvSize.setText("大小：" + CapacityUtils.bytesToHumanReadable(Long.parseLong(infoBean.getFileSize())));
        tvLocation.setText("位置：" + infoBean.getData().getPARENTDIRNAME());
        tvCreateTime.setText("创建时间：" + TimeUtils.longToString(infoBean.getData().getDIRCONTENT().getCreateTime(),
                "yyyy-MM-dd HH:mm:ss"));
        tvUpdateTime.setText("上次修改：" + TimeUtils.longToString(infoBean.getData().getDIRCONTENT().getUpdateTime(),
                "yyyy-MM-dd HH:mm:ss"));
        tvGeographyLocation.setVisibility(View.GONE);
        tvWidthHeight.setVisibility(View.GONE);
    }

    /**
     * 设置文件数据
     */
    private void setFileData(ResponseFileInfoBean infoBean) {
        imgRight.setVisibility(View.GONE);
        tvTitle.setText("文件详情");
        FileIconUtils.getThumnailURL(this, remoteFile, ivIcon); //获取缩略图
        tvName.setText(infoBean.getData().getFileName());
        tvType.setText("类型：" + infoBean.getData().getFileType());
        tvSize.setText("大小：" + CapacityUtils.bytesToHumanReadable(Long.parseLong(infoBean.getData().getFileSize())));
        tvLocation.setText("位置：" + infoBean.getData().getDir());
        tvCreateTime.setText("创建时间：" + TimeUtils.longToString(Long.parseLong(infoBean.getData().getCreateTime()),
                "yyyy-MM-dd HH:mm:ss"));
        tvUpdateTime.setText("上次修改：" + TimeUtils.longToString(Long.parseLong(infoBean.getData().getUpdateTime()),
                "yyyy-MM-dd HH:mm:ss"));
        tvGeographyLocation.setVisibility(View.GONE);
        tvWidthHeight.setVisibility(View.GONE);
    }

    private void initListener() {
        llBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
