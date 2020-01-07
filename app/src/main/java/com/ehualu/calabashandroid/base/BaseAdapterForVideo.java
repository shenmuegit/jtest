package com.ehualu.calabashandroid.base;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.interfaces.VideoOnItemClickInterface;
import com.ehualu.calabashandroid.model.Video;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapterForVideo<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    public ArrayList<Video> checkedList = new ArrayList<>();//选中的文件列表
    public VideoOnItemClickInterface videoItemClickInterface;

    public BaseAdapterForVideo(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public void setListener(VideoOnItemClickInterface videoItemClickInterface) {
        this.videoItemClickInterface = videoItemClickInterface;
    }

    public void addCheckList(Video file) {
        if (!checkedList.contains(file)) {
            checkedList.add(file);
        }

        if (videoItemClickInterface != null) {
            videoItemClickInterface.selectCount(checkedList, checkedList.size());
        }
    }

    public boolean isChecked(Video file) {
        return checkedList.contains(file);
    }

    public ArrayList<Video> getCheckedList() {
        return checkedList;
    }

    public void removeCheckList(Video file) {
        checkedList.remove(file);

        if (videoItemClickInterface != null) {
            videoItemClickInterface.selectCount(checkedList, checkedList.size());
        }
    }

    public void removeAllCheckedList() {
        checkedList.clear();
        if (videoItemClickInterface != null) {
            videoItemClickInterface.selectCount(checkedList, 0);
        }
    }

    public void openOrCloseAnimator(ImageView mySwitch, View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = v.getLayoutParams();
                params.height = value;
                v.setLayoutParams(params);
                if (value == end) {
                    if (end == 0) {
                        mySwitch.setTag("open");
                        mySwitch.setImageResource(R.mipmap.select_video_open);
                    } else {
                        mySwitch.setTag("close");
                        mySwitch.setImageResource(R.mipmap.select_video_close);
                    }
                }
            }
        });
        animator.setDuration(300);
        animator.start();
    }
}
