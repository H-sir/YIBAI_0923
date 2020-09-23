package com.ybw.yibai.module.addshippingaddress;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.Address.DataBean.ListBean;
import com.ybw.yibai.common.bean.CityAreaList;
import com.ybw.yibai.common.bean.DeleteShippingAddress;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.ShippingAddress;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.addshippingaddress.AddShippingAddressContract.AddShippingAddressPresenter;
import com.ybw.yibai.module.addshippingaddress.AddShippingAddressContract.AddShippingAddressView;
import com.ybw.yibai.module.shippingaddress.ShippingAddressActivity;

import org.greenrobot.eventbus.EventBus;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.SHIPPING_ADDRESS_INFO;

/**
 * 添加收货地址
 *
 * @author sjl
 * @date 2019/10/11
 */
public class AddShippingAddressActivity extends BaseActivity implements AddShippingAddressView,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    /**
     * 区Id
     */
    private int areaId;

    /**
     * 是否默认收货地址(1是0否)
     */
    private int isDefault;

    /**
     * 收货地址id(有此字段表示修改)
     */
    private Integer addressId;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 保存
     */
    private TextView mSaveTextView;

    /**
     * 联系人
     */
    private EditText mConsigneeEditText;

    /**
     * 手机号码
     */
    private EditText mPhoneNumberEditText;

    /**
     * 区域
     */
    private View mAreaView;

    /**
     * 区域
     */
    private EditText mAreaEditText;

    /**
     * 详细地址
     */
    private EditText mDetailedAddressEditText;

    /**
     *
     */
    private ToggleButton mToggleButton;

    /**
     * 删除收货地址
     */
    private TextView mDeleteShippingAddressTextView;

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
     * 收货地址信息
     */
    private ListBean mShippingAddressInfo;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private AddShippingAddressPresenter mAddShippingAddressPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_add_shipping_address;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mSaveTextView = findViewById(R.id.saveTextView);
        mConsigneeEditText = findViewById(R.id.consigneeEditText);
        mPhoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        mAreaView = findViewById(R.id.areaView);
        mAreaEditText = findViewById(R.id.areaEditText);
        mDetailedAddressEditText = findViewById(R.id.detailedAddressEditText);
        mToggleButton = findViewById(R.id.toggleButton);
        mDeleteShippingAddressTextView = findViewById(R.id.deleteShippingAddressTextView);
        mWaitDialog = new WaitDialog(this);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mShippingAddressInfo = (ListBean) getIntent().getSerializableExtra(SHIPPING_ADDRESS_INFO);
        if (null != mShippingAddressInfo) {
            areaId = mShippingAddressInfo.getArea_id();
            addressId = mShippingAddressInfo.getAddr_id();
            String consignee = mShippingAddressInfo.getConsignee();
            String mobile = mShippingAddressInfo.getMobile();
            String province = mShippingAddressInfo.getProvince();
            String city = mShippingAddressInfo.getCity();
            String area = mShippingAddressInfo.getArea();
            String address = mShippingAddressInfo.getAddress();
            int isDefault = mShippingAddressInfo.getIsdefault();

            if (!TextUtils.isEmpty(consignee)) {
                mConsigneeEditText.setText(consignee);
                mConsigneeEditText.setSelection(consignee.length());
            }
            if (!TextUtils.isEmpty(mobile)) {
                mPhoneNumberEditText.setText(mobile);
                mPhoneNumberEditText.setSelection(mobile.length());
            }
            if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(area)) {
                String text = province + "\n" + city + "\n" + area;
                mAreaEditText.setText(text);
                mAreaEditText.setSelection(text.length());
            }
            if (!TextUtils.isEmpty(address)) {
                mDetailedAddressEditText.setText(address);
                mDetailedAddressEditText.setSelection(address.length());
            }
            if (1 == isDefault) {
                mToggleButton.setChecked(true);
            } else {
                mToggleButton.setChecked(false);
            }
            mDeleteShippingAddressTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initEvent() {
        mAddShippingAddressPresenter = new AddShippingAddressPresenterImpl(this);
        mAddShippingAddressPresenter.getCityAreaList();

        mBackImageView.setOnClickListener(this);
        mSaveTextView.setOnClickListener(this);
        mAreaView.setOnClickListener(this);
        mDeleteShippingAddressTextView.setOnClickListener(this);

        mToggleButton.setOnCheckedChangeListener(this);
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

        // 保存
        if (id == R.id.saveTextView) {
            String consignee = mConsigneeEditText.getText().toString().trim();
            String mobile = mPhoneNumberEditText.getText().toString().trim();
            String address = mDetailedAddressEditText.getText().toString().trim();
            mAddShippingAddressPresenter.createOrModifyShippingAddress(addressId, areaId, consignee, mobile, address, isDefault);
        }

        // 初始化区域View
        if (id == R.id.areaView) {
            mAddShippingAddressPresenter.initAreaSelectView();
        }

        // 删除收货地址
        if (id == R.id.deleteShippingAddressTextView) {
            mAddShippingAddressPresenter.deleteShippingAddress(addressId);
        }
    }

    /**
     * ToggleButton 选择状态发生改变是回调
     *
     * @param buttonView 被点击的ToggleButton对象
     * @param isChecked  是否选中
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!buttonView.isPressed()) {
            return;
        }
        if (isChecked) {
            isDefault = 1;
        } else {
            isDefault = 0;
        }
    }

    /**
     * 在获取城市区域列表成功时回调
     *
     * @param cityAreaList 城市区域列表数据
     */
    @Override
    public void onGetCityAreaListSucceed(CityAreaList cityAreaList) {

    }

    /**
     * 返回用户选择的区域Id和名称
     *
     * @param provinceId   省Id
     * @param cityId       市Id
     * @param areaId       区Id
     * @param provinceName 省名称
     * @param cityName     市名称
     * @param areaName     区名称
     */
    @Override
    public void returnUserSelectArea(int provinceId, int cityId, int areaId, String provinceName, String cityName, String areaName) {
        this.areaId = areaId;
        String text = provinceName + "\n" + cityName + "\n" + areaName;
        mAreaEditText.setText(text);
    }

    /**
     * 在创建收货地址成功时回调
     *
     * @param shippingAddress 创建或修改收货地址
     */
    @Override
    public void onCreateOrModifyShippingAddressSucceed(ShippingAddress shippingAddress) {
        MessageUtil.showMessage(shippingAddress.getMsg());
        if (CODE_SUCCEED != shippingAddress.getCode()) {
            return;
        }
        /**
         * 发送数据到{@link ShippingAddressActivity#onCreateOrModifyShippingAddressSucceed(ShippingAddress)}
         */
        EventBus.getDefault().post(shippingAddress);
        finish();
    }

    /**
     * 在删除收货地址成功时回调
     *
     * @param deleteShippingAddress 删除收货地址时服务器端返回的数据
     */
    @Override
    public void onDeleteShippingAddressSucceed(DeleteShippingAddress deleteShippingAddress) {
        MessageUtil.showMessage(deleteShippingAddress.getMsg());
        if (CODE_SUCCEED != deleteShippingAddress.getCode()) {
            return;
        }
        /**
         * 发送数据到{@link ShippingAddressActivity#onDeleteShippingAddress(DeleteShippingAddress)}
         */
        EventBus.getDefault().post(deleteShippingAddress);
        finish();
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
        if (null != mAddShippingAddressPresenter) {
            mAddShippingAddressPresenter.onDetachView();
            mAddShippingAddressPresenter = null;
        }
    }
}
