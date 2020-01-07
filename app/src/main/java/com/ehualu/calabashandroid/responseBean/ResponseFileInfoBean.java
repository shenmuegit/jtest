package com.ehualu.calabashandroid.responseBean;

/**
 * author: houxiansheng
 * <p>
 * time：2019年12月24日11:33:03
 * <p>
 * describe：服务器返回的文件详情
 */
public class ResponseFileInfoBean {

    /**
     * success : true
     * message : 成功
     * data : {"fileId":"1240151-06","fileName":"gytg.mp4","fileSize":"565775","dir":"/","resolutionRatio":"",
     * "location":",","picturePixel":null,"fileType":".mp4","thumbnail":null,"createTime":"1577173463845",
     * "updateTime":"1577173463845","collectStatus":"0"}
     * status : 200
     * totalCount : 1
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

    @Override
    public String toString() {
        return "ResponseFileInfoBean{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", status=" + status +
                ", totalCount='" + totalCount + '\'' +
                ", fileSize='" + fileSize + '\'' +
                '}';
    }

    public static class DataBean {
        /**
         * fileId : 1240151-06
         * fileName : gytg.mp4
         * fileSize : 565775
         * dir : /
         * resolutionRatio :
         * location : ,
         * picturePixel : null
         * fileType : .mp4
         * thumbnail : null
         * createTime : 1577173463845
         * updateTime : 1577173463845
         * collectStatus : 0
         */

        private String fileId;
        private String fileName;
        private String fileSize;
        private String dir;
        private String resolutionRatio;
        private String location;
        private Object picturePixel;
        private String fileType;
        private String thumbnail;
        private String createTime;
        private String updateTime;
        private String collectStatus;

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public String getResolutionRatio() {
            return resolutionRatio;
        }

        public void setResolutionRatio(String resolutionRatio) {
            this.resolutionRatio = resolutionRatio;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Object getPicturePixel() {
            return picturePixel;
        }

        public void setPicturePixel(Object picturePixel) {
            this.picturePixel = picturePixel;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getCollectStatus() {
            return collectStatus;
        }

        public void setCollectStatus(String collectStatus) {
            this.collectStatus = collectStatus;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "fileId='" + fileId + '\'' +
                    ", fileName='" + fileName + '\'' +
                    ", fileSize='" + fileSize + '\'' +
                    ", dir='" + dir + '\'' +
                    ", resolutionRatio='" + resolutionRatio + '\'' +
                    ", location='" + location + '\'' +
                    ", picturePixel=" + picturePixel +
                    ", fileType='" + fileType + '\'' +
                    ", thumbnail=" + thumbnail +
                    ", createTime='" + createTime + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", collectStatus='" + collectStatus + '\'' +
                    '}';
        }
    }
}
