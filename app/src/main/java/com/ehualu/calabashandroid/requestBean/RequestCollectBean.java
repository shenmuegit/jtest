package com.ehualu.calabashandroid.requestBean;

/**
 * author: houxiansheng
 * <p>
 * time：2019年12月26日19:26:56
 * <p>
 * describe：请求收藏的Bean
 */
public class RequestCollectBean {

    /**
     * fileId : 1240151-59
     * operate : 0
     */

    private String fileId;
    private String operate;

    public RequestCollectBean(String fileId, String operate) {
        this.fileId = fileId;
        this.operate = operate;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
