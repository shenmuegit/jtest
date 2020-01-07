package com.ehualu.calabashandroid.requestBean;

/**
 * Created by GaoTing on 2019/12/26.
 * <p>
 * Explain:文件删除参数
 */
public class RequestDeleteFileBean {
   private final String fileId;//文件ID
   private final String category;//类型  1文件,2文件夹

    public RequestDeleteFileBean(String fileId, String category) {
        this.fileId = fileId;
        this.category = category;
    }
}
