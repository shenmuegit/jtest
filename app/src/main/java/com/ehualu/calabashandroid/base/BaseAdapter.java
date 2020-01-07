package com.ehualu.calabashandroid.base;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.interfaces.OnItemClickInterface;
import com.ehualu.calabashandroid.model.RemoteFile;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    public OnItemClickInterface onItemClickInterface;//处理item的各种点击事件
    public List<RemoteFile> checkedList = new ArrayList<>();//选中的文件列表
    private boolean isSelectMode = false;//当前是否是选择模式

    public BaseAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public void setListener(OnItemClickInterface onItemClickInterface) {
        this.onItemClickInterface = onItemClickInterface;
    }

    public void setSelectMode(boolean b) {
        this.isSelectMode = b;
        notifyDataSetChanged();
    }

    public boolean isSelectMode() {
        return isSelectMode;
    }

    public void addCheckList(RemoteFile file) {
        if (!checkedList.contains(file)) {
            checkedList.add(file);
        }
        if (onItemClickInterface != null) {
            onItemClickInterface.selectCount(checkedList, checkedList.size());
        }
    }

    public boolean isChecked(RemoteFile file) {
        return checkedList.contains(file);
    }

    public List<RemoteFile> getCheckedList() {
        return checkedList;
    }

    public void removeCheckList(RemoteFile file) {
        checkedList.remove(file);
        if (onItemClickInterface != null) {
            onItemClickInterface.selectCount(checkedList, checkedList.size());
        }
    }

    public void removeAllCheckedList() {
        checkedList.clear();
        if (onItemClickInterface != null) {
            onItemClickInterface.selectCount(checkedList, 0);
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
