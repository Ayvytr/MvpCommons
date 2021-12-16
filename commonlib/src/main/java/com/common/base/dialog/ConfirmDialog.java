package com.common.base.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.R;
import com.common.R2;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Xu wenxiang
 * create at 2018/11/2
 * description: 确认弹出框
 */
public class ConfirmDialog extends BaseDialog {

    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_content)
    TextView mTvContent;
    @BindView(R2.id.cb_confirm_check)
    ImageView mCbConfirmCheck;
    @BindView(R2.id.ll_confirm_check)
    LinearLayout mLlConfirmCheck;
    @BindView(R2.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R2.id.tv_confirm)
    TextView mTvConfirm;

    private String title;
    private String content;
    //    private String cancel;
//    private String confirm;
    private boolean isShowTips;

    private OnConfirmListener mOnConfirmListener;

    private boolean hideCancel = false;

    public ConfirmDialog(@NonNull Context context, @StringRes int titleId) {
        this(context, context.getString(titleId), null);
    }

    public ConfirmDialog(@NonNull Context context, @StringRes int titleId,
                         @StringRes int contentId) {
        this(context, context.getString(titleId), context.getString(contentId));
    }

    public ConfirmDialog(@NonNull Context context, String title, String content) {
        this(context, title, content, false
//                , null, null
        );
    }

    public ConfirmDialog(@NonNull Context context, @StringRes int titleId,
                         @StringRes int contentId, boolean isShowTips
//            ,
//                         @StringRes int confirmId,
//                         @StringRes int cancelId
    ) {
        this(context, context.getString(titleId), context.getString(contentId), isShowTips
//                , context.getString(confirmId), context.getString(cancelId)
        );
    }

    public ConfirmDialog(@NonNull Context context, String title, String content, boolean isShowTips
//            ,String confirm, String cancel
    ) {
        super(context);
        this.title = title;
        this.content = content;
        this.isShowTips = isShowTips;
//        this.confirm = confirm;
//        this.cancel = cancel;
    }

    @Override
    protected int getContentView() {
        return R.layout.common_dialog_confirm;
    }

    @Override
    protected void initView() {
        mTvTitle.setText(title);
        mLlConfirmCheck.setVisibility(isShowTips ? View.VISIBLE : View.GONE);
        mTvContent.setText(content);
        mTvContent.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
//        if(!TextUtils.isEmpty(confirm)) {
//            mBtnConfirm.setText(confirm);
//        }
//        if(!TextUtils.isEmpty(cancel)) {
//            mBtnCancel.setText(cancel);
//        }
    }

    @OnClick({R2.id.tv_cancel, R2.id.tv_confirm, R2.id.cb_confirm_check})
    public void onViewClicked(View view) {
        int i = view.getId();
        if(i == R.id.tv_cancel) {
            if(mOnConfirmListener != null) {
                mOnConfirmListener.cancel();
            }
            dismiss();
        } else if(i == R.id.tv_confirm) {
            if(mOnConfirmListener != null) {
                mOnConfirmListener.confirm(mCbConfirmCheck.isSelected());
            }
            dismiss();
        } else if(i == R.id.cb_confirm_check) {
            mCbConfirmCheck.setSelected(!mCbConfirmCheck.isSelected());
        }
    }

    public void setHideCancel() {
        hideCancel = true;
    }

    @Override
    public void show() {
        super.show();
        if(hideCancel) {
            mTvCancel.setVisibility(View.GONE);
            findViewById(R.id.view_line).setVisibility(View.GONE);
        }
    }

    public ConfirmDialog setOnConfirmListener(OnConfirmListener mOnConfirmListener) {
        this.mOnConfirmListener = mOnConfirmListener;
        return this;
    }

    public interface OnConfirmListener {
        void confirm(boolean isSelect);

        default void cancel() {
        }
    }
}
