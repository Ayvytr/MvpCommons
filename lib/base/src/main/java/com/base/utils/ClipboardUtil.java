/*
 * 深圳小旺网络科技有限公司 
 */
package com.base.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.os.Build;

/**
 * 剪切板相关工具类
 */
@SuppressLint("NewApi")
public class ClipboardUtil {

	public static void setText(Context context, String content) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			android.content.ClipboardManager cm = (android.content.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			cm.setPrimaryClip(ClipData.newPlainText(null, content));
		} else {
			android.text.ClipboardManager cm = (android.text.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			cm.setText(content);
		}
	}

	public static String getText(Context context, String content) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			android.content.ClipboardManager cm = (android.content.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			if (cm.hasPrimaryClip()) {
				if (cm.getPrimaryClip().getItemCount() > 0) {
					return cm.getPrimaryClip().getItemAt(0).getText().toString();
				}
			}
		} else {
			android.text.ClipboardManager cm = (android.text.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			if (cm.hasText()) {
				return cm.getText().toString();
			}
		}
		return null;
	}
}
