package com.common.base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayvytr.logger.L;
import com.base.mvp.IPresenter;

/**
 * 懒加载fragment
 * </br>
 * Date: 2018/8/17 12:01
 *
 * @author hemin
 */
public abstract class LazyFragment<P extends IPresenter> extends BaseFragment<P> {

    /** 显示的是否是当前页 */
    protected boolean mIsVisible = false;

    //是不是调用过initData初始化过
    protected boolean isInited;
    //是不是只初始化一次。可以在serUserVisibleHint调用之前设为true让initData只调用一次
    protected boolean initOnce=true;

    public LazyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.d("onCreateView:"+isViewCreated+"  "+this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isViewCreated = true;
        L.d("onViewCreated:"+isViewCreated+"  "+this);
        loadData();
    }

    private void loadData() {
        // 可见，并且view已经加载完才加载数据
        if(!(isViewCreated && mIsVisible)){
            return;
        }

        if(!isInited || !initOnce) {
            initData();
            isInited = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mIsVisible = isVisibleToUser ;
        L.d("setUserVisibleHint: mIsVisible "+mIsVisible+"  "+this);
        loadData();
        super.setUserVisibleHint(isVisibleToUser);
    }
}
