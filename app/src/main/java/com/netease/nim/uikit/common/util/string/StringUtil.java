package com.netease.nim.uikit.common.util.string;

import android.text.TextUtils;

import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.common.constant.Constants;
import com.da.library.tools.AESHelper;
import com.da.library.tools.SecurityUtils;

import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String getPercentString(float percent) {
        return String.format(Locale.US, "%d%%", (int) (percent * 100));
    }

    /**
     * 删除字符串中的空白符
     *
     * @param content
     * @return String
     */
    public static String removeBlanks(String content) {
        if (content == null) {
            return null;
        }
        StringBuilder buff = new StringBuilder();
        buff.append(content);
        for (int i = buff.length() - 1; i >= 0; i--) {
            if (' ' == buff.charAt(i) || ('\n' == buff.charAt(i)) || ('\t' == buff.charAt(i))
                    || ('\r' == buff.charAt(i))) {
                buff.deleteCharAt(i);
            }
        }
        return buff.toString();
    }

    /**
     * 获取32位uuid
     *
     * @return
     */
    public static String get32UUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean isEmpty(String input) {
        return TextUtils.isEmpty(input);
    }

    /**
     * 生成唯一号
     *
     * @return
     */
    public static String get36UUID() {
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;
    }

    public static String makeMd5(String source) {
        return MD5.getStringMD5(source);
    }

    public static final String filterUCS4(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        if (str.codePointCount(0, str.length()) == str.length()) {
            return str;
        }

        StringBuilder sb = new StringBuilder();

        int index = 0;
        while (index < str.length()) {
            int codePoint = str.codePointAt(index);
            index += Character.charCount(codePoint);
            if (Character.isSupplementaryCodePoint(codePoint)) {
                continue;
            }

            sb.appendCodePoint(codePoint);
        }

        return sb.toString();
    }

    /**
     * counter ASCII character as one, otherwise two
     *
     * @param str
     * @return count
     */
    public static int counterChars(String str) {
        // return
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            int tmp = (int) str.charAt(i);
            if (tmp > 0 && tmp < 127) {
                count += 1;
            } else {
                count += 2;
            }
        }
        return count;
    }


    //是否为合法的手机号码
    public static boolean isMobileNO(String mobiles) {
        if (!isPhoneNumber(mobiles)) {
            return false;
        }

        Pattern p = Pattern
                .compile("^((1[0-9]))\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    //是否为合法的手机号码
    public static boolean isPhoneNumber(String text) {
        if (text == null || text.length() == 0 || !text.matches("\\d{11}")) {
            return false;
        }
        return true;
    }

    public static final String bytesToString(byte[] bytes, String encoding) {
        if (bytes == null) return null;
        try {
            String string = new String(bytes);
            return string;
        } catch (Exception e) {
            return null;
        }
    }
    //设置密码
    public static String getPwdData(String string) {
        String uid = ModuleMgr.getCenterMgr().getUID();
        String stringBuffer = Constants.PAY_MD5 + uid + string;
        return SecurityUtils.get32MD5Str(stringBuffer);
    }
    //密码支付
    public static String getPwdDatas(String string) {
        String uid = ModuleMgr.getCenterMgr().getUID();
        String stringBuffer = Constants.PAY_MD5 + uid + string;
        String data = AESHelper.encryptString(SecurityUtils.get32MD5Str(stringBuffer));
        return data.replaceAll("\n", "");
    }


}
