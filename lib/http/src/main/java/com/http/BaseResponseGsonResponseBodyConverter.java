package com.http;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class BaseResponseGsonResponseBodyConverter<T>
        implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    public BaseResponseGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        if(TextUtils.isEmpty(response)) {
            return getEmptyT();
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if(code == 200) {
                String data = jsonObject.getString("data");
                if(TextUtils.isEmpty(data)) {
                    return getEmptyT();
                }

                T t = gson.fromJson(data, type);
                if(t == null) {
                   t = getEmptyT();
                }

                return t;
            } else {
                throw new HttpIoException(jsonObject.getString("message"), code);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
//        Type type = new TypeToken<BaseResponse<T>>() {}.getType();
//        Log.e("tag", type.getTypeName());
//        Log.e("tag", new TypeToken<BaseResponse<T>>() {}.getRawType().getName());
//        BaseResponse<T> bean = gson.fromJson(response, type);
        //修改data为空的异常问题
//        if(bean.getCode() == 200) {
//            T data = bean.getData();
//            if(data == null) {
//                data = getEmptyResult();
//            }
//            return data;
//        } else {
//            throw new IOCodeException(bean.getMessage(), bean.getCode());
//        }

        return getEmptyT();
    }

    private T getEmptyT() {
        String response;
        if(type instanceof ParameterizedType) {
            ParameterizedType paraType = (ParameterizedType) type;
            String rawType = paraType.getRawType().toString();
            if(rawType != null && rawType.contains("java.util.List")) {
                response = "[]";
                return gson.fromJson(response, type);
            } else {
                response = "{}";
                return gson.fromJson(response, type);
            }
        } else {
            T result = null;
            try {
                result = ((Class<T>) type).newInstance();
            } catch(InstantiationException e) {
                e.printStackTrace();
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

//    private T getEmptyResult() {
//        T result = null;
//        try {
//            result = ((Class<T>) type).newInstance();
//        } catch(InstantiationException e) {
//            e.printStackTrace();
//        } catch(IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

}