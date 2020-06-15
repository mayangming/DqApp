package com.dq.im.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dq.im.db.ImRoomDatabase;

/**
 * 这个类主要是用来做RoomDataBase的整体操作,比如创建、打开、关闭
 */
public class ApplicationViewModel extends AndroidViewModel {

    public ApplicationViewModel(@NonNull Application application) {
        super(application);
    }

    public void initRoomDataBase(String dataBaseName){
        ImRoomDatabase.createDatabase(getApplication(),dataBaseName);
    }

    /**
     * 关闭数据库
     */
    public void closeRoomDataBase(){
        Log.e("YM","退出登陆");
        ImRoomDatabase.getDatabase(getApplication()).closeDataBase();
    }

}