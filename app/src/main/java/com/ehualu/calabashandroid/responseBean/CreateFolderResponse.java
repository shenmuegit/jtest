package com.ehualu.calabashandroid.responseBean;

public class CreateFolderResponse {
    /**
     * success : true
     * message : 成功
     * data : {"dirId":"1323825329340448"}
     * status : 200
     * totalCount : 0
     * fileSize : 0
     */

    private boolean success;
    private String message;
    private DataBean data;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
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

    public static class DataBean {
        /**
         * dirId : 1323825329340448
         */

        private String dirId;

        public String getDirId() {
            return dirId;
        }

        public void setDirId(String dirId) {
            this.dirId = dirId;
        }
    }
}
