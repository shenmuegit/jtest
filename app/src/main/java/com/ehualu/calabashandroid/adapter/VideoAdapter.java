package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.model.Video;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.FileCut;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.ehualu.calabashandroid.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ehualu.calabashandroid.utils.LocalFileHelper.bytesToHexString;

public class VideoAdapter extends BaseQuickAdapter<Video, BaseViewHolder> {

    private Context context;
    private SelectVideoAdapter parentAdapter;


    public VideoAdapter(Context context, @Nullable List<Video> data, SelectVideoAdapter parentAdapter) {
        super(R.layout.item_video, data);
        this.context = context;
        this.parentAdapter = parentAdapter;
    }

    @Override
    protected void convert(BaseViewHolder helper, Video item) {
        ImageView ivVideo = helper.getView(R.id.ivVideo);
        ImageView cbVideo = helper.getView(R.id.cbVideo);

        cbVideo.setVisibility(View.VISIBLE);

        if (parentAdapter.isChecked(item)) {
            cbVideo.setImageResource(R.drawable.upload_photo_cb_checked);
        } else {
            cbVideo.setImageResource(R.drawable.upload_photo_cb_unchecked);
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivVideo.getLayoutParams();
        params.width = (ScreenUtils.getScreenWidth(context) - DensityUtil.dip2px(13 * 2 + 44 * 2, context)) / 3;
        params.height = params.width;
        ivVideo.setLayoutParams(params);

        RelativeLayout rlVideo = helper.getView(R.id.rlVideo);
        ViewGroup.LayoutParams params2 = rlVideo.getLayoutParams();
        params2.width = params.width;
        rlVideo.setLayoutParams(params2);

        helper.setText(R.id.tvDuration, stringForTime((int) item.getDuration()));

        Glide.with(context).load(Uri.fromFile(new File(item.getPath()))).into(ivVideo);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parentAdapter.isChecked(item)) {
                    parentAdapter.removeCheckList(item);
                    notifyItemChanged(helper.getAdapterPosition());
                } else {
                    parentAdapter.addCheckList(item);
                    notifyItemChanged(helper.getAdapterPosition());
                }
            }
        });

        if (parentAdapter.videoItemClickInterface != null) {
            if (parentAdapter.checkedList.size() != parentAdapter.getFileCount()) {
                parentAdapter.videoItemClickInterface.selectedAll();
            } else {
                parentAdapter.videoItemClickInterface.notSelectedAll();
            }
        }
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        final StringBuilder mFormatBuilder = new StringBuilder();
        final Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}
