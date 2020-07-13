package com.dq.im.third_system;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.dq.im.ImProvider;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * 小米推送通道
 */
public class MiSystemMessageReceiver extends PushMessageReceiver{
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceivePassThroughMessage(context, miPushMessage);
        Log.e("YM",
                "小米消息. " + miPushMessage.toString());
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
        Log.e("YM","注册结果:");
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                MiPushClient.setAlias(ImProvider.context, "YM", null);
//                mRegId = cmdArg1;
//                log = context.getString(R.string.register_success);
                Log.e("YM","注册成功2222222222");
            } else {
//                log = context.getString(R.string.register_fail);
                Log.e("YM","注册失败2222222222");
            }
        } else {
            log = miPushCommandMessage.getReason();
        }

//        Message msg = Message.obtain();
//        msg.obj = log;
//        DemoApplication.getHandler().sendMessage(msg);
    }
}