package com.ehualu.calabashandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.model.ShareModel;

import java.util.List;

/**
 * add by houxiansheng 2019-12-13 14:58:09 分享的Adapter
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {

    private List<ShareModel> shareModelList;
    private ClickListener clickListener;

    public ShareAdapter(List<ShareModel> shareModelList, ClickListener clickListener) {
        this.shareModelList = shareModelList;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(shareModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return shareModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivShareIcon;
        private TextView tvShareTitle;
        private ShareModel shareModel;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvShareTitle = itemView.findViewById(R.id.tv_share_title);
            ivShareIcon = itemView.findViewById(R.id.iv_share_icon);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(shareModel);
            }
        }

        public void setData(ShareModel model) {
            shareModel = model;
            ivShareIcon.setImageDrawable(shareModel.getDrawable());
            tvShareTitle.setText(shareModel.getTitle());
        }
    }

    public interface ClickListener {
        void onClick(ShareModel shareModel);
    }
}
