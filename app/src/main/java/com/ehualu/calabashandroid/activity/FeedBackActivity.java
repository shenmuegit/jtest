package com.ehualu.calabashandroid.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;

/**
 * 市民反馈界面
 */
public class FeedBackActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ImmersionBar.with(this).init();
    }
}
