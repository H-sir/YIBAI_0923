/*
 * Copyright (c) 2020/1/7. by Onlly
 * 个人简介：野生程序员
 * QQ:412102100
 * WX:i412102100
 * Github: Github.com/mingyouzhu
 * 简书: https://www.jianshu.com/u/4198eba0fbdd
 */

package com.ybw.yibai.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.soft.onlly.toastplus.ToastPlus
import com.ybw.yibai.R
import com.ybw.yibai.common.permissionhelper.ActivityManagePermission
import com.ybw.yibai.common.utils.ExceptionUtil
import com.ybw.yibai.common.utils.OtherUtil
import com.ybw.yibai.common.widget.WaitDialog
import com.ybw.yibai.view.inter.IBaseView

abstract class BaseActivity<T> : ActivityManagePermission(), IBaseView {

    protected var mWaitDialog:WaitDialog? = null

    protected var presenter: T = onGetPresenter()

    protected var isActive: Boolean? = null

    protected abstract fun onGetPresenter(): T

    protected abstract fun onGetLayoutRes(): Int

    protected abstract fun onInit(savedInstanceState: Bundle?)

    open fun onBindCtrlInstance() {

    }

    open fun startActivity(clazz: Class<*>) {
        val intent = Intent(baseContext!!, clazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    open fun startActivity(clazz: Class<*>, bundle: Bundle) {
        val intent = Intent(baseContext!!, clazz)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onGetContext(): Context {
        return this
    }

    override fun onFinish() {
        super.finish()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(onGetLayoutRes())

        actionBar?.hide()

        mWaitDialog = WaitDialog(onGetContext())

        // 设置状态栏成白色的背景,字体颜色为黑色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.white))
        }

        onBindCtrlInstance()
        onInit(savedInstanceState)
    }

    override fun onShowToastLength(msg: String?) {
        msg?.let {
            ToastPlus.show(this, it, true)
        }
    }

    override fun onShowToastShort(msg: String?) {
        msg?.let {
            ToastPlus.show(this, it, false)
        }
    }

    override fun onRequestBefore() {

    }

    override fun onRequestComplete() {

    }

    override fun onRequestFailure(throwable: Throwable?) {

    }

    override fun onShowLoading() {
        mWaitDialog?.let{
            if (!it.isShowing){
                it.setWaitDialogText(resources.getString(com.ybw.yibai.R.string.loading))
                it.show()
            }
        }
    }

    override fun onHideLoading() {
        mWaitDialog?.let {
            if(it.isShowing){
                it.dismiss()
            }
        }
    }

    override fun onLoadDataFailure(throwable: Throwable?) {
        ExceptionUtil.handleException(throwable)
    }
}
