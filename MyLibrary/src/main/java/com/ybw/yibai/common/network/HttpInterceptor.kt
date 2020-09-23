package com.huxin.common.network

import android.content.Context
import retrofit2.Call


// Http拦截器
class HttpInterceptor(val context: Context) : BaseInterceptors() {
    /*override fun onGetHeaders(): Map<String, String>? {
        super.onGetHeaders()
        val tokenModel = SPUtil.getValue(App.context!!, Constant.MODEL_TOKEN_MODEL, TokenModel::class.java)
        val info = CUtils.Device.getDeviceInfo(context)
        val map = hashMapOf<String, String>()
        map["platform"] = "2"
        map["version"] = info.version.toString()
        map["mobileModel"] = info.model.toString()
        map["Authorization"] = "Bearer ${tokenModel?.access_token}"
        return map
    }

    override fun onGetToken(): TokenModel? {
        val apiService: UserService = HttpClient.instance!!.retrofit!!.create(UserService::class.java)
        val call: Call<BaseResponse<TokenModel>> = apiService.onRefreshToken(AccountUtil.getToken(App.context!!))
        val responseBean: BaseResponse<TokenModel> = call.execute().body()!!
        if (responseBean.code == 200 && responseBean.data != null) { //获取新的token成功
            val tokenModel = responseBean.data
            SPUtil.putValue(context,Constant.MODEL_TOKEN_MODEL,tokenModel)
        }
        return responseBean.data
    }*/
}