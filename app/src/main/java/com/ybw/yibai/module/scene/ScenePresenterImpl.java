package com.ybw.yibai.module.scene;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.CreateSceneData;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.bean.DesignScheme;
import com.ybw.yibai.common.bean.DesignSchemeRequest;
import com.ybw.yibai.common.bean.PlacementQrQuotationList;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.bean.SystemParameter.DataBean;
import com.ybw.yibai.common.bean.SystemParameter.DataBean.SpectypeBean;
import com.ybw.yibai.common.model.CreateSceneOrPicModel;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.widget.CustomDialog;
import com.ybw.yibai.module.scene.SceneContract.CallBack;
import com.ybw.yibai.module.scene.SceneContract.SceneModel;
import com.ybw.yibai.module.scene.SceneContract.ScenePresenter;
import com.ybw.yibai.module.scene.SceneContract.SceneView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_CAMERA_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_PHOTOS_CODE;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;
import static com.ybw.yibai.common.utils.ImageUtil.createPhotoPath;

/**
 * 场景界面Presenter实现类
 *
 * @author sjl
 * @date 2019/9/14
 */
public class ScenePresenterImpl extends BasePresenterImpl<SceneView> implements ScenePresenter, CallBack{

    private static final String TAG = "ScenePresenterImpl";

    /**
     * 修改场景名称的PopupWindow
     */
    private PopupWindow mEditSceneNamePopupWindow;

    /**
     * 植物大中小类别
     */
    private List<SpectypeBean> mSpecTypeList;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private SceneView mSceneView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private SceneModel mSceneModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public ScenePresenterImpl(SceneView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mSceneView = getView();
        mSceneModel = new SceneModelImpl();

        SystemParameter systemParameter = YiBaiApplication.getSystemParameter();
        DataBean data = systemParameter.getData();
        if (null == data) {
            return;
        }
        List<SpectypeBean> specTypeList = data.getSpectype();
        if (null == specTypeList || specTypeList.size() == 0) {
            return;
        }
        mSpecTypeList = new ArrayList<>();
        mSpecTypeList.addAll(specTypeList);
    }

    /**
     * 动态设置"NavigationView"的宽度
     *
     * @param navigationView 要设置宽度的NavigationView
     */
    @Override
    public void setNavigationViewParams(NavigationView navigationView) {
        // 1:获取屏幕的宽
        Activity activity = (Activity) mSceneView;
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        // 2:将屏幕宽的80%设置为"NavigationView"的宽度
        ViewGroup.LayoutParams params = navigationView.getLayoutParams();
        params.width = (int) (width * 0.9);
        navigationView.setLayoutParams(params);
    }

    /**
     * 新创建场景信息
     *
     * @param createSceneData 创建场景时需要的数据
     */
    @Override
    public void addSceneInfo(List<CreateSceneData> createSceneData) {
        if (null == createSceneData || createSceneData.size() == 0) {
            mSceneModel.findUserSceneListInfo(this);
            return;
        }
        mSceneModel.addSceneInfo(createSceneData, this);
    }

    /**
     * 新创建场景信息的结果
     *
     * @param result true新创建成功,false新创建失败
     */
    @Override
    public void addSceneInfoResult(boolean result) {
        mSceneView.addSceneInfoResult(result);
    }

    /**
     * 查找用户的场景信息
     */
    @Override
    public void findUserSceneListInfo() {
        mSceneModel.findUserSceneListInfo(this);
    }

    @Override
    public void findUserSceneListInfo(boolean createFlag) {
        mSceneModel.findUserSceneListInfo(createFlag, this);
    }

    /**
     * 在查找用户的场景信息成功时回调
     *
     * @param sceneInfoList 用户场景信息列表
     */
    @Override
    public void onFindUserSceneInfoSuccess(List<SceneInfo> sceneInfoList) {
        mSceneView.onFindUserSceneInfoSuccess(sceneInfoList);
    }

    /**
     * 获取摆放清单列表
     */
    @Override
    public void getPlacementList() {
        mSceneModel.getPlacementList(this);
    }

    /**
     * 在获取摆放清单列表成功时回调
     *
     * @param placementQrQuotationList 摆放清单列表
     */
    @Override
    public void onGetPlacementListSuccess(PlacementQrQuotationList placementQrQuotationList) {
        mSceneView.onGetPlacementListSuccess(placementQrQuotationList);
    }

