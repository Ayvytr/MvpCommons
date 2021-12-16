package com.common.base;

import android.view.View;

import com.base.adapter.CommonAdapter;
import com.base.mvp.IPresenter;
import com.common.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Jason on 2018/8/23.
 */

public abstract class BaseListFragment2<P extends IPresenter> extends BaseFragment<P> {

    public static final int FIRST_PAGE = 1;
    /**
     * 列表recyclerView
     */
    public RecyclerView mRecyclerView;
    /**
     * 上拉加载/下拉刷新 的容器
     */
    public SmartRefreshLayout mSmartRefreshLayout;

    /**
     * 当前页面数
     */
    protected int currentPage = FIRST_PAGE;

    //    protected EmptyViewAdapter mEmptyViewAdapter;
    private CommonAdapter<?> adapter;

    private View emptyView;

    @Override
    protected void initView() {
        mRecyclerView = parentView.findViewById(R.id.recycler_view);
        mSmartRefreshLayout = parentView.findViewById(R.id.smart_refresh_layout);

        if(null != mRecyclerView) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        }
        if(null != mSmartRefreshLayout) {
            mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    currentPage++;
                    onLoadMoreCallback(refreshLayout);
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    currentPage = FIRST_PAGE;
                    onRefreshCallback(refreshLayout);
                }
            });
        }
    }

    protected abstract void onRefreshCallback(@NonNull RefreshLayout refreshLayout);

    protected void onLoadMoreCallback(@NonNull RefreshLayout refreshLayout) {

    }

    /**
     * 设置是否禁用下拉刷新
     *
     * @param enable true 可以下拉刷新 false 禁止
     */
    protected void enableRefresh(boolean enable) {
        mSmartRefreshLayout.setEnableRefresh(enable);
    }

    /**
     * 设置是否禁用上拉加载
     *
     * @param enable true 可以上拉加载 false 禁止
     */
    protected void enableLoadMore(boolean enable) {
        mSmartRefreshLayout.setEnableLoadMore(enable);
    }

    /**
     * 下拉刷新完成
     */
    protected void finishRefresh() {
        mSmartRefreshLayout.finishRefresh();
    }

    /**
     * 上拉加载完成
     */
    protected void finishLoadMore() {
        mSmartRefreshLayout.finishLoadMore();
    }


    /**
     * 设置列表的adater 封装了空视图
     *
     * @param adapter
     */
    protected <T> void setAdapter(CommonAdapter<T> adapter) {
//        mEmptyViewAdapter = new EmptyViewAdapter(adapter);
        this.adapter = adapter;
        mRecyclerView.setAdapter(adapter);
        emptyView = getEmptyView();
    }


    /**
     * 自定义空视图
     */
    protected View getEmptyView() {
        return getLayoutInflater().inflate(R.layout.item_no_content, null);
    }

    /**
     * 更新数据，page==totalPage时，停止上拉加载.
     */
    protected void updateData(List list, int page, int totalPage) {
        updateData(list, page, totalPage > page);
    }

    /**
     * 更新数据，haveMoreData=false时，停止上拉加载.
     */
    protected void updateData(List list, int page, boolean haveMoreData) {
        currentPage = page;
        if(list == null) {
            list = new ArrayList(0);
        }

//        mEmptyViewAdapter.setEmptyView(getEmptyView());
        if(currentPage == FIRST_PAGE) {
            adapter.updateList(list);
        } else {
            adapter.addList(list);
        }

        switchShowView();

        finishLoadData();
        enableLoadMore(haveMoreData);
    }

    private void switchShowView() {
        if(adapter.getItemCount() == 0) {
            if(emptyView != null && emptyView.getVisibility() != View.VISIBLE) {
                emptyView.setVisibility(View.VISIBLE);
            }
            if(mRecyclerView != null && mRecyclerView.getVisibility() != View.GONE) {
                mRecyclerView.setVisibility(View.GONE);
            }
        } else {
            if(emptyView != null && emptyView.getVisibility() != View.GONE) {
                emptyView.setVisibility(View.GONE);
            }
            if(mRecyclerView != null && mRecyclerView.getVisibility() != View.VISIBLE) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void updateDataFailed() {
        if(currentPage == FIRST_PAGE) {
            adapter.updateList(null);
            enableLoadMore(false);
        } else {
            enableLoadMore(true);
        }

        switchShowView();

        finishLoadData();
        if(currentPage > FIRST_PAGE) {
            currentPage--;
        }
    }

    protected void autoRefresh() {
        mSmartRefreshLayout.autoRefresh();
    }

    protected void finishLoadData() {
        if(mSmartRefreshLayout != null) {
            finishRefresh();
            finishLoadMore();
        }
    }

    @Override
    public void onDestroyView() {
        finishLoadData();
        super.onDestroyView();
    }
}
