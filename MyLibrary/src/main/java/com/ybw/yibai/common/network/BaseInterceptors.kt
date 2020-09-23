/*
 * Copyright (c) 2020/1/7. by Onlly
 * 个人简介：野生程序员
 * QQ:412102100
 * WX:i412102100
 * Github: Github.com/mingyouzhu
 * 简书: https://www.jianshu.com/u/4198eba0fbdd
 */
package com.huxin.common.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

abstract class BaseInterceptors : Interceptor {
    var token: String? = null

   /* @Throws(Exception::class)
    open fun onGetToken(): TokenModel?{
        return null
    }*/

    open fun onGetHeaders():Map<String,String>?{
        return mapOf()
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
        onGetHeaders()?.forEach { builder.addHeader(it.key,it.value) }
        builder.method(originalRequest.method(), originalRequest.body())
        val requestHeader = builder.build()
        val response = chain.proceed(requestHeader)
        if (response.code() == 401) { // Unauthorized
            /*val tokenModel = onGetToken()
            val newToken = tokenModel?.access_token
            token = "Bearer $newToken"
            Log.i("HTTP", "-->新获取的token =  $token")
            val requestBuilder = requestHeader.newBuilder()
                    .method(requestHeader.method(), requestHeader.body())
                    .headers(requestHeader.headers())
                    .header("Authorization", token)
            val request = requestBuilder.build()
            chain.proceed(request)*/
        }
        return response
    }
}