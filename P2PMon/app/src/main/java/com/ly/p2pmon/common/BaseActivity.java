package com.ly.p2pmon.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.ly.p2pmon.R;
import com.ly.p2pmon.bean.Login;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 12758 on 2016/5/18.
 */
public abstract class BaseActivity extends FragmentActivity {

    protected AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        AppManager.getInstance().addActivity(this);
        initTitle();
        initData();
    }

    public abstract void initData();

    public abstract void initTitle();

    public abstract int getLayoutId();

    /**
     * 关闭当前Activity
     */
    public void closeCurrent(){
        AppManager.getInstance().removeCurrent();
    }

    /**
     * 跳转到目标activity
     * @param clazz
     * @param bundle
     */
    public void gotoActivity(Class clazz,Bundle bundle){
        Intent intent = new Intent(this,clazz);
        if(bundle!=null){
            intent.putExtra("param",bundle);
        }
        startActivity(intent);
    }

    /**
     * 保存登录信息
     * @param login
     */
    public void saveLogin(Login login){
        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UF_ACC", login.UF_ACC);
        editor.putString("UF_AVATAR_URL", login.UF_AVATAR_URL);
        editor.putString("UF_IS_CERT", login.UF_IS_CERT);
        editor.putString("UF_PHONE", login.UF_PHONE);
        editor.commit();
    }


}
