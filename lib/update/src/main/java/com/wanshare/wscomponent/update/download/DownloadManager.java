package com.wanshare.wscomponent.update.download;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadManager {

    public DownloadManager() {
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private Handler mDelivery;     //主线程返回

    private Call downCall;         //下载的call

    private static OkHttpClient mOkHttpClient;

    private static DownloadManager mInstance;    //单例
    private int mOldProgress = 0;

    public static DownloadManager getInstance() {
        if (mInstance == null) {
            mInstance = new DownloadManager();
        }
        return mInstance;
    }


    //synchronized修饰的静态方法锁定的是这个类的所有对象,保证在同一时刻最多只有一个线程执行该段代码
    public synchronized OkHttpClient getOkHttpClient() {
        if (null == mOkHttpClient) {
            try {
                mOkHttpClient = newOkHttpClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mOkHttpClient;
    }

    /**
     * 创建okhttp
     * @return
     * @throws Exception
     */
    private OkHttpClient newOkHttpClient() throws Exception {

        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();

        return mOkHttpClient;
    }


    /**
     * 异步下载文件
     * @param url      文件的下载地址
     * @param fileName 文件路径
     * @param callback
     */
    private void okHttpDownload(final String url, final String fileName, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();

        downCall = getOkHttpClient().newCall(request);
        downCall.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long current = 0;
                    long total = response.body().contentLength();

                    is = response.body().byteStream();
                    File file = new File(fileName);
                    if (!file.exists()) {
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        file.createNewFile();
                    }
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        Log.d("----------------",total + "------------"+ current);
                        sendProgressCallBack(total, current, callback);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }




    //下载失败ui线程回调
    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    //下载成功ui线程回调
    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }


    //下载回调接口
    public static abstract class ResultCallback<T> {

        //下载错误
        public abstract void onError(Request request, Exception e);

        //下载成功
        public abstract void onResponse(T response);

        //下载进度
        public abstract void onProgress(long total, long current);
    }


    /**
     * 进度信息ui线程回调
     *
     * @param total    总计大小
     * @param current  当前进度
     * @param callBack
     * @param <T>
     */
    private <T> void sendProgressCallBack(final long total, final long current, final ResultCallback<T> callBack) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onProgress(total, current);
                }
            }
        });
    }


    /**
     * 下载文件
     *
     * @param url 文件链接
     * @param fileName 文件名称
     * @param callback 回调
     */
    public static void downloadApk(String url, String fileName, ResultCallback callback) {
        getInstance().okHttpDownload(url, fileName, callback);
    }


    /**
     * 取消下载
     */
    public static void cancelDown() {
        if (getInstance().downCall != null) {
            getInstance().downCall.cancel();
        }
    }


}
