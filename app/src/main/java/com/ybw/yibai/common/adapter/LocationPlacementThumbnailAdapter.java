package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.QuotationLocation.DataBean.ListBean.QuotelistBean;
import com.ybw.yibai.common.utils.ImageUtil;

import java.util.List;

/**
 * 显示报价单,某一个位置盆栽图片置缩略图适配器
 *
 * @author sjl
 * {@link LocationPlacementAdapter}
 */
public class LocationPlacementThumbnailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 某一个具体位置摆放清单详情
     */
    private List<QuotelistBean> mQuoteList;

    public LocationPlacementThumbnailAdapter(Context context, List<QuotelistBean> quoteList) {
        this.mContext = context;
        this.mQuoteList = quoteList;
    }

    @Override
    public int getItemCount() {
        return mQuoteList == null ? 0 : mQuoteList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_location_placement_thumbnail_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        QuotelistBean quotelistBean = mQuoteList.get(position);
        String pic = quotelistBean.getPic();
        int number = quotelistBean.getNum();

        if (!TextUtils.isEmpty(pic)) {
            myViewHolder.mImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, pic);
        } else {
            myViewHolder.mImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
            ImageUtil.displayImage(mContext, myViewHolder.mImageView, " ");
        }

        String numberString = "x" + number;
        myViewHolder.mNumberTextView.setText(numberString);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * 盆栽图片
         */
        ImageView mImageView;

        /**
         * 盆栽数量
         */
        TextView mNumberTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mNumberTextView = itemView.findViewById(R.id.numberTextView);
        }
    }
}
