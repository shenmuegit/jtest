package com.ehualu.calabashandroid.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.MoveAdapter;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.interfaces.OnItemClickInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;
import com.ehualu.calabashandroid.responseBean.ResponseFileSearchBean;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.RemoteConverter;
import com.ehualu.calabashandroid.utils.SortChineseName;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-17 09:57:50
 * <p>
 * describe：移动操作界面
 */
public class MoveActivity extends BaseActivity implements OnItemClickInterface {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.move_recyclerView)
    RecyclerView moveRecyclerView;
    @BindView(R.id.tv_move_cancel)
    TextView tvMoveCancel;
    @BindView(R.id.tv_move_confirm)
    TextView tvMoveConfirm;
    private MoveAdapter adapter;
    public List<RemoteFile> titleList = new ArrayList<>();//标题List
    private List<RemoteFile> remoteFiles = new ArrayList<>();
    private List<RemoteFile> allList = new ArrayList<>();//要移动的所有文件
    private List<String> fileList = new ArrayList<>();//要移动的文件列表
    private List<String> folderList = new ArrayList<>();//要移动的文件夹列表
    public static final int resultCodeMove = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        ButterKnife.bind(this);
        initViewAndData();
        initListener();
    }

    private void initViewAndData() {
        imgRight.setVisibility(View.GONE);
        tvTitle.setText("选择移动位置");

        adapter = new MoveAdapter(this, remoteFiles);
        moveRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        moveRecyclerView.setAdapter(adapter);
        adapter.setListener(this);

        getMoveList();
        getRootDirectory();

    }

    private void initListener() {
        llBack.setOnClickListener(this);
        tvMoveCancel.setOnClickListener(this);
        tvMoveConfirm.setOnClickListener(this);
    }

    /**
     * 获取根目录
     */
    private void getRootDirectory() {
        ApiRetrofit.getInstance().getFileList("0", "", "", "", "", "", "", "", "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseFileSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseFileSearchBean responseFileSearchBean) {
                        //                        MyLog.d(responseFileSearchBean.toString());
                        RemoteFile remoteFile = new RemoteFile();
                        remoteFile.setID("0");
                        remoteFile.setFileName("文件");
                        titleList.add(remoteFile);
                        initData(responseFileSearchBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterForBusiness(MoveActivity.this, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initData(ResponseFileSearchBean responseFileSearchBean) {
        if (!responseFileSearchBean.isSuccess()) {
            ToastUtil.showCenterNoImageToast(this, responseFileSearchBean.getMessage());
            return;
        }

        remoteFiles.clear();
        remoteFiles.addAll(RemoteConverter.getRemoteFiles(responseFileSearchBean));

        Iterator<RemoteFile> iterator = remoteFiles.iterator();
        while (iterator.hasNext()) {
            RemoteFile rf = iterator.next();
            if ("2".equals(rf.getCategory()) && "0".equals(rf.getParentId()) && rf.getFileName().equals("葫芦备份")) {
                iterator.remove();
            }
            if ("2".equals(rf.getCategory()) && "0".equals(rf.getParentId()) && rf.getFileName().equals("备份恢复")) {
                iterator.remove();
            }
            if ("2".equals(rf.getCategory()) && "0".equals(rf.getParentId()) && rf.getFileName().equals("收到文件")) {
                iterator.remove();
            }
            if ("2".equals(rf.getCategory()) && "0".equals(rf.getParentId()) && rf.getFileName().equals("回收站")) {
                iterator.remove();
            }
            if ("2".equals(rf.getCategory()) && "0".equals(rf.getParentId()) && rf.getFileName().equals("我的收藏")) {
                iterator.remove();
            }
            if ("2".equals(rf.getCategory()) && "0".equals(rf.getParentId()) && rf.getFileName().equals("我的相册")) {
                iterator.remove();
            }
            if ("1".equals(rf.getCategory())) {
                iterator.remove();//去掉所有文件，只保留文件夹
            }
        }

        Collections.sort(remoteFiles, new SortChineseName(this, true));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                if (isRootDirectory()) {//如果根目录，走退出逻辑
                    finish();
                } else {//不是根目录，回到上级目录
                    goPrevious();
                }

            case R.id.tv_move_cancel:

                break;

            case R.id.tv_move_confirm:
                if (fileList.size() > 0) {
                    fileMove();
                }

                if (folderList.size() > 0) {
                    folderMove();
                }
                break;
        }
    }

    /**
     * 获取要移动的数据：取出所有文件夹和所有文件，放入不同的List
     */
    private void getMoveList() {
        allList = (List<RemoteFile>) getIntent().getSerializableExtra("moveList");
        for (int i = 0; i < allList.size(); i++) {
            if (allList.get(i).getCategory().equals("1")) {
                fileList.add(allList.get(i).getID());
            } else {
                folderList.add(allList.get(i).getID());
            }
        }
    }

    /**
     * 是否是根目录
     */
    public boolean isRootDirectory() {
        if (titleList.size() == 1) {
            return true;
        }
        return false;
    }

    /**
     * 进入下一级目录
     */
    private void goNext(RemoteFile remoteFile) {
        titleList.add(remoteFile);
        ApiRetrofit.getInstance().getFileList(remoteFile.getID(), "", "", "", "", "", "", ""
                , "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseFileSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseFileSearchBean responseFileSearchBean) {
                        initData(responseFileSearchBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterHasImageToast(MoveActivity.this, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 进入上一级目录
     */
    public void goPrevious() {
        if (titleList.size() == 1) {
            return;
        }
        titleList.remove(titleList.size() - 1);
        ApiRetrofit.getInstance().getFileList(titleList.get(titleList.size() - 1).getID(), "", "", "", "", "", "", ""
                , ""
                , "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseFileSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseFileSearchBean responseFileSearchBean) {
                        initData(responseFileSearchBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterHasImageToast(MoveActivity.this, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 文件移动
     */
    private void fileMove() {
        ApiRetrofit.getInstance().postMove(titleList.get(titleList.size() - 1).getID(), "1", fileList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PublicResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PublicResponseBean responseBean) {
                        MyLog.d(responseBean.toString());
                        if (responseBean.isSuccess()) {
                            setResult(resultCodeMove);
                            finish();
                        } else {
                            ToastUtil.showCenterHasImageToast(MoveActivity.this, responseBean.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterHasImageToast(MoveActivity.this, "文件移动失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 文件夹移动
     */
    private void folderMove() {
        ApiRetrofit.getInstance().postMove(titleList.get(titleList.size() - 1).getID(), "2", folderList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PublicResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PublicResponseBean responseBean) {
                        MyLog.d(responseBean.toString());
                        if (responseBean.isSuccess()) {
                            setResult(resultCodeMove);
                            finish();
                        } else {
                            ToastUtil.showCenterHasImageToast(MoveActivity.this, responseBean.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterHasImageToast(MoveActivity.this, "文件夹移动失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        if (isRootDirectory()) {//如果根目录，走退出逻辑
            finish();
        } else {//不是根目录，回到上级目录
            goPrevious();
        }
    }

    @Override
    public void onItemClick(RemoteFile remoteFile) {
        goNext(remoteFile);
    }

    @Override
    public void onItemLongCick(RemoteFile remoteFile) {

    }

    @Override
    public void selectedAll() {

    }

    @Override
    public void notSelectedAll() {

    }

    @Override
    public void selectCount(List<RemoteFile> selectedList, int count) {

    }
}
