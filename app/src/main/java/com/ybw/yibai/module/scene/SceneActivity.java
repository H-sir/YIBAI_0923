package com.ybw.yibai.module.scene;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.FragmentPagerAdapter;
import com.ybw.yibai.common.adapter.SceneListAdapter;
import com.ybw.yibai.common.adapter.SceneQuotationAdapter;
import com.ybw.yibai.common.bean.AddSchemePathBead;
import com.ybw.yibai.common.bean.BarViewSelected;
import com.ybw.yibai.common.bean.BottomSheetBehaviorState;
import com.ybw.yibai.common.bean.CreateSceneData;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.bean.DesignScheme;
import com.ybw.yibai.common.bean.DesignSchemeRequest;
import com.ybw.yibai.common.bean.ExistSimulationData;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.PlacementQrQuotationList;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.bean.SaveAddSchemeBean;
import com.ybw.yibai.common.bean.SceneDesign;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SceneInfoChange;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.bean.StickerViewSelected;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.bean.SystemParameter.DataBean.SpectypeBean;
import com.ybw.yibai.common.bean.ToFragment;
import com.ybw.yibai.common.bean.ViewPagerPosition;
import com.ybw.yibai.common.classs.ScenePopupMenu;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.model.CreateSceneOrPicModel;
import com.ybw.yibai.common.network.response.BaseResponse;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.NavigationBarUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.utils.SPUtil;
import com.ybw.yibai.common.utils.ScreenAdaptationUtils;
import com.ybw.yibai.common.widget.ArcMenu;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.stickerview.BaseSticker;
import com.ybw.yibai.module.designdetails.DesignDetailsActivity;
import com.ybw.yibai.module.drawing.SimulationDrawingActivity;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.market.MarketActivity;
import com.ybw.yibai.module.more.MoreActivity;
import com.ybw.yibai.module.preselection.ConsumerPreselectionFragment;
import com.ybw.yibai.module.scene.SceneContract.ScenePresenter;
import com.ybw.yibai.module.scene.SceneContract.SceneView;
import com.ybw.yibai.module.sceneedit.SceneEditFragment;
import com.ybw.yibai.module.startdesign.StartDesignActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.DbManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.CREATE_SCENE_DATA_LIST;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_PRESELECTION_PRODUCT_LIST;
import static com.ybw.yibai.common.constants.Preferences.DESIGN_CREATE;
import static com.ybw.yibai.common.constants.Preferences.DESIGN_NUMBER;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ID;
import static com.ybw.yibai.common.constants.Preferences.SCENE_INFO;
import static com.ybw.yibai.common.utils.OtherUtil.setNativeLightStatusBar;
import static com.ybw.yibai.common.utils.OtherUtil.splitList;
import static com.ybw.yibai.common.utils.ViewPagerIndicatorUtil.initDots;

/**
 * 场景界面
 *
 * @author sjl
 * @date 2019/9/14
 * https://blog.csdn.net/u010365819/article/details/76618443?utm_source=blogxgwz2
 */
