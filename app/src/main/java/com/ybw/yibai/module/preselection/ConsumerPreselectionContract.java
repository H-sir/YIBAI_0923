package com.ybw.yibai.module.preselection;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.DeletePlacement;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/11/18
 */
public interface ConsumerPreselectionContract {

    interface ConsumerPreselectionView extends BaseView {

        /**
         * 显示是否确定删除"待摆放清单"的数据的结果
         *
         * @param result true 确定删除, false 取消
         */
        void areYouSureToDelete(boolean result);

        /**
         * 在删除待摆放清单产品成功时回调
         *
         * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
         */
        void onDeletePlacementListSuccess(DeletePlacement deletePlacement);
    }

    interface ConsumerPreselectionPresenter extends BasePresenter<ConsumerPreselectionView> {

        /**
         * 显示是否确定删除"待摆放清单"的数据
         */
        void displayConsumerPreselectionDialog();

        /**
         * 删除待摆放清单列表
         *
         * @param quoteIds 清单产品id,多个用英文逗号分隔
         */
        void deletePlacementList(String quoteIds);
    }

    interface ConsumerPreselectionModel {

        /**
         * 删除待摆放清单列表
         *
         * @param quoteIds 清单产品id,多个用英文逗号分隔
         * @param callBack 回调方法
         */
        void deletePlacementList(String quoteIds, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在删除待摆放清单产品成功时回调
         *
         * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
         */
        void onDeletePlacementListSuccess(DeletePlacement deletePlacement);
    }
}
