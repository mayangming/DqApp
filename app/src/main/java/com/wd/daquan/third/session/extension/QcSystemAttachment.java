package com.wd.daquan.third.session.extension;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.SystemType;
import com.wd.daquan.model.bean.Friend;

/**
 * @author: dukangkang
 * @date: 2018/9/12 13:28.
 * @description:
 * 系统消息，传递对象
 */
public class QcSystemAttachment extends CustomAttachment {
    public String uid;
    public String nickname;
    public String message;
    public String remarks;//群组备注
    public String groupId;
    public String groupName;
    public String groupPic;
    public String source_uid;
    public String source_nickname;
    public String extra;
    public String cnExtra;//是否阅后即焚

    public QcSystemAttachment() {
        super(CustomAttachmentType.QC_SYSTEM);
    }

    @Override
    protected void parseData(JSONObject data) {
        uid = data.getString(KeyValue.SystemMessage.UID);
        nickname = data.getString(KeyValue.SystemMessage.NICKNAME);
        message = data.getString(KeyValue.SystemMessage.MESSAGE);
        remarks = data.getString(KeyValue.SystemMessage.REMARKS);
        groupId = data.getString(KeyValue.SystemMessage.GROUP_ID);
        groupName = data.getString(KeyValue.SystemMessage.GROUP_NAME);
        groupPic = data.getString(KeyValue.SystemMessage.GROUP_PIC);
        source_uid = data.getString(KeyValue.SystemMessage.SOURCE_UID);
        source_nickname = data.getString(KeyValue.SystemMessage.SOURCE_NICKNAME);
        extra = data.getString(KeyValue.SystemMessage.EXTRA);
        cnExtra = data.getString(KeyValue.SystemMessage.CNEXTRA);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("nickName", nickname);
            jsonObject.put("content", message);
            jsonObject.put("remarks", remarks);
            jsonObject.put("groupId", groupId);
            jsonObject.put("groupName", groupName);
            jsonObject.put("groupPic", groupPic);
            jsonObject.put("source_uid", source_uid);
            jsonObject.put("source_nickname", source_nickname);
            jsonObject.put("extra", extra);
            jsonObject.put("cnExtra", cnExtra);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getContent(boolean isCurUser) {
        String tips = "未知通知提醒";
        if (SystemType.MODIFY_PROTRAIT.equals(this.extra)) {
            tips = "修改了群头像";
        } else if (SystemType.MODIFY_NOTICE.equals(this.extra)) {
            tips = "修改了群公告";
        } else if (SystemType.TEAM_SCREENSHOT_OPEN.equals(this.extra)) {
//            tips = "群主开启截屏通知";
            if (isCurUser) {
                tips = "你" + this.message;
            } else {
//                tips = this.nickName + this.content;
                tips = String.format(DqApp.getStringById(R.string.team_screenshot), this.nickname, this.message);
            }
        } else if (SystemType.TEAM_SCREENSHOT_CLOSE.equals(this.extra)) {
//            tips = "群主关闭截屏通知";
            if (isCurUser) {
                tips = "你" + this.message;
            } else {
                tips = String.format(DqApp.getStringById(R.string.team_screenshot), this.nickname, this.message);
//                tips = this.nickName + this.content;
            }
        } else if (SystemType.P2P_SCREENSHOT_OPEN.equals(this.extra)) {
            tips = "开启截屏通知";
        } else if (SystemType.P2P_SCREENSHOT_CLOSE.equals(this.extra)) {
            tips = "关闭截屏通知";
        } else if (SystemType.P2P_SCREENSHOT_OPTION.equals(this.extra)) {
            if (isCurUser) {
                tips = "你" + this.message;
            } else {
                tips = this.nickname + this.message;
            }
        } else if (SystemType.TEAM_SCREENSHOT_OPTION.equals(this.extra)) {
            if (isCurUser) {
                tips = "你" + this.message;
            } else {
                tips = this.nickname + this.message;
            }
        } else if (SystemType.TEAM_PROHIBITION.equals(this.extra)) {
            tips = "群组已封禁";
        } else if (SystemType.TEAM_UNSEAL.equals(this.extra)) {
            tips = "群组已解封";
        } else if (SystemType.SCAN_ADD_TEAM.equals(this.extra)) {
            String currentId = ModuleMgr.getCenterMgr().getUID();
            if ("0".equals(currentId)) {
                tips = "你" + this.message;
            } else if (isCurUser) {
                tips = "你通过扫描二维码加入群聊";
            } else if (currentId.equals(this.source_uid)) {
                tips = this.nickname + "通过扫描二维码加入群聊";
            } else {
                tips = this.nickname + "通过扫描" + this.source_nickname + "的二维码加入群聊";
            }
        } else if (SystemType.ALL_MEMBER_ANEXCUSE.equals(this.extra)) {
            tips = "全员禁言";
        } else if (SystemType.TEAM_AUTH_CLOSE.equals(this.extra)) {
            tips = this.message;
        } else if (SystemType.TEAM_AUTH_OPEN.equals(this.extra)) {
            tips = this.message;
        } else if (SystemType.TEAM_PROTECT_OPEN.equals(this.extra)) {
            if (isCurUser) {
                tips = "你" + this.message;
            } else {
                tips = upadteRemarksMsg(this.uid, this.nickname, this.message);
            }
        } else if (SystemType.TEAM_PROTECT_CLOSE.equals(this.extra)) {
            if (isCurUser) {
                tips = "你" + this.message;
            } else {
                tips = upadteRemarksMsg(this.uid, this.nickname, this.message);
            }
        } else if (SystemType.RED_RECEIVE_CLOSE.equals(this.extra)) {
            tips = this.message;
        } else if (SystemType.RED_RECEIVE_OPEN.equals(this.extra)) {
            tips = this.message;
        } else if (SystemType.RED_LONG_TIME_UNRECEIVE_OPEN.equals(this.extra)) {
            tips = this.message;
        } else if (SystemType.RED_LONG_TIME_UNRECEIVE_CLOSE.equals(this.extra)) {
            if (isCurUser) {
                tips = "长时间未领取红包功能已被你禁用";
            } else {
                tips = this.message;
            }
        } else if (SystemType.ADMIN_UPDATE_MINE_NICKNAME.equals(this.extra)) {
            if (isCurUser) {
                tips = "你修改了我在本群信息";
            } else {
                if (!TextUtils.isEmpty(remarks)) {
                    tips = remarks + "修改了我在本群信息" ;
                } else {
                    tips = nickname + "修改了我在本群信息" ;
                }
            }
        }
        return tips;
//        return new SpannableString(tips);
    }

    public String getContentSummary(boolean isCurUser) {
        String tips = "未知通知提醒";
        if (SystemType.MODIFY_PROTRAIT.equals(this.extra)) {
            tips = "[修改了群头像]";
        } else if (SystemType.MODIFY_NOTICE.equals(this.extra)) {
            tips = "[修改了群公告]";
        } else if (SystemType.TEAM_SCREENSHOT_OPEN.equals(this.extra)) {
            tips = "[开启截屏通知]";
        } else if (SystemType.TEAM_SCREENSHOT_CLOSE.equals(this.extra)) {
            tips = "[关闭截屏通知]";
        } else if (SystemType.P2P_SCREENSHOT_OPEN.equals(this.extra)) {
            tips = "[开启截屏通知]";
        } else if (SystemType.P2P_SCREENSHOT_CLOSE.equals(this.extra)) {
            tips = "[关闭截屏通知]";
        } else if (SystemType.P2P_SCREENSHOT_OPTION.equals(this.extra)) {
            if (isCurUser) {
                tips = "你" + this.message;
            } else {
                tips = this.nickname + this.message;
            }
        } else if (SystemType.TEAM_SCREENSHOT_OPTION.equals(this.extra)) {
            if (isCurUser) {
                tips = "你" + this.message;
            } else {
                tips = this.nickname + this.message;
            }
        } else if (SystemType.TEAM_PROHIBITION.equals(this.extra)) {
            tips = "[群组已封禁]";
        } else if (SystemType.TEAM_UNSEAL.equals(this.extra)) {
            tips = "[群组已解封]";
        } else if (SystemType.SCAN_ADD_TEAM.equals(this.extra)) {
            String currentId = ModuleMgr.getCenterMgr().getUID();
            if ("0".equals(currentId)) {
                tips = "你" + this.message;
            } else if (isCurUser) {
                tips = "你通过扫描二维码加入群聊";
            } else if (currentId.equals(this.source_uid)) {
                tips = this.nickname + "通过扫描二维码加入群聊";
            } else {
                tips = this.nickname + "通过扫描" + this.source_nickname + "的二维码加入群聊";
            }
        } else if (SystemType.ALL_MEMBER_ANEXCUSE.equals(this.extra)) {
            tips = "[全员禁言]";
        } else if (SystemType.TEAM_AUTH_CLOSE.equals(this.extra)) {
            tips = this.message;
        } else if (SystemType.TEAM_AUTH_OPEN.equals(this.extra)) {
            tips = this.message;
        } else if (SystemType.TEAM_PROTECT_OPEN.equals(this.extra)) {
            if (isCurUser) {
                tips = "你" + this.message;
            } else {
                tips = upadteRemarksMsg(this.uid, this.nickname, this.message);
            }
        } else if (SystemType.TEAM_PROTECT_CLOSE.equals(this.extra)) {
            if (isCurUser) {
                tips = "你" + this.message;
            } else {
                tips = upadteRemarksMsg(this.uid, this.nickname, this.message);
            }
        } else if (SystemType.RED_RECEIVE_CLOSE.equals(this.extra)) {
            tips = this.message;
        } else if (SystemType.RED_RECEIVE_OPEN.equals(this.extra)) {
            tips = this.message;
        } else if (SystemType.RED_LONG_TIME_UNRECEIVE_OPEN.equals(this.extra)) {
            tips = this.message;
        } else if (SystemType.RED_LONG_TIME_UNRECEIVE_CLOSE.equals(this.extra)) {
            if (isCurUser) {
                tips = "长时间未领取红包功能已被你禁用";
            } else {
                tips = this.message;
            }
        } else if (SystemType.ADMIN_UPDATE_MINE_NICKNAME.equals(this.extra)) {
            return "[群组通知]";
        }
        return tips;
    }

    private String upadteRemarksMsg(String uid, String nickName, String message) {
//            Friend mFriend = SealUserInfoManager.getInstance().getFriendByID(uid);
        Friend friend = FriendDbHelper.getInstance().getFriend(uid);
        String content;
        if(friend == null){
            return "“" + nickName + "”" +  message;
        }
        String remarks = friend.getRemarks(); // 从库中查询备注
        if (!TextUtils.isEmpty(remarks)) {
            content = "“" + remarks + "”" + message;
        } else {
            content = "“" + nickName + "”" +  message;
        }
        return content;
    }
}
