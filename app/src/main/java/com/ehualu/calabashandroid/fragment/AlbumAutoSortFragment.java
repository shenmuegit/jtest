package com.ehualu.calabashandroid.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.AlbumByAutoSortAdapter;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.model.AlbumMultipleItem;
import com.ehualu.calabashandroid.model.Classify;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.responseBean.ResponseFileSearchBean;
import com.ehualu.calabashandroid.utils.RemoteConverter;
import com.ehualu.calabashandroid.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是网盘相册界面，智能分类列表fragment
 */
public class AlbumAutoSortFragment extends BaseFragment {

    @BindView(R.id.recyRemotePhoto)
    RecyclerView recyRemotePhoto;
    @BindView(R.id.ll_empty_page)
    LinearLayout llEmptyPage;

    private List<AlbumMultipleItem> items = new ArrayList<>();
    private AlbumByAutoSortAdapter adapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_album;
    }

    @Override
    protected void setUpView() {
        adapter = new AlbumByAutoSortAdapter(baseActivity, items);
        recyRemotePhoto.setLayoutManager(new LinearLayoutManager(baseActivity));
        recyRemotePhoto.setAdapter(adapter);
    }

    @Override
    protected void setUpData() {
        ApiRetrofit.getInstance().getFileList("", "2", "", "", "", "", "", "", "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseFileSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseFileSearchBean bean) {
                        convert(RemoteConverter.getRemoteFiles(bean));
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showCenterHasImageToast(baseActivity, "读取图片列表失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {
    }

    private void convert(List<RemoteFile> remoteFiles) {
        /**
         * categoryMap.put("1", "person");
         * categoryMap.put("2", "map");
         * categoryMap.put("3", "commemoration");
         * categoryMap.put("4", "pet");
         * categoryMap.put("5", "sky");
         * categoryMap.put("6", "scenery");
         * categoryMap.put("7", "delicacy");
         * categoryMap.put("8", "abroad");
         * categoryMap.put("9", "child");
         * categoryMap.put("100", "roll");
         */

        List<RemoteFile> personList = new ArrayList<>();//人物
        List<RemoteFile> mapList = new ArrayList<>();//地点
        List<RemoteFile> commemorationList = new ArrayList<>();//纪念照
        List<RemoteFile> petList = new ArrayList<>();//宠物
        List<RemoteFile> skyList = new ArrayList<>();//天空
        List<RemoteFile> sceneryList = new ArrayList<>();//风景
        List<RemoteFile> delicacyList = new ArrayList<>();//美食
        List<RemoteFile> abroadList = new ArrayList<>();//国外
        List<RemoteFile> childList = new ArrayList<>();//孩子
        List<RemoteFile> rollList = new ArrayList<>();//顶部滑动

        //过滤掉标签为空的数据
        Iterator<RemoteFile> iterator = remoteFiles.iterator();
        while (iterator.hasNext()) {
            RemoteFile remoteFile = iterator.next();
            if (TextUtils.isEmpty(remoteFile.getLabels())) {
                iterator.remove();
            }
        }
        //取出所有的标签
        Map<String, List<RemoteFile>> map = new HashMap<>();
        for (int i = 0; i < remoteFiles.size(); i++) {
            RemoteFile rf = remoteFiles.get(i);
            String labels = rf.getLabels();
            if (labels.contains(",")) {
                String[] tags = rf.getLabels().split(",");
                for (String tag : tags) {
                    if (map.containsKey(tag)) {
                        List<RemoteFile> li = map.get(tag);
                        li.add(rf);
                    } else {
                        List<RemoteFile> li2 = new ArrayList<>();
                        li2.add(rf);
                        map.put(tag, li2);
                    }
                }
            } else {
                if (map.containsKey(labels)) {
                    List<RemoteFile> li = map.get(labels);
                    li.add(rf);
                } else {
                    List<RemoteFile> li2 = new ArrayList<>();
                    li2.add(rf);
                    map.put(labels, li2);
                }
            }
        }

        //遍历标签，添加数据
        for (Map.Entry<String, List<RemoteFile>> entry : map.entrySet()) {
            if (entry.getKey().equals("1")) {
                personList.addAll(entry.getValue());
            }
            if (entry.getKey().equals("2")) {
                mapList.addAll(entry.getValue());
            }
            if (entry.getKey().equals("3")) {
                commemorationList.addAll(entry.getValue());
            }
            if (entry.getKey().equals("4")) {
                petList.addAll(entry.getValue());
            }
            if (entry.getKey().equals("5")) {
                skyList.addAll(entry.getValue());
            }
            if (entry.getKey().equals("6")) {
                sceneryList.addAll(entry.getValue());
            }
            if (entry.getKey().equals("7")) {
                delicacyList.addAll(entry.getValue());
            }
            if (entry.getKey().equals("8")) {
                abroadList.addAll(entry.getValue());
            }
            if (entry.getKey().equals("9")) {
                childList.addAll(entry.getValue());
            }
            if (entry.getKey().equals("100")) {
                rollList.addAll(entry.getValue());
            }
        }

        //拼装数据
        if (rollList.size() > 0) {//滑动
            AlbumMultipleItem rollItems = new AlbumMultipleItem(AlbumMultipleItem.RECOMMEND);
            rollItems.setFiles(rollList);
            items.add(rollItems);
        }

        if (personList.size() > 0) {//人物
            AlbumMultipleItem personItems = new AlbumMultipleItem(AlbumMultipleItem.PERSONAGE);
            personItems.setFiles(personList);
            items.add(personItems);
        }

        AlbumMultipleItem classifyItems = new AlbumMultipleItem(AlbumMultipleItem.CLASSIFY);//分类
        List<Classify> classifyList = new ArrayList<>();//子分类列表
        if (commemorationList.size() > 0) {//纪念照
            Classify classify = new Classify();
            classify.setFiles(commemorationList);
            classify.setTitle("纪念照");
            classifyList.add(classify);
        }

        if (petList.size() > 0) {//宠物
            Classify classify = new Classify();
            classify.setFiles(petList);
            classify.setTitle("宠物");
            classifyList.add(classify);
        }

        if (skyList.size() > 0) {//天空
            Classify classify = new Classify();
            classify.setFiles(skyList);
            classify.setTitle("天空");
            classifyList.add(classify);
        }

        if (sceneryList.size() > 0) {//风景
            Classify classify = new Classify();
            classify.setFiles(sceneryList);
            classify.setTitle("风景");
            classifyList.add(classify);
        }

        if (delicacyList.size() > 0) {
            Classify classify = new Classify();
            classify.setFiles(delicacyList);
            classify.setTitle("美食");
            classifyList.add(classify);
        }

        if (abroadList.size() > 0) {//国外
            Classify classify = new Classify();
            classify.setFiles(abroadList);
            classify.setTitle("国外");
            classifyList.add(classify);
        }

        if (childList.size() > 0) {//孩子
            Classify classify = new Classify();
            classify.setFiles(childList);
            classify.setTitle("孩子");
            classifyList.add(classify);
        }
        classifyItems.setClassifies(classifyList);
        items.add(classifyItems);

        if (mapList.size() > 0) {
            AlbumMultipleItem mapItems = new AlbumMultipleItem(AlbumMultipleItem.MAP);
            mapItems.setFiles(mapList);
            items.add(mapItems);
        }
        if (remoteFiles.size() > 0) {
            llEmptyPage.setVisibility(View.GONE);
            recyRemotePhoto.setVisibility(View.VISIBLE);
        } else {
            llEmptyPage.setVisibility(View.VISIBLE);
            recyRemotePhoto.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }
}
