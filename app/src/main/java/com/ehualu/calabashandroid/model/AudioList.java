package com.ehualu.calabashandroid.model;

import java.util.List;

public class AudioList implements Comparable<AudioList> {

    private String date;
    private long time;
    private List<RemoteFile> audios;

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

    public List<RemoteFile> getAudios() {
        return audios;
    }

    public AudioList setAudios(List<RemoteFile> audios) {
        this.audios = audios;
        return this;
    }

    @Override
    public int compareTo(AudioList o) {
        return this.getTime() > o.getTime() ? -1 : 1;
    }
}
