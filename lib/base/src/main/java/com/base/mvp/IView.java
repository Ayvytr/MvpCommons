package com.base.mvp;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * MVP中 View层的base接口
 * </br>
 * Date: 2018/7/21 11:06
 *
 * @author hemin
 */
public interface IView {

    void showLoading();
    /**
     * 显示加载中
     */
     void showLoading(String tips);

    /**
     * 隐藏加载中
     */
    void hideLoading();

    /**
     * 显示提示信息
     * @param message 消息内容, 不能为 {@code null}
     */
    void showMessage(@NonNull String message);

    void showMessage(@StringRes int strId);

    void showError(String message, int code);

}
