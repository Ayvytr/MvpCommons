package com.cv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Xu wenxiang
 * create at 2019/3/26
 * description: 圆形
 */
public class CircleView extends View {


    private int mCircleColor = 0xff34AF72;



    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void onDraw(Canvas canvas) {
        //画布背景
        canvas.drawColor(Color.WHITE);

        float cirX = getWidth() / 2;
        float cirY = getHeight() / 2;
        float radius = cirX;//150;

        Paint mPaint = new Paint();

        mPaint.setColor(mCircleColor);
        canvas.drawCircle(cirX, cirY, radius, mPaint);

    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        invalidate();
    }
}
