package com.wd.daquan.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

import com.da.library.listener.DialogListener;
import com.da.library.utils.TypeConvertUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.login.helper.LogoutHelper;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DELL on 2018/9/10.
 */

public class DqUtils {

    private static final int PERMISSON_REQUESTCODE = 0;


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void setEditText(final EditText editText) {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                return true;
            }
            return false;
        });
    }
    public static void setEditTextInhibitInputSpeChat(EditText editText) {

        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            String sourceText = source.toString();
            String destText = dest.toString();
            int sumText = (destText + sourceText).length();
            String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

            Pattern pattern = Pattern.compile(speChat);

            Matcher matcher = pattern.matcher(source.toString());

            if (matcher.find() || sumText > 11)
                return "";

            else return null;

        };

        editText.setFilters(new InputFilter[]{filter});

    }

    /**
     * 验证手机号码是否合乎规则
     */
    public static boolean validatePhoneNumber(String phoneNumbers, Context context) {
        String phoneNumber = phoneNumbers.replace(" ", "");
        if (!isMobileNO(phoneNumber)) {
            DqToast.showShort(context.getString(R.string.feeedback_right_phone));
            return false;
        }
        return true;
    }
    private static boolean isMobileNO(String mobiles) {
        if (!isPhoneNumber(mobiles)) {
            return false;
        }

        Pattern p = Pattern
                .compile("^((1[0-9]))\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    private static boolean isPhoneNumber(String text) {
        if (text == null || text.length() == 0 || !text.matches("\\d{11}")) {
            return false;
        }
        return true;
    }

    /**
     * 获取应用程序版本号
     *
     */
    public static String getVersion(Context activity) {
        PackageManager packageManager = activity.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
            String version = packageInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取应用程序版本号
     *
     * @param activity
     * @return
     */
    public static int getVersionCode(Context activity) {
        PackageManager packageManager = activity.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 密码的正确性
     */
    public static boolean validate(String password) {
        if (!NetworkUtil.isNetAvailable(DqApp.sContext)) {
            DqToast.showShort(DqApp.sContext.getString(R.string.network_err_please_check_and_try_again));
            return false;
        }
        if (password.length() < 6 || password.length() > 20) {
            DqToast.showShort(DqApp.sContext.getString(R.string.your_password_input_error_please_input_again));
            return false;
        }

        return true;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    public static boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * @since 2.5.0
     */
    public static boolean checkPermissions(Activity context, String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(context, permissions);
        if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(context, needRequestPermissonList.toArray(
                    new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * @since 2.5.0
     */
    public static boolean checkPermissions(Activity context, int requestCode, String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(context, permissions);
        if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(context, needRequestPermissonList.toArray(
                    new String[needRequestPermissonList.size()]), requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    public static List<String> findDeniedPermissions(Activity context, String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(context, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        Environment.getExternalStorageDirectory().exists();
        return needRequestPermissonList;
    }

    @SuppressLint("HardwareIds")
    public static void device() {
        try {
            EBSharedPrefUser userInfoSp = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
            TelephonyManager tm = (TelephonyManager) DqApp.sContext.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(DqApp.sContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                String DEVICE_ID = tm == null ? "" : tm.getDeviceId();

                userInfoSp.saveString(EBSharedPrefUser.DEVICE_ID, DEVICE_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceId() {
        String str = "";
        for (int i = 0; i < 10; i++) {
            Double random = Math.random();
            str = random.toString().substring(2, 11);
        }
        return str;
    }

    /**
     * 验证手机号码是否合乎规则
     */
    public static boolean validatePhoneNumber(String phoneNumbers) {
        String phoneNumber = phoneNumbers.replace(" ", "");
        if (!NetworkUtil.isNetAvailable(DqApp.sContext)) {
            DqToast.showShort(DqApp.sContext.getString(R.string.network_err_please_check_and_try_again));
            return false;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            DqToast.showShort(DqApp.sContext.getString(R.string.mobile_phone_number_cannot_be_empty));
            return false;
        }

        if (!StringUtil.isMobileNO(phoneNumber)) {
            DqToast.showShort(DqApp.sContext.getString(R.string.please_input_the_correct_phone_number));
            return false;
        }
        return true;
    }


    //判断是否连接网络
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }


    @SuppressLint("SetJavaScriptEnabled")
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static void setWebSettings(WebView mVWv, Activity activity){
        WebSettings mWebSettings = mVWv.getSettings();
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setPluginState(WebSettings.PluginState.ON);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebSettings.setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        String appCachePath = activity.getApplicationContext().getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }


    public static void bequit(DataBean s, Context activity) {
        if (s != null) {
            if (KeyValue.Code.TOKEN_ERR == s.result) {
                DqToast.showShort(activity.getResources().getString(R.string.auth_fail));
                LogoutHelper.logout(activity);
            } else {
                DqToast.showShort(s.content);
            }
        }
    }

    public static void quit(Context activity) {
        QCBroadcastManager broadcastManager = new QCBroadcastManager();
        broadcastManager.sendBroadcast(KeyValue.EXIT, KeyValue.ONE_STRING);
    }


    public static String deviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            @SuppressLint("HardwareIds") String DEVICE_ID = tm != null ? tm.getDeviceId() : null;
            return DEVICE_ID;
        }
        return "";
    }
    //限制点击次数
    private static long lastClickTime;
    //按钮点击频率控制
    public static boolean isFastDoubleClick(int duration) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < duration) {//500毫秒内按钮无效，这样可以控制快速点击，自己调整频率
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static int getDates() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    public static void bequitRedPay(Context context, int code, String message) {
        if (KeyValue.Code.TOKEN_ERR == code) {
            DqToast.showShort(DqApp.sContext.getResources().getString(R.string.auth_fail));
            quit(context);
        } else if (code == 107801) {
            DialogUtils.showSettingQCNumDialog(context, "", message, context.getString(R.string.cancel),
                    context.getString(R.string.authority), new DialogListener() {
                        @Override
                        public void onCancel() {}
                        @Override
                        public void onOk() {
                            NavUtils.gotoAlipayAuthActivity(context);
                        }
                    });
        } else {
            if(TextUtils.isEmpty(message)) {
                message = DqApp.getStringById(R.string.request_error_please_again);
            }
            DqToast.showShort(message);
        }
    }


    public static String md5(Object object) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(toByteArray(object));
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException("Huh, MD5 should be supported?", var7);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        byte[] var3 = hash;
        int var4 = hash.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            if((b & 255) < 16) {
                hex.append("0");
            }

            hex.append(Integer.toHexString(b & 255));
        }

        return hex.toString();
    }
    private static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return bytes;
    }

    public static String moneyformat(String money) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(TypeConvertUtil.toDouble(money));
    }
}
