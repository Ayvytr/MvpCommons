package com.common.base.observer;

import android.util.Log;

import com.ayvytr.logger.L;
import com.common.base.BasePresenter;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 封装了错误处理和取消订阅的Observer
 * </br>
 * Date: 2018/7/23 16:36
 *
 * @author hemin
 */
public class ErrorHandleObserver<T> implements Observer<T> {
    private static final String TAG = "log";
    private BasePresenter mPresenter;

    public ErrorHandleObserver(BasePresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mPresenter != null) {
            // 在订阅时必须调用这个方法,不然Activity退出时可能内存泄漏
            mPresenter.addDispose(d);
            L.d(TAG, "onSubscribe: " + d, "Thread: ", Thread.currentThread().getName());
        }
    }

    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(Throwable e) {
        L.e(e);

        if(mPresenter == null) {
            return;
        }

        mPresenter.getRootView().hideLoading();

        mPresenter.handleResponseError(e);
    }


    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete: on thread:" + Thread.currentThread().getName());
    }
}
