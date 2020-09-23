package com.ybw.yibai.module.filter;

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

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.FragmentPagerAdapter;
import com.ybw.yibai.common.bean.ProductScreeningParam.DataBean;
import com.ybw.yibai.common.bean.ViewPagerPositions;
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
import com.ybw.yibai.module.home.HomeFragment;
import com.ybw.yibai.module.product.ProductFragment;
import com.ybw.yibai.module.productusestate.ProductUseStateFragment;
import com.ybw.yibai.module.sceneedit.SceneEditFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SCREENING_PARAM;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_TYPE;
import static com.ybw.yibai.common.utils.OtherUtil.createBarView;
import static com.ybw.yibai.common.utils.OtherUtil.setNativeLightStatusBar;
import static com.ybw.yibai.common.utils.OtherUtil.transparentStatusBar;

/**
 * 筛选
 *
 * @author sjl
 * @date 2019/9/9
 */
public class FilterActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "FilterActivity";

    /**
     * ViewPager当前滑动到的页面
     */
    private int position;

    /**
     * 当前选择的是盆器/植物
     */
    private String productType;

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
     * 产品类别集合列表
     */
    private List<String> mClassList;

    /**
     * 产品筛选参数集合
     */
    private List<DataBean> mProductScreeningParamList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        transparentStatusBar(this);
        setNativeLightStatusBar(this, true);

        mContext = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mView = findViewById(R.id.barView);
        mMagicIndicator = findViewById(R.id.magicIndicator);
        mViewPager = findViewById(R.id.viewPager);

        createBarView(this, mView);
        initMagicIndicator();
    }

    private void initData() {
        mClassList = new ArrayList<>();
        mProductScreeningParamList = new ArrayList<>();

        Intent intent = getIntent();
        if (null != intent) {
            position = intent.getIntExtra(POSITION, 0);
            productType = intent.getStringExtra(PRODUCT_TYPE);
        }
        // MyApplication.getProductScreeningParamList()在MainActivity界面已经赋值了
        mProductScreeningParamList.addAll(YiBaiApplication.getProductScreeningParamList());
        if (null == mProductScreeningParamList || mProductScreeningParamList.size() == 0) {
            return;
        }

        if (TextUtils.isEmpty(productType)) {
            /**
             * 说明用户是从{@link ProductFragment}
             * 说明用户是从{@link HomeFragment}
             * 点击进入本界面的（需要显示所有分类）
             */
        } else {
            /**
             * 说明用户是从{@link SceneEditFragment#onClick(View)}
             * 点击进入本界面的（只需要显示单个分类即可）
             */
            // 删除不是productType的数据
            Iterator<DataBean> iterator = mProductScreeningParamList.iterator();
            while (iterator.hasNext()) {
                if (!productType.equals(iterator.next().getCate_code())) {
                    iterator.remove();
                }
            }
        }

        Fragment[] fragment = new Fragment[mProductScreeningParamList.size()];
        if (TextUtils.isEmpty(productType)) {
            for (int i = 0; i < fragment.length; i++) {
                DataBean dataBean = mProductScreeningParamList.get(i);
                mClassList.add(dataBean.getName());
                fragment[i] = new FilterFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(POSITION, i);
                bundle.putParcelable(PRODUCT_SCREENING_PARAM, dataBean);
                fragment[i].setArguments(bundle);
            }
        } else {
            DataBean dataBean = mProductScreeningParamList.get(0);
            mClassList.add(dataBean.getName());
            fragment[0] = new FilterFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(POSITION, position);
            bundle.putParcelable(PRODUCT_SCREENING_PARAM, dataBean);
            fragment[0].setArguments(bundle);
        }

        mCommonNavigatorAdapter.notifyDataSetChanged();
        FragmentManager manager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
        mViewPager.setCurrentItem(position);

        /*Fragment[] fragment = new Fragment[mProductScreeningParamList.size()];
        for (int i = 0; i < fragment.length; i++) {
            DataBean dataBean = mProductScreeningParamList.get(i);
            mClassList.add(dataBean.getName());
            fragment[i] = new FilterFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(POSITION, i);
            bundle.putParcelable(PRODUCT_SCREENING_PARAM, dataBean);
            fragment[i].setArguments(bundle);
        }
        mCommonNavigatorAdapter.notifyDataSetChanged();
        FragmentManager manager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
        mViewPager.setCurrentItem(position);*/
    }

    private void initEvent() {
        // https://www.itcodemonkey.com/article/5148.html
        SlidrConfig build = new SlidrConfig.Builder()
                // 滑动开始时Activity之间蒙层颜色的透明度,0-1f,默认值0.8f
                .scrimStartAlpha(0.5f)
                // 滑动结束时Activity之间蒙层颜色的透明度,0-1f,默认值0f
                .scrimEndAlpha(0.5f)
                // boolean类型,是否设置响应事件的边界,默认是false,没有边界,滑动任何地方都有响应
                .edge(true)
                .build();
        Slidr.attach(this, build);
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * ViewPager某一个页面被选中时回调
     *
     * @param position 当前滑动到的页面
     */
    @Override
    public void onPageSelected(int position) {
        this.position = position;
        /**
         * 发送数据到{@link ProductUseStateFragment#filterActivityViewPagerSelected(ViewPagerPositions)}
         * 通知其界面的ViewPager滑动到相应的页面
         */
        EventBus.getDefault().postSticky(new ViewPagerPositions(position));
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
        CommonNavigator commonNavigator = new CommonNavigator(this);
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
            indicator.setYOffset(Util.dipToPx(context, 5));
            indicator.setStartInterpolator(new AccelerateInterpolator());
            indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
            indicator.setColors(ContextCompat.getColor(mContext, R.color.auxiliary_classification_column_color));
            return indicator;
        }
    };
}
