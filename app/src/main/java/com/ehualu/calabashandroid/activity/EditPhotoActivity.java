package com.ehualu.calabashandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.dialog.CreateFolderAndReNameDialog;
import com.ehualu.calabashandroid.interfaces.ConfirmCancelInterface;
import com.ehualu.calabashandroid.interfaces.BottomMoreInterface;
import com.ehualu.calabashandroid.interfaces.NormalDialogInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.popupwindow.BottomDeletePopupWindow;
import com.ehualu.calabashandroid.popupwindow.BottomMorePopupWindow;
import com.ehualu.calabashandroid.popupwindow.SharePopupWindow;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: houxiansheng
 * time：2019-12-6 14:17:54
 * describe：编辑图片
 */
public class EditPhotoActivity extends BaseActivity implements BottomMoreInterface, ConfirmCancelInterface {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        ButterKnife.bind(this);
        initViewAndData();
        initListener();
    }

    private void initViewAndData() {
        imgRight.setVisibility(View.GONE);
    }

    private void initListener() {
        llDownload.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llBackup.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        llMore.setOnClickListener(this);
        llBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_delete:
                new BottomDeletePopupWindow(this, this, null).showMoreOperationPopup(llDelete);
                break;
            case R.id.ll_more:
                new BottomMorePopupWindow(this, this, null, false).showMoreOperationPopup(llMore);
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_download:
                ToastUtil.showCenterHasImageToast(this, "文件已添加至传输列表");
                break;
            case R.id.ll_share:
                new SharePopupWindow(this, new RemoteFile()).showPopupWindow(llShare);
                break;
            case R.id.ll_backup:
                ToastUtil.showCenterHasImageToast(this, "文件已添加至葫芦备份");
                break;
        }
    }

    @Override
    public void onMove(List<RemoteFile> list) {
        startActivity(new Intent(this, MoveActivity.class));
    }

    @Override
    public void onRename(RemoteFile remoteFile) {
        new CreateFolderAndReNameDialog(this, "重命名", "文件名称", reNameInterface).show();
    }

    /**
     * 重命名
     */
    NormalDialogInterface reNameInterface = new NormalDialogInterface() {
        @Override
        public void onConfirm(String text) {

        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    public void onInfo(RemoteFile remoteFile) {
        startActivity(new Intent(this, FileInfoActivity.class));
    }

    @Override
    public void onCancel(List<RemoteFile> list) {
        ToastUtil.showCenterHasImageToast(this, "取消删除");
    }

    @Override
    public void onConfirm(List<RemoteFile> list) {
        ToastUtil.showCenterHasImageToast(this, "确定删除");
    }
}
