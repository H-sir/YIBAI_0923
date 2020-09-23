package com.ybw.yibai.module.quotationset;

import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SetIncrease;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.quotationset.QuotationSetContract.QuotationSetPresenter;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.INCREASE_RENT;
import static com.ybw.yibai.common.constants.Preferences.INCREASE_SELL;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * 报价设置
 *
 * @author sjl
 * @date 2019/12/2
 */
public class QuotationSetActivity extends BaseActivity implements View.OnClickListener, QuotationSetContract.QuotationSetView {

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 月租价增幅比例
     */
    private EditText mMonthRentEditText;

    /**
     * 零售价增幅比例
     */
    private EditText mRetailPriceEditText;

    /**
     * 更新
     */
    private Button mUpdateButton;

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
     *
     */
    private SharedPreferences mSharedPreferences;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private QuotationSetPresenter mQuotationSetPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_quotation_set;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mMonthRentEditText = findViewById(R.id.monthRentEditText);
        mRetailPriceEditText = findViewById(R.id.retailPriceEditText);
        mUpdateButton = findViewById(R.id.updateButton);

        mWaitDialog = new WaitDialog(this);
        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mSharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        int increaseRent = mSharedPreferences.getInt(INCREASE_RENT, 0);
        int increaseSell = mSharedPreferences.getInt(INCREASE_SELL, 0);

        mMonthRentEditText.setText(String.valueOf(increaseRent));
        mMonthRentEditText.setSelection(mMonthRentEditText.getText().toString().length());
        mRetailPriceEditText.setText(String.valueOf(increaseSell));
        mRetailPriceEditText.setSelection(mRetailPriceEditText.getText().toString().length());
    }

    @Override
    protected void initEvent() {
        mQuotationSetPresenter = new QuotationSetPresenterImpl(this);
        mBackImageView.setOnClickListener(this);
        mUpdateButton.setOnClickListener(this);
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

        // 更新
        if (id == R.id.updateButton) {
            String increaseRent = mMonthRentEditText.getText().toString().trim();
            String increaseSell = mRetailPriceEditText.getText().toString().trim();
            mQuotationSetPresenter.setIncrease(increaseRent, increaseSell);
        }
    }

    /**
     * 在设置设置价格增幅成功时回调
     *
     * @param setIncrease 设置价格增幅时服务器端返回的数据
     */
    @Override
    public void onSetIncreaseSuccess(SetIncrease setIncrease) {
        MessageUtil.showMessage(setIncrease.getMsg());
        if (CODE_SUCCEED == setIncrease.getCode()) {
            SharedPreferences.Editor edit = mSharedPreferences.edit();
            String increaseRent = mMonthRentEditText.getText().toString().trim();
            String increaseSell = mRetailPriceEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(increaseRent)) {
                edit.putInt(INCREASE_RENT, Integer.parseInt(increaseRent));
            }
            if (!TextUtils.isEmpty(increaseSell)) {
                edit.putInt(INCREASE_SELL, Integer.parseInt(increaseSell));
            }
            edit.apply();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mQuotationSetPresenter) {
            mQuotationSetPresenter.onDetachView();
            mQuotationSetPresenter = null;
        }
    }
}
