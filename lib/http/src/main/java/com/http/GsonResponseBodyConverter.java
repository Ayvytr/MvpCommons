package com.http;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        if (TextUtils.isEmpty(response)) {
            if (type instanceof ParameterizedType) {
                ParameterizedType paraType = (ParameterizedType) type;
                String rawType = paraType.getRawType().toString();
                if (rawType != null && rawType.contains("java.util.List")) {
                    response = "[]";
                    return gson.fromJson(response, type);
                }else{
                    response = "{}";
                    return gson.fromJson(response, type);
                }
            }else{
                return getEmptyResult();
            }
        }
        return gson.fromJson(response, type);
    }

    private T getEmptyResult() {
        T result = null;
        try {
            result = ((Class<T>) type).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

}