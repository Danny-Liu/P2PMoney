package com.ly.p2pmon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ly.p2pmon.R;
import com.ly.p2pmon.bean.Product;
import com.ly.p2pmon.common.AppNetConfig;
import com.ly.p2pmon.common.BaseHolder;
import com.ly.p2pmon.common.MyBaseAdapter2;
import com.ly.p2pmon.ui.RoundProgress;
import com.ly.p2pmon.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 12758 on 2016/5/15.
 */
public class ProductListFragment extends Fragment {

    @Bind(R.id.lv)
    ListView lv;
    private AsyncHttpClient client = new AsyncHttpClient();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = UIUtils.getXmlView(R.layout.fragment_product_list);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        client.post(AppNetConfig.PRODUCT, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject jsonObject = JSONObject.parseObject(content);
                if (jsonObject.getBoolean("success")) {
                    String data = jsonObject.getString("data");
                    List<Product> products = JSONObject.parseArray(data, Product.class);
                    lv.setAdapter(new MyAdapter(products));

                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class MyAdapter extends MyBaseAdapter2<Product> {


        public MyAdapter(List<Product> list) {
            super(list);
        }

        @Override
        public BaseHolder getHolder() {
            return new MyHolder();
        }
    }

    class MyHolder extends BaseHolder<Product> {
        @Bind(R.id.tv_pro_name)
        TextView tvProName;
        @Bind(R.id.p_money)
        TextView pMoney;
        @Bind(R.id.p_yearlv)
        TextView pYearlv;
        @Bind(R.id.p_suodingdays)
        TextView pSuodingdays;
        @Bind(R.id.p_minzouzi)
        TextView pMinzouzi;
        @Bind(R.id.p_progresss)
        RoundProgress pProgresss;

        @Override
        protected View initView() {

            return View.inflate(getActivity(), R.layout.item_product_list, null);
        }

        @Override
        protected void refreashView() {
            Product product = getData();
            //设置数据
            pMinzouzi.setText(product.minTouMoney);
            pMoney.setText(product.money);
            tvProName.setText(product.name);
            pSuodingdays.setText(product.suodingDays);
            pYearlv.setText(product.yearLv);
            pProgresss.setProgress(Integer.parseInt(product.progress));
        }
    }
}
