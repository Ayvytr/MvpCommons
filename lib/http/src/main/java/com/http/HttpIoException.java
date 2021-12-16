package com.http;

import java.io.IOException;

/**
 * @author Administrator
 */
public class HttpIoException extends IOException {
    private int code;

    public HttpIoException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

