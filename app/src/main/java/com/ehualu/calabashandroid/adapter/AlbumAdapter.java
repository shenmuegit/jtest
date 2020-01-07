package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.Album;
import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.DingLayoutManager;
import com.othershe.combinebitmap.listener.OnProgressListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AlbumAdapter extends BaseQuickAdapter<Album, BaseViewHolder> {

    private Context context;

    public AlbumAdapter(Context context, List<Album> albums) {
        super(R.layout.item_album, albums);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Album item) {
        FrameLayout frRoot = helper.getView(R.id.frRoot);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frRoot.getLayoutParams();
        if (helper.getAdapterPosition() % 2 == 0) {
            params.gravity = Gravity.LEFT;
        } else {
            params.gravity = Gravity.RIGHT;
        }
        helper.setText(R.id.tvName, item.getName())
                .setText(R.id.tvNumber, item.getCount() + "");
        ImageView ivThumbnail = helper.getView(R.id.ivThumbnail);
        Observable.create(new ObservableOnSubscribe<Bitmap[]>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap[]> emitter) throws Exception {
                List<Bitmap> bitmaps = new ArrayList<>();
                for (int i = 0; i < item.getFiles().size(); i++) {
                    Bitmap b = BitmapFactory.decodeFile(item.getFiles().get(i));
                    if (b != null) {
                        bitmaps.add(b);
                    }
                }
                emitter.onNext(bitmaps.toArray(new Bitmap[]{}));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap[]>() {
                    @Override
                    public void accept(Bitmap[] bitmaps) throws Exception {
                        CombineBitmap.init(context)
                                .setLayoutManager(new DingLayoutManager())
                                .setSize(180)
                                .setGap(2)
                                .setBitmaps(bitmaps)
                                .setOnProgressListener(new OnProgressListener() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onComplete(Bitmap bitmap) {
                                        ivThumbnail.setImageBitmap(bitmap);
                                    }
                                })
                                .build();
                    }
                });
    }
}
