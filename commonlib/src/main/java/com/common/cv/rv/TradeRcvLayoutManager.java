package com.common.cv.rv;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 捕获IndexOutOfBoundsException异常
 * @author wangdunwei
 * @date 2018/6/4
 */
public class TradeRcvLayoutManager extends LinearLayoutManager
{
    public TradeRcvLayoutManager(Context context)
    {
        super(context);
    }

    public TradeRcvLayoutManager(Context context, int orientation, boolean reverseLayout)
    {
        super(context, orientation, reverseLayout);
    }

    public TradeRcvLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state)
    {
        try
        {
            super.onLayoutChildren(recycler, state);
        } catch(IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
    }
}
