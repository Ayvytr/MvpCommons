package com.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.base.R;

import androidx.annotation.StyleRes;

/**
 * Created by Richard on 2018/7/26
 * 通用对话框
 * 包含：一个title，一个message，两个按钮
 * 支持自定义ContentView
 * 颜色，字体等属性可以通过style自定义，自定义style需继承base_common_dialog_style
 */
public class CommonDialog extends Dialog {


    private TextView tvTitle;
    private TextView tvMsg;
    private ProgressBar pbLoading;
    private Button btnPositive;
    private Button btnNegative;
    private FrameLayout flCustom;
    private OnPositiveClickListener onPositiveListener;
    private OnNegativeClickListener onNegativeListener;
    private String mTitle;
    private String mMessage;
    private String positiveText;
    private String negativeText;
    private boolean isProgressBarShow = false;
    private boolean isNegativeBtnShow = true;
    private View mView;
    private View titleDivider;

    /**
     * 默认的样式，可以在应用启动时设置app默认对话框样式，需继承base_common_dialog_style
     */
    @StyleRes
    private static int defaultStyleId = R.style.base_common_dialog_style;

    private CommonDialog(Context context) {
        super(context, defaultStyleId);
    }

    private CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common_dialog);
        flCustom = (FrameLayout) findViewById(R.id.fl_dialog_content);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        btnPositive = (Button) findViewById(R.id.btn_positive);
        btnNegative = (Button) findViewById(R.id.btn_negative);
        titleDivider = findViewById(R.id.title_divider);
    }

    /**
     * 调用完Builder类的create()方法后显示该对话框的方法
     */
    @Override
    public void show() {
        super.show();
        show(this);
    }

    private void show(final CommonDialog mDialog) {
        if (!TextUtils.isEmpty(mDialog.mTitle)) {
            mDialog.tvTitle.setText(mDialog.mTitle);
            titleDivider.setVisibility(View.VISIBLE);
        } else {
            titleDivider.setVisibility(View.GONE);
        }
        if (mDialog.mView != null) {
            mDialog.flCustom.addView(mDialog.mView);
            mDialog.pbLoading.setVisibility(View.GONE);
            mDialog.tvMsg.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(mDialog.mMessage)) {
                mDialog.tvMsg.setText(mDialog.mMessage);
                mDialog.tvMsg.setVisibility(View.VISIBLE);
            }
            if (isProgressBarShow) {
                mDialog.pbLoading.setVisibility(View.VISIBLE);
                mDialog.btnPositive.setVisibility(View.GONE);
                mDialog.btnNegative.setVisibility(View.GONE);
            }
        }
        if (!mDialog.isNegativeBtnShow) {
            mDialog.btnNegative.setVisibility(View.GONE);
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mDialog.btnPositive
//                    .getLayoutParams();
//            layoutParams.setMargins(150, layoutParams.topMargin, 150, layoutParams.bottomMargin);
//            mDialog.btnPositive.setLayoutParams(layoutParams);
        } else {
            mDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDialog.onNegativeListener != null) {
                        mDialog.onNegativeListener.onClick(mDialog);
                    }
                }
            });
            if (!TextUtils.isEmpty(mDialog.negativeText)) {
                mDialog.btnNegative.setText(mDialog.negativeText);
            }
        }
        mDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog.onPositiveListener != null) {
                    mDialog.onPositiveListener.onClick(mDialog);
                }
                cancel();
            }
        });
        if (!TextUtils.isEmpty(mDialog.positiveText)) {
            mDialog.btnPositive.setText(mDialog.positiveText);
        }
    }

    public static class Builder {

        private CommonDialog mDialog;

        public Builder(Context context) {
            mDialog = new CommonDialog(context);
        }

        public Builder(Context context, int theme) {
            mDialog = new CommonDialog(context, theme);
        }

        /**
         * 设置对话框标题
         *
         * @param title
         */
        public Builder setTitle(String title) {
            mDialog.mTitle = title;
            return this;
        }

        /**
         * 设置对话框文本内容,如果调用了setView()方法，该项失效
         *
         * @param msg
         */
        public Builder setMessage(String msg) {
            mDialog.mMessage = msg;
            return this;
        }

        /**
         * 设置确认按钮的回调
         *
         * @param onClickListener
         */
        public Builder setPositiveButton(OnPositiveClickListener onClickListener) {
            mDialog.onPositiveListener = onClickListener;
            return this;
        }

        /**
         * 设置确认按钮的回调
         *
         * @param btnText,onClickListener
         */
        public Builder setPositiveButton(String btnText, OnPositiveClickListener onClickListener) {
            mDialog.positiveText = btnText;
            mDialog.onPositiveListener = onClickListener;
            return this;
        }

        /**
         * 设置取消按钮的回掉
         *
         * @param onClickListener
         */
        public Builder setNegativeButton(OnNegativeClickListener onClickListener) {
            mDialog.onNegativeListener = onClickListener;
            return this;
        }

        /**
         * 设置取消按钮的回调
         *
         * @param btnText,onClickListener
         */
        public Builder setNegativeButton(String btnText, OnNegativeClickListener onClickListener) {
            mDialog.negativeText = btnText;
            mDialog.onNegativeListener = onClickListener;
            return this;
        }

        /**
         * 设置手否显示ProgressBar，默认不显示
         *
         * @param isProgressBarShow
         */
        public Builder setProgressBarShow(boolean isProgressBarShow) {
            mDialog.isProgressBarShow = isProgressBarShow;
            return this;
        }

        /**
         * 设置是否显示取消按钮，默认显示
         *
         * @param isNegativeBtnShow
         */
        public Builder setNegativeBtnShow(boolean isNegativeBtnShow) {
            mDialog.isNegativeBtnShow = isNegativeBtnShow;
            return this;
        }

        /**
         * 设置自定义内容View
         *
         * @param view
         */
        public Builder setView(View view) {
            mDialog.mView = view;
            return this;
        }

        /**
         * 设置该对话框能否被Cancel掉，默认可以
         *
         * @param cancelable
         */
        public Builder setCancelable(boolean cancelable) {
            mDialog.setCancelable(cancelable);
            return this;
        }

        /**
         * 设置对话框被cancel对应的回调接口，cancel()方法被调用时才会回调该接口
         *
         * @param onCancelListener
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            mDialog.setOnCancelListener(onCancelListener);
            return this;
        }

        /**
         * 设置对话框消失对应的回调接口，一切对话框消失都会回调该接口
         *
         * @param onDismissListener
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            mDialog.setOnDismissListener(onDismissListener);
            return this;
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        public CommonDialog create() {
            return mDialog;
        }
    }

    public interface OnPositiveClickListener {
        void onClick(CommonDialog dialog);
    }

    public interface OnNegativeClickListener {
        void onClick(CommonDialog dialog);
    }

    public static void setDefaultStyle(@StyleRes int resId) {
        defaultStyleId = resId;
    }

}
