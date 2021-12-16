package com.common.cv.rv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.base.utils.DensityUtil;
import com.common.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/4/2.
 */

public class CustomItemDecoration extends RecyclerView.ItemDecoration {

    //height /px
    private int mDividerHeight;

    private Paint mPaint;

    //px
    private int mLeftMargin;
    private int mRightMargin;

    public static CustomItemDecoration ofDefault(Context context) {
        return new CustomItemDecoration(context.getResources().getColor(R.color.color_gray_bg),
                DensityUtil.dip2px(context, 1));
    }

    public static CustomItemDecoration ofDefault(Context context, int leftMargin) {
        return new CustomItemDecoration(context.getResources().getColor(R.color.color_gray_bg),
                DensityUtil.dip2px(context, 1), DensityUtil.dip2px(context, leftMargin), 0);
    }

    public CustomItemDecoration(int color) {
        this(color, 1, 0, 0);
    }

    public CustomItemDecoration(int color, int dividerHeight) {
        this(dividerHeight, color, 0, 0);
    }

    public CustomItemDecoration(int dividerHeight, int color, int leftMargin, int rightMargin) {
        this.mDividerHeight = dividerHeight;
        this.mLeftMargin = leftMargin;
        this.mRightMargin = rightMargin;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

//        //第一个ItemView不需要在上面绘制分割线
        if (parent.getChildAdapterPosition(view) != 0) {
            //这里直接硬编码为1px
            outRect.top = mDividerHeight;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);

            int index = parent.getChildAdapterPosition(view);
            //第一个ItemView不需要绘制
            if (index == 0) {
                continue;
            }

            float dividerTop = view.getTop() - mDividerHeight;
            float dividerLeft = mLeftMargin;
            float dividerBottom = view.getTop();
            float dividerRight = parent.getWidth() - parent.getPaddingRight() - mRightMargin;

            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, mPaint);
        }
    }
}
