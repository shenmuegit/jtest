package com.ehualu.calabashandroid.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.ehualu.calabashandroid.activity.AlbumActivity;
import com.ehualu.calabashandroid.activity.AlbumListActivity;
import com.ehualu.calabashandroid.model.Album;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取本地相册
 */
public class LocalImageHelper {

    private List<Album> albums = new ArrayList<>();//用来存储本地相册对象
    private Map<Integer, ArrayList<String>> photoSort = new HashMap<>();//key是buck_id,value是该相册中所有的图片地址
    private Map<Integer, String> albumDisplayNames = new HashMap<>();

    //大图遍历字段
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.ORIENTATION
    };
    //小图遍历字段
    private static final String[] THUMBNAIL_STORE_IMAGE = {
            MediaStore.Images.Thumbnails._ID,
            MediaStore.Images.Thumbnails.DATA
    };

    public synchronized void getImagesByAlbum(Context context, int buck_id, AlbumActivity.AlbumCallBack callback) {
        List<String> paths = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = context.getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        STORE_IMAGES,
                        MediaStore.Images.Media.BUCKET_ID + " = ?", new String[]{buck_id + ""}, MediaStore.Images.Media.DATE_TAKEN + " DESC"
                );
                if (cursor == null) {
                    return;
                }
                while (cursor.moveToNext()) {
                    String path = cursor.getString(3);
                    File file = new File(path);
                    if (file.exists()) {
                        paths.add(path);
                    }
                }
                cursor.close();
                callback.onReadFinished(paths);
            }
        }).start();
    }

    public synchronized void initImage(Context context, AlbumListActivity.DataCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = context.getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        STORE_IMAGES,
                        null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC"
                );
                if (cursor == null) {
                    return;
                }
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    int buck_id = cursor.getInt(1);
                    String buck_display_name = cursor.getString(2);
                    String path = cursor.getString(3);
                    File file = new File(path);
                    if (file.exists()) {
                        if (photoSort.containsKey(buck_id)) {
                            List<String> list = photoSort.get(buck_id);
                            list.add(path);
                        } else {
                            ArrayList<String> list = new ArrayList<>();
                            list.add(path);
                            photoSort.put(buck_id, list);
                            albumDisplayNames.put(buck_id, buck_display_name);
                        }
                    }
                }

                for (Map.Entry<Integer, ArrayList<String>> entry : photoSort.entrySet()) {
                    Album album = new Album();
                    album.setId(entry.getKey());
                    album.setCount(entry.getValue().size());
                    album.setName(albumDisplayNames.get(entry.getKey()));
                    if (entry.getValue().size() > 3) {
                        album.setFiles(entry.getValue().subList(0, 3));
                    } else {
                        album.setFiles(entry.getValue());
                    }
                    if (album.getFiles().size() > 0) {
                        albums.add(album);
                    }
                }
                cursor.close();
                callBack.onReadFinished(albums);
            }
        }).start();
    }
}
