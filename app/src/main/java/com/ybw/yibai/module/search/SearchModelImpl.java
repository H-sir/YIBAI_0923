package com.ybw.yibai.module.search;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.bean.SearchRecord;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.search.SearchContract.CallBack;
import com.ybw.yibai.module.search.SearchContract.SearchModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_SUK_LIST_METHOD;

/**
 * 搜索Model实现类
 *
 * @author sjl
 * @date 2019/9/29
 */
@SuppressWarnings("all")
public class SearchModelImpl implements SearchModel {

    private ApiService mApiService;

    public SearchModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取搜索记录
     *
     * @param callBack 回调方法
     */
    @Override
    public void getSearchRecord(CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            List<SearchRecord> searchRecordList = dbManager.findAll(SearchRecord.class);
            if (null != searchRecordList && searchRecordList.size() > 0) {
                Collections.sort(searchRecordList);
                // 取前10个数据
                if (searchRecordList.size() > 10) {
                    searchRecordList = searchRecordList.subList(0, 10);
                }
            }
            callBack.onGetSearchRecordSuccess(searchRecordList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取产品sku列表
     *
     * @param keyWord  关键词搜索
     * @param callBack 回调方法
     */
    @Override
    public void getSKUList(String keyWord, int searchcate, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<SKUList> observable;
        if (searchcate == -1) {
            observable = mApiService.getSKUList(timeStamp,
                    OtherUtil.getSign(timeStamp, GET_SUK_LIST_METHOD),
                    YiBaiApplication.getUid(),
                    1,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    keyWord,
                    null,
                    null);
        } else {
            observable = mApiService.getSKUList(timeStamp,
                    OtherUtil.getSign(timeStamp, GET_SUK_LIST_METHOD),
                    YiBaiApplication.getUid(),
                    1,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    keyWord,
                    searchcate,
                    1);
        }

        Observer<SKUList> observer = new Observer<SKUList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(SKUList skuList) {
                seveSearchRecord(keyWord);
                callBack.onGetSKUListSuccess(skuList);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onRequestFailure(e);
            }

            @Override
            public void onComplete() {
                callBack.onRequestComplete();
            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    /**
     * 保存搜索记录
     *
     * @param keyWord 搜索关键词
     */
    private void seveSearchRecord(String keyWord) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            SearchRecord searchRecord = dbManager.findById(SearchRecord.class, keyWord);
            if (null == searchRecord) {
                SearchRecord record = new SearchRecord();
                record.setTime(TimeUtil.getTimeStamp());
                record.setContent(keyWord);
                dbManager.save(record);
            } else {
                searchRecord.setTime(TimeUtil.getTimeStamp());
                // 更新quotation列名为time的数据
                dbManager.update(searchRecord, "time");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
