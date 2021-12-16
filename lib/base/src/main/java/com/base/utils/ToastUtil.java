package com.base.utils;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.base.R;
import com.base.manager.ActivityStack;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;


public class ToastUtil {
    private static Toast toast;

    /**
     * 初始化ToastUtils，建议在Application中初始化
     */
    public static void init(Context c) {
        if(toast == null) {
            toast = new Toast(c.getApplicationContext());
            toast.setGravity(Gravity.CENTER, 0, 0);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                toast.addCallback(new Toast.Callback() {
                    @Override
                    public void onToastHidden() {
                        super.onToastHidden();
                        toast.setView(null);
                    }
                });
            }
        }
    }

    /**
     * 显示一个吐司
     *
     * @param id 字符串id
     */
    public static void show(Context context, @StringRes int id) {
        show(context.getApplicationContext(), context.getString(id));
    }

    private static void setText(Context context, CharSequence text) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && ActivityStack.getRunningActivityCount() == 0) {
            toast.setView(null);
            toast.setText(text);
        } else {
            TextView tv;
            if(toast.getView() == null) {
                tv = getTextView(context);
                toast.setView(tv);
            } else {
                tv = (TextView) toast.getView();
            }
            tv.setText(text);
        }
    }

    private static TextView getTextView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return (TextView) inflater.inflate(R.layout.layout_toast, null);
    }

    /**
     * 显示一个吐司
     *
     * @param id string id
     */
    public static void showLong(Context context, @StringRes int id) {
        showLong(context.getApplicationContext(), context.getString(id));
    }

    /**
     * 显示一个吐司
     *
     * @param text 需要显示的文本
     */
    public static void show(Context context, @NonNull CharSequence text) {
        init(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        setText(context, text);
        toast.show();
    }

    /**
     * 显示一个吐司
     *
     * @param text 需要显示的文本
     */
    public static void showLong(Context context, CharSequence text) {
        init(context);
        toast.setDuration(Toast.LENGTH_LONG);
        setText(context, text);
        toast.show();
    }

}
