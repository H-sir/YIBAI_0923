package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.QuotationLocation.DataBean.ListBean;
import com.ybw.yibai.common.bean.QuotationLocation.DataBean.ListBean.QuotelistBean;
import com.ybw.yibai.common.classs.GridSpacingItemDecorations;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.quotation.QuotationFragment;

import java.util.List;

/**
 * 显示报价单,盆栽摆放位置明细列表的适配器
 *
 * @author sjl
 * {@link QuotationFragment}
 */
public class LocationPlacementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 位置摆放清单数据
     */
    private List<ListBean> mLocationPlacementList;

    public LocationPlacementAdapter(Context context, List<ListBean> locationPlacementList) {
        this.mContext = context;
        this.mLocationPlacementList = locationPlacementList;
    }

    @Override
    public int getItemCount() {
        return mLocationPlacementList == null ? 0 : mLocationPlacementList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_location_placement_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean listBean = mLocationPlacementList.get(position);
        boolean select = listBean.isSelect();
        String designPic = listBean.getDesign_pic();
        String placeName = listBean.getQuote_place_name();
        List<QuotelistBean> quoteList = listBean.getQuotelist();
        int sumNumber = 0;
        for (QuotelistBean quotelistBean : quoteList) {
            int num = quotelistBean.getNum();
            sumNumber += num;
        }

        if (select) {
            myViewHolder.mRootLayout.setBackground(mContext.getDrawable(R.drawable.background_location_selected));
            myViewHolder.mDesignDrawingImageView.setOnClickListener(v -> {
                if (null == onDesignDrawingClickListener) {
                    return;
                }
                onDesignDrawingClickListener.onDesignDrawingClick(position);
            });
            myViewHolder.mDeleteTextView.setOnClickListener(v -> {
                if (null == onDeleteClickListener) {
                    return;
                }
                onDeleteClickListener.onDeleteClick(position);
            });
            myViewHolder.mRenameTextView.setOnClickListener(v -> {
                if (null == onRenameClickListener) {
                    return;
                }
                onRenameClickListener.onRenameClick(position);
            });
            myViewHolder.mDeleteTextView.setVisibility(View.VISIBLE);
            myViewHolder.mRenameTextView.setVisibility(View.VISIBLE);
            myViewHolder.mSumTextView.setVisibility(View.GONE);
        } else {
            myViewHolder.mRootLayout.setBackground(mContext.getDrawable(R.drawable.background_wait_dialog));
            myViewHolder.mDesignDrawingImageView.setClickable(false);
            myViewHolder.mDeleteTextView.setVisibility(View.GONE);
            myViewHolder.mRenameTextView.setVisibility(View.GONE);
            myViewHolder.mSumTextView.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(designPic)) {
            ImageUtil.displayImage(mContext, myViewHolder.mDesignDrawingImageView, designPic);
        } else {
            myViewHolder.mDesignDrawingImageView.setImageResource(R.mipmap.select_design_picture);
        }

        if (TextUtils.isEmpty(placeName)) {
            myViewHolder.mLocationTextView.setText(" ");
        } else {
            myViewHolder.mLocationTextView.setText(placeName);
        }

        if (position == mLocationPlacementList.size() - 1) {
            myViewHolder.mDividerView.setVisibility(View.INVISIBLE);
        } else {
            myViewHolder.mDividerView.setVisibility(View.VISIBLE);
        }

        String sumString = mContext.getResources().getString(R.string.general) + sumNumber + mContext.getResources().getString(R.string.pot_);
        myViewHolder.mSumTextView.setText(sumString);

        myViewHolder.mShrinkImageView.setOnClickListener(v -> {
            int visibility = myViewHolder.mDetailRecyclerView.getVisibility();
            if (visibility == View.VISIBLE) {
                myViewHolder.mView.setVisibility(View.GONE);
                myViewHolder.mDetailRecyclerView.setVisibility(View.GONE);
                myViewHolder.mShrinkImageView.setImageResource(R.mipmap.down_circular);
            } else {
                myViewHolder.mView.setVisibility(View.VISIBLE);
                myViewHolder.mDetailRecyclerView.setVisibility(View.VISIBLE);
                myViewHolder.mShrinkImageView.setImageResource(R.mipmap.up_circular);
            }
        });

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager verticalManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // 布局横向
        verticalManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        myViewHolder.mThumbnailRecyclerView.setLayoutManager(verticalManager);
        // 设置RecyclerView间距,每一行Item的数目1000(10000为假设的数据)
        int gap = DensityUtil.dpToPx(mContext, 11);
        GridSpacingItemDecorations decoration = new GridSpacingItemDecorations(10000, gap, 0, false);
        // 判断是否已经添加了ItemDecorations,如果存在就不添加(解决RecyclerView刷新ItemDecorations间距不断增加问题)
        if (myViewHolder.mThumbnailRecyclerView.getItemDecorationCount() == 0) {
            myViewHolder.mThumbnailRecyclerView.addItemDecoration(decoration);
        }
        LocationPlacementThumbnailAdapter locationPlacementThumbnailAdapter = new LocationPlacementThumbnailAdapter(mContext, quoteList);
        myViewHolder.mThumbnailRecyclerView.setAdapter(locationPlacementThumbnailAdapter);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // 给RecyclerView设置布局管理器(必须设置)
        myViewHolder.mDetailRecyclerView.setLayoutManager(manager);
        LocationPlacementDetailsAdapter locationPlacementDetailsAdapter = new LocationPlacementDetailsAdapter(mContext, quoteList);
        myViewHolder.mDetailRecyclerView.setAdapter(locationPlacementDetailsAdapter);
        locationPlacementDetailsAdapter.setOnItemLongClickListener(childPosition -> {
            if (null == mOnLocationPlacementDetailsItemLongClickListener) {
                return;
            }
            mOnLocationPlacementDetailsItemLongClickListener.onLocationPlacementDetailsItemLongClick(position, childPosition);
        });
        locationPlacementDetailsAdapter.setOnNumberChangeListener((childPosition, number) -> {
            if (null == mOnNumberChangeListener) {
                return;
            }
            mOnNumberChangeListener.onNumberChange(position, childPosition, number);
        });
        locationPlacementDetailsAdapter.setOnOrderEditTextClickListener(childPosition -> {
            if (null == onOrderEditTextClickListener) {
                return;
            }
            onOrderEditTextClickListener.onOrderEditTextClick(position, childPosition);
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout mRootLayout;

        /**
         * 设计图
         */
        ImageView mDesignDrawingImageView;

        /**
         * 位置名称
         */
        TextView mLocationTextView;

        /**
         * 删除
         */
        TextView mDeleteTextView;

        /**
         * 改名
         */
        TextView mRenameTextView;

        /**
         * 盆栽总数
         */
        TextView mSumTextView;

        /**
         * 展开折叠
         */
        ImageView mShrinkImageView;

        /**
         * 产品缩略图
         */
        RecyclerView mThumbnailRecyclerView;

        /**
         *
         */
        View mView;

        /**
         * 位置报价详情
         */
        RecyclerView mDetailRecyclerView;

        /**
         * 分隔线
         */
        View mDividerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mRootLayout = itemView.findViewById(R.id.rootLayout);
            mDesignDrawingImageView = itemView.findViewById(R.id.designDrawingImageView);
            mLocationTextView = itemView.findViewById(R.id.locationTextView);
            mDeleteTextView = itemView.findViewById(R.id.deleteTextView);
            mRenameTextView = itemView.findViewById(R.id.renameTextView);
            mSumTextView = itemView.findViewById(R.id.sumTextView);
            mShrinkImageView = itemView.findViewById(R.id.shrinkImageView);
            mThumbnailRecyclerView = itemView.findViewById(R.id.thumbnailRecyclerView);
            mView = itemView.findViewById(R.id.view);
            mDetailRecyclerView = itemView.findViewById(R.id.detailRecyclerView);
            mDividerView = itemView.findViewById(R.id.dividerView);

            // 屏蔽item中嵌套的RecycleView的点击事件
            mThumbnailRecyclerView.setOnTouchListener((v, event) -> itemView.onTouchEvent(event));
            // 屏蔽item中嵌套的RecycleView的点击事件
            mDetailRecyclerView.setOnTouchListener((v, event) -> itemView.onTouchEvent(event));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null == onItemClickListener) {
                return;
            }
            onItemClickListener.onItemClick(getLayoutPosition());
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
     * 1.定义一个接口
     */
    public interface OnDesignDrawingClickListener {

        /**
         * 在RecyclerView Item "设计图" 点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onDesignDrawingClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnDesignDrawingClickListener onDesignDrawingClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onDesignDrawingClickListener RecyclerView Item "设计图" 点击的侦听器
     */
    public void setOnDesignDrawingClickListener(OnDesignDrawingClickListener onDesignDrawingClickListener) {
        this.onDesignDrawingClickListener = onDesignDrawingClickListener;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnDeleteClickListener {

        /**
         * 在RecyclerView Item "删除" 点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onDeleteClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnDeleteClickListener onDeleteClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onDeleteClickListener RecyclerView Item "删除" 点击的侦听器
     */
    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnRenameClickListener {

        /**
         * 在RecyclerView Item "改名" 点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onRenameClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnRenameClickListener onRenameClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onRenameClickListener RecyclerView Item "改名" 点击的侦听器
     */
    public void setOnRenameClickListener(OnRenameClickListener onRenameClickListener) {
        this.onRenameClickListener = onRenameClickListener;
    }

    /*----------*/

    /**
     * 自定义接口,用于回调OrderEditText数量发生改变事件
     */
    public interface OnNumberChangeListener {

        /**
         * 在OrderEditText数量发生改变时回调
         *
         * @param parentPosition 被点击的父Item位置
         * @param childPosition  被点击的子Item位置
         * @param number         数量
         */
        void onNumberChange(int parentPosition, int childPosition, int number);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnNumberChangeListener mOnNumberChangeListener;

    /**
     * 3.初始化接口变量
     *
     * @param onNumberChangeListener 在OrderEditText数量发生改变时回调侦听器
     */
    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        mOnNumberChangeListener = onNumberChangeListener;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnOrderEditTextClickListener {

        /**
         * 在OrderEditText中的EditText被点击时回调
         *
         * @param parentPosition 被点击的父Item位置
         * @param childPosition  被点击的子Item位置
         */
        void onOrderEditTextClick(int parentPosition, int childPosition);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnOrderEditTextClickListener onOrderEditTextClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onOrderEditTextClickListener 侦听OrderEditText中的EditText被点击的侦听器
     */
    public void setOnOrderEditTextClickListener(OnOrderEditTextClickListener onOrderEditTextClickListener) {
        this.onOrderEditTextClickListener = onOrderEditTextClickListener;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnLocationPlacementDetailsItemLongClickListener {

        /**
         * 在显示报价单,某一个位置盆栽详细的RecyclerView Item 点击时回调
         *
         * @param parentPosition 被点击的父Item位置
         * @param childPosition  被点击的子Item位置
         */
        void onLocationPlacementDetailsItemLongClick(int parentPosition, int childPosition);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnLocationPlacementDetailsItemLongClickListener mOnLocationPlacementDetailsItemLongClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onLocationPlacementDetailsItemLongClickListener 在显示报价单,某一个位置盆栽详细的RecyclerView Item 点击时回调的侦听器
     */
    public void setOnLocationPlacementDetailsItemLongClickListener(OnLocationPlacementDetailsItemLongClickListener onLocationPlacementDetailsItemLongClickListener) {
        this.mOnLocationPlacementDetailsItemLongClickListener = onLocationPlacementDetailsItemLongClickListener;
    }
}
