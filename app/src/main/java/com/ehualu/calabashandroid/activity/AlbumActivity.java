package com.ehualu.calabashandroid.activity;

import android.os.Bundle;
import android.view.View;
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
import com.ehualu.calabashandroid.fragment.AllPhotoFragment;
import com.ehualu.calabashandroid.fragment.UnUploadPhotoFragment;
import com.ehualu.calabashandroid.fragment.UnUploadVideoFragment;
import com.ehualu.calabashandroid.fragment.UploadedPhotoFragment;
import com.ehualu.calabashandroid.model.Video;
import com.ehualu.calabashandroid.model.VideoList;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 某一相册的界面
 */
public class AlbumActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSelectAllOrNot)
    public TextView tvSelectAllOrNot;
    @BindView(R.id.rbUploaded)
    RadioButton rbUploaded;
    @BindView(R.id.rbUnUploaded)
    RadioButton rbUnUploaded;
    @BindView(R.id.rbAll)
    RadioButton rbAll;
    @BindView(R.id.rgAlbum)
    RadioGroup rgAlbum;
    @BindView(R.id.photoContainer)
    FrameLayout photoContainer;
    @BindView(R.id.llBack)
    LinearLayout llBack;

    private List<BaseFragment> fragments = new ArrayList<>();
    private BaseFragment mContent;
    private UnUploadPhotoFragment unUploadPhotoFragment;
    private UploadedPhotoFragment uploadedPhotoFragment;
    private AllPhotoFragment allPhotoFragment;
    private int position = 0;
    private int buck_id;
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
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("选择照片");

        llBack.setOnClickListener(this);

        MyApp.needDestoryActs.add(this);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        dirID = getIntent().getStringExtra("dirID");
        buck_id = getIntent().getExtras().getInt("buck_id");

        tvSelectAllOrNot.setVisibility(View.VISIBLE);
        tvSelectAllOrNot.setOnClickListener(this);

        unUploadPhotoFragment = new UnUploadPhotoFragment(buck_id);
        uploadedPhotoFragment = new UploadedPhotoFragment(buck_id);
        allPhotoFragment = new AllPhotoFragment(buck_id);
        fragments.add(unUploadPhotoFragment);
        fragments.add(uploadedPhotoFragment);
        fragments.add(allPhotoFragment);
        rgAlbum.setOnCheckedChangeListener(this);
        rgAlbum.check(R.id.rbUnUploaded);
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

    private void switchFragment(BaseFragment from, BaseFragment to) {
        if (from != to) {
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    ft.hide(from);
                }
                if (to != null) {
                    ft.add(R.id.photoContainer, to).commit();
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

    public void selectedAllOrNot() {
        int size = 0;
        Fragment fragment = getVisibleFragment();
        if (fragment instanceof UnUploadPhotoFragment) {
            size = unUploadPhotoFragment.adapter.getData().size();

            if (unUploadPhotoFragment.adapter.getCheckedList().size() < size) {
                unUploadPhotoFragment.adapter.removeAllCheckedList();
                List<String> vs = unUploadPhotoFragment.adapter.getData();
                for (int i = 0; i < vs.size(); i++) {
                    String s = vs.get(i);
                    unUploadPhotoFragment.adapter.addCheckList(s);
                }
                unUploadPhotoFragment.adapter.notifyDataSetChanged();
            } else {
                unUploadPhotoFragment.adapter.removeAllCheckedList();
                unUploadPhotoFragment.adapter.notifyDataSetChanged();
            }
        } else if (fragment instanceof UploadedPhotoFragment) {
            size = uploadedPhotoFragment.adapter.getData().size();

            if (uploadedPhotoFragment.adapter.getCheckedList().size() < size) {
                uploadedPhotoFragment.adapter.removeAllCheckedList();
                List<String> vs = uploadedPhotoFragment.adapter.getData();
                for (int i = 0; i < vs.size(); i++) {
                    String s = vs.get(i);
                    uploadedPhotoFragment.adapter.addCheckList(s);
                }
                uploadedPhotoFragment.adapter.notifyDataSetChanged();
            } else {
                uploadedPhotoFragment.adapter.removeAllCheckedList();
                uploadedPhotoFragment.adapter.notifyDataSetChanged();
            }
        } else if (fragment instanceof AllPhotoFragment) {
            size = allPhotoFragment.adapter.getData().size();

            if (allPhotoFragment.adapter.getCheckedList().size() < size) {
                allPhotoFragment.adapter.removeAllCheckedList();
                List<String> vs = allPhotoFragment.adapter.getData();
                for (int i = 0; i < vs.size(); i++) {
                    String s = vs.get(i);
                    allPhotoFragment.adapter.addCheckList(s);
                }
                allPhotoFragment.adapter.notifyDataSetChanged();
            } else {
                allPhotoFragment.adapter.removeAllCheckedList();
                allPhotoFragment.adapter.notifyDataSetChanged();
            }
        }
    }

    public interface AlbumCallBack {
        void onReadFinished(List<String> images);
    }
}
