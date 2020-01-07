package com.ehualu.calabashandroid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.AudioAdapter;
import com.ehualu.calabashandroid.adapter.AudioParentAdapter;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.base.BaseAdapter;
import com.ehualu.calabashandroid.dialog.CreateFolderAndReNameDialog;
import com.ehualu.calabashandroid.interfaces.BottomMoreInterface;
import com.ehualu.calabashandroid.interfaces.ConfirmCancelInterface;
import com.ehualu.calabashandroid.interfaces.NormalDialogInterface;
import com.ehualu.calabashandroid.interfaces.OnItemClickInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.model.RemoteFileListSortByTime;
import com.ehualu.calabashandroid.popupwindow.BottomDeletePopupWindow;
import com.ehualu.calabashandroid.popupwindow.BottomMorePopupWindow;
import com.ehualu.calabashandroid.popupwindow.SharePopupWindow;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;
import com.ehualu.calabashandroid.responseBean.ResponseFileSearchBean;
import com.ehualu.calabashandroid.utils.BroadcastUtils;
import com.ehualu.calabashandroid.utils.Constants;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.OperationUtils;
import com.ehualu.calabashandroid.utils.RemoteConverter;
import com.ehualu.calabashandroid.utils.SortByTime;
import com.ehualu.calabashandroid.utils.SortChineseName;
import com.ehualu.calabashandroid.utils.TimeUtils;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-18 14:17:36
 * <p>
 * describe：音频界面
 */
