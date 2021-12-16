package com.http;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public abstract class BaseRequestBody extends RequestBody {

    private static MediaType MEDIA_TYPE = MediaType.parse("application/json");

    public static final Gson gson = new Gson();

    public static RequestBody create(Object object) {
        return create(MEDIA_TYPE, gson.toJson(object));
    }

    public static RequestBody create(MediaType type, Object object) {
        return create(type, gson.toJson(object));
    }

}
