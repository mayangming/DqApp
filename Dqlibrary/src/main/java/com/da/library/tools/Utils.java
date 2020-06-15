package com.da.library.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/9/11 10:24.
 * @description: todo ...
 */
public class Utils {
    //限制点击次数
    private static long lastClickTime;

    /**
     * 转换时间
     *
     * @param duration
     * @return
     */
    public static String getPlayTime(String duration) {
        if (TextUtils.isEmpty(duration)) {
            return "0:00";
        }
        float result = Float.parseFloat(duration);
        int time = (int) Math.floor(result);
        int hh = time / 3600;
        int mm = time % 3600 / 60;
        int ss = time % 3600 % 60;
        String strTemp = null;
        if (time >= 3600) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%2d:%02d", mm, ss);
        }
        return strTemp;
    }

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


    /**
     * 获取视频第一帧
     * @param path
     * @return
     */
    public static Bitmap getFirstFrame(String path){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        if(path.contains("http://") || path.contains("https://")){//path是网络地址
            mmr.setDataSource(path, new HashMap<String, String>());//设置数据源为该文件对象指定的绝对路径
        }else{
            mmr.setDataSource(path);//设置数据源为该文件对象指定的绝对路径
        }

        Bitmap tmpBmp = mmr.getFrameAtTime(0);//获得视频第一帧的Bitmap对象
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tmpBmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        return tmpBmp;
    }

    /**
     * 获取视频旋转角度
     * @param path
     * @return
     */
    public static String getVideoRotate(String path) {
        if (TextUtils.isEmpty(path)) {
            return "90";
        }
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        String rotation = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        return rotation;
    }

    //隐藏软键盘
    public static void hideSoftInput(Context mContext, EditText editText) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    // 隐藏软键盘
    public static void toggleSoftInput(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static String format(String money) {
        DecimalFormat df = new DecimalFormat("######0.00");
        double str = Double.parseDouble(money);
        return df.format(str);
    }

    public static void setEditText(final EditText editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    return true;
                }
                return false;
            }
        });
    }

    public static String formatUnread(int unread) {
        String target = "";
        if (unread <= 0) {
            target = "";
        } else if (unread < 99) {
            target = "" + unread;
        } else {
            target = "99+";
        }
        return target;
    }

    public static boolean isInstallApp(Context mContext, String packageName) {
        PackageManager manager = mContext.getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo pI = pkgList.get(i);
            if (pI.packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

//
//    /**
//     * 规定时间内是否可以点击
//     *
//     * @param unique 自定义唯一标示
//     */
//    public static boolean isEnableClick(String unique, String tips, int threshold) {
////        EBSharedPrefManager sharedPrefManager = BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);
////        EBSharedPrefApp sharedPrefApp = sharedPrefManager.getKDPreferenceApp();
////
////        long time = System.currentTimeMillis();
////        long lastTime = sharedPrefApp.getLong(EBSharedPrefApp.POKE_ONE_POKE + unique, 0);
////        int second = (int) ((time - lastTime) / 1000);
////        if (second < threshold) {
////            String targetTips = String.format(tips, "" + (threshold - second));
////            CNToastUtil.makeText(EBApplication.sContext, targetTips);
////            return false;
////        }
//        return true;
//    }
//
//    /**
//     * 保存戳一下时间戳
//     *
//     * @param unique
//     */
//    public static void savePokeTime(String unique) {
////        long time = System.currentTimeMillis();
////        QCSharedPrefManager sharedPrefManager = BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);
////        EBSharedPrefApp sharedPrefApp = sharedPrefManager.getKDPreferenceApp();
////        sharedPrefApp.saveLong(EBSharedPrefApp.POKE_ONE_POKE + unique, time);
//    }

}
