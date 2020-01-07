package com.ehualu.calabashandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UploadEntity {

    @Id
    private Long id;
    private String taskId;//上传任务id
    private String fileName;//文件名称
    private String dirId;//上传的目标路径
    private Integer status;//0表示等待中，1表示进行中，2表示暂停，3表示传输失败，4表示传输完成
    private Long createTime;//任务创建时间
    private Long updateTime;//任务更新时间
    private Long fileSize;//文件大小
    private long progress;//已经上传的字节数
    private String path;//文件的路径
    private String targetPath;//上传的目标路径
    private int fileStatus;//0表示正常状态，1表示被清空列表，2表示文件被删除
    @Generated(hash = 2113952504)
    public UploadEntity(Long id, String taskId, String fileName, String dirId,
            Integer status, Long createTime, Long updateTime, Long fileSize,
            long progress, String path, String targetPath, int fileStatus) {
        this.id = id;
        this.taskId = taskId;
        this.fileName = fileName;
        this.dirId = dirId;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.fileSize = fileSize;
        this.progress = progress;
        this.path = path;
        this.targetPath = targetPath;
        this.fileStatus = fileStatus;
    }
    @Generated(hash = 1993781207)
    public UploadEntity() {
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
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getDirId() {
        return this.dirId;
    }
    public void setDirId(String dirId) {
        this.dirId = dirId;
    }
    public Integer getStatus() {
        return this.status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public Long getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
    public Long getFileSize() {
        return this.fileSize;
    }
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    public long getProgress() {
        return this.progress;
    }
    public void setProgress(long progress) {
        this.progress = progress;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getTargetPath() {
        return this.targetPath;
    }
    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
    public int getFileStatus() {
        return this.fileStatus;
    }
    public void setFileStatus(int fileStatus) {
        this.fileStatus = fileStatus;
    }

}
