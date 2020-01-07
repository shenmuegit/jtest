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
import androidx.fragment.app.FragmentTransaction;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.fragment.DocumentFragment;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectDocumentActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSelectAllOrNot)
    TextView tvSelectAllOrNot;
    @BindView(R.id.rbUnUploaded)
    RadioButton rbUnUploaded;
    @BindView(R.id.rbUploaded)
    RadioButton rbUploaded;
    @BindView(R.id.rbAll)
    RadioButton rbAll;
    @BindView(R.id.rgDocument)
    RadioGroup rgDocument;
    @BindView(R.id.documentContainer)
    FrameLayout documentContainer;
    @BindView(R.id.llBack)
    LinearLayout llBack;

    private DocumentFragment documentFragment01;
    private DocumentFragment documentFragment02;
    private DocumentFragment documentFragment03;
    private List<BaseFragment> fragments = new ArrayList<>();
    private BaseFragment mContent;
    private int position = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_document);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("选择文档");
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        llBack.setOnClickListener(this);

        documentFragment01 = new DocumentFragment();
        documentFragment02 = new DocumentFragment();
        documentFragment03 = new DocumentFragment();
        fragments.add(documentFragment01);
        fragments.add(documentFragment02);
        fragments.add(documentFragment03);
        rgDocument.setOnCheckedChangeListener(this);
        rgDocument.check(R.id.rbUnUploaded);
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
                    ft.add(R.id.documentContainer, to).commit();
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
                break;
            case R.id.rbUploaded:
                position = 1;
                break;
            case R.id.rbAll:
                position = 2;
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
        }
    }
}
