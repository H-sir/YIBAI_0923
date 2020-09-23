package com.ybw.yibai.module.search;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.bean.SearchRecord;

import java.util.List;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/9/29
 */
@SuppressWarnings("all")
public interface SearchContract {

    interface SearchView extends BaseView {

        /**
         * 在获取搜索记录成功时回调
         *
         * @param searchRecordList 搜索记录
         */
        void onGetSearchRecordSuccess(List<SearchRecord> searchRecordList);

        /**
         * 在获取产品sku列表成功时回调
         *
         * @param skuList 获取产品sku列表时服务器端返回的数据
         */
        void onGetSKUListSuccess(SKUList skuList);
    }

    interface SearchPresenter extends BasePresenter<SearchView> {

        /**
         * 获取搜索记录
         */
        void getSearchRecord();

        /**
         * 获取产品sku列表
         *
         * @param keyWord 关键词搜索
         */
        void getSKUList(String keyWord);

        /**
         * 显示删除搜索记录的Dialog
         */
        void displayDeleteSearchRecordDialog();
    }

    interface SearchModel {

        /**
         * 获取搜索记录
         *
         * @param callBack 回调方法
         */
        void getSearchRecord(CallBack callBack);

        /**
         * 获取产品sku列表
         *
         * @param keyWord  关键词搜索
         * @param callBack 回调方法
         */
        void getSKUList(String keyWord, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取搜索记录成功时回调
         *
         * @param searchRecordList 搜索记录
         */
        void onGetSearchRecordSuccess(List<SearchRecord> searchRecordList);

        /**
         * 在获取产品sku列表成功时回调
         *
         * @param skuList 获取产品sku列表时服务器端返回的数据
         */
        void onGetSKUListSuccess(SKUList skuList);
    }
}
