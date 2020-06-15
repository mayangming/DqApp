package com.wd.daquan.model.sp;

/**
 * @Author: 方志
 * @Time: 2018/6/13 13:46
 * @Description: 群组信息缓存
 */
public class QCSharedPreTeamInfo extends BaseSharedPreference {

    public static final String MEMBER_COUNT = "member_count";
    public static final String GROUP_OWNER = "group_owner";
    public static final String CONVERSATION_BANNER = "conversation_banner";
    public static final String LONG_TIME_RED_PACKAGE = "long_time_red_package";


    public QCSharedPreTeamInfo(String fileName) {
        super( fileName);
    }
}
