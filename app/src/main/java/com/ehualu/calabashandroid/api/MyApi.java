package com.ehualu.calabashandroid.api;

import com.ehualu.calabashandroid.model.UserInfo;
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

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface MyApi {

    //    String BASE_URL = "http://172.16.1.242:8080/";
    String BASE_URL = "http://49.7.59.236:13800/";

    @GET("ocs/v1.php/cloud/user?format=json")
    Observable<UserInfo> getUserInfo();

    //获取文件列表
    @POST("calabash/file/_search")
    Observable<ResponseFileSearchBean> getFileList(@Body RequestBody body);

    /**
     * 文件详情
     */
    @GET("calabash/file/{fileId}")
    Observable<ResponseFileInfoBean> getFileInfo(@Path("fileId") String fileId);

    /**
     * 文件夹详情
     */
    @GET("calabash/file/fileFolder/{fileId}")
    Observable<ResponseFolderInfoBean> getFolderInfo(@Path("fileId") String fileId);

    /**
     * 文件重命名
     */
    @POST("calabash/file/_rename")
    Observable<PublicResponseBean> postRename(@Query("newFileName") String newFileName,
                                              @Query("fileId") String fileId, @Query("category") String category);

    /**
     * 上传接口
     */
    @Multipart
    @POST("calabash/file/_upload")
    Observable<UploadPieceResponse> uploadFile(@HeaderMap Map<String, String> headers, @Part MultipartBody.Part part);

    /**
     * 下载接口
     *
     * @param fileId
     * @param startIndex
     * @param endIndex
     * @return
     */
    @Streaming
    @GET("calabash/file/_downloadFile")
    Call<ResponseBody> downloadFile(@Header("Accept-Encoding") String acceptEncoding, @Query("fileId") String fileId,
                                    @Query("startIndex") long startIndex, @Query("endIndex") long endIndex);

    /**
     * 文件永存
     */
    @POST("calabash/file/_immortal")
    Observable<BaseResponseBean> postImmortal(@Query(("fileId")) String fileId, @Query("category") String category);

    /**
     * 文件收藏
     */
    @POST("calabash/file/_collection")
    Observable<PublicResponseBean> postCollection(@Body RequestBody body);

    /**
     * 新建文件夹
     */
    @POST("calabash/file/_insertDir")
    Observable<CreateFolderResponse> postCreateFolder(@Body RequestBody body);

    /**
     * 文件(夹)移动
     */
    @POST("calabash/file/_move")
    Observable<PublicResponseBean> postMove(@Body RequestBody bean);

    /**
     * 文件删除
     */
    @POST("calabash/file/_delete")
    Observable<PublicResponseBean> postDelete(@Body RequestBody bean);

    /**
     * 葫芦备份
     */
    @POST("calabash/file/_backUp")
    Observable<PublicResponseBean> postBackUp(@Body RequestBody bean);

    /**
     * 葫芦提取
     */
    @POST("calabash/file/_backUpRecover")
    Observable<PublicResponseBean> postExtract(@Body RequestBody bean);


    /**
     * 创建任务id，保证唯一性
     *
     * @return
     */
    @GET("calabash/file/taskId")
    Observable<CreateTaskIdResponse> createTask();

    /**
     * 在分片传输完毕后，获取服务器的接收状态，如果成功，更新更新数据库
     *
     * @return
     */
    @GET("calabash/file/uploadStatus")
    Observable<UploadNotifyResponse> notifyUploadStatus(@Query("taskId") String taskId);

    /**
     * 获取Base64的图片
     */
    @GET("calabash/file/base64")
    Observable<ResponseBase64PhotoBean> getBase64Photo(@Query("fileIds") String fileIds);

    /**
     * 获取缩略图URL
     */
    public static String getThumbnailUrl = BASE_URL + "calabash/file/thumbnail/";
}
