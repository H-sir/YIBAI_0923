package com.ybw.yibai.module.details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.SimilarProductAdapter;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.ProductData;
import com.ybw.yibai.common.bean.ProductDetails;
import com.ybw.yibai.common.bean.ProductDetails.DataBean;
import com.ybw.yibai.common.bean.ProductDetails.DataBean.SkuListBean;
import com.ybw.yibai.common.bean.ProductDetails.DataBean.SpecBean;
import com.ybw.yibai.common.bean.ProductDetails.DataBean.SpecBean.SonBean;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.bean.SimilarSKU;
import com.ybw.yibai.common.bean.SimilarSKU.DataBean.ListBean;
import com.ybw.yibai.common.bean.SkuMarketBean;
import com.ybw.yibai.common.bean.ToFragment;
import com.ybw.yibai.common.bean.UpdateSKUUseState;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.EncryptionUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.PopupWindowUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.collocation.CollocationActivity;
import com.ybw.yibai.module.details.ProductDetailsContract.ProductDetailsPresenter;
import com.ybw.yibai.module.details.ProductDetailsContract.ProductDetailsView;
import com.ybw.yibai.module.market.MarketActivity;
import com.ybw.yibai.module.product.ProductFragment;
import com.ybw.yibai.module.scene.SceneActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.PLANT;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_ID;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ADDORSELECT;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ID;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_LIST;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_NAME;
import static com.ybw.yibai.common.constants.Preferences.SERVICE;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.VIP_LEVEL;
import static com.ybw.yibai.common.utils.OtherUtil.setNativeLightStatusBar;

/**
 * 产品详情界面
 *
 * @author sjl
 * @date 2019/5/16
 * https://api.100ybw.com/?method=mybw.productinfo&uid=22&product_id=5343&check=no&apiver=v2
 */
