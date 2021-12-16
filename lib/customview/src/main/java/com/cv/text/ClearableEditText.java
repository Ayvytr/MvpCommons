package com.cv.text;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cv.R;
import com.cv.text.watcher.SimpleTextWatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * 可点击清除按钮清除文本的EditText
 * 最新修改，修改字体Padding，请使用textPadding，textPaddingLeft, textPaddingRight, textPaddingTop, textPaddingBottom.
 *
 * @author wangdunwei
 * @date 2018/5/7
 */
public class ClearableEditText extends LinearLayout {

    private EditText inputEt;
    private ImageView clearIv;
    private ImageButton eyePwdIb;

    private TextWatcher textWatcher;
    private TextWatcher inputNameTextWatcher;

    private OnFocusChangeCustomListener mFocusChangeListener;

    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public interface OnFocusChangeCustomListener {
        void focusChange(boolean hasFocus);
    }

    public void setOnFocusChangeCustomListener(OnFocusChangeCustomListener focusChangeListener) {
        this.mFocusChangeListener = focusChangeListener;
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        View.inflate(getContext(), R.layout.common_layout_clearable_edittext, this);
        inputEt = findViewById(R.id.et_input);
        clearIv = findViewById(R.id.iv_clear);
        eyePwdIb = findViewById(R.id.ib_eye_pwd);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                clearIv.setVisibility(s.toString().isEmpty() ? View.GONE : View.VISIBLE);
            }
        };
        clearIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEt.setText(null);
            }
        });

        if(attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ClearAbleEditText, defStyleAttr, 0);
            setEnabled(ta.getBoolean(R.styleable.ClearAbleEditText_android_enabled, true));
            inputEt.setText(ta.getString(R.styleable.ClearAbleEditText_android_text));

            final int inputType = ta.getInt(R.styleable.ClearAbleEditText_android_inputType, InputType.TYPE_CLASS_TEXT);
            inputEt.setInputType(inputType);

            boolean eyePwd = ta.getBoolean(R.styleable.ClearAbleEditText_eye_pwd, false);
            if(eyePwd) {
                eyePwdIb.setVisibility(VISIBLE);
                eyePwdIb.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eyePwdIb.setSelected(!eyePwdIb.isSelected());
                        inputEt.setTransformationMethod(
                                eyePwdIb.isSelected() ? HideReturnsTransformationMethod.getInstance() :
                                        PasswordTransformationMethod.getInstance());
                        inputEt.setSelection(inputEt.getText().length());
                    }
                });
                inputEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                eyePwdIb.setVisibility(GONE);
                inputEt.addTextChangedListener(textWatcher);
            }

            float textSize = ta.getDimension(R.styleable.ClearAbleEditText_android_textSize, 0f);
            if(textSize > 0) {
                inputEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            inputEt.setHint(ta.getString(R.styleable.ClearAbleEditText_android_hint));

            ColorStateList colorStateList = ta.getColorStateList(R.styleable.ClearAbleEditText_android_textColor);
            if(colorStateList != null) {
                inputEt.setTextColor(colorStateList);
            }

            colorStateList = ta.getColorStateList(R.styleable.ClearAbleEditText_android_textColorHint);
            if(colorStateList != null) {
                inputEt.setHintTextColor(colorStateList);
            }

            inputEt.setBackgroundDrawable(ta.getDrawable(R.styleable.ClearAbleEditText_android_background));
            //输入长度限制
            int maxLength = ta.getInt(R.styleable.ClearAbleEditText_android_maxLength, -1);
            if(maxLength >= 0) {
                inputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            } else {
                inputEt.setFilters(new InputFilter[0]);
            }

            int paddingTop, paddingRight, paddingLeft = 0, paddingBottom;
            int tempPadding = ta.getDimensionPixelSize(R.styleable.ClearAbleEditText_textPadding, 0);
            if(tempPadding != -1) {
                paddingBottom = paddingLeft = paddingRight = paddingTop = tempPadding;
            }

            tempPadding = ta.getDimensionPixelSize(R.styleable.ClearAbleEditText_textPaddingLeft, -1);
            if(tempPadding != -1) {
                paddingLeft = tempPadding;
            }
            paddingRight = ta.getDimensionPixelSize(R.styleable.ClearAbleEditText_textPaddingRight, -1);
            if(tempPadding != -1) {
                paddingRight = tempPadding;
            }
            paddingTop = ta.getDimensionPixelSize(R.styleable.ClearAbleEditText_textPaddingTop, -1);
            if(tempPadding != -1) {
                paddingTop = tempPadding;
            }
            paddingBottom = ta.getDimensionPixelSize(R.styleable.ClearAbleEditText_textPaddingBottom, -1);
            if(tempPadding != -1) {
                paddingBottom = tempPadding;
            }
            inputEt.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

            //输入内容限制
            final String digits = ta.getString(R.styleable.ClearAbleEditText_android_digits);
            if(digits != null) {
                inputEt.setKeyListener(new NumberKeyListener() {
                    @NonNull
                    @Override
                    protected char[] getAcceptedChars() {
                        return digits.toCharArray();
                    }

                    @Override
                    public int getInputType() {
                        return InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                    }
                });
            }

            boolean inputName = ta.getBoolean(R.styleable.ClearAbleEditText_inputName, false);
            if(inputName) {
                addInputNameTextWatcher();
            }

            Drawable drawable = ta.getDrawable(R.styleable.ClearAbleEditText_android_drawableRight);
            if(drawable != null) {
                clearIv.setImageDrawable(drawable);
            }



            ta.recycle();

            inputEt.setEnabled(isEnabled());
            clearIv.setEnabled(isEnabled());

        }

        inputEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(mFocusChangeListener != null) {
                    mFocusChangeListener.focusChange(hasFocus);
                }
            }
        });
    }

    private void addInputNameTextWatcher() {
        if(inputNameTextWatcher == null) {
            inputNameTextWatcher = new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String editable = inputEt.getText().toString();
                    String str = chineseFilter(editable);
                    if(!editable.equals(str)) {
                        inputEt.setText(str);
                        //设置新的光标所在位置
                        inputEt.setSelection(str.length());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    clearIv.setVisibility(s.toString().isEmpty() ? View.GONE : View.VISIBLE);
                }

            };
            inputEt.addTextChangedListener(inputNameTextWatcher);


        }
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        inputEt.setEnabled(isEnabled());
        clearIv.setEnabled(isEnabled());
    }


    public void setInputFilter(InputFilter inputFilter) {
        inputEt.setFilters(new  InputFilter[]{inputFilter});
    }

    public String getText() {
        return inputEt.getText().toString();
    }

    public void setText(String text) {
        inputEt.setText(text);
    }

    public void setText(@StringRes int id) {
        inputEt.setText(id);
    }

    public void setHint(String hint) {
        inputEt.setHint(hint);
    }

    public void setHint(@StringRes int id) {
        inputEt.setHint(id);
    }

    public void setDrawableRight(Drawable drawable) {
        clearIv.setImageDrawable(drawable);
    }

    public void setDrawableRight(@DrawableRes int id) {
        clearIv.setImageResource(id);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        inputEt.addTextChangedListener(watcher);
    }

    public void setSelection(int index) {
        inputEt.setSelection(index);
    }

    public void setKeyListener(KeyListener input) {
        inputEt.setKeyListener(input);
    }

    public static String chineseFilter(String str) {
        try {
            String regEx = "[^\u4E00-\u9FA5]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        } catch(Exception e) {
            return str;
        }

    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.text = inputEt!=null?inputEt.getText().toString():"";
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        inputEt.setText(ss.text);
    }

    public static class SavedState extends BaseSavedState {
        CharSequence text;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            TextUtils.writeToParcel(text, out, flags);
        }

        @Override
        public String toString() {
            return super.toString();
        }

        @SuppressWarnings("hiding")
        public static final Parcelable.Creator<ClearableEditText.SavedState> CREATOR =
                new Parcelable.Creator<ClearableEditText.SavedState>() {
                    public ClearableEditText.SavedState createFromParcel(Parcel in) {
                        return new ClearableEditText.SavedState(in);
                    }

                    public ClearableEditText.SavedState[] newArray(int size) {
                        return new ClearableEditText.SavedState[size];
                    }
                };

        private SavedState(Parcel in) {
            super(in);
            text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        }
    }

}
