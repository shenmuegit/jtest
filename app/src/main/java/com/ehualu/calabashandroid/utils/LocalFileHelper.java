package com.ehualu.calabashandroid.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.health.PackageHealthStats;
import android.provider.MediaStore;
import android.util.Log;

import com.ehualu.calabashandroid.fragment.DocumentFragment;
import com.ehualu.calabashandroid.model.Document;
import com.ehualu.calabashandroid.model.DocumentList;
import com.ehualu.calabashandroid.model.Video;
import com.ehualu.calabashandroid.model.VideoList;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleConsumer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LocalFileHelper {

    private static final String[] fileColumns = {
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
    };

    public synchronized void initFile(Context context, DocumentFragment.DataCallBack callBack) {
        List<String> fileMD5List = new ArrayList<>();
        Observable.create(new ObservableOnSubscribe<List<Document>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Document>> emitter) throws Exception {
                Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"),
                        fileColumns,
                        null,
                        null,
                        null
                );
                List<Document> documents = new ArrayList<>();
                List<String> strs = new ArrayList<>();
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                        long dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED));
                        if (isDocument(data + "")) {

                            File file = new File(data);

                            if (file.exists() && !file.isHidden() && !fileMD5List.contains(getFileMD5(file)) && file.isFile() && !data.contains("/cache/") && !data.contains("/.")
                                    ) {
                                Document document = new Document();
                                document.setDate(dateModified);
                                document.setName(title);
                                document.setPath(data);
                                Log.e("读写成功", data);
                                documents.add(document);
                                strs.add(title);
                                fileMD5List.add(getFileMD5(file));
                            } else {
                                //Log.e("读写失败", data);
                            }
                        }
                    } while (cursor.moveToNext());
                    cursor.close();
                }

                for (Document d : documents) {
                    if (d.getName().startsWith("1")) {
                        Log.e("_开头", d.getName() + "--->" + d.getPath());
                    }
                }

                emitter.onNext(documents);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Document>>() {
                    @Override
                    public void accept(List<Document> documents) throws Exception {
                        Map<String, List<Document>> map = new HashMap<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        for (Document document : documents) {
                            String time = dateFormat.format(new Date(document.getDate() * 1000));
                            if (map.containsKey(time)) {
                                map.get(time).add(document);
                            } else {
                                List<Document> vs = new ArrayList<>();
                                vs.add(document);
                                map.put(time, vs);
                            }
                        }
                        List<DocumentList> list = new ArrayList<>();
                        for (Map.Entry<String, List<Document>> entry : map.entrySet()) {
                            DocumentList documentList = new DocumentList();
                            documentList.setDate(entry.getKey());
                            documentList.setTime(dateFormat.parse(entry.getKey()).getTime());
                            documentList.setDocuments(entry.getValue());
                            list.add(documentList);
                        }
                        Collections.sort(list);
                        callBack.onReadFinished(list);
                    }
                });
    }

    private static boolean isDocument(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".doc") ||
                path.endsWith(".docx") ||
                path.endsWith(".xlsx") ||
                path.endsWith(".xls") ||
                path.endsWith(".pptx") ||
                path.endsWith(".ppt") ||
                path.endsWith(".pdf") ||
                path.endsWith(".txt") ||
                path.endsWith(".html") ||
                path.endsWith(".htm")) {
            return true;
        }
        return false;
    }

    private String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
