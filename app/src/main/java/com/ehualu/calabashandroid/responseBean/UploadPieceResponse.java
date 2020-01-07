package com.ehualu.calabashandroid.responseBean;

public class UploadPieceResponse {
    /**
     * success : false
     * message : 失败
     * data : null
     * status : 300
     * totalCount : 0
     * fileSize : 0
     */

    private boolean success;
    private String message;
    private Object data;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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
}
