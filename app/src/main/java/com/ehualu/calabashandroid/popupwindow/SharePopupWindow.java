package com.ehualu.calabashandroid.popupwindow;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.ShareAdapter;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.model.ShareModel;
import com.ehualu.calabashandroid.utils.FileUtils;
import com.ehualu.calabashandroid.utils.MyLog;
import com.ehualu.calabashandroid.utils.MyPopupWindowUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author: houxiansheng
 * time：2019-6-14 10:47:02
 * describe：分享弹窗
 */
public class SharePopupWindow {
    private Activity mContext;
    private PopupWindow popupWindow;
    private RemoteFile remoteFile;

    public SharePopupWindow(Activity mContext, RemoteFile remoteFile) {
        this.mContext = mContext;
        this.remoteFile = remoteFile;
    }

    /**
     * add by houxiansheng 2019-6-13 14:23:32 显示弹窗列表
     */
    public void showPopupWindow(View view) {
        popupWindow = new PopupWindow(mContext);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popup_share, null);
        popupWindow.setContentView(contentView);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frg_file_popup_bg));

        //设置背景透明
        MyPopupWindowUtils.addBackground(mContext, popupWindow);
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
        setShareButton(contentView);
    }

    private void setShareButton(View contentView) {
        // populate send apps
        Intent sendIntent = createSendIntent();
        List<ShareModel> sendButtonDataList = setupSendButtonData(sendIntent);
        ShareAdapter.ClickListener clickListener = setupSendButtonClickListener(sendIntent);
        RecyclerView sendButtonsView = contentView.findViewById(R.id.share_recyclerView);
        sendButtonsView.setHasFixedSize(true);
        sendButtonsView.setLayoutManager(new GridLayoutManager(mContext, 4));
        sendButtonsView.setAdapter(new ShareAdapter(sendButtonDataList, clickListener));
    }

    private Intent createSendIntent() {
        File file = new File(remoteFile.getPath());
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType(FileUtils.getMimeType(remoteFile.getPath()));
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 授权读权限，必须添加这一句，否则找不到资源
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MyApp.getAppContext(), "com.ehualu.calabashandroid.FileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return sendIntent;
    }


    private List<ShareModel> setupSendButtonData(Intent sendIntent) {
        List<ShareModel> shareModelList = new ArrayList<>();
        for (ResolveInfo match : mContext.getPackageManager().queryIntentActivities(sendIntent, 0)) {
            Drawable icon = match.loadIcon(mContext.getPackageManager());
            CharSequence label = match.loadLabel(mContext.getPackageManager());
            ShareModel shareModel = new ShareModel(icon, label, match.activityInfo.packageName,
                    match.activityInfo.name);
            MyLog.d("shareModel===" + shareModel.toString());
            shareModelList.add(shareModel);
        }
        return shareModelList;
    }

    private ShareAdapter.ClickListener setupSendButtonClickListener(Intent sendIntent) {
        return shareModel -> {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            String packageName = shareModel.getPackageName();
            String activityName = shareModel.getActivityName();
            sendIntent.setComponent(new ComponentName(packageName, activityName));
            mContext.startActivity(Intent.createChooser(sendIntent, "分享"));
        };
    }

}
