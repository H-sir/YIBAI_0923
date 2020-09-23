package com.ybw.yibai.module.watermarksetting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.UpdateWatermark;
import com.ybw.yibai.common.bean.UpdateWatermark.DataBean;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.watermarksetting.WatermarkSettingContract.WatermarkSettingPresenter;
import com.ybw.yibai.module.watermarksetting.WatermarkSettingContract.WatermarkSettingView;

import java.io.File;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_PHOTOS_CODE;
import static com.ybw.yibai.common.constants.Preferences.OPEN_WATERMARK;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.VIP_LEVEL;
import static com.ybw.yibai.common.constants.Preferences.WATERMARK_PIC;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 水印设置
 *
 * @author sjl
 * @date 2019/10/24
 */
public class WatermarkSettingActivity extends BaseActivity implements WatermarkSettingView,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private String watermarkPic;

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 显示水印图片
     */
    private ImageView mWatermarkImageView;

    /**
     * 开启/关闭水印ToggleButton
     */
    private ToggleButton mToggleButton;

    /**
     * 上传水印
     */
    private TextView mUploadingWatermarkTextView;

    /**
     * 体验版本说明
     */
    private TextView mTrialVersionDescription;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /**
     *
     */
    private SharedPreferences mPreferences;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private WatermarkSettingPresenter mWatermarkSettingPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_watermark_setting;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mWatermarkImageView = findViewById(R.id.watermarkImageView);
        mToggleButton = findViewById(R.id.toggleButton);
        mUploadingWatermarkTextView = findViewById(R.id.uploadingWatermarkTextView);
        mTrialVersionDescription = findViewById(R.id.trialVersionDescription);

        mWaitDialog = new WaitDialog(this);
        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        int vipLevel = mPreferences.getInt(VIP_LEVEL, 0);
        boolean openWatermark = mPreferences.getBoolean(OPEN_WATERMARK, true);
        watermarkPic = mPreferences.getString(WATERMARK_PIC, null);
        if (1 == vipLevel) {
            mToggleButton.setClickable(false);
            mWatermarkImageView.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.logo));
        } else {
            if (openWatermark) {
                mWatermarkImageView.setVisibility(View.VISIBLE);
            } else {
                mWatermarkImageView.setVisibility(View.INVISIBLE);
            }
            if (!TextUtils.isEmpty(watermarkPic)) {
                ImageUtil.displayImage(this, mWatermarkImageView, watermarkPic);
            } else {
                mWatermarkImageView.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.logo));
            }
            mTrialVersionDescription.setVisibility(View.INVISIBLE);
        }
        mToggleButton.setChecked(openWatermark);
    }

    @Override
    protected void initEvent() {
        mWatermarkSettingPresenter = new WatermarkSettingPresenterImpl(this);
        mBackImageView.setOnClickListener(this);
        mUploadingWatermarkTextView.setOnClickListener(this);
        mToggleButton.setOnCheckedChangeListener(this);
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

        // 上传水印
        if (id == R.id.uploadingWatermarkTextView) {
            // 打开相册
            ImageUtil.openPhotoAlbum(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mWatermarkImageView.setVisibility(View.VISIBLE);
        } else {
            mWatermarkImageView.setVisibility(View.INVISIBLE);
        }
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putBoolean(OPEN_WATERMARK, isChecked);
        edit.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获得系统相册Activity返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_PHOTOS_CODE) {
            Uri uri = data.getData();
            if (null == uri) {
                return;
            }
            // 根据Uri获取图片的绝对路径
            String path = ImageUtil.getRealPathFromUri(this, uri);
            if (!judeFileExists(path)) {
                return;
            }
            File file = new File(path);
            mWatermarkSettingPresenter.updateWatermark(file);
        }
    }

    /**
     * 在修改水印图片成功时回调
     *
     * @param updateWatermark 修改水印图片时服务器端返回的数据
     */
    @Override
    public void onUpdateWatermarkSuccess(UpdateWatermark updateWatermark) {
        MessageUtil.showMessage(updateWatermark.getMsg());
        if (CODE_SUCCEED != updateWatermark.getCode()) {
            return;
        }
        DataBean data = updateWatermark.getData();
        if (null == data) {
            return;
        }
        SharedPreferences.Editor edit = mPreferences.edit();
        String watermarkPic = data.getWatermark_pic();
        if (!TextUtils.isEmpty(watermarkPic)) {
            edit.putString(WATERMARK_PIC, watermarkPic);
        }
        edit.apply();
        ImageUtil.displayImage(this, mWatermarkImageView, watermarkPic);
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
        if (null != mWatermarkSettingPresenter) {
            mWatermarkSettingPresenter.onDetachView();
            mWatermarkSettingPresenter = null;
        }
    }
}
