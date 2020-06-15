package com.wd.daquan.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.da.library.listener.DialogListener;
import com.da.library.view.CommDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 方志
 * Date: 2018/7/26 14:25
 * Description: Android手机打开自启动管理和锁屏清理页面 oppp新手机路径有问题
 */
@SuppressLint("FieldLeak, BatteryLife")
public class IntentWrapper {

    //Android 7.0+ Doze 模式
    private final int DOZE = 98;
    //华为 自启管理
    private final int HUAWEI = 99;
    //华为 锁屏清理
    private final int HUAWEI_GOD = 100;
    //小米 自启动管理
    private final int XIAOMI = 101;
    //小米 神隐模式
    private final int XIAOMI_GOD = 102;
    //三星 5.0/5.1 自启动应用程序管理
    private final int SAMSUNG = 103;
    //三星 6.0+ 未监视的应用程序管理
    private final int SAMSUNG_GOD = 107;
    //魅族 自启动管理
    private final int MEIZU = 104;
    //魅族 待机耗电管理
    private final int MEIZU_GOD = 105;
    //Oppo 自启动管理
    private final int OPPO = 106;
    //Oppo 电量优化
    private final int OPPO_DL = 1061;
    //Oppo 自启动管理(旧版本系统)
    private final int OPPO_OLD = 108;
    //Vivo 后台自启动
    private final int VIVO = 1091;
    //Vivo 后台高耗电
    private final int VIVO_GOD = 1092;
    //金立 应用自启
    private final int GIONEE = 110;
    //乐视 自启动管理
    private final int LETV = 111;
    //乐视 应用保护
    private final int LETV_GOD = 112;
    //酷派 自启动管理
    private final int COOLPAD = 113;
    //联想 后台管理
    private final int LENOVO = 114;
    //联想 后台耗电优化
    private final int LENOVO_GOD = 115;
    //中兴 自启管理
    private final int ZTE = 116;
    //中兴 锁屏加速受保护应用
    private final int ZTE_GOD = 117;
//    //360 自启管理
//    private  final int S360 = 118;
//    //360 锁屏加速受保护应用
//    private  final int S360_GOD = 119;

    private List<IntentWrapper> mIntentWrapperList = new ArrayList<>();
    private AlertDialog mDialog = null;
    private String sApplicationName;
    /**
     * 引导用户设置dialog
     */
    private CommDialog mGuideDialog = null;
    private Intent intent;
    private int type;

    public IntentWrapper() {
        if (mIntentWrapperList == null) {
            mIntentWrapperList = new ArrayList<>();
        }

        getIntentWrapperList();
    }

    private IntentWrapper(Intent intent, int type) {
        this.intent = intent;
        this.type = type;
    }

//    private String getMobileType() {
//        Log.e("TAG", "Build.MANUFACTURER ： " + Build.MANUFACTURER);
//        return Build.MANUFACTURER;
//    }

    private void getIntentWrapperList() {

        //Android 7.0+ Doze 模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            PowerManager pm = (PowerManager) DqApp.sContext.getSystemService(Context.POWER_SERVICE);
            boolean ignoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(DqApp.sContext.getPackageName());
            if (!ignoringBatteryOptimizations) {
                Intent dozeIntent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                dozeIntent.setData(Uri.parse("package:" + DqApp.sContext.getPackageName()));
                mIntentWrapperList.add(new IntentWrapper(dozeIntent, DOZE));
            }
        }
        
//            ("HUAWEI".equalsIgnoreCase(Build.MANUFACTURER))
//            ("Lenovo".equalsIgnoreCase(Build.MANUFACTURER))
//            ("Xiaomi".equalsIgnoreCase(Build.MANUFACTURER))
//            ("samsung".equalsIgnoreCase(Build.MANUFACTURER))
//            ("Meizu".equalsIgnoreCase(Build.MANUFACTURER))
//            ("Oppo".equalsIgnoreCase(Build.MANUFACTURER))
//            ("Vivo".equalsIgnoreCase(Build.MANUFACTURER))
//            ("Letv".equalsIgnoreCase(Build.MANUFACTURER))

        //华为 自启管理
        Intent huaweiIntent = new Intent();
        huaweiIntent.setAction("huawei.intent.action.HSM_BOOTAPP_MANAGER");
        mIntentWrapperList.add(new IntentWrapper(huaweiIntent, HUAWEI));

        //华为 锁屏清理
        Intent huaweiGodIntent = new Intent();
        huaweiGodIntent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
        mIntentWrapperList.add(new IntentWrapper(huaweiGodIntent, HUAWEI_GOD));

        //小米 自启动管理
        Intent xiaomiIntent = new Intent();
        xiaomiIntent.setAction("miui.intent.action.OP_AUTO_START");
        xiaomiIntent.addCategory(Intent.CATEGORY_DEFAULT);
        mIntentWrapperList.add(new IntentWrapper(xiaomiIntent, XIAOMI));