public class ProductDetailsActivity extends BaseActivity implements ProductDetailsView,
        View.OnClickListener, SimilarProductAdapter.OnItemClickListener {
    private ProductDetailsActivity mProductDetailsActivity = null;
    private static final String TAG = "ProductDetailsActivity";

    /**
     * 产品的Id
     */
    private int productId;

    /**
     * 产品的SKUid
     */
    private int productSkuId;

    /**
     * 产品类别(plant=植物,pot=花盆)
     */
    private String cateCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品SKU名称
     */
    private String productSKUName;

    /**
     * 产品规格
     */
    private String productSpec;

    /**
     * 产品价格
     */
    private double productPrice;

    /**
     * 产品月租
     */
    private double productMonthRent;

    /**
     * 产品批发价
     */
    private double productTradePrice;

    /**
     * 产品主图url地址
     */
    private String productPic1;

    /**
     * 产品模拟图url地址
     */
    private String productPic2;

    /**
     * 产品配图url地址
     */
    private String productPic3;

    /**
     * 组合好的用户选择的"某种产品的规格"
     * 例如: 160cm 小型
     */
    private String productSpecText;

    /**
     * 产品的高度
     */
    private double productHeight;

    /**
     * 产品详情
     */
    private TextView mProductDetailsBtn;
    /**
     * 盆栽习性
     */
    private TextView mProductHaBitBtn;

    /**
     * 主产品组合模式: 1组合2单产品
     */
    private int productCombinationType;

    /**
     * 产品售价代码
     */
    private String productPriceCode;

    /**
     * 产品批发价代码
     */
    private String productTradePriceCode;

    /**
     * 当前用户选择的Sku数据
     */
    private SkuListBean mSkuListBean;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * Root Layout
     */
    private RelativeLayout mRootLayout;

    /**
     * 返回图标
     */
    private ImageView mBackImageView;

    /**
     * 标题
     */
    private TextView mTitleTextView;

    /**
     * 跳转到进货详情
     */
    private ImageView mToQuotationImageView;

    /**
     * 跳转到报价详情
     */
    private TextView mSaveTextViewNum;

    /**
     * 产品使用状态
     */
    private TextView mUseStateTextView;

    /**
     * 产品图片
     */
    private ImageView mProductImageView;

    /**
     * 产品名称
     */
    private TextView mProductNameTextView;

    /**
     * 产品规格
     */
    private TextView mProductSpecTextView;

    /**
     *
     */
    private View mView;

    /*----------*/

    /**
     * 点击显示产品规格PopupWindow的View
     */
    private RelativeLayout mSelectedLayout;

    /**
     * 货源
     */
    private RelativeLayout mMarketLayout;

    /**
     * 货源信息
     */
    private TextView mMarketSpecTextView;

    /**
     * 显示用户选中的规格
     */
    private TextView mSpecTextView;

    /**
     * 显示用户选中的规格
     */

    /**
     * 产品价格
     */
    private TextView mProductPriceTextView;
    /**
     * H5
     */
    private WebView webView;
    private WebView webViewHabit;

    /**
     * 显示相似推荐列表
     */
    private RecyclerView mRecyclerView;

    /**
     * 看设计
     */
    private TextView mLookDesignTextView;

    /**
     * 换搭配
     */
    private TextView mChangeMatchTextView;

    /**
     * 加入进货
     */
    private TextView mJoinPurchaseTextView;

    /**
     * 加入报价
     */
    private TextView mJoinQuotationTextView;

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
     * 产品规格集合
     */
    private List<SpecBean> mSpecList;

    /**
     * 产品Sku集合
     */
    private List<SkuListBean> mSkuList;

    /**
     * 相似推荐sku集合
     */
    private List<ListBean> mSimilarSkuList;

    /**
     * 相似产品列表适配器
     */
    private SimilarProductAdapter mAdapter;

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
    private ProductDetailsPresenter mProductDetailsPresenter;

    @Override
    protected int setLayout() {
        mProductDetailsActivity = this;
        setNativeLightStatusBar(this, true);
        return R.layout.activity_product_details;
    }

    @Override
    protected void initView() {
        mRootLayout = findViewById(R.id.rootLayout);
        mBackImageView = findViewById(R.id.backImageView);
        mTitleTextView = findViewById(R.id.titleTextView);
        mToQuotationImageView = findViewById(R.id.toQuotationImageView);
        mSaveTextViewNum = findViewById(R.id.saveTextViewNum);
        mUseStateTextView = findViewById(R.id.useStateTextView);
        mProductDetailsBtn = findViewById(R.id.productDetailsBtn);
        mProductHaBitBtn = findViewById(R.id.productHaBitBtn);

        mProductImageView = findViewById(R.id.productImageView);
        mProductNameTextView = findViewById(R.id.productNameTextView);
        mProductSpecTextView = findViewById(R.id.productSpecTextView);
        mView = findViewById(R.id.view);

        mMarketLayout = findViewById(R.id.marketLayout);
        mMarketSpecTextView = findViewById(R.id.marketSpecTextView);
        mSelectedLayout = findViewById(R.id.selectedLayout);
        mSpecTextView = findViewById(R.id.specTextView);
        mProductPriceTextView = findViewById(R.id.priceTextView);
        webView = findViewById(R.id.webView);
        webViewHabit = findViewById(R.id.webViewHabit);
        mRecyclerView = findViewById(R.id.recyclerView);

        mLookDesignTextView = findViewById(R.id.lookDesignTextView);
        mChangeMatchTextView = findViewById(R.id.changeMatchTextView);
        mJoinPurchaseTextView = findViewById(R.id.joinPurchaseTextView);
        mJoinQuotationTextView = findViewById(R.id.joinQuotationTextView);

        mWaitDialog = new WaitDialog(this);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // 布局横向
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置RecyclerView间距,每一行Item的数目1000(1000为假设的数据)
        int gap = DensityUtil.dpToPx(this, 8);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(1000, gap, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        mSpecList = new ArrayList<>();
        mSkuList = new ArrayList<>();
        mSimilarSkuList = new ArrayList<>();

        Intent intent = getIntent();
        if (null != intent) {
            productId = intent.getIntExtra(PRODUCT_ID, 0);
            productSkuId = intent.getIntExtra(PRODUCT_SKU_ID, 0);
            productSKUName = intent.getStringExtra(PRODUCT_SKU_NAME);
        }

        // 服务商才可以进货
        String roleName = YiBaiApplication.getRoleName();
        if (!TextUtils.isEmpty(roleName) && !roleName.equals(SERVICE)) {
            mJoinPurchaseTextView.setVisibility(View.GONE);
        }

        mAdapter = new SimilarProductAdapter(this, mSimilarSkuList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        settingWeb(webView);
        settingWeb(webViewHabit);
    }

    private void settingWeb(WebView mWebView) {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.requestFocusFromTouch();              //如果webView中需要用户手动输入用户名、密码或其他，则webview必须设置支持获取手势焦点
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setUseWideViewPort(true);                                      //设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);                                          //便页面支持缩放
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);           //优先使用缓存
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);                     //不使用缓存：
        settings.setJavaScriptEnabled(true);                                    //支持js
        settings.setUseWideViewPort(false);                                     //将图片调整到适合webview的大小
        settings.setSupportZoom(true);                                          //支持缩放
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.supportMultipleWindows();                                      //多窗口
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);             //关闭webview中缓存
        settings.setAllowFileAccess(true);                                      //设置可以访问文件
        settings.setNeedInitialFocus(true);                                     //当webview调用requestFocus时为webview设置节点
        settings.setBuiltInZoomControls(true);                                  //设置支持缩放
        settings.setJavaScriptCanOpenWindowsAutomatically(true);                //支持通过JS打开新窗口
        settings.setLoadWithOverviewMode(true);                                 // 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true);                             //支持自动加载图片
    }

    @Override
    protected void initEvent() {
        mProductDetailsPresenter = new ProductDetailsPresenterImpl(this);
        mProductDetailsPresenter.getProductDetails(productId);
        mProductDetailsPresenter.getSimilarSKUList(productSkuId);
        mProductDetailsPresenter.getPurCartData();
//        mProductDetailsPresenter.getSkuMarket(productSkuId);
        mBackImageView.setOnClickListener(this);
        mToQuotationImageView.setOnClickListener(this);
        mUseStateTextView.setOnClickListener(this);
        mSelectedLayout.setOnClickListener(this);
        mMarketLayout.setOnClickListener(this);
        mProductPriceTextView.setOnClickListener(this);

        mLookDesignTextView.setOnClickListener(this);
        mChangeMatchTextView.setOnClickListener(this);
        mJoinPurchaseTextView.setOnClickListener(this);
        mJoinQuotationTextView.setOnClickListener(this);
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

        // 跳转到报价详情
        if (id == R.id.toQuotationImageView) {
            /**
             * 发送数据到{@link MainActivity#ratioActivitySendData(ToFragment)}
             * 使其跳转到对应的Fragment
             */
            ToFragment toFragment = new ToFragment(4);
            EventBus.getDefault().postSticky(toFragment);
            finish();
        }

        // 产品使用状态
        if (id == R.id.useStateTextView) {
            mProductDetailsPresenter.updateSKUUseState(productSkuId);
        }

        // 点击显示产品规格PopupWindow的View
        if (id == R.id.selectedLayout) {
            mProductDetailsPresenter.displaySpecPopupWindow(mRootLayout, mSpecList);
        }

        // 产品价格
        if (id == R.id.priceTextView) {
            mProductDetailsPresenter.displayPricePopupWindow(mRootLayout, mSkuListBean);
        }

        // 看设计
        if (id == R.id.lookDesignTextView) {
            SharedPreferences preferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
            int vipLevel = preferences.getInt(VIP_LEVEL, 0);
            if (1 == vipLevel) {
                PopupWindowUtil.displayUpdateVipPopupWindow(mProductDetailsActivity, mRootLayout);
                return;
            }
            ProductData productData = new ProductData(
                    productSkuId,
                    productName,
                    productPrice,
                    productMonthRent,
                    productTradePrice,
                    productPic1,
                    productPic2,
                    productPic3,
                    productSpecText,
                    productHeight,
                    productCombinationType,
                    productPriceCode,
                    productTradePriceCode

            );
            mProductDetailsPresenter.saveSimulationData(cateCode, productData);
        }

        // 换搭配
        if (id == R.id.changeMatchTextView) {
            Intent intent = new Intent(this, CollocationActivity.class);
            if (null != mSkuListBean) {
                intent.putExtra(PRODUCT_SKU_LIST, mSkuListBean);
            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        // 加入进货
        if (id == R.id.joinPurchaseTextView) {
//            ProductData productData = new ProductData(
//                    productSkuId,
//                    productName,
//                    productPrice,
//                    productMonthRent,
//                    productTradePrice,
//                    productPic1,
//                    productPic2,
//                    productPic3,
//                    productSpecText,
//                    productHeight,
//                    productCombinationType,
//                    productPriceCode,
//                    productTradePriceCode
//            );
//            mProductDetailsPresenter.saveQuotationData(productData);
//            mProductDetailsPresenter.addpurcart(productData);
            if (productSkuId != 0) {
                Intent intent = new Intent(ProductDetailsActivity.this, MarketActivity.class);
                intent.putExtra(PRODUCT_SKU_ID, productSkuId);
                intent.putExtra(PRODUCT_SKU_ADDORSELECT, true);
                startActivity(intent);
            }
        }

        // 加入报价
        if (id == R.id.joinQuotationTextView) {
            mProductDetailsPresenter.addQuotation(productSkuId);
        }
//        // 产品详情
//        if (id == R.id.productDetailsBtn) {
//            mProductDetailsBtn.setBackground(getResources().getDrawable(R.drawable.sharp_underline));
//            mProductHaBitBtn.setBackground(null);
//            webViewHabit.setVisibility(View.GONE);
//            webView.setVisibility(View.VISIBLE);
//        }
//        // 盆栽习性
//        if (id == R.id.productHaBitBtn) {
//            mProductHaBitBtn.setBackground(getResources().getDrawable(R.drawable.sharp_underline));
//            mProductDetailsBtn.setBackground(null);
//            webViewHabit.setVisibility(View.VISIBLE);
//            webView.setVisibility(View.GONE);
//        }

        // 调整货源
        if (id == R.id.marketLayout) {
            Intent intent = new Intent(ProductDetailsActivity.this, MarketActivity.class);
            intent.putExtra(PRODUCT_SKU_ID, productSkuId);
            intent.putExtra(PRODUCT_SKU_ADDORSELECT, false);
            startActivity(intent);
        }
    }

    @Override
    public void onGetSkuMarketSuccess(SkuMarketBean skuMarketBean) {

    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        ListBean listBean = mSimilarSkuList.get(position);
        int productId = listBean.getProduct_id();
        int productSkuId = listBean.getSku_id();
        String name = listBean.getName();
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        intent.putExtra(PRODUCT_SKU_ID, productSkuId);
        intent.putExtra(PRODUCT_SKU_NAME, name);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 在获取产品详情成功时回调
     *
     * @param productDetails 产品详情
     */
    @Override
    public void onGetProductDetailsSuccess(ProductDetails productDetails) {
        if (CODE_SUCCEED != productDetails.getCode()) {
            return;
        }
        DataBean data = productDetails.getData();
        if (null == data) {
            return;
        }

        if (productDetails.getData().getSource() != null) {
            mMarketSpecTextView.setText(productDetails.getData().getSource().getDelivery());
        }

        if (!TextUtils.isEmpty(productDetails.getData().getContent())) {
            String content = productDetails.getData().getContent();
            String htmlString = EncryptionUtil.base64DecodeString(content);
            if (!TextUtils.isEmpty(htmlString)) {
                webView.clearCache(true);
//                String replace = htmlString.replace("http", "https");
                webView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
            }
        }
        if (!TextUtils.isEmpty(productDetails.getData().getHabit_url())) {
            String content = productDetails.getData().getHabit_url();
            if (!TextUtils.isEmpty(content)) {
                webViewHabit.clearCache(true);
//                String replace = htmlString.replace("http", "https");
                Map<String, String> headMap = new HashMap<>(2);
                headMap.put("uid", String.valueOf(YiBaiApplication.getUid()));
                headMap.put("token", YiBaiApplication.getToken());
                webViewHabit.loadUrl(content, headMap);
//                webViewHabit.loadDataWithBaseURL(htmlString, htmlString, "text/html", "utf-8", null);
            }
        }

        List<SpecBean> specList = data.getSpec();
        if (null != specList && specList.size() > 0) {
            mSpecList.addAll(specList);
        }
        List<SkuListBean> skuList = data.getSkuList();
        if (null != skuList && skuList.size() > 0) {
            mSkuList.addAll(skuList);
        }
        String pic = data.getPic();
        productName = data.getName();
        cateCode = data.getCate_code();
        if (!TextUtils.isEmpty(productName)) {
            mTitleTextView.setText(productName);
        }
        if (!TextUtils.isEmpty(productSKUName)) {
            mProductNameTextView.setText(productSKUName);
        }
        if (!TextUtils.isEmpty(pic)) {
            ImageUtil.displayImage(this, mProductImageView, pic);
        }
        // 如果没有产品规格集合,就隐藏对应的View
        if (null == mSpecList || mSpecList.size() == 0) {
            mView.setVisibility(View.INVISIBLE);
            mSelectedLayout.setVisibility(View.GONE);
        }
        // 如果产品不是植物,就隐藏
        if (!PLANT.equals(cateCode)) {
            mChangeMatchTextView.setVisibility(View.GONE);
        }
        // 获取一开始进入本界面的产品的规格
        mProductDetailsPresenter.getInitialSelectsSpec(productSkuId, mSpecList, mSkuList);
    }

    /**
     * 在获取相似推荐sku列表成功时回调
     *
     * @param similarSKU 相似推荐sku列表
     */
    @Override
    public void onGetSimilarSKUListSuccess(SimilarSKU similarSKU) {
        if (CODE_SUCCEED != similarSKU.getCode()) {
            return;
        }
        mSimilarSkuList.clear();
        SimilarSKU.DataBean data = similarSKU.getData();
        if (null == data) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        mSimilarSkuList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 在修改产品sku使用状态成功时回调
     *
     * @param updateSKUUseState 修改产品sku使用状态时服务器端返回的数据
     */
    @Override
    public void onUpdateSKUUseStateSuccess(UpdateSKUUseState updateSKUUseState) {
        MessageUtil.showMessage(updateSKUUseState.getMsg());
        if (CODE_SUCCEED != updateSKUUseState.getCode()) {
            return;
        }
        UpdateSKUUseState.DataBean data = updateSKUUseState.getData();
        if (null == data) {
            return;
        }
        int SKUId = data.getSku_id();
        int useState = data.getUsestate();
        // 更新状态
        for (SkuListBean skuListBean : mSkuList) {
            int skuId = skuListBean.getSku_id();
            if (SKUId != skuId) {
                continue;
            }
            skuListBean.setUsestate(useState);
            break;
        }
        mProductDetailsPresenter.getInitialSelectsSpec(SKUId, mSpecList, mSkuList);
        /**
         * 发送数据到{@link ProductFragment#onUpdateSKUUseStateSuccess(UpdateSKUUseState)}
         * 使其刷新界面数据
         */
        EventBus.getDefault().post(updateSKUUseState);
    }

    /**
     * 在获取用户选择的植物规格"成功时回调
     *
     * @param selectsSpecMap 用户选择的规格
     */
    @Override
    public void onGetSelectsSpecSuccess(Map<Integer, Integer> selectsSpecMap) {
        // 用户选择的产品其规格ID的集合
        List<Integer> specIdList = new ArrayList<>();
        // 用户选择的产品其规格名称
        StringBuilder stringBuffer = new StringBuilder();
        // 遍历用户选择的规格
        for (Map.Entry<Integer, Integer> entry : selectsSpecMap.entrySet()) {
            // 规格名称在列表中的位置
            int key = entry.getKey();
            // 具体规格值在列表中的位置
            int value = entry.getValue();

            SpecBean specBean = mSpecList.get(key);
            if (null == specBean) {
                continue;
            }
            List<SonBean> sonList = specBean.getSon();
            if (null == sonList || sonList.size() == 0) {
                continue;
            }
            SonBean sonBean = sonList.get(value);
            if (null == sonBean) {
                continue;
            }

            // 获取具体规格值ID
            int id = sonBean.getId();
            // 获取具体规格值名称
            String name = sonBean.getName();
            specIdList.add(id);
            stringBuffer.append(name);
            stringBuffer.append("  ");
        }
        if (!TextUtils.isEmpty(stringBuffer)) {
            productSpecText = stringBuffer.toString().trim();
            mSpecTextView.setText(productSpecText);
        }
        // 根据用户选中的规格来获取规格对应的产品信息
        mProductDetailsPresenter.getProductInfoAccordingToSpec(specIdList, mSkuList);
    }

    /**
     * 在PopupWindow Item 点击时回调
     *
     * @param v 被点击的item
     */
    @Override
    public void onPopupWindowItemClick(View v) {
        int id = v.getId();

        // 零售
        if (id == R.id.retailPriceTextView) {
            String productRetailPriceString = getResources().getString(R.string.retail_price) + " " + YiBaiApplication.getCurrencySymbol() + productPrice;
            mProductPriceTextView.setText(productRetailPriceString);
        }

        // 月租
        if (id == R.id.monthRentPriceTextView) {
            String productMonthRentString = getResources().getString(R.string.month_rent_price) + " " + YiBaiApplication.getCurrencySymbol() + productMonthRent;
            mProductPriceTextView.setText(productMonthRentString);
        }

        // 进货
        if (id == R.id.tradePriceTextView) {
            String productTradePriceString = getResources().getString(R.string.trade_price) + " " + YiBaiApplication.getCurrencySymbol() + productTradePrice;
            mProductPriceTextView.setText(productTradePriceString);
        }
    }

    /**
     * 返回根据用户选中产品信息
     *
     * @param skuList 产品信息
     */
    @SuppressLint("NewApi")
    @Override
    public void returnSelectsProductInfo(SkuListBean skuList) {
        mSkuListBean = skuList;
        productSkuId = skuList.getSku_id();
        productSKUName = skuList.getName();
        productPrice = skuList.getPrice();
        productMonthRent = skuList.getMonth_rent();
        productTradePrice = skuList.getTrade_price();
        productPic1 = skuList.getPic1();
        productPic2 = skuList.getPic2();
        productPic3 = skuList.getPic3();
        productHeight = skuList.getHeight();
        productSpec = skuList.getParstring();
        productCombinationType = skuList.getComtype();
        productPriceCode = skuList.getPrice_code();
        productTradePriceCode = skuList.getTrade_price_code();
        int useState = skuList.getUsestate();
        if (1 == useState) {
            // 使用
            mUseStateTextView.setText("收藏");
            mUseStateTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            mUseStateTextView.setBackground(getDrawable(R.drawable.background_button_unpressed));
        } else {
            // 不使用
            mUseStateTextView.setText("收藏");
            mUseStateTextView.setTextColor(ContextCompat.getColor(this, R.color.prompt_low_text_color));
            mUseStateTextView.setBackground(getDrawable(R.drawable.background_image_view));
        }
        if (!TextUtils.isEmpty(productPic1)) {
            ImageUtil.displayImage(this, mProductImageView, productPic1);
        }
        if (!TextUtils.isEmpty(productSKUName)) {
            mProductNameTextView.setText(productSKUName);
        }
        if (TextUtils.isEmpty(productSpec)) {
            mProductSpecTextView.setText(" ");
            mProductSpecTextView.setVisibility(View.GONE);
        } else {
            mProductSpecTextView.setText(productSpec);
            mProductSpecTextView.setVisibility(View.VISIBLE);
        }
        String productMonthRentString = getResources().getString(R.string.month_rent_price) + " " + YiBaiApplication.getCurrencySymbol() + productMonthRent;
        mProductPriceTextView.setText(productMonthRentString);
    }

    /**
     * 保存"模拟搭配产品"数据成功或者失败时回调
     *
     * @param result true成功 false失败
     */
    @Override
    public void onSaveSimulationDataDataResult(boolean result) {
        if (result) {
            MessageUtil.showMessage(getResources().getString(R.string.added_to_the_simulation_scene_successfully));
            Intent intent = new Intent(this, SceneActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            MessageUtil.showMessage(getResources().getString(R.string.added_to_the_simulation_scene_failed));
        }
    }

    /**
     * 保存"报价"数据成功或者失败时回调
     *
     * @param result true成功 false失败
     */
    @Override
    public void onSaveQuotationDataDataResult(boolean result) {
        if (result) {
            MessageUtil.showMessage(getResources().getString(R.string.dding_to_purchase_list_successfully));
        } else {
            MessageUtil.showMessage(getResources().getString(R.string.adding_to_purchase_list_failed));
        }
    }

    /**
     * 在sku加入待摆放清单成功时回调
     *
     * @param addQuotation sku加入待摆放清单时服务器端返回的数据
     */
    @Override
    public void onAddQuotationSuccess(AddQuotation addQuotation) {
        MessageUtil.showMessage(addQuotation.getMsg());
    }

    @Override
    public void onGetPurCartDataSuccess(PurCartBean purCartBean) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Iterator<PurCartBean.DataBean.ComlistBean> iterator = purCartBean.getData().getComlist().iterator(); iterator.hasNext(); ) {
            PurCartBean.DataBean.ComlistBean comlistBean = iterator.next();
            map.put(comlistBean.getCartId(), 1);
        }
        for (Iterator<PurCartBean.DataBean.ItemlistBean> iterator = purCartBean.getData().getItemlist().iterator(); iterator.hasNext(); ) {
            PurCartBean.DataBean.ItemlistBean itemlistBean = iterator.next();
            map.put(itemlistBean.getCartId(), 1);
        }
        mSaveTextViewNum.setText(String.valueOf(map.size()));
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
        if (null != mProductDetailsPresenter) {
            mProductDetailsPresenter.onDetachView();
            mProductDetailsPresenter = null;
        }
    }

    @OnClick({R.id.productDetailsBtn, R.id.productHaBitBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.productDetailsBtn:
                mProductDetailsBtn.setBackground(getResources().getDrawable(R.drawable.sharp_underline));
                mProductHaBitBtn.setBackground(null);
                webViewHabit.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                break;
            case R.id.productHaBitBtn:
                mProductHaBitBtn.setBackground(getResources().getDrawable(R.drawable.sharp_underline));
                mProductDetailsBtn.setBackground(null);
                webViewHabit.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                break;
        }
    }
}
