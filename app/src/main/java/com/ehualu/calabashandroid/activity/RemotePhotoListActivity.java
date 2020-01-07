package com.ehualu.calabashandroid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.dialog.CreateFolderAndReNameDialog;
import com.ehualu.calabashandroid.fragment.AlbumAutoSortFragment;
import com.ehualu.calabashandroid.fragment.AlbumSortByTimeFragment;
import com.ehualu.calabashandroid.interfaces.BottomMoreInterface;
import com.ehualu.calabashandroid.interfaces.ConfirmCancelInterface;
import com.ehualu.calabashandroid.interfaces.NormalDialogInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.model.RemoteFileListSortByTime;
import com.ehualu.calabashandroid.popupwindow.BottomDeletePopupWindow;
import com.ehualu.calabashandroid.popupwindow.BottomMorePopupWindow;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;
import com.ehualu.calabashandroid.utils.BroadcastUtils;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.OperationUtils;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 首页fragment点击进入照片列表
 */
public class RemotePhotoListActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        BottomMoreInterface {


    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvFile)
    TextView tvFile;
    @BindView(R.id.ll_top_title)
    public LinearLayout llTopTitle;
    @BindView(R.id.tv_select_cancel)
    public TextView tvSelectCancel;
    @BindView(R.id.tv_select_num)
    public TextView tvSelectNum;
    @BindView(R.id.tv_select_all_or_not)
    public TextView tvSelectAllOrNot;
    @BindView(R.id.ll_top_select)
    public LinearLayout llTopSelect;
    @BindView(R.id.viewLine)
    View viewLine;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.ivFileEdit)
    public ImageView ivFileEdit;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.rbTimeSort)
    RadioButton rbTimeSort;
    @BindView(R.id.rbAutoSort)
    RadioButton rbAutoSort;
    @BindView(R.id.rgAlbum)
    RadioGroup rgAlbum;
    @BindView(R.id.albumContainer)
    FrameLayout albumContainer;
    @BindView(R.id.ll_download)
    public LinearLayout llDownload;
    @BindView(R.id.ll_share)
    public LinearLayout llShare;
    @BindView(R.id.ll_backup)
    LinearLayout llBackup;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.ll_bottom_edit)
    public LinearLayout llBottomEdit;
    @BindView(R.id.iv_share)
    public ImageView ivShare;
    @BindView(R.id.tv_share)
    public TextView tvShare;
    @BindView(R.id.iv_download)
    public ImageView ivDownload;
    @BindView(R.id.tv_download)
    public TextView tvDownload;
    private List<BaseFragment> fragments = new ArrayList<>();
    private AlbumSortByTimeFragment albumSortByTimeFragment;
    private AlbumAutoSortFragment albumAutoSortFragment;
    private BaseFragment mContent;
    private int position = 0;
    private PopupWindow fileEditPopup;
    private int requestCodeMove = 1;//移动

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_photo_list);
        ButterKnife.bind(this);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        albumSortByTimeFragment = new AlbumSortByTimeFragment();
        albumAutoSortFragment = new AlbumAutoSortFragment();
        fragments.add(albumSortByTimeFragment);
        fragments.add(albumAutoSortFragment);
        rgAlbum.setOnCheckedChangeListener(this);
        tvSearch.setOnClickListener(this);
        rgAlbum.check(R.id.rbTimeSort);
        ivBack.setOnClickListener(this);
        tvFile.setText("相册");
        setListener();
        ivDownload.setBackgroundResource(R.drawable.icon_bottom_download);
    }

    private void setListener() {
        ivFileEdit.setOnClickListener(this);
        tvSelectCancel.setOnClickListener(this);
        tvSelectAllOrNot.setOnClickListener(this);
        llMore.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llBackup.setOnClickListener(this);
        llDownload.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvFile.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbTimeSort:
                position = 0;
                ivFileEdit.setImageResource(R.mipmap.frg_file_edit);
                ivFileEdit.setClickable(true);
                break;
            case R.id.rbAutoSort:
                position = 1;
                ivFileEdit.setImageResource(R.mipmap.frg_file_edit_disabled);
                ivFileEdit.setClickable(false);
                break;
        }
        BaseFragment to = fragments.get(position);
        switchFragment(mContent, to);
    }

    private void switchFragment(BaseFragment from, BaseFragment to) {
        if (getVisibleFragment() instanceof AlbumSortByTimeFragment) {//切换时去掉选择模式
            if (((AlbumSortByTimeFragment) getVisibleFragment()).isSelectMode) {
                isSelectStatus(false);
            }
        }

        if (from != to) {
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    ft.hide(from);
                }
                if (to != null) {
                    ft.add(R.id.albumContainer, to).commit();
                }
            } else {
                if (from != null) {
                    ft.hide(from);
                }
                if (to != null) {
                    ft.show(to).commit();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_select_cancel:
                isSelectStatus(false);
                break;

            case R.id.tv_select_all_or_not:
                SelectedAllOrNot();
                break;

            case R.id.tvSearch:
                startActivity(new Intent(RemotePhotoListActivity.this, CommonSearchActivity.class));
                break;

            case R.id.ivBack:
            case R.id.tvFile:
                finish();
                break;

            case R.id.ivFileEdit:
                showOperationPopup();
                break;

            case R.id.rlSelectFile:
                isSelectStatus(true);
                //隐藏底部弹出框
                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                break;

            case R.id.rlStandardMode:
                if (!albumSortByTimeFragment.getIsBitmapMode()) {
                    return;
                }
                albumSortByTimeFragment.switchStandard();

                //隐藏底部弹出框
                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                break;

            case R.id.rlBigBitmapMode:
                if (albumSortByTimeFragment.getIsBitmapMode()) {
                    return;
                }
                albumSortByTimeFragment.switchBigBitmap();

                //隐藏底部弹出框
                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                break;

            case R.id.ll_more:
                if (albumSortByTimeFragment.adapter.getCheckedList().size() > 0) {
                    new BottomMorePopupWindow(this, this, albumSortByTimeFragment.adapter.getCheckedList(), false).showMoreOperationPopup(llMore);
                } else {
                    ToastUtil.showCenterHasImageToast(this, "请先选择文件！");
                }
                break;

            case R.id.ll_backup:
                if (albumSortByTimeFragment.adapter.getCheckedList().size() > 0) {
                    albumSortByTimeFragment.backup();
                } else {
                    ToastUtil.showCenterHasImageToast(this, "请先选择文件！");
                }
                break;

            case R.id.ll_download:
                if (albumSortByTimeFragment.adapter.getCheckedList().size() > 0) {
                    for (Object file : albumSortByTimeFragment.adapter.getCheckedList()) {
                        BroadcastUtils.sendDownloadBroadcast((RemoteFile) file);
                    }
                    ToastUtil.showCenterHasImageToast(this, "文件已添加至传输列表");
                    isSelectStatus(false);
                } else {
                    ToastUtil.showCenterHasImageToast(this, "请先选择文件！");
                }
                break;

            case R.id.ll_delete:
                if (albumSortByTimeFragment.adapter.getCheckedList().size() > 0) {
                    new BottomDeletePopupWindow(this, deleteInterface,
                            albumSortByTimeFragment.adapter.getCheckedList()).showMoreOperationPopup(llDelete);
                } else {
                    ToastUtil.showCenterHasImageToast(this, "请先选择文件！");
                }
                break;
        }
    }

    int sum = 0;
    /**
     * 删除操作
     */
    ConfirmCancelInterface deleteInterface = new ConfirmCancelInterface() {
        @Override
        public void onCancel(List<RemoteFile> list) {

        }

        @Override
        public void onConfirm(List<RemoteFile> list) {
            OperationUtils.deleteRemoteFile(RemotePhotoListActivity.this, list);
        }
    };

    public void showOperationPopup() {
        Window window = getWindow();
        View contentView = View.inflate(this, R.layout.popup_bottom_album_edit, null);

        RelativeLayout rlSelectFile = contentView.findViewById(R.id.rlSelectFile);
        RelativeLayout rlStandardMode = contentView.findViewById(R.id.rlStandardMode);
        RelativeLayout rlBigBitmapMode = contentView.findViewById(R.id.rlBigBitmapMode);

        TextView tvBigBitmapMode = contentView.findViewById(R.id.tv_big_bitmap_mode);
        ImageView ivBigBitmapMode = contentView.findViewById(R.id.iv_big_bitmap_mode);
        TextView tvStandardMode = contentView.findViewById(R.id.tv_standard_mode);
        ImageView ivStandardMode = contentView.findViewById(R.id.iv_standard_mode);

        if (albumSortByTimeFragment.getIsBitmapMode()) {
            setTextViewSelected(tvBigBitmapMode, ivBigBitmapMode);
        } else {
            setTextViewSelected(tvStandardMode, ivStandardMode);
        }

        rlSelectFile.setOnClickListener(this);
        rlStandardMode.setOnClickListener(this);
        rlBigBitmapMode.setOnClickListener(this);

        fileEditPopup = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
        fileEditPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp2 = window.getAttributes();
                lp2.alpha = 1f;
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setAttributes(lp2);
            }
        });
        fileEditPopup.setOutsideTouchable(true);
        fileEditPopup.setFocusable(true);
        fileEditPopup.showAtLocation(ivFileEdit, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
    }

    public void setTextViewSelected(TextView tv, ImageView duigou) {
        Drawable leftDrawable = null;
        switch (tv.getId()) {
            case R.id.tv_big_bitmap_mode:
                leftDrawable = getResources().getDrawable(R.drawable.icon_big_mode_blue);
                break;
            case R.id.tv_standard_mode:
                leftDrawable = getResources().getDrawable(R.drawable.icon_standard_mode_blue);
                break;
        }
        leftDrawable.setBounds(0, 0, DensityUtil.dip2px(22, this), DensityUtil.dip2px(22, this));
        tv.setCompoundDrawables(leftDrawable, null, null, null);
        tv.setTextColor(Color.parseColor("#5BA0E9"));
        duigou.setVisibility(View.VISIBLE);
    }

    public void setSelectMode(boolean b) {
        //filefragment的属性
        albumSortByTimeFragment.isSelectMode = b;
        isSelectMode = b;
        if (b) {
            albumSortByTimeFragment.adapter.setSelectMode(true);
            //将搜索框后面的编辑图标置灰
            ivFileEdit.setImageResource(R.mipmap.frg_file_edit_disabled);
            ivFileEdit.setClickable(false);
        } else {
            albumSortByTimeFragment.adapter.setSelectMode(false);
            ivFileEdit.setImageResource(R.mipmap.frg_file_edit);
            ivFileEdit.setClickable(true);
        }
    }

    /**
     * add by houxiansheng 2019-12-12 11:58:43 是否是选择状态
     */
    public void isSelectStatus(boolean isSelectStatus) {
        if (isSelectStatus) {
            llTopTitle.setVisibility(View.GONE);
            llTopSelect.setVisibility(View.VISIBLE);
            llBottomEdit.setVisibility(View.VISIBLE);
            albumSortByTimeFragment.adapter.setSelectMode(true);
        } else {
            llTopTitle.setVisibility(View.VISIBLE);
            llTopSelect.setVisibility(View.GONE);
            albumSortByTimeFragment.adapter.removeAllCheckedList();
            llBottomEdit.setVisibility(View.GONE);
            albumSortByTimeFragment.adapter.setSelectMode(false);
        }
        setSelectMode(isSelectStatus);
    }

    /**
     * add by houxiansheng 2019-12-12 15:36:08 全选、全不选
     */
    public void SelectedAllOrNot() {
        if (albumSortByTimeFragment.adapter.getCheckedList().size() < albumSortByTimeFragment.adapter.getFileNumber()) {
            albumSortByTimeFragment.adapter.removeAllCheckedList();
            List<RemoteFileListSortByTime> list = albumSortByTimeFragment.adapter.getData();
            for (int i = 0; i < list.size(); i++) {
                RemoteFileListSortByTime remoteFileListSortByTime = list.get(i);
                for (int j = 0; j < remoteFileListSortByTime.getList().size(); j++) {
                    RemoteFile ocFile = remoteFileListSortByTime.getList().get(j);
                    albumSortByTimeFragment.adapter.addCheckList(ocFile);
                }
            }
            albumSortByTimeFragment.adapter.notifyDataSetChanged();
        } else {
            albumSortByTimeFragment.adapter.removeAllCheckedList();
            albumSortByTimeFragment.adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getVisibleFragment();
        if (fragment instanceof AlbumSortByTimeFragment) {
            if (((AlbumSortByTimeFragment) fragment).isSelectMode) {
                isSelectStatus(false);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMove(List<RemoteFile> list) {
        Intent intent = new Intent();
        intent.putExtra("moveList", (Serializable) albumSortByTimeFragment.adapter.getCheckedList());
        intent.setClass(this, MoveActivity.class);
        startActivityForResult(intent, requestCodeMove);
        isSelectStatus(false);
    }

    @Override
    public void onRename(RemoteFile remoteFile) {
        new CreateFolderAndReNameDialog(this, "重命名", remoteFile.getFileName(), new NormalDialogInterface() {
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
                                    albumSortByTimeFragment.getFileList();
                                } else {
                                    ToastUtil.showCenterHasImageToast(RemotePhotoListActivity.this,
                                            responseBean.getMessage());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showCenterForBusiness(RemotePhotoListActivity.this, "重命名失败！");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }

            @Override
            public void onCancel() {

            }
        }).show();
    }

    @Override
    public void onInfo(RemoteFile remoteFile) {
        Intent intent = new Intent();
        intent.setClass(this, FileInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("remoteFile", remoteFile);
        intent.putExtras(bundle);
        startActivity(intent);
        isSelectStatus(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == requestCodeMove && resultCode == MoveActivity.resultCodeMove) {//移动操作后刷新界面
            if (getVisibleFragment() instanceof AlbumSortByTimeFragment) {
                albumSortByTimeFragment.getFileList();
            }
        }
    }
}
