package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * @author: fangzhi
 * @date: 2018/11/20 10:25.
 * @description: app配置
 */
public class ConfigBean implements Serializable {
    public String share_url;
    // 1：使用新消息类型（减少消息尺寸）0：使用旧消息类型
    public String msg_type;
    // 0:关 1:开
    public String poke;
    // 小游戏入口图片名称
    public String game_icon;
    // 默认祝福语
    public String greetings;
    ////领取红包开关 0：关 1：开
    public String redpacket_switch;
    // 阿里支付宝收款码路径
    public String ali_payment_path;
    // 阿里支付宝收款码上传目录
    public String ali_payment_directory;

    @Override
    public String toString() {
        return "ConfigBean{" +
                "share_url='" + share_url + '\'' +
                ", msg_type='" + msg_type + '\'' +
                ", poke='" + poke + '\'' +
                ", game_icon='" + game_icon + '\'' +
                ", greetings='" + greetings + '\'' +
                ", redpacket_switch='" + redpacket_switch + '\'' +
                '}';
    }
}
