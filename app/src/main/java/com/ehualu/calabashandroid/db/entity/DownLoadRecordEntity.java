package com.ehualu.calabashandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;
import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by GaoTing on 2019/12/27.
 * <p>
 * Explain:文件下载记录
 */
@Entity
public class DownLoadRecordEntity implements Serializable{


    private static final long serialVersionUID = -4446833317411247555L;
    @Id
    private Long id;
    @NotNull
    private String taskId;//下载任务id
    @NotNull
    private String userId;//用户Id
    @NotNull
    private String fileId;//文件id;
    @NotNull
    private String fileName;//文件名
    @NotNull
    private String fileType;//1,文件,2,片段

    private Integer partNum;//当前片(从0开始)
    @NotNull
    private Long fileSize;//文件大小/片段大小
    @NotNull
    private String path;//本地路径
    @NotNull
    private int status;////任务状态   0:待下载,1:下载中,2,暂停,3:失败,4,完成;
    @NotNull
    private Date createTime;//创建时间
    @NotNull
    private Date updateTime;//更新时间


    @Generated(hash = 2111712130)
    public DownLoadRecordEntity() {
    }


    @Generated(hash = 2020272392)
    public DownLoadRecordEntity(Long id, @NotNull String taskId,
                                @NotNull String userId, @NotNull String fileId,
                                @NotNull String fileName, @NotNull String fileType, Integer partNum,
                                @NotNull Long fileSize, @NotNull String path, int status,
                                @NotNull Date createTime, @NotNull Date updateTime) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.partNum = partNum;
        this.fileSize = fileSize;
        this.path = path;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getPartNum() {
        return this.partNum;
    }

    public void setPartNum(Integer partNum) {
        this.partNum = partNum;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getFileId() {
        return this.fileId;
    }


    public void setFileId(String fileId) {
        this.fileId = fileId;
    }


    public String getFileName() {
        return this.fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public Long getFileSize() {
        return this.fileSize;
    }


    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }


}