    /**
     * 删除待摆放清单列表
     *
     * @param quoteIds 清单产品id,多个用英文逗号分隔
     */
    @Override
    public void deletePlacementList(String quoteIds) {
        mSceneModel.deletePlacementList(quoteIds, this);
    }

    /**
     * 在删除待摆放清单产品成功时回调
     *
     * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
     */
    @Override
    public void onDeletePlacementListSuccess(DeletePlacement deletePlacement) {
        mSceneView.onDeletePlacementListSuccess(deletePlacement);
    }

    /**
     * 获取用户保存的"报价"数据
     */
    @Override
    public void getQuotationData() {
        mSceneModel.getQuotationData(this);
    }

    /**
     * 在获取用户保存的"报价"数据成功时回调
     *
     * @param quotationDataList 用户保存的"报价"数据列表
     */
    @Override
    public void onGetQuotationDataSuccess(List<QuotationData> quotationDataList) {
        mSceneView.onGetQuotationDataSuccess(quotationDataList);
    }

    /**
     * 更新用户场景信息列表的集合
     *
     * @param sceneInfoList 用户场景信息列表的集合
     */
    @Override
    public void updateSceneListInfo(List<SceneInfo> sceneInfoList) {
        mSceneModel.updateSceneListInfo(sceneInfoList, this);
    }

    /**
     * 更新用户场景信息列表的集合的结果
     *
     * @param result true添加成功,false添加失败
     */
    @Override
    public void updateSceneListInfoResult(boolean result) {
        mSceneView.updateSceneListInfoResult(result);
    }

    /**
     * 将某一个场景的"模拟搭配产品"数据添加到报价列表数据中
     *
     * @param sceneId 场景id(一般为创建该场景的时间戳)
     */
    @Override
    public void addQuotation(long sceneId) {
        mSceneModel.addQuotation(sceneId, this);
    }

    /**
     * 将"模拟搭配产品"数据添加到报价列表数据中的结果
     *
     * @param result true添加成功,false添加失败
     */
    @Override
    public void addQuotationResult(boolean result) {
        mSceneView.addQuotationResult(result);
    }

    @Override
    public void onCreateSceneOrPic(CreateSceneOrPicModel model) {
        mSceneView.onCreateSceneOrPicCallback(model);
    }

