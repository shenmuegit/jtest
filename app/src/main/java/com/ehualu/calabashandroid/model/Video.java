package com.ehualu.calabashandroid.model;

public class Video {
    private long duration;
    private String path;
    private long date;

    public Video() {
    }

    public Video(long duration, String path, long date) {
        this.duration = duration;
        this.path = path;
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}
