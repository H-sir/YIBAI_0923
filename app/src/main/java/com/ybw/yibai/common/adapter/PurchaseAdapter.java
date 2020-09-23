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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.widget.OrderEditText;
import com.ybw.yibai.module.purchase.PurchaseFragment;

import java.io.File;
import java.util.List;

import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 显示进货列表的适配器
 *
 * @author sjl
 * {@link PurchaseFragment}
 */
public class PurchaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
     * 用户保存的"进货"数据列表
     */
    private List<QuotationData> mQuotationDataList;

    public PurchaseAdapter(Context context, List<QuotationData> quotationDataList) {
        this.mContext = context;
        this.mQuotationDataList = quotationDataList;
    }

    @Override
    public int getItemCount() {
        return mQuotationDataList == null ? 0 : mQuotationDataList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_purchase_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        QuotationData quotationData = mQuotationDataList.get(position);
        String productName = quotationData.getProductName();
        double productPrice = quotationData.getProductPrice();
        double productMonthRent = quotationData.getProductMonthRent();
        double productTradePrice = quotationData.getProductTradePrice();
        String productPic1 = quotationData.getProductPic1();
        String productPic2 = quotationData.getProductPic2();
        String productPic3 = quotationData.getProductPic3();
        String productSpecText = quotationData.getProductSpecText();

        String augmentedProductName = quotationData.getAugmentedProductName();
        double augmentedProductPrice = quotationData.getAugmentedProductPrice();
        double augmentedProductMonthRent = quotationData.getAugmentedProductMonthRent();
        double augmentedProductTradePrice = quotationData.getAugmentedProductTradePrice();
        String augmentedProductPic1 = quotationData.getAugmentedProductPic1();
        String augmentedProductPic2 = quotationData.getAugmentedProductPic2();
        String augmentedProductPic3 = quotationData.getAugmentedProductPic3();
        String augmentedProductSpecText = quotationData.getAugmentedProductSpecText();

        // 数量
        int number = quotationData.getNumber();
        // 是否显示CheckBox
        boolean showCheckBox = quotationData.isShowCheckBox();
        // 是否选中CheckBox
        boolean select = quotationData.isSelectCheckBox();
        // 组合图片在本地保存的路径
        String picturePath = quotationData.getPicturePath();
        // 批发价格
        double totalPrice = (productTradePrice + augmentedProductTradePrice) * number;

        if (showCheckBox) {
            myViewHolder.mCheckBox.setVisibility(View.VISIBLE);
            myViewHolder.mView.setVisibility(View.VISIBLE);
            // 设置可以点击
            myViewHolder.mPriceTextView.setEnabled(true);
        } else {
            myViewHolder.mCheckBox.setVisibility(View.GONE);
            myViewHolder.mView.setVisibility(View.GONE);
            // 设置不可以点击
            myViewHolder.mPriceTextView.setEnabled(false);
        }
        if (select) {
            myViewHolder.mCheckBox.setChecked(true);
        } else {
            myViewHolder.mCheckBox.setChecked(false);
        }

        if (judeFileExists(picturePath)) {
            myViewHolder.mIconImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, new File(picturePath).toURI().toString());
        } else if (!TextUtils.isEmpty(productPic1)) {
            myViewHolder.mIconImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, productPic1);
        } else if (!TextUtils.isEmpty(productPic2)) {
            myViewHolder.mIconImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, productPic2);
        } else if (!TextUtils.isEmpty(productPic3)) {
            myViewHolder.mIconImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, productPic3);
        } else {
            myViewHolder.mIconImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, " ");
        }

        StringBuilder nameStringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(productName)) {
            nameStringBuilder.append(productName);
        }
        if (!TextUtils.isEmpty(productSpecText)) {
            nameStringBuilder.append(productSpecText);
        }
        if (!TextUtils.isEmpty(nameStringBuilder)) {
            myViewHolder.mProductNameTextView.setText(nameStringBuilder);
        } else {
            myViewHolder.mProductNameTextView.setText(" ");
        }

        StringBuilder augmentedNameStringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(augmentedProductName)) {
            augmentedNameStringBuilder.append(augmentedProductName);
        }
        if (!TextUtils.isEmpty(augmentedProductSpecText)) {
            augmentedNameStringBuilder.append(augmentedProductSpecText);
        }
        if (!TextUtils.isEmpty(augmentedNameStringBuilder)) {
            myViewHolder.mAugmentedProductNameTextView.setText(augmentedNameStringBuilder);
        } else {
            myViewHolder.mAugmentedProductNameTextView.setText(" ");
        }

        String currencySymbol = YiBaiApplication.getCurrencySymbol();
        if (!TextUtils.isEmpty(currencySymbol)) {
            myViewHolder.mRmbTextView.setText(currencySymbol);
        }

        myViewHolder.mPriceTextView.setText(String.valueOf(totalPrice));

        if (position == mQuotationDataList.size() - 1) {
            myViewHolder.mDividerView.setVisibility(View.INVISIBLE);
        } else {
            myViewHolder.mDividerView.setVisibility(View.VISIBLE);
        }

        myViewHolder.mItemView.setOnClickListener(v -> {
            boolean selectCheckBox = quotationData.isSelectCheckBox();
            if (selectCheckBox) {
                // 如果之前选中,那现在设置没有选中
                myViewHolder.mCheckBox.setChecked(false);
                quotationData.setSelectCheckBox(false);
            } else {
                // 如果之前没有选中,那现在设置选中
                myViewHolder.mCheckBox.setChecked(true);
                quotationData.setSelectCheckBox(true);
            }
        });

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
                // 更新对应位置的数量
                quotationData.setNumber(num);
                if (null != mOnNumberChangeListener) {
                    mOnNumberChangeListener.onNumberChange(position);
                }
            }
        };
        // 4.添加一个监听
        editText.addTextChangedListener(textWatcher);
        // 5.设置一个Tag
        editText.setTag(textWatcher);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        /**
         *
         */
        View mItemView;

        /**
         *
         */
        CheckBox mCheckBox;

        /**
         * 图片
         */
        ImageView mIconImageView;

        /**
         * 主产品名称+规格
         */
        TextView mProductNameTextView;

        /**
         * 附加产品名称+规格
         */
        TextView mAugmentedProductNameTextView;

        /**
         * 钱币符号
         */
        private TextView mRmbTextView;

        /**
         * 价格
         */
        TextView mPriceTextView;

        /**
         * EditText 下面的下划线
         */
        View mView;

        /**
         * 报价方式
         */
        TextView mModeTextView;

        /**
         * 自定义购物车加减按钮控件
         */
        OrderEditText mOrderEditText;

        /**
         * 分隔线
         */
        View mDividerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemView = itemView;
            mCheckBox = itemView.findViewById(R.id.checkbox);
            mIconImageView = itemView.findViewById(R.id.iconImageView);
            mProductNameTextView = itemView.findViewById(R.id.productNameTextView);
            mAugmentedProductNameTextView = itemView.findViewById(R.id.augmentedProductNameTextView);
            mRmbTextView = itemView.findViewById(R.id.rmbTextView);
            mPriceTextView = itemView.findViewById(R.id.priceTextView);
            mView = itemView.findViewById(R.id.view);
            mModeTextView = itemView.findViewById(R.id.modeTextView);
            mOrderEditText = itemView.findViewById(R.id.orderEditText);
            mDividerView = itemView.findViewById(R.id.dividerView);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            // 4.在点击事件中调用接口里的方法
            if (null != onItemLongClickListener) {
                // holder.getLayoutPosition()方法表示获得当前所点击item的真正位置
                onItemLongClickListener.onItemLongClick(getLayoutPosition());
            }
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
     * @param onItemLongClickListener RecyclerView Item点击的侦听器
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
         */
        void onNumberChange(int position);
    }

    private OnNumberChangeListener mOnNumberChangeListener;

    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        mOnNumberChangeListener = onNumberChangeListener;
    }
}
