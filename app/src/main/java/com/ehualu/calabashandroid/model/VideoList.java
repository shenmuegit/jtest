package com.ehualu.calabashandroid.model;

import java.util.List;

public class VideoList implements Comparable<VideoList> {

    private String date;
    private long time;
    private List<Video> videos;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public int compareTo(VideoList o) {
        return this.getTime() > o.getTime() ? -1 : 1;
    }
}
