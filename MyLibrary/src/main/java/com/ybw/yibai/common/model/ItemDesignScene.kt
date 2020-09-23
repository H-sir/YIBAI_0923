package com.ybw.yibai.common.model

import java.io.Serializable
import java.util.*

class ItemDesignSceneModel(
        val number:String?,
        val addtime:String?,
        val schemelist:LinkedList<ItemModel>?
):Serializable{
    class ItemModel(
            val scheme_id:String?,
            val scheme_name:String?,
            val imglist:LinkedList<ItemImgModel>?,
            var select:Boolean?
    ):Serializable
    class ItemImgModel(
            val id:String?,
            val pic:String?,
            val offer_url:String?
    ):Serializable
}