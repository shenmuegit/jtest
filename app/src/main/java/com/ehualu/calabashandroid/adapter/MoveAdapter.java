package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.interfaces.OnItemClickInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.FileIconUtils;
import com.ehualu.calabashandroid.utils.ScreenUtils;
import com.ehualu.calabashandroid.utils.TimeUtils;

import java.util.List;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-17 11:21:23
 * <p>
 * describe：移动的Adapter
 */
public class MoveAdapter extends BaseQuickAdapter<RemoteFile, BaseViewHolder> {
    private Context mContext;
    public OnItemClickInterface onItemClickInterface;//处理item的各种点击事件

    public MoveAdapter(Context mContext, List<RemoteFile> ocFileList) {
        super(R.layout.item_move_list, ocFileList);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFile item) {
        ImageView iv = helper.getView(R.id.iv_icon);
        LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) iv.getLayoutParams();
        llParams.width = DensityUtil.dip2px(46, mContext);
        llParams.height = (int) (llParams.width / 222f * 177);
        iv.setLayoutParams(llParams);
        helper.setBackgroundRes(R.id.iv_icon, FileIconUtils.getFileIcon(item));

        helper.setText(R.id.tv_name, item.getFileName());
        helper.setText(R.id.tv_time, TimeUtils.longToString(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickInterface != null) {
                    onItemClickInterface.onItemClick(item);
                }
            }
        });
    }

    public void setListener(OnItemClickInterface onItemClickInterface) {
        this.onItemClickInterface = onItemClickInterface;
    }
}
