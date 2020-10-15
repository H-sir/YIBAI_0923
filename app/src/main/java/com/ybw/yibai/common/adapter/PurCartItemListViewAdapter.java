package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.DesignList;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.bean.PurCartComBean;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public class PurCartItemListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "PurCartComListViewAdapter";

    /**
     * 上下文对象
     */
    private Context mContext;

    private List<PurCartBean.DataBean.ItemlistBean> itemlistBeans;

    /**
     * 正在编辑的场景
     */

    public PurCartItemListViewAdapter(Context context, List<PurCartBean.DataBean.ItemlistBean> itemlistBeans) {
        this.mContext = context;
        this.itemlistBeans = itemlistBeans;
    }

    @Override
    public int getItemCount() {
        return itemlistBeans == null ? 0 : itemlistBeans.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.listview_purcart_item_list_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        PurCartBean.DataBean.ItemlistBean itemlistBean = itemlistBeans.get(position);
        PurCartBean.DataBean.ItemlistBean.FirstBeanX first = itemlistBean.getFirst();

        if (itemlistBean.getChecked() == 1) {
            myViewHolder.purcartComSelect.setImageResource(R.mipmap.selected_img);
        } else {
            myViewHolder.purcartComSelect.setImageResource(R.mipmap.purcart_no_select);
        }
        if (itemlistBean.getPic() != null)
            ImageUtil.displayImage(mContext, myViewHolder.purcartComImage, itemlistBean.getPic());

        myViewHolder.purcartComName.setText(first.getName());
        myViewHolder.purcartComPrice.setText(first.getPrice());
        myViewHolder.purcartComType.setText("");
        myViewHolder.purcartComNum.setText(String.valueOf(itemlistBean.getNum()));
        myViewHolder.titleName.setText(first.getGateName());
        myViewHolder.titleTime.setText("");

    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView purcartComSelect, purcartComImage;
        TextView purcartComName, purcartComType, purcartComPrice;
        TextView purcartComSubtract, purcartComNum, purcartComAdd;
        TextView titleName, titleTime;

        private MyViewHolder(View itemView) {
            super(itemView);
            purcartComSelect = itemView.findViewById(R.id.purcartComSelect);    //是否选择
            purcartComImage = itemView.findViewById(R.id.purcartComImage);      //图片
            purcartComName = itemView.findViewById(R.id.purcartComName);        //名称
            purcartComType = itemView.findViewById(R.id.purcartComType);        //类型
            purcartComPrice = itemView.findViewById(R.id.purcartComPrice);      //价格
            purcartComSubtract = itemView.findViewById(R.id.purcartComSubtract);//数量减少
            purcartComNum = itemView.findViewById(R.id.purcartComNum);          //数量
            purcartComAdd = itemView.findViewById(R.id.purcartComAdd);          //数量增加
            titleName = itemView.findViewById(R.id.titleName);          //
            titleTime = itemView.findViewById(R.id.titleTime);          //

            purcartComSubtract.setOnClickListener(this);
            purcartComAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.purcartComAdd:
                    if (onItemAddClickListener != null)
                        onItemAddClickListener.onItemAddNum(getLayoutPosition());
                    break;
                case R.id.purcartComSubtract:
                    if (onItemSubtractClickListener != null)
                        onItemSubtractClickListener.onItemSubtractNum(getLayoutPosition());
                    break;
            }
        }
    }

    public interface OnItemAddClickListener {

        /**
         * 点击+的回调
         *
         * @param position 被点击的Item位置
         */
        void onItemAddNum(int position);
    }

    private OnItemAddClickListener onItemAddClickListener;

    public void setOnItemAddClickListener(OnItemAddClickListener onItemAddClickListener) {
        this.onItemAddClickListener = onItemAddClickListener;
    }

    public interface OnItemSubtractClickListener {

        /**
         * 点击-的回调
         *
         * @param position 被点击的Item位置
         */
        void onItemSubtractNum(int position);
    }

    private OnItemSubtractClickListener onItemSubtractClickListener;

    public void setOnItemSubtractClickListener(OnItemSubtractClickListener onItemSubtractClickListener) {
        this.onItemSubtractClickListener = onItemSubtractClickListener;
    }


}