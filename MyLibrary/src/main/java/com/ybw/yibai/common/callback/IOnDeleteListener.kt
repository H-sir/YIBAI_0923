package com.ybw.yibai.common.callback

interface IOnDeleteListener<T>{
    fun onDelete(index:Int,item:T)
}