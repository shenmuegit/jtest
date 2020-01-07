package com.ehualu.calabashandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TestFile {

    @Id
    private Long id;
    private String path;
    private long size;
    @Generated(hash = 718087854)
    public TestFile(Long id, String path, long size) {
        this.id = id;
        this.path = path;
        this.size = size;
    }
    @Generated(hash = 870352640)
    public TestFile() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
}
