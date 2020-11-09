package com.ybw.yibai.module.quotation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.adapter.QuotationAdapter;
import com.ybw.yibai.common.bean.AddQuotationLocation;
import com.ybw.yibai.common.bean.CreateQuotationLocation;
import com.ybw.yibai.common.bean.CreateQuotationOrder;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.bean.DeleteQuotationLocation;
import com.ybw.yibai.common.bean.PlacementQrQuotationList;
import com.ybw.yibai.common.bean.QuotationLocation;
import com.ybw.yibai.common.bean.UpdateQuotationInfo;
import com.ybw.yibai.common.bean.UpdateQuotationLocation;
import com.ybw.yibai.common.bean.UpdateQuotationPrice;
import com.ybw.yibai.common.bean.VerifyPassword;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.CustomDialog;
import com.ybw.yibai.module.quotation.QuotationContract.CallBack;
import com.ybw.yibai.module.quotation.QuotationContract.QuotationModel;
import com.ybw.yibai.module.quotation.QuotationContract.QuotationPresenter;
import com.ybw.yibai.module.quotation.QuotationContract.QuotationView;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.AUTHORIZATION_CODE;
import static com.ybw.yibai.common.constants.Preferences.SAVE_AUTHORIZATION;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * 报价Presenter实现类
 *
 * @author sjl
 * @date 2019/10/30
 */
