package com.ybw.yibai.module.hotscheme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.PlantSelectAdapter;
import com.ybw.yibai.common.adapter.PotSelectAdapter;
import com.ybw.yibai.common.bean.HotScheme;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.GuideUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.module.hotscheme.HotSchemeContract.HotSchemePresenter;
import com.ybw.yibai.module.hotscheme.HotSchemeContract.HotSchemeView;
import com.ybw.yibai.module.scene.SceneActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.ybw.yibai.common.constants.Preferences.HOT_SCHEME_INFO;

/**
 * 热门场景
 *
 * @author sjl
 * @date 2019/11/11
 */
public class HotSchemeFragment extends BaseFragment implements HotSchemeView, View.OnClickListener {

    private final String TAG = "HotSchemeFragment";

    /**
     * 场景背景图片地址
     */
    private String sceneBackgroundUrl;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 场景背景图片
     */
    private ImageView mSceneBackgroundImageView;

    /**
     * 搭配图片布局的容器
     */
    private RelativeLayout mCollocationLayout;

    /**
     * 放置"植物自由搭配图"
     */
    private ViewPager mPlantViewPager;

    /**
     * 放置"盆器自由搭配图"
     */
    private ViewPager mPotViewPager;

    /**
     * 查看产品代码的图标
     */
    private ImageButton mProductCodeImageButton;

    /**
     * 场景预览
     */
    private Button mScenePreviewButton;

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
     * 热门场景信息
     */
    private HotScheme.DataBean.ListBean mHotSchemeInfo;

    /**
     * 用户当前选择的植物信息
     */
    private ListBean mPlantBean;

    /**
     * 用户当前选择的盆器信息
     */
    private ListBean mPotBean;

    /**
     * 植物列表
     */
    private List<ListBean> mPlantList;

    /**
     * 盆器列表
     */
    private List<ListBean> mPotList;

    /**
     * 植物选择适配器
     */
    private PlantSelectAdapter mPlantSelectAdapter;

    /**
     * 盆器选择适配器
     */
    private PotSelectAdapter mPotSelectAdapter;

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
    private HotSchemePresenter mHotSchemePresenter;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        mActivity = getActivity();
        return R.layout.fragment_hot_scheme;
    }

    @Override
    protected void findViews(View view) {
        mSceneBackgroundImageView = view.findViewById(R.id.sceneBackgroundImageView);
        mCollocationLayout = view.findViewById(R.id.collocationLayout);
        mPlantViewPager = view.findViewById(R.id.plantViewPager);
        mPotViewPager = view.findViewById(R.id.potViewPager);
        mProductCodeImageButton = view.findViewById(R.id.productCodeImageButton);
        mScenePreviewButton = view.findViewById(R.id.scenePreviewButton);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mPlantList = new ArrayList<>();
        mPotList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }
        mHotSchemeInfo = bundle.getParcelable(HOT_SCHEME_INFO);
        if (null == mHotSchemeInfo) {
            return;
        }

        mPlantList = mHotSchemeInfo.getPlant();
        mPotList = mHotSchemeInfo.getPot();
        // 默认选中第一个
        mPlantBean = mPlantList.get(0);
        mPotBean = mPotList.get(0);
        sceneBackgroundUrl = mHotSchemeInfo.getBg_pic();
    }

    @Override
    protected void initEvent() {
        mHotSchemePresenter = new HotSchemePresenterImpl(this);
        if (!TextUtils.isEmpty(sceneBackgroundUrl)) {
            ImageUtil.displayImage(mContext, mSceneBackgroundImageView, sceneBackgroundUrl);
        }
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            // 横屏
            mHotSchemePresenter.setLandscapeScreenCollocationLayoutPosition(mCollocationLayout, mHotSchemeInfo);
        } else {
            // 竖屏
            mHotSchemePresenter.setPortraitScreenCollocationLayoutPosition(mCollocationLayout, mHotSchemeInfo);
        }
        mProductCodeImageButton.setOnClickListener(this);
        mScenePreviewButton.setOnClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ORIENTATION_LANDSCAPE) {
            // 横屏
            mHotSchemePresenter.setLandscapeScreenCollocationLayoutPosition(mCollocationLayout, mHotSchemeInfo);
        } else {
            // 竖屏
            mHotSchemePresenter.setPortraitScreenCollocationLayoutPosition(mCollocationLayout, mHotSchemeInfo);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 查看产品代码的图标
        if (id == R.id.productCodeImageButton) {
            String productPriceCode = mPlantBean.getPrice_code();
            String productTradePriceCode = mPlantBean.getTrade_price_code();
            String augmentedPriceCode = mPotBean.getPrice_code();
            String augmentedTradePriceCode = mPotBean.getPrice_code();
            mHotSchemePresenter.displayProductCodePopupWindow(mProductCodeImageButton, productPriceCode,
                    productTradePriceCode, augmentedPriceCode, augmentedTradePriceCode);
        }

        // 场景预览
        if (id == R.id.scenePreviewButton) {
            mHotSchemePresenter.saveSimulationData(mPlantBean, mPotBean);
        }
    }

    /**
     * 在设置"搭配图片布局的容器"的位置成功时回调
     */
    @Override
    public void onSetCollocationLayoutPositionSucceed() {
        mProductCodeImageButton.setVisibility(View.VISIBLE);
        mScenePreviewButton.setVisibility(View.VISIBLE);

        mPlantSelectAdapter = new PlantSelectAdapter(mContext, mPlantList);
        mPlantViewPager.setAdapter(mPlantSelectAdapter);
        mPotSelectAdapter = new PotSelectAdapter(mContext, mPotList);
        mPotViewPager.setAdapter(mPotSelectAdapter);

        mPlantViewPager.addOnPageChangeListener(mOnPlantPageChangeListener);
        mPotViewPager.addOnPageChangeListener(mOnPotPageChangeListener);

        mOnPotPageChangeListener.onPageSelected(0);
        mOnPotPageChangeListener.onPageSelected(0);

        mPlantViewPager.setCurrentItem(0);
        mPotViewPager.setCurrentItem(0);

        GuideUtil.showGuideView(this, R.layout.guide_hot_scheme_layout);
    }

    /**
     * 显示"植物自由搭配图"ViewPager的Listener
     */
    private OnPageChangeListener mOnPlantPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mPlantBean = mPlantList.get(position);
            mHotSchemePresenter.setCollocationContentParams(mCollocationLayout, mPlantViewPager, mPotViewPager, mPlantBean, mPotBean);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 显示"盆器自由搭配图"ViewPager的Listener
     */
    private OnPageChangeListener mOnPotPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mPotBean = mPotList.get(position);
            mHotSchemePresenter.setCollocationContentParams(mCollocationLayout, mPlantViewPager, mPotViewPager, mPlantBean, mPotBean);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 在保存"模拟"成功/失败时回调
     *
     * @param result true成功,false失败
     */
    @Override
    public void onSaveSimulationDataResult(boolean result) {
        if (result) {
            MessageUtil.showMessage(getResources().getString(R.string.added_to_the_simulation_scene_successfully));
            Intent intent = new Intent(mContext, SceneActivity.class);
            startActivity(intent);
            mActivity.finish();
        } else {
            MessageUtil.showMessage(getResources().getString(R.string.added_to_the_simulation_scene_failed));
        }
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
        if (null != mHotSchemePresenter) {
            mHotSchemePresenter.onDetachView();
            mHotSchemePresenter = null;
        }
    }
}
