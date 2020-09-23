package com.ybw.yibai.module.confirmorder;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.OrderAdapter;
import com.ybw.yibai.common.bean.Address;
import com.ybw.yibai.common.bean.Address.DataBean;
import com.ybw.yibai.common.bean.Address.DataBean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.PurchaseOrder;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.browser.BrowserActivity;
import com.ybw.yibai.module.confirmorder.ConfirmOrderContract.ConfirmOrderPresenter;
import com.ybw.yibai.module.confirmorder.ConfirmOrderContract.ConfirmOrderView;
import com.ybw.yibai.module.purchase.PurchaseFragment;
import com.ybw.yibai.module.shippingaddress.ShippingAddressActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.QUOTATION_LIST;
import static com.ybw.yibai.common.constants.Preferences.URL;

/**
 * 确认进货订单
 *
 * @author sjl
 * @date 2019/10/10
 */
public class ConfirmOrderActivity extends BaseActivity implements ConfirmOrderView, View.OnClickListener {

    /**
     * 收获地址id
     */
    private int addressId;

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 收货地址
     */
    private RelativeLayout mAddressLayout;

    /**
     * 姓名与电话
     */
    private TextView mNameTextView;

    /**
     * 地址
     */
    private TextView mAddressTextView;

    /**
     * 显示订单列表
     */
    private RecyclerView mRecyclerView;

    /**
     * 订单数量
     */
    private TextView mNumberTextView;

    /**
     * 订单价格
     */
    private TextView mPriceTextView;

    /**
     * 订单总价格
     */
    private TextView mTradePriceTextView;

    /**
     * 提交订单
     */
    private TextView mSubmitOrderTextView;

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
     * 用户保存的"报价"数据列表
     */
    protected List<QuotationData> mQuotationDataList;

    /**
     * 显示进货订单列表的适配器
     */
    private OrderAdapter mAdapter;

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
    private ConfirmOrderPresenter mConfirmOrderPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_confirm_order;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mAddressLayout = findViewById(R.id.addressLayout);
        mNameTextView = findViewById(R.id.nameTextView);
        mAddressTextView = findViewById(R.id.addressTextView);
        mRecyclerView = findViewById(R.id.recyclerView);
        mNumberTextView = findViewById(R.id.numberTextView);
        mPriceTextView = findViewById(R.id.priceTextView);
        mTradePriceTextView = findViewById(R.id.tradePriceTextView);
        mSubmitOrderTextView = findViewById(R.id.submitOrderTextView);

        mWaitDialog = new WaitDialog(this);
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        String jsonString = getIntent().getStringExtra(QUOTATION_LIST);
        mQuotationDataList = new Gson().fromJson(jsonString, new TypeToken<List<QuotationData>>() {
        }.getType());

        mAdapter = new OrderAdapter(this, mQuotationDataList);
        mRecyclerView.setAdapter(mAdapter);

        int sum = 0;
        if (null != mQuotationDataList && mQuotationDataList.size() > 0) {
            for (QuotationData quotationData : mQuotationDataList) {
                sum += quotationData.getNumber();
            }
        }
        String numberString = getResources().getString(R.string.general) + sum + getResources().getString(R.string.items) + "," + getResources().getString(R.string.total);
        mNumberTextView.setText(numberString);
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mConfirmOrderPresenter = new ConfirmOrderPresenterImpl(this);
        mConfirmOrderPresenter.getTotalTradePrice(mQuotationDataList);
        mConfirmOrderPresenter.getShippingAddress();

        mBackImageView.setOnClickListener(this);
        mAddressLayout.setOnClickListener(this);
        mSubmitOrderTextView.setOnClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 返回
        if (id == R.id.backImageView) {
            onBackPressed();
        }

        // 收货地址
        if (id == R.id.addressLayout) {
            Intent intent = new Intent(this, ShippingAddressActivity.class);
            startActivity(intent);
        }

