package com.common.base;

import android.content.Intent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.common.R;
import com.common.R2;
import com.common.constant.CommonArouterCanstant;
import com.common.constant.IntentConstant;
import com.cv.webview.ProgressWebView;

import androidx.annotation.NonNull;
import butterknife.BindView;

/**
 * Created by Jason on 2018/8/7.
 */

@Route(path = CommonArouterCanstant.COMMON_WEBVIEW)
public class CommonWebViewActivity extends BaseActivity {

    @BindView(R2.id.progressWebView)
    ProgressWebView mProgressWebView;
    private String mUrl;

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            mUrl = intent.getStringExtra(IntentConstant.WEB_URL);
        }
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.common_activity_common_webview;
    }

    @Override
    protected void initView() {
        mProgressWebView.loadUrl(mUrl);
    }

    @Override
    protected void initData() {

        mProgressWebView.setOnTitleChangedListener(new ProgressWebView.OnTitleChangedListener() {
            @Override
            public void onTitleChanged(ProgressWebView progressWebView, String title) {
                mMyToolbar.setTitle(title);
            }
        });

//        mProgressWebView.setShowProgressBar(false);
    }


    @Override
    protected void onBackButton(View view) {
        backPage();
    }

    @Override
    public void onBackPressed() {
        backPage();
    }

    private void backPage(){
        if(mProgressWebView.getWebView().canGoBack()) {
            mProgressWebView.getWebView().goBack();
        }else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressWebView.destroy();
    }
}
