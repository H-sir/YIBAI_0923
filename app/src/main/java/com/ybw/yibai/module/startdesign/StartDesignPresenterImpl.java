package com.ybw.yibai.module.startdesign;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.adapter.PresetSceneNameAdapter;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.module.startdesign.StartDesignContract.StartDesignPresenter;
import com.ybw.yibai.module.startdesign.StartDesignContract.StartDesignView;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_CAMERA_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_PHOTOS_CODE;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;
import static com.ybw.yibai.common.utils.ImageUtil.createPhotoPath;

/**
 * 开始设计Presenter实现类
 *
 * @author sjl
 * @date 2019/11/12
 */
public class StartDesignPresenterImpl extends BasePresenterImpl<StartDesignView> implements StartDesignPresenter {

    /**
     * 显示场景名称列表的PopupWindow
     */
    private PopupWindow mPopupWindow;

    private EditText mEditText;

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private StartDesignView mStartDesignView;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public StartDesignPresenterImpl(StartDesignView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mStartDesignView = getView();
    }

    /**
     * 申请权限
     *
     * @param permissions 要申请的权限列表
     */
    @Override
    public void applyPermissions(String[] permissions) {
        Activity activity = (Activity) mStartDesignView;
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(activity, permissions)) {
                // 开始提交请求权限
                PermissionsUtil.startRequestPermission(activity, permissions);
            } else {
                // 已经获取全部权限
                mStartDesignView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mStartDesignView.applyPermissionsResults(true);
        }
    }

    /**
     * 打开相机
     */
    @Override
    public void openCamera() {
        Activity activity = (Activity) mStartDesignView;
        ImageUtil.openCamera(activity, createPhotoPath());
    }

    /**
     * 打开相册
     */
    @Override
    public void openPhotoAlbum() {
        Activity activity = (Activity) mStartDesignView;
        ImageUtil.openPhotoAlbum(activity);
    }

    /**
     * 获得Activity返回的数据
     *
     * @param requestCode 请求代码
     * @param resultCode  结果代码
     * @param data        返回的数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Activity activity = (Activity) mStartDesignView;
        // 获得系统相册返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_PHOTOS_CODE) {
            Uri uri = data.getData();
            if (null == uri) {
                return;
            }
            // 根据Uri获取图片的绝对路径
            String path = ImageUtil.getRealPathFromUri(activity, uri);
            if (!judeFileExists(path)) {
                return;
            }
            File file = new File(path);
            mStartDesignView.returnsTheImageReturnedFromTheCameraOrAlbum(file);
        }

        // 获得系统相机返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_CAMERA_CODE) {
            // 获得相机拍照后图片保存的路径
            String path = ImageUtil.getCropPhotoPath();
            if (!judeFileExists(path)) {
                return;
            }
            File file = new File(path);
            mStartDesignView.returnsTheImageReturnedFromTheCameraOrAlbum(file);
        }
    }

    /**
     * 显示场景名称列表的PopupWindow
     *
     * @param rootLayout    界面Root布局
     * @param sceneNameList 预设场景名称列表
     * @param oldSceneName  用户点击"选择设计的背景图"位置之前显示的场景名称
     */
    @Override
    public void displaySceneNamePopupWindow(View rootLayout, List<String> sceneNameList, String oldSceneName) {
        Activity activity = (Activity) mStartDesignView;
        if (mPopupWindow == null) {
            View view = activity.getLayoutInflater().inflate(R.layout.popup_window_scene_name_list_layout, null);
            mPopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            ImageView closeImageView = view.findViewById(R.id.closeImageView);
            mEditText = view.findViewById(R.id.editText);
            Button button = view.findViewById(R.id.determineButton);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

            // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            // 设置RecyclerView间距
            int spacing = DensityUtil.dpToPx(activity, 8);
            GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(4, spacing, false);
            // 给RecyclerView设置布局管理器(必须设置)
            recyclerView.setLayoutManager(manager);
            recyclerView.addItemDecoration(decoration);
            if (null != sceneNameList && sceneNameList.size() > 0) {
                PresetSceneNameAdapter adapter = new PresetSceneNameAdapter(activity, sceneNameList);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(position -> {
                    if (null != mPopupWindow && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    String name = sceneNameList.get(position);
                    mStartDesignView.onPresetSceneNameItemClick(name);
                });
            }
            closeImageView.setOnClickListener(v -> {
                if (null != mPopupWindow && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            });

            button.setOnClickListener(v -> {
                String name = mEditText.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    return;
                }
                if (null != mPopupWindow && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                mStartDesignView.onPresetSceneNameItemClick(name);
            });

            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);

            // 设置一个动画效果
            mPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);
            mPopupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
        } else {
            mPopupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
        }
        if (!TextUtils.isEmpty(oldSceneName)) {
            mEditText.setText(oldSceneName);
            mEditText.setSelection(oldSceneName.length());
        }else {
            mEditText.setText("");
        }
    }
}
