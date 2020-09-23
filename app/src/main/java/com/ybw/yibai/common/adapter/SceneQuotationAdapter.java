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
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.scene.SceneActivity;

import java.io.File;
import java.util.List;

import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 场景界面显示报价列表的适配器
 *
 * @author sjl
 * {@link SceneActivity}
 */
public class SceneQuotationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final String TAG = "SceneQuotationAdapter";

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 用户保存的"报价"数据列表
     */
    private List<QuotationData> mQuotationDataList;

    public SceneQuotationAdapter(Context context, List<QuotationData> quotationDataList) {
        this.mContext = context;
        this.mQuotationDataList = quotationDataList;
    }

    @Override
    public int getItemCount() {
        return mQuotationDataList == null ? 0 : mQuotationDataList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_scene_quotation_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        QuotationData quotationData = mQuotationDataList.get(position);
        String productName = quotationData.getProductName();
        String productPic1 = quotationData.getProductPic1();
        String productPic2 = quotationData.getProductPic2();
        String productPic3 = quotationData.getProductPic3();

        String augmentedProductName = quotationData.getAugmentedProductName();

        // 数量
        int number = quotationData.getNumber();
        // 组合图片在本地保存的路径
        String picturePath = quotationData.getPicturePath();

        if (judeFileExists(picturePath)) {
            myViewHolder.mIconImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, new File(picturePath).toURI().toString());
        } else if (!TextUtils.isEmpty(productPic1)) {
            myViewHolder.mIconImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, productPic1);
        } else if (!TextUtils.isEmpty(productPic2)) {
            myViewHolder.mIconImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, productPic2);
        } else if (!TextUtils.isEmpty(productPic3)) {
            myViewHolder.mIconImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, productPic3);
        } else {
            myViewHolder.mIconImageView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, " ");
        }

        if (TextUtils.isEmpty(productName)) {
            myViewHolder.mProductName.setText(" ");
        } else {
            myViewHolder.mProductName.setText(productName);
        }

        if (TextUtils.isEmpty(augmentedProductName)) {
            myViewHolder.mAugmentedProductName.setText(" ");
        } else {
            myViewHolder.mAugmentedProductName.setText(augmentedProductName);
        }

        String numberString = "x" + number;
        myViewHolder.mNumberTextView.setText(numberString);

        if (position == mQuotationDataList.size() - 1) {
            myViewHolder.mDividerView.setVisibility(View.INVISIBLE);
        } else {
            myViewHolder.mDividerView.setVisibility(View.VISIBLE);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mIconImageView;

        TextView mProductName;

        TextView mAugmentedProductName;

        TextView mNumberTextView;

        /**
         * 分隔线
         */
        View mDividerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mIconImageView = itemView.findViewById(R.id.iconImageView);
            mProductName = itemView.findViewById(R.id.productNameTextView);
            mAugmentedProductName = itemView.findViewById(R.id.augmentedProductName);
            mNumberTextView = itemView.findViewById(R.id.numberTextView);
            mDividerView = itemView.findViewById(R.id.dividerView);

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
