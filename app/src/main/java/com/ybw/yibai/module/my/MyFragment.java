package com.ybw.yibai.module.my;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.UserInfo;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.browser.BrowserActivity;
import com.ybw.yibai.module.collection.CollectionLayoutActivity;
import com.ybw.yibai.module.customerlist.CustomerListActivity;
import com.ybw.yibai.module.invitefriends.InviteFriendsActivity;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.purchaseorder.PurchaseOrderActivity;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsActivity;
import com.ybw.yibai.module.quotationset.QuotationSetActivity;
import com.ybw.yibai.module.setting.SettingActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.Context.MODE_PRIVATE;
import static com.ybw.yibai.common.constants.Preferences.ACTIVITY_INVITE_BG;
import static com.ybw.yibai.common.constants.Preferences.CITY_NAME;
import static com.ybw.yibai.common.constants.Preferences.COMPANY;
import static com.ybw.yibai.common.constants.Preferences.HEAD;
import static com.ybw.yibai.common.constants.Preferences.MY_WALLET_URL;
import static com.ybw.yibai.common.constants.Preferences.NICK_NAME;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.TYPE;
import static com.ybw.yibai.common.constants.Preferences.URL;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.USER_VIP_URL;
import static com.ybw.yibai.common.constants.Preferences.VIP_LEVEL;
import static com.ybw.yibai.common.constants.Preferences.WALLET_URL_TYPE;
import static com.ybw.yibai.common.utils.OtherUtil.createBarView;

