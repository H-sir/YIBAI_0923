package com.ybw.yibai.module.quotationdetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.QuotationListAdapter;
import com.ybw.yibai.common.bean.CustomersInfo;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.QuotationAgain;
import com.ybw.yibai.common.bean.QuotationList;
import com.ybw.yibai.common.bean.QuotationList.DataBean;
import com.ybw.yibai.common.bean.QuotationList.DataBean.ListBean;
import com.ybw.yibai.common.bean.ToFragment;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.QRCodeUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.browser.BrowserActivity;
import com.ybw.yibai.module.home.HomeFragment;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.quotation.QuotationFragment;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsContract.QuotationDetailsPresenter;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsContract.QuotationDetailsView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Folder.QR_CODE_IMAGE_PREFIX;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMER_NAME;
import static com.ybw.yibai.common.constants.Preferences.ORDER_NUMBER;
import static com.ybw.yibai.common.constants.Preferences.ORDER_SHARE_URL_TYPE;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.TYPE;
import static com.ybw.yibai.common.constants.Preferences.URL;
import static com.ybw.yibai.common.utils.DensityUtil.dpToPx;
import static com.ybw.yibai.common.utils.SDCardHelperUtil.getSDCardPrivateFilesDir;
import static com.ybw.yibai.common.utils.SDCardHelperUtil.isSDCardMounted;

/**
 * 报价列表界面
 *
 * @author sjl
 * @date 2019/09/28
 */
