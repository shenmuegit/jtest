package com.ehualu.calabashandroid.requestBean;

import java.util.List;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-29 09:56:07
 * <p>
 * describe：备份/提取Bean
 */
public class RequestBackUpAndExtractBean {
    /**
     * dirIds : ["1322944800227360","1322944800227360"]
     * fileIds : ["1322948564615200","1322948564615200"]
     * user : {"userId":"001"}
     */

    private UserBean user;
    private List<String> dirIds;
    private List<String> fileIds;

    public RequestBackUpAndExtractBean(UserBean user, List<String> dirIds, List<String> fileIds) {
        this.user = user;
        this.dirIds = dirIds;
        this.fileIds = fileIds;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<String> getDirIds() {
        return dirIds;
    }

    public void setDirIds(List<String> dirIds) {
        this.dirIds = dirIds;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }

    public static class UserBean {
        /**
         * userId : 001
         */

        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
