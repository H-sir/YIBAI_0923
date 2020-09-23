package com.ybw.yibai.module.quotation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.LocationPlacementAdapter;
import com.ybw.yibai.common.adapter.StayPlacementAdapter;
import com.ybw.yibai.common.bean.AddQuotationLocation;
import com.ybw.yibai.common.bean.CreateQuotationLocation;
import com.ybw.yibai.common.bean.CreateQuotationOrder;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.bean.DeleteQuotationLocation;
import com.ybw.yibai.common.bean.HiddenChanged;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.PlacementQrQuotationList;
import com.ybw.yibai.common.bean.QuotationAgain;
import com.ybw.yibai.common.bean.QuotationLocation;
import com.ybw.yibai.common.bean.QuotationLocation.DataBean;
import com.ybw.yibai.common.bean.QuotationLocation.DataBean.ListBean;
import com.ybw.yibai.common.bean.QuotationLocation.DataBean.ListBean.QuotelistBean;
import com.ybw.yibai.common.bean.SelectImage;
import com.ybw.yibai.common.bean.ToFragment;
import com.ybw.yibai.common.bean.UpdateQuotationInfo;
import com.ybw.yibai.common.bean.UpdateQuotationLocation;
import com.ybw.yibai.common.bean.UpdateQuotationPrice;
import com.ybw.yibai.common.bean.VerifyPassword;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.GuideUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.browser.BrowserActivity;
import com.ybw.yibai.module.drawing.SimulationDrawingActivity;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.pictureview.PictureViewActivity;
import com.ybw.yibai.module.quotation.QuotationContract.QuotationPresenter;
import com.ybw.yibai.module.quotation.QuotationContract.QuotationView;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsFragment;
import com.ybw.yibai.module.quotationpurchase.QuotationPurchaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMER_NAME;
import static com.ybw.yibai.common.constants.Preferences.ORDER_NUMBER;
import static com.ybw.yibai.common.constants.Preferences.ORDER_SHARE_URL_TYPE;
import static com.ybw.yibai.common.constants.Preferences.SELECT_IMAGE;
import static com.ybw.yibai.common.constants.Preferences.SERVICE;
import static com.ybw.yibai.common.constants.Preferences.TYPE;
import static com.ybw.yibai.common.constants.Preferences.URL;

/**
 * 报价Fragment
 *
 * @author sjl
 * @date 2019/10/29
 */