public class AudioActivity extends BaseActivity implements OnItemClickInterface, BottomMoreInterface {
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivFileEdit)
    ImageView ivFileEdit;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.recyclerView_audio)
    RecyclerView recyclerViewAudio;
    @BindView(R.id.tv_select_cancel)
    TextView tvSelectCancel;
    @BindView(R.id.tv_select_num)
    TextView tvSelectNum;
    @BindView(R.id.tv_select_all_or_not)
    TextView tvSelectAllOrNot;
    @BindView(R.id.ll_top_select)
    LinearLayout llTopSelect;
    @BindView(R.id.ll_top_title)
    LinearLayout llTopTitle;
    @BindView(R.id.ll_download)
    LinearLayout llDownload;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.ll_backup)
    LinearLayout llBackup;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.ll_bottom_edit)
    LinearLayout llBottomEdit;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.ll_empty_page)
    LinearLayout llEmptyPage;

    private BaseAdapter adapter;

    private PopupWindow fileEditPopup;
    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_TIME = 2;
    public int sortType = SORT_BY_NAME;
    private List<RemoteFile> remoteFiles = new ArrayList<>();
    private int requestCodeMove = 1;//移动

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        ButterKnife.bind(this);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        initViewAndData();
        initListener();
    }

    private void initViewAndData() {
        tvTitle.setText("音频");
        adapter = new AudioAdapter(remoteFiles, R.layout.item_audio_child);
        adapter.setListener(this);
        recyclerViewAudio.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAudio.setAdapter(adapter);
        getFileList();
        ivDownload.setBackgroundResource(R.drawable.icon_bottom_download);
    }

    private void getFileList() {
        ApiRetrofit.getInstance().getFileList("", "4", "", "", "", "", "", "", "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseFileSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseFileSearchBean responseFileSearchBean) {
                        MyLog.d(responseFileSearchBean.toString());
                        remoteFiles.clear();
                        remoteFiles.addAll(RemoteConverter.getRemoteFiles(responseFileSearchBean));
                        Collections.sort(remoteFiles, new SortChineseName(AudioActivity.this, true));
                        if (remoteFiles.size() > 0) {
                            llEmptyPage.setVisibility(View.GONE);
                            recyclerViewAudio.setVisibility(View.VISIBLE);
                        } else {
                            llEmptyPage.setVisibility(View.VISIBLE);
                            recyclerViewAudio.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterForBusiness(AudioActivity.this, "读取音频列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initListener() {
        tvSearch.setOnClickListener(this);
        ivFileEdit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        tvSelectAllOrNot.setOnClickListener(this);
        tvSelectCancel.setOnClickListener(this);

        llDownload.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llBackup.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        llMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tvSearch:
                startActivity(new Intent(AudioActivity.this, CommonSearchActivity.class));
                break;
            case R.id.ivFileEdit:
                showOperationPopup();
                break;

            case R.id.imgBack:
            case R.id.tvTitle:
                finish();
                break;

            case R.id.rl05:
                setSelectStatus(true);  //进入选择模式
                if (fileEditPopup != null && fileEditPopup.isShowing()) {   //隐藏底部弹出框
                    fileEditPopup.dismiss();
                }
                break;
            case R.id.tv_select_all_or_not:
                SelectedAllOrNot();
                break;

            case R.id.tv_select_cancel:
                setSelectStatus(false);
                break;

            case R.id.ll_download:
                if (adapter.getCheckedList().size() > 0) {
                    for (Object file : adapter.getCheckedList()) {
                        BroadcastUtils.sendDownloadBroadcast((RemoteFile) file);
                    }
                    ToastUtil.showCenterHasImageToast(this, "文件已添加至传输列表");
                    setSelectStatus(false);
                } else {
                    ToastUtil.showCenterHasImageToast(this, "请先选择文件！");
                }
                break;
            case R.id.ll_share:
                if (adapter.getCheckedList().size() > 0) {
                    RemoteFile remoteFile = (RemoteFile) adapter.getCheckedList().get(0);
                    String path = Constants.DOWNLOAD_PATH + remoteFile.getFileName();
                    if (new File(path).exists()) {
                        remoteFile.setPath(path);
                        new SharePopupWindow(this, remoteFile).showPopupWindow(llShare);
                    } else {
                        ToastUtil.showCenterHasImageToast(this, "请先下载后再分享！");
                    }
                } else {
                    ToastUtil.showCenterHasImageToast(this, "请先选择文件！");
                }
                break;
            case R.id.ll_backup:
                if (adapter.getCheckedList().size() > 0) {
                    backup();
                } else {
                    ToastUtil.showCenterHasImageToast(this, "请先选择文件！");
                }

                break;
            case R.id.ll_delete:
                if (adapter.getCheckedList().size() > 0) {
                    new BottomDeletePopupWindow(this, deleteInterface, adapter.getCheckedList()).showMoreOperationPopup(llDelete);
                } else {
                    ToastUtil.showCenterHasImageToast(this, "请先选择文件！");
                }
                break;
            case R.id.ll_more:
                if (adapter.getCheckedList().size() > 0) {
                    new BottomMorePopupWindow(this, this, adapter.getCheckedList(), false).showMoreOperationPopup(llMore);
                } else {
                    ToastUtil.showCenterHasImageToast(this, "请先选择文件！");
                }
                break;

            case R.id.rl02:
                if (sortType == SORT_BY_NAME) {
                    return;
                }

                sortType = SORT_BY_NAME;
                recyclerViewAudio.setLayoutManager(new LinearLayoutManager(this));
                Collections.sort(remoteFiles, new SortChineseName(this, true));
                adapter = new AudioAdapter(remoteFiles, R.layout.item_audio_child);
                recyclerViewAudio.setAdapter(adapter);
                adapter.setListener(this);

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                setSelectStatus(false);
                break;

            case R.id.rl01:
                //按时间排序，首先判断当前视图样式
                if (sortType == SORT_BY_TIME) {
                    return;
                }
                sortType = SORT_BY_TIME;
                adapter = new AudioParentAdapter(this, convert());
                adapter.setListener(this);
                recyclerViewAudio.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewAudio.setAdapter(adapter);
                adapter.setListener(this);
                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                setSelectStatus(false);
                break;
        }
    }

    /**
     * 显示操作的底部弹窗
     */
    public void showOperationPopup() {
        Window window = getWindow();
        View contentView = View.inflate(this, R.layout.popup_bottom_edit, null);

        RelativeLayout rl01 = contentView.findViewById(R.id.rl01);
        RelativeLayout rl02 = contentView.findViewById(R.id.rl02);
        RelativeLayout rl03 = contentView.findViewById(R.id.rl03);
        RelativeLayout rl04 = contentView.findViewById(R.id.rl04);
        RelativeLayout rl05 = contentView.findViewById(R.id.rl05);

        rl01.setOnClickListener(this);
        rl02.setOnClickListener(this);
        rl03.setOnClickListener(this);
        rl04.setOnClickListener(this);
        rl05.setOnClickListener(this);

        TextView tvTimeSort = contentView.findViewById(R.id.tvTimeSort);
        TextView tvNameSort = contentView.findViewById(R.id.tvNameSort);

        ImageView dg01 = contentView.findViewById(R.id.dg01);
        ImageView dg02 = contentView.findViewById(R.id.dg02);

        rl03.setVisibility(View.GONE);
        rl04.setVisibility(View.GONE);

        if (sortType == SORT_BY_TIME) {
            setTextViewSelected(tvTimeSort, dg01);
        } else if (sortType == SORT_BY_NAME) {
            setTextViewSelected(tvNameSort, dg02);
        }

        fileEditPopup = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
        fileEditPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp2 = window.getAttributes();
                lp2.alpha = 1f;
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setAttributes(lp2);
            }
        });
        fileEditPopup.setOutsideTouchable(true);
        fileEditPopup.setFocusable(true);
        fileEditPopup.showAtLocation(ivFileEdit, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
    }

    public List<RemoteFileListSortByTime> convert() {
        List<RemoteFileListSortByTime> list = new ArrayList<>();
        Map<String, List<RemoteFile>> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {
                // 降序排序
                return obj2.compareTo(obj1);
            }
        });
        for (RemoteFile file : remoteFiles) {
            String key = TimeUtils.longToString(file.getUpdateTime(), "yyyy-MM-dd");
            if (map.containsKey(key)) {
                List<RemoteFile> tempList = map.get(key);
                tempList.add(file);
                map.put(key, tempList);
            } else {
                List<RemoteFile> tempList = new ArrayList<>();
                tempList.add(file);
                map.put(key, tempList);
            }
        }

        for (Map.Entry<String, List<RemoteFile>> entry : map.entrySet()) {
            RemoteFileListSortByTime ocFileListByTime = new RemoteFileListSortByTime();
            try {
                ocFileListByTime.setTime(TimeUtils.stringToLong(entry.getKey(), "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //排序操作
            if (sortType == SORT_BY_TIME) {
                Collections.sort(entry.getValue(), new SortByTime());
            } else if (sortType == SORT_BY_NAME) {
                Collections.sort(entry.getValue(), new SortChineseName(this, true));
            }

            ocFileListByTime.setList(entry.getValue());
            ocFileListByTime.setShowDate(entry.getKey());
            list.add(ocFileListByTime);
        }
        return list;
    }

    private void setTextViewSelected(TextView tv, ImageView duigou) {
        Drawable leftDrawable;
        switch (tv.getId()) {
            case R.id.tvTimeSort:
                leftDrawable = getResources().getDrawable(R.drawable.bottom_popup_time_sort_blue);
                break;
            case R.id.tvNameSort:
                leftDrawable = getResources().getDrawable(R.drawable.bottom_popup_name_sort_blue);
                break;
            default:
                leftDrawable = getResources().getDrawable(R.drawable.bottom_popup_select_black);
                break;
        }
        leftDrawable.setBounds(0, 0, DensityUtil.dip2px(22, this), DensityUtil.dip2px(22, this));
        tv.setCompoundDrawables(leftDrawable, null, null, null);
        tv.setTextColor(Color.parseColor("#5BA0E9"));
        duigou.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (adapter.isSelectMode()) {
                setSelectStatus(false);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * add by houxiansheng 2019-12-12 11:58:43 设置选择状态
     */
    public void setSelectStatus(boolean isSelectStatus) {
        if (isSelectStatus) {
            llTopTitle.setVisibility(View.GONE);
            llTopSelect.setVisibility(View.VISIBLE);
            adapter.setSelectMode(true);
            llBottomEdit.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            llTopTitle.setVisibility(View.VISIBLE);
            llTopSelect.setVisibility(View.GONE);
            adapter.setSelectMode(false);
            adapter.removeAllCheckedList();
            llBottomEdit.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
        setSelectMode(isSelectStatus); //更新adapter的选中状态
    }

    public void setSelectMode(boolean b) {
        if (b) {
            //将搜索框后面的编辑图标置灰
            ivFileEdit.setImageResource(R.mipmap.frg_file_edit_disabled);
            ivFileEdit.setClickable(false);
        } else {
            ivFileEdit.setImageResource(R.mipmap.frg_file_edit);
            ivFileEdit.setClickable(true);
        }
    }

    /**
     * add by houxiansheng 2019-12-12 15:36:08 全选、全不选
     */
    public void SelectedAllOrNot() {
        int size = 0;
        if (adapter instanceof AudioParentAdapter) {
            size = ((AudioParentAdapter) adapter).getTotalSize();
        } else if (adapter instanceof AudioAdapter) {
            size = adapter.getData().size();
        }
        if (adapter.getCheckedList().size() < size) {
            adapter.removeAllCheckedList();
            if (adapter instanceof AudioAdapter) {
                List<RemoteFile> allList = adapter.getData();
                for (int i = 0; i < allList.size(); i++) {
                    RemoteFile file = allList.get(i);
                    adapter.addCheckList(file);
                }
            } else if (adapter instanceof AudioParentAdapter) {
                List<RemoteFileListSortByTime> list = adapter.getData();
                for (int i = 0; i < list.size(); i++) {
                    RemoteFileListSortByTime remoteFileListSortByTime = list.get(i);
                    for (int j = 0; j < remoteFileListSortByTime.getList().size(); j++) {
                        RemoteFile ocFile = remoteFileListSortByTime.getList().get(j);
                        adapter.addCheckList(ocFile);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        } else {
            adapter.removeAllCheckedList();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(RemoteFile file) {

    }

    @Override
    public void onItemLongCick(RemoteFile file) {
        setSelectStatus(true);
    }

    @Override
    public void selectedAll() {
        tvSelectAllOrNot.setText("全选");
    }

    @Override
    public void notSelectedAll() {
        tvSelectAllOrNot.setText("全不选");
    }

    @Override
    public void selectCount(List<RemoteFile> selectedList, int count) {
        tvSelectNum.setText(String.format(getResources().getString(R.string.select_count), count + ""));
        if (count > 1) {
            llShare.setOnClickListener(null);
            ivShare.setBackgroundResource(R.drawable.icon_bottom_share_not);
            tvShare.setTextColor(Color.parseColor("#ffc1c1c0"));
        } else {
            llShare.setOnClickListener(this);
            ivShare.setBackgroundResource(R.drawable.icon_bottom_share);
            tvShare.setTextColor(Color.parseColor("#ff000000"));
        }
    }

    @Override
    public void onMove(List<RemoteFile> list) {
        Intent intent = new Intent();
        intent.putExtra("moveList", (Serializable) adapter.getCheckedList());
        intent.setClass(this, MoveActivity.class);
        startActivityForResult(intent, requestCodeMove);
        setSelectStatus(false);
    }

    @Override
    public void onRename(RemoteFile remoteFile) {
        new CreateFolderAndReNameDialog(this, "重命名", remoteFile.getFileName(), new NormalDialogInterface() {
            @Override
            public void onConfirm(String text) {
                ApiRetrofit.getInstance().postRename(text, remoteFile.getID(), remoteFile.getCategory())
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
                                    getFileList();
                                    setSelectStatus(false);
                                } else {
                                    ToastUtil.showCenterHasImageToast(AudioActivity.this, responseBean.getMessage());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showCenterForBusiness(AudioActivity.this, "重命名失败！");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }

            @Override
            public void onCancel() {

            }
        }).show();
    }

    /**
     * 葫芦备份
     */
    private void backup() {
        List<RemoteFile> allList = adapter.getCheckedList();//选中的所有列表
        List<String> fileList = new ArrayList<>();//文件列表
        List<String> folderList = new ArrayList<>();//文件夹列表
        for (int i = 0; i < allList.size(); i++) {
            if (allList.get(i).getCategory().equals("1")) {
                fileList.add(allList.get(i).getID());
            } else {
                folderList.add(allList.get(i).getID());
            }
        }
        ApiRetrofit.getInstance().postBackUp(folderList, fileList)
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
                            ToastUtil.showCenterHasImageToast(AudioActivity.this, "文件已添加至葫芦备份");
                            setSelectStatus(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterForBusiness(AudioActivity.this, "葫芦备份失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    int sum = 0;
    /**
     * 删除操作
     */
    ConfirmCancelInterface deleteInterface = new ConfirmCancelInterface() {
        @Override
        public void onCancel(List<RemoteFile> list) {

        }

        @Override
        public void onConfirm(List<RemoteFile> list) {
            OperationUtils.deleteRemoteFile(AudioActivity.this, list);
        }
    };

    /**
     * 重命名
     */
    NormalDialogInterface reNameInterface = new NormalDialogInterface() {
        @Override
        public void onConfirm(String text) {

        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    public void onInfo(RemoteFile remoteFile) {
        Intent intent = new Intent();
        intent.setClass(this, FileInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("remoteFile", remoteFile);
        intent.putExtras(bundle);
        startActivity(intent);
        setSelectStatus(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == requestCodeMove && resultCode == MoveActivity.resultCodeMove) {//移动操作后刷新界面
            getFileList();
        }
    }
}
