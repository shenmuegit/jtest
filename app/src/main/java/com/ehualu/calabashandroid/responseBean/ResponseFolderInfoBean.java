package com.ehualu.calabashandroid.responseBean;

/**
 * author: houxiansheng
 * <p>
 * time：2019年12月24日15:05:02
 * <p>
 * describe：服务器返回的文件夹详情
 */
public class ResponseFolderInfoBean {

    /**
     * success : true
     * message : 成功
     * data : {"DIRCONTENT":{"dirId":"4","dirName":"文档1","userId":"001","parentDirId":"3","dirLevel":2,
     * "storageMedia":null,"deleteFlag":"0","displayFlag":"1","createTime":1576667269000,"updateTime":1577371011000,
     * "dlid":null},"PARENTDIRNAME":"/文档/文档1","DIRSIZE":1.3470754E7}
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

    @Override
    public String toString() {
        return "ResponseFolderInfoBean{" +
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
         * DIRCONTENT : {"dirId":"4","dirName":"文档1","userId":"001","parentDirId":"3","dirLevel":2,
         * "storageMedia":null,"deleteFlag":"0","displayFlag":"1","createTime":1576667269000,
         * "updateTime":1577371011000,"dlid":null}
         * PARENTDIRNAME : /文档/文档1
         * DIRSIZE : 1.3470754E7
         */

        private DIRCONTENTBean DIRCONTENT;
        private String PARENTDIRNAME;
        private double DIRSIZE;

        public DIRCONTENTBean getDIRCONTENT() {
            return DIRCONTENT;
        }

        public void setDIRCONTENT(DIRCONTENTBean DIRCONTENT) {
            this.DIRCONTENT = DIRCONTENT;
        }

        public String getPARENTDIRNAME() {
            return PARENTDIRNAME;
        }

        public void setPARENTDIRNAME(String PARENTDIRNAME) {
            this.PARENTDIRNAME = PARENTDIRNAME;
        }

        public double getDIRSIZE() {
            return DIRSIZE;
        }

        public void setDIRSIZE(double DIRSIZE) {
            this.DIRSIZE = DIRSIZE;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "DIRCONTENT=" + DIRCONTENT +
                    ", PARENTDIRNAME='" + PARENTDIRNAME + '\'' +
                    ", DIRSIZE=" + DIRSIZE +
                    '}';
        }

        public static class DIRCONTENTBean {
            /**
             * dirId : 4
             * dirName : 文档1
             * userId : 001
             * parentDirId : 3
             * dirLevel : 2
             * storageMedia : null
             * deleteFlag : 0
             * displayFlag : 1
             * createTime : 1576667269000
             * updateTime : 1577371011000
             * dlid : null
             */

            private String dirId;
            private String dirName;
            private String userId;
            private String parentDirId;
            private int dirLevel;
            private Object storageMedia;
            private String deleteFlag;
            private String displayFlag;
            private long createTime;
            private long updateTime;
            private Object dlid;

            public String getDirId() {
                return dirId;
            }

            public void setDirId(String dirId) {
                this.dirId = dirId;
            }

            public String getDirName() {
                return dirName;
            }

            public void setDirName(String dirName) {
                this.dirName = dirName;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getParentDirId() {
                return parentDirId;
            }

            public void setParentDirId(String parentDirId) {
                this.parentDirId = parentDirId;
            }

            public int getDirLevel() {
                return dirLevel;
            }

            public void setDirLevel(int dirLevel) {
                this.dirLevel = dirLevel;
            }

            public Object getStorageMedia() {
                return storageMedia;
            }

            public void setStorageMedia(Object storageMedia) {
                this.storageMedia = storageMedia;
            }

            public String getDeleteFlag() {
                return deleteFlag;
            }

            public void setDeleteFlag(String deleteFlag) {
                this.deleteFlag = deleteFlag;
            }

            public String getDisplayFlag() {
                return displayFlag;
            }

            public void setDisplayFlag(String displayFlag) {
                this.displayFlag = displayFlag;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public Object getDlid() {
                return dlid;
            }

            public void setDlid(Object dlid) {
                this.dlid = dlid;
            }

            @Override
            public String toString() {
                return "DIRCONTENTBean{" +
                        "dirId='" + dirId + '\'' +
                        ", dirName='" + dirName + '\'' +
                        ", userId='" + userId + '\'' +
                        ", parentDirId='" + parentDirId + '\'' +
                        ", dirLevel=" + dirLevel +
                        ", storageMedia=" + storageMedia +
                        ", deleteFlag='" + deleteFlag + '\'' +
                        ", displayFlag='" + displayFlag + '\'' +
                        ", createTime=" + createTime +
                        ", updateTime=" + updateTime +
                        ", dlid=" + dlid +
                        '}';
            }
        }
    }
}
