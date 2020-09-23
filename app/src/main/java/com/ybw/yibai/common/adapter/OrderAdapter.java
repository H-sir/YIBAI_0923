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
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.confirmorder.ConfirmOrderActivity;

import java.io.File;
import java.util.List;

import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 显示进货订单列表的适配器
 *
 * @author sjl
 * {@link ConfirmOrderActivity}
 */
public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 用户保存的"报价"数据列表
     */
    protected List<QuotationData> mQuotationDataList;

    public OrderAdapter(Context context, List<QuotationData> quotationDataList) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_order_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        QuotationData quotationData = mQuotationDataList.get(position);
        String productName = quotationData.getProductName();
        double productTradePrice = quotationData.getProductTradePrice();
        String productPic1 = quotationData.getProductPic1();
        String productPic2 = quotationData.getProductPic2();
        String productPic3 = quotationData.getProductPic3();

        String augmentedProductName = quotationData.getAugmentedProductName();
        double augmentedProductTradePrice = quotationData.getAugmentedProductTradePrice();
        String augmentedProductPic1 = quotationData.getAugmentedProductPic1();
        String augmentedProductPic2 = quotationData.getAugmentedProductPic2();
        String augmentedProductPic3 = quotationData.getAugmentedProductPic3();

        // 数量
        int number = quotationData.getNumber();
        // 组合图片在本地保存的路径
        String picturePath = quotationData.getPicturePath();
        // 价格
        double price = productTradePrice + augmentedProductTradePrice;

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

        if (TextUtils.isEmpty(productName)) {
            myViewHolder.mProductName.setText(" ");
        } else {
            myViewHolder.mProductName.setText(productName);
        }

        String priceString = YiBaiApplication.getCurrencySymbol() + " " + price;
        myViewHolder.mPriceTextView.setText(priceString);

        if (TextUtils.isEmpty(augmentedProductName)) {
            myViewHolder.mAugmentedProductName.setText(" ");
        } else {
            myViewHolder.mAugmentedProductName.setText(augmentedProductName);
        }

        String numberString = "x" + number;
        myViewHolder.mNumberTextView.setText(numberString);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mIconImageView;

        TextView mProductName;

        TextView mPriceTextView;

        TextView mAugmentedProductName;

        TextView mNumberTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mIconImageView = itemView.findViewById(R.id.iconImageView);
            mProductName = itemView.findViewById(R.id.productNameTextView);
            mPriceTextView = itemView.findViewById(R.id.priceTextView);
            mAugmentedProductName = itemView.findViewById(R.id.augmentedProductName);
            mNumberTextView = itemView.findViewById(R.id.numberTextView);
        }
    }
}
