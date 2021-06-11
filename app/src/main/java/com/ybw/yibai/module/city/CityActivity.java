package com.ybw.yibai.module.city;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.adapter.CityListAdapter;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.bean.MarketListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.PlaceBean;
import com.ybw.yibai.common.bean.UserPosition;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.LocationUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.home.HomeFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static com.ybw.yibai.common.constants.Preferences.COM_OPEN;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/07
 *     desc   :
 * </pre>
 */
public class CityActivity extends BaseActivity implements CityContract.CityView, CityListAdapter.OnCityClickListener {
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.barView)
    RelativeLayout barView;
    @BindView(R.id.cityCurrent)
    TextView cityCurrent;
    @BindView(R.id.cityListView)
    RecyclerView cityListView;
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.rootLayout)
    NestedScrollView rootLayout;
    @BindView(R.id.productAllSelectImg) ImageView productAllSelectImg;
    @BindView(R.id.productOneSelectImg) ImageView productOneSelectImg;

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
     * 城市列表
     */
    private List<CityListBean.DataBean.ListBean> hotcityList = new ArrayList<>();

    /**
     * 城市列表适配器
     */
    private CityListAdapter mCityListAdapter;
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
        return R.layout.city_layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mSharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String cityName = mSharedPreferences.getString(COM_OPEN, "1");
        if (cityName.equals("1")) {
            productAllSelectImg.setImageResource(R.mipmap.purcart_select);
            productOneSelectImg.setImageResource(R.mipmap.purcart_no_select);
        } else {
            productAllSelectImg.setImageResource(R.mipmap.purcart_no_select);
            productOneSelectImg.setImageResource(R.mipmap.purcart_select);
        }

        mWaitDialog = new WaitDialog(this);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, VERTICAL);
        // 设置RecyclerView间距
        int gap = DensityUtil.dpToPx(getApplicationContext(), 8);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(1, gap, false);
        // 给RecyclerView设置布局管理器(必须设置)
        cityListView.setLayoutManager(manager);
        cityListView.addItemDecoration(decoration);
        cityListView.setNestedScrollingEnabled(false);
        cityListView.setHasFixedSize(false);
    }

    @Override
    protected void initEvent() {
        mCityListAdapter = new CityListAdapter(this, hotcityList);
        cityListView.setAdapter(mCityListAdapter);
        mCityListAdapter.setOnCityClickListener(this);

        mCityPresenter = new CityPresenterImpl(this);
        mCityPresenter.getCity();
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onCityClick(int position) {
        CityListBean.DataBean.ListBean listBean = hotcityList.get(position);
        mCityPresenter.setUserPosition(listBean.getCode());
    }

    PlaceBean placeBean;

    @Override
    public void onGetLocationSuccess(PlaceBean placeBean) {
        this.placeBean = placeBean;
    }

    /**
     * 城市列表
     */
    @Override
    public void onGetCitySuccess(CityListBean cityListBean) {
//        hotcityList.addAll(cityListBean.getData().getList());
        mCityListAdapter.notifyDataSetChanged();
        mCityPresenter.applyPermissions(permissions);
    }


    @Override
    public void onGetMarketListSuccess(MarketListBean marketListBean) {

    }

    @Override
    public void onSetEditUserSuccess(EditUserInfo editUserInfo) {

    }

    @Override
    public void onBindMarketSuccess(MarketListBean marketListBean, int marketId) {

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
            mCityPresenter.getLocation(latitude, longitude);
        }
    };


    @Override
    public void onSetUserPositionSuccess(UserPosition userPosition) {
        cityName = userPosition.getData().getCity_name();
        if (cityName != null) {
            /**
             * 发送数据到{@link HomeFragment#onSetCity(String)}
             * 使其跳转到对应的Fragment
             */
            EventBus.getDefault().postSticky(cityName);
            onBackPressed();
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

    @OnClick({R.id.backImageView, R.id.cityCurrent, R.id.productAllSelect, R.id.productOneSelect})
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
                if (placeBean != null) {
                    mCityPresenter.setUserPosition(placeBean.getData().getCitycode());
                }
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
            case R.id.productAllSelect:
                mCityPresenter.onSetProduct(2);
                break;
            case R.id.productOneSelect:
                mCityPresenter.onSetProduct(1);
                break;
        }
    }

    @Override
    public void onSetProductSuccess(EditUserInfo editUserInfo) {
        MessageUtil.showMessage(editUserInfo.getMsg());

        mSharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String cityName = mSharedPreferences.getString(COM_OPEN, "1");
        if (cityName.equals("1")) {
            productAllSelectImg.setImageResource(R.mipmap.purcart_no_select);
            productOneSelectImg.setImageResource(R.mipmap.purcart_select);
        } else {
            productAllSelectImg.setImageResource(R.mipmap.purcart_select);
            productOneSelectImg.setImageResource(R.mipmap.purcart_no_select);
        }
    }
}
