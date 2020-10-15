package com.ybw.yibai.module.details;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.ProductData;
import com.ybw.yibai.common.bean.ProductDetails;
import com.ybw.yibai.common.bean.ProductDetails.DataBean.SkuListBean;
import com.ybw.yibai.common.bean.ProductDetails.DataBean.SpecBean;
import com.ybw.yibai.common.bean.ProductDetails.DataBean.SpecBean.SonBean;
import com.ybw.yibai.common.bean.SimilarSKU;
import com.ybw.yibai.common.bean.UpdateSKUUseState;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.selectsspecview.OnSpecSelectedListener;
import com.ybw.yibai.common.widget.selectsspecview.SelectsSpecView;
import com.ybw.yibai.module.details.ProductDetailsContract.CallBack;
import com.ybw.yibai.module.details.ProductDetailsContract.ProductDetailsModel;
import com.ybw.yibai.module.details.ProductDetailsContract.ProductDetailsPresenter;
import com.ybw.yibai.module.details.ProductDetailsContract.ProductDetailsView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ybw.yibai.common.constants.Preferences.SERVICE;
import static com.ybw.yibai.common.utils.OtherUtil.checkListElementEqual;


/**
 * 盆栽详情界面Presenter实现类
 *
 * @author sjl
 * @date 2019/5/16
 */
