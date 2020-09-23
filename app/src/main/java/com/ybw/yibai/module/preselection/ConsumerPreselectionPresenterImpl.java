package com.ybw.yibai.module.preselection;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.CustomDialog;
import com.ybw.yibai.module.preselection.ConsumerPreselectionContract.CallBack;
import com.ybw.yibai.module.preselection.ConsumerPreselectionContract.ConsumerPreselectionModel;
import com.ybw.yibai.module.preselection.ConsumerPreselectionContract.ConsumerPreselectionPresenter;
import com.ybw.yibai.module.preselection.ConsumerPreselectionContract.ConsumerPreselectionView;

/**
 * 客户预选Presenter实现类
 *
 * @author sjl
 * @date 2019/11/18
 */
public class ConsumerPreselectionPresenterImpl extends BasePresenterImpl<ConsumerPreselectionView> implements ConsumerPreselectionPresenter, CallBack {

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private ConsumerPreselectionView mConsumerPreselectionView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private ConsumerPreselectionModel mConsumerPreselectionModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public ConsumerPreselectionPresenterImpl(ConsumerPreselectionView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mConsumerPreselectionView = getView();
        mConsumerPreselectionModel = new ConsumerPreselectionModelImpl();
    }

    /**
     * 显示是否确定删除"待摆放清单"的数据
     */
    @Override
    public void displayConsumerPreselectionDialog() {
        Fragment fragment = ((Fragment) mConsumerPreselectionView);
        Activity activity = fragment.getActivity();
        if (null != activity) {
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            CustomDialog customDialog = new CustomDialog(activity);
            customDialog.setMessage(activity.getResources().getString(R.string.delete_customer_pre_selected_bonsai));
            customDialog.setYesOnclickListener(activity.getResources().getString(R.string.determine),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                        mConsumerPreselectionView.areYouSureToDelete(true);
                    });
            customDialog.setNoOnclickListener(activity.getResources().getString(R.string.cancel),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                        mConsumerPreselectionView.areYouSureToDelete(false);
                    });
            customDialog.show();
        }
    }

    /**
     * 删除待摆放清单列表
     *
     * @param quoteIds 清单产品id,多个用英文逗号分隔
     */
    @Override
    public void deletePlacementList(String quoteIds) {
        mConsumerPreselectionModel.deletePlacementList(quoteIds, this);
    }

    /**
     * 在删除待摆放清单产品成功时回调
     *
     * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
     */
    @Override
    public void onDeletePlacementListSuccess(DeletePlacement deletePlacement) {
        mConsumerPreselectionView.onDeletePlacementListSuccess(deletePlacement);
    }
}
