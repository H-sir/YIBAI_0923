package com.ybw.yibai.module.market;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SkuMarketBean;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.nestlistview.NestFullListView;
import com.ybw.yibai.common.widget.nestlistview.NestFullListViewAdapter;
import com.ybw.yibai.common.widget.nestlistview.NestFullViewHolder;
import com.ybw.yibai.module.browser.BrowserActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ybw.yibai.common.constants.Preferences.CUSTOMER_NAME;
import static com.ybw.yibai.common.constants.Preferences.GYS_DETAILS_URL_TYPE;
import static com.ybw.yibai.common.constants.Preferences.ORDER_NUMBER;
import static com.ybw.yibai.common.constants.Preferences.ORDER_SHARE_URL_TYPE;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ADDORSELECT;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ID;
import static com.ybw.yibai.common.constants.Preferences.TYPE;
import static com.ybw.yibai.common.constants.Preferences.URL;

/**
 * 多货源信息
 */
public class MarketActivity extends BaseActivity implements MarketContract.MarketView {

    //    @BindView(R.id.marketProduct)
//    TextView marketProduct;
//    @BindView(R.id.marketMonthRent)
//    TextView marketMonthRent;
//    @BindView(R.id.marketPrice)
//    TextView marketPrice;
    @BindView(R.id.marketListView)
    NestFullListView marketListView;
    @BindView(R.id.rootLayout)
    LinearLayout rootLayout;

    private MarketContract.MarketPresenter mMarketPresenter;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /**
     * 多货源集合
     */
    private List<SkuMarketBean.DataBean.GateInfoBean> gateInfoBeanList = new ArrayList<>();
    /**
     * 多货源列表适配器
     */
    private NestFullListViewAdapter mNestFullListViewAdapter;

    @Override
    protected int setLayout() {
        return R.layout.activity_market_layout;
    }

    @Override
    protected void initView() {

    }

    private boolean addOrSelect = true;
    private int productSkuId = 0;

