package com.ehualu.calabashandroid.fragment;

import android.view.View;
import android.widget.TextView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.BackupExtractAdapter;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.model.TransferModel;
import com.ehualu.calabashandroid.utils.SpacesItemDecoration;
import com.ehualu.calabashandroid.widget.FullyLinearLayoutManager;
import com.ehualu.calabashandroid.widget.SlideRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: houxiansheng
 * time：2019-12-9 17:21:53
 * describe：备份提取列表
 */
public class BackupExtractListFragment extends BaseFragment {

    @BindView(R.id.tv_downloading_num)
    TextView tvDownloadingNum;
    @BindView(R.id.tv_paused_all)
    TextView tvPausedAll;
    @BindView(R.id.slide_recycler_view_downloading)
    SlideRecyclerView slideRecyclerViewDownloading;
    @BindView(R.id.tv_finished_num)
    TextView tvFinishedNum;
    @BindView(R.id.tv_finished_clear_all)
    TextView tvFinishedClearAll;
    @BindView(R.id.slide_recycler_view_finished)
    SlideRecyclerView slideRecyclerViewFinished;
    private BackupExtractAdapter goingAdapter;
    private BackupExtractAdapter finishedAdapter;
    List<TransferModel> goingList = new ArrayList<>();
    List<TransferModel> finishedList = new ArrayList<>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_transfer_list;
    }

    @Override
    protected void setUpView() {

//        for (int i = 0; i < 10; i++) {
//            TransferModel transferModel = new TransferModel();
//            transferModel.setName("测试数据" + i);
//            goingList.add(transferModel);
//        }
//
//        for (int i = 0; i < 15; i++) {
//            TransferModel transferModel = new TransferModel();
//            transferModel.setName("测试数据" + i);
//            finishedList.add(transferModel);
//        }

        setDownloadingSize();
        setFinishedSize();

        slideRecyclerViewDownloading.setLayoutManager(new FullyLinearLayoutManager(baseActivity));
        slideRecyclerViewDownloading.addItemDecoration(new SpacesItemDecoration(0, 16, 0, 0)); //RecycleView 增加边距
        goingAdapter = new BackupExtractAdapter(baseActivity, goingList, false);
        slideRecyclerViewDownloading.setAdapter(goingAdapter);
        goingAdapter.setOnDeleteClickListener(new BackupExtractAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                goingList.remove(position);
                goingAdapter.notifyDataSetChanged();
                slideRecyclerViewDownloading.closeMenu();
                setDownloadingSize();
            }
        });


        slideRecyclerViewFinished.setLayoutManager(new FullyLinearLayoutManager(baseActivity));
        slideRecyclerViewFinished.addItemDecoration(new SpacesItemDecoration(0, 16, 0, 0)); //RecycleView 增加边距
        finishedAdapter = new BackupExtractAdapter(baseActivity, finishedList, true);
        slideRecyclerViewFinished.setAdapter(finishedAdapter);
        finishedAdapter.setOnDeleteClickListener(new BackupExtractAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                finishedList.remove(position);
                finishedAdapter.notifyDataSetChanged();
                slideRecyclerViewFinished.closeMenu();
                setFinishedSize();
            }
        });
    }

    private void setDownloadingSize() {
        tvDownloadingNum.setText(String.format(baseActivity.getString(R.string.tv_transfer_title_num), "正在下载", goingList.size()));
    }

    private void setFinishedSize() {
        tvFinishedNum.setText(String.format(baseActivity.getString(R.string.tv_transfer_title_num), "下载完成", finishedList.size()));
    }

    @Override
    protected void setUpData() {
        tvFinishedClearAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_finished_clear_all:
                finishedList.clear();
                finishedAdapter.notifyDataSetChanged();
                setFinishedSize();
        }
    }
}
