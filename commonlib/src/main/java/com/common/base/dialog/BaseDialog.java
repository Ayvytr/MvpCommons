package com.common.base.dialog;

import android.content.Context;
import android.os.Bundle;

import com.base.dialog.BaseAbsDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 对话框基类
 *
 * @author wangdunwei
 * @date 2018/5/7
 */
public abstract class BaseDialog extends BaseAbsDialog {
    private Unbinder mUnbinder;

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public BaseDialog(@NonNull Context context, boolean cancelable,
                         @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getContentView());
        mUnbinder = ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onDetachedFromWindow() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDetachedFromWindow();
    }

}
