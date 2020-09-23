package com.ybw.yibai.common.callback

interface ICallback<T> : IBaseCallback{
    fun onResponse(response:T)
    fun onFailed(reason:String)
}