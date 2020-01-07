package com.ehualu.calabashandroid.upload;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UploadChunk implements Serializable {

    private String chunkId;
    private String taskId;
    private String targetDirId;
    private String fileName;//需要base64加密
    private Long chunkNum;
    private long chunkSize;
    private long chunkTotal;
    private String md5;
    private File partFile;

    public String getChunkId() {
        return chunkId;
    }

    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTargetDirId() {
        return targetDirId;
    }

    public void setTargetDirId(String targetDirId) {
        this.targetDirId = targetDirId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getChunkNum() {
        return chunkNum;
    }

    public void setChunkNum(Long chunkNum) {
        this.chunkNum = chunkNum;
    }

    public long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(long chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Long getChunkTotal() {
        return chunkTotal;
    }

    public void setChunkTotal(Long chunkTotal) {
        this.chunkTotal = chunkTotal;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public File getPartFile() {
        return partFile;
    }

    public void setPartFile(File partFile) {
        this.partFile = partFile;
    }

    public Map<String, String> convert() {
        Map<String, String> map = new HashMap<>();
        map.put("chunkId", chunkId);
        map.put("taskId", taskId);
        map.put("targetDirId", targetDirId);
        map.put("fileName", fileName);
        map.put("chunkNum", chunkNum + "");
        map.put("chunkSize", chunkSize + "");
        map.put("chunkTotal", chunkTotal + "");
        map.put("md5", md5);
        return map;
    }
}
