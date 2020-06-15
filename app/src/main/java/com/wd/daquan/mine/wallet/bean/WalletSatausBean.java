package com.wd.daquan.mine.wallet.bean;

import java.io.Serializable;

/**
 * Created by DELL on 2019/5/25.
 */

public class WalletSatausBean implements Serializable {

    private static final long serialVersionUID = 3443425364857042196L;

    public String username;
    public String balance;//账户可用余额，单位为 RMB-元。 大于 0 的数字，精确到小数点后两位。
    public String freeze_balance;//账户冻结余额，单位为 RMB-元。 大于 0 的数字，精确到小数点后两位。
    public String cashout_amt;//可提现金额，单位为 RMB-元。 大于 0 的数字，精确到小数点后两位。
    public String dt_register;//用户注册时间，格式：YYYY-MM-DD HH:MM:SS
    public String stat_user;//用户状态（0 ：V0(待实名认证)、 1：V1（完成信息认证） 2：V2（V1+证件认证） 3：暂停 4：注销 5：审核 6：登录锁定 7：待激活）
    public String type_idcard;//证件类型，0：身份证或企业经营证件
    public String no_idcard;//证件号
    public String mob_bind;//绑定手机号码
    public String addr_conn;//联系地址
    public String kyc_status;//审核状态（0:待实名认证 1:实名认证通过 2:实名认证不通过 3:审核中 4:审核通过 5:审核不通过 6:证件过期 7:待完善）
    public String fail_reason;//审核失败原因
    public String bindcard_nums;//绑定银行卡个数

}
