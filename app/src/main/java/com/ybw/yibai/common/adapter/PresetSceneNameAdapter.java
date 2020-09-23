package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.module.startdesign.StartDesignPresenterImpl;

import java.util.List;

/**
 * 开始设计界面预设场景列表适配器
 *
 * @author sjl
 * {@link StartDesignPresenterImpl}
 */
public class PresetSceneNameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 场景名称列表
     */
    private List<String> mSceneNameList;

    public PresetSceneNameAdapter(Context context, List<String> sceneNameList) {
        this.mContext = context;
        this.mSceneNameList = sceneNameList;
    }

    @Override
    public int getItemCount() {
        return mSceneNameList == null ? 0 : mSceneNameList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_scene_name_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        String name = mSceneNameList.get(position);
        if (TextUtils.isEmpty(name)) {
            myViewHolder.mNameTextView.setText(" ");
        } else {
            myViewHolder.mNameTextView.setText(name);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mNameTextView;

        private MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mNameTextView = itemView.findViewById(R.id.nameTextView);
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
