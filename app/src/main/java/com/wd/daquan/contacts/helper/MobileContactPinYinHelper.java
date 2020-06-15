package com.wd.daquan.contacts.helper;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wd.daquan.contacts.bean.MobileContactBean;
import com.wd.daquan.contacts.utils.CharacterParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 方志
 * @Time: 2018/9/17 16:58
 * @Description:
 */
public class MobileContactPinYinHelper {
    private static MobileContactPinYinHelper instance = null;

    private MobileContactPinYinHelper() {
    }

    public static MobileContactPinYinHelper getInstance() {
        synchronized (MobileContactPinYinHelper.class) {
            if (instance == null) {
                instance = new MobileContactPinYinHelper();
            }
        }
        return instance;
    }

    public List<MobileContactBean> setPinYin(@NonNull List<MobileContactBean> list) {
        List<MobileContactBean> contactList = new ArrayList<>();
        for (MobileContactBean contact : list) {
            String pinyin;
            CharacterParser parser = new CharacterParser();
            pinyin = parser.getSpelling(contact.userName);

            if (parser.isHaveInterpunction) {
                pinyin = "00000";
            }

            String letter = TextUtils.isEmpty(pinyin) ? "" : pinyin.substring(0, 1).toUpperCase();
//            String sortString;
//            if (!TextUtils.isEmpty(pinyin)) {
//                sortString = pinyin.substring(0, 1).toUpperCase();
//            } else {
//                sortString = "#";
//            }
            // 正则表达式，判断首字母是否是英文字母
//            if (sortString.matches("[A-Z]")) {
//                contact.pinYin = sortString;
//            } else {
//                contact.pinYin = "#";
//            }
            //正则表达式，判断首字母是否是英文字母/数字/其他
//            if (letter.matches("[0-9]")) {
//                letter = "#";
//            } else
            if (!letter.matches("[A-Z]")) {
                letter = "#";
            }
            contact.pinYin = letter;
            contactList.add(contact);
        }
        return contactList;
    }
}
