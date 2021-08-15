package com.ybw.yibai.module.producttype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.ProductAdapter;
import com.ybw.yibai.common.adapter.ProductSecondaryClassifyFilterAdapter;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.bean.SKUList.DataBean;
import com.ybw.yibai.common.bean.SelectProductParam;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.bean.SystemParameter.DataBean.CatelistBean;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.module.details.ProductDetailsActivity;
import com.ybw.yibai.module.filter.FilterFragment;
import com.ybw.yibai.module.producttype.ProductTypeContract.ProductTypePresenter;
import com.ybw.yibai.module.producttype.ProductTypeContract.ProductTypeView;
import com.ybw.yibai.module.scene.SceneActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.CATEGORY_CODE;
import static com.ybw.yibai.common.constants.Preferences.KEY_WORD;
import static com.ybw.yibai.common.constants.Preferences.PLANT;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.POT;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_ID;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ID;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_NAME;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_USE_STATE_ID;
import static com.ybw.yibai.common.constants.Preferences.TYPE;
import static com.ybw.yibai.common.utils.FilterUtil.getFilterParam;

/**
 * 产品类型Fragment
 *
 * @author sjl
 * @date 2019/9/9
 */
public class ProductTypeFragment extends BaseFragment implements ProductTypeView,
        ProductSecondaryClassifyFilterAdapter.OnItemClickListener, ProductAdapter.OnItemClickListener {

    public final String TAG = "ProductTypeFragment";

    /**
     * 当前Fragment在ViewPager中的位置
     */
    private int position;

    /**
     * 产品使用状态Id
     */
    private int productUseStateId;

    /**
     * 标记用户是从那个界面打开本界面的
     * 如果从"产品/首页"界面打开,那么点击Item时进入"产品详情界面"
     * 如果从"更多"界面打开,那么点击Item时将产品保存到模拟列表数据中
     */
    private String type;

    /**
     * 搜索关键词
     */
    private String keyWord;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 是否获取全部:1获取全部0分页(默认为0分页)
     */
    private int isAll = 1;

    /**
     * 产品类别(产品筛选参数大类别名)默认获取植物
     */
    private String categoryCode;

    /**
     * 产品类别id(多个用逗号拼接)
     */
    private String pcId;

    /**
     * 动态设置的参数
     */
    private Map<String, String> paramMap;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 显示二级分类筛选条件列表
     */
    private RecyclerView mClassifyRecyclerView;

    /**
     * 显示产品列表
     */
    private RecyclerView mRecyclerView;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 二级分类筛选条件列表
     */
    private List<CatelistBean.ListBean> mClassifyList;

    /**
     * 产品二级分类过滤器
     */
    private ProductSecondaryClassifyFilterAdapter mFilterAdapter;

    /**
     * 产品sku列表
     */
    private List<ListBean> mSKUList;

    /**
     * 产品列表适配器
     */
    private ProductAdapter mProductAdapter;

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
    private ProductTypePresenter mProductTypePresenter;

    @Override
    protected int setLayouts() {
        return R.layout.fragment_product_type;
    }

    @Override
    protected void findViews(View view) {
        mContext = getContext();
        mActivity = getActivity();
        mClassifyRecyclerView = view.findViewById(R.id.classifyRecyclerView);
        mRecyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    protected void initView() {
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager classifyManager = new StaggeredGridLayoutManager(1, VERTICAL);
        // 布局横向
        classifyManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置RecyclerView间距,每一行Item的数目1000(1000为假设的数据)
        int classifyGap = DensityUtil.dpToPx(mContext, 12);
        GridSpacingItemDecoration classifyDecoration = new GridSpacingItemDecoration(1000, classifyGap, false);
        mClassifyRecyclerView.setLayoutManager(classifyManager);
        mClassifyRecyclerView.addItemDecoration(classifyDecoration);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, VERTICAL);
        // 设置RecyclerView间距
        int gap = DensityUtil.dpToPx(mContext, 8);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(3, gap, false);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void initData() {
        mClassifyList = new ArrayList<>();
        mSKUList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (null != bundle) {
            position = bundle.getInt(POSITION);
            productUseStateId = bundle.getInt(PRODUCT_USE_STATE_ID);
            categoryCode = bundle.getString(CATEGORY_CODE);
            type = bundle.getString(TYPE);
            keyWord = bundle.getString(KEY_WORD);
        }

        mFilterAdapter = new ProductSecondaryClassifyFilterAdapter(mContext, mClassifyList);
        mClassifyRecyclerView.setAdapter(mFilterAdapter);
        mFilterAdapter.setOnItemClickListener(this);

        mProductAdapter = new ProductAdapter(mContext, mSKUList);
        mRecyclerView.setAdapter(mProductAdapter);
        mProductAdapter.setOnItemClickListener(this);

        getClassifyList();
        paramMap = getFilterParam(categoryCode);
    }

    /**
     * 获取二级分类筛选条件列表
     */
    private void getClassifyList() {
        SystemParameter systemParameter = YiBaiApplication.getSystemParameter();
        if (null == systemParameter) {
            return;
        }
        SystemParameter.DataBean data = systemParameter.getData();
        if (null == data) {
            return;
        }
        List<CatelistBean> cateList = data.getCatelist();
        if (null == cateList || cateList.size() == 0) {
            return;
        }
        for (CatelistBean catelistBean : cateList) {
            String cateCode = catelistBean.getCate_code();
            List<CatelistBean.ListBean> list = catelistBean.getList();
            if (!cateCode.equals(categoryCode) || null == list || list.size() == 0) {
                continue;
            }
            mClassifyList.addAll(list);
            break;
        }
        if (null == mClassifyList || mClassifyList.size() == 0) {
            mClassifyRecyclerView.setVisibility(View.GONE);
        }
        // 初始化为false
        for (CatelistBean.ListBean classify : mClassifyList) {
            classify.setSelect(false);
        }
        mFilterAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mProductTypePresenter = new ProductTypePresenterImpl(this);
        mProductTypePresenter.getSKUList(isAll, productUseStateId, categoryCode, null, keyWord, paramMap);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * 在显示产品二级分类RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onClassifyFilterItemClick(int position) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = mClassifyList.size();
        for (int i = 0; i < size; i++) {
            CatelistBean.ListBean classify = mClassifyList.get(i);
            if (!classify.isSelect()) {
                continue;
            }
            if (!TextUtils.isEmpty(stringBuilder)) {
                stringBuilder.append(",");
            }
            int id = classify.getId();
            stringBuilder.append(id);
        }
        pcId = stringBuilder.toString().trim();
        mProductTypePresenter.getSKUList(isAll, productUseStateId, categoryCode, pcId, keyWord, paramMap);
    }

    /**
     * 在显示产品列表RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        ListBean listBean = mSKUList.get(position);
        mSelectItem = listBean;
        if (TextUtils.isEmpty(type)) {
            // 说明从"产品"/"首页"界面打开
            int productId = listBean.getProduct_id();
            int productSkuId = listBean.getSku_id();
            String name = listBean.getName();
            Intent intent = new Intent(mContext, ProductDetailsActivity.class);
            intent.putExtra(PRODUCT_ID, productId);
            intent.putExtra(PRODUCT_SKU_ID, productSkuId);
            intent.putExtra(PRODUCT_SKU_NAME, name);
            startActivity(intent);
        } else {
            // 说明从"更多"界面打开
            if (listBean.getComtype() == 2)
                listBean.setCategoryCode(PLANT);
            else
                listBean.setCategoryCode(POT);
            mProductTypePresenter.saveSimulation(listBean);
        }
    }

    /**
     * 在获取产品sku列表成功时回调
     *
     * @param skuList 获取产品sku列表时服务器端返回的数据
     */
    @Override
    public void onGetSKUListSuccess(SKUList skuList) {
        if (CODE_SUCCEED != skuList.getCode()) {
            return;
        }
        mSKUList.clear();
        DataBean data = skuList.getData();
        if (null == data) {
            mProductAdapter.notifyDataSetChanged();
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            mProductAdapter.notifyDataSetChanged();
            return;
        }
        mSKUList.addAll(list);
        mProductAdapter.notifyDataSetChanged();
    }

    ListBean mSelectItem = null;

    /**
     * 在保存"模拟"成功/失败时回调
     *
     * @param result true成功,false失败
     */
    @Override
    public void onSaveSimulationResult(boolean result) {
        Intent intent = new Intent(mContext, SceneActivity.class);
        if (mSelectItem != null) {
            intent.putExtra("com_type", String.valueOf(mSelectItem.getComtype()));
        }
        startActivity(intent);
        mActivity.finish();
    }

    /**
     * EventBus
     * 接收用户从{@link FilterFragment#returnSelectProductParam(Map)}
     * 发送过来的数据(在返回用户选中的产品筛选参数)
     *
     * @param selectProductParam 用户选中的产品筛选参数
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void returnSelectProductParam(SelectProductParam selectProductParam) {
        int position = selectProductParam.getPosition();
        if (this.position != position) {
            return;
        }
        paramMap = selectProductParam.getSelectProductParamMap();
        mProductTypePresenter.getSKUList(isAll, productUseStateId, categoryCode, pcId, keyWord, paramMap);
    }

    /**
     * 在请求网络数据之前显示Loading界面
     */
    @Override
    public void onShowLoading() {

    }

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    @Override
    public void onHideLoading() {

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
        if (null != mProductTypePresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mProductTypePresenter.onDetachView();
            mProductTypePresenter = null;
        }
    }
}