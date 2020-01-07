package com.ehualu.calabashandroid.requestBean;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-24 11:33:23
 * <p>
 * describe：请求文件重命名的Bean
 */
public class RequestRenameBean {
    /**
     * newFileName : 新建文件
     * fileId : gadfhdfgdfshgrthgfdgfdh
     * category : 1
     */

    private String newFileName;
    private String fileId;
    private String category;

    public RequestRenameBean(String newFileName, String fileId, String category) {
        this.newFileName = newFileName;
        this.fileId = fileId;
        this.category = category;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
