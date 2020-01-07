package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.decoration.DividerItemDecoration03;
import com.ehualu.calabashandroid.base.BaseAdapter;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.model.RemoteFileListSortByTime;
import com.ehualu.calabashandroid.utils.DensityUtil;

import java.util.List;

public class FileFragmentSortByTimeAdapter extends BaseAdapter<RemoteFileListSortByTime, BaseViewHolder> {

    private Context mContext;
    private int viewType;
    private int canEditFileTotal;//可编辑的文件总数

    public FileFragmentSortByTimeAdapter(Context context, @Nullable List<RemoteFileListSortByTime> data, int viewType) {
        super(R.layout.item_file_sort_by_time_parent, data);
        this.mContext = context;
        this.viewType = viewType;
        for (RemoteFileListSortByTime item : data) {
            for (RemoteFile file : item.getList()) {
                if (file.isCanSelected()) {
                    canEditFileTotal++;
                }
            }
        }
    }

    public int getCanEditFileTotal() {
        return canEditFileTotal;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFileListSortByTime item) {
        FrameLayout flSwitch = helper.getView(R.id.fl_switch);
        ImageView ivSwitch = helper.getView(R.id.iv_switch);
        if (item.getShowDate().equals("9999-99-99")) {
            helper.setGone(R.id.tvDate, false);
            flSwitch.setVisibility(View.GONE);
        } else {
            helper.setGone(R.id.tvDate, true).setText(R.id.tvDate, item.getShowDate());
            TextView tvDate = helper.getView(R.id.tvDate);
            if (viewType == BaseFragment.LIST_VIEW) {
                tvDate.setPadding(DensityUtil.dip2px(8, mContext), tvDate.getPaddingTop(), tvDate.getPaddingRight(),
                        tvDate.getPaddingBottom());
            }
        }

        RecyclerView recyFile = helper.getView(R.id.recyFile);
        if (viewType == BaseFragment.GRID_VIEW) {
            recyFile.setLayoutManager(new GridLayoutManager(mContext, 3));
            FileFragmentSortByTimeChildAdapter fileFragmentSortByTimeChildAdapter =
                    new FileFragmentSortByTimeChildAdapter(mContext, R.layout.item_file_grid, item.getList(), this);
            if (recyFile.getItemDecorationCount() == 0) {
                DividerItemDecoration03 decoration03 = new DividerItemDecoration03(mContext);
                recyFile.addItemDecoration(decoration03);
            }
            recyFile.setAdapter(fileFragmentSortByTimeChildAdapter);
        } else if (viewType == BaseFragment.LIST_VIEW) {
            recyFile.setLayoutManager(new LinearLayoutManager(mContext));
            FileFragmentSortByTimeChildAdapter fileFragmentSortByTimeChildAdapter =
                    new FileFragmentSortByTimeChildAdapter(mContext, R.layout.item_file_list, item.getList(), this);
            recyFile.setAdapter(fileFragmentSortByTimeChildAdapter);
        }

        flSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyFile.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                if (ivSwitch.getTag() == null || ivSwitch.getTag().toString().equals("close")) {
                    openOrCloseAnimator(ivSwitch, recyFile, recyFile.getMeasuredHeight(), 0);
                } else if (ivSwitch.getTag().toString().equals("open")) {
                    openOrCloseAnimator(ivSwitch, recyFile, 0, recyFile.getMeasuredHeight());
                }
            }
        });

    }
}
