package com.ybw.yibai.module.city;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.adapter.CityListAdapter;
import com.ybw.yibai.common.adapter.SelectAddressListAdapter;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.bean.MarketListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.PlaceBean;
import com.ybw.yibai.common.bean.UserPosition;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.utils.AndroidUtils;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.LocationUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.nestlistview.NestFullListView;
import com.ybw.yibai.common.widget.nestlistview.NestFullListViewAdapter;
import com.ybw.yibai.common.widget.nestlistview.NestFullViewHolder;
import com.ybw.yibai.module.change.ChangeAddressActivity;
import com.ybw.yibai.module.main.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static com.ybw.yibai.common.constants.Preferences.COM_OPEN;
import static com.ybw.yibai.common.constants.Preferences.MARKET_ID;
import static com.ybw.yibai.common.constants.Preferences.MARKET_NAME;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * <pre>
 *     author : HKR
 *     time   : 2021/03/22
 *     desc   :
 * </pre>
 */
public class SelectAddressActivity extends BaseActivity implements CityContract.CityView, SelectAddressListAdapter.OnCityClickListener {
    private SelectAddressActivity mSelectAddressActivity = null;

    @BindView(R.id.cityCurrent)
    TextView cityCurrent;
    @BindView(R.id.noCityListView)
    TextView noCityListView;
    @BindView(R.id.cityListView)
    NestFullListView cityListView;
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.rootLayout)
    NestedScrollView rootLayout;
    @BindView(R.id.productSettingName)
    TextView productSettingName;

    private CityContract.CityPresenter mCityPresenter = null;


    /**
     * 要申请的权限(允许一个程序访问CellID或WiFi热点来获取粗略的位置
     * 允许应用程序访问精确位置(如GPS))
     */
    private String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    /**
     * 百度定位工具类
     */
    private LocationUtil mLocationInstance;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;
    /**
     * 设置的信息
     */
    private String cityName = "";
    private SharedPreferences mSharedPreferences;

    @Override
    protected int setLayout() {
        mSelectAddressActivity = this;
        return R.layout.select_address_layout;
    }

    @Override
    protected void initView() {

    }

    /**
     * 设计详情列表适配器
     */
    private NestFullListViewAdapter mNestFullListViewAdapter;

    @Override
    protected void initData() {
        mSharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String cityName = mSharedPreferences.getString(COM_OPEN, "1");
        if (cityName.equals("1")) {
            productSettingName.setText("展示所有产品");
        } else {
            productSettingName.setText("仅展示有供货的产品");
        }

        mWaitDialog = new WaitDialog(this);
    }

    @Override
    protected void initEvent() {
        mCityPresenter = new CityPresenterImpl(this);
        String city = SceneHelper.getCity(getApplicationContext());
        LogUtil.e("ChangeAddress", "city:" + city);
        if (city.equals("全国")) {
            mCityPresenter.applyPermissions(permissions);
        } else {
            cityCurrent.setText(city);
            String latitude = SceneHelper.getLatitude(getApplicationContext());
            String longitude = SceneHelper.getLongitude(getApplicationContext());
            if (latitude.equals("") || longitude.equals("")) {
                LogUtil.e("ChangeAddress", "latitude:" + true);
                mCityPresenter.applyPermissions(permissions);
            } else {
                LogUtil.e("ChangeAddress", "latitude:" + false);
                mCityPresenter.getMarketList(Double.valueOf(latitude), Double.valueOf(longitude));
            }
        }
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCityClick(int position) {
    }


    @Override
    public void onGetLocationSuccess(PlaceBean placeBean) {

    }

    /**
     * 市场列表
     */
    @Override
    public void onGetMarketListSuccess(MarketListBean marketListBean) {
        if (marketListBean.getData().getList().size() > 0) {
            String marketId = mSharedPreferences.getString(MARKET_ID, "0");
            for (Iterator<MarketListBean.DataBean.ListBean> iterator = marketListBean.getData().getList().iterator(); iterator.hasNext(); ) {
                MarketListBean.DataBean.ListBean bean = iterator.next();
                if (marketId.equals(String.valueOf(bean.getMarketId()))) {
                    bean.setCheck(true);
                }
                break;
            }
            mNestFullListViewAdapter = new NestFullListViewAdapter<MarketListBean.DataBean.ListBean>(
                    R.layout.listview_select_address_list_item_layout, marketListBean.getData().getList()) {
                @Override
                public void onBind(int pos, MarketListBean.DataBean.ListBean listBean, NestFullViewHolder holder) {
                    LinearLayout selectAddressItem = holder.getView(R.id.select_address_item);
                    ImageView productAllSelectImg = holder.getView(R.id.productAllSelectImg);
                    ImageView selectAddressImg = holder.getView(R.id.selectAddressImg);
                    TextView selectAddressName = holder.getView(R.id.selectAddressName);
                    TextView selectAddressText = holder.getView(R.id.selectAddressText);
                    TextView selectAddressDistance = holder.getView(R.id.selectAddressDistance);

                    selectAddressName.setText(listBean.getName());
                    selectAddressText.setText(listBean.getAddress());
                    selectAddressDistance.setText(listBean.getDistance());
                    if (listBean.isCheck()) {
                        productAllSelectImg.setVisibility(View.VISIBLE);
                    } else {
                        productAllSelectImg.setVisibility(View.GONE);
                    }
                    ImageUtil.displayImage(getApplicationContext(), selectAddressImg, listBean.getLogo());

                    selectAddressItem.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View view) {
                            for (Iterator<MarketListBean.DataBean.ListBean> iterator = marketListBean.getData().getList().iterator(); iterator.hasNext(); ) {
                                MarketListBean.DataBean.ListBean bean = iterator.next();
                                bean.setCheck(false);
                            }
                            MarketListBean.DataBean.ListBean listBean = marketListBean.getData().getList().get(pos);
                            listBean.setCheck(true);
                            bindMarket(marketListBean, listBean);

                        }
                    });
                }
            };
            cityListView.setAdapter(mNestFullListViewAdapter);
            noCityListView.setVisibility(View.GONE);
        } else {
            noCityListView.setVisibility(View.VISIBLE);
        }
    }

    private void bindMarket(MarketListBean marketListBean, MarketListBean.DataBean.ListBean listBean) {
        mCityPresenter.bindMarket(marketListBean, listBean);
    }

    @Override
    public void onBindMarketSuccess(MarketListBean marketListBean, int marketId) {
        MessageUtil.showMessage("绑定成功");
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(MARKET_ID, String.valueOf(marketId));
        edit.apply();
        onGetMarketListSuccess(marketListBean);
    }

    /**
     * 城市列表
     */
    @Override
    public void onGetCitySuccess(CityListBean cityListBean) {
        mCityPresenter.applyPermissions(permissions);
    }

    /**
     * 申请权限的结果
     *
     * @param b true 已经获取全部权限,false 没有获取全部权限
     */
    @Override
    public void applyPermissionsResults(boolean b) {
        if (b) {
            mCityPresenter.checkIfGpsOpen();
        }
    }

    /**
     * 检查手机是否打开GPS功能的结果
     *
     * @param result true 已经打开GPS功能,false 没有打开GPS功能
     */
    @Override
    public void checkIfGpsOpenResult(boolean result) {
        if (result) {
            startPositioning();
        } else {
            mCityPresenter.displayOpenGpsDialog(rootLayout);
        }
    }

    /**
     * 开始定位
     */
    private void startPositioning() {
        if (null != mLocationInstance) {
            mLocationInstance.stopPositioning();
        }
        mLocationInstance = LocationUtil.getInstance();
        mLocationInstance.startPositioning(mListener);
    }

    /**
     * 百度定位侦听器
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = bdLocation.getLatitude();    //获取纬度信息
            double longitude = bdLocation.getLongitude();    //获取经度信息
            // 获取城市
            String city = bdLocation.getCity();
            if (TextUtils.isEmpty(city)) {
                return;
            }
            cityCurrent.setText(city);
            mCityPresenter.getMarketList(latitude, longitude);
        }
    };

    @Override
    public void onSetUserPositionSuccess(UserPosition userPosition) {
        if (userPosition.getData() != null && userPosition.getData().getCity_name() != null) {
            cityName = userPosition.getData().getCity_name();
            if (cityName != null) {
                /**
                 * 发送数据到{@link HomeFragment#onSetCity(String)}
                 * 使其跳转到对应的Fragment
                 */
                EventBus.getDefault().postSticky(cityName);
            }
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
        if (null != mCityPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            if (null != mLocationInstance) {
                mLocationInstance.stopPositioning();
            }
            mCityPresenter.onDetachView();
            mCityPresenter = null;
        }
    }

    @OnClick({R.id.backImageView, R.id.cityCurrent, R.id.changeAddress, R.id.productSettingType})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                if (cityName != null && !cityName.isEmpty())
                /**
                 * 发送数据到{@link HomeFragment#onSetCity(String)}
                 * 使其跳转到对应的Fragment
                 */
                    EventBus.getDefault().postSticky(cityName);
                finish();
                break;
            case R.id.cityCurrent:
                cityName = cityCurrent.getText().toString();
                if (cityName != null) {
                    /**
                     * 发送数据到{@link HomeFragment#onSetCity(String)}
                     * 使其跳转到对应的Fragment
                     */
                    EventBus.getDefault().postSticky(cityName);
                    onBackPressed();
                }
                break;
            case R.id.changeAddress:
                if (null != mLocationInstance) {
                    mLocationInstance.stopPositioning();
                }
                Intent intent = new Intent(mSelectAddressActivity, ChangeAddressActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.productSettingType:
                String cityName = mSharedPreferences.getString(COM_OPEN, "1");
                mCityPresenter.selectProductType(rootLayout, cityName);
                break;
        }
    }

    @Override
    public void onSetProductSuccess(EditUserInfo editUserInfo) {
        MessageUtil.showMessage(editUserInfo.getMsg());

        mSharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String cityName = mSharedPreferences.getString(COM_OPEN, "1");
        if (cityName.equals("1")) {
            productSettingName.setText("展示所有产品");
        } else {
            productSettingName.setText("仅展示有供货的产品");
        }
    }

    private final static int REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == ChangeAddressActivity.RESULT_CODE) {
                Bundle bundle = data.getExtras();
                String name = bundle.getString("name");
                String mCitycode = bundle.getString("citycode");
                double latitude = bundle.getDouble("latitude");
                double longitude = bundle.getDouble("longitude");
                MessageUtil.showMessage(name);

                LogUtil.e("ChangeAddress", "mCitycode:" + mCitycode);
                mCityPresenter.setUserPosition(mCitycode);
                mCityPresenter.getMarketList(latitude, longitude);
                cityCurrent.setText(name);
            }
        }
    }
}