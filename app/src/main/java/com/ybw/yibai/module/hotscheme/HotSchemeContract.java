package com.ybw.yibai.module.hotscheme;

import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.HotScheme;
import com.ybw.yibai.common.bean.ListBean;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/11/11
 */
public interface HotSchemeContract {

    interface HotSchemeView extends BaseView {

        /**
         * 在设置"搭配图片布局的容器"的位置成功时回调
         */
        void onSetCollocationLayoutPositionSucceed();

        /**
         * 在保存"模拟"成功/失败时回调
         *
         * @param result true成功,false失败
         */
        void onSaveSimulationDataResult(boolean result);
    }

    interface HotSchemePresenter extends BasePresenter<HotSchemeView> {

        /**
         * 设置竖屏情况下"搭配图片布局的容器"的位置
         *
         * @param collocationLayout 搭配图片布局的容器
         * @param hotSchemeInfo     热门场景信息
         */
        void setPortraitScreenCollocationLayoutPosition(RelativeLayout collocationLayout, HotScheme.DataBean.ListBean hotSchemeInfo);

        /**
         * 设置横屏情况下"搭配图片布局的容器"的位置
         *
         * @param collocationLayout 搭配图片布局的容器
         * @param hotSchemeInfo     热门场景信息
         */
        void setLandscapeScreenCollocationLayoutPosition(RelativeLayout collocationLayout, HotScheme.DataBean.ListBean hotSchemeInfo);

        /**
         * 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
         *
         * @param collocationLayout 搭配图片布局的容器
         * @param plantViewPager    放置"植物自由搭配图"的ViewPager
         * @param potViewPager      放置"盆器自由搭配图"的ViewPager
         * @param plantBean         用户当前选择的植物信息
         * @param potBean           用户当前选择的盆器信息
         */
        void setCollocationContentParams(RelativeLayout collocationLayout,
                                         ViewPager plantViewPager, ViewPager potViewPager,
                                         ListBean plantBean, ListBean potBean);

        /**
         * 显示显示产品代码信息的PopupWindow
         *
         * @param v                       锚点
         * @param productPriceCode        主产品售价代码
         * @param productTradePriceCode   主产品批发价代码
         * @param augmentedPriceCode      附加产品售价代码
         * @param augmentedTradePriceCode 附加产品批发价代码
         */
        void displayProductCodePopupWindow(View v, String productPriceCode, String productTradePriceCode,
                                           String augmentedPriceCode, String augmentedTradePriceCode);

        /**
         * 添加到模拟列表
         *
         * @param plantBean 用户当前选择的植物信息
         * @param potBean   用户当前选择的盆器信息
         */
        void saveSimulationData(ListBean plantBean, ListBean potBean);
    }

    interface HotSchemeModel {

        /**
         * 保存到模拟列表数据
         *
         * @param plantBean 用户当前选择的植物信息
         * @param potBean   用户当前选择的盆器信息
         * @param bitmap    植物与盆栽合成后的位图
         * @param callBack  回调方法
         */
        void saveSimulationData(ListBean plantBean, ListBean potBean, Bitmap bitmap, CallBack callBack);
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
