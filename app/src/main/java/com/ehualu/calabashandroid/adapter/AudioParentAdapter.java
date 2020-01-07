package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseAdapter;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.model.RemoteFileListSortByTime;

import java.util.List;

/**
 * add by houxiansheng 2019-12-18 15:06:32 音频的父类Adapter
 */
public class AudioParentAdapter extends BaseAdapter<RemoteFileListSortByTime, BaseViewHolder> {
    private Context mContext;
    private int totalSize;//文件总数

    public AudioParentAdapter(Context mContext, @Nullable List<RemoteFileListSortByTime> data) {
        super(R.layout.item_audio_list, data);
        this.mContext = mContext;
        for (int i = 0; i < data.size(); i++) {
            List<RemoteFile> list = data.get(i).getList();
            for (int j = 0; j < list.size(); j++) {
                totalSize++;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFileListSortByTime item) {
        helper.setText(R.id.tv_time, item.getShowDate());
        TextView tvTime = helper.getView(R.id.tv_time);

        FrameLayout flSwitch = helper.getView(R.id.fl_switch);
        ImageView ivSwitch = helper.getView(R.id.iv_switch);
        RecyclerView recyclerViewAudio = helper.getView(R.id.recyclerView_audio);

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

        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) tvTime.getLayoutParams();
        fl.setMargins(21, 0, 0, 0);
        tvTime.setLayoutParams(fl);
        recyclerViewAudio.setLayoutManager(new LinearLayoutManager(mContext));
        AudioChildAdapter audioChildAdapter = new AudioChildAdapter(mContext, item.getList(), this);
        recyclerViewAudio.setAdapter(audioChildAdapter);
    }

    public int getTotalSize() {
        return totalSize;
    }
}
