package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.decoration.DividerItemDecoration01;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.base.BaseAdapter;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.model.RemoteFileListSortByTime;
import com.ehualu.calabashandroid.utils.DensityUtil;

import java.util.List;

public class RemoteVideoParentAdapter extends BaseAdapter<RemoteFileListSortByTime, BaseViewHolder> {

    private Context mContext;
    private int viewType;
    private int canEditFileTotal;
    private DividerItemDecoration01 decoration01;

    public RemoteVideoParentAdapter(Context context, List<RemoteFileListSortByTime> data, int viewType) {
        super(R.layout.item_remote_video_parent, data);
        this.mContext = context;
        this.viewType = viewType;
        decoration01 = new DividerItemDecoration01(mContext);
        for (RemoteFileListSortByTime item : data) {
            for (RemoteFile file : item.getList()) {
                canEditFileTotal++;
            }
        }
    }

    public int getCanEditFileTotal() {
        return canEditFileTotal;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFileListSortByTime item) {
        FrameLayout flHeader = helper.getView(R.id.flHeader);
        RecyclerView recyVideoChild = helper.getView(R.id.recyVideoChild);
        ImageView ivCloseOrOpen = helper.getView(R.id.ivCloseOrOpen);
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) flHeader.getLayoutParams();
        LinearLayout.LayoutParams ll2 = (LinearLayout.LayoutParams) recyVideoChild.getLayoutParams();
        if (viewType == BaseActivity.LIST_VIEW) {
            ll.leftMargin = DensityUtil.dip2px(20, mContext);
            ll2.leftMargin = DensityUtil.dip2px(20, mContext);
            ll.setMargins(ll.leftMargin, ll.topMargin, ll.rightMargin, ll.bottomMargin);
            ll2.setMargins(ll2.leftMargin, ll2.topMargin, ll2.rightMargin, ll2.bottomMargin);
            flHeader.setLayoutParams(ll);
            recyVideoChild.setLayoutParams(ll2);
        } else {
            ll.leftMargin = DensityUtil.dip2px(13, mContext);
            ll2.leftMargin = DensityUtil.dip2px(13, mContext);
            ll.setMargins(ll.leftMargin, ll.topMargin, ll.rightMargin, ll.bottomMargin);
            ll2.setMargins(ll2.leftMargin, ll2.topMargin, ll2.rightMargin, ll2.bottomMargin);
            flHeader.setLayoutParams(ll);
            recyVideoChild.setLayoutParams(ll2);
        }
        helper.setText(R.id.tvDate, item.getShowDate());
        if (viewType == BaseActivity.LIST_VIEW) {
            recyVideoChild.removeItemDecoration(decoration01);
            recyVideoChild.setLayoutManager(new LinearLayoutManager(mContext));
            RemoteVideoChildAdapter adapter = new RemoteVideoChildAdapter(mContext, item.getList(), R.layout.item_remote_video_child_list, this);
            recyVideoChild.setAdapter(adapter);
        } else if (viewType == BaseActivity.GRID_VIEW) {
            recyVideoChild.setLayoutManager(new GridLayoutManager(mContext, 3));
            if (recyVideoChild.getItemDecorationCount() == 0) {
                recyVideoChild.addItemDecoration(decoration01);
            }
            RemoteVideoChildAdapter adapter = new RemoteVideoChildAdapter(mContext, item.getList(), R.layout.item_remote_video_child_grid, this);
            recyVideoChild.setAdapter(adapter);
        }

        ivCloseOrOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyVideoChild.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                if (ivCloseOrOpen.getTag() == null || ivCloseOrOpen.getTag().toString().equals("close")) {
                    openOrCloseAnimator(ivCloseOrOpen, recyVideoChild, recyVideoChild.getMeasuredHeight(), 0);
                } else if (ivCloseOrOpen.getTag().toString().equals("open")) {
                    openOrCloseAnimator(ivCloseOrOpen, recyVideoChild, 0, recyVideoChild.getMeasuredHeight());
                }
            }
        });
    }
}
