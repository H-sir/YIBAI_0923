package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.MarketListBean;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.designdetails.DesignDetailsActivity;

import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   : 城市列表适配器
 * </pre>
 */
public class SelectAddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "CityListAdapter";

    /**
     * 上下文对象
     */
    private Context mContext;

    private List<MarketListBean.DataBean.ListBean> dataBeanList;

    public SelectAddressListAdapter(Context context, List<MarketListBean.DataBean.ListBean> dataBeanList) {
        this.mContext = context;
        this.dataBeanList = dataBeanList;
    }

    @Override
    public int getItemCount() {
        return dataBeanList == null ? 0 : dataBeanList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.listview_select_address_list_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        MarketListBean.DataBean.ListBean listBean = dataBeanList.get(position);
        myViewHolder.selectAddressName.setText(listBean.getName());
        myViewHolder.selectAddressText.setText(listBean.getAddress());
        myViewHolder.selectAddressDistance.setText(listBean.getDistance());
        if (listBean.isCheck()) {
            myViewHolder.productAllSelectImg.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.productAllSelectImg.setVisibility(View.GONE);
        }
        ImageUtil.displayImage(mContext, myViewHolder.selectAddressImg, listBean.getLogo());
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyViewHolder(View itemView) {
            super(itemView);

            productAllSelectImg = itemView.findViewById(R.id.productAllSelectImg);
            selectAddressImg = itemView.findViewById(R.id.selectAddressImg);
            selectAddressName = itemView.findViewById(R.id.selectAddressName);
            selectAddressText = itemView.findViewById(R.id.selectAddressText);
            selectAddressDistance = itemView.findViewById(R.id.selectAddressDistance);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onCityClickListener != null)
                onCityClickListener.onCityClick(getLayoutPosition());
        }

        ImageView productAllSelectImg;
        ImageView selectAddressImg;
        TextView selectAddressName;
        TextView selectAddressText;
        TextView selectAddressDistance;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnCityClickListener {

        /**
         * 在删除按钮点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onCityClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnCityClickListener onCityClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onCityClickListener 删除按钮点击的侦听器
     */
    public void setOnCityClickListener(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }
}