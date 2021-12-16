package com.wanshare.wscomponent.album;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.wanshare.wscomponent.album.select.PhotoPickerActivity;
import com.wanshare.wscomponent.album.intent.PhotoPickerIntent;
import com.wanshare.wscomponent.album.select.SelectModel;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SelectModel mSelectModel;
    private boolean mIsCrop;
    private int total = 1;

    private ImageView mIvAlbumTest;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIvAlbumTest = findViewById(R.id.iv_album_test);

        initArouter();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


    }

    public void single(View view) {
        mIsCrop = false;
        mSelectModel = SelectModel.SINGLE;
        onStorage();
    }

    public void singleCrop(View view) {
        mIsCrop = true;
        mSelectModel = SelectModel.SINGLE;
        onStorage();
    }

    public void multi(View view) {
        mIsCrop = false;
        mSelectModel = SelectModel.MULTI;
        total = 9;
        onStorage();
    }

    private void initArouter() {
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this.getApplication());
    }


    /**
     * @deprecated 请使用下方 onContactPermission 或者 onReadSmsPermission 中的权限申请流程
     */
    @Deprecated
    public void onStorage() {
        //AndPermission本身使用方法
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE, Permission.Group.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        gotoPhoto();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Toast.makeText(MainActivity.this, "权限已拒绝", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    private void gotoPhoto() {
        PhotoPickerIntent pt = new PhotoPickerIntent();
        pt.setSelectModel(mSelectModel);
        pt.setMaxTotal(total);
        pt.setIsCrop(mIsCrop);
        pt.setShowCamera(true); // 是否显示拍照， 默认false
        pt.start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (data != null) {
                ArrayList<String> imageLists = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                if (imageLists != null && imageLists.size() > 0) {
                    String path = imageLists.get(0);
                    final File file = new File(path);
                    if (file.exists()) {
                        mIvAlbumTest.setImageBitmap(null);
                        Glide.with(this)
                                .load(file)
                                .into(mIvAlbumTest);
                    }
                }
            }
        }
    }
}
