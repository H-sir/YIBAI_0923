package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.DesignList;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.bean.PurCartComBean;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.common.widget.horizontal.adatper.AbsAdapterItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public class PurCartComListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "PurCartComListViewAdapter";

    /**
     * 上下文对象
     */
    private Context mContext;

    private List<PurCartBean.DataBean.ComlistBean> comlistBeans;

    /**
     * 正在编辑的场景
     */

    public PurCartComListViewAdapter(Context context, List<PurCartBean.DataBean.ComlistBean> comlistBeans) {
        this.mContext = context;
        this.comlistBeans = comlistBeans;
    }

    @Override
    public int getItemCount() {
        return comlistBeans == null ? 0 : comlistBeans.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.listview_purcart_com_list_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        PurCartBean.DataBean.ComlistBean comlistBean = comlistBeans.get(position);
        PurCartBean.DataBean.ComlistBean.FirstBean first = comlistBean.getFirst();
        PurCartBean.DataBean.ComlistBean.SecondBean second = comlistBean.getSecond();

        if (comlistBean.getChecked() == 1) {
            myViewHolder.purcartComSelect.setImageResource(R.mipmap.selected_img);
        } else {
            myViewHolder.purcartComSelect.setImageResource(R.mipmap.purcart_no_select);
        }
        if (comlistBean.getPic() != null)
            ImageUtil.displayImage(mContext, myViewHolder.purcartComImage, comlistBean.getPic());

        myViewHolder.purcartComName.setText(first.getName());
        myViewHolder.purcartComPrice.setText(first.getPrice());
        if (second != null) {
            myViewHolder.purcartComType.setText(second.getName());
            float allPrice = Float.parseFloat(first.getPrice()) + Float.parseFloat(second.getFprice());
            myViewHolder.purcartComPrice.setText(String.valueOf(allPrice));
        }
        myViewHolder.purcartComNum.setText(String.valueOf(comlistBean.getNum()));

        if (second != null && first != null) {
            addGroup(myViewHolder.PurCartComBeanHead, myViewHolder.PurCartComBeanChild, "分开供应", 0);
            addChild(myViewHolder.PurCartComBeanChild, 0, first);
            addChild(myViewHolder.PurCartComBeanChild, 0, second);
        }else {
            addGroup(myViewHolder.PurCartComBeanHead, myViewHolder.PurCartComBeanChild, "供应商 : " + first.getGateName(), 0);
            addChild(myViewHolder.PurCartComBeanChild, 0, first);
        }

        myViewHolder.inventoryEpcSyfcAdapter = new PurCartComBeanAdapter(mContext, myViewHolder.PurCartComBeanHead, myViewHolder.PurCartComBeanChild);
        myViewHolder.purcartComList.setAdapter(myViewHolder.inventoryEpcSyfcAdapter); //设置数据适配器
        myViewHolder.purcartComList.setCacheColorHint(0);
    }


    //添加组列表项
    public void addGroup(List<PurCartComBean> mPurCartComBeanHead, List<List<PurCartComBean>> mPurCartComBeanChild, String name, int time) {
        PurCartComBean purCartComBean = new PurCartComBean();
        purCartComBean.setName(name);
        purCartComBean.setSkuId(time);
        mPurCartComBeanHead.add(purCartComBean);
        mPurCartComBeanChild.add(new ArrayList<>()); //child中添加新数组
    }

    //添加对应组的自列表项
    public void addChild(List<List<PurCartComBean>> mPurCartComBeanChild, int position, PurCartBean.DataBean.ComlistBean.FirstBean first) {
        List<PurCartComBean> purCartComBeans = mPurCartComBeanChild.get(position);
        if (purCartComBeans != null) {
            PurCartComBean purCartComBean = new PurCartComBean();
            purCartComBean.setSkuId(first.getSkuId());
            purCartComBean.setName(first.getName());
            purCartComBean.setPrice(first.getPrice());
            purCartComBean.setGateId(first.getGateId());
            purCartComBean.setGateName(first.getGateName());
            purCartComBean.setGateProductId(first.getGateProductId());
            purCartComBeans.add(purCartComBean);
        } else {
            purCartComBeans = new ArrayList<>();
            PurCartComBean purCartComBean = new PurCartComBean();
            purCartComBean.setSkuId(first.getSkuId());
            purCartComBean.setName(first.getName());
            purCartComBean.setPrice(first.getPrice());
            purCartComBean.setGateId(first.getGateId());
            purCartComBean.setGateName(first.getGateName());
            purCartComBean.setGateProductId(first.getGateProductId());
            purCartComBeans.add(purCartComBean);
        }
    }

    //添加对应组的自列表项
    public void addChild(List<List<PurCartComBean>> mPurCartComBeanChild, int position, PurCartBean.DataBean.ComlistBean.SecondBean second) {
        List<PurCartComBean> purCartComBeans = mPurCartComBeanChild.get(position);
        if (purCartComBeans != null) {
            PurCartComBean purCartComBean = new PurCartComBean();
            purCartComBean.setSkuId(second.getSkuId());
            purCartComBean.setName(second.getName());
            purCartComBean.setPrice(second.getFprice());
            purCartComBean.setGateId(second.getGateId());
            purCartComBean.setGateName(second.getGateName());
            purCartComBean.setGateProductId(second.getGateProductId());
            purCartComBeans.add(purCartComBean);
        } else {
            purCartComBeans = new ArrayList<>();
            PurCartComBean purCartComBean = new PurCartComBean();
            purCartComBean.setSkuId(second.getSkuId());
            purCartComBean.setName(second.getName());
            purCartComBean.setPrice(second.getFprice());
            purCartComBean.setGateId(second.getGateId());
            purCartComBean.setGateName(second.getGateName());
            purCartComBean.setGateProductId(second.getGateProductId());
            purCartComBeans.add(purCartComBean);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView purcartComSelect, purcartComImage;
        TextView purcartComName, purcartComType, purcartComPrice;
        TextView purcartComSubtract, purcartComNum, purcartComAdd;
        ExpandableListView purcartComList;

        List<PurCartComBean> PurCartComBeanHead = new ArrayList<>();                    //组列表项，每个组都有一个子List
        List<List<PurCartComBean>> PurCartComBeanChild = new ArrayList<>();                   //子列表项
        PurCartComBeanAdapter inventoryEpcSyfcAdapter;

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
            purcartComList = itemView.findViewById(R.id.purcartComList);        //可伸展收缩列表

            purcartComSubtract.setOnClickListener(this);
            purcartComAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.purcartComAdd:
                    if (onComAddClickListener != null)
                        onComAddClickListener.onComAddNum(getLayoutPosition(),purcartComNum);
                    break;
                case R.id.purcartComSubtract:
                    if (onComSubtractClickListener != null)
                        onComSubtractClickListener.onComSubtractNum(getLayoutPosition(),purcartComNum);
                    break;
            }
        }
    }

    public interface OnComAddClickListener {

        /**
         * 点击+的回调
         *
         * @param position 被点击的Item位置
         */
        void onComAddNum(int position,TextView purcartComNum);
    }

    private OnComAddClickListener onComAddClickListener;

    public void setOnComAddClickListener(OnComAddClickListener onComAddClickListener) {
        this.onComAddClickListener = onComAddClickListener;
    }

    public interface OnComSubtractClickListener {

        /**
         * 点击-的回调
         *
         * @param position 被点击的Item位置
         */
        void onComSubtractNum(int position,TextView purcartComNum);
    }

    private OnComSubtractClickListener onComSubtractClickListener;

    public void setOnComSubtractClickListener(OnComSubtractClickListener onComSubtractClickListener) {
        this.onComSubtractClickListener = onComSubtractClickListener;
    }

    public class PurCartComBeanAdapter extends BaseExpandableListAdapter {

        private List<PurCartComBean> PurCartComBeanHead;
        private List<List<PurCartComBean>> PurCartComBeanChild;
        private Context context;

        public PurCartComBeanAdapter(Context context, List<PurCartComBean> PurCartComBeanHead, List<List<PurCartComBean>> PurCartComBeanChild) {
            this.context = context;
            //初始化组、子列表项
            this.PurCartComBeanHead = PurCartComBeanHead;
            this.PurCartComBeanChild = PurCartComBeanChild;
        }


        @Override
        public int getGroupCount() {
            return this.PurCartComBeanHead.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (groupPosition < 0 || groupPosition >= this.PurCartComBeanChild.size())
                return 0;
            return PurCartComBeanChild.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return PurCartComBeanHead.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return PurCartComBeanChild.get(childPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            PurCartComBean purCartComBean = PurCartComBeanHead.get(groupPosition);
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_purcart_com_list_item_title_layout, null);
            }
            TextView titleName = (TextView) convertView.findViewById(R.id.titleName);
            TextView titleTime = (TextView) convertView.findViewById(R.id.titleTime);
            ImageView titlePhoto = (ImageView) convertView.findViewById(R.id.titlePhoto);

            titleName.setText(purCartComBean.getGateName());
            titleTime.setText(purCartComBean.getGateName());

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            //获取文本
            PurCartComBean purCartComBean = PurCartComBeanChild.get(groupPosition).get(childPosition);
            //子列表控件通过界面文件设计
            if (convertView == null) {//convert在运行中会重用，如果不为空，则表明不用重新获取
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_purcart_com_list_item_title_item_layout, null);
            }
            LinearLayout titleItemView = (LinearLayout) convertView.findViewById(R.id.titleItemView);
            TextView purcartComName = (TextView) convertView.findViewById(R.id.purcartComName);
            TextView purcartComPrice = (TextView) convertView.findViewById(R.id.purcartComPrice);
            TextView purcartComGateName = (TextView) convertView.findViewById(R.id.purcartComGateName);
            TextView purcartComTime = (TextView) convertView.findViewById(R.id.purcartComTime);

            purcartComName.setText(purCartComBean.getName());
            purcartComPrice.setText(purCartComBean.getPrice());
            purcartComGateName.setText(purCartComBean.getGateName());
            purcartComTime.setText(purCartComBean.getGateProductId());

            titleItemView.setOnClickListener(view -> {

            });
            //获取文本控件，并设置值
            return convertView;
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }
}