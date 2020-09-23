package com.ybw.yibai.module.recommendproduct;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.RecommendAdapter;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.NewMatch;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.module.sceneedit.SceneEditFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.ybw.yibai.common.constants.Preferences.CATEGORY_CODE;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_LIST;

/**
 * 推荐产品信息界面
 *
 * @author sjl
 * @date 2019/09/26
 */
public class RecommendProductFragment extends BaseFragment implements RecommendAdapter.OnItemClickListener {

    private String TAG = "RecommendProductFragment";

    /**
     * 当前Fragment在ViewPager中的位置
     */
    private int position;

    /**
     * 盆器/植物
     */
    private String categoryCode;

    /**
     * 显示产品SUK列表
     */
    private RecyclerView mRecyclerView;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * SKU列表
     */
    private List<ListBean> mSKUList;

    /**
     * 推荐产品的适配器
     */
    private RecommendAdapter mAdapter;

     /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 上下文对象
     */
    private Context mContext;

    @Override
    protected int setLayouts() {
        return R.layout.fragment_recommend_product;
    }

    @Override
    protected void findViews(View view) {
        mContext = getContext();
        mRecyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    protected void initView() {
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        int spacing = DensityUtil.dpToPx(mContext, 8);
        // 设置RecyclerView间距
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(5, spacing, true);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }
        position = bundle.getInt(POSITION);
        categoryCode = bundle.getString(CATEGORY_CODE);
        mSKUList = new Gson().fromJson(bundle.getString(PRODUCT_SKU_LIST), new TypeToken<List<ListBean>>() {
        }.getType());
        if (null == mSKUList) {
            return;
        }
        mAdapter = new RecommendAdapter(mContext, mSKUList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        ListBean listBean = mSKUList.get(position);
        NewMatch match = new NewMatch();
        match.setSku_id(listBean.getSku_id());
        match.setProduct_id(listBean.getProduct_id());
        match.setName(listBean.getName());
        match.setPic1(listBean.getPic1());
        match.setPic2(listBean.getPic2());
        match.setPic3(listBean.getPic3());
        match.setScore(listBean.getScore());
        match.setPrice(listBean.getPrice());
        match.setMonth_rent(listBean.getMonth_rent());
        match.setTrade_price(listBean.getTrade_price());
        match.setHeight(listBean.getHeight());
        match.setDiameter(listBean.getDiameter());
        match.setOffset_ratio(listBean.getOffset_ratio());
        match.setCategoryCode(categoryCode);
        // 10为每一页数据的数目
        match.setPosition(this.position * 10 + position);

        /**
         * 发送数据到{@link SceneEditFragment#onSelectedRecommendProduct(NewMatch)}
         */
        EventBus.getDefault().post(match);
    }
}
