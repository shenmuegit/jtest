package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.Classify;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.DingLayoutManager;
import com.othershe.combinebitmap.listener.OnProgressListener;

import java.util.List;

public class AlbumClassifyAdapter extends BaseQuickAdapter<Classify, BaseViewHolder> {

    private Context mContext;

    public AlbumClassifyAdapter(Context context, @Nullable List<Classify> data) {
        super(R.layout.item_classify_content, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Classify item) {
        helper.setText(R.id.tvItem, item.getTitle());
        ImageView ivItem = helper.getView(R.id.ivItem);

        int count;
        if (item.getFiles().size() > 4) {
            count = 4;
        } else {
            count = item.getFiles().size();
        }

        String[] urls = new String[count];

        for (int i = 0; i < count; i++) {
            urls[i] = item.getFiles().get(i).getThumbnail();
        }

        int size = (ScreenUtils.getScreenWidth(mContext) - DensityUtil.dip2px(14 + 11 + 2 * 10, mContext)) / 3;
        CombineBitmap.init(mContext)
                .setLayoutManager(new DingLayoutManager())
                .setSize(size / 3)
                .setUrls(urls)
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(Bitmap bitmap) {
                        ivItem.setImageBitmap(bitmapRound(bitmap, 30));
                    }
                }).build();
    }

    private Bitmap bitmapRound(Bitmap mBitmap, float index) {
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        //设置矩形大小
        Rect rect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF rectf = new RectF(rect);

        // 相当于清屏
        canvas.drawARGB(0, 0, 0, 0);
        //画圆角
        canvas.drawRoundRect(rectf, index, index, paint);
        // 取两层绘制，显示上层
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 把原生的图片放到这个画布上，使之带有画布的效果
        canvas.drawBitmap(mBitmap, rect, rect, paint);
        return bitmap;

    }

}
