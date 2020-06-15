package com.da.library.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: dukangkang
 * @date: 2018/6/1 05:26.
 * @description: todo ...
 */
public class ThreadTools {
   volatile static ExecutorService mExecutorService = null;

   private static void init() {
       if (null == mExecutorService) {
           synchronized (ThreadTools.class) {
               if (null == mExecutorService) {
                   mExecutorService = Executors.newCachedThreadPool();
               }
           }
       }
   }

   public static void newThread(Runnable runnable) {
       if (null == mExecutorService) {
           init();
       }

       mExecutorService.execute(runnable);
   }

}
