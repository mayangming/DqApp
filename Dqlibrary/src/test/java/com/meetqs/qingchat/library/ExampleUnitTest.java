package com.meetqs.qingchat.library;

import com.da.library.utils.BigDecimalUtils;
import com.da.library.utils.NetToolsUtil;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);

        String result = BigDecimalUtils.penny2Dollar(435).toPlainString();

        System.out.println("计算结果:"+result);
        System.out.println("获取IP地址:"+ NetToolsUtil.getLocalHostIp());

    }
}