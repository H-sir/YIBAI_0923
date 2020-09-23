package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.Address.DataBean.ListBean;

import java.util.List;

/**
 * 收货地址适配器
 *
 * @author sjl
 */
public class ShippingAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 收货地址的集合
     */
    private List<ListBean> mShippingAddressList;

    public ShippingAddressAdapter(Context context, List<ListBean> shippingAddressList) {
        this.mContext = context;
        this.mShippingAddressList = shippingAddressList;
    }

    @Override
    public int getItemCount() {
        return mShippingAddressList == null ? 0 : mShippingAddressList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_shipping_address_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean listBean = mShippingAddressList.get(position);
        String consignee = listBean.getConsignee();
        String mobile = listBean.getMobile();
        String province = listBean.getProvince();
        String city = listBean.getCity();
        String area = listBean.getArea();
        String address = listBean.getAddress();
        int isDefault = listBean.getIsdefault();

        String consigneeMobile = consignee + "  " + mobile;
        if (TextUtils.isEmpty(consigneeMobile)) {
            myViewHolder.mNameTextView.setText(" ");
        } else {
            myViewHolder.mNameTextView.setText(consigneeMobile);
        }
        String addressString = province + city + area + address;
        if (TextUtils.isEmpty(addressString)) {
            myViewHolder.mAddressTextView.setText(" ");
        } else {
            myViewHolder.mAddressTextView.setText(addressString);
        }

        if (1 == isDefault) {
            myViewHolder.mDefaultTextView.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.mDefaultTextView.setVisibility(View.GONE);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * 姓名与电话
         */
        TextView mNameTextView;

        /**
         * 地址
         */
        TextView mAddressTextView;

        /**
         * 是否为默认收货地址
         */
        TextView mDefaultTextView;

        /**
         * 编辑
         */
        RelativeLayout mEditLayout;

        private MyViewHolder(View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.nameTextView);
            mAddressTextView = itemView.findViewById(R.id.addressTextView);
            mDefaultTextView = itemView.findViewById(R.id.defaultTextView);
            mEditLayout = itemView.findViewById(R.id.editLayout);

            mEditLayout.setOnClickListener(v -> onItemClickListener.onItemClick(getLayoutPosition(), true));
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(getLayoutPosition(), false));
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
         * @param position     被点击的Item位置
         * @param isEditLayout 点击的区域是否为"编辑"区域
         */
        void onItemClick(int position, boolean isEditLayout);
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
