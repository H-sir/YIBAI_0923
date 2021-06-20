package com.ybw.yibai.module.change;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.utils.CnSpellUtils;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.spinner.SpinnerPopWindow;
import com.ybw.yibai.module.citypicker.CityPickerDialogActivity;
import com.ybw.yibai.module.main.MainActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ybw.yibai.common.constants.Preferences.CITY_NAME;
import static com.ybw.yibai.common.constants.Preferences.LOCATED_CITY;
import static com.ybw.yibai.common.constants.Preferences.POSITION_ADDRESS;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * <pre>
 *     author : HKR
 *     time   : 2021/03/24
 *     desc   :
 * </pre>
 */
public class ChangeAddressActivity extends BaseActivity implements ChangeAddressContract.ChangeAddressView {
    private ChangeAddressActivity mChangeAddressActivity = null;

    @BindView(R.id.ll_map)
    FrameLayout llMap;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.tv_selected_city)
    TextView mTvSelectedCity;
    @BindView(R.id.et_jiedao_name)
    EditText etJiedaoName;
    @BindView(R.id.mMap)
    MapView mMap;
    @BindView(R.id.rv_result)
    ListView mLvResult;
    @BindView(R.id.rv_result_button)
    ListView mLvResultButton;
    @BindView(R.id.rv_layout)
    LinearLayout rvLayout;
    @BindView(R.id.lv_search)
    ListView mLvSearch;


    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private SuggestionSearch mSuggestionSearch;
    private PoiSearch mPoiSearch;
    private List<SuggestionResult.SuggestionInfo> mSuggestionInfos = new ArrayList<>();// 搜索结果列表
    private ArrayAdapter<String> sugAdapter;//输入搜索内容显示的提示
    private GeoCoder geoCoder;
    private String mSelectCity;
    private List<PoiInfo> mPoiInfoList = new ArrayList<>();//存放地图中心点附近的POI信息
    private PoiAdapter mPoiAdapter;//存放地图中心点附近的POI信息
    private boolean isFirstLoc = true;
    private LatLng locationLatLng;
    private int REQUEST_CODE_CITY = 999;
    private boolean acStateIsMap = true;//当前页面是地图还是搜索
    public final static int RESULT_CODE = 1;
    private ChangeAddressContract.ChangeAddressPresenter mChangeAddressPresenter = null;

    private List<String> spinnerListCity;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;
    private SharedPreferences mSharedPreferences;

    @Override
    protected int setLayout() {
        mChangeAddressActivity = this;
        return R.layout.change_address_layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mSharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        mWaitDialog = new WaitDialog(this);
        initMap();
    }

    @Override
    protected void initEvent() {
        mChangeAddressPresenter = new ChangeAddressPresenterImpl(this);
//        mChangeAddressPresenter.getCity();
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    private void initMap() {
        mBaiduMap = mMap.getMap();
        MapStatus mapStatus = new MapStatus.Builder().zoom(15f).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 地图状态改变相关监听
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                // 获取地图最后状态改变的中心点
                LatLng cenpt = mapStatus.target;
                LogUtil.d("", "最后停止点:" + cenpt.latitude + ", " + cenpt.longitude);
                //发起地理编码，当转化成功后调用onGetReverseGeoCodeResult()方法
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
            }
        });

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位图层显示方式
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        mLocClient = new LocationClient(this);

        // 创建GeoCoder实例对象
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                List<PoiInfo> poiList = reverseGeoCodeResult.getPoiList();
                mPoiInfoList.clear();
                mPoiInfoList.addAll(poiList);//只读
                mPoiAdapter.notifyDataSetChanged();
            }
        });

        mPoiAdapter = new PoiAdapter(getApplication(), mPoiInfoList);//地图下方POI列表适配器
        mLvResult.setAdapter(mPoiAdapter);
        mLvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PoiInfo poiInfo = mPoiInfoList.get(position);
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putString(CITY_NAME, poiInfo.getCity());
                edit.putString(POSITION_ADDRESS, poiInfo.name);
                edit.apply();
                SceneHelper.saveCity(getApplicationContext(), poiInfo.getCity());
                SceneHelper.saveLatLng(getApplicationContext(), String.valueOf(poiInfo.getLocation().latitude), String.valueOf(poiInfo.getLocation().longitude));
                Intent intent = new Intent();
                intent.putExtra("name", poiInfo.name);
                intent.putExtra("citycode", mCitycode);
                intent.putExtra("latitude", poiInfo.getLocation().latitude);
                intent.putExtra("longitude", poiInfo.getLocation().longitude);
                setResult(RESULT_CODE, intent);
                finish();
            }
        });

        mLvResultButton.setAdapter(mPoiAdapter);
        mLvResultButton.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PoiInfo poiInfo = mPoiInfoList.get(position);
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putString(CITY_NAME, poiInfo.getCity());
                edit.putString(POSITION_ADDRESS, poiInfo.name);
                edit.apply();
                SceneHelper.saveCity(getApplicationContext(), poiInfo.getCity());
                SceneHelper.saveLatLng(getApplicationContext(), String.valueOf(poiInfo.getLocation().latitude), String.valueOf(poiInfo.getLocation().longitude));
                Intent intent = new Intent();
                intent.putExtra("name", poiInfo.name);
                intent.putExtra("citycode", mCitycode);
                intent.putExtra("latitude", poiInfo.getLocation().latitude);
                intent.putExtra("longitude", poiInfo.getLocation().longitude);
                setResult(RESULT_CODE, intent);
                finish();
            }
        });

        // 初始化搜索模块，注册搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                List<SuggestionResult.SuggestionInfo> allSuggestions = suggestionResult.getAllSuggestions();
                if (allSuggestions != null && allSuggestions.size() > 0) {
                    mSuggestionInfos.clear();
                    sugAdapter.clear();
                    for (Iterator<SuggestionResult.SuggestionInfo> iterator = allSuggestions.iterator(); iterator.hasNext(); ) {
                        SuggestionResult.SuggestionInfo suggestionInfo = iterator.next();
                        mSuggestionInfos.add(suggestionInfo);
                        sugAdapter.add(suggestionInfo.district + suggestionInfo.key);
                    }
                    sugAdapter.notifyDataSetChanged();


                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(allSuggestions.get(0).getPt(), 18f);
                    mBaiduMap.animateMapStatus(msu);
                    // 发起反地理编码请求(经纬度->地址信息)
                    ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
                    // 设置反地理编码位置坐标
                    reverseGeoCodeOption.location(allSuggestions.get(0).getPt());
                    geoCoder.reverseGeoCode(reverseGeoCodeOption);
                }
            }
        });

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }

                //遍历所有POI，找到类型为公交线路的POI
                if (poiResult.getAllPoi().size() > 0) {
                    mPoiInfoList.clear();
                    mPoiInfoList.addAll(poiResult.getAllPoi());//只读
                    mPoiAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

        // 注册定位监听
        mLocClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                // 如果bdLocation为空或mapView销毁后不再处理新数据接收的位置
                if (bdLocation == null || mBaiduMap == null) {
                    return;
                }
                MyLocationData data = new MyLocationData.Builder()// 定位数据
                        .accuracy(bdLocation.getRadius())// 定位精度bdLocation.getRadius()
                        .direction(bdLocation.getDirection())// 此处设置开发者获取到的方向信息，顺时针0-360
                        .latitude(bdLocation.getLatitude())// 经度
                        .longitude(bdLocation.getLongitude())// 纬度
                        .build();// 构建
                mBaiduMap.setMyLocationData(data);// 设置定位数据
                // 是否是第一次定位
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18f);
                    mBaiduMap.animateMapStatus(msu);
                    locationLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    // 获取城市，待会用于POISearch
                    mSelectCity = bdLocation.getCity();
                    mTvSelectedCity.setText(mSelectCity);
                    // 发起反地理编码请求(经纬度->地址信息)
                    ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
                    // 设置反地理编码位置坐标
                    reverseGeoCodeOption.location(locationLatLng);
                    geoCoder.reverseGeoCode(reverseGeoCodeOption);
                }
            }
        });
        // 定位选项
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        sugAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line);
        mLvSearch.setAdapter(sugAdapter);
        mLvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SuggestionResult.SuggestionInfo suggestionInfo = mSuggestionInfos.get(i);
                Intent intent = new Intent();
                intent.putExtra("address", suggestionInfo.district + suggestionInfo.key);
                setResult(RESULT_OK, intent);
