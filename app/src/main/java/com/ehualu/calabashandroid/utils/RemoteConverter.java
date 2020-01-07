package com.ehualu.calabashandroid.utils;

import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.responseBean.ResponseFileSearchBean;

import java.util.ArrayList;
import java.util.List;

public class RemoteConverter {

    public static List<RemoteFile> getRemoteFiles(ResponseFileSearchBean response) {
        List<RemoteFile> files = new ArrayList<>();
        if (response.isSuccess()) {
            for (ResponseFileSearchBean.DataBean bean : response.getData()) {
                RemoteFile remoteFile = new RemoteFile();
                remoteFile.setFileName(bean.getFileName());
                remoteFile.setThumbnail(bean.getThumbnail());
                remoteFile.setFileSize(bean.getFileSize());
                remoteFile.setUpdateTime(bean.getUpdateTime());
                remoteFile.setLocation(bean.getLocation());
                remoteFile.setID(bean.getID());
                remoteFile.setCategory(bean.getCategory());
                remoteFile.setParentId(bean.getParentId());
                remoteFile.setLabels(bean.getLabels());
                remoteFile.setDuration(bean.getDuration());
                files.add(remoteFile);
            }
        }
        return files;
    }
}
