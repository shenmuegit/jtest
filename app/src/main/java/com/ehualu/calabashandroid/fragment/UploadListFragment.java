package com.ehualu.calabashandroid.fragment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.UploadAdapter;
import com.ehualu.calabashandroid.adapter.UploadAdapter007;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.db.UploadEntityDao;
import com.ehualu.calabashandroid.db.entity.UploadEntity;
import com.ehualu.calabashandroid.db.manager.EntityManager;
import com.ehualu.calabashandroid.model.TransferModel;
import com.ehualu.calabashandroid.utils.FileIconUtils;
import com.ehualu.calabashandroid.utils.SpacesItemDecoration;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.ehualu.calabashandroid.widget.FullyLinearLayoutManager;
import com.ehualu.calabashandroid.widget.SlideRecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author: houxiansheng
 * time：2019-12-9 17:21:37
 * describe：上传列表
 */
public class UploadListFragment extends BaseFragment {
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
    private UploadAdapter007 goingAdapter;
    private UploadAdapter007 finishedAdapter;
    List<TransferModel> goingList = new ArrayList<>();
    List<TransferModel> finishedList = new ArrayList<>();

    private Activity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_transfer_list;
    }

    @Override
    protected void setUpView() {

        slideRecyclerViewDownloading.getItemAnimator().setChangeDuration(0);

        slideRecyclerViewDownloading.setLayoutManager(new FullyLinearLayoutManager(activity));
        slideRecyclerViewDownloading.addItemDecoration(new SpacesItemDecoration(0, 16, 0, 0)); //RecycleView 增加边距
        goingAdapter = new UploadAdapter007((BaseActivity) activity, goingList, false);
        slideRecyclerViewDownloading.setAdapter(goingAdapter);
        goingAdapter.setOnDeleteClickListener(new UploadAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                goingList.remove(position);
                goingAdapter.notifyDataSetChanged();
                slideRecyclerViewDownloading.closeMenu();
                setDownloadingSize();
            }
        });

        slideRecyclerViewFinished.getItemAnimator().setChangeDuration(0);

        slideRecyclerViewFinished.setLayoutManager(new FullyLinearLayoutManager(activity));
        slideRecyclerViewFinished.addItemDecoration(new SpacesItemDecoration(0, 16, 0, 0)); //RecycleView 增加边距
        finishedAdapter = new UploadAdapter007((BaseActivity) activity, finishedList, true);
        slideRecyclerViewFinished.setAdapter(finishedAdapter);
        finishedAdapter.setOnDeleteClickListener(new UploadAdapter.OnDeleteClickLister() {
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
        tvDownloadingNum.setText(String.format(activity.getString(R.string.tv_transfer_title_num), "正在上传", goingList.size()));
    }

    private void setFinishedSize() {
        tvFinishedNum.setText(String.format(MyApp.getAppContext().getString(R.string.tv_transfer_title_num), "上传完成", finishedList.size()));
    }

    @Override
    protected void setUpData() {
        tvFinishedClearAll.setOnClickListener(this);

        loadAllUploadTasks();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_finished_clear_all:
                //从上传表删除已经完成的上传任务
                Observable.create(new ObservableOnSubscribe<Object>() {

                    @Override
                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                        EntityManager.getInstance().deleteAllFinishTask();
                        emitter.onNext("success");
                    }
                }).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Object>() {

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Object o) {
                                if (o.toString().equals("success")) {
                                    loadUploadFinishTasks();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        }
    }

    public void refresh() {
        goingList.clear();
        finishedList.clear();
        List<UploadEntity> uploadTasks = EntityManager.getInstance().getUploadEntityDao()
                .queryBuilder().orderDesc(UploadEntityDao.Properties.UpdateTime).list();
        for (UploadEntity task : uploadTasks) {
            if (task.getStatus() == 4) {
                finishedList.add(convert(task));
            } else {
                goingList.add(convert(task));
            }
        }
        goingAdapter = new UploadAdapter007((BaseActivity) activity, goingList, false);
        slideRecyclerViewDownloading.setAdapter(goingAdapter);

        finishedAdapter = new UploadAdapter007((BaseActivity) activity, finishedList, true);
        slideRecyclerViewFinished.setAdapter(finishedAdapter);
        setDownloadingSize();
        setFinishedSize();
    }

    /**
     * 每完成一个文件的传输，需要调用该方法来刷新
     */
    public void loadAllUploadTasks() {
        goingList.clear();
        finishedList.clear();
        List<UploadEntity> uploadTasks = EntityManager.getInstance().getUploadEntityDao()
                .queryBuilder().where(UploadEntityDao.Properties.FileStatus.eq(0)).orderDesc(UploadEntityDao.Properties.UpdateTime).list();
        for (UploadEntity task : uploadTasks) {
            if (task.getStatus() == 4) {
                finishedList.add(convert(task));
            } else {
                goingList.add(convert(task));
            }
        }
        goingAdapter.notifyDataSetChanged();
        finishedAdapter.notifyDataSetChanged();
        setDownloadingSize();
        setFinishedSize();
    }

    public void loadUploadFinishTasks() {
        finishedList.clear();
        List<UploadEntity> uploadTasks = EntityManager.getInstance().getUploadEntityDao().queryBuilder().where(UploadEntityDao.Properties.FileStatus.eq(0)).orderDesc(UploadEntityDao.Properties.UpdateTime).list();
        for (UploadEntity task : uploadTasks) {
            if (task.getStatus() == 4) {
                finishedList.add(convert(task));
            }
        }
        finishedAdapter.notifyDataSetChanged();
        setFinishedSize();
    }

    /**
     * 数据库对象转换为界面bean
     *
     * @param entity
     * @return
     */
    private TransferModel convert(UploadEntity entity) {
        TransferModel model = new TransferModel();
        model.setTaskId(entity.getTaskId());
        model.setName(entity.getFileName());
        model.setSize(entity.getFileSize());
        model.setTargetPath(entity.getTargetPath());
        model.setPath(entity.getPath());
        int percent = (int) (100.0 * ((double) entity.getProgress()) / ((double) entity.getFileSize()));
        model.setCurrentProgress(percent);
        model.setIcon(FileIconUtils.getFileIcon(entity.getFileName()));
        return model;
    }
}
