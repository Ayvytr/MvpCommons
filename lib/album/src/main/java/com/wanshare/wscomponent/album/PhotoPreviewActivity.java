package com.wanshare.wscomponent.album;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wanshare.wscomponent.album.widget.ViewPagerFixed;

import java.util.ArrayList;

import com.wanshare.wscomponent.utils.StatusBarUtil;

/**
 * Created by Venn on 2018/08/24
 */
@Route(path = AlbumArouterConstant.ALBUM_PHOTO_PREVIEW)
public class PhotoPreviewActivity extends BaseAlbumActivity implements PhotoPagerAdapter.PhotoViewClickListener{

    public static final String EXTRA_PHOTOS = "extra_photos";
    public static final String EXTRA_CURRENT_ITEM = "extra_current_item";

    /** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合  */
    public static final String EXTRA_RESULT = "preview_result";

    /** 预览请求状态码 */
    public static final int REQUEST_PREVIEW = 99;

    private ArrayList<String> paths;
    private ViewPagerFixed mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatus();
        setContentView(R.layout.album_activity_image_preview);
        initIntent();
        initViews();

        paths = new ArrayList<>();
        ArrayList<String> pathArr = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
        if(pathArr != null){
            paths.addAll(pathArr);
        }

        mPagerAdapter = new PhotoPagerAdapter(this, paths);
        mPagerAdapter.setPhotoViewClickListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(5);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateActionBarTitle();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        updateActionBarTitle();
    }

    private void initIntent(){
        Bundle bundle = getArgument();
        if (bundle != null) {
            currentItem = bundle.getInt(EXTRA_CURRENT_ITEM, 0);
        }
    }

    protected void initStatus() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);
    }

    private void initViews(){
        mViewPager = (ViewPagerFixed) findViewById(R.id.vp_photos);
    }

    @Override
    public void OnPhotoTapListener(View view, float v, float v1) {
        onBackPressed();
    }

    public void updateActionBarTitle() {
        getSupportActionBar().setTitle(
                getString(R.string.album_image_index, mViewPager.getCurrentItem() + 1, paths.size()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, paths);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_menu_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        // 删除当前照片
        if(item.getItemId() == R.id.action_discard){
            final int index = mViewPager.getCurrentItem();
            final String deletedPath =  paths.get(index);
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.album_deleted_a_photo,
                    Snackbar.LENGTH_LONG);
            if(paths.size() <= 1){
                // 最后一张照片弹出删除提示
                // show confirm dialog
                new AlertDialog.Builder(this)
                        .setTitle(R.string.album_confirm_to_delete)
                        .setPositiveButton(R.string.album_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                paths.remove(index);
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(R.string.album_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }else{
                snackbar.show();
                paths.remove(index);
                mPagerAdapter.notifyDataSetChanged();
            }

            snackbar.setAction(R.string.album_undo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (paths.size() > 0) {
                        paths.add(index, deletedPath);
                    } else {
                        paths.add(deletedPath);
                    }
                    mPagerAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(index, true);
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}
