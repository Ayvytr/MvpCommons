package com.wanshare.wscomponent.album.intent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wanshare.wscomponent.album.PhotoPreviewActivity;

import java.util.ArrayList;

/**
 * 预览照片
 * Created by Venn on 2018/08/24
 */
public class PhotoPreviewIntent{

    private Bundle mBundle;

    public PhotoPreviewIntent(Context packageContext) {
        mBundle = new Bundle();
    }

    public Bundle getBundle() {
        return mBundle;
    }

    /**
     * 照片地址
     * @param paths
     */
    public void setPhotoPaths(ArrayList<String> paths){
        mBundle.putStringArrayList(PhotoPreviewActivity.EXTRA_PHOTOS, paths);
    }

    /**
     * 当前照片的下标
     * @param currentItem
     */
    public void setCurrentItem(int currentItem){
        mBundle.putInt(PhotoPreviewActivity.EXTRA_CURRENT_ITEM, currentItem);
    }
}
