package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.module.producttype.ProductTypeFragment;

import java.util.List;

/**
 * 产品列表适配器
 *
 * @author sjl
 * {@link ProductTypeFragment}
 */
public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "ProductAdapter";

    /**
     * Item宽度
     */
    private int mItemWidth;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 产品sku列表
     */
    private List<ListBean> mSKUList;

    /**
     * 每一行Item的数目
     */
    private int mSpanCount;

    public ProductAdapter(Context context, List<ListBean> SKUList) {
        this.mContext = context;
        this.mSKUList = SKUList;
        mItemWidth = DensityUtil.dpToPx(mContext, 104);
    }

    public ProductAdapter(Context context, List<ListBean> SKUList, int spanCount) {
        this.mContext = context;
        this.mSKUList = SKUList;
        this.mSpanCount = spanCount;
        mItemWidth = DensityUtil.dpToPx(mContext, 156);
    }

    @Override
    public int getItemCount() {
        return mSKUList == null ? 0 : mSKUList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_product_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean listBean = mSKUList.get(position);
        String thumbPic1 = listBean.getThumb_pic1();
        String pic1 = listBean.getPic1();
        String pic2 = listBean.getPic2();
        String pic3 = listBean.getPic3();
        String name = listBean.getName();

        if (!TextUtils.isEmpty(thumbPic1)) {
            Picasso.with(mContext).load(thumbPic1).into(myViewHolder.mImageView);
            // ImageUtil.displayImages(mContext, myViewHolder.mImageView, thumbPic1, DensityUtil.dpToPx(mContext, 2));
            myViewHolder.mImageView.setBackground(null);
        } else if (!TextUtils.isEmpty(pic1)) {
            Picasso.with(mContext).load(pic1).into(myViewHolder.mImageView);
            // ImageUtil.displayImages(mContext, myViewHolder.mImageView, pic1, DensityUtil.dpToPx(mContext, 2));
            myViewHolder.mImageView.setBackground(null);
        } else if (!TextUtils.isEmpty(pic2)) {
            Picasso.with(mContext).load(pic2).into(myViewHolder.mImageView);
            // ImageUtil.displayImages(mContext, myViewHolder.mImageView, pic2, DensityUtil.dpToPx(mContext, 2));
            myViewHolder.mImageView.setBackground(null);
        } else if (!TextUtils.isEmpty(pic3)) {
            Picasso.with(mContext).load(pic3).into(myViewHolder.mImageView);
            // ImageUtil.displayImages(mContext, myViewHolder.mImageView, pic3, DensityUtil.dpToPx(mContext, 2));
            myViewHolder.mImageView.setBackground(null);
        } else {
            Picasso.with(mContext).load(" ").into(myViewHolder.mImageView);
            // ImageUtil.displayImage(mContext, myViewHolder.mImageView, " ");
            myViewHolder.mImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
        }
        if (TextUtils.isEmpty(name)) {
            myViewHolder.mTextView.setText("");
        } else {
            myViewHolder.mTextView.setText(name);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout mLinearLayout;

        ImageView mImageView;

        TextView mTextView;

        private MyViewHolder(View itemView) {
            super(itemView);

            mLinearLayout = itemView.findViewById(R.id.rootLayout);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.textView);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLinearLayout.getLayoutParams();
            layoutParams.weight = mItemWidth;
            mLinearLayout.setLayoutParams(layoutParams);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mImageView.getLayoutParams();
            params.weight = mItemWidth;
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
