package com.ehualu.calabashandroid.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 公共搜索界面
 */
public class CommonSearchActivity extends BaseActivity {

    @BindView(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    @BindView(R.id.id_flowlayout2)
    TagFlowLayout idFlowlayout2;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvCancel)
    TextView tvCancel;

    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_search);
        ButterKnife.bind(this);

        layoutInflater = LayoutInflater.from(this);

        tvCancel.setOnClickListener(this);

        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        /**
         * 通过反射修改光标颜色
         */
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(etSearch, R.drawable.edittext_cursor_bg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> strs01 = new ArrayList<>();
        strs01.add("千手柱间");
        strs01.add("漩涡鸣人");
        strs01.add("猿飞日斩");
        strs01.add("千手扉间");
        strs01.add("波风水门");
        strs01.add("千手纲手");
        strs01.add("旗木卡卡西");
        strs01.add("宇智波佐助");

        idFlowlayout.setAdapter(new TagAdapter<String>(strs01) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) layoutInflater.inflate(R.layout.tag_textview, idFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });

        idFlowlayout2.setAdapter(new TagAdapter<String>(strs01) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) layoutInflater.inflate(R.layout.tag_textview, idFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tvCancel:
                finish();
                break;
        }
    }
}
