package com.ly.p2pmon.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.ly.p2pmon.ui.LoadingPage;
import com.ly.p2pmon.utils.UIUtils;

import butterknife.ButterKnife;

/**
 * Created by 12758 on 2016/5/9.
 */
public abstract class BaseFragment extends Fragment {

    private LoadingPage loadingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (loadingPage==null){
            loadingPage = new LoadingPage(container.getContext()) {
                @Override
                public int layoutId() {
                    return getLayoutId();
                }

                @Override
                protected RequestParams params() {
                    return getParams();
                }

                @Override
                protected void OnSuccess(ResultState resultState,View successView) {
                    ButterKnife.bind(BaseFragment.this,successView);
                    initTitle();
                    initData(resultState.getContent());
                }

                @Override
                protected String url() {
                    return getUrl();
                }
            };
        }else{
            ((ViewGroup) loadingPage.getParent()).removeView(loadingPage);
        }


        /*View view = UIUtils.getXmlView(getLayoutId());
        ButterKnife.bind(this, view);
        initTitle();
        initData();*/
        return loadingPage;
    }

    public void show(){
        loadingPage.show();
    }

    protected abstract RequestParams getParams();

    protected abstract String getUrl();

    protected abstract void initData(String content);

    protected abstract void initTitle();

    public abstract int getLayoutId();

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UIUtils.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
            }
        },1000);

    }


}
