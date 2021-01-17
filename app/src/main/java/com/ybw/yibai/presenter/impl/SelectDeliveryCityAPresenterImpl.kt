package com.ybw.yibai.presenter.impl

import android.location.Location
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ybw.yibai.base.YiBaiApplication
import com.ybw.yibai.common.bean.EditUserInfo
import com.ybw.yibai.common.constants.HttpUrls
import com.ybw.yibai.common.interfaces.ApiService
import com.ybw.yibai.common.network.request.GetLocRequest
import com.ybw.yibai.common.network.response.BaseResponse
import com.ybw.yibai.common.network.response.CityItemResponse
import com.ybw.yibai.common.network.response.CurAddrResponse
import com.ybw.yibai.common.network.response.HotCityResponse
import com.ybw.yibai.common.utils.OtherUtil
import com.ybw.yibai.common.utils.RetrofitManagerUtil
import com.ybw.yibai.common.utils.SPUtil
import com.ybw.yibai.common.utils.TimeUtil
import com.ybw.yibai.presenter.inter.ISelectDeliveryCityAPresenter
import com.ybw.yibai.view.inter.ISelectDeliveryCityAView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.xutils.common.util.KeyValue

class SelectDeliveryCityAPresenterImpl(val view: ISelectDeliveryCityAView)
    : ISelectDeliveryCityAPresenter {

    private val mApiService: ApiService

    init {
        val instance = RetrofitManagerUtil.getInstance()
        mApiService = instance.apiService
    }

    override fun onGetCurrentCity(loc: Location) {
        val data = JSONObject()
        data.put("uid", YiBaiApplication.getUid().toString())
        data.put("longitude", loc.longitude.toString())
        data.put("latitude", loc.latitude.toString())
        val timestamp = TimeUtil.getTimestamp().toString()
        val observable = mApiService.getLocation(
                timestamp,
                OtherUtil.getSign(timestamp, "mybw.getplace"),
                data.toString());
        val observer: Observer<BaseResponse<CurAddrResponse>> = object : Observer<BaseResponse<CurAddrResponse>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: BaseResponse<CurAddrResponse>) {
                view.onGetCurCityCallback(t.data?.cityname ?: "", t.data?.citycode ?: "")
            }
        }
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer)
    }

    override fun onGetAllCities(phone: String) {
        val timestamp = TimeUtil.getTimestamp().toString()
        val observable = mApiService.getAllCities(
                timestamp,
                OtherUtil.getSign(timestamp, "mybw.getcity"),
                YiBaiApplication.getUid().toString()
        );
        val observer: Observer<BaseResponse<HotCityResponse>> = object : Observer<BaseResponse<HotCityResponse>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: BaseResponse<HotCityResponse>) {
                t.data?.let {
                    view.onGetCitiesCallback(it)
                }
                if (t.code != 200) view.onShowToastShort(t.msg)
            }
        }
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer)
    }

    override fun onSetCity(code: String) {
        val timestamp = TimeUtil.getTimestamp().toString()
        val observable = mApiService.setCity(
                timestamp,
                OtherUtil.getSign(timestamp, "mybw.setuserposition"),
                YiBaiApplication.getUid().toString(),
                code
        );
        val observer: Observer<BaseResponse<Any>> = object : Observer<BaseResponse<Any>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: BaseResponse<Any>) {
                if (t.code != 200) {
                    view.onShowToastShort(t.msg)
                } else {
                    val obj = JSONObject(t.data.toString())
                    view.onSetCityCallback(obj.optString("city_name"), code)
                    view.onShowToastShort("设置成功!")
                    view.onFinish()
                }
            }
        }
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer)
    }

    override fun onSetProduct(conOpen: Int) {
        val timestamp = TimeUtil.getTimestamp().toString()
        val observable = mApiService.editUserProductInfo(
                timestamp,
                OtherUtil.getSign(timestamp, "mybw.edituser"),
                YiBaiApplication.getUid(),
                conOpen
        );
        val observer: Observer<EditUserInfo> = object : Observer<EditUserInfo> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: EditUserInfo) {
                if (t.code != 200) {
                    view.onShowToastShort(t.msg)
                } else {
                    view.onShowToastShort(t.msg)
                    view.onFinish()
                }
            }
        }
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer)
    }
}