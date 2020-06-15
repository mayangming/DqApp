package com.wd.daquan.chat.redpacket;

import com.wd.daquan.chat.bean.RobRpEntity;
import java.io.Serializable;

public interface RedPacketCallback extends Serializable {
    // 允许收发红包
    public void allowForbidRedpacket();
    // 抢红包
    public void robRedPacket(RobRpEntity robRpEntity);
    // 开红包
    public void openRedPacket(int code, String message);
    // 红包已抢完
    public void redpacketEmpty();

    public void onSuccess();

    public void onFailed(int code);
}