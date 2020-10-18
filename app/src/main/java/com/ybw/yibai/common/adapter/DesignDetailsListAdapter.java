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
import com.ybw.yibai.common.bean.DesignDetails;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.utils.ImageUtil;
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
public class DesignDetailsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "DesignDetailsListAdapter";

    /**
     * 上下文对象
     */
    private Context mContext;

    private List<DesignDetails.DataBean.SchemelistBean> schemelistBeans;
    /**
     * 场景适配器
     */
    private com.ybw.yibai.common.widget.horizontal.adatper.BaseAdapter<AbsAdapterItem> mDesignListAdapter;

    public DesignDetailsListAdapter(Context context, List<DesignDetails.DataBean.SchemelistBean> schemelistBeans) {
        this.mContext = context;
        this.schemelistBeans = schemelistBeans;
    }

    @Override
    public int getItemCount() {
        return schemelistBeans == null ? 0 : schemelistBeans.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.listview_design_details_list_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        DesignDetails.DataBean.SchemelistBean schemelistBean = schemelistBeans.get(position);
        myViewHolder.mDesignDetailsName.setText(schemelistBean.getSchemeName());
        if (schemelistBean.getBgpic() != null && !schemelistBean.getBgpic().isEmpty())
            ImageUtil.displayImage(mContext, myViewHolder.mDesignBg, schemelistBean.getBgpic());

        mDesignListAdapter = new com.ybw.yibai.common.widget.horizontal.adatper.BaseAdapter<>();
        myViewHolder.mDesignDetailsListView.setAdapter(mDesignListAdapter);

        if (schemelistBean.getImglist() != null && schemelistBean.getImglist().size() > 0) {
            for (Iterator<DesignDetails.DataBean.SchemelistBean.ImaData> iterator = schemelistBean.getImglist().iterator(); iterator.hasNext(); ) {
                DesignDetails.DataBean.SchemelistBean.ImaData imaData = iterator.next();
                mDesignListAdapter.addItem(new DesignSchemeDetailsAdapter(imaData));
            }
        }
        myViewHolder.mDesignDetailsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSceneItemClickListener != null)
                    onSceneItemClickListener.onSceneItemClick(schemelistBean);
            }
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyViewHolder(View itemView) {
            super(itemView);

            mDesignBg = itemView.findViewById(R.id.designBg);
            mDesignDetailsName = itemView.findViewById(R.id.designDetailsName);
            mDesignDetailsRename = itemView.findViewById(R.id.designDetailsRename);
            mDesignDetailsDelete = itemView.findViewById(R.id.designDetailsDelete);
            mDesignDetailsListView = itemView.findViewById(R.id.designDetailsListView);

            mDesignDetailsRename.setOnClickListener(this);
            mDesignDetailsDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.designDetailsRename:
                    if (onDesignDetailsRenameClickListener != null)
                        onDesignDetailsRenameClickListener.onDesignDetailsRename(getLayoutPosition());
                    break;
                case R.id.designDetailsDelete:
                    if (onDesignDetailsDeleteListClickListener != null)
                        onDesignDetailsDeleteListClickListener.onDesignDetailsListDelete(getLayoutPosition());
                    break;
            }
        }

        /**
         * 场景名称
         */
        TextView mDesignDetailsName;
        ImageView mDesignBg;

        /**
         * 场景改名
         */
        TextView mDesignDetailsRename;

        /**
         * 场景列表
         */
        com.ybw.yibai.common.widget.horizontal.adatper.HorizontalScrollListView mDesignDetailsListView;

        /**
         * 场景删除
         */
        TextView mDesignDetailsDelete;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnDesignDetailsDeleteClickListener {

        /**
         * 在删除按钮点击时回调
         */
        void onDesignDetailsDelete(DesignDetails.DataBean.SchemelistBean.ImaData imaData);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnDesignDetailsDeleteClickListener onDesignDetailsDeleteClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onDesignDetailsDeleteClickListener 删除按钮点击的侦听器
     */
    public void setOnDesignDetailsDeleteClickListener(OnDesignDetailsDeleteClickListener onDesignDetailsDeleteClickListener) {
        this.onDesignDetailsDeleteClickListener = onDesignDetailsDeleteClickListener;
    }

    /**
     * 1.定义一个接口
     */
    public interface OnDesignDetailsRenameClickListener {

        /**
         * 在删除按钮点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onDesignDetailsRename(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnDesignDetailsRenameClickListener onDesignDetailsRenameClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onDesignDetailsRenameClickListener 删除按钮点击的侦听器
     */
    public void setOnDesignDetailsRenameClickListener(OnDesignDetailsRenameClickListener onDesignDetailsRenameClickListener) {
        this.onDesignDetailsRenameClickListener = onDesignDetailsRenameClickListener;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnDesignDetailsDeleteListClickListener {

        /**
         * 在删除按钮点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onDesignDetailsListDelete(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnDesignDetailsDeleteListClickListener onDesignDetailsDeleteListClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onDesignDetailsDeleteListClickListener 删除按钮点击的侦听器
     */
    public void setOnDesignDetailsListDeleteClickListener(OnDesignDetailsDeleteListClickListener onDesignDetailsDeleteListClickListener) {
        this.onDesignDetailsDeleteListClickListener = onDesignDetailsDeleteListClickListener;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnSceneItemClickListener {

        /**
         * 在删除按钮点击时回调
         *
         * @param schemelistBean 被点击的Item位置
         */
        void onSceneItemClick(DesignDetails.DataBean.SchemelistBean schemelistBean);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnSceneItemClickListener onSceneItemClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onSceneItemClickListener 删除按钮点击的侦听器
     */
    public void setSceneItemClickListener(OnSceneItemClickListener onSceneItemClickListener) {
        this.onSceneItemClickListener = onSceneItemClickListener;
    }


    private class DesignSchemeDetailsAdapter extends AbsAdapterItem {
        DesignDetails.DataBean.SchemelistBean.ImaData imaData;

        public DesignSchemeDetailsAdapter(DesignDetails.DataBean.SchemelistBean.ImaData imaData) {
            this.imaData = imaData;
        }

        @Override
        public View onCreateView(int position, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.listview_design_scheme_details_list_item_layout, null);
            return view;
        }

        @Override
        public void onUpdateView(View view, int position, ViewGroup parent) {
            ImageView designSchemeImageDelete = view.findViewById(R.id.designSchemeImageDelete);
            designSchemeImageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDesignDetailsDeleteClickListener != null)
                        onDesignDetailsDeleteClickListener.onDesignDetailsDelete(imaData);
                }
            });
        }

        @Override
        public void onLoadViewResource(View view, int position, ViewGroup parent) {
            ImageView mDesignSchemeImage = view.findViewById(R.id.designSchemeImage);
            ImageUtil.displayImage(mContext, mDesignSchemeImage, imaData.getPic());
        }

        @Override
        public void onRecycleViewResource(View view, int position, ViewGroup parent) {

        }
    }
//    /**
//     * EventBus
//     * 接收用户从{@link DesignDetailsActivity#onDeleteSchemePicSuccess(String)} 传递过来的数据
//     *
//     * @param pic 图片ID
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void deleteSchemePicSuccess(String pic){
//        mDesignListAdapter.getItem()
//    }
}