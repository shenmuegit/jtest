package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.Document;
import com.ehualu.calabashandroid.model.Video;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.ScreenUtils;

import java.io.File;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class DocumentAdapter extends BaseQuickAdapter<Document, BaseViewHolder> {

    private Context context;


    public DocumentAdapter(Context context, @Nullable List<Document> data) {
        super(R.layout.item_document, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Document item) {
        ImageView ivDocument = helper.getView(R.id.ivDocument);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivDocument.getLayoutParams();
        params.width = (ScreenUtils.getScreenWidth(context) - DensityUtil.dip2px(13 * 2 + 44 * 2, context)) / 3;
        params.height = params.width;
        ivDocument.setLayoutParams(params);

        LinearLayout llDocument = helper.getView(R.id.llDocument);
        ViewGroup.LayoutParams params2 = llDocument.getLayoutParams();
        params2.width = params.width;
        llDocument.setLayoutParams(params2);
        helper.setText(R.id.tvTitle, item.getName());
        String path = item.getPath();
        if (path.endsWith(".doc") || path.endsWith(".docx")) {
            Glide.with(context).load(R.mipmap.document_icon_word).into(ivDocument);
        } else if (path.endsWith(".xlsx") || path.endsWith(".xls")) {
            Glide.with(context).load(R.mipmap.document_icon_execl).into(ivDocument);
        } else if (path.endsWith(".pptx") || path.endsWith(".ppt")) {
            Glide.with(context).load(R.mipmap.document_icon_ppt).into(ivDocument);
        } else if (path.endsWith(".pdf")) {
            Glide.with(context).load(R.mipmap.document_icon_pdf).into(ivDocument);
        } else if (path.endsWith(".txt")) {
            Glide.with(context).load(R.mipmap.document_icon_txt).into(ivDocument);
        } else if (path.endsWith(".html") || path.endsWith(".htm")) {
            Glide.with(context).load(R.mipmap.document_icon_html).into(ivDocument);
        }
    }

}
