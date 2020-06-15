package com.dq.im.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.text.TextUtils;

import com.dq.im.dao.HomeImBaseDao;
import com.dq.im.dao.P2PMessageBaseDao;
import com.dq.im.dao.TeamDao;
import com.dq.im.dao.TeamMessageBaseDao;
import com.dq.im.dao.TeamUserJoinDao;
import com.dq.im.dao.UserDao;
import com.dq.im.model.HomeImBaseMode;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.model.TeamModel;
import com.dq.im.model.TeamUserJoinModel;
import com.dq.im.model.UserModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * IM数据库
 * 表中可以传递多个类型
 */

@Database(entities = {
          UserModel.class,
          TeamModel.class,
          HomeImBaseMode.class,
          TeamUserJoinModel.class,
          TeamMessageBaseModel.class,
          P2PMessageBaseModel.class},
          version = 3, exportSchema = true)
public abstract class ImRoomDatabase extends RoomDatabase {
    public static volatile String USER_ID = "";

    public abstract UserDao userDao();

    public abstract TeamDao teamDao();

    public abstract P2PMessageBaseDao p2pMessageBaseDao();

    public abstract TeamMessageBaseDao teamMessageBaseDao();

    public abstract HomeImBaseDao homeImBaseDao();

    public abstract TeamUserJoinDao teamUserJoinDao();

    private static volatile ImRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ImRoomDatabase createDatabase(final Context context,String userId) {
        USER_ID = userId;
        return getDatabase(context);
    }

    public static String getUserId(){
        return USER_ID;
    }

    public static ImRoomDatabase getDatabase(final Context context) {
        if (TextUtils.isEmpty(USER_ID)){
            throw new IllegalStateException("使用数据库前必须先调用ApplicationViewModel定义数据库名字");
        }
        if (INSTANCE == null) {
            synchronized (ImRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ImRoomDatabase.class, "im_"+USER_ID)//数据库名
                            .addMigrations(MIGRATION_2_3)
//                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase(){
        if (null != INSTANCE){
            INSTANCE.close();
            INSTANCE = null;
            USER_ID = "";
        }
    }

    static Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // 为个人聊天记录旧表添加新的字段
            database.execSQL("ALTER TABLE person_message "
                    + "ADD COLUMN signal TEXT NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE person_message "
                    + "ADD COLUMN subSignal TEXT NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE person_message "
                    + "ADD COLUMN conversationType TEXT NOT NULL DEFAULT ''");

            // 为首页聊天记录旧表添加新的字段
            database.execSQL("ALTER TABLE team_message "
                    + " ADD COLUMN signal TEXT NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE team_message "
                    + " ADD COLUMN subSignal TEXT NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE team_message "
                    + " ADD COLUMN conversationType TEXT NOT NULL DEFAULT ''");

            // 为群聊聊天记录旧表添加新的字段
            database.execSQL("ALTER TABLE home_message "
                    + " ADD COLUMN signal TEXT NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE home_message "
                    + " ADD COLUMN subSignal TEXT NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE home_message "
                    + " ADD COLUMN conversationType TEXT NOT NULL DEFAULT ''");
        }
    };

}