package com.ybw.yibai.common.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ybw.yibai.R;

/**
 * 自定义购物车加减按钮控件
 *
 * @author sjl
 */
public class OrderEditText extends FrameLayout implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener {

    public static final String TAG = "OrderEditText";

    /**
     * 最小值
     */
    private int min = 1;

    /**
     * 最大值
     */
    private int max = Integer.MAX_VALUE;

    /**
     * 编辑
     */
    private EditText mEditText;

    public OrderEditText(Context context) {
        super(context);
        init();
    }

    public OrderEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        View view = View.inflate(getContext(), R.layout.order_edit_text_layout, this);

        ImageView mMinusImageView = view.findViewById(R.id.minusImageView);
        mEditText = view.findViewById(R.id.editText);
        ImageView mAugmentImageView = view.findViewById(R.id.augmentImageView);

        mEditText.setText(String.valueOf(min));
        mEditText.setSelection(mEditText.getText().length());

        mMinusImageView.setOnClickListener(this);
        mEditText.setOnClickListener(this);
        // 对EditText焦点状态监听
        mEditText.setOnFocusChangeListener(this);
        mEditText.addTextChangedListener(this);
        mAugmentImageView.setOnClickListener(this);
    }

    /**
     * 在EditText失去焦点时,判断EditText内容是否为null如果为null,就赋值EditText最小值
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            return;
        }
        String number = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            mEditText.setText(String.valueOf(min));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String string = mEditText.getText().toString();
        int number = Integer.parseInt(TextUtils.isEmpty(string) ? String.valueOf(min) : string);
        if (number > max) {
            // 设置最大值
            mEditText.setText(String.valueOf(max));
        }
        if (number < min) {
            // 设置最小值
            mEditText.setText(String.valueOf(min));
        }
        mEditText.setSelection(mEditText.getText().length());
        if (null != numberChangeCallBack) {
            numberChangeCallBack.onNumberChangeCallBack(getNumber());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        String string = mEditText.getText().toString();
        int number = Integer.parseInt(TextUtils.isEmpty(string) ? String.valueOf(min) : string);

        // 减少
        if (id == R.id.minusImageView) {
            if (number > min) {
                mEditText.setText(String.valueOf(number - 1));
            } else {
                // 最小值不可以小于min
            }
        }

        if (id == R.id.editText) {
            if (null != onEditTextClickListener) {
                onEditTextClickListener.onEditTextClick();
            }
        }

        // 增加
        if (id == R.id.augmentImageView) {
            if (number < max) {
                mEditText.setText(String.valueOf(number + 1));
            } else {
                // 最大值不可以大于max
            }
        }

        mEditText.setSelection(mEditText.getText().length());
    }

    public EditText getEditText() {
        return mEditText;
    }

    /**
     * 设置OrderEditText最大值
     */
    public void setMaxNumber(int max) {
        this.max = max;
    }

    /**
     * 获取OrderEditText的值
     *
     * @return OrderEditText的值
     */
    public int getNumber() {
        String number = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            return min;
        } else {
            return Integer.parseInt(number);
        }
    }

    /**
     * 设置EditText是否可编辑
     *
     * @param b true可编辑,false不可编辑
     */
    public void setEditTextFocusableInTouchMode(boolean b) {
        mEditText.setFocusableInTouchMode(b);
    }

    /*----------*/

    /**
     * 设置OrderEditText的值
     *
     * @param number 要设置的值
     */
    public void setNumber(int number) {
        mEditText.setText(String.valueOf(number));
    }

    /**
     * 1.定义一个接口
     */
    public interface NumberChangeCallBack {

        /**
         * 在OrderEditText中的数目发生改变时回调
         *
         * @param number 当前的数目
         */
        void onNumberChangeCallBack(int number);
    }

    /**
     * 2.声明一个接口变量
     */
    private NumberChangeCallBack numberChangeCallBack;

    /**
     * 3.初始化接口变量
     *
     * @param numberChangeCallBack 侦听在OrderEditText中的数目发生改变侦听器
     */
    public void setCallBack(NumberChangeCallBack numberChangeCallBack) {
        this.numberChangeCallBack = numberChangeCallBack;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnEditTextClickListener {

        /**
         * 在EditText被点击时回调
         */
        void onEditTextClick();
    }

    /**
     * 2.声明一个接口变量
     */
    private OnEditTextClickListener onEditTextClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onEditTextClickListener 侦听EditText被点击的侦听器
     */
    public void setOnEditTextClickListener(OnEditTextClickListener onEditTextClickListener) {
        this.onEditTextClickListener = onEditTextClickListener;
    }
}
