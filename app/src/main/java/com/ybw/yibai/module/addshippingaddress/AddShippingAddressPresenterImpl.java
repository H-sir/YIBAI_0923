package com.ybw.yibai.module.addshippingaddress;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CityAreaList;
import com.ybw.yibai.common.bean.CityAreaList.DataBean;
import com.ybw.yibai.common.bean.CityAreaList.DataBean.ChildrenBeanX;
import com.ybw.yibai.common.bean.CityAreaList.DataBean.ChildrenBeanX.ChildrenBean;
import com.ybw.yibai.common.bean.DeleteShippingAddress;
import com.ybw.yibai.common.bean.ShippingAddress;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.module.addshippingaddress.AddShippingAddressContract.AddShippingAddressModel;
import com.ybw.yibai.module.addshippingaddress.AddShippingAddressContract.AddShippingAddressPresenter;
import com.ybw.yibai.module.addshippingaddress.AddShippingAddressContract.AddShippingAddressView;
import com.ybw.yibai.module.addshippingaddress.AddShippingAddressContract.CallBack;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;

/**
 * 添加收货地址Presenter实现类
 *
 * @author sjl
 * @date 2019/10/11
 */
public class AddShippingAddressPresenterImpl extends BasePresenterImpl<AddShippingAddressView> implements AddShippingAddressPresenter, CallBack {

    private static final String TAG = "AddShippingAddressPresenterImpl";

    public int oldOptions1, oldOptions2, oldOptions3;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 中国行政区域列表-省
     */
    private List<DataBean> mProvinceList;

    /**
     * 中国行政区域列表-市
     */
    private List<List<ChildrenBeanX>> mCityList;

    /**
     * 中国行政区域列表-区
     */
    private List<List<List<ChildrenBean>>> mAreaList;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private AddShippingAddressView mAddShippingAddressView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private AddShippingAddressModel mAddShippingAddressModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public AddShippingAddressPresenterImpl(AddShippingAddressView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mAddShippingAddressView = getView();
        mAddShippingAddressModel = new AddShippingAddressModelImpl();

        mProvinceList = new ArrayList<>();
        mCityList = new ArrayList<>();
        mAreaList = new ArrayList<>();
    }

    /**
     * 获取城市区域列表
     */
    @Override
    public void getCityAreaList() {
        mAddShippingAddressModel.getCityAreaList(this);
    }

    /**
     * 在获取城市区域列表成功时回调
     *
     * @param cityAreaList 城市区域列表数据
     */
    @Override
    public void onGetCityAreaListSucceed(CityAreaList cityAreaList) {
        mAddShippingAddressView.onGetCityAreaListSucceed(cityAreaList);
        if (CODE_SUCCEED != cityAreaList.getCode()) {
            return;
        }
        initData(cityAreaList);
    }

    private void initData(CityAreaList cityAreaList) {
        List<DataBean> provinceList = cityAreaList.getData();
        if (null == provinceList || provinceList.size() == 0) {
            return;
        }

        mProvinceList.addAll(provinceList);
        // 遍历省份
        for (int i = 0; i < mProvinceList.size(); i++) {
            // 该省的城市列表（第二级）
            List<ChildrenBeanX> provinceCityLists = new ArrayList<>();
            // 该省的所有地区列表（第三极）
            List<List<ChildrenBean>> provinceCityAreaLists = new ArrayList<>();
            // 获取该省的城市列表
            List<ChildrenBeanX> childrenBeanXList = mProvinceList.get(i).getChildren();
            if (null == childrenBeanXList || childrenBeanXList.size() == 0) {
                continue;
            }

            // 遍历该省份的所有城市
            for (int j = 0; j < childrenBeanXList.size(); j++) {
                // 添加城市
                provinceCityLists.add(childrenBeanXList.get(j));
                // 该城市的所有地区列表
                ArrayList<ChildrenBean> cityAreaLists = new ArrayList<>();
                // 获取该省的城市区域列表
                List<ChildrenBean> childrenBeanList = childrenBeanXList.get(j).getChildren();

                // 如果无地区数据,添加空字符串,防止数据为null 导致三个选项长度不匹配造成崩溃
                if (childrenBeanList == null || 0 == childrenBeanList.size()) {
                    ChildrenBean childrenBean = new ChildrenBean();
                    childrenBean.setName(" ");
                    cityAreaLists.add(childrenBean);
                } else {
                    cityAreaLists.addAll(childrenBeanList);
                }
                // 添加该省所有地区数据
                provinceCityAreaLists.add(cityAreaLists);
            }

            // 添加城市数据
            mCityList.add(provinceCityLists);
            // 添加地区数据
            mAreaList.add(provinceCityAreaLists);
        }
    }

