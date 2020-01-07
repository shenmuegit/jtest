package com.ehualu.calabashandroid.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.activity.TakePhotoActivity;
import com.ehualu.calabashandroid.activity.UploadFileTypeActivity;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.base.BaseAdapterForPhoto;
import com.ehualu.calabashandroid.base.BaseAdapterForVideo;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.GlideRoundTransform;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class PhotoAdapter extends BaseAdapterForPhoto<String, BaseViewHolder> {

    private Context context;
    private int type;
    private String dirID;

    public PhotoAdapter(Context context, @Nullable List<String> data, int type, String dirID) {
        super(R.layout.item_photo, data);
        this.context = context;
        this.type = type;
        this.dirID = dirID;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivPhoto = helper.getView(R.id.ivPhoto);
        ImageView cbPhoto = helper.getView(R.id.cbPhoto);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPhoto.getLayoutParams();
        params.width = (ScreenUtils.getScreenWidth(context) - DensityUtil.dip2px(13 * 2 + 3 * 2, context)) / 4;
        params.height = params.width;
        ivPhoto.setLayoutParams(params);

        if (type == BaseFragment.UPLOADED || type == BaseFragment.ALL_UPLOAD) {
            RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(context, 4));
            Glide.with(context).load(item).apply(options).into(ivPhoto);
        } else {
            if (TextUtils.isEmpty(item)) {
                //第一个
                Glide.with(context).load(R.mipmap.select_photo_first).into(ivPhoto);
                cbPhoto.setVisibility(View.GONE);
            } else {
                RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(context, 4));
                Glide.with(context).load(item).apply(options).into(ivPhoto);
                cbPhoto.setVisibility(View.VISIBLE);
            }
        }

        if (isChecked(item)) {
            cbPhoto.setImageResource(R.drawable.upload_photo_cb_checked);
        } else {
            cbPhoto.setImageResource(R.drawable.upload_photo_cb_unchecked);
        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(item)) {
                    new RxPermissions((BaseActivity) mContext).request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                //用户点击了拍照功能
                                Intent intent = new Intent(mContext, TakePhotoActivity.class);
                                intent.putExtra("dirID", dirID);
                                mContext.startActivity(intent);
                            } else {
                                ToastUtil.showCenterForBusiness(mContext, "请先允许拍照权限！");
                            }
                        }
                    });
                } else {
                    if (isChecked(item)) {
                        removeCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                    } else {
                        addCheckList(item);
                        notifyItemChanged(helper.getAdapterPosition());
                    }
                }

            }
        });

        if (photoItemClickInterface != null) {
            if (checkedList.size() != getData().size()) {
                photoItemClickInterface.selectedAll();
            } else {
                photoItemClickInterface.notSelectedAll();
            }
        }
    }

}
