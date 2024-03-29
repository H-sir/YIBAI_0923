package com.ybw.yibai.module.citypicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.CityListHotBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.pickercity.City;
import com.ybw.yibai.common.widget.pickercity.MyLetterListView;
import com.ybw.yibai.common.widget.pickercity.PingYinUtil;
import com.ybw.yibai.common.widget.pickercity.ResultListAdapter;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static com.ybw.yibai.R.layout.city_picker_list_item;
import static com.ybw.yibai.common.constants.Preferences.LOCATED_CITY;
import static com.ybw.yibai.module.change.ChangeAddressActivity.RESULT_CODE;

public class CityPickerDialogActivity extends BaseActivity implements CityPickerContract.CityPickerView, AbsListView.OnScrollListener, TextWatcher {

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;
    private CityPickerContract.CityPickerPresenter mCityPickerPresenter = null;

    private ImageView cityPickerBack;
    private BaseAdapter adapter;
    private ResultListAdapter resultListAdapter;
    private ListView personList;
    private ListView resultList;
    private TextView overlay; // 对话框首字母textview
    private MyLetterListView letterListView; // A-Z listview
    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;// 存放存在的汉语拼音首字母
    private Handler handler;
    private OverlayThread overlayThread; // 显示首字母对话框
    private ArrayList<City> allCity_lists; // 所有城市列表
    private ArrayList<City> city_lists;// 城市列表
    private ArrayList<City> city_hot;   //热门城市
    private ArrayList<City> city_result;//搜索
    private EditText sh;
    private ImageView cityPickerColse;
    private TextView tv_noresult;

    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;

    private String currentCity; // 用于保存定位到的城市
    private String currentCityCode; // 用于保存定位到的城市
    private int locateProcess = 1; // 记录当前定位的状态 正在定位-定位成功-定位失败
    private boolean isNeedFresh;

    private boolean isScroll = false;
    private boolean mReady;
    private ArrayList<City> cityList;

    @Override
    protected int setLayout() {
        return R.layout.city_picker_layout;
    }

    @Override
    protected void initView() {

    }

    private String mLocatedCity;

    @Override
    protected void initData() {
        mWaitDialog = new WaitDialog(this);
        mLocatedCity = (String) getIntent().getSerializableExtra(LOCATED_CITY);
        if (null == mLocatedCity) {
            return;
        }

        cityPickerColse = findViewById(R.id.city_picker_colse);
        cityPickerBack = findViewById(R.id.city_picker_back);
        personList = (ListView) findViewById(R.id.list_view);
        allCity_lists = new ArrayList<City>();
        city_hot = new ArrayList<City>();
        city_result = new ArrayList<City>();
        resultList = (ListView) findViewById(R.id.search_result);
        sh = (EditText) findViewById(R.id.sh);
        tv_noresult = (TextView) findViewById(R.id.tv_noresult);
        sh.addTextChangedListener(this);
        letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
        letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        isNeedFresh = true;
        personList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position >= 4) {
                    onBack(allCity_lists.get(position));
//                    Toast.makeText(getApplicationContext(), allCity_lists.get(position).getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        locateProcess = 1;
        personList.setAdapter(adapter);
        personList.setOnScrollListener(this);
        resultListAdapter = new ResultListAdapter(this, city_result);
        resultList.setAdapter(resultListAdapter);
        resultList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), city_result.get(position).getName(), Toast.LENGTH_SHORT)
