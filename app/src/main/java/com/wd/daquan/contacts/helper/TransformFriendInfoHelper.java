package com.wd.daquan.contacts.helper;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.wd.daquan.contacts.utils.CharacterParser;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.util.ChinesePinyinUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: 方志
 * @Time: 2018/8/30 17:08
 * @Description: 转换好友信息
 */
public class TransformFriendInfoHelper {

    private TransformFriendInfoHelper(){
    }

    private static class TransformFriendInfoHolder{
        static TransformFriendInfoHelper INSTANCE = new TransformFriendInfoHelper();
    }

    public static TransformFriendInfoHelper getInstance() {
        return TransformFriendInfoHolder.INSTANCE;
    }

    public List<Friend> setPinYin(@NonNull List<Friend> list){
        List<Friend> friendList = new ArrayList<>();
        for (Friend friend : list) {
            //汉字转换成拼音
            String pinyin = null;
            CharacterParser parser = new CharacterParser();
            if(!TextUtils.isEmpty(friend.remarks)) {
                //群备注
//                pinyin = parser.getSpelling(friend.remarks);
                pinyin = ChinesePinyinUtil.getPingYin(friend.remarks);
            }else if(!TextUtils.isEmpty(friend.friend_remarks)) {
                //好友备注
//                pinyin = parser.getSpelling(friend.friend_remarks);
                pinyin = ChinesePinyinUtil.getPingYin(friend.friend_remarks);
            }else if(!TextUtils.isEmpty(friend.nickname)) {
                //个人昵称
//                pinyin = parser.getSpelling(friend.nickname);
                pinyin = ChinesePinyinUtil.getPingYin(friend.nickname);
            }
            if (parser.isHaveInterpunction) {
                pinyin = "00000";
            }
            String letter = TextUtils.isEmpty(pinyin) ? "" : pinyin.substring(0, 1).toUpperCase();
            if (!letter.matches("[A-Z]")) {
                letter = "#";
            }
            friend.pinYin = letter;
            friendList.add(friend);
        }
        return friendList;
    }


    /**
     * 拼接群组内，群成员头像
     */
    public String joinUserHead(List<Friend> list){
        String headPic = ModuleMgr.getCenterMgr().getAvatar();
        List<String> headList = new ArrayList<>();
        headList.add(headPic);
        int count = 0;
        for (Friend friend : list) {
            if (friend == null) {
                continue;
            }
            if(count > 8) {
                break;
            }
            headList.add(friend.headpic);
            count++;

        }

        return GsonUtils.toJson(headList);
    }

    /**
     * 拼接群组内，群成员ID
     */
    public String jointId(List<Friend> list, boolean isAddSelf) {
        List<String> uidList = new ArrayList<>();
        if(isAddSelf) {
            String uid = ModuleMgr.getCenterMgr().getUID();
            uidList.add(uid);
        }

        for (Friend friend : list) {
            if (friend == null) {
                continue;
            }
            uidList.add(friend.uid);
        }
        return GsonUtils.toJson(uidList);
    }

    /**
     * 拼接群组内，群成员名称
     */
    public String jointNames(List<Friend> list, boolean isAddSelf) {
        List<String> nameList = new ArrayList<>();
        if(isAddSelf) {
            String nickName = ModuleMgr.getCenterMgr().getNickName();
            nameList.add(nickName);
        }

        int count = 0;
        for (Friend friend : list) {
            if (friend == null) {
                continue;
            }
            if(count > 8) {
                break;
            }
            nameList.add(friend.getName());
            count++;
        }
        return GsonUtils.toJson(nameList);
    }

    public String subString(String target) {
        if (target.length() > 16) {
            target = target.substring(0, 16);
            return target + "...";
        } else {
            return target;
        }
    }
}
