package com.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.base.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 对话框基类
 *
 * @author wangdunwei
 * @date 2018/5/7
 */
public abstract class BaseAbsDialog extends Dialog {
    private boolean isFillWidth = true;
    private int mGravity;

    protected BaseAbsDialog(@NonNull Context context) {
        super(context, R.style.TransparentDimBgDialog);
    }

    protected BaseAbsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseAbsDialog(@NonNull Context context, boolean cancelable,
                            @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected abstract int getContentView();

    protected abstract void initView();

    @Override
    public void show() {
        super.show();
        if (isFillWidth) {
            Window window = getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.horizontalMargin = 0;
            window.setAttributes(layoutParams);
            window.getDecorView().setMinimumWidth(getContext().getResources().getDisplayMetrics().widthPixels);
        }

        if (mGravity != 0) {
            getWindow().setGravity(mGravity);
        }
    }

    /**
     * 设置Dialog宽度占满屏幕宽度
     */
    public void setFillWidth(boolean isFillWidth) {
        this.isFillWidth = isFillWidth;
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }
}
