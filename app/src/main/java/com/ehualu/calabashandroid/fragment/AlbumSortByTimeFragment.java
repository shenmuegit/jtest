package com.ehualu.calabashandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.activity.EditFileActivity;
import com.ehualu.calabashandroid.activity.RemotePhotoListActivity;
import com.ehualu.calabashandroid.adapter.AlbumByTimeSortAdapter;
import com.ehualu.calabashandroid.adapter.anim.MyItemAnimator;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.interfaces.OnItemClickInterface;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.model.RemoteFileListSortByTime;
import com.ehualu.calabashandroid.popupwindow.SharePopupWindow;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;
import com.ehualu.calabashandroid.responseBean.ResponseFileSearchBean;
import com.ehualu.calabashandroid.utils.Constants;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.RemoteConverter;
import com.ehualu.calabashandroid.utils.SortByTime;
import com.ehualu.calabashandroid.utils.SortChineseName;
import com.ehualu.calabashandroid.utils.TimeUtils;
import com.ehualu.calabashandroid.utils.ToastUtil;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是网盘相册界面，时间轴列表fragment
 */
public class AlbumSortByTimeFragment extends BaseFragment implements OnItemClickInterface {

    @BindView(R.id.recyRemotePhoto)
    RecyclerView recyRemotePhoto;

