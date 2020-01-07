package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.ehualu.calabashandroid.widget.AutoScrollViewPager;

import java.util.List;

public class AlbumRecommendPageAdapter extends PagerAdapter {

    private Context mContext;
    private List<RemoteFile> mFiles;
    private ViewPager viewPager;

    public AlbumRecommendPageAdapter(Context context, List<RemoteFile> files, AutoScrollViewPager viewPager) {
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
        View view = View.inflate(mContext, R.layout.item_recommend_content, null);
        TextView tvRecommend = view.findViewById(R.id.tvRecommend);

        ImageView ivRecommend = view.findViewById(R.id.ivRecommend);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRecommend.getLayoutParams();
        params.width = (int) (ScreenUtils.getScreenWidth(mContext) * (306f / 360));
        params.height = (int) ((160f / 306) * params.width);
        ivRecommend.setLayoutParams(params);

        ViewGroup.LayoutParams vl = viewPager.getLayoutParams();
        vl.height = params.height;
        viewPager.setLayoutParams(vl);

        container.addView(view);

        RemoteFile file = this.mFiles.get(position);
        tvRecommend.setText(file.getFileName());
        Glide.with(mContext).load(file.getThumbnail()).into(ivRecommend);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
