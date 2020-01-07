package com.ehualu.calabashandroid.download;

/**
 * 下载文件的分片对象，包括起始和终止的索引位置，文件的唯一id
 */
public class DownloadChunk {

    private long startIndex;
    private long endIndex;
    private String fileId;

    public DownloadChunk(long startIndex, long endIndex, String fileId) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.fileId = fileId;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
