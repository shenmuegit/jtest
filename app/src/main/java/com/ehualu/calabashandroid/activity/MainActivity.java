package com.ehualu.calabashandroid.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.fragment.FileFragment;
import com.ehualu.calabashandroid.fragment.HomeFragment;
import com.ehualu.calabashandroid.fragment.MessageFragment;
import com.ehualu.calabashandroid.fragment.MineFragment;
import com.ehualu.calabashandroid.service.FileDownloader;
import com.ehualu.calabashandroid.service.FileUploader;
import com.ehualu.calabashandroid.upload.ProgressRequestBody;
import com.ehualu.calabashandroid.utils.Constants;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.ehualu.calabashandroid.widget.DrawableCenterRadioButton;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.actMainRbHome)
    DrawableCenterRadioButton actMainRbHome;
    @BindView(R.id.actMainRbStorage)
    DrawableCenterRadioButton actMainRbStorage;
    @BindView(R.id.actMainRbMsg)
    DrawableCenterRadioButton actMainRbMsg;
    @BindView(R.id.actMainRbMine)
    DrawableCenterRadioButton actMainRbMine;
    @BindView(R.id.actMainRg)
    RadioGroup actMainRg;
    @BindView(R.id.actMainBg)
    ImageView actMainBg;
    @BindView(R.id.actMainContainer)
    LinearLayout actMainContainer;

    private LocalBroadcastManager localBroadcastManager;

    private List<RadioButton> rbs = new ArrayList<>();
    private List<String> tabNames = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private HomeFragment homeFragment;
    public FileFragment storageFragment;
    private MessageFragment messageFragment;
    private MineFragment mineFragment;
    private int position = 0;
    private BaseFragment mContent;
    private RxPermissions rxPermissions = new RxPermissions(this);

    public static List<ProgressRequestBody> requestBodies = Collections.synchronizedList(new ArrayList<>());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    Log.e("pathpath", path);
                }
            }
        });

        initView();
        initFragment();
        setListener();

        //开启下载服务
        startService(new Intent(this, FileDownloader.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(FileUploader.UPLOAD_COMPLETE);
        filter.addAction(Constants.DELETE_COMPLETE);
        localBroadcastManager.registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        localBroadcastManager.unregisterReceiver(receiver);
    }

    private void initView() {
        tabNames.add("首页");
        tabNames.add("文件");
        tabNames.add("消息");
        tabNames.add("我的");

        rbs.add(actMainRbHome);
        rbs.add(actMainRbStorage);
        rbs.add(actMainRbMsg);
        rbs.add(actMainRbMine);
    }

    private void initFragment() {
        homeFragment = new HomeFragment();
        storageFragment = new FileFragment();
        messageFragment = new MessageFragment();
        mineFragment = new MineFragment();

        fragments.add(homeFragment);
        fragments.add(storageFragment);
        fragments.add(messageFragment);
        fragments.add(mineFragment);
    }

    private void setListener() {
        actMainRg.setOnCheckedChangeListener(this);
        actMainRg.check(R.id.actMainRbHome);
    }

    public void switchFileFragment() {
        actMainRg.check(R.id.actMainRbStorage);
    }

    private void setTextShowOrHide(int position) {
        for (int i = 0; i < rbs.size(); i++) {
            if (i == position) {
                rbs.get(i).setText(tabNames.get(i));
            } else {
                rbs.get(i).setText("");
            }
        }
    }

    private void switchFragment(BaseFragment from, BaseFragment to) {
        if (from != to) {
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    ft.hide(from);
                }
                if (to != null) {
                    ft.add(R.id.actMainContainer, to).commit();
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.actMainRbHome:
                position = 0;
                break;
            case R.id.actMainRbStorage:
                position = 1;
                break;
            case R.id.actMainRbMsg:
                position = 2;
                break;
            case R.id.actMainRbMine:
                position = 3;
                break;
        }
        if (position == 0) {
            actMainBg.setVisibility(View.VISIBLE);
        } else {
            //actMainBg.setVisibility(View.GONE);
        }
        setTextShowOrHide(position);
        BaseFragment to = fragments.get(position);
        switchFragment(mContent, to);
    }

    /**
     * 获取整个背景图
     *
     * @return
     */
    public View getMainBg() {
        return actMainBg;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private long last_back_time = 0;

    @Override
    public void onBackPressed() {
        if (getVisibleFragment() instanceof FileFragment) {//在文件页面
            if (storageFragment.getSelectMode()) {//如果是选择模式，去掉选择模式
                storageFragment.isSelectStatus(false);
            } else {//不是选择模式
                if (storageFragment.isRootDirectory()) {//如果根目录，走退出逻辑
                    long current_time = System.currentTimeMillis();
                    if (current_time - last_back_time > 2000) {
                        ToastUtil.showCenterNoImageToast(this, "再按一次退出");
                        last_back_time = current_time;
                    } else {
                        finish();
                    }
                } else {//不是根目录，回到上级目录
                    storageFragment.goPrevious();
                }
            }
        } else { //保证所有fragment都可以正常返回
            long current_time = System.currentTimeMillis();
            if (current_time - last_back_time > 2000) {
                ToastUtil.showCenterNoImageToast(this, "再按一次退出");
                last_back_time = current_time;
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * add by houxiansheng 2019-12-12 11:11:34 设置底部按钮是否显示
     */
    public void setMenuShowStatus(boolean showMenu) {
        if (showMenu) {
            actMainRg.setVisibility(View.VISIBLE);
        } else {
            actMainRg.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case FileUploader.UPLOAD_COMPLETE:
                case Constants.DELETE_COMPLETE:
                    String dirID = intent.getStringExtra("dirID");
                    Fragment fragment = getVisibleFragment();
                    if (fragment instanceof FileFragment) {
                        ((FileFragment) fragment).refreshCurrentDir(dirID);
                    }
                    break;
            }
        }
    };
}
