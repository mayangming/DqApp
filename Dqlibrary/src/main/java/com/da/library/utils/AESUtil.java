package com.da.library.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Create by LiuYu
 * @Description:
 * @date Create in 2019/12/24/024  16:53
 *
 *
 *
示例参照本类的main方法
 *
 */
public class AESUtil {
    //算法
    private static final String ALGORITHM = "AES";
    //默认的加密算法
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    // 编码
    private static final String ENCODING = "UTF-8";
    // 密匙
    private static final String KEY = "DQSHAREREGISTER5ED2C2B96";
    // 偏移量
    private static final String OFFSET = "0102030405060708";

    /**
     * AES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data,String KEY) throws Exception {
        // 初始化cipher
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //转化成JAVA的密钥格式
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("ASCII"), ALGORITHM);
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(OFFSET.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(data.getBytes(ENCODING));
        //此处使用BASE64做转码。
//        android.util.Base64 base64 = new android.util.Base64()
        String result = Base64.encodeToString(encrypted,Base64.DEFAULT);
        return result;
    }
    /**
     * AES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        // 初始化cipher
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //转化成JAVA的密钥格式
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("ASCII"), ALGORITHM);
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(OFFSET.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(data.getBytes(ENCODING));
        //此处使用BASE64做转码。
//        android.util.Base64 base64 = new android.util.Base64()
        String result = Base64.encodeToString(encrypted,Base64.DEFAULT);
        return result;
    }

    /**
     * AES解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String decrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("ASCII"), ALGORITHM);
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(OFFSET.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//        byte[] buffer = new BASE64Decoder().decodeBuffer(data);
        byte[] buffer = Base64.decode(data,Base64.DEFAULT);
        byte[] encrypted = cipher.doFinal(buffer);
        //此处使用BASE64做转码。
        String result = new String(encrypted, ENCODING);
        return result;
    }

    public static void main(String[] args) {
        String s1 = "492478404554129408";
        String str = "";
        try {
            str = AESUtil.encrypt(s1);
            System.out.println("111111:" + str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = "";
        try {
            result = AESUtil.decrypt(str);
            System.out.println("222222:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
