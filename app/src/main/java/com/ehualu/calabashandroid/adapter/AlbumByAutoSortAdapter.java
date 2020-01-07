package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.decoration.DividerItemDecoration05;
import com.ehualu.calabashandroid.model.AlbumMultipleItem;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.ehualu.calabashandroid.widget.AutoScrollViewPager;

import java.util.List;

/**
 * 相册智能排序adapter
 */
public class AlbumByAutoSortAdapter extends BaseMultiItemQuickAdapter<AlbumMultipleItem, BaseViewHolder> {

    private Context mContext;

    public AlbumByAutoSortAdapter(Context context, List<AlbumMultipleItem> data) {
        super(data);
        this.mContext = context;
        addItemType(AlbumMultipleItem.RECOMMEND, R.layout.item_photo_recommend);
        addItemType(AlbumMultipleItem.PERSONAGE, R.layout.item_photo_personage);
        addItemType(AlbumMultipleItem.CLASSIFY, R.layout.item_photo_classify);
        addItemType(AlbumMultipleItem.MAP, R.layout.item_photo_map);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumMultipleItem item) {
        switch (helper.getItemViewType()) {
            case AlbumMultipleItem.RECOMMEND:
                initRecommend(helper, item);
                break;
            case AlbumMultipleItem.PERSONAGE:
                initPersonage(helper, item);
                break;
            case AlbumMultipleItem.CLASSIFY:
                initClassify(helper, item);
                break;
            case AlbumMultipleItem.MAP:
                initMap(helper, item);
                break;
        }
    }

    private void initMap(BaseViewHolder helper, AlbumMultipleItem item) {
        RecyclerView recyMap = helper.getView(R.id.recyMap);
        recyMap.setLayoutManager(new GridLayoutManager(mContext, 3));
        AlbumMapAdapter adapter = new AlbumMapAdapter(mContext, item.getFiles());
        if (recyMap.getItemDecorationCount() == 0) {
            recyMap.addItemDecoration(new DividerItemDecoration05(mContext));
        }
        recyMap.setAdapter(adapter);
    }

    private void initClassify(BaseViewHolder helper, AlbumMultipleItem item) {
        RecyclerView recyClassify = helper.getView(R.id.recyClassify);
        recyClassify.setLayoutManager(new GridLayoutManager(mContext, 3));
        AlbumClassifyAdapter adapter = new AlbumClassifyAdapter(mContext, item.getClassifies());
        if (recyClassify.getItemDecorationCount() == 0) {
            recyClassify.addItemDecoration(new DividerItemDecoration05(mContext));
        }
        recyClassify.setAdapter(adapter);

    }

    private void initPersonage(BaseViewHolder helper, AlbumMultipleItem item) {
        //设置布局的高度
        int ivWidth = (int) ((ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(14 + 10 * 4, mContext)) / (4.25));
        int layoutHeight = ivWidth;
        AutoScrollViewPager viewPager = helper.getView(R.id.viewPager);
        ViewGroup.LayoutParams vl = viewPager.getLayoutParams();
        vl.height = layoutHeight;
        viewPager.setLayoutParams(vl);

        viewPager.setOffscreenPageLimit(5);
        viewPager.setPageMargin(DensityUtil.dip2px(10, mContext));
        viewPager.setAdapter(new AlbumPersonagePageAdapter(mContext, item.getFiles(), viewPager));
    }

    private void initRecommend(BaseViewHolder helper, AlbumMultipleItem item) {
        //设置布局的高度
        int ivWidth = (int) ((ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(14 + 10, mContext)) / (1 + 30 / 306f));
        int layoutHeight = (int) ((ivWidth / 306f) * 160);
        AutoScrollViewPager viewPager = helper.getView(R.id.viewPager);
        ViewGroup.LayoutParams vl = viewPager.getLayoutParams();
        vl.height = layoutHeight;
        viewPager.setLayoutParams(vl);

        int paddingRight = (int) ((ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(14, mContext)) * (30 / 336f));

        viewPager.setOffscreenPageLimit(2);
        viewPager.setPadding(0, 0, paddingRight, 0);
        viewPager.setAdapter(new AlbumRecommendPageAdapter(mContext, item.getFiles(), viewPager));
    }
}
