package com.ybw.yibai.module.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.FragmentPagerAdapter;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.bean.SystemParameter.DataBean;
import com.ybw.yibai.common.bean.SystemParameter.DataBean.UsestateBean;
import com.ybw.yibai.common.bean.UpdateSKUUseState;
import com.ybw.yibai.common.bean.UserPosition;
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
import com.ybw.yibai.module.details.ProductDetailsActivity;
import com.ybw.yibai.module.home.HomeFragment;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.product.ProductContract.ProductPresenter;
import com.ybw.yibai.module.product.ProductContract.ProductView;
import com.ybw.yibai.module.productusestate.ProductUseStateFragment;
import com.ybw.yibai.module.search.SearchActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.ybw.yibai.common.constants.Preferences.PRODUCT_USE_STATE_ID;
import static com.ybw.yibai.common.utils.OtherUtil.createBarView;

/**
 * 产品Fragment
 *
 * @author sjl
 * @date 2019/9/5
 */
public class ProductFragment extends BaseFragment implements ProductView, View.OnClickListener, ViewPager.OnPageChangeListener {

    public static final String TAG = "ProductFragment";

    /**
     * ViewPager当前滑动到的页面
     */
    private int position;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 一个和"状态栏"一样高的view
     */
    private View mView;

    /**
     * 搜索
     */
    private ImageView mSearchImageView;

    /**
     * 指示器框架
     */
    private MagicIndicator mMagicIndicator;

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
    private ProductPresenter mProductPresenter;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        mActivity = getActivity();
        return R.layout.fragment_product;
    }

    @Override
    protected void findViews(View view) {
        mView = view.findViewById(R.id.barView);
        mSearchImageView = view.findViewById(R.id.searchImageView);

        mMagicIndicator = view.findViewById(R.id.magicIndicator);
        mViewPager = view.findViewById(R.id.viewPager);
        mWaitDialog = new WaitDialog(mContext);
    }

    @Override
    protected void initView() {
        createBarView(mActivity, mView);
        initMagicIndicator();
    }

    @Override
    protected void initData() {
        mClassList = new ArrayList<>();
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mProductPresenter = new ProductPresenterImpl(this);
        mSearchImageView.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 搜索
        if (id == R.id.searchImageView) {
            Intent intent = new Intent(mContext, SearchActivity.class);
            startActivity(intent);
            mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
            titleView.setTextSize(20);
            titleView.setText(mClassList.get(index));
            titleView.setSelectedColor(ContextCompat.getColor(mContext, android.R.color.white));
            titleView.setNormalColor(ContextCompat.getColor(mContext, android.R.color.white));
            titleView.setOnClickListener(v -> mViewPager.setCurrentItem(index));
            return titleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
            indicator.setYOffset(Util.dipToPx(context, 20));
            indicator.setStartInterpolator(new AccelerateInterpolator());
            indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
            indicator.setColors(ContextCompat.getColor(mContext, R.color.colorPrimary));
            return indicator;
        }
    };

    /**
     * EventBus
     * 接收用户从{@link MainActivity#onGetSystemParameterSuccess(SystemParameter)}
     * 发送过来的数据(在获取系统参数成功时回调)
     *
     * @param systemParameter 系统参数
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getSystemParameterSuccess(SystemParameter systemParameter) {
        if (null == systemParameter) {
            return;
        }
        DataBean data = systemParameter.getData();
        if (null == data) {
            return;
        }
        List<UsestateBean> useState = new ArrayList<>();
        for (Iterator<UsestateBean> iterator = data.getUsestate().iterator(); iterator.hasNext(); ) {
            UsestateBean usestateBean = iterator.next();
            if (usestateBean.getName().equals("已使用") || usestateBean.getName().equals("未使用"))
                continue;
            useState.add(usestateBean);
        }

        if (null == useState || useState.size() == 0) {
            return;
        }
        mClassList.clear();
        mViewPager.removeAllViews();
        int size = useState.size();
        Fragment[] fragment = new Fragment[size];
        for (int i = 0; i < size; i++) {
            UsestateBean bean = useState.get(i);
            int id = bean.getId();
            String name = bean.getName();
            mClassList.add(name);

            fragment[i] = new ProductUseStateFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(PRODUCT_USE_STATE_ID, id);
            fragment[i].setArguments(bundle);
        }
        mCommonNavigatorAdapter.notifyDataSetChanged();
        FragmentManager manager = getChildFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
        mViewPager.setCurrentItem(position);
    }

    /**
     * EventBus
     * 接收用户从{@link HomeFragment#onSetUserPositionSuccess(UserPosition)}
     * 发送过来的数据(在设置货源城市成功时回调)
     *
     * @param userPosition 设置货源城市时服务器端返回的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetUserPositionSuccess(UserPosition userPosition) {
        getSystemParameterSuccess(YiBaiApplication.getSystemParameter());
    }

    /**
     * EventBus
     * 接收用户从{@link ProductDetailsActivity#onUpdateSKUUseStateSuccess(UpdateSKUUseState)}
     * 发送过来的数据(在修改产品sku使用状态成功时回调)
     *
     * @param updateSKUUseState 修改产品sku使用状态时服务器端返回的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateSKUUseStateSuccess(UpdateSKUUseState updateSKUUseState) {
        getSystemParameterSuccess(YiBaiApplication.getSystemParameter());
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
        if (null != mProductPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mProductPresenter.onDetachView();
            mProductPresenter = null;
        }
    }
}