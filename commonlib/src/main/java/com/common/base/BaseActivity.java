package com.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.ayvytr.logger.L;
import com.base.dialog.CommonProgressDialog;
import com.base.mvp.BaseMVPActivity;
import com.base.mvp.IPresenter;
import com.base.mvp.IView;
import com.base.utils.DeviceUtil;
import com.base.utils.StatusBarUtil;
import com.base.utils.ToastUtil;
import com.common.R;
import com.common.biz.setting.LanguageEvent;
import com.cv.toolbar.MyToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import butterknife.ButterKnife;


/**
 * Created by Jason on 2018/7/21.
 * <p>
 * Activity的基类，所有Activity必须继承此Activity
 */

public abstract class BaseActivity<P extends IPresenter> extends BaseMVPActivity<P> implements IView {

    protected Context mContext;
    private RelativeLayout mRlRoot;
    protected MyToolbar mMyToolbar;
    private CommonProgressDialog mProgressDialog;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(LanguageWrapper.attachBaseContext(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (showMyToolBar()) {
            setStatus(true);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.common_activity_base);
            findView();
            addView(true);
            initTitleButtonClick();
        } else {
            setStatus(updateStatusBarColor());
//            setContentView(getContentViewRes());
            super.onCreate(savedInstanceState);
            View contentView = tryGetContentView();
            if(contentView != null) {
                setContentView(contentView);
            }
        }
        mContext = this;
        ButterKnife.bind(this);
        initView();
        initIntent();
        initData();
    }

    protected void setStatus(boolean isStatus) {
        initStatus(isStatus);
    }

    /**
     * 更新状态栏字体颜色
     *
     * @return true 黑色字体 / false 白色字体
     */
    protected boolean updateStatusBarColor() {
        return true;
    }

    protected void initIntent() {

    }

    protected Bundle getArguments() {
        return getIntent() == null ? new Bundle() : getIntent().getExtras();
    }

    public Activity getActivity() {
        return this;
    }

    public Context getContext() {
        return this;
    }

    /**
     * 初始化状态栏
     */
    protected void initStatus(boolean statusBarDarkText) {
        StatusBarUtil.transparent(this);
        if (statusBarDarkText) {
            // 设置状态栏字体为黑色
            StatusBarUtil.setLightMode(this);
        }
    }

    @Override
    public void showError(String message, int code) {
        showMessage(message);
    }

    private void findView() {
        mRlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        mMyToolbar = (MyToolbar) findViewById(R.id.my_toolbar);
//        mMyToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.color_main_light));
//        mMyToolbar.setLineBackgroundColor(ContextCompat.getColor(this, R.color.color_gray_light1));
        mMyToolbar.hideUnderline();
    }

    /**
     * 添加标题栏和activity界面视图
     */
    protected void addView(boolean isShowStatus) {
        int statusBarHeight = DeviceUtil.getStatusBarHeight(this);
        mMyToolbar.setPadding(mMyToolbar.getPaddingLeft(), mMyToolbar.getPaddingTop() + (isShowStatus ? statusBarHeight : 0),
                mMyToolbar.getPaddingRight(), mMyToolbar.getPaddingBottom());
        View view = tryGetContentView();
        if(view == null) {
            L.e(getClass().getSimpleName(), "BaseActivity addView failed");
            return;
        }

        if(null != mRlRoot) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.BELOW, R.id.my_toolbar);
            mRlRoot.addView(view, lp);
        }
    }

    @Nullable
    private View tryGetContentView() {
        View view = null;
        try {
            if(getContentViewRes() >= 0) {
                view = getLayoutInflater().inflate(getContentViewRes(), null);
            } else {
                view = getContentView();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    /**
     * 初始化标题栏左右两边按钮点击事件
     */
    private void initTitleButtonClick() {
        if (null != mMyToolbar) {
            //返回按钮点击事件处理
            mMyToolbar.setLeftIvOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackButton(view);
                }
            });

            //标题栏右边按钮点击事件处理
            mMyToolbar.setOnRightTvClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRightButton(view);
                }
            });

            mMyToolbar.setLeftTvVisible(true);
            mMyToolbar.setLeftTvOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 返回按钮点击事件
     *
     * @param view
     */
    protected void onBackButton(View view) {
        finish();
    }

    /**
     * 右边按钮点击事件处理
     *
     * @param view
     */
    protected void onRightButton(View view) {

    }

    /**
     * 设置是否展示标题栏
     *
     * @return true 展示 false 不展示
     */
    protected boolean showMyToolBar() {
        return true;
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


    @Override
    public void showLoading(String tips) {
        if (mProgressDialog == null) {
            mProgressDialog = new CommonProgressDialog(this, R.style.my_progress_dialog_theme);
            View progressView = LayoutInflater.from(this).inflate(R.layout.common_layout_loading, null, false);
            mProgressDialog.setProgressView(progressView);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMsg(tips);
        }
        mProgressDialog.show();
    }

    @Override
    public void showLoading() {
        showLoading("");
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(@NonNull String message) {
        ToastUtil.show(this, message);
    }

    @Override
    public void showMessage(@StringRes int strId) {
        ToastUtil.show(this, strId);
    }

//    /**
//     * 如果activity下有使用到fragment，需要在activity的入口处调用此方法，目的是去除重复统计
//     */
//    protected void deletActivityAgent() {
////        MobclickAgent.openActivityDurationTrack(false);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟基础指标统计
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 友盟基础指标统计
//        MobclickAgent.onPause(this);
    }

    @Override
    protected P getPresenter() {
        return null;
    }

    protected int getContentViewRes() {
        return -1;
    }

    protected @Nullable View getContentView() {
        return null;
    }

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
        try {
            fixInputMethod(this);
        } catch (Exception e) {
            L.d("" + e.getMessage());
        }
    }

    /**
     * 解决输入法导致的内存泄漏
     *
     * @param context
     */
    public static void fixInputMethod(Context context) {
        if (context == null) {
            return;
        }
        InputMethodManager inputMethodManager = null;
        try {
            inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (inputMethodManager == null) {
            return;
        }
        Field[] declaredFields = inputMethodManager.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            try {
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                Object obj = declaredField.get(inputMethodManager);
                if (obj == null || !(obj instanceof View)) {
                    continue;
                }
                View view = (View) obj;
                if (view.getContext() == context) {
                    declaredField.set(inputMethodManager, null);
                } else {
                    return;
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLanguageChoseEvent(LanguageEvent event) {
        if (null != event) {
            recreate();
        }
    }

    /**
     * 设置 app 字体不随系统字体设置改变
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res != null) {
            Configuration config = res.getConfiguration();
            if (config != null && config.fontScale != 1.0f) {
                config.fontScale = 1.0f;
                res.updateConfiguration(config, res.getDisplayMetrics());
            }
        }
        return res;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        if(mMyToolbar != null) {
            mMyToolbar.setTitle(titleId);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if(mMyToolbar != null) {
            mMyToolbar.setTitle(title.toString());
        }
    }
}
