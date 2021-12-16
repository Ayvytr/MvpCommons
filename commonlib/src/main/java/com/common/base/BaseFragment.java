package com.common.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.dialog.CommonProgressDialog;
import com.base.mvp.BaseMVPFragment;
import com.base.mvp.IPresenter;
import com.base.mvp.IView;
import com.base.utils.DeviceUtil;
import com.base.utils.StatusBarUtil;
import com.base.utils.ToastUtil;
import com.common.R;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * fragment基类，所有fragment必须继承此fragment
 */

public abstract class BaseFragment<P extends IPresenter> extends BaseMVPFragment<P> implements IView {
    private Unbinder mUnbinder;
    protected BaseActivity mActivity;
    /**
     * fragment生命周期标志位，表示fragment是否已经执行onViewCreated()方法
     */
    protected boolean isViewCreated = false;
    public View parentView;
    private CommonProgressDialog mProgressDialog;

    /**
     * 获取宿主Activity
     *
     * @return
     */
    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        StatusBarUtil.transparent(mActivity);
        // 设置状态栏字体为黑色
        StatusBarUtil.setLightMode(mActivity);

//        parentView = inflater.inflate(getContentViewRes(), container, false);
        tryGetContentView(inflater, container);

        mUnbinder = ButterKnife.bind(this, parentView);
        initView();
        return parentView;
    }

    private void tryGetContentView(LayoutInflater inflater, ViewGroup root) {
        View view = null;
        try {
            if(getContentViewRes() >= 0) {
                view = inflater.inflate(getContentViewRes(), root, false);
            } else {
                view = getContentView(inflater, root);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        parentView = view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        initArguments(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        unregisterEventBus();
    }


    @Override
    public void showError(String message, int code) {
        showMessage(message);
    }

    @Override
    public void showLoading() {
        showLoading("");
    }

    @Override
    public void showLoading(String tips) {
        if (null == mProgressDialog) {
            mProgressDialog = new CommonProgressDialog(mActivity, R.style.my_progress_dialog_theme);
            View progressView= LayoutInflater.from(getContext()).inflate(R.layout.common_layout_loading,null,false);
            mProgressDialog.setProgressView(progressView);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMsg(tips);
        }
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMsg(tips);
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void addPadding(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(view.getPaddingLeft(),
                    DeviceUtil.getStatusBarHeight(mActivity),
                    view.getPaddingRight(),
                    view.getPaddingBottom());
        }

    }

    @Override
    public void showMessage(@NonNull String message) {
        ToastUtil.show(mActivity, message);
    }

    @Override
    public void showMessage(@StringRes int strId) {
        ToastUtil.show(getContext(), strId);
    }

    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    protected P getPresenter() {
        return null;
    }

    protected int getContentViewRes() {
        return -1;
    }

    protected View getContentView(LayoutInflater inflater, ViewGroup root) {
        return null;
    }

    protected abstract void initView();

    protected abstract void initData();

    protected void initArguments(Bundle savedInstanceState) {

    }

    /**
     * 初始化EvenBus
     */
    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 取消注册EvenBus
     */
    protected void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