public class ProductDetailsPresenterImpl extends BasePresenterImpl<ProductDetailsView> implements
        ProductDetailsPresenter, CallBack, View.OnClickListener, OnSpecSelectedListener {

    private static final String TAG = "ProductDetailsPresenterImpl";

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 显示选择规格的PopupWindow
     */
    private PopupWindow mSpecPopupWindow;

    /**
     * 显示产品价格的PopupWindow
     */
    private PopupWindow mPricePopupWindow;

    /**
     * 用户选择的规格
     * <p>
     * key   规格名称在列表中的位置
     * Value 具体规格值在列表中的位置
     */
    private Map<Integer, Integer> mSelectsSpecMap;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private ProductDetailsView mBonsaiDetailsView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private ProductDetailsModel mBonsaiDetailsModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public ProductDetailsPresenterImpl(ProductDetailsView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mBonsaiDetailsView = getView();
        mBonsaiDetailsModel = new ProductDetailsModelImpl();

        mSelectsSpecMap = new HashMap<>();
    }

    @Override
    public void addpurcart(ProductData productData) {
        mBonsaiDetailsModel.addPurcart(productData,this);
    }

    /**
     * 获取产品详情
     *
     * @param productId 盆栽
     */
    @Override
    public void getProductDetails(int productId) {
        mBonsaiDetailsModel.getProductDetails(productId, this);
    }

    /**
     * 在获取产品详情成功时回调
     *
     * @param productDetails 产品详情
     */
    @Override
    public void onGetProductDetailsSuccess(ProductDetails productDetails) {
        mBonsaiDetailsView.onGetProductDetailsSuccess(productDetails);
    }

    /**
     * 获取相似推荐sku列表
     *
     * @param skuId 产品SKUid
     */
    @Override
    public void getSimilarSKUList(int skuId) {
        mBonsaiDetailsModel.getSimilarSKUList(skuId, this);
    }

    /**
     * 在获取相似推荐sku列表成功时回调
     *
     * @param similarSKU 相似推荐sku列表
     */
    @Override
    public void onGetSimilarSKUListSuccess(SimilarSKU similarSKU) {
        mBonsaiDetailsView.onGetSimilarSKUListSuccess(similarSKU);
    }

    /**
     * 修改产品sku使用状态
     *
     * @param skuId 产品SKUid
     */
    @Override
    public void updateSKUUseState(int skuId) {
        mBonsaiDetailsModel.updateSKUUseState(skuId, this);
    }

    /**
     * 在修改产品sku使用状态成功时回调
     *
     * @param updateSKUUseState 修改产品sku使用状态时服务器端返回的数据
     */
    @Override
    public void onUpdateSKUUseStateSuccess(UpdateSKUUseState updateSKUUseState) {
        mBonsaiDetailsView.onUpdateSKUUseStateSuccess(updateSKUUseState);
    }

    /**
     * 获取一开始进入本界面的产品的规格
     *
     * @param skuId    当前产品的SKUid
     * @param specList 产品规格集合
     * @param skuList  产品Sku集合
     */
    @Override
    public void getInitialSelectsSpec(int skuId, List<SpecBean> specList, List<SkuListBean> skuList) {
        if (null == skuList || skuList.size() == 0) {
            return;
        }
        // 循环遍历产品Sku集合,获得当前产品的SKUid对应的specId集合
        List<Integer> specIdList = null;
        for (SkuListBean skuListBean : skuList) {
            int skuIds = skuListBean.getSku_id();
            if (skuId != skuIds) {
                continue;
            }
            specIdList = skuListBean.getSpec_id();
            mBonsaiDetailsView.returnSelectsProductInfo(skuListBean);
            break;
        }
        if (null == specIdList || specIdList.size() == 0) {
            return;
        }
        if (null == specList || specList.size() == 0) {
            return;
        }

        // 循环遍历产品规格集合,获得规格
        for (int i = 0; i < specList.size(); i++) {
            List<SonBean> sonList = specList.get(i).getSon();
            if (null == sonList || sonList.size() == 0) {
                continue;
            }
            // 循环遍历规格对象的ID如果与当前产品的specId集合里面的内容相同表明,当前产品的规格就是这个
            for (int j = 0; j < sonList.size(); j++) {
                SonBean sonBean = sonList.get(j);
                if (null == sonBean) {
                    continue;
                }
                for (Integer integer : specIdList) {
                    int id = sonBean.getId();
                    if (id != integer) {
                        continue;
                    }
                    sonBean.setSelected(true);
                    mSelectsSpecMap.put(i, j);
                }
            }
        }
        mBonsaiDetailsView.onGetSelectsSpecSuccess(mSelectsSpecMap);
    }

    /**
     * 根据用户选中的规格来获取规格对应的产品信息
     *
     * @param specIdList 用户选择的产品其规格ID的集合
     * @param skuList    产品Sku集合
     */
    @Override
    public void getProductInfoAccordingToSpec(List<Integer> specIdList, List<SkuListBean> skuList) {
        if (null == specIdList || specIdList.size() == 0) {
            return;
        }
        if (null == skuList || skuList.size() == 0) {
            return;
        }
        for (SkuListBean skuListBean : skuList) {
            List<Integer> specId = skuListBean.getSpec_id();
            if (!checkListElementEqual(specIdList, specId)) {
                continue;
            }
            mBonsaiDetailsView.returnSelectsProductInfo(skuListBean);
            break;
        }
    }

    /**
     * 显示产品规格PopupWindow
     *
     * @param rootLayout 界面Root布局
     * @param specList   当前植物规格列表
     */
    @Override
    public void displaySpecPopupWindow(View rootLayout, List<SpecBean> specList) {
        Activity activity = (Activity) mBonsaiDetailsView;
        if (mSpecPopupWindow == null) {
            View view = activity.getLayoutInflater().inflate(R.layout.popup_window_spec_list_layout, null);
            mSpecPopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            Button determineButton = view.findViewById(R.id.determineButton);
            determineButton.setOnClickListener(this);

            SelectsSpecView selectsSpecView = view.findViewById(R.id.selectsSpecView);
            // 设置侦听器要在setData()之前
            selectsSpecView.setOnSelectedListener(this);
            selectsSpecView.setData(specList);

            mSpecPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mSpecPopupWindow.setOutsideTouchable(true);
            mSpecPopupWindow.setFocusable(true);

            // 设置一个动画效果
            mSpecPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mSpecPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
            mSpecPopupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
        } else {
            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mSpecPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
            mSpecPopupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 显示产品价格PopupWindow
     *
     * @param rootLayout 界面Root布局
     * @param skuList    产品Sku
     */
    @Override
    public void displayPricePopupWindow(View rootLayout, SkuListBean skuList) {
        Activity activity = (Activity) mBonsaiDetailsView;
        if (mPricePopupWindow == null) {
            View view = activity.getLayoutInflater().inflate(R.layout.popup_window_price_list_layout, null);
            mPricePopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            double productPrice = skuList.getPrice();
            double productMonthRent = skuList.getMonth_rent();
            double productTradePrice = skuList.getTrade_price();

            String productRetailPriceString = activity.getResources().getString(R.string.retail_price) + " " + YiBaiApplication.getCurrencySymbol() + productPrice;
            String productMonthRentRetailString = activity.getResources().getString(R.string.month_rent_price) + " " + YiBaiApplication.getCurrencySymbol() + productMonthRent;
            String productTradePriceString = activity.getResources().getString(R.string.trade_price) + " " + YiBaiApplication.getCurrencySymbol() + productTradePrice;

            TextView retailPriceTextView = view.findViewById(R.id.retailPriceTextView);
            TextView monthRentPriceTextView = view.findViewById(R.id.monthRentPriceTextView);
            TextView tradePriceTextView = view.findViewById(R.id.tradePriceTextView);

            retailPriceTextView.setText(productRetailPriceString);
            monthRentPriceTextView.setText(productMonthRentRetailString);
            tradePriceTextView.setText(productTradePriceString);

            retailPriceTextView.setOnClickListener(this);
            monthRentPriceTextView.setOnClickListener(this);
            tradePriceTextView.setOnClickListener(this);

            // 员工不能看进货价
            String roleName = YiBaiApplication.getRoleName();
            if (!TextUtils.isEmpty(roleName) && !roleName.equals(SERVICE)) {
                tradePriceTextView.setVisibility(View.GONE);
            }

            mPricePopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPricePopupWindow.setOutsideTouchable(true);
            mPricePopupWindow.setFocusable(true);

            // 设置一个动画效果
            mPricePopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPricePopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
            mPricePopupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
        } else {
            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPricePopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
            mPricePopupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 在一个新的植物规格被选中的时候回调
     *
     * @param title              规格名称
     * @param titlePosition      规格名称在列表中的位置
     * @param smallTitle         具体规格值
     * @param smallTitlePosition 具体规格值在列表中的位置
     */
    @Override
    public void onSpecSelected(String title, int titlePosition, String smallTitle, int smallTitlePosition) {
        // put的方法即是添加也是修改,所以直接put就可以了
        mSelectsSpecMap.put(titlePosition, smallTitlePosition);
    }

    /**
     * 将ProductData数据保存到"模拟搭配产品"数据的数据库中
     *
     * @param cateCode    产品类别(plant=植物,pot=花盆)
     * @param productData 产品数据
     */
    @Override
    public void saveSimulationData(String cateCode, ProductData productData) {
        Activity activity = (Activity) mBonsaiDetailsView;
        String pic2 = productData.getProductPic2();
        if (TextUtils.isEmpty(pic2)) {
            mBonsaiDetailsView.onSaveSimulationDataDataResult(false);
        } else {
            ImageUtil.DownloadCallback downloadCallback = new ImageUtil.DownloadCallback() {
                @Override
                public void onDownloadStarted() {

                }

                @Override
                public void onDownloadFinished(List<Bitmap> bitmapList) {
                    Bitmap bitmap = bitmapList.get(0);
                    if (null == bitmap) {
                        mBonsaiDetailsView.onSaveSimulationDataDataResult(false);
                    } else {
                        mBonsaiDetailsModel.saveSimulationData(
                                cateCode,
                                productData,
                                bitmap,
                                ProductDetailsPresenterImpl.this
                        );
                    }
                }

                @Override
                public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

                }
            };
            ImageUtil.downloadPicture(activity, downloadCallback, pic2);
        }
    }

    /**
     * 保存"模拟搭配产品"数据成功或者失败时回调
     *
     * @param result true成功 false失败
     */
    @Override
    public void onSaveSimulationDataDataResult(boolean result) {
        mBonsaiDetailsView.onSaveSimulationDataDataResult(result);
    }

    /**
     * 将ProductData数据保存到"报价"数据的数据库中
     *
     * @param productData 产品数据
     */
    @Override
    public void saveQuotationData(ProductData productData) {
        mBonsaiDetailsModel.saveQuotationData(productData, this);
    }

    /**
     * 保存"报价"数据成功或者失败时回调
     *
     * @param result true成功 false失败
     */
    @Override
    public void onSaveQuotationDataDataResult(boolean result) {
        mBonsaiDetailsView.onSaveQuotationDataDataResult(result);
    }

    /**
     * sku加入待摆放清单
     *
     * @param firstSkuId 主产品 skuId
     */
    @Override
    public void addQuotation(int firstSkuId) {
        mBonsaiDetailsModel.addQuotation(firstSkuId, this);
    }

    /**
     * 在sku加入待摆放清单成功时回调
     *
     * @param addQuotation sku加入待摆放清单时服务器端返回的数据
     */
    @Override
    public void onAddQuotationSuccess(AddQuotation addQuotation) {
        mBonsaiDetailsView.onAddQuotationSuccess(addQuotation);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (null != mSpecPopupWindow && mSpecPopupWindow.isShowing()) {
            mSpecPopupWindow.dismiss();
        }
        if (null != mPricePopupWindow && mPricePopupWindow.isShowing()) {
            mPricePopupWindow.dismiss();
        }

        if (id == R.id.determineButton) {
            mBonsaiDetailsView.onGetSelectsSpecSuccess(mSelectsSpecMap);
        }

        mBonsaiDetailsView.onPopupWindowItemClick(v);
    }
}
