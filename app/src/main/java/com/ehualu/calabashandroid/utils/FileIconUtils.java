package com.ehualu.calabashandroid.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.responseBean.ResponseBase64PhotoBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FileIconUtils {

    public static int getFileIcon(String fileName) {

        if (fileName.indexOf(".") > -1) {
            Log.e("fileName", fileName + "----------->");
            int lastIndex = fileName.lastIndexOf(".");
            String suffix = fileName.substring(lastIndex + 1);
            Log.e("fileName", fileName + "----------->" + suffix);
            switch (suffix) {
                case "txt":
                    return R.mipmap.file_icon_txt;
                case "html":
                    return R.mipmap.file_icon_html;
                case "ppt":
                case "pptx":
                    return R.mipmap.file_icon_ppt;
                case "pdf":
                    return R.mipmap.file_icon_pdf;
                case "xls":
                case "xlsx":
                    return R.mipmap.file_icon_execl;
                case "doc":
                case "docx":
                    return R.mipmap.file_icon_word;
                case "mp3":
                    return R.mipmap.file_icon_mp3;
                case "jpg":
                case "png":
                case "jpeg":
                    return R.mipmap.file_icon_picture;
                case "mp4":
                case "rmvb":
                case "avi":
                case "3gp":
                    return R.mipmap.file_icon_video;
                default:
                    return R.mipmap.file_icon_undefined;

            }
        } else {
            //返回默认文件图标
            return R.mipmap.file_icon_undefined;
        }
    }

    /**
     * 根据文件后缀名获取文件图标
     *
     * @param remoteFile
     * @return
     */
    public static int getFileIcon(RemoteFile remoteFile) {
        if ("2".equals(remoteFile.getCategory())) { //文件夹
            if (!remoteFile.isCanSelected()) {
                switch (remoteFile.getFileName()) {
                    case "葫芦备份":
                        return R.mipmap.file_icon_backup;
                    case "备份恢复":
                        return R.mipmap.file_icon_restore;
                    case "收到文件":
                        return R.mipmap.file_icon_received;
                    case "我的收藏":
                        return R.mipmap.file_icon_favourite;
                    case "我的相册":
                        return R.mipmap.file_icon_album;
                }
            }
            return R.mipmap.file_icon_normal_folder;
        } else {  //文件
            String fileName = remoteFile.getFileName();
            if (fileName.indexOf(".") > -1) {
                Log.e("fileName", fileName + "----------->");
                int lastIndex = fileName.lastIndexOf(".");
                String suffix = fileName.substring(lastIndex + 1);
                Log.e("fileName", fileName + "----------->" + suffix);
                switch (suffix) {
                    case "txt":
                        return R.mipmap.file_icon_txt;
                    case "html":
                        return R.mipmap.file_icon_html;
                    case "ppt":
                    case "pptx":
                        return R.mipmap.file_icon_ppt;
                    case "pdf":
                        return R.mipmap.file_icon_pdf;
                    case "xls":
                    case "xlsx":
                        return R.mipmap.file_icon_execl;
                    case "doc":
                    case "docx":
                        return R.mipmap.file_icon_word;
                    case "mp3":
                        return R.mipmap.file_icon_mp3;
                    case "jpg":
                    case "png":
                    case "jpeg":
                        return R.mipmap.file_icon_picture;
                    case "mp4":
                    case "rmvb":
                    case "avi":
                    case "3gp":
                        return R.mipmap.file_icon_video;
                    default:
                        return R.mipmap.file_icon_undefined;

                }
            } else {
                //返回默认文件图标
                return R.mipmap.file_icon_undefined;
            }
        }
    }

    /**
     * add by houxiansheng 2019-12-30 14:11:56 加载图片，支持base64的图片
     */
    public static void getImageIcon(Context mContext, RemoteFile remoteFile, ImageView imageView) {
        String thumbnail = remoteFile.getThumbnail();
        if (!TextUtils.isEmpty(thumbnail) && isBase64Img(thumbnail)) {
            thumbnail = thumbnail.split(",")[1];
            byte[] decode = Base64.decode(thumbnail, Base64.DEFAULT);
            Glide.with(mContext).load(decode).transform(new GlideRoundTransform(mContext, 5)).into(imageView);
        } else {
            Glide.with(mContext).load(getFileIcon(remoteFile)).transform(new GlideRoundTransform(mContext, 5)).into(imageView);
        }
    }

    /**
     * 判断图片是否是base64的图片
     */
    public static boolean isBase64Img(String imgurl) {
        if (!TextUtils.isEmpty(imgurl) && (imgurl.startsWith("data:image/png;base64,")
                || imgurl.startsWith("data:image/*;base64,")
                || imgurl.startsWith("data:image/jpg;base64,"))) {
            return true;
        }
        return false;
    }

    /**
     * add by houxiansheng 2019-12-30 23:05:15 加载Base64的图片
     */
    public static void getBase64Photo(Context mContext, RemoteFile remoteFile, ImageView imageView) {
        //先设置图片占位符
        imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.file_icon_picture));
        final String url = remoteFile.getFileName();
        //为imageView设置Tag,内容是该imageView等待加载的图片url
        imageView.setTag(url);
        if (remoteFile.getCategory().equals("2")) {
            if (url.equals(imageView.getTag())) {
                Glide.with(mContext).load(getFileIcon(remoteFile)).placeholder(R.mipmap.file_icon_normal_folder).transform(new GlideRoundTransform(mContext, 5)).into(imageView);
            }
        } else {
            if (!TextUtils.isEmpty(remoteFile.getThumbnail())) {
                ApiRetrofit.getInstance().getBase64Photo(remoteFile.getID())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseBase64PhotoBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseBase64PhotoBean responseBase64PhotoBean) {
                                MyLog.d(responseBase64PhotoBean.toString());
                                if (responseBase64PhotoBean.isSuccess()) {
                                    if (url.equals(imageView.getTag())) {
                                        String base64 = responseBase64PhotoBean.getData().get(0).getBase64();
                                        byte[] base = Base64.decode(base64, Base64.DEFAULT);
                                        Glide.with(MyApp.getAppContext()).load(base).placeholder(R.mipmap.file_icon_picture).transform(new GlideRoundTransform(mContext, 5)).into(imageView);
                                    }
                                } else {
                                    if (url.equals(imageView.getTag())) {
                                        Glide.with(mContext).load(getFileIcon(remoteFile)).placeholder(R.mipmap.file_icon_picture).transform(new GlideRoundTransform(mContext, 5)).into(imageView);
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showCenterHasImageToast(mContext, "加载图片失败！");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } else {
                if (url.equals(imageView.getTag())) {
                    Glide.with(mContext).load(getFileIcon(remoteFile)).placeholder(R.mipmap.file_icon_picture).transform(new GlideRoundTransform(mContext, 5)).into(imageView);
                }
            }
        }
    }

    /**
     * 获取缩略图：URL
     */
    public static void getThumnailURL(Context mContext, RemoteFile remoteFile, ImageView imageView) {
        if (remoteFile.getCategory().equals("1")) {
            if (!TextUtils.isEmpty(remoteFile.getThumbnail())) {
                Glide.with(mContext).load(remoteFile.getThumbnail()).skipMemoryCache(false).placeholder(R.mipmap.file_icon_picture).transform(new GlideRoundTransform(mContext, 5)).into(imageView);
            } else {
                Glide.with(mContext).load(getFileIcon(remoteFile)).placeholder(R.mipmap.file_icon_picture).transform(new GlideRoundTransform(mContext, 5)).into(imageView);
            }
        } else {
            Glide.with(mContext).load(getFileIcon(remoteFile)).placeholder(R.mipmap.file_icon_normal_folder).transform(new GlideRoundTransform(mContext, 5)).into(imageView);
        }
    }
}
