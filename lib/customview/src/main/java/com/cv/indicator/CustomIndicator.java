package com.cv.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cv.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;


/**
 * @author Xu wenxiang
 * create at 2018/10/20
 * description: 验证头部控件
 */
public class CustomIndicator extends View {

    private float textSize = 16;

    private float strokeWidth = 1;

    private int backgroundColor = Color.TRANSPARENT;

    private float radius = 5;

    private boolean isInitBorder;

    private int itemSelectedColor = 0xFFE5C580;

    private int strokeColor = 0xFF666b74;


    private int textNormalColor = 0xFF808490;
    private int textSelectedColor = 0xFF1F222D;

    private List<String> itemTitles;

    private int itemNumber;

    private float itemWidth;


    private int height;
    private int width;


    private int currentPage = 0;

    private Paint paint;

    List<Path> pathList = new ArrayList<Path>();


    public CustomIndicator(Context context) {
        this(context, null);
    }

    public CustomIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CustomIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(context, attrs, defStyleAttr);
    }

    private void initStyle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        init();
        if (attrs == null) {
            return;
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomIndicator);
        textSize = array.getDimension(R.styleable.CustomIndicator_indicator_text_size, textSize);
        backgroundColor = array.getColor(R.styleable.CustomIndicator_indicator_background_color, backgroundColor);
        strokeWidth = array.getDimension(R.styleable.CustomIndicator_indicator_frame_width, strokeWidth);
        radius = array.getDimension(R.styleable.CustomIndicator_indicator_frame_corner, dp2px(this.radius));
        itemSelectedColor = array.getColor(R.styleable.CustomIndicator_indicator_select_color, itemSelectedColor);
        strokeColor = array.getColor(R.styleable.CustomIndicator_indicator_frame_color, strokeColor);
        textNormalColor = array.getColor(R.styleable.CustomIndicator_indicator_text_un_select_color, textNormalColor);
        textSelectedColor = array.getColor(R.styleable.CustomIndicator_indicator_text_select_color, textSelectedColor);
    }

    private void init() {
        initPaint();
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(dp2px(strokeWidth));
        paint.setTextSize(dp2px(textSize));
        paint.setLinearText(true);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    private void initPath() {

        for (int i = 0; i < itemNumber; i++) {
            Path path = new Path();

            if (i == 0) {
                path.moveTo(0, radius);
                path.quadTo(0, 0, radius, 0);
                path.lineTo(itemWidth, 0);
                path.lineTo(itemWidth, height);
                path.lineTo(radius, height);
                path.quadTo(0, height, 0, height - radius);
                path.close();
            } else if (i == itemNumber - 1) {
                path.moveTo(itemWidth * i, 0);
                path.lineTo(width - radius, 0);
                path.quadTo(width, 0, width, radius);
                path.lineTo(width, height - radius);
                path.quadTo(width, height, width - radius, height);
                path.lineTo(itemWidth * i, height);
                path.close();

            } else {
                path.moveTo(itemWidth * i, 0);
                path.lineTo(itemWidth * (i + 1), 0);
                path.lineTo(itemWidth * (i + 1), height);
                path.lineTo(itemWidth * i, height);
                path.close();
            }

            pathList.add(path);

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);

        if (itemNumber < 2) {
            throwException();
        }

        itemWidth = (width / itemNumber);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);

        if (!isInitBorder) {
            isInitBorder = true;
            initPath();//初始化path

            GradientDrawable gd = new GradientDrawable();
            gd.setColor(backgroundColor);
            gd.setCornerRadius(dp2px(5));
            gd.setStroke(dp2px(strokeWidth), strokeColor);
            this.setBackgroundDrawable(gd);
        }

        for (int i = 1; i < itemNumber; i++) {
            canvas.drawLine(itemWidth * i, 0, itemWidth * i, height, paint);
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(itemSelectedColor);


        for (int i = 0; i < itemNumber; i++) {
            if (currentPage == i) {
                canvas.drawPath(pathList.get(i), paint);
            }
        }

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (height / 2 - top / 2 - bottom / 2);

        for (int i = 0; i < itemNumber; i++) {
            if (currentPage != i) {
                paint.setColor(textNormalColor);
            } else {
                paint.setColor(textSelectedColor);
            }
            canvas.drawText(itemTitles.get(i), itemWidth / 2f + itemWidth * i, baseLineY, paint);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                handleActionUp(event.getX(), event.getY());
                break;
            default:
                break;
        }

        return true;
    }


    private void handleActionUp(float x, float y) {
        if (y <= height && y >= 0) {//手指移除范围
            int newCurrentPage = (int) (x / itemWidth);

            if (currentPage == newCurrentPage) {
                //已经是当前页
                if (mListener != null) {
                    mListener.onClick(newCurrentPage, true);
                }
            } else {
                if (mListener != null) {
                    mListener.onClick(newCurrentPage, false);
                }
                currentPage = newCurrentPage;
            }

            invalidate();
        }
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        invalidate();
    }

    private ClickListener mListener;

    public void setSwitchListener(ClickListener listener) {
        mListener = listener;
    }

    public interface ClickListener {

        void onClick(int currentIndex, boolean isRepeat);
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public float getTextSize() {
        return textSize;
    }

    public CustomIndicator setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public List<String> getItemTitles() {
        return itemTitles;
    }

    public CustomIndicator setItemTitles(List<String> itemTitles) {

        this.itemTitles = itemTitles;
        itemNumber = itemTitles.size();

        if (itemNumber < 2) {
            throwException();
        }

        return this;
    }

    private void throwException() {
        throw new IllegalArgumentException("The number of the item may not be less than 2");
    }

    public int getNormalBackgroundColor() {
        return backgroundColor;
    }

    public CustomIndicator setNormalBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public CustomIndicator setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public int getItemSelectedColor() {
        return itemSelectedColor;
    }

    public CustomIndicator setItemSelectedColor(int itemSelectedColor) {
        this.itemSelectedColor = itemSelectedColor;
        return this;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public CustomIndicator setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public int getTextNormalColor() {
        return textNormalColor;
    }

    public CustomIndicator setTextNormalColor(int textNormalColor) {
        this.textNormalColor = textNormalColor;
        return this;
    }

    public int getTextSelectedColor() {
        return textSelectedColor;
    }

    public CustomIndicator setTextSelectedColor(int textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
        return this;
    }
}



