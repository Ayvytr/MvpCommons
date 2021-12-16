package com.wanshare.wscomponent.album.select;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.widget.ListPopupWindow;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wanshare.wscomponent.album.AlbumArouterConstant;
import com.wanshare.wscomponent.album.BaseAlbumActivity;
import com.wanshare.wscomponent.album.model.Folder;
import com.wanshare.wscomponent.album.FolderAdapter;
import com.wanshare.wscomponent.album.model.Image;
import com.wanshare.wscomponent.album.ImageCaptureManager;
import com.wanshare.wscomponent.album.model.ImageConfig;
import com.wanshare.wscomponent.album.ImageGridAdapter;
import com.wanshare.wscomponent.album.PhotoPreviewActivity;
import com.wanshare.wscomponent.album.R;
import com.wanshare.wscomponent.album.intent.PhotoPreviewIntent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venn on 2018/8/25.
 */

@Route(path = AlbumArouterConstant.ALBUM_PHOTO_PICKER)
public class PhotoPickerActivity extends BaseAlbumActivity {

    public static final String TAG = PhotoPickerActivity.class.getName();

    private Context mCxt;


    public static final String EXTRA_SELECT_MODE = "select_count_mode";

    public static final int MODE_SINGLE = 0;

    public static final int MODE_MULTI = 1;

    public static final String EXTRA_SELECT_COUNT = "max_select_count";

    public static final int DEFAULT_MAX_TOTAL = 9;

    public static final String EXTRA_SHOW_CAMERA = "show_camera";

    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";

    public static final String EXTRA_IMAGE_CONFIG = "image_config";

    public static final String EXTRA_RESULT = "select_result";

    public static final String EXTRA_SHOW_CROP = "extra_show_crop";

    private ArrayList<String> mResultList = new ArrayList<>();
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    private static final int REQUEST_CODE = 1001;

    private MenuItem mMenuDoneItem;
    private GridView mGridView;
    private View mPopupAnchorView;
    private Button mBtnAlbum;
    private Button mBtnPreview;
    private ImageView mBtnBack;
    private TextView mTvTitle;
    private TextView mTvRightTitle;

    // 最大照片数量
    private ImageCaptureManager captureManager;
    private int mDesireImageCount;
    private ImageConfig imageConfig; // 照片配置

    private ImageGridAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;
    private ListPopupWindow mFolderPopupWindow;

