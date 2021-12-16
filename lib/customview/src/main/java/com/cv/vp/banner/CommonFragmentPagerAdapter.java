package com.cv.vp.banner;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * 通用fragment的ViewPagerAdapter
 * </br>
 * Date: 2018/6/21 17:43
 *
 * @author hemin
 */
public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    private String[] mTitles;

    public CommonFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mFragments = list;
    }

    public void updateData(List<Fragment> data) {
        if (null == mFragments) {
            mFragments = new ArrayList<>();
        } else {
            mFragments.clear();
        }
        mFragments.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment f = (Fragment) super.instantiateItem(container, position);
        // fragment重新创建后替换列表中对象
        mFragments.set(position, f);
        return f;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < 0 || mFragments == null || position >= mFragments.size()) {
            return null;
        }
        return mFragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null && position < mTitles.length && position >= 0) {
            return mTitles[position];
        }
        return super.getPageTitle(position);
    }

    public void setTitles(String[] titles) {
        this.mTitles = titles;
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}