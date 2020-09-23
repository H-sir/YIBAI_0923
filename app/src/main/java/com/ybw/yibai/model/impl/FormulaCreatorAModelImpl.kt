package com.ybw.yibai.model.impl

import com.ybw.yibai.base.YiBaiApplication
import com.ybw.yibai.common.callback.ICallback
import com.ybw.yibai.common.interfaces.ApiService
import com.ybw.yibai.common.model.ItemDesignSceneModel
import com.ybw.yibai.common.network.response.BaseResponse
import com.ybw.yibai.common.utils.OtherUtil
import com.ybw.yibai.common.utils.RetrofitManagerUtil
import com.ybw.yibai.common.utils.TimeUtil
import com.ybw.yibai.model.inter.IFormulaCreatorAModel
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FormulaCreatorAModelImpl : IFormulaCreatorAModel{
    private val apiService:ApiService
    init {
        val instance = RetrofitManagerUtil.getInstance()
        apiService = instance.apiService
    }

    override fun onGetDesignDetail(number:String,callback: ICallback<ItemDesignSceneModel?>) {
        val timeStamp = TimeUtil.getTimestamp().toString()
        val observable: Observable<BaseResponse<ItemDesignSceneModel>> = apiService.getDesignInfo(
                timeStamp,OtherUtil.getSign(timeStamp, "mybw.getdesigninfo"),
                YiBaiApplication.getUid().toString(),number
        )
        val observer: Observer<BaseResponse<ItemDesignSceneModel>> = object : Observer<BaseResponse<ItemDesignSceneModel>> {
            override fun onSubscribe(d: Disposable) {
                callback.onRequestBefore(d)
            }

            override fun onError(e: Throwable) {
                callback.onRequestFailure(e)
            }

            override fun onComplete() {
                callback.onRequestComplete()
            }

            override fun onNext(t: BaseResponse<ItemDesignSceneModel>) {
                if(t.code == 200)callback.onResponse(t.data)
                else callback.onFailed(t.msg)
            }
        }
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer)
    }

    override fun onDeleteDesign(number: String, callback: ICallback<Any>) {
        val timeStamp = TimeUtil.getTimestamp().toString()
        val observable: Observable<BaseResponse<Any>> = apiService.deleteDesign(
                timeStamp,OtherUtil.getSign(timeStamp, "mybw.deldesign"),
                YiBaiApplication.getUid().toString(),number
        )
        val observer: Observer<BaseResponse<Any>> = object : Observer<BaseResponse<Any>> {
            override fun onSubscribe(d: Disposable) {
                callback.onRequestBefore(d)
            }

            override fun onError(e: Throwable) {
                callback.onRequestFailure(e)
            }

            override fun onComplete() {
                callback.onRequestComplete()
            }

            override fun onNext(response: BaseResponse<Any>) {
                if(response.code == 200)callback.onResponse(response.data.toString())
                else callback.onFailed(response.msg)
            }
        }
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer)
    }

    override fun onDeleteScene(schemeId: String, callback: ICallback<Any>) {
        val timeStamp = TimeUtil.getTimestamp().toString()
        val observable: Observable<BaseResponse<Any>> = apiService.deleteScene(
                timeStamp,OtherUtil.getSign(timeStamp, "mybw.delscheme"),
                YiBaiApplication.getUid().toString(),schemeId
        )
        val observer: Observer<BaseResponse<Any>> = object : Observer<BaseResponse<Any>> {
            override fun onSubscribe(d: Disposable) {
                callback.onRequestBefore(d)
            }

            override fun onError(e: Throwable) {
                callback.onRequestFailure(e)
            }

            override fun onComplete() {
                callback.onRequestComplete()
            }

            override fun onNext(response: BaseResponse<Any>) {
                if(response.code == 200)callback.onResponse(response.data.toString())
                else callback.onFailed(response.msg)
            }
        }
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer)
    }

    override fun onDeleteSchemePic(id: String,callback: ICallback<Any>) {
        val timeStamp = TimeUtil.getTimestamp().toString()
        val observable: Observable<BaseResponse<Any>> = apiService.deleteSchemePic(
                timeStamp,OtherUtil.getSign(timeStamp, "mybw.delschemepic"),
                YiBaiApplication.getUid().toString(),id
        )
        val observer: Observer<BaseResponse<Any>> = object : Observer<BaseResponse<Any>> {
            override fun onSubscribe(d: Disposable) {
                callback.onRequestBefore(d)
            }

            override fun onError(e: Throwable) {
                callback.onRequestFailure(e)
            }

            override fun onComplete() {
                callback.onRequestComplete()
            }

            override fun onNext(response: BaseResponse<Any>) {
                if(response.code == 200)callback.onResponse(response.data.toString())
                else callback.onFailed(response.msg)
            }
        }
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer)
    }
}