package com.ybw.yibai.common.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.CreateSceneData;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.startdesign.StartDesignActivity;

import java.io.File;
import java.util.List;

/**
 * 开始设计界面场景列表适配器
 *
 * @author sjl
 * {@link StartDesignActivity}
 */
public class SceneBackgroundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 创建场景时需要的数据
     */
    private List<CreateSceneData> mCreateSceneDataList;

    public SceneBackgroundAdapter(Context context, List<CreateSceneData> createSceneDataList) {
        this.mContext = context;
        this.mCreateSceneDataList = createSceneDataList;
    }

    @Override
    public int getItemCount() {
        return mCreateSceneDataList == null ? 0 : mCreateSceneDataList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_scene_background_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        CreateSceneData createSceneData = mCreateSceneDataList.get(position);
        File file = createSceneData.getFile();
        String name = createSceneData.getName();

        ImageUtil.displayImage(mContext, myViewHolder.mSceneBackgroundImageView, file, DensityUtil.dpToPx(mContext, 2));
        if (TextUtils.isEmpty(name)) {
            myViewHolder.mNameTextView.setText(mContext.getString(R.string.select_scene));
        } else {
            myViewHolder.mNameTextView.setText(name);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout mItemLayout;

        /**
         * 场景背景
         */
        ImageView mSceneBackgroundImageView;

        /**
         * 删除
         */
        ImageView mDeleteImageView;

        /**
         * 场景名称
         */
        TextView mNameTextView;

        private MyViewHolder(View itemView) {
            super(itemView);

            mItemLayout = itemView.findViewById(R.id.itemLayout);
            mSceneBackgroundImageView = itemView.findViewById(R.id.sceneBackgroundImageView);
            mDeleteImageView = itemView.findViewById(R.id.deleteImageView);
            mNameTextView = itemView.findViewById(R.id.nameTextView);

            mDeleteImageView.setOnClickListener(this);
            mNameTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            // holder.getLayoutPosition()方法表示获得当前所点击item的真正位置
            int position = getLayoutPosition();

            // 删除
            if (id == R.id.deleteImageView) {
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
                        if (null == onDeleteItemClickListener) {
                            return;
                        }
                        onDeleteItemClickListener.onDeleteItemClick(position);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 因复用布局,所以将控件删除的时候需要将他还原
                        mItemLayout.setScaleX(1f);
                        mItemLayout.setScaleY(1f);
                        // 4.在点击事件中调用接口里的方法
                        if (null == onDeleteItemClickListener) {
                            return;
                        }
                        onDeleteItemClickListener.onDeleteItemClick(position);
                    }
                });
            }

            // 名称
            if (id == R.id.nameTextView) {
                // 4.在点击事件中调用接口里的方法
                if (null == onItemClickListener) {
                    return;
                }
                onItemClickListener.onSceneNameItemClick(position);
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
    public interface OnItemClickListener {

        /**
         * 在RecyclerView Item 里的场景名称被点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onSceneNameItemClick(int position);
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

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 1.定义一个接口
     */
    public interface OnDeleteItemClickListener {

        /**
         * 在RecyclerView Item 里的删除被点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onDeleteItemClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnDeleteItemClickListener onDeleteItemClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onDeleteItemClickListener RecyclerView Item点击的侦听器
     */
    public void setOnDeleteItemClickListener(OnDeleteItemClickListener onDeleteItemClickListener) {
        this.onDeleteItemClickListener = onDeleteItemClickListener;
    }
}