        // 直接结算
        if (id == R.id.submitOrderTextView) {
            mConfirmOrderPresenter.createPurchaseOrder(mQuotationDataList, addressId);
        }
    }

    /**
     * 在计算总批发价格成功时回调
     *
     * @param totalTradePrice 总批发价格
     */
    @Override
    public void onGetTotalTradePriceSuccess(double totalTradePrice) {
        String priceString = YiBaiApplication.getCurrencySymbol() + totalTradePrice;
        mPriceTextView.setText(priceString);
        mTradePriceTextView.setText(priceString);
    }

    /**
     * 在获取收货地址成功时回调
     *
     * @param shippingAddress 收获地址
     */
    @Override
    public void onGetShippingAddressSuccess(Address shippingAddress) {
        if (CODE_SUCCEED != shippingAddress.getCode()) {
            return;
        }
        DataBean data = shippingAddress.getData();
        if (null == data) {
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        ListBean listBean = null;
        for (ListBean lb : list) {
            if (1 == lb.getIsdefault()) {
                listBean = lb;
                break;
            }
        }
        if (null == listBean) {
            if (list.size() >= 1) {
                // 没有默认的收货地址,就选中第一个
                listBean = list.get(0);
            } else {
                return;
            }
        }
        addressId = listBean.getAddr_id();
        String consignee = listBean.getConsignee();
        String mobile = listBean.getMobile();
        String province = listBean.getProvince();
        String city = listBean.getCity();
        String area = listBean.getArea();
        String address = listBean.getAddress();

        String consigneeMobile = consignee + "  " + mobile;
        if (!TextUtils.isEmpty(consigneeMobile)) {
            mNameTextView.setText(consigneeMobile);
        }
        String addressString = province + city + area + address;
        if (!TextUtils.isEmpty(addressString)) {
            mAddressTextView.setText(addressString);
        }
    }

    /**
     * 在创建采购订单成功时回调
     *
     * @param purchaseOrder 创建采购订单时服务器端返回的数据
     */
    @Override
    public void onCreatePurchaseOrderSuccess(PurchaseOrder purchaseOrder) {
        MessageUtil.showMessage(purchaseOrder.getMsg());
        /**
         * 发送事件通知"报价界面"通知其刷新UI
         * {@link PurchaseFragment#onCreatePurchaseOrderSuccess(PurchaseOrder)}
         */
        EventBus.getDefault().post(purchaseOrder);
        if (CODE_SUCCEED != purchaseOrder.getCode()) {
            return;
        }
        PurchaseOrder.DataBean data = purchaseOrder.getData();
        if (null == data) {
            return;
        }
        String share = data.getShare();
        if (TextUtils.isEmpty(share)) {
            return;
        }
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra(URL, share);
        startActivity(intent);
        finish();
    }

    /**
     * EventBus
     * 接收用户从{@link ShippingAddressActivity#onItemClick(int, boolean)}传递过来的数据
     * 在选中新的收货地址时
     *
     * @param listBean 新的收货地址时
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectNewShippingAddress(ListBean listBean) {
        addressId = listBean.getAddr_id();
        String consignee = listBean.getConsignee();
        String mobile = listBean.getMobile();
        String province = listBean.getProvince();
        String city = listBean.getCity();
        String area = listBean.getArea();
        String address = listBean.getAddress();

        if (!TextUtils.isEmpty(consignee) && !TextUtils.isEmpty(mobile)) {
            String consigneeMobile = consignee + "  " + mobile;
            if (!TextUtils.isEmpty(consigneeMobile)) {
                mNameTextView.setText(consigneeMobile);
            }
        } else {
            mNameTextView.setText(" ");
        }

        if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city) &&
                !TextUtils.isEmpty(area) && !TextUtils.isEmpty(address)) {
            String addressString = province + city + area + address;
            if (!TextUtils.isEmpty(addressString)) {
                mAddressTextView.setText(addressString);
            }
        } else {
            mAddressTextView.setText(" ");
        }
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
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mConfirmOrderPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mConfirmOrderPresenter.onDetachView();
            mConfirmOrderPresenter = null;
        }
    }
}
