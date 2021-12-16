package com.common.base;


import com.base.adapter.EmptyViewAdapter;
import com.base.adapter.MultiItemTypeAdapter;
import com.base.mvp.IPresenter;
import com.common.R;
import com.common.constant.Const;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Jason on 2018/7/31
 * <p>
 * 列表展示的基类
 */
public abstract class BaseListActivity<P extends IPresenter> extends BaseActivity<P> {

    public static int FIRST_PAGE = 1;
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

    protected int pageSize = Const.PAGE_SIZE;

    private EmptyViewAdapter mEmptyViewAdapter;

    @CallSuper
    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mSmartRefreshLayout = findViewById(R.id.smart_refresh_layout);

        if (null != mRecyclerView) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        }

        if (null != mSmartRefreshLayout) {
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
    protected void setAdapter(MultiItemTypeAdapter adapter) {
        mEmptyViewAdapter = new EmptyViewAdapter(adapter);
        mRecyclerView.setAdapter(mEmptyViewAdapter);
    }

    /**
     * 自定义空视图
     */
    protected int getEmptyView() {
        return R.layout.item_no_content;
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

        mEmptyViewAdapter.setEmptyView(getEmptyView());
        if(currentPage == FIRST_PAGE) {
            mEmptyViewAdapter.updateList(list);
        } else {
            mEmptyViewAdapter.addList(list);
        }
        refreshAdapter();

        finishLoadData();
        enableLoadMore(haveMoreData);
    }

    protected void updateDataFailed() {
        if(currentPage == FIRST_PAGE) {
            mEmptyViewAdapter.updateList(null);
            enableLoadMore(false);
        } else {
            enableLoadMore(true);
        }

        finishLoadData();
        if(currentPage > FIRST_PAGE) {
            currentPage--;
        }
    }

    /**
     * 刷新列表
     */
    protected void refreshAdapter() {
        mEmptyViewAdapter.refresh();
    }

    protected void refreshAdapter(int i) {
        mEmptyViewAdapter.refreshPosition(i);
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
    protected void onDestroy() {
        finishLoadData();
        super.onDestroy();
    }
}
