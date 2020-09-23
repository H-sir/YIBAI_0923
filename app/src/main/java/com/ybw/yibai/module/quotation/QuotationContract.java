package com.ybw.yibai.module.quotation;

import android.view.View;
import android.widget.RadioGroup;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.AddQuotationLocation;
import com.ybw.yibai.common.bean.CreateQuotationLocation;
import com.ybw.yibai.common.bean.CreateQuotationOrder;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.bean.DeleteQuotationLocation;
import com.ybw.yibai.common.bean.PlacementQrQuotationList;
import com.ybw.yibai.common.bean.QuotationLocation;
import com.ybw.yibai.common.bean.UpdateQuotationInfo;
import com.ybw.yibai.common.bean.UpdateQuotationLocation;
import com.ybw.yibai.common.bean.UpdateQuotationPrice;
import com.ybw.yibai.common.bean.VerifyPassword;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/10/30
 */
public interface QuotationContract {

    interface QuotationView extends BaseView {

        /**
         * 在获取报价位置明细列表成功时回调
         *
         * @param quotationLocation 报价位置明细列表
         */
        void onGetQuotationLocationSuccess(QuotationLocation quotationLocation);

        /**
         * 在获取摆放清单列表成功时回调
         *
         * @param placementQrQuotationList 摆放清单列表
         */
        void onGetPlacementListSuccess(PlacementQrQuotationList placementQrQuotationList);

        /**
         * 在获取报价清单列表成功时回调
         *
         * @param placementQrQuotationList 报价清单列表
         */
        void onGetQuotationListSuccess(PlacementQrQuotationList placementQrQuotationList);

        /**
         * 在创建报价位置成功时回调
         *
         * @param createQuotationLocation 创建报价位置时服务器端返回的数据
         */
        void onCreateQuotationLocationSuccess(CreateQuotationLocation createQuotationLocation);

        /**
         * 在删除报价位置成功时回调
         *
         * @param deleteQuotationLocation 删除报价位置信息时服务器端返回的数据
         */
        void onDeleteQuotationLocationSuccess(DeleteQuotationLocation deleteQuotationLocation);

        /**
         * 在修改报价位置信息成功时回调
         *
         * @param updateQuotationLocation 修改报价位置信息时服务器端返回的数据
         */
        void onUpdateQuotationLocationSuccess(UpdateQuotationLocation updateQuotationLocation);

        /**
         * 在修改报价位置中产品数量成功时回调
         *
         * @param updateQuotationInfo 修改报价位置中产品数量时服务器端返回的数据
         */
        void onUpdateQuotationInfoSuccess(UpdateQuotationInfo updateQuotationInfo);

        /**
         * 在将待摆放清单加入到报价位置成功时回调
         *
         * @param addQuotationLocation 待摆放清单加入到报价位置时服务器端返回的数据
         */
        void onAddQuotationLocationSuccess(AddQuotationLocation addQuotationLocation);

        /**
         * 在创建报价单成功时回调
         *
         * @param createQuotationOrder 创建报价单时服务器端返回的数据
         */
        void onCreateQuotationOrderSuccess(CreateQuotationOrder createQuotationOrder);

        /**
         * 在计算报价清单里面盆栽的总价格成功时回调
         *
         * @param totalPrice 总价格
         */
        void onCalculatedTotalPriceSuccess(String totalPrice);

        /**
         * 返回修改后的场景名称时回调
         *
         * @param newSceneName 新的场景名称
         */
        void returnNewSceneName(String newSceneName);

        /**
         * 在PopupWindow里面的RadioButton被选中时回调
         *
         * @param group     被点击的RadioGroup
         * @param checkedId 被选中的RadioButton ID
         */
        void onCheckedChanged(RadioGroup group, int checkedId);

        /**
         * 在获取税率成功时回调
         *
         * @param taxRate 税率
         */
        void onGetTaxRateSuccess(double taxRate);

        /**
         * 在获取优惠价格成功时回调
         *
         * @param discountMoney 优惠价格
         */
        void onGetDiscountMoneySuccess(double discountMoney);

        /**
         * 在显示"清单及价格"RecyclerView Item 中的"价格"点击时回调
         *
         * @param position 被点击的Item位置
         * @param mode     报价方式
         * @param price    被点击的Item位置的价格
         */
        void onPriceClick(int position, int mode, double price);

        /**
         * 在验证修改价格密码成功时回调
         *
         * @param verifyPassword 验证修改价格密码时服务器端返回的数据
         */
        void onVerifyPasswordSuccess(VerifyPassword verifyPassword);

        /**
         * 返回修改后的新价格
         *
         * @param newPrice 改后的新价格
         */
        void returnNewPrice(double newPrice);

        /**
         * 在修改清单产品价格成功时回调
         *
         * @param updateQuotationPrice 修改清单产品价格时服务器端返回的数据
         */
        void onUpdateQuotationPriceSuccess(UpdateQuotationPrice updateQuotationPrice);

