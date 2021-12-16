package com.base.mvp;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MVP Activity 基类
 * </br>
 * Date: 2018/7/21 12:13
 *
 * @author hemin
 */
public abstract class BaseMVPActivity<P extends IPresenter> extends AppCompatActivity {

    //如果当前页面逻辑简单, Presenter 可以为 null
    @Nullable
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = getPresenter();
    }

    protected abstract P getPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //释放资源
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        this.mPresenter = null;
    }


}
