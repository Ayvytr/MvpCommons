package com.wanshare.wscomponent.update.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;

public class BaseDialog extends Dialog{

    protected IDialogListener mIDialogListener;

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public BaseDialog(@NonNull Context context, int themeResId, IDialogListener IDialogListener) {
        super(context, themeResId);
        mIDialogListener = IDialogListener;
    }

    public void setIDialogListener(IDialogListener IDialogListener) {
        mIDialogListener = IDialogListener;
    }

    public void cancel(){
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }
}