        //小米 神隐模式
        Intent xiaomiGodIntent = new Intent();
        xiaomiGodIntent.setComponent(new ComponentName("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsConfigActivity"));
        xiaomiGodIntent.putExtra("package_name", DqApp.sContext.getPackageName());
        xiaomiGodIntent.putExtra("package_label", getApplicationName());
        mIntentWrapperList.add(new IntentWrapper(xiaomiGodIntent, XIAOMI_GOD));


        //三星 5.0/5.1 自启动应用程序管理
        Intent samsungLIntent = DqApp.sContext.getPackageManager().getLaunchIntentForPackage("com.samsung.android.sm");
        if (samsungLIntent != null)
            mIntentWrapperList.add(new IntentWrapper(samsungLIntent, SAMSUNG));

        //三星 6.0+ 未监视的应用程序管理
        Intent samsungMIntent = new Intent();
        samsungMIntent.setComponent(new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.battery.BatteryActivity"));
        mIntentWrapperList.add(new IntentWrapper(samsungMIntent, SAMSUNG_GOD));

        //魅族 自启动管理
        Intent meizuIntent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        meizuIntent.addCategory(Intent.CATEGORY_DEFAULT);
        meizuIntent.putExtra("packageName", DqApp.sContext.getPackageName());
        mIntentWrapperList.add(new IntentWrapper(meizuIntent, MEIZU));

        //魅族 待机耗电管理
        Intent meizuGodIntent = new Intent();
        meizuGodIntent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.powerui.PowerAppPermissionActivity"));
        mIntentWrapperList.add(new IntentWrapper(meizuGodIntent, MEIZU_GOD));

        //Oppo 自启动管理 手机管家权限页面
        Intent oppoIntent = new Intent();
        oppoIntent.setComponent(ComponentName.unflattenFromString("com.coloros.safecenter/com.coloros.privacypermissionsentry.PermissionTopActivity"));
        mIntentWrapperList.add(new IntentWrapper(oppoIntent, OPPO));

        //oppo电量优化
        Intent oppoDIntent = new Intent();
        oppoDIntent.setComponent(ComponentName.unflattenFromString("com.coloros.oppoguardelf/com.coloros.powermanager.fuelgaue.PowerConsumptionActivity"));
        mIntentWrapperList.add(new IntentWrapper(oppoDIntent, OPPO_DL));

        //Oppo 自启动管理(旧版本系统)
        Intent oppoOldIntent = new Intent();
        oppoOldIntent.setComponent(new ComponentName("com.color.safecenter", "com.color.safecenter.permission.startup.StartupAppListActivity"));
        mIntentWrapperList.add(new IntentWrapper(oppoOldIntent, OPPO_OLD));

        //vivo自启动 手机管家权限页面
        Intent vivoIntent = new Intent();
        vivoIntent.setComponent(ComponentName.unflattenFromString("com.vivo.permissionmanager/.activity.PurviewTabActivity"));
        mIntentWrapperList.add(new IntentWrapper(vivoIntent, VIVO));

        //Vivo 后台高耗电
        Intent vivoGodIntent = new Intent();
        vivoGodIntent.setComponent(new ComponentName("com.vivo.abe", "com.vivo.applicationbehaviorengine.ui.ExcessivePowerManagerActivity"));
        mIntentWrapperList.add(new IntentWrapper(vivoGodIntent, VIVO_GOD));

        //乐视 自启动管理
        Intent letvIntent = new Intent();
        letvIntent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
        mIntentWrapperList.add(new IntentWrapper(letvIntent, LETV));

        //乐视 应用保护
        Intent letvGodIntent = new Intent();
        letvGodIntent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.BackgroundAppManageActivity"));
        mIntentWrapperList.add(new IntentWrapper(letvGodIntent, LETV_GOD));

        //联想 后台管理
        Intent lenovoIntent = new Intent();
        lenovoIntent.setComponent(new ComponentName("com.lenovo.security", "com.lenovo.security.purebackground.PureBackgroundActivity"));
        mIntentWrapperList.add(new IntentWrapper(lenovoIntent, LENOVO));

        //联想 后台耗电优化
        Intent lenovoGodIntent = new Intent();
        lenovoGodIntent.setComponent(new ComponentName("com.lenovo.powersetting", "com.lenovo.powersetting.ui.Settings$HighPowerApplicationsActivity"));
        mIntentWrapperList.add(new IntentWrapper(lenovoGodIntent, LENOVO_GOD));

        //金立 应用自启
        Intent gioneeIntent = new Intent();
        gioneeIntent.setComponent(new ComponentName("com.gionee.softmanager", "com.gionee.softmanager.MainActivity"));
        mIntentWrapperList.add(new IntentWrapper(gioneeIntent, GIONEE));

        //酷派 自启动管理
        Intent coolpadIntent = new Intent();
        coolpadIntent.setComponent(new ComponentName("com.yulong.android.security", "com.yulong.android.seccenter.tabbarmain"));
        mIntentWrapperList.add(new IntentWrapper(coolpadIntent, COOLPAD));

        //中兴 自启管理
        Intent zteIntent = new Intent();
        zteIntent.setComponent(new ComponentName("com.zte.heartyservice", "com.zte.heartyservice.autorun.AppAutoRunManager"));
        mIntentWrapperList.add(new IntentWrapper(zteIntent, ZTE));

        //中兴 锁屏加速受保护应用
        Intent zteGodIntent = new Intent();
        zteGodIntent.setComponent(new ComponentName("com.zte.heartyservice", "com.zte.heartyservice.setting.ClearAppSettingsActivity"));
        mIntentWrapperList.add(new IntentWrapper(zteGodIntent, ZTE_GOD));
    }

