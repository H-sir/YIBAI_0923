package com.ybw.yibai.presenter.impl

import android.app.AlertDialog
import android.content.DialogInterface
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.ybw.yibai.R
import com.ybw.yibai.base.YiBaiApplication
import com.ybw.yibai.common.bean.SceneInfo
import com.ybw.yibai.common.callback.ICallback
import com.ybw.yibai.common.model.ItemDesignSceneModel
import com.ybw.yibai.common.utils.SPUtil
import com.ybw.yibai.model.impl.FormulaCreatorAModelImpl
import com.ybw.yibai.model.inter.IFormulaCreatorAModel
import com.ybw.yibai.presenter.base.BasePresenter
import com.ybw.yibai.presenter.inter.IFormulaCreatorAPresenter
import com.ybw.yibai.view.inter.IFormulaCreatorAView
import io.reactivex.disposables.Disposable


class FormulaCreatorAPresenterImpl(view: IFormulaCreatorAView)
    : BasePresenter<IFormulaCreatorAModel, IFormulaCreatorAView>(view),
        IFormulaCreatorAPresenter{

    override fun onGetModel(): IFormulaCreatorAModel {
        return FormulaCreatorAModelImpl()
    }

    override fun onGetDesignDetail(number: String) {
        model.onGetDesignDetail(number,object:ICallback<ItemDesignSceneModel?>{
            override fun onFailed(reason: String) {
                view.onRequestFailure(Throwable(reason))
            }

            override fun onRequestBefore(disposable: Disposable) {
                view.onRequestBefore()
            }

            override fun onRequestComplete() {
                view.onRequestComplete()
            }

            override fun onRequestFailure(throwable: Throwable?) {
                view.onRequestFailure(throwable)
            }

            override fun onResponse(response:ItemDesignSceneModel?) {
                view.onGetDesignListCallback(response)
            }
        })
    }

    override fun onDeleteDesign(number: String) {
        try{
            val manager = YiBaiApplication.getDbManager()
            val sceneInfoList = manager.selector(SceneInfo::class.java)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .findAll()
            manager.delete(sceneInfoList);
        }catch (e:Exception){
            e.printStackTrace()
        }
        model.onDeleteDesign(number,object:ICallback<Any>{
            override fun onFailed(reason: String) {
                view.onRequestFailure(Throwable(reason))
            }

            override fun onRequestBefore(disposable: Disposable) {
                view.onRequestBefore()
            }

            override fun onRequestComplete() {
                view.onRequestComplete()
            }

            override fun onRequestFailure(throwable: Throwable?) {
                view.onRequestFailure(throwable)
            }

            override fun onResponse(response:Any) {
                SPUtil.remove(view.onGetContext(),"number")
                view.onDeleteDesignCallback(response)
            }
        })
    }

    override fun onDeleteSchemePic(id: String) {
        model.onDeleteSchemePic(id,object:ICallback<Any>{
            override fun onFailed(reason: String) {
                view.onRequestFailure(Throwable(reason))
            }

            override fun onRequestBefore(disposable: Disposable) {
                view.onRequestBefore()
            }

            override fun onRequestComplete() {
                view.onRequestComplete()
            }

            override fun onRequestFailure(throwable: Throwable?) {
                view.onRequestFailure(throwable)
            }

            override fun onResponse(response:Any) {
                view.onDeleteSchemePic(id)
            }
        })
    }

    override fun onDoneDesign(schemeId: String) {
        AlertDialog.Builder(view.onGetActivity())
                .setMessage(R.string.S1)
                .setPositiveButton(R.string.S3) { dialog, which ->
                    val appId = "wx08cd98ecf42af1d7" // 填应用AppId
                    val api = WXAPIFactory.createWXAPI(view.onGetContext(), appId)
                    val req = WXLaunchMiniProgram.Req()
                    req.userName = "gh_a532df421aeb" // 填小程序原始id
                    req.path = "/pages/index/index?number=${schemeId}"
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE // 可选打开 开发版，体验版和正式版
                    api.sendReq(req)
                }
                .setNegativeButton(R.string.S2,object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        view.onDelSchemeId()
                    }
                }).show()
    }

    override fun onDeleteScene(schemeId: String) {
        model.onDeleteScene(schemeId,object:ICallback<Any>{
            override fun onFailed(reason: String) {
                view.onRequestFailure(Throwable(reason))
            }

            override fun onRequestBefore(disposable: Disposable) {
                view.onRequestBefore()
            }

            override fun onRequestComplete() {
                view.onRequestComplete()
            }

            override fun onRequestFailure(throwable: Throwable?) {
                view.onRequestFailure(throwable)
            }

            override fun onResponse(response:Any) {
                view.onDeleteSceneCallback(schemeId)
            }
        })
    }
}