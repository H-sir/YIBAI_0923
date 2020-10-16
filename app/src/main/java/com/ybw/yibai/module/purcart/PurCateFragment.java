package com.ybw.yibai.module.purcart;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Iterator;
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
        PurCartItemListViewAdapter.OnItemSubtractClickListener,
        PurCartItemListViewAdapter.OnSelectClickListener {
    @BindView(R.id.purCartCity)
    TextView purCartCity;
    @BindView(R.id.purCartComListView)
    RecyclerView purCartComListView;
    @BindView(R.id.purCartItemListView)
    RecyclerView purCartItemListView;
    @BindView(R.id.purCartItemView)
    TextView purCartItemView;
    @BindView(R.id.purCartComView)
    TextView purCartComView;
    @BindView(R.id.purCartAllSelect)
    LinearLayout purCartAllSelect;
    @BindView(R.id.purCartAllSelectText)
    TextView purCartAllSelectText;
    @BindView(R.id.purCartAllSelectImg)
    ImageView purCartAllSelectImg;
    @BindView(R.id.purCartAllPrice)
    TextView purCartAllPrice;
    @BindView(R.id.rootLayout)
    LinearLayout rootLayout;
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
    private boolean isAllSelect = true;
    private float allPrice = 0f;

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
        mPurCartItemListViewAdapter.setSelectClickListener(this);

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
        comlistBeans.clear();
        itemlistBeans.clear();
        // 获取用户的进货数据
        if (mPurCartPresenter != null)
            mPurCartPresenter.getPurCartData();
    }

    @Override
    public void onGetPurCartDataSuccess(PurCartBean purCartBean) {
        this.purCartBean = purCartBean;
        allPrice = 0;
        if (purCartBean.getData().getComlist() != null && purCartBean.getData().getComlist().size() > 0) {
            comlistBeans.addAll(purCartBean.getData().getComlist());
            purCartComView.setVisibility(View.VISIBLE);
            for (Iterator<PurCartBean.DataBean.ComlistBean> iterator = comlistBeans.iterator(); iterator.hasNext(); ) {
                PurCartBean.DataBean.ComlistBean comlistBean = iterator.next();
                if (comlistBean.getChecked() == 0) {
                    isAllSelect = false;
                    purCartAllSelectText.setText("全选");
                    purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.selected_img));
                } else {
                    allPrice = allPrice + (comlistBean.getPrice() * comlistBean.getNum());
                }
            }
            mPurCartComListViewAdapter = new PurCartComListViewAdapter(getActivity(), comlistBeans);
            purCartComListView.setAdapter(mPurCartComListViewAdapter);
            mPurCartComListViewAdapter.notifyDataSetChanged();
        }
        if (purCartBean.getData().getItemlist() != null && purCartBean.getData().getItemlist().size() > 0) {
            itemlistBeans.addAll(purCartBean.getData().getItemlist());
            purCartItemView.setVisibility(View.VISIBLE);
            for (Iterator<PurCartBean.DataBean.ItemlistBean> iterator = itemlistBeans.iterator(); iterator.hasNext(); ) {
                PurCartBean.DataBean.ItemlistBean itemlistBean = iterator.next();
                if (itemlistBean.getChecked() == 0) {
                    isAllSelect = false;
                    purCartAllSelectText.setText("全选");
                    purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.selected_img));
                } else {
                    allPrice = allPrice + (itemlistBean.getPrice() * itemlistBean.getNum());
                }
            }
        }

        purCartAllPrice.setText(String.valueOf(allPrice));
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
                purCartAllSelectData();
                break;
            case R.id.purCartSettlement:
                break;
        }
    }

    private void purCartAllSelectData() {
        if (isAllSelect) {
            isAllSelect = false;
            purCartAllSelectText.setText("全选");
            purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.selected_img));
        } else {
            isAllSelect = true;
            purCartAllSelectText.setText("全不选");
            purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.purcart_no_select));
        }
    }

    TextView purcartComNum;
    ImageView purcartComSelect;
    int index = 0;
    int position = 0;

    @Override
    public void onComAddNum(int position, TextView purcartComNum) {
        index = 1;
        this.position = position;
        this.purcartComNum = purcartComNum;
        PurCartBean.DataBean.ComlistBean comlistBean = comlistBeans.get(position);
        int num = comlistBean.getNum() + 1;
        int cartId = comlistBean.getCartId();
        if (mPurCartPresenter != null)
            mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onComSubtractNum(int position, TextView purcartComNum) {
        index = 2;
        this.position = position;
        this.purcartComNum = purcartComNum;
        PurCartBean.DataBean.ComlistBean comlistBean = comlistBeans.get(position);
        int num = comlistBean.getNum() - 1;
        int cartId = comlistBean.getCartId();
        if (mPurCartPresenter != null)
            mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onItemAddNum(int position, TextView purcartComNum) {
        index = 3;
        this.position = position;
        this.purcartComNum = purcartComNum;
        PurCartBean.DataBean.ItemlistBean itemlistBean = itemlistBeans.get(position);
        int num = itemlistBean.getNum() + 1;
        int cartId = itemlistBean.getCartId();
        if (mPurCartPresenter != null)
            mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onItemSubtractNum(int position, TextView purcartComNum) {
        index = 4;
        this.position = position;
        this.purcartComNum = purcartComNum;
        PurCartBean.DataBean.ItemlistBean itemlistBean = itemlistBeans.get(position);
        int num = itemlistBean.getNum() - 1;
        int cartId = itemlistBean.getCartId();
        if (mPurCartPresenter != null)
            mPurCartPresenter.updateCartGate(cartId, num);
    }

    @Override
    public void onItemSelectNum(int position, ImageView purcartComSelect, boolean isSelect) {
        index = 6;
        this.position = position;
        this.purcartComSelect = purcartComSelect;
        PurCartBean.DataBean.ItemlistBean itemlistBean = itemlistBeans.get(position);
        int check = 0;
        if (isSelect) {
            check = 1;
        }
        int cartId = itemlistBean.getCartId();
        if (mPurCartPresenter != null)
            mPurCartPresenter.updateCartGateCheck(cartId, check);
    }

    @Override
    public void onUpdateCartGateSuccess(int num) {
        switch (index) {
            case 1:
            case 2:
                comlistBeans.get(position).setNum(num);
                purcartComNum.setText(String.valueOf(num));
                break;
            case 3:
            case 4:
                itemlistBeans.get(position).setNum(num);
                purcartComNum.setText(String.valueOf(num));
                break;
            case 5:
                PurCartBean.DataBean.ComlistBean comlistBean = comlistBeans.get(position);
                comlistBean.setChecked(num);
                if (comlistBean.getChecked() == 1) {
                    purcartComSelect.setImageResource(R.mipmap.selected_img);
                } else {
                    purcartComSelect.setImageResource(R.mipmap.purcart_no_select);
                }
                setAllSelect();
                break;
            case 6:
                PurCartBean.DataBean.ItemlistBean itemlistBean = itemlistBeans.get(position);
                itemlistBean.setChecked(num);
                if (itemlistBean.getChecked() == 1) {
                    purcartComSelect.setImageResource(R.mipmap.selected_img);
                } else {
                    purcartComSelect.setImageResource(R.mipmap.purcart_no_select);
                }
                setAllSelect();
                break;
        }
        index = 0;
    }

    private void setAllSelect() {
        isAllSelect = false;
        purCartAllSelectText.setText("全不选");
        purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.purcart_no_select));

        allPrice = 0;
        comlistBeans.clear();
        itemlistBeans.clear();
        if (purCartBean.getData().getComlist() != null && purCartBean.getData().getComlist().size() > 0) {
            comlistBeans.addAll(purCartBean.getData().getComlist());
            purCartComView.setVisibility(View.VISIBLE);
            for (Iterator<PurCartBean.DataBean.ComlistBean> iterator = comlistBeans.iterator(); iterator.hasNext(); ) {
                PurCartBean.DataBean.ComlistBean comlistBean = iterator.next();
                if (comlistBean.getChecked() == 0) {
                    isAllSelect = false;
                    purCartAllSelectText.setText("全选");
                    purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.selected_img));
                } else {
                    allPrice = allPrice + (comlistBean.getPrice() * comlistBean.getNum());
                }
            }
        }
        if (purCartBean.getData().getItemlist() != null && purCartBean.getData().getItemlist().size() > 0) {
            itemlistBeans.addAll(purCartBean.getData().getItemlist());
            purCartItemView.setVisibility(View.VISIBLE);
            for (Iterator<PurCartBean.DataBean.ItemlistBean> iterator = itemlistBeans.iterator(); iterator.hasNext(); ) {
                PurCartBean.DataBean.ItemlistBean itemlistBean1 = iterator.next();
                if (itemlistBean1.getChecked() == 0) {
                    isAllSelect = false;
                    purCartAllSelectText.setText("全选");
                    purCartAllSelectImg.setImageDrawable(getResources().getDrawable(R.mipmap.selected_img));
                } else {
                    allPrice = allPrice + (itemlistBean1.getPrice() * itemlistBean1.getNum());
                }
            }
        }

        purCartAllPrice.setText(String.valueOf(allPrice));
        mPurCartComListViewAdapter.notifyDataSetChanged();
        mPurCartItemListViewAdapter.notifyDataSetChanged();
    }
}
