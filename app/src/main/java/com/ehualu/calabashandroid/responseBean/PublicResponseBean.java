package com.ehualu.calabashandroid.responseBean;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-26 10:49:10
 * <p>
 * describe：服务器返回的公共的Bean
 */
public class PublicResponseBean {

    /**
     * success : true
     * message : 成功
     * data : SUCCESS
     * status : 200
     * totalCount : 0
     * fileSize : 0
     */

    private boolean success;
    private String message;
    private String data;
    private int status;
    private String totalCount;
    private String fileSize;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "PublicResponseBean{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                ", status=" + status +
                ", totalCount='" + totalCount + '\'' +
                ", fileSize='" + fileSize + '\'' +
                '}';
    }
}