    public AlbumByTimeSortAdapter adapter;
    @BindView(R.id.ll_empty_page)
    LinearLayout llEmptyPage;
    private RemotePhotoListActivity activity;
    public boolean isSelectAll = true;//是否全选
    private List<RemoteFile> remoteFiles = new ArrayList<>();
    private List<RemoteFileListSortByTime> filesByTime = new ArrayList<>();
    private boolean isBitmapMode = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (RemotePhotoListActivity) context;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_album;
    }

    @Override
    protected void setUpView() {
        isBitmapMode = false;
        adapter = new AlbumByTimeSortAdapter(activity, filesByTime, isBitmapMode);
        recyRemotePhoto.setItemAnimator(new MyItemAnimator());
        ((SimpleItemAnimator) recyRemotePhoto.getItemAnimator()).setSupportsChangeAnimations(false);

        adapter.setListener(this);
        DividerItemDecoration decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.vertical_line30));
        recyRemotePhoto.addItemDecoration(decoration);
        recyRemotePhoto.setLayoutManager(new LinearLayoutManager(activity));
        recyRemotePhoto.setAdapter(adapter);
    }

    @Override
    protected void setUpData() {
        getFileList();
    }

    @Override
    public void onClick(View v) {

    }

    public void getFileList() {
        ApiRetrofit.getInstance().getFileList("", "2", "", "", "", "", "", "", "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseFileSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseFileSearchBean responseFileSearchBean) {
                        MyLog.d(responseFileSearchBean.toString());
                        remoteFiles.addAll(RemoteConverter.getRemoteFiles(responseFileSearchBean));
                        testConvert(remoteFiles);
                        Collections.sort(remoteFiles, new SortChineseName(baseActivity, true));
                        switchStandard();
                        if (remoteFiles.size() > 0) {
                            llEmptyPage.setVisibility(View.GONE);
                            recyRemotePhoto.setVisibility(View.VISIBLE);
                        } else {
                            llEmptyPage.setVisibility(View.VISIBLE);
                            recyRemotePhoto.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterHasImageToast(activity, "读取图片列表失败！");
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
                Collections.sort(entry.getValue(), new SortChineseName(baseActivity, true));
            }

            ocFileListByTime.setList(entry.getValue());
            ocFileListByTime.setShowDate(entry.getKey());
            filesByTime.add(ocFileListByTime);
        }
    }

    private void removeAllDecorations(RecyclerView recyclerView) {
        for (int i = 0; i < recyclerView.getItemDecorationCount(); i++) {
            recyclerView.removeItemDecorationAt(i);
        }
    }

    /**
     * 切换大图模式
     */
    public void switchBigBitmap() {
        isBitmapMode = true;
        removeAllDecorations(recyRemotePhoto);
        adapter = new AlbumByTimeSortAdapter(activity, filesByTime, isBitmapMode);
        adapter.setListener(this);
        DividerItemDecoration decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.vertical_line30));
        recyRemotePhoto.addItemDecoration(decoration);
        recyRemotePhoto.setLayoutManager(new LinearLayoutManager(activity));
        recyRemotePhoto.setAdapter(adapter);
    }

    /**
     * 切换标准模式
     */
    public void switchStandard() {
        isBitmapMode = false;
        removeAllDecorations(recyRemotePhoto);
        adapter = new AlbumByTimeSortAdapter(activity, filesByTime, isBitmapMode);
        adapter.setListener(this);
        DividerItemDecoration decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(baseActivity.getResources().getDrawable(R.drawable.vertical_line30));
        recyRemotePhoto.addItemDecoration(decoration);
        recyRemotePhoto.setLayoutManager(new LinearLayoutManager(activity));
        recyRemotePhoto.setAdapter(adapter);
    }

    /**
     * 获取是否是大图模式
     */
    public boolean getIsBitmapMode() {
        return isBitmapMode;
    }

    @Override
    public void onItemClick(RemoteFile remoteFile) {
        Intent intent = new Intent();
        intent.setClass(activity, EditFileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("remoteFile", remoteFile);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    public void onItemLongCick(RemoteFile file) {
        isSelectStatus(true);
    }

    @Override
    public void selectedAll() {
        isSelectAll = true;
        activity.tvSelectAllOrNot.setText("全选");
    }

    @Override
    public void notSelectedAll() {
        isSelectAll = false;
        activity.tvSelectAllOrNot.setText("全不选");
    }

    @Override
    public void selectCount(List<RemoteFile> selectedList, int count) {
        activity.tvSelectNum.setText(String.format(getResources().getString(R.string.select_count), count + ""));
        if (count > 1) {
            activity.llShare.setOnClickListener(null);
            activity.ivShare.setBackgroundResource(R.drawable.icon_bottom_share_not);
            activity.tvShare.setTextColor(Color.parseColor("#ffc1c1c0"));
        } else {
            activity.ivShare.setBackgroundResource(R.drawable.icon_bottom_share);
            activity.tvShare.setTextColor(Color.parseColor("#ff000000"));
            activity.llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedList.size() > 0) {
                        RemoteFile remoteFile = selectedList.get(0);
                        String path = Constants.DOWNLOAD_PATH + remoteFile.getFileName();
                        if (new File(path).exists()) {
                            remoteFile.setPath(path);
                            new SharePopupWindow(activity, remoteFile).showPopupWindow(activity.llShare);
                        } else {
                            ToastUtil.showCenterHasImageToast(activity, "请先下载后再分享！");
                        }
                    } else {
                        ToastUtil.showCenterHasImageToast(activity, "请先选择文件！");
                    }
                }
            });
        }
    }

    public void setSelectMode(boolean b) {
        //filefragment的属性
        isSelectMode = b;
        if (b) {
            adapter.setSelectMode(true);
            //将搜索框后面的编辑图标置灰
            activity.ivFileEdit.setImageResource(R.mipmap.frg_file_edit_disabled);
            activity.ivFileEdit.setClickable(false);
        } else {
            adapter.setSelectMode(false);
            activity.ivFileEdit.setImageResource(R.mipmap.frg_file_edit);
            activity.ivFileEdit.setClickable(true);
        }
    }

    /**
     * add by houxiansheng 2019-12-12 11:58:43 是否是选择状态
     */
    public void isSelectStatus(boolean isSelectStatus) {
        setSelectMode(isSelectStatus);
        if (isSelectStatus) {
            activity.llTopTitle.setVisibility(View.GONE);
            activity.llTopSelect.setVisibility(View.VISIBLE);
            adapter.setSelectMode(true);
            activity.llBottomEdit.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            activity.llTopTitle.setVisibility(View.VISIBLE);
            activity.llTopSelect.setVisibility(View.GONE);
            adapter.setSelectMode(false);
            adapter.removeAllCheckedList();
            activity.llBottomEdit.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 葫芦备份
     */
    public void backup() {
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
            ToastUtil.showCenterHasImageToast(baseActivity, "请先选择文件！");
        } else {
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
                                ToastUtil.showCenterHasImageToast(activity, "文件已添加至葫芦备份");
                                isSelectStatus(false);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.showCenterForBusiness(activity, "葫芦备份失败！");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
