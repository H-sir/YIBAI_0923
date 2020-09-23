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
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.sceneedit.SceneEditFragment;

import java.io.File;
import java.util.List;

import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 场景编辑界面显示以摆放列表的适配器
 *
 * @author sjl
 * {@link SceneEditFragment}
 */
public class AlreadyPlacedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 已摆放"模拟搭配图片"数据
     * 与 mSimulationDataList 的区别在于剔除了"finallySkuId"相同的数据
     */
    private List<SimulationData> mAlreadyPlacedList;

    public AlreadyPlacedAdapter(Context context, List<SimulationData> alreadyPlacedList) {
        this.mContext = context;
        this.mAlreadyPlacedList = alreadyPlacedList;
    }

    @Override
    public int getItemCount() {
        return mAlreadyPlacedList == null ? 0 : mAlreadyPlacedList.size();
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

        SimulationData simulationData = mAlreadyPlacedList.get(position);
        String picturePath = simulationData.getPicturePath();
        String productName = simulationData.getProductName();
        String augmentedProductName = simulationData.getAugmentedProductName();
        int number = simulationData.getNumber();

        if (judeFileExists(picturePath)) {
            myViewHolder.mIconImageView.setBackground(null);
            ImageUtil.displayImage(mContext, myViewHolder.mIconImageView, new File(picturePath).toURI().toString());
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

        // number + 1 是因为int默认初始化为0,这里只有一个产品时数量也时0,所以为了显示真实的数量就必须+1了
        String numberString = "x" + (number + 1);
        myViewHolder.mNumberTextView.setText(numberString);

        if (position == mAlreadyPlacedList.size() - 1) {
            myViewHolder.mDividerView.setVisibility(View.INVISIBLE);
        } else {
            myViewHolder.mDividerView.setVisibility(View.VISIBLE);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

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
        }
    }
}
