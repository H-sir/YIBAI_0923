package com.ybw.yibai.module.hotschemes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.ybw.yibai.common.bean.HotSchemes;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.DisplayUpdateVipPopupWindowUtil;
import com.ybw.yibai.common.utils.GuideUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.widget.HorizontalViewPager;
import com.ybw.yibai.common.widget.HorizontalViewPager.OnPageChangeListener;
import com.ybw.yibai.common.widget.MatchLayout;
import com.ybw.yibai.module.hotschemes.HotSchemesContract.HotSchemesPresenter;
import com.ybw.yibai.module.hotschemes.HotSchemesContract.HotSchemesView;
import com.ybw.yibai.module.scene.SceneActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.ybw.yibai.common.constants.Preferences.HOT_SCHEME_INFO;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.VIP_LEVEL;

/**
 * 新热门场景
 *
 * @author sjl
 * @date 2019/12/2
 */
public class HotSchemesFragment extends BaseFragment implements HotSchemesView, View.OnClickListener {

    private String TAG = "HotSchemesFragment";

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 当前被选中植物在"植物列表"中的位置
     */
    private int plantPosition;

    /**
     * 当前被选中盆器在"盆器列表"中的位置
     */
    private int potPosition;

    /**
     * 组合模式: 1组合,2单产品
     */
    private int comType;

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
    private View mRootView;
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
    private HotSchemes.DataBean.ListBean mHotSchemeInfo;

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
     * 显示植物列表的ViewPager的集合
     */
    private List<HorizontalViewPager> mPlantViewPagerList;

    /**
     * 显示盆器列表的ViewPager的集合
     */
    private List<HorizontalViewPager> mPotViewPagerList;

    /**
     * 显示植物列表的ViewPager其侦听器的集合
     */
    private List<OnPageChangeListener> mOnPlantPageChangeListenerList;