public class SceneActivity extends BaseActivity implements SceneView,
        View.OnClickListener,
        ScenePopupMenu.OnItemClickListener,
        SceneQuotationAdapter.OnItemClickListener,
        SceneListAdapter.OnCheckBoxClickListener,
        SceneListAdapter.OnSceneNameClickListener,
        SceneListAdapter.OnItemClickListener {

    private static final String TAG = "SceneActivity";

    /**
     * 是否获取全部:1获取全部0分页(默认为0分页)
     */
    private int isAll = 1;

    /**
     * 显示场景详情的ViewPager当前选中的位置
     */
    private int position;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 标题view
     */
    private RelativeLayout mBarView;

    private CoordinatorLayout mRootLayout;

    /**
     * 抽屉式布局
     */
    private DrawerLayout mDrawerLayout;

    /**
     * 容纳SceneEditFragment的ViewPager
     */
    private ViewPager mViewPager;

    /**
     * 返回图标
     */
    private ImageView mBackImageView;

    /**
     * 标题
     */
    private TextView mTitleTextView;

    /**
     * 将屏幕旋转90°
     */
    private ImageView mScreenRotationImageView;

    /**
     * 显示场景列表
     */
    private ImageView mListImageView;

    /**
     * 浮动Button
     */
    private FloatingActionButton mFloatingActionButton;

    /*----------*/

    /**
     * 底部功能Layout
     */
    private LinearLayout mFunctionLayout;

    /**
     * 背景图
     */
    private TextView mBackgroundImageTextView;

    /**
     * 推荐搭配
     */
    private TextView mRecommendedMatchTextView;

    /**
     * 加入报价
     */
    private TextView mJoinQuotationTextView;

    /**
     * 保存
     */
    private TextView mSaveTextView;

    /**
     * 保存的数量
     */
    private TextView mSaveTextViewNum;

    /**
     * 我的设计
     */
    private TextView mMyDesignImageTextView;

    /*----------*/

    /**
     * 导航视图
     */
    private NavigationView mNavigationView;

    /**
     * 编辑场景列表
     */
    private TextView mEditTextView;

    /**
     * 显示场景列表
     */
    private ListView mListView;

    /**
     * 全选/删除 Root布局
     */
    private LinearLayout mBottomLayout;

    /**
     * 全选
     */
    private LinearLayout mCheckAllLayout;

    /**
     *
     */
    private CheckBox mCheckBox;

    /**
     * 删除
     */
    private TextView mDeleteTextView;

    /*----------*/

    /**
     * 推荐搭配
     */
    private RelativeLayout mRecommendedMatchLayout;

    /**
     * 全部删除"客户预选"
     */
    private TextView mAllDeleteTextView;

    /**
     * 推荐搭配类别
     */
    private RadioGroup mRecommendedMatchTypeRadioGroup;

    /**
     * 显示推荐搭配类别内容
     */
    private ViewPager mRecommendedMatchTypeViewPager;

    /**
     * 指示点父布局
     */
    private LinearLayout mRecommendedMatchDotLinearLayout;

    /**
     *
     */
    private BottomSheetBehavior mRecommendedMatchBottomSheetBehavior;

    /*----------*/

    /**
     * 显示报价数据
     */
    private RelativeLayout mQuotationLayout;

    /**
     * 报价详情
     */
    private TextView mQuotationDetailsTextView;

    /**
     * 显示报价详情
     */
    private RecyclerView mRecyclerView;

    /**
     * 添加到报价列表
     */
    private TextView mAddQuotationTextView;

    /**
     *
     */
    private BottomSheetBehavior mQuotationBottomSheetBehavior;

    /*----------*/

    /**
     * 弧形菜单
     */
    private ArcMenu mArcMenu;

    /**
     * 场景界面中间PopupMenu
     */
    private ScenePopupMenu mPopupMenu;

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
     * 盆栽大中小类别
     */
    private List<SpectypeBean> mBonsaiSpecTypeList;

    /**
     * 创建场景时需要的数据
     */
    private List<CreateSceneData> mCreateSceneDataList;

    /**
     * 用户场景信息列表的集合
     */
    private List<SceneInfo> mSceneInfoList;

    /**
     * 显示场景列表的适配器
     */
    private SceneListAdapter mSceneListAdapter;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 待摆放清单列表
     */
    private List<PlacementQrQuotationList.DataBean.ListBean> mPlacementList;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 用户保存的"报价"数据列表
     */
    private List<QuotationData> mQuotationDataList;

    /**
     * 显示报价列表的适配器
     */
    private SceneQuotationAdapter mSceneQuotationAdapter;

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
    private View mBtnCamera;

    /**
     * 要申请的权限(访问手机相机权限)
     */
    private String[] permissions = {
            Manifest.permission.CAMERA
    };

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private ScenePresenter mScenePresenter;

    /**
     * 创建设计的编号
     */
    private String mDesingNumber = null;
    /**
     * 是否新建设计号
     */
    private boolean designCreate;

    /**
     * 当前正在编辑用户场景
     */
    private SceneInfo mSceneInfo;

    /**
     * 场景数据列表
     */
    private List<SceneDesign> mSceneDesignDataList = new ArrayList<>();

    @Override
    protected int setLayout() {
        mContext = this;
        Application application = (Application) YiBaiApplication.getContext();
        if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            ScreenAdaptationUtils.applyAdaptationScreen(application, this, ORIENTATION_PORTRAIT);
        } else {
            ScreenAdaptationUtils.applyAdaptationScreen(application, this, ORIENTATION_LANDSCAPE);
        }
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        YiBaiApplication.setWindowWidth(width);
        YiBaiApplication.setWindowHeight(height);
        setNativeLightStatusBar(this, true);
//        if (NavigationBarUtil.hasNavigationBar(this)) {
//            NavigationBarUtil.initActivity(findViewById(android.R.id.content));
//        }
        return R.layout.activity_scene;
    }

    @Override
    protected void initView() {
        mRootLayout = findViewById(R.id.rootLayout);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mBarView = findViewById(R.id.barView);
        mViewPager = findViewById(R.id.viewPager);
        mBackImageView = findViewById(R.id.backImageView);
        mTitleTextView = findViewById(R.id.titleTextView);
        mScreenRotationImageView = findViewById(R.id.screenRotationImageView);
        mListImageView = findViewById(R.id.listImageView);
        mFloatingActionButton = findViewById(R.id.floatingActionButton);

        mFunctionLayout = findViewById(R.id.functionLayout);
        mBackgroundImageTextView = findViewById(R.id.backgroundImageTextView);
        mRecommendedMatchTextView = findViewById(R.id.recommendedMatchTextView);
        mJoinQuotationTextView = findViewById(R.id.joinQuotationTextView);
        mSaveTextView = findViewById(R.id.saveTextView);
        mSaveTextViewNum = findViewById(R.id.saveTextViewNum);
        mMyDesignImageTextView = findViewById(R.id.myDesignImageTextView);

        mNavigationView = findViewById(R.id.nav_view);
        mEditTextView = findViewById(R.id.editTextView);
        mListView = findViewById(R.id.listView);
        mBottomLayout = findViewById(R.id.bottomEditLayout);
        mBtnCamera = findViewById(R.id.floatingActionCamera);
        mCheckAllLayout = findViewById(R.id.checkAllLayout);
        mCheckBox = findViewById(R.id.checkbox);
        mDeleteTextView = findViewById(R.id.deleteTextView);

        mRecommendedMatchLayout = findViewById(R.id.recommended_match_layout);
        mAllDeleteTextView = findViewById(R.id.allDeleteTextView);
        mRecommendedMatchTypeRadioGroup = findViewById(R.id.recommendedMatchTypeRadioGroup);
        mRecommendedMatchTypeViewPager = findViewById(R.id.recommendedMatchTypeViewPager);
        mRecommendedMatchDotLinearLayout = findViewById(R.id.recommendedMatchDotLinearLayout);

        mQuotationLayout = findViewById(R.id.quotation_layout);
        mQuotationDetailsTextView = findViewById(R.id.quotationDetailsTextView);
        mRecyclerView = findViewById(R.id.recyclerView);
        mAddQuotationTextView = findViewById(R.id.addQuotationTextView);

        mRecommendedMatchBottomSheetBehavior = BottomSheetBehavior.from(mRecommendedMatchLayout);
        mRecommendedMatchBottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
        mRecommendedMatchBottomSheetBehavior.setState(STATE_HIDDEN);

        mQuotationBottomSheetBehavior = BottomSheetBehavior.from(mQuotationLayout);
        mQuotationBottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
        mQuotationBottomSheetBehavior.setState(STATE_HIDDEN);

        mArcMenu = findViewById(R.id.arcMenu);
        mPopupMenu = ScenePopupMenu.getInstance();
        mWaitDialog = new WaitDialog(this);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);
    }

    String mComType;

    @SuppressLint("RestrictedApi")
    @Override
    protected void initData() {
        mBonsaiSpecTypeList = new ArrayList<>();
        mCreateSceneDataList = new ArrayList<>();
        mSceneInfoList = new ArrayList<>();
        mPlacementList = new ArrayList<>();
        mQuotationDataList = new ArrayList<>();

        Intent intent = getIntent();
        if (null != intent) {
            mComType = intent.getStringExtra("com_type");
            if (mComType == null || mComType.isEmpty()) {
                mComType = SPUtil.INSTANCE.getValue(getApplicationContext(), "com_type", String.class);
            }
            mCreateSceneDataList = intent.getParcelableArrayListExtra(CREATE_SCENE_DATA_LIST);
            designCreate = intent.getBooleanExtra(DESIGN_CREATE, false);
        }

        mSceneListAdapter = new SceneListAdapter(this, mSceneInfoList);
        mListView.setAdapter(mSceneListAdapter);
        mSceneListAdapter.setOnCheckBoxClickListener(this);
        mSceneListAdapter.setOnSceneNameClickListener(this);
        mSceneListAdapter.setOnItemClickListener(this);

        mSceneQuotationAdapter = new SceneQuotationAdapter(this, mQuotationDataList);
        mRecyclerView.setAdapter(mSceneQuotationAdapter);
        mSceneQuotationAdapter.setOnItemClickListener(this);

        if (mComType != null && mComType.equals("1")) {
            mFloatingActionButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mScenePresenter = new ScenePresenterImpl(this);
//        mScenePresenter.setNavigationViewParams(mNavigationView);
//        mScenePresenter.addSceneInfo(mCreateSceneDataList);

        mBackImageView.setOnClickListener(this);
        mTitleTextView.setOnClickListener(this);
        mScreenRotationImageView.setOnClickListener(this);
        mListImageView.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);

        mBackgroundImageTextView.setOnClickListener(this);
        mRecommendedMatchTextView.setOnClickListener(this);
        mJoinQuotationTextView.setOnClickListener(this);
        mSaveTextView.setOnClickListener(this);
        mMyDesignImageTextView.setOnClickListener(this);

        mDrawerLayout.addDrawerListener(mDrawerListener);
        mEditTextView.setOnClickListener(this);
        mCheckAllLayout.setOnClickListener(this);
        mDeleteTextView.setOnClickListener(this);
        mBtnCamera.setOnClickListener(this);
        mAllDeleteTextView.setOnClickListener(this);

        mQuotationDetailsTextView.setOnClickListener(this);
        mAddQuotationTextView.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(mViewPagerListener);
        mArcMenu.setArcMenuStateListener(mArcMenuStateListener);

        if (designCreate) {
            designCreate = false;
            if (mCreateSceneDataList == null || mCreateSceneDataList.size() == 0) {
                mScenePresenter.addDesignAndSceneInfo();//创建新场景
            } else {
                mScenePresenter.addDesignAndSceneInfo(mCreateSceneDataList);//创建新场景
            }
        } else {
            /**
             * 获取场景信息
             * */
            if (mCreateSceneDataList == null || mCreateSceneDataList.size() == 0) {
                mScenePresenter.findUserSceneListInfo(true);
            } else {
                mScenePresenter.addSceneInfo(mCreateSceneDataList);//创建新场景
            }
        }
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * 在用户点击相机按钮打开{@link StartDesignActivity} 接收该界面传递过来的数据
     *
     * @param intent Intent对象
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent) {
            mComType = intent.getStringExtra("com_type");
            if (mComType == null || mComType.isEmpty()) {
                mComType = SPUtil.INSTANCE.getValue(getApplicationContext(), "com_type", String.class);
            }
            mCreateSceneDataList = intent.getParcelableArrayListExtra(CREATE_SCENE_DATA_LIST);
        }
        initEvent();
    }

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    public void onClick(View v) {
        int id = v.getId();

        // 返回
        if (id == R.id.backImageView) {
            onBackPressed();
        }

        // 将屏幕旋转90°
        if (id == R.id.screenRotationImageView) {
            if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
                // 横屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                // 竖屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        // 显示场景列表
        if (id == R.id.listImageView || id == R.id.titleTextView) {
            if (!mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        }

        // 浮动Button
        if (id == R.id.floatingActionButton) {
            Intent intent = new Intent(this, StartDesignActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        // 背景图
        if (id == R.id.backgroundImageTextView) {

        }

        // 推荐搭配
        if (id == R.id.recommendedMatchTextView) {
            mScenePresenter.getPlacementList();
        }

        // 加入报价
        if (id == R.id.joinQuotationTextView) {

        }

        // 保存图片
        if (id == R.id.saveTextView) {
            try {
                DbManager manager = YiBaiApplication.getDbManager();
                mSceneInfo.setCount(mSceneInfo.getCount() + 1);
                manager.update(mSceneInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }
            /**
             * 发送数据到{@link SceneEditFragment#onAddScheme(SceneContract.SceneView)}
             */
            EventBus.getDefault().post(this);
        }
        // 我的设计
        if (id == R.id.myDesignImageTextView) {
            Intent intent = new Intent(this, DesignDetailsActivity.class);
            intent.putExtra(DESIGN_NUMBER, mDesingNumber);
            startActivity(intent);

//            Intent intent = new Intent(this, SimulationDrawingActivity.class);
//            startActivity(intent);
        }

        // 编辑
        if (id == R.id.editTextView) {
            for (SceneInfo sceneInfo : mSceneInfoList) {
                if (sceneInfo.isShowCheckBox()) {
                    sceneInfo.setShowCheckBox(false);
                    sceneInfo.setSelectCheckBox(false);
                } else {
                    sceneInfo.setShowCheckBox(true);
                    sceneInfo.setSelectCheckBox(false);
                }
            }
            mSceneListAdapter.notifyDataSetChanged();
            if (mSceneInfoList.get(0).isShowCheckBox()) {
                mBtnCamera.setVisibility(View.GONE);
                mBottomLayout.setVisibility(View.VISIBLE);
                mCheckBox.setChecked(false);
            } else {
                mBtnCamera.setVisibility(View.VISIBLE);
                mBottomLayout.setVisibility(View.GONE);
                mCheckBox.setChecked(false);
            }
        }

        // 全选
        if (id == R.id.checkAllLayout) {
            if (mCheckBox.isChecked()) {
                mCheckBox.setChecked(false);
            } else {
                mCheckBox.setChecked(true);
            }
            boolean checked = mCheckBox.isChecked();
            for (SceneInfo sceneInfo : mSceneInfoList) {
                if (checked) {
                    sceneInfo.setSelectCheckBox(true);
                } else {
                    sceneInfo.setSelectCheckBox(false);
                }
            }
            mSceneListAdapter.notifyDataSetChanged();
        }

        // 删除
        if (id == R.id.deleteTextView) {
            mScenePresenter.displayDeleteSceneNameDialog(mSceneInfoList);
        }

        // 全部删除"客户预选"
        if (id == R.id.allDeleteTextView) {
            if (null == mPlacementList || mPlacementList.size() == 0) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            int size = mPlacementList.size();
            for (int i = 0; i < size; i++) {
                PlacementQrQuotationList.DataBean.ListBean listBean = mPlacementList.get(i);
                if (null == listBean) {
                    continue;
                }
                int quoteId = listBean.getQuote_id();
                stringBuilder.append(quoteId);
                if (i != size - 1) {
                    stringBuilder.append(",");
                }
            }
            mScenePresenter.deletePlacementList(stringBuilder.toString());
        }

        // 跳转到报价详情
        if (id == R.id.quotationDetailsTextView) {
            /**
             * 发送数据到{@link MainActivity#ratioActivitySendData(ToFragment)}
             * 使其跳转到对应的Fragment
             */
            ToFragment toFragment = new ToFragment(3);
            EventBus.getDefault().postSticky(toFragment);
            finish();
        }

        // 将本场景内摆放的盆栽添加到报价中
        if (id == R.id.addQuotationTextView) {
            SceneInfo sceneInfo = mSceneInfoList.get(position);
            mScenePresenter.addQuotation(sceneInfo.getSceneId());
        }

        if (id == R.id.floatingActionCamera) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
            }
        }

        // 盆栽信息
        if (id == R.id.bonsaiInfoTextView) {
//            BaseSticker currentSticker = mStickerView.getCurrentSticker();
//            if (null == currentSticker) {
//                return;
//            }
//            int position = (int) currentSticker.getTag();
//            SimulationData simulationData = mSimulationDataList.get(position);
//            mSceneEditPresenter.displayPlantInfoPopupWindow(mRootView, simulationData);
        }

        // 加入相册
        if (id == R.id.joinPhotoAlbum) {
//            BaseSticker currentSticker = mStickerView.getCurrentSticker();
//            if (null == currentSticker) {
//                return;
//            }
//            int position = (int) currentSticker.getTag();
//            SimulationData simulationData = mSimulationDataList.get(position);
//            mSceneEditPresenter.joinPhotoAlbum(simulationData);
        }

        // 加入进货
        if (id == R.id.joinPurchaseTextView) {
//            BaseSticker currentSticker = mStickerView.getCurrentSticker();
//            if (null == currentSticker) {
//                return;
//            }
//            int position = (int) currentSticker.getTag();
//            SimulationData simulationData = mSimulationDataList.get(position);
//            mSceneEditPresenter.addQuotationData(simulationData);
        }

        //查看货源
        if (id == R.id.skuMarketTextView) {
//            if (augmentedProductSkuId != 0 && productSkuId != 0) {
//                skuMarketPopupWindow();
//            } else {
//                Intent intent = new Intent(getActivity(), MarketActivity.class);
//                if (productSkuId != 0) {
//                    intent.putExtra(PRODUCT_SKU_ID, productSkuId);
//                } else {
//                    intent.putExtra(PRODUCT_SKU_ID, augmentedProductSkuId);
//                }
//                startActivity(intent);
//            }
        }

        // 加入该款
        if (id == R.id.changeStyleTextView) {
//            replaceCollocation(false);
            if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
            }
        }

        /**
         * 发送数据到{@link SceneEditFragment#onSceneActivityClick(View)}
         */
        EventBus.getDefault().post(v);
    }

    /**
     * 场景列表中的CheckBox选择状态发生改变时回调
     *
     * @param position 位置
     */
    @Override
    public void onCheckBoxClick(int position) {
        // 判断用户是否全部选中CheckBox默认是的
        boolean allSelect = true;
        for (SceneInfo sceneInfo : mSceneInfoList) {
            boolean selectCheckBox = sceneInfo.isSelectCheckBox();
            if (!selectCheckBox) {
                // 说明存在没有选中的CheckBox
                allSelect = false;
                break;
            }
        }
        if (allSelect) {
            mCheckBox.setChecked(true);
        } else {
            mCheckBox.setChecked(false);
        }
    }


    /**
     * EventBus
     * 接收用户从{@link SceneEditFragment}传递过来的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void uploadSceneNum(SceneInfo sceneInfo) {
        mSaveTextViewNum.setVisibility(View.VISIBLE);
        mSaveTextViewNum.setText(String.valueOf(SceneHelper.getPhotoNum(getApplicationContext())));
    }


    /**
     * 场景列表中的场景名称点击时回调
     *
     * @param position 位置
     */
    @Override
    public void onSceneNameClick(int position) {
        mScenePresenter.displayEditSceneNamePopupWindow(mRootLayout, mSceneInfoList, position);
    }

    /**
     * 在场景列表ListView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onSceneListItemClick(int position) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }
        mViewPager.setCurrentItem(position);
        EventBus.getDefault().post(new BaseResponse<String>(2, "场景列表项被点击", null));
    }

    /**
     * 在PopupWindow Item 点击时回调
     *
     * @param v        被点击的View
     * @param position 被点击的Item位置
     */
    @Override
    public void onPopupWindowItemClick(View v, int position) {
        if (1 == position) {
            // 拍照
            mScenePresenter.applyPermissions(permissions);
        } else if (2 == position) {
            // 相册
            mScenePresenter.openPhotoAlbum();
        } else if (3 == position) {
            // 背景模板
        }
        if (null != mPopupMenu && mPopupMenu.isShowing()) {
            mPopupMenu.closePopupWindow();
        }
    }

    /**
     * 在显示报价列表的RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        LogUtil.e(TAG, "在显示报价列表的RecyclerView Item 点击时回调: " + position);
        /**
         * 发送数据到{@link SceneEditFragment#onSelectedQuotationData(QuotationData)}
         */
        EventBus.getDefault().post(mQuotationDataList.get(position));
    }

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
            onBottomSheetBehaviorStateChanged(new BottomSheetBehaviorState(newState));
        }
    };

    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            if (0 == slideOffset) {
                mArcMenu.show();
            } else {
                mArcMenu.hide();
            }
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            for (SceneInfo sceneInfo : mSceneInfoList) {
                sceneInfo.setShowCheckBox(false);
                sceneInfo.setSelectCheckBox(false);
            }
            mSceneListAdapter.notifyDataSetChanged();
            mBottomLayout.setVisibility(View.GONE);
            mCheckBox.setChecked(false);
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    private ViewPager.OnPageChangeListener mViewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            SceneActivity.this.position = position;
            if (null == mSceneInfoList || mSceneInfoList.size() == 0) {
                return;
            }
            for (int i = 0; i < mSceneInfoList.size(); i++) {
                SceneInfo sceneInfo = mSceneInfoList.get(i);
                if (position == i) {
                    // 将当前页面设置为在编辑的场景
                    sceneInfo.setEditScene(true);
                } else {
                    sceneInfo.setEditScene(false);
                }
            }
            // 延时0.01S,防止TitleTextView.setText(sceneName);无效
            SceneInfo sceneInfo = mSceneInfoList.get(position);
            if (null != sceneInfo) {
                new Handler().postDelayed(() -> {
                    String sceneName = sceneInfo.getSceneName();
                    if (TextUtils.isEmpty(sceneName)) {
                        mTitleTextView.setText(" ");
                    } else {
                        mTitleTextView.setText(sceneName);
                        EventBus.getDefault().post(new BaseResponse<String>(1, "", sceneName));
                    }
                }, 10);
            }
            // 在数据库中更新当前的位置为为在编辑的场景
            mScenePresenter.updateSceneListInfo(mSceneInfoList);
            /**
             * 发送数据到{@link SceneEditFragment#onPageSelected(ViewPagerPosition)}
             */
            EventBus.getDefault().post(new ViewPagerPosition(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
    };

    /**
     * ArcMenu 各种状态侦听器
     */
    private ArcMenu.ArcMenuStateListener mArcMenuStateListener = new ArcMenu.ArcMenuStateListener() {
        @Override
        public void onArcMenuExpand() {

        }

        @Override
        public void onArcMenuShrink() {

        }

        @Override
        public void onChildViewClick(View v) {
            int index = (int) v.getTag();
            SpectypeBean specType = mBonsaiSpecTypeList.get(index);
            if (null == specType) return;
            int id = specType.getId();
            if (-1 != id) {
                /**
                 * 发送数据到{@link SceneEditFragment#onPlantSpecItemClick(SpectypeBean)}
                 */
                EventBus.getDefault().post(specType);
                return;
            }
            Intent intent = new Intent(mContext, MoreActivity.class);
            startActivity(intent);
        }
    };

    /**
     * 申请权限的结果
     *
     * @param b true 已经获取全部权限,false 没有获取全部权限
     */
    @Override
    public void applyPermissionsResults(boolean b) {
        if (b) {
            // 打开相机
            mScenePresenter.openCamera();
        }
    }

    /**
     * 请求权限的结果的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获得申请的全部权限
        if (PermissionsUtil.allPermissionsAreGranted(grantResults)) {
            // 打开相机
            mScenePresenter.openCamera();
        } else {
            PermissionsUtil.showNoCameraPermissions(this);
        }
    }

    /**
     * 获得Activity返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mScenePresenter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 返回从相机或相册返回的图像
     *
     * @param file 图像文件
     */
    @Override
    public void returnsTheImageReturnedFromTheCameraOrAlbum(File file) {
        List<CreateSceneData> createSceneDataList = new ArrayList<>();
        CreateSceneData createSceneData = new CreateSceneData(file);
        createSceneDataList.add(createSceneData);
        mScenePresenter.addSceneInfo(createSceneDataList);
        mScenePresenter.findUserSceneListInfo();
    }

    // 14.4 创建场景或场景添加图片产品
    @Override
    public void onCreateSceneOrPicCallback(CreateSceneOrPicModel model) {

    }

    /**
     * EventBus
     * 接收用户从{@link SceneEditFragment}传递过来的数据
     *
     * @param saveAddSchemeBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void saveAddScheme(SaveAddSchemeBean saveAddSchemeBean) {
        DesignSchemeRequest designSchemeRequest = new DesignSchemeRequest();
        designSchemeRequest.setDesingNumber(mDesingNumber);
        designSchemeRequest.setType(2);
        designSchemeRequest.setSchemeId(mSceneInfo.getScheme_id());
        designSchemeRequest.setPic(saveAddSchemeBean.getPathName());
        designSchemeRequest.setProductSkuId(saveAddSchemeBean.getProductSkuId() + "," + saveAddSchemeBean.getAugmentedProductSkuId());

        mScenePresenter.designScheme(designSchemeRequest);
    }

    @Override
    public void onGetAddDesignSchemeSuccess(DesignScheme designScheme) {
        SceneHelper.savePhotoNum(getApplicationContext(), SceneHelper.getPhotoNum(getApplicationContext()) + 1);
        MessageUtil.showMessage("添加完成");
        for (Iterator<SceneDesign> iterator = mSceneDesignDataList.iterator(); iterator.hasNext(); ) {
            SceneDesign sceneDesign = iterator.next();
            if (sceneDesign.getSchemeName().equals(designScheme.getData().getSchemeName())) {
                sceneDesign.setSceneNum(sceneDesign.getSceneNum() + 1);
                break;
            }
        }
        mSaveTextViewNum.setVisibility(View.VISIBLE);
        mSaveTextViewNum.setText(String.valueOf(mSceneInfo.getCount()));
//        mSceneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddSchemePath(String path, int productSkuId, int augmentedProductSkuId) {
        DesignSchemeRequest designSchemeRequest = new DesignSchemeRequest();
        designSchemeRequest.setDesingNumber(mDesingNumber);
        designSchemeRequest.setType(2);
        designSchemeRequest.setSchemeId(mSceneInfo.getScheme_id());
        designSchemeRequest.setPic(path);
        designSchemeRequest.setProductSkuId(productSkuId + "," + augmentedProductSkuId);
        mScenePresenter.designScheme(designSchemeRequest);
    }

    /**
     * EventBus
     * 接收用户从{@link SceneEditFragment} 传递过来的保存设计的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddSchemePath(AddSchemePathBead addSchemePathBead) {
        DesignSchemeRequest designSchemeRequest = new DesignSchemeRequest();
        designSchemeRequest.setDesingNumber(mDesingNumber);
        designSchemeRequest.setType(2);
        designSchemeRequest.setSchemeId(mSceneInfo.getScheme_id());
        designSchemeRequest.setPic(addSchemePathBead.getPathName());
        designSchemeRequest.setProductSkuId(addSchemePathBead.getProductSkuId() + "," + addSchemePathBead.getAugmentedProductSkuId());
        mScenePresenter.designScheme(designSchemeRequest);
    }

    @Override
    public void onGetDesignSchemeSuccess(DesignScheme designScheme) {
        SceneDesign sceneDesign = new SceneDesign();
        sceneDesign.setSchemeId(designScheme.getData().getSchemeId());
        sceneDesign.setDesingNumber(designScheme.getData().getDesingNumber());
        sceneDesign.setSchemeName(designScheme.getData().getSchemeName());
        sceneDesign.setSceneNum(1);
        mSceneDesignDataList.add(sceneDesign);
//        mSceneAdapter.notifyDataSetChanged();
    }

    /**
     * 新创建场景信息的结果
     *
     * @param result true新创建成功,false新创建失败
     */
    @Override
    public void addSceneInfoResult(boolean result) {
        mScenePresenter.findUserSceneListInfo();
    }

    /**
     * 在查找用户的场景信息成功时回调
     *
     * @param sceneInfoList 用户场景信息列表
     */
    @Override
    public void onFindUserSceneInfoSuccess(List<SceneInfo> sceneInfoList) {
        mSceneInfoList.clear();
        if (null == sceneInfoList || sceneInfoList.size() == 0) {
            return;
        }
        mSceneInfoList.addAll(sceneInfoList);
        // 查找正在编辑的场景的位置
        int editScenePosition = 0;
        Fragment[] fragment = new Fragment[sceneInfoList.size()];
        for (int i = 0; i < sceneInfoList.size(); i++) {
            SceneInfo sceneListInfo = sceneInfoList.get(i);
            fragment[i] = new SceneEditFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(POSITION, i);
            bundle.putString("com_type", mComType);
            bundle.putString("location", getIntent().getStringExtra("location"));
            bundle.putSerializable(SCENE_INFO, sceneListInfo);
            fragment[i].setArguments(bundle);
            if (sceneListInfo.isEditScene()) {
                editScenePosition = i;
                mDesingNumber = sceneListInfo.getNumber();
                mSceneInfo = sceneListInfo;
                mSaveTextViewNum.setText(String.valueOf(mSceneInfo.getCount()));
                if (mSceneInfo.getCount() == 0)
                    mSaveTextViewNum.setVisibility(View.GONE);
            }
        }
        FragmentManager manager = getSupportFragmentManager();
        mViewPager.removeAllViews();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
        mViewPager.setCurrentItem(editScenePosition);
        mViewPagerListener.onPageSelected(editScenePosition);
        mSceneListAdapter.notifyDataSetChanged();
        String sceneName = sceneInfoList.get(0).getSceneName();
        if (TextUtils.isEmpty(sceneName)) {
            mTitleTextView.setText(" ");
        } else {
            mTitleTextView.setText(sceneName);
            EventBus.getDefault().post(new BaseResponse<String>(1, "", sceneName));
        }
        EventBus.getDefault().postSticky(new ViewPagerPosition(editScenePosition));
    }

    /**
     * 在获取摆放清单列表成功时回调
     *
     * @param placementQrQuotationList 摆放清单列表
     */
    @Override
    public void onGetPlacementListSuccess(PlacementQrQuotationList placementQrQuotationList) {
        if (CODE_SUCCEED != placementQrQuotationList.getCode()) {
            MessageUtil.showMessage(placementQrQuotationList.getMsg());
            return;
        }
        PlacementQrQuotationList.DataBean data = placementQrQuotationList.getData();
        if (null == data) {
            return;
        }

        mPlacementList.clear();
        mRecommendedMatchTypeViewPager.removeAllViews();
        mRecommendedMatchBottomSheetBehavior.setState(STATE_EXPANDED);

        List<PlacementQrQuotationList.DataBean.ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        mPlacementList.addAll(list);
        List<List<PlacementQrQuotationList.DataBean.ListBean>> lists = splitList(list, 20);
        if (null == lists || lists.size() == 0) {
            return;
        }
        Fragment[] fragment = new Fragment[lists.size()];
        for (int i = 0; i < lists.size(); i++) {
            List<PlacementQrQuotationList.DataBean.ListBean> beans = lists.get(i);
            fragment[i] = new ConsumerPreselectionFragment();
            Bundle bundle = new Bundle();
            bundle.putString(CUSTOMERS_PRESELECTION_PRODUCT_LIST, new Gson().toJson(beans));
            fragment[i].setArguments(bundle);
        }
        initDots(this, mRecommendedMatchDotLinearLayout, lists.size());
        FragmentManager manager = getSupportFragmentManager();
        mRecommendedMatchTypeViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
    }

    /**
     * EventBus
     * 接收用户从{@link ConsumerPreselectionFragment#onDeletePlacementListSuccess(DeletePlacement)}
     * 传递过来的数据在删除待摆放清单产品成功时回调
     *
     * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
     */
    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeletePlacementListSuccess(DeletePlacement deletePlacement) {
        MessageUtil.showMessage(deletePlacement.getMsg());
        if (CODE_SUCCEED == deletePlacement.getCode()) {
            mScenePresenter.getPlacementList();
        }
    }

    /**
     * 在获取用户保存的"报价"数据成功时回调
     *
     * @param quotationDataList 用户保存的"报价"数据列表
     */
    @Override
    public void onGetQuotationDataSuccess(List<QuotationData> quotationDataList) {
        mQuotationDataList.clear();
        if (null != quotationDataList && quotationDataList.size() > 0) {
            Collections.sort(quotationDataList);
            mQuotationDataList.addAll(quotationDataList);
        }
        mSceneQuotationAdapter.notifyDataSetChanged();
    }

    /**
     * 更新用户场景信息列表的集合的结果
     *
     * @param result true添加成功,false添加失败
     */
    @Override
    public void updateSceneListInfoResult(boolean result) {
        if (!result) {
            return;
        }
        SceneInfo sceneInfo = mSceneInfoList.get(position);
        if (null == sceneInfo) {
            return;
        }
        String sceneName = sceneInfo.getSceneName();
        if (TextUtils.isEmpty(sceneName)) {
            mTitleTextView.setText(" ");
        } else {
            mTitleTextView.setText(sceneName);
            EventBus.getDefault().post(new BaseResponse<String>(1, "", sceneName));
        }
        mSceneListAdapter.notifyDataSetChanged();
    }

    /**
     * 将"模拟搭配产品"数据添加到报价列表数据中的结果
     *
     * @param result true添加成功,false添加失败
     */
    @Override
    public void addQuotationResult(boolean result) {
        if (result) {
            mScenePresenter.getQuotationData();
            MessageUtil.showMessage(getResources().getString(R.string.add_the_bonsai_in_this_scene_to_the_quotation_succeed));
        } else {
            MessageUtil.showMessage(getResources().getString(R.string.add_the_bonsai_in_this_scene_to_the_quotation_failure));
        }
    }

    /**
     * EventBus
     * 接收用户从{@link MainActivity#onGetSystemParameterSuccess(SystemParameter)}
     * 发送过来的数据(在获取系统参数成功时回调)
     *
     * @param systemParameter 系统参数
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getSystemParameterSuccess(SystemParameter systemParameter) {
        if (null == systemParameter) {
            return;
        }
        SystemParameter.DataBean data = systemParameter.getData();
        if (null == data) {
            return;
        }
        // 手动添加一个"更多"item
        SpectypeBean spectypeBean = new SpectypeBean();
        spectypeBean.setId(-1);
        spectypeBean.setName(getResources().getString(R.string.more));
        mBonsaiSpecTypeList.clear();
        Collections.sort(data.getSpectype(), new Comparator<SpectypeBean>() {
            @Override
            public int compare(SpectypeBean s1, SpectypeBean s2) {
                int diff = s1.getId() - s2.getId();
                if (diff > 0) {
                    return -1;
                } else if (diff < 0) {
                    return 1;
                }
                return 0;
            }
        });

        mBonsaiSpecTypeList.addAll(data.getSpectype());
        mBonsaiSpecTypeList.add(0, spectypeBean);
        for (int i = 0; i < mBonsaiSpecTypeList.size(); i++) {
            SpectypeBean specType = mBonsaiSpecTypeList.get(i);
            String name = specType.getName();
            if (TextUtils.isEmpty(name)) {
                continue;
            }
            mArcMenu.addChildView(name, i);
        }
    }

    /**
     * EventBus
     * 接收用户从{@link SceneEditFragment}
     * 传递过来的数据,在BottomSheetBehavior状态发生改变时
     *
     * @param state BottomSheetBehavior状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onBottomSheetBehaviorStateChanged(BottomSheetBehaviorState state) {
        int newState = state.getState();
        if (STATE_EXPANDED == newState || STATE_COLLAPSED == newState) {
            // 完全展开/折叠状态,就是peekHeight设置的窥视高度
            mArcMenu.hide();
            mFloatingActionButton.hide();
        } else if (STATE_HIDDEN == newState) {
            // 隐藏状态
            mArcMenu.show();
            mFloatingActionButton.show();
        }
    }

    /**
     * EventBus
     * 接收用户从{@link SceneEditFragment#returnsTheImageReturnedFromTheCameraOrAlbum(File)}
     * 传递过来的数据,在用户修改场景图片时
     *
     * @param sceneInfoChange 数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSceneInfoChange(SceneInfoChange sceneInfoChange) {
        mScenePresenter.findUserSceneListInfo();
    }

    /**
     * EventBus
     * 接收用户从{@link SceneEditFragment#onGetSimulationDataSuccess(List)} 传递过来的数据
     * （在根据场景id查找用户保存的"模拟搭配图片"数据成功时）
     *
     * @param existSimulationData 某一个场景中是否存在"模拟搭配图片"数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetSimulationDataSuccess(ExistSimulationData existSimulationData) {
        boolean exist = existSimulationData.isExist();
        if (!exist) {
            mArcMenu.expandArcMenu();
        }
    }

    /**
     * @param barViewSelected "贴纸View"被选中状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void barViewSelected(@NonNull BarViewSelected barViewSelected) {
        boolean selected = barViewSelected.isSelected();
        if (selected) {
            mBarView.setVisibility(View.GONE);
        } else {
            mBarView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * EventBus
     * 接收用户从{@link SceneEditFragment} 传递过来的数据在"贴纸View"被选中状态发生改变时
     *
     * @param stickerViewSelected "贴纸View"被选中状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void stickerViewSelected(@NonNull StickerViewSelected stickerViewSelected) {
        boolean selected = stickerViewSelected.isSelected();
        if (selected) {
            mArcMenu.hide();
            mFloatingActionButton.hide();
            mFunctionLayout.setVisibility(View.GONE);
        } else {
            mArcMenu.show();
            mFunctionLayout.setVisibility(View.VISIBLE);
            if (mComType != null && mComType.equals("1")) return;
            mFloatingActionButton.show();
        }
    }

    /**
     * EventBus
     * 接收用户从{@link ConsumerPreselectionFragment#onItemClick(int)}传递过来的数据
     *
     * @param consumerPreselection 用户选择的"客户预选产品"的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedConsumerPreselection(PlacementQrQuotationList.DataBean.ListBean consumerPreselection) {
        // 当用户选择的"客户预选产品"的数据时,隐藏该UI
        mRecommendedMatchBottomSheetBehavior.setState(STATE_HIDDEN);
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
    public void onBackPressed() {
        int state = mRecommendedMatchBottomSheetBehavior.getState();
        if (STATE_EXPANDED == state || STATE_COLLAPSED == state) {
            mRecommendedMatchBottomSheetBehavior.setState(STATE_HIDDEN);
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else if (null != mPopupMenu && mPopupMenu.isShowing()) {
            mPopupMenu.closePopupWindow();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mScenePresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mScenePresenter.onDetachView();
            mScenePresenter = null;
        }
        SPUtil.INSTANCE.putValue(getApplicationContext(), "com_type", mComType);
    }
}