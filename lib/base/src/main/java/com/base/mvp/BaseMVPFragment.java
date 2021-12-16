package com.base.mvp;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * MVP Fragment 基类
 * </br>
 * Date: 2018/7/24 10:00
 *
 * @author hemin
 */
public abstract class BaseMVPFragment<P extends IPresenter> extends Fragment {
//    protected final String TAG = this.getClass().getSimpleName();

    //如果当前页面逻辑简单, Presenter 可以为 null
    @Nullable
    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = getPresenter();
    }

    protected abstract P getPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            //释放资源
            mPresenter.onDestroy();
        }
        this.mPresenter = null;
    }
}
