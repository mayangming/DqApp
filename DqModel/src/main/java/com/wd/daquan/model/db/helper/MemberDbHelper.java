package com.wd.daquan.model.db.helper;

import android.text.TextUtils;
import android.util.Log;

import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.db.DbSubscribe;
import com.wd.daquan.model.db.note.MemberNote;
import com.wd.daquan.model.db.note.MemberNoteDao;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 群组数据库查询
 */
public class MemberDbHelper extends DbThreadHelper{
    private MemberDbHelper(){}
    public static MemberDbHelper getInstance(){
        return TeamMemberDbHolder.INSTANCE;
    }

    private static class TeamMemberDbHolder {
         static MemberDbHelper INSTANCE = new MemberDbHelper();
    }

    private MemberNoteDao getDao(){
        return ModuleMgr.getDbMgr().getMemberDao();
    }

    public void clear(){
        getDao().detachAll();
    }

    /**
     * 插入单个数据
     */
    public void update(String groupId, GroupMemberBean bean){
        if(TextUtils.isEmpty(groupId) || bean == null) {
            return;
        }
        MemberNote teamMemberNote = setMemberNote(groupId, bean);
        try {
            getDao().insertOrReplace(teamMemberNote);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dq", "team member update : " + e.toString());
        }
    }

    /**
     * 插入多条数据
     */
    public void update(String groupId, List<GroupMemberBean> list,  DbSubscribe<List<GroupMemberBean>> subscribe){

        toFlowable(emitter -> {
            try {
                MemberNoteDao dao = getDao();
                for (GroupMemberBean bean : list) {
                    if(bean == null) {
                        continue;
                    }
                    dao.insertOrReplace(setMemberNote(groupId, bean));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("dq", "team member update: " + e.toString());
            }
            emitter.onNext(list);
            emitter.onComplete();
        }, subscribe);


    }

    /**
     * 根据群成员key删除
     */
    public void delete(List<String> idList) {
        if(idList != null && idList.size() > 0) {
            try {
                MemberNoteDao dao = getDao();
                for (String key : idList) {
                    dao.delete(getMemberNote(key));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("dq", "team member delete : " + e.toString());
            }
        }
    }

    /**
     * 根据groupId删除当前群组下所有群成员
     */
    public void delete(String teamId) {
        try {
            MemberNoteDao dao = getDao();
            List<MemberNote> list = dao.queryBuilder().where(MemberNoteDao.
                    Properties.Group_id.eq(teamId)).build().list();
            dao.deleteInTx(list);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dq", "team member delete : " + e.toString());
        }
    }

    private MemberNote getMemberNote(String key) {
        MemberNote memberNote = null;
        try {
            memberNote = getDao().queryBuilder()
                    .where(MemberNoteDao.Properties.Key.eq(key)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memberNote;
    }

    /**
     * 获取所有某个群里的所有群成员数据
     */
    public List<GroupMemberBean> getAll(String groupId){
        List<GroupMemberBean> memberList = new ArrayList<>();
        List<MemberNote> list = null;
        try {
            list = getDao().queryBuilder().where(MemberNoteDao.Properties.Group_id.eq(groupId)).build().list();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dq", "team member getAll : " + e.toString());
        }

        if(list != null && list.size() > 0) {
            for (MemberNote note : list) {
                if(note == null) {
                    continue;
                }
                memberList.add(setGroupMemberBean(note));
            }
        }
        return memberList;
    }

    /**
     * 获取群成员数据
     */
    public GroupMemberBean getTeamMember(String groupId, String uid){
        MemberNote memberNote = null;
        try {
            memberNote = getDao().queryBuilder().where(MemberNoteDao.Properties.Group_id.eq(groupId),
                    MemberNoteDao.Properties.Uid.eq(uid)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(memberNote != null) {
            return setGroupMemberBean(memberNote);
        }
        return new GroupMemberBean();
    }

    @NonNull
    private MemberNote setMemberNote(String groupId, Friend bean) {
        MemberNote note = new MemberNote();
        note.key = groupId + bean.uid;
        note.group_id = groupId;
        note.uid = bean.uid;
        note.phone = bean.phone;
        note.nickname = bean.nickname;
        note.headpic = bean.headpic;
        note.role = bean.role;
        note.sex = bean.sex;
        note.dq_num = bean.dq_num;
        note.remarks = bean.remarks;
        note.friend_remarks = bean.friend_remarks;
        note.whether_friend = bean.whether_friend;
        note.whether_black = bean.whether_black;
        note.source = bean.source;
        note.is_join = bean.is_join;
        return note;
    }

    @NonNull
    private MemberNote setMemberNote(String groupId, GroupMemberBean bean) {
        MemberNote note = new MemberNote();
        note.key = groupId + bean.uid;
        note.group_id = groupId;
        note.uid = bean.uid;
        note.phone = bean.phone;
        note.nickname = bean.nickname;
        note.headpic = bean.headpic;
        note.role = bean.role;
        note.sex = bean.sex;
        note.dq_num = bean.dq_num;
        note.remarks = bean.remarks;
        note.friend_remarks = bean.friend_remarks;
        note.whether_friend = bean.whether_friend;
        note.whether_black = bean.whether_black;
        note.source = bean.source;
        note.is_join = bean.is_join;
        return note;
    }

    @NonNull
    private GroupMemberBean setGroupMemberBean(MemberNote note) {
        GroupMemberBean bean = new GroupMemberBean();
        bean.groupID = note.group_id;
        bean.uid = note.uid;
        bean.phone = note.phone;
        bean.nickname = note.nickname;
        bean.headpic = note.headpic;
        bean.role = note.role;
        bean.sex = note.sex;
        bean.dq_num = note.dq_num;
        bean.remarks = note.remarks;
        bean.friend_remarks = note.friend_remarks;
        bean.whether_friend = note.whether_friend;
        bean.whether_black = note.whether_black;
        bean.source = note.source;
        bean.is_join = note.is_join;
        return bean;
    }
}
