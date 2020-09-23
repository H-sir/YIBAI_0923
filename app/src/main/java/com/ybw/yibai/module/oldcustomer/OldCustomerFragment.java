package com.ybw.yibai.module.oldcustomer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.FragmentPagerAdapter;
import com.ybw.yibai.common.bean.CustomerList;
import com.ybw.yibai.common.bean.CustomerList.DataBean;
import com.ybw.yibai.common.bean.CustomerList.DataBean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.oldcustomer.OldCustomerContract.OldCustomerPresenter;
import com.ybw.yibai.module.oldcustomer.OldCustomerContract.OldCustomerView;

import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_LIST;
import static com.ybw.yibai.common.utils.OtherUtil.splitList;
import static com.ybw.yibai.common.utils.ViewPagerIndicatorUtil.initDots;
import static com.ybw.yibai.common.utils.ViewPagerIndicatorUtil.onPageSelectedDots;

/**
 * 老客户Fragment
 *
 * @author sjl
 * @date 2019/9/16
 */
public class OldCustomerFragment extends BaseFragment implements OldCustomerView, ViewPager.OnPageChangeListener {

    /**
     *
     */
    private ViewPager mViewPager;

    /**
     * 指示点父布局
     */
    private LinearLayout mDotLinearLayout;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    private Context mContext;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private OldCustomerPresenter mOldCustomersPresenter;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        return R.layout.fragment_old_customer;
    }

    @Override
    protected void findViews(View view) {
        mViewPager = view.findViewById(R.id.viewPager);
        mDotLinearLayout = view.findViewById(R.id.dotLinearLayout);
    }

    @Override
    protected void initView() {
        mWaitDialog = new WaitDialog(mContext);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mOldCustomersPresenter = new OldCustomerPresenterImpl(this);
        mOldCustomersPresenter.getCustomerList();
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * ViewPager某一个页面被选中时回调
     *
     * @param position 当前滑动到的页面
     */
    @Override
    public void onPageSelected(int position) {
        onPageSelectedDots(mContext, mDotLinearLayout, position);
    }

    /**
     * ViewPager这个方法在手指操作屏幕的时候发生变化
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * ViewPager在屏幕滚动过程中不断被调用
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 在获取客户列表成功时回调
     *
     * @param customersList 客户列表数据
     */
    @Override
    public void onGetCustomerListSuccess(CustomerList customersList) {
        if (CODE_SUCCEED != customersList.getCode()) {
            MessageUtil.showMessage(customersList.getMsg());
            return;
        }
        DataBean data = customersList.getData();
        if (null == data) {
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        List<List<ListBean>> lists = splitList(list, 9);
        if (null == lists || lists.size() == 0) {
            return;
        }
        Fragment[] fragment = new Fragment[lists.size()];
        for (int i = 0; i < lists.size(); i++) {
            List<ListBean> beans = lists.get(i);
            fragment[i] = new OldCustomerListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(CUSTOMERS_LIST, new Gson().toJson(beans));
            fragment[i].setArguments(bundle);
        }
        initDots(mContext, mDotLinearLayout, lists.size());
        FragmentManager manager = getChildFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
    }

    /**
     * 在请求网络数据之前显示Loading界面
     */
    @Override
    public void onShowLoading() {
        if (!mWaitDialog.isShowing()) {
            mWaitDialog.setWaitDialogText(getResources().getString(R.string.loading));
            mWaitDialog.show();
        }
    }

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    @Override
    public void onHideLoading() {
        if (mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }

    /**
     * 在请求网络数据失败时进行一些操作,如显示错误信息...
     *
     * @param throwable 异常类型
     */
    @Override
    public void onLoadDataFailure(Throwable throwable) {
        ExceptionUtil.handleException(throwable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mOldCustomersPresenter) {
            mOldCustomersPresenter.onDetachView();
            mOldCustomersPresenter = null;
        }
    }
}
