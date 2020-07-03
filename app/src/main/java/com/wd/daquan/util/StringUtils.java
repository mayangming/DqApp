package com.wd.daquan.util;

import java.util.List;

/**
 * 字符串工具
 */
public class StringUtils {


    public static String matcherContent(String content){
//        String emojiContent = PatternUtils.getEmojiContent(content);
//        Log.e("YM","提取的内容:"+emojiContent);
//        if (!TextUtils.isEmpty(emojiContent)){//假如不为空的话
//            String emojiContentCode = emojiContent.substring(1,emojiContent.length()-1);
//            Log.e("YM","转换的表情的内容emojiContentCode:"+translateCode(emojiContentCode));
//            Log.e("YM","转换的表情的内容:"+AndroidEmoji.ensure(emojiContentCode));
//            content = content.replace(emojiContent,translateCode(emojiContentCode));
//        }
//        Log.e("YM","转换过后的内容:"+content);
        List<String> emojiContentList = PatternUtils.getEmojiContent(content);
        for (String emoji : emojiContentList){
            String emojiContentCode = emoji.substring(1,emoji.length()-1);
            content = content.replace(emoji,translateCode(emojiContentCode));
        }
        return content;
    }
    public static String translateCode(String content){
        String key = content;
        try {
            int code = Integer.parseInt(content.substring(2),16);
//            int code = Integer.parseInt(content,16);
            char[] chars = Character.toChars(code);
            key = Character.toString(chars[0]);
            for(int i = 1; i < chars.length; ++i) {
                key = key + Character.toString(chars[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return key;
    }
}