public class QuotationDetailsFragment extends BaseFragment implements QuotationDetailsView,
        QuotationListAdapter.OnItemClickListener,
        QuotationListAdapter.OnShareItemClickListener,
        QuotationListAdapter.OnQuotationAgainItemClickListener {

    /**
     * 0分页1不分页默认为1
     */
    private int isAll = 1;

    /**
     * 当前Fragment在ViewPager中的位置
     */
    private int position;

    /**
     * 订单编号
     */
    private String orderNumber;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * RootLayout
     */
    private View mRootLayout;

    /**
     * 显示报价列表
     */
    private RecyclerView mRecyclerView;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /**
     * 报价数据集合
     */
    private List<ListBean> mQuotationList;

    /**
     * 报价列表的适配器
     */
    private QuotationListAdapter mAdapter;

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

    /**
     * Activity对象
     */
    private Activity mActivity;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private QuotationDetailsPresenter mQuotationDetailsPresenter;

    @Override
    protected int setLayouts() {
        mContext = getContext();
        mActivity = getActivity();
        return R.layout.fragment_details_list;
    }

    @Override
    protected void findViews(View view) {
        mRootLayout = view.findViewById(R.id.rootLayout);
        mRecyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    protected void initView() {
        mWaitDialog = new WaitDialog(mContext);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        // 布局水平
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        // 设置RecyclerView间距
        int spacing = dpToPx(mContext, 16);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(1, spacing, false);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        mQuotationList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (null != bundle) {
            position = bundle.getInt(POSITION);
        }

        mAdapter = new QuotationListAdapter(mContext, mQuotationList);
        mAdapter.setQuotationAgainOnItemClickListener(this);
        mAdapter.setShareOnItemClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mQuotationDetailsPresenter = new QuotationDetailsPresenterImpl(this);
        if (0 == position) {
            mQuotationDetailsPresenter.getQuotationList(0, isAll);
        } else if (1 == position) {
            mQuotationDetailsPresenter.getQuotationList(1, isAll);
        } else if (2 == position) {
            mQuotationDetailsPresenter.getQuotationList(3, isAll);
        } else if (3 == position) {
            mQuotationDetailsPresenter.getQuotationList(4, isAll);
        }
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * 在获取报价列表数据成功时回调
     *
     * @param quotationList 报价列表
     */
    @Override
    public void onGetQuotationListSuccess(QuotationList quotationList) {
        if (CODE_SUCCEED != quotationList.getCode()) {
            return;
        }
        mQuotationList.clear();
        DataBean data = quotationList.getData();
        if (null == data) {
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        mQuotationList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 在RecyclerView Item点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        ListBean list = mQuotationList.get(position);
        String share = list.getShare();
        String customerName = list.getCname();
        String orderNumber = list.getOrder_number();
        if (TextUtils.isEmpty(share)) {
            return;
        }
        Intent intent = new Intent(mContext, BrowserActivity.class);
        intent.putExtra(URL, share);
        intent.putExtra(TYPE, ORDER_SHARE_URL_TYPE);
        intent.putExtra(CUSTOMER_NAME, customerName);
        intent.putExtra(ORDER_NUMBER, orderNumber);
        startActivity(intent);
    }

    /**
     * 在RecyclerView 再次报价Item点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onQuotationAgainItemClick(int position) {
        ListBean list = mQuotationList.get(position);
        orderNumber = list.getOrder_number();
        mQuotationDetailsPresenter.quotationAgain(orderNumber, 0);
    }

    /**
     * 在RecyclerView 分享Item点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onShareItemClick(int position) {
        ListBean list = mQuotationList.get(position);
        String name = list.getCname();
        String share = list.getShare();
        String orderNumber = list.getOrder_number();
        String qrCodeFilePath = getSDCardPrivateFilesDir(mContext, DIRECTORY_PICTURES) + File.separator + QR_CODE_IMAGE_PREFIX + orderNumber + ".png";
        if (isSDCardMounted() || !TextUtils.isEmpty(share)) {
            QRCodeUtil.createQRImage(share, 400, 400, null, qrCodeFilePath);
        }
        mQuotationDetailsPresenter.displaySharePopupWindow(mRootLayout, name, orderNumber, qrCodeFilePath);
    }

    /**
     * 在报价单再次报价成功时回调
     *
     * @param quotationAgain 报价单再次报价时服务器端返回的数据
     */
    @Override
    public void onQuotationAgainSuccess(QuotationAgain quotationAgain) {
        if (CODE_SUCCEED == quotationAgain.getCode()) {
            MessageUtil.showMessage(quotationAgain.getMsg());
            /**
             * 发送事件通知"Home界面"获取用户信息成功,通知其切换用户
             * {@link HomeFragment#returnCustomersInfo(CustomersInfo)}
             */
            QuotationAgain.DataBean data = quotationAgain.getData();
            if (null != data) {
                int id = data.getCid();
                String name = data.getCname();
                String logo = data.getLogo();
                EventBus.getDefault().post(new CustomersInfo(id, name, logo));
            }

            /**
             * 发送数据到{@link QuotationFragment#onQuotationAgainSuccess(QuotationAgain)}
             */
            EventBus.getDefault().postSticky(quotationAgain);

            /**
             * 发送数据到{@link MainActivity#ratioActivitySendData(ToFragment)}
             * 使其跳转到对应的Fragment
             */
            ToFragment toFragment = new ToFragment(3);
            EventBus.getDefault().postSticky(toFragment);
            mActivity.finish();
        } else if (202 == quotationAgain.getCode()) {
            QuotationAgain.DataBean data = quotationAgain.getData();
            if (null == data) {
                return;
            }
            String title = data.getTitle();
            String content = data.getContent();
            mQuotationDetailsPresenter.displayHintPopupWindow(mRootLayout, title, content);
        } else {
            MessageUtil.showMessage(quotationAgain.getMsg());
        }
    }

    /**
     * 用户是否确定继续报价的结果
     *
     * @param result true继续 , false不继续
     */
    @Override
    public void continueQuoting(boolean result) {
        if (result) {
            mQuotationDetailsPresenter.quotationAgain(orderNumber, 1);
        } else {

        }
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

    /**
     * 友盟统计Fragment页面
     */
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    /**
     * 友盟统计Fragment页面
     */
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mQuotationDetailsPresenter) {
            mQuotationDetailsPresenter.onDetachView();
            mQuotationDetailsPresenter = null;
        }
    }
}
