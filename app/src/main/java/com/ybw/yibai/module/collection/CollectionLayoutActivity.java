package com.ybw.yibai.module.collection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.adapter.CollectionLayoutAdapter;
import com.ybw.yibai.common.bean.CollectionListBean;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SkuDetailsBean;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.xlist.XListView;
import com.ybw.yibai.common.widget.xlist.recycler.EndlessRecyclerOnScrollListener;
import com.ybw.yibai.module.scene.SceneActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.PLANT;
import static com.ybw.yibai.common.constants.Preferences.POT;

public class CollectionLayoutActivity extends BaseActivity implements CollectionLayoutAdapter.OnItemClickListener,
        CollectionLayoutContract.CollectionLayoutView, View.OnClickListener {

    /**
     * 指示器框架
     */
    private TextView mCreateDesign;
    private TextView mDeleteCollection;
    private TextView mCompleteCollection;
    private TextView mCollectionItem;
    private TextView mCollectionCombination;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /**
     * 上下文对象
     */
    private Context mContext;
    /**
     * 当前页数
     */
    private int page = 1;
    /**
     * 收藏列表数据
     */
    private List<CollectionListBean.DataBean.ListBean> dataBeanList = new ArrayList<>();
    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private CollectionLayoutContract.CollectionLayoutPresenter mCollectionLayoutPresenter;
    private CollectionLayoutAdapter mCollectionLayoutAdapter;
    private int mIndex = 0; //0:1 = 单品：组合

    @Override
    protected int setLayout() {
        mContext = getApplicationContext();
        return R.layout.collection_layout;
    }

    @Override
    protected void initView() {
        mCreateDesign = findViewById(R.id.createDesign);
        mDeleteCollection = findViewById(R.id.deleteCollection);
        mCompleteCollection = findViewById(R.id.completeCollection);
        mCollectionItem = findViewById(R.id.collection_item);
        mCollectionCombination = findViewById(R.id.collection_combination);
        mWaitDialog = new WaitDialog(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));

        mCollectionLayoutAdapter = new CollectionLayoutAdapter(getApplicationContext(), dataBeanList);
        recyclerView.setAdapter(mCollectionLayoutAdapter);

        // 设置加载更多监听
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                mCollectionLayoutAdapter.setLoadState(mCollectionLayoutAdapter.LOADING);
                page++;
                mCollectionLayoutPresenter.getCollect(mIndex, page);
            }
        });

        mCollectionLayoutAdapter.setOnItemClickListener(this);
        mCreateDesign.setOnClickListener(this);
        mDeleteCollection.setOnClickListener(this);
        mCompleteCollection.setOnClickListener(this);
        mCollectionItem.setOnClickListener(this);
        mCollectionCombination.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mCollectionLayoutPresenter = new CollectionLayoutPresenterImpl(this);
        mCollectionLayoutPresenter.getCollect(1, page);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.createDesign) {
            mDeleteCollection.setVisibility(View.VISIBLE);
            mCompleteCollection.setVisibility(View.VISIBLE);
            mCreateDesign.setVisibility(View.GONE);
            for (Iterator<CollectionListBean.DataBean.ListBean> iterator = dataBeanList.iterator(); iterator.hasNext(); ) {
                CollectionListBean.DataBean.ListBean next = iterator.next();
                next.setEdit(true);
                next.setSelect(false);
            }
            mCollectionLayoutAdapter.notifyDataSetChanged();
        }
        if (id == R.id.deleteCollection) {
            List<String> skuOrCollectId = new ArrayList<>();
            for (Iterator<CollectionListBean.DataBean.ListBean> iterator = dataBeanList.iterator(); iterator.hasNext(); ) {
                CollectionListBean.DataBean.ListBean collectData = iterator.next();
                if (collectData.isSelect()) {
                    skuOrCollectId.add(collectData.getCollect_id());
                }
            }
            if (skuOrCollectId.size() > 0) {
                mCollectionLayoutPresenter.deleteCollection(skuOrCollectId);
            } else {
                MessageUtil.showMessage("请至少选择一个删除!");
            }
        }
        if (id == R.id.completeCollection) {
            mDeleteCollection.setVisibility(View.GONE);
            mCompleteCollection.setVisibility(View.GONE);
            mCreateDesign.setVisibility(View.VISIBLE);
            for (Iterator<CollectionListBean.DataBean.ListBean> iterator = dataBeanList.iterator(); iterator.hasNext(); ) {
                CollectionListBean.DataBean.ListBean next = iterator.next();
                next.setEdit(false);
            }
            mCollectionLayoutAdapter.notifyDataSetChanged();
        }
        if (id == R.id.collection_item) {
            dataBeanList.clear();
            mCollectionItem.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            mCollectionItem.setTextColor(getResources().getColor(R.color.black));
            //字体大小为16，并且加粗
            mCollectionItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mCollectionItem.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            mCollectionCombination.setTextColor(getResources().getColor(R.color.prompt_high_text_color));
            //取消加粗
            mCollectionCombination.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            mCollectionCombination.getPaint().setFlags(0);
            mCollectionCombination.invalidate();
            page = 1;
            mIndex = 1;
            mCollectionLayoutPresenter.getCollect(mIndex, page);
        }
        if (id == R.id.collection_combination) {
            dataBeanList.clear();
            mCollectionCombination.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            mCollectionCombination.setTextColor(getResources().getColor(R.color.black));
            //字体大小为16，并且加粗
            mCollectionCombination.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mCollectionCombination.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            mCollectionItem.setTextColor(getResources().getColor(R.color.prompt_high_text_color));
            //取消加粗
            mCollectionItem.getPaint().setFlags(0);
            mCollectionItem.invalidate();

            mCollectionItem.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            page = 1;
            mIndex = 2;
            mCollectionLayoutPresenter.getCollect(mIndex, page);
        }
    }

    @Override
    public void onItemClick(CollectionListBean.DataBean.ListBean listBean) {
        if (mIndex == 1) {
            mCollectionLayoutPresenter.getSkuListIds(listBean.getSku_id(), "");
        } else {
            mCollectionLayoutPresenter.getSkuListIds(listBean.getPlant_sku_id(),"");
        }
    }

    @Override
    public void onGetProductDetailsSuccess(SkuDetailsBean skuDetailsBean) {
        SkuDetailsBean.DataBean.ListBean listBean = skuDetailsBean.getData().getList().get(0);
        mSelectItem = listBean;
        // 说明从"更多"界面打开
        if (mIndex == 1)
            listBean.setCategoryCode(POT);
        else
            listBean.setCategoryCode(PLANT);
        mCollectionLayoutPresenter.saveSimulation(listBean);
    }

    SkuDetailsBean.DataBean.ListBean mSelectItem = null;

    @Override
    public void onSaveSimulationResult(boolean result) {
        Intent intent = new Intent(mContext, SceneActivity.class);
        if (mSelectItem != null) {
            intent.putExtra("com_type", String.valueOf(mSelectItem.getComtype()));
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onDeleteCollectionListSuccess(List<String> skuOrCollectId) {
        List<CollectionListBean.DataBean.ListBean> deleteCollectId = new ArrayList<>();
        for (Iterator<CollectionListBean.DataBean.ListBean> iterator = dataBeanList.iterator(); iterator.hasNext(); ) {
            CollectionListBean.DataBean.ListBean collectData = iterator.next();
            if (collectData.isCombination()) {
                if (collectData.isSelect()) {
                    deleteCollectId.add(collectData);
                }
            } else {
                if (collectData.isSelect()) {
                    deleteCollectId.add(collectData);
                }
            }
        }
        if (skuOrCollectId.size() > 0) {
            dataBeanList.removeAll(deleteCollectId);
            mCollectionLayoutAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCollectionListBeanSuccess(CollectionListBean collectionListBean) {
        if (CODE_SUCCEED != collectionListBean.getCode()) {
            return;
        }
        List<CollectionListBean.DataBean.ListBean> dataList = collectionListBean.getData().getList();
        if (null == dataList || dataList.size() == 0) {
            MessageUtil.showMessage("已经最后一页了");
            // 显示加载到底的提示
            mCollectionLayoutAdapter.setLoadState(mCollectionLayoutAdapter.LOADING_END);
            return;
        }
        dataBeanList.addAll(collectionListBean.getData().getList());
        if (mIndex == 2) {
            for (Iterator<CollectionListBean.DataBean.ListBean> iterator = dataBeanList.iterator(); iterator.hasNext(); ) {
                CollectionListBean.DataBean.ListBean next = iterator.next();
                next.setCombination(true);
            }
        }
        mCollectionLayoutAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * 在请求网络数据之前显示Loading界面
     */
    @Override
    public void onShowLoading() {
        if (!mWaitDialog.isShowing()) {
            mWaitDialog.setWaitDialogText(getResources().getString(R.string.loading));
            mWaitDialog.show();
        }
    }

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    @Override
    public void onHideLoading() {
        if (mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }

    /**
     * 在请求网络数据失败时进行一些操作,如显示错误信息...
     *
     * @param throwable 异常类型
     */
    @Override
    public void onLoadDataFailure(Throwable throwable) {
        ExceptionUtil.handleException(throwable);
    }

}
