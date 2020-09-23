package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.SystemParameter.DataBean.PotBean;
import com.ybw.yibai.module.sceneedit.SceneEditFragment;

import java.util.List;

/**
 * 推荐盆器分类适配器
 *
 * @author sjl
 * {@link SceneEditFragment}
 */
public class RecommendPotTypeAdapter extends BaseAdapter {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 推荐盆器分类
     */
    private List<PotBean> mRecommendPotType;

    private LayoutInflater mInflater;

    public RecommendPotTypeAdapter(Context context, List<PotBean> recommendPotType) {
        this.mContext = context;
        this.mRecommendPotType = recommendPotType;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mRecommendPotType == null ? 0 : mRecommendPotType.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecommendPotType.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_recommend_pot_type_item_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mNameTextView = convertView.findViewById(R.id.nameTextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PotBean potBean = mRecommendPotType.get(position);
        String name = potBean.getName();
        if (TextUtils.isEmpty(name)) {
            viewHolder.mNameTextView.setText(" ");
        } else {
            viewHolder.mNameTextView.setText(name);
        }

        return convertView;
    }

    private class ViewHolder {

        /**
         * 盆器分类名称
         */
        TextView mNameTextView;
    }
}
