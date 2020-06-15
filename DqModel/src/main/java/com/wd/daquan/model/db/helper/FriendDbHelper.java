package com.wd.daquan.model.db.helper;

import android.util.Log;

import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.DbSubscribe;
import com.wd.daquan.model.db.note.FriendNote;
import com.wd.daquan.model.db.note.FriendNoteDao;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 好友数据库, 包含自己的好友，自己，小助手和黑名单
 */
public class FriendDbHelper extends DbThreadHelper{
    private FriendDbHelper(){}
    public static FriendDbHelper getInstance(){
        return FriendDbHolder.INSTANCE;
    }

    private static class FriendDbHolder {
        static FriendDbHelper INSTANCE = new FriendDbHelper();
    }

    private FriendNoteDao getDao(){
        return ModuleMgr.getDbMgr().getFriendDao();
    }

    public void clear(){
        getDao().detachAll();
    }

    /**
     * 插入单个数据
     */
    public void update(Friend friend, DbSubscribe<Friend> subscribe){

        toFlowable(emitter -> {
            if(friend == null) {
                emitter.onComplete();
                return;
            }
            FriendNote friendNote = setFriendNote(friend);
            try {
                getDao().insertOrReplace(friendNote);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("dq", "FRIEND update : " + e.toString());
            }
            emitter.onNext(friend);
            emitter.onComplete();
        }, subscribe);
    }

