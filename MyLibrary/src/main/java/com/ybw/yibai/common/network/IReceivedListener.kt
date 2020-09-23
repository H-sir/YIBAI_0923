package com.huxin.common.network

interface IReceivedListener<T> {
    fun onSucceed(result:T)
    fun onFailed(reason:String)
}