    private boolean mHasFolder = false;
    private boolean mIsShowCamera = false;
    private boolean mIsShowCrop = false;
    private int mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatus();
        setContentView(R.layout.album_activity_photopicker);
        initIntent();
        initView();
    }

    private void initIntent() {
        Bundle bundle = getArgument();
        if (bundle != null) {
            // 选择图片数量
            mDesireImageCount = bundle.getInt(EXTRA_SELECT_COUNT, DEFAULT_MAX_TOTAL);
            // 图片选择模式
            mode = bundle.getInt(EXTRA_SELECT_MODE, MODE_SINGLE);
            // 是否显示照相机
            mIsShowCamera = bundle.getBoolean(EXTRA_SHOW_CAMERA, false);
            mIsShowCrop = bundle.getBoolean(EXTRA_SHOW_CROP, false);
        }
    }

    protected void initView() {
        mTvTitle = findViewById(R.id.tv_title);
        mBtnBack = findViewById(R.id.btn_back);
        mTvRightTitle = findViewById(R.id.tv_right);

        mTvTitle.setText(getString(R.string.album_image));


        mCxt = this;
        captureManager = new ImageCaptureManager(mCxt);
        mGridView = (GridView) findViewById(R.id.grid);
        mGridView.setNumColumns(getNumColnums());

        mPopupAnchorView = findViewById(R.id.photo_picker_footer);
        mBtnAlbum = (Button) findViewById(R.id.btnAlbum);
        mBtnPreview = (Button) findViewById(R.id.btnPreview);

        imageConfig = getIntent().getParcelableExtra(EXTRA_IMAGE_CONFIG);

        // 首次加载所有图片
        getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);


        // 默认选择
        if (mode == MODE_MULTI) {
            ArrayList<String> tmp = getIntent().getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                mResultList.addAll(tmp);
            }
        }


        mImageAdapter = new ImageGridAdapter(mCxt, mIsShowCamera, getItemImageWidth());
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(mode == MODE_MULTI);
        mGridView.setAdapter(mImageAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImageAdapter.isShowCamera()) {
                    // 如果显示照相机，则第一个Grid显示为照相机，处理特殊逻辑
                    if (i == 0) {
                        if (mode == MODE_MULTI) {
                            // 判断选择数量问题
                            if (mDesireImageCount == mResultList.size() - 1) {
                                Toast.makeText(mCxt, R.string.album_msg_amount_limit, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        showCameraAction();
                    } else {
                        // 正常操作
                        Image image = (Image) adapterView.getAdapter().getItem(i);
                        selectImageFromGrid(image, mode);
                    }
                } else {
                    // 正常操作
                    Image image = (Image) adapterView.getAdapter().getItem(i);
                    selectImageFromGrid(image, mode);
                }
            }
        });

        mFolderAdapter = new FolderAdapter(mCxt);

        // 打开相册列表
        mBtnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFolderPopupWindow == null) {
                    createPopupFolderList();
                }

                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });

        // 预览
        mBtnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(mCxt);
                intent.setCurrentItem(0);
                intent.setPhotoPaths(mResultList);
                ARouter.getInstance().build(AlbumArouterConstant.ALBUM_PHOTO_PREVIEW).
                        with(intent.getBundle()).navigation(PhotoPickerActivity.this, PhotoPreviewActivity.REQUEST_PREVIEW);
            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        mTvRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void createPopupFolderList() {

        mFolderPopupWindow = new ListPopupWindow(mCxt);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(ListPopupWindow.MATCH_PARENT);
        mFolderPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);

        // 计算ListPopupWindow内容的高度(忽略mPopupAnchorView.height)，R.layout.item_foloer
        int folderItemViewHeight =
                // 图片高度
                getResources().getDimensionPixelOffset(R.dimen.album_folder_cover_size) +
                        // Padding Top
                        getResources().getDimensionPixelOffset(R.dimen.album_folder_padding) +
                        // Padding Bottom
                        getResources().getDimensionPixelOffset(R.dimen.album_folder_padding);
        int folderViewHeight = mFolderAdapter.getCount() * folderItemViewHeight;

        int screenHeigh = getResources().getDisplayMetrics().heightPixels;
        if (folderViewHeight >= screenHeigh) {
            mFolderPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
        } else {
            mFolderPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        }

        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mFolderAdapter.setSelectIndex(position);

                final int index = position;
                final AdapterView v = parent;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();

                        if (index == 0) {
                            getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            mBtnAlbum.setText(R.string.album_all_image);
                            mImageAdapter.setShowCamera(mIsShowCamera);
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                mImageAdapter.setData(folder.images);
                                mBtnAlbum.setText(folder.name);
                                // 设定默认选择
                                if (mResultList != null && mResultList.size() > 0) {
                                    mImageAdapter.setDefaultSelected(mResultList);
                                }
                            }
                            mImageAdapter.setShowCamera(false);
                        }

                        // 滑动到最初始位置
                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);
            }
        });
    }

    public void onSingleImageSelected(String path) {
        if (mIsShowCrop) {
            startCrop(path);
        }else{
            Intent data = new Intent();
            mResultList.add(path);
            data.putStringArrayListExtra(EXTRA_RESULT, mResultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    private void startCrop(String path) {
        ARouter.getInstance().build(AlbumArouterConstant.ALBUM_PHOTO_CROP).
                withString("path", path).navigation(this, REQUEST_CODE);
    }

    public void onImageSelected(String path) {
        if (!mResultList.contains(path)) {
            mResultList.add(path);
        }
        refreshActionStatus();
    }

    public void onImageUnselected(String path) {
        if (mResultList.contains(path)) {
            mResultList.remove(path);
        }
        refreshActionStatus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相机拍照完成后，返回图片路径
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        mResultList.add(captureManager.getCurrentPhotoPath());
                    }
                    if (mIsShowCrop) {
                        startCrop(captureManager.getCurrentPhotoPath());
                    }else{
                        complete();
                    }

                    break;
                // 预览照片
                case PhotoPreviewActivity.REQUEST_PREVIEW:
                    ArrayList<String> pathArr = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    // 刷新页面
                    if (pathArr != null && pathArr.size() != mResultList.size()) {
                        mResultList = pathArr;
                        refreshActionStatus();
                        mImageAdapter.setDefaultSelected(mResultList);
                    }
                    break;
                case REQUEST_CODE:
                    if (data != null) {
                        String imgPath = data.getStringExtra("img");
                        Intent data1 = new Intent();
                        mResultList.add(imgPath);
                        data1.putStringArrayListExtra(EXTRA_RESULT, mResultList);
                        setResult(RESULT_OK, data1);
                    }
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "on change");

        // 重置列数
        mGridView.setNumColumns(getNumColnums());
        // 重置Item宽度
        mImageAdapter.setItemSize(getItemImageWidth());

        if (mFolderPopupWindow != null) {
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }

            // 重置PopupWindow高度
            int screenHeigh = getResources().getDisplayMetrics().heightPixels;
            mFolderPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
        }

        super.onConfigurationChanged(newConfig);
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            Toast.makeText(mCxt, R.string.album_msg_no_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 选择图片操作
     *
     * @param image
     */
    private void selectImageFromGrid(Image image, int mode) {
        if (image != null) {
            // 多选模式
            if (mode == MODE_MULTI) {
                if (mResultList.contains(image.path)) {
                    mResultList.remove(image.path);
                    onImageUnselected(image.path);
                } else {
                    // 判断选择数量问题
                    if (mDesireImageCount == mResultList.size()) {
                        Toast.makeText(mCxt, R.string.album_msg_amount_limit, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mResultList.add(image.path);
                    onImageSelected(image.path);
                }
                mImageAdapter.select(image);
            } else if (mode == MODE_SINGLE) {
                // 单选模式
                onSingleImageSelected(image.path);
            }
        }
    }

    /**
     * 刷新操作按钮状态
     */
    private void refreshActionStatus() {
        if (mResultList.contains("000000")) {
            mResultList.remove("000000");
        }
        String text = getString(R.string.album_done_with_count, mResultList.size(), mDesireImageCount);
        mTvRightTitle.setText(text);
        boolean hasSelected = mResultList.size() > 0;
        mTvRightTitle.setVisibility(hasSelected ? View.VISIBLE : View.GONE);
        mBtnPreview.setEnabled(hasSelected);
        if (hasSelected) {
            mBtnPreview.setText(getResources().getString(R.string.album_preview) + "(" + (mResultList.size()) + ")");
        } else {
            mBtnPreview.setText(getResources().getString(R.string.album_preview));
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            // 根据图片设置参数新增验证条件
            StringBuilder selectionArgs = new StringBuilder();

            if (imageConfig != null) {
                if (imageConfig.minWidth != 0) {
                    selectionArgs.append(MediaStore.Images.Media.WIDTH + " >= " + imageConfig.minWidth);
                }

                if (imageConfig.minHeight != 0) {
                    selectionArgs.append("".equals(selectionArgs.toString()) ? "" : " and ");
                    selectionArgs.append(MediaStore.Images.Media.HEIGHT + " >= " + imageConfig.minHeight);
                }

                if (imageConfig.minSize != 0f) {
                    selectionArgs.append("".equals(selectionArgs.toString()) ? "" : " and ");
                    selectionArgs.append(MediaStore.Images.Media.SIZE + " >= " + imageConfig.minSize);
                }

                if (imageConfig.mimeType != null) {
                    selectionArgs.append(" and (");
                    for (int i = 0, len = imageConfig.mimeType.length; i < len; i++) {
                        if (i != 0) {
                            selectionArgs.append(" or ");
                        }
                        selectionArgs.append(MediaStore.Images.Media.MIME_TYPE + " = '" + imageConfig.mimeType[i] + "'");
                    }
                    selectionArgs.append(")");
                }
            }

            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(mCxt,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        selectionArgs.toString(), null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                String selectionStr = selectionArgs.toString();
                if (!"".equals(selectionStr)) {
                    selectionStr += " and" + selectionStr;
                }
                CursorLoader cursorLoader = new CursorLoader(mCxt,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'" + selectionStr, null,
                        IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<Image> images = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));

                        Image image = new Image(path, name, dateTime);
                        images.add(image);
                        if (!mHasFolder) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!mResultFolder.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.images = imageList;
                                mResultFolder.add(folder);
                            } else {
                                // 更新
                                Folder f = mResultFolder.get(mResultFolder.indexOf(folder));
                                f.images.add(image);
                            }
                        }

                    } while (data.moveToNext());

                    mImageAdapter.setData(images);

                    // 设定默认选择
                    if (mResultList != null && mResultList.size() > 0) {
                        mImageAdapter.setDefaultSelected(mResultList);
                    }

                    mFolderAdapter.setData(mResultFolder);
                    mHasFolder = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /**
     * 获取GridView Item宽度
     *
     * @return
     */
    private int getItemImageWidth() {
        int cols = getNumColnums();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.album_space_size);
        return (screenWidth - columnSpace * (cols - 1)) / cols;
    }

    /**
     * 根据屏幕宽度与密度计算GridView显示的列数， 最少为三列
     *
     * @return
     */
    private int getNumColnums() {
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        return cols < 3 ? 3 : cols;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if (item.getItemId() == R.id.action_picker_done) {
            complete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 返回已选择的图片数据
    private void complete() {
        Intent data = new Intent();
        data.putStringArrayListExtra(EXTRA_RESULT, mResultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
