package com.ehualu.calabashandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.fragment.AllVideoFragment;
import com.ehualu.calabashandroid.fragment.UnUploadVideoFragment;
import com.ehualu.calabashandroid.fragment.UploadedFragment;
import com.ehualu.calabashandroid.model.Video;
import com.ehualu.calabashandroid.model.VideoList;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectVideoActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSelectAllOrNot)
    public TextView tvSelectAllOrNot;
    @BindView(R.id.rbUnUploaded)
    RadioButton rbUnUploaded;
    @BindView(R.id.rbUploaded)
    RadioButton rbUploaded;
    @BindView(R.id.rbAll)
    RadioButton rbAll;
    @BindView(R.id.rgVideo)
    RadioGroup rgVideo;
    @BindView(R.id.videoContainer)
    FrameLayout videoContainer;
    @BindView(R.id.llBack)
    LinearLayout llBack;

    private BaseFragment mContent;
    private int position = 0;
    private List<BaseFragment> fragments = new ArrayList<>();
    private UnUploadVideoFragment unUploadVideoFragment;
    private UploadedFragment uploadedFragment;
    private AllVideoFragment allVideoFragment;

    public String dirID;

    public void refreshSize(int unSize, int edSize, int allSize) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rbAll.setText("全部（" + allSize + "）");
                rbUnUploaded.setText("未上传（" + unSize + "）");
                rbUploaded.setText("已上传（" + edSize + "）");
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video);
        ButterKnife.bind(this);

        MyApp.needDestoryActs.add(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvSelectAllOrNot.setVisibility(View.VISIBLE);
        tvTitle.setText("选择视频");
        dirID = getIntent().getStringExtra("dirID");
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        unUploadVideoFragment = new UnUploadVideoFragment();
        uploadedFragment = new UploadedFragment();
        allVideoFragment = new AllVideoFragment();

        fragments.add(unUploadVideoFragment);
        fragments.add(uploadedFragment);
        fragments.add(allVideoFragment);

        tvSelectAllOrNot.setOnClickListener(this);
        rgVideo.setOnCheckedChangeListener(this);
        llBack.setOnClickListener(this);
        rgVideo.check(R.id.rbUnUploaded);
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
                    ft.add(R.id.videoContainer, to).commit();
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
            case R.id.rbUnUploaded:
                position = 0;
                tvSelectAllOrNot.setVisibility(View.VISIBLE);
                break;
            case R.id.rbUploaded:
                position = 1;
                tvSelectAllOrNot.setVisibility(View.VISIBLE);
                break;
            case R.id.rbAll:
                position = 2;
                tvSelectAllOrNot.setVisibility(View.VISIBLE);
                break;
        }
        BaseFragment to = fragments.get(position);
        switchFragment(mContent, to);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.tvSelectAllOrNot:
                selectedAllOrNot();
                break;
        }
    }

    public void selectedAllOrNot() {
        int size = 0;
        Fragment fragment = getVisibleFragment();
        if (fragment instanceof UnUploadVideoFragment) {
            size = unUploadVideoFragment.adapter.getFileCount();

            if (unUploadVideoFragment.adapter.getCheckedList().size() < size) {
                unUploadVideoFragment.adapter.removeAllCheckedList();
                List<VideoList> vls = unUploadVideoFragment.adapter.getData();
                for (int i = 0; i < vls.size(); i++) {
                    VideoList vl = vls.get(i);
                    for (int j = 0; j < vl.getVideos().size(); j++) {
                        Video v = vl.getVideos().get(j);
                        unUploadVideoFragment.adapter.addCheckList(v);
                    }
                }
                unUploadVideoFragment.adapter.notifyDataSetChanged();
            } else {
                unUploadVideoFragment.adapter.removeAllCheckedList();
                unUploadVideoFragment.adapter.notifyDataSetChanged();
            }
        } else if (fragment instanceof UploadedFragment) {
            size = uploadedFragment.adapter.getFileCount();

            if (uploadedFragment.adapter.getCheckedList().size() < size) {
                uploadedFragment.adapter.removeAllCheckedList();
                List<VideoList> vls = uploadedFragment.adapter.getData();
                for (int i = 0; i < vls.size(); i++) {
                    VideoList vl = vls.get(i);
                    for (int j = 0; j < vl.getVideos().size(); j++) {
                        Video v = vl.getVideos().get(j);
                        uploadedFragment.adapter.addCheckList(v);
                    }
                }
                uploadedFragment.adapter.notifyDataSetChanged();
            } else {
                uploadedFragment.adapter.removeAllCheckedList();
                uploadedFragment.adapter.notifyDataSetChanged();
            }
        } else if (fragment instanceof AllVideoFragment) {
            size = allVideoFragment.adapter.getFileCount();

            if (allVideoFragment.adapter.getCheckedList().size() < size) {
                allVideoFragment.adapter.removeAllCheckedList();
                List<VideoList> vls = allVideoFragment.adapter.getData();
                for (int i = 0; i < vls.size(); i++) {
                    VideoList vl = vls.get(i);
                    for (int j = 0; j < vl.getVideos().size(); j++) {
                        Video v = vl.getVideos().get(j);
                        allVideoFragment.adapter.addCheckList(v);
                    }
                }
                allVideoFragment.adapter.notifyDataSetChanged();
            } else {
                allVideoFragment.adapter.removeAllCheckedList();
                allVideoFragment.adapter.notifyDataSetChanged();
            }
        }
    }
}
