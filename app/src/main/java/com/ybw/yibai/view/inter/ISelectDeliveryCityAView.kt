package com.ybw.yibai.view.inter

import com.ybw.yibai.common.network.response.CityItemResponse
import com.ybw.yibai.common.network.response.HotCityResponse

interface ISelectDeliveryCityAView : IBaseView{
    fun onGetCurCityCallback(city:String,code:String)
    fun onGetCitiesCallback(cities: HotCityResponse)
    fun onSetCityCallback(cityName:String,code:String)
}