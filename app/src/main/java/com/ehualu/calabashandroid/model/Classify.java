package com.ehualu.calabashandroid.model;

import java.util.List;

public class Classify {
    private String title;
    private List<RemoteFile> files;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RemoteFile> getFiles() {
        return files;
    }

    public void setFiles(List<RemoteFile> files) {
        this.files = files;
    }
}
