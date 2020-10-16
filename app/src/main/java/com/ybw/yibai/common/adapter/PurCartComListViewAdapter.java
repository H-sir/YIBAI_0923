package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.bean.PurCartChildBean;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public class PurCartComListViewAdapter extends BaseExpandableListAdapter {

    public static final String TAG = "PurCartComListViewAdapter";

    private List<PurCartBean.DataBean.ComlistBean> PurCartComBeanHead = new ArrayList<>();
    private List<List<PurCartChildBean>> PurCartComBeanChild = new ArrayList<>();
    private Context context;

    public PurCartComListViewAdapter(Context context, List<PurCartBean.DataBean.ComlistBean> PurCartComBeanHead, List<List<PurCartChildBean>> PurCartComBeanChild) {
        this.context = context;
        //初始化组、子列表项
        this.PurCartComBeanHead.addAll(PurCartComBeanHead);
        this.PurCartComBeanChild.addAll(PurCartComBeanChild);
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
        PurCartBean.DataBean.ComlistBean purCartComBean = PurCartComBeanHead.get(groupPosition);
        PurCartBean.DataBean.ComlistBean.FirstBean first = purCartComBean.getFirst();
        PurCartBean.DataBean.ComlistBean.SecondBean second = purCartComBean.getSecond();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_purcart_com_list_item_layout, null);
        }
        ImageView purcartComSelect = convertView.findViewById(R.id.purcartComSelect);    //是否选择
        ImageView purcartComImage = convertView.findViewById(R.id.purcartComImage);      //图片
        TextView purcartComName = convertView.findViewById(R.id.purcartComName);        //名称
        TextView purcartComType = convertView.findViewById(R.id.purcartComType);        //类型
        TextView purcartComPrice = convertView.findViewById(R.id.purcartComPrice);      //价格
        TextView purcartComSubtract = convertView.findViewById(R.id.purcartComSubtract);//数量减少
        TextView purcartComNum = convertView.findViewById(R.id.purcartComNum);          //数量
        TextView purcartComAdd = convertView.findViewById(R.id.purcartComAdd);          //数量增加
        TextView titleName = (TextView) convertView.findViewById(R.id.titleName);
        TextView titleTime = (TextView) convertView.findViewById(R.id.titleTime);
        ImageView titlePhoto = (ImageView) convertView.findViewById(R.id.titlePhoto);

//        if (purCartComBean.getChecked() == 1) {
//            purcartComSelect.setImageResource(R.mipmap.selected_img);
//        } else {
//            purcartComSelect.setImageResource(R.mipmap.purcart_no_select);
//        }
//        if (purCartComBean.getPic() != null)
//            ImageUtil.displayImage(context, purcartComImage, purCartComBean.getPic());
//
//        purcartComName.setText(first.getName());
//        purcartComPrice.setText(first.getPrice());
//        if (second != null) {
//            purcartComType.setText(second.getName());
//            float allPrice = Float.parseFloat(first.getPrice()) + Float.parseFloat(second.getFprice());
//            purcartComPrice.setText(String.valueOf(allPrice));
//        }
//        purcartComNum.setText(String.valueOf(purCartComBean.getNum()));
//
//        titleName.setText("");
//        titleTime.setText("");

//        purcartComSubtract.setOnClickListener(view -> {
//            if (onComSubtractClickListener != null)
//                onComSubtractClickListener.onComSubtractNum(groupPosition, purcartComNum);
//        });
//        purcartComAdd.setOnClickListener(view -> {
//            if (onComAddClickListener != null)
//                onComAddClickListener.onComAddNum(groupPosition, purcartComNum);
//        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //获取文本
        PurCartChildBean purCartChildBean = PurCartComBeanChild.get(groupPosition).get(childPosition);
        //子列表控件通过界面文件设计
        if (convertView == null) {//convert在运行中会重用，如果不为空，则表明不用重新获取
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_purcart_com_list_item_title_item_layout, null);
        }
        LinearLayout titleItemView = (LinearLayout) convertView.findViewById(R.id.titleItemView);
        TextView purcartComName = (TextView) convertView.findViewById(R.id.purcartComName);
        TextView purcartComPrice = (TextView) convertView.findViewById(R.id.purcartComPrice);
        TextView purcartComGateName = (TextView) convertView.findViewById(R.id.purcartComGateName);
        TextView purcartComTime = (TextView) convertView.findViewById(R.id.purcartComTime);

        purcartComName.setText(purCartChildBean.getName());
        purcartComPrice.setText(purCartChildBean.getPrice());
        purcartComGateName.setText(purCartChildBean.getGateName());
        purcartComTime.setText(purCartChildBean.getGateProductId());

        //获取文本控件，并设置值
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public interface OnComAddClickListener {

        /**
         * 点击+的回调
         *
         * @param position 被点击的Item位置
         */
        void onComAddNum(int position, TextView purcartComNum);
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
        void onComSubtractNum(int position, TextView purcartComNum);
    }

    private OnComSubtractClickListener onComSubtractClickListener;

    public void setOnComSubtractClickListener(OnComSubtractClickListener onComSubtractClickListener) {
        this.onComSubtractClickListener = onComSubtractClickListener;
    }

}