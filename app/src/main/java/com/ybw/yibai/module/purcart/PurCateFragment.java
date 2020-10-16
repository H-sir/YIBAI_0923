package com.ybw.yibai.module.purcart;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.PurCartComExtendableListViewAdapter;
import com.ybw.yibai.common.adapter.PurCartComListViewAdapter;
import com.ybw.yibai.common.adapter.PurCartItemListViewAdapter;
import com.ybw.yibai.common.bean.HiddenChanged;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.bean.PurCartChildBean;
import com.ybw.yibai.common.bean.PurCartHeadBean;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageDispose;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.nestlistview.NestFullListViewAdapter;
import com.ybw.yibai.module.details.ProductDetailsActivity;
import com.ybw.yibai.module.market.MarketActivity;
import com.ybw.yibai.module.quotationpurchase.QuotationPurchaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static com.ybw.yibai.common.constants.HttpUrls.BASE_URL;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ADDORSELECT;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ID;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/10/15
 *     desc   :
 * </pre>
 */
public class PurCateFragment extends BaseFragment implements PurCartContract.PurCartView,
        PurCartComExtendableListViewAdapter.OnComAddClickListener,
        PurCartComExtendableListViewAdapter.OnItemAddClickListener,
        PurCartComExtendableListViewAdapter.OnComSubtractClickListener,
        PurCartComExtendableListViewAdapter.OnItemSubtractClickListener,
        PurCartComExtendableListViewAdapter.OnSelectItemClickListener,
        PurCartComExtendableListViewAdapter.OnSelectComClickListener,
        PurCartComExtendableListViewAdapter.OnChildClickListener {

    private PurCateFragment mPurCateFragment = null;

    @BindView(R.id.purCartCity)
    TextView purCartCity;
    @BindView(R.id.purCartComListView)
    ExpandableListView purCartComListView;
    @BindView(R.id.purCartAllSelect)
    LinearLayout purCartAllSelect;
    @BindView(R.id.purCartAllSelectText)
    TextView purCartAllSelectText;
    @BindView(R.id.purCartAllSelectImg)
    ImageView purCartAllSelectImg;
    @BindView(R.id.purCartAllPrice)
    TextView purCartAllPrice;
    @BindView(R.id.rootLayout)
    LinearLayout rootLayout;

    private PurCartContract.PurCartPresenter mPurCartPresenter;
    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;
    /**
     * 设计详情列表适配器
     */
    private PurCartComExtendableListViewAdapter mPurCartComExtendableListViewAdapter;

    private PurCartBean purCartBean;
    private boolean isAllSelect = true;
    private float allPrice = 0f;

    @Override
    protected int setLayouts() {
        mPurCateFragment = this;
        return R.layout.fragment_purcart_layout;
    }

    @Override
    protected void findViews(View view) {

    }

    @Override
    protected void initView() {
        mWaitDialog = new WaitDialog(getActivity());
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
        mPurCartPresenter = new PurCartPresenterImpl(this);

//        mPurCartItemListViewAdapter = new PurCartItemListViewAdapter(getActivity(), itemlistBeans);
//        purCartItemListView.setAdapter(mPurCartItemListViewAdapter);
//
//        mPurCartItemListViewAdapter.setOnItemAddClickListener(this);
//        mPurCartItemListViewAdapter.setOnItemSubtractClickListener(this);
//        mPurCartItemListViewAdapter.setSelectClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * EventBus
     * 接收用户从{@link QuotationPurchaseFragment#onHiddenChanged(boolean)} 传递过来的数据
     * 在Fragment隐藏状态发生改变时
     *
     * @param hiddenChanged Fragment隐藏状态(true Fragment隐藏,false Fragment显示)
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onFragmentHiddenChanged(HiddenChanged hiddenChanged) {
        boolean hidden = hiddenChanged.isHidden();
        if (hidden) {
            return;
        }
        // 获取用户的进货数据
        if (mPurCartPresenter != null)
            mPurCartPresenter.getPurCartData();
    }


    List<PurCartHeadBean> mPurCartHeadBean = new ArrayList<>();
    List<List<PurCartChildBean>> mPurCartChildBean = new ArrayList<>();

    @Override
    public void onGetPurCartDataSuccess(PurCartBean purCartBean) {
        this.purCartBean = purCartBean;
        allPrice = 0;
        int index = 0;
        mPurCartHeadBean.clear();
        mPurCartChildBean.clear();
        if (purCartBean.getData().getComlist() != null && purCartBean.getData().getComlist().size() > 0) {
            List<PurCartBean.DataBean.ComlistBean> comlistBeans = purCartBean.getData().getComlist();
            addGroup(0);
            index++;
            for (Iterator<PurCartBean.DataBean.ComlistBean> iterator = comlistBeans.iterator(); iterator.hasNext(); ) {
                PurCartBean.DataBean.ComlistBean comlistBean = iterator.next();
                if (comlistBean.getChecked() == 0) {
                    isAllSelect = false;
                    purCartAllSelectText.setText("全选");
                    purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.selected_img));
                } else {
                    allPrice = allPrice + (comlistBean.getPrice() * comlistBean.getNum());
                }

                addGroup(1, comlistBean);
                if (comlistBean.getFirst() != null) {
                    addChild(index, comlistBean.getFirst());
                }
                if (comlistBean.getSecond() != null) {
                    addChild(index, comlistBean.getSecond());
                }
                index++;
            }
        }
        if (purCartBean.getData().getItemlist() != null && purCartBean.getData().getItemlist().size() > 0) {
            List<PurCartBean.DataBean.ItemlistBean> itemlistBeans = purCartBean.getData().getItemlist();
            addGroup(2);
            index++;

            for (Iterator<PurCartBean.DataBean.ItemlistBean> iterator = itemlistBeans.iterator(); iterator.hasNext(); ) {
                PurCartBean.DataBean.ItemlistBean itemlistBean = iterator.next();
                if (itemlistBean.getChecked() == 0) {
                    isAllSelect = false;
                    purCartAllSelectText.setText("全选");
                    purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.selected_img));
                } else {
                    allPrice = allPrice + (itemlistBean.getPrice() * itemlistBean.getNum());
                }
                addGroup(3, itemlistBean);
                index++;
            }
        }
        mPurCartComExtendableListViewAdapter = new PurCartComExtendableListViewAdapter(getActivity(), mPurCartHeadBean, mPurCartChildBean);
        purCartComListView.setAdapter(mPurCartComExtendableListViewAdapter);
        mPurCartComExtendableListViewAdapter.setOnComAddClickListener(mPurCateFragment);
        mPurCartComExtendableListViewAdapter.setOnComSubtractClickListener(mPurCateFragment);
        mPurCartComExtendableListViewAdapter.setSelectComClickListener(mPurCateFragment);
        mPurCartComExtendableListViewAdapter.setOnItemAddClickListener(mPurCateFragment);
        mPurCartComExtendableListViewAdapter.setOnItemSubtractClickListener(mPurCateFragment);
        mPurCartComExtendableListViewAdapter.setSelectClickListener(mPurCateFragment);
        mPurCartComExtendableListViewAdapter.setChildClickListener(mPurCateFragment);
        purCartAllPrice.setText(String.valueOf(allPrice));
    }

    //添加组列表项
    public void addGroup(int type) {
        PurCartHeadBean purCartHeadBean = new PurCartHeadBean();
        purCartHeadBean.setType(type);
        mPurCartHeadBean.add(purCartHeadBean);
        mPurCartChildBean.add(new ArrayList<>()); //child中添加新数组
    }

    //添加组列表项
    public void addGroup(int type, PurCartBean.DataBean.ComlistBean comlistBean) {
        PurCartHeadBean purCartHeadBean = new PurCartHeadBean();
        purCartHeadBean.setType(type);
        purCartHeadBean.setCartId(comlistBean.getCartId());
        purCartHeadBean.setChecked(comlistBean.getChecked());
        purCartHeadBean.setNum(comlistBean.getNum());
        purCartHeadBean.setPic(comlistBean.getPic());
        purCartHeadBean.setPrice(comlistBean.getPrice());

        PurCartBean.DataBean.ComlistBean.FirstBean first = comlistBean.getFirst();
        if (first != null) {
            PurCartHeadBean.DataBean dataBean = new PurCartHeadBean.DataBean();
            dataBean.setGateId(first.getGateId());
            dataBean.setGateName(first.getGateName());
            dataBean.setGateProductId(first.getGateProductId());
            dataBean.setName(first.getName());
            dataBean.setPrice(first.getPrice());
            dataBean.setSkuId(first.getSkuId());
            purCartHeadBean.setFirst(dataBean);
        }
        PurCartBean.DataBean.ComlistBean.SecondBean second = comlistBean.getSecond();
        if (second != null) {
            PurCartHeadBean.DataBean dataBean = new PurCartHeadBean.DataBean();
            dataBean.setGateId(second.getGateId());
            dataBean.setGateName(second.getGateName());
            dataBean.setGateProductId(second.getGateProductId());
            dataBean.setName(second.getName());
            dataBean.setPrice(second.getFprice());
            dataBean.setSkuId(second.getSkuId());
            purCartHeadBean.setSecond(dataBean);
        }
        mPurCartHeadBean.add(purCartHeadBean);
        mPurCartChildBean.add(new ArrayList<>()); //child中添加新数组
    }

    //添加组列表项
    public void addGroup(int type, PurCartBean.DataBean.ItemlistBean itemlistBean) {
        PurCartHeadBean purCartHeadBean = new PurCartHeadBean();
        purCartHeadBean.setType(type);
        purCartHeadBean.setCartId(itemlistBean.getCartId());
        purCartHeadBean.setChecked(itemlistBean.getChecked());
        purCartHeadBean.setNum(itemlistBean.getNum());
        purCartHeadBean.setPic(itemlistBean.getPic());
        purCartHeadBean.setPrice(itemlistBean.getPrice());

        PurCartBean.DataBean.ItemlistBean.FirstBeanX first = itemlistBean.getFirst();
        if (first != null) {
            PurCartHeadBean.DataBean dataBean = new PurCartHeadBean.DataBean();
            dataBean.setGateId(first.getGateId());
            dataBean.setGateName(first.getGateName());
            dataBean.setGateProductId(first.getGateProductId());
            dataBean.setName(first.getName());
            dataBean.setPrice(first.getPrice());
            dataBean.setSkuId(first.getSkuId());
            purCartHeadBean.setFirst(dataBean);
        }
        mPurCartHeadBean.add(purCartHeadBean);
        mPurCartChildBean.add(new ArrayList<>()); //child中添加新数组
    }

    //添加对应组的自列表项
    public void addChild(int position, PurCartBean.DataBean.ComlistBean.FirstBean first) {
        List<PurCartChildBean> purCartChildBeans = mPurCartChildBean.get(position);
        if (purCartChildBeans != null) {
            PurCartChildBean purCartChildBean = new PurCartChildBean();
            purCartChildBean.setSkuId(first.getSkuId());
            purCartChildBean.setName(first.getName());
            purCartChildBean.setPrice(first.getPrice());
            purCartChildBean.setGateId(first.getGateId());
            purCartChildBean.setGateName(first.getGateName());
            purCartChildBean.setGateProductId(first.getGateProductId());
            purCartChildBeans.add(purCartChildBean);
        } else {
            purCartChildBeans = new ArrayList<>();
            PurCartChildBean purCartChildBean = new PurCartChildBean();
            purCartChildBean.setSkuId(first.getSkuId());
            purCartChildBean.setName(first.getName());
            purCartChildBean.setPrice(first.getPrice());
            purCartChildBean.setGateId(first.getGateId());
            purCartChildBean.setGateName(first.getGateName());
            purCartChildBean.setGateProductId(first.getGateProductId());
            purCartChildBeans.add(purCartChildBean);
        }
    }

    //添加对应组的自列表项
    public void addChild(int position, PurCartBean.DataBean.ComlistBean.SecondBean second) {
        List<PurCartChildBean> purCartChildBeans = mPurCartChildBean.get(position);
        if (purCartChildBeans != null) {
            PurCartChildBean purCartChildBean = new PurCartChildBean();
            purCartChildBean.setSkuId(second.getSkuId());
            purCartChildBean.setName(second.getName());
            purCartChildBean.setPrice(second.getFprice());
            purCartChildBean.setGateId(second.getGateId());
            purCartChildBean.setGateName(second.getGateName());
            purCartChildBean.setGateProductId(second.getGateProductId());
            purCartChildBeans.add(purCartChildBean);
        } else {
            purCartChildBeans = new ArrayList<>();
            PurCartChildBean purCartChildBean = new PurCartChildBean();
            purCartChildBean.setSkuId(second.getSkuId());
            purCartChildBean.setName(second.getName());
            purCartChildBean.setPrice(second.getFprice());
            purCartChildBean.setGateId(second.getGateId());
            purCartChildBean.setGateName(second.getGateName());
            purCartChildBean.setGateProductId(second.getGateProductId());
            purCartChildBeans.add(purCartChildBean);
        }
    }


    //添加对应组的自列表项
    public void addChild(int position, PurCartBean.DataBean.ItemlistBean.FirstBeanX first) {
        List<PurCartChildBean> purCartChildBeans = mPurCartChildBean.get(position);
        if (purCartChildBeans != null) {
            PurCartChildBean purCartChildBean = new PurCartChildBean();
            purCartChildBean.setSkuId(first.getSkuId());
            purCartChildBean.setName(first.getName());
            purCartChildBean.setPrice(first.getPrice());
            purCartChildBean.setGateId(first.getGateId());
            purCartChildBean.setGateName(first.getGateName());
            purCartChildBean.setGateProductId(first.getGateProductId());
            purCartChildBeans.add(purCartChildBean);
        } else {
            purCartChildBeans = new ArrayList<>();
            PurCartChildBean purCartChildBean = new PurCartChildBean();
            purCartChildBean.setSkuId(first.getSkuId());
            purCartChildBean.setName(first.getName());
            purCartChildBean.setPrice(first.getPrice());
            purCartChildBean.setGateId(first.getGateId());
            purCartChildBean.setGateName(first.getGateName());
            purCartChildBean.setGateProductId(first.getGateProductId());
            purCartChildBeans.add(purCartChildBean);
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
        if (null != mPurCartPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mPurCartPresenter.onDetachView();
            mPurCartPresenter = null;
        }
        ExceptionUtil.handleException(throwable);
    }

    @OnClick({R.id.purCartAllSelect, R.id.purCartSettlement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.purCartAllSelect:
                purCartAllSelectData();
                break;
            case R.id.purCartSettlement:
                WXMiniProgramObject miniProgram = new WXMiniProgramObject();
                miniProgram.webpageUrl = BASE_URL;//兼容低版本的网页链接
                miniProgram.userName = "gh_a532df421aeb";//小程序端提供参数
                miniProgram.path = "/pages/index/index?target=settlement"; //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
                WXMediaMessage mediaMessage = new WXMediaMessage(miniProgram);
                mediaMessage.title = "亲，请进行进货结算~"; //自定义
                ImageDispose.returnBitMap("http://f.100ybw.com/images/wxminiprogramshare.png", new ImageDispose.CallBack() {
                    @Override
                    public void onCallBack(byte[] bytes) {
                        if (bytes != null)
                            mediaMessage.thumbData = bytes;
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = "";
                        req.scene = SendMessageToWX.Req.WXSceneSession;
                        req.message = mediaMessage;
                        YiBaiApplication.getIWXAPI().sendReq(req);
                    }
                });
                break;
        }
    }

    private void purCartAllSelectData() {
        if (isAllSelect) {
            String cartIds = "";
            for (Iterator<PurCartBean.DataBean.ItemlistBean> iterator = purCartBean.getData().getItemlist().iterator(); iterator.hasNext(); ) {
                PurCartBean.DataBean.ItemlistBean itemlistBean = iterator.next();
                if (itemlistBean.getChecked() == 1) {
                    cartIds = cartIds + itemlistBean.getCartId() + ",";
                }
            }
            for (Iterator<PurCartBean.DataBean.ComlistBean> iterator = purCartBean.getData().getComlist().iterator(); iterator.hasNext(); ) {
                PurCartBean.DataBean.ComlistBean comlistBean = iterator.next();
                if (comlistBean.getChecked() == 1) {
                    cartIds = cartIds + comlistBean.getCartId() + ",";
                }
            }
            cartIds.substring(0, cartIds.length() - 2);
            mPurCartPresenter.upAllCart(cartIds, 1, 0);

        } else {
            String cartIds = "";
            for (Iterator<PurCartBean.DataBean.ItemlistBean> iterator = purCartBean.getData().getItemlist().iterator(); iterator.hasNext(); ) {
                PurCartBean.DataBean.ItemlistBean itemlistBean = iterator.next();
                if (itemlistBean.getChecked() == 0) {
                    cartIds = cartIds + itemlistBean.getCartId() + ",";
                }
            }
            for (Iterator<PurCartBean.DataBean.ComlistBean> iterator = purCartBean.getData().getComlist().iterator(); iterator.hasNext(); ) {
                PurCartBean.DataBean.ComlistBean comlistBean = iterator.next();
                if (comlistBean.getChecked() == 0) {
                    cartIds = cartIds + comlistBean.getCartId() + ",";
                }
            }
            cartIds.substring(0, cartIds.length() - 2);
            mPurCartPresenter.upAllCart(cartIds, 1, 1);
        }
    }

    @Override
    public void onUpAllCartSuccess(int isCheck) {
        if (isCheck == 1) {
            isAllSelect = true;
            purCartAllSelectText.setText("全不选");
            purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.selected_img));
        } else {
            isAllSelect = false;
            purCartAllSelectText.setText("全选");
            purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.purcart_no_select));
        }
        for (Iterator<PurCartBean.DataBean.ItemlistBean> iterator = purCartBean.getData().getItemlist().iterator(); iterator.hasNext(); ) {
            PurCartBean.DataBean.ItemlistBean itemlistBean = iterator.next();
            itemlistBean.setChecked(isCheck);
        }
        for (Iterator<PurCartBean.DataBean.ComlistBean> iterator = purCartBean.getData().getComlist().iterator(); iterator.hasNext(); ) {
            PurCartBean.DataBean.ComlistBean comlistBean = iterator.next();
            comlistBean.setChecked(isCheck);
        }
        onGetPurCartDataSuccess(purCartBean);
    }

    TextView purcartComNum;
    ImageView purcartComSelect;
    int index = 0;
    int position = 0;

    @Override
    public void onComAddNum(int position, TextView purcartComNum) {
        index = 1;
        this.position = position;
        this.purcartComNum = purcartComNum;
        PurCartHeadBean purCartHeadBean = mPurCartHeadBean.get(position);
        int num = purCartHeadBean.getNum() + 1;
        int cartId = purCartHeadBean.getCartId();
        if (mPurCartPresenter != null)
            mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onComSubtractNum(int position, TextView purcartComNum) {
        index = 2;
        this.position = position;
        this.purcartComNum = purcartComNum;
        PurCartHeadBean purCartHeadBean = mPurCartHeadBean.get(position);
        int num = purCartHeadBean.getNum() - 1;
        int cartId = purCartHeadBean.getCartId();
        if (mPurCartPresenter != null)
            mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onItemAddNum(int position, TextView purcartComNum) {
        index = 3;
        this.position = position;
        this.purcartComNum = purcartComNum;
        PurCartHeadBean purCartHeadBean = mPurCartHeadBean.get(position);
        int num = purCartHeadBean.getNum() + 1;
        int cartId = purCartHeadBean.getCartId();
        if (mPurCartPresenter != null)
            mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onItemSubtractNum(int position, TextView purcartComNum) {
        index = 4;
        this.position = position;
        this.purcartComNum = purcartComNum;
        PurCartHeadBean purCartHeadBean = mPurCartHeadBean.get(position);
        int num = purCartHeadBean.getNum() - 1;
        int cartId = purCartHeadBean.getCartId();
        if (mPurCartPresenter != null)
            mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onComSelectNum(int position, ImageView purcartComSelect, boolean isSelect) {
        index = 5;
        this.position = position;
        this.purcartComSelect = purcartComSelect;
        PurCartHeadBean purCartHeadBean = mPurCartHeadBean.get(position);
        int check = 0;
        if (isSelect) {
            check = 1;
        }
        int cartId = purCartHeadBean.getCartId();
        if (mPurCartPresenter != null)
            mPurCartPresenter.updateCartGateCheck(cartId, check);
    }

    @Override
    public void onItemSelectNum(int position, ImageView purcartComSelect, boolean isSelect) {
        index = 6;
        this.position = position;
        this.purcartComSelect = purcartComSelect;
        PurCartHeadBean purCartHeadBean = mPurCartHeadBean.get(position);
        int check = 0;
        if (isSelect) {
            check = 1;
        }
        int cartId = purCartHeadBean.getCartId();
        if (mPurCartPresenter != null)
            mPurCartPresenter.updateCartGateCheck(cartId, check);
    }

    @Override
    public void onUpdateCartGateSuccess(int num) {
        PurCartHeadBean purCartHeadBean = mPurCartHeadBean.get(position);
        int cartId = purCartHeadBean.getCartId();

        switch (index) {
            case 1:
            case 2:
            case 3:
            case 4:
                for (Iterator<PurCartBean.DataBean.ComlistBean> iterator = purCartBean.getData().getComlist().iterator(); iterator.hasNext(); ) {
                    PurCartBean.DataBean.ComlistBean comlistBean = iterator.next();
                    if (comlistBean.getCartId() == cartId) {
                        comlistBean.setNum(num);
                        break;
                    }
                }
                for (Iterator<PurCartBean.DataBean.ItemlistBean> iterator = purCartBean.getData().getItemlist().iterator(); iterator.hasNext(); ) {
                    PurCartBean.DataBean.ItemlistBean itemlistBean = iterator.next();
                    if (itemlistBean.getCartId() == cartId) {
                        itemlistBean.setNum(num);
                        break;
                    }
                }
                purCartHeadBean.setNum(num);
                purcartComNum.setText(String.valueOf(num));
                break;
            case 5:
            case 6:
                for (Iterator<PurCartBean.DataBean.ComlistBean> iterator = purCartBean.getData().getComlist().iterator(); iterator.hasNext(); ) {
                    PurCartBean.DataBean.ComlistBean comlistBean = iterator.next();
                    if (comlistBean.getCartId() == cartId) {
                        comlistBean.setChecked(num);
                        break;
                    }
                }
                for (Iterator<PurCartBean.DataBean.ItemlistBean> iterator = purCartBean.getData().getItemlist().iterator(); iterator.hasNext(); ) {
                    PurCartBean.DataBean.ItemlistBean itemlistBean = iterator.next();
                    if (itemlistBean.getCartId() == cartId) {
                        itemlistBean.setChecked(num);
                        break;
                    }
                }
                purCartHeadBean.setChecked(num);
                if (purCartHeadBean.getChecked() == 1) {
                    purcartComSelect.setImageResource(R.mipmap.selected_img);
                } else {
                    purcartComSelect.setImageResource(R.mipmap.purcart_no_select);
                }
                setAllSelect();
                break;
        }
        index = 0;
    }

    private void setAllSelect() {
        isAllSelect = false;
        purCartAllSelectText.setText("全不选");
        purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.purcart_no_select));

        onGetPurCartDataSuccess(purCartBean);
    }

    @Override
    public void onChild(PurCartChildBean purCartChildBean) {
        if (purCartChildBean.getSkuId() != 0) {
            Intent intent = new Intent(getActivity(), MarketActivity.class);
            intent.putExtra(PRODUCT_SKU_ID, purCartChildBean.getSkuId());
            intent.putExtra(PRODUCT_SKU_ADDORSELECT, false);
            startActivity(intent);
        }
    }
}
