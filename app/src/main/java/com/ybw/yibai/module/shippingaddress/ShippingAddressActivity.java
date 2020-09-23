package com.ybw.yibai.module.shippingaddress;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.adapter.ShippingAddressAdapter;
import com.ybw.yibai.common.bean.Address;
import com.ybw.yibai.common.bean.Address.DataBean;
import com.ybw.yibai.common.bean.Address.DataBean.ListBean;
import com.ybw.yibai.common.bean.DeleteShippingAddress;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.ShippingAddress;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.addshippingaddress.AddShippingAddressActivity;
import com.ybw.yibai.module.confirmorder.ConfirmOrderActivity;
import com.ybw.yibai.module.shippingaddress.ShippingAddressContract.ShippingAddressPresenter;
import com.ybw.yibai.module.shippingaddress.ShippingAddressContract.ShippingAddressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.SHIPPING_ADDRESS_INFO;

/**
 * 收货地址界面
 *
 * @author sjl
 */
public class ShippingAddressActivity extends BaseActivity implements View.OnClickListener,
        ShippingAddressView, ShippingAddressAdapter.OnItemClickListener {

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 显示收货地址
     */
    private RecyclerView mRecyclerView;

    /**
     * 新增收货地址
     */
    private Button mNewAddShippingAddressButton;

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
     * 收货地址的集合
     */
    private List<ListBean> mShippingAddressList;

    /**
     * 收货地址适配器
     */
    private ShippingAddressAdapter mAdapter;

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
    private ShippingAddressPresenter mShippingAddressPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_shipping_address;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mRecyclerView = findViewById(R.id.recyclerView);
        mNewAddShippingAddressButton = findViewById(R.id.newAddShippingAddressButton);

        mWaitDialog = new WaitDialog(this);
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        int spacing = DensityUtil.dpToPx(this, 1);
        // 设置RecyclerView间距
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(1, spacing, false);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);
        // 添加间距
        mRecyclerView.addItemDecoration(decoration);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mShippingAddressList = new ArrayList<>();

        mAdapter = new ShippingAddressAdapter(this, mShippingAddressList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mShippingAddressPresenter = new ShippingAddressPresenterImpl(this);
        mShippingAddressPresenter.getShippingAddress();
        mBackImageView.setOnClickListener(this);
        mNewAddShippingAddressButton.setOnClickListener(this);

        mAdapter.setOnItemClickListener(this);
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

        // 新增收获地址
        if (id == R.id.newAddShippingAddressButton) {
            Intent intent = new Intent(this, AddShippingAddressActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param position     被点击的Item位置
     * @param isEditLayout 点击的区域是否为"编辑"区域
     */
    @Override
    public void onItemClick(int position, boolean isEditLayout) {
        if (isEditLayout) {
            Intent intent = new Intent(this, AddShippingAddressActivity.class);
            intent.putExtra(SHIPPING_ADDRESS_INFO, mShippingAddressList.get(position));
            startActivity(intent);
        } else {
            /**
             *发送数据到{@link ConfirmOrderActivity#onSelectNewShippingAddress(ListBean)}
             */
            EventBus.getDefault().post(mShippingAddressList.get(position));
            finish();
        }
    }

    /**
     * 在获取收货地址成功时回调
     *
     * @param address 收获地址
     */
    @Override
    public void onGetShippingAddressSuccess(Address address) {
        if (CODE_SUCCEED != address.getCode()) {
            return;
        }
        mShippingAddressList.clear();
        DataBean data = address.getData();
        if (null == data) {
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().post(new ListBean());
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().post(new ListBean());
            return;
        }
        mShippingAddressList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * EventBus
     * 接收用户从{@link AddShippingAddressActivity#onCreateOrModifyShippingAddressSucceed(ShippingAddress)}传递过来的数据
     * 在在创建收货地址成功时
     *
     * @param shippingAddress 创建或修改收货地址
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateOrModifyShippingAddressSucceed(ShippingAddress shippingAddress) {
        mShippingAddressPresenter.getShippingAddress();
    }

    /**
     * EventBus
     * 接收用户从{@link AddShippingAddressActivity#onDeleteShippingAddressSucceed(DeleteShippingAddress)}传递过来的数据
     * 在在创建收货地址成功时
     *
     * @param deleteShippingAddress 在删除收货地址成功时
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteShippingAddress(DeleteShippingAddress deleteShippingAddress) {
        mShippingAddressPresenter.getShippingAddress();
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
        if (null != mShippingAddressPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mShippingAddressPresenter.onDetachView();
            mShippingAddressPresenter = null;
        }
    }
}