package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.SimilarSKU.DataBean.ListBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.details.ProductDetailsActivity;

import java.util.List;

/**
 * 相似产品列表适配器
 *
 * @author sjl
 * {@link ProductDetailsActivity}
 */
public class SimilarProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 相似推荐sku集合
     */
    private List<ListBean> mSimilarSkuList;

    public SimilarProductAdapter(Context context, List<ListBean> similarSkuList) {
        this.mContext = context;
        this.mSimilarSkuList = similarSkuList;
    }

    @Override
    public int getItemCount() {
        return mSimilarSkuList == null ? 0 : mSimilarSkuList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_similar_product_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean listBean = mSimilarSkuList.get(position);
        String pic = listBean.getPic();

        if (TextUtils.isEmpty(pic)) {
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, " ");
            myViewHolder.mImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
        } else {
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, pic, DensityUtil.dpToPx(mContext, 2));
            myViewHolder.mImageView.setBackground(null);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;

        private MyViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(this);
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
