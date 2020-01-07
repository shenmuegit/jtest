package com.ehualu.calabashandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FileEntity {

    @Id
    private Long id;
    private String fileName;
    private String thumbnail;//文件缩略图
    private long fileSize;//文件大小
    @Generated(hash = 1411906061)
    public FileEntity(Long id, String fileName, String thumbnail, long fileSize) {
        this.id = id;
        this.fileName = fileName;
        this.thumbnail = thumbnail;
        this.fileSize = fileSize;
    }
    @Generated(hash = 1879603201)
    public FileEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getThumbnail() {
        return this.thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public long getFileSize() {
        return this.fileSize;
    }
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
