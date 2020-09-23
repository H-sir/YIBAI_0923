package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.Case.DataBean.ListBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.cases.CaseFragment;

import java.util.List;

import static com.ybw.yibai.common.utils.ImageUtil.drawableToBitmap;

/**
 * 案例界面的适配器
 *
 * @author sjl
 * {@link CaseFragment}
 */
public class CaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "CaseAdapter";

    /**
     * Item宽度
     */
    private int mItemWidth;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 公司案例列表
     */
    private List<ListBean> mCaseList;

    public CaseAdapter(Context context, List<ListBean> caseList) {
        this.mContext = context;
        this.mCaseList = caseList;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mItemWidth = (dm.widthPixels - DensityUtil.dpToPx(mContext, 16 + 5 + 16)) / 2;
    }

    @Override
    public int getItemCount() {
        return mCaseList == null ? 0 : mCaseList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_case_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean listBean = mCaseList.get(position);
        String pic = listBean.getPic_M();

        if (TextUtils.isEmpty(pic)) {
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, " ");
        } else {
            downloadImage(mContext, myViewHolder.mImageView, pic);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        private MyViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(v -> {
                // 4.在点击事件中调用接口里的方法
                if (null != onItemClickListener) {
                    // holder.getLayoutPosition()方法表示获得当前所点击item的真正位置
                    onItemClickListener.onItemClick(getLayoutPosition());
                }
            });
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

    /*----------*/

    /**
     * 下载图片
     *
     * @param context   上下文大小
     * @param imageView ImageView对象
     * @param imageUrl  图片url地址
     */
    private void downloadImage(Context context, ImageView imageView, String imageUrl) {
        /*RoundedBitmapDrawable*/
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = drawableToBitmap(resource);
                if (null == bitmap) {
                    return;
                }
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.width = mItemWidth;
                params.height = height * mItemWidth / width;
                imageView.setLayoutParams(params);
                imageView.setImageDrawable(resource);
            }
        };
        CenterCrop centerCrop = new CenterCrop();
        RoundedCorners roundedCorners = new RoundedCorners(DensityUtil.dpToPx(mContext, 2));
        RequestOptions transforms = new RequestOptions().transforms(centerCrop, roundedCorners);
        Glide.with(context).load(imageUrl).apply(transforms).into(simpleTarget);
    }
}
