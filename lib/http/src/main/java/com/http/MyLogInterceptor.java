package com.http;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 自定义http日志打印
 * </br>
 * Date: 2019/3/20 16:00
 *
 * @author hemin
 */
public class MyLogInterceptor implements Interceptor {

    private static final String TAG = "MyLogInterceptor";
    private static final Charset UTF8 = Charset.forName("UTF-8"); //urf8编码

    @Override
    public Response intercept(Chain chain) throws IOException {  //实现Interceptor接口方法
        Log.d(TAG, "before chain,request()");
        Request request = chain.request();  //获取request

        Response response;
        try {
            long t1 = System.nanoTime();
            response = chain.proceed(request); //OkHttp链式调用
            long t2 = System.nanoTime();
            double time = (t2 - t1) / 1e6d;   //用请求后的时间减去请求前的时间得到耗时

            String type = request.method();
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            String logStr = "\n--------------------".concat("  \n--------------------begin--------------------\n")
                    .concat(type)
                    .concat("\nnetwork code->").concat(response.code() + "")
                    .concat("\nurl->").concat(response.request().url() + "")
                    .concat("\ntime->").concat(time + "")
                    .concat("\nrequest headers->\n").concat(response.request().headers() + "")
                    .concat("request body->").concat(bodyToString(response.request().body()))
                    .concat("\nresponse body->").concat(buffer.clone().readString(UTF8)) //响应体转String
                    .concat("\n--------------------end--------------------\n\n");
            Log.i(TAG, logStr);

        } catch (Exception e) {
            throw e; //不拦截exception，由上层处理网络错误
        }
        return response;
    }

    private String bodyToJSON(String s) {
        try {
            if(s==null){
                return "";
            }else if(s.startsWith("{")){
                return  new JSONObject(s).toString(4);
            }else if(s.startsWith("[")){
                return  new JSONArray(s).toString(4);
            }else{
                return s;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return s;
        }
    }

    /**
     * 请求体转String
     *
     * @param request
     * @return
     */
    private static String bodyToString(final RequestBody request) {
        try {
            if (request == null) {
                return "[]";
            }
            final Buffer buffer = new Buffer();
            request.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}