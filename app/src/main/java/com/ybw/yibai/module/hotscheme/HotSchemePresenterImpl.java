package com.ybw.yibai.module.hotscheme;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.ybw.yibai.common.bean.HotScheme;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.module.hotscheme.HotSchemeContract.CallBack;
import com.ybw.yibai.module.hotscheme.HotSchemeContract.HotSchemeModel;
import com.ybw.yibai.module.hotscheme.HotSchemeContract.HotSchemePresenter;
import com.ybw.yibai.module.hotscheme.HotSchemeContract.HotSchemeView;

import java.math.BigDecimal;
import java.util.List;

import static com.ybw.yibai.common.utils.ImageUtil.drawableToBitmap;

/**
 * 热门场景界面Presenter实现类
 *
 * @author sjl
 * @date 2019/11/11
 */
public class HotSchemePresenterImpl extends BasePresenterImpl<HotSchemeView> implements HotSchemePresenter, CallBack {

    private String TAG = "HotSchemePresenterImpl";

    /**
     * 显示显示产品代码信息的PopupWindow
     */
    private PopupWindow mProductCodePopupWindow;

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private HotSchemeView mHotSchemeView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private HotSchemeModel mHotSchemeModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public HotSchemePresenterImpl(HotSchemeView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mHotSchemeView = getView();
        mHotSchemeModel = new HotSchemeModelImpl();
    }

    /**
     * 设置竖屏情况下"搭配图片布局的容器"的位置
     *
     * @param collocationLayout 搭配图片布局的容器
     * @param hotSchemeInfo     热门场景信息
     */
    @Override
    public void setPortraitScreenCollocationLayoutPosition(RelativeLayout collocationLayout, HotScheme.DataBean.ListBean hotSchemeInfo) {

        Fragment fragment = (Fragment) mHotSchemeView;
        String backgroundPic = hotSchemeInfo.getBg_pic();
        String points = hotSchemeInfo.getPoints();
        String[] point = points.split(":");
        float x = Float.parseFloat(point[0]);
        float y = Float.parseFloat(point[1]);
        float heightScale = hotSchemeInfo.getHeight();

        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = drawableToBitmap(resource);
                if (null == bitmap) {
                    return;
                }
                int bitmapWidth = bitmap.getWidth();
                int bitmapHeight = bitmap.getHeight();

                DisplayMetrics dm = fragment.getResources().getDisplayMetrics();
                int widthPixels = dm.widthPixels;
                int heightPixels = dm.heightPixels;
                // 获取背景图片在竖屏的情况下显示的的高度(竖屏的情况下显示的的宽度等于屏幕的宽度)
                int h = bitmapHeight * widthPixels / bitmapWidth;

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) collocationLayout.getLayoutParams();
                layoutParams.leftMargin = (int) (widthPixels * x);
                layoutParams.topMargin = (int) ((heightPixels - h) / 2 + h * y);
                layoutParams.width = (int) (h * heightScale);
                layoutParams.height = (int) (h * heightScale);
                collocationLayout.setLayoutParams(layoutParams);

                mHotSchemeView.onSetCollocationLayoutPositionSucceed();
            }
        };
        Glide.with(fragment).load(backgroundPic).into(simpleTarget);
    }

    /**
     * 设置横屏情况下"搭配图片布局的容器"的位置
     *
     * @param collocationLayout 搭配图片布局的容器
     * @param hotSchemeInfo     热门场景信息
     */
    @Override
    public void setLandscapeScreenCollocationLayoutPosition(RelativeLayout collocationLayout, HotScheme.DataBean.ListBean hotSchemeInfo) {

        Fragment fragment = (Fragment) mHotSchemeView;
        String backgroundPic = hotSchemeInfo.getBg_pic();
        String points = hotSchemeInfo.getPoints();
        String[] point = points.split(":");
        float x = Float.parseFloat(point[0]);
        float y = Float.parseFloat(point[1]);
        float heightScale = hotSchemeInfo.getHeight();

        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = drawableToBitmap(resource);
                if (null == bitmap) {
                    return;
                }
                int bitmapWidth = bitmap.getWidth();
                int bitmapHeight = bitmap.getHeight();

                DisplayMetrics dm = fragment.getResources().getDisplayMetrics();
                int widthPixels = dm.widthPixels;
                int heightPixels = dm.heightPixels;
                // 获取背景图片在横屏的情况下显示的的宽度(横屏的情况下显示的的高度等于屏幕的高度)
                int w = bitmapWidth * heightPixels / bitmapHeight;

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) collocationLayout.getLayoutParams();
                layoutParams.leftMargin = (int) ((widthPixels - w) / 2 + w * x);
                layoutParams.topMargin = (int) (heightPixels * y);
                layoutParams.width = (int) (heightPixels * heightScale);
                layoutParams.height = (int) (heightPixels * heightScale);
                collocationLayout.setLayoutParams(layoutParams);

                mHotSchemeView.onSetCollocationLayoutPositionSucceed();
            }
        };
        Glide.with(fragment).load(backgroundPic).into(simpleTarget);
    }

    /**
     * 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
     *
     * @param collocationLayout 搭配图片布局的容器
     * @param plantViewPager    放置"植物自由搭配图"的ViewPager
     * @param potViewPager      放置"盆器自由搭配图"的ViewPager
     * @param plantBean         用户当前选择的植物信息
     * @param potBean           用户当前选择的盆器信息
     */
    @Override
    public void setCollocationContentParams(RelativeLayout collocationLayout,
                                            ViewPager plantViewPager, ViewPager potViewPager,
                                            ListBean plantBean, ListBean potBean) {

        double plantHeight = plantBean.getHeight();
        double potHeight = potBean.getHeight();
        double offsetRatio = potBean.getOffset_ratio();

        collocationLayout.post(() -> {
            int height = collocationLayout.getHeight();
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
     * @param v                       锚点
     * @param productPriceCode        主产品售价代码
     * @param productTradePriceCode   主产品批发价代码
     * @param augmentedPriceCode      附加产品售价代码
     * @param augmentedTradePriceCode 附加产品批发价代码
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void displayProductCodePopupWindow(View v, String productPriceCode, String productTradePriceCode,
                                              String augmentedPriceCode, String augmentedTradePriceCode) {
        Fragment fragment = (Fragment) mHotSchemeView;
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

        rootLayout.setOnClickListener(v1 -> mProductCodePopupWindow.dismiss());

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) arrowsImageView.getLayoutParams();
        layoutParams.leftMargin = DensityUtil.dpToPx(activity, 75);
        arrowsImageView.setLayoutParams(layoutParams);

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

        mProductCodePopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mProductCodePopupWindow.setOutsideTouchable(true);
        mProductCodePopupWindow.setFocusable(true);

        // 添加PopupWindow窗口关闭事件
        mProductCodePopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        mProductCodePopupWindow.showAsDropDown(v, Gravity.BOTTOM, -DensityUtil.dpToPx(activity, 56), 0);
    }

    /**
     * 添加到模拟列表
     *
     * @param plantBean 用户当前选择的植物信息
     * @param potBean   用户当前选择的盆器信息
     */
    @Override
    public void saveSimulationData(ListBean plantBean, ListBean potBean) {
        Fragment fragment = (Fragment) mHotSchemeView;
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
                mHotSchemeModel.saveSimulationData(
                        plantBean,
                        potBean,
                        bitmap,
                        HotSchemePresenterImpl.this
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
        mHotSchemeView.onSaveSimulationDataResult(result);
    }
}
