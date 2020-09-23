package com.ybw.yibai.common.model

import java.io.Serializable

class CreateSceneOrPicModel(
        val desing_number:String?,
        val scheme_id:String?,
        val scheme_name:String?,
        val img:List<ItemModel>?
):Serializable{
    inner class ItemModel(
            val id:String?,
            val pic:String
    ):Serializable
}