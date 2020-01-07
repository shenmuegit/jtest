package com.ehualu.calabashandroid.db.manager;


import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.db.DaoMaster;
import com.ehualu.calabashandroid.db.DaoSession;

import org.greenrobot.greendao.database.Database;

public class DBHelper {
    private static DaoMaster.DevOpenHelper helper;
    private static DaoSession daoSession;
    private static final String DB_NAME = "hulu.db";

    private DBHelper() {
        helper = new DaoMaster.DevOpenHelper(MyApp.getAppContext(), DB_NAME);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static void init() {
        if (helper == null || daoSession == null)
            new DBHelper();
    }

     static DaoMaster.DevOpenHelper getHelper() {
        if (helper == null) {
            init();
        }
        return helper;
    }

     static DaoSession getSession() {
        if (daoSession == null) {
            init();
        }
        return daoSession;
    }

}
