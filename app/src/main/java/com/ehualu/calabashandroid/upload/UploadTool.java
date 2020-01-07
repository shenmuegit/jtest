package com.ehualu.calabashandroid.upload;

import com.ehualu.calabashandroid.utils.Constants;
import com.ehualu.calabashandroid.utils.FileUtils;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class UploadTool {

    /**
     * 上传分片大小设置为10M
     */
    public static int UPLOAD_CHUNK_SIZE = 1024 * 1024 * 10;

    /**
     * 将原始文件按照分片大小切割成多个小文件
     *
     * @param originFile
     * @return
     */
    public static List<File> splitFile(File originFile) {
        List<File> files = new ArrayList<>();
        long fileSize = originFile.length();
        long count = fileSize % UPLOAD_CHUNK_SIZE == 0 ? fileSize / UPLOAD_CHUNK_SIZE : fileSize / UPLOAD_CHUNK_SIZE + 1;
        File folder = new File(Constants.UPLOAD_PATH + FileUtils.getFileMD5(originFile) + File.separator);
        if (folder.exists()) {
            //删除文件夹中的临时文件
            for (File ff : folder.listFiles()) {
                ff.delete();
            }
        } else {
            folder.mkdirs();
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(originFile, "rw");
            for (int i = 0; i < count; i++) {
                raf.seek(i * UPLOAD_CHUNK_SIZE);
                File part = new File(folder, originFile.getName() + ".part" + i);
                int len = 0;
                byte[] buff = new byte[UPLOAD_CHUNK_SIZE];
                len = raf.read(buff);
                if (len == -1) {
                    break;
                }
                RandomAccessFile rafPart = new RandomAccessFile(part, "rw");
                rafPart.write(buff, 0, len);
                rafPart.close();
                files.add(part);
            }
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }
}
