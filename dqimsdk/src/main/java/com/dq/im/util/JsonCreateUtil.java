package com.dq.im.util;

import android.content.Context;

import com.dq.im.SystemMsgUtil;
import com.dq.im.bean.MessageReceiptBean;
import com.dq.im.model.HeartbeatModel;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * JSON创建工具类
 */
public class JsonCreateUtil {
    private static Gson gson = new Gson();
    public static String getSystemDeviceJson(String userId){
        HeartbeatModel heartbeatModel = new HeartbeatModel();
        heartbeatModel.setDevice_id(SystemMsgUtil.getAndroidId());
        heartbeatModel.setDevice_model(SystemMsgUtil.getSystemModel());
        heartbeatModel.setDevice_version(SystemMsgUtil.getSystemVersion());
        heartbeatModel.setUser_id(userId);
//        heartbeatModel.setDevice_phone_iccd(SystemMsgUtil.getPhoneCardICCD());
        return gson.toJson(heartbeatModel);
    }

    /**
     * 创建消息回执的json
     * @param userId
     * @return
     */
    public static String getMessageReceiptJson(String msgIdServer){
        MessageReceiptBean messageReceiptBean = new MessageReceiptBean();
        messageReceiptBean.setMsgIdServer(msgIdServer);
//        heartbeatModel.setDevice_phone_iccd(SystemMsgUtil.getPhoneCardICCD());
        return gson.toJson(messageReceiptBean);
    }

    //获取Android唯一设备号: https://blog.csdn.net/zhangjin12312/article/details/64123602
    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String id(Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }

}