package com.ehualu.calabashandroid.download;

import com.ehualu.calabashandroid.model.RemoteFile;

import java.util.ArrayList;
import java.util.List;

public class DownloadTool {

    public static long DOWNLOAD_CHUNK_SIZE = 1024 * 1024 * 25;//暂时定为10M一片

    /**
     * 根据文件对象，获此下载此文件的分片列表
     *
     * @param file
     * @return
     */
    public static List<DownloadChunk> getDownloadChunks(RemoteFile file) {
        List<DownloadChunk> chunks = new ArrayList<>();
        long fileSize = file.getFileSize();
        //10389;
        long count = fileSize % DOWNLOAD_CHUNK_SIZE == 0 ? fileSize / DOWNLOAD_CHUNK_SIZE : fileSize / DOWNLOAD_CHUNK_SIZE + 1;
        long end = -1;
        for (int i = 0; i < count; i++) {
            DownloadChunk chunk;
            if (i == count - 1) {
                //最后一片
                chunk = new DownloadChunk(end + 1, file.getFileSize(), file.getID());
                end = chunk.getEndIndex();
            } else {
                //之前片数
                chunk = new DownloadChunk(end + 1, DOWNLOAD_CHUNK_SIZE * (i + 1), file.getID());
                end = chunk.getEndIndex();
            }
            chunks.add(chunk);
        }
        return chunks;
    }
}
