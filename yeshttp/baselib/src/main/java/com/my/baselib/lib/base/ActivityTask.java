package com.my.baselib.lib.base;

import android.app.Activity;

import java.util.Stack;

/**
 *
 */
public class ActivityTask {
    private static ActivityTask activityTask;
    private static Stack<Activity> activityStack;
    private ActivityTask() {

    }
    //退出棧頂Activity
    public void popActivity(Activity activity) {
        if (activity != null) {
            //在從自定義集合中取出當前Activity時，也進行了Activity的關閉操作
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }
    //獲得當前棧頂Activity
    public Activity currentActivity() {
        Activity activity = null;
        if(!activityStack.empty())
            activity= activityStack.lastElement();
        return activity;
    }
    //將當前Activity推入棧中
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    //退出棧中所有Activity
    public void popAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }

    //退出棧中所有Activity
    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    public void popAllActivityExceptOne(Activity a){
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.equals(a)) {
                break;
            }
            popActivity(activity);
        }
    }
    //通过一个单例模式返回当前类的实例对象
    public static ActivityTask getInstance() {
        if (activityTask == null) {
            synchronized (ActivityTask.class) {
                if (activityTask == null) {
                    activityTask = new ActivityTask();
                }
            }
        }
        return activityTask;
    }
}
