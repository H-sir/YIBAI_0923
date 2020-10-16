package com.ybw.yibai.module.quotationpurchase;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
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
import com.ybw.yibai.common.bean.FunctionSwitch;
import com.ybw.yibai.common.bean.HiddenChanged;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.UserInfo;
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
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.purcart.PurCateFragment;
import com.ybw.yibai.module.purchase.PurchaseFragment;
import com.ybw.yibai.module.quotation.QuotationFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Preferences.SERVICE;
import static com.ybw.yibai.common.utils.OtherUtil.createBarView;

/**
 * 报价/进货Fragment
 *
 * @author sjl
 * @date 2019/10/29
 */
public class QuotationPurchaseFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    /**
     * 一个和"状态栏"一样高的view
     */
    private View mView;

    /**
     * 编辑
     */
    private TextView mEditTextView;

    /**
     * 指示器框架
     */
    private MagicIndicator mMagicIndicator;

    /**
     *
     */
    private ViewPager mViewPager;

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
    private Activity mActivity;

    /**
     * 功能集合列表
     */
    private List<String> mClassList;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        mActivity = getActivity();
        return R.layout.fragment_quotation_purchase;
    }

    @Override
    protected void findViews(View view) {
        mView = view.findViewById(R.id.barView);
        mEditTextView = view.findViewById(R.id.editTextView);
        mMagicIndicator = view.findViewById(R.id.magicIndicator);
        mViewPager = view.findViewById(R.id.viewPager);

        mEditTextView.setVisibility(View.GONE);
        mEditTextView.setOnClickListener(view1 -> {
            flag = !flag;
            /**
             * 发送数据到 {@link PurCateFragment#onDelete(HiddenChanged)}
             */
            EventBus.getDefault().postSticky(new HiddenChanged(flag));
        });
    }

    boolean flag = false;

    @Override
    protected void initView() {
        createBarView(mActivity, mView);
        initMagicIndicator();
    }

    @Override
    protected void initData() {
        mClassList = new ArrayList<>();
        /*mClassList.add(getResources().getString(R.string.quotation));
        mClassList.add(getResources().getString(R.string.purchase));

        Fragment[] fragment = new Fragment[mClassList.size()];
        fragment[0] = new QuotationFragment();
        fragment[1] = new PurchaseFragment();

        mCommonNavigatorAdapter.notifyDataSetChanged();
        FragmentManager manager = getChildFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));*/
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * ViewPager某一个页面被选中时回调
     *
     * @param position 当前滑动到的页面
     */
    @Override
    public void onPageSelected(int position) {
        /**
         * 发送数据到{@link MainActivity#onFunctionSwitchChanged(FunctionSwitch)}
         * 使其获得更改后的功能名称
         */
        String string = mClassList.get(position);
        EventBus.getDefault().post(new FunctionSwitch(string));

        switch (position) {
            case 0:
                mEditTextView.setVisibility(View.GONE);
                break;
            case 1:
                mEditTextView.setVisibility(View.VISIBLE);
                /**
                 * 发送数据到 {@link PurCateFragment#onFragmentHiddenChanged(HiddenChanged)}
                 */
                EventBus.getDefault().postSticky(new HiddenChanged(false));
                break;
        }
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
     * 在Fragment隐藏状态发生改变时回调
     *
     * @param hidden true Fragment隐藏,false Fragment显示
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        /**
         * 发送数据到 {@link QuotationFragment#onFragmentHiddenChanged(HiddenChanged)}
         * 发送数据到 {@link PurCateFragment#onFragmentHiddenChanged(HiddenChanged)}
         * 发送数据到 {@link PurchaseFragment#onFragmentHiddenChanged(HiddenChanged)}
         */
        EventBus.getDefault().postSticky(new HiddenChanged(hidden));
    }

    /**
     * EventBus
     * 接收用户从{@link MainActivity#onGetUserInfoSuccess(UserInfo)}
     * 传递过来的数据(在获取用户信息成功时)
     *
     * @param userInfo 用户信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetUserInfoSuccess(UserInfo userInfo) {
        // 员工没有进货功能的
        UserInfo.DataBean data = userInfo.getData();
        if (null == data) {
            return;
        }
        String roleName = data.getRole();
        mClassList.clear();
        mClassList.add(getResources().getString(R.string.quotation));
        if (!TextUtils.isEmpty(roleName) && roleName.equals(SERVICE)) {
            mClassList.add(getResources().getString(R.string.purchase));
        }
        Fragment[] fragment = new Fragment[mClassList.size()];
        fragment[0] = new QuotationFragment();
        if (!TextUtils.isEmpty(roleName) && roleName.equals(SERVICE)) {
            fragment[1] = new PurCateFragment();
        }
        mCommonNavigatorAdapter.notifyDataSetChanged();
        FragmentManager manager = getChildFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
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
        if (EventBus.getDefault().isRegistered(this)) {
            // 解除注册
            EventBus.getDefault().unregister(this);
        }
    }
}
