package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * @author: dukangkang
 * @date: 2019/2/25 16:20.
 * @description: todo ...
 */
public class GroupInviteAttachment extends CustomAttachment {
    public String group_id;
    public String sourceUserId;

    public GroupInviteAttachment() {
        super(CustomAttachmentType.GROUP_INVITE_MSG);
    }

    @Override
    protected void parseData(JSONObject data) {
        group_id = data.getString(KeyValue.TeamInvite.GROUP_ID);
        sourceUserId = data.getString(KeyValue.TeamInvite.SOURCE_USERID);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KeyValue.TeamInvite.GROUP_ID, group_id);
            jsonObject.put(KeyValue.TeamInvite.SOURCE_USERID, sourceUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
