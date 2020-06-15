package com.wd.daquan.model.mgr;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.wd.daquan.model.ModelConfig;
import com.wd.daquan.model.db.DqDbHelper;
import com.wd.daquan.model.db.note.DaoMaster;
import com.wd.daquan.model.db.note.DaoSession;
import com.wd.daquan.model.db.note.DraftNoteDao;
import com.wd.daquan.model.db.note.FriendNoteDao;
import com.wd.daquan.model.db.note.MemberNoteDao;
import com.wd.daquan.model.db.note.TeamNoteDao;
import com.wd.daquan.model.rxbus.ModuleBase;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;

/**
 * 聊天
 * Created by Kind on 2018/9/12.
 */

public class DbMgr implements ModuleBase, QCObserver {

    private DqDbHelper mDbHelper;
    private DaoSession mDaoSession;

    @Override
    public void init() {
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void release() {
        MsgMgr.getInstance().detach(this);
    }

    @Override
    public void onMessage(String key, Object value) {
        if (key.equals(MsgType.MT_App_Login)) {
            boolean result = (Boolean) value;
            Log.e("YM","关闭数据库:"+result);
            if (result) {//登录成功
                login(ModuleMgr.getCenterMgr().getUID());
            } else {
                logout();
            }
        }
    }


    /**
     * DB初始化
     */
    public void login(String uid) {
        if (TextUtils.isEmpty(uid)) {
            return;
        }
        //斗圈数据库
        initDb(uid);
    }

    private void initDb(String uid) {
        mDbHelper = new DqDbHelper(ModelConfig.getContext(), uid + ".db");
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDaoSession = new DaoMaster(db).newSession();
    }

    private void logout() {

        if(mDbHelper != null) {
            mDbHelper.close();
        }
    }

    private DaoSession getDaoSession() {
        if(mDaoSession == null) {
            if(mDbHelper == null) {
                String uid = ModuleMgr.getCenterMgr().getUID();
                initDb(uid);
            }else {
                mDaoSession = new DaoMaster(mDbHelper.getWritableDatabase()).newSession();
            }
        }
        return mDaoSession;
    }

    public FriendNoteDao getFriendDao(){
        return getDaoSession().getFriendNoteDao();
    }
    public TeamNoteDao getTeamDao(){
        return getDaoSession().getTeamNoteDao();
    }
    public MemberNoteDao getMemberDao(){
        return getDaoSession().getMemberNoteDao();
    }
    public DraftNoteDao getDraftDao(){
        return getDaoSession().getDraftNoteDao();
    }

    public void clear(){
        getDaoSession().clear();
    }
}