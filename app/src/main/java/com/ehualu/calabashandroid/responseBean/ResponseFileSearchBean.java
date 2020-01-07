package com.ehualu.calabashandroid.responseBean;


import java.util.List;

public class ResponseFileSearchBean {

    /**
     * totalcount : 12
     * data : [{"duration":"31","fileName":"SVID_20191103_173531_1.mp4","thumbnail":"http://49.7.59
     * .236:13800/calabash/file/thumbnail/1324060216656096","fileSize":1608759,"updateTime":1578045922873,
     * "location":"","ID":"1324060216656096","category":"1","parentId":"","labels":""},{"duration":"20",
     * "fileName":"SVID_20190624_172753_1.mp4","thumbnail":"http://49.7.59
     * .236:13800/calabash/file/thumbnail/1324060417982496","fileSize":1998908,"updateTime":1578046010578,
     * "location":"","ID":"1324060417982496","category":"1","parentId":"","labels":""},{"duration":"215",
     * "fileName":"gaobai.mp4","thumbnail":"http://49.7.59.236:13800/calabash/file/thumbnail/1324062424956960",
     * "fileSize":30565774,"updateTime":1578046981254,"location":"","ID":"1324062424956960","category":"1",
     * "parentId":"","labels":""},{"duration":"14","fileName":"bea4b368dc2f3f8c210cbb278014ab19.mp4",
     * "thumbnail":"http://49.7.59.236:13800/calabash/file/thumbnail/1324060417982528","fileSize":1655061,
     * "updateTime":1578046010459,"location":"","ID":"1324060417982528","category":"1","parentId":"","labels":""},{
     * "duration":"10","fileName":"SVID_20190626_153757_1.mp4","thumbnail":"http://49.7.59
     * .236:13800/calabash/file/thumbnail/1324060417982592","fileSize":1356546,"updateTime":1578046009559,
     * "location":"","ID":"1324060417982592","category":"1","parentId":"","labels":""},{"duration":"42",
     * "fileName":"SVID_20191025_220457_1.mp4","thumbnail":"http://49.7.59
     * .236:13800/calabash/file/thumbnail/1324060216655936","fileSize":5254339,"updateTime":1578045922889,
     * "location":"","ID":"1324060216655936","category":"1","parentId":"","labels":""},{"duration":"209",
     * "fileName":"SVID_20191103_174205_1.mp4","thumbnail":"http://49.7.59
     * .236:13800/calabash/file/thumbnail/1324060216656032","fileSize":38895132,"updateTime":1578045951868,
     * "location":"","ID":"1324060216656032","category":"1","parentId":"","labels":""},{"duration":"18",
     * "fileName":"SVID_20191025_212420_1.mp4","thumbnail":"http://49.7.59
     * .236:13800/calabash/file/thumbnail/1324060216656000","fileSize":1281426,"updateTime":1578045922875,
     * "location":"","ID":"1324060216656000","category":"1","parentId":"","labels":""},{"duration":"13",
     * "fileName":"SVID_20191115_180553_1.mp4","thumbnail":"http://49.7.59
     * .236:13800/calabash/file/thumbnail/1324060216655968","fileSize":3171802,"updateTime":1578045922873,
     * "location":"","ID":"1324060216655968","category":"1","parentId":"","labels":""},{"duration":"15",
     * "fileName":"SVID_20190625_193117_1.mp4","thumbnail":"http://49.7.59
     * .236:13800/calabash/file/thumbnail/1324060417982560","fileSize":4673237,"updateTime":1578046012208,
     * "location":"","ID":"1324060417982560","category":"1","parentId":"","labels":""},{"duration":"8",
     * "fileName":"SVID_20191220_193522_1.mp4","thumbnail":"http://49.7.59
     * .236:13800/calabash/file/thumbnail/1324060216656064","fileSize":895968,"updateTime":1578045922873,
     * "location":"","ID":"1324060216656064","category":"1","parentId":"","labels":""},{"duration":"108",
     * "fileName":"【www.zxit8.com】 7-10 本章小结.mp4","thumbnail":"http://49.7.59
     * .236:13800/calabash/file/thumbnail/1324062424956992","fileSize":2605571,"updateTime":1578046970012,
     * "location":"","ID":"1324062424956992","category":"1","parentId":"","labels":""}]
     * success : true
     * message : 成功
     * status : 200
     */

    private int totalcount;
    private boolean success;
    private String message;
    private int status;
    private List<DataBean> data;

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * duration : 31
         * fileName : SVID_20191103_173531_1.mp4
         * thumbnail : http://49.7.59.236:13800/calabash/file/thumbnail/1324060216656096
         * fileSize : 1608759
         * updateTime : 1578045922873
         * location :
         * ID : 1324060216656096
         * category : 1
         * parentId :
         * labels :
         */

        private String duration;
        private String fileName;
        private String thumbnail;
        private int fileSize;
        private long updateTime;
        private String location;
        private String ID;
        private String category;
        private String parentId;
        private String labels;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public int getFileSize() {
            return fileSize;
        }

        public void setFileSize(int fileSize) {
            this.fileSize = fileSize;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getLabels() {
            return labels;
        }

        public void setLabels(String labels) {
            this.labels = labels;
        }
    }
}
