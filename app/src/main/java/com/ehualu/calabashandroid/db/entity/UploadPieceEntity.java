package com.ehualu.calabashandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 上传的分片实体类
 */
@Entity
public class UploadPieceEntity {

    @Id
    private Long id;
    private String chunkId;//分片的id
    private String taskId;//分片所属的任务id,同一个文件，taskId一样
    private Long current;//该分片当前已读字节数
    private String dirId;//所属的目标路径
    private String fileName;//文件名称
    private Long chunkSize;//分片大小
    private Long chunkTotal;//分片总数
    private Long chunkNum;//当前分片
    private Integer status;//传输状态，0表示等待中，1表示进行中，2表示暂停，3表示传输失败，4表示已完成
    @Generated(hash = 1750393024)
    public UploadPieceEntity(Long id, String chunkId, String taskId, Long current,
            String dirId, String fileName, Long chunkSize, Long chunkTotal,
            Long chunkNum, Integer status) {
        this.id = id;
        this.chunkId = chunkId;
        this.taskId = taskId;
        this.current = current;
        this.dirId = dirId;
        this.fileName = fileName;
        this.chunkSize = chunkSize;
        this.chunkTotal = chunkTotal;
        this.chunkNum = chunkNum;
        this.status = status;
    }
    @Generated(hash = 1959429609)
    public UploadPieceEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getChunkId() {
        return this.chunkId;
    }
    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }
    public String getTaskId() {
        return this.taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public Long getCurrent() {
        return this.current;
    }
    public void setCurrent(Long current) {
        this.current = current;
    }
    public String getDirId() {
        return this.dirId;
    }
    public void setDirId(String dirId) {
        this.dirId = dirId;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public Long getChunkSize() {
        return this.chunkSize;
    }
    public void setChunkSize(Long chunkSize) {
        this.chunkSize = chunkSize;
    }
    public Long getChunkTotal() {
        return this.chunkTotal;
    }
    public void setChunkTotal(Long chunkTotal) {
        this.chunkTotal = chunkTotal;
    }
    public Long getChunkNum() {
        return this.chunkNum;
    }
    public void setChunkNum(Long chunkNum) {
        this.chunkNum = chunkNum;
    }
    public Integer getStatus() {
        return this.status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
