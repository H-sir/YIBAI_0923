package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.SystemParameter.DataBean.CatelistBean.ListBean;
import com.ybw.yibai.module.producttype.ProductTypeFragment;

import java.util.Iterator;
import java.util.List;

/**
 * 产品二级分类过滤器
 *
 * @author sjl
 * {@link ProductTypeFragment}
 */
public class ProductSecondaryClassifyFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 二级分类筛选条件列表
     */
    private List<ListBean> mClassifyList;

    public ProductSecondaryClassifyFilterAdapter(Context context, List<ListBean> classifyList) {
        this.mContext = context;
        this.mClassifyList = classifyList;
    }

    @Override
    public int getItemCount() {
        return mClassifyList == null ? 0 : mClassifyList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_product_secondary_classify_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        ListBean listBean = mClassifyList.get(position);
        String name = listBean.getName();
        boolean select = listBean.isSelect();
        if (TextUtils.isEmpty(name)) {
            myViewHolder.mTextView.setText(" ");
        } else {
            myViewHolder.mTextView.setText(name);
        }

        if (select) {
            myViewHolder.mTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
            myViewHolder.mTextView.setBackground(mContext.getDrawable(R.drawable.background_button_unpressed));
        } else {
            myViewHolder.mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.prompt_low_text_color));
            myViewHolder.mTextView.setBackground(mContext.getDrawable(R.drawable.background_image_view));
        }

        myViewHolder.mTextView.setOnClickListener(v -> {
            if(listBean.isSelect()){
                listBean.setSelect(false);
            }else {
                for (Iterator<ListBean> iterator = mClassifyList.iterator(); iterator.hasNext(); ) {
                    ListBean bean = iterator.next();
                    bean.setSelect(false);
                }
                listBean.setSelect(true);
            }
//            // 如果之前选中,那现在设置没有选中
//            // 如果之前没有选中,那现在设置选中
//            listBean.setSelect(!select);
            notifyDataSetChanged();
            if (null == onItemClickListener) {
                return;
            }
            onItemClickListener.onClassifyFilterItemClick(position);
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.textView);
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
         * 在RecyclerView Item 长按时回调
         *
         * @param position 被点击的Item位置
         */
        void onClassifyFilterItemClick(int position);
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
