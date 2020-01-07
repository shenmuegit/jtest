package com.ehualu.calabashandroid.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.utils.Constants;
import com.ehualu.calabashandroid.widget.CameraSurfaceView;
import com.gyf.immersionbar.ImmersionBar;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TakePhotoActivity extends BaseActivity {

    @BindView(R.id.imgBack)
    ImageView imgBack;
    //    @BindView(R.id.cameraView)
    //    JCameraView cameraView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.iv_take_photo)
    ImageView ivTakePhoto;
    @BindView(R.id.iv_change_photo)
    ImageView ivChangePhoto;
    @BindView(R.id.camera_surface_view)
    CameraSurfaceView cameraSurfaceView;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.tvSelectAllOrNot)
    TextView tvSelectAllOrNot;
    private boolean isClick = true;

    private String dirID;//当前上传的目录

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        MyApp.needDestoryActs.add(this);
        ButterKnife.bind(this);
        tvTitle.setText("拍照");
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        initViewAndData();
        initListener();

        dirID = getIntent().getStringExtra("dirID");
    }

    private void initViewAndData() {

    }

    private void initListener() {
        ivTakePhoto.setOnClickListener(this);
        ivChangePhoto.setOnClickListener(this);
        llBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_take_photo:
                takePhoto();
                break;
            case R.id.llBack:
                finish();
                break;
            case R.id.iv_change_photo:
                cameraSurfaceView.changeCamera();
                break;
        }
    }

    public void takePhoto() {
        if (isClick) {
            isClick = false;
            cameraSurfaceView.takePicture(mShutterCallback, rawPictureCallback, jpegPictureCallback);
        }
    }

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            // 按下快门之后进行的操作
        }
    };

    private Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    private Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            cameraSurfaceView.stopPreview();
            saveFile(data);
            isClick = true;
        }
    };

    public void saveFile(byte[] data) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        FileOutputStream outputStream = null;
        try {
            File file = new File(Constants.CAMERA_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            outputStream = new FileOutputStream(Constants.CAMERA_PATH + File.separator + fileName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(TakePhotoActivity.this, PhotoPreviewUploadActivity.class);
        intent.putExtra("path", Constants.CAMERA_PATH + File.separator + fileName);
        intent.putExtra("dirID", dirID);
        startActivity(intent);
    }
}
