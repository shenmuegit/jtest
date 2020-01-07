package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.base.BaseAdapter;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.CapacityUtils;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.FileIconUtils;
import com.ehualu.calabashandroid.utils.GlideRoundTransform;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.ehualu.calabashandroid.utils.TimeUtils;

import java.util.List;

public class RemoteVideoNormalAdapter extends BaseAdapter<RemoteFile, BaseViewHolder> {

    private Context mContext;
    private int itemLayout;

    public RemoteVideoNormalAdapter(Context context, int itemLayout, List<RemoteFile> data) {
        super(itemLayout, data);
        this.mContext = context;
        this.itemLayout = itemLayout;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFile item) {
        ImageView ivChildVideo = helper.getView(R.id.ivChildVideo);
        switch (itemLayout) {
            case R.layout.item_remote_video_child_grid:
                helper.setText(R.id.tvName, item.getFileName());
                RelativeLayout rlVideo = helper.getView(R.id.rlVideo);
                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) rlVideo.getLayoutParams();
                int width = (ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(13 * 2 + 44 * 2, mContext)) / 3;
                ll.width = width;
                ll.height = width;
                rlVideo.setLayoutParams(ll);
                FileIconUtils.getThumnailURL(mContext, item, ivChildVideo); //获取缩略图
                break;
            case R.layout.item_remote_video_child_list:
                RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(mContext, 4));
                helper.setText(R.id.tv_name, item.getFileName());
                helper.setText(R.id.tv_time, TimeUtils.longToString(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
                helper.setText(R.id.tv_size, CapacityUtils.bytesToHumanReadable(item.getFileSize()));
                FileIconUtils.getThumnailURL(mContext, item, ivChildVideo); //获取缩略图
                break;
        }
    }
}
