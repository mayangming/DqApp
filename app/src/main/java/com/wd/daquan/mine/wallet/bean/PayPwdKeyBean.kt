package com.wd.daquan.mine.wallet.bean

/**
 * @Author: 方志
 * @Time: 2019/6/5 19:04
 * @Description:
 */
class PayPwdKeyBean {
    var random_value: String? = ""//随机因子VALUE
    var random_key: String? = ""//随机因子KEY
    var license: String? = ""//license
    var rsa_public_content: String? = ""//连连 RSA 公钥
}