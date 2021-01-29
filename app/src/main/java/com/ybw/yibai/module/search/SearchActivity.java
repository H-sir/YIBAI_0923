package com.ybw.yibai.module.search;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.ProductAdapter;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.bean.SKUList.DataBean;
import com.ybw.yibai.common.bean.SearchRecord;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.spinner.SpinnerPopWindow;
import com.ybw.yibai.module.details.ProductDetailsActivity;
import com.ybw.yibai.module.search.SearchContract.SearchPresenter;
import com.ybw.yibai.module.search.SearchContract.SearchView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_ID;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_ID;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_SKU_NAME;

/**
 * 搜索
 *
 * @author sjl
 * @date 2019/9/19
 * https://blog.csdn.net/yaojie5519/article/details/80899436
 */
public class SearchActivity extends BaseActivity implements SearchView,
        TextView.OnEditorActionListener, ProductAdapter.OnItemClickListener, View.OnClickListener {

    private SearchActivity mSearchActivity = null;
    /**
     * 搜索内容
     */
    private EditText mEditText;

    /**
     * 搜索分类
     */
    private TextView mSpnnerText;

    /**
     * 取消搜索
     */
    private TextView mCancelTextView;

    /**
     * 最近搜索
     */
    private LinearLayout mRecentSearchLayout;

    /**
     * 删除搜索记录
     */
    private ImageView mDeleteImageView;

    /**
     * 流式布局
     */
    private FlexboxLayout mFlexBoxLayout;

    /**
     * 显示搜索内容
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
     * 产品sku列表
     */
    private List<ListBean> mSKUList;

    /**
     * 产品列表适配器
     */
    private ProductAdapter mAdapter;

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
    private SearchPresenter mSearchPresenter;

    private List<String> mSearchSpinnerList = new ArrayList<>();
    private SpinnerPopWindow<String> mSpinnerPopWindowType;


    @Override
    protected int setLayout() {
        mSearchActivity = this;
        return R.layout.activity_search;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView() {
        mSpnnerText = findViewById(R.id.spnnerText);
        mEditText = findViewById(R.id.editText);
        mCancelTextView = findViewById(R.id.cancelTextView);
        mRecentSearchLayout = findViewById(R.id.recentSearchLayout);
        mDeleteImageView = findViewById(R.id.deleteImageView);
        mFlexBoxLayout = findViewById(R.id.flexBoxLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mWaitDialog = new WaitDialog(this);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        // 设置RecyclerView间距
        int spacing = DensityUtil.dpToPx(this, 8);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(3, spacing, false);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(decoration);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @Override
    protected void initData() {
        mSKUList = new ArrayList<>();
        mAdapter = new ProductAdapter(this, mSKUList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        SystemParameter systemParameter = YiBaiApplication.getSystemParameter();
        List<SystemParameter.DataBean.SearchcateBean> searchcate = systemParameter.getData().getSearchcate();
        if (searchcate != null && searchcate.size() > 0) {
            for (Iterator<SystemParameter.DataBean.SearchcateBean> iterator = systemParameter.getData().getSearchcate().iterator(); iterator.hasNext(); ) {
                SystemParameter.DataBean.SearchcateBean next = iterator.next();
                mSearchSpinnerList.add(next.getName());
            }
        }

        mSpinnerPopWindowType = new SpinnerPopWindow<>(mSearchActivity, mSearchSpinnerList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mSpinnerPopWindowType.dismiss();
                mSpnnerText.setText(mSearchSpinnerList.get(position));
                search();
            }
        });
        mSpnnerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpinnerPopWindowType.setWidth(mSpnnerText.getWidth());
                mSpinnerPopWindowType.showAsDropDown(mSpnnerText);
                setTextImage(R.mipmap.up, mSpnnerText);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //每次内容改变时，先移除前面的消息，避免太过频繁的搜索
                taskHandler.removeMessages(1);
                if (s.length() <= 0){
                    return;
                }
                //间隔500后发送消息
                taskHandler.sendEmptyMessageDelayed(1, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private Handler taskHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //收到消息
            search();
            return false;
        }
    });

    /**
     * 给TextView右边设置图片
     *
     * @param resId
     * @param binding_anew_spinner
     */
    private void setTextImage(int resId, TextView binding_anew_spinner) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        binding_anew_spinner.setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    protected void initEvent() {
        mSearchPresenter = new SearchPresenterImpl(this);
        mSearchPresenter.getSearchRecord();
        mCancelTextView.setOnClickListener(this);
        mDeleteImageView.setOnClickListener(this);
        mEditText.setOnEditorActionListener(this);


        if(mSearchSpinnerList != null && mSearchSpinnerList.size() > 0){
            mSpnnerText.setText(mSearchSpinnerList.get(0));
            search();
        }
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 取消
        if (id == R.id.cancelTextView) {
            onBackPressed();
        }

        // 删除搜索记录
        if (id == R.id.deleteImageView) {
            mSearchPresenter.displayDeleteSearchRecordDialog();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
            return true;
        }
        return false;
    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        ListBean listBean = mSKUList.get(position);
        int productId = listBean.getProduct_id();
        int productSkuId = listBean.getSku_id();
        String name = listBean.getName();
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        intent.putExtra(PRODUCT_SKU_ID, productSkuId);
        intent.putExtra(PRODUCT_SKU_NAME, name);
        startActivity(intent);
    }

    /**
     * 在获取搜索记录成功时回调
     *
     * @param searchRecordList 搜索记录
     */
    @Override
    public void onGetSearchRecordSuccess(List<SearchRecord> searchRecordList) {
        mFlexBoxLayout.removeAllViews();
        if (null != searchRecordList && searchRecordList.size() > 0) {
            for (int i = 0; i < searchRecordList.size(); i++) {
                SearchRecord searchRecord = searchRecordList.get(i);
                String content = searchRecord.getContent();
                View view = getFleBoxLayoutItemView(content);
                mFlexBoxLayout.addView(view);
            }
            mDeleteImageView.setVisibility(View.VISIBLE);
        } else {
            mDeleteImageView.setVisibility(View.INVISIBLE);
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
            MessageUtil.showMessage(skuList.getMsg());
            return;
        }
        mRecentSearchLayout.setVisibility(View.GONE);
        mSearchPresenter.getSearchRecord();
        DataBean data = skuList.getData();
        if (null == data) {
            return;
        }
        List<ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        mSKUList.clear();
        mSKUList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private View getFleBoxLayoutItemView(String text) {
        View view = getLayoutInflater().inflate(R.layout.flex_box_item_layout, null);
        TextView textView = view.findViewById(R.id.textView);
        textView.setOnClickListener(v -> {
            String string = textView.getText().toString();
            if (TextUtils.isEmpty(string)) {
                return;
            }
            mEditText.setText(string);
            mEditText.setSelection(string.length());
            search();
        });
        textView.setText(text);
        return view;
    }

    /**
     * 搜索
     */
    private void search() {
        SystemParameter systemParameter = YiBaiApplication.getSystemParameter();
        List<SystemParameter.DataBean.SearchcateBean> searchcate = systemParameter.getData().getSearchcate();
        int productId = -1;
        if (searchcate != null && searchcate.size() > 0) {
            String product = mSpnnerText.getText().toString();
            for (Iterator<SystemParameter.DataBean.SearchcateBean> iterator = systemParameter.getData().getSearchcate().iterator(); iterator.hasNext(); ) {
                SystemParameter.DataBean.SearchcateBean next = iterator.next();
                if (product.equals(next.getName())) {
                    productId = next.getId();
                    break;
                }
            }
        }

        String keyWord = mEditText.getText().toString().trim();
        mSearchPresenter.getSKUList(keyWord,productId);
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
    public void onBackPressed() {
        if (mRecentSearchLayout.getVisibility() == View.GONE) {
            mSKUList.clear();
            mEditText.setText("");
            mAdapter.notifyDataSetChanged();
            mRecentSearchLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mSearchPresenter) {
            mSearchPresenter.onDetachView();
            mSearchPresenter = null;
        }
    }
}
