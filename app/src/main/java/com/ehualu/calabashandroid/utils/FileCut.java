package com.ehualu.calabashandroid.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件按照指定大小分割成很多小文件
 */
public class FileCut {

    public static final int CHUNK_SIZE = 1024 * 1024 * 10;//每片大小10M

    /**
     * @param file 需要切割的文件
     * @return
     */
    public static List<File> cut(File file) {
        List<File> list = new ArrayList<>();
        File outFolder = new File(Environment.getDataDirectory(), "UploadTmp");
        if (!outFolder.exists()) {
            outFolder.mkdirs();
        }

        long fileLength = file.length();
        long value = fileLength % CHUNK_SIZE == 0 ? fileLength / CHUNK_SIZE : fileLength / CHUNK_SIZE + 1;

        File outFile;
        for (int i = 0; i < value; i++) {
            try {
                outFile = File.createTempFile("tmp", i + "");
                boolean b=outFile.exists();
                byte[] box = new byte[CHUNK_SIZE];
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.seek(i * CHUNK_SIZE);
                int read = raf.read(box);
                FileOutputStream out = new FileOutputStream(outFile);
                out.write(box, 0, read);
                out.close();
                raf.close();
                list.add(outFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
