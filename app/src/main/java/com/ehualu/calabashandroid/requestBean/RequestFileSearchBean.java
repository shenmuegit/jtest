package com.ehualu.calabashandroid.requestBean;

public class RequestFileSearchBean {

    /**
     * dirID :
     * fileType :
     * keywords :
     * collectStatus :
     * backupStatus :
     * classification :
     * order :
     * orderRule :
     * index :
     * num :
     * autoSort :
     */

    private String dirID;
    private String fileType;
    private String keywords;
    private String collectStatus;
    private String backupStatus;
    private String classification;
    private String order;
    private String orderRule;
    private String index;
    private String num;
    private String autoSort;

    public RequestFileSearchBean(String dirID, String fileType, String keywords, String collectStatus,
                                 String backupStatus, String classification, String order, String orderRule,
                                 String index, String num, String autoSort) {
        this.dirID = dirID;
        this.fileType = fileType;
        this.keywords = keywords;
        this.collectStatus = collectStatus;
        this.backupStatus = backupStatus;
        this.classification = classification;
        this.order = order;
        this.orderRule = orderRule;
        this.index = index;
        this.num = num;
        this.autoSort = autoSort;
    }

    public String getDirID() {
        return dirID;
    }

    public void setDirID(String dirID) {
        this.dirID = dirID;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(String collectStatus) {
        this.collectStatus = collectStatus;
    }

    public String getBackupStatus() {
        return backupStatus;
    }

    public void setBackupStatus(String backupStatus) {
        this.backupStatus = backupStatus;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderRule() {
        return orderRule;
    }

    public void setOrderRule(String orderRule) {
        this.orderRule = orderRule;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAutoSort() {
        return autoSort;
    }

    public void setAutoSort(String autoSort) {
        this.autoSort = autoSort;
    }
}
