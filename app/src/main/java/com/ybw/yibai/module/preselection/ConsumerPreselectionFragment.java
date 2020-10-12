package com.ybw.yibai.module.preselection;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.ConsumerPreselectionAdapter;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.PlacementQrQuotationList.DataBean.ListBean;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.preselection.ConsumerPreselectionContract.ConsumerPreselectionPresenter;
import com.ybw.yibai.module.preselection.ConsumerPreselectionContract.ConsumerPreselectionView;
import com.ybw.yibai.module.scene.SceneActivity;
import com.ybw.yibai.module.sceneedit.SceneEditFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_PRESELECTION_PRODUCT_LIST;

/**
 * 客户预选
 *
 * @author sjl
 * @date 2019/11/14
 */
public class ConsumerPreselectionFragment extends BaseFragment implements
        ConsumerPreselectionView,
        ConsumerPreselectionAdapter.OnItemClickListener,
        ConsumerPreselectionAdapter.OnItemLongClickListener {

    private int position;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 显示客户预选产品
     */
    private RecyclerView mRecyclerView;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 客户预选产品
     */
    private List<ListBean> mConsumerPreselectionList;

    /**
     * 客户预选产品的适配器
     */
    private ConsumerPreselectionAdapter mAdapter;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 上下文对象
     */
    private Context mContext;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private ConsumerPreselectionPresenter mConsumerPreselectionPresenter;

    @Override
    protected int setLayouts() {
        return R.layout.fragment_consumer_preselection;
    }

    @Override
    protected void findViews(View view) {
        mContext = getContext();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mWaitDialog = new WaitDialog(mContext);
    }

    @Override
    protected void initView() {
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        int spacing = DensityUtil.dpToPx(mContext, 8);
        // 设置RecyclerView间距
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(5, spacing, true);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }
        mConsumerPreselectionList = new Gson().fromJson(bundle.getString(CUSTOMERS_PRESELECTION_PRODUCT_LIST), new TypeToken<List<ListBean>>() {
        }.getType());
        mAdapter = new ConsumerPreselectionAdapter(mContext, mConsumerPreselectionList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mConsumerPreselectionPresenter = new ConsumerPreselectionPresenterImpl(this);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        ListBean listBean = mConsumerPreselectionList.get(position);
        /**
         * 发送数据到{@link SceneEditFragment#onSelectedConsumerPreselection(ListBean)}
         * 发送数据到{@link SceneActivity#onSelectedConsumerPreselection(ListBean)}
         */
        EventBus.getDefault().post(listBean);
    }

    /**
     * 在RecyclerView Item 长按时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemLongClick(int position) {
        this.position = position;
        mConsumerPreselectionPresenter.displayConsumerPreselectionDialog();
    }

    /**
     * 显示是否确定删除"待摆放清单"的数据的结果
     *
     * @param result true 确定删除, false 取消
     */
    @Override
    public void areYouSureToDelete(boolean result) {
        if (!result) {
            return;
        }
        ListBean listBean = mConsumerPreselectionList.get(position);
        int quoteId = listBean.getQuote_id();
        mConsumerPreselectionPresenter.deletePlacementList(String.valueOf(quoteId));
    }

    /**
     * 在删除待摆放清单产品成功时回调
     *
     * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
     */
    @Override
    public void onDeletePlacementListSuccess(DeletePlacement deletePlacement) {
        /**
         * 发送数据到{@link SceneActivity#onDeletePlacementListSuccess(DeletePlacement)}
         */
        EventBus.getDefault().postSticky(deletePlacement);
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
        ExceptionUtil.handleException(throwable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mConsumerPreselectionPresenter) {
            mConsumerPreselectionPresenter.onDetachView();
            mConsumerPreselectionPresenter = null;
        }
    }
}
