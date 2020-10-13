package com.ybw.yibai.module.imagecontrast;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.Recommend;
import com.ybw.yibai.common.bean.RecommendBean;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.module.imagecontrast.ImageContrastContract.CallBack;
import com.ybw.yibai.module.imagecontrast.ImageContrastContract.ImageContrastModel;
import com.ybw.yibai.module.imagecontrast.ImageContrastContract.ImageContrastPresenter;
import com.ybw.yibai.module.imagecontrast.ImageContrastContract.ImageContrastView;

/**
 * 多张搭配图片模拟效果对比Presenter实现类
 *
 * @author sjl
 * @date 2019/11/8
 */
public class ImageContrastPresenterImpl extends BasePresenterImpl<ImageContrastView> implements ImageContrastPresenter, CallBack, RadioGroup.OnCheckedChangeListener {

    /**
     * 盆器对比/植物对比切换的PopupWindow
     */
    private PopupWindow mPopupWindow;

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private ImageContrastView mImageContrastView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private ImageContrastModel mImageContrastModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public ImageContrastPresenterImpl(ImageContrastView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mImageContrastView = getView();
        mImageContrastModel = new ImageContrastModelImpl();
    }

    /**
     * 换搭配获取植物花盆列表
     *
     * @param cateCode   类别:plant,获取植物 pot获取盆
     * @param plantSkuId 植物SkuId
     * @param potSkuId   盆SkuId
     * @param specType   大中小id(此参数仅在获取花盆时有效)
     * @param pCid       分类id(多个用逗号拼接)
     * @param specId     规格id(同类别规格用逗号分隔,不同类别用|分隔) 例:specId=11,12|15,16
     * @param attrId     属性id,(格式同规格)
     * @param height     搭配高度范围,用|分隔 例:height=5|20
     * @param diameter   搭配口径范围,格式同上
     */
    @Override
    public void getRecommend(String cateCode, Integer plantSkuId, Integer potSkuId, Integer specType,
                             Integer pCid, String specId, String attrId, String height, String diameter) {
        mImageContrastModel.getRecommend(cateCode, plantSkuId, potSkuId, specType, pCid, specId, attrId, height, diameter, this);
    }

    /**
     * 换搭配获取植物花盆列表成功时回调
     *
     * @param recommend 植物花盆列表
     */
    @Override
    public void onGetRecommendSuccess(Recommend recommend) {
        mImageContrastView.onGetRecommendSuccess(recommend);
    }

    @Override
    public void onGetRecommendSuccess(RecommendBean recommend) {
        mImageContrastView.onGetRecommendSuccess(recommend);
    }

    /**
     * 显示盆器对比/植物对比切换的PopupWindow
     *
     * @param v 要显示的位置
     */
    @Override
    public void displayComparisonPopupWindow(View v) {
        Activity activity = (Activity) mImageContrastView;
        if (null == mPopupWindow) {
            View view = activity.getLayoutInflater().inflate(R.layout.popup_window_comparison_layout, null);
            mPopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
            radioGroup.setOnCheckedChangeListener(this);

            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(activity, 0.9f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
            mPopupWindow.showAsDropDown(v, 0, 0);
        } else {
            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(activity, 0.9f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
            mPopupWindow.showAsDropDown(v, 0, 0);
        }
    }

    /**
     * 添加到模拟列表
     *
     * @param simulationData 贴纸数据
     * @param productInfo    推荐产品
     * @param picturePath    组合图片在本地保存的路径
     */
    @Override
    public void addSimulationData(SimulationData simulationData, ListBean productInfo, String picturePath) {
        mImageContrastModel.addSimulationData(simulationData,productInfo, picturePath, this);
    }

    /**
     * 添加到模拟列表操作的结果
     *
     * @param result true操作成功,false操作失败
     */
    @Override
    public void onAddSimulationDataResult(boolean result) {
        mImageContrastView.onAddSimulationDataResult(result);
    }

    /**
     * 在PopupWindow里面的RadioButton被选中时回调
     *
     * @param group     被点击的RadioGroup
     * @param checkedId 被选中的RadioButton ID
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        mImageContrastView.onCheckedChanged(group, checkedId);
    }
}
