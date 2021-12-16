package com.cv.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cv.R;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;


/**
 * Created by Richard on 2018/7/26
 * 通用标题栏
 * 包含:返回按钮，标题，右侧文本，右侧image，可通过自定义属性在布局文件中设置，也可以在代码中设置
 * 后续再根据需求添加
 */
public class MyToolbar extends RelativeLayout{
    private static final String TAG = MyToolbar.class.getName();

    ImageView ivLeft;

    TextView tvLeft;

    TextView tvTitle;

    TextView tvRight;

    ImageView ivRight;

    View line;
    int mBackgroundColor;
    int mLineBackgroundColor;
    private String mTitleText;
    private String mRightBtnText;
    private int mRightBtnDrawableId;
    private int mBackBtnDrawableId;
    private boolean mBackBtnVisible = true;
    private int mTitleTextColor;
    private int mRightBtnTextColor;
    private float mTitleTextSize;
    private float mRightBtnTextSize;
    private float mLeftTextSize;
    private int mTitleTextStyle;


    public MyToolbar(@NonNull final Context context) {
        this(context, null);
    }

    public MyToolbar(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolbar(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyle) {
        super(context, attrs, defStyle);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.lib_layout_my_toolbar, this, true);
        initView(view);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyToolbar, 0, 0);
        final int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            final int attr = a.getIndex(i);
            if (attr == R.styleable.MyToolbar_toolbarBackBtnVisible) {
                mBackBtnVisible = a.getBoolean(attr, mBackBtnVisible);

            } else if (attr == R.styleable.MyToolbar_toolbarTitle) {
                mTitleText = a.getString(attr);

            } else if (attr == R.styleable.MyToolbar_toolbarRightBtnText) {
                mRightBtnText = a.getString(attr);

            } else if (attr == R.styleable.MyToolbar_toolbarLeftBtnText) {
                tvLeft.setText(a.getString(attr));
            } else if (attr == R.styleable.MyToolbar_toolbarRightBtnDrawable) {
                mRightBtnDrawableId = a.getResourceId(R.styleable.MyToolbar_toolbarRightBtnDrawable, 0);

            }else if (attr == R.styleable.MyToolbar_toolbarBackBtnDrawable) {
                mBackBtnDrawableId = a.getResourceId(R.styleable.MyToolbar_toolbarBackBtnDrawable, 0);

//            } else if (attr == R.styleable.MyToolbar_toolbarBackgroundColor) {
//                mBackgroundColor = a.getColor(attr, 0);
//                rlRoot.setBackgroundColor(mBackgroundColor);

            }else if (attr == R.styleable.MyToolbar_toolbarLineBackgroundColor) {
                mLineBackgroundColor = a.getColor(attr, 0);
                line.setBackgroundColor(mLineBackgroundColor);

            }  else if (attr == R.styleable.MyToolbar_toolbarTitleColor) {
                mTitleTextColor = a.getColor(attr, 0);

            } else if (attr == R.styleable.MyToolbar_toolbarRightBtnTextColor) {
                mRightBtnTextColor = a.getColor(attr, 0);

            } else if (attr == R.styleable.MyToolbar_toolbarTitleSize) {
                mTitleTextSize = a.getDimension(attr, 0);

            } else if (attr == R.styleable.MyToolbar_toolbarRightBtnTextSize) {
                mRightBtnTextSize = a.getDimension(attr, 0);

            } else if (attr == R.styleable.MyToolbar_toolbarTitleTextStyle) {
                mTitleTextStyle = a.getInteger(attr, 0);

            } else if (attr == R.styleable.MyToolbar_toolbarLeftBtnTextColor) {
                tvLeft.setTextColor(a.getColor(attr, 0));
            } else if (attr == R.styleable.MyToolbar_toolbarLeftBtnTextSize) {
                mLeftTextSize = (int) a.getDimension(attr, 0);
            }
            else {
                Log.d(TAG, "Unknown attribute for " + getClass().toString() + ": " + attr);

            }
        }
        a.recycle();

        initAttrs();
    }

    private void initView(View view) {
        ivLeft = view.findViewById(R.id.btn_back);
        tvTitle = view.findViewById(R.id.tv_title);
        tvRight = view.findViewById(R.id.tv_right);
        tvLeft = view.findViewById(R.id.tv_left_title);
        ivRight = view.findViewById(R.id.btn_right);
        line = view.findViewById(R.id.view_line);
    }

    private void initAttrs() {
//        if (mBackgroundColor != 0) {
//        rlRoot.setBackgroundColor(mBackgroundColor);
//        }
//        if (mBackgroundColor != 0) {
//            setLineBackgroundColor(mLineBackgroundColor);
//        }
        setLeftImageVisible(mBackBtnVisible);
        setTitle(mTitleText);
        setRightTvText(mRightBtnText);
        if (mTitleTextColor != 0) {
            setTitleTextColor(mTitleTextColor);
        }
        if (mRightBtnTextColor != 0) {
            setRightButtonTextColor(mRightBtnTextColor);
        }
        if (mRightBtnDrawableId != 0) {
            ivRight.setVisibility(VISIBLE);
            ivRight.setImageResource(mRightBtnDrawableId);
        } else {
            ivRight.setVisibility(GONE);
        }
        if (mTitleTextSize > 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        }
        if (mTitleTextStyle > 0) {
            switch (mTitleTextStyle) {
                case Typeface.BOLD:
                    tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    break;
                case Typeface.ITALIC:
                    tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    break;
            }
        }
        if (mRightBtnTextSize > 0) {
            tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightBtnTextSize);
        }
        if (mLeftTextSize > 0) {
            tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
        }
        if (mBackBtnDrawableId != 0) {
            ivLeft.setImageResource(mBackBtnDrawableId);
        }
    }


    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvLeft() {
        return tvLeft;
    }


    public void setTitle(@StringRes int stringRes) {
        tvTitle.setText(stringRes);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitleTextColor(int color) {
        tvTitle.setTextColor(color);
    }

    public void setLineBackgroundColor(int color) {
        line.setBackgroundColor(color);
    }

    public void setTitleTextSize(float textSize) {
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public void setLeftImageVisible(final boolean visible) {
        ivLeft.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setLeftTvOnClickListener(final OnClickListener onClickListener) {
        tvLeft.setOnClickListener(onClickListener);
    }

    public void setLeftTvVisible(final boolean visible) {
        tvLeft.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setOnTitleClickListener(final OnClickListener onClickListener) {
        tvTitle.setOnClickListener(onClickListener);
    }

    public void setLeftIvOnClickListener(final OnClickListener onClickListener) {
        ivLeft.setOnClickListener(onClickListener);
    }

    public void setRightTvText(@Nullable final String text) {
        tvRight.setText(text);
//        rightButton.setVisibility(TextUtils.isEmpty(text) ? INVISIBLE : VISIBLE);
    }

    public void setRightTvTextSize(float textSize) {
        tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public void setRightButtonTextColor(int color) {
        tvRight.setTextColor(color);
    }

    public void setRightTvEnable(boolean enable) {
//        rightButton.setTextColor(getResources().getColorStateList(R.color.selector_text_color_toolbar_right_btn));
        tvRight.setEnabled(enable);
    }

    public void setRightTvText(@StringRes final int resId) {
        setRightTvText(getContext().getString(resId));
    }

    public void setRightTvVisible(final boolean visible) {
        tvRight.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setOnRightTvClickListener(@NonNull final OnClickListener onClickListener) {
        tvRight.setOnClickListener(onClickListener);
    }

    public void setRightIvVisible(final boolean visible) {
        ivRight.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setOnRightIvClickListener(@NonNull final OnClickListener onClickListener) {
        ivRight.setOnClickListener(onClickListener);
    }

    public View getLine() {
        return line;
    }

    public void setRightIvImage(int res) {
        ivRight.setImageResource(res);
    }

    public void setRightIvImage(Drawable drawable) {
        ivRight.setImageDrawable(drawable);
    }

    public void setTitleTextStyle(Typeface tf) {
        tvTitle.setTypeface(tf);
    }

    public void setLeftIvImage(int res) {
        ivLeft.setImageResource(res);
    }

    public void setLeftIvImage(Drawable drawable) {
        ivLeft.setImageDrawable(drawable);
    }

    public void setLeftTitle(String text) {
        tvLeft.setText(text);
    }

    public void setLeftTitle(@StringRes int stringId) {
        tvLeft.setText(stringId);
    }

    public TextView getTvRight() {
        return tvRight;
    }

    public void removeUnderline() {
        line.setBackground(null);
    }

    public void hideUnderline() {
        line.setVisibility(View.GONE);
    }

    public void hideLeftContent() {
        ivLeft.setVisibility(View.GONE);
        tvLeft.setVisibility(View.GONE);
    }

    public void setLeftIvMarginDp(int dp) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tvLeft.getLayoutParams();
        if(dp == 0) {
            lp.leftMargin = 0;
        } else {
            lp.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dp, getResources().getDisplayMetrics());
        }
    }
}