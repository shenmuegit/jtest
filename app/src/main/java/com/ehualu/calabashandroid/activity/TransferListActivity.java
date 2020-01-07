package com.ehualu.calabashandroid.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.db.entity.UploadEntity;
import com.ehualu.calabashandroid.db.manager.EntityManager;
import com.ehualu.calabashandroid.fragment.BackupExtractListFragment;
import com.ehualu.calabashandroid.fragment.DownloadListFragment;
import com.ehualu.calabashandroid.fragment.UploadListFragment;
import com.ehualu.calabashandroid.service.FileDownloader;
import com.ehualu.calabashandroid.service.FileUploader;
import com.ehualu.calabashandroid.utils.Constants;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-9 16:40:01
 * <p>
 * describe：传输列表
 */
public class TransferListActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.tv_download_list)
    TextView tvDownloadList;
    @BindView(R.id.tv_upload_list)
    TextView tvUploadList;
    @BindView(R.id.tv_backup_extract_list)
    TextView tvBackupExtractList;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    private DownloadListFragment downloadListFragment;
    private UploadListFragment uploadListFragment;
    private BackupExtractListFragment backupExtractListFragment;
    private FragmentManager fragmentManager;
    private PopupWindow popupWindow;

    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_list);
        ButterKnife.bind(this);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        initViewAndData();
        initListener();

        //开启下载服务
        startService(new Intent(this, FileDownloader.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(FileUploader.UPLOAD_COMPLETE);
        filter.addAction(Constants.UPLOAD_TABLE_INSERT);
        localBroadcastManager.registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        localBroadcastManager.unregisterReceiver(receiver);
    }

    private void initViewAndData() {
        tvTitle.setText("传输列表");
        imgRight.setImageDrawable(getResources().getDrawable(R.drawable.menu));
        fragmentManager = getSupportFragmentManager();
        setChoiceItem(0);
    }

    private void initListener() {
        tvDownloadList.setOnClickListener(this);
        tvUploadList.setOnClickListener(this);
        tvBackupExtractList.setOnClickListener(this);
        imgRight.setOnClickListener(this);
        llBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_download_list:
                setChoiceItem(0);
                break;
            case R.id.tv_upload_list:
                setChoiceItem(1);
                break;
            case R.id.tv_backup_extract_list:
                setChoiceItem(2);
                break;
            case R.id.img_right:
                showOperationPopup();
                break;
            case R.id.tv_clear_going:
                ToastUtil.showCenterForBusiness(this, "清除进行中的任务");
                closePopupwindow();
                break;
            case R.id.tv_clear_fail:
                ToastUtil.showCenterForBusiness(this, "重试失败任务");
                closePopupwindow();
                break;
            case R.id.tv_retry_fail:
                ToastUtil.showCenterForBusiness(this, "清除失败任务");
                closePopupwindow();
                break;
            case R.id.ll_back:
                finish();
                break;
        }

    }

    /**
     * Fragment切换
     */
    private void setChoiceItem(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        clearChoice();
        hideFragment(transaction);
        switch (index) {
            case 0:
                tvDownloadList.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                tvDownloadList.setTextColor(Color.parseColor("#ff030303"));
                if (downloadListFragment == null) {
                    downloadListFragment = new DownloadListFragment();
                    transaction.add(R.id.framelayout, downloadListFragment);
                } else {
                    transaction.show(downloadListFragment);
                }
                break;

            case 1:
                tvUploadList.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                tvUploadList.setTextColor(Color.parseColor("#ff030303"));
                if (uploadListFragment == null) {
                    uploadListFragment = new UploadListFragment();
                    transaction.add(R.id.framelayout, uploadListFragment);
                }
                transaction.show(uploadListFragment);
                break;

            case 2:
                tvBackupExtractList.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                tvBackupExtractList.setTextColor(Color.parseColor("#ff030303"));
                if (backupExtractListFragment == null) {
                    backupExtractListFragment = new BackupExtractListFragment();
                    transaction.add(R.id.framelayout, backupExtractListFragment);
                }
                transaction.show(backupExtractListFragment);
        }
        transaction.commit();
    }

    /**
     * 清除选中Fragment的样式
     */
    public void clearChoice() {
        tvDownloadList.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//取消加粗
        tvUploadList.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//取消加粗
        tvBackupExtractList.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//取消加粗
        tvDownloadList.setTextColor(Color.parseColor("#ff8a8a8d"));
        tvUploadList.setTextColor(Color.parseColor("#ff8a8a8d"));
        tvBackupExtractList.setTextColor(Color.parseColor("#ff8a8a8d"));
    }

    /**
     * 用于隐藏fragment
     */
    private void hideFragment(FragmentTransaction ft) {
        if (downloadListFragment != null) {
            ft.hide(downloadListFragment);
        }
        if (uploadListFragment != null) {
            ft.hide(uploadListFragment);
        }
        if (backupExtractListFragment != null) {
            ft.hide(backupExtractListFragment);
        }
    }

    /**
     * 显示操作的底部弹窗
     */
    private void showOperationPopup() {
        Window window = getWindow();
        View contentView = View.inflate(this, R.layout.popup_transfer_list_edit, null);

        TextView clearGoing = contentView.findViewById(R.id.tv_clear_going);
        TextView clearFail = contentView.findViewById(R.id.tv_clear_fail);
        TextView retryFail = contentView.findViewById(R.id.tv_retry_fail);

        clearGoing.setOnClickListener(this);
        clearFail.setOnClickListener(this);
        retryFail.setOnClickListener(this);

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp2 = window.getAttributes();
                lp2.alpha = 1f;
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setAttributes(lp2);
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(imgRight, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
    }

    /**
     * 关闭底部弹窗
     */
    private void closePopupwindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case FileUploader.UPLOAD_COMPLETE:
                    String taskId = intent.getStringExtra("taskId");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("传完了", "开始刷新界面");
                            Fragment fragment = getVisibleFragment();
                            if (fragment instanceof UploadListFragment) {
                                ((UploadListFragment) fragment).refresh();
                            }
                        }
                    });
                case Constants
                        .UPLOAD_TABLE_INSERT:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("添加新任务", "添加新任务");
                            Fragment fragment = getVisibleFragment();
                            if (fragment instanceof UploadListFragment) {
                                ((UploadListFragment) fragment).refresh();
                            }
                        }
                    });
                    break;
            }
        }
    };

}
