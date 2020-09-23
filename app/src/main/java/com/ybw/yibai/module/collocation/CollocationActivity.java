package com.ybw.yibai.module.collocation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.PlantSelectAdapter;
import com.ybw.yibai.common.adapter.PotSelectAdapter;
import com.ybw.yibai.common.adapter.RecommendedPlantAdapter;
import com.ybw.yibai.common.adapter.RecommendedPotAdapter;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.ProductData;
import com.ybw.yibai.common.bean.ProductDetails.DataBean.SkuListBean;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.bean.SimilarPlantSKU;
import com.ybw.yibai.common.bean.SimilarPlantSKU.DataBean;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.module.collocation.CollocationContract.CollocationPresenter;
import com.ybw.yibai.module.collocation.CollocationContract.CollocationView;
import com.ybw.yibai.module.scene.SceneActivity;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;
import static android.support.design.widget.BottomSheetBehavior.STATE_SETTLING;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.POT;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_LIST;
import static com.ybw.yibai.common.utils.OtherUtil.setStatusBarColor;

/**
 * 搭配界面
 *
 * @author sjl
 * @date 2019/9/19
 */
public class CollocationActivity extends FragmentActivity implements CollocationView, View.OnClickListener,
        RecommendedPlantAdapter.OnItemClickListener, RecommendedPotAdapter.OnItemClickListener {

    public static final String TAG = "CollocationActivity";

    /**
     * 当前用户选择的植物SkuId
     */
    private int skuId;

    /**
     * 当前用户选择的植物搭配口径
     */
    private double diameter;

    /**
     * 当前口径:搭配口径+边距*2的值
     */
    private double curDiameter;

    /**
     * 是否显示背景
     */
    boolean enableBackground;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 主产品名称的款名Id
     */
    private int productSkuId;

    /**
     * 主产品名称
     */
    private String productName;

    /**
     * 主产品价格
     */
    private double productPrice;

    /**
     * 主产品月租
     */
    private double productMonthRent;

    /**
     * 主产品批发价
     */
    private double productTradePrice;

    /**
     * 主产品主图url地址
     */
    private String productPic1;

    /**
     * 主产品模拟图url地址
     */
    private String productPic2;

    /**
     * 主产品配图url地址
     */
    private String productPic3;

    /**
     * 主产品名称的高度
     */
    private double productHeight;

    /**
     * 主产品组合模式: 1组合2单产品
     */
    private int productCombinationType;

    /**
     * 主产品售价代码
     */
    private String productPriceCode;

    /**
     * 主产品批发价代码
     */
    private String productTradePriceCode;

    /*----------*/

    /**
     * 附加产品的款名Id
     */
    private int augmentedProductSkuId;

    /**
     * 附加产品名称
     */
    private String augmentedProductName;

    /**
     * 附加产品价格
     */
    private double augmentedProductPrice;

    /**
     * 附加产品月租
     */
    private double augmentedProductMonthRent;

    /**
     * 附加产品批发价
     */
    private double augmentedProductTradePrice;

    /**
     * 附加产品主图url地址
     */
    private String augmentedProductPic1;

    /**
     * 附加产品模拟图url地址
     */
    private String augmentedProductPic2;

    /**
     * 附加产品配图url地址
     */
    private String augmentedProductPic3;

    /**
     * 附加产品的高度
     */
    private double augmentedProductHeight;

    /**
     * 植物的偏移量
     */
    private double productOffsetRatio;
    /**
     * 花盆的偏移量
     */
    private double augmentedProductOffsetRatio;

    /**
     * 附加产品组合模式: 1组合2单产品
     */
    private int augmentedCombinationType;

    /**
     * 附加产品售价代码
     */
    private String augmentedPriceCode;

    /**
     * 附加产品批发价代码
     */
    private String augmentedTradePriceCode;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     *
     */
    private CoordinatorLayout mRootLayout;

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 查看设计
     */
    private TextView mSeeDesignTextView;

    /**
     * 加入相册
     */
    private TextView mJoinPhotoAlbum;

    /**
     * 加入预选
     */
    private TextView mJoinPreselectionTextView;

    /**
     * 加入进货
     */
    private TextView mJoinPurchaseTextView;

    /**
     * 关闭背景
     */
    private TextView mCloseBackgroundTextView;

    /**
     * 显示推荐植物列表的RecyclerView
     */
    private RecyclerView mRecommendedPlantRecyclerView;

    /**
     * 显示推荐植物列表的的指示点父布局
     */
    private LinearLayout mRecommendedPlantDotLayout;

    /**
     * 搭配图片布局的容器
     */
    private FrameLayout mCollocationRelativeLayout;

    /**
     * 放置"植物自由搭配图"
     */
    private ViewPager mPlantViewPager;

    /**
     * 放置"盆器自由搭配图"
     */
    private ViewPager mPotViewPager;

    /**
     * 推荐盆器展开是显示的布局
     */
    private LinearLayout mRecommendedPotLayout;

    /**
     * 显示推荐盆器列表的RecyclerView
     */
    private RecyclerView mRecommendedPotRecyclerView;

    /**
     *
     */
    private BottomSheetBehavior mBottomSheetBehavior;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 推荐植物列表
     */
    private List<ListBean> mRecommendedPlantList;

    /**
     * 推荐植物列表适配器
     */
    private RecommendedPlantAdapter mRecommendedPlantAdapter;

    /**
     * 推荐盆器列表
     */
    private List<ListBean> mRecommendedPotList;

    /**
     * 推荐盆器列表适配器
     */
    private RecommendedPotAdapter mRecommendedPotAdapter;

    /**
     * 植物选择适配器
     */
    private PlantSelectAdapter mPlantSelectAdapter;

    /**
     * 盆器选择适配器
     */
    private PotSelectAdapter mPotSelectAdapter;

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
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private CollocationPresenter mCollocationPresenter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collocation);
        setStatusBarColor(this, Color.WHITE);
        mContext = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mRootLayout = findViewById(R.id.rootLayout);
        mBackImageView = findViewById(R.id.backImageView);

        mSeeDesignTextView = findViewById(R.id.seeDesignTextView);
        mJoinPhotoAlbum = findViewById(R.id.joinPhotoAlbum);
        mJoinPreselectionTextView = findViewById(R.id.joinPreselectionTextView);
        mJoinPurchaseTextView = findViewById(R.id.joinPurchaseTextView);
        mCloseBackgroundTextView = findViewById(R.id.closeBackgroundTextView);

        mRecommendedPlantRecyclerView = findViewById(R.id.recommendedPlantRecyclerView);
        mRecommendedPlantDotLayout = findViewById(R.id.recommendedPlantDotLinearLayout);

        mCollocationRelativeLayout = findViewById(R.id.collocationFrameLayout);
        mPlantViewPager = findViewById(R.id.plantViewPager);
        mPotViewPager = findViewById(R.id.potViewPager);

        mRecommendedPotLayout = findViewById(R.id.recommended_basin_layout);
        mRecommendedPotRecyclerView = findViewById(R.id.recommendedBasinRecyclerView);
        mBottomSheetBehavior = BottomSheetBehavior.from(mRecommendedPotLayout);
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager plantManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // 布局横向
        plantManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager potManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);

        // 设置RecyclerView间距,每一行Item的数目1000(10000为假设的数据)
        int gap = DensityUtil.dpToPx(mContext, 8);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(10000, gap, true);
        GridSpacingItemDecoration decorations = new GridSpacingItemDecoration(5, gap, true);

        mRecommendedPlantRecyclerView.setLayoutManager(plantManager);
        mRecommendedPlantRecyclerView.addItemDecoration(decoration);
        mRecommendedPotRecyclerView.setLayoutManager(potManager);
        mRecommendedPotRecyclerView.addItemDecoration(decorations);
    }

    private void initData() {
        mRecommendedPlantList = new ArrayList<>();
        mRecommendedPotList = new ArrayList<>();

        Intent intent = getIntent();
        if (null != intent) {
            SkuListBean mPlantSkuListBean = intent.getParcelableExtra(PRODUCT_SKU_LIST);
            skuId = mPlantSkuListBean.getSku_id();
            diameter = mPlantSkuListBean.getDiameter();
            curDiameter = diameter;
        }
        enableBackground = YiBaiApplication.isEnableBackground();

        mRecommendedPlantAdapter = new RecommendedPlantAdapter(mContext, mRecommendedPlantList);
        mRecommendedPlantRecyclerView.setAdapter(mRecommendedPlantAdapter);
        mRecommendedPotAdapter = new RecommendedPotAdapter(mContext, mRecommendedPotList);
        mRecommendedPotRecyclerView.setAdapter(mRecommendedPotAdapter);

        mPlantSelectAdapter = new PlantSelectAdapter(this, mRecommendedPlantList);
        mPlantViewPager.setAdapter(mPlantSelectAdapter);
        mPotSelectAdapter = new PotSelectAdapter(this, mRecommendedPotList);
        mPotViewPager.setAdapter(mPotSelectAdapter);
    }

    private void initEvent() {
        mCollocationPresenter = new CollocationPresenterImpl(this);
        mCollocationPresenter.getSimilarPlantSKUList(skuId);
        mCollocationPresenter.setCollocationRootLayoutParams(mCollocationRelativeLayout);

        mBackImageView.setOnClickListener(this);

        mSeeDesignTextView.setOnClickListener(this);
        mJoinPhotoAlbum.setOnClickListener(this);
        mJoinPreselectionTextView.setOnClickListener(this);
        mJoinPurchaseTextView.setOnClickListener(this);
        mCloseBackgroundTextView.setOnClickListener(this);

        mRecommendedPlantAdapter.setOnItemClickListener(this);
        mRecommendedPotAdapter.setOnItemClickListener(this);

        mPlantViewPager.addOnPageChangeListener(mOnPlantPageChangeListener);
        mPotViewPager.addOnPageChangeListener(mOnPotPageChangeListener);

        enableBackground();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 返回
        if (id == R.id.backImageView) {
            onBackPressed();
        }

        // 查看设计
        if (id == R.id.seeDesignTextView) {
            ProductData productData = new ProductData(
                    productSkuId,
                    productName,
                    productPrice,
                    productMonthRent,
                    productTradePrice,
                    productPic1,
                    productPic2,
                    productPic3,
                    productHeight,
                    productOffsetRatio,
                    productCombinationType,
                    productPriceCode,
                    productTradePriceCode,
                    augmentedProductSkuId,
                    augmentedProductName,
                    augmentedProductPrice,
                    augmentedProductMonthRent,
                    augmentedProductTradePrice,
                    augmentedProductPic1,
                    augmentedProductPic2,
                    augmentedProductPic3,
                    augmentedProductHeight,
                    augmentedProductOffsetRatio,
                    augmentedCombinationType,
                    augmentedPriceCode,
                    augmentedTradePriceCode
            );
            mCollocationPresenter.saveSimulationData(productData);
        }

        // 加入预选
        if (id == R.id.joinPreselectionTextView) {
            ProductData productData = new ProductData(
                    productSkuId,
                    productName,
                    productPrice,
                    productMonthRent,
                    productTradePrice,
                    productPic1,
                    productPic2,
                    productPic3,
                    productHeight,
                    productOffsetRatio,
                    productCombinationType,
                    productPriceCode,
                    productTradePriceCode,
                    augmentedProductSkuId,
                    augmentedProductName,
                    augmentedProductPrice,
                    augmentedProductMonthRent,
                    augmentedProductTradePrice,
                    augmentedProductPic1,
                    augmentedProductPic2,
                    augmentedProductPic3,
                    augmentedProductHeight,
                    augmentedProductOffsetRatio,
                    augmentedCombinationType,
                    augmentedPriceCode,
                    augmentedTradePriceCode
            );
            mCollocationPresenter.joinPreselection(productData);
        }

        // 加入相册
        if (id == R.id.joinPhotoAlbum) {
            ProductData productData = new ProductData(
                    productSkuId,
                    productName,
                    productPrice,
                    productMonthRent,
                    productTradePrice,
                    productPic1,
                    productPic2,
                    productPic3,
                    productHeight,
                    productOffsetRatio,
                    productCombinationType,
                    productPriceCode,
                    productTradePriceCode,
                    augmentedProductSkuId,
                    augmentedProductName,
                    augmentedProductPrice,
                    augmentedProductMonthRent,
                    augmentedProductTradePrice,
                    augmentedProductPic1,
                    augmentedProductPic2,
                    augmentedProductPic3,
                    augmentedProductHeight,
                    augmentedProductOffsetRatio,
                    augmentedCombinationType,
                    augmentedPriceCode,
                    augmentedTradePriceCode
            );
            mCollocationPresenter.joinPhotoAlbum(productData);
        }

        // 加入进货
        if (id == R.id.joinPurchaseTextView) {
            ProductData productData = new ProductData(
                    productSkuId,
                    productName,
                    productPrice,
                    productMonthRent,
                    productTradePrice,
                    productPic1,
                    productPic2,
                    productPic3,
                    productHeight,
                    productOffsetRatio,
                    productCombinationType,
                    productPriceCode,
                    productTradePriceCode,
                    augmentedProductSkuId,
                    augmentedProductName,
                    augmentedProductPrice,
                    augmentedProductMonthRent,
                    augmentedProductTradePrice,
                    augmentedProductPic1,
                    augmentedProductPic2,
                    augmentedProductPic3,
                    augmentedProductHeight,
                    augmentedProductOffsetRatio,
                    augmentedCombinationType,
                    augmentedPriceCode,
                    augmentedTradePriceCode
            );
            mCollocationPresenter.saveQuotationData(productData);
        }

        // 关闭背景
        if (id == R.id.closeBackgroundTextView) {
            if (enableBackground) {
                enableBackground = false;
            } else {
                enableBackground = true;
            }
            enableBackground();
        }
    }

    /**
     * 在推荐植物列表RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onPlantItemClick(int position) {
        ListBean listBean = mRecommendedPlantList.get(position);
        diameter = listBean.getDiameter();
        curDiameter = diameter;
        // 滚动到对应位置的Item
        mPlantViewPager.setCurrentItem(position);
        mCollocationPresenter.getSimilarPotSKUList(POT, curDiameter, diameter);
    }

    /**
     * 在推荐盆器列表RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onPotItemClick(int position) {
        // 滚动到对应位置的Item
        mPotViewPager.setCurrentItem(position);
    }

    /**
     * 在获取搭配时植物sku推荐列表成功时回调
     *
     * @param similarPlantSKU 获取搭配时植物sku推荐列表时服务器端返回的数据
     */
    @Override
    @SuppressWarnings("all")
    public void onGetSimilarPlantSKUListSuccess(SimilarPlantSKU similarPlantSKU) {
        if (CODE_SUCCEED != similarPlantSKU.getCode()) {
            return;
        }
        mRecommendedPlantList.clear();
        DataBean data = similarPlantSKU.getData();
        if (null == data) {
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        mRecommendedPlantList.addAll(list);
        mRecommendedPlantAdapter.notifyDataSetChanged();
        mPlantSelectAdapter.notifyDataSetChanged();
        // 默认选中第一个
        mOnPlantPageChangeListener.onPageSelected(0);
    }

    /**
     * 在获取搭配时盆器sku推荐列表成功时回调
     *
     * @param similarPotSKU 获取搭配时盆器sku推荐列表时服务器端返回的数据
     */
    @Override
    @SuppressWarnings("all")
    public void onGetSimilarPotSKUListSuccess(SKUList similarPotSKU) {
        if (CODE_SUCCEED != similarPotSKU.getCode()) {
            return;
        }
        mRecommendedPotList.clear();
        SKUList.DataBean data = similarPotSKU.getData();
        if (null == data) {
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        mRecommendedPotList.addAll(list);
        mRecommendedPotAdapter.notifyDataSetChanged();
        mPotSelectAdapter.notifyDataSetChanged();
        // 默认选中第一个
        mOnPotPageChangeListener.onPageSelected(0);
    }

    /**
     * 显示"植物自由搭配图"ViewPager的Listener
     */
    private OnPageChangeListener mOnPlantPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            // 获取盆器信息(主产品)
            getPlantInfo(position);
            // 当用户滑动ViewPager时,需要选中RecyclerView对应位置的Item
            mRecommendedPlantAdapter.onSelectStateChanged(position);
            // 当用户滑动ViewPager时,需要RecyclerView滚动到指定位置
            mRecommendedPlantRecyclerView.scrollToPosition(position);
            // 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
            mCollocationPresenter.setCollocationContentParams(mPlantViewPager, mPotViewPager,
                    productHeight, augmentedProductHeight, productOffsetRatio, augmentedProductOffsetRatio);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 显示"盆器自由搭配图"ViewPager的Listener
     */
    private OnPageChangeListener mOnPotPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            // 获取盆器信息(附加产品)
            getPotInfo(position);
            // 当用户滑动ViewPager时,需要选中RecyclerView对应位置的Item
            mRecommendedPotAdapter.onSelectStateChanged(position);
            // 当用户滑动ViewPager时,需要RecyclerView滚动到指定位置
            mRecommendedPotRecyclerView.scrollToPosition(position);
            // 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
            mCollocationPresenter.setCollocationContentParams(mPlantViewPager, mPotViewPager,
                    productHeight, augmentedProductHeight, productOffsetRatio, augmentedProductOffsetRatio);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * BottomSheetBehavior的回调方法
     * <p>
     * https://material.io/design/#
     */
    private BottomSheetCallback mBottomSheetCallback = new BottomSheetCallback() {
        @Override
        public void onSlide(@NonNull View bottomSheetBehavior, float slideOffset) {

        }

        @Override
        public void onStateChanged(@NonNull View bottomSheetBehavior, int newState) {
            if (STATE_DRAGGING == newState) {
                // 拖动状态,此时用户正在向上或者向下拖动视图

            } else if (STATE_SETTLING == newState) {
                // 视图从脱离手指自由滑动到最终停下的这一小段时间
            } else if (STATE_EXPANDED == newState) {
                // 完全展开

            } else if (STATE_COLLAPSED == newState) {
                // 折叠状态,就是peekHeight设置的窥视高度

            } else if (STATE_HIDDEN == newState) {
                // 隐藏状态
            }
        }
    };

    /**
     * 获取植物信息(主产品)
     *
     * @param position 被选中的植物在推荐植物列表中的位置
     */
    private void getPlantInfo(int position) {
        if (null == mRecommendedPlantList || mRecommendedPlantList.size() == 0) {
            return;
        }
        ListBean listBean = mRecommendedPlantList.get(position);
        if (null == listBean) {
            return;
        }
        productSkuId = listBean.getSku_id();
        productName = listBean.getName();
        productPrice = listBean.getPrice();
        productMonthRent = listBean.getMonth_rent();
        productTradePrice = listBean.getTrade_price();
        productPic1 = listBean.getPic1();
        productPic2 = listBean.getPic2();
        productPic3 = listBean.getPic3();
        productHeight = listBean.getHeight();
        productOffsetRatio = listBean.getOffset_ratio();
        productCombinationType = listBean.getComtype();
        productPriceCode = listBean.getPrice_code();
        productTradePriceCode = listBean.getTrade_price_code();

        diameter = listBean.getDiameter();
        curDiameter = diameter;
    }

    /**
     * 获取盆器信息(附加产品)
     *
     * @param position 被选中的盆器在推荐盆器列表中的位置
     */
    private void getPotInfo(int position) {
        if (null == mRecommendedPotList || mRecommendedPotList.size() == 0) {
            return;
        }
        ListBean listBean = mRecommendedPotList.get(position);
        if (null == listBean) {
            return;
        }
        augmentedProductSkuId = listBean.getSku_id();
        augmentedProductName = listBean.getName();
        augmentedProductPrice = listBean.getPrice();
        augmentedProductMonthRent = listBean.getMonth_rent();
        augmentedProductTradePrice = listBean.getTrade_price();
        augmentedProductPic1 = listBean.getPic1();
        augmentedProductPic2 = listBean.getPic2();
        augmentedProductPic3 = listBean.getPic3();
        augmentedProductHeight = listBean.getHeight();
        augmentedProductOffsetRatio = listBean.getOffset_ratio();
        augmentedCombinationType = listBean.getComtype();
        augmentedPriceCode = listBean.getPrice_code();
        augmentedTradePriceCode = listBean.getTrade_price_code();
    }

    private void enableBackground() {
        String text;
        int textColor;
        Drawable drawable;
        int backgroundColor;

        if (enableBackground) {
            text = getResources().getString(R.string.close_background);
            textColor = ContextCompat.getColor(this, R.color.colorPrimary);
            drawable = ContextCompat.getDrawable(this, R.drawable.perspective_click);
            backgroundColor = ContextCompat.getColor(this, R.color.white_fifty_percent_color);
        } else {
            text = getResources().getString(R.string.open_background);
            textColor = ContextCompat.getColor(this, R.color.main_text_color);
            drawable = ContextCompat.getDrawable(this, R.drawable.perspective_default);
            backgroundColor = ContextCompat.getColor(this, R.color.line_color);
        }

        mCloseBackgroundTextView.setText(text);
        mCloseBackgroundTextView.setTextColor(textColor);
        mCloseBackgroundTextView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        mRootLayout.setBackgroundColor(backgroundColor);
    }

    /**
     * 在保存"模拟"数据成功时回调
     */
    @Override
    public void onSaveSimulationDataSuccess() {
        MessageUtil.showMessage(getResources().getString(R.string.added_to_the_simulation_scene_successfully));
        Intent intent = new Intent(this, SceneActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 在保存"报价"数据成功时回调
     */
    @Override
    public void onSaveQuotationDataDataSuccess() {
        MessageUtil.showMessage(getResources().getString(R.string.dding_to_purchase_list_successfully));
    }

    /**
     * 在保存"模拟"or"报价"数据失败时回调
     */
    @Override
    public void onSaveSimulationOrQuotationDataFairly() {
        MessageUtil.showMessage(getResources().getString(R.string.operation_failed));
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

    /**
     * 在请求网络数据之前显示Loading界面
     */
    @Override
    public void onShowLoading() {

    }

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    @Override
    public void onHideLoading() {

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
        if (null != mCollocationPresenter) {
            YiBaiApplication.setEnableBackground(enableBackground);
            mCollocationPresenter.onDetachView();
            mCollocationPresenter = null;
        }
    }
}
