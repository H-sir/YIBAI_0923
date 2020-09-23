package com.huxin.common.network

import android.content.Context
import okhttp3.Headers
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// 网络请求模板原型
abstract class BaseRequest{
    protected val timeout:Long = 10
    protected var executorService:ExecutorService = Executors.newFixedThreadPool(5)
    abstract fun BeginGet(cxt:Context,adr:String,vararg params:Pair<String,String?>,listener:IReceivedListener<String>)
    abstract fun BeginGet(cxt:Context,adr:String, params:Map<String,String>, listener: IReceivedListener<String>)
    abstract fun BeginPost(cxt:Context,adr:String,params:Map<String,Any>,listener:IReceivedListener<String>)
    abstract fun BeginPost(cxt:Context,adr:String,vararg params:Pair<String,Any>,listener:IReceivedListener<String>)
    abstract fun BeginDelete(cxt:Context,adr:String,params:Map<String,Any>,listener:IReceivedListener<String>)
    abstract fun BeginPut(cxt:Context,adr:String,params:Map<String,Any>,listener:IReceivedListener<String>)
    protected fun getHeaders(cxt: Context):Headers{
        val headers = Headers.Builder()
        //headers.add("Authorization", "Bearer ${AccountUtil.getToken(cxt)}")
        return headers.build()
    }
}