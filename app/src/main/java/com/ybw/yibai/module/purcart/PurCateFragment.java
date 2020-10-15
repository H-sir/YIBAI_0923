package com.ybw.yibai.module.purcart;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.PurCartComListViewAdapter;
import com.ybw.yibai.common.adapter.PurCartItemListViewAdapter;
import com.ybw.yibai.common.bean.HiddenChanged;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.quotationpurchase.QuotationPurchaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/10/15
 *     desc   :
 * </pre>
 */
public class PurCateFragment extends BaseFragment implements PurCartContract.PurCartView,
        PurCartComListViewAdapter.OnComAddClickListener,
        PurCartItemListViewAdapter.OnItemAddClickListener,
        PurCartComListViewAdapter.OnComSubtractClickListener,
        PurCartItemListViewAdapter.OnItemSubtractClickListener {
    @BindView(R.id.purCartCity) TextView purCartCity;
    @BindView(R.id.purCartComListView) RecyclerView purCartComListView;
    @BindView(R.id.purCartItemListView) RecyclerView purCartItemListView;
    @BindView(R.id.purCartItemView) TextView purCartItemView;
    @BindView(R.id.purCartComView) TextView purCartComView;
    @BindView(R.id.purCartAllSelect) TextView purCartAllSelect;
    @BindView(R.id.purCartAllPrice) TextView purCartAllPrice;
    @BindView(R.id.rootLayout) LinearLayout rootLayout;
    private PurCartContract.PurCartPresenter mPurCartPresenter;
    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /**
     * 组合列表适配器
     */
    private PurCartComListViewAdapter mPurCartComListViewAdapter;

    /**
     * 单品列表适配器
     */
    private PurCartItemListViewAdapter mPurCartItemListViewAdapter;

    private List<PurCartBean.DataBean.ComlistBean> comlistBeans = new ArrayList<>();
    private List<PurCartBean.DataBean.ItemlistBean> itemlistBeans = new ArrayList<>();
    private PurCartBean purCartBean;

    @Override
    protected int setLayouts() {
        return R.layout.fragment_purcart_layout;
    }

    @Override
    protected void findViews(View view) {

    }

    @Override
    protected void initView() {
        mWaitDialog = new WaitDialog(getActivity());
    }

    @Override
    protected void initData() {
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager managerCom = new StaggeredGridLayoutManager(1, VERTICAL);
        // 设置RecyclerView间距
        int gapCom = DensityUtil.dpToPx(getActivity(), 8);
        GridSpacingItemDecoration decorationCom = new GridSpacingItemDecoration(1, gapCom, false);
        // 给RecyclerView设置布局管理器(必须设置)
        purCartComListView.setLayoutManager(managerCom);
        purCartComListView.addItemDecoration(decorationCom);
        purCartComListView.setNestedScrollingEnabled(false);
        purCartComListView.setHasFixedSize(false);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, VERTICAL);
        // 设置RecyclerView间距
        int gap = DensityUtil.dpToPx(getActivity(), 8);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(1, gap, false);
        // 给RecyclerView设置布局管理器(必须设置)
        purCartItemListView.setLayoutManager(manager);
        purCartItemListView.addItemDecoration(decoration);
        purCartItemListView.setNestedScrollingEnabled(false);
        purCartItemListView.setHasFixedSize(false);
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mPurCartPresenter = new PurCartPresenterImpl(this);
        mPurCartComListViewAdapter = new PurCartComListViewAdapter(getActivity(), comlistBeans);
        purCartComListView.setAdapter(mPurCartComListViewAdapter);

        mPurCartItemListViewAdapter = new PurCartItemListViewAdapter(getActivity(), itemlistBeans);
        purCartItemListView.setAdapter(mPurCartItemListViewAdapter);

        mPurCartItemListViewAdapter.setOnItemAddClickListener(this);
        mPurCartItemListViewAdapter.setOnItemSubtractClickListener(this);
        mPurCartComListViewAdapter.setOnComAddClickListener(this);
        mPurCartComListViewAdapter.setOnComSubtractClickListener(this);


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
            return;
        }
        // 获取用户的进货数据
        mPurCartPresenter.getPurCartData();
    }

    @Override
    public void onGetPurCartDataSuccess(PurCartBean purCartBean) {
        this.purCartBean = purCartBean;

        if (purCartBean.getData().getComlist() != null && purCartBean.getData().getComlist().size() > 0) {
            comlistBeans.addAll(purCartBean.getData().getComlist());
            purCartComView.setVisibility(View.VISIBLE);
        }
        if (purCartBean.getData().getItemlist() != null && purCartBean.getData().getItemlist().size() > 0) {
            itemlistBeans.addAll(purCartBean.getData().getItemlist());
            purCartItemView.setVisibility(View.VISIBLE);
        }

        mPurCartComListViewAdapter.notifyDataSetChanged();
        mPurCartItemListViewAdapter.notifyDataSetChanged();
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
        if (null != mPurCartPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mPurCartPresenter.onDetachView();
            mPurCartPresenter = null;
        }
        ExceptionUtil.handleException(throwable);
    }

    @OnClick({R.id.purCartAllSelect, R.id.purCartSettlement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.purCartAllSelect:

                break;
            case R.id.purCartSettlement:
                break;
        }
    }

    TextView purcartComNum;

    @Override
    public void onComAddNum(int position, TextView purcartComNum) {
        this.purcartComNum = purcartComNum;
        PurCartBean.DataBean.ComlistBean comlistBean = comlistBeans.get(position);
        int num = comlistBean.getNum() + 1;
        int cartId = comlistBean.getCartId();
        mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onComSubtractNum(int position, TextView purcartComNum) {
        this.purcartComNum = purcartComNum;
        PurCartBean.DataBean.ComlistBean comlistBean = comlistBeans.get(position);
        int num = comlistBean.getNum() - 1;
        int cartId = comlistBean.getCartId();
        mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onItemAddNum(int position, TextView purcartComNum) {
        this.purcartComNum = purcartComNum;
        PurCartBean.DataBean.ItemlistBean itemlistBean = itemlistBeans.get(position);
        int num = itemlistBean.getNum() + 1;
        int cartId = itemlistBean.getCartId();
        mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onItemSubtractNum(int position, TextView purcartComNum) {
        this.purcartComNum = purcartComNum;
        PurCartBean.DataBean.ItemlistBean itemlistBean = itemlistBeans.get(position);
        int num = itemlistBean.getNum() - 1;
        int cartId = itemlistBean.getCartId();
        mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onUpdateCartGateSuccess(int num) {
        purcartComNum.setText(String.valueOf(num));
    }
}
