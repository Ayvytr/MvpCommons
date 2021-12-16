package com.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.base.R;

/**
 * Created by Richard on 2018/7/31
 * 通用进度框
 * 可以自定义msg
 * 自定义主题需继承common_progress_dialog_theme_base
 */
public class CommonProgressDialog extends Dialog {

    private TextView mTVMsg;
    private FrameLayout mFrameLayout;
    private ProgressBar mProgressBar;
    private View mCustomProgressView;
    private String mMsg;

    public CommonProgressDialog(Context context) {
        this(context, R.style.common_progress_dialog_theme_base);
    }

    public CommonProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common_progress_dialog);
        mTVMsg = findViewById(R.id.tv_msg);
        mFrameLayout = findViewById(R.id.fl_progress);
        mProgressBar = findViewById(R.id.pd_loading);
        if (!TextUtils.isEmpty(mMsg)) {
            mTVMsg.setVisibility(View.VISIBLE);
            mTVMsg.setText(mMsg);
        } else {
            mTVMsg.setVisibility(View.GONE);
        }
        if (mCustomProgressView != null) {
            mProgressBar.setVisibility(View.GONE);
            mFrameLayout.addView(mCustomProgressView);
        }
    }

    public void setMsg(String msg) {
        this.mMsg = msg;
    }

    public void setMsg(int msg) {
        this.mMsg = getContext().getString(msg);
    }

    public void setProgressView(View view) {
        mCustomProgressView = view;
    }
}
