package com.ybw.yibai.module.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.BindingWechat;
import com.ybw.yibai.common.bean.SearchRecord;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.StackManagerUtil;
import com.ybw.yibai.common.widget.CustomDialog;
import com.ybw.yibai.module.login.AccountLoginActivity;
import com.ybw.yibai.module.setting.SettingContract.CallBack;
import com.ybw.yibai.module.setting.SettingContract.SettingModel;
import com.ybw.yibai.module.setting.SettingContract.SettingPresenter;
import com.ybw.yibai.module.setting.SettingContract.SettingView;

import org.xutils.ex.DbException;

import static android.content.Context.MODE_PRIVATE;
import static com.ybw.yibai.common.constants.Preferences.ACTIVITY_INVITE_BG;
import static com.ybw.yibai.common.constants.Preferences.ADDRESS;
import static com.ybw.yibai.common.constants.Preferences.COMPANY;
import static com.ybw.yibai.common.constants.Preferences.COMPANY_DETAILS;
import static com.ybw.yibai.common.constants.Preferences.COMPANY_DETAILS_PIC;
import static com.ybw.yibai.common.constants.Preferences.COMPANY_LOGO;
import static com.ybw.yibai.common.constants.Preferences.CURRENCY_SYMBOL;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_ID;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_LOGO;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_MANE;
import static com.ybw.yibai.common.constants.Preferences.EMAIL;
import static com.ybw.yibai.common.constants.Preferences.HEAD;
import static com.ybw.yibai.common.constants.Preferences.INCREASE_RENT;
import static com.ybw.yibai.common.constants.Preferences.INCREASE_SELL;
import static com.ybw.yibai.common.constants.Preferences.INVITE_BG;
import static com.ybw.yibai.common.constants.Preferences.INVITE_RULE_BG;
import static com.ybw.yibai.common.constants.Preferences.INVITE_URL;
import static com.ybw.yibai.common.constants.Preferences.IS_BIND_WX;
import static com.ybw.yibai.common.constants.Preferences.MY_WALLET_URL;
import static com.ybw.yibai.common.constants.Preferences.NICK_NAME;
import static com.ybw.yibai.common.constants.Preferences.OPEN_WATERMARK;
import static com.ybw.yibai.common.constants.Preferences.RECORD_URL;
import static com.ybw.yibai.common.constants.Preferences.ROLE_NAME;
import static com.ybw.yibai.common.constants.Preferences.TAKE_CASE_URL;
import static com.ybw.yibai.common.constants.Preferences.TELEPHOTO;
import static com.ybw.yibai.common.constants.Preferences.TOKEN;
import static com.ybw.yibai.common.constants.Preferences.TRUE_NAME;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.USER_NAME;
import static com.ybw.yibai.common.constants.Preferences.USER_VIP_URL;
import static com.ybw.yibai.common.constants.Preferences.VIP_LEVEL;
import static com.ybw.yibai.common.constants.Preferences.WATERMARK_PIC;

/**
 * 设置界面Presenter实现类
 *
 * @author sjl
 * @date 2019/10/21
 */
public class SettingPresenterImpl extends BasePresenterImpl<SettingView> implements SettingPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private SettingView mSettingView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private SettingModel mSettingModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public SettingPresenterImpl(SettingView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mSettingView = getView();
        mSettingModel = new SettingModelImpl();
    }

    /**
     * 绑定微信
     *
     * @param code 微信给的code
     */
    @Override
    public void bindingWechat(String code) {
        mSettingModel.bindingWechat(code, this);
    }

    /**
     * 在绑定微信成功时回调
     *
     * @param bindingWechat 在绑定微信成功时服务器端返回的数据
     */
    @Override
    public void onBindingWechatSuccess(BindingWechat bindingWechat) {
        mSettingView.onBindingWechatSuccess(bindingWechat);
    }

    /**
     * 显示切换账号的Dialog
     */
    @Override
    public void displaySwitchAccountDialog() {
        Activity activity = ((Activity) mSettingView);
        if (null != activity) {
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            CustomDialog customDialog = new CustomDialog(activity);
            customDialog.setMessage(activity.getResources().getString(R.string.are_you_sure_to_exit_the_current_account));
            customDialog.setYesOnclickListener(activity.getResources().getString(R.string.determine),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                        exit(activity);
                    });
            customDialog.setNoOnclickListener(activity.getResources().getString(R.string.cancel),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                    });
            customDialog.show();
        }
    }

    /**
     * 退出当前账号
     */
    private void exit(Activity activity) {
        // 重新初始化为0,恢复到用户没有选择报价客户的状态
        YiBaiApplication.setCid(0);
        // 重新初始化为false,恢复验证查看密码没有通过的默认状态
        YiBaiApplication.setAuthorization(false);

        SharedPreferences sharedPreferences = activity.getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(USER_NAME, null);
        edit.putString(NICK_NAME, null);
        edit.putString(TRUE_NAME, null);
        edit.putString(ROLE_NAME, null);
        edit.putString(TELEPHOTO, null);
        edit.putString(HEAD, null);
        edit.putString(EMAIL, null);
        edit.putString(ADDRESS, null);
        edit.putString(COMPANY, null);
        edit.putString(COMPANY_LOGO, null);
        edit.putString(COMPANY_DETAILS, null);
        edit.putString(COMPANY_DETAILS_PIC, null);
        edit.putInt(IS_BIND_WX, 0);
        edit.putInt(VIP_LEVEL, 0);

        edit.putInt(INCREASE_RENT, 0);
        edit.putInt(INCREASE_SELL, 0);
        edit.putString(CURRENCY_SYMBOL, null);

        edit.putString(INVITE_BG, null);
        edit.putString(INVITE_RULE_BG, null);
        edit.putString(ACTIVITY_INVITE_BG, null);

        edit.putString(INVITE_URL, null);
        edit.putString(RECORD_URL, null);
        edit.putString(USER_VIP_URL, null);
        edit.putString(MY_WALLET_URL, null);
        edit.putString(TAKE_CASE_URL, null);
        edit.putString(WATERMARK_PIC, null);
        edit.putString(TOKEN, null);

        edit.putBoolean(OPEN_WATERMARK, true);

        edit.putInt(CUSTOMERS_ID, 0);
        edit.putString(CUSTOMERS_LOGO, null);
        edit.putString(CUSTOMERS_MANE, null);

        edit.apply();

        // 删除搜索记录
        try {
            YiBaiApplication.getDbManager().delete(SearchRecord.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(() -> {
            // 友盟统计退出登录
            MobclickAgent.onProfileSignOff();
            Intent intent = new Intent(activity, AccountLoginActivity.class);
            activity.startActivity(intent);
            StackManagerUtil.getInstance().finishAllActivity();
        }, 50);
    }
}