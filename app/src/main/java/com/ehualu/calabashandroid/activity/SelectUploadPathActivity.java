package com.ehualu.calabashandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.MoveAdapter;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.dialog.CreateFolderAndReNameDialog;
import com.ehualu.calabashandroid.interfaces.NormalDialogInterface;
import com.ehualu.calabashandroid.interfaces.OnItemClickInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.responseBean.CreateFolderResponse;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;
import com.ehualu.calabashandroid.responseBean.ResponseFileSearchBean;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.RemoteConverter;
import com.ehualu.calabashandroid.utils.SortChineseName;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.ehualu.calabashandroid.widget.DrawableCenterRadioButton;
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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 选择上传的目标路径
 */
public class SelectUploadPathActivity extends BaseActivity implements OnItemClickInterface, NormalDialogInterface {

    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.ivCancel)
    TextView ivCancel;
    @BindView(R.id.recyFolders)
    RecyclerView recyFolders;
    @BindView(R.id.tvCreateFolder)
    DrawableCenterRadioButton tvCreateFolder;
    @BindView(R.id.tvEnsure)
    TextView tvEnsure;

    private List<RemoteFile> remoteFiles = new ArrayList<>();
    private List<RemoteFile> titles = new ArrayList<>();
    private MoveAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_upload_path);

        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        ButterKnife.bind(this);

        adapter = new MoveAdapter(this, remoteFiles);
        recyFolders.setLayoutManager(new LinearLayoutManager(this));
        recyFolders.setAdapter(adapter);
        adapter.setListener(this);

        initListener();
        getRootDirectory();
    }

    private void initListener() {
        llBack.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        tvEnsure.setOnClickListener(this);
        tvCreateFolder.setOnClickListener(this);
    }


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
                        RemoteFile remoteFile = new RemoteFile();
                        remoteFile.setID("0");
                        remoteFile.setFileName("文件");
                        titles.add(remoteFile);
                        initData(responseFileSearchBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterHasImageToast(SelectUploadPathActivity.this, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 是否是根目录
     */
    public boolean isRootDirectory() {
        if (titles.size() == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //返回
        if (isRootDirectory()) {
            finish();
            super.onBackPressed();
        } else {
            goPrevious();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.llBack:
                //返回
                if (isRootDirectory()) {
                    finish();
                } else {
                    goPrevious();
                }
                break;
            case R.id.ivCancel:
                //右上角的取消
                finish();
                break;
            case R.id.tvEnsure:
                //确定上传路径
                Intent intent = new Intent();
                intent.putExtra("uploadPath", titles.get(titles.size() - 1).getID());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.tvCreateFolder:
                //在当前目录创建文件夹
                if (titles.size() == 0) {
                    ToastUtil.showCenterForBusiness(SelectUploadPathActivity.this, "无法获取创建目录！");
                }
                new CreateFolderAndReNameDialog(this, "新建文件夹", "新建文件夹", this).show();
                break;
        }
    }

    /**
     * 进入上一级目录
     */
    public void goPrevious() {
        if (titles.size() == 1 || titles.size() == 0) {
            //等于0，就是没有请求到任何数据
            return;
        }
        titles.remove(titles.size() - 1);
        ApiRetrofit.getInstance().getFileList(titles.get(titles.size() - 1).getID(), "", "", "", "", "", "", ""
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
                        ToastUtil.showCenterHasImageToast(SelectUploadPathActivity.this, "读取文件列表失败！");
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

    private void goNext(RemoteFile remoteFile) {
        titles.add(remoteFile);
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
                        ToastUtil.showCenterHasImageToast(SelectUploadPathActivity.this, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onConfirm(String text) {
        String dir = titles.get(titles.size() - 1).getID();
        if (dir.equals("0")) {//根目录过滤掉固定文件夹
            if (text.equals("我的相册") || text.equals("我的收藏") || text.equals("回收站")
                    || text.equals("葫芦备份") || text.equals("备份恢复") || text.equals("收到文件")) {
                ToastUtil.showCenterHasImageToast(this, "文件夹已存在");
                return;
            }
        }
        ApiRetrofit.getInstance().postCreateFolder(text, dir)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CreateFolderResponse>() {
                    @Override
                    public void accept(CreateFolderResponse createFolderResponse) throws Exception {
                        MyLog.d(createFolderResponse.toString());
                        if (createFolderResponse.isSuccess()) {
                            ToastUtil.showCenterHasImageToast(SelectUploadPathActivity.this, "新建文件夹成功");
                            RemoteFile rf = titles.get(titles.size() - 1);
                            titles.remove(titles.size() - 1);
                            goNext(rf);
                        } else {
                            ToastUtil.showCenterHasImageToast(SelectUploadPathActivity.this,
                                    createFolderResponse.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onCancel() {

    }
}
