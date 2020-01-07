package com.ehualu.calabashandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.activity.SelectUploadPathActivity;
import com.ehualu.calabashandroid.activity.SelectVideoActivity;
import com.ehualu.calabashandroid.adapter.SelectVideoAdapter;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.db.UploadEntityDao;
import com.ehualu.calabashandroid.db.entity.UploadEntity;
import com.ehualu.calabashandroid.db.manager.EntityManager;
import com.ehualu.calabashandroid.interfaces.VideoOnItemClickInterface;
import com.ehualu.calabashandroid.model.Video;
import com.ehualu.calabashandroid.model.VideoList;
import com.ehualu.calabashandroid.utils.LocalVideoHelper;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.ehualu.calabashandroid.widget.DrawableCenterRadioButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * 未上传的本地视频列表
 */
public class UnUploadVideoFragment extends BaseFragment implements VideoOnItemClickInterface {

    @BindView(R.id.recyVideoList)
    RecyclerView recyVideoList;
    @BindView(R.id.tvSelectPosition)
    DrawableCenterRadioButton tvSelectPosition;
    @BindView(R.id.tvEnsure)
    TextView tvEnsure;
    @BindView(R.id.llBottomSelectPath)
    LinearLayout llBottomSelectPath;
    private String dirID;

    private List<VideoList> lists = new ArrayList<>();
    public SelectVideoAdapter adapter;
    private LocalVideoHelper videoHelper;
    private SelectVideoActivity activity;

    private List<String> uploadedPaths = new ArrayList<>();//保存已经上传的文件列表

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (SelectVideoActivity) context;
        dirID = activity.dirID;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void setUpView() {
        tvEnsure.setOnClickListener(this);
        tvSelectPosition.setOnClickListener(this);
        videoHelper = new LocalVideoHelper();
    }

    @Override
    protected void setUpData() {
        Observable.create(new ObservableOnSubscribe<List<String>>() {

            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                //获取所有已经上传的
                List<UploadEntity> list = EntityManager.getInstance().getUploadEntityDao().queryBuilder().where(UploadEntityDao.Properties.Status.eq(4)).list();
                for (UploadEntity e : list) {
                    uploadedPaths.add(e.getPath());
                }
                emitter.onNext(uploadedPaths);
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        videoHelper.initVideo(activity, new DataCallBack() {
                            @Override
                            public void onReadFinished(List<VideoList> videos) {
                                int unUploadSize = 0;
                                int uploadedSize = 0;
                                int allSize = 0;
                                Iterator<VideoList> outIter = videos.iterator();
                                while (outIter.hasNext()) {
                                    VideoList vl = outIter.next();
                                    List<Video> vs = vl.getVideos();
                                    Iterator<Video> innerIter = vs.iterator();
                                    while (innerIter.hasNext()) {
                                        Video v = innerIter.next();
                                        if (strings.contains(v.getPath())) {
                                            innerIter.remove();
                                            uploadedSize++;
                                        } else {
                                            unUploadSize++;
                                        }
                                        allSize++;
                                    }
                                    if (vs.size() == 0) {
                                        outIter.remove();
                                    }
                                }

                                activity.refreshSize(unUploadSize, uploadedSize, allSize);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lists.addAll(videos);
                                        adapter = new SelectVideoAdapter(activity, lists, UN_UPLOAD);
                                        recyVideoList.setLayoutManager(new LinearLayoutManager(activity));
                                        recyVideoList.setAdapter(adapter);
                                        adapter.setListener(UnUploadVideoFragment.this);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TARGET_PATH) {
            if (resultCode == RESULT_OK) {
                dirID = data.getStringExtra("uploadPath");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvEnsure:
                ArrayList<Video> videos = adapter.getCheckedList();
                if (videos.size() == 0) {
                    ToastUtil.showCenterForBusiness(activity, "请选择要上传的视频！");
                } else {
                    //开始文件上传
                    for (Video video : videos) {
                        File file = new File(video.getPath());
                        if (file.exists()) {
                            startUpload(file, dirID);
                        }
                    }
                    ToastUtil.showCenterHasImageToast(activity, "文件已添加至传输列表");
                }
                MyApp.closeAllNeedDestoryActs();
                break;
            case R.id.tvSelectPosition:
                Intent intent = new Intent(activity, SelectUploadPathActivity.class);
                startActivityForResult(intent, REQUEST_TARGET_PATH);
                break;
        }
    }

    @Override
    public void onItemClick(Video video) {

    }

    @Override
    public void selectedAll() {
        activity.tvSelectAllOrNot.setText("全选");
    }

    @Override
    public void notSelectedAll() {
        activity.tvSelectAllOrNot.setText("全不选");
    }

    @Override
    public void selectCount(List<Video> selectedList, int count) {

    }
}
