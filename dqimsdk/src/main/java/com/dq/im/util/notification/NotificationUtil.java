package com.dq.im.util.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.dq.im.R;

import java.util.ArrayList;
import java.util.List;

/** 通知工具类 */
public class NotificationUtil{
    private static NotificationUtil notificationUtil = new NotificationUtil();
    private int notificationId = 1;
    public static NotificationUtil getInstance(){
        return notificationUtil;
    }

    private NotificationUtil(){

    }

    /**
     * momoren默认的通知消息
     * @param content 消息内容
     */
    public void showDefaultNotification(String content,Context context){
        NotificationUtil.NotificationUtilBuild notificationUtilBuild = notificationUtil.createBuild();
        notificationUtilBuild.context = context;
        notificationUtilBuild.title = "斗圈";
        notificationUtilBuild.content = content;
        notificationUtil.showNotification(notificationUtilBuild);
    }

    public void showNotification(NotificationUtilBuild build) {
        if (null == build){
            throw new RuntimeException("NotificationUtilBuild 不能为空");
        }

        if (null == build.context){
            throw new RuntimeException("NotificationUtil中Context 不能为空");
        }

        for (NotificationFilter filter : build.filterList){
            Log.e("YM","拦截器:"+filter.filter());
            if (filter.filter()){
                return;
            }
        }

        NotificationManager notificationManager =
                (NotificationManager) build.context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;//5.0以上会显示横幅
            //注意Name和description不能为null或者""
            NotificationChannel mChannel = new NotificationChannel(build.channelId, build.channelName, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(build.description);
            notificationManager.createNotificationChannel(mChannel);
        }
        PendingIntent notifyPendingIntent = null;
        if (null != build.intent){
//            Intent notifyIntent = new Intent(build.context, MainActivity.class);
            notifyPendingIntent =  PendingIntent.getActivity(
                            build.context,
                            1,
//                            notifyIntent,
                            build.intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );
        }

        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder( build.context, build.channelId);
        notificationCompatBuilder.setAutoCancel(false);
        notificationCompatBuilder.setContentTitle(build.title);
        notificationCompatBuilder.setContentText(build.content);
        notificationCompatBuilder.setSmallIcon(R.mipmap.qc_logo);
        if (null != notifyPendingIntent){
            notificationCompatBuilder.setContentIntent(notifyPendingIntent);
        }
        notificationCompatBuilder.setPriority(NotificationManagerCompat.IMPORTANCE_HIGH);
        notificationCompatBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
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
//        public PendingIntent pendingIntent;//点击跳转操作
        public Intent intent;//点击跳转操作
        @NonNull
        public Context context;
        public String title = "";
        public String content = "";
        public String time = "";
        public String channelId = "chat";
        public String channelName = "聊天";
        public String description = "聊天通知";
        public @DrawableRes
        int idRed = R.mipmap.qc_logo;
//        public NotificationFilter filter = new DefaultNotificationFilter();//拦截器
        private List<NotificationFilter> filterList = new ArrayList<>();//拦截器集合
        public void addNotificationFilter(NotificationFilter filter){
            filterList.add(filter);
        }

        public NotificationUtilBuild() {
            NotificationUtil.this.notificationId = NotificationUtil.this.notificationId + 1;
            notificationId = NotificationUtil.this.notificationId;
            Log.e("Ym","notification:"+notificationId);
        }
    }

}