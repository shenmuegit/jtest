package com.ehualu.calabashandroid.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.DownloadAdapter;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.db.DownLoadRecordEntityDao;
import com.ehualu.calabashandroid.db.entity.DownLoadRecordEntity;
import com.ehualu.calabashandroid.db.manager.DBManager;
import com.ehualu.calabashandroid.model.DownloadTask;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.receiver.DownloadBroadcast;
import com.ehualu.calabashandroid.utils.SpacesItemDecoration;
import com.ehualu.calabashandroid.widget.FullyLinearLayoutManager;
import com.ehualu.calabashandroid.widget.SlideRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * author: houxiansheng
 * time：2019-12-9 17:19:45
 * describe：下载列表
 */
public class DownloadListFragment extends BaseFragment {

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
    private DownloadAdapter goingAdapter;
    private DownloadAdapter finishedAdapter;
    List<DownloadTask> goingList = new ArrayList<>();
    List<DownloadTask> finishedList = new ArrayList<>();

    DownloadBroadcast.DownloadTaskListener listener;
    Map<String, DownloadTask> taskMap = new HashMap<>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_transfer_list;
    }

    @Override
    protected void init() {
        super.init();
        listener = task -> {
            DownloadTask downloadTask = taskMap.get(task.id);
            if (downloadTask == null) {
                return;
            }
            if (!downloadTask.id.equals(task.id)) {
                return;
            }
            boolean haves = false;
            if (downloadTask.getStatus() == 1 && task.getStatus() == 4) {
                for (DownloadTask _task : finishedList) {
                    if (_task.id.equals(task.id)) {
                        haves = true;
                        break;
                    }

                }
                if (!haves) {
                    finishedList.add(task);
                }
                goingList.remove(downloadTask);
                goingAdapter.notifyDataSetChanged();
                finishedAdapter.notifyDataSetChanged();
                setDownloadingSize();
                setFinishedSize();
            } else {
                goingList.set(goingList.indexOf(downloadTask), task);
                taskMap.put(task.id, task);
                goingAdapter.notifyDataSetChanged();
            }
        };
        MyApp.getBroadcast().addListener(listener);
    }

    @Override
    protected void setUpView() {


        slideRecyclerViewDownloading.setLayoutManager(new FullyLinearLayoutManager(baseActivity));
        slideRecyclerViewDownloading.setNestedScrollingEnabled(false);
        slideRecyclerViewDownloading.addItemDecoration(new SpacesItemDecoration(0, 16, 0, 0)); //RecycleView 增加边距
        goingAdapter = new DownloadAdapter(baseActivity, goingList, false);
        slideRecyclerViewDownloading.setAdapter(goingAdapter);
        goingAdapter.setOnDeleteClickListener(new DownloadAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                DownloadTask task = goingList.get(position);
                task.setStatus(5);
                DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().delete(task.getDownLoadRecordEntity());
                DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().deleteInTx(task.getParts());
                taskMap.put(task.id, new DownloadTask(new RemoteFile()));
                goingList.remove(position);
                goingAdapter.notifyDataSetChanged();
                slideRecyclerViewDownloading.closeMenu();
                setDownloadingSize();
            }
        });


        slideRecyclerViewFinished.setLayoutManager(new FullyLinearLayoutManager(baseActivity));
        slideRecyclerViewFinished.setNestedScrollingEnabled(false);
        slideRecyclerViewFinished.addItemDecoration(new SpacesItemDecoration(0, 16, 0, 0)); //RecycleView 增加边距
        finishedAdapter = new DownloadAdapter(baseActivity, finishedList, true);
        slideRecyclerViewFinished.setAdapter(finishedAdapter);
        finishedAdapter.setOnDeleteClickListener(new DownloadAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                DownloadTask task = finishedList.get(position);
                task.setStatus(5);
                DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().delete(task.getDownLoadRecordEntity());
                DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().deleteInTx(task.getParts());
                taskMap.put(task.id, new DownloadTask(new RemoteFile()));
                finishedList.remove(position);
                finishedAdapter.notifyDataSetChanged();
                slideRecyclerViewFinished.closeMenu();
                setFinishedSize();
            }
        });
        getDate();
    }

    private void getDate() {
        List<DownLoadRecordEntity> loadings =
                DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().queryBuilder()
                        .whereOr(DownLoadRecordEntityDao.Properties.Status.eq("1")
                                , DownLoadRecordEntityDao.Properties.Status.eq("0"))
                        .where(DownLoadRecordEntityDao.Properties.FileType.eq("1"))
                        .orderAsc(DownLoadRecordEntityDao.Properties.CreateTime)
                        .list();
        List<DownLoadRecordEntity> finishs =
                DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().queryBuilder()
                        .where(DownLoadRecordEntityDao.Properties.Status.eq("4"))
                        .where(DownLoadRecordEntityDao.Properties.FileType.eq("1"))
                        .orderAsc(DownLoadRecordEntityDao.Properties.CreateTime)
                        .list();
        for (DownLoadRecordEntity entity : loadings) {
            RemoteFile file = new RemoteFile();
            file.setFileSize(entity.getFileSize());
            file.setFileName(entity.getFileName());
            file.setID(entity.getFileId());
            DownloadTask task = new DownloadTask(entity.getTaskId(), file, entity.getCreateTime());
            task.setDownLoadRecordEntity(entity);
            List<DownLoadRecordEntity> parts =
                    DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().queryBuilder()
                            .where(DownLoadRecordEntityDao.Properties.TaskId.eq(task.id))
                            .where(DownLoadRecordEntityDao.Properties.FileType.eq("2"))
                            .orderAsc(DownLoadRecordEntityDao.Properties.PartNum)
                            .list();
            task.setParts(parts);
            taskMap.put(task.id, task);
            goingList.add(task);
        }
        for (DownLoadRecordEntity entity : finishs) {
            RemoteFile file = new RemoteFile();
            file.setFileSize(entity.getFileSize());
            file.setFileName(entity.getFileName());
            file.setID(entity.getFileId());
            DownloadTask task = new DownloadTask(entity.getTaskId(), file, entity.getCreateTime());
            task.setDownLoadRecordEntity(entity);
            List<DownLoadRecordEntity> parts =
                    DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().queryBuilder()
                            .where(DownLoadRecordEntityDao.Properties.TaskId.eq(task.id))
                            .where(DownLoadRecordEntityDao.Properties.FileType.eq("2"))
                            .orderAsc(DownLoadRecordEntityDao.Properties.PartNum)
                            .list();
            task.setParts(parts);
            taskMap.put(task.id, task);
            finishedList.add(task);
        }

        goingAdapter.notifyDataSetChanged();
        finishedAdapter.notifyDataSetChanged();

        setDownloadingSize();
        setFinishedSize();

    }

    private void setDownloadingSize() {
        tvDownloadingNum.setText(String.format(baseActivity.getString(R.string.tv_transfer_title_num), "正在下载",
                goingList.size()));
    }

    private void setFinishedSize() {
        tvFinishedNum.setText(String.format(baseActivity.getString(R.string.tv_transfer_title_num), "下载完成",
                finishedList.size()));
    }

    @Override
    protected void setUpData() {
        tvFinishedClearAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_finished_clear_all:
                List<DownLoadRecordEntity> allList = new ArrayList<>();
                for (DownloadTask task : finishedList) {
                    List<DownLoadRecordEntity> list =
                            DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().queryBuilder().where(DownLoadRecordEntityDao.Properties.TaskId.eq(task.id)).list();
                    allList.addAll(list);
                }
                DBManager.getInstance().getDaoSession().getDownLoadRecordEntityDao().deleteInTx(allList);
                finishedList.clear();
                finishedAdapter.notifyDataSetChanged();
                setFinishedSize();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁时移除此监听
        if (listener != null) {
            MyApp.getBroadcast().removeListener(listener);
        }
    }
}
