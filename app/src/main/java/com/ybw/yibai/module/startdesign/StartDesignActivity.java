package com.ybw.yibai.module.startdesign;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.SceneBackgroundAdapter;
import com.ybw.yibai.common.bean.CreateSceneData;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.utils.SPUtil;
import com.ybw.yibai.module.scene.SceneActivity;
import com.ybw.yibai.module.startdesign.StartDesignContract.StartDesignPresenter;
import com.ybw.yibai.module.startdesign.StartDesignContract.StartDesignView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Preferences.CREATE_SCENE_DATA_LIST;
import static com.ybw.yibai.common.constants.Preferences.DESIGN_CREATE;
import static com.ybw.yibai.common.utils.OtherUtil.transparentStatusBar;

/**
 * 开始设计
 *
 * @author sjl
 * @date 2019/11/12
 */
public class StartDesignActivity extends FragmentActivity implements View.OnClickListener,
        StartDesignView, SceneBackgroundAdapter.OnItemClickListener, SceneBackgroundAdapter.OnDeleteItemClickListener {

    private final String TAG = "StartDesignActivity";

    /**
     * 用户点击的场景名称在"创建场景时需要的数据"中的位置
     */
    private int position;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 界面Root布局
     */
    private View mRootLayout;

    /**
     * 关闭
     */
    private View mView;

    /**
     * 关闭
     */
    private ImageView mCloseImageView;

    /**
     * 没有数据显示的UI
     */
    private View mNoDataLayout;

    /**
     * 有数据显示的UI
     */
    private View mHaveDataLayout;

    /**
     *
     */
    private RecyclerView mRecyclerView;

    /**
     * 相机
     */
    private ImageView mTakePhotoImageView;

    /**
     * 相册
     */
    private ImageView mAlbumImageView;

    /**
     * 开始设计
     */
    private Button mStartDesignButton;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 预设场景名称列表
     */
    private List<String> mSceneNameList;

    /**
     * 创建场景时需要的数据
     */
    private List<CreateSceneData> mCreateSceneDataList;

    /**
     * 开始设计的场景列表适配器
     */
    private SceneBackgroundAdapter mAdapter;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 要申请的权限(访问手机相机权限)
     */
    private String[] permissions = {
            Manifest.permission.CAMERA
    };

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private StartDesignPresenter mStartDesignPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_design);
        transparentStatusBar(this);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mRootLayout = findViewById(R.id.rootLayout);
        mView = findViewById(R.id.view);
        mCloseImageView = findViewById(R.id.closeImageView);

        mNoDataLayout = findViewById(R.id.noDataLayout);
        mHaveDataLayout = findViewById(R.id.haveDataLayout);

        mRecyclerView = findViewById(R.id.recyclerView);
        mTakePhotoImageView = findViewById(R.id.takePhotoImageView);
        mAlbumImageView = findViewById(R.id.albumImageView);
        mStartDesignButton = findViewById(R.id.startDesignButton);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        // 设置RecyclerView间距
        int spacing = DensityUtil.dpToPx(this, 5);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(3, spacing, false);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(decoration);
    }

    private void initData() {
        mSceneNameList = new ArrayList<>();
        mCreateSceneDataList = new ArrayList<>();
        mAdapter = new SceneBackgroundAdapter(this, mCreateSceneDataList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnDeleteItemClickListener(this);
        mAdapter.setOnItemClickListener(this);

        SystemParameter systemParameter = YiBaiApplication.getSystemParameter();
        if (null != systemParameter) {
            SystemParameter.DataBean data = systemParameter.getData();
            if (null != data) {
                mSceneNameList = data.getScene();
            }
        }

        Intent intent = getIntent();
        if (null != intent) {
            designCreate = intent.getBooleanExtra(DESIGN_CREATE, false);
        }
    }

    /**
     * 是否需要新建设计号
     */
    private boolean designCreate = false;

    private void initEvent() {
        // https://www.itcodemonkey.com/article/5148.html
        SlidrConfig build = new SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                // 滑动开始时Activity之间蒙层颜色的透明度,0-1f,默认值0.8f
                .scrimStartAlpha(0.5f)
                // 滑动结束时Activity之间蒙层颜色的透明度,0-1f,默认值0f
                .scrimEndAlpha(0.5f)
                // 边界占屏幕大小30%
                .edgeSize(0.3f)
                .edge(true)
                .build();
        Slidr.attach(this, build);

        mStartDesignPresenter = new StartDesignPresenterImpl(this);
        mView.setOnClickListener(this);
        mCloseImageView.setOnClickListener(this);
        mTakePhotoImageView.setOnClickListener(this);
        mAlbumImageView.setOnClickListener(this);
        mStartDesignButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 关闭
        if (id == R.id.view) {
            onBackPressed();
        }

        // 关闭
        if (id == R.id.closeImageView) {
            onBackPressed();
        }

        // 相机
        if (id == R.id.takePhotoImageView || id == R.id.cameraImageView) {
            mStartDesignPresenter.applyPermissions(permissions);
        }

        // 相册
        if (id == R.id.albumImageView || id == R.id.photoAlbumImageView) {
            mStartDesignPresenter.openPhotoAlbum();
        }

        // 开始设计
        if (id == R.id.startDesignButton) {
            if (mSceneNameList.isEmpty()) {
                MessageUtil.showMessage(getResources().getString(R.string.you_need_to_select_at_least_one_design_view_to_start_designing));
            } else {
                Intent intent = new Intent(this, SceneActivity.class);
                intent.putParcelableArrayListExtra(CREATE_SCENE_DATA_LIST, (ArrayList<? extends Parcelable>) mCreateSceneDataList);
                intent.putExtra(DESIGN_CREATE, designCreate);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }
    }

    /**
     * 在RecyclerView Item 里的删除被点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onDeleteItemClick(int position) {
        mCreateSceneDataList.remove(position);
        if (mCreateSceneDataList.size() > 0) {
            mNoDataLayout.setVisibility(View.GONE);
            mHaveDataLayout.setVisibility(View.VISIBLE);
        } else {
            mNoDataLayout.setVisibility(View.VISIBLE);
            mHaveDataLayout.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 在RecyclerView Item 里的场景名称被点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onSceneNameItemClick(int position) {
        this.position = position;
        String oldSceneName = mCreateSceneDataList.get(position).getName();
        mStartDesignPresenter.displaySceneNamePopupWindow(mRootLayout, mSceneNameList, oldSceneName);
    }

    /**
     * 在显示预设场景名称的RecyclerView的Item被点击时回调
     *
     * @param name 被点击的Item位置的场景名称
     */
    @Override
    public void onPresetSceneNameItemClick(String name) {
        mCreateSceneDataList.get(this.position).setName(name);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    /**
     * 申请权限的结果
     *
     * @param b true 已经获取全部权限,false 没有获取全部权限
     */
    @Override
    public void applyPermissionsResults(boolean b) {
        if (b) {
            // 打开相机
            mStartDesignPresenter.openCamera();
        }
    }

    /**
     * 请求权限的结果的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获得申请的全部权限
        if (PermissionsUtil.allPermissionsAreGranted(grantResults)) {
            // 打开相机
            mStartDesignPresenter.openCamera();
        } else {
            PermissionsUtil.showNoCameraPermissions(this);
        }
    }

    /**
     * 获得Activity返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mStartDesignPresenter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 返回从相机或相册返回的图像
     *
     * @param file 图像文件
     */
    @Override
    public void returnsTheImageReturnedFromTheCameraOrAlbum(File file) {
        mNoDataLayout.setVisibility(View.GONE);
        mHaveDataLayout.setVisibility(View.VISIBLE);
        mCreateSceneDataList.add(new CreateSceneData(file));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onShowLoading() {

    }

    @Override
    public void onHideLoading() {

    }

    @Override
    public void onLoadDataFailure(Throwable throwable) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mStartDesignPresenter) {
            mStartDesignPresenter.onDetachView();
            mStartDesignPresenter = null;
        }
    }
}
