package com.ybw.yibai.module.sceneedit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaygoo.widget.RangeSeekBar;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.soft.onlly.toastplus.ToastPlus;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.AlreadyPlacedAdapter;
import com.ybw.yibai.common.adapter.BTCAdapter;
import com.ybw.yibai.common.adapter.BTNAdapter;
import com.ybw.yibai.common.adapter.FragmentPagerAdapter;
import com.ybw.yibai.common.adapter.OBowlAdapter;
import com.ybw.yibai.common.adapter.OPlantAdapter;
import com.ybw.yibai.common.adapter.PlantSelectAdapter;
import com.ybw.yibai.common.adapter.PotSelectAdapter;
import com.ybw.yibai.common.adapter.RecommendPotTypeAdapter;
import com.ybw.yibai.common.adapter.SOAdapter;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.BTCBean;
import com.ybw.yibai.common.bean.BottomSheetBehaviorState;
import com.ybw.yibai.common.bean.CategorySimilarSKU;
import com.ybw.yibai.common.bean.ExistSimulationData;
import com.ybw.yibai.common.bean.FastImport;
import com.ybw.yibai.common.bean.ImageContrastSelectedProduct;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.NewMatch;
import com.ybw.yibai.common.bean.PlacementQrQuotationList;
import com.ybw.yibai.common.bean.ProductData;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.bean.Recommend;
import com.ybw.yibai.common.bean.SaveAddSchemeBean;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SceneInfoChange;
import com.ybw.yibai.common.bean.SelectProductParam;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.bean.SpecSuk;
import com.ybw.yibai.common.bean.SpecSuk.DataBean;
import com.ybw.yibai.common.bean.StickerViewSelected;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.bean.SystemParameter.DataBean.PotBean;
import com.ybw.yibai.common.bean.SystemParameter.DataBean.SpectypeBean;
import com.ybw.yibai.common.bean.ToFragment;
import com.ybw.yibai.common.bean.UserPosition;
import com.ybw.yibai.common.bean.ViewPagerPosition;
import com.ybw.yibai.common.contants.GVar;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.model.CreateSceneOrPicModel;
import com.ybw.yibai.common.model.ItemDesignSceneModel;
import com.ybw.yibai.common.network.response.BaseResponse;
import com.ybw.yibai.common.network.response.ResponsePage;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.FilterFactoryUtil;
import com.ybw.yibai.common.utils.GuideUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.utils.SPUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.common.widget.HorizontalViewPager;
import com.ybw.yibai.common.widget.MatchLayout;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.stickerview.BaseSticker;
import com.ybw.yibai.common.widget.stickerview.BitmapStickerIcon;
import com.ybw.yibai.common.widget.stickerview.StickerView;
import com.ybw.yibai.common.widget.stickerview.event.StickerIconEvent;
import com.ybw.yibai.common.widget.stickerview.event.StickerViewEvent;
import com.ybw.yibai.common.widget.stickerview.event.StickerViewSelectedListener;
import com.ybw.yibai.common.widget.stickerview.event.ZoomIconEvent;
import com.ybw.yibai.module.filter.FilterActivity;
import com.ybw.yibai.module.filter.FilterFragment;
import com.ybw.yibai.module.imagecontrast.ImageContrastActivity;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.market.MarketActivity;
import com.ybw.yibai.module.preselection.ConsumerPreselectionFragment;
import com.ybw.yibai.module.product.ProductFragment;
import com.ybw.yibai.module.recommendproduct.RecommendProductFragment;
import com.ybw.yibai.module.scene.SceneActivity;
import com.ybw.yibai.module.scene.SceneContract;
import com.ybw.yibai.module.sceneedit.SceneEditContract.SceneEditPresenter;
import com.ybw.yibai.module.sceneedit.SceneEditContract.SceneEditView;
import com.ybw.yibai.module.startdesign.StartDesignActivity;
import com.ybw.yibai.view.activity.FormulaCreatorActivity;
import com.ybw.yibai.view.activity.SelectDeliveryCityActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import kotlin.Pair;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;
import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;
import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Folder.SIMULATION_IMAGE_PREFIX;
import static com.ybw.yibai.common.constants.Preferences.CATEGORY_CODE;
import static com.ybw.yibai.common.constants.Preferences.FILE_PATH;
import static com.ybw.yibai.common.constants.Preferences.OPEN_WATERMARK;
import static com.ybw.yibai.common.constants.Preferences.PLANT;
import static com.ybw.yibai.common.constants.Preferences.PLANT_INFO;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.POT;
import static com.ybw.yibai.common.constants.Preferences.POT_INFO;
import static com.ybw.yibai.common.constants.Preferences.POT_TYPE_ID;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ID;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_LIST;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_TYPE;
import static com.ybw.yibai.common.constants.Preferences.SCENE_INFO;
import static com.ybw.yibai.common.constants.Preferences.SIMULATION_DATA;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.VIP_LEVEL;
import static com.ybw.yibai.common.constants.Preferences.WATERMARK_PIC;
import static com.ybw.yibai.common.utils.EncryptionUtil.sha1;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;
import static com.ybw.yibai.common.utils.FilterUtil.getFilterParam;
import static com.ybw.yibai.common.utils.OtherUtil.splitList;
import static com.ybw.yibai.common.utils.ViewPagerIndicatorUtil.initDots;
import static com.ybw.yibai.common.utils.ViewPagerIndicatorUtil.onPageSelectedDots;
import static com.ybw.yibai.common.widget.stickerview.BitmapStickerIcon.LEFT_TOP;
import static com.ybw.yibai.common.widget.stickerview.BitmapStickerIcon.RIGHT_TOP;

/**
 * 场景编辑
 *
 * @author sjl
 * @date 2019/9/23
 */
