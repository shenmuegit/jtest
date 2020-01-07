package com.ehualu.calabashandroid.testactivity;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by GaoTing on 2019/12/25.
 * <p>
 * Explain:LayrenActivity List Item
 */
public class Layren {
    public String name;
    public Observable observable;
    public boolean success;//是否测试成功

    Layren(String name, Observable observable, boolean success) {
        this.name = name;
        this.success = success;
        this.observable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
