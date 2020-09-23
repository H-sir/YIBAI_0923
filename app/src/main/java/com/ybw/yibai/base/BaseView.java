package com.ybw.yibai.base;

/**
 * 基类的View
 * View.interface用来定义View的行为,由Activity或者是Fragment来实现这个接口
 *
 * @author sjl
 */
public interface BaseView {

    /**
     * 在请求网络数据之前显示Loading界面
     */
    void onShowLoading();

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    void onHideLoading();

    /**
     * 在请求网络数据失败时进行一些操作,如显示错误信息...
     *
     * @param throwable 异常类型
     */
    void onLoadDataFailure(Throwable throwable);
}
