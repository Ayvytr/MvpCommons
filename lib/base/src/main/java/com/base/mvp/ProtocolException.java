package com.base.mvp;

/**
 * 协议错误码
 * </br>
 * Date: 2018/9/11 20:26
 *
 * @author hemin
 */
public class ProtocolException extends Exception {
    public static final int CODE_601 = 601;
    public static final int CODE_400 = 400;
    public static final int CODE_401 = 401;
    public static final int CODE_403 = 403;
    public static final int CODE_404 = 404;
    public static final int CODE_406 = 406;
    public static final int CODE_500 = 500;


    private int mErrorCode;


    public ProtocolException(int errorCode, String message) {
        super(message);
        this.mErrorCode = errorCode;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int mErrorCode) {
        this.mErrorCode = mErrorCode;
    }
}
