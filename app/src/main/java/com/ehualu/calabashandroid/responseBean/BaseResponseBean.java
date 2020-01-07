package com.ehualu.calabashandroid.responseBean;

import android.text.TextUtils;

/**
 * Created by GaoTing on 2019/12/25.
 * <p>
 * Explain: Response外层结构
 */
public class BaseResponseBean<T> {
    private boolean success;
    private String message;
    private int status;
    private String totalCount;
    private T data;

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

    public String getTotalCount() {
        return totalCount;
    }

    public int getTotalCountInt() {
        if (TextUtils.isEmpty(totalCount))
            return 0;
        return Integer.parseInt(totalCount);
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponseBean{\n" +
                "success=" + success +
                ",\n message='" + message + '\'' +
                ",\n status=" + status +
                ", \ntotalCount='" + totalCount + '\'' +
                ", \ndata=" + data +
                '}';
    }
}
