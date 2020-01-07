package com.ehualu.calabashandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.activity.AlbumActivity;
import com.ehualu.calabashandroid.activity.SelectUploadPathActivity;
import com.ehualu.calabashandroid.adapter.PhotoAdapter;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.db.UploadEntityDao;
import com.ehualu.calabashandroid.db.entity.UploadEntity;
import com.ehualu.calabashandroid.db.manager.EntityManager;
import com.ehualu.calabashandroid.interfaces.OnItemClickInterface;
import com.ehualu.calabashandroid.interfaces.PhotoOnItemClickInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.utils.LocalImageHelper;
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

public class UploadedPhotoFragment extends BaseFragment implements PhotoOnItemClickInterface {

    @BindView(R.id.recyPhoto)
    RecyclerView recyPhoto;
    @BindView(R.id.tvSelectPosition)
    DrawableCenterRadioButton tvSelectPosition;
    @BindView(R.id.tvEnsure)
    TextView tvEnsure;
    @BindView(R.id.llBottomSelectPath)
    LinearLayout llBottomSelectPath;

    private List<String> paths = new ArrayList<>();
    public PhotoAdapter adapter;
    private int buck_id;
    private AlbumActivity activity;
    String dirID;

    private List<String> uploadedPaths = new ArrayList<>();//保存已经上传的文件列表

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AlbumActivity) context;
        dirID = activity.dirID;
    }

    public UploadedPhotoFragment(int buck_id) {
        this.buck_id = buck_id;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_photo;
    }

    @Override
    protected void setUpView() {
        adapter = new PhotoAdapter(activity, paths, UPLOADED, dirID);
        recyPhoto.setLayoutManager(new GridLayoutManager(activity, 4));
        DividerItemDecoration decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.vertical_line2));
        recyPhoto.addItemDecoration(decoration);
        recyPhoto.setAdapter(adapter);
        adapter.setListener(this);

        tvSelectPosition.setOnClickListener(this);
        tvEnsure.setOnClickListener(this);
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
                        LocalImageHelper helper = new LocalImageHelper();
                        helper.getImagesByAlbum(activity, buck_id, new AlbumActivity.AlbumCallBack() {
                            @Override
                            public void onReadFinished(List<String> images) {
                                Iterator<String> iterator = images.iterator();
                                while (iterator.hasNext()) {
                                    String img = iterator.next();
                                    if (!strings.contains(img)) {
                                        iterator.remove();
                                    }
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        paths.addAll(images);
                                        adapter.notifyDataSetChanged();
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
            case R.id.tvSelectPosition:
                Intent intent = new Intent(activity, SelectUploadPathActivity.class);
                startActivityForResult(intent, REQUEST_TARGET_PATH);
                break;
            case R.id.tvEnsure:
                ArrayList<String> checkedPaths = adapter.getCheckedList();
                if (checkedPaths.size() == 0) {
                    ToastUtil.showCenterForBusiness(activity, "请选择要上传的图片！");
                } else {
                    //开始文件上传
                    for (String path : checkedPaths) {
                        File file = new File(path);
                        if (file.exists()) {
                            startUpload(file, dirID);
                        }
                    }
                    ToastUtil.showCenterHasImageToast(activity, "文件已添加至传输列表");
                    MyApp.closeAllNeedDestoryActs();
                }
                break;
        }
    }

    @Override
    public void onItemClick(String t) {

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
    public void selectCount(List<String> selectedList, int count) {

    }

}
