package com.wanshare.wscomponent.album.intent;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wanshare.wscomponent.album.AlbumArouterConstant;
import com.wanshare.wscomponent.album.model.ImageConfig;
import com.wanshare.wscomponent.album.select.PhotoPickerActivity;
import com.wanshare.wscomponent.album.select.SelectModel;

import java.util.ArrayList;

/**
 * 选择照片
 * Created by Venn on 2018/8/25.
 */
public class PhotoPickerIntent {

    private Bundle mBundle;

    public PhotoPickerIntent() {
        mBundle = new Bundle();
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public PhotoPickerIntent setShowCamera(boolean bool){
        mBundle.putBoolean(PhotoPickerActivity.EXTRA_SHOW_CAMERA, bool);
        return this;
    }

    public PhotoPickerIntent setIsCrop(boolean isCrop){
        mBundle.putBoolean(PhotoPickerActivity.EXTRA_SHOW_CROP, isCrop);
        return this;
    }

    public PhotoPickerIntent setMaxTotal(int total){
        mBundle.putInt(PhotoPickerActivity.EXTRA_SELECT_COUNT, total);
        return this;
    }

    /**
     * 选择
     * @param model
     */
    public PhotoPickerIntent setSelectModel(SelectModel model){
        mBundle.putInt(PhotoPickerActivity.EXTRA_SELECT_MODE, Integer.parseInt(model.toString()));
        return this;
    }

    /**
     * 已选择的照片地址
     * @param imagePathis
     */
    public PhotoPickerIntent setSelectedPaths(ArrayList<String> imagePathis){
        mBundle.putStringArrayList(PhotoPickerActivity.EXTRA_DEFAULT_SELECTED_LIST, imagePathis);
        return this;
    }

    /**
     * 显示相册图片的属性
     * @param config
     */
    public PhotoPickerIntent setImageConfig(ImageConfig config){
        mBundle.putParcelable(PhotoPickerActivity.EXTRA_IMAGE_CONFIG, config);
        return this;
    }

    public void start(Activity activity){
        ARouter.getInstance().build(AlbumArouterConstant.ALBUM_PHOTO_PICKER).
                with(mBundle).
                navigation(activity, 101);
    }
}
