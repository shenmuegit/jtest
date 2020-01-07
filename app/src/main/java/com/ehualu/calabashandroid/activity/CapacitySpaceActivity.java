package com.ehualu.calabashandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: houxiansheng
 * time：2019年12月5日16:25:48
 * describe：容量空间
 */
public class CapacitySpaceActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_back)
    LinearLayout llBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacity_space);
        ButterKnife.bind(this);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        initViewAndData();
        initListener();
    }

    private void initViewAndData() {
        tvTitle.setText("容量空间");
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
