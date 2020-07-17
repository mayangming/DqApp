package com.dq.im.util;

/**
 * 文件工具类
 */
public class FileUtils {

    /**
     * 获取文件名字的后缀
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName){
        String result = fileName;
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex);
        return result;
    }
}