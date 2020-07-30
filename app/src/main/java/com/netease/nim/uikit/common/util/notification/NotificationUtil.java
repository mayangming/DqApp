package com.netease.nim.uikit.common.util.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.wd.daquan.DqApp;
import com.wd.daquan.MainActivity;
import com.wd.daquan.R;

import java.util.ArrayList;
import java.util.List;

/** 通知工具类 */
public class NotificationUtil{

    /**
     * momoren默认的通知消息
     * @param content 消息内容
     */
    public static void showDefaultNotification(String content){
        NotificationUtil notificationUtil = new NotificationUtil();
        NotificationUtil.NotificationUtilBuild notificationUtilBuild = notificationUtil.createBuild();
        notificationUtilBuild.context = DqApp.getInstance();
        notificationUtilBuild.title = "斗圈";
        notificationUtilBuild.content = content;
        notificationUtil.showNotification(notificationUtilBuild);
    }

    public void showNotification(NotificationUtilBuild build) {
        if (null == build){
            throw new RuntimeException("NotificationUtilBuild 不能为空");
        }

        for (NotificationFilter filter : build.filterList){
            Log.e("YM","拦截器:"+filter.filter());
            if (filter.filter()){
                return;
            }
        }
        long[] pattern = new long[]{1000, 1000, 1000, 1000, 1000};
        NotificationManager notificationManager =
                (NotificationManager) build.context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;//5.0以上会显示横幅
            //注意Name和description不能为null或者""
            NotificationChannel mChannel = new NotificationChannel(build.channelId, build.channelName, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(build.description);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(pattern);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), Notification.AUDIO_ATTRIBUTES_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent notifyIntent = new Intent(build.context, MainActivity.class);
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        build.context,
                        1,
                        notifyIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder( build.context, build.channelId);
        notificationCompatBuilder.setAutoCancel(true);
        notificationCompatBuilder.setContentTitle(build.title);
        notificationCompatBuilder.setContentText(build.content);
        notificationCompatBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationCompatBuilder.setContentIntent(notifyPendingIntent);
        notificationCompatBuilder.setPriority(NotificationManagerCompat.IMPORTANCE_HIGH);
        notificationCompatBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationCompatBuilder.setDefaults(Notification.DEFAULT_ALL);
        Notification notification = notificationCompatBuilder.build();
        notificationManager.notify(build.notificationId, notification);
    }

    public NotificationUtilBuild createBuild(){
        return new NotificationUtilBuild();
    }

    /** 通知工具类的Build */
    public class NotificationUtilBuild{
        public int notificationId = 1;
        public String notificationTag = "chat";
        public Context context;
        public String title = "";
        public String content = "";
        public String time = "";
        public String channelId = "chat";
        public String channelName = "聊天";
        public String description = "聊天通知";
//        public NotificationFilter filter = new DefaultNotificationFilter();//拦截器
        private List<NotificationFilter> filterList = new ArrayList<>();//拦截器集合
        public void addNotificationFilter(NotificationFilter filter){
            filterList.add(filter);
        }

    }

}