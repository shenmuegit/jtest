package com.ehualu.calabashandroid.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.activity.BackUpActivity;
import com.ehualu.calabashandroid.activity.CommonSearchActivity;
import com.ehualu.calabashandroid.activity.EditFileActivity;
import com.ehualu.calabashandroid.activity.FileInfoActivity;
import com.ehualu.calabashandroid.activity.MainActivity;
import com.ehualu.calabashandroid.activity.MoveActivity;
import com.ehualu.calabashandroid.activity.RemotePhotoListActivity;
import com.ehualu.calabashandroid.activity.TransferListActivity;
import com.ehualu.calabashandroid.activity.UploadFileTypeActivity;
import com.ehualu.calabashandroid.adapter.FileAdapter;
import com.ehualu.calabashandroid.adapter.FileFragmentSortByTimeAdapter;
import com.ehualu.calabashandroid.adapter.decoration.DividerItemDecoration03;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.api.bean.MyDownloadTask;
import com.ehualu.calabashandroid.base.BaseAdapter;
import com.ehualu.calabashandroid.base.BaseFragment;
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
import com.ehualu.calabashandroid.responseBean.CreateFolderResponse;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class FileFragment extends BaseFragment implements OnItemClickInterface, BottomMoreInterface,
        NormalDialogInterface {

    @BindView(R.id.tvFile)
    TextView tvFile;
    @BindView(R.id.ivUpload)
    ImageView ivUpload;
    @BindView(R.id.ivTransfer)
    ImageView ivTransfer;
    @BindView(R.id.viewLine)
    View viewLine;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.ivFileEdit)
    ImageView ivFileEdit;
    @BindView(R.id.ll_top_title)
    LinearLayout llTopTitle;
    @BindView(R.id.ll_top_select)
    LinearLayout llTopSelect;
    @BindView(R.id.tv_select_cancel)
    TextView tvSelectCancel;
    @BindView(R.id.tv_select_num)
    TextView tvSelectNum;
    @BindView(R.id.tv_select_all_or_not)
    TextView tvSelectAllOrNot;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    public boolean isSelectAll = true;//是否全选
    @BindView(R.id.recyFiles)
    RecyclerView recyFiles;
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
    @BindView(R.id.tv_upload_file)
    TextView tvUploadFile;
    @BindView(R.id.emptyPage)
    LinearLayout emptyPage;

    private BaseAdapter adapter;

    private PopupWindow fileEditPopup;
    private DividerItemDecoration03 decoration03;

    private List<RemoteFile> remoteFiles = new ArrayList<>();
    public static List<MyDownloadTask> tasks = new ArrayList<>();
    public List<RemoteFile> titleList = new ArrayList<>();//标题List

    public static final int requestCodeMove = 1;//移动

    public boolean isCreateFolder = false;
    public RemoteFile newFolder;

    public void setNewFolder(RemoteFile rf) {
        this.isCreateFolder = true;
        this.newFolder = rf;

        if (getActivity() != null) {
            //fragment已经被添加过了
            titleList.clear();//因为此时可能fragment已经有多层目录了，但是现在是重新请求根目录的第一级目录
            setRootDirectory();
            goNext(newFolder);

            isCreateFolder = false;
            newFolder = null;
        } else {
            //fragment还没有被添加
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_file;
    }

    @Override
    protected void setUpView() {
        ImmersionBar.with(this).fitsSystemWindows(false).statusBarDarkFont(true).init();
        ((MainActivity) baseActivity).getMainBg().setVisibility(View.GONE);
        tvUploadFile.setOnClickListener(this);

        adapter = new FileAdapter(remoteFiles, R.layout.item_file_grid);
        adapter.setListener(this);
        decoration03 = new DividerItemDecoration03(baseActivity);
        recyFiles.addItemDecoration(decoration03);
        recyFiles.setLayoutManager(new GridLayoutManager(baseActivity, 3));
        recyFiles.setAdapter(adapter);

        String path = Environment.getDownloadCacheDirectory().getAbsolutePath();
        Log.e("下载路径", path);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((MainActivity) baseActivity).getMainBg().setVisibility(View.GONE);
        }
    }

    public void refreshCurrentDir(String dirID) {
        if (titleList.size() > 0) {
            String currentDir = titleList.get(titleList.size() - 1).getID();
            if (dirID.equals(currentDir)) {
                onReFresh();
            }
        } else {
            ToastUtil.showCenterHasImageToast(baseActivity, "无法获取目录");
        }
    }

    @Override
    protected void setUpData() {
        initListener();
        setRootDirectory();

        if (isCreateFolder) {
            goNext(newFolder);
            isCreateFolder = false;
            newFolder = null;
        } else {
            ApiRetrofit.getInstance().getFileList("0", "", "", "", "", "", "", "", "", "", "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseFileSearchBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseFileSearchBean responseFileSearchBean) {
                            MyLog.d(responseFileSearchBean.toString());
                            if (responseFileSearchBean.isSuccess()) {
                                initData(responseFileSearchBean);
                            }
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
    }

    /**
     * 设置根目录
     */
    private void setRootDirectory() {
        RemoteFile remoteFile = new RemoteFile();
        remoteFile.setID("0");
        remoteFile.setFileName("文件");
        titleList.add(remoteFile);
        tvFile.setText(titleList.get(titleList.size() - 1).getFileName());
        ivBack.setVisibility(View.GONE);
    }

    private void initListener() {
        tvSearch.setOnClickListener(this);
        ivUpload.setOnClickListener(this);
        ivTransfer.setOnClickListener(this);
        tvSelectCancel.setOnClickListener(this);
        tvSelectAllOrNot.setOnClickListener(this);

        llDownload.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llBackup.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        llMore.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @OnClick({R.id.ivFileEdit})
    public void handleClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ivFileEdit:
                if (adapter instanceof FileFragmentSortByTimeAdapter) {
                    //按时间排序
                    if (viewType == GRID_VIEW) {
                        showOperationPopup(true);
                    } else if (viewType == LIST_VIEW) {
                        showOperationPopup(false);
                    }
                } else if (adapter instanceof FileAdapter) {
                    //按名称排序
                    RecyclerView.LayoutManager layoutManager = recyFiles.getLayoutManager();
                    if (layoutManager instanceof GridLayoutManager) {
                        showOperationPopup(true);
                    } else {
                        showOperationPopup(false);
                    }
                }
                break;
        }
    }

    private synchronized void initData(ResponseFileSearchBean responseFileSearchBean) {
        tvFile.setText(titleList.get(titleList.size() - 1).getFileName());

        if (titleList.size() == 1) {
            ivBack.setVisibility(View.GONE);
        } else {
            ivBack.setVisibility(View.VISIBLE);
        }

        if (!responseFileSearchBean.isSuccess()) {
            ToastUtil.showCenterNoImageToast(baseActivity, responseFileSearchBean.getMessage());
            return;
        }
        remoteFiles.clear();
        //        if (responseFileSearchBean.getData().size() != 0) {
        //            if (responseFileSearchBean.getData().get(0).getParentId().equals("0")) {
        //                RemoteFile fav = new RemoteFile();
        //                fav.setFileName("我的收藏");
        //                fav.setCategory("2");
        //                fav.setParentId("0");
        //                remoteFiles.add(fav);
        //
        //                RemoteFile album = new RemoteFile();
        //                album.setFileName("我的相册");
        //                album.setCategory("2");
        //                album.setParentId("0");
        //                remoteFiles.add(album);
        //            }
        //        }

        remoteFiles.addAll(RemoteConverter.getRemoteFiles(responseFileSearchBean));

        //去掉回收站
        Iterator<RemoteFile> iterator = remoteFiles.iterator();
        while (iterator.hasNext()) {
            RemoteFile rf = iterator.next();
            if ("2".equals(rf.getCategory()) && "0".equals(rf.getParentId()) && rf.getFileName().equals("回收站")) {
                iterator.remove();
            }
        }

        Collections.sort(remoteFiles, sortByName);

        if (remoteFiles.size() == 0) {
            recyFiles.setVisibility(View.GONE);
            emptyPage.setVisibility(View.VISIBLE);
        } else {
            recyFiles.setVisibility(View.VISIBLE);
            emptyPage.setVisibility(View.GONE);
        }

        //将文件列表内容缓存进数据库


        adapter.notifyDataSetChanged();
    }

    public void setSelectMode(boolean b) {
        //filefragment的属性
        isSelectMode = b;
        if (b) {
            adapter.setSelectMode(true);
            //将搜索框后面的编辑图标置灰
            ivFileEdit.setImageResource(R.mipmap.frg_file_edit_disabled);
            ivFileEdit.setClickable(false);
        } else {
            adapter.setSelectMode(false);
            ivFileEdit.setImageResource(R.mipmap.frg_file_edit);
            ivFileEdit.setClickable(true);
        }
    }

    public void removeAllDecorations() {
        for (int i = 0; i < recyFiles.getItemDecorationCount(); i++) {
            recyFiles.removeItemDecorationAt(i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_upload_file:
            case R.id.ivUpload:
                Intent intent = new Intent();
                intent.setClass(baseActivity, UploadFileTypeActivity.class);
                intent.putExtra("dirID", titleList.get(titleList.size() - 1).getID());
                startActivityForResult(intent, UploadFileTypeActivity.START_UPLOAD_FILE_TYPE_ACTIVITY);
                break;
            case R.id.tvSearch:
                startActivity(new Intent(baseActivity, CommonSearchActivity.class));
                break;
            case R.id.ivTransfer:
                startActivity(new Intent(baseActivity, TransferListActivity.class));
                break;

            case R.id.tv_select_cancel:
                isSelectStatus(false);
                break;

            case R.id.tv_select_all_or_not:
                SelectedAllOrNot();
                break;
            case R.id.rl03:
                //切换到缩略图模式，首先判断当前排序规则
                if (viewType == GRID_VIEW) {
                    //当前已经是缩略图模式
                    return;
                }
                viewType = GRID_VIEW;
                removeAllDecorations();
                if (sortType == SORT_BY_NAME) {
                    recyFiles.addItemDecoration(decoration03);
                    recyFiles.setLayoutManager(new GridLayoutManager(baseActivity, 3));
                    adapter = new FileAdapter(remoteFiles, R.layout.item_file_grid);
                    recyFiles.setAdapter(adapter);
                } else if (sortType == SORT_BY_TIME) {
                    recyFiles.setLayoutManager(new LinearLayoutManager(baseActivity));
                    adapter = new FileFragmentSortByTimeAdapter(baseActivity, convert(), viewType);
                    recyFiles.setAdapter(adapter);
                }
                adapter.setListener(this);

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                isSelectStatus(false);
                break;
            case R.id.rl04:
                //切换到列表模式，首先判断当前排序规则
                if (viewType == LIST_VIEW) {
                    //当前已经是列表模式
                    return;
                }
                viewType = LIST_VIEW;
                removeAllDecorations();
                if (sortType == SORT_BY_NAME) {
                    recyFiles.setLayoutManager(new LinearLayoutManager(baseActivity));
                    adapter = new FileAdapter(remoteFiles, R.layout.item_file_list);
                    recyFiles.setAdapter(adapter);
                } else if (sortType == SORT_BY_TIME) {
                    recyFiles.setLayoutManager(new LinearLayoutManager(baseActivity));
                    adapter = new FileFragmentSortByTimeAdapter(baseActivity, convert(), viewType);
                    recyFiles.setAdapter(adapter);
                }
                adapter.setListener(this);

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                isSelectStatus(false);
                break;
            case R.id.rl05:
                //进入选择模式
                //隐藏底部弹出框
                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                //更新adapter的选中状态
                isSelectStatus(true);
                break;

            case R.id.ll_download:
                if (adapter.getCheckedList().size() > 0) {
                    for (Object file : adapter.getCheckedList()) {
                        BroadcastUtils.sendDownloadBroadcast((RemoteFile) file);
                    }
                    ToastUtil.showCenterHasImageToast(baseActivity, "文件已添加至传输列表");
                    isSelectStatus(false);
                } else {
                    ToastUtil.showCenterHasImageToast(baseActivity, "请先选择文件！");
                }
                break;
            case R.id.ll_share:
                if (adapter.getCheckedList().size() > 0) {
                    RemoteFile remoteFile = (RemoteFile) adapter.getCheckedList().get(0);
                    String path = Constants.DOWNLOAD_PATH + remoteFile.getFileName();
                    if (new File(path).exists()) {
                        remoteFile.setPath(path);
                        new SharePopupWindow(baseActivity, remoteFile).showPopupWindow(llShare);
                    } else {
                        ToastUtil.showCenterHasImageToast(baseActivity, "请先下载后再分享！");
                    }
                } else {
                    ToastUtil.showCenterHasImageToast(baseActivity, "请先选择文件！");
                }
                break;
            case R.id.ll_backup:
                if (adapter.getCheckedList().size() > 0) {
                    backup();
                } else {
                    ToastUtil.showCenterHasImageToast(baseActivity, "请先选择文件！");
                }
                break;
            case R.id.ll_delete:
                if (adapter.getCheckedList().size() > 0) {
                    new BottomDeletePopupWindow(baseActivity, deleteInterface, adapter.getCheckedList()).showMoreOperationPopup(llDelete);
                } else {
                    ToastUtil.showCenterHasImageToast(baseActivity, "请先选择文件！");
                }
                break;
            case R.id.ll_more:
                if (adapter.getCheckedList().size() > 0) {
                    new BottomMorePopupWindow(baseActivity, this, adapter.getCheckedList(), false).showMoreOperationPopup(llMore);
                } else {
                    ToastUtil.showCenterHasImageToast(baseActivity, "请先选择文件！");
                }
                break;
            case R.id.rl02:
                if (sortType == SORT_BY_NAME) {
                    return;
                }

                sortType = SORT_BY_NAME;
                removeAllDecorations();
                Collections.sort(remoteFiles, sortByName);

                if (viewType == GRID_VIEW) {
                    recyFiles.setLayoutManager(new GridLayoutManager(baseActivity, 3));
                    recyFiles.addItemDecoration(decoration03);
                    adapter = new FileAdapter(remoteFiles, R.layout.item_file_grid);
                    recyFiles.setAdapter(adapter);
                } else if (viewType == LIST_VIEW) {
                    recyFiles.setLayoutManager(new LinearLayoutManager(baseActivity));
                    adapter = new FileAdapter(remoteFiles, R.layout.item_file_list);
                    recyFiles.setAdapter(adapter);
                }
                adapter.setListener(this);

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                isSelectStatus(false);
                break;
            case R.id.rl01:
                //按时间排序，首先判断当前视图样式
                if (sortType == SORT_BY_TIME) {
                    return;
                }

                sortType = SORT_BY_TIME;
                removeAllDecorations();
                adapter = new FileFragmentSortByTimeAdapter(baseActivity, convert(), viewType);
                adapter.setListener(this);
                recyFiles.setLayoutManager(new LinearLayoutManager(baseActivity));
                recyFiles.setAdapter(adapter);

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }

                isSelectStatus(false);
                break;
            case R.id.ivBack:
                goPrevious();
        }
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
            if (!file.isCanSelected()) {
                if (map.containsKey("9999-99-99")) {
                    List<RemoteFile> tempList = map.get("9999-99-99");
                    tempList.add(file);
                    map.put("9999-99-99", tempList);
                } else {
                    List<RemoteFile> tempList = new ArrayList<>();
                    tempList.add(file);
                    map.put("9999-99-99", tempList);
                }
            } else {
                long updateTime = file.getUpdateTime();
                String key = TimeUtils.longToString(updateTime, "yyyy-MM-dd");
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
        }

        for (Map.Entry<String, List<RemoteFile>> entry : map.entrySet()) {
            RemoteFileListSortByTime remoteFileListSortByTime = new RemoteFileListSortByTime();
            try {
                remoteFileListSortByTime.setTime(TimeUtils.stringToLong(entry.getKey(), "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //排序操作
            if (sortType == SORT_BY_TIME) {
                Collections.sort(entry.getValue(), new SortByTime());
            } else if (sortType == SORT_BY_NAME) {
                Collections.sort(entry.getValue(), new SortChineseName(baseActivity, false));
            }

            remoteFileListSortByTime.setList(entry.getValue());
            remoteFileListSortByTime.setShowDate(entry.getKey());
            list.add(remoteFileListSortByTime);
        }
        return list;
    }

    public void setTextViewSelected(TextView tv, ImageView duigou) {
        Drawable leftDrawable;
        switch (tv.getId()) {
            case R.id.tvTimeSort:
                leftDrawable = baseActivity.getResources().getDrawable(R.drawable.bottom_popup_time_sort_blue);
                break;
            case R.id.tvNameSort:
                leftDrawable = baseActivity.getResources().getDrawable(R.drawable.bottom_popup_name_sort_blue);
                break;
            case R.id.tvGrid:
                leftDrawable = baseActivity.getResources().getDrawable(R.drawable.bottom_popup_grid_blue);
                break;
            case R.id.tvList:
                leftDrawable = baseActivity.getResources().getDrawable(R.drawable.bottom_popup_list_blue);
                break;
            default:
                leftDrawable = baseActivity.getResources().getDrawable(R.drawable.bottom_popup_select_black);
                break;
        }
        leftDrawable.setBounds(0, 0, DensityUtil.dip2px(22, baseActivity), DensityUtil.dip2px(22, baseActivity));
        tv.setCompoundDrawables(leftDrawable, null, null, null);
        tv.setTextColor(Color.parseColor("#5BA0E9"));
        duigou.setVisibility(View.VISIBLE);
    }

    /**
     * 显示操作的底部弹窗
     */
    public void showOperationPopup(boolean isGrid) {
        Window window = baseActivity.getWindow();
        View contentView = View.inflate(baseActivity, R.layout.popup_bottom_edit, null);

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
        TextView tvGrid = contentView.findViewById(R.id.tvGrid);
        TextView tvList = contentView.findViewById(R.id.tvList);

        ImageView dg01 = contentView.findViewById(R.id.dg01);
        ImageView dg02 = contentView.findViewById(R.id.dg02);
        ImageView dg03 = contentView.findViewById(R.id.dg03);
        ImageView dg04 = contentView.findViewById(R.id.dg04);

        if (sortType == SORT_BY_TIME) {
            setTextViewSelected(tvTimeSort, dg01);
        } else if (sortType == SORT_BY_NAME) {
            setTextViewSelected(tvNameSort, dg02);
        }

        if (viewType == GRID_VIEW) {
            setTextViewSelected(tvGrid, dg03);
        } else if (viewType == LIST_VIEW) {
            setTextViewSelected(tvList, dg04);
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

    /**
     * add by houxiansheng 2019-12-12 11:58:43 是否是选择状态
     */
    public void isSelectStatus(boolean isSelectStatus) {
        setSelectMode(isSelectStatus);
        if (isSelectStatus) {
            llTopTitle.setVisibility(View.GONE);
            llTopSelect.setVisibility(View.VISIBLE);
            adapter.setSelectMode(true);
            ((MainActivity) baseActivity).setMenuShowStatus(false);
            llBottomEdit.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            llTopTitle.setVisibility(View.VISIBLE);
            llTopSelect.setVisibility(View.GONE);
            adapter.setSelectMode(false);
            ((MainActivity) baseActivity).setMenuShowStatus(true);
            adapter.removeAllCheckedList();
            llBottomEdit.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * add by houxiansheng 2019-12-12 15:36:08 全选、全不选
     */
    public void SelectedAllOrNot() {
        int size = 0;
        if (adapter instanceof FileFragmentSortByTimeAdapter) {
            size = ((FileFragmentSortByTimeAdapter) adapter).getCanEditFileTotal();
        } else if (adapter instanceof FileAdapter) {
            size = ((FileAdapter) adapter).getCanEditFileTotal();
        }
        if (adapter.getCheckedList().size() < size) {
            adapter.removeAllCheckedList();
            if (adapter instanceof FileAdapter) {
                List<RemoteFile> allList = adapter.getData();
                for (int i = 0; i < allList.size(); i++) {
                    RemoteFile file = allList.get(i);
                    if (file.isCanSelected()) {
                        adapter.addCheckList(file);
                    }
                }
            } else if (adapter instanceof FileFragmentSortByTimeAdapter) {
                List<RemoteFileListSortByTime> list = adapter.getData();
                for (int i = 0; i < list.size(); i++) {
                    RemoteFileListSortByTime remoteFileListSortByTime = list.get(i);
                    for (int j = 0; j < remoteFileListSortByTime.getList().size(); j++) {
                        RemoteFile remoteFile = remoteFileListSortByTime.getList().get(j);
                        if (remoteFile.isCanSelected()) {
                            adapter.addCheckList(remoteFile);
                        }
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
    public void onMove(List<RemoteFile> list) {
        Intent intent = new Intent();
        intent.putExtra("moveList", (Serializable) adapter.getCheckedList());
        intent.setClass(baseActivity, MoveActivity.class);
        startActivityForResult(intent, requestCodeMove);
        isSelectStatus(false);
    }

    @Override
    public void onRename(RemoteFile remoteFile) {
        new CreateFolderAndReNameDialog(baseActivity, "重命名", remoteFile.getFileName(), new NormalDialogInterface() {
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
                                    onReFresh();
                                } else {
                                    ToastUtil.showCenterHasImageToast(baseActivity, responseBean.getMessage());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showCenterForBusiness(baseActivity, "重命名失败！");
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
     * 删除操作
     */
    ConfirmCancelInterface deleteInterface = new ConfirmCancelInterface() {
        @Override
        public void onCancel(List<RemoteFile> list) {

        }

        @Override
        public void onConfirm(List<RemoteFile> list) {

            OperationUtils.deleteRemoteFile(baseActivity, list);
        }
    };

    @Override
    public void onInfo(RemoteFile remoteFile) {
        Intent intent = new Intent();
        intent.setClass(baseActivity, FileInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("remoteFile", remoteFile);
        intent.putExtras(bundle);
        baseActivity.startActivity(intent);
        isSelectStatus(false);
    }

    @Override
    public void onItemClick(RemoteFile remoteFile) {
        if (remoteFile.getCategory().equals("2")) {//文件夹
            if (remoteFile.getFileName().equals("我的相册") && remoteFile.getParentId().equals("0")) {//我的相册
                startActivity(new Intent(baseActivity, RemotePhotoListActivity.class));
            } else if (remoteFile.getFileName().equals("葫芦备份") && remoteFile.getID().equals("1") && remoteFile.getParentId().equals("0")) {//葫芦备份
                startActivity(new Intent(baseActivity, BackUpActivity.class));
            } else {
                goNext(remoteFile);
            }
        } else if (remoteFile.getCategory().equals("1")) {//文件
            Intent intent = new Intent();
            intent.setClass(baseActivity, EditFileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("remoteFile", remoteFile);
            intent.putExtras(bundle);
            baseActivity.startActivity(intent);
        }

    }

    /**
     * 进入下一级目录
     */
    private void goNext(RemoteFile remoteFile) {
        titleList.add(remoteFile);
        String id = "";
        String collectStatus = "";
        if (remoteFile.getFileName().equals("我的收藏") && remoteFile.getParentId().equals("0")) {
            collectStatus = "1";
        } else {
            id = remoteFile.getID();
        }
        ApiRetrofit.getInstance().getFileList(id, "", "", collectStatus, "", "", "", ""
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
                        ToastUtil.showCenterForBusiness(baseActivity, "读取文件列表失败！");
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
        if (titleList.size() == 1) {
            return true;
        }
        return false;
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
                        ToastUtil.showCenterForBusiness(baseActivity, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 刷新
     */
    public void onReFresh() {
        if (getSelectMode()) {//去掉选择模式
            isSelectStatus(false);
        }
        ApiRetrofit.getInstance().getFileList(titleList.get(titleList.size() - 1).getID(), "", "", "", "", "", "", ""
                , "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseFileSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseFileSearchBean responseFileSearchBean) {
                        Log.e("开始刷新哈哈哈2333*******", responseFileSearchBean.getData().size() + "----");
                        initData(responseFileSearchBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterForBusiness(baseActivity, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
                            ToastUtil.showCenterHasImageToast(baseActivity, "文件已添加至葫芦备份");
                            isSelectStatus(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterForBusiness(baseActivity, "葫芦备份失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onItemLongCick(RemoteFile file) {
        isSelectStatus(true);
    }

    @Override
    public void selectedAll() {
        isSelectAll = true;
        tvSelectAllOrNot.setText("全选");
    }

    @Override
    public void notSelectedAll() {
        isSelectAll = false;
        tvSelectAllOrNot.setText("全不选");
    }

    @Override
    public void selectCount(List<RemoteFile> selectedList, int count) {
        tvSelectNum.setText(String.format(baseActivity.getResources().getString(R.string.select_count), count + ""));
        if (count > 1) {
            llShare.setOnClickListener(null);
            ivShare.setBackgroundResource(R.drawable.icon_bottom_share_not);
            tvShare.setTextColor(Color.parseColor("#ffc1c1c0"));
        } else {
            llShare.setOnClickListener(this);
            ivShare.setBackgroundResource(R.drawable.icon_bottom_share);
            tvShare.setTextColor(Color.parseColor("#ff000000"));
        }
        setDownloadStatus(selectedList);
    }

    /**
     * 设置下载和分享的状态：如果选择了文件夹，把下载和置灰
     */
    private void setDownloadStatus(List<RemoteFile> list) {
        boolean isHasFolder = false;
        if (list != null && list.size() > 0) {//列表不等于空，并且列表数量大于0
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCategory().equals("2")) {//包含文件夹
                    isHasFolder = true;
                    break;
                }
            }
        }

        if (isHasFolder) {
            llDownload.setOnClickListener(null);
            ivDownload.setBackgroundResource(R.drawable.icon_bottom_download_not);
            tvDownload.setTextColor(Color.parseColor("#ffc1c1c0"));

            llShare.setOnClickListener(null);
            ivShare.setBackgroundResource(R.drawable.icon_bottom_share_not);
            tvShare.setTextColor(Color.parseColor("#ffc1c1c0"));
        } else {
            llDownload.setOnClickListener(this);
            ivDownload.setBackgroundResource(R.drawable.icon_bottom_download);
            tvDownload.setTextColor(Color.parseColor("#ff000000"));

            if (list.size() > 1) {
                llShare.setOnClickListener(null);
                ivShare.setBackgroundResource(R.drawable.icon_bottom_share_not);
                tvShare.setTextColor(Color.parseColor("#ffc1c1c0"));
            } else {
                llShare.setOnClickListener(this);
                ivShare.setBackgroundResource(R.drawable.icon_bottom_share);
                tvShare.setTextColor(Color.parseColor("#ff000000"));
            }
        }
    }

    public boolean getSelectMode() {
        return adapter.isSelectMode();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        switch (requestCode) {
            case requestCodeMove:
                if (resultCode == MoveActivity.resultCodeMove) {//移动操作后刷新界面
                    onReFresh();
                }
                break;
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
        if (titleList.size() == 0) {
            return;
        }

        String dirID = titleList.get(titleList.size() - 1).getID();

        if (dirID.equals("0")) {//根目录过滤掉固定文件夹
            if (text.equals("我的相册") || text.equals("我的收藏") || text.equals("回收站")
                    || text.equals("葫芦备份") || text.equals("备份恢复") || text.equals("收到文件")) {
                ToastUtil.showCenterHasImageToast(baseActivity, "文件夹已存在");
                return;
            }
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

        final String finalFolderName = text;

        ApiRetrofit.getInstance().postCreateFolder(text, dirID)
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
                            String newDirId = createFolderResponse.getData().getDirId();
                            //进入新文件夹
                            RemoteFile rf = new RemoteFile();
                            rf.setID(newDirId);
                            rf.setFileName(finalFolderName);
                            goNext(rf);
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
