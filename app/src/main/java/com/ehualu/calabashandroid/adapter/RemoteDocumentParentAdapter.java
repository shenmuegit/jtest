package com.ehualu.calabashandroid.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.decoration.DividerItemDecoration06;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.base.BaseAdapter;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.model.RemoteFileListSortByTime;

import java.util.List;

public class RemoteDocumentParentAdapter extends BaseAdapter<RemoteFileListSortByTime, BaseViewHolder> {

    private Context mContext;
    private int viewType;
    private DividerItemDecoration06 decoration06;
    private int canEditFileTotal;//可编辑的文件总数

    public RemoteDocumentParentAdapter(Context context, @Nullable List<RemoteFileListSortByTime> data, int viewType) {
        super(R.layout.item_remote_document_parent, data);
        this.mContext = context;
        this.viewType = viewType;
        decoration06 = new DividerItemDecoration06(mContext);
        for (RemoteFileListSortByTime item : data) {
            for (RemoteFile file : item.getList()) {
                if (file.isCanSelected()) {
                    canEditFileTotal++;
                }
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteFileListSortByTime item) {
        helper.setText(R.id.tvDate, item.getShowDate());
        RecyclerView recyDocumentChild = helper.getView(R.id.recyDocumentChild);
        if (viewType == BaseActivity.GRID_VIEW) {
            if (recyDocumentChild.getItemDecorationCount() == 0) {
                recyDocumentChild.addItemDecoration(decoration06);
            }
            recyDocumentChild.setLayoutManager(new GridLayoutManager(mContext, 3));
            RemoteDocumentChildAdapter remoteDocumentChildAdapter = new RemoteDocumentChildAdapter(mContext,
                    item.getList(), R.layout.item_remote_document_child_grid, this);
            recyDocumentChild.setAdapter(remoteDocumentChildAdapter);
        } else if (viewType == BaseActivity.LIST_VIEW) {
            recyDocumentChild.removeItemDecoration(decoration06);
            recyDocumentChild.setLayoutManager(new LinearLayoutManager(mContext));
            RemoteDocumentChildAdapter remoteDocumentChildAdapter = new RemoteDocumentChildAdapter(mContext,
                    item.getList(), R.layout.item_remote_document_child_list, this);
            recyDocumentChild.setAdapter(remoteDocumentChildAdapter);
        }

        FrameLayout flSwitch = helper.getView(R.id.flHeader);
        ImageView ivSwitch = helper.getView(R.id.ivSwitch);
        flSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyDocumentChild.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                if (ivSwitch.getTag() == null || ivSwitch.getTag().toString().equals("close")) {
                    openOrCloseAnimator(ivSwitch, recyDocumentChild, recyDocumentChild.getMeasuredHeight(), 0);
                } else if (ivSwitch.getTag().toString().equals("open")) {
                    openOrCloseAnimator(ivSwitch, recyDocumentChild, 0, recyDocumentChild.getMeasuredHeight());
                }
            }
        });

    }

    public int getCanEditFileTotal() {
        return canEditFileTotal;
    }
}
