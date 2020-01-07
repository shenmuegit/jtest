package com.ehualu.calabashandroid.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-10 10:45:40
 * <p>
 * describe：item设置间隔方法
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int top;
    private int bottom;
    private int left;
    private int right;

    public SpacesItemDecoration(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //         Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0) {
            outRect.top = top;
        }
        outRect.bottom = bottom;
        outRect.left = left;
        outRect.right = right;

    }
}
