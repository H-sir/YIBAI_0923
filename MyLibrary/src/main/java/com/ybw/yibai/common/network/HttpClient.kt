/*
 * Copyright (c) 2020/1/7. by Onlly
 * 个人简介：野生程序员
 * QQ:412102100
 * WX:i412102100
 * Github: Github.com/mingyouzhu
 * 简书: https://www.jianshu.com/u/4198eba0fbdd
 */
package com.huxin.common.network

import android.text.TextUtils
import android.util.Log
import com.google.gson.*
import com.google.gson.annotations.Expose
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.sql.Timestamp
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class HttpClient private constructor() {
    companion object {
        private var httpClient: HttpClient? = null
        private val lock = Any()

        fun init() {
            httpClient = null
        }

        val instance: HttpClient?
            get() {
                if (null == httpClient) {
                    synchronized(lock) {
                        if (null == httpClient) {
                            httpClient = HttpClient()
                        }
                    }
                }
                return httpClient
            }
    }

    var isOpenLOG = true
    var retrofit: Retrofit? = null
        private set
    private var host_api: String? = null
    private var host_web: String? = null
    private var host_image: String? = null
    private var eTag: String? = null
    private val gson: Gson
    private var authenticator: Authenticator? = null
    private val interceptors: ArrayList<Interceptor>?
    private var tokenInterceptor: BaseInterceptors? = null
    private val calls: ArrayList<Call<*>>

    init {
        interceptors = ArrayList()
        calls = ArrayList()
        gson = GsonBuilder().enableComplexMapKeySerialization().setLenient()
                .setDateFormat("yyyy-MM-dd HH:mm:ss") //时间转化为特定格
                .addSerializationExclusionStrategy(object : ExclusionStrategy {
                    override fun shouldSkipField(f: FieldAttributes): Boolean {
                        val expose = f.getAnnotation(Expose::class.java)
                        return expose != null && !expose.serialize
                    }

                    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                        return false
                    }
                })
                .addDeserializationExclusionStrategy(object : ExclusionStrategy {
                    override fun shouldSkipField(f: FieldAttributes): Boolean {
                        val expose = f.getAnnotation(Expose::class.java)
                        return expose != null && !expose.deserialize
                    }

                    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                        return false
                    }
                })
                .create()
    }

    inner class TimestampTypeAdapter : JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {
        private val format: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        override fun serialize(src: Timestamp, arg1: Type, arg2: JsonSerializationContext): JsonElement {
            val dateFormatAsString = format.format(Date(src.time))
            return JsonPrimitive(dateFormatAsString)
        }

        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Timestamp {
            if (json !is JsonPrimitive) {
                throw JsonParseException("The date should be a string value")
            }
            return try {
                val date = format.parse(json.getAsString())
                Timestamp(date.time)
            } catch (e: ParseException) {
                throw JsonParseException(e)
            }
        }
    }

    fun setHostUrl(host_api: String?): HttpClient? {
        return setHostUrl(host_api, null, null)
    }

    fun setHostUrl(host_api: String?, host_web: String?, host_image: String?): HttpClient? {
        this.host_api = host_api
        this.host_web = host_web
        this.host_image = host_image
        return httpClient
    }

    fun setTokenInterceptor(tokenInterceptor: BaseInterceptors?): HttpClient? {
        this.tokenInterceptor = tokenInterceptor
        return httpClient
    }

    fun setToken(token: String?): HttpClient? {
        tokenInterceptor?.token = token
        return httpClient
    }

    fun seteTag(eTag: String?): HttpClient? {
        this.eTag = eTag
        return httpClient
    }

    fun setAuthenticator(authenticator: Authenticator?): HttpClient? {
        this.authenticator = authenticator
        return httpClient
    }

    fun addInterceptors(interceptor: Interceptor): HttpClient? {
        interceptors!!.add(interceptor)
        return httpClient
    }

    fun initialize() {
        if (TextUtils.isEmpty(host_api)) {
            throw RuntimeException("HttpClient Host Api is empty.")
        }
        val builder = OkHttpClient.Builder()
        if (null != interceptors && interceptors.size > 0) {
            for (i in interceptors) {
                builder.addInterceptor(i)
            }
        }
        if (null != authenticator) builder.authenticator(authenticator)
        if (null != tokenInterceptor) builder.addInterceptor(tokenInterceptor)
        if (isOpenLOG) builder.addNetworkInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            Log.i("HTTP", message.toString())
        }).setLevel(HttpLoggingInterceptor.Level.BODY))
        builder.connectTimeout(60, TimeUnit.SECONDS) //60s delay
        builder.readTimeout(60,TimeUnit.SECONDS)
        builder.writeTimeout(60,TimeUnit.SECONDS)
        builder.protocols(listOf(Protocol.HTTP_1_1))
        val client = builder.build()
        retrofit = Retrofit.Builder()
                .baseUrl(host_api)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }

    fun <T> createService(server: Class<T>?): T {
        return retrofit!!.create(server)
    }

    fun getCall(tCall: Call<*>): Call<*> {
        calls.add(tCall)
        return tCall
    }

    fun cancleCall() {
        for (call in calls) {
            call.cancel()
        }
    }
}