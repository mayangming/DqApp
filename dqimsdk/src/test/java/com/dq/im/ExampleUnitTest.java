package com.dq.im;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
        int code = 0x1f603;
        char[] chars = Character.toChars(code);
        String key = Character.toString(chars[0]);
        System.out.println("key:"+key);
        for (char c : chars){
            System.out.println("char:"+c);
        }
    }
}