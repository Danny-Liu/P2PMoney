package com.ly.p2pmon.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ly.p2pmon.R;
import com.ly.p2pmon.ui.randomLayout.StellarMap;
import com.ly.p2pmon.utils.UIUtils;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 12758 on 2016/5/15.
 * 飞入飞出效果
 */
public class ProductRecommendFragment extends Fragment {

    @Bind(R.id.stellarMap)
    StellarMap stellarMap;
    private String[] datas = new String[]{"超级新手计划", "乐享活系列90天计划", "钱包计划", "30天理财计划(加息2%)", "90天理财计划(加息5%)", "180天理财计划(加息10%)",
            "林业局投资商业经营", "中学老师购买车辆", "屌丝下海经商计划", "新西游影视拍摄投资", "Java培训老师自己周转", "养猪场扩大经营",
            "旅游公司扩大规模", "阿里巴巴洗钱计划", "铁路局回款计划", "高级白领赢取白富美投资计划"
    };
    private Random random;
    private String[] one = new String[8];
    private String[] two = new String[8];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = UIUtils.getXmlView(R.layout.fragment_product_recommend);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        for (int i = 0; i < 8; i++) {
            one[i] = datas[i];
        }
        for (int i = 0; i < 8; i++) {
            two[i] = datas[i + 8];
        }

        //设置数据适配器
        stellarMap.setAdapter(new MyAdapter());
        //设置内边距
        int padding = UIUtils.dp2px(5);
        stellarMap.setInnerPadding(padding, padding, padding, padding);
        //设置组的搭配数量  总共16个数据
        stellarMap.setRegularity(7, 9);
        //设置哪一组开始做动画动作
        stellarMap.setGroup(0, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class MyAdapter implements StellarMap.Adapter {
        @Override
        public int getGroupCount() {//有几组数据
            return 2;
        }

        @Override
        public int getCount(int group) {//组对应的条目
            return 8;
        }

        @Override
        public View getView(int group, int position, View convertView) {//每组对应的View对象
            TextView tv = new TextView(getActivity());

            random = new Random();
            int r = random.nextInt(220);
            int g = random.nextInt(220);
            int b = random.nextInt(220);
            int rgb = Color.rgb(r, g, b);
            tv.setTextColor(rgb);
            tv.setTextSize(UIUtils.dp2px(5) + random.nextInt(8));
            if(group ==0){
                tv.setText(one[position]);
            }else if(group == 1){
                tv.setText(two[position]);
            }

            return tv;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {//下一组将要出现的动画  只要小于group的count就行了
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 1;
        }
    }
}
