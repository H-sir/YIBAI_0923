package com.ybw.yibai.module.hotschemes;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.RelativeLayout;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.HotSchemes.DataBean.ListBean;
import com.ybw.yibai.common.widget.HorizontalViewPager;
import com.ybw.yibai.common.widget.MatchLayout;

import java.util.List;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/12/2
 */
public interface HotSchemesContract {

    interface HotSchemesView extends BaseView {

        /**
         * 在设置"搭配图片布局的容器"的位置成功时回调 -> 单产品
         *
         * @param viewPagerList 存放动态添加的"ViewPager"
         */
        void onSetSingleCollocationLayoutPositionSucceed(List<HorizontalViewPager> viewPagerList);

        /**
         * 在设置"搭配图片布局的容器"的位置成功时回调 -> 组合
         *
         * @param matchLayoutList 动态添加的"植物与盆器互相搭配的View"列表
         */
        void onSetGroupCollocationLayoutPositionSucceed(List<MatchLayout> matchLayoutList);

        /**
         * 在保存"模拟"成功/失败时回调
         *
         * @param result true成功,false失败
         */
        void onSaveSimulationDataResult(boolean result);
    }

    interface HotSchemesPresenter extends BasePresenter<HotSchemesView> {

        /**
         * 设置竖屏情况下"搭配图片布局的容器"子View的位置
         *
         * @param collocationLayout 搭配图片布局的容器
         * @param hotSchemeInfo     热门场景信息
         * @param comType           组合模式: 1组合,2单产品
         */
        void setPortraitScreenCollocationLayoutPosition(RelativeLayout collocationLayout, ListBean hotSchemeInfo, int comType);

        /**
         * 设置横屏情况下"搭配图片布局的容器"子View的位置
         *
         * @param collocationLayout 搭配图片布局的容器
         * @param hotSchemeInfo     热门场景信息
         * @param comType           组合模式: 1组合,2单产品
         */
        void setLandscapeScreenCollocationLayoutPosition(RelativeLayout collocationLayout, ListBean hotSchemeInfo, int comType);

        /**
         * 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
         *
         * @param matchLayout    植物与盆器互相搭配的View
         * @param plantViewPager 放置"植物自由搭配图"的ViewPager
         * @param potViewPager   放置"盆器自由搭配图"的ViewPager
         * @param plantBean      用户当前选择的植物信息
         * @param potBean        用户当前选择的盆器信息
         */
        void setCollocationContentParams(MatchLayout matchLayout,
                                         HorizontalViewPager plantViewPager, HorizontalViewPager potViewPager,
                                         com.ybw.yibai.common.bean.ListBean plantBean,
                                         com.ybw.yibai.common.bean.ListBean potBean);

        /**
         * 显示显示产品代码信息的PopupWindow
         *
         * @param v         锚点
         * @param plantBean 用户当前选择的植物信息
         * @param potBean   用户当前选择的盆器信息
         * @param comType   组合模式: 1组合,2单产品
         */
        void displayProductCodePopupWindow(View v, com.ybw.yibai.common.bean.ListBean plantBean,
                                           com.ybw.yibai.common.bean.ListBean potBean, int comType);

        /**
         * 保存到模拟列表数据
         *
         * @param plantBean 用户当前选择的植物信息
         */
        void saveSimulationData(com.ybw.yibai.common.bean.ListBean plantBean);

        /**
         * 保存到模拟列表数据
         *
         * @param plantBean 用户当前选择的植物信息
         * @param potBean   用户当前选择的盆器信息
         */
        void saveSimulationData(com.ybw.yibai.common.bean.ListBean plantBean,
                                com.ybw.yibai.common.bean.ListBean potBean);
    }

    interface HotSchemesModel {

        /**
         * 保存到模拟列表数据
         *
         * @param plantBean 用户当前选择的植物信息
         * @param bitmap    植物位图
         * @param callBack  回调方法
         */
        void saveSimulationData(com.ybw.yibai.common.bean.ListBean plantBean,
                                Bitmap bitmap, CallBack callBack);

        /**
         * 保存到模拟列表数据
         *
         * @param plantBean 用户当前选择的植物信息
         * @param potBean   用户当前选择的盆器信息
         * @param bitmap    植物与盆栽合成后的位图
         * @param callBack  回调方法
         */
        void saveSimulationData(com.ybw.yibai.common.bean.ListBean plantBean,
                                com.ybw.yibai.common.bean.ListBean potBean,
                                Bitmap bitmap, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在保存"模拟"成功/失败时回调
         *
         * @param result true成功,false失败
         */
        void onSaveSimulationDataResult(boolean result);
    }
}
