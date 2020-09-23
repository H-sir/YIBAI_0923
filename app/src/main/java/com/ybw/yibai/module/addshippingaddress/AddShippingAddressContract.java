package com.ybw.yibai.module.addshippingaddress;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.CityAreaList;
import com.ybw.yibai.common.bean.DeleteShippingAddress;
import com.ybw.yibai.common.bean.ShippingAddress;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/10/11
 */
public interface AddShippingAddressContract {

    interface AddShippingAddressView extends BaseView {

        /**
         * 在获取城市区域列表成功时回调
         *
         * @param cityAreaList 城市区域列表数据
         */
        void onGetCityAreaListSucceed(CityAreaList cityAreaList);

        /**
         * 返回用户选择的区域Id和名称
         *
         * @param provinceId   省Id
         * @param cityId       市Id
         * @param areaId       区Id
         * @param provinceName 省名称
         * @param cityName     市名称
         * @param areaName     区名称
         */
        void returnUserSelectArea(int provinceId, int cityId, int areaId, String provinceName, String cityName, String areaName);

        /**
         * 在创建/修改收货地址成功时回调
         *
         * @param shippingAddress 创建或修改收货地址
         */
        void onCreateOrModifyShippingAddressSucceed(ShippingAddress shippingAddress);

        /**
         * 在删除收货地址成功时回调
         *
         * @param deleteShippingAddress 删除收货地址时服务器端返回的数据
         */
        void onDeleteShippingAddressSucceed(DeleteShippingAddress deleteShippingAddress);
    }

    interface AddShippingAddressPresenter extends BasePresenter<AddShippingAddressView> {

        /**
         * 获取城市区域列表
         */
        void getCityAreaList();

        /**
         * 初始化区域View
         */
        void initAreaSelectView();

        /**
         * 创建/修改收货地址
         *
         * @param addressId 收货地址id(有此字段表示修改)
         * @param areaId    区id
         * @param consignee 收货人
         * @param mobile    联系电话
         * @param address   详细地址
         * @param isDefault 是否默认(1是0否)
         */
        void createOrModifyShippingAddress(Integer addressId, int areaId, String consignee, String mobile, String address, int isDefault);

        /**
         * 删除收货地址
         *
         * @param addressId 收货地址id
         */
        void deleteShippingAddress(int addressId);
    }

    interface AddShippingAddressModel {

        /**
         * 获取城市区域列表
         *
         * @param callBack 回调方法
         */
        void getCityAreaList(CallBack callBack);

        /**
         * 创建/修改收货地址
         *
         * @param addressId 收货地址id(有此字段表示修改)
         * @param areaId    区id
         * @param consignee 收货人
         * @param mobile    联系电话
         * @param address   详细地址
         * @param isDefault 是否默认(1是0否)
         * @param callBack  回调方法
         */
        void createOrModifyShippingAddress(Integer addressId, int areaId, String consignee, String mobile, String address, int isDefault, CallBack callBack);

        /**
         * 删除收货地址
         *
         * @param addressId 收货地址id
         * @param callBack  回调方法
         */
        void deleteShippingAddress(int addressId, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取城市区域列表成功时回调
         *
         * @param cityAreaList 城市区域列表数据
         */
        void onGetCityAreaListSucceed(CityAreaList cityAreaList);

        /**
         * 在创建/修改收货地址成功时回调
         *
         * @param shippingAddress 创建或修改收货地址
         */
        void onCreateOrModifyShippingAddressSucceed(ShippingAddress shippingAddress);

        /**
         * 在删除收货地址成功时回调
         *
         * @param deleteShippingAddress 删除收货地址时服务器端返回的数据
         */
        void onDeleteShippingAddressSucceed(DeleteShippingAddress deleteShippingAddress);
    }
}
