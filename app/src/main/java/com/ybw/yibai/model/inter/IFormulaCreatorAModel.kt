package com.ybw.yibai.model.inter

import com.ybw.yibai.common.callback.ICallback
import com.ybw.yibai.common.model.ItemDesignSceneModel

interface IFormulaCreatorAModel{
    fun onGetDesignDetail(number:String,callback:ICallback<ItemDesignSceneModel?>)
    fun onDeleteDesign(number: String,callback:ICallback<Any>)
    fun onDeleteScene(schemeId:String,callback: ICallback<Any>)
    fun onDeleteSchemePic(id:String,callback: ICallback<Any>)
}