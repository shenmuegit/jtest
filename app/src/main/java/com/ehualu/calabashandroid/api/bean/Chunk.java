package com.ehualu.calabashandroid.api.bean;

public class Chunk {

    public long start;
    public long end;
    public int partnumber;//从0开始

    public Chunk(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long length() {
        return end - start + 1;
    }
}