//                finish();
            }
        });

        etJiedaoName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    rvLayout.setVisibility(View.GONE);
                    return;
                }
                String cityCode = mTvSelectedCity.getText().toString();
                mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                        .citylimit(true)
                        .keyword(charSequence.toString())
                        .city(cityCode));
                rvLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMap.onDestroy();
        geoCoder.destroy();
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CITY && resultCode == Activity.RESULT_OK) {
            mSelectCity = data.getStringExtra("city");
            mTvSelectedCity.setText(mSelectCity);
            mSuggestionInfos.clear();
            sugAdapter.clear();
            sugAdapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE) {
            if (resultCode == ChangeAddressActivity.RESULT_CODE) {
                Bundle bundle = data.getExtras();
                String name = bundle.getString("name");
                String code = bundle.getString("code");
                MessageUtil.showMessage(name);
                mTvSelectedCity.setText(name);
            }
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!acStateIsMap) {
                llMap.setVisibility(View.VISIBLE);
                mLlSearch.setVisibility(View.GONE);
                acStateIsMap = true;
                return false;
            } else {
                this.setResult(Activity.RESULT_CANCELED);
                finish();
                return true;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @OnClick({R.id.backImageView, R.id.btn_selected_city, R.id.serch_selected_city, R.id.tv_selected_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                finish();
                break;
            case R.id.serch_selected_city:
                // 发起请求
                String cityCode = mTvSelectedCity.getText().toString();
                String etJiedao = etJiedaoName.getText().toString();
                if (etJiedao.isEmpty()) etJiedao = cityCode;
//                mPoiSearch.searchInCity((new PoiCitySearchOption())
//                        .city(cityCode)
//                        .keyword(etJiedao));
                mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                        .citylimit(true)
                        .keyword(etJiedao)
                        .city(cityCode));
                break;
            case R.id.btn_selected_city:
                break;
            case R.id.tv_selected_city:
                Intent intent = new Intent(ChangeAddressActivity.this, CityPickerDialogActivity.class);
                intent.putExtra(LOCATED_CITY, mSelectCity);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }


    private final static int REQUEST_CODE = 1;


    private String mCitycode;

    @Override
    public void onGetCitySuccess(CityListBean cityListBean) {
        String citYname = mSharedPreferences.getString(CITY_NAME, "");
        LogUtil.e("ChangeAddress", "citYname:" + citYname);
        if (!citYname.isEmpty()) {
            mTvSelectedCity.setText(citYname);
        }

//        if (cityListBean != null && cityListBean.getData() != null && cityListBean.getData().getList() != null) {
//            spinnerListCity = new ArrayList<>();
//            for (Iterator<CityListBean.DataBean.ListBean> iterator = cityListBean.getData().getList().iterator(); iterator.hasNext(); ) {
//                CityListBean.DataBean.ListBean listBean = iterator.next();
//                spinnerListCity.add(listBean.getRegionName());
//                LogUtil.e("ChangeAddress", "listBean.getRegionName():" + listBean.getRegionName() + "listBean.getCode():" + listBean.getCode());
//                if (!citYname.isEmpty()) {
//                    if (citYname.equals(listBean.getRegionName())) {
//                        mCitycode = listBean.getCode();
//                    }
//                }
//            }
//            mSpinnerPopWindowCity = new SpinnerPopWindow<>(getApplication(), spinnerListCity, new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                    mSpinnerPopWindowCity.dismiss();
//                    mTvSelectedCity.setText(spinnerListCity.get(position));
//                    for (Iterator<CityListBean.DataBean.ListBean> iterator = cityListBean.getData().getList().iterator(); iterator.hasNext(); ) {
//                        CityListBean.DataBean.ListBean listBean = iterator.next();
//                        if (spinnerListCity.get(position).equals(listBean.getRegionName())) {
//                            mCitycode = listBean.getCode();
//                        }
//                    }
//                }
//            });
//            mTvSelectedCity.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    CityPicker.from(ChangeAddressActivity.this)
//                            .enableAnimation(true)
//                            .setCityList(cityList)
//                            .setAnimationStyle(R.style.DefaultCityPickerAnimation)
//                            .setLocatedCity(null)
//                            .setHotCities(null)
//                            .setOnPickListener(new OnPickListener() {
//                                @Override
//                                public void onPick(int position, City data) {
//
//                                    LogUtil.e("ChangeAddress", String.format("点击的数据：%s，%s", data.getName(), data.getCode()));
//                                }
//
//                                @Override
//                                public void onCancel() {
//                                    LogUtil.e("ChangeAddress", "取消选择");
//                                }
//
//                                @Override
//                                public void onLocate() {
//                                    //开始定位，这里模拟一下定位
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            CityPicker.from(ChangeAddressActivity.this).locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
//                                        }
//                                    }, 3000);
//                                }
//                            })
//                            .show();
//                }
//            });
//        }
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

}
