package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

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

import java.util.List;

public class BackUpParentAdapter extends BaseAdapter<RemoteFileListSortByTime, BaseViewHolder> {

    private Context mContext;
    private int viewType;
    private int totalSize;//可编辑的文件总数

    public BackUpParentAdapter(Context context, @Nullable List<RemoteFileListSortByTime> data, int viewType) {
        super(R.layout.item_audio_list, data);
        this.mContext = context;
        this.viewType = viewType;
        for (int i = 0; i < data.size(); i++) {
            List<RemoteFile> list = data.get(i).getList();
            for (int j = 0; j < list.size(); j++) {
                totalSize++;
            }
        }
    }

    public int getTotalSize() {
        return totalSize;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFileListSortByTime item) {
        helper.setText(R.id.tv_time, item.getShowDate());
        RecyclerView recyclerViewAudio = helper.getView(R.id.recyclerView_audio);

        if (viewType == BaseFragment.GRID_VIEW) {
            recyclerViewAudio.setLayoutManager(new GridLayoutManager(mContext, 3));
            BackUpChildAdapter backUpChildAdapter = new BackUpChildAdapter(mContext, R.layout.item_file_grid, item.getList(), this);
            if (recyclerViewAudio.getItemDecorationCount() == 0) {
                DividerItemDecoration03 decoration03 = new DividerItemDecoration03(mContext);
                recyclerViewAudio.addItemDecoration(decoration03);
            }
            recyclerViewAudio.setAdapter(backUpChildAdapter);
        } else if (viewType == BaseFragment.LIST_VIEW) {
            recyclerViewAudio.setLayoutManager(new LinearLayoutManager(mContext));
            BackUpChildAdapter backUpChildAdapter = new BackUpChildAdapter(mContext, R.layout.item_file_list, item.getList(), this);
            recyclerViewAudio.setAdapter(backUpChildAdapter);
        }

        FrameLayout flSwitch = helper.getView(R.id.fl_switch);
        ImageView ivSwitch = helper.getView(R.id.iv_switch);
        flSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewAudio.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                if (ivSwitch.getTag() == null || ivSwitch.getTag().toString().equals("close")) {
                    openOrCloseAnimator(ivSwitch, recyclerViewAudio, recyclerViewAudio.getMeasuredHeight(), 0);
                } else if (ivSwitch.getTag().toString().equals("open")) {
                    openOrCloseAnimator(ivSwitch, recyclerViewAudio, 0, recyclerViewAudio.getMeasuredHeight());
                }
            }
        });
    }
}
