package com.ybw.yibai.module.market;

import android.content.Intent;
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
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.nestlistview.NestFullListView;
import com.ybw.yibai.common.widget.nestlistview.NestFullListViewAdapter;
import com.ybw.yibai.common.widget.nestlistview.NestFullViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ID;

/**
 * 多货源信息
 */
public class MarketActivity extends BaseActivity implements MarketContract.MarketView {

    @BindView(R.id.marketProduct)
    TextView marketProduct;
    @BindView(R.id.marketMonthRent)
    TextView marketMonthRent;
    @BindView(R.id.marketPrice)
    TextView marketPrice;
    @BindView(R.id.marketListView)
    NestFullListView marketListView;
    @BindView(R.id.rootLayout)
    LinearLayout rootLayout;

    /**
     * 货源的SKUID
     */
    private String productSkuId;

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

    @Override
    protected void initData() {
        mMarketPresenter = new MarketPresenterImpl(this);
        mWaitDialog = new WaitDialog(this);
        Intent intent = getIntent();
        if (null != intent) {
            int productSkuId = intent.getIntExtra(PRODUCT_SKU_ID, 0);
            mMarketPresenter.getSkuMarket(productSkuId);
        }
    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void onGetSkuMarketSuccess(SkuMarketBean skuMarketBean) {
        if (skuMarketBean.getData().getProduct_name() != null)
            marketProduct.setText(skuMarketBean.getData().getProduct_name());
        if (skuMarketBean.getData().getSelf_info().getMonth_rent() != null)
            marketMonthRent.setText(skuMarketBean.getData().getSelf_info().getMonth_rent());
        if (skuMarketBean.getData().getSelf_info().getPrice() != null)
            marketPrice.setText(skuMarketBean.getData().getSelf_info().getPrice());
        if (skuMarketBean.getData().getGate_info() != null)
            gateInfoBeanList.addAll(skuMarketBean.getData().getGate_info());

        mNestFullListViewAdapter = new NestFullListViewAdapter<SkuMarketBean.DataBean.GateInfoBean>(
                R.layout.listview_market_list_item_layout, gateInfoBeanList) {
            @Override
            public void onBind(int pos, SkuMarketBean.DataBean.GateInfoBean gateInfoBean, NestFullViewHolder holder) {
                Log.d("NullListView", "嵌套第一层ScrollView onBind() called with: pos = [" + pos + "], testBean = [" + gateInfoBean + "], v = [" + holder + "]");
                TextView gateName = holder.getView(R.id.gateName);//平台
                TextView gateAdd = holder.getView(R.id.gateAdd);//描述
                gateName.setText(gateInfoBean.getGate_name());
                gateAdd.setText(gateInfoBean.getGate_add());

                //第二层
                NestFullListView view = (NestFullListView) holder.getView(R.id.gateSkuListView);
                view.setOrientation(LinearLayout.VERTICAL);
                view.setAdapter(
                        new NestFullListViewAdapter<SkuMarketBean.DataBean.GateInfoBean.GateSkuBean>
                                (R.layout.listview_market_list_sku_item_layout,
                                        gateInfoBean.getGate_sku()) {
                            @Override
                            public void onBind(int pos, SkuMarketBean.DataBean.GateInfoBean.GateSkuBean gateSkuBean, NestFullViewHolder holder) {
                                Log.d("NullListView", "嵌套第二层onBind() called with: pos = [" + pos + "], nestBean = [" + gateSkuBean.getTrade_price() + "], v = [" + holder + "]");
                                TextView tradePrice = holder.getView(R.id.tradePrice);
                                TextView gateNameText = holder.getView(R.id.gateNameText);
                                TextView gateNameSelect = holder.getView(R.id.gateNameSelect);
                                ImageView gateNameSelectImg = holder.getView(R.id.gateNameSelectImg);
                                TextView stockMarket = holder.getView(R.id.stockMarket);
                                TextView uptime = holder.getView(R.id.uptime);

                                tradePrice.setText(gateSkuBean.getTrade_price());
                                gateNameText.setText("");
                                stockMarket.setText(String.valueOf(gateSkuBean.getStock()));
                                uptime.setText(gateSkuBean.getUptime());
                                gateNameSelect.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        gateSkuBean.setSelect(!gateSkuBean.isSelect());
                                        if (gateSkuBean.isSelect()) {
                                            gateNameSelect.setText("已选用");
                                            gateNameSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.selected_img));
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
                                ((NestFullListView) holder.getView(R.id.gateSkuPicListView)).setAdapter(
                                        new NestFullListViewAdapter<String>(R.layout.listview_market_list_pic_item_layout, gateSkuBean.getPic_arr()) {
                                            @Override
                                            public void onBind(int pos, String pic, NestFullViewHolder holder) {
                                                Log.d("NullListView", "嵌套第三层onBind() called with: pos = [" + pos + "], nestBean = [" + pic + "], v = [" + holder + "]");
                                                ImageView imagePic = holder.getView(R.id.imagePic);
                                                if (pic != null && !pic.isEmpty())
                                                    ImageUtil.displayImage(getApplicationContext(), imagePic, pic);
                                            }
                                        }
                                );
                            }
                        });
            }
        };
        marketListView.setAdapter(mNestFullListViewAdapter);
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
}
