package com.ybw.yibai.module.savepricing;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.FragmentPagerAdapter;
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
import com.ybw.yibai.module.newuser.NewUserFragment;
import com.ybw.yibai.module.oldcustomer.OldCustomerFragment;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.utils.OtherUtil.transparentStatusBar;

/**
 * 按客户保存报价
 *
 * @author sjl
 * @date 2019/9/16
 */
public class SavePricingByCustomerActivity extends FragmentActivity implements View.OnClickListener {

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
     * 指示器框架标题内容集合
     */
    private List<String> mTitleList;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_pricing_by_customer);
        transparentStatusBar(this);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mView = findViewById(R.id.view);
        mMagicIndicator = findViewById(R.id.magicIndicator);
        mViewPager = findViewById(R.id.viewPager);
        initMagicIndicator();
    }

    private void initData() {
        mTitleList = new ArrayList<>();
        mTitleList.add(getResources().getString(R.string.new_user));
        mTitleList.add(getResources().getString(R.string.old_customers));
        Fragment[] fragment = new Fragment[mTitleList.size()];
        fragment[0] = new NewUserFragment();
        fragment[1] = new OldCustomerFragment();
        mCommonNavigatorAdapter.notifyDataSetChanged();
        FragmentManager manager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
    }

    private void initEvent() {
        // https://www.itcodemonkey.com/article/5148.html
        SlidrConfig build = new SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                // 滑动开始时Activity之间蒙层颜色的透明度,0-1f,默认值0.8f
                .scrimStartAlpha(0.5f)
                // 滑动结束时Activity之间蒙层颜色的透明度,0-1f,默认值0f
                .scrimEndAlpha(0.5f)
                .build();
        if (0 != YiBaiApplication.getCid()) {
            Slidr.attach(this, build);
            mView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.view) {
            onBackPressed();
        }
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
            return mTitleList == null ? 0 : mTitleList.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, int index) {
            SimplePagerTitleView titleView = new ScaleTransitionPagerTitleView(context);
            titleView.setTextSize(16);
            titleView.setText(mTitleList.get(index));
            titleView.setSelectedColor(ContextCompat.getColor(SavePricingByCustomerActivity.this, R.color.main_text_color));
            titleView.setNormalColor(ContextCompat.getColor(SavePricingByCustomerActivity.this, R.color.prompt_high_text_color));
            titleView.setOnClickListener(v -> mViewPager.setCurrentItem(index));
            return titleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
            indicator.setYOffset(Util.dipToPx(context, 2));
            indicator.setStartInterpolator(new AccelerateInterpolator());
            indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
            indicator.setColors(ContextCompat.getColor(SavePricingByCustomerActivity.this, R.color.auxiliary_classification_column_color));
            return indicator;
        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 屏蔽返回键,强制客户选择一个客户
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && 0 == YiBaiApplication.getCid()) {
            // 返回为true,就无法执行父类的 onBackPressed(); 返回
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
