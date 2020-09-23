package com.ybw.yibai.module.oldcustomer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.common.adapter.CustomersListAdapter;
import com.ybw.yibai.common.bean.CustomerList.DataBean.ListBean;
import com.ybw.yibai.common.bean.CustomersInfo;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.classs.GridSpacingItemDecorations;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.module.home.HomeFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_LIST;

/**
 * 老客户列表Fragment
 *
 * @author sjl
 * @date 2019/9/16
 */
public class OldCustomerListFragment extends BaseFragment implements CustomersListAdapter.OnItemClickListener {

    /**
     * 显示客户列表
     */
    private RecyclerView mRecyclerView;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 客户列表
     */
    private List<ListBean> mCustomersList;

    /**
     * 客户列表适配器
     */
    private CustomersListAdapter mAdapter;

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

    @Override
    protected int setLayouts() {
        mContext = getContext();
        return R.layout.fragment_old_customers_list;
    }

    @Override
    protected void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    protected void initView() {
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        int x = DensityUtil.dpToPx(mContext, 20);
        int y = DensityUtil.dpToPx(mContext, 12);
        // 设置RecyclerView间距
        GridSpacingItemDecorations decoration = new GridSpacingItemDecorations(3, x, y, false);
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
        mCustomersList = new Gson().fromJson(bundle.getString(CUSTOMERS_LIST), new TypeToken<List<ListBean>>() {
        }.getType());
        if (null == mCustomersList) {
            return;
        }
        mAdapter = new CustomersListAdapter(mContext, mCustomersList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener(this);
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
        ListBean listBean = mCustomersList.get(position);
        int id = listBean.getId();
        String name = listBean.getName();
        String logo = listBean.getLogo();
        /**
         * 发送事件通知"Home界面"获取用户信息成功
         * {@link HomeFragment#returnCustomersInfo(CustomersInfo)}
         */
        EventBus.getDefault().post(new CustomersInfo(id, name, logo));

        Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        getActivity().finish();
    }
}
