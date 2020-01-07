package com.ehualu.calabashandroid.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.activity.AudioActivity;
import com.ehualu.calabashandroid.activity.BackUpActivity;
import com.ehualu.calabashandroid.activity.MainActivity;
import com.ehualu.calabashandroid.activity.RemoteDocumentListActivity;
import com.ehualu.calabashandroid.activity.RemotePhotoListActivity;
import com.ehualu.calabashandroid.activity.RemoteVideoListActivity;
import com.ehualu.calabashandroid.activity.TransferListActivity;
import com.ehualu.calabashandroid.activity.UploadFileTypeActivity;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.dialog.CreateFolderAndReNameDialog;
import com.ehualu.calabashandroid.interfaces.NormalDialogInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.responseBean.CreateFolderResponse;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;
import com.ehualu.calabashandroid.responseBean.ResponseFileSearchBean;
import com.ehualu.calabashandroid.testactivity.LayrenActivity;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.RemoteConverter;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment implements NormalDialogInterface {

    @BindView(R.id.tvWelcome)
    TextView tvWelcome;
    @BindView(R.id.ivUpload)
    ImageView ivUpload;
    @BindView(R.id.ivTransfer)
    ImageView ivTransfer;
    @BindView(R.id.firstLine)
    LinearLayout firstLine;
    @BindView(R.id.secondLine)
    LinearLayout secondLine;
    @BindView(R.id.ivHulu)
    ImageView ivHulu;
    @BindView(R.id.llVideo)
    LinearLayout llVideo;
    @BindView(R.id.llPhoto)
    LinearLayout llPhoto;
    @BindView(R.id.ll_backup)
    LinearLayout llBackup;
    @BindView(R.id.llDocument)
    LinearLayout llDocument;
    @BindView(R.id.ll_audio)
    LinearLayout llAudio;

    private List<RemoteFile> remoteFiles = new ArrayList<>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setUpView() {
        ImmersionBar.with(this).fitsSystemWindows(false).statusBarDarkFont(true).init();
        ivUpload.setOnClickListener(this);
        ivTransfer.setOnClickListener(this);
        llVideo.setOnClickListener(this);
        llPhoto.setOnClickListener(this);
        llDocument.setOnClickListener(this);
        llBackup.setOnClickListener(this);
        llAudio.setOnClickListener(this);

        ivHulu.setOnClickListener(this);

        //高厅添加,只为跳转到接口测试界面.无其他作用;
        tvWelcome.setOnClickListener(v ->
        {
            String authorization = "Basic " + Base64.encodeToString(("18211185976:123456abc").getBytes(),
                    Base64.DEFAULT);
            authorization = authorization.replaceAll("[\\n]", "");
            Log.d("tt", authorization);
            startActivity(new Intent(baseActivity, LayrenActivity.class));
        });

        //加载GIF
        Glide.with(this).load(R.drawable.feedback).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target,
                                        boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target target, DataSource dataSource,
                                           boolean isFirstResource) {
                //                if (resource instanceof GifDrawable) {
                //                    ((GifDrawable) resource).setLoopCount(1); //只加载一次
                //                }
                return false;
            }
        }).into(ivHulu);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((MainActivity) baseActivity).getMainBg().setVisibility(View.VISIBLE);
        }
    }

    private void requestData() {
        ApiRetrofit.getInstance().getFileList("0", "", "", "", "", "", "", "", "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseFileSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseFileSearchBean responseFileSearchBean) {
                        remoteFiles.addAll(RemoteConverter.getRemoteFiles(responseFileSearchBean));
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterHasImageToast(baseActivity, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void setUpData() {
        ApiRetrofit.getInstance().getFileList("0", "", "", "", "", "", "", "", "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseFileSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseFileSearchBean responseFileSearchBean) {
                        remoteFiles.addAll(RemoteConverter.getRemoteFiles(responseFileSearchBean));
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterHasImageToast(baseActivity, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivHulu:
                //                File file = new File(Environment.getExternalStorageDirectory(), "aaabbb.mp3");
                //                startUpload(file, "0");
                //
                //                File file2 = new File(Environment.getExternalStorageDirectory(), "zzz001.doc");
                //                startUpload(file2, "0");
                break;
            case R.id.ivUpload:
                Intent intent = new Intent();
                intent.setClass(baseActivity, UploadFileTypeActivity.class);
                intent.putExtra("dirID", "0");
                startActivityForResult(intent, UploadFileTypeActivity.START_UPLOAD_FILE_TYPE_ACTIVITY);
                break;
            case R.id.ivTransfer:
                startActivity(new Intent(baseActivity, TransferListActivity.class));
                break;
            case R.id.llVideo:
                startActivity(new Intent(baseActivity, RemoteVideoListActivity.class));
                break;
            case R.id.llPhoto:
                startActivity(new Intent(baseActivity, RemotePhotoListActivity.class));
                break;
            case R.id.llDocument:
                startActivity(new Intent(baseActivity, RemoteDocumentListActivity.class));
                break;
            case R.id.ll_backup:
                startActivity(new Intent(baseActivity, BackUpActivity.class));
                break;
            case R.id.ll_audio:
                startActivity(new Intent(baseActivity, AudioActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UploadFileTypeActivity.START_UPLOAD_FILE_TYPE_ACTIVITY:
                switch (resultCode) {
                    case RESULT_OK:
                        //弹出新建文件夹
                        new CreateFolderAndReNameDialog(baseActivity, "新建文件夹", "新建文件夹", this).show();
                        break;
                }
                break;
        }
    }

    @Override
    public void onConfirm(String text) {
        if (text.equals("我的相册") || text.equals("我的收藏") || text.equals("回收站")
                || text.equals("葫芦备份") || text.equals("备份恢复") || text.equals("收到文件")) {
            ToastUtil.showCenterHasImageToast(baseActivity, "文件夹已存在");
            return;
        }

        //检索当前目录的文件夹名称
        if (text.equals("新建文件夹")) {
            List<String> folders = new ArrayList<>();
            for (RemoteFile rf : remoteFiles) {
                if ("2".equals(rf.getCategory())) {
                    folders.add(rf.getFileName());
                }
            }

            boolean enter = true;
            int i = 1;
            while (enter) {
                if (folders.contains(text)) {
                    text = "新建文件夹";
                    text += i;
                    i++;
                } else {
                    break;
                }
            }
        }

        final String newFolderName = text;

        ApiRetrofit.getInstance().postCreateFolder(text, "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CreateFolderResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CreateFolderResponse createFolderResponse) {
                        if (createFolderResponse.isSuccess()) {
                            ToastUtil.showCenterHasImageToast(baseActivity, "新建文件夹成功");
                            requestData();

                            //切换到FileFragment，然后再跳转到新建文件夹里面
                            RemoteFile rf = new RemoteFile();
                            rf.setID(createFolderResponse.getData().getDirId());
                            rf.setFileName(newFolderName);
                            ((MainActivity) baseActivity).storageFragment.setNewFolder(rf);
                            ((MainActivity) baseActivity).switchFileFragment();
                        } else {
                            ToastUtil.showCenterHasImageToast(baseActivity,
                                    createFolderResponse.getMessage());
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

    @Override
    public void onCancel() {

    }
}
