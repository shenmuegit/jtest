package com.ehualu.calabashandroid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.ehualu.calabashandroid.adapter.RemoteDocumentNormalAdapter;
import com.ehualu.calabashandroid.adapter.RemoteDocumentParentAdapter;
import com.ehualu.calabashandroid.adapter.decoration.DividerItemDecoration06;
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

public class RemoteDocumentListActivity extends BaseActivity implements OnItemClickInterface, BottomMoreInterface {

    @BindView(R.id.tvFile)
    TextView tvFile;
    @BindView(R.id.viewLine)
    View viewLine;
    @BindView(R.id.ivFileEdit)
    ImageView ivFileEdit;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.recyDocument)
    RecyclerView recyDocument;
    @BindView(R.id.ll_top_title)
    LinearLayout llTopTitle;
    @BindView(R.id.tv_select_cancel)
    TextView tvSelectCancel;
    @BindView(R.id.tv_select_num)
    TextView tvSelectNum;
    @BindView(R.id.tv_select_all_or_not)
    TextView tvSelectAllOrNot;
    @BindView(R.id.ll_top_select)
    LinearLayout llTopSelect;
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
    @BindView(R.id.ivBack)
    ImageView ivBack;
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

    private PopupWindow fileEditPopup;
    private BaseAdapter adapter;
    private List<RemoteFile> files = new ArrayList<>();//原始文件列表
    private List<RemoteFileListSortByTime> filesByTime = new ArrayList<>();//自己封装的文件列表
    public boolean isSelectAll = true;//是否全选

    private DividerItemDecoration06 decoration06;
    private int requestCodeMove = 1;//移动

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_document_list);
        ButterKnife.bind(this);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        tvFile.setText("文档");
        sortType = SORT_BY_TIME;
        viewType = GRID_VIEW;
        decoration06 = new DividerItemDecoration06(this);
        setListener();
        adapter = new RemoteDocumentParentAdapter(this, filesByTime, viewType);
        recyDocument.setLayoutManager(new LinearLayoutManager(this));
        recyDocument.setAdapter(adapter);
        adapter.setListener(this);
        getFileList();
        ivDownload.setBackgroundResource(R.drawable.icon_bottom_download);
    }

    public void setListener() {
        ivBack.setOnClickListener(this);
        tvFile.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        ivFileEdit.setOnClickListener(this);
        tvSelectAllOrNot.setOnClickListener(this);
        tvSelectCancel.setOnClickListener(this);
        llDownload.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llBackup.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        llMore.setOnClickListener(this);
    }

    private void getFileList() {
        ApiRetrofit.getInstance().getFileList("", "3", "", "", "", "", "", "", "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseFileSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseFileSearchBean responseFileSearchBean) {
                        MyLog.d(responseFileSearchBean.toString());
                        files.clear();
                        files.addAll(RemoteConverter.getRemoteFiles(responseFileSearchBean));
                        testConvert(files);
                        Collections.sort(files, new SortChineseName(RemoteDocumentListActivity.this, true));
                        if (files.size() > 0) {
                            llEmptyPage.setVisibility(View.GONE);
                            recyDocument.setVisibility(View.VISIBLE);
                        } else {
                            llEmptyPage.setVisibility(View.VISIBLE);
                            recyDocument.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterForBusiness(RemoteDocumentListActivity.this, "读取文档列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void testConvert(List<RemoteFile> rfs) {
        filesByTime.clear();
        Map<String, List<RemoteFile>> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {
                // 降序排序
                return obj2.compareTo(obj1);
            }
        });
        for (RemoteFile file : rfs) {
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
            filesByTime.add(ocFileListByTime);
        }
    }

    public void removeAllDecorationsAndPaddings() {
        for (int i = 0; i < recyDocument.getItemDecorationCount(); i++) {
            recyDocument.removeItemDecorationAt(i);
        }
        recyDocument.setPadding(0, 0, 0, 0);
    }

    /**
     * add by houxiansheng 2019-12-12 15:36:08 全选、全不选
     */
    public void SelectedAllOrNot() {
        int size = 0;
        if (adapter instanceof RemoteDocumentParentAdapter) {
            size = ((RemoteDocumentParentAdapter) adapter).getCanEditFileTotal();
        } else if (adapter instanceof RemoteDocumentNormalAdapter) {
            size = adapter.getData().size();
        }
        if (adapter.getCheckedList().size() < size) {
            adapter.removeAllCheckedList();
            if (adapter instanceof RemoteDocumentNormalAdapter) {
                List<RemoteFile> allList = adapter.getData();
                for (int i = 0; i < allList.size(); i++) {
                    RemoteFile file = allList.get(i);
                    if (file.isCanSelected()) {
                        adapter.addCheckList(file);
                    }
                }
            } else if (adapter instanceof RemoteDocumentParentAdapter) {
                List<RemoteFileListSortByTime> list = adapter.getData();
                for (int i = 0; i < list.size(); i++) {
                    RemoteFileListSortByTime ocFileListByTime = list.get(i);
                    for (int j = 0; j < ocFileListByTime.getList().size(); j++) {
                        RemoteFile ocFile = ocFileListByTime.getList().get(j);
                        if (ocFile.isCanSelected()) {
                            adapter.addCheckList(ocFile);
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
    public void onClick(View v) {
        super.onClick(v);
        LinearLayout.LayoutParams linearParams;
        switch (v.getId()) {
            case R.id.tvSearch:
                startActivity(new Intent(RemoteDocumentListActivity.this, CommonSearchActivity.class));
                break;
            case R.id.ivBack:
            case R.id.tvFile:
                finish();//关闭当前界面
                break;
            case R.id.tv_select_cancel:
                isSelectStatus(false);
                setSelectMode(false);
                break;
            case R.id.tv_select_all_or_not:
                SelectedAllOrNot();
                break;
            case R.id.ivFileEdit:
                showOperationPopup();
                break;
            case R.id.rl01:
                if (sortType == SORT_BY_TIME) {
                    if (fileEditPopup != null && fileEditPopup.isShowing()) {
                        fileEditPopup.dismiss();
                    }
                    return;
                }

                sortType = SORT_BY_TIME;
                removeAllDecorationsAndPaddings();
                testConvert(files);
                adapter = new RemoteDocumentParentAdapter(this, filesByTime, viewType);
                adapter.setListener(this);
                recyDocument.setLayoutManager(new LinearLayoutManager(this));
                recyDocument.setAdapter(adapter);
                adapter.setListener(this);
                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                break;
            case R.id.rl02:
                if (sortType == SORT_BY_NAME) {
                    if (fileEditPopup != null && fileEditPopup.isShowing()) {
                        fileEditPopup.dismiss();
                    }
                    return;
                }

                sortType = SORT_BY_NAME;
                removeAllDecorationsAndPaddings();
                if (viewType == GRID_VIEW) {
                    recyDocument.setPadding(DensityUtil.dip2px(21, this), DensityUtil.dip2px(20, this),
                            DensityUtil.dip2px(21, this), 0);
                    recyDocument.setLayoutManager(new GridLayoutManager(this, 3));
                    recyDocument.addItemDecoration(decoration06);
                    adapter = new RemoteDocumentNormalAdapter(this, R.layout.item_remote_document_child_grid, files);
                    recyDocument.setAdapter(adapter);
                    adapter.setListener(this);
                } else if (viewType == LIST_VIEW) {
                    recyDocument.setPadding(DensityUtil.dip2px(29, this), 0, 0, 0);
                    recyDocument.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new RemoteDocumentNormalAdapter(this, R.layout.item_remote_document_child_list, files);
                    recyDocument.setAdapter(adapter);
                    adapter.setListener(this);
                }
                adapter.setListener(this);

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                break;
            case R.id.rl03:
                if (viewType == GRID_VIEW) {
                    if (fileEditPopup != null && fileEditPopup.isShowing()) {
                        fileEditPopup.dismiss();
                    }
                    return;
                }

                viewType = GRID_VIEW;
                removeAllDecorationsAndPaddings();
                if (sortType == SORT_BY_NAME) {
                    recyDocument.setPadding(DensityUtil.dip2px(21, this), DensityUtil.dip2px(20, this),
                            DensityUtil.dip2px(21, this), 0);
                    recyDocument.setLayoutManager(new GridLayoutManager(this, 3));
                    recyDocument.addItemDecoration(decoration06);
                    adapter = new RemoteDocumentNormalAdapter(this, R.layout.item_remote_document_child_grid, files);
                    recyDocument.setAdapter(adapter);
                    adapter.setListener(this);
                } else if (sortType == SORT_BY_TIME) {
                    linearParams = (LinearLayout.LayoutParams) recyDocument.getLayoutParams();
                    linearParams.setMargins(0, DensityUtil.dip2px(14, this), 0, 0);
                    testConvert(files);
                    adapter = new RemoteDocumentParentAdapter(this, filesByTime, viewType);
                    recyDocument.setLayoutManager(new LinearLayoutManager(this));
                    recyDocument.setAdapter(adapter);
                    adapter.setListener(this);
                }
                adapter.setListener(this);

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                break;
            case R.id.rl04:
                if (viewType == LIST_VIEW) {
                    if (fileEditPopup != null && fileEditPopup.isShowing()) {
                        fileEditPopup.dismiss();
                    }
                    return;
                }

                viewType = LIST_VIEW;
                removeAllDecorationsAndPaddings();
                if (sortType == SORT_BY_NAME) {
                    recyDocument.setPadding(DensityUtil.dip2px(29, this), 0, 0, 0);
                    recyDocument.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new RemoteDocumentNormalAdapter(this, R.layout.item_remote_document_child_list, files);
                    recyDocument.setAdapter(adapter);
                    adapter.setListener(this);
                } else if (sortType == SORT_BY_TIME) {
                    linearParams = (LinearLayout.LayoutParams) recyDocument.getLayoutParams();
                    linearParams.setMargins(0, DensityUtil.dip2px(14, this), 0, 0);
                    testConvert(files);
                    adapter = new RemoteDocumentParentAdapter(this, filesByTime, viewType);
                    recyDocument.setLayoutManager(new LinearLayoutManager(this));
                    recyDocument.setAdapter(adapter);
                    adapter.setListener(this);
                }
                adapter.setListener(this);

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                break;
            case R.id.rl05:
                setSelectMode(true);

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }

                isSelectStatus(true);
                break;
            case R.id.ll_download:
                if (adapter.getCheckedList().size() > 0) {
                    for (Object file : adapter.getCheckedList()) {
                        BroadcastUtils.sendDownloadBroadcast((RemoteFile) file);
                    }
                    ToastUtil.showCenterHasImageToast(this, "文件已添加至传输列表");
                    isSelectStatus(false);
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
        }
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
                            ToastUtil.showCenterHasImageToast(RemoteDocumentListActivity.this, "文件已添加至葫芦备份");
                            isSelectStatus(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterForBusiness(RemoteDocumentListActivity.this, "葫芦备份失败！");
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
            OperationUtils.deleteRemoteFile(RemoteDocumentListActivity.this, list);
        }
    };

    /**
     * add by houxiansheng 2019-12-12 11:58:43 是否是选择状态
     */
    public void isSelectStatus(boolean isSelectStatus) {
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
    }

    /**
     * 设置是否进入选择模式
     *
     * @param b
     */
    public void setSelectMode(boolean b) {
        isSelectMode = b;
        if (b) {
            //进入选择模式
            adapter.setSelectMode(true);
            ivFileEdit.setImageResource(R.mipmap.frg_file_edit_disabled);
            ivFileEdit.setClickable(false);
        } else {
            //退出选择模式
            adapter.setSelectMode(false);
            ivFileEdit.setImageResource(R.mipmap.frg_file_edit);
            ivFileEdit.setClickable(true);
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

    public void setTextViewSelected(TextView tv, ImageView duigou) {
        Drawable leftDrawable;
        switch (tv.getId()) {
            case R.id.tvTimeSort:
                leftDrawable = getResources().getDrawable(R.drawable.bottom_popup_time_sort_blue);
                break;
            case R.id.tvNameSort:
                leftDrawable = getResources().getDrawable(R.drawable.bottom_popup_name_sort_blue);
                break;
            case R.id.tvGrid:
                leftDrawable = getResources().getDrawable(R.drawable.bottom_popup_grid_blue);
                break;
            case R.id.tvList:
                leftDrawable = getResources().getDrawable(R.drawable.bottom_popup_list_blue);
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
    public void onItemClick(RemoteFile file) {

    }

    @Override
    public void onItemLongCick(RemoteFile file) {
        setSelectMode(true);
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
    public void onBackPressed() {
        if (isSelectMode) {
            setSelectMode(false);
            isSelectStatus(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMove(List<RemoteFile> list) {
        Intent intent = new Intent();
        intent.putExtra("moveList", (Serializable) adapter.getCheckedList());
        intent.setClass(this, MoveActivity.class);
        startActivityForResult(intent, requestCodeMove);
        isSelectStatus(false);
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
                                    isSelectStatus(false);
                                } else {
                                    ToastUtil.showCenterHasImageToast(RemoteDocumentListActivity.this,
                                            responseBean.getMessage());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showCenterForBusiness(RemoteDocumentListActivity.this, "重命名失败！");
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

    @Override
    public void onInfo(RemoteFile remoteFile) {
        Intent intent = new Intent();
        intent.setClass(this, FileInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("remoteFile", remoteFile);
        intent.putExtras(bundle);
        startActivity(intent);
        isSelectStatus(false);
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
