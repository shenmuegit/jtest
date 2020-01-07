package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.anim.MyItemAnimator;
import com.ehualu.calabashandroid.adapter.decoration.DividerItemDecoration07;
import com.ehualu.calabashandroid.adapter.decoration.DividerItemDecoration08;
import com.ehualu.calabashandroid.base.BaseAdapter;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.model.RemoteFileListSortByTime;

import java.util.List;

public class AlbumByTimeSortAdapter extends BaseAdapter<RemoteFileListSortByTime, BaseViewHolder> {

    private Context mContext;
    private boolean isBitmapMode = false;//是否是大图模式
    private int fileNumber;//文件总数

    public AlbumByTimeSortAdapter(Context context, @Nullable List<RemoteFileListSortByTime> data,
                                  boolean isBitmapMode) {
        super(R.layout.item_album_by_time_sort_parent, data);
        this.mContext = context;
        this.isBitmapMode = isBitmapMode;

        for (RemoteFileListSortByTime photo : data) {
            for (RemoteFile file : photo.getList()) {
                fileNumber++;
            }
        }
    }

    public void setData() {

    }

    public int getFileNumber() {
        return fileNumber;
    }

    private void removeAllDecorations(RecyclerView recyclerView) {
        for (int i = 0; i < recyclerView.getItemDecorationCount(); i++) {
            recyclerView.removeItemDecorationAt(i);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFileListSortByTime item) {
        ImageView ivSwitch = helper.getView(R.id.ivSwitch);
        helper.setText(R.id.tvDate, item.getShowDate());
        RecyclerView recyAlbum = helper.getView(R.id.recyAlbum);
        removeAllDecorations(recyAlbum);
        if (isBitmapMode) {
            recyAlbum.setLayoutManager(new GridLayoutManager(mContext, 2));
            recyAlbum.addItemDecoration(new DividerItemDecoration07(mContext));
        } else {
            recyAlbum.setLayoutManager(new GridLayoutManager(mContext, 4));
            recyAlbum.addItemDecoration(new DividerItemDecoration08(mContext));
        }
        AlbumByTimeSortChildAdapter adapter = new AlbumByTimeSortChildAdapter(mContext, item.getList(), isBitmapMode,
                this);
        recyAlbum.setItemAnimator(new MyItemAnimator());
        ((SimpleItemAnimator) recyAlbum.getItemAnimator()).setSupportsChangeAnimations(false);
        recyAlbum.setAdapter(adapter);

        ivSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyAlbum.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                if (ivSwitch.getTag() == null || ivSwitch.getTag().toString().equals("close")) {
                    openOrCloseAnimator(ivSwitch, recyAlbum, recyAlbum.getMeasuredHeight(), 0);
                } else if (ivSwitch.getTag().toString().equals("open")) {
                    openOrCloseAnimator(ivSwitch, recyAlbum, 0, recyAlbum.getMeasuredHeight());
                }
            }
        });
    }
}
