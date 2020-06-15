package com.wd.daquan.mine.wallet.bean;


import java.io.Serializable;

public class LooseDetailsItemBean implements Serializable {
    private static final long serialVersionUID = 3330369656982792155L;
    public String countno;//记录总条数
    public String oid_biz;//业务类型（110001 账户充值；101001 虚拟商品销售； 107001 商户付款； 202000 账号提现； 301000 转账； 301001 我要付款；
    // 301002 我要收款）
    public String oid_paybill;//连连支付支付单 号
    public String date_acct;//账务日期
    public String amt_bal;//交易后余额
    public String flag_dc;//资金存取标识
    public String amt_inoccur;//收款金额
    public String amt_outoccur;//付款金额
    public String dt_billtrans;//交易时间
    public String memo;//备注
}
