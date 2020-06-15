package com.wd.daquan.mine.wallet.bean

/**
 * @Author: 方志
 * @Time: 2019/6/3 14:42
 * @Description:提现 withdrawal
 */
class CashOutBean {

    //接口返回字段
    var total_amount: String? = ""//提现总金额
    var fee: String? = ""//提现手续费
    var amount: String? = ""//提现到账金额
    var cardno: String? = ""//银行卡号
    var bankname: String? = ""//银行名称
}