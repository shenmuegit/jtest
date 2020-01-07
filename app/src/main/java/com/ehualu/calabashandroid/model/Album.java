package com.ehualu.calabashandroid.model;

import java.util.List;

public class Album {

    private int id;
    private String name;
    private int count;
    private List<String> files;//小于3张，直接返回count；大于等于3张，返回3张

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
