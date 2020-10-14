package com.ybw.yibai.module.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.HotSchemePagerAdapter;
import com.ybw.yibai.common.adapter.HotSchemesPagerAdapter;
import com.ybw.yibai.common.adapter.ProductAdapter;
import com.ybw.yibai.common.bean.CreateCustomers;
import com.ybw.yibai.common.bean.CustomerList;
import com.ybw.yibai.common.bean.CustomersInfo;
import com.ybw.yibai.common.bean.HotScheme;
import com.ybw.yibai.common.bean.HotSchemeCategory;
import com.ybw.yibai.common.bean.HotSchemeCategory.DataBean;
import com.ybw.yibai.common.bean.HotSchemeCategory.DataBean.ListBean;
import com.ybw.yibai.common.bean.HotSchemes;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.QuotationAgain;
import com.ybw.yibai.common.bean.RecommendProductList;
import com.ybw.yibai.common.bean.ToFragment;
import com.ybw.yibai.common.bean.UpdateCompany;
import com.ybw.yibai.common.bean.UserInfo;
import com.ybw.yibai.common.bean.UserPosition;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.utils.AnimationUtil.CustomTransformer;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.DisplayUpdateVipPopupWindowUtil;
import com.ybw.yibai.common.utils.EncryptionUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.GuideUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.LocationUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.magicindicator.MagicIndicator;
import com.ybw.yibai.common.widget.magicindicator.buildins.Util;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.ybw.yibai.common.widget.magicindicator.title.ScaleTransitionPagerTitleView;
import com.ybw.yibai.module.browser.BrowserActivity;
import com.ybw.yibai.module.city.CityActivity;
import com.ybw.yibai.module.company.CompanyInfoEditActivity;
import com.ybw.yibai.module.design.DesignActivity;
import com.ybw.yibai.module.details.ProductDetailsActivity;
import com.ybw.yibai.module.drawing.SimulationDrawingActivity;
import com.ybw.yibai.module.home.HomeContract.HomePresenter;
import com.ybw.yibai.module.home.HomeContract.HomeView;
import com.ybw.yibai.module.hotschemes.HotSchemesActivity;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.newuser.NewUserFragment;
import com.ybw.yibai.module.oldcustomer.OldCustomerListFragment;
import com.ybw.yibai.module.picturecase.PictureCaseActivity;
import com.ybw.yibai.module.product.ProductFragment;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsFragment;
import com.ybw.yibai.module.savepricing.SavePricingByCustomerActivity;
import com.ybw.yibai.module.scene.SceneActivity;
import com.ybw.yibai.module.search.SearchActivity;
import com.ybw.yibai.view.activity.SelectDeliveryCityActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_LOCATION_PERMISSIONS_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_GPS_CODE;
import static com.ybw.yibai.common.constants.Preferences.COMPANY;
import static com.ybw.yibai.common.constants.Preferences.COMPANY_DETAILS;
import static com.ybw.yibai.common.constants.Preferences.COMPANY_DETAILS_PIC;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_ID;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_LOGO;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_MANE;
import static com.ybw.yibai.common.constants.Preferences.HOT_SCHEME_LIST;
import static com.ybw.yibai.common.constants.Preferences.LOCAL_URL_TYPE;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_ID;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ID;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_NAME;
import static com.ybw.yibai.common.constants.Preferences.TYPE;
import static com.ybw.yibai.common.constants.Preferences.URL;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.VIP_LEVEL;
import static com.ybw.yibai.common.utils.AnimationUtil.zoomAnimation;

/**
 * 首页Fragment
 *
 * @author sjl
 * @date 2019/9/5
 */
