package com.ly.p2pmon.common;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

/**
 * Created by 12758 on 2016/5/8.
 */
public class MyApplication extends Application {

    public static Context context = null;
    public static Handler handler = null;
    public static Thread mainThread = null;
    public static int mainThreadId = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread();//因为application就是运行在主线程中的，所以当前线程就是主线程
        mainThreadId = Process.myTid();//同上
        CrashHandler.getInstance().init(context);
    }
}