public class QuotationPresenterImpl extends BasePresenterImpl<QuotationView> implements QuotationPresenter,
        CallBack, View.OnClickListener, RadioGroup.OnCheckedChangeListener, QuotationAdapter.OnPriceClickListener {

    private final String TAG = "QuotationPresenterImpl";

    /**
     * 报价方式(默认月租)
     * 0:购买
     * 1:月租
     * 2:年租
     * 3:日租
     */
    private int payType = 1;

    /**
     * 税率EditText
     */
    private EditText mTaxRateEditText;

    /**
     * 优惠EditText
     */
    private EditText mDiscountsEditText;

    /**
     * 显示修改"场景名称"的PopupWindow
     */
    private PopupWindow mSceneNamePopupWindow;

    /**
     * 报价设置的PopupWindow
     */
    private PopupWindow mQuotationSetPopupWindow;

    /**
     * 显示报价列表的适配器
     */
    private QuotationAdapter mAdapter;

    /**
     * 报价清单列表
     */
    private List<PlacementQrQuotationList.DataBean.ListBean> mQuotationList;

    /**
     * 显示请输入授权码的PopupWindow
     */
    private PopupWindow mAuthorizationCodePopupWindow;

    /**
     * 修改盆栽报价的PopupWindow
     */
    private PopupWindow mEditPricePopupWindow;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private QuotationView mQuotationView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private QuotationModel mQuotationModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public QuotationPresenterImpl(QuotationView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mQuotationView = getView();
        mQuotationModel = new QuotationModelImpl();

        mQuotationList = new ArrayList<>();
    }

    /**
     * 获取报价位置明细列表
     */
    @Override
    public void getQuotationLocation() {
        mQuotationModel.getQuotationLocation(this);
    }

    /**
     * 在获取报价位置明细列表成功时回调
     *
     * @param quotationLocation 报价位置明细列表
     */
    @Override
    public void onGetQuotationLocationSuccess(QuotationLocation quotationLocation) {
        mQuotationView.onGetQuotationLocationSuccess(quotationLocation);
    }

    /**
     * 获取摆放清单列表
     */
    @Override
    public void getPlacementList() {
        mQuotationModel.getPlacementList(this);
    }

    /**
     * 在获取摆放清单列表成功时回调
     *
     * @param placementQrQuotationList 摆放清单列表
     */
    @Override
    public void onGetPlacementListSuccess(PlacementQrQuotationList placementQrQuotationList) {
        mQuotationView.onGetPlacementListSuccess(placementQrQuotationList);
    }

    /**
     * 获取报价清单列表
     */
    @Override
    public void getQuotationList() {
        mQuotationModel.getQuotationList(this);
    }

    /**
     * 在获取报价清单列表成功时回调
     *
     * @param placementQrQuotationList 报价清单列表
     */
    @Override
    public void onGetQuotationListSuccess(PlacementQrQuotationList placementQrQuotationList) {
        mQuotationView.onGetQuotationListSuccess(placementQrQuotationList);
        updateQuotationListData(placementQrQuotationList);
    }

    /**
     * 每一次获取报价清单列表成功时,就更新Adapter数据
     *
     * @param placementQrQuotationList 报价清单列表
     */
    private void updateQuotationListData(PlacementQrQuotationList placementQrQuotationList) {
        if (CODE_SUCCEED != placementQrQuotationList.getCode()) {
            return;
        }
        mQuotationList.clear();
        PlacementQrQuotationList.DataBean data = placementQrQuotationList.getData();
        if (null == data) {
            if (null != mAdapter) {
                mAdapter.notifyDataSetChanged();
            }
            return;
        }
        List<PlacementQrQuotationList.DataBean.ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            if (null != mAdapter) {
                mAdapter.notifyDataSetChanged();
            }
            return;
        }
        mQuotationList.addAll(list);
        if (null != mQuotationList && mQuotationList.size() > 0) {
            for (PlacementQrQuotationList.DataBean.ListBean listBean : mQuotationList) {
                listBean.setMode(payType);
            }
        }
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 创建报价位置
     *
     * @param name 位置名称
     */
    @Override
    public void createQuotationLocation(String name) {
        mQuotationModel.createQuotationLocation(name, this);
    }

    /**
     * 在创建报价位置成功时回调
     *
     * @param createQuotationLocation 创建报价位置时服务器端返回的数据
     */
    @Override
    public void onCreateQuotationLocationSuccess(CreateQuotationLocation createQuotationLocation) {
        mQuotationView.onCreateQuotationLocationSuccess(createQuotationLocation);
    }

    /**
     * 删除报价位置
     *
     * @param quotePlaceId 报价位置id
     */
    @Override
    public void deleteQuotationLocation(int quotePlaceId) {
        mQuotationModel.deleteQuotationLocation(quotePlaceId, this);
    }

    /**
     * 在删除报价位置成功时回调
     *
     * @param deleteQuotationLocation 删除报价位置信息时服务器端返回的数据
     */
    @Override
    public void onDeleteQuotationLocationSuccess(DeleteQuotationLocation deleteQuotationLocation) {
        mQuotationView.onDeleteQuotationLocationSuccess(deleteQuotationLocation);
    }

    /**
     * 修改报价位置信息
     *
     * @param quotePlaceId 报价位置id
     * @param name         位置名称
     * @param file         设计图
     */
    @Override
    public void updateQuotationLocation(int quotePlaceId, String name, File file) {
        if (null != file && file.exists()) {
            Map<String, RequestBody> params = new HashMap<>();
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
            params.put("design_pic\"; filename=\"" + file.getName(), requestBody);
            mQuotationModel.updateQuotationLocation(quotePlaceId, name, params, this);
        } else {
            mQuotationModel.updateQuotationLocation(quotePlaceId, name, null, this);
        }
    }

    /**
     * 在修改报价位置信息成功时回调
     *
     * @param updateQuotationLocation 修改报价位置信息时服务器端返回的数据
     */
    @Override
    public void onUpdateQuotationLocationSuccess(UpdateQuotationLocation updateQuotationLocation) {
        mQuotationView.onUpdateQuotationLocationSuccess(updateQuotationLocation);
    }

    /**
     * 修改报价位置中产品数量
     *
     * @param quotePlaceId 报价位置id
     * @param quoteId      清单产品id
     * @param number       产品数量
     */
    @Override
    public void updateQuotationInfo(int quotePlaceId, int quoteId, int number) {
        mQuotationModel.updateQuotationInfo(quotePlaceId, quoteId, number, this);
    }

    /**
     * 在修改报价位置中产品数量成功时回调
     *
     * @param updateQuotationInfo 修改报价位置中产品数量时服务器端返回的数据
     */
    @Override
    public void onUpdateQuotationInfoSuccess(UpdateQuotationInfo updateQuotationInfo) {
        mQuotationView.onUpdateQuotationInfoSuccess(updateQuotationInfo);
    }

    /**
     * 待摆放清单加入到报价位置
     *
     * @param quoteId      清单id
     * @param quotePlaceId 报价位置id
     */
    @Override
    public void addQuotationLocation(int quoteId, int quotePlaceId) {
        mQuotationModel.addQuotationLocation(quoteId, quotePlaceId, this);
    }

    /**
     * 在将待摆放清单加入到报价位置成功时回调
     *
     * @param addQuotationLocation 待摆放清单加入到报价位置时服务器端返回的数据
     */
    @Override
    public void onAddQuotationLocationSuccess(AddQuotationLocation addQuotationLocation) {
        mQuotationView.onAddQuotationLocationSuccess(addQuotationLocation);
    }

    /**
     * 创建报价单
     *
     * @param payType       支付模式id(支付模式0购买1月租2年租)默认为1
     * @param taxRate       税点
     * @param discountMoney 优惠金额
     */
    @Override
    public void createQuotationOrder(int payType, double taxRate, double discountMoney) {
        mQuotationModel.createQuotationOrder(payType, taxRate, discountMoney, this);
    }

    /**
     * 在创建报价单成功时回调
     *
     * @param createQuotationOrder 创建报价单时服务器端返回的数据
     */
    @Override
    public void onCreateQuotationOrderSuccess(CreateQuotationOrder createQuotationOrder) {
        mQuotationView.onCreateQuotationOrderSuccess(createQuotationOrder);
        // 创建报价单成功时清空税率/优惠
        if (CODE_SUCCEED != createQuotationOrder.getCode()) {
            return;
        }
        if (null != mTaxRateEditText) {
            mTaxRateEditText.setText("");
        }
        if (null != mDiscountsEditText) {
            mDiscountsEditText.setText("");
        }
    }

    /**
     * 计算报价清单里面盆栽的总价格
     *
     * @param payType       支付模式id(支付模式0购买1月租2年租)默认为1
     * @param taxRate       税点
     * @param discountMoney 优惠金额
     * @param quotationList 报价清单列表
     */
    @SuppressLint("DefaultLocale")
    @Override
    public void calculatedTotalPrice(int payType, double taxRate, double discountMoney,
                                     List<PlacementQrQuotationList.DataBean.ListBean> quotationList) {
        // 税点按小数显示
        double tax = new BigDecimal(taxRate / 100).setScale(2, ROUND_HALF_UP).doubleValue();
        // 总价格
        double totalPrice = 0;
        for (PlacementQrQuotationList.DataBean.ListBean listBean : quotationList) {
            float price = listBean.getPrice();
            float monthRent = listBean.getMonth_rent();
            int number = listBean.getNum();
            if (0 == payType) {
                // 购买
                // 购买价格
                totalPrice += price * number;
            } else {
                // 日租/月租/年租
                // 月租价格
                totalPrice += monthRent * number;
            }
        }
        if (3 == payType) {
            // 日租 = 月租 / 30
            BigDecimal bigDecimal = new BigDecimal(totalPrice / 30);
            totalPrice = bigDecimal.setScale(2, ROUND_HALF_UP).doubleValue();
        } else if (2 == payType) {
            // 年租 = 月租 * 12
            totalPrice = totalPrice * 12;
        }
        // 当总价大于0,那么总价格需要减去优惠金额
        if (0 < totalPrice) {
            totalPrice = totalPrice * (1 + tax) - discountMoney;
        }
        if (0 >= totalPrice) {
            totalPrice = 0;
        }
        // 保留两位小数
        String tp = String.format("%.2f", totalPrice);
        mQuotationView.onCalculatedTotalPriceSuccess(tp);
    }

    /**
     * 显示修改"场景名称"的PopupWindow
     *
     * @param v    RootView
     * @param name 旧位置名
     */
    @Override
    public void displaySceneNamePopupWindow(View v, String name) {
        Fragment fragment = (Fragment) mQuotationView;
        Activity activity = fragment.getActivity();
        if (null == activity) {
            return;
        }

        View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_scene_name_layout, null);
        mSceneNamePopupWindow = new PopupWindow(view, MATCH_PARENT, MATCH_PARENT);

        EditText editText = view.findViewById(R.id.editText);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button determineButton = view.findViewById(R.id.determineButton);
        cancelButton.setOnClickListener(vie -> mSceneNamePopupWindow.dismiss());
        determineButton.setOnClickListener(vie -> {
            String newName = editText.getText().toString().trim();
            mQuotationView.returnNewSceneName(newName);
            mSceneNamePopupWindow.dismiss();
        });

        if (!TextUtils.isEmpty(name)) {
            editText.setText(name);
            editText.setSelection(name.length());
        }

        // 在弹出PopupWindow设置屏幕透明度
        OtherUtil.setBackgroundAlpha(activity, 0.6f);

        mSceneNamePopupWindow.setFocusable(true);
        mSceneNamePopupWindow.setOutsideTouchable(true);
        mSceneNamePopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        // 添加PopupWindow窗口关闭事件
        mSceneNamePopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        mSceneNamePopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    /**
     * 显示是否删除该盆栽
     *
     * @param name         盆栽名称
     * @param quotePlaceId 报价位置id
     * @param quoteId      清单产品id
     */
    @Override
    public void displayAreYouSureToDeleteTheBonsaiDialog(String name, int quotePlaceId, int quoteId) {
        Fragment fragment = (Fragment) mQuotationView;
        Activity activity = fragment.getActivity();
        if (null != activity) {
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            CustomDialog customDialog = new CustomDialog(activity);
            customDialog.setMessage(activity.getResources().getString(R.string.are_you_sure_to_delete) + name);
            customDialog.setYesOnclickListener(activity.getResources().getString(R.string.determine),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                        mQuotationModel.updateQuotationInfo(quotePlaceId, quoteId, 0, this);
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
     * 显示修改"盆栽数量"的PopupWindow
     *
     * @param v            RootView
     * @param quotePlaceId 报价位置id
     * @param quoteId      清单产品id
     * @param number       旧盆栽数量
     */
    @Override
    public void displayModifyBonsaiNumberPopupWindow(View v, int quotePlaceId, int quoteId, int number) {
        Fragment fragment = (Fragment) mQuotationView;
        Activity activity = fragment.getActivity();

        View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_modify_bonsai_number_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, MATCH_PARENT, MATCH_PARENT);

        EditText editText = view.findViewById(R.id.editText);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button determineButton = view.findViewById(R.id.determineButton);

        cancelButton.setOnClickListener(vie -> popupWindow.dismiss());
        determineButton.setOnClickListener(vie -> {
            String string = editText.getText().toString().trim();
            if (TextUtils.isEmpty(string)) {
                return;
            }
            popupWindow.dismiss();
            int num = Integer.parseInt(string);
            mQuotationModel.updateQuotationInfo(quotePlaceId, quoteId, num, QuotationPresenterImpl.this);
        });

        editText.setText(String.valueOf(number));
        editText.setSelection(editText.getText().toString().length());

        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // 添加PopupWindow窗口关闭事件
        popupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    /**
     * 显示报价设置的PopupWindow
     *
     * @param v    要显示的位置
     * @param list 报价清单列表
     */
    @Override
    public void displayQuotationSetPopupWindow(View v, List<PlacementQrQuotationList.DataBean.ListBean> list) {
        Fragment fragment = (Fragment) mQuotationView;
        Activity activity = fragment.getActivity();

        if (null == mQuotationSetPopupWindow) {
            View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_quotation_set_layout, null);
            mQuotationSetPopupWindow = new PopupWindow(view, MATCH_PARENT, MATCH_PARENT);

            View topView = view.findViewById(R.id.topView);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            RadioGroup typeRadioGroup = view.findViewById(R.id.typeRadioGroup);
            mTaxRateEditText = view.findViewById(R.id.taxRateEditText);
            mDiscountsEditText = view.findViewById(R.id.discountAmountEditText);
            View bottomView = view.findViewById(R.id.bottomView);

            // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            // 给RecyclerView设置布局管理器(必须设置)
            recyclerView.setLayoutManager(manager);

            mQuotationList.clear();
            if (null != list && list.size() > 0) {
                mQuotationList.addAll(list);
            }
            if (null != mQuotationList && mQuotationList.size() > 0) {
                for (PlacementQrQuotationList.DataBean.ListBean listBean : mQuotationList) {
                    listBean.setMode(payType);
                }
            }
            mAdapter = new QuotationAdapter(activity, mQuotationList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnPriceClickListener(this);

            topView.setOnClickListener(this);
            bottomView.setOnClickListener(this);
            typeRadioGroup.setOnCheckedChangeListener(this);
            initTaxRateEditTextEvent();
            initDiscountsEditTextEvent();
            initQuotationSetPopupWindowOnDismissListener();

            mQuotationSetPopupWindow.setFocusable(true);
            mQuotationSetPopupWindow.setOutsideTouchable(true);
            mQuotationSetPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mQuotationSetPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        } else {
            initQuotationSetPopupWindowOnDismissListener();
            mQuotationSetPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 显示报价设置的PopupWindow
     *
     * @param v             要显示的位置
     * @param payType       报价方式
     * @param taxRate       税率
     * @param discountMoney 优惠金额
     * @param list          报价清单列表
     */
    @Override
    public void displayQuotationSetPopupWindow(View v, int payType, double taxRate, double discountMoney,
                                               List<PlacementQrQuotationList.DataBean.ListBean> list) {
        Fragment fragment = (Fragment) mQuotationView;
        Activity activity = fragment.getActivity();

        View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_quotation_set_layout, null);
        mQuotationSetPopupWindow = new PopupWindow(view, MATCH_PARENT, MATCH_PARENT);

        View topView = view.findViewById(R.id.topView);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RadioGroup typeRadioGroup = view.findViewById(R.id.typeRadioGroup);
        mTaxRateEditText = view.findViewById(R.id.taxRateEditText);
        mDiscountsEditText = view.findViewById(R.id.discountAmountEditText);
        View bottomView = view.findViewById(R.id.bottomView);

        int index;
        if (1 == payType) {
            // 月租
            index = 1;
        } else if (2 == payType) {
            // 年租
            index = 2;
        } else if (3 == payType) {
            // 日租
            index = 0;
        } else {
            // 购买
            index = 3;
        }
        RadioButton radioButton = (RadioButton) typeRadioGroup.getChildAt(index);
        radioButton.setChecked(true);
        mTaxRateEditText.setText(String.valueOf(taxRate));
        mDiscountsEditText.setText(String.valueOf(discountMoney));

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // 给RecyclerView设置布局管理器(必须设置)
        recyclerView.setLayoutManager(manager);

        mQuotationList.clear();
        if (null != list && list.size() > 0) {
            mQuotationList.addAll(list);
        }
        if (null != mQuotationList && mQuotationList.size() > 0) {
            for (PlacementQrQuotationList.DataBean.ListBean listBean : mQuotationList) {
                listBean.setMode(payType);
            }
        }
        mAdapter = new QuotationAdapter(activity, mQuotationList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnPriceClickListener(this);

        topView.setOnClickListener(this);
        bottomView.setOnClickListener(this);
        typeRadioGroup.setOnCheckedChangeListener(this);
        initTaxRateEditTextEvent();
        initDiscountsEditTextEvent();
        initQuotationSetPopupWindowOnDismissListener();

        mQuotationSetPopupWindow.setFocusable(true);
        mQuotationSetPopupWindow.setOutsideTouchable(true);
        mQuotationSetPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mQuotationSetPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    private void initTaxRateEditTextEvent() {
        Fragment fragment = (Fragment) mQuotationView;
        // 1s自动发送税率
        Observable<CharSequence> observable = RxTextView
                .textChanges(mTaxRateEditText)
                .skip(1)
                .debounce(1, TimeUnit.SECONDS);
        Observer<CharSequence> observer = new Observer<CharSequence>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CharSequence charSequence) {
                String taxRateString = charSequence.toString();
                if (TextUtils.isEmpty(taxRateString)) {
                    mQuotationView.onGetTaxRateSuccess(0);
                    return;
                }
                double taxRate = Double.parseDouble(taxRateString);
                if (100 < taxRate) {
                    mTaxRateEditText.setText("");
                    mQuotationView.onGetTaxRateSuccess(0);
                    MessageUtil.showMessage(fragment.getResources().getString(R.string.the_tax_rate_cannot_be_greater_than_one_hundred_percent));
                } else {
                    mQuotationView.onGetTaxRateSuccess(taxRate);
                }
            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onComplete() {

            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void initDiscountsEditTextEvent() {
        // 1s自动发送优惠价格
        Observable<CharSequence> observable = RxTextView
                .textChanges(mDiscountsEditText)
                .skip(1)
                .debounce(1, TimeUnit.SECONDS);
        Observer<CharSequence> observer = new Observer<CharSequence>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CharSequence charSequence) {
                String discountString = charSequence.toString();
                if (TextUtils.isEmpty(discountString)) {
                    mQuotationView.onGetDiscountMoneySuccess(0);
                } else {
                    double discountMoney = Double.parseDouble(discountString);
                    mQuotationView.onGetDiscountMoneySuccess(discountMoney);
                }
            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onComplete() {

            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void initQuotationSetPopupWindowOnDismissListener() {
        Fragment fragment = (Fragment) mQuotationView;
        // 添加PopupWindow窗口关闭事件
        mQuotationSetPopupWindow.setOnDismissListener(() -> {
            String discountString = mDiscountsEditText.getText().toString().trim();
            if (TextUtils.isEmpty(discountString)) {
                mQuotationView.onGetDiscountMoneySuccess(0);
            } else {
                double discountMoney = Double.parseDouble(discountString);
                mQuotationView.onGetDiscountMoneySuccess(discountMoney);
            }

            String taxRateString = mTaxRateEditText.getText().toString().trim();
            if (TextUtils.isEmpty(taxRateString)) {
                mQuotationView.onGetTaxRateSuccess(0);
                return;
            }
            double taxRate = Double.parseDouble(taxRateString);
            if (100 < taxRate) {
                mTaxRateEditText.setText("");
                mQuotationView.onGetTaxRateSuccess(0);
                MessageUtil.showMessage(fragment.getResources().getString(R.string.the_tax_rate_cannot_be_greater_than_one_hundred_percent));
            } else {
                mQuotationView.onGetTaxRateSuccess(taxRate);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (null != mQuotationSetPopupWindow && mQuotationSetPopupWindow.isShowing()) {
            mQuotationSetPopupWindow.dismiss();
        }
    }

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

        if (null != mQuotationList && mQuotationList.size() > 0) {
            for (PlacementQrQuotationList.DataBean.ListBean listBean : mQuotationList) {
                listBean.setMode(payType);
            }
        }
        mAdapter.notifyDataSetChanged();
        mQuotationView.onCheckedChanged(group, checkedId);
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
        mQuotationView.onPriceClick(position, mode, price);
    }

    /**
     * 显示请输入授权码的PopupWindow
     *
     * @param v 界面Root布局
     */
    @Override
    public void displayAuthorizationCodePopupWindow(View v) {
        Fragment fragment = (Fragment) mQuotationView;
        Activity activity = fragment.getActivity();
        if (null == activity) {
            return;
        }
        SharedPreferences sharedPreferences = activity.getSharedPreferences(USER_INFO, MODE_PRIVATE);
        boolean saveAuthorization = sharedPreferences.getBoolean(SAVE_AUTHORIZATION, false);
        String authCode = sharedPreferences.getString(AUTHORIZATION_CODE, null);

        View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_authorization_code_layout, null);
        mAuthorizationCodePopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        EditText editText = view.findViewById(R.id.editText);
        CheckBox checkBox = view.findViewById(R.id.checkbox);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button determineButton = view.findViewById(R.id.determineButton);

        if (saveAuthorization && !TextUtils.isEmpty(authCode)) {
            editText.setText(authCode);
            editText.setSelection(editText.getText().toString().length());
        }
        checkBox.setChecked(saveAuthorization);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 保存CheckBox状态
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean(SAVE_AUTHORIZATION, isChecked);
            edit.apply();
        });
        cancelButton.setOnClickListener(vie -> mAuthorizationCodePopupWindow.dismiss());
        determineButton.setOnClickListener(vie -> {
            String authorizationCode = editText.getText().toString().trim();
            if (checkBox.isChecked() && !TextUtils.isEmpty(authorizationCode)) {
                // 保存授权码
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString(AUTHORIZATION_CODE, authorizationCode);
                edit.apply();
            }
            mQuotationModel.verifyPassword(authorizationCode, this);
            mAuthorizationCodePopupWindow.dismiss();
        });

        mAuthorizationCodePopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mAuthorizationCodePopupWindow.setOutsideTouchable(true);
        mAuthorizationCodePopupWindow.setFocusable(true);

        // 设置一个动画效果
        mAuthorizationCodePopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

        // 添加PopupWindow窗口关闭事件
        mAuthorizationCodePopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        mAuthorizationCodePopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    /**
     * 在验证修改价格密码成功时回调
     *
     * @param verifyPassword 验证修改价格密码时服务器端返回的数据
     */
    @Override
    public void onVerifyPasswordSuccess(VerifyPassword verifyPassword) {
        mQuotationView.onVerifyPasswordSuccess(verifyPassword);
    }

    /**
     * 显示修改盆栽报价的PopupWindow
     *
     * @param v        界面Root布局
     * @param mode     报价方式
     * @param oldPrice 旧报价
     */
    @Override
    public void displayEditPricePopupWindow(View v, int mode, double oldPrice) {
        Fragment fragment = (Fragment) mQuotationView;
        Activity activity = ((Fragment) mQuotationView).getActivity();
        if (null == activity) {
            return;
        }

        View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_edit_price_layout, null);
        mEditPricePopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        EditText editText = view.findViewById(R.id.editText);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button determineButton = view.findViewById(R.id.determineButton);

        if (0 == mode) {
            titleTextView.setText(fragment.getResources().getString(R.string.modify_buy_price));
            editText.setHint(fragment.getResources().getString(R.string.enter_new_buy_price));
        } else if (1 == mode || 2 == mode) {
            titleTextView.setText(fragment.getResources().getString(R.string.modify_month_rent_price));
            editText.setHint(fragment.getResources().getString(R.string.enter_new_month_rent_price));
        }

        editText.setText(String.valueOf(oldPrice));
        editText.setSelection(editText.getText().toString().length());
        cancelButton.setOnClickListener(vie -> mEditPricePopupWindow.dismiss());
        determineButton.setOnClickListener(vie -> {
            String price = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(price)) {
                mQuotationView.returnNewPrice(Double.parseDouble(price));
                mEditPricePopupWindow.dismiss();
            }
        });

        mEditPricePopupWindow.setFocusable(true);
        mEditPricePopupWindow.setOutsideTouchable(true);
        mEditPricePopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        // 添加PopupWindow窗口关闭事件
        mEditPricePopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        mEditPricePopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    /**
     * 修改清单产品价格
     *
     * @param quoteId 清单id
     * @param price   新价格
     * @param type    1售价2月租价
     */
    @Override
    public void updateQuotationPrice(int quoteId, double price, int type) {
        mQuotationModel.updateQuotationPrice(quoteId, price, type, this);
    }

    /**
     * 在修改清单产品价格成功时回调
     *
     * @param updateQuotationPrice 修改清单产品价格时服务器端返回的数据
     */
    @Override
    public void onUpdateQuotationPriceSuccess(UpdateQuotationPrice updateQuotationPrice) {
        mQuotationView.onUpdateQuotationPriceSuccess(updateQuotationPrice);
    }

    /**
     * 删除待摆放清单列表
     *
     * @param quoteIds 清单产品id,多个用英文逗号分隔
     */
    @Override
    public void deletePlacementList(String quoteIds) {
        mQuotationModel.deletePlacementList(quoteIds, this);
    }

    /**
     * 在删除待摆放清单产品成功时回调
     *
     * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
     */
    @Override
    public void onDeletePlacementListSuccess(DeletePlacement deletePlacement) {
        mQuotationView.onDeletePlacementListSuccess(deletePlacement);
    }
}