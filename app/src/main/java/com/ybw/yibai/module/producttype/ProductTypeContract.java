package com.ybw.yibai.module.producttype;

import android.graphics.Bitmap;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.SKUList;

import java.util.Map;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/5/16
 */
public interface ProductTypeContract {

    interface ProductTypeView extends BaseView {

        /**
         * 在获取产品sku列表成功时回调
         *
         * @param skuList 获取产品sku列表时服务器端返回的数据
         */
        void onGetSKUListSuccess(SKUList skuList);

        /**
         * 在保存"模拟"成功/失败时回调
         *
         * @param result true成功,false失败
         */
        void onSaveSimulationResult(boolean result);
    }

    interface ProductTypePresenter extends BasePresenter<ProductTypeView> {

        /**
         * 获取产品sku列表
         *
         * @param isAll    是否获取全部:1获取全部0分页(默认为0分页)
         * @param useState 使用状态
         * @param cateCode 产品类别(产品筛选参数大类别名)默认获取植物
         * @param pcId     产品类别id(多个用逗号拼接)
         * @param keyWord  搜索关键词
         * @param param    动态设置的参数
         */
        void getSKUList(int isAll, int useState, String cateCode, String pcId, String keyWord, Map<String, String> param);

        /**
         * 保存到模拟列表数据
         *
         * @param plantBean 用户当前选择的植物信息
         */
        void saveSimulation(ListBean plantBean);
    }

    interface ProductTypeModel {

        /**
         * 获取产品sku列表
         *
         * @param isAll    是否获取全部:1获取全部0分页(默认为0分页)
         * @param useState 使用状态
         * @param cateCode 产品类别(产品筛选参数大类别名)默认获取植物
         * @param pcId     产品类别id(多个用逗号拼接)
         * @param keyWord  搜索关键词
         * @param param    动态设置的参数
         * @param callBack 回调方法
         */
        void getSKUList(int isAll, int useState, String cateCode, String pcId, String keyWord, Map<String, String> param, CallBack callBack);

        /**
         * 保存到模拟列表数据
         *
         * @param plantBean 用户当前选择的植物信息
         * @param bitmap    植物位图
         * @param callBack  回调方法
         */
        void saveSimulation(ListBean plantBean, Bitmap bitmap, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取产品sku列表成功时回调
         *
         * @param skuList 获取产品sku列表时服务器端返回的数据
         */
        void onGetSKUListSuccess(SKUList skuList);

        /**
         * 在保存"模拟"成功/失败时回调
         *
         * @param result true成功,false失败
         */
        void onSaveSimulationResult(boolean result);
    }
}
