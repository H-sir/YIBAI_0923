package com.ybw.yibai.module.purcart;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.CustomDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 进货Fragment Presenter 实现类
 *
 * @author sjl
 * @date 2019/11/5
 */
public class PurCartPresenterImpl extends BasePresenterImpl<PurCartContract.PurCartView> implements PurCartContract.PurCartPresenter, PurCartContract.CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private PurCartContract.PurCartView mPurCartView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private PurCartContract.PurCartModel mPurCartModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public PurCartPresenterImpl(PurCartContract.PurCartView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mPurCartView = getView();
        mPurCartModel = new PurCartModelImpl();
    }

    @Override
    public void getPurCartData() {
        mPurCartModel.getPurCartData(this);
    }

    @Override
    public void onGetPurCartDataSuccess(PurCartBean purCartBean) {
        mPurCartView.onGetPurCartDataSuccess(purCartBean);
    }

    @Override
    public void updateCartGate(int cartId, int num) {
        mPurCartModel.updateCartGate(cartId, num, this);
    }

    @Override
    public void updateCartGateCheck(int cartId, int check) {
        mPurCartModel.updateCartGateCheck(cartId, check, this);
    }

    @Override
    public void onUpdateCartGateSuccess(int num) {
        mPurCartView.onUpdateCartGateSuccess(num);
    }

    @Override
    public void upAllCart(String cartIds, int type, int isCheck) {
        mPurCartModel.upAllCart(cartIds,type,isCheck,this);
    }

    @Override
    public void onUpAllCartSuccess(int isCheck) {
        mPurCartView.onUpAllCartSuccess(isCheck);
    }
}
