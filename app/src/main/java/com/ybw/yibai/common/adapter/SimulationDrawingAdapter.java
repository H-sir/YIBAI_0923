package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.SimulationDrawingData;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.drawing.SimulationDrawingActivity;

import java.io.File;
import java.util.List;

/**
 * 已模拟图界面适配器
 *
 * @author sjl
 * @date 2019/9/24
 * {@link SimulationDrawingActivity}
 */
public class SimulationDrawingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 屏幕宽度
     */
    private int mWidth;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * "已模拟图"数据
     */
    private List<SimulationDrawingData> mSimulationDrawingDataList;

    public SimulationDrawingAdapter(Context context, List<SimulationDrawingData> simulationDrawingDataList) {
        this.mContext = context;
        this.mSimulationDrawingDataList = simulationDrawingDataList;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mWidth = (dm.widthPixels - DensityUtil.dpToPx(mContext, 8) * 5) / 3;
    }

    @Override
    public int getItemCount() {
        return mSimulationDrawingDataList == null ? 0 : mSimulationDrawingDataList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_simulation_drawing_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        SimulationDrawingData simulationDrawingData = mSimulationDrawingDataList.get(position);
        // 用户保存的"模拟图"存储在手机中的位置
        File file = simulationDrawingData.getFile();
        // 是否显示CheckBox
        boolean isShowCheckBox = simulationDrawingData.isShowCheckBox();
        // 是否选中CheckBox
        boolean isSelectCheckBox = simulationDrawingData.isSelectCheckBox();

        ImageUtil.displayImage(mContext, myViewHolder.mImageView, file, DensityUtil.dpToPx(mContext, 2));

        if (isShowCheckBox) {
            myViewHolder.mCheckBox.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.mCheckBox.setVisibility(View.INVISIBLE);
        }
        if (isSelectCheckBox) {
            myViewHolder.mCheckBox.setChecked(true);
            myViewHolder.mMaskView.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.mCheckBox.setChecked(false);
            myViewHolder.mMaskView.setVisibility(View.GONE);
        }

        /*----------*/

        myViewHolder.mRootLayout.setOnClickListener(v -> {
            // 4.在点击事件中调用接口里的方法
            if (null != onItemClickListener) {
                onItemClickListener.onItemClick(position);
            }
        });

        myViewHolder.mRootLayout.setOnLongClickListener(v -> {
            // 4.在点击事件中调用接口里的方法
            if (null != onItemLongClickListener) {
                onItemLongClickListener.onItemLongClick(position);
            }
            return false;
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRootLayout;

        private ImageView mImageView;

        private CheckBox mCheckBox;

        private RelativeLayout mMaskView;

        private MyViewHolder(View itemView) {
            super(itemView);

            mRootLayout = itemView.findViewById(R.id.simulation_drawing_item);
            mCheckBox = itemView.findViewById(R.id.checkbox);
            mImageView = itemView.findViewById(R.id.imageView);
            mMaskView = itemView.findViewById(R.id.maskView);

            // 设置宽高
            ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
            layoutParams.height = mWidth;
            layoutParams.width = mWidth;
            //mImageView.setLayoutParams(layoutParams);

            ViewGroup.LayoutParams params = mMaskView.getLayoutParams();
            params.height = mWidth;
            params.width = mWidth;
            // mMaskView.setLayoutParams(params);
        }
    }

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
    public interface OnItemLongClickListener {

        /**
         * 在RecyclerView Item 长按点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onItemLongClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onItemLongClickListener RecyclerView Item点击的侦听器
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
