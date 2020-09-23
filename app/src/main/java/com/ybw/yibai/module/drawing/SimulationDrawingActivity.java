package com.ybw.yibai.module.drawing;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.SimulationDrawingAdapter;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SelectImage;
import com.ybw.yibai.common.bean.SimulationDrawingData;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.EncryptionUtil;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.module.pictureview.PictureViewActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;
import static com.ybw.yibai.common.constants.Folder.SIMULATION_IMAGE_PREFIX;
import static com.ybw.yibai.common.constants.Preferences.PHOTO_URL_LIST;
import static com.ybw.yibai.common.constants.Preferences.POSITION;
import static com.ybw.yibai.common.constants.Preferences.TYPE;
import static com.ybw.yibai.common.utils.SDCardHelperUtil.getSDCardPrivateFilesDir;

/**
 * 已模拟图界面
 *
 * @author sjl
 * @date 2019/9/24
 */
public class SimulationDrawingActivity extends BaseActivity implements View.OnClickListener,
        SimulationDrawingAdapter.OnItemClickListener,
        SimulationDrawingAdapter.OnItemLongClickListener,
        FileUtil.DeleteFileCallback {

    private String TAG = "SimulationDrawingActivity";

    /**
     * 手机中的模拟图片名称前面的前缀
     */
    private String prefix;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 标题栏
     */
    private RelativeLayout mTitleRelativeLayout;

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 编辑栏
     */
    private RelativeLayout mEditRelativeLayout;

    /**
     * 取消
     */
    private TextView mCancelTextView;

    /**
     * 用户选择的照片数量
     */
    private TextView mSelectNumberTextView;

    /**
     * 全选或者全不选功能TextView
     */
    private TextView mCheckAllOrNoneTextView;

    /**
     * 显示已模拟图片列表
     */
    private RecyclerView mRecyclerView;

    /**
     * 显示底部功能
     */
    private LinearLayout mBottomLinearLayout;

    /**
     * 分享
     */
    private TextView mShareTextView;

    /**
     * 删除
     */
    private TextView mDeleteTextView;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 用户全部的图片
     */
    private List<File> mAllFileList;

    /**
     * 用户选择的图片
     */
    private List<File> mSelectFileList;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * "已模拟图"数据
     */
    private List<SimulationDrawingData> mSimulationDrawingDataList;

    /**
     * 显示已模拟图列表适配器
     */
    private SimulationDrawingAdapter mSimulationDrawingAdapter;

    @Override
    protected int setLayout() {
        return R.layout.activity_simulation_drawing;
    }

    @Override
    protected void initView() {
        mTitleRelativeLayout = findViewById(R.id.barView);
        mBackImageView = findViewById(R.id.backImageView);

        mEditRelativeLayout = findViewById(R.id.editRelativeLayout);
        mCancelTextView = findViewById(R.id.cancelTextView);
        mSelectNumberTextView = findViewById(R.id.selectNumberTextView);
        mCheckAllOrNoneTextView = findViewById(R.id.checkAllOrNoneTextView);

        mRecyclerView = findViewById(R.id.recyclerView);
        mBottomLinearLayout = findViewById(R.id.bottomLinearLayout);
        mShareTextView = findViewById(R.id.shareTextView);
        mDeleteTextView = findViewById(R.id.deleteTextView);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);
        // 设置RecyclerView间距
        int px = DensityUtil.dpToPx(this, 8);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(3, px, true);
        mRecyclerView.addItemDecoration(decoration);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mAllFileList = new ArrayList<>();
        mSelectFileList = new ArrayList<>();
        mSimulationDrawingDataList = new ArrayList<>();

        mSimulationDrawingAdapter = new SimulationDrawingAdapter(this, mSimulationDrawingDataList);
        mRecyclerView.setAdapter(mSimulationDrawingAdapter);
        mSimulationDrawingAdapter.setOnItemClickListener(this);
        mSimulationDrawingAdapter.setOnItemLongClickListener(this);

        prefix = SIMULATION_IMAGE_PREFIX + EncryptionUtil.sha1(String.valueOf(YiBaiApplication.getUid()));

        getImagesSavedOnYourPhone();
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mBackImageView.setOnClickListener(this);

        mCancelTextView.setOnClickListener(this);
        mCheckAllOrNoneTextView.setOnClickListener(this);

        mShareTextView.setOnClickListener(this);
        mDeleteTextView.setOnClickListener(this);
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

        // 取消
        if (id == R.id.cancelTextView) {
            mSelectFileList.clear();
            if (View.GONE == mTitleRelativeLayout.getVisibility()) {
                mTitleRelativeLayout.setVisibility(View.VISIBLE);
            }
            if (View.VISIBLE == mEditRelativeLayout.getVisibility()) {
                mEditRelativeLayout.setVisibility(View.GONE);
            }
            if (View.VISIBLE == mBottomLinearLayout.getVisibility()) {
                mBottomLinearLayout.setVisibility(View.GONE);
            }
            for (SimulationDrawingData simulationDrawingData : mSimulationDrawingDataList) {
                simulationDrawingData.setShowCheckBox(false);
                simulationDrawingData.setSelectCheckBox(false);
            }
            mSimulationDrawingAdapter.notifyDataSetChanged();
        }

        // 全选或者全不选
        if (id == R.id.checkAllOrNoneTextView) {
            mSelectFileList.clear();
            String string = mCheckAllOrNoneTextView.getText().toString();
            if (string.equals(getResources().getString(R.string.check_all))) {
                for (SimulationDrawingData simulationDrawingData : mSimulationDrawingDataList) {
                    simulationDrawingData.setShowCheckBox(true);
                    simulationDrawingData.setSelectCheckBox(true);
                    mSelectFileList.add(simulationDrawingData.getFile());
                }
                mCheckAllOrNoneTextView.setText(getResources().getString(R.string.none));
                int size = mSimulationDrawingDataList.size();
                String text = getResources().getString(R.string.already_selected) + size + getResources().getString(R.string.photo);
                mSelectNumberTextView.setText(text);
                mBottomLinearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.auxiliary_classification_column_color));
            } else if (string.equals(getResources().getString(R.string.none))) {
                for (SimulationDrawingData simulationDrawingData : mSimulationDrawingDataList) {
                    simulationDrawingData.setShowCheckBox(false);
                    simulationDrawingData.setSelectCheckBox(false);
                }
                mCheckAllOrNoneTextView.setText(getResources().getString(R.string.check_all));
                mSelectNumberTextView.setText(getResources().getString(R.string.select_photo));
                mBottomLinearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.line_color));
            }
            mSimulationDrawingAdapter.notifyDataSetChanged();
        }

        // 分享图片
        if (id == R.id.shareTextView) {
            int size = mSelectFileList.size();
            if (0 == size) {
                return;
            }
            ImageUtil.shareImage(this, mSelectFileList);
        }

        // 删除图片
        if (id == R.id.deleteTextView) {
            int size = mSelectFileList.size();
            if (0 == size) {
                return;
            }
            FileUtil.deleteFile(mSelectFileList, this);
        }
    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemClick(int position) {
        if (View.VISIBLE == mEditRelativeLayout.getVisibility()) {
            OtherUtil.vibrate(this, 20);
            // 编辑图片操作
            editOperation(position);
        } else {
            // 查看图片
            Intent intent = new Intent(this, PictureViewActivity.class);
            intent.putExtra(POSITION, position);
            if (null != mAllFileList && mAllFileList.size() > 0) {
                intent.putExtras(getIntent());
                intent.putExtra(TYPE, 1);
                intent.putExtra(PHOTO_URL_LIST, new Gson().toJson(mAllFileList));
            }
            startActivity(intent);
            // 淡入淡出动画效果
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    /**
     * 在RecyclerView Item 长按点击时回调
     *
     * @param position 被点击的Item位置
     */
    @Override
    public void onItemLongClick(int position) {
        if (View.VISIBLE == mTitleRelativeLayout.getVisibility()) {
            mTitleRelativeLayout.setVisibility(View.GONE);
        }
        if (View.GONE == mEditRelativeLayout.getVisibility()) {
            mEditRelativeLayout.setVisibility(View.VISIBLE);
        }
        if (View.GONE == mBottomLinearLayout.getVisibility()) {
            mBottomLinearLayout.setVisibility(View.VISIBLE);
        }
        OtherUtil.vibrate(this, 20);
        editOperation(position);
    }

    /**
     * 在删除文件时回调
     *
     * @param currentNumber 还剩余的文件数量
     */
    @Override
    public void onDeleteFile(int currentNumber) {
        if (0 == currentNumber) {
            getImagesSavedOnYourPhone();
        }
        int size = mSimulationDrawingDataList.size();
        if (0 != size) {
            return;
        }
        if (View.GONE == mTitleRelativeLayout.getVisibility()) {
            mTitleRelativeLayout.setVisibility(View.VISIBLE);
        }
        if (View.VISIBLE == mEditRelativeLayout.getVisibility()) {
            mEditRelativeLayout.setVisibility(View.GONE);
        }
        if (View.VISIBLE == mBottomLinearLayout.getVisibility()) {
            mBottomLinearLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 获取保存在手机中的模拟图片
     */
    private void getImagesSavedOnYourPhone() {
        mAllFileList.clear();
        mSimulationDrawingDataList.clear();
        File file = new File(getSDCardPrivateFilesDir(this, DIRECTORY_PICTURES));
        List<File> fileList = FileUtil.indistinctSearchFile(file, prefix);
        if (null != fileList && fileList.size() > 0) {
            for (File f : fileList) {
                SimulationDrawingData simulationDrawingData = new SimulationDrawingData();
                simulationDrawingData.setFile(f);
                mAllFileList.add(f);
                mSimulationDrawingDataList.add(simulationDrawingData);
            }
        }
        if (mSimulationDrawingDataList.size() == 0) {

        }
        mSimulationDrawingAdapter.notifyDataSetChanged();
    }

    /**
     * 编辑图片操作
     *
     * @param position 被点击的Item位置
     */
    private void editOperation(int position) {
        SimulationDrawingData simulationDrawingData = mSimulationDrawingDataList.get(position);
        if (simulationDrawingData.isShowCheckBox()) {
            simulationDrawingData.setShowCheckBox(false);
            simulationDrawingData.setSelectCheckBox(false);
            mSelectFileList.remove(simulationDrawingData.getFile());
        } else {
            simulationDrawingData.setShowCheckBox(true);
            simulationDrawingData.setSelectCheckBox(true);
            mSelectFileList.add(simulationDrawingData.getFile());
        }
        mSimulationDrawingAdapter.notifyDataSetChanged();

        // 选中照片的数量
        int selectCheckBoxNumber = 0;
        for (SimulationDrawingData s : mSimulationDrawingDataList) {
            if (s.isSelectCheckBox()) {
                selectCheckBoxNumber++;
            }
        }
        if (selectCheckBoxNumber == mSimulationDrawingDataList.size()) {
            mCheckAllOrNoneTextView.setText(getResources().getString(R.string.none));
        } else {
            mCheckAllOrNoneTextView.setText(getResources().getString(R.string.check_all));
        }
        if (0 == selectCheckBoxNumber) {
            mSelectNumberTextView.setText(getResources().getString(R.string.select_photo));
            mBottomLinearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.line_color));
        } else {
            String text = getResources().getString(R.string.already_selected) + selectCheckBoxNumber + getResources().getString(R.string.photo);
            mSelectNumberTextView.setText(text);
            mBottomLinearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.auxiliary_classification_column_color));
        }
    }

    /**
     * EventBus
     * 接收用户从{@link PictureViewActivity} 传递过来的用户选择的图片数据
     *
     * @param selectImage 选择以模拟图片数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pictureViewActivityReturnImageData(SelectImage selectImage) {
        // 接受到数据就应该关闭本界面
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            // 解除注册
            EventBus.getDefault().unregister(this);
        }
    }
}