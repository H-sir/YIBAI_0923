package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.ProductScreeningParam.DataBean.ParamBean.SonBean;

import java.util.List;

/**
 * 显示产品筛选文字参数的Adapter
 *
 * @author sjl
 * {@link ProductScreeningParamAdapter}
 */
public class ProductScreeningTextParamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "ProductScreeningTextParamAdapter";

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     *
     */
    private List<SonBean> mSonList;

    public ProductScreeningTextParamAdapter(Context context, List<SonBean> sonList) {
        this.mContext = context;
        this.mSonList = sonList;
    }

    @Override
    public int getItemCount() {
        return mSonList == null ? 0 : mSonList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_product_screening_param_text_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        SonBean sonBean = mSonList.get(position);
        String name = sonBean.getName();
        boolean selected = sonBean.isSelected();

        if (TextUtils.isEmpty(name)) {
            myViewHolder.mNameTextView.setText("");
        } else {
            myViewHolder.mNameTextView.setText(name);
        }

        if (selected) {
            myViewHolder.mNameTextView.setTextColor(ContextCompat.getColor(mContext, R.color.embellishment_text_color));
            myViewHolder.mNameTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_product_screening_text_selected));
        } else {
            myViewHolder.mNameTextView.setTextColor(ContextCompat.getColor(mContext, R.color.prompt_low_text_color));
            myViewHolder.mNameTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_product_screening_text_unselected));
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mNameTextView;

        private MyViewHolder(View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.nameTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // holder.getLayoutPosition()方法表示获得当前所点击item的真正位置
            int position = getLayoutPosition();
            SonBean sonBean = mSonList.get(position);
            boolean selected = sonBean.isSelected();
            if (selected) {
                sonBean.setSelected(false);
            } else {
                sonBean.setSelected(true);
            }
            notifyDataSetChanged();
            // 4.在点击事件中调用接口里的方法
            if (null != onChildItemClickListener) {
                onChildItemClickListener.onChildItemClick(position);
            }
        }
    }

    /*
     /\_/\
   =( °w° )=
     )   (
    (__ __)
   */

    /**
     * 1.定义一个接口
     */
    public interface OnChildItemClickListener {

        /**
         * 在RecyclerView Item 点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onChildItemClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnChildItemClickListener onChildItemClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onChildItemClickListener RecyclerView Item点击的侦听器
     */
    public void setOnItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        this.onChildItemClickListener = onChildItemClickListener;
    }
}
