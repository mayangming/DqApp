package com.da.library.tools;

import android.text.TextUtils;

import com.da.library.constant.IConstant;


/**
 * @author: fangzhi
 * @date: 2018/9/11 09:18.
 * @description: todo ...
 */
public class AESHelper {


    /**
     * 字符串加密
     * @param content
     * @return
     */
    public static String encryptString(String content) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        try {
            //这里填写密码和iv字符串，注意要确保16位的
            EasyAES ea = new EasyAES(IConstant.AES.KEY,128, IConstant.AES.VALUE);
            return ea.encrypt(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 字符串加密
     * @param content 加密内容
     * @param key 加密key
     * @return
     */
    public static String encryptString(String content,String key) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        try {
            //这里填写密码和iv字符串，注意要确保16位的
            EasyAES ea = new EasyAES(key,128, IConstant.AES.VALUE);
            return ea.encrypt(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 字符串解密
     * @param content
     * @return
     */
    public static String decryptString(String content) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }

        try {
            //这里填写密码和iv字符串，注意要确保16位的
            EasyAES ea = new EasyAES(IConstant.AES.KEY,128, IConstant.AES.VALUE);
            String result = ea.decrypt(content);
            if (TextUtils.isEmpty(result)) {
                return content;
            }
            return result;
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }
}
