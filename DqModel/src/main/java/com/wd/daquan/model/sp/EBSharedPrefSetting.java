package com.wd.daquan.model.sp;

/**
 * <设置信息缓存>
 */
public class EBSharedPrefSetting extends BaseSharedPreference {
    /**
     * 声音提醒 默认已开启
     */
    public static final String SOUND_REMINDER = "sound_reminder";

    /**
     * 震动提醒 默认已开启
     */
    public static final String VIBRATION_REMINDER = "vibration_reminder";

    /**
     * 是否打开过引导页
     */
    public static final String ISBOOTPAGE= "isBootpage";




    public EBSharedPrefSetting(String fileName) {
        super(fileName);
    }
}
