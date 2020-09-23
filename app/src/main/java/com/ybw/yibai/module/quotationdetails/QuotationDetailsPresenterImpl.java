package com.ybw.yibai.module.quotationdetails;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.QuotationAgain;
import com.ybw.yibai.common.bean.QuotationList;
import com.ybw.yibai.common.classs.RoundCornersTransformation.CornerType;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsContract.CallBack;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsContract.QuotationDetailsModel;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsContract.QuotationDetailsPresenter;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsContract.QuotationDetailsView;

import java.io.File;
import java.io.IOException;

import static com.ybw.yibai.common.constants.Folder.PICTURES_PATH;
import static com.ybw.yibai.common.constants.Folder.SHARE_IMAGE_PREFIX;

/**
 * 报价列表界面Presenter实现类
 *
 * @author sjl
 * @date 2019/09/28
 */
public class QuotationDetailsPresenterImpl extends BasePresenterImpl<QuotationDetailsView> implements QuotationDetailsPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private QuotationDetailsView mQuotationDetailsView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private QuotationDetailsModel mQuotationDetailsModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public QuotationDetailsPresenterImpl(QuotationDetailsView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mQuotationDetailsView = getView();
        mQuotationDetailsModel = new QuotationDetailsModelImpl();
    }

    /**
     * 获取报价列表
     *
     * @param type  0/空获取所有,1待客户确定,2待跟进3已成交4已失效
     * @param isAll 0分页1不分页默认为1
     */
    @Override
    public void getQuotationList(int type, int isAll) {
        mQuotationDetailsModel.getQuotationList(type, isAll, this);
    }

    /**
     * 在获取报价列表数据成功时回调
     *
     * @param quotationList 报价列表
     */
    @Override
    public void onGetQuotationListSuccess(QuotationList quotationList) {
        mQuotationDetailsView.onGetQuotationListSuccess(quotationList);
    }

    /**
     * 报价单再次报价
     *
     * @param orderNumber 订单编号
     */
    @Override
    public void quotationAgain(String orderNumber, int clearOk) {
        mQuotationDetailsModel.quotationAgain(orderNumber, clearOk, this);
    }

    /**
     * 在报价单再次报价成功时回调
     *
     * @param quotationAgain 报价单再次报价时服务器端返回的数据
     */
    @Override
    public void onQuotationAgainSuccess(QuotationAgain quotationAgain) {
        mQuotationDetailsView.onQuotationAgainSuccess(quotationAgain);
    }

    /**
     * 初始化选择"分享"的PopupWindow
     *
     * @param rootLayout     View根布局
     * @param clientName     客户名称
     * @param orderNumber    订单号
     * @param qrCodeFilePath 二维码路径
     */
    @Override
    public void displaySharePopupWindow(View rootLayout, String clientName, String orderNumber, String qrCodeFilePath) {
        Fragment fragment = (Fragment) mQuotationDetailsView;
        Activity activity = fragment.getActivity();
        if (null == activity) {
            return;
        }
        View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_share_quotation_layout, null);
        PopupWindow mPopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout shareLayout = view.findViewById(R.id.shareLinearLayout);
        ImageView qrCodeImageView = view.findViewById(R.id.qrCodeImageView);
        TextView clientNameTextView = view.findViewById(R.id.clientNameTextView);

        File file = new File(qrCodeFilePath);
        ImageUtil.displayImage(activity, qrCodeImageView, file, DensityUtil.dpToPx(activity, 2), CornerType.TOP);
        if (!TextUtils.isEmpty(clientName)) {
            clientNameTextView.setText(clientName);
        }

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        // 在弹出PopupWindow设置屏幕透明度
        OtherUtil.setBackgroundAlpha(activity, 0.7f);
        // 添加PopupWindow窗口关闭事件
        mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        mPopupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);

        // 延时0.1S
        new Handler().postDelayed(() -> {
            Bitmap bitmap = ImageUtil.viewConversionBitmap(shareLayout);
            if (null == bitmap) {
                return;
            }

            String shareImageFilePath = ImageUtil.saveImage(bitmap, SHARE_IMAGE_PREFIX + orderNumber);
            ImageUtil.shareImage(activity, shareImageFilePath);

            view.setOnLongClickListener(v -> {
                String path = FileUtil.createExternalStorageFile(PICTURES_PATH);
                if (TextUtils.isEmpty(path)) {
                    MessageUtil.showMessage(activity.getResources().getString(R.string.failed_to_save_image));
                    return false;
                }
                try {
                    FileUtil.copyFile(shareImageFilePath, path + file.getName());
                    // 发送广播通知相册更新图片
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(path + file.getName()));
                    intent.setData(uri);
                    activity.sendBroadcast(intent);
                    MessageUtil.showMessage(activity.getResources().getString(R.string.image_saved_to) + path + activity.getResources().getString(R.string.folder));
                } catch (IOException e) {
                    e.printStackTrace();
                    MessageUtil.showMessage(activity.getResources().getString(R.string.failed_to_save_image));
                }
                return false;
            });
        }, 100);
    }

    /**
     * 初始化选择"提示"的PopupWindow
     *
     * @param rootLayout View根布局
     * @param title      标题
     * @param content    内容
     */
    @Override
    public void displayHintPopupWindow(View rootLayout, String title, String content) {
        Fragment fragment = (Fragment) mQuotationDetailsView;
        Activity activity = fragment.getActivity();
        if (null == activity) {
            return;
        }
        View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_hint_layout, null);
        PopupWindow mPopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView contentTextView = view.findViewById(R.id.contentTextView);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button carryOnButton = view.findViewById(R.id.carryOnButton);

        if (!TextUtils.isEmpty(title)) {
            titleTextView.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            contentTextView.setText(content);
        }
        cancelButton.setOnClickListener(v -> {
            mPopupWindow.dismiss();
            mQuotationDetailsView.continueQuoting(true);
        });
        carryOnButton.setOnClickListener(v -> {
            mPopupWindow.dismiss();
            mQuotationDetailsView.continueQuoting(false);
        });

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        // 在弹出PopupWindow设置屏幕透明度
        OtherUtil.setBackgroundAlpha(activity, 0.7f);
        // 添加PopupWindow窗口关闭事件
        mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        mPopupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
    }
}
