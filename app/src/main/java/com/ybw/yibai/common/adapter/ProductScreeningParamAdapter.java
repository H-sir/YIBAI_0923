package com.ybw.yibai.common.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.ProductScreeningParam.DataBean.ParamBean;
import com.ybw.yibai.common.bean.ProductScreeningParam.DataBean.ParamBean.SonBean;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.module.filter.FilterFragment;

import java.util.List;

import static com.ybw.yibai.common.utils.OtherUtil.stringFormat;

/**
 * 显示产品筛选参数的Adapter
 *
 * @author sjl
 * {@link FilterFragment}
 */
public class ProductScreeningParamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "ProductScreeningParamAdapter";

    /**
     * 文字类型数据
     */
    private static final int TYPE_TEXT = 0;

    /**
     * 图文类型数据
     */
    private static final int TYPE_IMAGE_TEXT = 1;

    /**
     * 颜色类型数据
     */
    private static final int TYPE_COLOR = 2;

    /**
     * 范围类型数据
     */
    private static final int TYPE_SCOPE = 3;

   /*
     /\_/\
   =( °w° )=
     )   (
    (__ __)
   */

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     *
     */
    private List<ParamBean> mParamList;

    public ProductScreeningParamAdapter(Context context, List<ParamBean> paramList) {
        this.mContext = context;
        this.mParamList = paramList;
    }

    @Override
    public int getItemCount() {
        return mParamList == null ? 0 : mParamList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = mParamList.get(position).getScreen_type();
        if (1 == type) {
            return TYPE_TEXT;
        } else if (2 == type) {
            return TYPE_IMAGE_TEXT;
        } else if (3 == type) {
            return TYPE_COLOR;
        } else {
            return TYPE_SCOPE;
        }
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == TYPE_TEXT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_product_screening_param_text_layout, null);
            viewHolder = new TextViewHolder(view);
        } else if (viewType == TYPE_IMAGE_TEXT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_product_screening_param_textimage_layout, null);
            viewHolder = new ImageTextViewHolder(view);
        } else if (viewType == TYPE_COLOR) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_product_screening_param_color_layout, null);
            viewHolder = new ColorViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_product_screening_param_scope_layout, null);
            viewHolder = new ScopeViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int itemViewType = getItemViewType(position);
        ParamBean paramBean = mParamList.get(position);
        int max = paramBean.getMax();
        int min = paramBean.getMin();
        float leftValue = paramBean.getLeftValue();
        float rightValue = paramBean.getRightValue();
        String name = paramBean.getName();
        String sub = paramBean.getSub();
        List<SonBean> sonList = paramBean.getSon();

        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(name)) {
            stringBuilder.append(name);
        }
        if (!TextUtils.isEmpty(sub)) {
            stringBuilder.append(sub);
        }
        String text = stringBuilder.toString().trim();
        SpannableStringBuilder stringFormat = stringFormat(mContext, text, sub, 12, ContextCompat.getColor(mContext, R.color.prompt_high_text_color));

        if (itemViewType == TYPE_TEXT) {
            TextViewHolder textViewHolder = (TextViewHolder) viewHolder;

            if (TextUtils.isEmpty(stringFormat)) {
                textViewHolder.mNameTextView.setText("");
            } else {
                textViewHolder.mNameTextView.setText(stringFormat);
            }
            // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            // 设置RecyclerView间距
            GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(4, DensityUtil.dpToPx(mContext, 8), false);
            textViewHolder.mRecyclerView.setLayoutManager(manager);
            // 判断是否已经添加了ItemDecorations,如果存在就不添加(解决RecyclerView刷新ItemDecorations间距不断增加问题)
            if (textViewHolder.mRecyclerView.getItemDecorationCount() == 0) {
                textViewHolder.mRecyclerView.addItemDecoration(decoration);
            }
            // 创建子Adapter
            ProductScreeningTextParamAdapter mAdapter = new ProductScreeningTextParamAdapter(mContext, sonList);
            textViewHolder.mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(childPosition -> {
                // 4.在点击事件中调用接口里的方法
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(position, childPosition);
                }
            });
        } else if (itemViewType == TYPE_IMAGE_TEXT) {
            ImageTextViewHolder imageTextViewHolder = (ImageTextViewHolder) viewHolder;

            if (TextUtils.isEmpty(stringFormat)) {
                imageTextViewHolder.mNameTextView.setText("");
            } else {
                imageTextViewHolder.mNameTextView.setText(stringFormat);
            }

            // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            // 设置RecyclerView间距
            GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(3, DensityUtil.dpToPx(mContext, 8), false);
            imageTextViewHolder.mRecyclerView.setLayoutManager(manager);
            // 判断是否已经添加了ItemDecorations,如果存在就不添加(解决RecyclerView刷新ItemDecorations间距不断增加问题)
            if (imageTextViewHolder.mRecyclerView.getItemDecorationCount() == 0) {
                imageTextViewHolder.mRecyclerView.addItemDecoration(decoration);
            }
            // 创建子Adapter
            ProductScreeningImageTextParamAdapter mAdapter = new ProductScreeningImageTextParamAdapter(mContext, sonList);
            imageTextViewHolder.mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(childPosition -> {
                // 4.在点击事件中调用接口里的方法
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(position, childPosition);
                }
            });
        } else if (itemViewType == TYPE_COLOR) {
            ColorViewHolder colorViewHolder = (ColorViewHolder) viewHolder;

            if (TextUtils.isEmpty(stringFormat)) {
                colorViewHolder.mNameTextView.setText("");
            } else {
                colorViewHolder.mNameTextView.setText(stringFormat);
            }

            // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(8, StaggeredGridLayoutManager.VERTICAL);
            // 设置RecyclerView间距
            GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(8, DensityUtil.dpToPx(mContext, 8), false);
            colorViewHolder.mRecyclerView.setLayoutManager(manager);
            // 判断是否已经添加了ItemDecorations,如果存在就不添加(解决RecyclerView刷新ItemDecorations间距不断增加问题)
            if (colorViewHolder.mRecyclerView.getItemDecorationCount() == 0) {
                colorViewHolder.mRecyclerView.addItemDecoration(decoration);
            }
            // 创建子Adapter
            ProductScreeningColorParamAdapter mAdapter = new ProductScreeningColorParamAdapter(mContext, sonList);
            colorViewHolder.mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(childPosition -> {
                // 4.在点击事件中调用接口里的方法
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(position, childPosition);
                }
            });
        } else {
            ScopeViewHolder scopeViewHolder = (ScopeViewHolder) viewHolder;

            if (TextUtils.isEmpty(stringFormat)) {
                scopeViewHolder.mNameTextView.setText("");
            } else {
                scopeViewHolder.mNameTextView.setText(stringFormat);
            }

            // 设置范围
            scopeViewHolder.mRangeSeekBar.setRange(min, max);
            // 设置当前进度
            scopeViewHolder.mRangeSeekBar.setProgress(leftValue, rightValue);
            scopeViewHolder.mRangeSeekBar.setIndicatorTextDecimalFormat("0");
            scopeViewHolder.mRangeSeekBar.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    /**
     * 文字类型
     */
    private class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout mTopLayout;
        TextView mNameTextView;
        ImageView mImageView;
        RecyclerView mRecyclerView;

        private TextViewHolder(View itemView) {
            super(itemView);

            mTopLayout = itemView.findViewById(R.id.topLayout);
            mNameTextView = itemView.findViewById(R.id.nameTextView);
            mImageView = itemView.findViewById(R.id.imageView);
            mRecyclerView = itemView.findViewById(R.id.recyclerView);

            mTopLayout.setOnClickListener(this);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.topLayout || id == R.id.imageView) {
                if (mRecyclerView.getVisibility() == View.VISIBLE) {
                    mRecyclerView.setVisibility(View.GONE);
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.up));
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.down));
                }
            }
        }
    }

    /**
     * 图文类型
     */
    private class ImageTextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout mTopLayout;
        TextView mNameTextView;
        ImageView mImageView;
        RecyclerView mRecyclerView;

        private ImageTextViewHolder(View itemView) {
            super(itemView);

            mTopLayout = itemView.findViewById(R.id.topLayout);
            mNameTextView = itemView.findViewById(R.id.nameTextView);
            mImageView = itemView.findViewById(R.id.imageView);
            mRecyclerView = itemView.findViewById(R.id.recyclerView);

            mTopLayout.setOnClickListener(this);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.topLayout || id == R.id.imageView) {
                if (mRecyclerView.getVisibility() == View.VISIBLE) {
                    mRecyclerView.setVisibility(View.GONE);
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.up));
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.down));
                }
            }
        }
    }

    /**
     * 颜色类型
     */
    private class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout mTopLayout;
        TextView mNameTextView;
        ImageView mImageView;
        RecyclerView mRecyclerView;

        private ColorViewHolder(View itemView) {
            super(itemView);

            mTopLayout = itemView.findViewById(R.id.topLayout);
            mNameTextView = itemView.findViewById(R.id.nameTextView);
            mImageView = itemView.findViewById(R.id.imageView);
            mRecyclerView = itemView.findViewById(R.id.recyclerView);

            mTopLayout.setOnClickListener(this);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.topLayout || id == R.id.imageView) {
                if (mRecyclerView.getVisibility() == View.VISIBLE) {
                    mRecyclerView.setVisibility(View.GONE);
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.up));
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.down));
                }
            }
        }
    }

    /**
     * 范围类型
     */
    private class ScopeViewHolder extends RecyclerView.ViewHolder implements OnRangeChangedListener, View.OnClickListener {

        RelativeLayout mTopLayout;
        TextView mNameTextView;
        ImageView mImageView;
        RangeSeekBar mRangeSeekBar;

        private ScopeViewHolder(View itemView) {
            super(itemView);

            mTopLayout = itemView.findViewById(R.id.topLayout);
            mNameTextView = itemView.findViewById(R.id.nameTextView);
            mImageView = itemView.findViewById(R.id.imageView);
            mRangeSeekBar = itemView.findViewById(R.id.rangeSeekBar);

            mTopLayout.setOnClickListener(this);
            mImageView.setOnClickListener(this);
            mRangeSeekBar.setOnRangeChangedListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.topLayout || id == R.id.imageView) {
                if (mRangeSeekBar.getVisibility() == View.VISIBLE) {
                    mRangeSeekBar.setVisibility(View.GONE);
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.up));
                } else {
                    mRangeSeekBar.setVisibility(View.VISIBLE);
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.down));
                }
            }
        }

        @Override
        public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

        }

        @Override
        public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

        }

        @Override
        public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
            int position = getLayoutPosition();
            ParamBean paramBean = mParamList.get(position);
            paramBean.setLeftValue(leftValue);
            paramBean.setRightValue(rightValue);
            // 4.在点击事件中调用接口里的方法
            if (null != onRangeSeekBarChangedListener) {
                onRangeSeekBarChangedListener.onRangeChanged(view, leftValue, rightValue, isFromUser, position);
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
         * 在RecyclerView Item 点击时回调
         *
         * @param parentPosition 被点击的父Item位置
         * @param childPosition  被点击的子Item位置
         */
        void onItemClick(int parentPosition, int childPosition);
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
    public interface OnRangeSeekBarChangedListener {

        /**
         * 在RangeSeekBar值发生变化时回调
         *
         * @param view       值发生变化的RangeSeekBar
         * @param leftValue  RangeSeekBar左边的值
         * @param rightValue RangeSeekBar右边的值
         * @param isFromUser 是否来自于用户主动触发的
         * @param position   值发生变化的RangeSeekBar在列表中的位置
         */
        void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser, int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnRangeSeekBarChangedListener onRangeSeekBarChangedListener;

    /**
     * 3.初始化接口变量
     *
     * @param onRangeSeekBarChangedListener 侦听RangeSeekBar值发生变化的侦听器
     */
    public void setOnRangeSeekBarChangedListener(OnRangeSeekBarChangedListener onRangeSeekBarChangedListener) {
        this.onRangeSeekBarChangedListener = onRangeSeekBarChangedListener;
    }
}
