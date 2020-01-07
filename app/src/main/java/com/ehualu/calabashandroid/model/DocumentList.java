package com.ehualu.calabashandroid.model;

import java.util.List;

public class DocumentList implements Comparable<DocumentList> {

    private String date;
    private long time;
    private List<Document> documents;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public int compareTo(DocumentList o) {
        return this.getTime() > o.getTime() ? -1 : 1;
    }
}