        /**
         * 在删除待摆放清单产品成功时回调
         *
         * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
         */
        void onDeletePlacementListSuccess(DeletePlacement deletePlacement);
    }

    interface QuotationPresenter extends BasePresenter<QuotationView> {

        /**
         * 获取报价位置明细列表
         */
        void getQuotationLocation();

        /**
         * 获取摆放清单列表
         */
        void getPlacementList();

        /**
         * 获取报价清单列表
         */
        void getQuotationList();

        /**
         * 创建报价位置
         *
         * @param name 位置名称
         */
        void createQuotationLocation(String name);

        /**
         * 删除报价位置
         *
         * @param quotePlaceId 报价位置id
         */
        void deleteQuotationLocation(int quotePlaceId);

        /**
         * 修改报价位置信息
         *
         * @param quotePlaceId 报价位置id
         * @param name         位置名称
         * @param file         设计图
         */
        void updateQuotationLocation(int quotePlaceId, String name, File file);

        /**
         * 修改报价位置中产品数量
         *
         * @param quotePlaceId 报价位置id
         * @param quoteId      清单产品id
         * @param number       产品数量
         */
        void updateQuotationInfo(int quotePlaceId, int quoteId, int number);

        /**
         * 待摆放清单加入到报价位置
         *
         * @param quoteId      清单id
         * @param quotePlaceId 报价位置id
         */
        void addQuotationLocation(int quoteId, int quotePlaceId);

        /**
         * 创建报价单
         *
         * @param payType       支付模式id(支付模式0购买1月租2年租)默认为1
         * @param taxRate       税点
         * @param discountMoney 优惠金额
         */
        void createQuotationOrder(int payType, double taxRate, double discountMoney);

        /**
         * 计算报价清单里面盆栽的总价格
         *
         * @param payType       支付模式id(支付模式0购买1月租2年租)默认为1
         * @param taxRate       税点
         * @param discountMoney 优惠金额
         * @param quotationList 报价清单列表
         */
        void calculatedTotalPrice(int payType, double taxRate, double discountMoney, List<PlacementQrQuotationList.DataBean.ListBean> quotationList);

        /**
         * 显示修改"场景名称"的PopupWindow
         *
         * @param v    RootView
         * @param name 旧位置名
         */
        void displaySceneNamePopupWindow(View v, String name);

        /**
         * 显示是否删除该盆栽
         *
         * @param name         盆栽名称
         * @param quotePlaceId 报价位置id
         * @param quoteId      清单产品id
         */
        void displayAreYouSureToDeleteTheBonsaiDialog(String name, int quotePlaceId, int quoteId);

        /**
         * 显示修改"盆栽数量"的PopupWindow
         *
         * @param v            RootView
         * @param quotePlaceId 报价位置id
         * @param quoteId      清单产品id
         * @param number       旧盆栽数量
         */
        void displayModifyBonsaiNumberPopupWindow(View v, int quotePlaceId, int quoteId, int number);

        /**
         * 显示报价设置的PopupWindow
         *
         * @param view 要显示的位置
         * @param list 报价清单列表
         */
        void displayQuotationSetPopupWindow(View view, List<PlacementQrQuotationList.DataBean.ListBean> list);

        /**
         * 显示报价设置的PopupWindow
         *
         * @param view          要显示的位置
         * @param payType       报价方式
         * @param taxRate       税率
         * @param discountMoney 优惠金额
         * @param list          报价清单列表
         */
        void displayQuotationSetPopupWindow(View view, int payType, double taxRate, double discountMoney, List<PlacementQrQuotationList.DataBean.ListBean> list);

        /**
         * 显示请输入授权码的PopupWindow
         *
         * @param v 界面Root布局
         */
        void displayAuthorizationCodePopupWindow(View v);

        /**
         * 显示修改盆栽报价的PopupWindow
         *
         * @param v        界面Root布局
         * @param mode     报价方式
         * @param oldPrice 旧报价
         */
        void displayEditPricePopupWindow(View v, int mode, double oldPrice);

        /**
         * 修改清单产品价格
         *
         * @param quoteId 清单id
         * @param price   新价格
         * @param type    1售价2月租价
         */
        void updateQuotationPrice(int quoteId, double price, int type);

        /**
         * 删除待摆放清单列表
         *
         * @param quoteIds 清单产品id,多个用英文逗号分隔
         */
        void deletePlacementList(String quoteIds);
    }

    interface QuotationModel {

        /**
         * 获取报价位置明细列表
         *
         * @param callBack 回调方法
         */
        void getQuotationLocation(CallBack callBack);

        /**
         * 获取摆放清单列表
         *
         * @param callBack 回调方法
         */
        void getPlacementList(CallBack callBack);

        /**
         * 获取报价清单列表
         *
         * @param callBack 回调方法
         */
        void getQuotationList(CallBack callBack);

        /**
         * 创建报价位置
         *
         * @param name     位置名称
         * @param callBack 回调方法
         */
        void createQuotationLocation(String name, CallBack callBack);

