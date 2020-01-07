package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.GlideRoundTransform;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.ehualu.calabashandroid.widget.AutoScrollViewPager;

import java.util.List;

public class AlbumPersonagePageAdapter extends PagerAdapter {

    private Context mContext;
    private List<RemoteFile> mFiles;
    private ViewPager viewPager;

    public AlbumPersonagePageAdapter(Context context, List<RemoteFile> files, AutoScrollViewPager viewPager) {
        this.mContext = context;
        this.mFiles = files;
        this.viewPager = viewPager;
    }

    @Override
    public int getCount() {
        return this.mFiles.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.item_personage_content, null);

        ImageView ivPersonage = view.findViewById(R.id.ivPersonage);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPersonage.getLayoutParams();

        params.width =
                (int) ((ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(14 + 4 * 10, mContext)) / (4.25));
        params.height = params.width;
        ivPersonage.setLayoutParams(params);
        container.addView(view);

        ViewGroup.LayoutParams vl = viewPager.getLayoutParams();
        vl.height = params.height;
        viewPager.setLayoutParams(vl);
        viewPager.setPadding(0, 0,
                ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(14, mContext) - params.width, 0);
        RemoteFile file = this.mFiles.get(position);
        Glide.with(mContext).load(file.getThumbnail()).transform(new GlideRoundTransform(mContext, 50)).into(ivPersonage);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
