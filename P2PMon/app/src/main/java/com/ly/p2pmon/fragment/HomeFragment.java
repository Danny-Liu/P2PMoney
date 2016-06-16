package com.ly.p2pmon.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ly.p2pmon.R;
import com.ly.p2pmon.bean.Image;
import com.ly.p2pmon.bean.Index;
import com.ly.p2pmon.bean.Product;
import com.ly.p2pmon.common.AppNetConfig;
import com.ly.p2pmon.common.BaseFragment;
import com.ly.p2pmon.ui.RoundProgress;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import butterknife.Bind;

/**
 * Created by 12758 on 2016/5/7.
 */
public class HomeFragment extends BaseFragment {

    AsyncHttpClient client = new AsyncHttpClient();
    @Bind(R.id.title_left)
    ImageView titleLeft;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.title_right)
    ImageView titleRight;
    @Bind(R.id.vp_barner)
    ViewPager vpBarner;
    @Bind(R.id.circle_barner)
    CirclePageIndicator circleBarner;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.p_progresss)
    RoundProgress pProgresss;
    @Bind(R.id.p_yearlv)
    TextView pYearlv;
    @Bind(R.id.button1)
    Button button1;
    Index index;

    @Override
    protected RequestParams getParams() {
        return new RequestParams();
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.INDEX;
    }

    private int totalProgress;

    @Override
    protected void initData(String content) {
        index = new Index();
        client.post(AppNetConfig.INDEX, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                // System.out.println("content="+content);
                JSONObject jsonObject = JSONObject.parseObject(content);
                String proInfo = jsonObject.getString("proInfo");
                Product product = JSONObject.parseObject(proInfo, Product.class);
                String imageArr = jsonObject.getString("imageArr");
                List<Image> images = JSONObject.parseArray(imageArr, Image.class);
                index.imageList = images;
                index.product = product;

                //适配数据
                vpBarner.setAdapter(new MyAdatper());
                //把viewpager交给Indicator
                circleBarner.setViewPager(vpBarner);
                totalProgress = Integer.parseInt(index.product.progress);
                new Thread(runnable).start();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("totalProgress=" + totalProgress);

            int tempProgress = 0;
            try {
                while (tempProgress <= totalProgress) {
                    pProgresss.setProgress(tempProgress);
                    tempProgress++;
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private class MyAdatper extends PagerAdapter {

        @Override
        public int getCount() {
            return index.imageList == null ? 0 : index.imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String imaurl = index.imageList.get(position).IMAURL;
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getActivity()).load(imaurl).into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    protected void initTitle() {
        titleLeft.setVisibility(View.INVISIBLE);
        titleRight.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


}
