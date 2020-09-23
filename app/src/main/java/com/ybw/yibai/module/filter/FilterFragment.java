package com.ybw.yibai.module.filter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jaygoo.widget.RangeSeekBar;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.ProductScreeningParamAdapter;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.ProductScreeningParam.DataBean;
import com.ybw.yibai.common.bean.ProductScreeningParam.DataBean.ParamBean;
import com.ybw.yibai.common.bean.SelectProductParam;
import com.ybw.yibai.module.filter.FilterContract.FilterPresenter;
import com.ybw.yibai.module.filter.FilterContract.FilterView;
import com.ybw.yibai.module.producttype.ProductTypeFragment;
import com.ybw.yibai.module.sceneedit.SceneEditFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SCREENING_PARAM;

/**
 * 筛选
 *
 * @author sjl
 * @date 2019/9/9
 */
public class FilterFragment extends BaseFragment implements FilterView,
        View.OnClickListener,
        ProductScreeningParamAdapter.OnItemClickListener,
        ProductScreeningParamAdapter.OnRangeSeekBarChangedListener {

    public static final String TAG = "FilterFragment";

    /**
     * 当前Fragment在ViewPager中的位置
     */
    private int position;

    /*
     /\_/\
   =( °w° )=
     )   (
    (__ __)
   */

    private RecyclerView mRecyclerView;

    /**
     * 重置
     */
    private TextView mResetTextView;

    /**
     * 确定
     */
    private TextView mDetermineTextView;

   /*
     /\_/\
   =( °w° )=
     )   (
    (__ __)
   */

    private DataBean mParam;

    /**
     * 产品筛选参数集合
     */
    protected List<ParamBean> mParamList;

    /**
     * 显示产品筛选参数的Adapter
     */
    protected ProductScreeningParamAdapter mAdapter;

    /*
     /\_/\
   =( °w° )=
     )   (
    (__ __)
   */

    private Activity mActivity;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private FilterPresenter mFilterPresenter;

    @Override
    protected int setLayouts() {
        return R.layout.fragment_filter;
    }

    @Override
    protected void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mResetTextView = view.findViewById(R.id.resetTextView);
        mDetermineTextView = view.findViewById(R.id.determineTextView);
    }

    @Override
    protected void initView() {
        mActivity = getActivity();
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        // 布局水平
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initData() {
        mParamList = new ArrayList<>();

        // 获取FilterActivity传过来的数据
        Bundle bundle = getArguments();
        if (null != bundle) {
            position = bundle.getInt(POSITION);
            mParam = bundle.getParcelable(PRODUCT_SCREENING_PARAM);
        }
        if (null != mParam) {
            mParamList.addAll(mParam.getParam());
        }

        mAdapter = new ProductScreeningParamAdapter(getContext(), mParamList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mFilterPresenter = new FilterPresenterImpl(this);
        mResetTextView.setOnClickListener(this);
        mDetermineTextView.setOnClickListener(this);

        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnRangeSeekBarChangedListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        mFilterPresenter.onClick(v);
    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param parentPosition 被点击的父Item位置
     * @param childPosition  被点击的子Item位置
     */
    @Override
    public void onItemClick(int parentPosition, int childPosition) {
        // LogUtil.e(TAG, "被点击的父Item位置: " + parentPosition + " 被点击的子Item位置: " + childPosition);
    }

    /**
     * 在RangeSeekBar值发生变化时回调
     *
     * @param view       值发生变化的RangeSeekBar
     * @param leftValue  RangeSeekBar左边的值
     * @param rightValue RangeSeekBar右边的值
     * @param isFromUser 是否来自于用户主动触发的
     * @param position   值发生变化的RangeSeekBar在列表中的位置
     */
    @Override
    public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser, int position) {
        // LogUtil.e(TAG, "左边的值: " + leftValue + " 右边的值: " + rightValue + " 位置: " + position);
    }

    /**
     * 返回用户选中的产品筛选参数
     *
     * @param selectProductParamMap 用户选中的产品筛选参数
     */
    @Override
    public void returnSelectProductParam(Map<String, String> selectProductParamMap) {
        /**
         * 发送数据到{@link ProductTypeFragment#returnSelectProductParam(SelectProductParam)} 使其获得用户选中的产品筛选参数
         * 发送数据到{@link SceneEditFragment#returnSelectProductParam(SelectProductParam)}使其获得用户选中的产品筛选参数
         */
        EventBus.getDefault().post(new SelectProductParam(position, selectProductParamMap));
        mActivity.finish();
    }

    @Override
    public void onShowLoading() {

    }

    @Override
    public void onHideLoading() {

    }

    @Override
    public void onLoadDataFailure(Throwable throwable) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mFilterPresenter) {
            mFilterPresenter.onDetachView();
            mFilterPresenter = null;
        }
    }
}