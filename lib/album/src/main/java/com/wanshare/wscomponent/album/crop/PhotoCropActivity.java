package com.wanshare.wscomponent.album.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wanshare.wscomponent.album.AlbumArouterConstant;
import com.wanshare.wscomponent.album.BaseAlbumActivity;
import com.wanshare.wscomponent.album.R;
import com.wanshare.wscomponent.album.widget.ClipImageView;
import com.wanshare.wscomponent.utils.DateUtil;
import com.wanshare.wscomponent.utils.FileUtil;

import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Venn on 2018/8/25.
 */
@Route(path = AlbumArouterConstant.ALBUM_PHOTO_CROP)
public class PhotoCropActivity extends BaseAlbumActivity {

    ClipImageView mImageBit;
    ImageView mBtnBack;
    TextView mTvTitle;
    TextView mBtnRight;

    private String mOutput;
    private String mInput;
    private int mMaxWidth;

    // 图片被旋转的角度
    private int mDegree;
    // 大图被设置之前的缩放比例
    private int mSampleSize;
    private int mSourceWidth;
    private int mSourceHeight;
    private int mRequestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatus();
        setContentView(R.layout.album_activity_image_crop_view);
        initIntent();
        initView();
    }

    protected void initIntent() {
        Bundle bundle = getArgument();
        if (bundle != null) {
            mInput = bundle.getString("path");
            mRequestCode = bundle.getInt("requestCode");
        }
        mOutput = FileUtil.changeFile(FileUtil.getSDCardAppPath(), DateUtil.getCurrentDate() + ".png").getPath();
        mMaxWidth = 600;
    }

    protected void initView() {
        mImageBit = findViewById(R.id.image_bit);
        mBtnBack = findViewById(R.id.btn_back);
        mTvTitle = findViewById(R.id.tv_title);
        mBtnRight = findViewById(R.id.tv_right);

        mTvTitle.setText(R.string.album_image_crop);
        mBtnRight.setText(R.string.album_yes);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipImage();
            }
        });

        mImageBit.setAspect(3, 3);
        mImageBit.setMaxOutputWidth(mMaxWidth);
        setImageAndClipParams(); //大图裁剪
    }



    private void clipImage() {
        if (mOutput != null) {
            showProgress();
            new ClipImageAsyncTask().execute();
        } else {
            finish();
        }
    }

    private class ClipImageAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mOutput);
                Bitmap bitmap = createClippedBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            } catch (Exception e) {
                Log.d("PhotoCropActivity", getString(R.string.album_image_crop_fail));
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent();
            intent.putExtra("img", mOutput);
            setResult(RESULT_OK, intent);
            finish();
            hideProgress();
        }
    }

    private Bitmap createClippedBitmap() {
        if (mSampleSize <= 1) {
            return mImageBit.clip();
        }

        // 获取缩放位移后的矩阵值
        final float[] matrixValues = mImageBit.getClipMatrixValues();
        final float scale = matrixValues[Matrix.MSCALE_X];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        // 获取在显示的图片中裁剪的位置
        final Rect border = mImageBit.getClipBorder();
        final float cropX = ((-transX + border.left) / scale) * mSampleSize;
        final float cropY = ((-transY + border.top) / scale) * mSampleSize;
        final float cropWidth = (border.width() / scale) * mSampleSize;
        final float cropHeight = (border.height() / scale) * mSampleSize;

        // 获取在旋转之前的裁剪位置
        final RectF srcRect = new RectF(cropX, cropY, cropX + cropWidth, cropY + cropHeight);
        final Rect clipRect = getRealRect(srcRect);

        final BitmapFactory.Options ops = new BitmapFactory.Options();
        final Matrix outputMatrix = new Matrix();

        outputMatrix.setRotate(mDegree);
        // 如果裁剪之后的图片宽高仍然太大,则进行缩小
        if (mMaxWidth > 0 && cropWidth > mMaxWidth) {
            ops.inSampleSize = findBestSample((int) cropWidth, mMaxWidth);

            final float outputScale = mMaxWidth / (cropWidth / ops.inSampleSize);
            outputMatrix.postScale(outputScale, outputScale);
        }

        // 裁剪
        BitmapRegionDecoder decoder = null;
        try {
            decoder = BitmapRegionDecoder.newInstance(mInput, false);
            final Bitmap source = decoder.decodeRegion(clipRect, ops);
            recycleImageViewBitmap();
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), outputMatrix, false);
        } catch (Exception e) {
            return mImageBit.clip();
        } finally {
            if (decoder != null && !decoder.isRecycled()) {
                decoder.recycle();
            }
        }
    }

    private Rect getRealRect(RectF srcRect) {
        switch (mDegree) {
            case 90:
                return new Rect((int) srcRect.top, (int) (mSourceHeight - srcRect.right),
                        (int) srcRect.bottom, (int) (mSourceHeight - srcRect.left));
            case 180:
                return new Rect((int) (mSourceWidth - srcRect.right), (int) (mSourceHeight - srcRect.bottom),
                        (int) (mSourceWidth - srcRect.left), (int) (mSourceHeight - srcRect.top));
            case 270:
                return new Rect((int) (mSourceWidth - srcRect.bottom), (int) srcRect.left,
                        (int) (mSourceWidth - srcRect.top), (int) srcRect.right);
            default:
                return new Rect((int) srcRect.left, (int) srcRect.top, (int) srcRect.right, (int) srcRect.bottom);
        }
    }

    private void recycleImageViewBitmap() {
        mImageBit.post(new Runnable() {
            @Override
            public void run() {
                mImageBit.setImageBitmap(null);
            }
        });
    }

    private void setImageAndClipParams() {
        mImageBit.post(new Runnable() {
            @Override
            public void run() {
                mImageBit.setMaxOutputWidth(mMaxWidth);

                mDegree = readPictureDegree(mInput);

                final boolean isRotate = (mDegree == 90 || mDegree == 270);

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mInput, options);

                mSourceWidth = options.outWidth;
                mSourceHeight = options.outHeight;

                // 如果图片被旋转，则宽高度置换
                int w = isRotate ? options.outHeight : options.outWidth;

                // 裁剪是宽高比例3:2，只考虑宽度情况，这里按border宽度的两倍来计算缩放。
                mSampleSize = findBestSample(w, mImageBit.getClipBorder().width());

                options.inJustDecodeBounds = false;
                options.inSampleSize = mSampleSize;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                final Bitmap source = BitmapFactory.decodeFile(mInput, options);

                // 解决图片被旋转的问题
                Bitmap target;
                if (mDegree == 0) {
                    target = source;
                } else {
                    final Matrix matrix = new Matrix();
                    matrix.postRotate(mDegree);
                    target = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
                    if (target != source && !source.isRecycled()) {
                        source.recycle();
                    }
                }
                mImageBit.setImageBitmap(target);
            }
        });
    }

    /**
     * 计算最好的采样大小。
     *
     * @param origin 当前宽度
     * @param target 限定宽度
     * @return sampleSize
     */
    private static int findBestSample(int origin, int target) {
        int sample = 1;
        for (int out = origin / 2; out > target; out /= 2) {
            sample *= 2;
        }
        return sample;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}
