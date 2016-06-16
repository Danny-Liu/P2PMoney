package com.ly.p2pmon.common;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by 12758 on 2016/5/8.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {


    private Context mContext;

    private CrashHandler(){

    }
    private static CrashHandler crashHandler = null;

    public static CrashHandler getInstance(){
        if(crashHandler==null){
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    public void init(Context context){
        this.mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 异常时进入的方法
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //要弹吐司  所以用子线程
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "系统出现异常，即将退出。。。。", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        collectionException(ex);
        try {
            thread.sleep(2000);
            AppManager.getInstance().removeAll();
            Process.killProcess(Process.myPid());
            //关闭虚拟机
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收集异常信息
     * @param ex
     */
    private void collectionException(Throwable ex) {
        final String deviceInfo = Build.DEVICE + Build.VERSION.SDK_INT + Build.MODEL + Build.PRODUCT;

        final String errorInfo = ex.getMessage();
        new Thread(){
            @Override
            public void run() {
                Log.e("CrashHandler", "deviceInfo---" + deviceInfo + ":errorInfo" + errorInfo);
            }
        }.start();
    }
}
