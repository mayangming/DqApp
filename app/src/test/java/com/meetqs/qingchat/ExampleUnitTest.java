package com.meetqs.qingchat;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void debounce() {
//        System.out.println("######debounce#####");
//        Flowable flowable = Flowable.just("李晓明", "张宝庆", "赵无极")
//                .throttleFirst(1, TimeUnit.MILLISECONDS);
//
//        flowable.subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String value) throws Exception {
//                System.out.println("value = " + value);
//            }
//        });

        String head = ChinesePinyinUtil.getPinYinHeadChar("闫");
        System.out.println("head = " + head);
    }
}