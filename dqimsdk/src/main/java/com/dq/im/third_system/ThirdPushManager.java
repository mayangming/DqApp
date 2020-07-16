package com.dq.im.third_system;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * 第三方推送基类
 */
public class ThirdPushManager{
    protected ThirdSystemRegisterResultIml thirdSystemRegisterResultIml;
    private String regId = "";
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    regId = msg.obj.toString();
                    break;
            }
            Log.e("YM","接收消息,是否为null:"+(null == thirdSystemRegisterResultIml));
            if (null != thirdSystemRegisterResultIml){
                thirdSystemRegisterResultIml.registerResult(ThirdSystemType.XIAO_MI, regId);
            }
        }
    };
    public void setThirdSystemRegisterResultIml(ThirdSystemRegisterResultIml thirdSystemRegisterResultIml) {
        Log.e("YM","初始化监听");
        this.thirdSystemRegisterResultIml = thirdSystemRegisterResultIml;
    }

    public void sendMessage(String modeType,String regId){
        Log.e("YM","类型:"+modeType+"----注册信息:"+regId);
        Message message = Message.obtain();
        message.what = 1;
        message.obj = regId;
        handler.sendMessage(message);
    }

}