public class HomeFragment extends BaseFragment implements HomeView, View.OnTouchListener, View.OnClickListener,
        HotSchemePagerAdapter.OnViewPagerItemClickListener, HotSchemesPagerAdapter.OnViewPagerItemClickListener,
        ProductAdapter.OnItemClickListener {

    public static final String TAG = "HomeFragment";

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    private View mRootLayout;

    /**
     * 广告图片
     */
    private ImageView mAdImageView;

    /**
     * 定位
     */
    private TextView mLocationTextView;

    /**
     * 公司名称
     */
    private TextView mCompanyTextView;

    /**
     * 搜索
     */
    private ImageView mSearchImageView;

    /**
     * 选择客户布局
     */
    private RelativeLayout mSelectCompanyLayout;

    /**
     * 客户Ico
     */
    private ImageView mCompanyIcoImageView;

    /**
     * 客户名称
     */
    private TextView mCustomerNameTextView;

    /**
     * 开始设计
     */
    private TextView mStartDesignTextView;

    /**
     * 我的设计
     */
    private TextView mMyDesignTextView;

    /**
     * 快速采购
     */
    private TextView mQuickPurchaseTextView;

    /**
     * 图片案例
     */
    private TextView mPictureCaseTextView;

    /**
     * 指示器框架
     */
    private MagicIndicator mMagicIndicator;

    /**
     *
     */
    private ViewPager mViewPager;

    /**
     * 显示产品列表标题
     */
    private TextView mRecommendProductTextView;

    /**
     * 显示产品列表
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
     * 指示器框架标题内容集合
     */
    private List<String> mTitleList;

    /**
     * 热门方案列表
     */
    private List<HotScheme.DataBean.ListBean> mHotSchemeList;

    /**
     * 新热门方案列表
     */
    private List<HotSchemes.DataBean.ListBean> mHotSchemesList;

    /**
     * 产品sku列表
     */
    private List<com.ybw.yibai.common.bean.ListBean> mSKUList;

    /**
     * 首页显示热门方案的适配器
     */
    private HotSchemePagerAdapter mAdapter;

    /**
     * 首页显示新热门方案的适配器
     */
    private HotSchemesPagerAdapter mAdapters;

    /**
     * 产品列表适配器
     */
    private ProductAdapter mProductAdapter;

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
    private Activity mActivity;

    /**
     *
     */
    private SharedPreferences mPreferences;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

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
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private HomePresenter mHomePresenter;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        mActivity = getActivity();
        return R.layout.fragment_home;
    }

    @Override
    protected void findViews(View view) {
        mRootLayout = view.findViewById(R.id.rootLayout);

        mAdImageView = view.findViewById(R.id.adImageView);
        mLocationTextView = view.findViewById(R.id.locationTextView);
        mCompanyTextView = view.findViewById(R.id.companyTextView);
        mSearchImageView = view.findViewById(R.id.searchImageView);
        mSelectCompanyLayout = view.findViewById(R.id.selectCompanyLayout);
        mCompanyIcoImageView = view.findViewById(R.id.companyIcoImageView);
        mCustomerNameTextView = view.findViewById(R.id.customerNameTextView);

        mStartDesignTextView = view.findViewById(R.id.startDesignTextView);
        mMyDesignTextView = view.findViewById(R.id.myDesignTextView);
        mQuickPurchaseTextView = view.findViewById(R.id.quickPurchaseTextView);
        mPictureCaseTextView = view.findViewById(R.id.pictureCaseTextView);
        mMagicIndicator = view.findViewById(R.id.magicIndicator);
        mViewPager = view.findViewById(R.id.viewPager);
        mRecommendProductTextView = view.findViewById(R.id.recommendProductTextView);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mWaitDialog = new WaitDialog(mContext);
    }

    @Override
    protected void initView() {
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(DensityUtil.dpToPx(mContext, 30), 0, DensityUtil.dpToPx(mContext, 30), 0);
        mViewPager.setPageMargin(DensityUtil.dpToPx(mContext, -24));
        CustomTransformer mCustomTransformer = new CustomTransformer();
        mViewPager.setPageTransformer(true, mCustomTransformer);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, VERTICAL);
        // 设置RecyclerView间距
        int gap = DensityUtil.dpToPx(mContext, 8);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(2, gap, false);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);

        initMagicIndicator();
    }

    @Override
    protected void initData() {
        mTitleList = new ArrayList<>();
        mHotSchemeList = new ArrayList<>();
        mHotSchemesList = new ArrayList<>();
        mSKUList = new ArrayList<>();

        mPreferences = mContext.getSharedPreferences(USER_INFO, MODE_PRIVATE);
        int customersId = mPreferences.getInt(CUSTOMERS_ID, 0);
        String customersName = mPreferences.getString(CUSTOMERS_MANE, null);
        String customersLogo = mPreferences.getString(CUSTOMERS_LOGO, null);
        String companyDetailsPic = mPreferences.getString(COMPANY_DETAILS_PIC, null);

        YiBaiApplication.setCid(customersId);
        if (!TextUtils.isEmpty(customersName)) {
            mCustomerNameTextView.setText(customersName);
        }
        if (!TextUtils.isEmpty(customersLogo)) {
            ImageUtil.displayRoundImage(mContext, mCompanyIcoImageView, customersLogo);
        }
        if (!TextUtils.isEmpty(companyDetailsPic)) {
            ImageUtil.displayImage(mContext, mAdImageView, companyDetailsPic);
        }

        // mAdapter = new HotSchemePagerAdapter(mContext, mHotSchemeList);
        // mViewPager.setAdapter(mAdapter);
        // mAdapter.setOnViewPagerItemClickListener(this);

        mAdapters = new HotSchemesPagerAdapter(mContext, mHotSchemesList);
        mViewPager.setAdapter(mAdapters);
        mAdapters.setOnViewPagerItemClickListener(this);

        mProductAdapter = new ProductAdapter(mContext, mSKUList, 2);
        mRecyclerView.setAdapter(mProductAdapter);
        mProductAdapter.setOnItemClickListener(this);
    }

    @Override
    @SuppressWarnings("all")
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mHomePresenter = new HomePresenterImpl(this);
        mHomePresenter.getHotSchemeCategory();
        mHomePresenter.getRecommendProductList();

        mAdImageView.setOnClickListener(this);
        mLocationTextView.setOnClickListener(this);
        mCompanyTextView.setOnClickListener(this);
        mSearchImageView.setOnClickListener(this);
        mSelectCompanyLayout.setOnClickListener(this);

        mStartDesignTextView.setOnTouchListener(this);
        mMyDesignTextView.setOnTouchListener(this);
        mQuickPurchaseTextView.setOnTouchListener(this);
        mPictureCaseTextView.setOnTouchListener(this);

        mStartDesignTextView.setOnClickListener(this);
        mMyDesignTextView.setOnClickListener(this);
        mQuickPurchaseTextView.setOnClickListener(this);
        mPictureCaseTextView.setOnClickListener(this);

        // 获取客户列表,默认选择第一个客户
        if (0 == YiBaiApplication.getCid()) {
            mHomePresenter.getCustomerList();
        }
        GuideUtil.showGuideView(this, R.layout.guide_home_layout);

        mLocationTextView.setText(SceneHelper.getCity(getActivity()));
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            zoomAnimation(v, 1f, 1.1f);
        } else if (action == MotionEvent.ACTION_UP) {
            zoomAnimation(v, 1.1f, 1f);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 广告图片
        if (id == R.id.adImageView) {
            String companyDetails = mPreferences.getString(COMPANY_DETAILS, null);
            if (TextUtils.isEmpty(companyDetails)) {
                return;
            }
            String htmlString = EncryptionUtil.base64DecodeString(companyDetails);
            if (TextUtils.isEmpty(htmlString)) {
                return;
            }
            Intent intent = new Intent(mContext, BrowserActivity.class);
            intent.putExtra(URL, htmlString);
            intent.putExtra(TYPE, LOCAL_URL_TYPE);
            startActivity(intent);
        }

        // 定位
        if (id == R.id.locationTextView) {
//            Intent toIntent = new Intent(getContext(), SelectDeliveryCityActivity.class);
//            startActivityForResult(toIntent, 1001);
            Intent intent = new Intent(getActivity(), CityActivity.class);
            startActivityForResult(intent, 1);
            /*String locationName = mLocationTextView.getText().toString().trim();
            if (!TextUtils.isEmpty(locationName) && getResources().getString(R.string.nationwide).equals(locationName)) {
                mHomePresenter.displaySwitchSupplyOfGoodsDialog(true);
            } else {
                mHomePresenter.displaySwitchSupplyOfGoodsDialog(false);
            }*/
//            startActivityForResult(new Intent(getContext(),SelectDeliveryCityActivity.class),101);
        }

        // 编辑公司信息
        if (id == R.id.companyTextView) {
            Intent intent = new Intent(mContext, CompanyInfoEditActivity.class);
            startActivity(intent);
        }

        // 搜索
        if (id == R.id.searchImageView) {
            Intent intent = new Intent(mContext, SearchActivity.class);
            startActivity(intent);
            mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        // 选择客户
        if (id == R.id.selectCompanyLayout) {
            Intent intent = new Intent(mContext, SavePricingByCustomerActivity.class);
            startActivity(intent);
            mActivity.overridePendingTransition(R.anim.activity_open, android.R.anim.fade_out);
        }

        // 开始设计
        if (id == R.id.startDesignTextView) {
            SharedPreferences preferences = mContext.getSharedPreferences(USER_INFO, MODE_PRIVATE);
            int vipLevel = preferences.getInt(VIP_LEVEL, 0);
            if (1 == vipLevel) {
                DisplayUpdateVipPopupWindowUtil.displayUpdateVipPopupWindow(getActivity(),mRootLayout);
                return;
            }
            Intent intent = new Intent(mContext, SceneActivity.class);
            intent.putExtra("location",mLocationTextView.getText().toString());
            startActivity(intent);
        }

        // 我的设计
        if (id == R.id.myDesignTextView) {
            Intent intent = new Intent(mContext, DesignActivity.class);
            startActivity(intent);
//            Intent intent = new Intent(mContext, SimulationDrawingActivity.class);
//            startActivity(intent);
        }

        // 快速采购
        if (id == R.id.quickPurchaseTextView) {
            /**
             * 发送数据到{@link MainActivity#ratioActivitySendData(ToFragment)}
             * 使其跳转到对应的Fragment
             */
            ToFragment toFragment = new ToFragment(2);
            EventBus.getDefault().postSticky(toFragment);
        }

        // 图片案例
        if (id == R.id.pictureCaseTextView) {
            Intent intent = new Intent(mContext, PictureCaseActivity.class);
            startActivity(intent);
        }
    }


    /**
     * EventBus
     * 接收用户从{@link SceneActivity#}传递过来的数据}传递过来的数据
     *
     * @param city 城市名称
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetCity(String city) {
        mLocationTextView.setText(city);
        SceneHelper.saveCity(getActivity(), city);
    }

    /**
     * 在ViewPager Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onViewPagerItemClick(int position) {
        // Intent intent = new Intent(mContext, HotSchemeActivity.class);
        // intent.putExtra(POSITION, position);
        // intent.putParcelableArrayListExtra(HOT_SCHEME_LIST, (ArrayList<? extends Parcelable>) mHotSchemeList);
        // startActivity(intent);

        Intent intent = new Intent(mContext, HotSchemesActivity.class);
        intent.putExtra(POSITION, position);
        intent.putParcelableArrayListExtra(HOT_SCHEME_LIST, (ArrayList<? extends Parcelable>) mHotSchemesList);
        startActivity(intent);
    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        com.ybw.yibai.common.bean.ListBean listBean = mSKUList.get(position);
        int productId = listBean.getProduct_id();
        int productSkuId = listBean.getSku_id();
        String name = listBean.getName();
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        intent.putExtra(PRODUCT_SKU_ID, productSkuId);
        intent.putExtra(PRODUCT_SKU_NAME, name);
        startActivity(intent);
    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(mCommonNavigatorAdapter);
        mMagicIndicator.setNavigator(commonNavigator);
    }

    private CommonNavigatorAdapter mCommonNavigatorAdapter = new CommonNavigatorAdapter() {
        @Override
        public int getCount() {
            return mTitleList == null ? 0 : mTitleList.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, int index) {
            SimplePagerTitleView titleView = new ScaleTransitionPagerTitleView(context);
            titleView.setTextSize(16);
            titleView.setText(mTitleList.get(index));
            titleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.main_text_color));
            titleView.setNormalColor(ContextCompat.getColor(mContext, R.color.prompt_high_text_color));
            return titleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
            indicator.setYOffset(Util.dipToPx(context, 2));
            indicator.setStartInterpolator(new AccelerateInterpolator());
            indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
            indicator.setColors(Color.TRANSPARENT);
            return indicator;
        }
    };

    /**
     * 在获取客户列表成功时回调
     *
     * @param customersList 客户列表数据
     */
    @Override
    public void onGetCustomerListSuccess(CustomerList customersList) {
        if (CODE_SUCCEED != customersList.getCode()) {
            return;
        }
        CustomerList.DataBean data = customersList.getData();
        if (null == data) {
            return;
        }
        List<CustomerList.DataBean.ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        // 默认选择第一个客户
        CustomerList.DataBean.ListBean listBean = list.get(0);
        int customersId = listBean.getId();
        String customersLogo = listBean.getLogo();
        String customersName = listBean.getName();

        YiBaiApplication.setCid(customersId);
        if (!TextUtils.isEmpty(customersName)) {
            mCustomerNameTextView.setText(customersName);
        }
        if (!TextUtils.isEmpty(customersLogo)) {
            ImageUtil.displayRoundImage(mContext, mCompanyIcoImageView, customersLogo);
        }

        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putInt(CUSTOMERS_ID, customersId);
        edit.putString(CUSTOMERS_MANE, customersName);
        edit.putString(CUSTOMERS_LOGO, customersLogo);
        edit.apply();
    }

    /**
     * 在获取热门方案分类成功时回调
     *
     * @param hotSchemeCategory 热门方案分类
     */
    @Override
    public void onGetHotSchemeCategorySuccess(HotSchemeCategory hotSchemeCategory) {
        if (CODE_SUCCEED != hotSchemeCategory.getCode()) {
            return;
        }
        DataBean data = hotSchemeCategory.getData();
        if (null == data) {
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ListBean listBean = list.get(i);
            int id = listBean.getId();
            String name = listBean.getName();
            mTitleList.add(name);
            if (i != 0) {
                continue;
            }
            // mHomePresenter.getHotScheme(id);
            mHomePresenter.getHotSchemes(id);
        }
        mCommonNavigatorAdapter.notifyDataSetChanged();
    }

    /**
     * 在获取热门方案列表成功时回调
     *
     * @param hotScheme 门方案列表
     */
    @Override
    public void onGetHotSchemeSuccess(HotScheme hotScheme) {
        if (CODE_SUCCEED != hotScheme.getCode()) {
            return;
        }
        mHotSchemeList.clear();
        HotScheme.DataBean data = hotScheme.getData();
        if (null == data) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        List<HotScheme.DataBean.ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        mHotSchemeList.addAll(list);
        mAdapter.notifyDataSetChanged();
        // 定位到10000实现左右无限滑动
        mViewPager.setCurrentItem(10000);
        mViewPager.setOffscreenPageLimit(3);
    }

    /**
     * 在获取新热门方案列表成功时回调
     *
     * @param hotSchemes 门方案列表
     */
    @Override
    public void onGetHotSchemesSuccess(HotSchemes hotSchemes) {
        if (CODE_SUCCEED != hotSchemes.getCode()) {
            return;
        }
        mHotSchemesList.clear();
        HotSchemes.DataBean data = hotSchemes.getData();
        if (null == data) {
            mAdapters.notifyDataSetChanged();
            return;
        }
        List<HotSchemes.DataBean.ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            mAdapters.notifyDataSetChanged();
            return;
        }
        mHotSchemesList.addAll(list);
        mAdapters.notifyDataSetChanged();
        // 定位到10000实现左右无限滑动
        mViewPager.setCurrentItem(10000);
        mViewPager.setOffscreenPageLimit(3);
    }

    /**
     * 在获取推荐产品列表成功时回调
     *
     * @param recommendProductList 推荐产品列表
     */
    @Override
    public void onGetRecommendProductListSuccess(RecommendProductList recommendProductList) {
        if (CODE_SUCCEED != recommendProductList.getCode()) {
            return;
        }
        mSKUList.clear();
        mRecommendProductTextView.setVisibility(View.VISIBLE);
        RecommendProductList.DataBean data = recommendProductList.getData();
        if (null == data) {
            mProductAdapter.notifyDataSetChanged();
            return;
        }
        List<com.ybw.yibai.common.bean.ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            mProductAdapter.notifyDataSetChanged();
            return;
        }
        mSKUList.addAll(list);
        mProductAdapter.notifyDataSetChanged();
    }

    /**
     * 是否切换货源的结果
     *
     * @param results true切换货源,false不切换货源
     */
    @Override
    public void whetherToSwitchResults(boolean results) {
        if (!results) {
            return;
        }
//        String locationName = mLocationTextView.getText().toString().trim();
//        if (!TextUtils.isEmpty(locationName) && getResources().getString(R.string.nationwide).equals(locationName)) {
//            // 切换为同城货源
//            mHomePresenter.applyPermissions(permissions);
//            return;
//        }
//        // 切换为全国货源
//        mHomePresenter.setUserPosition(null);
        mHomePresenter.applyPermissions(permissions);
    }

    /**
     * 申请权限的结果
     *
     * @param b true 已经获取全部权限,false 没有获取全部权限
     */
    @Override
    public void applyPermissionsResults(boolean b) {
        if (b) {
            mHomePresenter.checkIfGpsOpen();
        }
    }

    /**
     * 请求权限的结果的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_LOCATION_PERMISSIONS_CODE != requestCode) {
            return;
        }
        // 获得申请的全部权限
        if (PermissionsUtil.allPermissionsAreGranted(grantResults)) {
            mHomePresenter.checkIfGpsOpen();
        } else {
            PermissionsUtil.initPermissionsDialog(
                    mActivity,
                    getResources().getString(R.string.the_current_application_lacks_positioning_permissions_please_authorize_the_permissions_required_by_the_app),
                    getResources().getString(R.string.manual_authorization),
                    getResources().getString(R.string.cancel),
                    false);
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
            mHomePresenter.displayOpenGpsDialog(mRootLayout);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 判断GPS是否打开
        if (requestCode == REQUEST_OPEN_GPS_CODE && LocationUtil.isGpsOpen(mContext)) {
            startPositioning();
        }else if(requestCode == 101 && resultCode == 1){
            String city = data.getStringExtra("city_code");
            mHomePresenter.setUserPosition(city);
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
            // 获取省份
            String province = bdLocation.getProvince();
            // 获取城市
            String city = bdLocation.getCity();
            if (TextUtils.isEmpty(city)) {
                return;
            }
//            mLocationTextView.setText(city);
//            mHomePresenter.setUserPosition(province + city);
        }
    };

    /**
     * 在设置货源城市成功时回调
     *
     * @param userPosition 设置货源城市时服务器端返回的数据
     */
    @Override
    public void onSetUserPositionSuccess(UserPosition userPosition) {
        MessageUtil.showMessage(userPosition.getMsg());
        if (CODE_SUCCEED != userPosition.getCode()) {
            return;
        }
        UserPosition.DataBean data = userPosition.getData();
        if (null == data) {
            return;
        }
        String cityName = data.getCity_name();
        if (!TextUtils.isEmpty(cityName)) {
            mLocationTextView.setText(cityName);
        }
        /**
         * 发送数据到{@link ProductFragment#onSetUserPositionSuccess(UserPosition)}
         * 使其刷新界面数据
         */
        EventBus.getDefault().post(userPosition);
    }

    /**
     * EventBus
     * 接收用户从{@link MainActivity#onGetUserInfoSuccess(UserInfo)}
     * 传递过来的数据(在获取用户信息成功时)
     *
     * @param userInfo 用户信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetUserInfoSuccess(UserInfo userInfo) {
        UserInfo.DataBean data = userInfo.getData();
        if (null == data) {
            return;
        }
        String companyDetailsPic = data.getCompany_details_pic();
        String company = data.getCompany();
        String cityName = data.getCity_name();
        if (!TextUtils.isEmpty(companyDetailsPic)) {
            ImageUtil.displayImage(mContext, mAdImageView, companyDetailsPic);
        }
        if (!TextUtils.isEmpty(company)) {
            mCompanyTextView.setText(company);
        }
//        if (!TextUtils.isEmpty(cityName)) {
//            mLocationTextView.setText(cityName);
//        }
    }

    /**
     * EventBus
     * 接收用户从
     * {@link NewUserFragment#onNewCustomersSuccess(CreateCustomers)}
     * {@link OldCustomerListFragment#onItemClick(int)}
     * {@link QuotationDetailsFragment#onQuotationAgainSuccess(QuotationAgain)}
     * 返回用户选择的客户信息
     *
     * @param customersInfo 客户信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void returnCustomersInfo(CustomersInfo customersInfo) {
        int customersId = customersInfo.getId();
        String customersLogo = customersInfo.getLogo();
        String customersName = customersInfo.getName();
        YiBaiApplication.setCid(customersId);
        if (!TextUtils.isEmpty(customersName)) {
            mCustomerNameTextView.setText(customersName);
        }
        if (!TextUtils.isEmpty(customersLogo)) {
            ImageUtil.displayRoundImage(mContext, mCompanyIcoImageView, customersLogo);
        }

        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putInt(CUSTOMERS_ID, customersId);
        edit.putString(CUSTOMERS_MANE, customersName);
        edit.putString(CUSTOMERS_LOGO, customersLogo);
        edit.apply();
    }

    /**
     * EventBus
     * 接收用户从{@link CompanyInfoEditActivity#onUpdateCompanyInfoSuccess(UpdateCompany)}
     * 传递过来的数据(在修改用户公司信息成功时)
     *
     * @param updateCompany 修改用户公司信息时服务器端返回的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateCompanyInfoSuccess(UpdateCompany updateCompany) {
        String companyDetailsPic = mPreferences.getString(COMPANY_DETAILS_PIC, null);
        String company = mPreferences.getString(COMPANY, null);
        if (!TextUtils.isEmpty(companyDetailsPic)) {
            ImageUtil.displayImage(mContext, mAdImageView, companyDetailsPic);
        }
        if (!TextUtils.isEmpty(company)) {
            mCompanyTextView.setText(company);
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
        if (null != mHomePresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            if (null != mLocationInstance) {
                mLocationInstance.stopPositioning();
            }
            mHomePresenter.onDetachView();
            mHomePresenter = null;
        }
    }
}