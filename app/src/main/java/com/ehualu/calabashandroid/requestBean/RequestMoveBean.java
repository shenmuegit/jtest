package com.ehualu.calabashandroid.requestBean;

import java.util.List;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-28 11:09:58
 * <p>
 * describe：请求移动的Bean
 */
public class RequestMoveBean {

    /**
     * fileIds : ["1240151-011","1240151-012"]
     * dirId : 3
     * category : 1
     */

    private String dirId;
    private String category;
    private List<String> fileIds;

    public RequestMoveBean(String dirId, String category, List<String> fileIds) {
        this.dirId = dirId;
        this.category = category;
        this.fileIds = fileIds;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }
}
