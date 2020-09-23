package com.ybw.yibai.common.bean

import java.io.Serializable

class BTCBean(
        val id:String?,
        val field:String?,
        val name:String?,
        val cate_code:String?,
        val sub:String?,
        val screen_type:String?,
        val checktype:String?,
        val type:String?,
        var son:List<Child>?,
        var select:Boolean = false,
        var mName:String = ""
):Serializable{
    class Child(
            val id:String?,
            val name:String?,
            val pic:String?,
            val color:String?,
            var screen_type:String?,
            var select:Boolean = false
    ):Serializable
}