package com.wd.daquan.mine.bean;

/**
 * @author: dukangkang
 * @date: 2018/5/18 15:29.
 * @description: todo ...
 */
public class ScanCodeEntity {

    public String type;         // (个人)person_qrcode （群组）group_qrcode （斗圈收款码）cnpay_qrcode
    public String uid;          // 用户ID
    public String group_id;     // 群组ID
    public String source_id;   // 邀请进群来源uid
    public String money;        // 斗圈收款码
}
