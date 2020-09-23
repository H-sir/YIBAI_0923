package com.ybw.yibai.common.network.request

import com.google.gson.JsonSerializer
import java.io.Serializable

data class GetLocRequest(
        val uid:String,
        val longitude:String,
        val latitude:String
):Serializable