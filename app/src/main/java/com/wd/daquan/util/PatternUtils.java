package com.wd.daquan.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 */
public class PatternUtils{
    /**
     * 提取字符串中的Emoji符号
     * 用[]进行提取
     * @return 返回提取的内容，包含中括号
     */
    public static List<String> getEmojiContent(String content){
//        Pattern pattern = Pattern.compile("(\\[(.*?)])" );//不包含中括号
        Pattern pattern = Pattern.compile("(\\[(.*?)])" );//包含中括号
        Matcher matcher = pattern.matcher(content);
        List<String> groupList = new ArrayList<>();
        String result = "";
//        if (matcher.find()) {
//
//            result = matcher.group(0);
//        }
//        Log.e("YM","匹配长度:"+matcher.groupCount());
//        Log.e("YM","匹配[0]:"+matcher.group(0));
//        Log.e("YM","匹配[1]:"+matcher.group(2));
//        for (int i = 0; i < matcher.groupCount(); i++){
//            groupList.add(matcher.group(i));
//        }
        while (matcher.find()){
            String temp = matcher.group(0);
            groupList.add(temp);
        }
        return groupList;
    }
    /**
     * 提取字符串中的Emoji符号
     * 用[]进行提取
     * @return 返回提取的内容,不包含中括号
     */
    public static String getEmojiContentCode(String content){
        Pattern pattern = Pattern.compile("\\[(.*?)]" );//不包含中括号
//        Pattern pattern = Pattern.compile("(\\[(.*?)])" );//包含中括号
        Matcher matcher = pattern.matcher(content);
        String result = "";
        if (matcher.find()) {
            result = matcher.group(0);
        }
        return result;
    }
}