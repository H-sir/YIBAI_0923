package com.ybw.yibai.module.filter;

import android.view.View;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.utils.FilterUtil;
import com.ybw.yibai.module.filter.FilterContract.FilterPresenter;
import com.ybw.yibai.module.filter.FilterContract.FilterView;

/**
 * 筛选界面Presenter实现类
 *
 * @author sjl
 * @date 2019/9/12
 */
public class FilterPresenterImpl extends BasePresenterImpl<FilterView> implements FilterPresenter {

    private static final String TAG = "FilterPresenterImpl";

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private FilterView mFilterView;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public FilterPresenterImpl(FilterView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mFilterView = getView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        FilterFragment fragment = (FilterFragment) mFilterView;
        if (null == fragment.mParamList || fragment.mParamList.size() == 0) {
            return;
        }

        // 重置
        if (id == R.id.resetTextView) {
            FilterUtil.resetFilterParam(fragment.mParamList);
            fragment.mAdapter.notifyDataSetChanged();
            // 用户选中的产品筛选参数null
            mFilterView.returnSelectProductParam(null);
        }

        // 确定
        if (id == R.id.determineTextView) {
            mFilterView.returnSelectProductParam(FilterUtil.getFilterParam(fragment.mParamList));
        }
    }
}
