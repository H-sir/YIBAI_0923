package com.ybw.yibai.presenter.inter

interface IFormulaCreatorAPresenter{
    fun onGetDesignDetail(number: String)

    /**
     * 删除场景
     * @param schemeId 设计ID
     */
    fun onDeleteScene(schemeId:String)

    /**
     * 删除设计
     * @param number 场景id
     */
    fun onDeleteDesign(number:String)
    fun onDeleteSchemePic(id:String)
    fun onDoneDesign(schemeId: String)
}