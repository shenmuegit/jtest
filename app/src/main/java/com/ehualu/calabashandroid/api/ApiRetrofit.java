package com.ehualu.calabashandroid.api;

import android.util.Base64;

import com.ehualu.calabashandroid.activity.MainActivity;
import com.ehualu.calabashandroid.api.base.BaseApiRetrofit;
import com.ehualu.calabashandroid.model.UserInfo;
import com.ehualu.calabashandroid.requestBean.RequestBackUpAndExtractBean;
import com.ehualu.calabashandroid.requestBean.RequestCollectBean;
import com.ehualu.calabashandroid.requestBean.RequestCreateFolderBean;
import com.ehualu.calabashandroid.requestBean.RequestDeleteFileBean;
import com.ehualu.calabashandroid.requestBean.RequestFileSearchBean;
import com.ehualu.calabashandroid.requestBean.RequestMoveBean;
import com.ehualu.calabashandroid.responseBean.BaseResponseBean;
import com.ehualu.calabashandroid.responseBean.CreateFolderResponse;
import com.ehualu.calabashandroid.responseBean.CreateTaskIdResponse;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;
import com.ehualu.calabashandroid.responseBean.ResponseBase64PhotoBean;
import com.ehualu.calabashandroid.responseBean.ResponseFileInfoBean;
import com.ehualu.calabashandroid.responseBean.ResponseFileSearchBean;
import com.ehualu.calabashandroid.responseBean.ResponseFolderInfoBean;
import com.ehualu.calabashandroid.responseBean.UploadNotifyResponse;
import com.ehualu.calabashandroid.responseBean.UploadPieceResponse;
import com.ehualu.calabashandroid.upload.ProgressRequestBody;
import com.ehualu.calabashandroid.upload.UploadRunnable;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofit extends BaseApiRetrofit {

    public MyApi myApi;
    public static ApiRetrofit mInstance;

    private ApiRetrofit() {
        super();
        myApi = new Retrofit.Builder().baseUrl(MyApi.BASE_URL).client(getClient()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(MyApi.class);
    }

    public static ApiRetrofit getInstance() {
        if (mInstance == null) {
            synchronized (ApiRetrofit.class) {
                if (mInstance == null) {
                    mInstance = new ApiRetrofit();
                }
            }
        }
        return mInstance;
    }

    private RequestBody getRequestBody(Object obj) {
        String json = new Gson().toJson(obj);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        return body;
    }

    private MultipartBody.Part getPart(File file, String taskId, long originFileLength, UploadRunnable uploadRunnable) {
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(taskId, originFileLength,
                uploadRunnable, file, MediaType.parse("multipart/form-data"));
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", Base64.encodeToString((file.getName()).getBytes(), Base64.DEFAULT), progressRequestBody);
        MainActivity.requestBodies.add(progressRequestBody);
        return part;
    }

    public Observable<UserInfo> getUserInfo() {
        return myApi.getUserInfo();
    }

    /**
     * 文件列表
     */
    public Observable<ResponseFileSearchBean> getFileList(String dirID, String fileType, String keywords,
                                                          String collectStatus, String backupStatus,
                                                          String classification, String order, String orderRule,
                                                          String index, String num, String autoSort) {
        RequestFileSearchBean bean = new RequestFileSearchBean(dirID, fileType, keywords, collectStatus, backupStatus
                , classification, order, orderRule, index, num, autoSort);
        return myApi.getFileList(getRequestBody(bean));
    }

    /**
     * 文件详情
     */
    public Observable<ResponseFileInfoBean> getFileInfo(String fileId) {
        return myApi.getFileInfo(fileId);
    }

    /**
     * 文件夹详情
     */
    public Observable<ResponseFolderInfoBean> getFolderInfo(String fileId) {
        return myApi.getFolderInfo(fileId);
    }

    /**
     * 文件重命名
     */
    public Observable<PublicResponseBean> postRename(String newFileName, String fileId, String category) {
        return myApi.postRename(newFileName, fileId, category);
    }

    public Observable<UploadPieceResponse> uploadFile(Map<String, String> headers, File file, long originFileLength,
                                                      UploadRunnable uploadRunnable) {
        return myApi.uploadFile(headers, getPart(file, headers.get("taskId"), originFileLength, uploadRunnable));
    }

    public Call<ResponseBody> downloadFile(String fileId, long startIndex, long endIndex) {
        return myApi.downloadFile("identity", fileId, startIndex, endIndex);
    }

    /**
     * 文件永存
     *
     * @param category 类型 1-文件，2文件夹
     * @param fileId   文件ID
     */
    public Observable<BaseResponseBean> postImmortal(String fileId, String category) {
        return myApi.postImmortal(fileId, category);
    }

    /**
     * 文件收藏
     *
     * @param fileId  文件ID
     * @param operate 类型 0:未收藏,1收藏
     */
    public Observable<PublicResponseBean> postCollection(String fileId, String operate) {
        RequestCollectBean bean = new RequestCollectBean(fileId, operate);
        return myApi.postCollection(getRequestBody(bean));
    }

    /**
     * 新建文件夹
     *
     * @param dirName     文件夹名称
     * @param parentDirId 文件夹所在路径ID
     * @return
     */
    public Observable<CreateFolderResponse> postCreateFolder(String dirName, String parentDirId) {
        RequestCreateFolderBean bean = new RequestCreateFolderBean(dirName, parentDirId);
        return myApi.postCreateFolder(getRequestBody(bean));
    }

    /**
     * 文件(夹)移动
     *
     * @param dirId
     * @param category
     * @param fileIds
     * @return
     */
    public Observable<PublicResponseBean> postMove(String dirId, String category, List<String> fileIds) {
        RequestMoveBean bean = new RequestMoveBean(dirId, category, fileIds);
        return myApi.postMove(getRequestBody(bean));
    }

    /**
     * 文件删除
     */
    public Observable<PublicResponseBean> postDelete(String fileId, String category) {
        RequestDeleteFileBean bean = new RequestDeleteFileBean(fileId, category);
        return myApi.postDelete(getRequestBody(bean));
    }

    /**
     * 葫芦备份
     */
    public Observable<PublicResponseBean> postBackUp(List<String> dirIds, List<String> fileIds) {
        RequestBackUpAndExtractBean.UserBean userBean = new RequestBackUpAndExtractBean.UserBean();
        userBean.setUserId("001");
        RequestBackUpAndExtractBean bean = new RequestBackUpAndExtractBean(userBean, dirIds, fileIds);
        return myApi.postBackUp(getRequestBody(bean));
    }

    /**
     * 葫芦提取
     */
    public Observable<PublicResponseBean> postExtract(List<String> dirIds, List<String> fileIds) {
        RequestBackUpAndExtractBean.UserBean userBean = new RequestBackUpAndExtractBean.UserBean();
        userBean.setUserId("001");
        RequestBackUpAndExtractBean bean = new RequestBackUpAndExtractBean(userBean, dirIds, fileIds);
        return myApi.postExtract(getRequestBody(bean));
    }

    public Observable<CreateTaskIdResponse> createTask() {
        return myApi.createTask();
    }

    public Observable<UploadNotifyResponse> notifyUploadStatus(String taskId) {
        return myApi.notifyUploadStatus(taskId);
    }

    /**
     * 获取Base64的图片
     */
    public Observable<ResponseBase64PhotoBean> getBase64Photo(String fileIds) {
        return myApi.getBase64Photo(fileIds);
    }

}
