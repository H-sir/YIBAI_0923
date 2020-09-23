package com.ybw.yibai.module.main;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AppUpdate;
import com.ybw.yibai.common.bean.FunctionSwitch;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.bean.ToFragment;
import com.ybw.yibai.common.bean.UserInfo;
import com.ybw.yibai.common.classs.MainPopupMenu;
import com.ybw.yibai.common.utils.DownloadApkUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.module.home.HomeFragment;
import com.ybw.yibai.module.main.MainContract.MainPresenter;
import com.ybw.yibai.module.main.MainContract.MainView;
import com.ybw.yibai.module.my.MyFragment;
import com.ybw.yibai.module.product.ProductFragment;
import com.ybw.yibai.module.productusestate.ProductUseStateFragment;
import com.ybw.yibai.module.quotationpurchase.QuotationPurchaseFragment;
import com.ybw.yibai.module.scene.SceneActivity;
import com.ybw.yibai.module.startdesign.StartDesignActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_PERMISSIONS_CODE;
import static com.ybw.yibai.common.constants.Preferences.FILE_PATH;
import static com.ybw.yibai.common.utils.OtherUtil.transparentStatusBar;

/**
 * 主界面
 *
 * @author sjl
 * @date 2019/09/05
 */
public class MainActivity extends BaseActivity implements MainView,
        View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        MainPopupMenu.OnItemClickListener {

    private static final String TAG = "MainActivity";

    /**
     * 是否需要更新APP(1更新0否)
     */
    private int renew;

    /**
     * Root布局
     */
    private RelativeLayout mRootLayout;

    /**
     * 产品
     */
    private RadioButton mProductRadioButton;

    /**
     * 报价
     */
    private RadioButton mQuotationRadioButton;

    /**
     *
     */
    private RadioGroup mRadioGroup;

    /**
     *
     */
    private RelativeLayout mCentreLayout;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 上一个打开的Fragment
     */
    private Fragment mLastFragment;

    /**
     * 首页Fragment
     */
    private HomeFragment mHomeFragment;

    /**
     * 产品Fragment
     */
    private ProductFragment mProductFragment;

    /**
     * 报价/进货Fragment
     */
    private QuotationPurchaseFragment mQuotationPurchaseFragment;

    /**
     * 我的Fragment
     */
    private MyFragment mMyFragment;

    /**
     * Fragment事务对象
     */
    private FragmentTransaction mTransaction;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 要申请的权限(访问手机相机权限)
     */
    private String[] permissions = {
            Manifest.permission.CAMERA
    };

    /**
     * 主界面中间PopupMenu
     */
    private MainPopupMenu mPopupMenu;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private MainPresenter mMainPresenter;

    @Override
    protected int setLayout() {
        transparentStatusBar(this);
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mRootLayout = findViewById(R.id.rootLayout);
        mRadioGroup = findViewById(R.id.selectRadioGroup);
        mCentreLayout = findViewById(R.id.centreLayout);
        mProductRadioButton = findViewById(R.id.productRadioButton);
        mQuotationRadioButton = findViewById(R.id.quotationRadioButton);

        mHomeFragment = new HomeFragment();
        mProductFragment = new ProductFragment();
        mQuotationPurchaseFragment = new QuotationPurchaseFragment();
        mMyFragment = new MyFragment();

        // 获得FragmentManager对象.
        FragmentManager manager = getSupportFragmentManager();

        // 获得事务对象
        mTransaction = manager.beginTransaction();

        // 在指定的容器中添加Fragment
        mTransaction.add(R.id.contentFrameLayout, mHomeFragment, HomeFragment.class.getSimpleName());
        mTransaction.add(R.id.contentFrameLayout, mProductFragment, ProductFragment.class.getSimpleName());
        mTransaction.add(R.id.contentFrameLayout, mQuotationPurchaseFragment, QuotationPurchaseFragment.class.getSimpleName());
        mTransaction.add(R.id.contentFrameLayout, mMyFragment, MyFragment.class.getSimpleName());

        // 隐藏不必show的Fragment
        mTransaction.hide(mProductFragment);
        mTransaction.hide(mQuotationPurchaseFragment);
        mTransaction.hide(mMyFragment);

        mLastFragment = mHomeFragment;

        // 提交事务 https://www.jianshu.com/p/83e673c453f9
        mTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // 注册下载广播接收器
        DownloadApkUtil.registerBroadcast(this);

        mMainPresenter = new MainPresenterImpl(this);
        mMainPresenter.initDataAndXUtilsDataBase();
        mMainPresenter.findIfThereIsDefaultScene();
        mMainPresenter.appUpdate();
        mMainPresenter.getSystemParameter();
        mMainPresenter.getProductScreeningParam();
        mRadioGroup.setOnCheckedChangeListener(this);
        mCentreLayout.setOnClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.centreLayout) {
            Intent intent = new Intent(this, StartDesignActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            /*int navigationBarHeight = getNavigationBarHeight(this);
            mPopupMenu = MainPopupMenu.getInstance();
            mPopupMenu.show(this, mRootLayout, navigationBarHeight);
            mPopupMenu.setOnItemClickListener(this);*/
        }
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
            mMainPresenter.applyPermissions(permissions);
        } else if (2 == position) {
            // 相册
            mMainPresenter.openPhotoAlbum();
        } else if (3 == position) {
            // 背景模板
        }
        if (null != mPopupMenu && mPopupMenu.isShowing()) {
            mPopupMenu.closePopupWindow();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 重新获得事务对象
        mTransaction = getSupportFragmentManager().beginTransaction();

        if (checkedId == R.id.homeRadioButton) {
            if (!(mLastFragment instanceof HomeFragment)) {
                // 隐藏上一个显示的fragment
                mTransaction.hide(mLastFragment);
            }
            // 显示对应的fragment
            mTransaction.show(mHomeFragment);
            mLastFragment = mHomeFragment;
        } else if (checkedId == R.id.productRadioButton) {
            if (!(mLastFragment instanceof ProductFragment)) {
                // 隐藏上一个显示的fragment
                mTransaction.hide(mLastFragment);
            }
            // 显示对应的fragment
            mTransaction.show(mProductFragment);
            mLastFragment = mProductFragment;
        } else if (checkedId == R.id.quotationRadioButton) {
            if (!(mLastFragment instanceof QuotationPurchaseFragment)) {
                // 隐藏上一个显示的fragment
                mTransaction.hide(mLastFragment);
            }
            // 显示对应的fragment
            mTransaction.show(mQuotationPurchaseFragment);
            mLastFragment = mQuotationPurchaseFragment;
        } else if (checkedId == R.id.myRadioButton) {
            if (!(mLastFragment instanceof MyFragment)) {
                // 隐藏上一个显示的fragment
                mTransaction.hide(mLastFragment);
            }
            // 显示对应的fragment
            mTransaction.show(mMyFragment);
            mLastFragment = mMyFragment;
        }

        // 提交事务
        mTransaction.commitAllowingStateLoss();
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
            mMainPresenter.openCamera();
        }
    }

    /**
     * 请求权限的结果的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_PERMISSIONS_CODE != requestCode) {
            return;
        }
        // 获得申请的全部权限
        if (PermissionsUtil.allPermissionsAreGranted(grantResults)) {
            // 打开相机
            mMainPresenter.openCamera();
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
        mMainPresenter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 返回从相机或相册返回的图像
     *
     * @param file 图像文件
     */
    @Override
    public void returnsTheImageReturnedFromTheCameraOrAlbum(File file) {
        Intent intent = new Intent(this, SceneActivity.class);
        intent.putExtra(FILE_PATH, file.getAbsolutePath());
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 请求应用更新成功时回调
     *
     * @param appUpdate 请求应用更新成功时服务器端返回的数据
     */
    @Override
    public void onAppUpdateSuccess(AppUpdate appUpdate) {
        if (CODE_SUCCEED != appUpdate.getCode()) {
            mMainPresenter.getUserInfo();
            return;
        }
        AppUpdate.DataBean data = appUpdate.getData();
        if (null == data) {
            mMainPresenter.getUserInfo();
            return;
        }
        renew = data.getRenew();
        if (1 == renew) {
            mMainPresenter.downloadApp(appUpdate);
        }
        mMainPresenter.getUserInfo();
    }

    /**
     * 在获取用户信息成功时回调
     *
     * @param userInfo 用户信息
     */
    @Override
    public void onGetUserInfoSuccess(UserInfo userInfo) {
        if (CODE_SUCCEED != userInfo.getCode()) {
            return;
        }
        /**
         * 发送数据到{@link HomeFragment#onGetUserInfoSuccess(UserInfo)}
         * 发送数据到{@link QuotationPurchaseFragment#onGetUserInfoSuccess(UserInfo)}
         * 发送数据到{@link MyFragment#onGetUserInfoSuccess(UserInfo)}
         * 使其更新部分UI
         */
        EventBus.getDefault().post(userInfo);
        UserInfo.DataBean data = userInfo.getData();
        if (null == data) {
            return;
        }
        // 会员等级(1体验2专业3终生旗舰)
        int vipLevel = data.getVip_level();
        if (0 == renew && 1 == vipLevel) {
            mMainPresenter.displayUpdateVipPopupWindow(mRootLayout);
        }
    }

    /**
     * 在获取系统参数成功时回调
     *
     * @param systemParameter 系统参数
     */
    @Override
    public void onGetSystemParameterSuccess(SystemParameter systemParameter) {
        if (CODE_SUCCEED == systemParameter.getCode()) {
            YiBaiApplication.setSystemParameter(systemParameter);
            /**
             * 发送数据到{@link SceneActivity#getSystemParameterSuccess(SystemParameter)}
             * 使其初始化圆形浮动菜单
             * 发送数据到{@link ProductFragment#getSystemParameterSuccess(SystemParameter)}
             * 使其初始化产品使用状态Fragment
             */
            EventBus.getDefault().postSticky(systemParameter);
        }
    }

    /**
     * 在获取产品筛选参数成功时回调
     * (问: 为什么在MainActivity界面就需要获取产品筛选参数。答: 1,为了防止用户在场景编辑界面点击搭配按钮时,长按植物or盆器
     * 时点击筛选按钮进入筛选界面时,因为产品筛选参数集合为null导致筛选界面空指针异常的错误。2,如果在
     * {@link ProductUseStateFragment#onGetProductScreeningParamSuccess(ProductScreeningParam)}),界面设置
     * MyApplication.setProductScreeningParamList(dataList);就会导致多次赋值,可能致使上一个界面的筛选条件清空了的错误
     *
     * @param productScreeningParam 获取产品筛选参数时服务器端返回的数据
     */
    @Override
    public void onGetProductScreeningParamSuccess(ProductScreeningParam productScreeningParam) {
        if (CODE_SUCCEED != productScreeningParam.getCode()) {
            return;
        }
        List<ProductScreeningParam.DataBean> dataList = productScreeningParam.getData();
        if (null == dataList || dataList.size() == 0) {
            return;
        }
        YiBaiApplication.setProductScreeningParamList(dataList);
    }

    /**
     * EventBus
     * 接收用户从{@link QuotationPurchaseFragment#onPageSelected(int)}
     * 发送过来的数据(在功能被切换时)
     *
     * @param functionSwitch 数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFunctionSwitchChanged(FunctionSwitch functionSwitch) {
        String name = functionSwitch.getName();
        if (!TextUtils.isEmpty(name)) {
            mQuotationRadioButton.setText(name);
        }
    }

    /**
     * EventBus
     * 接收用户从{@link HomeFragment#onClick(View)} 传递过来的数据
     * 接收用户从{@link QuotationPurchaseFragment} 传递过来的数据
     *
     * @param toFragment 用于在其他界面跳转到首页的某一个Fragment的标记
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ratioActivitySendData(ToFragment toFragment) {
        int index = toFragment.getIndex();
        if (2 == index) {
            // 显示"产品"界面
            mProductRadioButton.setChecked(true);
        } else if (3 == index) {
            // 显示"报价"界面
            mQuotationRadioButton.setChecked(true);
        }
    }

    @Override
    public void onShowLoading() {

    }

    @Override
    public void onHideLoading() {

    }

    @Override
    public void onLoadDataFailure(Throwable throwable) {

    }

    @Override
    public void onBackPressed() {
        // 当popupWindow 正在展示的时候 按下返回键 关闭popupWindow 否则关闭activity
        if (null != mPopupMenu && mPopupMenu.isShowing()) {
            mPopupMenu.closePopupWindow();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mMainPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            // 注销下载广播接收器
            DownloadApkUtil.unregisterBroadcast(this);
            mMainPresenter.onDetachView();
            mMainPresenter = null;
        }
    }
}
