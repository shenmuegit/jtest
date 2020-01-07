package com.ehualu.calabashandroid.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class AlbumMultipleItem implements MultiItemEntity {

    public static final int RECOMMEND = 1;
    public static final int PERSONAGE = 2;
    public static final int CLASSIFY = 3;
    public static final int MAP = 4;

    private int itemType;

    private List<RemoteFile> files;
    private List<Classify> classifies;//这是针对分类列表，每个分类又是一个列表

    public AlbumMultipleItem(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public List<RemoteFile> getFiles() {
        return files;
    }

    public void setFiles(List<RemoteFile> files) {
        this.files = files;
    }

    public List<Classify> getClassifies() {
        return classifies;
    }

    public void setClassifies(List<Classify> classifies) {
        this.classifies = classifies;
    }
}

