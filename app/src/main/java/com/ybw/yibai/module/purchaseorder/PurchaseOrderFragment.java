package com.ybw.yibai.module.purchaseorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.PurchaseOrderAdapter;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.PurchaseOrderList;
import com.ybw.yibai.common.bean.PurchaseOrderList.DataBean;
import com.ybw.yibai.common.bean.PurchaseOrderList.DataBean.ListBean;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.browser.BrowserActivity;
import com.ybw.yibai.module.purchaseorder.PurchaseOrderContract.PurchaseOrderPresenter;
import com.ybw.yibai.module.purchaseorder.PurchaseOrderContract.PurchaseOrderView;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.URL;
import static com.ybw.yibai.common.utils.DensityUtil.dpToPx;

/**
 * 已进货订单
 *
 * @author sjl
 * @date 2019/10/14
 */
public class PurchaseOrderFragment extends BaseFragment implements PurchaseOrderView, PurchaseOrderAdapter.OnItemClickListener {

    /**
     * 0分页1不分页默认为1
     */
    private int isAll = 1;

    /**
     * 当前Fragment在ViewPager中的位置
     */
    private int position;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * RootLayout
     */
    private View mRootLayout;

    /**
     * 显示进货订单列表
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
     * 已进货订单列表
     */
    private List<ListBean> mPurchaseOrderList;

    /**
     * 已进货订单适配器
     */
    private PurchaseOrderAdapter mAdapter;

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
    private PurchaseOrderPresenter mPurchaseOrderPresenter;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        return R.layout.fragment_purchase_order;
    }

    @Override
    protected void findViews(View view) {
        mRootLayout = view.findViewById(R.id.rootLayout);
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
        int spacing = dpToPx(mContext, 16);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(1, spacing, false);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        mPurchaseOrderList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (null != bundle) {
            position = bundle.getInt(POSITION);
        }

        mAdapter = new PurchaseOrderAdapter(mContext, mPurchaseOrderList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initEvent() {
        mPurchaseOrderPresenter = new PurchaseOrderPresenterImpl(this);
        mPurchaseOrderPresenter.getPurchaseOrderList(isAll, position);
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
        ListBean listBean = mPurchaseOrderList.get(position);
        String share = listBean.getShare();
        if (TextUtils.isEmpty(share)) {
            return;
        }
        Intent intent = new Intent(mContext, BrowserActivity.class);
        intent.putExtra(URL, share);
        startActivity(intent);
    }

    /**
     * 在获取获取采购单列表成功时回调
     *
     * @param purchaseOrderList 采购单列表
     */
    @Override
    public void onGetPurchaseOrderListSuccess(PurchaseOrderList purchaseOrderList) {
        if (CODE_SUCCEED != purchaseOrderList.getCode()) {
            return;
        }
        mPurchaseOrderList.clear();
        DataBean data = purchaseOrderList.getData();
        if (null == data) {
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        mPurchaseOrderList.addAll(list);
        mAdapter.notifyDataSetChanged();
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
        if (null != mPurchaseOrderPresenter) {
            mPurchaseOrderPresenter.onDetachView();
            mPurchaseOrderPresenter = null;
        }
    }
}
