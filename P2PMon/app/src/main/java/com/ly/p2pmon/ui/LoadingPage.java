package com.ly.p2pmon.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ly.p2pmon.R;
import com.ly.p2pmon.utils.UIUtils;

/**
 * Created by 12758 on 2016/5/10.
 */
public abstract class LoadingPage extends FrameLayout {


    private Context mContext;
    AsyncHttpClient client = new AsyncHttpClient();
    //四种加载状态  正在加载，错误，数据为空，成功
    private static final int PAGE_LOADING_STATE = 1;

    private static final int PAGE_ERROR_STATE = 2;

    private static final int PAGE_EMPTY_STATE = 3;

    private static final int PAGE_SUCCESS_STATE = 4;

    //当前状态
    private int PAGE_CURRENT_STATE = 1;

    private View loadingView;
    private View errorView;
    private View emptyView;
    private View successView;
    private LayoutParams lp;

    private ResultState resultState = null;



    public LoadingPage(Context context) {
        this(context, null);
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (loadingView == null) {
            loadingView = UIUtils.getXmlView(R.layout.page_loading);
            addView(loadingView, lp);
        }
        if (errorView == null) {
            errorView = UIUtils.getXmlView(R.layout.page_error);
            addView(errorView, lp);
        }
        if (emptyView == null) {
            emptyView = UIUtils.getXmlView(R.layout.page_empty);
            addView(emptyView, lp);
        }
        //因为successView每个都不同  所以要根据layout的id来显示布局
        if (successView == null) {
            /*successView = UIUtils.getXmlView(layoutId());*/
            successView = inflate(mContext,layoutId(),null);
            //记得addView   不然空指针异常
            addView(successView, lp);
        }


        showSafePage();//要在主线程显示  所以safe
    }

    private void showSafePage() {
        UIUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                showPage();
            }
        });
    }

    private void showPage() {

        loadingView.setVisibility(PAGE_CURRENT_STATE == PAGE_LOADING_STATE ? VISIBLE : GONE);
        errorView.setVisibility(PAGE_CURRENT_STATE == PAGE_ERROR_STATE ? VISIBLE : GONE);
        emptyView.setVisibility(PAGE_CURRENT_STATE == PAGE_EMPTY_STATE ? VISIBLE : GONE);

        successView.setVisibility(PAGE_CURRENT_STATE == PAGE_SUCCESS_STATE ? VISIBLE : GONE);
    }




    public abstract int layoutId();

    public void show() {

        //状态归位
        if(PAGE_CURRENT_STATE!=PAGE_LOADING_STATE){
            PAGE_CURRENT_STATE = PAGE_LOADING_STATE;
        }
        //处理不需要发送网络请求来决定界面的情况
        String url = url();
        if(TextUtils.isEmpty(url)){//url是空的话   就不需要发送请求了
            resultState = ResultState.SUCCESS;
            resultState.setContent("");
            loadPage();
        }else{
            client.get(url,  params(), new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String content) {
                    System.out.println("content="+content);
                    if (TextUtils.isEmpty(content)) {
                        resultState = ResultState.EMPTY;
                        resultState.setContent("");
                    } else {
                        resultState = ResultState.SUCCESS;
                        resultState.setContent(content);
                    }
                    loadPage();
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    super.onFailure(error, content);
                    resultState = ResultState.ERROR;
                    resultState.setContent("");
                    loadPage();
                }
            });
        }
    }

    protected abstract RequestParams params();


    /**
     * 根据状态  加载界面
     */
    private void loadPage() {
        switch (resultState) {
            case ERROR:
                PAGE_CURRENT_STATE = 2;
                break;
            case EMPTY:
                PAGE_CURRENT_STATE = 3;
                break;
            case SUCCESS:
                PAGE_CURRENT_STATE = 4;
                break;
        }
        showSafePage();
        if(PAGE_CURRENT_STATE == 4){
            OnSuccess(resultState,successView);
        }
    }

    protected abstract void OnSuccess(ResultState resultState,View successView);


    protected abstract String url();

    //使用枚举类   把请求结果封装到枚举中，（枚举：可以限定结果类型，还可以像普通类一样提供供外界访问的方法与字段）,所以我们可以把枚举
    //对象暴露出去，然后fragment就可以得到请求的结果   根据请求的结果进行处理


    public enum ResultState {
        ERROR(2), EMPTY(3), SUCCESS(4);

        private int state;
        private String content;

        ResultState(int state) {
            this.state = state;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }


}
