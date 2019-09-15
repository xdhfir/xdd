package com.my.baselib.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.my.baselib.R;
import com.my.baselib.lib.utils.EditTextUtils;


/**
 * create by Administrator at 2017/2/21
 * description: 一个带删除按钮、及密码显示或隐藏的输入框
 */
public class CanDeleteInputText extends LinearLayout implements View.OnClickListener {
    private ImageView isShow;
    private EditText mEt;
    private ImageView clear;
    private boolean flag = false;
    private Context context;
    private boolean isNumber = false;
    private boolean isPwd = false;

    private boolean hadText = false;
    private boolean hadFocus = false;

    public CanDeleteInputText(Context context) {
        this(context, null);
    }

    public CanDeleteInputText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.view_can_delete_input_text, this);
        mEt = (EditText) view.findViewById(R.id.et_can_delete_input_text);
        isShow = (ImageView) view.findViewById(R.id.is_show_can_delete_input_text);
        clear = (ImageView) view.findViewById(R.id.clear_can_delete_input_text);
        clear.setOnClickListener(this);
        isShow.setOnClickListener(this);

        @SuppressLint("Recycle") TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CanDeleteInputText);
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            /*输入字数限制,默认250*/
            if (typedArray.getIndex(i) == R.styleable.CanDeleteInputText_count) {
                int integer = typedArray.getInteger(i, 250);
                mEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integer)});
            }
            /*文本框默认输入*/
            if (typedArray.getIndex(i) == R.styleable.CanDeleteInputText_default_text) {
                CharSequence text = typedArray.getText(i);
                mEt.setText(text);
            }
             /*文本框默认提示*/
            if (typedArray.getIndex(i) == R.styleable.CanDeleteInputText_hint) {
                CharSequence text = typedArray.getText(i);
                mEt.setHint(text);
            }
             /*是否只能输入数字*/
            if (typedArray.getIndex(i) == R.styleable.CanDeleteInputText_is_number) {
                isNumber = typedArray.getBoolean(i, false);
            }
             /*是否输入的是密码*/
            if (typedArray.getIndex(i) == R.styleable.CanDeleteInputText_is_pwd) {
                isPwd = typedArray.getBoolean(i, false);
            }
        }
        checkTextType();
        mEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (listener != null) {
                    listener.onTextBeforeChange(s);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    hadText = true;
                } else {
                    hadText = false;
                }
                checkEtStatus();

                if (listener != null) {
                    listener.onTextChange(s);
                }
            }
        });
        mEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hadFocus = hasFocus;
                checkEtStatus();
                if (focusListener != null) {
                    focusListener.onFocusChange(hasFocus);
                }
            }
        });
        clear.setVisibility(View.GONE);
    }

    //设置EditText输入类型
    public void setType(int type) {
        mEt.setInputType(type);
    }

    //设置EditText布局位置
    public void setEtGravity(int gravity) {
        mEt.setGravity(gravity);
    }

    //修改输入框删除按钮状态
    private void checkEtStatus() {
        if (hadFocus && hadText) {
            clear.setVisibility(View.VISIBLE);
        } else {
            clear.setVisibility(View.GONE);
        }
    }

    //根据属性，判断控件内部的输入文本格式
    private void checkTextType() {

        if (isPwd && isNumber) {
            isShow.setVisibility(View.VISIBLE);
            EditTextUtils.inputDismissNumber(mEt);
        } else if (isPwd) {
            isShow.setVisibility(View.VISIBLE);
            EditTextUtils.inputDismiss(mEt);
        } else if (isNumber) {
            mEt.setInputType(InputType.TYPE_CLASS_NUMBER);
            isShow.setVisibility(View.GONE);
        } else {
            isShow.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.is_show_can_delete_input_text) {
            if (flag) {
                if (isNumber) {
                    EditTextUtils.inputDismissNumber(mEt);
                } else {
                    EditTextUtils.inputDismiss(mEt);
                }
                flag = false;
                isShow.setImageResource(R.mipmap.base_dismiss);
            } else {
                if (isNumber) {
                    EditTextUtils.inputShowNumber(mEt);
                } else {
                    EditTextUtils.inputShow(mEt);
                }
                flag = true;
                isShow.setImageResource(R.mipmap.base_show);
            }

            String s = mEt.getText().toString();
            int length = s.length();
            mEt.setSelection(length);
        } else if (i == R.id.clear_can_delete_input_text) {
            mEt.setText("");
        }
    }

    /**
     * @time 2017/2/21 11:14
     * @desc 将输入框内的字符串返回
     */
    public String getText() {
        return mEt.getText().toString();
    }

    public void setText(CharSequence text) {
        mEt.setText(text);
    }

    /**
     * @time 2017/2/21 11:14
     * @desc 一个监听输入框内输入发生变化的回调
     */
    public interface TextChangeListener {
        void onTextChange(Editable s);

        void onTextBeforeChange(CharSequence s);

    }

    private TextChangeListener listener;

    public void setTextChangeListener(TextChangeListener listener) {
        this.listener = listener;
    }

    /**
     * @desc 一个监听输入框焦点发生变化的回调
     */
    public interface FocusChangeListener {
        void onFocusChange(boolean hasFocus);
    }

    private FocusChangeListener focusListener;

    public void setFocusChangeListener(FocusChangeListener focusListener) {
        this.focusListener = focusListener;
    }
}
