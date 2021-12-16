package com.wanshare.wscomponent.album;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.wanshare.wscomponent.dialog.CommonProgressDialog;
import com.wanshare.wscomponent.utils.StatusBarUtil;

public class BaseAlbumActivity extends AppCompatActivity {

    protected Bundle getArgument(){
        return getIntent() == null ? null : getIntent().getExtras();
    }

    protected void initStatus() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);
    }

    protected CommonProgressDialog mDialog;

    protected void showProgress(){
        if (mDialog == null) {
            mDialog = new CommonProgressDialog(this);
        }
        mDialog.show();
    }

    protected void hideProgress(){
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
