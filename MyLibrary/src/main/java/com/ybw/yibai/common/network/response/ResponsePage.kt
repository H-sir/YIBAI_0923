package com.ybw.yibai.common.network.response

import java.io.Serializable

class ResponsePage<T>(
        val count:Int?,
        val page:Int?,
        val pageSize:Int?,
        val list:List<T>?
):Serializable