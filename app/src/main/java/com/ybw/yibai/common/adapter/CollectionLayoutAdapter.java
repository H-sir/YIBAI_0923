package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.CollectionListBean;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.widget.xlist.BaseListAdapter;
import com.ybw.yibai.module.producttype.ProductTypeFragment;

import java.util.List;

/**
 * 收藏列表适配器
 *
 * @author sjl
 * {@link ProductTypeFragment}
 */
public class CollectionLayoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CollectionListBean.DataBean.ListBean> dataList;

    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;
    private Context mContext;

    public CollectionLayoutAdapter(Context context, List<CollectionListBean.DataBean.ListBean> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 通过判断显示类型，来创建不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_collection_item_layout, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            CollectionListBean.DataBean.ListBean dataBean = dataList.get(position);

            String pic = dataBean.getPic();
            String sku_id = dataBean.getSku_id();
            String name = dataBean.getName();

            //收藏ID
            String collect_id = dataBean.getCollect_id();
            // 盆器sku_id
            String pot_sku_id = dataBean.getPot_sku_id();
            //植物sku_id
            String plant_sku_id = dataBean.getPlant_sku_id();
            //植物名称
            String plant_name = dataBean.getPlant_name();
            //盆器名称
            String pot_name = dataBean.getPot_name();
            //是否编辑
            boolean isEdit = dataBean.isEdit();
            //是否组合
            boolean isCombination = dataBean.isCombination();

            if (isEdit) {
                recyclerViewHolder.mIsCollectionSelect.setVisibility(View.VISIBLE);
            } else {
                recyclerViewHolder.mIsCollectionSelect.setVisibility(View.GONE);
            }

            if (isCombination) {
                if (TextUtils.isEmpty(plant_name)) {
                    recyclerViewHolder.mTextView.setText("");
                } else {
                    recyclerViewHolder.mTextView.setText(plant_name);
                }
                if (TextUtils.isEmpty(pot_name)) {
                    recyclerViewHolder.mTextView1.setText("");
                } else {
                    recyclerViewHolder.mTextView1.setText(pot_name);
                }
                recyclerViewHolder.mTextView1.setVisibility(View.VISIBLE);
            } else {
                recyclerViewHolder.mTextView1.setVisibility(View.GONE);
                if (TextUtils.isEmpty(name)) {
                    recyclerViewHolder.mTextView.setText("");
                } else {
                    recyclerViewHolder.mTextView.setText(name);
                }
            }

            if (!TextUtils.isEmpty(pic)) {
                Picasso.with(mContext).load(pic).into(recyclerViewHolder.mImageView);
                // ImageUtil.displayImages(mContext, myViewHolder.mImageView, thumbPic1, DensityUtil.dpToPx(mContext, 2));
                recyclerViewHolder.mImageView.setBackground(null);
            }

            recyclerViewHolder.mFrameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isEdit) {
                        dataBean.setSelect(!dataBean.isSelect());
                        if (dataBean.isSelect()) {
                            recyclerViewHolder.mIsCollectionSelect.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.purcart_select));
                        } else {
                            recyclerViewHolder.mIsCollectionSelect.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.purcart_no_select));
                        }
                    }
                }
            });

        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_END: // 加载到底
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                    return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        FrameLayout mFrameLayout;
        ImageView mImageView;
        ImageView mIsCollectionSelect;
        TextView mTextView;
        TextView mTextView1;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            mFrameLayout = itemView.findViewById(R.id.rootLayout);
            mImageView = itemView.findViewById(R.id.imageView);
            mIsCollectionSelect = itemView.findViewById(R.id.is_collection_select);
            mTextView = itemView.findViewById(R.id.textView);
            mTextView1 = itemView.findViewById(R.id.textView1);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;

        FootViewHolder(View itemView) {
            super(itemView);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
        }
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }
}
