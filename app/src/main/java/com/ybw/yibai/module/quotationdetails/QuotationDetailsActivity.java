package com.ybw.yibai.module.quotationdetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.adapter.FragmentPagerAdapter;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.OtherUtil;
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

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Preferences.POSITION;

/**
 * 报价详情界面
 *
 * @author sjl
 * @date 2019/09/28
 */
public class QuotationDetailsActivity extends BaseActivity implements View.OnClickListener {

    private int position;

    /**
     * 返回
     */
    private ImageView mBackImageView;

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

    /**
     * 上下文对象
     */
    private Context mContext;

    @Override
    protected int setLayout() {
        mContext = this;
        return R.layout.activity_quotation_details;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mMagicIndicator = findViewById(R.id.magicIndicator);
        mViewPager = findViewById(R.id.viewPager);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        position = getIntent().getIntExtra(POSITION, 0);

        mTitleList = new ArrayList<>();
        mTitleList.add(getResources().getString(R.string.all));
        mTitleList.add(getResources().getString(R.string.waiting_for_customer_to_determine));
        mTitleList.add(getResources().getString(R.string.deal_done));
        mTitleList.add(getResources().getString(R.string.expired));

        initMagicIndicator();

        Fragment[] fragment = new Fragment[mTitleList.size()];
        for (int i = 0; i < mTitleList.size(); i++) {
            fragment[i] = new QuotationDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(POSITION, i);
            fragment[i].setArguments(bundle);
        }
        mCommonNavigatorAdapter.notifyDataSetChanged();
        FragmentManager manager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
        mViewPager.setCurrentItem(position);
    }

    @Override
    protected void initEvent() {
        mBackImageView.setOnClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 返回
        if (id == R.id.backImageView) {
            onBackPressed();
        }
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
            return mTitleList == null ? 0 : mTitleList.size();
        }

        @NonNull
        @Override
        public IPagerTitleView getTitleView(Context context, int index) {
            SimplePagerTitleView titleView = new ScaleTransitionPagerTitleView(context);
            titleView.setTextSize(16);
            titleView.setText(mTitleList.get(index));
            titleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.main_text_color));
            titleView.setNormalColor(ContextCompat.getColor(mContext, R.color.prompt_high_text_color));
            titleView.setOnClickListener(v -> mViewPager.setCurrentItem(index));
            return titleView;
        }

        @NonNull
        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
            indicator.setYOffset(Util.dipToPx(context, 2));
            indicator.setStartInterpolator(new AccelerateInterpolator());
            indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
            indicator.setColors(ContextCompat.getColor(mContext, R.color.auxiliary_classification_column_color));
            return indicator;
        }
    };
}
