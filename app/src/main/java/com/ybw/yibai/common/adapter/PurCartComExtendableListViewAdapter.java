package com.ybw.yibai.common.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.PurCartChildBean;
import com.ybw.yibai.common.bean.PurCartHeadBean;
import com.ybw.yibai.common.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/10/16
 *     desc   :
 * </pre>
 */
public class PurCartComExtendableListViewAdapter extends BaseExpandableListAdapter {
    private Context mcontext;
    private List<PurCartHeadBean> mPurCartComBeanGroup = new ArrayList<>();
    private List<List<PurCartChildBean>> mPurCartComBeanChild = new ArrayList<>();

    public PurCartComExtendableListViewAdapter(Activity activity, List<PurCartHeadBean> mPurCartComBeanHead, List<List<PurCartChildBean>> mPurCartComBeanChild) {
        mcontext = activity.getApplication();
        this.mPurCartComBeanGroup.addAll(mPurCartComBeanHead);
        this.mPurCartComBeanChild.addAll(mPurCartComBeanChild);
    }

    @Override
    // 获取分组的个数
    public int getGroupCount() {
        return mPurCartComBeanGroup.size();
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return mPurCartComBeanChild.get(groupPosition).size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return mPurCartComBeanGroup.get(groupPosition);
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mPurCartComBeanChild.get(groupPosition).get(childPosition);
    }

    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     */
// 获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        PurCartHeadBean purCartHeadBean = mPurCartComBeanGroup.get(groupPosition);
        switch (purCartHeadBean.getType()) {
            case 0:
                convertView = setTitle(parent, "需种植");
                break;
            case 1:
                convertView = setCom(parent, purCartHeadBean, groupPosition);
                break;
            case 2:
                convertView = setTitle(parent, "单品");
                break;
            case 3:
                convertView = setItem(parent, purCartHeadBean, groupPosition);
                break;
        }
        return convertView;
    }

    @NonNull
    private View setItem(ViewGroup parent, PurCartHeadBean purCartHeadBean, int position) {
        View convertView;
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_purcart_item_list_item_layout, parent, false);
        PurCartHeadBean.DataBean first = purCartHeadBean.getFirst();

        View itemView = convertView.findViewById(R.id.itemView);
        ImageView purcartComSelect = convertView.findViewById(R.id.purcartComSelect);    //是否选择
        ImageView purcartComImage = convertView.findViewById(R.id.purcartComImage);      //图片
        TextView purcartComName = convertView.findViewById(R.id.purcartComName);        //名称
        TextView purcartComType = convertView.findViewById(R.id.purcartComType);        //类型
        TextView purcartComPrice = convertView.findViewById(R.id.purcartComPrice);      //价格
        TextView purcartComSubtract = convertView.findViewById(R.id.purcartComSubtract);//数量减少
        TextView purcartComNum = convertView.findViewById(R.id.purcartComNum);          //数量
        TextView purcartComAdd = convertView.findViewById(R.id.purcartComAdd);          //数量增加
        TextView titleName = convertView.findViewById(R.id.titleName);          //
        TextView titleTime = convertView.findViewById(R.id.titleTime);          //
        titleTime.setVisibility(View.GONE);

        boolean isSelect;
        if (purCartHeadBean.getChecked() == 1) {
            isSelect = true;
            purcartComSelect.setImageResource(R.mipmap.selected_img);
        } else {
            isSelect = false;
            purcartComSelect.setImageResource(R.mipmap.purcart_no_select);
        }
        if (purCartHeadBean.getPic() != null)
            ImageUtil.displayImage(mcontext, purcartComImage, purCartHeadBean.getPic());

        purcartComName.setText(first.getName());
        purcartComPrice.setText(first.getPrice());
        purcartComType.setText("");
        purcartComNum.setText(String.valueOf(purCartHeadBean.getNum()));
        titleName.setText(first.getGateName());
        titleTime.setText("");

        purcartComSelect.setOnClickListener(view -> {
            if (onSelectItemClickListener != null)
                onSelectItemClickListener.onItemSelectNum(position, purcartComSelect, !isSelect);
        });
        purcartComSubtract.setOnClickListener(view -> {
            if (onItemSubtractClickListener != null)
                onItemSubtractClickListener.onItemSubtractNum(position, purcartComNum);
        });
        purcartComAdd.setOnClickListener(view -> {
            if (onItemAddClickListener != null)
                onItemAddClickListener.onItemAddNum(position, purcartComNum);
        });
        itemView.setOnClickListener(view -> {
            if (onItemViewClickListener != null)
                onItemViewClickListener.onItemViewNum(first);
        });

        return convertView;
    }

    @NonNull
    private View setCom(ViewGroup parent, PurCartHeadBean purCartHeadBean, int position) {
        View convertView;
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_purcart_com_list_item_layout, parent, false);
        PurCartHeadBean.DataBean first = purCartHeadBean.getFirst();
        PurCartHeadBean.DataBean second = purCartHeadBean.getSecond();

        ImageView purcartComSelect = convertView.findViewById(R.id.purcartComSelect);    //是否选择
        ImageView purcartComImage = convertView.findViewById(R.id.purcartComImage);      //图片
        TextView purcartComName = convertView.findViewById(R.id.purcartComName);        //名称
        TextView purcartComType = convertView.findViewById(R.id.purcartComType);        //类型
        TextView purcartComPrice = convertView.findViewById(R.id.purcartComPrice);      //价格
        TextView purcartComSubtract = convertView.findViewById(R.id.purcartComSubtract);//数量减少
        TextView purcartComNum = convertView.findViewById(R.id.purcartComNum);          //数量
        TextView purcartComAdd = convertView.findViewById(R.id.purcartComAdd);          //数量增加
        TextView titleName = convertView.findViewById(R.id.titleName);
        TextView titleTime = convertView.findViewById(R.id.titleTime);
        titleTime.setVisibility(View.GONE);
        boolean isSelect;
        if (purCartHeadBean.getChecked() == 1) {
            isSelect = true;
            purcartComSelect.setImageResource(R.mipmap.selected_img);
        } else {
            isSelect = false;
            purcartComSelect.setImageResource(R.mipmap.purcart_no_select);
        }
        if (purCartHeadBean.getPic() != null)
            ImageUtil.displayImage(mcontext, purcartComImage, purCartHeadBean.getPic());

        purcartComName.setText(first.getName());
        purcartComPrice.setText(first.getPrice());
        if (second != null) {
            purcartComType.setText(second.getName());
            if (first.getPrice() != null && second.getPrice() != null) {
                float allPrice = Float.parseFloat(first.getPrice()) + Float.parseFloat(second.getPrice());
                purcartComPrice.setText(String.valueOf(allPrice));
            } else {
                float allPrice = Float.parseFloat(second.getPrice());
                purcartComPrice.setText(String.valueOf(allPrice));
            }
        }
        purcartComNum.setText(String.valueOf(purCartHeadBean.getNum()));
        titleName.setText("分开供应");
        titleTime.setText("");

        purcartComSelect.setOnClickListener(view -> {
            if (onSelectComClickListener != null)
                onSelectComClickListener.onComSelectNum(position, purcartComSelect, !isSelect);
        });
        purcartComSubtract.setOnClickListener(view -> {
            if (onComSubtractClickListener != null)
                onComSubtractClickListener.onComSubtractNum(position, purcartComNum);
        });
        purcartComAdd.setOnClickListener(view -> {
            if (onComAddClickListener != null)
                onComAddClickListener.onComAddNum(position, purcartComNum);
        });
        return convertView;
    }

    @NonNull
    private View setTitle(ViewGroup parent, String title) {
        View convertView;
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_purcart_title_layout, parent, false);
        TextView purcartTitle = convertView.findViewById(R.id.purcartTitle);
        purcartTitle.setText(title);
        return convertView;
    }


    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PurCartChildBean purCartChildBean = mPurCartComBeanChild.get(groupPosition).get(childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_purcart_com_list_item_title_item_layout, parent, false);
        }
        LinearLayout titleItemView = (LinearLayout) convertView.findViewById(R.id.titleItemView);
        TextView purcartComName = (TextView) convertView.findViewById(R.id.purcartComName);
        TextView purcartComPrice = (TextView) convertView.findViewById(R.id.purcartComPrice);
        TextView purcartComGateName = (TextView) convertView.findViewById(R.id.purcartComGateName);
        TextView purcartComTime = (TextView) convertView.findViewById(R.id.purcartComTime);

        purcartComTime.setVisibility(View.GONE);

        purcartComName.setText(purCartChildBean.getName());
        purcartComPrice.setText(purCartChildBean.getPrice());
        purcartComGateName.setText(purCartChildBean.getGateName());
        purcartComTime.setText("");

        titleItemView.setOnClickListener(view -> {
            if (onChildClickListener != null)
                onChildClickListener.onChild(purCartChildBean);
        });

        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface OnItemAddClickListener {

        /**
         * 点击+的回调
         *
         * @param position 被点击的Item位置
         */
        void onItemAddNum(int position, TextView purcartComNum);
    }

    private OnItemAddClickListener onItemAddClickListener;

    public void setOnItemAddClickListener(OnItemAddClickListener onItemAddClickListener) {
        this.onItemAddClickListener = onItemAddClickListener;
    }

    public interface OnItemViewClickListener {

        /**
         * 点击+的回调
         *
         * @param position 被点击的Item位置
         */
        void onItemViewNum(PurCartHeadBean.DataBean dataBean);
    }

    private OnItemViewClickListener onItemViewClickListener;

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }

    public interface OnItemSubtractClickListener {

        /**
         * 点击-的回调
         *
         * @param position 被点击的Item位置
         */
        void onItemSubtractNum(int position, TextView purcartComNum);
    }

    private OnItemSubtractClickListener onItemSubtractClickListener;

    public void setOnItemSubtractClickListener(OnItemSubtractClickListener onItemSubtractClickListener) {
        this.onItemSubtractClickListener = onItemSubtractClickListener;
    }

    public interface OnSelectItemClickListener {

        /**
         * 点击-的回调
         *
         * @param position 被点击的Item位置
         */
        void onItemSelectNum(int position, ImageView purcartComSelect, boolean isSelect);
    }

    private OnSelectItemClickListener onSelectItemClickListener;

    public void setSelectClickListener(OnSelectItemClickListener onSelectItemClickListener) {
        this.onSelectItemClickListener = onSelectItemClickListener;
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

    public interface OnSelectComClickListener {

        /**
         * 点击-的回调
         *
         * @param position 被点击的Item位置
         */
        void onComSelectNum(int position, ImageView purcartComSelect, boolean isSelect);
    }

    private OnSelectComClickListener onSelectComClickListener;

    public void setSelectComClickListener(OnSelectComClickListener onSelectComClickListener) {
        this.onSelectComClickListener = onSelectComClickListener;
    }

    public interface OnChildClickListener {

        /**
         * 点击-的回调
         */
        void onChild(PurCartChildBean purCartChildBean);
    }

    private OnChildClickListener onChildClickListener;

    public void setChildClickListener(OnChildClickListener onChildClickListener) {
        this.onChildClickListener = onChildClickListener;
    }
}
