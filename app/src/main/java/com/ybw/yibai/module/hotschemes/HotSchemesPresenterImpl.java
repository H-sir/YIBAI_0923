package com.ybw.yibai.module.hotschemes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.HotSchemes.DataBean.ListBean;
import com.ybw.yibai.common.bean.HotSchemes.DataBean.ListBean.SpotsBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.HorizontalViewPager;
import com.ybw.yibai.common.widget.MatchLayout;
import com.ybw.yibai.module.hotschemes.HotSchemesContract.CallBack;
import com.ybw.yibai.module.hotschemes.HotSchemesContract.HotSchemesModel;
import com.ybw.yibai.module.hotschemes.HotSchemesContract.HotSchemesPresenter;
import com.ybw.yibai.module.hotschemes.HotSchemesContract.HotSchemesView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.ybw.yibai.common.utils.ImageUtil.drawableToBitmap;

/**
 * 新热门场景界面Presenter实现类
 *
 * @author sjl
 * @date 2019/12/2
 */
public class HotSchemesPresenterImpl extends BasePresenterImpl<HotSchemesView> implements HotSchemesPresenter, CallBack {

    private String TAG = "HotSchemesPresenterImpl";

    /**
     * 存放动态添加的"ViewPager"
     */
    private List<HorizontalViewPager> mViewPagerList;

    /**
     * 存放动态添加的"植物与盆器互相搭配的View"
     */
    private List<MatchLayout> mMatchLayoutList;

    /**
     * 显示显示产品代码信息的PopupWindow
     */
    private PopupWindow mProductCodePopupWindow;

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private HotSchemesView mHotSchemesView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private HotSchemesModel mHotSchemesModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public HotSchemesPresenterImpl(HotSchemesView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mHotSchemesView = getView();
        mHotSchemesModel = new HotSchemesModelImpl();

        mViewPagerList = new ArrayList<>();
        mMatchLayoutList = new ArrayList<>();
    }

