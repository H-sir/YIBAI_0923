package com.ybw.yibai.module.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.FragmentPagerAdapter;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.bean.SystemParameter.DataBean.UsestateBean;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.module.productusestate.ProductUseStateFragment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.ybw.yibai.common.constants.Preferences.KEY_WORD;
import static com.ybw.yibai.common.constants.Preferences.TYPE;

/**
 * 更多界面
 *
 * @author sjl
 */
public class MoreActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private final String TAG = "MoreActivity";

    /**
     * 搜索关键词
     */
    private String keyWord;
    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 搜索框
     */
    private EditText mEditText;

    /**
     * 搜索
     */
    private TextView mSearchTextView;

    /**
     *
     */
    private ViewPager mViewPager;

    @Override
    protected int setLayout() {
        return R.layout.activity_more;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mEditText = findViewById(R.id.editText);
        mSearchTextView = findViewById(R.id.searchTextView);
        mViewPager = findViewById(R.id.viewPager);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @Override
    protected void initData() {
        getSystemParameterSuccess(YiBaiApplication.getSystemParameter());
    }

    @Override
    protected void initEvent() {
        mBackImageView.setOnClickListener(this);
        mSearchTextView.setOnClickListener(this);
        mEditText.setOnEditorActionListener(this);

        initEditTextEvent();
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 返回
        if (id == R.id.backImageView) {
            onBackPressed();
        }

        // 搜索
        if (id == R.id.searchTextView) {
            search();
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

    private void initEditTextEvent() {
        Observable<CharSequence> observable = RxTextView
                .textChanges(mEditText)
                .skip(1)
                .debounce(1, TimeUnit.SECONDS);
        Observer<CharSequence> observer = new Observer<CharSequence>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CharSequence charSequence) {
                search();
            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onComplete() {

            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 搜索
     */
    private void search() {
        keyWord = mEditText.getText().toString();
        getSystemParameterSuccess(YiBaiApplication.getSystemParameter());
    }

    /**
     * 处理在获取系统参数成功时的逻辑
     *
     * @param systemParameter 系统参数
     */
    public void getSystemParameterSuccess(SystemParameter systemParameter) {
        if(null == systemParameter)return;
        SystemParameter.DataBean data = systemParameter.getData();
        if(null == data)return;
        List<UsestateBean> useState = data.getUsestate();
        if(null == useState || useState.size() == 0)return;
        Fragment[] fragment = new Fragment[1];
        fragment[0] = new ProductUseStateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, TAG);
        if(!TextUtils.isEmpty(keyWord)){
            bundle.putString(KEY_WORD, keyWord);
        }
        fragment[0].setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        mViewPager.removeAllViews();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager, fragment));
    }
}