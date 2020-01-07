package com.ehualu.calabashandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.activity.CapacitySpaceActivity;
import com.ehualu.calabashandroid.activity.MainActivity;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;

public class MineFragment extends BaseFragment {

    @BindView(R.id.tv_capacity_space)
    TextView tvCapacitySpace;
    @BindView(R.id.ll_vip)
    LinearLayout llVip;
    @BindView(R.id.ll_personal_set)
    LinearLayout llPersonalSet;
    @BindView(R.id.ll_inviteFriend)
    LinearLayout llInviteFriend;

    private MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void setUpView() {
        ((MainActivity) baseActivity).getMainBg().setVisibility(View.GONE);
        ImmersionBar.with(this).fitsSystemWindows(false).statusBarDarkFont(true).init();
        tvCapacitySpace.setOnClickListener(this);
        llVip.setOnClickListener(this);
        llPersonalSet.setOnClickListener(this);
        llInviteFriend.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((MainActivity) baseActivity).getMainBg().setVisibility(View.GONE);
        }
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_capacity_space:
                startActivity(new Intent(getContext(), CapacitySpaceActivity.class));
                break;
            case R.id.ll_vip:
                break;
            case R.id.ll_inviteFriend:
                break;
        }
    }
}
