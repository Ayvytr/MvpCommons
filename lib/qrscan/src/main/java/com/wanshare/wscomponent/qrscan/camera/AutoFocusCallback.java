package com.wanshare.wscomponent.qrscan.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

/**
 * An instance of Camera.AutoFocusCallback.
 *
 * @author wangdunwei
 * @since 1.0.0
 */
public final class AutoFocusCallback implements Camera.AutoFocusCallback {

    private static final long AUTOFOCUS_INTERVAL_MS = 1500L;

    private Handler autoFocusHandler;
    private int autoFocusMessage;

    public void setHandler(Handler autoFocusHandler, int autoFocusMessage) {
        this.autoFocusHandler = autoFocusHandler;
        this.autoFocusMessage = autoFocusMessage;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (autoFocusHandler != null) {
            Message message = autoFocusHandler.obtainMessage(autoFocusMessage, success);
            autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_INTERVAL_MS);
            autoFocusHandler = null;
        } else {
        }
    }

}
