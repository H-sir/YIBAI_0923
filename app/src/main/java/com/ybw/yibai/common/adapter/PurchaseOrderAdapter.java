package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.PurchaseOrderList.DataBean.ListBean;
import com.ybw.yibai.common.bean.PurchaseOrderList.DataBean.ListBean.SkuListBean;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.module.purchaseorder.PurchaseOrderFragment;

import java.util.List;

import static com.ybw.yibai.common.utils.DensityUtil.dpToPx;

/**
 * 已进货订单适配器
 *
 * @author sjl
 * {@link PurchaseOrderFragment}
 */
public class PurchaseOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 已进货订单列表
     */
    private List<ListBean> mPurchaseOrderList;

    public PurchaseOrderAdapter(Context context, List<ListBean> purchaseOrderList) {
        mContext = context;
        mPurchaseOrderList = purchaseOrderList;
    }

    @Override
    public int getItemCount() {
        return mPurchaseOrderList == null ? 0 : mPurchaseOrderList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_purchase_order_list_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean listBean = mPurchaseOrderList.get(position);
        int state = listBean.getState();
        String stateMsg = listBean.getState_msg();
        int goodsNum = listBean.getGoods_num();
        double payableAmount = listBean.getPayable_amount();

        ListBean bean = mPurchaseOrderList.get(position);
        List<SkuListBean> mSkuList = bean.getSku_list();

        int color;
        if (1 == state) {
            color = ContextCompat.getColor(mContext, R.color.embellishment_color);
        } else if (2 == state) {
            color = ContextCompat.getColor(mContext, R.color.colorPrimary);
        } else if (3 == state) {
            color = ContextCompat.getColor(mContext, R.color.warning_color);
        } else {
            color = ContextCompat.getColor(mContext, R.color.main_text_color);
        }
        if (TextUtils.isEmpty(stateMsg)) {
            myViewHolder.mStateTextView.setText(" ");
        } else {
            myViewHolder.mStateTextView.setText(stateMsg);
        }
        myViewHolder.mStateTextView.setTextColor(color);

        String goodsNumString = mContext.getResources().getString(R.string.sum) + goodsNum + mContext.getResources().getString(R.string.items_commodity);
        myViewHolder.mNumberTextView.setText(goodsNumString);

        myViewHolder.mNameTextView.setText(listBean.getOrder_number());

        String payableAmountString = YiBaiApplication.getCurrencySymbol() + payableAmount;
        myViewHolder.mPrice.setText(payableAmountString);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        // 布局水平
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myViewHolder.mRecyclerView.setLayoutManager(manager);
        // 设置RecyclerView间距
        int spacing = dpToPx(mContext, 16);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(1, spacing, false);
        // 判断是否已经添加了ItemDecorations,如果存在就不添加(解决RecyclerView刷新ItemDecorations间距不断增加问题)
        if (myViewHolder.mRecyclerView.getItemDecorationCount() == 0) {
            myViewHolder.mRecyclerView.addItemDecoration(decoration);
        }
        PurchaseOrderItemAdapter mAdapter = new PurchaseOrderItemAdapter(mContext, mSkuList);
        myViewHolder.mRecyclerView.setAdapter(mAdapter);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * 城市运营中心名称
         */
        TextView mNameTextView;

        /**
         * 状态
         */
        TextView mStateTextView;

        /**
         * 共X件商品,合计:
         */
        TextView mNumberTextView;

        /**
         * 价格
         */
        TextView mPrice;

        /**
         *
         */
        RecyclerView mRecyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.nameTextView);
            mStateTextView = itemView.findViewById(R.id.stateTextView);
            mNumberTextView = itemView.findViewById(R.id.numberTextView);
            mPrice = itemView.findViewById(R.id.priceTextView);

            mRecyclerView = itemView.findViewById(R.id.recyclerView);

            itemView.setOnClickListener(this);
            // 屏蔽item中嵌套的RecycleView的点击事件
            mRecyclerView.setOnTouchListener((v, event) -> itemView.onTouchEvent(event));
        }

        @Override
        public void onClick(View v) {
            // 4.在点击事件中调用接口里的方法
            if (null != onItemClickListener) {
                // holder.getLayoutPosition()方法表示获得当前所点击item的真正位置
                onItemClickListener.onItemClick(getLayoutPosition());
            }
        }
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnItemClickListener {

        /**
         * 在RecyclerView Item 点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onItemClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnItemClickListener onItemClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onItemClickListener RecyclerView Item点击的侦听器
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
