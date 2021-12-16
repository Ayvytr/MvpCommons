package com.base.mvp;

/**
 * MVP中 Presenter层的base接口
 * </br>
 * Date: 2018/7/21 11:31
 *
 * @author hemin
 */
public interface IPresenter {
    /**
     * 做一些初始化操作
     */
    void onStart();

    /**
     * 在框架中 { Activity#onDestroy() } 时会默认调用 {@link IPresenter#onDestroy()}
     */
    void onDestroy();
}
