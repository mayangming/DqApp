package com.dq.im.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.dq.im.ImProvider;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 获取手机号的唯一标识ISMI
 * 双卡获取方式
 * https://blog.csdn.net/niyingxunzong/article/details/77188040
 */
public class GetPhoneISMIUtil {
    /**
     * Author: liuqiang
     * Time: 2017-08-14 15:28
     * Description:
     * <p>
     * IMEI 与你的手机是绑定关系 用于区别移动终端设备
     * IMSI 与你的手机卡是绑定关系 用于区别移动用户的有效信息 IMSI是用户的标识。
     * ICCID ICCID是卡的标识，由20位数字组成
     * ICCID只是用来区别SIM卡，不作接入网络的鉴权认证。而IMSI在接入网络的时候，会到运营商的服务器中进行验证。
     * https://github.com/android/platform_frameworks_base/blob/master/telephony/java/android/telephony/TelephonyManager.java
     */
//    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("HardwareIds")
    public static String check() {

        TelephonyManager telephonyManager = (TelephonyManager) ImProvider.context
                .getSystemService(TELEPHONY_SERVICE);// 取得相关系统服务

        String simOperatorName = telephonyManager.getSimOperatorName();
        if (ActivityCompat.checkSelfPermission(ImProvider.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
         String imei = telephonyManager.getDeviceId();       //取出 IMEI
//        String imeiAPI26 = telephonyManager.getImei();       //取出 IMEI 需要 api26以上
        String tel = telephonyManager.getLine1Number();     //取出 MSISDN，很可能为空
        String imsi = telephonyManager.getSubscriberId();     //取出 IMSI
        String iccd = telephonyManager.getSimSerialNumber();  //取出 ICCID

        Log.d("Q_M", "运行商名字--" + simOperatorName);
        Log.d("Q_M", "IMEI--" + imei);
//        Log.d("Q_M", "IMEI_API26--" + imeiAPI26);
        Log.d("Q_M", "IMSI--" + imsi);
        Log.d("Q_M", "ICCID--" + iccd);
        return iccd;
    }
}