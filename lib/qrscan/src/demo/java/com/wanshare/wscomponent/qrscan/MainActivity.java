package com.wanshare.wscomponent.qrscan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wanshare.wscomponent.qrscan.activity.CaptureActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

public class MainActivity extends Activity {
    public static final int REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onScan(View view) {
        if(!AndPermission.hasPermissions(this, Manifest.permission.CAMERA)) {
            AndPermission.with(this)
                         .runtime()
                         .permission(Permission.CAMERA)
                         .onGranted(new Action<List<String>>() {
                             @Override
                             public void onAction(List<String> data) {
                                 Intent intent = new Intent(getApplication(), CaptureActivity.class);
                                 startActivityForResult(intent, REQUEST_CODE);
                             }
                         })
                         .onDenied(new Action<List<String>>() {
                             @Override
                             public void onAction(List<String> data) {

                             }
                         }).start();
        }
        else{
            Intent intent = new Intent(getApplication(), CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if(requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if(null != data) {
                Bundle bundle = data.getExtras();
                if(bundle == null) {
                    return;
                }
                if(resultCode == RESULT_OK) {
                    String result = bundle.getString(QrUtils.RESULT);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
