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
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;

import java.util.List;

/**
 * 显示设计图列表的适配器
 * @author sjl
 * {@link QuotationListAdapter}
 */
public class QuotationDesignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 设计图列表
     */
    private List<String> mDrawingPic;

    public QuotationDesignAdapter(Context context, List<String> drawingPic) {
        this.mContext = context;
        this.mDrawingPic = drawingPic;
    }

    @Override
    public int getItemCount() {
        return mDrawingPic == null ? 0 : mDrawingPic.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_design_list_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        String design = mDrawingPic.get(position);
        if (TextUtils.isEmpty(design)) {
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, " ");
            myViewHolder.mImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
        } else {
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, design, DensityUtil.dpToPx(mContext, 2));
            myViewHolder.mImageView.setBackground(null);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
        }
    }
}
