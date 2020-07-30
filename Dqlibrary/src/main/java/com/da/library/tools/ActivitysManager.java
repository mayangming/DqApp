package com.da.library.tools;

import android.app.Activity;
import androidx.fragment.app.FragmentActivity;

import java.util.Stack;

import io.reactivex.annotations.NonNull;

/**
 * @Author: 方志
 * @Time: 2018/9/10 13:35
 * @Description: activity栈管理
 */
public class ActivitysManager {

    private static final Stack<FragmentActivity> mActivityStack = new Stack<>();
    private static ActivitysManager mInstance = null;

    private ActivitysManager() {
    }

    public static ActivitysManager getInstance() {
        if (mInstance == null) {
            mInstance = new ActivitysManager();
        }
        return mInstance;
    }


    /**
     * 添加Activity到堆栈
     */
    public void add(@NonNull FragmentActivity activity) {
        mActivityStack.add(activity);
        //Log.e("TAG", "mActivityStack : " + mActivityStack.size() + " , " + mActivityStack.toString());
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public FragmentActivity currentActivity() {
        FragmentActivity activity = mActivityStack.lastElement();
        if (null != activity) {
            return activity;
        }
        return null;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finish() {
        FragmentActivity activity = mActivityStack.lastElement();
        finish(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finish(@NonNull FragmentActivity activity) {
        try {
            if (activity != null) {
                mActivityStack.remove(activity);
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finish(@NonNull Class<?> cls) {

        Stack<Activity> activityStack = new Stack<>();
        try {
            for (Activity activity : mActivityStack) {
                if(activity == null) {
                    continue;
                }

                if (activity.getClass().equals(cls)) {
                    activityStack.add(activity);
                    activity.finish();
                }
            }

            mActivityStack.removeAll(activityStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.e("TAG", "mActivityStack : " + mActivityStack.size() + " , " + mActivityStack.toString());
    }

    /**
     * 结束多个指定类名的Activity
     */
    public void finishMore(@NonNull Class ... classes) {

        if(classes.length == 1) {
            finish(classes[0]);
            return;
        }

        //筛选需要保留的activity
        Stack<Activity> activityStack = new Stack<>();
        try {
            for (Activity activity : mActivityStack) {
                if(activity == null) {
                    continue;
                }
                for (Class aClass : classes) {
                    if(activity.getClass().equals(aClass)) {
                        activityStack.add(activity);
                    }
                }
            }

            //关闭
            for (Activity activity : activityStack){
                if(activity != null) {
                    activity.finish();
                    mActivityStack.remove(activity);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        //Log.e("TAG", "mActivityStack : " + mActivityStack.size());
    }

    /**
     * 结束所有Activity
     */
    public void finishAll() {
        try {
            for (Activity activity : mActivityStack) {
                if(activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mActivityStack.clear();
    }

    /**
     * 结束所有Activity，过滤传入的activity
     */
    public void finishAllFilter(@NonNull Class<?> cls) {
        Stack<Activity> activityStack = new Stack<>();
        try {
            for (Activity activity : mActivityStack) {
                if(null != activity && !activity.getClass().equals(cls)) {
                    activityStack.add(activity);
                    activity.finish();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        mActivityStack.removeAll(activityStack);

        //Log.e("TAG", "mActivityStack : " + mActivityStack.size() + " , " + mActivityStack.toString());
    }

    /**
     * 结束所有Activity，过滤传入的activity集合
     * @param classes activity类名集合
     */
    public void finishAllFilterMore(@NonNull Class ... classes) {

        if(classes.length == 1) {
            finishAllFilter(classes[0]);
            return;
        }

        //筛选需要保留的activity
        Stack<FragmentActivity> activityStack = new Stack<>();
        try {
            for (FragmentActivity activity : mActivityStack) {
                if(activity == null) {
                    continue;
                }
                for (Class aClass : classes) {
                    if(activity.getClass().equals(aClass)) {
                        activityStack.add(activity);
                    }
                }
            }

            //剔除
            mActivityStack.removeAll(activityStack);
            //关闭
            for (FragmentActivity activity : mActivityStack){
                if(activity != null) {
                    activity.finish();
                }
            }

            mActivityStack.clear();
            //保留的传入的activity
            mActivityStack.addAll(activityStack);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Log.e("TAG", "mActivityStack : " + mActivityStack.size() + " , " + mActivityStack.toString());
    }


}
