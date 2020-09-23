package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.QuotationList.DataBean.ListBean;
import com.ybw.yibai.common.classs.GridSpacingItemDecorations;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsFragment;

import java.util.List;

/**
 * 报价列表的适配器
 *
 * @author sjl
 * {@link QuotationDetailsFragment}
 */
public class QuotationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 报价数据集合
     */
    private List<ListBean> mQuotationList;

    public QuotationListAdapter(Context context, List<ListBean> quotationList) {
        mContext = context;
        mQuotationList = quotationList;
    }

    @Override
    public int getItemCount() {
        return mQuotationList == null ? 0 : mQuotationList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_quotation_list_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean listBean = mQuotationList.get(position);
        String cname = listBean.getCname();
        int state = listBean.getState();
        String stateMsg = listBean.getStatemsg();
        int goodsNum = listBean.getGoods_num();
        String payableAmount = listBean.getPayable_amount();
        int tax = listBean.getTax();
        double districtMoney = listBean.getDistrict_money();
        List<String> drawingPic = listBean.getDrawing_pic();

        String number = mContext.getResources().getString(R.string.sum) + goodsNum + mContext.getResources().getString(R.string.tub) + ", " + mContext.getResources().getString(R.string.amount) + payableAmount;
        String taxRate = mContext.getResources().getString(R.string.has_included) + tax + mContext.getResources().getString(R.string.percent_sign) + mContext.getResources().getString(R.string.tax_rate) + ", " + mContext.getResources().getString(R.string.discounts) + districtMoney;

        if (TextUtils.isEmpty(cname)) {
            myViewHolder.mCustomerNameTextView.setText(" ");
        } else {
            myViewHolder.mCustomerNameTextView.setText(cname);
        }

        if (TextUtils.isEmpty(stateMsg)) {
            myViewHolder.mStateTextView.setText("");
        } else {
            int color;
            if (1 == state) {
                color = ContextCompat.getColor(mContext, R.color.embellishment_color);
            } else if (2 == state) {
                color = ContextCompat.getColor(mContext, R.color.embellishment_text_color);
            } else if (3 == state) {
                color = ContextCompat.getColor(mContext, R.color.embellishment_text_color);
            } else {
                color = ContextCompat.getColor(mContext, R.color.prompt_low_text_color);
            }
            myViewHolder.mStateTextView.setText(stateMsg);
            myViewHolder.mStateTextView.setTextColor(color);
        }

        myViewHolder.mNumberTextView.setText(number);
        myViewHolder.mTaxRateTextView.setText(taxRate);

        if (null == drawingPic || drawingPic.size() == 0) {
            myViewHolder.mView.setVisibility(View.GONE);
            myViewHolder.mRecyclerView.setVisibility(View.GONE);
        } else {
            myViewHolder.mView.setVisibility(View.VISIBLE);
            myViewHolder.mRecyclerView.setVisibility(View.VISIBLE);
        }

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager plantManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // 布局横向
        plantManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        myViewHolder.mRecyclerView.setLayoutManager(plantManager);
        // 设置RecyclerView间距,每一行Item的数目1000(10000为假设的数据)
        int gap = DensityUtil.dpToPx(mContext, 8);
        GridSpacingItemDecorations decoration = new GridSpacingItemDecorations(10000, gap, 0, false);
        // 判断是否已经添加了ItemDecorations,如果存在就不添加(解决RecyclerView刷新ItemDecorations间距不断增加问题)
        if (myViewHolder.mRecyclerView.getItemDecorationCount() == 0) {
            myViewHolder.mRecyclerView.addItemDecoration(decoration);
        }
        QuotationDesignAdapter adapter = new QuotationDesignAdapter(mContext, drawingPic);
        myViewHolder.mRecyclerView.setAdapter(adapter);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * 客户名称
         */
        TextView mCustomerNameTextView;

        /**
         * 状态
         */
        TextView mStateTextView;

        /**
         * 共X盆,金额:X /月
         */
        TextView mNumberTextView;

        /**
         * 已含X%税率,优惠X
         */
        TextView mTaxRateTextView;

        /**
         *
         */
        View mView;

        /**
         *
         */
        RecyclerView mRecyclerView;

        /**
         * 再次报价
         */
        Button mQuotationAgainButton;

        /**
         * 分享
         */
        Button mShareButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mCustomerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            mStateTextView = itemView.findViewById(R.id.stateTextView);
            mNumberTextView = itemView.findViewById(R.id.numberTextView);
            mTaxRateTextView = itemView.findViewById(R.id.taxRateTextView);

            mView = itemView.findViewById(R.id.view);
            mRecyclerView = itemView.findViewById(R.id.recyclerView);
            mQuotationAgainButton = itemView.findViewById(R.id.quotationAgainButton);
            mShareButton = itemView.findViewById(R.id.shareButton);

            mQuotationAgainButton.setOnClickListener(v -> {
                // 4.在点击事件中调用接口里的方法
                if (null != onQuotationAgainItemClickListener) {
                    // holder.getLayoutPosition()方法表示获得当前所点击item的真正位置
                    onQuotationAgainItemClickListener.onQuotationAgainItemClick(getLayoutPosition());
                }
            });
            mShareButton.setOnClickListener(v -> {
                // 4.在点击事件中调用接口里的方法
                if (null != onShareItemClickListener) {
                    // holder.getLayoutPosition()方法表示获得当前所点击item的真正位置
                    onShareItemClickListener.onShareItemClick(getLayoutPosition());
                }
            });
            itemView.setOnClickListener(v -> {
                // 4.在点击事件中调用接口里的方法
                if (null != onItemClickListener) {
                    // holder.getLayoutPosition()方法表示获得当前所点击item的真正位置
                    onItemClickListener.onItemClick(getLayoutPosition());
                }
            });

            // 屏蔽item中嵌套的RecycleView的点击事件
            mRecyclerView.setOnTouchListener((v, event) -> itemView.onTouchEvent(event));
        }
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnItemClickListener {

        /**
         * 在RecyclerView Item点击时回调
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

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnQuotationAgainItemClickListener {

        /**
         * 在RecyclerView 再次报价Item点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onQuotationAgainItemClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnQuotationAgainItemClickListener onQuotationAgainItemClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onQuotationAgainItemClickListener RecyclerView Item点击的侦听器
     */
    public void setQuotationAgainOnItemClickListener(OnQuotationAgainItemClickListener onQuotationAgainItemClickListener) {
        this.onQuotationAgainItemClickListener = onQuotationAgainItemClickListener;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnShareItemClickListener {

        /**
         * 在RecyclerView 分享Item点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onShareItemClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnShareItemClickListener onShareItemClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onShareItemClickListener RecyclerView Item点击的侦听器
     */
    public void setShareOnItemClickListener(OnShareItemClickListener onShareItemClickListener) {
        this.onShareItemClickListener = onShareItemClickListener;
    }
}
