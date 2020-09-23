package com.ybw.yibai.module.cases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.CaseAdapter;
import com.ybw.yibai.common.bean.Case;
import com.ybw.yibai.common.bean.CaseClassify.DataBean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.classs.SpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.cases.CaseContract.CasePresenter;
import com.ybw.yibai.module.cases.CaseContract.CaseView;
import com.ybw.yibai.module.pictureview.PictureViewActivity;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.PHOTO_URL_LIST;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.PROJECT_CLASSIFY_INFO;
import static com.ybw.yibai.common.constants.Preferences.TYPE;

/**
 * 案例Fragment
 *
 * @author sjl
 * @date 2019/9/5
 */
public class CaseFragment extends BaseFragment implements CaseView, CaseAdapter.OnItemClickListener {

    /**
     * 案例分类Id
     */
    private int projectClassifyId;

    /**
     * 显示案例列表
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
     * 公司案例列表中的图片地址列表
     */
    private List<String> mPicUrlList;

    /**
     * 公司案例列表
     */
    private List<Case.DataBean.ListBean> mCaseList;

    /**
     * 案例的适配器
     */
    private CaseAdapter mAdapter;

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
    private CasePresenter mCasePresenter;

    @Override
    protected int setLayouts() {
        return R.layout.fragment_case;
    }

    @Override
    protected void findViews(View view) {
        mContext = getContext();
        mActivity = getActivity();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mWaitDialog = new WaitDialog(mContext);
    }

    @Override
    protected void initView() {
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 防止item 交换位置
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        // 设置RecyclerView间距
        SpacingItemDecoration decoration = new SpacingItemDecoration(DensityUtil.dpToPx(mContext, 5));
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);
        // 添加间距
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {
        mPicUrlList = new ArrayList<>();
        mCaseList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }
        ListBean listBean = (ListBean) bundle.getSerializable(PROJECT_CLASSIFY_INFO);
        if (null != listBean) {
            projectClassifyId = listBean.getId();
        }

        mAdapter = new CaseAdapter(mContext, mCaseList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initEvent() {
        mCasePresenter = new CasePresenterImpl(this);
        mCasePresenter.getCase(projectClassifyId);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * 在获取公司案例成功时回调
     *
     * @param case_ 公司案例
     */
    @Override
    public void onGetCaseSuccess(Case case_) {
        if (CODE_SUCCEED != case_.getCode()) {
            return;
        }
        Case.DataBean data = case_.getData();
        if (null == data) {
            return;
        }
        List<Case.DataBean.ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        mCaseList.addAll(list);
        for (int i = 0; i < mCaseList.size(); i++) {
            String pic = mCaseList.get(i).getPic();
            if (TextUtils.isEmpty(pic)) {
                continue;
            }
            mPicUrlList.add(pic);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        // 查看图片
        Intent intent = new Intent(mContext, PictureViewActivity.class);
        intent.putExtra(POSITION, position);
        if (null != mPicUrlList && mPicUrlList.size() > 0) {
            intent.putExtra(TYPE, 0);
            intent.putExtra(PHOTO_URL_LIST, new Gson().toJson(mPicUrlList));
        }
        startActivity(intent);
        // 淡入淡出动画效果
        mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
        if (null != mCasePresenter) {
            mCasePresenter.onDetachView();
            mCasePresenter = null;
        }
    }
}