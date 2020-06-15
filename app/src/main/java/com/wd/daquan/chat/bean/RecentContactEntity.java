package com.wd.daquan.chat.bean;

import com.dq.im.model.IMContentDataModel;
import com.dq.im.type.ImType;
import com.dq.im.type.MessageType;
import com.wd.daquan.model.interfaces.ISelect;

/**
 * @author: dukangkang
 * @date: 2018/9/26 19:22.
 * @description: todo ...
 */
public class RecentContactEntity implements ISelect {
    public String headpic;
    public String contactId;
    public String fromAccount;
    public String fromNick;
    public String recentMessageId;
    public MessageType msgType;
    public String content;
    public IMContentDataModel attachment;
    public ImType sessionType;

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean selected) {

    }
}
