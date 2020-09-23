package com.ybw.yibai.common.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.FeedBackImage;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.feedback.FeedBackActivity;

import java.io.File;
import java.util.List;

/**
 * 意见反馈界面{@link FeedBackActivity}显示意见反馈图片的适配器
 *
 * @author sjl
 */
public class FeedBackImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private LayoutInflater mInflater;

    private List<FeedBackImage> mFeedBackImageList;

    public FeedBackImageAdapter(Context context, List<FeedBackImage> feedBackImageList) {
        this.mContext = context;
        this.mFeedBackImageList = feedBackImageList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mFeedBackImageList == null ? 0 : mFeedBackImageList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder;
        View view = mInflater.inflate(R.layout.recyclerview_feed_back_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;

        FeedBackImage feedBackImage = mFeedBackImageList.get(position);
        int type = feedBackImage.getType();
        File file = feedBackImage.getFile();
        if (1 == type) {
            viewHolder.mAddImageView.setImageDrawable(mContext.getDrawable(R.mipmap.add));
            viewHolder.mDeleteImageView.setImageDrawable(null);
        } else {
            ImageUtil.displayImage(mContext, viewHolder.mAddImageView, file);
            viewHolder.mDeleteImageView.setImageDrawable(mContext.getDrawable(R.mipmap.image_adjustment_false));
        }
        // 最多可以上传4张图片,多于4张就隐藏
        if (4 == position) {
            viewHolder.mAddImageView.setVisibility(View.GONE);
        } else {
            viewHolder.mAddImageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     */
    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout mItemLayout;

        /**
         * 添加
         */
        ImageView mAddImageView;

        /**
         * 删除
         */
        ImageView mDeleteImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemLayout = itemView.findViewById(R.id.itemLayout);
            mAddImageView = itemView.findViewById(R.id.addImageView);
            mDeleteImageView = itemView.findViewById(R.id.deleteImageView);

            mAddImageView.setOnClickListener(this);
            mDeleteImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // holder.getLayoutPosition()方法表示获得当前所点击item的真正位置
            int position = getLayoutPosition();

            int id = v.getId();
            int type = mFeedBackImageList.get(position).getType();

            // 删除图片
            if (id == R.id.deleteImageView && 2 == type) {
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(mItemLayout, "scaleX", 1f, 0);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(mItemLayout, "scaleY", 1f, 0);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(scaleX).with(scaleY);
                animatorSet.setDuration(200);
                animatorSet.start();
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // 因复用布局,所以将控件删除的时候需要将他还原
                        mItemLayout.setScaleX(1f);
                        mItemLayout.setScaleY(1f);
                        // 4.在点击事件中调用接口里的方法
                        if (null == onItemClickListener) {
                            return;
                        }
                        onItemClickListener.onItemClick(v, position);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 因复用布局,所以将控件删除的时候需要将他还原
                        mItemLayout.setScaleX(1f);
                        mItemLayout.setScaleY(1f);
                        // 4.在点击事件中调用接口里的方法
                        if (null == onItemClickListener) {
                            return;
                        }
                        onItemClickListener.onItemClick(v, position);
                    }
                });
            }

            // 添加图片
            if (id == R.id.addImageView && 1 == type) {
                // 4.在点击事件中调用接口里的方法
                if (null == onItemClickListener) {
                    return;
                }
                onItemClickListener.onItemClick(v, position);
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
         * @param v        被点击的View
         * @param position 被点击的Item位置
         */
        void onItemClick(View v, int position);
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
