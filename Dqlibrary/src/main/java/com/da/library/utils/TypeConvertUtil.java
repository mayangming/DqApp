package com.da.library.utils;

/**
 * 类型转换
 * Created by Kind on 2019/3/22.
 */
public class TypeConvertUtil {

    /**
     * {@link #toFloat(String, float)}
     */
    public static Float toFloat(String value) {
        return toFloat(value, 0);
    }

    /**
     * 将String转成Float。
     *
     * @param value    传入值。
     * @param defValue 默认值。
     * @return 如果转换失败，返回defValue。
     */
    public static Float toFloat(String value, float defValue) {
        try {
            return Float.valueOf(value);
        } catch (Exception e) {
        }
        return defValue;
    }

    public static Double toDouble(String value) {
        return toDouble(value, 0.00);
    }


    public static Double toDouble(String value, Double defValue) {
        try {
            return Double.valueOf(value);
        } catch (Exception e) {
        }
        return defValue;
    }


    public static Integer toInteger(String value) {
        return toInteger(value, 0);
    }


    public static Integer toInteger(String value, Integer defValue) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
        }
        return defValue;
    }

}