public class SceneEditFragment extends BaseFragment implements SceneEditView,
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        ViewPager.OnPageChangeListener,
        PlantSelectAdapter.OnItemLongClickListener,
        PotSelectAdapter.OnItemLongClickListener {

    public static final String TAG = "SceneEditFragment";

    /**
     * 复制的一个贴纸View在原来贴纸View位置的偏移量
     */
    private final int OFFSET = 200;

    /**
     * 当前Fragment页面在ViewPager中的位置
     */
    private int position;

    /**
     * ViewPager当前滑动的位置
     */
    private int currentPosition;

    /**
     * 用户当前选中的植物在换搭配推荐植物列表中的位置
     */
    private int plantPosition;

    /**
     * 用户当前选中的盆器在换搭配推荐盆器列表中的位置
     */
    private int potPosition;

    /**
     * 场景id(一般为创建该场景的时间戳)
     */
    private long sceneId;

    /**
     * 场景背景图片文件的绝对路径
     */
    private String filePath;

    /**
     * 手机中的模拟图片名称前面的前缀
     * (包含UID是为了区分不同用户的模拟图片)
     */
    private String prefix;

    /**
     * 当前选择的是盆器/植物
     */
    private String productType;

    /**
     * 用户最后拖曳的ViewPager
     * true  植物ViewPager
     * false 盆器ViewPager
     */
    private boolean lastDraggingViewPager;

    /**
     * 标记是否是第一次根据场景id查找保存用户的"模拟搭配图片"数据
     * (用于在用户打开本Fragment时判断本场景有没有"模拟搭配图片"数据
     * 如果没有就打开“弧形菜单”,默认为true)
     */
    private boolean firstTime = true;

    /*----------*/

    /**
     * 主产品的款名Id+附加产品的款名Id的组合+用户的uid
     */
    private String finallySkuId;

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
     * 主产品组合模式: 1单图模式,2搭配上部,3搭配下部
     */
    private int productCombinationType;

    /**
     * 植物的偏移量
     */
    private double productOffsetRatio;

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
     * 花盆的偏移量
     */
    private double augmentedProductOffsetRatio;

    /**
     * 附加产品组合模式: 1单图模式,2搭配上部,3搭配下部
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

    /*----------*/

    /**
     * 用户点击"选择分类"获取到的盆器类别ID
     */
    private int potTypeId;

    /*----------*/

    /**
     * 当前用户正在操作的贴纸距离屏幕左上角X的距离
     */
    private float currentX;

    /**
     * 当前用户正在操作的贴纸距离屏幕左上角Y的距离
     */
    private float currentY;

    /**
     * 当前用户正在操作的贴纸的宽度
     */
    private float currentStickerWidth;

    /**
     * 当前用户正在操作的贴纸的高度
     */
    private float currentStickerHeight;

    /**
     * 当前用户正在操作的贴纸数据
     */
    private SimulationData mSimulationData;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 界面跟布局
     */
    private View mRootView;

    /**
     * 场景背景图片
     */
    private ImageView mSceneBackgroundImageView;

    /**
     * VIP版/旗舰版客户自定义水印Logo
     */
    private ImageView mCustomerLogoImageView;

    /**
     * 体验版本水印Logo
     */
    private ImageView mExperienceVersionLogoImageView;

    /**
     * 贴纸View
     */
    private StickerView mStickerView;

    /*----------*/

    /**
     * 显示"已摆放产品"数据
     */
    private RelativeLayout mAlreadyPlacedLayout;

    /**
     * 报价详情
     */
    private TextView mQuotationDetailsTextView;

    /**
     * 显示"已摆放产品"的数据
     */
    private RecyclerView mRecyclerView;

    /**
     * 将产品及设计图导入报价清单
     */
    private TextView mAddQuotationTextView;

    /**
     *
     */
    private BottomSheetBehavior mAlreadyPlacedBottomSheetBehavior;

    /*----------*/

    /**
     * 用户点击"背景图"出现的编辑View
     */
    private LinearLayout mBackgroundEditLayout;

    /**
     * 拍照更换
     */
    private TextView mTakePhotoReplacementTextView;

    /**
     * 相册更换
     */
    private TextView mAlbumReplacementTextView;

    /**
     * 背景模版
     */
    private TextView mBackgroundTemplateTextView;

    /**
     * 调整背景
     */
    private TextView mAdjustBackgroundTextViewTextView;

    /*----------*/

    /**
     * 用户选中一个贴纸View出现编辑该贴纸View对应的盆栽信息
     */
    private LinearLayout mBonsaiEditLayout;

    /**
     * 查看产品代码的图标
     */
    private ImageButton mProductCodeImageButton;

    /**
     * 改款
     */
    private TextView mChangeStyleTextView;

    /**
     * 盆栽信息
     */
    private TextView mBonsaiInfoTextView;

    /**
     * 加入相册
     */
    private TextView mJoinPhotoAlbum;

    /**
     * 加入进货
     */
    private TextView mJoinPurchaseTextView;

    /**
     * 光亮调整
     */
    private TextView mBrightnessAdjustmentTextView;

    /**
     * 智能擦除
     */
    private TextView mIntelligentEraseTextView;

    /**
     * 还原设置
     */
    private TextView mRestoreSettingsTextView;

    /*----------*/

    /**
     * 推荐植物/盆器展开是显示的布局
     */
    private RelativeLayout mPlantPotLayout;

    /**
     * 筛选推荐植物/盆器
     */
    private TextView mFilterTextView;

    /**
     * 显示产品类别
     */
    private TextView mProductTypeTextView;

    /**
     * 显示产品类别
     */
    private ViewPager mProductTypeViewPager;

    /**
     * 指示点父布局
     */
    private LinearLayout mProductTypeDotLinearLayout;

    /**
     *
     */
    private BottomSheetBehavior mProductTypeBottomSheetBehavior;

    /*----------*/

    /**
     * 搭配图片布局的容器
     */
    private RelativeLayout mCollocationLayout;

    /*----------*/

    /**
     * 推荐花盆分类
     */
    private ListView mListView;

//    /**
//     * 选择花盆分类
//     */
//    private TextView mPotClassSelectTextView;

    /**
     * 保存图片
     */
    private TextView mSavePhoto;
    /**
     * 多图对比
     */
    private TextView mMultipleImageContrastTextView;

    /*----------*/

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
     * 上下文对象
     */
    private Context mContext;

    /**
     * Activity对象
     */
    private Activity mActivity;

    /**
     * 用户场景信息
     */
    private SceneInfo mSceneInfo;

    /**
     * 用户保存的"模拟搭配图片"数据
     */
    private List<SimulationData> mSimulationDataList;

    /**
     * 已摆放"模拟搭配图片"数据
     * 与 mSimulationDataList 的区别在于剔除了"finallySkuId"相同的数据
     */
    private List<SimulationData> mAlreadyPlacedList;

    /**
     * 推荐盆器分类列表
     */
    private List<PotBean> mRecommendPotType;

    /**
     * 换搭配推荐植物列表
     */
    private List<ListBean> mRecommendPlantList;

    /**
     * 换搭配推荐盆器列表
     */
    private List<ListBean> mRecommendPotList;

    /**
     * 场景编辑界面显示以摆放列表的适配器
     */
    private AlreadyPlacedAdapter mAlreadyPlacedAdapter;

    /**
     * 推荐盆器分类适配器
     */
    private RecommendPotTypeAdapter mRecommendPotTypeAdapter;

    /**
     * 规格id集合
     */
    private List<String> specidList = new ArrayList<>();

    /**
     * 属性id集合
     */
    private List<String> attridList = new ArrayList<>();


    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 动态设置的植物筛选参数
     */
    private Map<String, String> plantParamMap;

    /**
     * 动态设置的盆器筛选参数
     */
    private Map<String, String> potParamMap;

    /**
     * 显示植物列表的ViewPager的集合
     */
    private List<HorizontalViewPager> mPlantViewPagerList;

    /**
     * 显示盆器列表的ViewPager的集合
     */
    private List<HorizontalViewPager> mPotViewPagerList;

    /**
     * 植物选择适配器的集合
     */
    private List<PlantSelectAdapter> mPlantSelectAdapterList;

    /**
     * 盆器选择适配器的集合
     */
    private List<PotSelectAdapter> mPotSelectAdapterList;

    /**
     * 显示植物列表的ViewPager其侦听器的集合
     */
    private List<HorizontalViewPager.OnPageChangeListener> mOnPlantPageChangeListenerList;

    /**
     * 显示盆器列表的ViewPager其侦听器的集合
     */
    private List<HorizontalViewPager.OnPageChangeListener> mOnPotPageChangeListenerList;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     *
     */
    private GPUImage mGpuImage;

    /**
     *
     */
    private GPUImageFilter mGpuImageBrightnessFilter;

    /**
     *
     */
    private GPUImageFilter mGpuImageContrastFilter;

    /**
     *
     */
    private FilterFactoryUtil mBrightnessFilter;
    private View btnHideTools;
    /**
     * 查看货源
     */
    private TextView mSkuMarketTextView;
    /**
     *
     */
    private FilterFactoryUtil mContrastFilter;
    private OPlantAdapter mPlantAdapter;
    private OBowlAdapter mPotAdapter;
    private View mBottomCatTab, btnReset, mSceneContainerTool;
    private boolean isChangeFlag = false;

    /**
     * 要申请的权限(访问手机相机权限)
     */
    private String[] permissions = {
            Manifest.permission.CAMERA
    };
    private String mTitle;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private SceneEditPresenter mSceneEditPresenter;
    private EasyRecyclerView mPlantRecyclerView, mPotRecyclerView,
            mParamTabRecyclerView, mParamSonRecyclerView,
            mSceneRecyclerView;
    private TextView mBtnChangeLocation, btnCnAdd;
    private static ListBean lastBean;
    private BTCAdapter mParamAdapter;
    private BTNAdapter mParamSonAdapter;
    private SOAdapter mSceneAdapter;
    private BTCBean sceneParamDataBean;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        mActivity = getActivity();
        return R.layout.fragment_scene_edit;
    }

    @Override
    protected void findViews(View view) {
        mRootView = view.findViewById(R.id.rootLayout);
        mSceneBackgroundImageView = view.findViewById(R.id.sceneBackgroundImageView);
        mCustomerLogoImageView = view.findViewById(R.id.customerLogoImageView);
        mExperienceVersionLogoImageView = view.findViewById(R.id.experienceVersionLogoImageView);
        mStickerView = view.findViewById(R.id.stickerView);

        mAlreadyPlacedLayout = view.findViewById(R.id.already_placed_layout);
        mQuotationDetailsTextView = view.findViewById(R.id.quotationDetailsTextView);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mAddQuotationTextView = view.findViewById(R.id.addQuotationTextView);

        mBackgroundEditLayout = view.findViewById(R.id.backgroundEditLayout);
        mTakePhotoReplacementTextView = view.findViewById(R.id.takePhotoReplacementTextView);
        mAlbumReplacementTextView = view.findViewById(R.id.albumReplacementTextView);
        mBackgroundTemplateTextView = view.findViewById(R.id.backgroundTemplateTextView);
        mAdjustBackgroundTextViewTextView = view.findViewById(R.id.adjustBackgroundTextViewTextView);

        mBonsaiEditLayout = view.findViewById(R.id.bonsaiEditLayout);
        mProductCodeImageButton = view.findViewById(R.id.productCodeImageButton);
        mChangeStyleTextView = view.findViewById(R.id.changeStyleTextView);
        mBonsaiInfoTextView = view.findViewById(R.id.bonsaiInfoTextView);
        mJoinPhotoAlbum = view.findViewById(R.id.joinPhotoAlbum);
        mJoinPurchaseTextView = view.findViewById(R.id.joinPurchaseTextView);
        mBrightnessAdjustmentTextView = view.findViewById(R.id.brightnessAdjustmentTextView);
        mIntelligentEraseTextView = view.findViewById(R.id.intelligentEraseTextView);
        mRestoreSettingsTextView = view.findViewById(R.id.restoreSettingsTextView);
        mSkuMarketTextView = view.findViewById(R.id.skuMarketTextView);

        mPlantPotLayout = view.findViewById(R.id.recommended_plant_pot_layout);
        mFilterTextView = view.findViewById(R.id.filterTextView);
        mProductTypeTextView = view.findViewById(R.id.productTypeTextView);
        mProductTypeViewPager = view.findViewById(R.id.productTypeViewPager);
        mProductTypeDotLinearLayout = view.findViewById(R.id.productTypeDotLinearLayout);

        mCollocationLayout = view.findViewById(R.id.collocationLayout);

        mListView = view.findViewById(R.id.listView);
//        mPotClassSelectTextView = view.findViewById(R.id.potClassSelectTextView);
        mSavePhoto = view.findViewById(R.id.savePhoto);
        mMultipleImageContrastTextView = view.findViewById(R.id.multipleImageContrastTextView);
        mPlantRecyclerView = view.findViewById(R.id.plant_recyclerView);
        mPotRecyclerView = view.findViewById(R.id.pot_recyclerView);
        mParamTabRecyclerView = view.findViewById(R.id.paramTabRecyclerView);
        mParamSonRecyclerView = view.findViewById(R.id.paramSonRecyclerView);
        mBottomCatTab = view.findViewById(R.id.bottomCatTab);
        btnHideTools = view.findViewById(R.id.btnHideTools);
        mBtnChangeLocation = view.findViewById(R.id.btnChangeLocation);
        btnReset = view.findViewById(R.id.text1);
        btnCnAdd = view.findViewById(R.id.btnCreateDesign);
        mSceneRecyclerView = view.findViewById(R.id.scene_recycler_view);
        mSceneContainerTool = view.findViewById(R.id.containerSceneAction);
    }

    @Override
    protected void initView() {
        mWaitDialog = new WaitDialog(mContext);
        mGpuImage = new GPUImage(mContext);

        // 左上角"删除Icon"
        Drawable deleteDrawable = ContextCompat.getDrawable(mContext, R.mipmap.simulate_delete);
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(deleteDrawable, LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        // 右上角"拷贝Icon"
        Drawable copyDrawable = ContextCompat.getDrawable(mContext, R.mipmap.simulate_copy);
        BitmapStickerIcon copyIcon = new BitmapStickerIcon(copyDrawable, RIGHT_TOP);
        copyIcon.setIconEvent(new CopyIconEvent());

        // 右下角"缩放Icon"
        Drawable zoomDrawable = ContextCompat.getDrawable(mContext, R.mipmap.simulate_zoom);
        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(zoomDrawable, BitmapStickerIcon.RIGHT_BOTTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        // 左下角"购物Icon"
        Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.simulate_shopping);
        BitmapStickerIcon shoppingIcon = new BitmapStickerIcon(drawable, BitmapStickerIcon.LEFT_BOTTOM);
        shoppingIcon.setIconEvent(new ShopIconEvent());

        mStickerView.setIcons(Arrays.asList(deleteIcon, copyIcon, zoomIcon, shoppingIcon));
        mStickerView.setStickerViewEvent(mStickerViewEvent);
        mStickerView.setStickerViewSelectedListener(mStickerViewSelectedListener);

        mAlreadyPlacedBottomSheetBehavior = BottomSheetBehavior.from(mAlreadyPlacedLayout);
        mAlreadyPlacedBottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
        mAlreadyPlacedBottomSheetBehavior.setState(STATE_HIDDEN);

        mProductTypeBottomSheetBehavior = BottomSheetBehavior.from(mPlantPotLayout);
        mProductTypeBottomSheetBehavior.setState(STATE_HIDDEN);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, VERTICAL);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);

        // 显示第一个引导层
        String label = getClass().getSimpleName() + "One";
        GuideUtil.showGuideView(this, R.layout.guide_scene_edit_one_layout, label);
    }

    /**
     * 组合模式 1单图模式,2搭配上部,3搭配下部
     */
    String mComType;

    @Override
    protected void initData() {
        mSimulationDataList = new ArrayList<>();
        mAlreadyPlacedList = new ArrayList<>();
        mRecommendPotType = new ArrayList<>();
        mRecommendPlantList = new ArrayList<>();
        mRecommendPotList = new ArrayList<>();

        mPlantViewPagerList = new ArrayList<>();
        mPotViewPagerList = new ArrayList<>();
        mPlantSelectAdapterList = new ArrayList<>();
        mPotSelectAdapterList = new ArrayList<>();
        mOnPlantPageChangeListenerList = new ArrayList<>();
        mOnPotPageChangeListenerList = new ArrayList<>();
        mSceneEditPresenter = new SceneEditPresenterImpl(this);

        SharedPreferences preferences = mContext.getSharedPreferences(USER_INFO, MODE_PRIVATE);
        int vipLevel = preferences.getInt(VIP_LEVEL, 0);
        boolean openWatermark = preferences.getBoolean(OPEN_WATERMARK, true);
        String watermarkPic = preferences.getString(WATERMARK_PIC, null);
        if (1 == vipLevel) {
            mExperienceVersionLogoImageView.setVisibility(View.VISIBLE);
        } else if (openWatermark && !TextUtils.isEmpty(watermarkPic)) {
            ImageUtil.displayImage(mContext, mCustomerLogoImageView, watermarkPic);
        }
        prefix = SIMULATION_IMAGE_PREFIX + sha1(String.valueOf(YiBaiApplication.getUid()));

        Bundle bundle = getArguments();
        if (null != bundle) {
            mComType = bundle.getString("com_type");
            if (mComType == null || mComType.isEmpty()) {
                mComType = SPUtil.INSTANCE.getValue(getContext(), "com_type", String.class);
            }
            position = bundle.getInt(POSITION);
            mSceneInfo = (SceneInfo) bundle.getSerializable(SCENE_INFO);
        }
        if (null != mSceneInfo) {
            sceneId = mSceneInfo.getSceneId();
            filePath = mSceneInfo.getSceneBackground();
        }
        if (judeFileExists(filePath)) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            mGpuImage.setImage(bitmap);
        }

        SystemParameter systemParameter = YiBaiApplication.getSystemParameter();
        if (null != systemParameter) {
            SystemParameter.DataBean data = systemParameter.getData();
            if (null != data) {
                mRecommendPotType = data.getPot();
            }
        }

        plantParamMap = getFilterParam(PLANT);
        potParamMap = getFilterParam(POT);

        // 创建滤镜对象
        mGpuImageBrightnessFilter = new GPUImageBrightnessFilter();
        mGpuImageContrastFilter = new GPUImageContrastFilter();
        // 根据滤镜对象获取调节滤镜值的类
        mBrightnessFilter = new FilterFactoryUtil(mGpuImageBrightnessFilter);
        mContrastFilter = new FilterFactoryUtil(mGpuImageContrastFilter);

        mAlreadyPlacedAdapter = new AlreadyPlacedAdapter(mContext, mAlreadyPlacedList);
        mRecyclerView.setAdapter(mAlreadyPlacedAdapter);

        mRecommendPotTypeAdapter = new RecommendPotTypeAdapter(mContext, mRecommendPotType);
        mListView.setAdapter(mRecommendPotTypeAdapter);

        mPlantAdapter = new OPlantAdapter(getContext());
        mPotAdapter = new OBowlAdapter(getContext());
        mParamSonAdapter = new BTNAdapter(getContext());
        mParamAdapter = new BTCAdapter(getContext());
        mSceneAdapter = new SOAdapter(getContext());

        mParamTabRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mParamTabRecyclerView.setAdapter(mParamAdapter);
        mPlantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mParamSonRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mParamSonRecyclerView.setAdapter(mParamSonAdapter);


        mPlantRecyclerView.setAdapter(mPlantAdapter);
        mPotRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPotRecyclerView.setAdapter(mPotAdapter);

        mSceneRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSceneRecyclerView.setAdapter(mSceneAdapter);

        mBtnChangeLocation.setText(getArguments().getString("location"));

        // 屏幕左边植物被点击
        mPlantAdapter.setOnSelectListener(data -> {
            int pos = mPlantAdapter.getAllData().indexOf(data);
            if (mRecommendPotList.size() > pos) {
                getPlantInfo(pos);
                if (null != mPlantViewPagerList && mPlantViewPagerList.size() > 0) {
                    for (HorizontalViewPager viewPager : mPlantViewPagerList) {
                        viewPager.setCurrentItem(pos);
                    }
                }
                mSceneEditPresenter.getNewRecommed(POT, productSkuId, -5, mSelectType, specidList, attridList, plantParamMap);
//                mSceneEditPresenter.getRecommend(POT, productSkuId, augmentedProductSkuId,
//                        mSelectType,
//                        potTypeId, plantParamMap);
                //mProductTypeBottomSheetBehavior.setState(STATE_COLLAPSED);
            }
        });
        // 屏幕右边盆子垂直列表被点击
        mPotAdapter.setOnSelectListener(data -> {
            lastBean = data;
            if (mPotAdapter.getCount() > 0) {
                int pos = mPotAdapter.getAllData().indexOf(data);
                getPotInfo(pos);
                if (null != mPotViewPagerList && mPotViewPagerList.size() > 0) {
                    for (HorizontalViewPager viewPager : mPotViewPagerList) {
                        viewPager.setCurrentItem(pos);
                    }
                }
            }
        });
        // 筛选栏标题被点击
        mParamAdapter.setOnSelectListener(data -> {
            mParamSonAdapter.removeAll();
            mParamSonAdapter.setField(data.getField());
            mParamSonAdapter.setMultipleSelect(data.getType() == "2");
            mParamSonAdapter.addAll(mParams.get(mParamAdapter.getAllData().indexOf(data)).getSecond());
        });
        // 筛选栏所选值被点击
        mParamSonAdapter.setOnSelectListener(data -> {
            attridList.clear();
            specidList.clear();
            for (int p = 0; p < mParamAdapter.getAllData().size(); p++) {
                BTCBean item = mParamAdapter.getItem(p);
                item.setMName("");
                if (item.getSon() != null) {
                    for (int i = 0; i < item.getSon().size(); i++) {
                        BTCBean.Child sonBean = item.getSon().get(i);
                        if (!sonBean.getSelect()) continue;
                        item.setMName(sonBean.getName());
                        if (item.getField().equals("attrid")) {
                            attridList.add(sonBean.getId());
                        } else {
                            specidList.add(sonBean.getId());
                        }
                        break;
                    }
                }
            }

            // 移动到选中项位置
            int lastPos = 0;
            for (int i = mParamSonAdapter.getCount() - 1; i > 0; i--) {
                BTCBean.Child child = mParamSonAdapter.getItem(i);
                if (child.getSelect()) {
                    lastPos = i;
                    break;
                }
            }

            mParamSonRecyclerView.scrollToPosition(lastPos);
            mParamAdapter.notifyDataSetChanged();
            mParamSonAdapter.notifyDataSetChanged();

            mSceneEditPresenter.getNewRecommed(POT, productSkuId, -5, mSelectType, specidList, attridList, plantParamMap);
//            mSceneEditPresenter.getNewRecommed(POT, productSkuId, augmentedProductSkuId, mSelectType, specidList, attridList, plantParamMap);
//            mSceneEditPresenter.getNewRecommend(data.getFirst(),
//                    productSkuId, augmentedProductSkuId,
//                    null, potTypeId, param);
        });
        // 场景编辑框被点击
        mSceneAdapter.setOnItemClickListener(position -> {
            SceneInfo _item = mSceneAdapter.getItem(position);
            Intent toIntent = new Intent(getContext(), FormulaCreatorActivity.class);
            startActivityForResult(toIntent, FormulaCreatorActivity.Companion.getREQUEST_CODE());
        });

        if (mComType != null && mComType.equals("1")) {
            mBonsaiInfoTextView.setVisibility(View.GONE);
        }
    }

    private String SpecId = "";

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mSceneEditPresenter.getSimulationData(sceneId);
        mSceneEditPresenter.setBackgroundImage(mSceneBackgroundImageView, filePath);
        mSceneEditPresenter.getParams();

        mRootView.setOnClickListener(this);
        mQuotationDetailsTextView.setOnClickListener(this);
        mAddQuotationTextView.setOnClickListener(this);
        mSkuMarketTextView.setOnClickListener(this);

        mTakePhotoReplacementTextView.setOnClickListener(this);
        mAlbumReplacementTextView.setOnClickListener(this);
        mBackgroundTemplateTextView.setOnClickListener(this);
        mAdjustBackgroundTextViewTextView.setOnClickListener(this);

        mProductCodeImageButton.setOnClickListener(this);
        mChangeStyleTextView.setOnClickListener(this);
        mBonsaiInfoTextView.setOnClickListener(this);
        mJoinPhotoAlbum.setOnClickListener(this);
        mJoinPurchaseTextView.setOnClickListener(this);
        mBrightnessAdjustmentTextView.setOnClickListener(this);
        mIntelligentEraseTextView.setOnClickListener(this);
        mRestoreSettingsTextView.setOnClickListener(this);
        btnHideTools.setOnClickListener(this);

        mFilterTextView.setOnClickListener(this);
        mProductTypeViewPager.addOnPageChangeListener(this);

        mListView.setOnItemClickListener(this);
        //mPotClassSelectTextView.setOnClickListener(this);
        mMultipleImageContrastTextView.setOnClickListener(this);
        mSavePhoto.setOnClickListener(this);
        mBtnChangeLocation.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnCnAdd.setOnClickListener(this);

