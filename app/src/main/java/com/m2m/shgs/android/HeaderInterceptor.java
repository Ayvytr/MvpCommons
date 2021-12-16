package com.m2m.shgs.android;

import com.base.utils.DeviceUtil;
import com.common.manager.UserManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加header和返回code处理
 * </br>
 * Date: 2019/3/20 17:40
 *
 * @author hemin
 */
public class HeaderInterceptor implements Interceptor {
    private static String userAgent;

    static {
        StringBuffer buffer = new StringBuffer();
        buffer.append("App ");
        buffer.append(DeviceUtil.getAppVersion(MyApplication.getInstance().getApplicationContext()));
        buffer.append(" (android ");
        buffer.append(DeviceUtil.getBuildVersion()).append(")");
        userAgent = buffer.toString();
//        L.e(userAgent);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response;
        Request.Builder builder = chain.request().newBuilder()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("User-Agent", userAgent
//                ).addHeader("Language", LanguageManager.getInstance().getLanguageFlag()
                );
        if (UserManager.isLogin()) {
            String token = UserManager.getToken();
            builder.addHeader("Authorization", "Bearer " + token);
        }
        response = chain.proceed(builder.build());

//        // 将code为204  header X-MAINTAINANCE:true 的返回转换成601处理
//        if (response != null && response.code() == 204 && "true".equals(response.header("X-MAINTAINANCE"))) {
//            return response.newBuilder().code(ProtocolException.CODE_601).build();
//        }

        return response;
    }

}
