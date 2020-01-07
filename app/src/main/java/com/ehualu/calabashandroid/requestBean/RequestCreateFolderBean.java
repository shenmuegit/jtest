package com.ehualu.calabashandroid.requestBean;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-27 16:47:49
 * <p>
 * describe：请求新建文件夹的Bean
 */
public class RequestCreateFolderBean {
    /**
     * dirName : 456ssssss
     * parentDirId : 3
     */

    private String dirName;
    private String parentDirId;

    public RequestCreateFolderBean(String dirName, String parentDirId) {
        this.dirName = dirName;
        this.parentDirId = parentDirId;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getParentDirId() {
        return parentDirId;
    }

    public void setParentDirId(String parentDirId) {
        this.parentDirId = parentDirId;
    }
}