//         获取设计详情
        String number = SPUtil.INSTANCE.getValue(getContext(), "number", String.class);
        if (number != null && !number.isEmpty()) {
            mSceneEditPresenter.getDesignDetail(number);
            mSceneEditPresenter.getSceneList();
        }
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.rootLayout) {
            mProductTypeBottomSheetBehavior.setState(STATE_HIDDEN);
        }

        /**
         * 发送数据到{@link MainActivity#ratioActivitySendData(ToFragment)}
         * 使其跳转到对应的Fragment
         */
        // 报价详情
        if (id == R.id.quotationDetailsTextView) {
            ToFragment toFragment = new ToFragment(3);
            EventBus.getDefault().postSticky(toFragment);
            mActivity.finish();
        }

        // 将产品及设计图导入报价清单
        if (id == R.id.addQuotationTextView) {
            String photoName = prefix + TimeUtil.getTimeStamp();
            String pathName = mStickerView.saveSticker(photoName);
            File file = new File(pathName);
            String sceneName = mSceneInfo.getSceneName();
            mSceneEditPresenter.fastImport(sceneName, file, mAlreadyPlacedList);
        }

        // 拍照更换
        if (id == R.id.takePhotoReplacementTextView) {
            mSceneEditPresenter.applyPermissions(permissions);
        }

        // 相册更换
        if (id == R.id.albumReplacementTextView) {
            mSceneEditPresenter.openPhotoAlbum();
        }

        // 背景模版
        if (id == R.id.backgroundTemplateTextView) {

        }

        // 调整背景
        if (id == R.id.adjustBackgroundTextViewTextView) {
            mSceneEditPresenter.displaySceneBackgroundEditPopupWindow(mRootView);
        }

        // 查看产品代码的图标
        if (id == R.id.productCodeImageButton) {
            mSceneEditPresenter.displayProductCodePopupWindow(mProductCodeImageButton, productPriceCode,
                    productTradePriceCode, augmentedPriceCode, augmentedTradePriceCode, productPrice, augmentedProductPrice);
        }

        // 更换搭配
        if (id == R.id.changeStyleTextView) {
            replaceCollocation(false);
        }

        // 盆栽信息
        if (id == R.id.bonsaiInfoTextView) {
            BaseSticker currentSticker = mStickerView.getCurrentSticker();
            if (null == currentSticker) {
                return;
            }
            int position = (int) currentSticker.getTag();
            SimulationData simulationData = mSimulationDataList.get(position);
            mSceneEditPresenter.displayPlantInfoPopupWindow(mRootView, simulationData);
        }

        // 加入相册
        if (id == R.id.joinPhotoAlbum) {
            BaseSticker currentSticker = mStickerView.getCurrentSticker();
            if (null == currentSticker) {
                return;
            }
            int position = (int) currentSticker.getTag();
            SimulationData simulationData = mSimulationDataList.get(position);
            mSceneEditPresenter.joinPhotoAlbum(simulationData);
        }

        // 加入进货
        if (id == R.id.joinPurchaseTextView) {
            BaseSticker currentSticker = mStickerView.getCurrentSticker();
            if (null == currentSticker) {
                return;
            }
            int position = (int) currentSticker.getTag();
            SimulationData simulationData = mSimulationDataList.get(position);
            mSceneEditPresenter.addQuotationData(simulationData);
        }

        //查看货源
        if (id == R.id.skuMarketTextView) {
            if (augmentedProductSkuId != 0 && productSkuId != 0) {
                skuMarketPopupWindow();
            } else {
                Intent intent = new Intent(getActivity(), MarketActivity.class);
                if (productSkuId != 0) {
                    intent.putExtra(PRODUCT_SKU_ID, productSkuId);
                } else {
                    intent.putExtra(PRODUCT_SKU_ID, augmentedProductSkuId);
                }
                startActivity(intent);
            }
        }

        // 光亮调整
        if (id == R.id.brightnessAdjustmentTextView) {

        }

        // 智能擦除
        if (id == R.id.intelligentEraseTextView) {

        }

        // 还原设置
        if (id == R.id.restoreSettingsTextView) {

        }

        // 筛选推荐植物/盆器
        if (id == R.id.filterTextView) {
            Intent intent = new Intent(mActivity, FilterActivity.class);
            intent.putExtra(POSITION, position);
            intent.putExtra(PRODUCT_TYPE, productType);
            startActivity(intent);
        }

        // 选择分类
