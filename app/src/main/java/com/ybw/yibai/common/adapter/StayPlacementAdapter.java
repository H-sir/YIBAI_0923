package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.PlacementQrQuotationList.DataBean.ListBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.quotation.QuotationFragment;

import java.util.List;

/**
 * 待摆放清单列表适配器
 * {@link QuotationFragment}
 *
 * @author sjl
 */
public class StayPlacementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "StayPlacementAdapter";

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 待摆放清单列表
     */
    private List<ListBean> mPlacementList;

    public StayPlacementAdapter(Context context, List<ListBean> placementList) {
        this.mContext = context;
        this.mPlacementList = placementList;
    }

    @Override
    public int getItemCount() {
        return mPlacementList == null ? 0 : mPlacementList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_stay_placement_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean listBean = mPlacementList.get(position);
        String pic = listBean.getPic();
        String firstName = listBean.getFirst_name();
        // 是否显示CheckBox
        boolean showCheckBox = listBean.isShowCheckBox();
        // 是否选中CheckBox
        boolean select = listBean.isSelectCheckBox();

        if (TextUtils.isEmpty(pic)) {
            myViewHolder.mImageView.setBackground(mContext.getDrawable(R.mipmap.add));
        } else {
            ImageUtil.displayImages(mContext, myViewHolder.mImageView, pic, DensityUtil.dpToPx(mContext, 2));
        }

        if (TextUtils.isEmpty(firstName)) {
            myViewHolder.mTextView.setText(" ");
        } else {
            myViewHolder.mTextView.setText(firstName);
        }

        if (showCheckBox) {
            myViewHolder.mCheckBox.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.mCheckBox.setVisibility(View.GONE);
        }
        if (select) {
            myViewHolder.mCheckBox.setChecked(true);
        } else {
            myViewHolder.mCheckBox.setChecked(false);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {

        private ImageView mImageView;

        private TextView mTextView;

        private CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.textView);
            mCheckBox = itemView.findViewById(R.id.checkbox);

            mImageView.setOnClickListener(this);
            mImageView.setOnLongClickListener(this);
            mCheckBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            // 判断当前是否处于编辑状态,如果不是就执行点击回调方法
            if (!mPlacementList.get(0).isShowCheckBox()) {
                if (null == onItemClickListener) {
                    return;
                }
                onItemClickListener.onStayPlacementItemClick(position);
            }
            // 编辑状态的话,是否选中CheckBox的状态取反
            ListBean listBean = mPlacementList.get(position);
            boolean selectCheckBox = listBean.isSelectCheckBox();
            listBean.setSelectCheckBox(!selectCheckBox);
            notifyDataSetChanged();
            if (null == mOnCheckBoxClickListener) {
                return;
            }
            mOnCheckBoxClickListener.onStayPlacementCheckBoxClick(position);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getLayoutPosition();
            for (ListBean listBean : mPlacementList) {
                listBean.setShowCheckBox(true);
            }
            notifyDataSetChanged();
            if (null != onItemLongClickListener) {
                onItemLongClickListener.onStayPlacementItemLongClick(position);
            }
            return false;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // 判断CheckBox是不是手动点击的
            if (!buttonView.isPressed()) {
                return;
            }
            int position = getLayoutPosition();
            mPlacementList.get(position).setSelectCheckBox(isChecked);
            if (null == mOnCheckBoxClickListener) {
                return;
            }
            mOnCheckBoxClickListener.onStayPlacementCheckBoxClick(position);
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
        void onStayPlacementItemClick(int position);
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
         * 在RecyclerView Item 长按时回调
         *
         * @param position 被长按的Item位置
         */
        void onStayPlacementItemLongClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onItemLongClickListener RecyclerView Item长按的侦听器
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /*----------*/

    /**
     * 1.自定义接口,用于回调多选状态下CheckBox点击事件
     */
    public interface OnCheckBoxClickListener {

        /**
         * 在CheckBox选择状态发生改变时回调
         *
         * @param position 位置
         */
        void onStayPlacementCheckBoxClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnCheckBoxClickListener mOnCheckBoxClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onCheckBoxClickListener 监听CheckBox选择状态
     */
    public void setOnCheckBoxClickListener(OnCheckBoxClickListener onCheckBoxClickListener) {
        mOnCheckBoxClickListener = onCheckBoxClickListener;
    }
}