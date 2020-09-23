package com.ybw.yibai.view.inter

import android.app.Activity
import com.ybw.yibai.common.model.ItemDesignSceneModel

interface IFormulaCreatorAView : IBaseView{
    fun onGetDesignListCallback(model:ItemDesignSceneModel?)
    fun onDeleteDesignCallback(any:Any)
    fun onDeleteSceneCallback(schemeId:String)
    fun onDeleteSchemePic(id:String)
    fun onGetActivity():Activity
    fun onDelSchemeId()
}