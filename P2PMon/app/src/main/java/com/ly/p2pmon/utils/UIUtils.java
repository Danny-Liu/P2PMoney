package com.ly.p2pmon.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.view.View;
import android.widget.Toast;

import com.ly.p2pmon.common.MyApplication;


/**
 * Created by 12758 on 2016/5/7.
 */
public class UIUtils {

    public static Context getContext() {
        return MyApplication.context;
    }

    public static Handler getMainThreadHandler() {
        return MyApplication.handler;
    }

    public static int getColor(int colorId) {
        return getContext().getResources().getColor(colorId);
    }

    public static View getXmlView(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    public static String[] getStringArr(int arrId) {
        return getContext().getResources().getStringArray(arrId);
    }

    /**
     * 1dp---1px;
     * 1dp---0.75px;
     * 1dp---0.5px;
     * ....
     *
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }


    public static int px2dp(int px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);
    }

    /**
     * 保证线程运行在主线程中
     *
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable) {
        if (isInMainThread()) {
            runnable.run();
        } else {
            getMainThreadHandler().post(runnable);
        }

    }

    private static boolean isInMainThread() {
        int tid = Process.myTid();//当前线程id
        if (tid == MyApplication.mainThreadId) {
            return true;
        }
        return false;
    }

    public static void Toa(String text, boolean isLong) {
        Toast.makeText(getContext(), text, isLong == false ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

}
