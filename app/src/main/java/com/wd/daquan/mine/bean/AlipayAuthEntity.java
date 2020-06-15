package com.wd.daquan.mine.bean;

import java.io.Serializable;


public class AlipayAuthEntity implements Serializable {
    private static final long serialVersionUID = -1L;
    public String pid;
    public String appid;
    public String rsa_private_key;
    public String alipay_rsa_public_key;
    public String gat_way_url;
    public String signature;
    public String redpacket_id;
    public String out_order_no;
    public String out_request_no;
    public String pay_timeout;
    public String amount;
    public String sign;
    public String orderinfo;
    public String  redpacket_num;
    public String auth_no;
    public RedPacketEntity redResp;
    public String blessing;
}
