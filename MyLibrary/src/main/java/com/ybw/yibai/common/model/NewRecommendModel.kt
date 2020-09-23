package com.ybw.yibai.common.model

import java.io.Serializable

class NewRecommendModel(
         val plant:ItemExtModel?,
         val pot:ItemExtModel?

):Serializable{
    inner class ItemExtModel(
            val count:Int?,
            val list:List<ItemModel>?
    )
    inner class ItemModel(
            val sku_id:String?,
            val name:String?,
            val product_id:String?,
            val diameter:String?,
            val height:String?,
            val offset_ratio:String?,
            val price:String?,
            val month_rent:String?,
            val trade_price:String?,
            val pic1:String?,
            val pic2:String?,
            val pic3:String?
    ):Serializable
}