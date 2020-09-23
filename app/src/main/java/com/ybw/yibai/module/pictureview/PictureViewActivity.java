package com.ybw.yibai.module.pictureview;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.adapter.PictureViewAdapter;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SelectImage;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.module.drawing.SimulationDrawingActivity;
import com.ybw.yibai.module.quotation.QuotationFragment;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Preferences.PHOTO_URL_LIST;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.SELECT_IMAGE;
import static com.ybw.yibai.common.constants.Preferences.TYPE;

/**
 * 图片查看
 *
 * @author sjl
 */
public class PictureViewActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String TAG = "PictureViewActivity";

    /**
     *
     */
    private int type;

    /**
     *
     */
    private int size;

    /**
     *
     */
    private int position;

    /**
     *
     */
    private boolean selectImage;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     *
     */
    private RelativeLayout mBarView;

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 确定
     */
    private TextView mDetermineTextView;

    /**
     *
     */
    private ViewPager mViewPager;

    /**
     * 指示器
     */
    private TextView mIndexTextView;

    /**
     * 图片地址列表
     */
    private List<File> mPicFileList;

    /**
     * 图片地址列表
     */
    private List<String> mPicUrlList;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     *
     */
    private PictureViewAdapter mPictureViewAdapter;

    @Override
    protected int setLayout() {
        return R.layout.activity_picture_view;
    }

    @Override
    protected void initView() {
        mBarView = findViewById(R.id.barView);
        mBackImageView = findViewById(R.id.backImageView);
        mDetermineTextView = findViewById(R.id.determineTextView);
        mViewPager = findViewById(R.id.viewPager);
        mIndexTextView = findViewById(R.id.indexTextView);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mPicFileList = new ArrayList<>();
        mPicUrlList = new ArrayList<>();

        Intent intent = getIntent();
        type = intent.getIntExtra(TYPE, 0);
        position = intent.getIntExtra(POSITION, 0);
        selectImage = intent.getBooleanExtra(SELECT_IMAGE, false);
        String jsonString = intent.getStringExtra(PHOTO_URL_LIST);

        if (1 == type) {
            List<File> picFileList = new Gson().fromJson(jsonString, new TypeToken<List<File>>() {
            }.getType());
            mPicFileList.addAll(picFileList);
            size = mPicFileList.size();
        } else {
            List<String> picUrlList = new Gson().fromJson(jsonString, new TypeToken<List<String>>() {
            }.getType());
            mPicUrlList.addAll(picUrlList);
            size = mPicUrlList.size();
        }

        String string = position + 1 + "/" + size;
        mIndexTextView.setText(string);

        mPictureViewAdapter = new PictureViewAdapter(this, type, mPicFileList, mPicUrlList);
        mViewPager.setAdapter(mPictureViewAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(position);

        if (selectImage) {
            mBarView.setVisibility(View.VISIBLE);
        } else {
            OtherUtil.transparentStatusBar(this);
        }
    }

    @Override
    protected void initEvent() {
        mBackImageView.setOnClickListener(this);
        mDetermineTextView.setOnClickListener(this);
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

        // 确定
        if (id == R.id.determineTextView) {
            if (1 != type) {
                return;
            }
            /**
             * 发送数据到{@link QuotationFragment#pictureViewActivityReturnImageData(SelectImage)}
             * 发送数据到{@link SimulationDrawingActivity#pictureViewActivityReturnImageData(SelectImage)}
             */
            File file = mPicFileList.get(position);
            SelectImage selectImage = new SelectImage(file);
            EventBus.getDefault().post(selectImage);
            finish();
        }
    }

    @Override
    public void onPageSelected(int position) {
        String string = position + 1 + "/" + size;
        mIndexTextView.setText(string);
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 获取返回键操作
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
            // 淡入淡出动画效果
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
