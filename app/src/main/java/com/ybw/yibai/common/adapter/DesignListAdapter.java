package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.DesignList;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.common.widget.horizontal.adatper.AbsAdapterItem;

import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public class DesignListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "DesignListAdapter";

    /**
     * 上下文对象
     */
    private Context mContext;

    private List<DesignList.DataBean.ListBean> dataBeanList;
    /**
     * 正在编辑的场景
     */
    private SceneInfo sceneInfo;

    private boolean sceneInfoFlag = false;
    /**
     * 场景适配器
     */
    private com.ybw.yibai.common.widget.horizontal.adatper.BaseAdapter<AbsAdapterItem> mDesignListAdapter;

    public DesignListAdapter(Context context, List<DesignList.DataBean.ListBean> dataBeanList, SceneInfo sceneInfo) {
        this.mContext = context;
        this.dataBeanList = dataBeanList;
        this.sceneInfo = sceneInfo;
    }

    @Override
    public int getItemCount() {
        return dataBeanList == null ? 0 : dataBeanList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.listview_design_list_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        DesignList.DataBean.ListBean dataBean = dataBeanList.get(position);
        myViewHolder.mDesignName.setText(dataBean.getNumber());
        String twoDay = TimeUtil.getTwoDay(TimeUtil.getStringToday(), dataBean.getLasttime());

        mDesignListAdapter = new com.ybw.yibai.common.widget.horizontal.adatper.BaseAdapter<>();
        myViewHolder.mDesignListView.setAdapter(mDesignListAdapter);
        mDesignListAdapter.clear();

        boolean flag = false;
        for (Iterator<DesignList.DataBean.ListBean.SchemelistBean> iterator = dataBean.getSchemelist().iterator(); iterator.hasNext(); ) {
            DesignList.DataBean.ListBean.SchemelistBean schemelistBean = iterator.next();
            if (sceneInfo != null && schemelistBean.getSchemeId().equals(sceneInfo.getScheme_id())) {
                myViewHolder.mDesignStats.setText("正在编辑");
                sceneInfoFlag = true;
                flag = true;
            }
            mDesignListAdapter.addItem(new DesignSchemeAdapter(schemelistBean));
        }
        mDesignListAdapter.notifyDataSetChanged();
        if (!flag) {
            if (twoDay != null && !twoDay.isEmpty() && Integer.parseInt(twoDay) < 5) {
                myViewHolder.mDesignStats.setText("完成于" + twoDay + "天前");
            } else {
                myViewHolder.mDesignStats.setText(dataBean.getLasttime());
            }
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyViewHolder(View itemView) {
            super(itemView);

            mDesignName = itemView.findViewById(R.id.designName);
            mDesignStats = itemView.findViewById(R.id.designStats);
            mDesignListView = itemView.findViewById(R.id.designListView);
            mDesignDelete = itemView.findViewById(R.id.designDelete);
            mDesignShare = itemView.findViewById(R.id.designShare);

            mDesignDelete.setOnClickListener(this);
            mDesignShare.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.designDelete:
                    if (onDesignDeleteClickListener != null)
                        onDesignDeleteClickListener.onDesignDelete(getLayoutPosition());
                    break;
                case R.id.designShare:
                    if (onDesignShareClickListener != null)
                        onDesignShareClickListener.onDesignShare(getLayoutPosition());
                    break;
            }
        }

        /**
         * 设计名称
         */
        TextView mDesignName;

        /**
         * 设计状态
         */
        TextView mDesignStats;

        /**
         * 场景列表
         */
        com.ybw.yibai.common.widget.horizontal.adatper.HorizontalScrollListView mDesignListView;

        /**
         * 设计删除按钮
         */
        TextView mDesignDelete;

        /**
         * 设计分享按钮
         */
        TextView mDesignShare;

    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnDesignDeleteClickListener {

        /**
         * 在删除按钮点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onDesignDelete(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnDesignDeleteClickListener onDesignDeleteClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onDesignDeleteClickListener 删除按钮点击的侦听器
     */
    public void setOnDesignDeleteClickListener(OnDesignDeleteClickListener onDesignDeleteClickListener) {
        this.onDesignDeleteClickListener = onDesignDeleteClickListener;
    }

    /**
     * 1.定义一个接口
     */
    public interface OnDesignShareClickListener {

        /**
         * 在删除按钮点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onDesignShare(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnDesignShareClickListener onDesignShareClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onDesignShareClickListener 删除按钮点击的侦听器
     */
    public void setOnDesignShareClickListener(OnDesignShareClickListener onDesignShareClickListener) {
        this.onDesignShareClickListener = onDesignShareClickListener;
    }

    /**
     * 1.定义一个接口
     */
    public interface OnDesignSchemeImageClickListener {

        /**
         * 在场景图片点击时回调
         */
        void onDesignSchemeImage(DesignList.DataBean.ListBean.SchemelistBean schemelistBean,
                                 boolean sceneInfoFlag);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnDesignSchemeImageClickListener onDesignSchemeImageClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onDesignSchemeImageClickListener 删除按钮点击的侦听器
     */
    public void setDesignSchemeImageClickListener(OnDesignSchemeImageClickListener onDesignSchemeImageClickListener) {
        this.onDesignSchemeImageClickListener = onDesignSchemeImageClickListener;
    }

    private class DesignSchemeAdapter extends AbsAdapterItem {
        DesignList.DataBean.ListBean.SchemelistBean schemelistBean;

        public DesignSchemeAdapter(DesignList.DataBean.ListBean.SchemelistBean schemelistBean) {
            this.schemelistBean = schemelistBean;
        }

        @Override
        public View onCreateView(int position, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.listview_design_scheme_list_item_layout, null);
            return view;
        }

        @Override
        public void onUpdateView(View view, int position, ViewGroup parent) {
            ImageView mDesignSchemeImage = view.findViewById(R.id.designSchemeImage);
            TextView mDesignSchemeName = view.findViewById(R.id.designSchemeName);

            mDesignSchemeName.setText(schemelistBean.getSchemeName());
        }

        @Override
        public void onLoadViewResource(View view, int position, ViewGroup parent) {
            ImageView mDesignSchemeImage = view.findViewById(R.id.designSchemeImage);
            ImageUtil.displayImage(mContext, mDesignSchemeImage, schemelistBean.getBgpic());
            mDesignSchemeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDesignSchemeImageClickListener != null)
                        onDesignSchemeImageClickListener.onDesignSchemeImage(schemelistBean, sceneInfoFlag);
                }
            });
        }

        @Override
        public void onRecycleViewResource(View view, int position, ViewGroup parent) {

        }
    }
}