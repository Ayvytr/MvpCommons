package com.cv.text;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cv.R;


/**
 * 顶部搜索layout
 * Created by xiesuichao on 2018/8/20.
 */

public class CustomSearchLayout extends RelativeLayout {

    private EditText mInputEt;
    private ImageView mClearIv;
    private TextView mCancelTv;
    private OnInputChangeListener mInputChangeListener;
    private OnSearchCancelListener mCancelListener;
    private OnKeyboardSearchClickListener mSearchClickListener;

    public CustomSearchLayout(Context context) {
        this(context, null);
    }

    public CustomSearchLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSearchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface OnInputChangeListener{
        void textChange(Editable s);
    }

    public interface OnSearchCancelListener{
        void cancel();
    }

    public interface OnKeyboardSearchClickListener{
        void searchClick(String content);
    }

    public void setOnTextChangeListener(OnInputChangeListener inputChangeListener){
        this.mInputChangeListener = inputChangeListener;
    }

    public void setOnSearchCancelListener(OnSearchCancelListener cancelListener){
        this.mCancelListener = cancelListener;
    }

    public void setOnKeyboardSearchClickListener(OnKeyboardSearchClickListener searchClickListener){
        this.mSearchClickListener = searchClickListener;
    }

    public void setHintText(int resourceId){
        mInputEt.setHint(resourceId);
    }

    public void setInputTest(String text){
        if (text != null){
            mInputEt.setText(text);
            mInputEt.setSelection(text.length());
        }
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.common_layout_custom_search, this, true);
        mInputEt = findViewById(R.id.et_common_search_input);
        mClearIv = findViewById(R.id.iv_common_search_input_clear);
        mCancelTv = findViewById(R.id.tv_common_search_cancel);
        setListener();
    }

    public EditText getInputEt() {
        return mInputEt;
    }

    private void setListener(){
        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0){
                    mClearIv.setVisibility(INVISIBLE);
                }else {
                    mClearIv.setVisibility(VISIBLE);
                }
                if (mInputChangeListener != null){
                    mInputChangeListener.textChange(s);
                }
            }
        });

        mInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchClickListener != null){
                    mSearchClickListener.searchClick(mInputEt.getText().toString().trim());
                }
                return true;
            }
        });

        mClearIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputEt.setText("");
            }
        });

        mCancelTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancelListener != null){
                    mCancelListener.cancel();
                }
            }
        });

    }

    public String getText() {
        return mInputEt.getText().toString();
    }
}
