package com.ly.p2pmon.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ly.p2pmon.R;
import com.ly.p2pmon.fragment.HomeFragment;

/**
 * Created by 12758 on 2016/5/8.
 */
public class RoundProgress extends View {
    private Paint paint = new Paint();
    private int roundColor;
    private int roundProgressColor;
    private int textColor;
    private float textSize;
    private float roundWidth;
    int progress = 50;
    private int max = 100;


    public RoundProgress(Context context) {
        this(context, null);
    }

    public RoundProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgress);//不写attrs的话，取的值是默认值
        //圆环的颜色
        roundColor = typedArray.getColor(R.styleable.RoundProgress_roundColor, Color.RED);
        //圆环进度的颜色
        roundProgressColor = typedArray.getColor(R.styleable.RoundProgress_roundProgressColor, Color.GREEN);
        //中间进度百分比的字符串颜色
        textColor = typedArray.getColor(R.styleable.RoundProgress_textColor, Color.BLACK);
        //中间进度百分比的字符串的字体大小
        textSize = typedArray.getDimension(R.styleable.RoundProgress_textSize, 30);
        //圆环的宽度
        roundWidth = typedArray.getDimension(R.styleable.RoundProgress_roundWidth, 5);

        typedArray.recycle();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        //1.先绘制外层圆环
        paint.setColor(roundColor);//
        paint.setStrokeWidth(roundWidth);//画笔厚度
        paint.setStyle(Paint.Style.STROKE);//不填充
        paint.setAntiAlias(true);//抗锯齿改成true,就是平滑
        int center = getWidth() / 2;
        int radius = (int) (center - roundWidth / 2);
        canvas.drawCircle(center, center, radius, paint);
        //2.画中间的字符串文本
        float textWidth = paint.measureText(progress + "%");//得到文本的宽度
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(0);
        canvas.drawText(progress + "%", center - textWidth / 2, center + textSize / 2, paint);
        //3.画弧形
        /**
         * 参数解释：
         * oval：绘制弧形圈所包含的矩形范围轮廓，即确定弧形的4个点
         * 0：开始的角度
         * 360 * progress / max：扫描过的角度
         * false：是否包含圆心,true就会很丑
         * paint：绘制弧形时候的画笔
         */
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        paint.setColor(roundProgressColor);
        paint.setStrokeWidth(roundWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, 0, 360 * progress / max, false, paint);

        super.onDraw(canvas);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (progress > 100) {
            this.progress = 100;
        }
        postInvalidate();//重新绘制,不能手动调onDraw(),因为该前后可能还有一些处理
    }


}
