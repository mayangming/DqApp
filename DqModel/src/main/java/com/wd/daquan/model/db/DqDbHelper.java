package com.wd.daquan.model.db;

import android.content.Context;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.wd.daquan.model.db.note.DaoMaster;
import com.wd.daquan.model.db.note.FriendNoteDao;

import org.greenrobot.greendao.database.Database;

/**
 * 数据库中心
 */
public class DqDbHelper extends DaoMaster.OpenHelper {

    public DqDbHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        super.onUpgrade(db, oldVersion, newVersion);
//        if (oldVersion < newVersion){
//            MigrationHelper.migrate(db, FriendNoteDao.class);
//        }
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }
            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, FriendNoteDao.class);//可以添加多个Dao
    }
}