    /**
     * 插入集合数据
     */
    public void update(List<Friend> list,  DbSubscribe<List<Friend>> subscribe){

        toFlowable(emitter -> {
            if(list == null || list.size() <= 0) {
                emitter.onComplete();
                return;
            }
            try {
                FriendNoteDao friendNoteDao = getDao();
                for (Friend friend : list) {
                    if(friend == null) {
                        continue;
                    }
                    friendNoteDao.insertOrReplace(setFriendNote(friend));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("dq", "FRIEND update : " + e.toString());
            }
            emitter.onNext(list);
            emitter.onComplete();
        }, subscribe);
    }

    /**
     * 获取所有好友,包括自己和小助手
     */
    public void getAll(DbSubscribe<List<Friend>> subscribe){

        toFlowable(emitter -> {
            List<Friend> friendList = new ArrayList<>();
            List<FriendNote> list = null;
            try {
                list = getDao().queryBuilder().build().list();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onNext(friendList);
                emitter.onComplete();
            }
            if(list != null && list.size() > 0) {
                for (FriendNote friendNote : list) {
                    if(friendNote == null) {
                        continue;
                    }
                    if("0".equals(friendNote.whether_black)) {
                        continue;
                    }
                    friendList.add(setFriend(friendNote));
                }
            }
            emitter.onNext(friendList);
            emitter.onComplete();
        }, subscribe);

    }

    /**
     * 获取所有黑名单
     */
    public List<Friend> getBlackList(){
        List<Friend> friendList = new ArrayList<>();
        List<FriendNote> list = null;
        try {
            list = getDao().queryBuilder().build().list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(list != null && list.size() > 0) {
            for (FriendNote friendNote : list) {
                if(friendNote == null) {
                    continue;
                }
                if("0".equals(friendNote.whether_black)) {
                    friendList.add(setFriend(friendNote));
                }
            }
        }
        return friendList;
    }

    /**
     * 获取所有好友，不包括自己和小助手
     */
    public void getAllFriend(DbSubscribe<List<Friend>> subscribe){

        toFlowable(emitter -> {
            List<Friend> friendList = new ArrayList<>();
            List<FriendNote> list = null;
            try {
                list = getDao().queryBuilder().build().list();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onNext(friendList);
                emitter.onComplete();
            }
            String uid = ModuleMgr.getCenterMgr().getUID();
            if(list != null && list.size() > 0) {
                for (FriendNote friendNote : list) {
                    if(friendNote == null) {
                        continue;
                    }
                    if(uid.equals(friendNote.uid) || "1".equals(friendNote.uid)) {
                        continue;
                    }

                    if("0".equals(friendNote.whether_black)) {
                        continue;
                    }

                    friendList.add(setFriend(friendNote));
                }
            }
            emitter.onNext(friendList);
            emitter.onComplete();
        }, subscribe);


    }

    /**
     * 获取所有好友，包括自己，不包括小助手
     */
    public List<Friend> getAllFriendAndSelf(){
        List<FriendNote> list = getDao().queryBuilder().build().list();
        List<Friend> friendList = new ArrayList<>();
        if(list != null && list.size() > 0) {
            for (FriendNote friendNote : list) {
                if(friendNote == null) {
                    continue;
                }
                if("1".equals(friendNote.uid)) {
                    continue;
                }

                if("0".equals(friendNote.whether_black)) {
                    continue;
                }

                friendList.add(setFriend(friendNote));
            }
        }
        return friendList;
    }

    /**
     * 根据用户id查询好友
     */
    public void getFriend(String uid, DbSubscribe<Friend> subscribe){

        toFlowable(emitter -> {
            Friend friend;
            FriendNote friendNote = getFriendNote(uid);
            if(friendNote != null) {
                friend =  setFriend(friendNote);
            }else {
                friend = new Friend(uid);
            }
            emitter.onNext(friend);
            emitter.onComplete();
        }, subscribe);

    }
    public Friend getFriend(String uid){

        FriendNote friendNote = getFriendNote(uid);
        if(friendNote != null) {
            return setFriend(friendNote);
        }else {
            return new Friend(uid);
        }
    }

    /**
     * 数据库中是否存在自己
     */
    public Boolean isExitSelf(){
        String uid = ModuleMgr.getCenterMgr().getUID();
        FriendNote friendNote = getFriendNote(uid);
        return friendNote != null;
    }
    /**
     * 数据库中是否存在小助手
     */
    public Boolean isExitHelp(){
        FriendNote friendNote = getFriendNote("1");
        return friendNote != null;
    }
    /**
     * 获取自己
     */
    public Friend getSelf(){
        String uid = ModuleMgr.getCenterMgr().getUID();
        FriendNote friendNote = getFriendNote(uid);
        if(friendNote != null) {
            return setFriend(friendNote);
        }
        return new Friend(uid);
    }

    private FriendNote getFriendNote(String uid) {
        FriendNote friendNote = null;
        try {
            friendNote = getDao().queryBuilder()
                    .where(FriendNoteDao.Properties.Uid.eq(uid)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendNote;
    }

    /**
     * 删除好友
     */
    public void delete(String uid){
        FriendNote friendNote = getFriendNote(uid);
        try {
            getDao().delete(friendNote);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @NonNull
    private FriendNote setFriendNote(Friend friend) {
        FriendNote friendNote = new FriendNote();
        friendNote.uid = friend.uid;
        friendNote.phone = friend.phone;
        friendNote.nickname = friend.nickname;
        friendNote.headpic = friend.headpic;
        friendNote.time = String.valueOf(friend.time);
        friendNote.sex = friend.sex;
        friendNote.whether_friend = friend.whether_friend;
        friendNote.whether_black = friend.whether_black;
        friendNote.burn = friend.burn;
        friendNote.top = friend.top;
        friendNote.screenshot_notify = friend.screenshot_notify;
        friendNote.message_mute = friend.message_mute;
        friendNote.pinyin = friend.getPinYin();
        friendNote.friend_remark = friend.friend_remarks;
        friendNote.dq_num = friend.dq_num;
        friendNote.strjson = friend.descriptions;
        friendNote.isVip = friend.isVip;
        friendNote.vipStartTime = friend.vipStartTime;
        friendNote.vipEndTime = friend.vipEndTime;
        return friendNote;
    }

    @NonNull
    private Friend setFriend(FriendNote friendNote) {
        Friend friend = new Friend();
        friend.uid = friendNote.uid;
        friend.phone = friendNote.phone;
        friend.nickname = friendNote.nickname;
        friend.headpic = friendNote.headpic;
        friend.sex = friendNote.sex;
        friend.whether_friend = friendNote.whether_friend;
        friend.whether_black = friendNote.whether_black;
        friend.burn = friendNote.burn;
        friend.top = friendNote.top;
        friend.screenshot_notify = friendNote.screenshot_notify;
        friend.message_mute = friendNote.message_mute;
        friend.time = Long.parseLong(friendNote.time);
        friend.pinYin = friendNote.pinyin;
        friend.friend_remarks = friendNote.friend_remark;
        friend.dq_num = friendNote.dq_num;
        friend.descriptions = friendNote.strjson;
        friend.isVip = friendNote.isVip;
        friend.vipStartTime = friendNote.vipStartTime;
        friend.vipEndTime = friendNote.vipEndTime;
        return friend;
    }

    public interface OnLoading{
        void load(Boolean isLoad);
    }
}