//        if (id == R.id.potClassSelectTextView) {
//            if (View.GONE == mListView.getVisibility()) {
//                mListView.setVisibility(View.VISIBLE);
//            } else {
//                mListView.setVisibility(View.GONE);
//            }
//        }

        if (id == R.id.savePhoto) {
            String photoName = prefix + TimeUtil.getTimeStamp();
            String pathName = mStickerView.saveSticker(photoName);
            File file = new File(pathName);
            if (file.isFile() && file.exists()) {
                MessageUtil.showMessage(getResources().getString(R.string.save_successfully_you_can_click_on_my_design_below_to_view));
                /**
                 * 发送数据到{@link SceneActivity#saveAddScheme(SaveAddSchemeBean)}
                 */
                SaveAddSchemeBean saveAddSchemeBean = new SaveAddSchemeBean();
                saveAddSchemeBean.setPathName(pathName);
                saveAddSchemeBean.setAugmentedProductSkuId(augmentedProductSkuId);
                saveAddSchemeBean.setProductSkuId(productSkuId);
                EventBus.getDefault().post(saveAddSchemeBean);
                return;
            }
            MessageUtil.showMessage(getResources().getString(R.string.save_failed));
        }

        // 多图对比
        if (id == R.id.multipleImageContrastTextView) {
            ListBean plantBean = mRecommendPlantList.get(plantPosition);
            ListBean potBean = mRecommendPotList.get(potPosition);
            Intent intent = new Intent(mContext, ImageContrastActivity.class);
            intent.putExtra(FILE_PATH, filePath);
            intent.putExtra(PLANT_INFO, (Serializable) plantBean);
            intent.putExtra(POT_INFO, (Serializable) potBean);
            intent.putExtra(SIMULATION_DATA, mSimulationData);
            intent.putExtra(POT_TYPE_ID, potTypeId);
            if (lastDraggingViewPager) {
                intent.putExtra(PRODUCT_TYPE, PLANT);
            } else {
                intent.putExtra(PRODUCT_TYPE, POT);
            }
            startActivity(intent);
        }

        // 隐藏工具
        if (id == R.id.btnHideTools) {
            boolean hidden = false;
            if (btnHideTools.getTag() instanceof Boolean) {
                hidden = (boolean) btnHideTools.getTag();
            }
            if (hidden) {
                mBtnChangeLocation.setVisibility(View.INVISIBLE);
                mChangeStyleTextView.setVisibility(View.INVISIBLE);
                mPlantRecyclerView.setVisibility(View.INVISIBLE);
                mPotRecyclerView.setVisibility(View.INVISIBLE);
                mBottomCatTab.setVisibility(View.INVISIBLE);
                mMultipleImageContrastTextView.setVisibility(View.INVISIBLE);
                mSavePhoto.setVisibility(View.INVISIBLE);
                btnHideTools.setTag(false);
            } else {
                mBtnChangeLocation.setVisibility(View.INVISIBLE);
                mChangeStyleTextView.setVisibility(View.VISIBLE);
                mPlantRecyclerView.setVisibility(View.VISIBLE);
                mPotRecyclerView.setVisibility(View.VISIBLE);
                mBottomCatTab.setVisibility(View.VISIBLE);
                mSavePhoto.setVisibility(View.VISIBLE);
                mMultipleImageContrastTextView.setVisibility(View.VISIBLE);
                btnHideTools.setTag(true);
            }
        }

        // 切换地区
        if (id == R.id.btnChangeLocation) {
            Intent toIntent = new Intent(getContext(), SelectDeliveryCityActivity.class);
            startActivityForResult(toIntent, 1001);
        }

        // 重置筛选参数
        if (id == R.id.text1) {
            for (int i = 0; i < mParamAdapter.getAllData().size(); i++) {
                BTCBean item = mParamAdapter.getAllData().get(i);
                item.setSelect(false);
                for (int n = 0; n < item.getSon().size(); n++) {
                    BTCBean.Child child = item.getSon().get(n);
                    child.setSelect(false);
                }
            }
            SpecId = "";
            mParamAdapter.notifyDataSetChanged();
            mParamSonAdapter.notifyDataSetChanged();

            mSceneEditPresenter.getNewRecommedChangeStyle(productSkuId, augmentedProductSkuId);
//            mSceneEditPresenter.getRecommend(POT, productSkuId, augmentedProductSkuId,
//                    mSelectType,
//                    potTypeId, plantParamMap);
        }

        // 添加图片
        if (id == R.id.btnCreateDesign) {
            String title = btnCnAdd.getText().toString();
            if (title.equals("添加图片")) {
                mSceneEditPresenter.createDesign();
            } else if (title.equals("继续添加")) {
                increment();
                String type = mDesignSceneModel.getSchemelist().size() < 1 ? "1" : "2";
                String design_number = SPUtil.INSTANCE.getValue(getContext(), "number", String.class);
                String schemeId = null;
                if (mDesignSceneModel != null && mDesignSceneModel.getSchemelist().size() > 0) {
                    schemeId = mDesignSceneModel.getSchemelist().get(0).getScheme_id();
                }
                String schemeName = mTitle;

                Bitmap bm = mStickerView.createBitmap();
                File file = bitmapSaveToFile(bm);
                Map<String, RequestBody> body = new LinkedHashMap<>();
                String fileName = file.getName();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
                body.put("pic\"; filename=\"" + file.getName(), requestBody);

                String product_sku_id = null;
                if (mSimulationDataList.size() > 0) {
                    SimulationData sd = mSimulationDataList.get(0);
                    product_sku_id = String.valueOf(sd.getProductSkuId());
                    product_sku_id = String.format("%s,%s", product_sku_id, sd.getAugmentedProductSkuId());
                }
                mSceneEditPresenter.CreateSceneOrPic(design_number, type, schemeId,
                        schemeName, body, product_sku_id
                );
            }
        }
    }

    /**
     * 弹窗提示
     */
    private PopupWindow mPopupWindow = null;

    private void skuMarketPopupWindow() {
        if (null == mPopupWindow) {
            View view = getLayoutInflater().inflate(R.layout.popup_window_sku_market_layout, null);
            mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout productIdView = view.findViewById(R.id.productIdView);
            ImageView productIdImg = view.findViewById(R.id.productIdImg);
            TextView productIdName = view.findViewById(R.id.productIdName);
            TextView productIdSX = view.findViewById(R.id.productIdSX);
            if (productPic1 != null && !productPic1.isEmpty())
                ImageUtil.displayImage(getActivity(), productIdImg, productPic1);
            productIdName.setText(productName);
            productIdSX.setText(specTypeName);

            LinearLayout augmentedProductSkuIdView = view.findViewById(R.id.augmentedProductSkuIdView);
            ImageView augmentedProductSkuIdImg = view.findViewById(R.id.augmentedProductSkuIdImg);
            TextView augmentedProductSkuIdName = view.findViewById(R.id.augmentedProductSkuIdName);
            TextView augmentedProductSkuIdSX = view.findViewById(R.id.augmentedProductSkuIdSX);
            if (augmentedProductPic1 != null && !augmentedProductPic1.isEmpty())
                ImageUtil.displayImage(getActivity(), augmentedProductSkuIdImg, augmentedProductPic1);
            augmentedProductSkuIdName.setText(augmentedProductName);
            augmentedProductSkuIdSX.setText(specTypeName);

            augmentedProductSkuIdView.setOnClickListener(v -> {
                if (null != mPopupWindow && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    Intent intent = new Intent(getActivity(), MarketActivity.class);
                    intent.putExtra(PRODUCT_SKU_ID, augmentedProductSkuId);
                    startActivity(intent);
                }
            });
            productIdView.setOnClickListener(v -> {
                if (null != mPopupWindow && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    Intent intent = new Intent(getActivity(), MarketActivity.class);
                    intent.putExtra(PRODUCT_SKU_ID, productSkuId);
                    startActivity(intent);
                }
            });
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);

            // 设置一个动画效果
            mPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(getActivity(), 0.6f);
            // 添加PopupWindow窗口关闭事件\

            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(getActivity(), 1f));
            mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
        } else {
            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(getActivity(), 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(getActivity(), 1f));
            mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
        }
    }


    private File bitmapSaveToFile(Bitmap pictureBitmap) {
        // Assume block needs to be inside a Try/Catch block.
        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut;
        int counter = 0;
        File file = new File(path, "FitnessGirl" + counter + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
        try {
            fOut = new FileOutputStream(file);
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                    file.getAbsolutePath(), file.getName(), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 在显示植物的ViewPager Item 长按时回调
     *
     * @param position 被长按的Item位置
     */
    @Override
    public void onPlantViewPagerItemLongClick(int position) {
        mSceneEditPresenter.getNewRecommed(PLANT, productSkuId, augmentedProductSkuId, mSelectType, specidList, attridList, plantParamMap);
//        mSceneEditPresenter.getNewRecommed(PLANT, productSkuId, augmentedProductSkuId, mSelectType, specidList, attridList, plantParamMap);
//        mSceneEditPresenter.getRecommend(PLANT, productSkuId, augmentedProductSkuId,
//                mSelectType,
//                potTypeId, plantParamMap);
        mProductTypeBottomSheetBehavior.setState(STATE_EXPANDED);
    }

    /**
     * 在显示盆器的ViewPager Item 长按时回调
     *
     * @param position 被长按的Item位置
     */
    @Override
    public void onPotViewPagerItemLongClick(int position) {
        if (1 == productCombinationType || 1 == augmentedCombinationType) {
            // 单图模式

            mSceneEditPresenter.getCategorySimilarSKUList(augmentedProductSkuId);
        } else {
            // 上下搭配
            mSceneEditPresenter.getNewRecommed(POT, productSkuId, augmentedProductSkuId, mSelectType, specidList, attridList, potParamMap);
//
//            mSceneEditPresenter.getRecommend(POT, productSkuId, augmentedProductSkuId,
//                    mSelectType,
//                    potTypeId, potParamMap);
        }
        mProductTypeBottomSheetBehavior.setState(STATE_EXPANDED);
    }

    /**
     * ViewPager某一个页面被选中时回调
     *
     * @param position 当前滑动到的页面
     */
    @Override
    public void onPageSelected(int position) {
        onPageSelectedDots(mContext, mProductTypeDotLinearLayout, position);
    }

    /**
     * ViewPager这个方法在手指操作屏幕的时候发生变化
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * ViewPager在屏幕滚动过程中不断被调用
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 推荐盆器类型ListView Item点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        potTypeId = mRecommendPotType.get(position).getId();
        String name = mRecommendPotType.get(position).getName();
        mSceneEditPresenter.getNewRecommed(POT, productSkuId, augmentedProductSkuId, mSelectType, specidList, attridList, potParamMap);

//        mSceneEditPresenter.getRecommend(POT, productSkuId, augmentedProductSkuId,
//                mSelectType,
//                potTypeId, potParamMap);
//        mPotClassSelectTextView.setText(name);
        mListView.setVisibility(View.GONE);
    }

    ListBean getSelectPotItem() {
        ListBean foundIt = null;
        for (ListBean indice : mPotAdapter.getAllData()) {
            if (indice.isSelect()) {
                foundIt = indice;
            }
        }
        return foundIt;
    }

    ListBean getSelectPlantItem() {
        ListBean foundIt = null;
        for (ListBean indice : mPlantAdapter.getAllData()) {
            if (indice.isSelect()) {
                foundIt = indice;
            }
        }
        return foundIt;
    }

    /**
     * "贴纸"四个方向的Icon图标中的"删除Icon"的事件
     *
     * @author sjl
     */
    public class DeleteIconEvent implements StickerIconEvent {

        /**
         * 按下手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param event       触屏事件
         */
        @Override
        public void onActionDown(StickerView stickerView, MotionEvent event) {

        }

        /**
         * 移动手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param event       触屏事件
         */
        @Override
        public void onActionMove(StickerView stickerView, MotionEvent event) {

        }

        /**
         * 抬起手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param event       触屏事件
         */
        @Override
        public void onActionUp(StickerView stickerView, MotionEvent event) {
            BaseSticker currentSticker = mStickerView.getCurrentSticker();
            if (null == currentSticker) {
                return;
            }
            int position = (int) currentSticker.getTag();
            SimulationData simulationData = mSimulationDataList.get(position);
            mSimulationData = simulationData;
            mSceneEditPresenter.deleteSimulationData(simulationData);
            stickerView.removeCurrentSticker();
            mSceneEditPresenter.getSimulationData(sceneId);
        }
    }

    /**
     * 复制Icon事件
     */
    private class CopyIconEvent implements StickerIconEvent {

        /**
         * 按下手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param event       触屏事件
         */
        @Override
        public void onActionDown(StickerView stickerView, MotionEvent event) {

        }

        /**
         * 移动手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param event       触屏事件
         */
        @Override
        public void onActionMove(StickerView stickerView, MotionEvent event) {

        }

        /**
         * 抬起手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param event       触屏事件
         */
        @Override
        public void onActionUp(StickerView stickerView, MotionEvent event) {
            BaseSticker currentSticker = mStickerView.getCurrentSticker();
            if (null == currentSticker) {
                return;
            }
            int position = (int) currentSticker.getTag();
            SimulationData simulationData = mSimulationDataList.get(position);
            mSimulationData = simulationData;
            float x = simulationData.getX();
            float y = simulationData.getY();
            simulationData.setX(x + OFFSET);
            simulationData.setY(y);
            simulationData.setTimeStamp(TimeUtil.getNanoTime());
            mSceneEditPresenter.addSimulationData(simulationData);
            mSceneEditPresenter.getSimulationData(sceneId);
        }
    }

    /**
     * 购物Icon事件
     */
    private class ShopIconEvent implements StickerIconEvent {

        /**
         * 按下手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param event       触屏事件
         */
        @Override
        public void onActionDown(StickerView stickerView, MotionEvent event) {

        }

        /**
         * 移动手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param event       触屏事件
         */
        @Override
        public void onActionMove(StickerView stickerView, MotionEvent event) {

        }

        /**
         * 抬起手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param event       触屏事件
         */
        @Override
        public void onActionUp(StickerView stickerView, MotionEvent event) {
            BaseSticker currentSticker = mStickerView.getCurrentSticker();
            if (null == currentSticker) {
                return;
            }
            int position = (int) currentSticker.getTag();
            SimulationData simulationData = mSimulationDataList.get(position);
            mSimulationData = simulationData;
            mSceneEditPresenter.addQuotation(simulationData);
        }
    }

    /**
     * StickerView TouchEvent 侦听器
     */
    private StickerViewEvent mStickerViewEvent = new StickerViewEvent() {

        /**
         * 按下手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param bounds      StickerView范围点的坐标
         */
        @Override
        public void onActionDown(StickerView stickerView, float[] bounds) {

        }

        /**
         * 移动手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param bounds      StickerView范围点的坐标
         */
        @Override
        public void onActionMove(StickerView stickerView, float[] bounds) {

        }

        /**
         * 抬起手势
         *
         * @param stickerView 事件对象(贴纸View)
         * @param bounds      StickerView范围点的坐标
         */
        @Override
        public void onActionUp(StickerView stickerView, float[] bounds) {
            BaseSticker currentSticker = mStickerView.getCurrentSticker();
            if (null == currentSticker) {
                return;
            }
            int position = (int) currentSticker.getTag();
            SimulationData simulationData = mSimulationDataList.get(position);
            mSimulationData = simulationData;
            if (null == simulationData) {
                return;
            }
            double width = simulationData.getWidth();
            double height = simulationData.getHeight();
            // 因为对贴纸View只有平移和缩放操作,所以可以通过下面的简单方法计算,如果有旋转着该方法不成立
            float currentWidth = bounds[2] - bounds[0];
            float currentHeight = bounds[5] - bounds[1];
            double xScale = currentWidth / width;
            double yScale = currentHeight / height;

            currentX = bounds[0];
            currentY = bounds[1];
            currentStickerWidth = currentWidth;
            currentStickerHeight = currentHeight;

            simulationData.setX(bounds[0]);
            simulationData.setY(bounds[1]);
            simulationData.setxScale(xScale);
            simulationData.setyScale(yScale);
            mSceneEditPresenter.updateSimulationData(simulationData);
        }
    };

    /**
     * "贴纸View"是否被选中状态侦听器
     */
    private StickerViewSelectedListener mStickerViewSelectedListener = new StickerViewSelectedListener() {
        /**
         * 在有一个"贴纸View"被选中时回调
         *
         * @param sticker 被选中的"贴纸View"
         */
        @Override
        public void onSelectedStickerView(@NonNull BaseSticker sticker) {
            BaseSticker currentSticker = mStickerView.getCurrentSticker();
            if (null != currentSticker) {
                int position = (int) currentSticker.getTag();
                mSimulationData = mSimulationDataList.get(position);
                mComType = String.valueOf(mSimulationData.getAugmentedCombinationType());
                productPrice = mSimulationData.getProductPrice();
                augmentedProductPrice = mSimulationData.getAugmentedProductPrice();
                productPriceCode = mSimulationData.getProductPriceCode();
                productTradePriceCode = mSimulationData.getProductTradePriceCode();
                augmentedPriceCode = mSimulationData.getAugmentedPriceCode();
                augmentedTradePriceCode = mSimulationData.getAugmentedTradePriceCode();
            }
            if (View.GONE == mProductCodeImageButton.getVisibility()) {
                mProductCodeImageButton.setVisibility(View.VISIBLE);
            }
            if (View.GONE == mChangeStyleTextView.getVisibility()) {
                mChangeStyleTextView.setVisibility(View.VISIBLE);
            }
            if (View.GONE == mBonsaiEditLayout.getVisibility()) {
                mBonsaiEditLayout.setVisibility(View.VISIBLE);
            }
            if (View.VISIBLE == mBackgroundEditLayout.getVisibility()) {
                mBackgroundEditLayout.setVisibility(View.GONE);
            }
            // 显示第二个引导层
            String label = getClass().getSimpleName() + "Two";
            GuideUtil.showGuideView(SceneEditFragment.this, R.layout.guide_scene_edit_two_layout, label);
            /**
             * 发送数据到{@link SceneActivity#stickerViewSelected(StickerViewSelected)}
             */
            EventBus.getDefault()
                    .postSticky(new StickerViewSelected(true));
            if (mComType != null && mComType.equals("1")) {
                mSceneContainerTool.setVisibility(View.GONE);
            }
        }

        /**
         * 在没有"贴纸View"被选中时回调
         */
        @Override
        public void onUnselectedStickerView() {
            if (View.VISIBLE == mProductCodeImageButton.getVisibility()) {
                mProductCodeImageButton.setVisibility(View.GONE);
            }
            if (View.VISIBLE == mChangeStyleTextView.getVisibility()) {
                mChangeStyleTextView.setVisibility(View.GONE);
            }
            if (View.VISIBLE == mBonsaiEditLayout.getVisibility()) {
                mBonsaiEditLayout.setVisibility(View.GONE);
            }
            /**
             * 发送数据到{@link SceneActivity#stickerViewSelected(StickerViewSelected)}
             */
            EventBus.getDefault()
                    .postSticky(new StickerViewSelected(false));
            if (mComType != null && mComType.equals("1")) {
                mSceneContainerTool.setVisibility(View.GONE);
            }
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
            /**
             * 发送数据到{@link SceneActivity#onBottomSheetBehaviorStateChanged(BottomSheetBehaviorState)}
             */
            EventBus.getDefault().postSticky(new BottomSheetBehaviorState(newState));
        }
    };

    /**
     * 根据场景id查找用户保存的"模拟搭配图片"数据成功时回调
     *
     * @param simulationDataList 用户保存的"模拟搭配图片"数据
     */
    @Override
    public void onGetSimulationDataSuccess(List<SimulationData> simulationDataList) {
        // 移除上一次全部Sticker
        mStickerView.removeAllStickers();
        mSimulationDataList.clear();
        mAlreadyPlacedList.clear();
        if (null != simulationDataList && simulationDataList.size() > 0) {
            mSimulationDataList.addAll(simulationDataList);
            mAlreadyPlacedList.addAll(simulationDataList);
            mSceneEditPresenter.addSticker(mStickerView, simulationDataList);
            if (firstTime) {
                firstTime = false;
                /**
                 * 发送数据到{@link SceneActivity#onGetSimulationDataSuccess(ExistSimulationData)}
                 * 通知其是否展开“弧形菜单”
                 */
                EventBus.getDefault().postSticky(new ExistSimulationData(true));
            }
        } else {
            // 恢复初始页面
            mProductCodeImageButton.setVisibility(View.GONE);
            mChangeStyleTextView.setVisibility(View.GONE);
            mBonsaiEditLayout.setVisibility(View.GONE);
            if (firstTime) {
                firstTime = false;
                /**
                 * 发送数据到{@link SceneActivity#onGetSimulationDataSuccess(ExistSimulationData)}
                 * 通知其是否展开“弧形菜单”
                 */
                EventBus.getDefault().postSticky(new ExistSimulationData(false));
            }
            /**
             * 发送数据到{@link SceneActivity#stickerViewSelected(StickerViewSelected)}
             */
            EventBus.getDefault().postSticky(new StickerViewSelected(false));
            if (mComType != null && mComType.equals("1")) {
                mSceneContainerTool.setVisibility(View.GONE);
            }
        }
        if (null == mAlreadyPlacedList || mAlreadyPlacedList.size() == 0) {
            mAlreadyPlacedAdapter.notifyDataSetChanged();
            return;
        }
        // 删除集合中"finallySkuId"属性相同的元素
        for (int i = 0; i < mAlreadyPlacedList.size() - 1; i++) {
            for (int j = mAlreadyPlacedList.size() - 1; j > i; j--) {
                String finallySkuIdj = mAlreadyPlacedList.get(j).getFinallySkuId();
                String finallySkuIdi = mAlreadyPlacedList.get(i).getFinallySkuId();
                if (!finallySkuIdj.equals(finallySkuIdi)) {
                    continue;
                }
                // 存在相同的"finallySkuId"属性数量+1
                SimulationData simulationData = mAlreadyPlacedList.get(i);
                int number = simulationData.getNumber();
                simulationData.setNumber(number + 1);
                mAlreadyPlacedList.remove(j);
            }
        }
        mAlreadyPlacedAdapter.notifyDataSetChanged();
        if (addSpec) {
            addSpec = false;
            replaceCollocation(true);
        }
    }

    /**
     * 申请权限的结果
     *
     * @param b true 已经获取全部权限,false 没有获取全部权限
     */
    @Override
    public void applyPermissionsResults(boolean b) {
        if (b) {
            // 打开相机
            mSceneEditPresenter.openCamera();
        }
    }

    /**
     * 请求权限的结果的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获得申请的全部权限
        if (!PermissionsUtil.allPermissionsAreGranted(grantResults)) {
            PermissionsUtil.showNoCameraPermissions(mActivity);
        } else {
            // 打开相机
            mSceneEditPresenter.openCamera();
        }
    }

    /**
     * 获得Activity返回的数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == 1) {
            String city_code = data.getStringExtra("city_code");
            mSceneEditPresenter.setUserPosition(city_code);
        }
        // 删除设计成功
        if (requestCode == 1002 && resultCode == 1) {
            mSceneAdapter.clear();
            btnCnAdd.setText("添加图片");
            mSceneRecyclerView.setVisibility(View.GONE);
            return;
        }
//        mSceneEditPresenter.getDesignDetail(SPUtil.INSTANCE.getValue(getContext(), "number", String.class));
//        mSceneEditPresenter.getSceneList();
        if (position != currentPosition) {
            return;
        }
        mSceneEditPresenter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 返回从相机或相册返回的图像
     *
     * @param file 图像文件
     */
    @Override
    public void returnsTheImageReturnedFromTheCameraOrAlbum(File file) {
        mSceneInfo.setSceneBackground(file.getAbsolutePath());
        mSceneEditPresenter.updateSceneInfo(mSceneInfo);
        mSceneEditPresenter.setBackgroundImage(mSceneBackgroundImageView, file.getAbsolutePath());
        mSceneEditPresenter.updateSceneBg(file.getAbsolutePath(), mSceneInfo.getSceneName(), mSceneInfo.getScheme_id());
    }

    /**
     * 更新成功
     */
    @Override
    public void updateSceneBgSuccess() {
        /**
         * 发送数据到{@link SceneActivity#onSceneInfoChange(SceneInfoChange)}
         */
        EventBus.getDefault().post(new SceneInfoChange());
    }

    /**
     * 将场景中产品一键创建并导入服务器中成功时回调
     *
     * @param fastImport 将场景中产品一键创建并导入服务器中成功时服务器端返回的数据
     */
    @Override
    public void onFastImportSuccess(FastImport fastImport) {
        MessageUtil.showMessage(fastImport.getMsg());
    }

    /**
     * 根据大中小随机获取组合成功时回调
     *
     * @param specSuk 随机获取组合
     */
    @Override
    public void onGetSpecSukSuccess(SpecSuk specSuk) {
        if (CODE_SUCCEED != specSuk.getCode()) {
            MessageUtil.showMessage(specSuk.getMsg());
            return;
        }
        DataBean data = specSuk.getData();
        if (null == data) {
            return;
        }
        mSceneEditPresenter.addSimulationData(data);
    }

    /**
     * 在获取相似类型sku列表成功时回调
     *
     * @param categorySimilarSKU 相似类型sku列表
     */
    @Override
    public void onGetCategorySimilarSKUListSuccess(CategorySimilarSKU categorySimilarSKU) {
        if (CODE_SUCCEED != categorySimilarSKU.getCode()) {
            MessageUtil.showMessage(categorySimilarSKU.getMsg());
            return;
        }
        mProductTypeViewPager.removeAllViews();
        mProductTypeDotLinearLayout.removeAllViews();
        CategorySimilarSKU.DataBean data = categorySimilarSKU.getData();
        if (null == data) {
            return;
        }
        int count = data.getCount();
        productType = data.getCateCode();
        if (0 == count) {
            // 清空,避免使用上一次请求的旧数据
            mRecommendPotList.clear();
            MessageUtil.showMessage(getResources().getString(R.string.no_suitable_style_for_recommended));
            return;
        }

        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        List<List<ListBean>> lists = splitList(list, 10);
        if (null == lists || lists.size() == 0) {
            return;
        }

        mRecommendPotList.clear();
        mRecommendPotList.addAll(list);
        mProductTypeTextView.setText(getResources().getString(R.string.similar_products));
        mFilterTextView.setVisibility(View.INVISIBLE);
        if (null != mPotSelectAdapterList && mPotSelectAdapterList.size() > 0) {
            for (PotSelectAdapter potSelectAdapter : mPotSelectAdapterList) {
                potSelectAdapter.notifyDataSetChanged();
            }
        }
        // 默认选中第一个
        if (null != mPotViewPagerList && mPotViewPagerList.size() > 0) {
            for (HorizontalViewPager viewPager : mPotViewPagerList) {
                viewPager.setCurrentItem(0);
            }
        }
        if (null != mOnPotPageChangeListenerList && mOnPotPageChangeListenerList.size() > 0) {
            for (HorizontalViewPager.OnPageChangeListener onPageChangeListener : mOnPotPageChangeListenerList) {
                onPageChangeListener.onPageSelected(0);
            }
        }

        initRecommendProductFragment(lists);
    }

    /**
     * 换搭配获取植物/花盆列表成功时回调
     *
     * @param recommend 植物花盆列表
     */
    @Override
    public void onGetRecommendSuccess(Recommend recommend) {
        if (recommend.getData().getPot() == null && recommend.getData().getPlant() == null) {
            MessageUtil.showMessage("无筛选结果，选择无效");
            return;
        }
        if (CODE_SUCCEED != recommend.getCode()) {
            MessageUtil.showMessage(recommend.getMsg());
            return;
        }
        mProductTypeViewPager.removeAllViews();
        mProductTypeDotLinearLayout.removeAllViews();
        Recommend.DataBean data = recommend.getData();
        if (null == data) {
            return;
        }
        List<ListBean> potList = new ArrayList<>();
        List<ListBean> plantList = new ArrayList<>();

        if (recommend.getData().getPlant() != null) {
            plantList.addAll(recommend.getData().getPlant().getList());
            productType = PLANT;
            List<List<ListBean>> plantLists = splitList(plantList, 10);
            mPlantAdapter.clear();
            mPlantAdapter.addAll(plantList);
            int pos = 0;
            if (plantList.size() > 0) {
                if (productSkuId > 0) {
                    for (int i = 0; i < plantList.size(); i++) {
                        ListBean item = plantList.get(i);
                        if (item.getSku_id() == productSkuId) {
                            pos = i;
                            break;
                        }
                    }
                }

                plantList.get(pos).setSelect(true);

                mPlantAdapter.getAllData().get(pos).setSelect(true);
                mPlantRecyclerView.scrollToPosition(pos);
                mRecommendPlantList.clear();
                mRecommendPlantList.addAll(plantList);
                mProductTypeTextView.setText(getResources().getString(R.string.recommend_plants));
                mFilterTextView.setVisibility(View.VISIBLE);
                if (null != mPlantSelectAdapterList && mPlantSelectAdapterList.size() > 0) {
                    for (PlantSelectAdapter plantSelectAdapter : mPlantSelectAdapterList) {
                        plantSelectAdapter.notifyDataSetChanged();
                    }
                }
                // 默认选中第一个
                if (null != mPlantViewPagerList && mPlantViewPagerList.size() > 0) {
                    for (HorizontalViewPager viewPager : mPlantViewPagerList) {
                        viewPager.setCurrentItem(pos);
                    }
                }
                if (null != mOnPlantPageChangeListenerList && mOnPlantPageChangeListenerList.size() > 0) {
                    for (HorizontalViewPager.OnPageChangeListener onPageChangeListener : mOnPlantPageChangeListenerList) {
                        onPageChangeListener.onPageSelected(pos);
                    }
                }
                initRecommendProductFragment(plantLists);
            }
        }
        if (recommend.getData().getPot() != null) {
            potList.addAll(recommend.getData().getPot().getList());
            productType = POT;
            List<List<ListBean>> potLists = splitList(potList, 10);
            int pos = 0;
            mPotAdapter.clear();
            mPotAdapter.addAll(potList);
            if (potList.size() > 0) {
                if (lastBean != null) {
                    for (int i = 0; i < potList.size(); i++) {
                        ListBean item = potList.get(i);
                        if (item.getSku_id() == lastBean.getSku_id()) {
                            pos = i;
                            break;
                        }
                    }
                }
                potList.get(pos).setSelect(true);
                mPotAdapter.getAllData().get(pos).setSelect(true);
                mPotRecyclerView.scrollToPosition(pos);

                mRecommendPotList.clear();
                mRecommendPotList.addAll(potList);
                mProductTypeTextView.setText(getResources().getString(R.string.recommend_pot));
                if (null != mPotSelectAdapterList && mPotSelectAdapterList.size() > 0) {
                    for (PotSelectAdapter potSelectAdapter : mPotSelectAdapterList) {
                        potSelectAdapter.notifyDataSetChanged();
                    }
                }
                // 默认选中第一个
                if (null != mPotViewPagerList && mPotViewPagerList.size() > 0) {
                    for (HorizontalViewPager viewPager : mPotViewPagerList) {
                        viewPager.setCurrentItem(pos);
                    }
                }
                if (null != mOnPotPageChangeListenerList && mOnPotPageChangeListenerList.size() > 0) {
                    for (HorizontalViewPager.OnPageChangeListener onPageChangeListener : mOnPotPageChangeListenerList) {
                        onPageChangeListener.onPageSelected(pos);
                    }
                }

                mPotAdapter.notifyDataSetChanged();
                initRecommendProductFragment(potLists);
            }
        }
    }

    @Override
    public void onGetNewRecommendSuccess(Recommend model) {
        if (model.getData().getPot().getCount() < 2) {
            for (int i = 0; i < mParamSonAdapter.getCount(); i++) {
                BTCBean.Child child = mParamSonAdapter.getItem(i);
                child.setSelect(false);
            }
            mParamSonAdapter.notifyDataSetChanged();
            ToastPlus.Companion.show(getContext(), "无筛选结果，请选择其它条件", false);
            return;
        }

        mProductTypeViewPager.removeAllViews();
        mProductTypeDotLinearLayout.removeAllViews();
        mRecommendPotList.clear();

        LinkedList<ListBean> list = new LinkedList<>();
        for (Iterator<ListBean> iterator = model.getData().getPot().getList().iterator(); iterator.hasNext(); ) {
            ListBean copies = iterator.next();
            ListBean item = new ListBean();
            item.setSelect(false);
            item.setHeight(copies.getHeight());
            item.setDiameter(copies.getDiameter());
            item.setMonth_rent(copies.getMonth_rent());
            item.setName(copies.getName());
            item.setSku_id(copies.getSku_id());
            item.setProduct_id(copies.getProduct_id());
            item.setOffset_ratio(copies.getOffset_ratio());
            item.setPic1(copies.getPic1());
            item.setPic2(copies.getPic2());
            item.setPic3(copies.getPic3());
            item.setPrice(copies.getPrice());
            item.setTrade_price(copies.getTrade_price());
            list.add(item);
        }

        List<List<ListBean>> lists = splitList(list, 10);
        if (null == lists || lists.size() == 0) {
            return;
        }

        mPotAdapter.clear();
        mPotAdapter.addAll(list);

        int pos = 0;
        if (lastBean != null && mPotAdapter.getCount() > 0) {
            for (int i = 0; i < mPotAdapter.getCount(); i++) {
                ListBean item = mPotAdapter.getItem(i);
                if (item.getSku_id() == lastBean.getSku_id()) {
                    pos = mPotAdapter.getAllData().indexOf(item);
                }
            }
        }
        mPotAdapter.getAllData().get(pos).setSelect(true);
        mPotRecyclerView.scrollToPosition(pos);

        mRecommendPotList.clear();
        mRecommendPotList.addAll(list);
        mProductTypeTextView.setText(getResources().getString(R.string.recommend_pot));
        if (null != mPotSelectAdapterList && mPotSelectAdapterList.size() > 0) {
            for (PotSelectAdapter potSelectAdapter : mPotSelectAdapterList) {
                potSelectAdapter.notifyDataSetChanged();
            }
        }
        // 默认选中第一个
        if (null != mPotViewPagerList && mPotViewPagerList.size() > 0) {
            for (HorizontalViewPager viewPager : mPotViewPagerList) {
                viewPager.setCurrentItem(0);
            }
        }
        if (null != mOnPotPageChangeListenerList && mOnPotPageChangeListenerList.size() > 0) {
            for (HorizontalViewPager.OnPageChangeListener onPageChangeListener : mOnPotPageChangeListenerList) {
                onPageChangeListener.onPageSelected(0);
            }
        }
        initRecommendProductFragment(lists);
    }

    /**
     * 初始化{@link RecommendProductFragment}
     *
     * @param lists 换搭配推荐植物/盆器列表
     */
    private void initRecommendProductFragment(List<List<ListBean>> lists) {
        int size = lists.size();
        Fragment[] fragment = new Fragment[size];
        for (int i = 0; i < size; i++) {
            List<ListBean> beans = lists.get(i);
            fragment[i] = new RecommendProductFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(POSITION, i);
            bundle.putString(CATEGORY_CODE, productType);
            bundle.putString(PRODUCT_SKU_LIST, new Gson().toJson(beans));
            fragment[i].setArguments(bundle);
        }
        initDots(mContext, mProductTypeDotLinearLayout, size);
        FragmentManager manager = getChildFragmentManager();
        mProductTypeViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
    }

    /**
     * 将"模拟搭配产品"数据添加到报价列表数据中的结果
     *
     * @param result true添加成功,false添加失败
     */
    @Override
    public void addQuotationDataResult(boolean result) {
        if (result) {
            MessageUtil.showMessage(mContext.getResources().getString(R.string.dding_to_purchase_list_successfully));
        } else {
            MessageUtil.showMessage(mContext.getResources().getString(R.string.adding_to_purchase_list_failed));
        }
    }

    /**
     * 添加产品数据到"模拟搭配产品"数据操作的结果
     *
     * @param result true操作成功,false操作失败
     */
    @Override
    public void onAddSimulationDataResult(boolean result) {
        mSceneEditPresenter.getSimulationData(sceneId);
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
     * 在RangeSeekBar值发生变化时回调
     *
     * @param view  值发生变化的RangeSeekBar
     * @param value RangeSeekBar的值
     */
    @Override
    public void onRangeChanged(RangeSeekBar view, float value) {
        int id = view.getId();

        // 设置亮度
        if (id == R.id.brightnessRangeSeekBar) {
            if (!judeFileExists(filePath)) {
                return;
            }
            // 设置滤镜
            mGpuImage.setFilter(mGpuImageBrightnessFilter);
            // 设置滤镜的值
            mBrightnessFilter.adjust((int) value);
            // 获取当前应用了滤镜效果的Bitmap
            Bitmap bitmap = mGpuImage.getBitmapWithFilterApplied();
            mSceneBackgroundImageView.setImageBitmap(bitmap);
        }

        // 设置对比度
        if (id == R.id.contrastRangeSeekBar) {
            if (!judeFileExists(filePath)) {
                return;
            }
            // 设置滤镜
            mGpuImage.setFilter(mGpuImageContrastFilter);
            // 设置滤镜的值
            mContrastFilter.adjust((int) value);
            // 获取当前应用了滤镜效果的Bitmap
            Bitmap bitmap = mGpuImage.getBitmapWithFilterApplied();
            mSceneBackgroundImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 在设置"搭配图片布局的容器"的位置成功时回调 -> 单产品
     *
     * @param viewPagerList 存放动态添加的"ViewPager"
     */
    @Override
    public void onSetSingleCollocationLayoutPositionSucceed(List<HorizontalViewPager> viewPagerList) {
        mPotViewPagerList.clear();
        mPotSelectAdapterList.clear();
        mOnPotPageChangeListenerList.clear();

        for (int i = 0; i < viewPagerList.size(); i++) {
            HorizontalViewPager viewPager = viewPagerList.get(i);

            PotSelectAdapter potSelectAdapter = new PotSelectAdapter(mContext, mRecommendPotList, true);
            viewPager.setAdapter(potSelectAdapter);
            potSelectAdapter.setOnItemLongClickListener(this);

            int index = i;
            HorizontalViewPager.OnPageChangeListener onPageChangeListener = new HorizontalViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    onPotPageSelected(position, index);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
            };
            viewPager.addOnPageChangeListener(onPageChangeListener);

            mPotViewPagerList.add(viewPager);
            mPotSelectAdapterList.add(potSelectAdapter);
            mOnPotPageChangeListenerList.add(onPageChangeListener);
        }
    }

    /**
     * 在设置"搭配图片布局的容器"的位置成功时回调 -> 组合
     *
     * @param matchLayoutList 动态添加的"植物与盆器互相搭配的View"列表
     */
    @Override
    public void onSetGroupCollocationLayoutPositionSucceed(List<MatchLayout> matchLayoutList) {
        mPlantViewPagerList.clear();
        mPotViewPagerList.clear();
        mPlantSelectAdapterList.clear();
        mPotSelectAdapterList.clear();
        mOnPlantPageChangeListenerList.clear();
        mOnPotPageChangeListenerList.clear();

        for (int i = 0; i < matchLayoutList.size(); i++) {
            MatchLayout matchLayout = matchLayoutList.get(i);
            HorizontalViewPager plantViewPager = matchLayout.getPlantViewPager();
            HorizontalViewPager potViewPager = matchLayout.getPotViewPager();

            PlantSelectAdapter plantSelectAdapter = new PlantSelectAdapter(mContext, mRecommendPlantList);
            plantViewPager.setAdapter(plantSelectAdapter);
            plantSelectAdapter.setOnItemLongClickListener(this);
            PotSelectAdapter potSelectAdapter = new PotSelectAdapter(mContext, mRecommendPotList);
            potViewPager.setAdapter(potSelectAdapter);
            potSelectAdapter.setOnItemLongClickListener(this);

            // 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,防止只在下载完植物或者盆器图片时
            // 而与之对应的盆器或者植物图片没有下载完时,植物或者盆器图片占满整个MatchLayout的错误现象
            double productHeight_ = mSimulationData.getProductHeight();
            double augmentedProductHeight_ = mSimulationData.getAugmentedProductHeight();
            double productOffsetRatio_ = mSimulationData.getProductOffsetRatio();
            double augmentedProductOffsetRatio_ = mSimulationData.getAugmentedProductOffsetRatio();
            mSceneEditPresenter.setCollocationContentParams(matchLayout, plantViewPager,
                    potViewPager, productHeight_, augmentedProductHeight_, productOffsetRatio_, augmentedProductOffsetRatio_);

            // TODO 左右滚动
            int index = i;
            HorizontalViewPager.OnPageChangeListener onPlantPageChangeListener = new HorizontalViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    onPlantPageSelected(position, index);
                    // 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
                    mSceneEditPresenter.setCollocationContentParams(matchLayout, plantViewPager,
                            potViewPager, productHeight, augmentedProductHeight, productOffsetRatio, augmentedProductOffsetRatio);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    onPlantPageScrollStateChanged(state);
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
            };
            // TODO 左右滚动
            HorizontalViewPager.OnPageChangeListener onPotPageChangeListener = new HorizontalViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    onPotPageSelected(position, index);
                    // 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
                    mSceneEditPresenter.setCollocationContentParams(matchLayout, plantViewPager,
                            potViewPager, productHeight, augmentedProductHeight, productOffsetRatio, augmentedProductOffsetRatio);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    onPotPageScrollStateChanged(state);
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
            };
            plantViewPager.addOnPageChangeListener(onPlantPageChangeListener);
            potViewPager.addOnPageChangeListener(onPotPageChangeListener);

            mPlantViewPagerList.add(plantViewPager);
            mPotViewPagerList.add(potViewPager);
            mPlantSelectAdapterList.add(plantSelectAdapter);
            mPotSelectAdapterList.add(potSelectAdapter);
            mOnPlantPageChangeListenerList.add(onPlantPageChangeListener);
            mOnPotPageChangeListenerList.add(onPotPageChangeListener);
        }
    }

    /**
     * 填充底部筛选栏
     */
    private List<Pair<String, List<BTCBean.Child>>> mParams = new LinkedList<>();

    @Override
    public void onAddParam(List<BTCBean> bean) {
        if (bean != null && bean.size() > 0) {
            mParamAdapter.clear();
            mParamAdapter.addAll(bean);

            for (int i = 0; i < bean.size(); i++) {
                BTCBean item = bean.get(i);

                List<BTCBean.Child> children = new LinkedList<BTCBean.Child>();
                for (int j = 0; j < item.getSon().size(); j++) {
                    BTCBean.Child child = item.getSon().get(j);
                    child.setScreen_type(item.getScreen_type());
                    children.add(child);
                }
                mParams.add(new Pair<>(item.getCate_code(), children));

            }
            BTCBean item = bean.get(0);
            item.setSelect(true);

            mParamSonAdapter.setKey(item.getCate_code());
            mParamSonAdapter.addAll(mParams.get(0).getSecond());

            mParamAdapter.notifyDataSetChanged();
            mParamSonAdapter.notifyDataSetChanged();
        }
    }

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
            mBtnChangeLocation.setText(cityName);
        }
        /**
         * 发送数据到{@link ProductFragment#onSetUserPositionSuccess(UserPosition)}
         * 使其刷新界面数据
         */
        EventBus.getDefault().post(userPosition);
    }

    /**
     * 获取设计列表数据到达
     *
     * @param page 分页数据
     */
    @Override
    public void onGetDesignScenes(ResponsePage<ItemDesignSceneModel> page) {

    }

    /**
     * 添加图片
     *
     * @param number 设计编号
     */
    @Override
    public void onCreateDesignResult(String number) {
        btnCnAdd.setText("继续添加");
        SPUtil.INSTANCE.putValue(getContext(), "number", number);
        mSceneEditPresenter.getDesignDetail(number);
        mSceneEditPresenter.getSceneList();
    }

    private ItemDesignSceneModel mDesignSceneModel;

    /**
     * 获取设计详情
     *
     * @param model 信息
     */
    @Override
    public void onGetDesignDetail(ItemDesignSceneModel model) {
        mDesignSceneModel = model;
       /*mSceneAdapter.clear();
        if (model != null && model.getSchemelist().size() > 0) {
            btnCnAdd.setText("继续添加");
            mSceneAdapter.addAll(model.getSchemelist());
            mSceneRecyclerView.setVisibility(View.VISIBLE);
        }*/
        if (model != null && model.getSchemelist().size() > 0) {
            btnCnAdd.setText("继续添加");
            mSceneRecyclerView.setVisibility(View.VISIBLE);
        }

        try {
            DbManager manager = YiBaiApplication.getDbManager();
            List<SceneInfo> sceneInfos = manager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .findAll();
            for (int i = 0; i < sceneInfos.size(); i++) {
                SceneInfo info = sceneInfos.get(i);
                if (model.getSchemelist() != null && model.getSchemelist().size() >= sceneInfos.size()) {
                    ItemDesignSceneModel.ItemModel im = model.getSchemelist().get(i);
                    info.setScheme_id(im.getScheme_id());
                }
            }
            manager.update(sceneInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传方案
     */
    void uploadScheme() {
        try {
            DbManager manager = YiBaiApplication.getDbManager();
            List<SceneInfo> sceneInfos = manager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .findAll();
            for (int i = 0; i < mDesignSceneModel.getSchemelist().size(); i++) {
                ItemDesignSceneModel.ItemModel model = mDesignSceneModel.getSchemelist().get(i);
                SceneInfo item = sceneInfos.get(i);
                String type = mDesignSceneModel.getSchemelist().size() < 1 ? "1" : "2";
                String design_number = SPUtil.INSTANCE.getValue(getContext(), "number", String.class);
                String schemeId = model.getScheme_id();
                if (mDesignSceneModel != null && mDesignSceneModel.getSchemelist().size() > 0) {
                    schemeId = mDesignSceneModel.getSchemelist().get(0).getScheme_id();
                }
                String schemeName = mTitle;
                if (item.getSceneBackground() == null || item.getSceneBackground().isEmpty())
                    return;
                File file = new File(item.getSceneBackground());
                Map<String, RequestBody> body = new LinkedHashMap<>();
                String fileName = file.getName();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
                body.put("pic\"; filename=\"" + file.getName(), requestBody);

                String product_sku_id = null;
                if (mSimulationDataList.size() > 0) {
                    SimulationData sd = mSimulationDataList.get(0);
                    product_sku_id = String.valueOf(sd.getProductSkuId());
                    product_sku_id = String.format("%s,%s", product_sku_id, sd.getAugmentedProductSkuId());
                }
                mSceneEditPresenter.CreateSceneOrPic(design_number, type, schemeId,
                        schemeName, body, product_sku_id
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateSceneOrPicCallback(CreateSceneOrPicModel model) {
        mSceneEditPresenter.getDesignDetail(mDesignSceneModel.getNumber());
        mSceneEditPresenter.getSceneList();
    }

    // 场景列表
    @Override
    public void onGetSceneListCallback(List<SceneInfo> sceneInfos) {
        mSceneAdapter.clear();
        btnCnAdd.setText("继续添加");
        mSceneAdapter.addAll(sceneInfos);
        mSceneRecyclerView.setVisibility(View.VISIBLE);
    }

    private void increment() {
        try {
            DbManager manager = YiBaiApplication.getDbManager();
            List<SceneInfo> sceneInfoList = manager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .findAll();
            SceneInfo selected = null;
            for (int i = 0; i < sceneInfoList.size(); i++) {
                if (sceneInfoList.get(i).isEditScene()) {
                    selected = sceneInfoList.get(i);
                }
            }
            if (selected != null) {
                selected.setCount(selected.getCount() + 1);
            }
            manager.update(selected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在某一个显示推荐搭配植物列表的ViewPage选中新的位置时
     * 设置其他相同的ViewPage也选中新的位置,并且获取相关植物信息
     *
     * @param position 新选中的位置
     * @param index    当前选中新位置的ViewPage的侦听器在显示植物列表的ViewPager其侦听器的集合中的位置
     */
    private void onPlantPageSelected(int position, int index) {
        boolean positionFlag = false;
        if (potPosition != position) positionFlag = true;
        plantPosition = position;
        // 获取植物信息(主产品)
        getPlantInfo(position);
        if (null == mOnPlantPageChangeListenerList || mOnPlantPageChangeListenerList.size() == 0) {
            return;
        }
        for (int j = 0; j < mOnPlantPageChangeListenerList.size(); j++) {
            // 找出当前没有滑动的ViewPager,将其当前的位置设置为和当前滑动的ViewPager一样的位置
            if (index == j) {
                if (positionFlag) {
                    for (Iterator<ListBean> iterator = mRecommendPlantList.iterator(); iterator.hasNext(); ) {
                        ListBean bean = iterator.next();
                        bean.setSelect(false);
                    }
                    mRecommendPlantList.get(position).setSelect(true);
                    mPlantRecyclerView.scrollToPosition(position);
                    mPlantAdapter.notifyDataSetChanged();
                    if (!isChangeFlag) {
                        mSceneEditPresenter.getNewRecommed(POT, productSkuId, -5, mSelectType, specidList, attridList, plantParamMap);
                        isChangeFlag = false;
                    }
                }
                continue;
            }
            mPlantViewPagerList.get(j).setCurrentItem(position);
        }
    }

    /**
     * 在某一个显示推荐搭配盆器列表的ViewPage选中新的位置时
     * 设置其他相同的ViewPage也选中新的位置,并且获取相关盆器信息
     *
     * @param position 新选中的位置
     * @param index    当前选中新位置的ViewPage的侦听器在显示盆器列表的ViewPager其侦听器的集合中的位置
     */
    private void onPotPageSelected(int position, int index) {
        boolean positionFlag = false;
        if (potPosition != position) positionFlag = true;
        potPosition = position;
        // 获取盆器信息(附加产品)
        getPotInfo(position);
        if (null == mOnPotPageChangeListenerList || mOnPotPageChangeListenerList.size() == 0) {
            return;
        }
        for (int j = 0; j < mOnPotPageChangeListenerList.size(); j++) {
            // 找出当前没有滑动的ViewPager,将其当前的位置设置为和当前滑动的ViewPager一样的位置
            if (index == j) {
                if (positionFlag) {
                    if (augmentedCombinationType != 1) {
                        for (Iterator<ListBean> iterator = mRecommendPotList.iterator(); iterator.hasNext(); ) {
                            ListBean bean = iterator.next();
                            bean.setSelect(false);
                        }
                        mRecommendPotList.get(position).setSelect(true);
                        mPotRecyclerView.scrollToPosition(position);
                        mPotAdapter.notifyDataSetChanged();
                    }
                }
                continue;
            }
            mPotViewPagerList.get(j).setCurrentItem(position);
        }
    }

    private void onPlantPageScrollStateChanged(int state) {
        if (SCROLL_STATE_DRAGGING != state) {
            return;
        }
        if (!lastDraggingViewPager) {
            mProductTypeBottomSheetBehavior.setState(STATE_HIDDEN);
//            // 用户之前滑动的是盆器ViewPager,所以当用户再滑动植物ViewPager时,需要重新获取推荐的植物列表
//            mSceneEditPresenter.getRecommend(PLANT, productSkuId, augmentedProductSkuId,
//                    mSelectType,
//                    potTypeId, plantParamMap);
            mSceneEditPresenter.getNewRecommed(POT, productSkuId, -5, mSelectType, specidList, attridList, plantParamMap);
        }
        lastDraggingViewPager = true;
    }

    private void onPotPageScrollStateChanged(int state) {
        if (SCROLL_STATE_DRAGGING != state) {
            return;
        }
        if (lastDraggingViewPager) {
            mProductTypeBottomSheetBehavior.setState(STATE_HIDDEN);
            // 用户之前滑动的是植物ViewPager,所以当用户再滑动盆器ViewPager时,需要重新获取推荐的盆器列表
//            mSceneEditPresenter.getRecommend(POT, productSkuId, augmentedProductSkuId, mSelectType, potTypeId, potParamMap);
        }
        lastDraggingViewPager = false;
    }

    /**
     * 获取植物信息(主产品)
     *
     * @param position 被选中的植物在推荐植物列表中的位置
     */
    private void getPlantInfo(int position) {
        if (null == mRecommendPlantList || mRecommendPlantList.size() == 0) {
            return;
        }
        ListBean listBean = mRecommendPlantList.get(position);
        if (null == listBean) {
            return;
        }
        GVar.INSTANCE.setCurrent_product_sku_id(String.valueOf(listBean.getSku_id()));
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
    }

    /**
     * 获取盆器信息(附加产品)
     *
     * @param position 被选中的盆器在推荐盆器列表中的位置
     */
    private void getPotInfo(int position) {
        if (null == mRecommendPotList || mRecommendPotList.size() == 0) {
            return;
        }
        ListBean listBean = mRecommendPotList.get(position);
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

    /**
     * 更换搭配操作
     *
     * @param flag 选择大中小进来还是改款进来
     */
    private void replaceCollocation(boolean flag) {
        String text = mChangeStyleTextView.getText().toString();
        if (text.equals(mContext.getResources().getString(R.string.change_style))) {
            isChangeFlag = true;
            BaseSticker currentSticker = mStickerView.getCurrentSticker();
            if (null == currentSticker) {
                return;
            }
            int position = (int) currentSticker.getTag();
            mSimulationData = mSimulationDataList.get(position);
            mSelectType = SceneHelper.getSpecTypeId(getActivity());
            mComType = String.valueOf(mSimulationData.getAugmentedCombinationType());
            finallySkuId = mSimulationData.getFinallySkuId();
            productSkuId = mSimulationData.getProductSkuId();
            augmentedProductSkuId = mSimulationData.getAugmentedProductSkuId();
            productCombinationType = mSimulationData.getProductCombinationType();
            augmentedCombinationType = mSimulationData.getAugmentedCombinationType();

            mChangeStyleTextView.setText(mContext.getResources().getString(R.string.accomplish));
            mProductCodeImageButton.setVisibility(View.VISIBLE);
            mBonsaiEditLayout.setVisibility(View.GONE);
            mCollocationLayout.setVisibility(View.VISIBLE);
            mProductTypeBottomSheetBehavior.setState(STATE_HIDDEN);
            mSceneContainerTool.setVisibility(View.GONE);
            // 非单图模式
            if (!(mComType != null && mComType.equals("1"))) {
                mBottomCatTab.setVisibility(View.VISIBLE);
                btnHideTools.setVisibility(View.VISIBLE);
                mPlantRecyclerView.setVisibility(View.VISIBLE);
                mPotRecyclerView.setVisibility(View.VISIBLE);
                mParamSonRecyclerView.setVisibility(View.VISIBLE);
                mParamTabRecyclerView.setVisibility(View.VISIBLE);
                mBtnChangeLocation.setVisibility(View.INVISIBLE);
                mMultipleImageContrastTextView.setVisibility(View.VISIBLE);
                mSavePhoto.setVisibility(View.VISIBLE);
            }
            removeSticker();

            mSceneEditPresenter.setCollocationLayoutPosition(mCollocationLayout, mSimulationDataList,
                    finallySkuId, productCombinationType, augmentedCombinationType);
            if (1 == productCombinationType || 1 == augmentedCombinationType) {
                // 单图模式
                //mPotClassSelectTextView.setVisibility(View.GONE);
                //mMultipleImageContrastTextView.setVisibility(View.GONE);
                mBottomCatTab.setVisibility(View.GONE);
                btnHideTools.setVisibility(View.GONE);
                mPlantRecyclerView.setVisibility(View.GONE);
                mPotRecyclerView.setVisibility(View.GONE);
                mParamSonRecyclerView.setVisibility(View.GONE);
                mParamTabRecyclerView.setVisibility(View.GONE);
                mBtnChangeLocation.setVisibility(View.GONE);
                mMultipleImageContrastTextView.setVisibility(View.GONE);
                mSavePhoto.setVisibility(View.GONE);
                mSceneEditPresenter.getCategorySimilarSKUList(augmentedProductSkuId);
            } else {
                // 上下搭配
                //mPotClassSelectTextView.setVisibility(View.VISIBLE);
                mMultipleImageContrastTextView.setVisibility(View.VISIBLE);
                mSavePhoto.setVisibility(View.VISIBLE);
                if (flag) {
//                    mSceneEditPresenter.getNewRecommedByAddSpec(mSelectType);
                } else {
                    mSceneEditPresenter.getNewRecommedChangeStyle(productSkuId, augmentedProductSkuId);
//                    mSceneEditPresenter.getRecommend(PLANT, productSkuId, augmentedProductSkuId, mSelectType, potTypeId, plantParamMap);
//                    mSceneEditPresenter.getRecommend(POT, productSkuId, augmentedProductSkuId, mSelectType, potTypeId, potParamMap);
                }
            }

            // 显示第三个引导层
            String label = getClass().getSimpleName() + "Three";
            GuideUtil.showGuideView(this, R.layout.guide_scene_edit_three_layout, label);
        } else {
            mProductCodeImageButton.setVisibility(View.GONE);
            mChangeStyleTextView.setText(mContext.getResources().getString(R.string.change_style));
            mBtnChangeLocation.setVisibility(View.GONE);
            mBottomCatTab.setVisibility(View.GONE);
            mPlantRecyclerView.setVisibility(View.GONE);
            mPotRecyclerView.setVisibility(View.GONE);
            mChangeStyleTextView.setVisibility(View.GONE);
            mCollocationLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            //mPotClassSelectTextView.setVisibility(View.GONE);
            mSavePhoto.setVisibility(View.GONE);
            mMultipleImageContrastTextView.setVisibility(View.GONE);
            mProductTypeBottomSheetBehavior.setState(STATE_HIDDEN);
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
            if (1 == productCombinationType || 1 == augmentedCombinationType) {
                // 单图模式
                mSceneEditPresenter.addSimulationData(productData, mSimulationDataList, finallySkuId, true);
            } else {
                // 上下搭配
                mSceneEditPresenter.addSimulationData(productData, mSimulationDataList, finallySkuId, false);
            }
            /**
             * 发送数据到{@link SceneActivity#stickerViewSelected(StickerViewSelected)}
             */
            EventBus.getDefault().postSticky(new StickerViewSelected(false));
            if (mComType != null && mComType.equals("1")) {
                mSceneContainerTool.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 移除与当前正在操作的贴纸有着相同的finallySkuId的贴纸
     */
    private void removeSticker() {
        // 获取与当前正在操作的贴纸有着相同的finallySkuId的贴纸
        List<BaseSticker> allStickerList = mStickerView.getAllStickerList();
        if (null == allStickerList || allStickerList.size() == 0) {
            return;
        }
        List<BaseSticker> list = new ArrayList<>();
        for (BaseSticker baseSticker : allStickerList) {
            String finallySkuId_ = (String) baseSticker.getSign();
            if (TextUtils.isEmpty(finallySkuId) || TextUtils.isEmpty(finallySkuId_)) {
                continue;
            }
            if (!finallySkuId.equals(finallySkuId_)) {
                continue;
            }
            list.add(baseSticker);
        }
        if (list.size() == 0) {
            return;
        }
        // 移除贴纸
        for (BaseSticker baseSticker : list) {
            mStickerView.remove(baseSticker);
        }
    }

    /**
     * EventBus
     * 接收用户从{@link SceneActivity}传递过来的数据
     *
     * @param sceneView 返回地址
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onAddScheme(SceneContract.SceneView sceneView) {
        String photoName = prefix + TimeUtil.getTimeStamp();
        String pathName = mStickerView.saveSticker(photoName);
        File file = new File(pathName);
        if (file.isFile() && file.exists()) {
            MessageUtil.showMessage(getResources().getString(R.string.save_successfully_you_can_click_on_my_design_below_to_view));
            sceneView.onAddSchemePath(pathName, productSkuId, augmentedProductSkuId);
//            /**
//             * 发送数据到{@link SceneActivity#uploadSceneNum(SceneInfo)}
//             */
//            EventBus.getDefault().post(mSceneInfo);
            return;
        }
        MessageUtil.showMessage(getResources().getString(R.string.save_failed));
    }

    /**
     * EventBus
     * 接收用户从{@link SceneActivity#onClick(View)}
     * 发送过来的数据(在用户点击"背景图"时)
     *
     * @param v 被点击的view
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSceneActivityClick(@NonNull View v) {
        int id = v.getId();

        if (position != currentPosition) {
            return;
        }

        // 加入报价
        if (id == R.id.joinQuotationTextView) {
            int state = mAlreadyPlacedBottomSheetBehavior.getState();
            if (STATE_EXPANDED == state || STATE_COLLAPSED == state) {
                // 完全展开/折叠状态,就是peekHeight设置的窥视高度
                mAlreadyPlacedBottomSheetBehavior.setState(STATE_HIDDEN);
            } else if (STATE_HIDDEN == state) {
                // 隐藏状态
                mAlreadyPlacedBottomSheetBehavior.setState(STATE_EXPANDED);
            }
        }

        // 背景图
        if (id == R.id.backgroundImageTextView) {
            if (View.VISIBLE == mBonsaiEditLayout.getVisibility()) {
                mBonsaiEditLayout.setVisibility(View.GONE);
            }
            int newState = mProductTypeBottomSheetBehavior.getState();
            if (STATE_EXPANDED == newState || STATE_COLLAPSED == newState) {
                // 完全展开/折叠状态,就是peekHeight设置的窥视高度
                mProductTypeBottomSheetBehavior.setState(STATE_HIDDEN);
            }
            int visibility = mBackgroundEditLayout.getVisibility();
            if (View.GONE == visibility) {
                mBackgroundEditLayout.setVisibility(View.VISIBLE);
            } else {
                mBackgroundEditLayout.setVisibility(View.GONE);
            }
        }

//        // 保存
//        if (id == R.id.saveTextView) {
////            String photoName = prefix + TimeUtil.getTimeStamp();
////            String pathName = mStickerView.saveSticker(photoName);
////            File file = new File(pathName);
////            if (file.isFile() && file.exists()) {
////                MessageUtil.showMessage(getResources().getString(R.string.save_successfully_you_can_click_on_my_design_below_to_view));
////                return;
////            }
////            MessageUtil.showMessage(getResources().getString(R.string.save_failed));
//        }

        // 场景列表抽屉-》拍照📷被点击
        if (id == R.id.floatingActionCamera) {
            Intent intent = new Intent(getContext(), StartDesignActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    /**
     * EventBus
     * 接收用户从{@link SceneActivity}传递过来的数据
     * ,ViewPager某一个页面被选中时
     *
     * @param viewPagerPosition 当前ViewPager被选中的位置
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPageSelected(@NonNull ViewPagerPosition viewPagerPosition) {
        currentPosition = viewPagerPosition.getPosition();
        try {
            DbManager manager = YiBaiApplication.getDbManager();
            List<SceneInfo> sceneInfoList = manager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .findAll();
            for (int i = 0; i < sceneInfoList.size(); i++) {
                sceneInfoList.get(i).setEditScene(false);
            }
            SceneInfo item = sceneInfoList.get(currentPosition);
            item.setEditScene(true);
            manager.update(sceneInfoList);
        } catch (DbException e) {
            e.printStackTrace();
        }
        mSceneEditPresenter.getSceneList();
    }

    /**
     * EventBus
     * 接收用户从{@link SceneEditFragment}传递过来的数据
     *
     * @param specTypeBean 用户选择植物的规格
     */
    int mSelectType;

    /**
     * 植物大中小的名稱
     */
    private String specTypeName;
    private boolean addSpec = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlantSpecItemClick(SpectypeBean specTypeBean) {
        if (position != currentPosition) return;
        mSelectType = specTypeBean.getId();
        specTypeName = specTypeBean.getName();
        SceneHelper.saveSpecTypeId(getActivity(), mSelectType);
//        mSceneEditPresenter.getSpecSuk(mSelectType);
        int[] template = {4, 3, 2, 1};
        boolean exists = false;
        for (int value : template) {
            exists = value == mSelectType;
            if (exists) break;
        }
        if (exists) {
            new Handler().postDelayed(() -> {
                addSpec = true;
                mSceneEditPresenter.getNewRecommedByAddSpec(mSelectType);
//                replaceCollocation(true);
                EventBus.getDefault().postSticky(new StickerViewSelected(true));
                if (mComType != null && mComType.equals("1")) {
                    mSceneContainerTool.setVisibility(View.GONE);
                }
                mChangeStyleTextView.setVisibility(View.VISIBLE);
            }, 1000);
        }
    }

    void simulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    /**
     * EventBus
     * 接收用户从{@link ConsumerPreselectionFragment#onItemClick(int)}传递过来的数据
     *
     * @param consumerPreselection 用户选择的"客户预选产品"的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedConsumerPreselection(PlacementQrQuotationList.DataBean.ListBean consumerPreselection) {
        if (position != currentPosition) {
            return;
        }
        // TODO 服务器端没有给"批发价"数据,使用存在BUG
        ListBean listBean = new ListBean();
        listBean.setName(consumerPreselection.getFirst_name());
        listBean.setSku_id(consumerPreselection.getFirst_sku_id());
        listBean.setPic2(consumerPreselection.getPic());
        listBean.setPrice(consumerPreselection.getPrice());
        listBean.setMonth_rent(consumerPreselection.getMonth_rent());
        listBean.setComtype(consumerPreselection.getComtype());
        listBean.setCategoryCode(consumerPreselection.getFirst_cate_code());
        mSceneEditPresenter.addSimulationData(listBean);
    }

    /**
     * EventBus
     * 接收用户从{@link RecommendProductFragment#onItemClick(int)}传递过来的数据
     *
     * @param newMatch 用户选择的产品的数据(在用户更换盆器或者植物时)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedRecommendProduct(NewMatch newMatch) {
        if (position != currentPosition) {
            return;
        }
        int position = newMatch.getPosition();
        String categoryCode = newMatch.getCategoryCode();
        if (1 == productCombinationType || 1 == augmentedCombinationType) {
            // 单图模式
            setPotViewPagerCurrentItem(position);
        } else {
            // 上下搭配
            if (PLANT.equals(categoryCode)) {
                setPlantViewPagerCurrentItem(position);
            } else {
                setPotViewPagerCurrentItem(position);
            }
        }
        mProductTypeBottomSheetBehavior.setState(STATE_HIDDEN);
    }

    /**
     * EventBus
     * 接收用户从{@link SceneActivity#onItemClick(int)}传递过来的数据
     * 在用户点击报价列表Item的时候
     *
     * @param quotationData 用户的"报价"数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedQuotationData(QuotationData quotationData) {
        if (position != currentPosition) {
            return;
        }
        mSceneEditPresenter.addSimulationData(quotationData);
    }

    /**
     * EventBus
     * 接收用户从{@link FilterFragment#returnSelectProductParam(Map)}
     * 发送过来的数据(在返回用户选中的产品筛选参数)
     *
     * @param selectProductParam 用户选中的产品筛选参数
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void returnSelectProductParam(@NonNull SelectProductParam selectProductParam) {
        int position = selectProductParam.getPosition();
        if (this.position != position) {
            return;
        }
        Map<String, String> paramMap = selectProductParam.getSelectProductParamMap();
        if (PLANT.equals(productType)) {
            plantParamMap = paramMap;
        } else {
            potParamMap = paramMap;
        }
        // TODO 暂时只处理植物/盆器
        mSceneEditPresenter.getNewRecommed(productType, productSkuId, augmentedProductSkuId, mSelectType, specidList, attridList, paramMap);
//        mSceneEditPresenter.getRecommend(productType, productSkuId, augmentedProductSkuId, mSelectType, potTypeId, paramMap);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedEvent(BaseResponse<String> event) {
        if (event == null) return;
        // 标题
        if (event.getCode() == 1) {
            mTitle = event.getData();
        } else if (event.getCode() == 2) {
            uploadScheme();
        }
    }

    /**
     * EventBus
     * 接收用户从{@link ImageContrastActivity#onItemClick(int, String)}传递过来的数据
     *
     * @param imageContrastSelectedProduct 多张搭配图片模拟效果对比界面用户点击RecyclerView Item选中的盆栽信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void imageContrastActivitySelectedProduct(@NonNull ImageContrastSelectedProduct imageContrastSelectedProduct) {
        int productSkuId = imageContrastSelectedProduct.getProductSkuId();
        int augmentedProductSkuId = imageContrastSelectedProduct.getAugmentedProductSkuId();
        if (null != mRecommendPlantList && mRecommendPlantList.size() > 0) {
            for (int i = 0; i < mRecommendPlantList.size(); i++) {
                if (productSkuId == mRecommendPlantList.get(i).getSku_id()) {
                    setPlantViewPagerCurrentItem(i);
                    break;
                }
            }
        }
        if (null != mRecommendPotList && mRecommendPotList.size() > 0) {
            for (int i = 0; i < mRecommendPotList.size(); i++) {
                if (augmentedProductSkuId == mRecommendPotList.get(i).getSku_id()) {
                    setPotViewPagerCurrentItem(i);
                    break;
                }
            }
        }
    }

    /**
     * 设置显示植物列表的ViewPager当前显示的位置
     *
     * @param position 当前显示的位置
     */
    private void setPlantViewPagerCurrentItem(int position) {
        if (null != mPlantViewPagerList && mPlantViewPagerList.size() > 0) {
            for (HorizontalViewPager viewPager : mPlantViewPagerList) {
                viewPager.setCurrentItem(position);
            }
        }
    }

    /**
     * 设置显示盆器列表的ViewPager当前显示的位置
     *
     * @param position 当前显示的位置
     */
    private void setPotViewPagerCurrentItem(int position) {
        if (null != mPotViewPagerList && mPotViewPagerList.size() > 0) {
            for (HorizontalViewPager viewPager : mPotViewPagerList) {
                viewPager.setCurrentItem(position);
            }
        }
    }

    /**
     * 在请求网络数据之前显示Loading界面
     */
    @Override
    public void onShowLoading() {
        /*if (!mWaitDialog.isShowing()) {
            mWaitDialog.setWaitDialogText(getResources().getString(R.string.loading));
            mWaitDialog.show();
        }*/
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
        if (null != mSceneEditPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mSceneEditPresenter.onDetachView();
            mSceneEditPresenter = null;
            SPUtil.INSTANCE.putValue(getContext(), "com_type", mComType);
        }
    }
}