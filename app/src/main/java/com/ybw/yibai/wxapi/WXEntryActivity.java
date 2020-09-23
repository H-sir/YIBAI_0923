package com.ybw.yibai.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.module.login.AccountLoginActivity;
import com.ybw.yibai.module.login.PhoneLoginActivity;
import com.ybw.yibai.module.setting.SettingActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 微信客户端回调Activity示例
 *
 * @author sjl
 * @date 2019/10/17
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private String TAG = "WXEntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        try {
            IWXAPI iwxapi = YiBaiApplication.getIWXAPI();
            Intent intent = this.getIntent();
            iwxapi.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // 必须调用此句话
        IWXAPI iwxapi = YiBaiApplication.getIWXAPI();
        iwxapi.handleIntent(intent, this);
    }

    /**
     * 微信发送的请求将回调到onReq方法
     */
    @Override
    public void onReq(BaseReq baseReq) {

    }

    /**
     * 发送到微信请求的响应结果将回调到onResp方法
     */
    @Override
    public void onResp(BaseResp baseResp) {
        /**
         * 发送数据到{@link AccountLoginActivity#weChatEntryActivitySendData(BaseResp)}
         * 发送数据到{@link PhoneLoginActivity#weChatEntryActivitySendData(BaseResp)}
         * 发送数据到{@link SettingActivity#weChatEntryActivitySendData(BaseResp)}
         */
        EventBus.getDefault().postSticky(baseResp);
        finish();
    }
}
