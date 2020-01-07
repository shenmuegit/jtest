package com.ehualu.calabashandroid.model;

import java.util.List;

public class RemoteFileListSortByTime {

    private String showDate;
    private long time;
    private List<RemoteFile> list;

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<RemoteFile> getList() {
        return list;
    }

    public void setList(List<RemoteFile> list) {
        this.list = list;
    }
}
