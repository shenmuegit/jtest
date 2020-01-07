package com.ehualu.calabashandroid.db.manager;

import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.db.DaoMaster;
import com.ehualu.calabashandroid.db.DaoSession;

public class DBManager {

    private DaoSession daoSession;
    private static DBManager dbManager;

    private DBManager() {
       daoSession=DBHelper.getSession();
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            synchronized (DBManager.class) {
                if (dbManager == null) {
                    dbManager = new DBManager();
                }
            }
        }
        return dbManager;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
