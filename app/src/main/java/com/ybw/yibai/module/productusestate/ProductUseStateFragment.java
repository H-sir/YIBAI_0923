package com.ybw.yibai.module.productusestate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.FragmentPagerAdapter;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.common.bean.ProductScreeningParam.DataBean;
import com.ybw.yibai.common.bean.ViewPagerPositions;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.magicindicator.MagicIndicator;
import com.ybw.yibai.common.widget.magicindicator.ViewPagerHelper;
import com.ybw.yibai.common.widget.magicindicator.buildins.Util;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.ybw.yibai.common.widget.magicindicator.title.ScaleTransitionPagerTitleView;
import com.ybw.yibai.module.filter.FilterActivity;
import com.ybw.yibai.module.producttype.ProductTypeFragment;
import com.ybw.yibai.module.productusestate.ProductUseStateContract.ProductUseStatePresenter;
import com.ybw.yibai.module.productusestate.ProductUseStateContract.ProductUseStateView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.CATEGORY_CODE;
import static com.ybw.yibai.common.constants.Preferences.KEY_WORD;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_USE_STATE_ID;
import static com.ybw.yibai.common.constants.Preferences.TYPE;

/**
 * 产品使用状态Fragment
 *
 * @author sjl
 * @date 2019/12/10
 */
public class ProductUseStateFragment extends BaseFragment implements ProductUseStateView, View.OnClickListener, ViewPager.OnPageChangeListener {

    public static final String TAG = "ProductFragment";

    /**
     * ViewPager当前滑动到的页面
     */
    private int position;

    /**
     * 产品使用状态Id
     */
    private int productUseStateId;

    /**
     * 标记用户是从那个界面打开本界面的
     */
    private String type;

    /**
     * 搜索关键词
     */
    private String keyWord;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 指示器框架
     */
    private MagicIndicator mMagicIndicator;

    /**
     * 筛选
     */
    private TextView mFilterTextView;

    /**
     *
     */
    private ViewPager mViewPager;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 产品类别集合列表
     */
    private List<String> mClassList;

    /**
     * 产品筛选参数集合
     */
    private List<DataBean> mProductScreeningParamList;

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

    /**
     * Activity对象
     */
    private FragmentActivity mActivity;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private ProductUseStatePresenter mProductUseStatePresenter;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        mActivity = getActivity();
        return R.layout.fragment_product_use_state;
    }

    @Override
    protected void findViews(View view) {
        mMagicIndicator = view.findViewById(R.id.magicIndicator);
        mFilterTextView = view.findViewById(R.id.filterTextView);
        mViewPager = view.findViewById(R.id.viewPager);
        mWaitDialog = new WaitDialog(mContext);
    }

    @Override
    protected void initView() {
        initMagicIndicator();
    }

    @Override
    protected void initData() {
        mClassList = new ArrayList<>();
        mProductScreeningParamList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (null != bundle) {
            productUseStateId = bundle.getInt(PRODUCT_USE_STATE_ID);
            type = bundle.getString(TYPE);
            keyWord = bundle.getString(KEY_WORD);
        }
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mProductUseStatePresenter = new ProductUseStatePresenterImpl(this);
        mProductUseStatePresenter.getProductScreeningParam();

        mFilterTextView.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 筛选
        if (id == R.id.filterTextView) {
            Intent intent = new Intent(mActivity, FilterActivity.class);
            intent.putExtra(POSITION, position);
            startActivity(intent);
        }
    }

    /**
     * ViewPager某一个页面被选中时回调
     *
     * @param position 当前滑动到的页面
     */
    @Override
    public void onPageSelected(int position) {
        this.position = position;
    }

    /**
     * ViewPager这个方法在手指操作屏幕的时候发生变化
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * ViewPager在屏幕滚动过程中不断被调用
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(mCommonNavigatorAdapter);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    private CommonNavigatorAdapter mCommonNavigatorAdapter = new CommonNavigatorAdapter() {
        @Override
        public int getCount() {
            return mClassList == null ? 0 : mClassList.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, int index) {
            SimplePagerTitleView titleView = new ScaleTransitionPagerTitleView(context);
            titleView.setTextSize(16);
            titleView.setText(mClassList.get(index));
            titleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.main_text_color));
            titleView.setNormalColor(ContextCompat.getColor(mContext, R.color.prompt_high_text_color));
            titleView.setOnClickListener(v -> mViewPager.setCurrentItem(index));
            return titleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
            indicator.setYOffset(Util.dipToPx(context, 15));
            indicator.setStartInterpolator(new AccelerateInterpolator());
            indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
            indicator.setColors(ContextCompat.getColor(mContext, R.color.auxiliary_classification_column_color));
            return indicator;
        }
    };

    /**
     * 在获取产品筛选参数成功时回调
     *
     * @param productScreeningParam 获取产品筛选参数时服务器端返回的数据
     */
    @Override
    public void onGetProductScreeningParamSuccess(ProductScreeningParam productScreeningParam) {
        if (CODE_SUCCEED != productScreeningParam.getCode()) {
            return;
        }
        List<DataBean> dataList = productScreeningParam.getData();
        if (null == dataList || dataList.size() == 0) {
            return;
        }
        Fragment[] fragment = new Fragment[dataList.size()];
        for (int i = 0; i < fragment.length; i++) {
            DataBean dataBean = dataList.get(i);
            mClassList.add(dataBean.getName());
            fragment[i] = new ProductTypeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(POSITION, i);
            bundle.putInt(PRODUCT_USE_STATE_ID, productUseStateId);
            bundle.putString(CATEGORY_CODE, dataBean.getCate_code());
            if (!TextUtils.isEmpty(type)) {
                bundle.putString(TYPE, type);
            }
            if (!TextUtils.isEmpty(keyWord)) {
                bundle.putString(KEY_WORD, keyWord);
            }
            fragment[i].setArguments(bundle);
        }
        mFilterTextView.setVisibility(View.VISIBLE);
        mProductScreeningParamList.addAll(dataList);
        mCommonNavigatorAdapter.notifyDataSetChanged();
        FragmentManager manager = getChildFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void filterActivityViewPagerSelected(ViewPagerPositions viewPagerPositions) {
        int position = viewPagerPositions.getPosition();
        if (this.position == position) {
            return;
        }
        mViewPager.setCurrentItem(position);
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

    /**
     * 友盟统计Fragment页面
     */
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    /**
     * 友盟统计Fragment页面
     */
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mProductUseStatePresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mProductUseStatePresenter.onDetachView();
            mProductUseStatePresenter = null;
        }
    }
}
