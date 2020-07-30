package com.wd.daquan.model.mgr;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import com.wd.daquan.model.bean.Draft;
import com.wd.daquan.model.db.helper.DraftDbHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 */
public class DraftMgr {

    private final List<Draft> chatMemories = new ArrayList<>();

    private static class DraftHolder {
        public static DraftMgr INSTANCE = new DraftMgr();
    }

    public static DraftMgr getInstance() {
        return DraftHolder.INSTANCE;
    }

    /**
     * 查询单条状态,获取草稿
     *
     */
    public Draft getDraft(@NonNull String sessionID) {
        Draft draft = getDraftCache(sessionID);
        if (draft != null) {
            return draft;
        }

        return DraftDbHelper.getInstance().getDraft(sessionID);
    }

    /**
     * 获取内存缓存中信息
     */
    public String getContent(String sessionID) {
        Draft draft = getDraftCache(sessionID);
        if (draft == null || TextUtils.isEmpty(draft.content)) {
            draft = getDraft(sessionID);
        }
        if (draft == null) {
            return "";
        }
        return draft.content;
    }

    /**
     * 是否存在草稿信息
     */
    public boolean isDraft(String sessionID) {
        Draft draft = getDraftCache(sessionID);
        if (draft == null || TextUtils.isEmpty(draft.content)) {
            draft =  getDraft(sessionID);
        }
        if (draft == null) {
            return false;
        }
        if (TextUtils.isEmpty(draft.content)) {
            return false;
        }
        return true;
    }

    private Draft getDraftCache(String sessionID) {
        synchronized (chatMemories) {
            for (Draft temp : chatMemories) {
                if (temp.sessionID.equals(sessionID)) {
                    return temp;
                }
            }
            return null;
        }
    }

    /**
     * 更新草稿
     *
     */
    public void updateDraft(Draft draft) {
        chatMemories.add(draft);
        DraftDbHelper.getInstance().update(draft, null);
    }

    /**
     * 删除草稿
     *
     */
    public void deleteDraft(String sessionID) {
        Iterator<Draft> it = chatMemories.iterator();
        while(it.hasNext()) {
            Draft temp = it.next();
            if(temp.sessionID.equals(sessionID)) {
                it.remove();
            }
        }
        DraftDbHelper.getInstance().delete(sessionID);
    }

    public void queryDraftAll() {
        List<Draft> all = DraftDbHelper.getInstance().getAll();
        chatMemories.addAll(all);
    }

//    private void pushGroupDraftComplete(ChatInterface.GroupDraftComplete draftComplete,
//                                        boolean succes, Draft draft) {
//        if (draftComplete != null) {
//            draftComplete.onReqGroupDraft(succes, draft);
//        }
//    }


}
