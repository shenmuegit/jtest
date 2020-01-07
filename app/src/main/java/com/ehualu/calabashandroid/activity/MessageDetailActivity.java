package com.ehualu.calabashandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GaoTing on 2020/1/3.
 * <p>
 * Explain:消息详情
 */
public class MessageDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        tv_title.setText("");
    }

   @OnClick({R.id.ll_back})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
   }
}
