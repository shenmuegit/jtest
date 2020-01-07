package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.GlideRoundTransform;
import com.ehualu.calabashandroid.utils.ScreenUtils;

import java.util.List;

/**
 * 相册智能分类：地图Adapter
 */
public class AlbumMapAdapter extends BaseQuickAdapter<RemoteFile, BaseViewHolder> {

    private Context mContext;

    public AlbumMapAdapter(Context context, List<RemoteFile> data) {
        super(R.layout.item_map_content, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFile item) {
        helper.setText(R.id.tvItem, item.getFileName());
        ImageView ivItem = helper.getView(R.id.ivItem);
        int width = (ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(14 + 11 + 2 * 10, mContext)) / 3;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivItem.getLayoutParams();
        params.width = width;
        params.height = width;
        ivItem.setLayoutParams(params);
        Glide.with(mContext).load(item.getThumbnail()).transform(new GlideRoundTransform(mContext, 10)).into(ivItem);
    }
}
