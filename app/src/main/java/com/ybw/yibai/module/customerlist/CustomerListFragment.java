package com.ybw.yibai.module.customerlist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.CustomerListAdapter;
import com.ybw.yibai.common.bean.CustomerList;
import com.ybw.yibai.common.bean.CustomerList.DataBean;
import com.ybw.yibai.common.bean.CustomerList.DataBean.ListBean;
import com.ybw.yibai.common.bean.DeleteCustomer;
import com.ybw.yibai.common.bean.EditCustomer;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.classs.GridSpacingItemDecorations;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.customerdetails.CustomerDetailsActivity;
import com.ybw.yibai.module.customerlist.CustomerListContract.CustomerListPresenter;
import com.ybw.yibai.module.customerlist.CustomerListContract.CustomerListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMER_INFO;
import static com.ybw.yibai.common.utils.DensityUtil.dpToPx;

/**
 * 客户列表
 *
 * @author sjl
 * @date 2019/10/14
 */
public class CustomerListFragment extends BaseFragment implements CustomerListView, CustomerListAdapter.OnItemClickListener {

    /**
     * 显示客户列表
     */
    private RecyclerView mRecyclerView;

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

    /**
     * 客户列表
     */
    private List<ListBean> mCustomerList;

    /**
     * 客户列表
     */
    private CustomerListAdapter mAdapter;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private CustomerListPresenter mCustomerListPresenter;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        return R.layout.fragment_customer_list;
    }

    @Override
    protected void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    protected void initView() {
        mWaitDialog = new WaitDialog(mContext);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        // 布局水平
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        // 设置RecyclerView间距
        int leftRightSpacing = dpToPx(mContext, 16);
        int topBottomSpacing = dpToPx(mContext, 10);
        GridSpacingItemDecorations decoration = new GridSpacingItemDecorations(1, leftRightSpacing, topBottomSpacing, false);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        mCustomerList = new ArrayList<>();

        mAdapter = new CustomerListAdapter(mContext, mCustomerList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mCustomerListPresenter = new CustomerListPresenterImpl(this);
        mCustomerListPresenter.getCustomerList();
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(mContext, CustomerDetailsActivity.class);
        intent.putExtra(CUSTOMER_INFO, mCustomerList.get(position));
        startActivity(intent);
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
        mCustomerList.clear();
        DataBean data = customersList.getData();
        if (null == data) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        mCustomerList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * EventBus
     * 接收用户从{@link CustomerDetailsActivity#onEditCustomerSuccess(EditCustomer)}
     * 发送过来的数据(在修改客户信息成功时)
     *
     * @param editCustomer 在修改客户信息成功时回调服务器端返回的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEditCustomerSuccess(EditCustomer editCustomer) {
        // 重新获取客户列表
        mCustomerListPresenter.getCustomerList();
    }

    /**
     * EventBus
     * 接收用户从{@link CustomerDetailsActivity#onDeleteCustomerSuccess(DeleteCustomer)}
     * 发送过来的数据(在删除客户成功时)
     *
     * @param deleteCustomer 在删除客户成功时服务器端返回的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteCustomerSuccess(DeleteCustomer deleteCustomer) {
        // 重新获取客户列表
        mCustomerListPresenter.getCustomerList();
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

    /**
     * 友盟统计Fragment页面
     */
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    /**
     * 友盟统计Fragment页面
     */
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mCustomerListPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mCustomerListPresenter.onDetachView();
            mCustomerListPresenter = null;
        }
    }
}
