package com.ybw.yibai.module.search;

import android.app.Activity;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.bean.SearchRecord;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.CustomDialog;
import com.ybw.yibai.module.search.SearchContract.CallBack;
import com.ybw.yibai.module.search.SearchContract.SearchModel;
import com.ybw.yibai.module.search.SearchContract.SearchPresenter;
import com.ybw.yibai.module.search.SearchContract.SearchView;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * 搜索Presenter实现类
 *
 * @author sjl
 * @date 2019/9/29
 */
@SuppressWarnings("all")
public class SearchPresenterImpl extends BasePresenterImpl<SearchView> implements SearchPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private SearchView mSearchView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private SearchModel mSearchModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public SearchPresenterImpl(SearchView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mSearchView = getView();
        mSearchModel = new SearchModelImpl();
    }

    /**
     * 获取产品sku列表
     *
     * @param keyWord 关键词搜索
     */
    @Override
    public void getSKUList(String keyWord) {
        mSearchModel.getSKUList(keyWord, this);
    }

    /**
     * 在获取产品sku列表成功时回调
     *
     * @param skuList 获取产品sku列表时服务器端返回的数据
     */
    @Override
    public void onGetSKUListSuccess(SKUList skuList) {
        mSearchView.onGetSKUListSuccess(skuList);
    }

    /**
     * 获取搜索记录
     *
     * @param callBack 回调方法
     */
    @Override
    public void getSearchRecord() {
        mSearchModel.getSearchRecord(this);
    }

    /**
     * 在获取搜索记录成功时回调
     *
     * @param searchRecord 搜索记录
     */
    @Override
    public void onGetSearchRecordSuccess(List<SearchRecord> searchRecordList) {
        mSearchView.onGetSearchRecordSuccess(searchRecordList);
    }

    /**
     * 显示删除搜索记录的Dialog
     */
    @Override
    public void displayDeleteSearchRecordDialog() {
        Activity activity = ((Activity) mSearchView);
        if (null != activity) {
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            CustomDialog customDialog = new CustomDialog(activity);
            customDialog.setMessage(activity.getResources().getString(R.string.make_sure_to_clea_all_search_records));
            customDialog.setYesOnclickListener(activity.getResources().getString(R.string.determine),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                        delete();
                    });
            customDialog.setNoOnclickListener(activity.getResources().getString(R.string.cancel),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                    });
            customDialog.show();
        }
    }

    /**
     * 删除搜索记录
     */
    private void delete() {
        try {
            YiBaiApplication.getDbManager().delete(SearchRecord.class);
            mSearchModel.getSearchRecord(this);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