    /**
     * 申请权限
     *
     * @param permissions 要申请的权限列表
     */
    @Override
    public void applyPermissions(String[] permissions) {
        Activity activity = (Activity) mSceneView;
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(activity, permissions)) {
                // 开始提交请求权限
                PermissionsUtil.startRequestPermission(activity, permissions);
            } else {
                // 已经获取全部权限
                mSceneView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mSceneView.applyPermissionsResults(true);
        }
    }

    /**
     * 打开相机
     */
    @Override
    public void openCamera() {
        Activity activity = (Activity) mSceneView;
        ImageUtil.openCamera(activity, createPhotoPath());
    }

    /**
     * 打开相册
     */
    @Override
    public void openPhotoAlbum() {
        Activity activity = (Activity) mSceneView;
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
        Activity activity = (Activity) mSceneView;
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
            mSceneView.returnsTheImageReturnedFromTheCameraOrAlbum(file);
        }

        // 获得系统相机返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_CAMERA_CODE) {
            // 获得相机拍照后图片保存的路径
            String path = ImageUtil.getCropPhotoPath();
            if (!judeFileExists(path)) {
                return;
            }
            File file = new File(path);
            mSceneView.returnsTheImageReturnedFromTheCameraOrAlbum(file);
        }
    }

    /**
     * 显示修改场景名称的PopupWindow
     *
     * @param rootLayout    界面Root布局
     * @param sceneInfoList 用户场景信息列表的集合
     * @param position      位置
     */
    @Override
    public void displayEditSceneNamePopupWindow(View rootLayout, List<SceneInfo> sceneInfoList, int position) {
        Activity activity = (Activity) mSceneView;
        View view = activity.getLayoutInflater().inflate(R.layout.popup_window_edit_scene_name_layout, null);
        mEditSceneNamePopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        SceneInfo sceneInfo = sceneInfoList.get(position);
        String sceneName = sceneInfo.getSceneName();

        EditText editText = view.findViewById(R.id.editText);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button determineButton = view.findViewById(R.id.determineButton);

        if (!TextUtils.isEmpty(sceneName)) {
            editText.setText(sceneName);
            editText.setSelection(editText.getText().toString().length());
        }
        cancelButton.setOnClickListener(v -> mEditSceneNamePopupWindow.dismiss());
        determineButton.setOnClickListener(v -> {
            String newSceneName = editText.getText().toString().trim();
            if (TextUtils.isEmpty(newSceneName)) {
                return;
            }
            sceneInfo.setSceneName(newSceneName);
            mSceneModel.updateSceneListInfo(sceneInfoList, this);
            mEditSceneNamePopupWindow.dismiss();
        });

        mEditSceneNamePopupWindow.setFocusable(true);
        mEditSceneNamePopupWindow.setOutsideTouchable(true);
        mEditSceneNamePopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        // 在弹出PopupWindow设置屏幕透明度
        OtherUtil.setBackgroundAlpha(activity, 0.9f);
        // 添加PopupWindow窗口关闭事件
        mEditSceneNamePopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        mEditSceneNamePopupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
    }

    /**
     * 显示删除场景信息的Dialog
     *
     * @param sceneInfoList 用户场景信息列表的集合
     */
    @Override
    public void displayDeleteSceneNameDialog(List<SceneInfo> sceneInfoList) {
        Activity activity = (Activity) mSceneView;
        // 判断是否存在用户选中的场景
        boolean select = false;
        for (SceneInfo sceneInfo : sceneInfoList) {
            if (sceneInfo.isSelectCheckBox()) {
                select = true;
                break;
            }
        }
        if (!select) {
            MessageUtil.showMessage(activity.getResources().getString(R.string.no_scenes_are_selected));
            return;
        }
        if (null != activity) {
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            CustomDialog customDialog = new CustomDialog(activity);
            customDialog.setMessage(activity.getResources().getString(R.string.are_you_sure_to_delete_the_selected_scene));
            customDialog.setYesOnclickListener(activity.getResources().getString(R.string.determine),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                        mSceneModel.deleteSceneListInfo(sceneInfoList, this);
                    });
            customDialog.setNoOnclickListener(activity.getResources().getString(R.string.cancel),
                    () -> {
                        OtherUtil.setBackgroundAlpha(activity, 1f);
                        customDialog.dismiss();
                    });
            customDialog.show();
        }
    }

    @Override
    public void createSceneOrPic(String desing_number, String type, String scheme_id, String scheme_name, Map<String, RequestBody> pic, String product_sku_id) {
        mSceneModel.createSceneOrPic(desing_number,type,scheme_id,scheme_name,pic,product_sku_id,this);
    }

    @Override
    public void addDesignAndSceneInfo() {
        mSceneModel.addDesignAndSceneInfo(this);
    }

    @Override
    public void addDesignAndSceneInfo(List<CreateSceneData> mCreateSceneDataList) {
        mSceneModel.addDesignAndSceneInfo(mCreateSceneDataList,this);
    }

    /**
     * 创建场景或场景添加图片产品
     */
    @Override
    public void designScheme(DesignSchemeRequest designSchemeRequest) {
        String desingNumber = designSchemeRequest.getDesingNumber();
        int type = designSchemeRequest.getType();
        String schemeId = designSchemeRequest.getSchemeId();
        String schemeName = designSchemeRequest.getSchemeName();
        String pic = designSchemeRequest.getPic();
        String productSkuId = designSchemeRequest.getProductSkuId();
//        String picturePath = ImageUtil.saveImage(bitmap, String.valueOf(TimeUtil.getTimeStamp()));
        if (pic != null) {
            File file = new File(pic);
            Map<String, RequestBody> params = new HashMap<>();
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
            if (type == 1) {
                MultipartBody.Part parts = MultipartBody.Part.createFormData("bgpic", file.getName(), requestBody);
                mSceneModel.createDesignScheme(desingNumber, type, schemeId, schemeName, parts, productSkuId, this);
            } else {
                params.put("pic\"; filename=\"" + file.getName(), requestBody);
                mSceneModel.designScheme(desingNumber, type, schemeId, schemeName, params, productSkuId, this);
            }
        } else {
            mSceneModel.designScheme(desingNumber, type, schemeId, schemeName, null, null, this);
        }
    }

    @Override
    public void onGetDesignSchemeSuccess(DesignScheme designScheme) {
        mSceneView.onGetDesignSchemeSuccess(designScheme);
    }

    @Override
    public void onGetAddDesignSchemeSuccess(DesignScheme designScheme) {
        mSceneView.onGetAddDesignSchemeSuccess(designScheme);
    }
}
