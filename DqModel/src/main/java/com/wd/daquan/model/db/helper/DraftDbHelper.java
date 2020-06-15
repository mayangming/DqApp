package com.wd.daquan.model.db.helper;

import android.util.Log;

import com.wd.daquan.model.bean.Draft;
import com.wd.daquan.model.db.DbSubscribe;
import com.wd.daquan.model.db.note.DraftNote;
import com.wd.daquan.model.db.note.DraftNoteDao;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.ArrayList;
import java.util.List;

public class DraftDbHelper extends DbThreadHelper{

    private DraftDbHelper(){}
    public static DraftDbHelper getInstance(){
        return DraftDbHolder.INSTANCE;
    }

    private static class DraftDbHolder {
        static DraftDbHelper INSTANCE = new DraftDbHelper();
    }

    private DraftNoteDao getDao(){
        return ModuleMgr.getDbMgr().getDraftDao();
    }

    public void clear(){
        getDao().detachAll();
    }

    /**
     * 插入单个数据
     */
    public void update(Draft draft, DbSubscribe<Draft> subscribe){
        toFlowable(emitter ->{
            DraftNote note = setDraftNote(draft);
            try {
                getDao().insertOrReplace(note);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("dq", "Draft update : " + e.toString());
            }
            emitter.onNext(draft);
            emitter.onComplete();
        }, subscribe);

    }

    /**
     * 获取单个数据
     */
    public Draft getDraft(String sessionID){
        DraftNote draftNote = getDraftNote(sessionID);

        if(draftNote != null) {
            return setDraft(draftNote);
        }
        return new Draft();
    }
    /**
     * 获取所有数据
     */
    public List<Draft> getAll(){
        List<Draft> list = new ArrayList<>();
        List<DraftNote> noteList = null;
        try {
            noteList = getDao().queryBuilder().build().list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(noteList != null && noteList.size() > 0) {
            for (DraftNote note : noteList) {
                if(note == null) {
                    continue;
                }
                list.add(setDraft(note));
            }
        }
        return list;
    }

    /**
     * 删除单个数据
     */
    public void delete(String sessionID){
        DraftNote draftNote = getDraftNote(sessionID);

        try {
            getDao().delete(draftNote);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DraftNote getDraftNote(String sessionID) {
        DraftNote draftNote = null;
        try {
            draftNote = getDao().queryBuilder().where(DraftNoteDao.Properties.SessionID
                    .eq(sessionID)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dq", "Draft update : " + e.toString());
        }
        return draftNote;
    }

    private DraftNote setDraftNote(Draft draft) {
        DraftNote note = new DraftNote();
        note.sessionID = draft.sessionID;
        note.sessionType = draft.sessionType;
        note.content = draft.content;
        note.time = draft.time;
        return note;
    }

    private Draft setDraft(DraftNote note) {
        Draft draft = new Draft();
        draft.sessionID = note.sessionID;
        draft.sessionType = note.sessionType;
        draft.content = note.content;
        draft.time = note.time;
        return draft;
    }
}
