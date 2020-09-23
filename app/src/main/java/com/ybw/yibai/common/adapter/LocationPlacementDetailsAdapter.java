package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.QuotationLocation.DataBean.ListBean.QuotelistBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.widget.OrderEditText;

import java.util.List;

/**
 * 显示报价单,某一个位置盆栽详细适配器
 *
 * @author sjl
 * {@link LocationPlacementAdapter}
 */
public class LocationPlacementDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 最小值
     */
    private int min = 1;

    /**
     * 最大值
     */
    private int max = Integer.MAX_VALUE;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 某一个具体位置摆放清单详情
     */
    private List<QuotelistBean> mQuoteList;

    public LocationPlacementDetailsAdapter(Context context, List<QuotelistBean> quoteList) {
        this.mContext = context;
        this.mQuoteList = quoteList;
    }

    @Override
    public int getItemCount() {
        return mQuoteList == null ? 0 : mQuoteList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_location_placement_details_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        QuotelistBean quotelistBean = mQuoteList.get(position);
        String pic = quotelistBean.getPic();
        String firstName = quotelistBean.getFirst_name();
        String secondName = quotelistBean.getSecond_name();
        int number = quotelistBean.getNum();

        if (!TextUtils.isEmpty(pic)) {
            myViewHolder.mImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, pic, DensityUtil.dpToPx(mContext, 2));
        } else {
            myViewHolder.mImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, " ");
        }

        if (TextUtils.isEmpty(firstName)) {
            myViewHolder.mProductNameTextView.setText(" ");
        } else {
            myViewHolder.mProductNameTextView.setText(firstName);
        }

        if (TextUtils.isEmpty(secondName)) {
            myViewHolder.mAugmentedProductNameTextView.setText(" ");
        } else {
            myViewHolder.mAugmentedProductNameTextView.setText(secondName);
        }

        // Item嵌套EditText滑动时数据错乱解决方法
        EditText editText = myViewHolder.mOrderEditText.getEditText();
        // 1,根据Tag移除掉监听
        if (null != editText.getTag() && editText.getTag() instanceof TextWatcher) {
            editText.removeTextChangedListener((TextWatcher) editText.getTag());
        }
        // 2,设置内容
        editText.setText(String.valueOf(number));
        // 3,新建一个监听
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = editText.getText().toString();
                int number = Integer.parseInt(TextUtils.isEmpty(string) ? String.valueOf(min) : string);
                if (number > max) {
                    // 设置最大值
                    editText.setText(String.valueOf(max));
                }
                if (number < min) {
                    // 设置最小值
                    editText.setText(String.valueOf(min));
                }
                editText.setSelection(editText.getText().length());
                int num = myViewHolder.mOrderEditText.getNumber();
                if (null != mOnNumberChangeListener) {
                    mOnNumberChangeListener.onNumberChange(position, num);
                }
            }
        };
        // 4.添加一个监听
        editText.addTextChangedListener(textWatcher);
        // 5.设置一个Tag
        editText.setTag(textWatcher);

        myViewHolder.mOrderEditText.setOnEditTextClickListener(() -> {
            if (null == onOrderEditTextClickListener) {
                return;
            }
            onOrderEditTextClickListener.onOrderEditTextClick(position);
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        /**
         * 盆栽图片
         */
        ImageView mImageView;

        /**
         * 主产品名称
         */
        TextView mProductNameTextView;

        /**
         * 附加产品名称
         */
        TextView mAugmentedProductNameTextView;

        /**
         * 购物车加减按钮控件
         */
        OrderEditText mOrderEditText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.iconImageView);
            mProductNameTextView = itemView.findViewById(R.id.productNameTextView);
            mAugmentedProductNameTextView = itemView.findViewById(R.id.augmentedProductNameTextView);
            mOrderEditText = itemView.findViewById(R.id.orderEditText);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (null == onItemLongClickListener) {
                return false;
            }
            onItemLongClickListener.onItemLongClick(getLayoutPosition());
            return false;
        }
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnItemLongClickListener {

        /**
         * 在RecyclerView Item 长按时回调
         *
         * @param position 被点击的Item位置
         */
        void onItemLongClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onItemLongClickListener RecyclerView Item长按的侦听器
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /*----------*/

    /**
     * 自定义接口,用于回调OrderEditText数量发生改变事件
     */
    public interface OnNumberChangeListener {

        /**
         * 在OrderEditText数量发生改变时回调
         *
         * @param position 位置
         * @param number   数量
         */
        void onNumberChange(int position, int number);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnNumberChangeListener mOnNumberChangeListener;

    /**
     * 3.初始化接口变量
     *
     * @param onNumberChangeListener 在OrderEditText数量发生改变时回调侦听器
     */
    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        mOnNumberChangeListener = onNumberChangeListener;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnOrderEditTextClickListener {

        /**
         * 在OrderEditText中的EditText被点击时回调
         *
         * @param position 被点击的位置
         */
        void onOrderEditTextClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnOrderEditTextClickListener onOrderEditTextClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onOrderEditTextClickListener 侦听OrderEditText中的EditText被点击的侦听器
     */
    public void setOnOrderEditTextClickListener(OnOrderEditTextClickListener onOrderEditTextClickListener) {
        this.onOrderEditTextClickListener = onOrderEditTextClickListener;
    }
}
