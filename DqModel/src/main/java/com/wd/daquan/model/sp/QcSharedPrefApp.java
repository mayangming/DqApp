package com.wd.daquan.model.sp;

/**
 * @author: dukangkang
 * @date: 2019/2/20 14:25.
 * @description: todo ...
 */
public class QcSharedPrefApp extends BaseSharedPreference {

    // 戳一戳消息发送时间戳
    public static final String POKE_ONE_POKE = "poke_one_poke";
    //配置信息
    public static final String SERVER_CONFIG = "server_config";
    // 配置信息-横幅banner
    public static final String NEW_SERVER_CONFIG = "new_server_config";

    // 支付宝收款码
    public static final String ALI_PAYMENT_CODE_SEND = "ali_payment_code_send";
    public static final String redEnvelopedRainSwitch = "redEnvelopedRainSwitch";//红包雨开关
    public QcSharedPrefApp( String fileName) {
        super(fileName);
    }

}
