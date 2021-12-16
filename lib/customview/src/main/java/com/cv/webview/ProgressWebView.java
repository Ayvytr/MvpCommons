package com.cv.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cv.R;

import java.lang.reflect.Field;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 带进度条的WebView.
 * <p>
 * 添加了WebView释放的代码,,
 * <p>
 * Activity中onBackPressed页面回退功能需要在Activity中添加
 *
 * @author wangdunwei
 * @date 2018/7/28
 */
public class ProgressWebView extends LinearLayout {

    public static final int PROGRESS_100 = 100;
    private ProgressBar progressBar;
    private WebView webView;

    /**
     * 显示加载进度条。 true:显示加载中进度条（不是说这个设置为true了就马上显示了，而是说网页正在加载中时会显示，加载完成隐藏）
     * false:全程不显示加载中进度条
     */
    private boolean isShowProgressBar = true;

    private OnTitleChangedListener onTitleChangedListener;

    private SubWebChromeClient mSubWebChromeClient;

    public void setSubWebChromeClient(SubWebChromeClient subWebChromeClient) {
        mSubWebChromeClient = subWebChromeClient;
    }

    private SubWebClient mSubWebClient;

    public void setSubWebClient(SubWebClient subWebClient) {
        mSubWebClient = subWebClient;
    }

    public ProgressWebView(Context context) {
        this(context, null);
    }

    public ProgressWebView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressWebView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);

        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.lib_progress_bg));
        final float scale = context.getResources().getDisplayMetrics().density;
        addView(progressBar, LayoutParams.MATCH_PARENT, (int) (3 * scale + 0.5F));

        webView = new WebView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        addView(webView, lp);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        // 开启 localStorage
        webSettings.setDomStorageEnabled(true);
        String appCachePath = context.getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);

        // 防止跳出浏览器
        webView.setWebViewClient(new WebViewClient() {
            // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;

//                WebView.HitTestResult hitTestResult = view.getHitTestResult();
//                //hitTestResult==null解决重定向问题
//                if (!TextUtils.isEmpty(url) && hitTestResult == null) {
//                    view.loadUrl(url);
//                    return true;
//                }

                if (mSubWebClient == null) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    return mSubWebClient.shouldOverrideUrlLoading(view, url);
                }

            }

            //重写此方法可以让webview处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  //接受所有证书
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (isShowProgressBar) {
                    if (newProgress == PROGRESS_100) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setProgress(newProgress);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
            //For Android  >= 5.0
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mSubWebChromeClient != null) {
                    mSubWebChromeClient
                            .onShowFileChooser(webView, filePathCallback, fileChooserParams);
                }
                return true;
            }

            //For Android  >= 4.1
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                if (mSubWebChromeClient != null) {
                    mSubWebChromeClient.openFileChooser(valueCallback, acceptType, capture);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (onTitleChangedListener != null) {
                    onTitleChangedListener.onTitleChanged(ProgressWebView.this, title);
                }
            }
        });

        // 设置https和http混合模式下都正常显示，解决图片地址为http开头时不能正常显示的问题
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
    }

    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    public void LoadDataWithBaseUrl(String data) {
        loadDataWithBaseUrl(null, data, "text/html", "UTF-8", null);
    }

    public void loadDataWithBaseUrl(String url, String data, String mimeType, String encoding,
                         String historyUrl) {
        webView.loadDataWithBaseURL(url, data, mimeType, encoding, historyUrl);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public void releaseAllWebViewCallback() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            try {
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 销毁这个View的方法，主要是为了释放WebView。请在Activity.onDestroy() 方法调用。
     */
    public void destroy() {
        releaseAllWebViewCallback();
        removeAllViews();
        webView.setVisibility(View.GONE);
        webView.stopLoading();
        webView.getSettings().setJavaScriptEnabled(false);
        webView.setTag(null);
        webView.clearHistory();
        webView.clearView();
        webView.loadUrl("about:blank");
        webView.removeAllViews();
        webView.destroy();
        webView = null;
    }

    /**
     * 显示/隐藏进度条，隐藏进度条后加载中也不会显示进度条
     *
     * @param showProgressBar true:显示加载中进度条，false：不显示进度条
     */
    public void setShowProgressBar(boolean showProgressBar) {
        isShowProgressBar = showProgressBar;
    }


    public void setOnTitleChangedListener(OnTitleChangedListener l) {
        this.onTitleChangedListener = l;
    }

    public interface OnTitleChangedListener {
        /**
         * 标题变化监听器
         *
         * @param progressWebView ProgressWebView
         * @param title           标题
         */
        void onTitleChanged(ProgressWebView progressWebView, String title);
    }

    public interface SubWebClient{
         boolean shouldOverrideUrlLoading(WebView view, String url);

    }

    public interface SubWebChromeClient {

         boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams);

         void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture);
    }
}
