package com.ehualu.calabashandroid.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.fragment.UnUploadVideoFragment;
import com.ehualu.calabashandroid.model.Video;
import com.ehualu.calabashandroid.model.VideoList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LocalVideoHelper {

    private static final String[] localVideoColumns = {
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATE_MODIFIED
    };

    public synchronized void initVideo(Context context, BaseFragment.DataCallBack callBack) {
        Observable.create(new ObservableOnSubscribe<List<Video>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Video>> emitter) throws Exception {
                Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        localVideoColumns,
                        null,
                        null,
                        null
                );
                List<Video> videos = new ArrayList<>();
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Video video = new Video();
                        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        long dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
                        video.setPath(data);
                        video.setDuration(duration);
                        video.setDate(dateModified);
                        videos.add(video);
                    } while (cursor.moveToNext());
                    cursor.close();
                }
                emitter.onNext(videos);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Video>>() {
                    @Override
                    public void accept(List<Video> videos) throws Exception {
                        Map<String, List<Video>> map = new HashMap<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        for (Video video : videos) {
                            String time = dateFormat.format(new Date(video.getDate() * 1000));
                            if (map.containsKey(time)) {
                                map.get(time).add(video);
                            } else {
                                List<Video> vs = new ArrayList<>();
                                vs.add(video);
                                map.put(time, vs);
                            }
                        }
                        List<VideoList> list = new ArrayList<>();
                        for (Map.Entry<String, List<Video>> entry : map.entrySet()) {
                            VideoList videoList = new VideoList();
                            videoList.setDate(entry.getKey());
                            videoList.setTime(dateFormat.parse(entry.getKey()).getTime());
                            videoList.setVideos(entry.getValue());
                            list.add(videoList);
                        }
                        Collections.sort(list);
                        callBack.onReadFinished(list);
                    }
                });
    }
}
