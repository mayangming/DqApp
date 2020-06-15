
package com.wd.daquan.model.sp;

public class QCSharedPrefManager {

    /** SharedPreference文件名列表 */
    private static final String PREF_NAME_USERINFO = "userinfo";

    private static final String PREF_NAME_SETTING = "setting";

    private static final String PREF_NAME_TEAM_INFO = "team_info";

    private static final String PREF_NAME_APP_INFO= "app_info_sp";

    /**
     * 用户信息缓存
     */
    private static EBSharedPrefUser mEBSharedPrefUser;
    private static QCSharedPreTeamInfo mTeamInfoSharedPre;

    /**
     * APP存储相关配置
     */
    private static QcSharedPrefApp mAppSp = null;

    /**
     * 设置信息缓存
     */
    private EBSharedPrefSetting mEBSharedPrefSetting;


    private QCSharedPrefManager(){
    }

    private volatile static QCSharedPrefManager mQCSharedPrefManager = null;

    public static QCSharedPrefManager getInstance(){
        if(null == mQCSharedPrefManager){
            synchronized (QCSharedPrefManager.class){
                if(null == mQCSharedPrefManager){
                    mQCSharedPrefManager = new QCSharedPrefManager();
                    return mQCSharedPrefManager;
                }else{
                    return mQCSharedPrefManager;
                }
            }
        }else{
            return mQCSharedPrefManager;
        }
    }


    /**
     * 用户信息缓存
     * @return
     */
    public EBSharedPrefUser getKDPreferenceUserInfo() {
        return mEBSharedPrefUser == null ? mEBSharedPrefUser = new EBSharedPrefUser(
                PREF_NAME_USERINFO) : mEBSharedPrefUser;
    }

    /**
     * 设置信息缓存
     */
    public EBSharedPrefSetting getKDPreferenceTestSetting() {
        return mEBSharedPrefSetting == null ? mEBSharedPrefSetting = new EBSharedPrefSetting(
                PREF_NAME_SETTING)
                : mEBSharedPrefSetting;
    }

    /**
     * 群组相关配置
     * @return
     */
    public QCSharedPreTeamInfo getKDPreferenceTeamInfo() {
        return mTeamInfoSharedPre == null ? mTeamInfoSharedPre = new QCSharedPreTeamInfo(
              PREF_NAME_TEAM_INFO) : mTeamInfoSharedPre;
    }

    /**
     * 获取应用程序 存储SP
     * @return
     */
    public QcSharedPrefApp getSpForApp() {
        if (mAppSp == null) {
            mAppSp = new QcSharedPrefApp(PREF_NAME_APP_INFO);
        }
        return mAppSp;
    }



}
