package com.ly.p2pmon.common;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by 12758 on 2016/5/15.
 * getView的操作都交给baseholder去做
 */
public abstract class BaseHolder<T> {

    private View rootView;
    private T mData;
    public T getData(){
        return mData;
    }

    public BaseHolder() {
        this.rootView = initView();
        this.rootView.setTag(this);
        ButterKnife.bind(this,rootView);
    }

    protected abstract View initView();
    public View getRootView(){
        return rootView;
    }

    public void setData(T t){
        this.mData = t;
        refreashView();
    }

    protected abstract void refreashView();
}