//                        .show();
                onBack(city_result.get(position));
            }
        });
        initOverlay();

        cityPickerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cityPickerColse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sh.setText("");
            }
        });
    }

    @Override
    protected void initEvent() {
        mCityPickerPresenter = new CityPickerPresenterImpl(this);
        mCityPickerPresenter.getCity(2);

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString() == null || "".equals(s.toString())) {
            letterListView.setVisibility(View.VISIBLE);
            personList.setVisibility(View.VISIBLE);
            resultList.setVisibility(View.GONE);
            tv_noresult.setVisibility(View.GONE);
        } else {
            city_result.clear();
            letterListView.setVisibility(View.GONE);
            personList.setVisibility(View.GONE);
            getResultCityList(s.toString());
            if (city_result.size() <= 0) {
                tv_noresult.setVisibility(View.VISIBLE);
                resultList.setVisibility(View.GONE);
            } else {
                tv_noresult.setVisibility(View.GONE);
                resultList.setVisibility(View.VISIBLE);
                resultListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getResultCityList(String s) {
        for (Iterator<City> iterator = allCity_lists.iterator(); iterator.hasNext(); ) {
            City next = iterator.next();
            if (PingYinUtil.getPingYin(next.getName()).startsWith(s) ||
                    next.getName().startsWith(s)) {
                city_result.add(next);
            }
        }
        Collections.sort(city_result, comparator);
    }


    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        mReady = true;
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.city_picker_overlay_layout, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private void setAdapter(List<City> list, List<City> hotList) {
        adapter = new ListAdapter(this, list, hotList);
        personList.setAdapter(adapter);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onGetCityHotSuccess(int type, CityListHotBean cityListHotBean) {
        for (Iterator<CityListHotBean.DataBean.HotcityBean> iterator = cityListHotBean.getData().getHotcity().iterator(); iterator.hasNext(); ) {
            CityListHotBean.DataBean.HotcityBean hotcityBean = iterator.next();
            City city = new City(hotcityBean.getRegionName(), hotcityBean.getCode(), "2");
            city_hot.add(city);
        }
        setAdapter(allCity_lists, city_hot);
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        InitLocation();
        mLocationClient.start();
    }

    @Override
    public void onGetCitySuccess(int type, CityListBean cityListBean) {
//        City city = new City("定位", "", "0"); // 当前定位城市
//        allCity_lists.add(city);
////        city = new City("热门", "", "2"); // 热门城市
////        allCity_lists.add(city);
//        city = new City("全部", "", "3"); // 全部城市
//        allCity_lists.add(city);
        if (cityListBean != null && cityListBean.getData() != null) {
            ArrayList<City> list = new ArrayList<>();
            for (Iterator<CityListBean.DataBean> iterator = cityListBean.getData().iterator(); iterator.hasNext(); ) {
                CityListBean.DataBean dataBean = iterator.next();
                for (Iterator<CityListBean.DataBean.ListBean> cityIterator = dataBean.getList().iterator(); cityIterator.hasNext(); ) {
                    CityListBean.DataBean.ListBean listBean = cityIterator.next();
                    City cityBean = new City();
                    cityBean.setName(listBean.getName());
                    cityBean.setCode(listBean.getCode());
                    cityBean.setPinyi(PingYinUtil.getPingYin(listBean.getName()));
                    list.add(cityBean);
                }
            }
            city_lists = getCityList(list);
            allCity_lists.addAll(city_lists);
        }
        mCityPickerPresenter.getCity(1);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<City> getCityList(ArrayList<City> list) {
        Collections.sort(list, comparator);
        return list;
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

    private class LetterListViewListener implements MyLetterListView.OnTouchingLetterChangedListener {

        @SuppressLint("WrongConstant")
        @Override
        public void onTouchingLetterChanged(final String s) {
            isScroll = false;
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                personList.setSelection(position);
                overlay.setText(s);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1000);
            }
        }
    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            Log.e("info", "city = " + arg0.getCity());
            if (!isNeedFresh) {
                return;
            }
            isNeedFresh = false;
            if (arg0.getCity() == null) {
                locateProcess = 3; // 定位失败
                personList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return;
            }
            currentCity = arg0.getCity().substring(0, arg0.getCity().length() - 1);
            locateProcess = 2; // 定位成功
            personList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * a-z排序
     */
    @SuppressWarnings("rawtypes")
    Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyi().substring(0, 1);
            String b = rhs.getPinyi().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };

    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else if (str.equals("0")) {
            return "定位";
        } else if (str.equals("2")) {
            return "热门";
        } else {
            return "#";
        }
    }

    public class ListAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<City> list;
        private List<City> hotList;
        final int VIEW_TYPE = 5;

        public ListAdapter(Context context, List<City> list, List<City> hotList) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
            this.context = context;
            this.hotList = hotList;
            alphaIndexer = new HashMap<String, Integer>();
            sections = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                // 当前汉语拼音首字母
                String currentStr = getAlpha(list.get(i).getPinyi());
                // 上一个汉语拼音首字母，如果不存在为" "
                String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
                        .getPinyi()) : " ";
                if (!previewStr.equals(currentStr)) {
                    String name = getAlpha(list.get(i).getPinyi());
                    alphaIndexer.put(name, i);
                    sections[i] = name;
                }
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public int getItemViewType(int position) {
            return position < 4 ? position : 4;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView city;
            int viewType = getItemViewType(position);
            if (viewType == 0) { // 定位
                convertView = inflater.inflate(R.layout.city_picker_frist_list_item, null);
                TextView locateHint = (TextView) convertView.findViewById(R.id.locateHint);
                city = (TextView) convertView.findViewById(R.id.lng_city);
                city.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (locateProcess == 2) {
                            for (Iterator<City> iterator = allCity_lists.iterator(); iterator.hasNext(); ) {
                                City next = iterator.next();
                                if (next.getName().equals(currentCity)) {
                                    Intent intent = new Intent();
                                    intent.putExtra("name", next.getName());
                                    intent.putExtra("code", next.getCode());
                                    setResult(RESULT_CODE, intent);
                                    finish();
                                    break;
                                }
                            }
//                            Toast.makeText(context, city.getText().toString(), Toast.LENGTH_SHORT).show();
                        } else if (locateProcess == 3) {
                            locateProcess = 1;
                            personList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            mLocationClient.stop();
                            isNeedFresh = true;
                            InitLocation();
                            currentCity = "";
                            mLocationClient.start();
                        }
                    }
                });
                ProgressBar pbLocate = (ProgressBar) convertView.findViewById(R.id.pbLocate);
                if (locateProcess == 1) { // 正在定位
                    locateHint.setText("正在定位");
                    city.setVisibility(View.GONE);
                    pbLocate.setVisibility(View.VISIBLE);
                } else if (locateProcess == 2) { // 定位成功
                    locateHint.setText("当前定位城市");
                    city.setVisibility(View.VISIBLE);
                    city.setText(currentCity);
                    mLocationClient.stop();
                    pbLocate.setVisibility(View.GONE);
                } else if (locateProcess == 3) {
                    locateHint.setText("未定位到城市,请选择");
                    city.setVisibility(View.VISIBLE);
                    city.setText("重新选择");
                    pbLocate.setVisibility(View.GONE);
                }
            } else if (viewType == 1) {
                convertView = inflater.inflate(R.layout.city_picker_total_item, null);
                TextView locateHint = convertView.findViewById(R.id.locateHint);
                locateHint.setVisibility(View.GONE);
            } else if (viewType == 2) {
                convertView = inflater.inflate(R.layout.city_picker_recent_city, null);
                GridView hotCity = (GridView) convertView.findViewById(R.id.recent_city);
                hotCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
//                        Toast.makeText(context, city_hot.get(position).getName(), Toast.LENGTH_SHORT).show();
                        onBack(city_hot.get(position));
                    }
                });
                hotCity.setAdapter(new HotCityAdapter(context, this.hotList));
                TextView hotHint = (TextView) convertView.findViewById(R.id.recentHint);
                hotHint.setText("热门城市");
            } else if (viewType == 3) {
                convertView = inflater.inflate(R.layout.city_picker_total_item, null);
            } else {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.city_picker_list_item_layout, null);
                    holder = new ViewHolder();
                    holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
                    holder.name = (TextView) convertView.findViewById(R.id.name);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (position >= 1) {
                    holder.name.setText(list.get(position).getName());
                    String currentStr = getAlpha(list.get(position).getPinyi());
                    String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).getPinyi()) : " ";
                    if (!previewStr.equals(currentStr)) {
                        holder.alpha.setVisibility(View.VISIBLE);
                        holder.alpha.setText(currentStr);
                    } else {
                        holder.alpha.setVisibility(View.GONE);
                    }
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView alpha; // 首字母标题
            TextView name; // 城市名字
        }
    }

    class HotCityAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<City> hotCitys;

        public HotCityAdapter(Context context, List<City> hotCitys) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
            this.hotCitys = hotCitys;
        }

        @Override
        public int getCount() {
            return hotCitys.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.city_picker_item_city, null);
            TextView city = (TextView) convertView.findViewById(R.id.city);
            city.setText(hotCitys.get(position).getName());
            return convertView;
        }
    }

    class HitCityAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<String> hotCitys;

        public HitCityAdapter(Context context, List<String> hotCitys) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
            this.hotCitys = hotCitys;
        }

        @Override
        public int getCount() {
            return hotCitys.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.city_picker_item_city, null);
            TextView city = (TextView) convertView.findViewById(R.id.city);
            city.setText(hotCitys.get(position));
            return convertView;
        }
    }

    private void InitLocation() {
        // 设置定位参数
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(10000); // 10分钟扫描1次
        // 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。
        option.setAddrType("all");
        // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setProdName("通过GPS定位我当前的位置");
        // 禁用启用缓存定位数据
        option.disableCache(true);
        // 设置定位方式的优先级。
        // 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
        option.setPriority(LocationClientOption.GpsFirst);
        mLocationClient.setLocOption(option);
    }


    private void onBack(City city) {
        Intent intent = new Intent();
        intent.putExtra("name", city.getName());
        intent.putExtra("code", city.getCode());
        setResult(RESULT_CODE, intent);
        finish();
    }
}
