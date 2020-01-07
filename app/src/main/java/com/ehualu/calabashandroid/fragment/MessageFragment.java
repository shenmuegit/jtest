package com.ehualu.calabashandroid.fragment;

import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.activity.MainActivity;
import com.ehualu.calabashandroid.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class MessageFragment extends BaseFragment {

    @BindView(R.id.tv_num_message)
    TextView tvNum;
    @BindView(R.id.recycler_message)
    RecyclerView recyclerView;
    @BindView(R.id.layout_select_top_title_message)
    LinearLayout layoutSelectTop;
    @BindView(R.id.tv_select_all_message)
    TextView tvAllSelect;
    @BindView(R.id.tv_select_title_message)
    TextView tvSelectTitle;
    @BindView(R.id.llSearch)
    LinearLayout layoutSearch;
    @BindView(R.id.ll_top_title)
    LinearLayout layoutTop;
    @BindView(R.id.layout_menu_select_message)
    LinearLayout layoutMenu;

    boolean selectStatus = false;
    List<Object> selets=new ArrayList<>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void setUpView() {
        ((MainActivity) baseActivity).getMainBg().setVisibility(View.GONE);
    }

    @Override
    protected void init() {
        super.init();
        recyclerView.setAdapter(new BaseQuickAdapter<Message, BaseViewHolder>(R.layout.item_message) {
            @Override
            protected void convert(BaseViewHolder helper, Message item) {

            }
        });
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

    @OnClick({R.id.tv_cancel_message, R.id.tv_select_all_message, R.id.img_menu_message
            , R.id.llSearch, R.id.tv_share_message, R.id.tv_delete_message})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel_message://取消按钮
                showMenu(false);
                break;
            case R.id.tv_select_all_message://全选
                break;
            case R.id.img_menu_message://菜单
                showMenu(true);
                break;
            case R.id.llSearch://搜索
                break;
            case R.id.tv_share_message://分享
                break;
            case R.id.tv_delete_message://删除
                break;
            default:
                break;
        }
    }

    private void showMenu(boolean show) {
        ((MainActivity) baseActivity).setMenuShowStatus(!show);
        selectStatus = show;
        //刷新数据列表
        recyclerView.getAdapter().notifyDataSetChanged();
        if (show) {
            //隐藏默认头部
            layoutTop.setVisibility(View.GONE);
            //显示选择头部
            layoutSelectTop.setVisibility(View.VISIBLE);
            //显示底部菜单
            layoutMenu.setVisibility(View.VISIBLE);
        } else {
            //显示默认头部
            layoutTop.setVisibility(View.VISIBLE);
            //隐藏选择头部
            layoutSelectTop.setVisibility(View.GONE);
            //隐藏底部菜单
            layoutMenu.setVisibility(View.GONE);
        }

    }
}
