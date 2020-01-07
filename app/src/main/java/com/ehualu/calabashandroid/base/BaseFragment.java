package com.ehualu.calabashandroid.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ehualu.calabashandroid.activity.MainActivity;
import com.ehualu.calabashandroid.activity.UploadFileTypeActivity;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.db.entity.UploadEntity;
import com.ehualu.calabashandroid.db.manager.EntityManager;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.model.VideoList;
import com.ehualu.calabashandroid.responseBean.CreateTaskIdResponse;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;
import com.ehualu.calabashandroid.service.FileUploader;
import com.ehualu.calabashandroid.upload.UploadChunk;
import com.ehualu.calabashandroid.upload.UploadTool;
import com.ehualu.calabashandroid.utils.Constants;
import com.ehualu.calabashandroid.utils.FileUtils;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.StatusBarUtil;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    public static final int UN_UPLOAD=0;
    public static final int UPLOADED=1;
    public static final int ALL_UPLOAD=2;

    private View mContentView;
    public BaseActivity baseActivity;
    Collator collator = Collator.getInstance(java.util.Locale.CHINA);

    private RxPermissions rxPermissions;

    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_TIME = 2;

    public static final int GRID_VIEW = 1;
    public static final int LIST_VIEW = 2;

    public int sortType = SORT_BY_NAME;
    public int viewType = GRID_VIEW;
    public boolean isSelectMode = false;

    public static int REQUEST_TARGET_PATH = 1;

    private boolean isFixed(RemoteFile file, String name) {
        if ("0".equals(file.getParentId()) && name.equals(file.getFileName()) && "2".equals(file.getCategory())) {
            return true;
        }
        return false;
    }


    /**
     * 先显示所有文件夹，再显示所有文件
     * 根目录下，葫芦备份>备份恢复>收到文件>我的收藏>我的相册
     */
    public Comparator<RemoteFile> sortByName = new Comparator<RemoteFile>() {
        @Override
        public int compare(RemoteFile o1, RemoteFile o2) {
            if (isFixed(o1, "葫芦备份")) {
                return -1;
            }
            if (isFixed(o2, "葫芦备份")) {
                return 1;
            }

            if (isFixed(o1, "备份恢复")) {
                return -1;
            }
            if (isFixed(o2, "备份恢复")) {
                return 1;
            }

            if (isFixed(o1, "收到文件")) {
                return -1;
            }
            if (isFixed(o2, "收到文件")) {
                return 1;
            }

            if (isFixed(o1, "我的收藏")) {
                return -1;
            }
            if (isFixed(o2, "我的收藏")) {
                return 1;
            }

            if (isFixed(o1, "我的相册")) {
                return -1;
            }
            if (isFixed(o2, "我的相册")) {
                return 1;
            }

            if ("2".equals(o1.getCategory())) {
                //o1是文件夹
                if ("2".equals(o2.getCategory())) {
                    //都是文件夹
                    return collator.compare(o1.getFileName(), o2.getFileName());
                } else {
                    return -1;
                }
            } else {
                //o1是文件
                if ("2".equals(o2.getCategory())) {
                    return 1;
                } else {
                    //都是文件
                    return collator.compare(o1.getFileName(), o2.getFileName());
                }
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
        rxPermissions = new RxPermissions(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutResourceId(), container, false);
        ButterKnife.bind(this, mContentView);
        //add by houxiansheng 2019-12-5 17:40:23 动态修改StatusBar的字体颜色：修改顶部时间颜色为黑色
        StatusBarUtil.setStatusBar(baseActivity, false, false);
        init();
        setUpView();
        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpData();
    }

    protected abstract int getLayoutResourceId();

    protected abstract void setUpView();

    protected abstract void setUpData();

    protected void init() {
    }

    private UploadChunk createPiece(String taskId, long index, File originFile, long chunkTotal, File partFile, String dirID) {
        UploadChunk chunk = new UploadChunk();
        chunk.setChunkId(taskId + "_chunk" + index);
        chunk.setTaskId(taskId);
        chunk.setTargetDirId(dirID);
        String str = Base64.encodeToString((originFile.getName()).getBytes(), Base64.DEFAULT);
        chunk.setFileName(str.replace("\n", ""));
        chunk.setChunkNum(index);
        chunk.setChunkSize(UploadTool.UPLOAD_CHUNK_SIZE);
        chunk.setChunkTotal(chunkTotal);
        chunk.setMd5(FileUtils.getFileMD5(originFile));
        chunk.setPartFile(partFile);
        return chunk;
    }


    public void startUpload(File originFile, String dirID) {
        rxPermissions.request(Manifest.permission.FOREGROUND_SERVICE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //创建上传任务id
                            ApiRetrofit.getInstance().createTask()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.computation())
                                    .subscribe(new Observer<CreateTaskIdResponse>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(CreateTaskIdResponse createTaskIdResponse) {
                                            if (createTaskIdResponse.isSuccess()) {
                                                String taskId = createTaskIdResponse.getData().getTaskId();
                                                //切割文件
                                                List<File> files = UploadTool.splitFile(originFile);
                                                //向上传表插入一条记录
                                                UploadEntity entity = new UploadEntity();
                                                entity.setTaskId(taskId);
                                                entity.setStatus(0);//等待中
                                                entity.setUpdateTime(System.currentTimeMillis());
                                                entity.setFileSize(originFile.length());
                                                entity.setDirId(dirID);
                                                entity.setCreateTime(System.currentTimeMillis());
                                                entity.setFileName(originFile.getName());
                                                entity.setPath(originFile.getAbsolutePath());

                                                Observable.create(new ObservableOnSubscribe<Object>() {

                                                    @Override
                                                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                                        EntityManager.getInstance().getUploadEntityDao().insert(entity);
                                                        emitter.onNext("success");
                                                    }
                                                }).subscribe(new Consumer<Object>() {

                                                    @Override
                                                    public void accept(Object o) throws Exception {
                                                        Intent intent = new Intent(Constants.UPLOAD_TABLE_INSERT);
                                                        intent.putExtra("taskId", taskId);
                                                        LocalBroadcastManager.getInstance(baseActivity).sendBroadcast(intent);

                                                        //给每个分片创建Runnable
                                                        for (int i = 0; i < files.size(); i++) {
                                                            File f = files.get(i);
                                                            UploadChunk chunk = createPiece(taskId, i, originFile, files.size(), f, dirID);
                                                            FileUploader.UploadRequester requester = new FileUploader.UploadRequester();
                                                            requester.uploadFile(baseActivity, originFile.length(), chunk);
                                                        }
                                                    }
                                                });
                                            } else {
                                                ToastUtil.showCenterForBusiness(baseActivity, "创建上传任务失败!");
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            ToastUtil.showCenterForBusiness(baseActivity, "创建上传任务失败!");
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        }
                    }
                });
    }

    public interface DataCallBack {
        void onReadFinished(List<VideoList> videos);
    }

}