/**
 * 我的Fragment
 *
 * @author sjl
 * @date 2019/9/5
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 用户升级vip页面地址
     */
    private String userVipUrl;

    /**
     * 我的钱包地址
     */
    private String myWalletUrl;

    /**
     * 一个和"状态栏"一样高的view
     */
    private View mView;

    /**
     * 设置
     */
    private ImageView mSettingImageView;

    /**
     * 头部布局
     */
    private RelativeLayout mHeadLayout;

    /**
     * 用户头像
     */
    private ImageView mHeadPortraitImageView;

    /**
     * VIP等级
     */
    private ImageView mVipLevelIcoImageView;

    /**
     * 用户名称
     */
    private TextView mUserNameTextView;

    /**
     * 公司名称
     */
    private TextView mCompanyTextView;

    /**
     * 立即升级
     */
    private TextView mUpgradeNowTextView;

    /**
     * 活动图片
     */
    private ImageView mActivitiesImageView;

    /**
     * 待确定
     */
    private TextView mToBeDecidedTextView;

    /**/

    /**
     * 已成交
     */
    private TextView mDealDoneTextView;

    /**
     * 已失效
     */
    private TextView mExpiredTextView;
    /**
     * 收藏
     */
    private TextView mCollectionLayout;

    /**
     * 全部报价
     */
    private RelativeLayout mQuotationLayout;

    /**
     * 报价设置
     */
    private TextView mQuotationSetTextView;

    /**
     * 客户管理
     */
    private TextView mCustomerManagementTextView;

    /**/

    /**
     * 全部已进货订单
     */
    private RelativeLayout mAllOrdersLayout;

    /**
     * 待付款
     */
    private TextView mPendingPaymentTextView;

    /**
     * 待配送
     */
    private TextView mPendingDeliveryTextView;

    /**
     * 待收货
     */
    private TextView mPendingReceiptTextView;

    /**
     * 已完成
     */
    private TextView mCompletedTextView;

    /**/

    /**
     * 余额
     */
    private LinearLayout mBalanceLayout;

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
    private FragmentActivity mActivity;

    /**
     *
     */
    private SharedPreferences mSharedPreferences;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    @Override
    protected int setLayouts() {
        mContext = getContext();
        mActivity = getActivity();
        return R.layout.fragment_my;
    }

    @Override
    protected void findViews(View view) {
        mView = view.findViewById(R.id.barView);
        mSettingImageView = view.findViewById(R.id.settingImageView);
        mHeadLayout = view.findViewById(R.id.headLayout);
        mHeadPortraitImageView = view.findViewById(R.id.headPortraitImageView);
        mVipLevelIcoImageView = view.findViewById(R.id.vipLevelIcoImageView);
        mUserNameTextView = view.findViewById(R.id.userNameTextView);
        mCompanyTextView = view.findViewById(R.id.companyTextView);
        mUpgradeNowTextView = view.findViewById(R.id.upgradeNowTextView);

        mActivitiesImageView = view.findViewById(R.id.activitiesImageView);

        mToBeDecidedTextView = view.findViewById(R.id.toBeDecidedTextView);
        mDealDoneTextView = view.findViewById(R.id.dealDoneTextView);
        mCollectionLayout = view.findViewById(R.id.collectionLayout);
        mExpiredTextView = view.findViewById(R.id.expiredTextView);
        mQuotationLayout = view.findViewById(R.id.quotationLayout);
        mQuotationSetTextView = view.findViewById(R.id.quotationSetTextView);
        mCustomerManagementTextView = view.findViewById(R.id.customerManagementTextView);

        mAllOrdersLayout = view.findViewById(R.id.allOrdersLayout);
        mPendingPaymentTextView = view.findViewById(R.id.pendingPaymentTextView);
        mPendingDeliveryTextView = view.findViewById(R.id.pendingDeliveryTextView);
        mPendingReceiptTextView = view.findViewById(R.id.pendingReceiptTextView);
        mCompletedTextView = view.findViewById(R.id.completedTextView);

        mBalanceLayout = view.findViewById(R.id.balanceLayout);
    }

    @Override
    protected void initView() {
        createBarView(mActivity, mView);
    }

    @Override
    protected void initData() {
        mSharedPreferences = mContext.getSharedPreferences(USER_INFO, MODE_PRIVATE);
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mSettingImageView.setOnClickListener(this);
        mHeadLayout.setOnClickListener(this);

        mActivitiesImageView.setOnClickListener(this);

        mToBeDecidedTextView.setOnClickListener(this);
        mDealDoneTextView.setOnClickListener(this);
        mExpiredTextView.setOnClickListener(this);
        mQuotationLayout.setOnClickListener(this);
        mQuotationSetTextView.setOnClickListener(this);
        mCustomerManagementTextView.setOnClickListener(this);

        mAllOrdersLayout.setOnClickListener(this);
        mPendingPaymentTextView.setOnClickListener(this);
        mPendingDeliveryTextView.setOnClickListener(this);
        mPendingReceiptTextView.setOnClickListener(this);
        mCompletedTextView.setOnClickListener(this);
        mCollectionLayout.setOnClickListener(this);

        mBalanceLayout.setOnClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 设置
        if (id == R.id.settingImageView) {
            Intent intent = new Intent(mContext, SettingActivity.class);
            startActivity(intent);
        }

        // 头部布局
        if (id == R.id.headLayout) {
            if (TextUtils.isEmpty(userVipUrl)) {
                return;
            }
            Intent intent = new Intent(mContext, BrowserActivity.class);
            intent.putExtra(URL, userVipUrl);
            startActivity(intent);
        }

        // 活动图片
        if (id == R.id.activitiesImageView) {
            Intent intent = new Intent(mContext, InviteFriendsActivity.class);
            startActivity(intent);
        }

        // 待确定
        if (id == R.id.toBeDecidedTextView) {
            Intent intent = new Intent(mContext, QuotationDetailsActivity.class);
            intent.putExtra(POSITION, 1);
            startActivity(intent);
        }

        // 已成交
        if (id == R.id.dealDoneTextView) {
            Intent intent = new Intent(mContext, QuotationDetailsActivity.class);
            intent.putExtra(POSITION, 2);
            startActivity(intent);
        }

        // 已失效
        if (id == R.id.expiredTextView) {
            Intent intent = new Intent(mContext, QuotationDetailsActivity.class);
            intent.putExtra(POSITION, 3);
            startActivity(intent);
        }

        // 全部报价
        if (id == R.id.quotationLayout) {
            Intent intent = new Intent(mContext, QuotationDetailsActivity.class);
            intent.putExtra(POSITION, 0);
            startActivity(intent);
        }

        // 报价设置
        if (id == R.id.quotationSetTextView) {
            Intent intent = new Intent(mContext, QuotationSetActivity.class);
            startActivity(intent);
        }

        // 客户管理
        if (id == R.id.customerManagementTextView) {
            Intent intent = new Intent(mContext, CustomerListActivity.class);
            startActivity(intent);
        }

        // 已进货订单
        if (id == R.id.allOrdersLayout) {
            Intent intent = new Intent(mContext, PurchaseOrderActivity.class);
            intent.putExtra(POSITION, 0);
            startActivity(intent);
        }

        // 待付款
        if (id == R.id.pendingPaymentTextView) {
            Intent intent = new Intent(mContext, PurchaseOrderActivity.class);
            intent.putExtra(POSITION, 1);
            startActivity(intent);
        }

        // 待配送
        if (id == R.id.pendingDeliveryTextView) {
            Intent intent = new Intent(mContext, PurchaseOrderActivity.class);
            intent.putExtra(POSITION, 2);
            startActivity(intent);
        }

        // 待收货
        if (id == R.id.pendingReceiptTextView) {
            Intent intent = new Intent(mContext, PurchaseOrderActivity.class);
            intent.putExtra(POSITION, 3);
            startActivity(intent);
        }

        // 已完成
        if (id == R.id.completedTextView) {
            Intent intent = new Intent(mContext, PurchaseOrderActivity.class);
            intent.putExtra(POSITION, 4);
            startActivity(intent);
        }

        // 收藏
        if (id == R.id.collectionLayout) {
            Intent intent = new Intent(mContext, CollectionLayoutActivity.class);
            startActivity(intent);
        }

        // 余额
        if (id == R.id.balanceLayout) {
            if (TextUtils.isEmpty(myWalletUrl)) {
                return;
            }
            Intent intent = new Intent(mContext, BrowserActivity.class);
            intent.putExtra(TYPE, WALLET_URL_TYPE);
            intent.putExtra(URL, myWalletUrl);
            startActivity(intent);
        }


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
        getAndSetUserInfo();
    }

    /**
     * 获取并且设置用户信息到UI
     */
    private void getAndSetUserInfo() {
        String headPortrait = mSharedPreferences.getString(HEAD, null);
        int vipLevel = mSharedPreferences.getInt(VIP_LEVEL, 0);
        String cityName = mSharedPreferences.getString(CITY_NAME, "全国");
        String nickName = mSharedPreferences.getString(NICK_NAME, null);
        String company = mSharedPreferences.getString(COMPANY, null);
        String activityInviteBg = mSharedPreferences.getString(ACTIVITY_INVITE_BG, null);
        userVipUrl = mSharedPreferences.getString(USER_VIP_URL, null);
        myWalletUrl = mSharedPreferences.getString(MY_WALLET_URL, null);

        if (!TextUtils.isEmpty(headPortrait)) {
            ImageUtil.displayRoundImage(mContext, mHeadPortraitImageView, headPortrait);
        }
        if (1 == vipLevel) {
            mUpgradeNowTextView.setVisibility(View.VISIBLE);
            mVipLevelIcoImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.level_experience_version));
        } else if (2 == vipLevel) {
            mUpgradeNowTextView.setVisibility(View.GONE);
            mVipLevelIcoImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.level_vip_version));
        } else if (3 == vipLevel) {
            mUpgradeNowTextView.setVisibility(View.GONE);
            mVipLevelIcoImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.level_flagship_version));
        }
        if (!TextUtils.isEmpty(nickName)) {
            mUserNameTextView.setText(nickName);
        }
        if (!TextUtils.isEmpty(company)) {
            mCompanyTextView.setText(company);
        }
        if (!TextUtils.isEmpty(activityInviteBg)) {
            ImageUtil.displayImage(mContext, mActivitiesImageView, activityInviteBg);
        }
    }

    /**
     * 友盟统计Fragment页面
     */
    @Override
    public void onResume() {
        super.onResume();
        getAndSetUserInfo();
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
        if (EventBus.getDefault().isRegistered(this)) {
            // 解除注册
            EventBus.getDefault().unregister(this);
        }
    }
}
