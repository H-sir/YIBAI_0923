package com.ybw.yibai.module.picturecase;

import android.content.Context;
import android.os.Bundle;
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
import com.ybw.yibai.common.bean.CaseClassify;
import com.ybw.yibai.common.bean.CaseClassify.DataBean;
import com.ybw.yibai.common.bean.CaseClassify.DataBean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.OtherUtil;
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
import com.ybw.yibai.module.cases.CaseFragment;
import com.ybw.yibai.module.picturecase.PictureCaseContract.PictureCasePresenter;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.PROJECT_CLASSIFY_INFO;

/**
 * 图片案例
 *
 * @author sjl
 * @date 2019/11/09
 */
public class PictureCaseActivity extends BaseActivity implements PictureCaseContract.PictureCaseView, View.OnClickListener {

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
    private PictureCasePresenter mPictureCasePresenter;

    @Override
    protected int setLayout() {
        mContext = this;
        return R.layout.activity_picture_case;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mMagicIndicator = findViewById(R.id.magicIndicator);
        mViewPager = findViewById(R.id.viewPager);
        mWaitDialog = new WaitDialog(this);

        initMagicIndicator();
        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mTitleList = new ArrayList<>();
    }

    @Override
    protected void initEvent() {
        mPictureCasePresenter = new PictureCasePresenterImpl(this);
        mPictureCasePresenter.getCaseClassify();
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
            titleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.main_text_color));
            titleView.setNormalColor(ContextCompat.getColor(mContext, R.color.prompt_high_text_color));
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
            indicator.setColors(ContextCompat.getColor(mContext, R.color.auxiliary_classification_column_color));
            return indicator;
        }
    };

    /**
     * 在获取案例分类成功时回调
     *
     * @param caseClassify 案例分类
     */
    @Override
    public void onGetCaseClassifySuccess(CaseClassify caseClassify) {
        if (CODE_SUCCEED != caseClassify.getCode()) {
            return;
        }
        DataBean data = caseClassify.getData();
        if (null == data) {
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        Fragment[] fragment = new Fragment[list.size()];
        for (int i = 0; i < list.size(); i++) {
            CaseClassify.DataBean.ListBean listBean = list.get(i);
            mTitleList.add(listBean.getName());
            fragment[i] = new CaseFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(PROJECT_CLASSIFY_INFO, listBean);
            fragment[i].setArguments(bundle);
        }
        mCommonNavigatorAdapter.notifyDataSetChanged();
        FragmentManager manager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPictureCasePresenter) {
            mPictureCasePresenter.onDetachView();
            mPictureCasePresenter = null;
        }
    }
}
