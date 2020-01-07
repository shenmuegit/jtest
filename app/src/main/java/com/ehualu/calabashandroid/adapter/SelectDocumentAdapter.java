package com.ehualu.calabashandroid.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.DocumentList;
import com.ehualu.calabashandroid.model.VideoList;

import java.util.List;

public class SelectDocumentAdapter extends BaseQuickAdapter<DocumentList, BaseViewHolder> {

    private Context context;

    public SelectDocumentAdapter(Context context, @Nullable List<DocumentList> data) {
        super(R.layout.item_document_list, data);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    protected void convert(BaseViewHolder helper, DocumentList item) {
        helper.setText(R.id.tvDate, item.getDate());

        ImageView ivSwitch = helper.getView(R.id.ivSwitch);
        RecyclerView recyVideo = helper.getView(R.id.recyDocument);

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

        DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(context.getResources().getDrawable(R.drawable.vertical_line30));
        if (recyVideo.getItemDecorationCount() == 0) {
            recyVideo.addItemDecoration(decoration);
        }
        recyVideo.setLayoutManager(new GridLayoutManager(context, 3));
        DocumentAdapter adapter = new DocumentAdapter(context, item.getDocuments());
        recyVideo.setAdapter(adapter);
    }

    private void openOrCloseAnimator(ImageView mySwitch, View v, int start, int end) {
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
