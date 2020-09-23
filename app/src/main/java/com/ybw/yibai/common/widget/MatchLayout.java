package com.ybw.yibai.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ybw.yibai.R;

/**
 * 自定义植物与盆器互相搭配的View
 *
 * @author sjl
 */
public class MatchLayout extends FrameLayout {

    /**
     * 搭配图片布局的容器
     */
    private RelativeLayout mCollocationLayout;

    /**
     * 放置"植物自由搭配图"
     */
    private HorizontalViewPager mPlantViewPager;

    /**
     * 放置"盆器自由搭配图"
     */
    private HorizontalViewPager mPotViewPager;

    public MatchLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public MatchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.match_layout, this);
        mCollocationLayout = view.findViewById(R.id.collocationLayout);
        mPlantViewPager = view.findViewById(R.id.plantViewPager);
        mPotViewPager = view.findViewById(R.id.potViewPager);
    }

    public RelativeLayout getCollocationLayout() {
        return mCollocationLayout;
    }

    public void setCollocationLayout(RelativeLayout mCollocationLayout) {
        this.mCollocationLayout = mCollocationLayout;
    }

    public HorizontalViewPager getPlantViewPager() {
        return mPlantViewPager;
    }

    public void setPlantViewPager(HorizontalViewPager mPlantViewPager) {
        this.mPlantViewPager = mPlantViewPager;
    }

    public HorizontalViewPager getPotViewPager() {
        return mPotViewPager;
    }

    public void setPotViewPager(HorizontalViewPager mPotViewPager) {
        this.mPotViewPager = mPotViewPager;
    }
}
