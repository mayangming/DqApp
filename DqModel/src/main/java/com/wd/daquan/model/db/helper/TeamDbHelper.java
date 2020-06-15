package com.wd.daquan.model.db.helper;

import android.util.Log;

import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.db.DbSubscribe;
import com.wd.daquan.model.db.note.TeamNote;
import com.wd.daquan.model.db.note.TeamNoteDao;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class TeamDbHelper extends DbThreadHelper{
    private TeamDbHelper(){}
    public static TeamDbHelper getInstance(){
        return TeamDbHolder.INSTANCE;
    }

    private static class TeamDbHolder {
         static TeamDbHelper INSTANCE = new TeamDbHelper();
    }

    private TeamNoteDao getDao(){
        return ModuleMgr.getDbMgr().getTeamDao();
    }

    public void clear(){
        getDao().detachAll();
    }


    /**
     * 插入单个数据
     */
    public void update(GroupInfoBean bean, DbSubscribe<GroupInfoBean> subscribe){
        Log.e("YM","插入单条群组信息:"+bean.toString());
        toFlowable(emitter -> {
            if(bean == null) {
                emitter.onComplete();
                return;
            }
            TeamNote note = setTeamNote(bean);
            try {
               long count = getDao().insertOrReplace(note);
                Log.e("YM","改变的数量:"+count);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("dq", "team update : " + e.toString());
            }
            emitter.onNext(bean);
            emitter.onComplete();
        }, subscribe);

    }

    /**
     * 查询所有群数据
     */
    public List<GroupInfoBean> getAllTeam(){
        List<GroupInfoBean> teamList = new ArrayList<>();
        List<TeamNote> list = null;
        try {
            list = getDao().queryBuilder().build().list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(list != null && list.size() > 0) {
            for (TeamNote note : list) {
                if(note == null) {
                    continue;
                }
                teamList.add(setGroupInfoBean(note));
            }
        }
        return teamList;
    }

    /**
     * 插入单个数据
     */
    public GroupInfoBean getTeam(String teamId){

        TeamNote teamNote = getTeamNote(teamId);
        if(teamNote != null) {
            return setGroupInfoBean(teamNote);
        }
        return null;
    }
    public void getTeam(String teamId, DbSubscribe<GroupInfoBean> subscribe){

        toFlowable(emitter -> {
            GroupInfoBean infoBean;
            TeamNote teamNote = getTeamNote(teamId);
            if(teamNote != null) {
                infoBean =  setGroupInfoBean(teamNote);
            }else {
                infoBean = new GroupInfoBean(teamId);
            }
            emitter.onNext(infoBean);
            emitter.onComplete();
        }, subscribe);
    }

    public void delete(String sessionId) {
        try {
            getDao().delete(getTeamNote(sessionId));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dq", " team delete : " + e.toString());
        }
    }

    private TeamNote getTeamNote(String teamId) {
        TeamNote teamNote = null;
        try {
             teamNote = getDao().queryBuilder()
                    .where(TeamNoteDao.Properties.Group_id.eq(teamId)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dq", " team getTeamNote : " + e.toString());
        }
        return teamNote;
    }

    @NonNull
    private TeamNote setTeamNote(GroupInfoBean bean) {
        TeamNote teamNote = new TeamNote();
        teamNote.group_id = bean.group_id;
        teamNote.group_name = bean.group_name;
        teamNote.group_pic = bean.group_pic;
        teamNote.announcement = bean.announcement;
        teamNote.create_uid = bean.create_uid;
        teamNote.group_member_num = bean.group_member_num;
        teamNote.role = bean.role;
        teamNote.status = bean.status;
        teamNote.within_group = bean.within_group;
        teamNote.examine = bean.examine;
        teamNote.within_group = bean.within_group;
        teamNote.group_type = bean.group_type;
        teamNote.message_mute = bean.message_mute;
        teamNote.remarks = bean.remarks;
        teamNote.screenshot_notify = bean.screenshot_notify;
        teamNote.burn = bean.burn;
        teamNote.saved_team = bean.saved_team;
        teamNote.top = bean.top;
        teamNote.str_json = bean.strJson;
        return teamNote;
    }

    @NonNull
    private GroupInfoBean setGroupInfoBean(TeamNote teamNote) {
        GroupInfoBean bean = new GroupInfoBean();
        bean.group_id = teamNote.group_id;
        bean.group_name = teamNote.group_name;
        bean.group_pic = teamNote.group_pic;
        bean.announcement = teamNote.announcement;
        bean.create_uid = teamNote.create_uid;
        bean.group_member_num = teamNote.group_member_num;
        bean.role = teamNote.role;
        bean.status = teamNote.status;
        bean.within_group = teamNote.within_group;
        bean.examine = teamNote.examine;
        bean.within_group = teamNote.within_group;
        bean.group_type = teamNote.group_type;
        bean.message_mute = teamNote.message_mute;
        bean.remarks = teamNote.remarks;
        bean.screenshot_notify = teamNote.screenshot_notify;
        bean.burn = teamNote.burn;
        bean.saved_team = teamNote.saved_team;
        bean.top = teamNote.top;
        bean.strJson = teamNote.str_json;
        return bean;
    }

}
