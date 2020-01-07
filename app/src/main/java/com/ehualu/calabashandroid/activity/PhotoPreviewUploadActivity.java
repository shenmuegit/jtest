package com.ehualu.calabashandroid.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.utils.FileUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ehualu.calabashandroid.base.BaseFragment.REQUEST_TARGET_PATH;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-23 09:35:54
 * <p>
 * describe：照片预览上传
 */
public class PhotoPreviewUploadActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_upload_location)
    TextView tvUploadLocation;
    @BindView(R.id.ll_upload_location)
    LinearLayout llUploadLocation;
    @BindView(R.id.tv_upload_cofirm)
    TextView tvUploadCofirm;

    private String dirID;
    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview_upload);
        MyApp.needDestoryActs.add(this);
        ButterKnife.bind(this);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        initViewAndData();
        initListener();

        dirID = getIntent().getStringExtra("dirID");
        path = getIntent().getStringExtra("path");
    }

    private void initViewAndData() {
        tvTitle.setText("返回");
        String path = getIntent().getStringExtra("path");
        byte[] bytes = FileUtils.fileConvertToByteArray(new File(path));
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ivPhoto.setImageBitmap(FileUtils.rotaingImageView(FileUtils.readPictureDegree(path), bitmap));
    }

    private void initListener() {
        llBack.setOnClickListener(this);
        tvUploadLocation.setOnClickListener(this);
        tvUploadCofirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_upload_location:
                Intent intent = new Intent(PhotoPreviewUploadActivity.this, SelectUploadPathActivity.class);
                startActivityForResult(intent, REQUEST_TARGET_PATH);
                break;
            case R.id.tv_upload_cofirm:
                File file = new File(path);
                if (file.exists()) {
                    startUpload(file, dirID);
                }
                MyApp.closeAllNeedDestoryActs();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TARGET_PATH) {
            if (resultCode == RESULT_OK) {
                dirID = data.getStringExtra("uploadPath");
            }
        }
    }
}