    /**
     * 设置竖屏情况下"搭配图片布局的容器"子View的位置
     *
     * @param collocationLayout 搭配图片布局的容器
     * @param hotSchemeInfo     热门场景信息
     * @param comType           组合模式: 1组合,2单产品
     */
    @Override
    public void setPortraitScreenCollocationLayoutPosition(RelativeLayout collocationLayout, ListBean hotSchemeInfo, int comType) {
        Fragment fragment = (Fragment) mHotSchemesView;
        String backgroundPic = hotSchemeInfo.getBg_pic();
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                setCollocationLayoutPosition(collocationLayout, hotSchemeInfo, comType, ORIENTATION_PORTRAIT, resource);
            }
        };
        Glide.with(fragment).load(backgroundPic).into(simpleTarget);
    }

    /**
     * 设置横屏情况下"搭配图片布局的容器"子View的位置
     *
     * @param collocationLayout 搭配图片布局的容器
     * @param hotSchemeInfo     热门场景信息
     * @param comType           组合模式: 1组合,2单产品
     */
    @Override
    public void setLandscapeScreenCollocationLayoutPosition(RelativeLayout collocationLayout, ListBean hotSchemeInfo, int comType) {
        Fragment fragment = (Fragment) mHotSchemesView;
        String backgroundPic = hotSchemeInfo.getBg_pic();
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                setCollocationLayoutPosition(collocationLayout, hotSchemeInfo, comType, ORIENTATION_LANDSCAPE, resource);
            }
        };
        Glide.with(fragment).load(backgroundPic).into(simpleTarget);
    }

    /**
     * 设置"搭配图片布局的容器"子View的位置
     *
     * @param collocationLayout 搭配图片布局的容器
     * @param hotSchemeInfo     热门场景信息
     * @param comType           组合模式: 1组合,2单产品
     * @param orientation       屏幕方向
     * @param resource          场景背景图片
     */
    private void setCollocationLayoutPosition(RelativeLayout collocationLayout, ListBean hotSchemeInfo, int comType, int orientation, Drawable resource) {
        Fragment fragment = (Fragment) mHotSchemesView;
        Context context = fragment.getContext();
        if (null == context) {
            return;
        }
        List<SpotsBean> spots = hotSchemeInfo.getSpots();
        if (null == spots || spots.size() == 0) {
            return;
        }
        Bitmap bitmap = drawableToBitmap(resource);
        if (null == bitmap) {
            return;
        }
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        mViewPagerList.clear();
        mMatchLayoutList.clear();
        // 移除上一次动态添加的View
        collocationLayout.removeAllViews();

        DisplayMetrics dm = fragment.getResources().getDisplayMetrics();
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        // 获取背景图片在竖屏的情况下显示的的高度(竖屏的情况下显示的的宽度等于屏幕的宽度)
        int h = bitmapHeight * widthPixels / bitmapWidth;
        // 获取背景图片在横屏的情况下显示的的宽度(横屏的情况下显示的的高度等于屏幕的高度)
        int w = bitmapWidth * heightPixels / bitmapHeight;

        for (int i = 0; i < spots.size(); i++) {
            SpotsBean spotsBean = spots.get(i);
            if (null == spotsBean) {
                continue;
            }
            String points = spotsBean.getPoints();
            String[] point = points.split(":");
            float x = Float.parseFloat(point[0]);
            float y = Float.parseFloat(point[1]);
            float heightScale = spotsBean.getHeight();

            if (1 == comType) {
                // 动态添加View
                MatchLayout matchLayout = new MatchLayout(context);
                collocationLayout.addView(matchLayout);
                mMatchLayoutList.add(matchLayout);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) matchLayout.getLayoutParams();
                if (orientation == ORIENTATION_PORTRAIT) {
                    layoutParams.leftMargin = (int) (widthPixels * x);
                    layoutParams.topMargin = (int) ((heightPixels - h) / 2 + h * y);
                    layoutParams.width = (int) (h * heightScale);
                    layoutParams.height = (int) (h * heightScale);
                } else {
                    layoutParams.leftMargin = (int) ((widthPixels - w) / 2 + w * x + 90);
                    layoutParams.topMargin = (int) (heightPixels * y);
                    layoutParams.width = (int) (heightPixels * heightScale);
                    layoutParams.height = (int) (heightPixels * heightScale);
                }
                matchLayout.setLayoutParams(layoutParams);
            } else {
                // 动态添加View
                HorizontalViewPager viewPager = new HorizontalViewPager(context);
                viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
                collocationLayout.addView(viewPager);
                mViewPagerList.add(viewPager);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
                if (orientation == ORIENTATION_PORTRAIT) {
                    layoutParams.leftMargin = (int) (widthPixels * x);
                    layoutParams.topMargin = (int) ((heightPixels - h) / 2 + h * y);
                    layoutParams.width = (int) (h * heightScale);
                    layoutParams.height = (int) (h * heightScale);
                } else {
                    layoutParams.leftMargin = (int) ((widthPixels - w) / 2 + w * x);
                    layoutParams.topMargin = (int) (heightPixels * y);
                    layoutParams.width = (int) (heightPixels * heightScale);
                    layoutParams.height = (int) (heightPixels * heightScale);
                }
            }
        }
        if (1 == comType) {
            mHotSchemesView.onSetGroupCollocationLayoutPositionSucceed(mMatchLayoutList);
        } else {
            mHotSchemesView.onSetSingleCollocationLayoutPositionSucceed(mViewPagerList);
        }
    }

    /**
     * 在"组合模式"下,动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
     *
     * @param matchLayout    植物与盆器互相搭配的View
     * @param plantViewPager 放置"植物自由搭配图"的ViewPager
     * @param potViewPager   放置"盆器自由搭配图"的ViewPager
     * @param plantBean      用户当前选择的植物信息
     * @param potBean        用户当前选择的盆器信息
     */
    @Override
    public void setCollocationContentParams(MatchLayout matchLayout,
                                            HorizontalViewPager plantViewPager, HorizontalViewPager potViewPager,
                                            com.ybw.yibai.common.bean.ListBean plantBean,
                                            com.ybw.yibai.common.bean.ListBean potBean) {

        double plantHeight = plantBean.getHeight();
        double potHeight = potBean.getHeight();
        double offsetRatio = potBean.getOffset_ratio();

        matchLayout.post(() -> {
            int height = matchLayout.getHeight();
            // 将搭配图片布局的容器的高度/(植物高度 + 盆的高度 - 花盆的偏移量) = 每一份的高度,保留2为小数
            double portionHeight = new BigDecimal((float) height / (plantHeight + potHeight - offsetRatio))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            ViewGroup.LayoutParams plantImageViewParams = plantViewPager.getLayoutParams();
            plantImageViewParams.height = (int) (portionHeight * plantHeight);
            plantViewPager.setLayoutParams(plantImageViewParams);

            ViewGroup.LayoutParams viewPagerParams = potViewPager.getLayoutParams();
            viewPagerParams.height = (int) (portionHeight * potHeight);
            potViewPager.setLayoutParams(viewPagerParams);
        });
    }

    /**
     * 显示显示产品代码信息的PopupWindow
     *
     * @param v         锚点
     * @param plantBean 用户当前选择的植物信息
     * @param potBean   用户当前选择的盆器信息
     * @param comType   组合模式: 1组合,2单产品
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void displayProductCodePopupWindow(View v, com.ybw.yibai.common.bean.ListBean plantBean,
                                              com.ybw.yibai.common.bean.ListBean potBean, int comType) {

        Fragment fragment = (Fragment) mHotSchemesView;
        Activity activity = fragment.getActivity();
        if (null == activity) {
            return;
        }

        View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_product_code_info_layout, null);
        mProductCodePopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout rootLayout = view.findViewById(R.id.rootLayout);
        ImageView arrowsImageView = view.findViewById(R.id.arrowsImageView);
        TextView plantProductPriceCodeTextView = view.findViewById(R.id.plantProductPriceCodeTextView);
        TextView potProductPriceCodeTextView = view.findViewById(R.id.potProductPriceCodeTextView);
        TextView plantProductTradePriceCodeTextView = view.findViewById(R.id.plantProductTradePriceCodeTextView);
        TextView potProductTradePriceCodeTextView = view.findViewById(R.id.potProductTradePriceCodeTextView);
        TextView productPriceCodeTextView = view.findViewById(R.id.productPriceCodeTextView);

        rootLayout.setOnClickListener(v1 -> mProductCodePopupWindow.dismiss());

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) arrowsImageView.getLayoutParams();
        layoutParams.leftMargin = DensityUtil.dpToPx(activity, 75);
        arrowsImageView.setLayoutParams(layoutParams);

        String productPriceCode = plantBean.getPrice_code();
        String productTradePriceCode = plantBean.getTrade_price_code();
        String augmentedPriceCode = null;
        String augmentedTradePriceCode = null;
        double plantPrice = plantBean.getPrice();
        double potPrice = potBean.getPrice();
        if (1 == comType) {
            augmentedPriceCode = potBean.getPrice_code();
            augmentedTradePriceCode = potBean.getTrade_price_code();
        }

        if (TextUtils.isEmpty(productPriceCode)) {
            plantProductPriceCodeTextView.setText(" ");
        } else {
            plantProductPriceCodeTextView.setText(productPriceCode);
        }
        if (TextUtils.isEmpty(augmentedPriceCode)) {
            potProductPriceCodeTextView.setText(" ");
        } else {
            potProductPriceCodeTextView.setText(augmentedPriceCode);
        }
        if (TextUtils.isEmpty(productTradePriceCode)) {
            plantProductTradePriceCodeTextView.setText(" ");
        } else {
            plantProductTradePriceCodeTextView.setText(productTradePriceCode);
        }
        if (TextUtils.isEmpty(augmentedTradePriceCode)) {
            potProductTradePriceCodeTextView.setText(" ");
        } else {
            potProductTradePriceCodeTextView.setText(augmentedTradePriceCode);
        }
        double totalPrice = plantPrice + potPrice;
        String s = YiBaiApplication.getCurrencySymbol() + totalPrice;
        productPriceCodeTextView.setText(s);

        mProductCodePopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mProductCodePopupWindow.setOutsideTouchable(true);
        mProductCodePopupWindow.setFocusable(true);

        // 添加PopupWindow窗口关闭事件
        mProductCodePopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        mProductCodePopupWindow.showAsDropDown(v, Gravity.BOTTOM, -DensityUtil.dpToPx(activity, 56), 0);
    }

    /**
     * 保存到模拟列表数据
     *
     * @param plantBean 用户当前选择的植物信息
     */
    @Override
    public void saveSimulationData(com.ybw.yibai.common.bean.ListBean plantBean) {
        Fragment fragment = (Fragment) mHotSchemesView;
        Activity activity = fragment.getActivity();

        String productPic2 = plantBean.getPic2();
        if (TextUtils.isEmpty(productPic2)) {
            return;
        }
        ImageUtil.downloadPicture(activity, new ImageUtil.DownloadCallback() {
            @Override
            public void onDownloadStarted() {

            }

            @Override
            public void onDownloadFinished(List<Bitmap> bitmapList) {
                Bitmap bitmap = bitmapList.get(0);
                if (null == bitmap) {
                    // 下载图片失败return
                    return;
                }
                mHotSchemesModel.saveSimulationData(
                        plantBean,
                        bitmap,
                        HotSchemesPresenterImpl.this
                );
            }

            @Override
            public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

            }
        }, productPic2);
    }

    /**
     * 保存到模拟列表数据
     *
     * @param plantBean 用户当前选择的植物信息
     * @param potBean   用户当前选择的盆器信息
     */
    @Override
    public void saveSimulationData(com.ybw.yibai.common.bean.ListBean plantBean, com.ybw.yibai.common.bean.ListBean potBean) {
        Fragment fragment = (Fragment) mHotSchemesView;
        Activity activity = fragment.getActivity();

        String productPic3 = plantBean.getPic3();
        double productHeight = plantBean.getHeight();
        double productOffsetRatio = plantBean.getOffset_ratio();

        String augmentedProductPic3 = potBean.getPic3();
        double augmentedProductHeight = potBean.getHeight();
        double augmentedProductOffsetRatio = potBean.getOffset_ratio();

        if (TextUtils.isEmpty(productPic3) || TextUtils.isEmpty(augmentedProductPic3)) {
            return;
        }
        ImageUtil.downloadPicture(activity, new ImageUtil.DownloadCallback() {
            @Override
            public void onDownloadStarted() {

            }

            @Override
            public void onDownloadFinished(List<Bitmap> bitmapList) {
                // 标记是否存在下载图片失败的情况
                boolean isFailed = false;
                Bitmap[] bitmaps = new Bitmap[bitmapList.size()];
                for (int i = 0; i < bitmapList.size(); i++) {
                    Bitmap bitmap = bitmapList.get(i);
                    if (null == bitmap) {
                        isFailed = true;
                        break;
                    } else {
                        bitmaps[i] = bitmap;
                    }
                }
                if (isFailed) {
                    //  下载图片失败return
                    return;
                }
                Bitmap bitmap = ImageUtil.pictureSynthesis(
                        productHeight,
                        augmentedProductHeight,
                        productOffsetRatio,
                        augmentedProductOffsetRatio,
                        bitmaps
                );
                if (null == bitmap) {
                    // 合成图片失败return
                    return;
                }
                mHotSchemesModel.saveSimulationData(
                        plantBean,
                        potBean,
                        bitmap,
                        HotSchemesPresenterImpl.this
                );
            }

            @Override
            public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

            }
        }, productPic3, augmentedProductPic3);
    }

    /**
     * 在保存"模拟"成功/失败时回调
     *
     * @param result true成功,false失败
     */
    @Override
    public void onSaveSimulationDataResult(boolean result) {
        mHotSchemesView.onSaveSimulationDataResult(result);
    }
}
