package com.ehualu.calabashandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.activity.MainActivity;
import com.ehualu.calabashandroid.activity.SelectUploadPathActivity;
import com.ehualu.calabashandroid.activity.SelectVideoActivity;
import com.ehualu.calabashandroid.adapter.SelectVideoAdapter;
import com.ehualu.calabashandroid.base.BaseAdapterForVideo;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.interfaces.VideoOnItemClickInterface;
import com.ehualu.calabashandroid.model.Video;
import com.ehualu.calabashandroid.model.VideoList;
import com.ehualu.calabashandroid.utils.LocalVideoHelper;
import com.ehualu.calabashandroid.utils.ToastUtil;
import com.ehualu.calabashandroid.widget.DrawableCenterRadioButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * 全部的本地视频列表
 */
public class AllVideoFragment extends BaseFragment implements VideoOnItemClickInterface {

    @BindView(R.id.recyVideoList)
    RecyclerView recyVideoList;
    @BindView(R.id.tvSelectPosition)
    DrawableCenterRadioButton tvSelectPosition;
    @BindView(R.id.tvEnsure)
    TextView tvEnsure;
    @BindView(R.id.llBottomSelectPath)
    LinearLayout llBottomSelectPath;
    private String dirID;

    private List<VideoList> lists = new ArrayList<>();
    public SelectVideoAdapter adapter;
    private LocalVideoHelper videoHelper;
    private SelectVideoActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (SelectVideoActivity) context;
        dirID = activity.dirID;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void setUpView() {
        tvEnsure.setOnClickListener(this);
        tvSelectPosition.setOnClickListener(this);
        videoHelper = new LocalVideoHelper();
    }

    @Override
    protected void setUpData() {
        videoHelper.initVideo(activity, new DataCallBack() {
            @Override
            public void onReadFinished(List<VideoList> videos) {
                lists.addAll(videos);
                adapter = new SelectVideoAdapter(activity, lists, ALL_UPLOAD);
                recyVideoList.setLayoutManager(new LinearLayoutManager(activity));
                recyVideoList.setAdapter(adapter);
                adapter.setListener(AllVideoFragment.this);
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
            case R.id.tvEnsure:
                ArrayList<Video> videos = adapter.getCheckedList();
                if (videos.size() == 0) {
                    ToastUtil.showCenterForBusiness(activity, "请选择要上传的视频！");
                } else {
                    //开始文件上传
                    for (Video video : videos) {
                        File file = new File(video.getPath());
                        if (file.exists()) {
                            startUpload(file, dirID);
                        }
                    }
                }
                Intent toMain = new Intent(activity, MainActivity.class);
                startActivity(toMain);
                break;
            case R.id.tvSelectPosition:
                Intent intent = new Intent(activity, SelectUploadPathActivity.class);
                startActivityForResult(intent, REQUEST_TARGET_PATH);
                break;
        }
    }

    @Override
    public void onItemClick(Video t) {

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
    public void selectCount(List<Video> selectedList, int count) {

    }
}
