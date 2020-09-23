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
import com.ybw.yibai.common.bean.CustomerList.DataBean.ListBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.customerlist.CustomerListFragment;

import java.util.List;

/**
 * 客户列表适配器
 *
 * @author sjl
 * {@link CustomerListFragment}
 */
public class CustomerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 客户列表
     */
    private List<ListBean> mCustomersList;

    public CustomerListAdapter(Context context, List<ListBean> customersList) {
        this.mContext = context;
        this.mCustomersList = customersList;
    }

    @Override
    public int getItemCount() {
        return mCustomersList == null ? 0 : mCustomersList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_customer_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        ListBean listBean = mCustomersList.get(position);
        String logo = listBean.getLogo();
        String name = listBean.getName();
        String address = listBean.getAddress();
        String area = listBean.getArea();

        if (!TextUtils.isEmpty(logo)) {
            myViewHolder.mImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, logo, DensityUtil.dpToPx(mContext, 2));
        } else {
            myViewHolder.mImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, " ");
        }

        if (!TextUtils.isEmpty(name)) {
            myViewHolder.mNameTextView.setText(name);
        } else {
            myViewHolder.mNameTextView.setText(" ");
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(address)) {
            stringBuilder.append(address);
        }
        if (!TextUtils.isEmpty(area)) {
            stringBuilder.append(area);
        }
        String string = stringBuilder.toString();
        if (!TextUtils.isEmpty(string)) {
            myViewHolder.mAddressTextView.setText(string);
        } else {
            myViewHolder.mAddressTextView.setText(" ");
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        TextView mNameTextView;

        TextView mAddressTextView;

        private MyViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mNameTextView = itemView.findViewById(R.id.nameTextView);
            mAddressTextView = itemView.findViewById(R.id.addressTextView);

            itemView.setOnClickListener(v -> {
                // 4.在点击事件中调用接口里的方法
                if (null != onItemClickListener) {
                    // holder.getLayoutPosition()方法表示获得当前所点击item的真正位置
                    onItemClickListener.onItemClick(getLayoutPosition());
                }
            });
        }
    }

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