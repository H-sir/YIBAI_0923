package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.recommendproduct.RecommendProductFragment;

import java.util.List;

/**
 * 推荐产品的适配器
 *
 * @author sjl
 * <p>
 * {@link RecommendProductFragment}
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Item宽度
     */
    private int mItemWidth;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * SKU列表
     */
    private List<ListBean> mSKUList;

    public RecommendAdapter(Context context, List<ListBean> SKUList) {
        this.mContext = context;
        this.mSKUList = SKUList;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mItemWidth = (dm.widthPixels - DensityUtil.dpToPx(mContext, 10 * 6)) / 5;
    }

    @Override
    public int getItemCount() {
        return mSKUList == null ? 0 : mSKUList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_sku_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean listBean = mSKUList.get(position);

        String name = listBean.getName();
        String pic = listBean.getPic2();

        if (TextUtils.isEmpty(pic)) {
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, " ");
            myViewHolder.mImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
        } else {
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, pic, DensityUtil.dpToPx(mContext, 2));
            myViewHolder.mImageView.setBackground(null);
        }

        if (TextUtils.isEmpty(name)) {
            myViewHolder.mTextView.setText("");
        } else {
            myViewHolder.mTextView.setText(name);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;

        TextView mTextView;

        private MyViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.textView);

            ViewGroup.LayoutParams params = mImageView.getLayoutParams();
            params.width = mItemWidth;
            params.height = mItemWidth;
            mImageView.setLayoutParams(params);

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
