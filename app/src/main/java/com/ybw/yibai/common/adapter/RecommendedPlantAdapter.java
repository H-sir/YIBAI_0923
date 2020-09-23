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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.collocation.CollocationActivity;

import java.util.List;

/**
 * 推荐植物列表适配器
 *
 * @author sjl
 * {@link CollocationActivity}
 */
public class RecommendedPlantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Item宽度
     */
    private int mItemWidth;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 推荐植物列表
     */
    private List<ListBean> mRecommendedPlantList;

    public RecommendedPlantAdapter(Context context, List<ListBean> recommendedPlantList) {
        this.mContext = context;
        this.mRecommendedPlantList = recommendedPlantList;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mItemWidth = (dm.widthPixels - DensityUtil.dpToPx(mContext, 10 * 6)) / 5;
    }

    @Override
    public int getItemCount() {
        return mRecommendedPlantList == null ? 0 : mRecommendedPlantList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_recommended_plant_pot_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean listBean = mRecommendedPlantList.get(position);
        String name = listBean.getName();
        String pic1 = listBean.getPic1();
        String pic2 = listBean.getPic2();
        String pic3 = listBean.getPic3();
        boolean select = listBean.isSelect();

        if (!TextUtils.isEmpty(pic2)) {
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, pic2, DensityUtil.dpToPx(mContext, 2));
            myViewHolder.mImageView.setBackground(null);
        } else if (!TextUtils.isEmpty(pic1)) {
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, pic1, DensityUtil.dpToPx(mContext, 2));
            myViewHolder.mImageView.setBackground(null);
        } else if (!TextUtils.isEmpty(pic3)) {
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, pic3, DensityUtil.dpToPx(mContext, 2));
            myViewHolder.mImageView.setBackground(null);
        } else {
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, " ");
            myViewHolder.mImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
        }

        if (TextUtils.isEmpty(name)) {
            myViewHolder.mTextView.setText("");
        } else {
            myViewHolder.mTextView.setText(name);
        }

        if (select) {
            myViewHolder.mItemLayout.setBackground(mContext.getDrawable(R.drawable.background_item_selected));
        } else {
            myViewHolder.mItemLayout.setBackground(null);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout mItemLayout;

        ImageView mImageView;

        TextView mTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemLayout = itemView.findViewById(R.id.itemLayout);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.textView);

            ViewGroup.LayoutParams params = mItemLayout.getLayoutParams();
            params.width = mItemWidth;
            params.height = mItemWidth;
            mItemLayout.setLayoutParams(params);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            onSelectStateChanged(position);
        }
    }

    /*----------*/

    /**
     * 当用户滑动ViewPager时,需要选中对应位置的Item
     *
     * @param position 用户选中ViewPager的Item当前的位置
     */
    public void onSelectStateChanged(int position) {
        for (int i = 0; i < mRecommendedPlantList.size(); i++) {
            ListBean listBean = mRecommendedPlantList.get(i);
            if (i == position) {
                listBean.setSelect(true);
            } else {
                listBean.setSelect(false);
            }
        }
        notifyDataSetChanged();
        // 4.在点击事件中调用接口里的方法
        if (null == onItemClickListener) {
            return;
        }
        onItemClickListener.onPlantItemClick(position);
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
        void onPlantItemClick(int position);
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
