package com.ybw.yibai.module.feedback;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.adapter.FeedBackImageAdapter;
import com.ybw.yibai.common.bean.FeedBack;
import com.ybw.yibai.common.bean.FeedBackImage;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.feedback.FeedBackContract.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;

/**
 * 意见反馈
 *
 * @author sjl
 * @date 2019/10/24
 */
public class FeedBackActivity extends BaseActivity implements
        FeedBackView, View.OnClickListener, FeedBackImageAdapter.OnItemClickListener {

    /**
     * 界面Root布局
     */
    private RelativeLayout mRootLayout;

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 反馈内容
     */
    private EditText mEditText;

    /**
     * 反馈图片列表
     */
    private RecyclerView mRecyclerView;

    /**
     * 联系方式
     */
    private EditText mContactEditText;

    /**
     * 提交
     */
    private Button mSubmitButton;

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
     *
     */
    private List<FeedBackImage> mFeedBackImageList;

    /**
     *
     */
    private FeedBackImageAdapter mFeedBackImageAdapter;

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
    private FeedBackPresenter mFeedBackPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initView() {
        mRootLayout = findViewById(R.id.rootLayout);
        mBackImageView = findViewById(R.id.backImageView);

        mEditText = findViewById(R.id.editText);
        mRecyclerView = findViewById(R.id.recyclerView);
        mContactEditText = findViewById(R.id.contactEditText);
        mSubmitButton = findViewById(R.id.submitButton);

        mWaitDialog = new WaitDialog(this);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager plantManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // 布局横向
        plantManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager potManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // 布局横向
        potManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置RecyclerView间距,每一行Item的数目1000(1000为假设的数据)
        int gap = DensityUtil.dpToPx(this, 5);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(1000, gap, false);

        mRecyclerView.setLayoutManager(plantManager);
        mRecyclerView.addItemDecoration(decoration);

        // 解决软键盘弹出时底部布局上移问题
        getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN | SOFT_INPUT_ADJUST_PAN);
        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mFeedBackImageList = new ArrayList<>();
        FeedBackImage feedBackImage = new FeedBackImage();
        feedBackImage.setType(1);
        mFeedBackImageList.add(feedBackImage);

        mFeedBackImageAdapter = new FeedBackImageAdapter(this, mFeedBackImageList);
        mRecyclerView.setAdapter(mFeedBackImageAdapter);
    }

    @Override
    protected void initEvent() {
        mFeedBackPresenter = new FeedBackPresenterImpl(this);
        mBackImageView.setOnClickListener(this);
        mFeedBackImageAdapter.setOnItemClickListener(this);
        mSubmitButton.setOnClickListener(this);
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

        // 提交
        if (id == R.id.submitButton) {
            String content = mEditText.getText().toString().trim();
            String contactInformation = mContactEditText.getText().toString().trim();
            mFeedBackPresenter.feedBack(content, contactInformation, mFeedBackImageList);
        }
    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param v        被点击的View
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(View v, int position) {
        int id = v.getId();
        int type = mFeedBackImageList.get(position).getType();

        // 删除图片
        if (id == R.id.deleteImageView && 2 == type) {
            mFeedBackImageList.remove(position);
            mFeedBackImageAdapter.notifyDataSetChanged();
        }

        // 添加图片
        if (id == R.id.addImageView && 1 == type) {
            mFeedBackPresenter.displayTakePhotoOrAlbumPopupWindow(mRootLayout);
        }
    }

    /**
     * 在选择拍照还是从相册中选择的PopupWindow里面的Item View被点击的时候回调
     *
     * @param v 被点击的view
     */
    @Override
    public void onPopupWindowItemClick(View v) {
        int id = v.getId();

        // 拍照上传
        if (id == R.id.takePhotosTextView) {
            // 申请权限
            mFeedBackPresenter.applyPermissions(permissions);
        }

        // 从相册中选择
        if (id == R.id.photoAlbumChooseTextView) {
            // 打开相册
            mFeedBackPresenter.openPhotoAlbum();
        }
    }

    /**
     * 申请权限的结果
     *
     * @param b true 已经获取全部权限,false 没有获取全部权限
     */
    @Override
    public void applyPermissionsResults(boolean b) {
        if (!b) {
            return;
        }
        // 打开相机
        mFeedBackPresenter.openCamera();
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
            mFeedBackPresenter.openCamera();
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
        mFeedBackPresenter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 返回从相机或相册返回的图像
     *
     * @param file 图像文件
     */
    @Override
    public void returnsTheImageReturnedFromTheCameraOrAlbum(File file) {
        FeedBackImage feedBackImage = new FeedBackImage();
        feedBackImage.setType(2);
        feedBackImage.setFile(file);
        mFeedBackImageList.add(0, feedBackImage);
        mFeedBackImageAdapter.notifyDataSetChanged();
    }

    /**
     * 在意见反馈成功时回调
     *
     * @param feedBack 意见反馈数据
     */
    @Override
    public void onFeedBackSuccess(FeedBack feedBack) {
        MessageUtil.showMessage(feedBack.getMsg());
        if (CODE_SUCCEED == feedBack.getCode()) {
            onBackPressed();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (null != mFeedBackPresenter) {
            mFeedBackPresenter.onDetachView();
            mFeedBackPresenter = null;
        }
    }
}
