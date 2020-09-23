package com.ybw.yibai.presenter.inter

import android.location.Location

interface ISelectDeliveryCityAPresenter{
    fun onGetCurrentCity(loc:Location)
    fun onGetAllCities(phone:String)
    fun onSetCity(code:String)
}