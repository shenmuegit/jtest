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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.BackUpAdapter;
import com.ehualu.calabashandroid.adapter.BackUpParentAdapter;
import com.ehualu.calabashandroid.adapter.decoration.DividerItemDecoration03;
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
import com.ehualu.calabashandroid.popupwindow.BottomExtractPopupWindow;
import com.ehualu.calabashandroid.popupwindow.BottomMorePopupWindow;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;
import com.ehualu.calabashandroid.responseBean.ResponseFileSearchBean;
import com.ehualu.calabashandroid.utils.DensityUtil;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.OperationUtils;
import com.ehualu.calabashandroid.utils.RemoteConverter;
import com.ehualu.calabashandroid.utils.SortByTime;
import com.ehualu.calabashandroid.utils.SortChineseName;
import com.ehualu.calabashandroid.utils.TimeUtils;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;

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
 * time：2019-12-17 18:48:38
 * <p>
 * describe：葫芦备份
 */
public class BackUpActivity extends BaseActivity implements OnItemClickInterface, BottomMoreInterface {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.tvFile)
    TextView tvFile;
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
    @BindView(R.id.viewLine)
    View viewLine;
    @BindView(R.id.ivFileEdit)
    ImageView ivFileEdit;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.recyFiles)
    RecyclerView recyFiles;
    @BindView(R.id.ll_extract)
    LinearLayout llExtract;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.ll_bottom_edit)
    LinearLayout llBottomEdit;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.ll_empty_page)
    LinearLayout llEmptyPage;

    private BaseAdapter adapter;
    private PopupWindow fileEditPopup;
    private DividerItemDecoration03 decoration03;

    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_TIME = 2;

    public static final int GRID_VIEW = 1;
    public static final int LIST_VIEW = 2;

    public int sortType = SORT_BY_NAME;
    public int viewType = GRID_VIEW;
    public boolean isSelectMode = false;
    private List<RemoteFile> remoteFiles = new ArrayList<>();
    private List<RemoteFile> titleList = new ArrayList<>();//标题List
    private List<RemoteFileListSortByTime> filesByTime = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        ButterKnife.bind(this);
        initViewAndData();
        initListener();
    }

    private void initViewAndData() {
        Collections.sort(remoteFiles, new SortChineseName(this, true));
        adapter = new BackUpAdapter(remoteFiles, R.layout.item_file_grid);
        adapter.setListener(this);
        decoration03 = new DividerItemDecoration03(this);
        recyFiles.addItemDecoration(decoration03);
        recyFiles.setLayoutManager(new GridLayoutManager(this, 3));
        recyFiles.setAdapter(adapter);
        setRootDirectory();
        getFileList();
    }

    private void initListener() {
        tvSearch.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivFileEdit.setOnClickListener(this);
        tvSelectCancel.setOnClickListener(this);
        tvSelectAllOrNot.setOnClickListener(this);

        llExtract.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        llMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tvSearch:
                startActivity(new Intent(BackUpActivity.this, CommonSearchActivity.class));
                break;
            case R.id.ivBack:
                if (isRootDirectory()) {//如果根目录，走退出逻辑
                    finish();
                } else {//不是根目录，回到上级目录
                    goPrevious();
                }
                break;
            case R.id.ivFileEdit:
                if (adapter instanceof BackUpParentAdapter) {
                    //按时间排序
                    if (viewType == GRID_VIEW) {
                        showOperationPopup(true);
                    } else if (viewType == LIST_VIEW) {
                        showOperationPopup(false);
                    }
                } else if (adapter instanceof BackUpAdapter) {
                    //按名称排序
                    RecyclerView.LayoutManager layoutManager = recyFiles.getLayoutManager();
                    if (layoutManager instanceof GridLayoutManager) {
                        showOperationPopup(true);
                    } else {
                        showOperationPopup(false);
                    }
                }
                break;

            case R.id.tv_select_cancel:
                setSelectStatus(false);
                break;

            case R.id.tv_select_all_or_not:
                SelectedAllOrNot();
                break;

            case R.id.rl01:
                //按时间排序，首先判断当前视图样式
                if (sortType == SORT_BY_TIME) {
                    return;
                }

                sortType = SORT_BY_TIME;
                removeAllDecorations();
                adapter = new BackUpParentAdapter(this, filesByTime, viewType);
                adapter.setListener(this);
                recyFiles.setLayoutManager(new LinearLayoutManager(this));
                recyFiles.setAdapter(adapter);

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }

                setSelectStatus(false);
                break;
            case R.id.rl02:
                if (sortType == SORT_BY_NAME) {
                    return;
                }

                sortType = SORT_BY_NAME;
                removeAllDecorations();
                Collections.sort(remoteFiles, new SortChineseName(this, true));
                if (viewType == GRID_VIEW) {
                    recyFiles.setLayoutManager(new GridLayoutManager(this, 3));
                    recyFiles.addItemDecoration(decoration03);
                    adapter = new BackUpAdapter(remoteFiles, R.layout.item_file_grid);
                    recyFiles.setAdapter(adapter);
                    adapter.setListener(this);
                } else if (viewType == LIST_VIEW) {
                    recyFiles.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new BackUpAdapter(remoteFiles, R.layout.item_file_list);
                    recyFiles.setAdapter(adapter);
                    adapter.setListener(this);
                }
                adapter.setListener(this);

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                setSelectStatus(false);
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
                    recyFiles.setLayoutManager(new GridLayoutManager(this, 3));
                    adapter = new BackUpAdapter(remoteFiles, R.layout.item_file_grid);
                    recyFiles.setAdapter(adapter);
                    adapter.setListener(this);
                } else if (sortType == SORT_BY_TIME) {
                    recyFiles.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new BackUpParentAdapter(this, filesByTime, viewType);
                    adapter.setListener(this);
                    recyFiles.setAdapter(adapter);
                }

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                setSelectStatus(false);
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
                    recyFiles.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new BackUpAdapter(remoteFiles, R.layout.item_file_list);
                    recyFiles.setAdapter(adapter);
                    adapter.setListener(this);
                } else if (sortType == SORT_BY_TIME) {
                    recyFiles.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new BackUpParentAdapter(this, filesByTime, viewType);
                    adapter.setListener(this);
                    recyFiles.setAdapter(adapter);
                }

                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                setSelectStatus(false);
                break;
            case R.id.rl05:
                //隐藏底部弹出框
                if (fileEditPopup != null && fileEditPopup.isShowing()) {
                    fileEditPopup.dismiss();
                }
                //更新adapter的选中状态
                setSelectStatus(true);
                break;

            case R.id.ll_extract:
                new BottomExtractPopupWindow(this, extractInterface, adapter.getCheckedList()).showMoreOperationPopup(llDelete);
                break;

            case R.id.ll_delete:
                new BottomDeletePopupWindow(this, deleteInterface, adapter.getCheckedList()).showMoreOperationPopup(llDelete);
                break;

            case R.id.ll_more:
                new BottomMorePopupWindow(this, this, adapter.getCheckedList(), true).showMoreOperationPopup(llMore);
                break;

        }
    }

    /**
     * 设置根目录
     */
    private void setRootDirectory() {
        RemoteFile remoteFile = new RemoteFile();
        remoteFile.setID("1");
        remoteFile.setFileName("葫芦备份");
        titleList.add(remoteFile);
        tvFile.setText(titleList.get(titleList.size() - 1).getFileName());
        ivBack.setVisibility(View.VISIBLE);
    }

    /**
     * 文件列表
     */
    private void getFileList() {
        ApiRetrofit.getInstance().getFileList("1", "", "", "", "", "", "", "", "", "", "")
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
                        convert(remoteFiles);
                        Collections.sort(remoteFiles, new SortChineseName(BackUpActivity.this, true));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterHasImageToast(BackUpActivity.this, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 实例化数据
     */
    private void initData(ResponseFileSearchBean bean) {
        tvFile.setText(titleList.get(titleList.size() - 1).getFileName());
        if (!bean.isSuccess()) {
            ToastUtil.showCenterNoImageToast(BackUpActivity.this, bean.getMessage());
            return;
        }
        remoteFiles.clear();
        remoteFiles.addAll(RemoteConverter.getRemoteFiles(bean));
        Collections.sort(remoteFiles, new SortChineseName(this, true));

        if (remoteFiles.size() > 0) {
            llEmptyPage.setVisibility(View.GONE);
            recyFiles.setVisibility(View.VISIBLE);
        } else {
            llEmptyPage.setVisibility(View.VISIBLE);
            recyFiles.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 进入下一级目录
     */
    private void goNext(RemoteFile remoteFile) {
        titleList.add(remoteFile);
        ApiRetrofit.getInstance().getFileList(titleList.get(titleList.size() - 1).getID(), "", "", "", "",
                "", "", ""
                , "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseFileSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseFileSearchBean responseFileSearchBean) {
                        MyLog.d(responseFileSearchBean.toString());
                        initData(responseFileSearchBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterForBusiness(BackUpActivity.this, "读取文件列表失败！");
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
                        ToastUtil.showCenterForBusiness(BackUpActivity.this, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 刷新
     */
    private void onReFresh() {
        if (adapter.isSelectMode()) {//去掉选择模式
            setSelectStatus(false);
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
                        initData(responseFileSearchBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterForBusiness(BackUpActivity.this, "读取文件列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 备份恢复
     */
    ConfirmCancelInterface extractInterface = new ConfirmCancelInterface() {
        @Override
        public void onCancel(List<RemoteFile> list) {

        }

        @Override
        public void onConfirm(List<RemoteFile> list) {
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
            if (fileList.size() == 0 && folderList.size() == 0) {
                ToastUtil.showCenterHasImageToast(BackUpActivity.this, "请先选择文件！");
            } else {
                ApiRetrofit.getInstance().postExtract(folderList, fileList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<PublicResponseBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(PublicResponseBean responseBean) {
                                MyLog.d(responseBean.toString());
                                setSelectStatus(false);
                                if (responseBean.isSuccess()) {
                                    ToastUtil.showCenterHasImageToast(BackUpActivity.this, "文件已添加至备份恢复");
                                } else {
                                    ToastUtil.showCenterHasImageToast(BackUpActivity.this, responseBean.getMessage());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showCenterForBusiness(BackUpActivity.this, "备份恢复失败！");
                                setSelectStatus(false);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }
    };

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
            OperationUtils.deleteRemoteFile(BackUpActivity.this, list);
        }
    };

    public void removeAllDecorations() {
        for (int i = 0; i < recyFiles.getItemDecorationCount(); i++) {
            recyFiles.removeItemDecorationAt(i);
        }
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
        if (adapter.isSelectMode()) {
            setSelectStatus(false);
        } else {
            if (isRootDirectory()) {//如果根目录，走退出逻辑
                finish();
            } else {//不是根目录，回到上级目录
                goPrevious();
            }
        }
    }

    public void convert(List<RemoteFile> rfs) {
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

    /**
     * 显示操作的底部弹窗
     */
    public void showOperationPopup(boolean isGrid) {
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
        setSelectMode(isSelectStatus);
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
    /**
     * add by houxiansheng 2019-12-12 15:36:08 全选、全不选
     */
    public void SelectedAllOrNot() {
        int size = 0;
        if (adapter instanceof BackUpParentAdapter) {
            size = ((BackUpParentAdapter) adapter).getTotalSize();
        } else if (adapter instanceof BackUpAdapter) {
            size = adapter.getData().size();
        }
        if (adapter.getCheckedList().size() < size) {
            adapter.removeAllCheckedList();
            if (adapter instanceof BackUpAdapter) {
                List<RemoteFile> allList = adapter.getData();
                for (int i = 0; i < allList.size(); i++) {
                    RemoteFile file = allList.get(i);
                    adapter.addCheckList(file);
                }
            } else if (adapter instanceof BackUpParentAdapter) {
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
    public void onMove(List<RemoteFile> list) {
        //        startActivity(new Intent(this, MoveActivity.class));
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
                                    onReFresh();
                                } else {
                                    ToastUtil.showCenterHasImageToast(BackUpActivity.this, responseBean.getMessage());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showCenterForBusiness(BackUpActivity.this, "重命名失败！");
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
    public void onItemClick(RemoteFile remoteFile) {
        if (remoteFile.getCategory().equals("2")) {//文件夹
            goNext(remoteFile);
        }
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
    }
}
