package com.ybw.yibai.presenter.base

abstract class BasePresenter<TM,TV>(protected val view:TV){
    protected val model = onGetModel()
    protected abstract fun onGetModel():TM
}