    private String getApplicationName() {
        if (sApplicationName == null) {
            PackageManager pm;
            ApplicationInfo ai;
            try {
                pm = DqApp.sContext.getPackageManager();
                ai = pm.getApplicationInfo(DqApp.sContext.getPackageName(), 0);
                sApplicationName = pm.getApplicationLabel(ai).toString();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                sApplicationName = DqApp.sContext.getPackageName();
            }
        }
        return sApplicationName;
    }

    private String desc;
    /**
     * 处理自启动白名单.
     *
     */
    public void setAutoStart(@NonNull Activity activity) {
        if(mIntentWrapperList == null || mIntentWrapperList.size() > 0) {
            getIntentWrapperList();
        }
        mGuideDialog = new CommDialog(activity);
        for (IntentWrapper iw : mIntentWrapperList) {
            //如果本机上没有能处理这个Intent的Activity，说明不是对应的机型，直接忽略进入下一次循环。
            if (!iw.doesActivityExists()) continue;
            switch (iw.type) {
                case DOZE:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
                        if (pm.isIgnoringBatteryOptimizations(activity.getPackageName())) break;

                        desc = "需要 " + getApplicationName() + " 加入到电池优化的忽略名单。\n\n" +
                                "请点击『确定』，在弹出的『忽略电池优化』对话框中，选择『是』。";
                    }
                    setHintMessage(activity, desc, iw);
                    break;
                case HUAWEI:
                    desc = "需要 " + getApplicationName() + " 加入到自启动白名单。\n\n" +
                            "请点击『确定』，在弹出的『自启管理』中，将 " + getApplicationName() + " 对应的开关打开。";
                    setHintMessage(activity, desc, iw);
                    break;
                case SAMSUNG:
                    desc = "请点击『确定』，在弹出的『智能管理器』中，点击『内存』，选择『自启动应用程序』选项卡，将 " + getApplicationName() + " 对应的开关打开。";
                    setHintMessage(activity, desc, iw);
                    break;
                case MEIZU:
                    desc = "请点击『确定』，在弹出的应用信息界面中，将『后台管理』选项更改为『保持后台运行』。";
                    setHintMessage(activity, desc, iw);
                    break;
                case ZTE:
                case LETV:
                case XIAOMI:
                case OPPO_OLD:
                    desc = "需要 " + getApplicationName() + " 加入到自启动白名单。\n\n" +
                            "请点击『确定』，在弹出的『权限管理』中，打开『自启动管理』，将 " + getApplicationName() + " 对应的开关打开。";
                    setHintMessage(activity, desc, iw);
                    break;
                case OPPO:
                    desc = "需要 " + getApplicationName() + " 加入到自启动白名单。\n\n" +
                            "请点击『确定』，在弹出的『权限隐私』中，打开『自启动管理』，将 " + getApplicationName() + " 对应的开关打开。";
                    setHintMessage(activity, desc, iw);
                    break;
                case COOLPAD:
                    desc = "需要 " + getApplicationName() + " 加入到自启动白名单。\n\n" +
                            "请点击『确定』，在弹出的『酷管家』中，找到『软件管理』->『自启动管理』，取消勾选 "
                            + getApplicationName() + "，将 " + getApplicationName() + " 的状态改为『已允许』。";
                    break;
                case VIVO:
                    desc = "需要 " + getApplicationName() + " 加入到自启动白名单。\n\n" +
                            "请点击『确定』，在弹出的『权限管理』中，将 " + getApplicationName() + " 对应的开关打开。";
                    setHintMessage(activity, desc, iw);
                    break;
                case GIONEE:
                    desc = "需要 " + getApplicationName() + " 加入应用自启和绿色后台白名单。\n\n" +
                            "请点击『确定』，在弹出的『系统管家』中，分别找到『应用管理』->『应用自启』和『绿色后台』->『清理白名单』，将 " + getApplicationName() + " 添加到白名单。";
                    setHintMessage(activity, desc, iw);
                    break;
                case LENOVO:
                    desc = "需要 " + getApplicationName() + " 的后台自启、后台 GPS 和后台运行。\n\n" +
                            "请点击『确定』，在弹出的『后台管理』中，分别找到『后台自启』、『后台 GPS』和『后台运行』，将 " + getApplicationName() + " 对应的开关打开。";
                    setHintMessage(activity, desc, iw);
                    break;
            }
        }
    }
    /**
     * 设置后台电量优化
     *
     */

    public void setBackstageOptimization(@NonNull Activity activity) {
        if(mIntentWrapperList == null || mIntentWrapperList.size() > 0) {
            getIntentWrapperList();
        }
        mGuideDialog = new CommDialog(activity);
        for (final IntentWrapper iw : mIntentWrapperList) {
            //如果本机上没有能处理这个Intent的Activity，说明不是对应的机型，直接忽略进入下一次循环。
            if (!iw.doesActivityExists()) continue;
            switch (iw.type) {
                case ZTE_GOD:
                case HUAWEI_GOD:
                    desc = "需要 " + getApplicationName() + " 加入到锁屏清理白名单。\n\n" +
                            "请点击『确定』，在弹出的『锁屏清理』列表中，将 " + getApplicationName() + " 对应的开关打开。";
                    setHintMessage(activity, desc, iw);
                    break;
                case XIAOMI_GOD:
                    desc = "请点击『确定』，在弹出的 " + getApplicationName() + " 神隐模式设置中，选择『无限制』，然后选择『允许定位』。";
                    setHintMessage(activity, desc, iw);
                    break;
                case SAMSUNG_GOD:
                    desc = "请点击『确定』，在弹出的『电池』页面中，点击『未监视的应用程序』->『添加应用程序』，勾选 " + getApplicationName() + "，然后点击『完成』。";
                    setHintMessage(activity, desc, iw);
                    break;
                case MEIZU_GOD:
                    desc = "请点击『确定』，在弹出的『待机耗电管理』中，将 " + getApplicationName() + " 对应的开关打开。";
                    setHintMessage(activity, desc, iw);
                    break;
                case OPPO_DL:
                    desc = "请点击『确定』，在弹出的『电池』中，打开『耗电保护』，将 " + getApplicationName() + " 对应的开关关闭。";
                    setHintMessage(activity, desc, iw);
                    break;
                case VIVO_GOD:
                    desc = "请点击『确定』，在弹出的『后台高耗电』中，将 " + getApplicationName() + " 对应的开关打开。";
                    setHintMessage(activity, desc, iw);
                    break;
                case LETV_GOD:
                    desc = "需要禁止 " + getApplicationName() + " 被自动清理。\n\n" +
                            "请点击『确定』，在弹出的『应用保护』中，将 " + getApplicationName() + " 对应的开关关闭。";
                    setHintMessage(activity, desc, iw);
                    break;
                case LENOVO_GOD:
                    desc = "需要关闭 " + getApplicationName() + " 的后台耗电优化。\n\n" +
                            "请点击『确定』，在弹出的『后台耗电优化』中，将 " + getApplicationName() + " 对应的开关关闭。";
                    setHintMessage(activity, desc, iw);
                    break;
            }
        }
    }

    private void setHintMessage(Activity activity, String desc, IntentWrapper iw) {
        mGuideDialog.setTitle("需要允许 " + getApplicationName() + " 应用在后台运行");
        mGuideDialog.setDesc(desc);
        mGuideDialog.setCancelTxt(activity.getString(R.string.cancel));
        mGuideDialog.setOkTxt(activity.getString(R.string.determine));
        mGuideDialog.setOkTxtColor(activity.getResources().getColor(R.color.text_blue));
        mGuideDialog.setWidth();

        mGuideDialog.setCanceledOnTouchOutside(false);
        mGuideDialog.setCancelable(false);
        mGuideDialog.show();

        mGuideDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                startActivitySafely(iw.intent);
            }
        });
    }

    /**
     * 判断本机上是否有能处理当前Intent的Activity
     */
    private boolean doesActivityExists() {
        PackageManager pm = DqApp.sContext.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list != null && list.size() > 0;
    }

    /**
     * 安全地启动一个Activity
     */
    private void startActivitySafely(Intent intent) {
        CNLog.e("TAG", "intent.getAction() : " + intent.getAction());
        CNLog.e("TAG", "intent.getComponent() : " + intent.getComponent());
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            DqApp.sContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            //抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            DqApp.sContext.startActivity(intent);
        }
    }


    public void onDestroy() {
        if (null != mDialog) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
