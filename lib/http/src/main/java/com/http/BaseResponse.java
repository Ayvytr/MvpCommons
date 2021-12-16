package com.http;

/**
 * 为项目包装的响应bean，涉及{@link BaseResponseGsonResponseBodyConverter},
 * {@link ResponseConverterFactory}，实际使用retrofit写接口时直接返回T类型即可，减少工作量.
 * @author Administrator
 */
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
