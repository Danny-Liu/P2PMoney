package com.ly.p2pmon.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12758 on 2016/5/16.
 * 流式布局
 */
public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 求取布局的宽高  (分情况讨论:一种是EXACTLY,一种是AT_MOST)
     *
     *  @param widthMeasureSpec  --宽度的测量规格
     * @param heightMeasureSpec --高度的测量规格
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //AT_MOST下  要自己测量布局的宽高
        int height = 0;
        int width = 0;

        int lineWidth = 0;
        int lineHeight = 0;


        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);//这个方法可以先把子View的布局参数得到
            //测量之后就可以得到子View的宽高了
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            MarginLayoutParams mp = ((MarginLayoutParams) child.getLayoutParams());
            if (lineWidth + childWidth + mp.rightMargin + mp.leftMargin > widthSize) {//换行
               /* 宽度:摆放的所有子view占据宽度最多的一行,作为布局宽度。h
                高度:摆放的所有子view总共占据几行的高度总和。

                宽度=子view最多的那行的宽度=每一个子view的宽度+leftMargin+rightMargin;
                高度=所有行的高度 = 每一行的高度+topMargin+bottomMargin;*/
                width = Math.max(lineWidth, width);
                height += lineHeight;

                //重置一下
                lineWidth = childWidth + mp.leftMargin + mp.rightMargin;
                lineHeight = childHeight + mp.topMargin + mp.bottomMargin;
            } else {
                //不换行:行高--对比获得
                lineWidth += childWidth + mp.leftMargin + mp.rightMargin;
                lineHeight = Math.max(lineHeight, childHeight + mp.topMargin + mp.bottomMargin);
            }
            //记得最后一个还要再进行一次比较   经过计算，要是总共3个的话，那么最后一个没有比较  width和height可能就不准确了
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }
        //Log.e("FlowLayout","width:"+width+",height:"+height);
//        Log.e("FlowLayout","widthSize:"+widthSize+",heightSize:"+heightSize);
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    private List<List<View>> allViews = new ArrayList<>();
    private List<Integer> allHeights = new ArrayList<>();

    /**
     * 画布局   确定每个子View的left,right,top,bottom
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //把每行所有的View和每行行的高度放入集合中
        int width = getWidth();//过了onMeasure  就可以调用getWidth方法了
        int cCount = getChildCount();

        int lineWidth = 0;
        int lineHeight = 0;

        //每行的View  放入进来
        List<View> lineViews = new ArrayList<>();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();
            MarginLayoutParams mp = ((MarginLayoutParams) child.getLayoutParams());
            if (lineWidth + childWidth + mp.rightMargin + mp.leftMargin > width) {
                //换行
                allViews.add(lineViews);
                allHeights.add(lineHeight);
                //重置一下变量
                lineViews = new ArrayList<>();
                lineWidth = 0;
                lineHeight = childHeight + mp.topMargin + mp.bottomMargin;

            }
            //不换行
            lineWidth += childWidth + mp.leftMargin + mp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + mp.topMargin + mp.bottomMargin);
            lineViews.add(child);
            if (i == cCount - 1) {
                allViews.add(lineViews);
                allHeights.add(lineHeight);
            }

            Log.e("FlowLayout", "allViews:" + allViews.size() + ",allHeight:" + allHeights.size());
        }
        //通过计算每一行的每一个子view的left,top,right,bottom,摆放每一行的每一个子view的位置
        int left = 0;
        int top = 0;
        for (int i = 0; i < allViews.size(); i++) {
            Integer curLineHeight = allHeights.get(i);
            //当前行的所有子View
            List<View> views = allViews.get(i);
            for (View view : views) {
                int viewHeight = view.getMeasuredHeight();
                int viewWidth = view.getMeasuredWidth();
                MarginLayoutParams mp = ((MarginLayoutParams) view.getLayoutParams());
                int lc = left + mp.leftMargin;
                int tc = top + mp.topMargin;
                int rc = lc + viewWidth;
                int bc = tc + viewHeight;
                view.layout(lc, tc, rc, bc);
                //宽度要累加   高度不需要  因为在当前行
                left += viewWidth + mp.rightMargin + mp.leftMargin;
            }
            left = 0;
            top += curLineHeight;
        }
    }

    /**
     * 自动生成布局参数的方法  所以我们可以通过重写来设置布局参数  可以为一个View指定一个layoutParams
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //因为ViewGroup的LayoutParams  没有leftMargin之类的  所以我们重新指定一个有leftMargin的MarginLayoutParams
        return new MarginLayoutParams(getContext(), attrs);
    }
}
