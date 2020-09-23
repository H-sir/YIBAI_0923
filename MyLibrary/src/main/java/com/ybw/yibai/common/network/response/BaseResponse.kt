package com.ybw.yibai.common.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BaseResponse<T>(
        @SerializedName("code")
        val code: Int,
        @SerializedName("msg")
        val msg: String,
        @SerializedName("data")
        val data: T?
) : Serializable