        /**
         * 删除报价位置
         *
         * @param quotePlaceId 报价位置id
         * @param callBack     回调方法
         */
        void deleteQuotationLocation(int quotePlaceId, CallBack callBack);

        /**
         * 修改报价位置信息
         *
         * @param quotePlaceId 报价位置id
         * @param name         位置名称
         * @param params       设计图
         * @param callBack     回调方法
         */
        void updateQuotationLocation(int quotePlaceId, String name, Map<String, RequestBody> params, CallBack callBack);

        /**
         * 修改报价位置中产品数量
         *
         * @param quotePlaceId 报价位置id
         * @param quoteId      清单产品id
         * @param number       产品数量
         * @param callBack     回调方法
         */
        void updateQuotationInfo(int quotePlaceId, int quoteId, int number, CallBack callBack);

        /**
         * 待摆放清单加入到报价位置
         *
         * @param quoteId      清单id
         * @param quotePlaceId 报价位置id
         * @param callBack     回调方法
         */
        void addQuotationLocation(int quoteId, int quotePlaceId, CallBack callBack);

        /**
         * 创建报价单
         *
         * @param payType       支付模式id(支付模式0购买1月租2年租)默认为1
         * @param taxRate       税点
         * @param discountMoney 优惠金额
         * @param callBack      回调方法
         */
        void createQuotationOrder(int payType, double taxRate, double discountMoney, CallBack callBack);

        /**
         * 验证修改价格密码
         *
         * @param viewPassword 查看密码
         * @param callBack     回调方法
         */
        void verifyPassword(String viewPassword, CallBack callBack);

        /**
         * 修改清单产品价格
         *
         * @param quoteId  清单id
         * @param price    新价格
         * @param type     1售价2月租价
         * @param callBack 回调方法
         */
        void updateQuotationPrice(int quoteId, double price, int type, CallBack callBack);

        /**
         * 删除待摆放清单列表
         *
         * @param quoteIds 清单产品id,多个用英文逗号分隔
         * @param callBack 回调方法
         */
        void deletePlacementList(String quoteIds, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取报价位置明细列表成功时回调
         *
         * @param quotationLocation 报价位置明细列表
         */
        void onGetQuotationLocationSuccess(QuotationLocation quotationLocation);

        /**
         * 在获取摆放清单列表成功时回调
         *
         * @param placementQrQuotationList 摆放清单列表
         */
        void onGetPlacementListSuccess(PlacementQrQuotationList placementQrQuotationList);

        /**
         * 在获取报价清单列表成功时回调
         *
         * @param placementQrQuotationList 报价清单列表
         */
        void onGetQuotationListSuccess(PlacementQrQuotationList placementQrQuotationList);

        /**
         * 在创建报价位置成功时回调
         *
         * @param createQuotationLocation 创建报价位置时服务器端返回的数据
         */
        void onCreateQuotationLocationSuccess(CreateQuotationLocation createQuotationLocation);

        /**
         * 在删除报价位置成功时回调
         *
         * @param deleteQuotationLocation 删除报价位置信息时服务器端返回的数据
         */
        void onDeleteQuotationLocationSuccess(DeleteQuotationLocation deleteQuotationLocation);

        /**
         * 在修改报价位置信息成功时回调
         *
         * @param updateQuotationLocation 修改报价位置信息时服务器端返回的数据
         */
        void onUpdateQuotationLocationSuccess(UpdateQuotationLocation updateQuotationLocation);

        /**
         * 在修改报价位置中产品数量成功时回调
         *
         * @param updateQuotationInfo 修改报价位置中产品数量时服务器端返回的数据
         */
        void onUpdateQuotationInfoSuccess(UpdateQuotationInfo updateQuotationInfo);

        /**
         * 在将待摆放清单加入到报价位置成功时回调
         *
         * @param addQuotationLocation 待摆放清单加入到报价位置时服务器端返回的数据
         */
        void onAddQuotationLocationSuccess(AddQuotationLocation addQuotationLocation);

        /**
         * 在创建报价单成功时回调
         *
         * @param createQuotationOrder 创建报价单时服务器端返回的数据
         */
        void onCreateQuotationOrderSuccess(CreateQuotationOrder createQuotationOrder);

        /**
         * 在验证修改价格密码成功时回调
         *
         * @param verifyPassword 验证修改价格密码时服务器端返回的数据
         */
        void onVerifyPasswordSuccess(VerifyPassword verifyPassword);

        /**
         * 在修改清单产品价格成功时回调
         *
         * @param updateQuotationPrice 修改清单产品价格时服务器端返回的数据
         */
        void onUpdateQuotationPriceSuccess(UpdateQuotationPrice updateQuotationPrice);

        /**
         * 在删除待摆放清单产品成功时回调
         *
         * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
         */
        void onDeletePlacementListSuccess(DeletePlacement deletePlacement);
    }
}
