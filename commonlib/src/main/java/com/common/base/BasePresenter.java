package com.common.base;


import com.alibaba.android.arouter.launcher.ARouter;
import com.ayvytr.logger.L;
import com.base.manager.ActivityStack;
import com.base.mvp.IPresenter;
import com.base.mvp.IView;
import com.base.mvp.ProtocolException;
import com.base.mvp.ResponseErrorListener;
import com.base.utils.NetworkUtils;
import com.common.R;
import com.common.biz.account.AccountArouterConstant;
import com.common.manager.UserManager;
import com.http.HttpIoException;

import java.net.ConnectException;

import androidx.annotation.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * MVP Presenter 基类
 * </br>
 * Date: 2018/7/21 11:36
 *
 * @author hemin
 */
public class BasePresenter<V extends IView> implements IPresenter, ResponseErrorListener {
    private static final String TAG = BasePresenter.class.getSimpleName();
    protected V mRootView;

    protected CompositeDisposable mCompositeDisposable;

    /**
     * 凡是继承了BasePresenter的地方，一定需要rootView，所以不提供空参构造
     * @param rootView
     */
    public BasePresenter(@NonNull V rootView) {
        //触发空指针，checkNotNull，抛空指针不是好办法，还得定位哪里空了
        rootView.hashCode();
        this.mRootView = rootView;
        onStart();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        unDispose();
        //先不置空mRootView，如果有问题再置空
//        this.mRootView = null;
        mCompositeDisposable = null;
//        L.d("onDestroy: ");
    }

    public V getRootView() {
        return mRootView;
    }

    /**
     * 将 {@link Disposable} 添加到 {@link CompositeDisposable} 中统一管理
     * 可在 Activity.onDestroy() 中使用 {@link #unDispose()} 停止正在执行的 RxJava 任务,避免内存泄漏
     *
     * @param disposable disposable
     */
    public void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);//将所有 Disposable 放入集中处理
    }

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    public void unDispose() {
        if (mCompositeDisposable != null) {
            L.d("unDispose: ", mCompositeDisposable, "Thread:", Thread.currentThread().getName());
            mCompositeDisposable.clear();//保证 Activity 结束时取消所有正在执行的订阅
        }
    }

    @Override
    public void handleResponseError(Throwable e) {
        L.e(e, e.getMessage());

        if (e instanceof ProtocolException) {
            ProtocolException exception = (ProtocolException) e;
            if(ProtocolException.CODE_403 == exception.getErrorCode()) {
                // token异常，访问禁止
                UserManager.clearUserInfo();
                ARouter.getInstance().build(AccountArouterConstant.ACCOUNT_LOGIN).navigation();
                return;
//            } else if(ProtocolException.CODE_500 == exception.getErrorCode()) {
//               mRootView.showHintMessage(UnknownHostException.class.getSimpleName());
//               return;
            } else if(ProtocolException.CODE_601 == exception.getErrorCode()) {
                ARouter.getInstance().build(AccountArouterConstant.ACCOUNT_SAFEGUARD).navigation();
                return;
            }
        } else if(e instanceof HttpIoException) {
            HttpIoException exception = (HttpIoException) e;
            if(exception.getCode() == 501 &&
                    exception.getMessage().startsWith("invalid token or token has expired")) {
                UserManager.clearUserInfo();
                ARouter.getInstance().build(AccountArouterConstant.ACCOUNT_LOGIN).navigation();
                ActivityStack.finishAllExceptTop();
            }
            //TODO: 其他错误码处理

        }

//        L.d(TAG, "onError: " + e.getMessage(), "Thread: ", Thread.currentThread().getName());

        if(ActivityStack.getRunningActivityCount() > 0 && mRootView != null) {
            if(NetworkUtils.isNetworkAvailable(BaseApplication.getInstance()) ||
                    (e instanceof ConnectException && e.getMessage()
                            .startsWith("Failed to connect to"))) {
                mRootView.showMessage(R.string.common_net_error);
            } else {
                mRootView.showMessage(e.getMessage());
            }

        }
    }
}
