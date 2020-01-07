package com.ehualu.calabashandroid.responseBean;

/**
 * 创建上传任务的taskId
 */
public class CreateTaskIdResponse {
    /**
     * success : true
     * message : 成功
     * data : {"taskId":"1323353499500576"}
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
         * taskId : 1323353499500576
         */

        private String taskId;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }
    }
}