    /**
     * 初始化区域View
     */
    @Override
    @SuppressWarnings("all")
    public void initAreaSelectView() {
        Activity activity = (Activity) mAddShippingAddressView;
        OnOptionsSelectListener mOnOptionsSelectListener = (options1, options2, options3, v) -> {
            // 返回的分别是三个级别的选中位置
            oldOptions1 = options1;
            oldOptions2 = options2;
            oldOptions3 = options3;
            int provinceId = mProvinceList.get(options1).getId();
            int cityId = mCityList.get(options1).get(options2).getId();
            int areaId = mAreaList.get(options1).get(options2).get(options3).getId();

            String provinceName = mProvinceList.get(options1).getName();
            String cityName = mCityList.get(options1).get(options2).getName();
            String areaName = mAreaList.get(options1).get(options2).get(options3).getName();

            mAddShippingAddressView.returnUserSelectArea(provinceId, cityId, areaId, provinceName, cityName, areaName);
        };

        OptionsPickerView mOptionsPickerView = new OptionsPickerBuilder(activity, mOnOptionsSelectListener)
                // 标题
                .setTitleText(activity.getResources().getString(R.string.area_selection))
                // 标题文字颜色
                .setTitleColor(ContextCompat.getColor(activity, R.color.toast_text_color))
                // 确定按钮文字颜色
                .setSubmitColor(ContextCompat.getColor(activity, R.color.toast_text_color))
                // 取消按钮文字颜色
                .setCancelColor(ContextCompat.getColor(activity, R.color.toast_text_color))
                // 标题背景颜色
                .setTitleBgColor(ContextCompat.getColor(activity, R.color.colorPrimary))
                // 设置默认选中项
                .setSelectOptions(oldOptions1, oldOptions2, oldOptions3)
                // 设置Item的间距倍数,用于控制Item高度间隔
                .setLineSpacingMultiplier(2)
                // 滚轮文字大小
                .setContentTextSize(14)
                // 确定和取消文字大小
                .setSubCalSize(14)
                // 标题文字大小
                .setTitleSize(16)
                .build();
        // 添加数据源
        mOptionsPickerView.setPicker(mProvinceList, mCityList, mAreaList);
        mOptionsPickerView.show();
    }

    /**
     * 创建/修改收货地址
     *
     * @param areaId    区id
     * @param consignee 收货人
     * @param mobile    联系电话
     * @param address   详细地址
     * @param isDefault 是否默认(1是0否)
     */
    @Override
    public void createOrModifyShippingAddress(Integer addressId, int areaId, String consignee, String mobile, String address, int isDefault) {
        Activity activity = (Activity) mAddShippingAddressView;
        if (TextUtils.isEmpty(consignee) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(address) || 0 == areaId) {
            MessageUtil.showMessage(activity.getResources().getString(R.string.missing_the_necessary_consignee_information_please_complete_all_the_information));
            return;
        }
        mAddShippingAddressModel.createOrModifyShippingAddress(addressId, areaId, consignee, mobile, address, isDefault, this);
    }

    /**
     * 在创建收货/修改地址成功时回调
     *
     * @param shippingAddress 创建或修改收货地址
     */
    @Override
    public void onCreateOrModifyShippingAddressSucceed(ShippingAddress shippingAddress) {
        mAddShippingAddressView.onCreateOrModifyShippingAddressSucceed(shippingAddress);
    }

    /**
     * 删除收货地址
     *
     * @param addressId 收货地址id
     */
    @Override
    public void deleteShippingAddress(int addressId) {
        mAddShippingAddressModel.deleteShippingAddress(addressId, this);
    }

    /**
     * 在删除收货地址
     *
     * @param deleteShippingAddress 删除收货地址时服务器端返回的数据
     */
    @Override
    public void onDeleteShippingAddressSucceed(DeleteShippingAddress deleteShippingAddress) {
        mAddShippingAddressView.onDeleteShippingAddressSucceed(deleteShippingAddress);
    }
}