    @Override
    protected void initData() {
        mMarketPresenter = new MarketPresenterImpl(this);
        mWaitDialog = new WaitDialog(this);
        Intent intent = getIntent();
        if (null != intent) {
            productSkuId = intent.getIntExtra(PRODUCT_SKU_ID, 0);
            addOrSelect = intent.getBooleanExtra(PRODUCT_SKU_ADDORSELECT, true);
            mMarketPresenter.getSkuMarket(productSkuId);
        }
    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void onGetSkuMarketSuccess(SkuMarketBean skuMarketBean) {
//        if (skuMarketBean.getData().getProductName() != null)
//            marketProduct.setText(skuMarketBean.getData().getProductName());
//        if (skuMarketBean.getData().getSelfInfo().getMonthRent() != null)
//            marketMonthRent.setText(skuMarketBean.getData().getSelfInfo().getMonthRent());
//        if (skuMarketBean.getData().getSelfInfo().getPrice() != null)
//            marketPrice.setText(skuMarketBean.getData().getSelfInfo().getPrice());
        if (skuMarketBean.getData().getGateInfo() != null)
            gateInfoBeanList.addAll(skuMarketBean.getData().getGateInfo());

        mNestFullListViewAdapter = new NestFullListViewAdapter<SkuMarketBean.DataBean.GateInfoBean>(
                R.layout.listview_market_list_item_layout, gateInfoBeanList) {
            @Override
            public void onBind(int pos, SkuMarketBean.DataBean.GateInfoBean gateInfoBean, NestFullViewHolder holder) {
                Log.d("NullListView", "嵌套第一层ScrollView onBind() called with: pos = [" + pos + "], testBean = [" + gateInfoBean + "], v = [" + holder + "]");
                LinearLayout gysDetails = holder.getView(R.id.gysDetails);//点击条状供应商详情
                ImageView gysPhoto = holder.getView(R.id.gysPhoto);//供应商头像

                ImageUtil.displayImage(getApplicationContext(), gysPhoto, gateInfoBean.getGateLogo());
                TextView gateName = holder.getView(R.id.gateName);//平台
                TextView gateAdd = holder.getView(R.id.gateAdd);//描述
                gateName.setText(gateInfoBean.getGateName());
                gateAdd.setText(gateInfoBean.getGateAdd());

                gysDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (gateInfoBean.getGateInfoUrl() != null && !gateInfoBean.getGateInfoUrl().isEmpty()) {
                            Intent intent = new Intent(MarketActivity.this, BrowserActivity.class);
                            intent.putExtra(URL, gateInfoBean.getGateInfoUrl());
                            intent.putExtra(TYPE, GYS_DETAILS_URL_TYPE);
                            startActivity(intent);
                        }
                    }
                });

                //第二层
                NestFullListView view = (NestFullListView) holder.getView(R.id.gateSkuListView);
                view.setOrientation(LinearLayout.VERTICAL);
                view.setAdapter(new NestFullListViewAdapter<SkuMarketBean.DataBean.GateInfoBean.GateSkuBean>
                        (R.layout.listview_market_list_sku_item_layout,
                                gateInfoBean.getGateSku()) {
                    @Override
                    public void onBind(int pos, SkuMarketBean.DataBean.GateInfoBean.GateSkuBean gateSkuBean, NestFullViewHolder holder) {
                        View gateNameSelectImgView = holder.getView(R.id.gateNameSelectImgView);
                        TextView addGateName = holder.getView(R.id.addGateName);
                        TextView tradePrice = holder.getView(R.id.tradePrice);
                        TextView gateNameText = holder.getView(R.id.gateNameText);
                        TextView gateNameSelect = holder.getView(R.id.gateNameSelect);
                        ImageView gateNameSelectImg = holder.getView(R.id.gateNameSelectImg);
                        TextView stockMarket = holder.getView(R.id.stockMarket);
                        TextView deliveryMessage = holder.getView(R.id.deliveryMessage);
                        TextView uptime = holder.getView(R.id.uptime);

                        if (addOrSelect) {
                            addGateName.setVisibility(View.VISIBLE);
                            gateNameSelectImgView.setVisibility(View.GONE);
                        } else {
                            addGateName.setVisibility(View.GONE);
                            gateNameSelectImgView.setVisibility(View.VISIBLE);
                        }

                        deliveryMessage.setText(gateSkuBean.getDelivery());
                        tradePrice.setText("￥" + String.valueOf(gateSkuBean.getPrice()));
                        gateNameText.setText(gateSkuBean.getSubtitle());
                        stockMarket.setText(String.valueOf(gateSkuBean.getStock()));
                        uptime.setText(gateSkuBean.getUptime());
                        gateNameSelect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gateSkuBean.setSelect(!gateSkuBean.isSelect());
                                if (gateSkuBean.isSelect()) {
                                    gateNameSelect.setText("已选用");
                                    gateNameSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.selected_img));
                                    mMarketPresenter.addPurcart(productSkuId, gateSkuBean.getGateProductId());
                                } else {
                                    gateNameSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.un_selected_img));
                                    gateNameSelect.setText("未选用");

                                }
                            }
                        });

                        if (gateSkuBean.isSelect()) {
                            gateNameSelect.setText("已选用");
                            gateNameSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.selected_img));
                        } else {
                            gateNameSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.un_selected_img));
                            gateNameSelect.setText("未选用");
                        }
                        addGateName.setOnClickListener(view1 -> {
                            mMarketPresenter.addPurcart(productSkuId, gateSkuBean.getGateProductId());
                        });
                        ((NestFullListView) holder.getView(R.id.gateSkuPicListView)).setAdapter(
                                new NestFullListViewAdapter<String>(R.layout.listview_market_list_pic_item_layout, gateSkuBean.getPicArr()) {
                                    @Override
                                    public void onBind(int pos, String pic, NestFullViewHolder holder) {
                                        ImageView imagePic = holder.getView(R.id.imagePic);
                                        if (pic != null && !pic.isEmpty())
                                            ImageUtil.displayImage(getApplicationContext(), imagePic, pic);
                                        imagePic.setOnClickListener(view1 -> {
                                            ImageUtil.showImage(MarketActivity.this, gateSkuBean.getPicArr(), pos);
                                        });
                                    }
                                }
                        );
                    }
                });
            }
        };
        marketListView.setAdapter(mNestFullListViewAdapter);
    }

    @Override
    public void onAddPurcartSuccess() {
        MessageUtil.showMessage("加入完成");
        onFinish();
    }

    private void onFinish() {
        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
        intent.putExtra("data", "refresh");
        LocalBroadcastManager.getInstance(MarketActivity.this).sendBroadcast(intent);
        sendBroadcast(intent);
        finish();

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
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mMarketPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mMarketPresenter.onDetachView();
            mMarketPresenter = null;
        }
    }


    @OnClick({R.id.backImageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                onBackPressed();
                break;
        }
    }
}
