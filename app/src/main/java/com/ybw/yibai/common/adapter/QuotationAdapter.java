package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.PlacementQrQuotationList.DataBean.ListBean;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.quotation.QuotationPresenterImpl;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * 显示报价列表的适配器
 *
 * @author sjl
 * {@link QuotationPresenterImpl}
 */
public class QuotationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 报价列表
     */
    private List<ListBean> mQuotationList;

    public QuotationAdapter(Context context, List<ListBean> quotationList) {
        this.mContext = context;
        this.mQuotationList = quotationList;
    }

    @Override
    public int getItemCount() {
        return mQuotationList == null ? 0 : mQuotationList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_quotation_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean quotationData = mQuotationList.get(position);
        String productName = quotationData.getFirst_name();
        double productPrice = quotationData.getPrice();
        double productMonthRent = quotationData.getMonth_rent();
        double productTradePrice = quotationData.getYear_rent();
        String productPic = quotationData.getPic();

        String augmentedProductName = quotationData.getSecond_name();

        // 报价方式
        int mode = quotationData.getMode();
        // 数量
        int number = quotationData.getNum();

        if (!TextUtils.isEmpty(productPic)) {
            myViewHolder.mIconImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, productPic);
        } else {
            myViewHolder.mIconImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, " ");
        }

        if (!TextUtils.isEmpty(productName)) {
            myViewHolder.mProductNameTextView.setText(productName);
        } else {
            myViewHolder.mProductNameTextView.setText(" ");
        }

        if (!TextUtils.isEmpty(augmentedProductName)) {
            myViewHolder.mAugmentedProductNameTextView.setText(augmentedProductName);
        } else {
            myViewHolder.mAugmentedProductNameTextView.setText(" ");
        }

        double price = 0;
        String quotationMode;
        if (0 == mode) {
            // 购买
            price += productPrice;
            quotationMode = " ";
        } else if (1 == mode) {
            // 月租
            price += productMonthRent;
            quotationMode = mContext.getResources().getString(R.string.slash) + mContext.getResources().getString(R.string.month);
        } else if (2 == mode) {
            // 年租
            price += productMonthRent * 12;
            quotationMode = mContext.getResources().getString(R.string.slash) + mContext.getResources().getString(R.string.year);
        } else if (3 == mode) {
            // 日租
            BigDecimal bigDecimal = new BigDecimal(productMonthRent / 30);
            double t = bigDecimal.setScale(2, ROUND_HALF_UP).doubleValue();
            price += t;
            quotationMode = mContext.getResources().getString(R.string.slash) + mContext.getResources().getString(R.string.day);
        } else {
            // 批发
            price += productTradePrice * number;
            quotationMode = " ";
        }
        // 保留2位小数
        double p = new BigDecimal(price).setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
        myViewHolder.mPriceTextView.setText(String.valueOf(p));
        myViewHolder.mModeTextView.setText(quotationMode);

        myViewHolder.mNumberTextView.setText("x" + number);

        myViewHolder.mPriceTextView.setOnClickListener(v -> {
            // 4.在点击事件中调用接口里的方法
            if (null != onPriceClickListener) {
                if (0 == mode) {
                    onPriceClickListener.onPriceClick(position, mode, productPrice);
                } else if (1 == mode || 2 == mode) {
                    onPriceClickListener.onPriceClick(position, mode, productMonthRent);
                }
            }
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         *
         */
        View mItemView;

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
         * 价格
         */
        TextView mPriceTextView;

        /**
         * 报价方式
         */
        TextView mModeTextView;

        /**
         * 数量
         */
        TextView mNumberTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemView = itemView;
            mIconImageView = itemView.findViewById(R.id.iconImageView);
            mProductNameTextView = itemView.findViewById(R.id.productNameTextView);
            mAugmentedProductNameTextView = itemView.findViewById(R.id.augmentedProductNameTextView);
            mPriceTextView = itemView.findViewById(R.id.priceTextView);
            mModeTextView = itemView.findViewById(R.id.modeTextView);
            mNumberTextView = itemView.findViewById(R.id.numberTextView);
        }
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnPriceClickListener {

        /**
         * 在RecyclerView Item 中的"价格"点击时回调
         *
         * @param position 被点击的Item位置
         * @param mode     报价方式
         * @param price    被点击的Item位置的价格
         */
        void onPriceClick(int position, int mode, double price);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnPriceClickListener onPriceClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onPriceClickListener ListView Item 价格点击的侦听器
     */
    public void setOnPriceClickListener(OnPriceClickListener onPriceClickListener) {
        this.onPriceClickListener = onPriceClickListener;
    }
}
