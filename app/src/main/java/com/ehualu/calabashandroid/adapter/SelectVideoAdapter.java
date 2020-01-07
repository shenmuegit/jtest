package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.decoration.DividerItemDecoration04;
import com.ehualu.calabashandroid.base.BaseAdapterForVideo;
import com.ehualu.calabashandroid.interfaces.VideoOnItemClickInterface;
import com.ehualu.calabashandroid.model.Video;
import com.ehualu.calabashandroid.model.VideoList;

import java.util.List;

public class SelectVideoAdapter extends BaseAdapterForVideo<VideoList, BaseViewHolder> {

    private Context context;
    public int type;
    private int fileCount;

    public SelectVideoAdapter(Context context, @Nullable List<VideoList> data,int type) {
        super(R.layout.item_video_list, data);
        this.context = context;
        this.type=type;

        for(VideoList vl:data){
            for(Video v:vl.getVideos()){
                fileCount++;
            }
        }
    }

    public int getFileCount(){
        return fileCount;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoList item) {
        helper.setText(R.id.tvDate, item.getDate());

        ImageView ivSwitch = helper.getView(R.id.ivSwitch);
        RecyclerView recyVideo = helper.getView(R.id.recyVideo);

        ivSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyVideo.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                if (ivSwitch.getTag() == null || ivSwitch.getTag().toString().equals("close")) {
                    openOrCloseAnimator(ivSwitch, recyVideo, recyVideo.getMeasuredHeight(), 0);
                } else if (ivSwitch.getTag().toString().equals("open")) {
                    openOrCloseAnimator(ivSwitch, recyVideo, 0, recyVideo.getMeasuredHeight());
                }
            }
        });

        DividerItemDecoration04 decoration=new DividerItemDecoration04(mContext);
        if (recyVideo.getItemDecorationCount() == 0) {
            recyVideo.addItemDecoration(decoration);
        }
        recyVideo.setLayoutManager(new GridLayoutManager(context, 3));
        VideoAdapter adapter = new VideoAdapter(context, item.getVideos(),this);
        recyVideo.setAdapter(adapter);
    }

}