    /**
     * 显示盆器列表的ViewPager其侦听器的集合
     */
    private List<OnPageChangeListener> mOnPotPageChangeListenerList;

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
    private HotSchemesPresenter mHotSchemesPresenter;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        mActivity = getActivity();
        return R.layout.fragment_hot_schemes;
    }

    @Override
    protected void findViews(View view) {
        mRootView = view.findViewById(R.id.mRootView);
        mSceneBackgroundImageView = view.findViewById(R.id.sceneBackgroundImageView);
        mCollocationLayout = view.findViewById(R.id.collocationLayout);
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
        mPlantViewPagerList = new ArrayList<>();
        mPotViewPagerList = new ArrayList<>();
        mOnPlantPageChangeListenerList = new ArrayList<>();
        mOnPotPageChangeListenerList = new ArrayList<>();

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
        if (null != mPlantList && mPlantList.size() > 0) {
            mPlantBean = mPlantList.get(0);
        }
        if (null != mPotList && mPotList.size() > 0) {
            mPotBean = mPotList.get(0);
        }
        comType = mHotSchemeInfo.getComtype();
        sceneBackgroundUrl = mHotSchemeInfo.getBg_pic();
    }

    @Override
    protected void initEvent() {
        mHotSchemesPresenter = new HotSchemesPresenterImpl(this);
        if (!TextUtils.isEmpty(sceneBackgroundUrl)) {
            ImageUtil.displayImage(mContext, mSceneBackgroundImageView, sceneBackgroundUrl);
        }
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            // 横屏
            mHotSchemesPresenter.setLandscapeScreenCollocationLayoutPosition(mCollocationLayout, mHotSchemeInfo, comType);
        } else {
            // 竖屏
            mHotSchemesPresenter.setPortraitScreenCollocationLayoutPosition(mCollocationLayout, mHotSchemeInfo, comType);
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
            mHotSchemesPresenter.setLandscapeScreenCollocationLayoutPosition(mCollocationLayout, mHotSchemeInfo, comType);
        } else {
            // 竖屏
            mHotSchemesPresenter.setPortraitScreenCollocationLayoutPosition(mCollocationLayout, mHotSchemeInfo, comType);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 查看产品代码的图标
        if (id == R.id.productCodeImageButton) {
            mHotSchemesPresenter.displayProductCodePopupWindow(mProductCodeImageButton, mPlantBean, mPotBean, comType);
        }

        // 场景预览
        if (id == R.id.scenePreviewButton) {
            SharedPreferences preferences = getActivity().getSharedPreferences(USER_INFO, MODE_PRIVATE);
            int vipLevel = preferences.getInt(VIP_LEVEL, 0);
            if (1 == vipLevel) {
                DisplayUpdateVipPopupWindowUtil.displayUpdateVipPopupWindow(getActivity(), mRootView);
                return;
            }

            if (1 == comType) {
                mHotSchemesPresenter.saveSimulationData(mPlantBean, mPotBean);
            } else {
                mHotSchemesPresenter.saveSimulationData(mPlantBean);
            }
        }
    }

    /**
     * 在设置"搭配图片布局的容器"的位置成功时回调 -> 单产品
     *
     * @param viewPagerList 存放动态添加的"ViewPager"
     */
    @Override
    public void onSetSingleCollocationLayoutPositionSucceed(@NonNull List<HorizontalViewPager> viewPagerList) {
        mPlantViewPagerList.clear();
        mOnPlantPageChangeListenerList.clear();

        for (int i = 0; i < viewPagerList.size(); i++) {
            HorizontalViewPager viewPager = viewPagerList.get(i);
            PlantSelectAdapter plantSelectAdapter = new PlantSelectAdapter(mContext, mPlantList);
            viewPager.setAdapter(plantSelectAdapter);
            mPlantViewPagerList.add(viewPager);

            int index = i;
            OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    onPlantPageSelected(position, index);
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };

            viewPager.addOnPageChangeListener(onPageChangeListener);
            onPageChangeListener.onPageSelected(plantPosition);
            viewPager.setCurrentItem(plantPosition);
            mOnPlantPageChangeListenerList.add(onPageChangeListener);
        }

        mProductCodeImageButton.setVisibility(View.VISIBLE);
        mScenePreviewButton.setVisibility(View.VISIBLE);
        GuideUtil.showGuideView(this, R.layout.guide_hot_scheme_layout);
    }

    /**
     * 在设置"搭配图片布局的容器"的位置成功时回调 -> 组合
     *
     * @param matchLayoutList 动态添加的"植物与盆器互相搭配的View"列表
     */
    @Override
    public void onSetGroupCollocationLayoutPositionSucceed(@NonNull List<MatchLayout> matchLayoutList) {
        mPlantViewPagerList.clear();
        mPotViewPagerList.clear();
        mOnPlantPageChangeListenerList.clear();
        mOnPotPageChangeListenerList.clear();

        for (int i = 0; i < matchLayoutList.size(); i++) {
            MatchLayout matchLayout = matchLayoutList.get(i);
            HorizontalViewPager plantViewPager = matchLayout.getPlantViewPager();
            HorizontalViewPager potViewPager = matchLayout.getPotViewPager();
            mPlantViewPagerList.add(plantViewPager);
            mPotViewPagerList.add(potViewPager);

            PlantSelectAdapter plantSelectAdapter = new PlantSelectAdapter(mContext, mPlantList);
            plantViewPager.setAdapter(plantSelectAdapter);
            PotSelectAdapter potSelectAdapter = new PotSelectAdapter(mContext, mPotList);
            potViewPager.setAdapter(potSelectAdapter);

            int index = i;
            OnPageChangeListener onPlantPageChangeListener = new OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    onPlantPageSelected(position, index);
                    mHotSchemesPresenter.setCollocationContentParams(matchLayout,
                            plantViewPager, potViewPager, mPlantBean, mPotBean);
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };
            OnPageChangeListener onPotPageChangeListener = new OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    onPotPageSelected(position, index);
                    mHotSchemesPresenter.setCollocationContentParams(matchLayout,
                            plantViewPager, potViewPager, mPlantBean, mPotBean);
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };

            plantViewPager.addOnPageChangeListener(onPlantPageChangeListener);
            potViewPager.addOnPageChangeListener(onPotPageChangeListener);
            onPlantPageChangeListener.onPageSelected(plantPosition);
            onPotPageChangeListener.onPageSelected(potPosition);
            plantViewPager.setCurrentItem(plantPosition);
            potViewPager.setCurrentItem(potPosition);
            mOnPlantPageChangeListenerList.add(onPlantPageChangeListener);
            mOnPotPageChangeListenerList.add(onPotPageChangeListener);
        }

        mProductCodeImageButton.setVisibility(View.VISIBLE);
        mScenePreviewButton.setVisibility(View.VISIBLE);
        GuideUtil.showGuideView(this, R.layout.guide_hot_scheme_layout);
    }

    private void onPlantPageSelected(int position, int index) {
        plantPosition = position;
        if (null != mPlantBean) {
            mPlantBean = mPlantList.get(position);
        }
        if (null == mOnPlantPageChangeListenerList || mOnPlantPageChangeListenerList.size() == 0) {
            return;
        }
        for (int j = 0; j < mOnPlantPageChangeListenerList.size(); j++) {
            // 找出当前没有滑动的ViewPager,将其当前的位置设置为和当前滑动的ViewPager一样的位置
            if (index == j) {
                continue;
            }
            mPlantViewPagerList.get(j).setCurrentItem(position);
        }
    }

    private void onPotPageSelected(int position, int index) {
        potPosition = position;
        if (null != mPotBean) {
            mPotBean = mPotList.get(position);
        }
        if (null == mOnPotPageChangeListenerList || mOnPotPageChangeListenerList.size() == 0) {
            return;
        }
        for (int j = 0; j < mOnPotPageChangeListenerList.size(); j++) {
            // 找出当前没有滑动的ViewPager,将其当前的位置设置为和当前滑动的ViewPager一样的位置
            if (index == j) {
                continue;
            }
            mPotViewPagerList.get(j).setCurrentItem(position);
        }
    }

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
        if (null != mHotSchemesPresenter) {
            mHotSchemesPresenter.onDetachView();
            mHotSchemesPresenter = null;
        }
    }
}
