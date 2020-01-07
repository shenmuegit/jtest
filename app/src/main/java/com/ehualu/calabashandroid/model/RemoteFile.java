package com.ehualu.calabashandroid.model;

import java.io.Serializable;

public class RemoteFile implements Serializable {

    private String fileName;
    private String thumbnail;
    private long fileSize;
    private long updateTime;
    private String location;
    private String ID;
    private String category;//1是文件，2是文件夹
    private String parentId;
    private String labels;
    private String path;
    private String duration;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * 判断该文件是否可选
     *
     * @return
     */
    public boolean isCanSelected() {
        boolean canSelected = true;
        if ("0".equals(getParentId()) && "2".equals(getCategory())) {
            if ("葫芦备份".equals(getFileName()) ||
                    "备份恢复".equals(getFileName()) ||
                    "收到文件".equals(getFileName()) ||
                    "我的收藏".equals(getFileName()) ||
                    "我的相册".equals(getFileName())) {
                canSelected = false;
            }
        }
        return canSelected;
    }

    @Override
    public String toString() {
        return "RemoteFile{" +
                "fileName='" + fileName + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", fileSize=" + fileSize +
                ", updateTime=" + updateTime +
                ", location='" + location + '\'' +
                ", ID='" + ID + '\'' +
                ", category='" + category + '\'' +
                ", parentId='" + parentId + '\'' +
                ", labels='" + labels + '\'' +
                ", path='" + path + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
