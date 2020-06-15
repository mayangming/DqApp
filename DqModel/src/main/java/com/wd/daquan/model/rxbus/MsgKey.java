package com.wd.daquan.model.rxbus;

/**
 * @author: dukangkang
 * @date: 2018/9/26 20:56.
 * @description: todo ...
 */
public class MsgKey {
    public interface Recent {
        public String KEY_CONTACT_ID = "key_contact_id";
    }

    public interface GroupNotice {
        // 群组公告-@所有人
        public String KEY_AIT_ALL = "key_group_notice_ait_all";
    }

    public interface Team {
        // 退出劝阻，最近会话ID
        String KEY_QUIT_RECENTCONTACT_ID = "key_team_quit_recentcontact_id";

        // 草稿状态，消息ID
        String KEY_DRAFT_RECENTCONTACT_ID = "key_team_draft_recentcontact_id";
    }

    public interface Personalinfo_Change {
        String Personalinfo_Change_QingChatNum = "personalinfo_change_qingchatnum";

    }
}
