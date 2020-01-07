package com.ehualu.calabashandroid.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.dialog.CreateFolderAndReNameDialog;
import com.ehualu.calabashandroid.interfaces.NormalDialogInterface;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 选择上传文件类型界面
 */
public class UploadFileTypeActivity extends BaseActivity {

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.llTakePhoto)
    LinearLayout llTakePhoto;
    @BindView(R.id.llPhoto)
    LinearLayout llPhoto;
    @BindView(R.id.llVideo)
    LinearLayout llVideo;
    @BindView(R.id.llFile)
    LinearLayout llFile;
    @BindView(R.id.llFolder)
    LinearLayout llFolder;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSelectAllOrNot)
    TextView tvSelectAllOrNot;

    public static final int START_UPLOAD_FILE_TYPE_ACTIVITY = 101;

    private RxPermissions rxPermissions;

    private String dirID = "0";//用户跳转过来的当前目录

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file_type);
        ButterKnife.bind(this);

        MyApp.needDestoryActs.add(this);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        rxPermissions = new RxPermissions(this);
        dirID = getIntent().getStringExtra("dirID");

        setListener();
    }

    private void setListener() {
        llTakePhoto.setOnClickListener(this);
        llPhoto.setOnClickListener(this);
        llVideo.setOnClickListener(this);
        llFile.setOnClickListener(this);
        llFolder.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        llBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.llTakePhoto:
                rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Intent intent = new Intent(UploadFileTypeActivity.this, TakePhotoActivity.class);
                            intent.putExtra("dirID", dirID);
                            startActivity(intent);
                        } else {
                            ToastUtil.showCenterForBusiness(UploadFileTypeActivity.this, "请先允许拍照权限！");
                        }
                    }
                });
                break;
            case R.id.llPhoto:
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Intent intent = new Intent(UploadFileTypeActivity.this, AlbumListActivity.class);
                            intent.putExtra("dirID", dirID);
                            startActivity(intent);
                        } else {
                            ToastUtil.showCenterForBusiness(UploadFileTypeActivity.this, "请先允许存储权限！");
                        }
                    }
                });
                break;
            case R.id.llVideo:
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Intent intent = new Intent(UploadFileTypeActivity.this, SelectVideoActivity.class);
                            intent.putExtra("dirID", dirID);
                            startActivity(intent);
                        } else {
                            ToastUtil.showCenterForBusiness(UploadFileTypeActivity.this, "请先允许存储权限！");
                        }
                    }
                });
                break;
            case R.id.llFile:
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Intent intent = new Intent(UploadFileTypeActivity.this, SelectDocumentActivity.class);
                            startActivity(intent);
                        } else {
                            ToastUtil.showCenterForBusiness(UploadFileTypeActivity.this, "请先允许存储权限！");
                        }
                    }
                });
                break;
            case R.id.llFolder:
                //关闭当前界面，在上一界面弹出新建文件夹
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.llBack:
                finish();
                break;
        }
    }

}
