package com.bumptech.glide.load.engine;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.SafeKeyGenerator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.EmptySignature;

import java.io.File;
import java.io.IOException;

import androidx.annotation.DrawableRes;

/**
 * Glide帮助类. 获取缓存方法getCachedFile因为DataCacheKey不是public，只能写在同一包路径
 * @author Administrator
 */
public class ImageHelper {
    public static final String DEF_AVATAR_FILE_NAME = "def_avatar.png";


    public static File getCachedFile(Context context, String url) {
        DataCacheKey dataCacheKey = new DataCacheKey(new GlideUrl(url), EmptySignature.obtain());
        SafeKeyGenerator safeKeyGenerator = new SafeKeyGenerator();
        String safeKey = safeKeyGenerator.getSafeKey(dataCacheKey);
        try {
            int cacheSize = 10 * 1024 * 1024;
            DiskLruCache diskLruCache = DiskLruCache
                    .open(new File(context.getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR),
                            1, 1, cacheSize);
            DiskLruCache.Value value = diskLruCache.get(safeKey);
            if(value != null) {
                return value.getFile(0);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
//
//    public static void loadAvatar(Context context, String avatar, ImageView iv) {
//        Glide.with(context)
//             .load(avatar)
//             .placeholder(R.mipmap.ic_avatar)
//             .error(R.mipmap.ic_avatar)
//             .circleCrop()
//             .into(iv);
//    }

    public static void load(Context context, String url, @DrawableRes int errorDrawableId,
                                 ImageView iv) {
        Glide.with(context)
             .load(url)
             .error(errorDrawableId)
             .into(iv);
    }

    public static void load(Context context, String url, @DrawableRes int errorDrawableId,
            @DrawableRes int placeHolderDrawableId, ImageView iv) {
        Glide.with(context)
                .load(url)
                .error(errorDrawableId)
                .placeholder(placeHolderDrawableId)
                .into(iv);
    }
}
