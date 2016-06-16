package com.ly.p2pmon.common;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by 12758 on 2016/5/7.
 * AppManager设计成单例模式
 * 统一app程序中的所有activity栈管理
 * 添加  删除指定，删除当前，删除所有 求栈大小
 */
public class AppManager {

    private AppManager(){

    }
    private static AppManager appManager = null;

    public static AppManager getInstance(){
        if(appManager==null){
            appManager = new AppManager();
        }
        return appManager;
    }

    private Stack<Activity> activityStack = new Stack<>();

    public void addActivity(Activity activity){
        activityStack.add(activity);
    }

    public void removeActivity(Activity activity){
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity1 = activityStack.get(i);
            if(activity1.getClass().equals(activity.getClass())){
                activity1.finish();
                activityStack.remove(activity1);
                break;
            }
        }
    }

    public void removeCurrent(){
        Activity activity = activityStack.lastElement();
        activity.finish();
        activityStack.remove(activity);
    }

    public void removeAll(){
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            activity.finish();
            activityStack.remove(i);
        }
    }

    public int getSize(){
        return activityStack.size();
    }






}
