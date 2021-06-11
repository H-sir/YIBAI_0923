package com.ybw.yibai.module.citypicker;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.CityListHotBean;

/**
 * 城市界面Presenter实现类
 *
 * @author sjl
 * @date 2019/5/16
 */
public class CityPickerPresenterImpl extends BasePresenterImpl<CityPickerContract.CityPickerView> implements CityPickerContract.CityPickerPresenter, CityPickerContract.CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private CityPickerContract.CityPickerView mCityPickerView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private CityPickerContract.CityPickerModel mCityPickerModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public CityPickerPresenterImpl(CityPickerContract.CityPickerView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mCityPickerView = getView();
        mCityPickerModel = new CityPickerModelImpl();
    }


    @Override
    public void getCity(int type) {
        mCityPickerModel.getCity(type,this);
    }

    @Override
    public void onGetCitySuccess(int type,CityListBean cityListBean) {
        mCityPickerView.onGetCitySuccess(type,cityListBean);
    }

    @Override
    public void onGetCityHotSuccess(int type, CityListHotBean cityListHotBean) {
        mCityPickerView.onGetCityHotSuccess(type,cityListHotBean);
    }
}
