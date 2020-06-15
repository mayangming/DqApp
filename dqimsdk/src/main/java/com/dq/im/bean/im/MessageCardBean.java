package com.dq.im.bean.im;

import com.dq.im.model.IMContentDataModel;

/**
 * 名片布局
 */
public class MessageCardBean extends IMContentDataModel {
    public String userId;
    public String headPic;
    public String nickName;
    public String extra;
    public String dqNum;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getDqNum() {
        return dqNum;
    }

    public void setDqNum(String dqNum) {
        this.dqNum = dqNum;
    }

    @Override
    public String toString() {
        return "MessageCardBean{" +
                "userId='" + userId + '\'' +
                ", headPic='" + headPic + '\'' +
                ", nickName='" + nickName + '\'' +
                ", extra='" + extra + '\'' +
                ", dqNum='" + dqNum + '\'' +
                '}';
    }
}