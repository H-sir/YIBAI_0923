package com.ybw.yibai.module.hotscheme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.adapter.FragmentPagerAdapter;
import com.ybw.yibai.common.bean.HotScheme.DataBean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.widget.VerticalViewPager;

import java.util.List;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.ybw.yibai.common.constants.Preferences.HOT_SCHEME_INFO;
import static com.ybw.yibai.common.constants.Preferences.HOT_SCHEME_LIST;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.utils.OtherUtil.createBarView;
import static com.ybw.yibai.common.utils.OtherUtil.transparentStatusBar;

/**
 * 热门场景
 *
 * @author sjl
 * @date 2019/11/11
 */
public class HotSchemeActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 用户在首页点击的热门方案在热门方案列表中的位置
     */
    private int position;

    /**
     * 一个和"状态栏"一样高的view
     */
    private View mView;

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 将屏幕旋转90°
     */
    private ImageView mScreenRotationImageView;

    /**
     * 直滚动的ViewPager
     */
    private VerticalViewPager mVerticalViewPager;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 热门方案列表
     */
    private List<ListBean> mHotSchemeList;

    @Override
    protected int setLayout() {
        transparentStatusBar(this);
        return R.layout.activity_hot_scheme;
    }

    @Override
    protected void initView() {
        mView = findViewById(R.id.view);
        mBackImageView = findViewById(R.id.backImageView);
        mScreenRotationImageView = findViewById(R.id.screenRotationImageView);
        mVerticalViewPager = findViewById(R.id.verticalViewPager);
        createBarView(this, mView);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            position = intent.getIntExtra(POSITION, 0);
            mHotSchemeList = intent.getParcelableArrayListExtra(HOT_SCHEME_LIST);
        }
    }

    @Override
    protected void initEvent() {
        mBackImageView.setOnClickListener(this);
        mScreenRotationImageView.setOnClickListener(this);
        if (null == mHotSchemeList || mHotSchemeList.size() == 0) {
            return;
        }
        Fragment[] fragment = new Fragment[mHotSchemeList.size()];
        for (int i = 0; i < mHotSchemeList.size(); i++) {
            ListBean listBean = mHotSchemeList.get(i);
            fragment[i] = new HotSchemeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(POSITION, i);
            bundle.putParcelable(HOT_SCHEME_INFO, listBean);
            fragment[i].setArguments(bundle);
        }
        FragmentManager manager = getSupportFragmentManager();
        mVerticalViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
        mVerticalViewPager.setCurrentItem(position);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    public void onClick(View v) {
        int id = v.getId();

        // 返回
        if (id == R.id.backImageView) {
            onBackPressed();
        }

        // 将屏幕旋转90°
        if (id == R.id.screenRotationImageView) {
            if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
                // 切换为横屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                // 切换为竖屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }
}
