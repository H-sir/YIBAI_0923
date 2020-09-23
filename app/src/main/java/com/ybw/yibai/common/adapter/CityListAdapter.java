package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.CityListBean;

import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   : 城市列表适配器
 * </pre>
 */
public class CityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "CityListAdapter";

    /**
     * 上下文对象
     */
    private Context mContext;

    private List<CityListBean.DataBean.ListBean> dataBeanList;

    public CityListAdapter(Context context, List<CityListBean.DataBean.ListBean> dataBeanList) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.listview_city_list_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        CityListBean.DataBean.ListBean listBean = dataBeanList.get(position);
        myViewHolder.mCityTextView.setText(listBean.getRegionName());
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyViewHolder(View itemView) {
            super(itemView);

            mCityTextView = itemView.findViewById(R.id.cityTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onCityClickListener != null)
                onCityClickListener.onCityClick(getLayoutPosition());
        }

        TextView mCityTextView;
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