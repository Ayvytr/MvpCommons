package com.wanshare.wscomponent.update;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanshare.wscomponent.update.dialog.BaseDialog;
import com.wanshare.wscomponent.update.dialog.IDialogListener;

public class UpdateDialog extends BaseDialog implements View.OnClickListener {


    private TextView mTvUpdateContent;
    private TextView mTvUpdateVersion;
    private TextView mBtnUpdateConfirm;
    private ImageView mBtnUpdateCancel;
    private AroundProgressBar mPbUpdateProgress;

    private String mContent;
    private String mVersion;
    private boolean mIsForcibly;

    public UpdateDialog(@NonNull Context context, Builder builder) {
        super(context, R.style.MyDialog, builder.iDialogListener);
        mContent = builder.content;
        mVersion = builder.version;
        mIsForcibly = builder.isForcibly;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog_progress);
        initView();
        bindData();
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mIsForcibly) {
                    System.exit(0);
                }
                return false;
            }
        });
    }

    private void initView(){
        mTvUpdateContent = findViewById(R.id.tv_update_content);
        mTvUpdateVersion = findViewById(R.id.tv_update_version);
        mBtnUpdateConfirm = findViewById(R.id.btn_update_confirm);
        mBtnUpdateCancel = findViewById(R.id.btn_update_cancel);
        mPbUpdateProgress = findViewById(R.id.pb_update_progress);
        mTvUpdateContent.setMovementMethod(ScrollingMovementMethod.getInstance());

        mBtnUpdateConfirm.setOnClickListener(this);
        mBtnUpdateCancel.setOnClickListener(this);
    }

    private void bindData(){
        mTvUpdateVersion.setText(mVersion);
        mTvUpdateContent.setText(mContent);
        mBtnUpdateCancel.setVisibility(mIsForcibly ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_update_confirm) {
            mIDialogListener.confirm();
        } else if (v.getId() == R.id.btn_update_cancel) {
            mIDialogListener.cancel();
            dismiss();
        }
    }

    public void setPbUpdateProgress(int progress){
        if (mPbUpdateProgress.getVisibility() == View.GONE) {
            showProgress(true);
        }
        if (progress == 100) {
            showProgress(false);
            return;
        }
        mPbUpdateProgress.setProgress(progress);
    }

    public void showProgress(boolean isShow){
        mPbUpdateProgress.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mBtnUpdateConfirm.setVisibility(isShow ? View.GONE : View.VISIBLE);
        if (!isShow) {
            mPbUpdateProgress.reset();
        }
    }

    public static class Builder{
        private String content;
        private String version;
        private boolean isForcibly;
        private IDialogListener iDialogListener;

        public Builder() {

        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setForcibly(boolean forcibly) {
            isForcibly = forcibly;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setIDialogListener(IDialogListener iDialogListener) {
            this.iDialogListener = iDialogListener;
            return this;
        }

        public UpdateDialog create(Context context) {
            return new UpdateDialog(context, this);
        }
    }


}
