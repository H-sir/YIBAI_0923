package com.ybw.yibai.module.purchase;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.PurchaseAdapter;
import com.ybw.yibai.common.bean.HiddenChanged;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.PurchaseOrder;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.module.confirmorder.ConfirmOrderActivity;
import com.ybw.yibai.module.purchase.PurchaseContract.PurchasePresenter;
import com.ybw.yibai.module.purchase.PurchaseContract.PurchaseView;
import com.ybw.yibai.module.quotationpurchase.QuotationPurchaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.QUOTATION_LIST;

/**
 * 进货Fragment
 *
 * @author sjl
 * @date 2019/10/29
 */
public class PurchaseFragment extends BaseFragment implements PurchaseView, View.OnClickListener,
        PurchaseAdapter.OnItemLongClickListener, PurchaseAdapter.OnNumberChangeListener {

    /**
     * 显示报价的RecyclerView
     */
    private RecyclerView mQuotationRecyclerView;

    /**
     * 显示直接结算UI
     */
    private RelativeLayout mDirectClearingLayout;

    /**
     * 显示批发价
     */
    private TextView mTradePriceTextView;

    /**
     * 直接结算
     */
    private TextView mDirectClearingTextView;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 用户保存的"进货"数据列表
     */
    protected List<QuotationData> mQuotationDataList;

    /**
     * 显示报价列表的适配器
     */
    protected PurchaseAdapter mAdapter;

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
     * Activity对象
     */
    private FragmentActivity mActivity;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private PurchasePresenter mPurchasePresenter;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        mActivity = getActivity();
        return R.layout.fragment_purchase;
    }

    @Override
    protected void findViews(View view) {
        mQuotationRecyclerView = view.findViewById(R.id.recyclerView);
        mDirectClearingLayout = view.findViewById(R.id.directClearingLayout);
        mTradePriceTextView = view.findViewById(R.id.tradePriceTextView);
        mDirectClearingTextView = view.findViewById(R.id.directClearingTextView);
    }

    @Override
    protected void initView() {
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // 给RecyclerView设置布局管理器(必须设置)
        mQuotationRecyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initData() {
        mQuotationDataList = new ArrayList<>();
        mAdapter = new PurchaseAdapter(mContext, mQuotationDataList);
        mQuotationRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemLongClickListener(this);
        mAdapter.setOnNumberChangeListener(this);
    }

    @Override
    protected void initEvent() {
        mPurchasePresenter = new PurchasePresenterImpl(this);
        mDirectClearingTextView.setOnClickListener(this);

        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 直接结算
        if (id == R.id.directClearingTextView) {
            Intent intent = new Intent(mContext, ConfirmOrderActivity.class);
            intent.putExtra(QUOTATION_LIST, new Gson().toJson(mQuotationDataList));
            startActivity(intent);
            mActivity.overridePendingTransition(R.anim.activity_open, android.R.anim.fade_out);
        }
    }

    /**
     * 在RecyclerView Item 长按时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemLongClick(int position) {
        QuotationData quotationData = mQuotationDataList.get(position);
        mPurchasePresenter.displayAreYouSureToDeleteTheBonsaiDialog(quotationData);
    }

    /**
     * 在OrderEditText数量发生改变时回调
     *
     * @param position 位置
     */
    @Override
    public void onNumberChange(int position) {
        QuotationData quotationData = mQuotationDataList.get(position);
        mPurchasePresenter.updateQuotationData(quotationData);
    }

    /**
     * EventBus
     * 接收用户从{@link QuotationPurchaseFragment#onHiddenChanged(boolean)} 传递过来的数据
     * 在Fragment隐藏状态发生改变时
     *
     * @param hiddenChanged Fragment隐藏状态(true Fragment隐藏,false Fragment显示)
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onFragmentHiddenChanged(HiddenChanged hiddenChanged) {
        boolean hidden = hiddenChanged.isHidden();
        if (hidden) {
            return;
        }
        // 获取用户保存的"报价"数据
        mPurchasePresenter.getQuotationData();
    }

    /**
     * 在获取用户保存的"报价"数据成功时回调
     *
     * @param quotationDataList 用户保存的"报价"数据列表
     */
    @Override
    public void onGetQuotationDataSuccess(List<QuotationData> quotationDataList) {
        if (null != quotationDataList && quotationDataList.size() > 0) {
            mQuotationDataList.clear();
            mQuotationDataList.addAll(quotationDataList);
            mAdapter.notifyDataSetChanged();
            mPurchasePresenter.getTotalTradePrice(mQuotationDataList);
            mDirectClearingLayout.setVisibility(View.VISIBLE);
        } else {
            mQuotationDataList.clear();
            mAdapter.notifyDataSetChanged();
            mTradePriceTextView.setText(" ");
            mDirectClearingLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 在更新用户保存的"报价"数据完成时回调
     *
     * @param result 结果 true成功/ false失败
     */
    @Override
    public void onUpdateQuotationDataFinish(boolean result) {
        if (result){
            MessageUtil.showMessage(getResources().getString(R.string.modify_successfully));
        }else {
            MessageUtil.showMessage(getResources().getString(R.string.modify_unsuccessfully));
        }
        // 重新获取用户保存的"报价"数据
        mPurchasePresenter.getQuotationData();
    }

    /**
     * 在删除用户保存的"报价"数据完成时回调
     *
     * @param result 结果 true成功/ false失败
     */
    @Override
    public void onDeleteQuotationDataFinish(boolean result) {
        if (result){
            MessageUtil.showMessage(getResources().getString(R.string.successfully_delete));
        }else {
            MessageUtil.showMessage(getResources().getString(R.string.unsuccessfully_delete));
        }
        // 重新获取用户保存的"报价"数据
        mPurchasePresenter.getQuotationData();
    }

    /**
     * 在计算总批发价格成功时回调
     *
     * @param totalTradePrice 总批发价格
     */
    @Override
    public void onGetTotalTradePriceSuccess(double totalTradePrice) {
        String text = YiBaiApplication.getCurrencySymbol() + totalTradePrice;
        mTradePriceTextView.setText(text);
    }

    /**
     * EventBus
     * 接收用户从{@link ConfirmOrderActivity#onCreatePurchaseOrderSuccess(PurchaseOrder)}
     * 发送过来的数据(在创建采购订单成功时)
     *
     * @param purchaseOrder 创建采购订单时服务器端返回的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatePurchaseOrderSuccess(PurchaseOrder purchaseOrder) {
        /**
         * 因为在创建订单成功时删除用户保存的"报价"数据,所以要在这里重新刷新UI
         *{@link ConfirmOrderPresenterImpl#onCreatePurchaseOrderSuccess(PurchaseOrder)}
         */
        if (CODE_SUCCEED == purchaseOrder.getCode()) {
            // 获取用户保存的"报价"数据
            mPurchasePresenter.getQuotationData();
        }
    }

    @Override
    public void onShowLoading() {

    }

    @Override
    public void onHideLoading() {

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
    public void onLoadDataFailure(Throwable throwable) {
        if (null != mPurchasePresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mPurchasePresenter.onDetachView();
            mPurchasePresenter = null;
        }
    }
}