public class QuotationFragment extends BaseFragment implements QuotationView,
        View.OnClickListener,
        LocationPlacementAdapter.OnItemClickListener,
        LocationPlacementAdapter.OnDesignDrawingClickListener,
        LocationPlacementAdapter.OnDeleteClickListener,
        LocationPlacementAdapter.OnRenameClickListener,
        LocationPlacementAdapter.OnNumberChangeListener,
        LocationPlacementAdapter.OnLocationPlacementDetailsItemLongClickListener,
        LocationPlacementAdapter.OnOrderEditTextClickListener,
        StayPlacementAdapter.OnItemClickListener,
        StayPlacementAdapter.OnItemLongClickListener,
        StayPlacementAdapter.OnCheckBoxClickListener {

    private String TAG = "QuotationFragment";

    /**
     * 用户点击选择设计图在"报价位置信息"的位置
     * 用户点击场景名称在"报价位置信息"的位置
     */
    private int position;

    /**
     * 用户点击"清单及价格"的价格在"报价清单列表"的位置
     */
    private int pricePosition;

    /**
     * 用户修改清单产品价格的报价方式
     * 0:购买
     * 1:月租
     * 2:年租
     */
    private int quotationMode;

    /**
     * 报价方式(默认月租)
     * 0:购买
     * 1:月租
     * 2:年租
     * 3:日租
     */
    private int payType = 1;

    /**
     * 税率
     */
    private double taxRate;

    /**
     * 优惠金额
     */
    private double discountMoney;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    private View mRootView;

    /**
     * 有数据显示的UI
     */
    private RelativeLayout mContentLayout;

    /**
     * 添加新位置
     */
    private TextView mAddNewLocation;

    /**
     * 位置摆放清单
     */
    private RecyclerView mLocationPlacementRecyclerView;

    /**
     * 全选“待摆放清单”
     */
    private TextView mAllCheckTextView;

    /**
     * 删除“待摆放清单”
     */
    private TextView mDeleteTextView;

    /**
     * 取消删除“待摆放清单”
     */
    private TextView mCancelTextView;

    /**
     * 待摆放清单
     */
    private RecyclerView mStayPlacementRecyclerView;

    /**
     * 待摆放产品
     */
    private LinearLayout mProductsToBePlacedLayout;

    /**
     *
     */
    private CoordinatorLayout mCoordinatorLayout;

    /**
     *
     */
    private BottomSheetBehavior mBottomSheetBehavior;

    /**
     * 显示报价信息
     */
    private RelativeLayout mShowTotalLayout;

    /**
     * 显示价格
     */
    private TextView mTradePriceTextView;

    /**
     * 清单
     */
    private TextView mListTextView;

    /**
     * 保存报价
     */
    private TextView mSaveQuotationTextView;

    /*----------*/

    /**
     * 没有数据显示的UI
     */
    private RelativeLayout mNoDataLayout;

    /**
     * 看看产品Button
     */
    private Button mLookProductButton;

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
     * 位置摆放清单数据
     */
    private List<ListBean> mLocationPlacementList;

    /**
     * 显示报价单位置明细列表的适配器
     */
    private LocationPlacementAdapter mLocationPlacementAdapter;

    /**
     * 待摆放清单列表
     */
    private List<PlacementQrQuotationList.DataBean.ListBean> mPlacementList;

    /**
     * 待摆放清单列表适配器
     */
    private StayPlacementAdapter mStayPlacementAdapter;

    /**
     * 报价清单列表
     */
    private List<PlacementQrQuotationList.DataBean.ListBean> mQuotationList;

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
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private QuotationPresenter mQuotationPresenter;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        mActivity = getActivity();
        return R.layout.fragment_quotation;
    }

    @Override
    protected void findViews(View view) {
        mRootView = view.findViewById(R.id.rootLayout);
        mContentLayout = view.findViewById(R.id.contentLayout);
        mAddNewLocation = view.findViewById(R.id.addNewLocation);
        mLocationPlacementRecyclerView = view.findViewById(R.id.locationPlacementRecyclerView);
        mAllCheckTextView = view.findViewById(R.id.allCheckTextView);
        mDeleteTextView = view.findViewById(R.id.deleteTextView);
        mCancelTextView = view.findViewById(R.id.cancelTextView);
        mStayPlacementRecyclerView = view.findViewById(R.id.stayPlacementRecyclerView);
        mProductsToBePlacedLayout = view.findViewById(R.id.productsToBePlacedLayout);
        mCoordinatorLayout = view.findViewById(R.id.coordinatorLayout);

        mShowTotalLayout = view.findViewById(R.id.showTotalLayout);
        mTradePriceTextView = view.findViewById(R.id.totalPriceTextView);
        mListTextView = view.findViewById(R.id.listTextView);
        mSaveQuotationTextView = view.findViewById(R.id.saveQuotationTextView);

        mNoDataLayout = view.findViewById(R.id.noDataLayout);
        mLookProductButton = view.findViewById(R.id.lookProductButton);
    }

    @Override
    protected void initView() {
        mWaitDialog = new WaitDialog(mContext);
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // 给RecyclerView设置布局管理器(必须设置)
        mLocationPlacementRecyclerView.setLayoutManager(manager);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager managers = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
        int spacing = DensityUtil.dpToPx(mContext, 10);
        // 设置RecyclerView间距
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(6, spacing, true);
        // 给RecyclerView设置布局管理器(必须设置)
        mStayPlacementRecyclerView.setLayoutManager(managers);
        mStayPlacementRecyclerView.addItemDecoration(decoration);

        mBottomSheetBehavior = BottomSheetBehavior.from(mProductsToBePlacedLayout);
        mBottomSheetBehavior.setState(STATE_EXPANDED);
    }

    @Override
    protected void initData() {
        mLocationPlacementList = new ArrayList<>();
        mPlacementList = new ArrayList<>();
        mQuotationList = new ArrayList<>();

        mLocationPlacementAdapter = new LocationPlacementAdapter(mContext, mLocationPlacementList);
        mLocationPlacementRecyclerView.setAdapter(mLocationPlacementAdapter);
        mLocationPlacementAdapter.setOnItemClickListener(this);
        mLocationPlacementAdapter.setOnDesignDrawingClickListener(this);
        mLocationPlacementAdapter.setOnDeleteClickListener(this);
        mLocationPlacementAdapter.setOnRenameClickListener(this);
        mLocationPlacementAdapter.setOnNumberChangeListener(this);
        mLocationPlacementAdapter.setOnOrderEditTextClickListener(this);
        mLocationPlacementAdapter.setOnLocationPlacementDetailsItemLongClickListener(this);

        mStayPlacementAdapter = new StayPlacementAdapter(mContext, mPlacementList);
        mStayPlacementRecyclerView.setAdapter(mStayPlacementAdapter);
        mStayPlacementAdapter.setOnItemClickListener(this);
        mStayPlacementAdapter.setOnItemLongClickListener(this);
        mStayPlacementAdapter.setOnCheckBoxClickListener(this);
    }

    @Override
    protected void initEvent() {
        mQuotationPresenter = new QuotationPresenterImpl(this);
        mAddNewLocation.setOnClickListener(this);
        mAllCheckTextView.setOnClickListener(this);
        mDeleteTextView.setOnClickListener(this);
        mCancelTextView.setOnClickListener(this);
        mShowTotalLayout.setOnClickListener(this);
        mListTextView.setOnClickListener(this);
        mSaveQuotationTextView.setOnClickListener(this);
        mLookProductButton.setOnClickListener(this);

        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        GuideUtil.showGuideView(this, R.layout.guide_quotation_layout);
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
            recoverProductsToBePlacedLayout();
        } else {
            mQuotationPresenter.getQuotationLocation();
            mQuotationPresenter.getPlacementList();
            mQuotationPresenter.getQuotationList();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 添加新位置
        if (id == R.id.addNewLocation) {
            mQuotationPresenter.createQuotationLocation(null);
        }

        // 全选
        if (id == R.id.allCheckTextView) {
            String string = mAllCheckTextView.getText().toString();
            boolean selectCheckBox = string.equals(getResources().getString(R.string.check_all));
            for (PlacementQrQuotationList.DataBean.ListBean listBean : mPlacementList) {
                listBean.setSelectCheckBox(selectCheckBox);
            }
            if (selectCheckBox) {
                mAllCheckTextView.setText(getResources().getString(R.string.none));
            } else {
                mAllCheckTextView.setText(getResources().getString(R.string.check_all));
            }
            mStayPlacementAdapter.notifyDataSetChanged();
        }

        // 删除
        if (id == R.id.deleteTextView) {
            StringBuilder stringBuilder = new StringBuilder();
            for (PlacementQrQuotationList.DataBean.ListBean listBean : mPlacementList) {
                if (!listBean.isSelectCheckBox()) {
                    continue;
                }
                int quoteId = listBean.getQuote_id();
                stringBuilder.append(quoteId);
                stringBuilder.append(",");
            }
            String string = stringBuilder.toString();

            // 删除最后一个“,”
            if (string.length() >= 1) {
                string = string.substring(0, stringBuilder.length() - 1);
            }
            if (TextUtils.isEmpty(string)) {
                MessageUtil.showMessage(getResources().getString(R.string.you_currently_have_no_products_selected));
            } else {
                mQuotationPresenter.deletePlacementList(string);
            }
        }

        // 取消
        if (id == R.id.cancelTextView) {
            for (PlacementQrQuotationList.DataBean.ListBean listBean : mPlacementList) {
                listBean.setShowCheckBox(false);
                listBean.setSelectCheckBox(false);
            }
            mStayPlacementAdapter.notifyDataSetChanged();
            mAllCheckTextView.setVisibility(View.GONE);
            mDeleteTextView.setVisibility(View.GONE);
            mCancelTextView.setVisibility(View.GONE);
        }

        // 显示报价设置
        if (id == R.id.showTotalLayout || id == R.id.listTextView) {
            // mQuotationPresenter.displayQuotationSetPopupWindow(mShowTotalLayout, mQuotationList);
            mQuotationPresenter.displayQuotationSetPopupWindow(mShowTotalLayout, payType, taxRate, discountMoney, mQuotationList);
        }

        // 保存报价
        if (id == R.id.saveQuotationTextView) {
            mQuotationPresenter.createQuotationOrder(payType, taxRate, discountMoney);
        }

        // 看看产品
        if (id == R.id.lookProductButton) {
            /**
             * 发送数据到{@link MainActivity#ratioActivitySendData(ToFragment)}
             * 使其跳转到对应的Fragment
             */
            ToFragment toFragment = new ToFragment(2);
            EventBus.getDefault().postSticky(toFragment);
        }
    }

    /**
     * 在位置摆放清单RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        for (int i = 0; i < mLocationPlacementList.size(); i++) {
            ListBean listBean = mLocationPlacementList.get(i);
            boolean select;
            if (i == position) {
                select = true;
            } else {
                select = false;
            }
            listBean.setSelect(select);
        }
        mLocationPlacementAdapter.notifyDataSetChanged();
    }

    /**
     * 在位置摆放清单RecyclerView中的"设计图"被点击时回调
     *
     * @param position 被点击的位置
     */
    @Override
    public void onDesignDrawingClick(int position) {
        this.position = position;
        Intent intent = new Intent(mContext, SimulationDrawingActivity.class);
        intent.putExtra(SELECT_IMAGE, true);
        startActivity(intent);
    }

    /**
     * 在位置摆放清单RecyclerView Item "删除" 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onDeleteClick(int position) {
        ListBean listBean = mLocationPlacementList.get(position);
        int quotePlaceId = listBean.getQuote_place_id();
        mQuotationPresenter.deleteQuotationLocation(quotePlaceId);
    }

    /**
     * 在位置摆放清单RecyclerView Item "改名" 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onRenameClick(int position) {
        this.position = position;
        ListBean listBean = mLocationPlacementList.get(position);
        String quotePlaceName = listBean.getQuote_place_name();
        mQuotationPresenter.displaySceneNamePopupWindow(mRootView, quotePlaceName);
    }

    /**
     * 在位置摆放清单RecyclerView中的OrderEditText数量发生改变时回调
     *
     * @param parentPosition 被点击的父Item位置
     * @param childPosition  被点击的子Item位置
     * @param number         数量
     */
    @Override
    public void onNumberChange(int parentPosition, int childPosition, int number) {
        ListBean listBean = mLocationPlacementList.get(parentPosition);
        List<QuotelistBean> quoteList = listBean.getQuotelist();
        int quotePlaceId = listBean.getQuote_place_id();
        QuotelistBean quotelistBean = quoteList.get(childPosition);
        int quoteId = quotelistBean.getQuote_id();
        mQuotationPresenter.updateQuotationInfo(quotePlaceId, quoteId, number);
    }

    /**
     * 在位置摆放清单RecyclerView中的OrderEditText中的EditText被点击时回调
     *
     * @param parentPosition 被点击的父Item位置
     * @param childPosition  被点击的子Item位置
     */
    @Override
    public void onOrderEditTextClick(int parentPosition, int childPosition) {
        ListBean listBean = mLocationPlacementList.get(parentPosition);
        List<QuotelistBean> quoteList = listBean.getQuotelist();
        int quotePlaceId = listBean.getQuote_place_id();
        QuotelistBean quotelistBean = quoteList.get(childPosition);
        int quoteId = quotelistBean.getQuote_id();
        int number = quotelistBean.getNum();
        mQuotationPresenter.displayModifyBonsaiNumberPopupWindow(mRootView, quotePlaceId, quoteId, number);
    }

    /**
     * 在显示报价单,某一个位置盆栽详细的RecyclerView Item 点击时回调
     *
     * @param parentPosition 被点击的父Item位置
     * @param childPosition  被点击的子Item位置
     */
    @Override
    public void onLocationPlacementDetailsItemLongClick(int parentPosition, int childPosition) {
        ListBean listBean = mLocationPlacementList.get(parentPosition);
        List<QuotelistBean> quoteList = listBean.getQuotelist();
        int quotePlaceId = listBean.getQuote_place_id();
        QuotelistBean quotelistBean = quoteList.get(childPosition);
        int quoteId = quotelistBean.getQuote_id();
        String firstName = quotelistBean.getFirst_name();
        mQuotationPresenter.displayAreYouSureToDeleteTheBonsaiDialog(firstName, quotePlaceId, quoteId);
    }

    /**
     * 在待摆放清单RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onStayPlacementItemClick(int position) {
        if (null == mLocationPlacementList || mLocationPlacementList.size() == 0) {
            MessageUtil.showMessage(getResources().getString(R.string.you_need_to_create_an_added_location));
            return;
        }
        // 用户当前选中的位置
        int location = -1;
        for (int i = 0; i < mLocationPlacementList.size(); i++) {
            ListBean listBean = mLocationPlacementList.get(i);
            if (listBean.isSelect()) {
                location = i;
                break;
            }
        }
        if (-1 == location) {
            MessageUtil.showMessage(getResources().getString(R.string.you_need_to_select_an_added_location));
            return;
        }
        // 当前选中位置的id
        int quotePlaceId = mLocationPlacementList.get(location).getQuote_place_id();
        // 清单id
        int quoteId = mPlacementList.get(position).getQuote_id();
        mQuotationPresenter.addQuotationLocation(quoteId, quotePlaceId);
    }

    /**
     * 在待摆放清单RecyclerView Item 长按时回调
     *
     * @param position 被长按的Item位置
     */
    @Override
    public void onStayPlacementItemLongClick(int position) {
        mAllCheckTextView.setVisibility(View.VISIBLE);
        mDeleteTextView.setVisibility(View.VISIBLE);
        mCancelTextView.setVisibility(View.VISIBLE);
    }

    /**
     * 在待摆放清单RecyclerView Item 中的CheckBox选中状态发生改变时回调
     *
     * @param position 位置
     */
    @Override
    public void onStayPlacementCheckBoxClick(int position) {
        // 判断用户是否全部选中CheckBox默认是的
        boolean allSelect = true;
        for (PlacementQrQuotationList.DataBean.ListBean listBean : mPlacementList) {
            boolean selectCheckBox = listBean.isSelectCheckBox();
            if (!selectCheckBox) {
                // 说明存在没有选中的CheckBox
                allSelect = false;
                break;
            }
        }
        if (allSelect) {
            mAllCheckTextView.setText(getResources().getString(R.string.none));
        } else {
            mAllCheckTextView.setText(getResources().getString(R.string.check_all));
        }
    }

    /**
     * 在"清单及价格"PopupWindow里面的RadioButton被选中时回调
     *
     * @param group     被点击的RadioGroup
     * @param checkedId 被选中的RadioButton ID
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 日租
        if (checkedId == R.id.dayRentRadioButton) {
            payType = 3;
        }

        // 月租
        if (checkedId == R.id.monthRentRadioButton) {
            payType = 1;
        }

        // 年租
        if (checkedId == R.id.yearRentRadioButton) {
            payType = 2;
        }

        // 购买
        if (checkedId == R.id.buyRadioButton) {
            payType = 0;
        }

        mQuotationPresenter.calculatedTotalPrice(payType, taxRate, discountMoney, mQuotationList);
    }

    /**
     * 在计算报价清单里面盆栽的总价格成功时回调
     *
     * @param totalPrice 总价格
     */
    @Override
    public void onCalculatedTotalPriceSuccess(String totalPrice) {
        String payTypeString = null;
        if (1 == payType) {
            payTypeString = getResources().getString(R.string.month);
        } else if (2 == payType) {
            payTypeString = getResources().getString(R.string.year);
        } else if (3 == payType) {
            payTypeString = getResources().getString(R.string.day);
        }
        String text = YiBaiApplication.getCurrencySymbol() + totalPrice;
        if (!TextUtils.isEmpty(payTypeString)) {
            text = text + "/" + payTypeString;
        }
        mTradePriceTextView.setText(text);
    }

    /**
     * 在获取税率成功时回调
     *
     * @param taxRate 税率
     */
    @Override
    public void onGetTaxRateSuccess(double taxRate) {
        this.taxRate = taxRate;
        mQuotationPresenter.calculatedTotalPrice(payType, taxRate, discountMoney, mQuotationList);
    }

    /**
     * 在获取优惠价格成功时回调
     *
     * @param discountMoney 优惠价格
     */
    @Override
    public void onGetDiscountMoneySuccess(double discountMoney) {
        this.discountMoney = discountMoney;
        mQuotationPresenter.calculatedTotalPrice(payType, taxRate, discountMoney, mQuotationList);
    }

    /**
     * 返回修改后的场景名称时回调
     *
     * @param newSceneName 新的场景名称
     */
    @Override
    public void returnNewSceneName(String newSceneName) {
        ListBean listBean = mLocationPlacementList.get(position);
        int quotePlaceId = listBean.getQuote_place_id();
        mQuotationPresenter.updateQuotationLocation(quotePlaceId, newSceneName, null);
    }

    /**
     * 在显示"清单及价格"RecyclerView Item 中的"价格"点击时回调
     *
     * @param position 被点击的Item位置
     * @param mode     报价方式
     * @param price    被点击的Item位置的价格
     */
    @Override
    public void onPriceClick(int position, int mode, double price) {
        pricePosition = position;
        quotationMode = mode;
        if (YiBaiApplication.getRoleName().equals(SERVICE)) {
            mQuotationPresenter.displayEditPricePopupWindow(mRootView, mode, price);
        } else if (YiBaiApplication.isAuthorization()) {
            mQuotationPresenter.displayEditPricePopupWindow(mRootView, mode, price);
        } else {
            mQuotationPresenter.displayAuthorizationCodePopupWindow(mRootView);
        }
    }

    /**
     * 在验证修改价格密码成功时回调
     *
     * @param verifyPassword 验证修改价格密码时服务器端返回的数据
     */
    @Override
    public void onVerifyPasswordSuccess(VerifyPassword verifyPassword) {
        MessageUtil.showMessage(verifyPassword.getMsg());
        if (CODE_SUCCEED == verifyPassword.getCode()) {
            YiBaiApplication.setAuthorization(true);
        }
    }

    /**
     * 返回修改后的新价格
     *
     * @param newPrice 改后的新价格
     */
    @Override
    public void returnNewPrice(double newPrice) {
        PlacementQrQuotationList.DataBean.ListBean listBean = mQuotationList.get(pricePosition);
        int quoteId = listBean.getQuote_id();
        if (0 == quotationMode) {
            mQuotationPresenter.updateQuotationPrice(quoteId, newPrice, 1);
        } else {
            mQuotationPresenter.updateQuotationPrice(quoteId, newPrice, 2);
        }
    }

    /**
     * 在获取报价位置明细列表成功时回调
     *
     * @param quotationLocation 报价位置明细列表
     */
    @Override
    public void onGetQuotationLocationSuccess(QuotationLocation quotationLocation) {
        if (CODE_SUCCEED != quotationLocation.getCode()) {
            return;
        }
        mLocationPlacementList.clear();
        DataBean data = quotationLocation.getData();
        if (null == data) {
            mLocationPlacementAdapter.notifyDataSetChanged();
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            mLocationPlacementAdapter.notifyDataSetChanged();
            return;
        }
        mLocationPlacementList.addAll(list);
        mLocationPlacementAdapter.notifyDataSetChanged();
    }

    /**
     * 在获取摆放清单列表成功时回调
     *
     * @param placementQrQuotationList 摆放清单列表
     */
    @Override
    public void onGetPlacementListSuccess(PlacementQrQuotationList placementQrQuotationList) {
        if (CODE_SUCCEED != placementQrQuotationList.getCode()) {
            mContentLayout.setVisibility(View.GONE);
            mNoDataLayout.setVisibility(View.VISIBLE);
            return;
        }
        mPlacementList.clear();
        PlacementQrQuotationList.DataBean data = placementQrQuotationList.getData();
        if (null == data) {
            mContentLayout.setVisibility(View.GONE);
            mNoDataLayout.setVisibility(View.VISIBLE);
            mStayPlacementAdapter.notifyDataSetChanged();
            return;
        }
        List<PlacementQrQuotationList.DataBean.ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            mContentLayout.setVisibility(View.GONE);
            mNoDataLayout.setVisibility(View.VISIBLE);
            mStayPlacementAdapter.notifyDataSetChanged();
            return;
        }
        mPlacementList.addAll(list);
        mContentLayout.setVisibility(View.VISIBLE);
        mNoDataLayout.setVisibility(View.GONE);
        mStayPlacementAdapter.notifyDataSetChanged();
    }

    /**
     * 在获取报价清单列表成功时回调
     *
     * @param placementQrQuotationList 报价清单列表
     */
    @Override
    public void onGetQuotationListSuccess(PlacementQrQuotationList placementQrQuotationList) {
        if (CODE_SUCCEED != placementQrQuotationList.getCode()) {
            MessageUtil.showMessage(placementQrQuotationList.getMsg());
            return;
        }
        String payTypeString = null;
        if (1 == payType) {
            payTypeString = getResources().getString(R.string.month);
        } else if (2 == payType) {
            payTypeString = getResources().getString(R.string.year);
        } else if (3 == payType) {
            payTypeString = getResources().getString(R.string.day);
        }
        // 重置价格为0
        String text = YiBaiApplication.getCurrencySymbol() + 0.00;
        if (!TextUtils.isEmpty(payTypeString)) {
            text = text + "/" + payTypeString;
        }
        mTradePriceTextView.setText(text);
        mQuotationList.clear();
        PlacementQrQuotationList.DataBean data = placementQrQuotationList.getData();
        if (null == data) {
            return;
        }
        List<PlacementQrQuotationList.DataBean.ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        mQuotationList.addAll(list);
        mQuotationPresenter.calculatedTotalPrice(payType, taxRate, discountMoney, mQuotationList);
    }

    /**
     * 在创建报价位置成功时回调
     *
     * @param createQuotationLocation 创建报价位置时服务器端返回的数据
     */
    @Override
    public void onCreateQuotationLocationSuccess(CreateQuotationLocation createQuotationLocation) {
        MessageUtil.showMessage(createQuotationLocation.getMsg());
        if (CODE_SUCCEED != createQuotationLocation.getCode()) {
            return;
        }
        mQuotationPresenter.getQuotationLocation();
    }

    /**
     * 在删除报价位置成功时回调
     *
     * @param deleteQuotationLocation 删除报价位置信息时服务器端返回的数据
     */
    @Override
    public void onDeleteQuotationLocationSuccess(DeleteQuotationLocation deleteQuotationLocation) {
        MessageUtil.showMessage(deleteQuotationLocation.getMsg());
        if (CODE_SUCCEED != deleteQuotationLocation.getCode()) {
            return;
        }
        mQuotationPresenter.getQuotationLocation();
        mQuotationPresenter.getQuotationList();
    }

    /**
     * 在修改报价位置信息成功时回调
     *
     * @param updateQuotationLocation 修改报价位置信息时服务器端返回的数据
     */
    @Override
    public void onUpdateQuotationLocationSuccess(UpdateQuotationLocation updateQuotationLocation) {
        if (CODE_SUCCEED != updateQuotationLocation.getCode()) {
            MessageUtil.showMessage(updateQuotationLocation.getMsg());
            return;
        }
        UpdateQuotationLocation.DataBean data = updateQuotationLocation.getData();
        if (null == data) {
            return;
        }
        ListBean listBean = mLocationPlacementList.get(position);
        String designPic = data.getDesign_pic();
        if (!TextUtils.isEmpty(designPic)) {
            listBean.setDesign_pic(designPic);
        }
        String name = data.getName();
        if (!TextUtils.isEmpty(name)) {
            listBean.setQuote_place_name(name);
        }
        mLocationPlacementAdapter.notifyDataSetChanged();
    }

    /**
     * 在修改报价位置中产品数量成功时回调
     *
     * @param updateQuotationInfo 修改报价位置中产品数量时服务器端返回的数据
     */
    @Override
    public void onUpdateQuotationInfoSuccess(UpdateQuotationInfo updateQuotationInfo) {
        MessageUtil.showMessage(updateQuotationInfo.getMsg());
        int code = updateQuotationInfo.getCode();
        UpdateQuotationInfo.DataBean data = updateQuotationInfo.getData();
        if (null == data) {
            return;
        }
        int quotePlaceId = data.getQuote_place_id();
        int quoteId = data.getQuote_id();
        int number = data.getNum();
        if (CODE_SUCCEED == code && 0 == number) {
            mQuotationPresenter.getQuotationLocation();
            mQuotationPresenter.getQuotationList();
            return;
        }
        flag:
        for (ListBean listBean : mLocationPlacementList) {
            int quotePlaceIds = listBean.getQuote_place_id();
            if (quotePlaceId != quotePlaceIds) {
                continue;
            }
            List<QuotelistBean> quoteList = listBean.getQuotelist();
            if (null == quoteList) {
                continue;
            }
            for (QuotelistBean quotelistBean : quoteList) {
                int quoteIds = quotelistBean.getQuote_id();
                if (quoteId != quoteIds) {
                    continue;
                }
                if (CODE_SUCCEED == code) {
                    quotelistBean.setNum(number);
                } else {
                    int num = quotelistBean.getNum();
                    quotelistBean.setNum(num - 1);
                }
                break flag;
            }
        }
        mLocationPlacementAdapter.notifyDataSetChanged();
        mQuotationPresenter.getQuotationList();
    }

    /**
     * 在将待摆放清单加入到报价位置成功时回调
     *
     * @param addQuotationLocation 待摆放清单加入到报价位置时服务器端返回的数据
     */
    @Override
    public void onAddQuotationLocationSuccess(AddQuotationLocation addQuotationLocation) {
        MessageUtil.showMessage(addQuotationLocation.getMsg());
        if (CODE_SUCCEED != addQuotationLocation.getCode()) {
            return;
        }
        AddQuotationLocation.DataBean data = addQuotationLocation.getData();
        if (null == data) {
            return;
        }

        // 当前选中位置的id
        int quotePlaceId = data.getQuote_place_id();
        // 清单id
        int quoteId = data.getQuote_id();
        // 获取将待摆放清单加入到报价位置的信息
        ListBean locationPlacement = null;
        if (null != mLocationPlacementList && mLocationPlacementList.size() > 0) {
            for (ListBean listBean : mLocationPlacementList) {
                if (quotePlaceId == listBean.getQuote_place_id()) {
                    locationPlacement = listBean;
                    break;
                }
            }
        }
        if (null == locationPlacement) {
            return;
        }
        // 判断之前是否添加过了,如果有直接数量加1
        boolean exist = false;
        // 获取该位置信息下的位置明细
        List<QuotelistBean> quoteList = locationPlacement.getQuotelist();
        if (null != quoteList && quoteList.size() > 0) {
            for (QuotelistBean quotelistBean : quoteList) {
                if (quoteId == quotelistBean.getQuote_id()) {
                    int num = quotelistBean.getNum();
                    quotelistBean.setNum(num + 1);
                    exist = true;
                    break;
                }
            }
        }
        // 之前没有添加过,就创建一个数据,添加到位置摆放清单
        if (!exist) {
            // 获取之前提交的待摆放清单数据
            PlacementQrQuotationList.DataBean.ListBean placement = null;
            if (null != mPlacementList && mPlacementList.size() > 0) {
                for (PlacementQrQuotationList.DataBean.ListBean listBean : mPlacementList) {
                    if (quoteId == listBean.getQuote_id()) {
                        placement = listBean;
                    }
                }
            }
            if (null != placement) {
                QuotelistBean quote = new QuotelistBean(
                        placement.getQuote_id(),
                        placement.getFirst_name(),
                        placement.getSecond_name(),
                        placement.getPic(),
                        placement.getPrice(),
                        placement.getMonth_rent(),
                        placement.getYear_rent(),
                        1);
                // 添加之前提交的"待摆放清单数据"到"位置摆放清单"中
                if (null != quoteList) {
                    quoteList.add(quote);
                }
                locationPlacement.setQuotelist(quoteList);
            }
        }
        mLocationPlacementAdapter.notifyDataSetChanged();
        mQuotationPresenter.getQuotationList();
    }

    /**
     * 在创建报价单成功时回调
     *
     * @param createQuotationOrder 创建报价单时服务器端返回的数据
     */
    @Override
    public void onCreateQuotationOrderSuccess(CreateQuotationOrder createQuotationOrder) {
        MessageUtil.showMessage(createQuotationOrder.getMsg());
        if (CODE_SUCCEED != createQuotationOrder.getCode()) {
            return;
        }
        mQuotationPresenter.getQuotationLocation();
        mQuotationPresenter.getPlacementList();
        mQuotationPresenter.getQuotationList();

        CreateQuotationOrder.DataBean data = createQuotationOrder.getData();
        if (null == data) {
            return;
        }
        String share = data.getShare();
        String customerName = data.getCname();
        String orderNumber = data.getOrder_number();
        if (TextUtils.isEmpty(share)) {
            return;
        }
        Intent intent = new Intent(mActivity, BrowserActivity.class);
        intent.putExtra(URL, share);
        intent.putExtra(TYPE, ORDER_SHARE_URL_TYPE);
        intent.putExtra(CUSTOMER_NAME, customerName);
        intent.putExtra(ORDER_NUMBER, orderNumber);
        startActivity(intent);
    }

    /**
     * 在修改清单产品价格成功时回调
     *
     * @param updateQuotationPrice 修改清单产品价格时服务器端返回的数据
     */
    @Override
    public void onUpdateQuotationPriceSuccess(UpdateQuotationPrice updateQuotationPrice) {
        MessageUtil.showMessage(updateQuotationPrice.getMsg());
        if (CODE_SUCCEED != updateQuotationPrice.getCode()) {
            return;
        }
        mQuotationPresenter.getQuotationList();
    }

    /**
     * 在删除待摆放清单产品成功时回调
     *
     * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
     */
    @Override
    public void onDeletePlacementListSuccess(DeletePlacement deletePlacement) {
        MessageUtil.showMessage(deletePlacement.getMsg());
        if (CODE_SUCCEED == deletePlacement.getCode()) {
            mQuotationPresenter.getPlacementList();
        }
        recoverProductsToBePlacedLayout();
    }

    /**
     * 恢复“客户预选”默认的View样式
     */
    private void recoverProductsToBePlacedLayout() {
        mAllCheckTextView.setVisibility(View.GONE);
        mDeleteTextView.setVisibility(View.GONE);
        mCancelTextView.setVisibility(View.GONE);
        for (PlacementQrQuotationList.DataBean.ListBean listBean : mPlacementList) {
            listBean.setSelectCheckBox(false);
            listBean.setShowCheckBox(false);
        }
        mStayPlacementAdapter.notifyDataSetChanged();
    }

    /**
     * EventBus
     * 接收用户从{@link PictureViewActivity} 传递过来的用户选择的图片数据
     *
     * @param selectImage 选择以模拟图片数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pictureViewActivityReturnImageData(SelectImage selectImage) {
        ListBean listBean = mLocationPlacementList.get(position);
        int quotePlaceId = listBean.getQuote_place_id();
        File file = selectImage.getFile();
        mQuotationPresenter.updateQuotationLocation(quotePlaceId, null, file);
    }

    /**
     * EventBus
     * 接收用户从{@link QuotationDetailsFragment#onQuotationAgainSuccess(QuotationAgain)}
     * 传递过来的数据(在报价单再次报价成功时)
     *
     * @param quotationAgain 报价单再次报价时服务器端返回的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQuotationAgainSuccess(QuotationAgain quotationAgain) {
        QuotationAgain.DataBean data = quotationAgain.getData();
        if (null == data) {
            return;
        }
        // 初始化数据
        payType = data.getPay_type();
        taxRate = data.getTax();
        discountMoney = data.getDistrict_money();
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

    /**
     * 友盟统计Fragment页面
     */
    @Override
    public void onResume() {
        super.onResume();
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
        if (null != mQuotationPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mQuotationPresenter.onDetachView();
            mQuotationPresenter = null;
        }
    }
}