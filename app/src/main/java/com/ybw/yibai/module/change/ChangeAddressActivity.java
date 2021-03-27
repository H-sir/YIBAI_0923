package com.ybw.yibai.module.change;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : HKR
 *     time   : 2021/03/24
 *     desc   :
 * </pre>
 */
public class ChangeAddressActivity extends BaseActivity {
    private ChangeAddressActivity mChangeAddressActivity = null;

    @BindView(R.id.ll_map) LinearLayout llMap;
    @BindView(R.id.ll_search) LinearLayout mLlSearch;
    @BindView(R.id.tv_selected_city) TextView mTvSelectedCity;
    @BindView(R.id.et_jiedao_name) EditText etJiedaoName;
    @BindView(R.id.mMap) MapView mMap;
    @BindView(R.id.rv_result) ListView mLvResult;
    @BindView(R.id.lv_search) ListView mLvSearch;


    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private SuggestionSearch mSuggestionSearch;
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
    public final static int RESULT_CODE=1;

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
        initMap();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @OnClick(R.id.backImageView)
    public void onViewClicked() {
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
                Intent intent = new Intent();
                intent.putExtra("name", poiInfo.name);
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
                mSuggestionInfos.clear();
                sugAdapter.clear();
                for (Iterator<SuggestionResult.SuggestionInfo> iterator = allSuggestions.iterator(); iterator.hasNext(); ) {
                    SuggestionResult.SuggestionInfo suggestionInfo = iterator.next();
                    mSuggestionInfos.add(suggestionInfo);
                    sugAdapter.add(suggestionInfo.district + suggestionInfo.key);
                }
                sugAdapter.notifyDataSetChanged();
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
                finish();
            }
        });

        etJiedaoName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    return;
                }
                mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                        .citylimit(true)
                        .keyword(charSequence.toString())
                        .city(mSelectCity));
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
        }
    }

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
}
