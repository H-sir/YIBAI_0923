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
import com.ybw.yibai.common.bean.PurchaseOrderList.DataBean.ListBean.SkuListBean;
import com.ybw.yibai.common.utils.ImageUtil;

import java.util.List;

/**
 * 已进货订单盆栽详情列表适配器
 *
 * @author sjl
 * {@link PurchaseOrderAdapter}
 */
public class PurchaseOrderItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 商品列表
     */
    private List<SkuListBean> mSkuList;

    public PurchaseOrderItemAdapter(Context context, List<SkuListBean> skuList) {
        mContext = context;
        mSkuList = skuList;
    }

    @Override
    public int getItemCount() {
        return mSkuList == null ? 0 : mSkuList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_purchase_order_item_list_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        SkuListBean skuListBean = mSkuList.get(position);
        String pic = skuListBean.getPic();
        String name = skuListBean.getName();
        double price = skuListBean.getPrice();
        int num = skuListBean.getNum();

        if (!TextUtils.isEmpty(pic)) {
            myViewHolder.mIconImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, pic);
        } else {
            myViewHolder.mIconImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, " ");
        }

        if (TextUtils.isEmpty(name)) {
            myViewHolder.mProductName.setText(" ");
        } else {
            myViewHolder.mProductName.setText(name);
        }

        String priceString = YiBaiApplication.getCurrencySymbol() + " " + price;
        myViewHolder.mPriceTextView.setText(priceString);

        String numberString = "X" + num;
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
