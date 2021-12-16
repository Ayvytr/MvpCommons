package com.base.mvp;

/**
 * 错误处理
 * </br>
 * Date: 2018/7/23 16:51
 *
 * @author hemin
 */
public interface ResponseErrorListener {
    void handleResponseError(Throwable t);

    ResponseErrorListener EMPTY = new ResponseErrorListener() {
        @Override
        public void handleResponseError( Throwable t) {


        }
    };
}
