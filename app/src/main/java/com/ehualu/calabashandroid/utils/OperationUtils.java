package com.ehualu.calabashandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.model.RemoteFile;
import com.ehualu.calabashandroid.responseBean.PublicResponseBean;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OperationUtils {

    /**
     * 并发删除
     *
     * @param remoteFiles
     */
    public static void deleteRemoteFile(Context context, List<RemoteFile> remoteFiles) {

        AtomicInteger count = new AtomicInteger(0);
        for (RemoteFile rf : remoteFiles) {
            ApiRetrofit.getInstance().postDelete(rf.getID(), rf.getCategory())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.computation())
                    .subscribe(new Observer<PublicResponseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(PublicResponseBean publicResponseBean) {
                            if (publicResponseBean.isSuccess()) {
                                int value = count.incrementAndGet();
                                if (value == remoteFiles.size()) {
                                    //删除完成
                                    Intent intent = new Intent(Constants.DELETE_COMPLETE);
                                    intent.putExtra("dirID", rf.getParentId());
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
