package com.huxin.common.network

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object HttpRequest : BaseRequest() {

    override fun BeginGet(cxt: Context, adr: String, vararg params: Pair<String, String?>, listener: IReceivedListener<String>) {
        val handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                if (msg?.what == 0) {
                    Log.d("REQUEST", "RESULT: ${msg.obj}")
                    listener.onSucceed(msg.obj.toString())
                } else if (msg?.what == 1) {
                    listener.onFailed(msg.obj.toString())
                }
            }
        }
        val client = OkHttpClient()
            .newBuilder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .build()
        val args = StringBuilder()
        args.append(adr)
        if (params.count() > 0) {
            args.append("?")
            params.forEach {
                args.append(it.first)
                    .append("=")
                    .append(it.second)
                    .append("&")
            }
            if (args[args.length - 1] == '&') {
                args.replace(args.length - 1, args.length, "")
            }
        }
        val request = Request.Builder().url(args.toString()).headers(getHeaders(cxt)).build()
        executorService.submit {
            try {
                val response = client.newCall(request).execute()
                val result = response.body()!!.string()
                handler.sendMessage(handler.obtainMessage(0, result))
            } catch (e: Exception) {
                handler.sendMessage(handler.obtainMessage(1, e.message))
            }
        }
        Log.d("REQUEST", "GET: $args")
    }

    override fun BeginGet(cxt:Context,adr: String, params: Map<String, String>, listener: IReceivedListener<String>) {
        val handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                if (msg?.what == 0) {
                    Log.d("REQUEST", "RESULT: ${msg.obj}")
                    listener.onSucceed(msg.obj.toString())
                } else if (msg?.what == 1) {
                    listener.onFailed(msg.obj.toString())
                }
            }
        }
        val client = OkHttpClient()
            .newBuilder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .build()
        val args = StringBuilder()
        args.append(adr)
        if (params.count() > 0) {
            args.append("?")
            params.forEach {
                args.append(it.key)
                    .append("=")
                    .append(it.value)
                    .append("&")
            }
            if (args[args.length - 1] == '&') {
                args.replace(args.length - 1, args.length, "")
            }
        }
        val request = Request.Builder().url(args.toString()).headers(getHeaders(cxt)).build()
        executorService.submit {
            try {
                val response = client.newCall(request).execute()
                val result = response.body()!!.string()
                handler.sendMessage(handler.obtainMessage(0, result))
            } catch (e: Exception) {
                handler.sendMessage(handler.obtainMessage(1, e.message))
            }
        }
        Log.d("REQUEST", "GET: $args")
    }

    override fun BeginPost(cxt:Context,adr: String, vararg params: Pair<String, Any>, listener: IReceivedListener<String>) {
        val handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == 1) {
                    Log.d("REQUEST", "RESULT: ${msg.obj}")
                    listener.onSucceed(msg.obj.toString())
                } else if (msg.what == 2) {
                    listener.onFailed(msg.obj.toString())
                }
            }
        }
        val client = OkHttpClient()
            .newBuilder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .build()
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        params.forEach {
            if (it.second !is File) {
                builder.addFormDataPart(it.first, it.second.toString())
            } else {
                val file = it.second as File
                builder.addFormDataPart(it.first, file.name, RequestBody.create(null, file))
            }
        }
        val body = builder.build()
        val request = Request.Builder().url(adr).post(body).headers(getHeaders(cxt)).build()
        val call = client.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val message = Message()
                message.what = 2
                message.obj = e.message
                handler.sendMessage(message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val message = Message()
                message.what = 1
                message.obj = response.body()!!.string()
                handler.sendMessage(message)
            }
        })
        Log.d("REQUEST", "POST: $adr")
    }

    override fun BeginPost(cxt:Context,adr: String, params: Map<String, Any>, listener: IReceivedListener<String>) {
        val handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == 1) {
                    Log.d("REQUEST", "RESULT: ${msg.obj}")
                    listener.onSucceed(msg.obj.toString())
                } else if (msg.what == 2) {
                    listener.onFailed(msg.obj.toString())
                }
            }
        }
        val client = OkHttpClient()
            .newBuilder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .build()
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        if (params.isNotEmpty()) {
            val entries = params.entries.iterator()
            while (entries.hasNext()) {
                val entry = entries.next()
                if (entry.value !is File) {
                    builder.addFormDataPart(entry.key, entry.value.toString())
                } else {
                    val file = entry.value as File
                    builder.addFormDataPart(entry.key, file.name, RequestBody.create(null, file))
                }
            }
        }
        val body = builder.build()
        val request = Request.Builder().url(adr).post(body).headers(getHeaders(cxt)).build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val message = Message()
                message.what = 2
                message.obj = e.message
                handler.sendMessage(message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val message = Message()
                message.what = 1
                message.obj = response.body()!!.string()
                handler.sendMessage(message)
            }
        })
        Log.d("REQUEST", "GET: $adr")
    }

    override fun BeginDelete(cxt: Context, adr: String, params: Map<String, Any>, listener: IReceivedListener<String>) {
        val handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == 1) {
                    Log.d("REQUEST", "RESULT: ${msg.obj}")
                    listener.onSucceed(msg.obj.toString())
                } else if (msg.what == 2) {
                    listener.onFailed(msg.obj.toString())
                }
            }
        }
        val client = OkHttpClient()
            .newBuilder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .build()
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        if (params.isNotEmpty()) {
            val entries = params.entries.iterator()
            while (entries.hasNext()) {
                val entry = entries.next()
                if (entry.value !is File) {
                    builder.addFormDataPart(entry.key, entry.value.toString())
                } else {
                    val file = entry.value as File
                    builder.addFormDataPart(entry.key, file.name, RequestBody.create(null, file))
                }
            }
        }
        val body = builder.build()
        val request = Request.Builder().url(adr).delete(body).headers(getHeaders(cxt)).build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val message = Message()
                message.what = 2
                message.obj = e.message
                handler.sendMessage(message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val message = Message()
                message.what = 1
                message.obj = response.body()!!.string()
                handler.sendMessage(message)
            }
        })
        Log.d("REQUEST", "GET: $adr")
    }

    override fun BeginPut(cxt: Context, adr: String, params: Map<String, Any>, listener: IReceivedListener<String>) {
        val handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == 1) {
                    Log.d("REQUEST", "RESULT: ${msg.obj}")
                    listener.onSucceed(msg.obj.toString())
                } else if (msg.what == 2) {
                    listener.onFailed(msg.obj.toString())
                }
            }
        }
        val client = OkHttpClient()
            .newBuilder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .build()
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        if (params.isNotEmpty()) {
            val entries = params.entries.iterator()
            while (entries.hasNext()) {
                val entry = entries.next()
                if (entry.value !is File) {
                    builder.addFormDataPart(entry.key, entry.value.toString())
                } else {
                    val file = entry.value as File
                    builder.addFormDataPart(entry.key, file.name, RequestBody.create(null, file))
                }
            }
        }
        val body = builder.build()
        val request = Request.Builder().url(adr).put(body).headers(getHeaders(cxt)).build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val message = Message()
                message.what = 2
                message.obj = e.message
                handler.sendMessage(message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val message = Message()
                message.what = 1
                message.obj = response.body()!!.string()
                handler.sendMessage(message)
            }
        })
        Log.d("REQUEST", "GET: $adr")
    }
}