package com.wd.daquan.chat.bean;

import java.io.Serializable;

/**
 * @author: dukangkang
 * @date: 2018/9/17 17:04.
 * @description: todo ...
 */
public class RobRpEntity implements Serializable {
    public String whether_block;
    public String whether_unblock;
    public String status;  //1:未领取 2:已领取 3:过期退回 4:未领完
    public String whether_receive; //0未领取；1已领取
    public int number;  //0已领完，非0表示红包剩余个数
    public String is_receive; //0不可领；1可领取
}
