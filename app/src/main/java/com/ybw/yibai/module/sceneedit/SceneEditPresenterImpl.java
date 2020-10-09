package com.ybw.yibai.module.sceneedit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.model.ByteArrayLoader;
import com.google.gson.Gson;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.AlreadyPlaced;
import com.ybw.yibai.common.bean.BTCBean;
import com.ybw.yibai.common.bean.CategorySimilarSKU;
import com.ybw.yibai.common.bean.FastImport;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.ProductData;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.bean.Recommend;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.bean.SpecSuk;
import com.ybw.yibai.common.bean.UserPosition;
import com.ybw.yibai.common.model.CreateDesignModel;
import com.ybw.yibai.common.model.CreateSceneOrPicModel;
import com.ybw.yibai.common.model.ItemDesignSceneModel;
import com.ybw.yibai.common.network.response.ResponsePage;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.widget.HorizontalViewPager;
import com.ybw.yibai.common.widget.MatchLayout;
import com.ybw.yibai.common.widget.stickerview.BaseSticker;
import com.ybw.yibai.common.widget.stickerview.DrawableSticker;
import com.ybw.yibai.common.widget.stickerview.StickerView;
import com.ybw.yibai.module.sceneedit.SceneEditContract.CallBack;
import com.ybw.yibai.module.sceneedit.SceneEditContract.SceneEditModel;
import com.ybw.yibai.module.sceneedit.SceneEditContract.SceneEditPresenter;
import com.ybw.yibai.module.sceneedit.SceneEditContract.SceneEditView;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static android.widget.LinearLayout.LayoutParams.MATCH_PARENT;
import static android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_CAMERA_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_PHOTOS_CODE;
import static com.ybw.yibai.common.constants.Folder.PICTURES_PATH;
import static com.ybw.yibai.common.constants.Preferences.PLANT;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;
import static com.ybw.yibai.common.utils.ImageUtil.createPhotoPath;
import static com.ybw.yibai.common.utils.ImageUtil.getLocalBitmap;

/**
 * 场景编辑Presenter实现类
 *
 * @author sjl
 * @date 2019/9/23
 */
public class SceneEditPresenterImpl extends BasePresenterImpl<SceneEditView>
        implements SceneEditPresenter, CallBack, OnRangeChangedListener, View.OnClickListener {

    private static final String TAG = "SceneEditPresenterImpl";

    /**
     * 存放动态添加的"ViewPager"
     */
    private List<HorizontalViewPager> mViewPagerList;

    /**
     * 存放动态添加的"植物与盆器互相搭配的View"
     */
    private List<MatchLayout> mMatchLayoutList;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 显示显示产品代码信息的PopupWindow
     */
    private PopupWindow mProductCodePopupWindow;

    /**
     * 显示场景背景编辑的PopupWindow
     */
    private PopupWindow mSceneBackgroundEditPopupWindow;

    /**
     * 显示植物信息的PopupWindow
     */
    private PopupWindow mPlantInfoPopupWindow;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private SceneEditView mSceneEditView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private SceneEditModel mSceneEditModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public SceneEditPresenterImpl(SceneEditView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mSceneEditView = getView();
        mSceneEditModel = new SceneEditModelImpl();

        mViewPagerList = new ArrayList<>();
        mMatchLayoutList = new ArrayList<>();
    }

    /**
     * 根据场景id查找保存用户的"模拟搭配图片"数据
     *
     * @param sceneId 场景id
     */
    @Override
    public void getSimulationData(long sceneId) {
        mSceneEditModel.getSimulationData(sceneId, this);
    }

    /**
     * 根据场景id查找用户保存的"模拟搭配图片"数据成功时回调
     *
     * @param simulationDataList 用户保存的"模拟搭配图片"数据
     */
    @Override
    public void onGetSimulationDataSuccess(List<SimulationData> simulationDataList) {
        mSceneEditView.onGetSimulationDataSuccess(simulationDataList);
    }

    /**
     * 申请权限
     *
     * @param permissions 要申请的权限列表
     */
    @Override
    public void applyPermissions(String[] permissions) {
        Fragment fragment = (Fragment) mSceneEditView;
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(fragment, permissions)) {
                // 开始提交请求权限
                PermissionsUtil.startRequestPermission(fragment, permissions);
            } else {
                // 已经获取全部权限
                mSceneEditView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mSceneEditView.applyPermissionsResults(true);
        }
    }

    /**
     * 打开相机
     */
    @Override
    public void openCamera() {
        Fragment fragment = (Fragment) mSceneEditView;
        ImageUtil.openCamera(fragment, createPhotoPath());
    }

    /**
     * 打开相册
     */
    @Override
    public void openPhotoAlbum() {
        Fragment fragment = (Fragment) mSceneEditView;
        ImageUtil.openPhotoAlbum(fragment);
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
        Fragment fragment = (Fragment) mSceneEditView;
        // 获得系统相册返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_PHOTOS_CODE) {
            Uri uri = data.getData();
            if (null == uri) {
                return;
            }
            // 根据Uri获取图片的绝对路径
            String path = ImageUtil.getRealPathFromUri(fragment.getContext(), uri);
            if (!judeFileExists(path)) {
                return;
            }
            File file = new File(path);
            mSceneEditView.returnsTheImageReturnedFromTheCameraOrAlbum(file);
        }

        // 获得系统相机返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_CAMERA_CODE) {
            // 获得相机拍照后图片保存的路径
            String path = ImageUtil.getCropPhotoPath();
            if (!judeFileExists(path)) {
                return;
            }
            File file = new File(path);
            mSceneEditView.returnsTheImageReturnedFromTheCameraOrAlbum(file);
        }
    }

    /**
     * 设置场景背景
     *
     * @param imageView 要设置场景背景的ImageView
     * @param path      要设置场景背景路径
     */
    @Override
    public void setBackgroundImage(ImageView imageView, String path) {
        try {
            Fragment fragment = (Fragment) mSceneEditView;
            Context context = fragment.getContext();
            if (null == context) {
                return;
            }
            if (judeFileExists(path)) {
                File file = new File(path);
                ImageUtil.displayImage(context, imageView, file);
            } else {
                // 没有或者用户删除了,就设置默认的场景背景
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setBackground(context.getDrawable(R.mipmap.default_scene_background));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新当前场景的信息
     *
     * @param sceneInfo 当前场景的信息
     */
    @Override
    public void updateSceneInfo(SceneInfo sceneInfo) {
        if (null == sceneInfo) {
            return;
        }
        mSceneEditModel.updateSceneInfo(sceneInfo, this);
    }

    /**
     * 显示场景背景编辑的PopupWindow
     *
     * @param rootLayout View根布局
     */
    @Override
    public void displaySceneBackgroundEditPopupWindow(View rootLayout) {
        Fragment fragment = (Fragment) mSceneEditView;
        if (null == mSceneBackgroundEditPopupWindow) {
            View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_adjust_background_layout, null);
            mSceneBackgroundEditPopupWindow = new PopupWindow(view, MATCH_PARENT, WRAP_CONTENT);

            ImageView cancelImageView = view.findViewById(R.id.cancelImageView);
            ImageView confirmImageView = view.findViewById(R.id.confirmImageView);
            cancelImageView.setOnClickListener(this);
            confirmImageView.setOnClickListener(this);

            RangeSeekBar brightnessRangeSeekBar = view.findViewById(R.id.brightnessRangeSeekBar);
            RangeSeekBar contrastRangeSeekBar = view.findViewById(R.id.contrastRangeSeekBar);
            brightnessRangeSeekBar.setOnRangeChangedListener(this);
            contrastRangeSeekBar.setOnRangeChangedListener(this);
            brightnessRangeSeekBar.setProgress(50f);
            contrastRangeSeekBar.setProgress(25f);
            brightnessRangeSeekBar.setIndicatorTextDecimalFormat("0");
            contrastRangeSeekBar.setIndicatorTextDecimalFormat("0");
            brightnessRangeSeekBar.setTypeface(Typeface.DEFAULT_BOLD);
            contrastRangeSeekBar.setTypeface(Typeface.DEFAULT_BOLD);

            mSceneBackgroundEditPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mSceneBackgroundEditPopupWindow.setOutsideTouchable(true);
            mSceneBackgroundEditPopupWindow.setFocusable(true);

            // 设置一个动画效果
            mSceneBackgroundEditPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);
            mSceneBackgroundEditPopupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
        } else {
            // 设置一个动画效果
            mSceneBackgroundEditPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);
            mSceneBackgroundEditPopupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 将场景中产品一键创建并导入服务器中
     *
     * @param placeName         位置名称
     * @param file              位置场景图
     * @param alreadyPlacedList 已摆放"模拟搭配图片"数据
     */
    @Override
    public void fastImport(String placeName, File file, List<SimulationData> alreadyPlacedList) {
        List<AlreadyPlaced> alreadyPlacedLists = new ArrayList<>();
        MultipartBody.Part[] parts = new MultipartBody.Part[alreadyPlacedList.size()];

        for (int i = 0; i < alreadyPlacedList.size(); i++) {
            SimulationData simulationData = alreadyPlacedList.get(i);
            int productSkuId = simulationData.getProductSkuId();
            int augmentedProductSkuId = simulationData.getAugmentedProductSkuId();
            String picturePath = simulationData.getPicturePath();
            int havePic = judeFileExists(picturePath) ? 1 : 0;
            int number = simulationData.getNumber() + 1;

            AlreadyPlaced alreadyPlaced = new AlreadyPlaced(productSkuId, augmentedProductSkuId, number, havePic);
            alreadyPlacedLists.add(alreadyPlaced);

            File f = new File(picturePath);
            if (!f.exists()) {
                continue;
            }
            String fileName = f.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), f);
            parts[i] = MultipartBody.Part.createFormData("com_pic[]", f.getName(), requestBody);
        }

        Map<String, RequestBody> params = new HashMap<>();
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
        params.put("design_pic\"; filename=\"" + file.getName(), requestBody);

        mSceneEditModel.fastImport(placeName, new Gson().toJson(alreadyPlacedLists), params, parts, this);
    }

    /**
     * 将场景中产品一键创建并导入服务器中成功时回调
     *
     * @param fastImport 将场景中产品一键创建并导入服务器中成功时服务器端返回的数据
     */
    @Override
    public void onFastImportSuccess(FastImport fastImport) {
        mSceneEditView.onFastImportSuccess(fastImport);
    }

    /**
     * 根据大中小随机获取组合
     *
     * @param type 规格大中小id
     */
    @Override
    public void getSpecSuk(int type) {
        mSceneEditModel.getSpecSuk(type, this);
    }

    /**
     * 根据大中小随机获取组合成功时回调
     *
     * @param specSuk 随机获取组合
     */
    @Override
    public void onGetSpecSukSuccess(SpecSuk specSuk) {
        mSceneEditView.onGetSpecSukSuccess(specSuk);
    }

    @Override
    public void onGetRecommendByAddSpecSuccess(Recommend recommend) {
        mSceneEditView.onGetRecommendSuccess(recommend);
        if (CODE_SUCCEED != recommend.getCode()) {
            MessageUtil.showMessage(recommend.getMsg());
            return;
        }
        Recommend.DataBean data = recommend.getData();
        if (null == data) {
            return;
        }
        addSimulationData(data);
    }

    @Override
    public void updateSceneBgSuccess() {
        mSceneEditView.updateSceneBgSuccess();
    }

    /**
     * 获取相似类型sku列表
     *
     * @param skuId 产品SKUid
     */
    @Override
    public void getCategorySimilarSKUList(int skuId) {
        mSceneEditModel.getCategorySimilarSKUList(skuId, this);
    }

    /**
     * 在获取相似类型sku列表成功时回调
     *
     * @param categorySimilarSKU 相似类型sku列表
     */
    @Override
    public void onGetCategorySimilarSKUListSuccess(CategorySimilarSKU categorySimilarSKU) {
        mSceneEditView.onGetCategorySimilarSKUListSuccess(categorySimilarSKU);
    }

    /**
     * 换搭配获取植物花盆列表
     *
     * @param cateCode   类别:plant,获取植物 pot获取盆
     * @param plantSkuId 植物SkuId
     * @param potSkuId   盆SkuId
     * @param specType   大中小id(此参数仅在获取花盆时有效)
     * @param pCid       分类id(多个用逗号拼接)
     * @param param      动态设置的参数
     */
    @Override
    public void getRecommend(String cateCode, Integer plantSkuId, Integer potSkuId, Integer specType,
                             Integer pCid, Map<String, String> param) {
        mSceneEditModel.getRecommend(cateCode, plantSkuId, potSkuId, specType, pCid, param, this);
    }

    @Override
    public void getNewRecommend(String cateCode, Integer plantSkuId, Integer potSkuId, Integer specType, Integer pCid, Map<String, String> param) {
        mSceneEditModel.getNewRecommend(cateCode, plantSkuId, potSkuId, specType, pCid, null, param, this);
    }

    /**
     * 点击新增按钮时获取搭配列表
     *
     * @param specTypeId 只传盆栽规格类型
     */
    @Override
    public void getNewRecommedByAddSpec(int specTypeId) {
        mSceneEditModel.getNewRecommedByAddSpec(specTypeId, this);
    }

    /**
     * 点击改款获取搭配列表
     */
    @Override
    public void getNewRecommedChangeStyle(int productSkuId, int augmentedProductSkuId) {
        mSceneEditModel.getNewRecommedChangeStyle(productSkuId, augmentedProductSkuId, this);
    }

    @Override
    public void getNewRecommed(String cateCode, int productSkuId, int augmentedProductSkuId, int specTypeId, List<String> specidList, List<String> attridList, Map<String, String> plantParamMap) {
        String specid = "";
        for (int i = 0; i < specidList.size(); i++) {
            if (i < specidList.size() - 1) {
                specid = specid + specidList.get(i) + "|";
            } else {
                specid = specid + specidList.get(i);
            }
        }

        String attrid = "";
        for (int i = 0; i < attridList.size(); i++) {
            if (i < attridList.size() - 1) {
                attrid = attrid + attridList.get(i) + "|";
            } else {
                attrid = attrid + attridList.get(i);
            }
        }
        mSceneEditModel.getNewRecommed(cateCode, productSkuId, augmentedProductSkuId, specTypeId, specid, attrid, plantParamMap, this);
    }

    /**
     * 更新背景图片
     */
    @Override
    public void updateSceneBg(String absolutePath, String sceneName, String scheme_id) {
        mSceneEditModel.updateSceneBg(absolutePath, sceneName, scheme_id, this);
    }

    /**
     * 换搭配获取植物花盆列表成功时回调
     *
     * @param recommend 植物花盆列表
     */
    @Override
    public void onGetRecommendSuccess(Recommend recommend) {
        mSceneEditView.onGetRecommendSuccess(recommend);
    }

    /**
     * 点击新增按钮时获取搭配列表成功的数据
     * 添加到"模拟搭配产品"数据
     *
     * @param data 用户点击植物规格获取的随机组合数据
     */
    public void addSimulationData(Recommend.DataBean data) {
        Fragment fragment = (Fragment) mSceneEditView;
        Context context = fragment.getContext();
        List<ListBean> listPlant, listPot;
        String productPic3 = null;
        String augmentedProductPic3 = null;
        if (data.getPlant() != null) {
            listPlant = data.getPlant().getList();
            for (Iterator<ListBean> iterator = listPlant.iterator(); iterator.hasNext(); ) {
                ListBean listBean = iterator.next();
                productPic3 = listBean.getPic3();
                break;
            }
        }
        if (data.getPot() != null) {
            listPot = data.getPot().getList();
            for (Iterator<ListBean> iterator = listPot.iterator(); iterator.hasNext(); ) {
                ListBean listBean = iterator.next();
                augmentedProductPic3 = listBean.getPic3();
                break;
            }
        }

        if (TextUtils.isEmpty(productPic3) || TextUtils.isEmpty(augmentedProductPic3)) {
            return;
        }
        ImageUtil.downloadPicture(context, new ImageUtil.DownloadCallback() {
            @Override
            public void onDownloadStarted() {

            }

            @Override
            public void onDownloadFinished(List<Bitmap> bitmapList) {
                handleOnDownloadFinished(data, bitmapList);
            }

            @Override
            public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

            }
        }, productPic3, augmentedProductPic3);
    }

    /**
     * 点击新增按钮时获取搭配列表成功的数据
     * 添加到"模拟搭配产品"数据下
     * 处理在下载全部图片完成之后的一些事情
     *
     * @param data       用户点击植物规格获取的随机组合数据
     * @param bitmapList 下载完成的图片
     */
    private void handleOnDownloadFinished(Recommend.DataBean data, List<Bitmap> bitmapList) {
        mSceneEditView.onShowLoading();
        double productHeight = 0;
        double augmentedProductHeight = 0;
        double augmentedProductOffsetRatio = 0;
        double productOffsetRatio = 0;
        List<ListBean> listPlant = data.getPlant().getList();
        List<ListBean> listPot = data.getPot().getList();
        for (Iterator<ListBean> iterator = listPot.iterator(); iterator.hasNext(); ) {
            ListBean listBean = iterator.next();
            augmentedProductHeight = listBean.getHeight();
            augmentedProductOffsetRatio = listBean.getOffset_ratio();
            break;
        }
        for (Iterator<ListBean> iterator = listPlant.iterator(); iterator.hasNext(); ) {
            ListBean listBean = iterator.next();
            productHeight = listBean.getHeight();
            productOffsetRatio = listBean.getOffset_ratio();
            break;
        }

        // 标记是否存在下载图片失败的情况
        boolean isFailed = false;
        Bitmap[] bitmaps = new Bitmap[bitmapList.size()];
        for (int i = 0; i < bitmapList.size(); i++) {
            Bitmap bitmap = bitmapList.get(i);
            if (null == bitmap) {
                isFailed = true;
                break;
            } else {
                bitmaps[i] = bitmap;
            }
        }
        if (isFailed) {
            //  下载图片失败
            return;
        }
        Bitmap bitmap = ImageUtil.pictureSynthesis(productHeight, augmentedProductHeight, augmentedProductOffsetRatio, productOffsetRatio, bitmaps);
        mSceneEditModel.addSimulationData(data, bitmap, this);
    }


    @Override
    public void onGetNewRecommendSuccess(Recommend model) {
        mSceneEditView.onGetNewRecommendSuccess(model);
    }

    /**
     * 显示植物信息的PopupWindow
     *
     * @param rootLayout     View根布局
     * @param simulationData "模拟搭配产品"数据
     */
    @Override
    public void displayPlantInfoPopupWindow(View rootLayout, SimulationData simulationData) {
        Fragment fragment = (Fragment) mSceneEditView;
        Context context = fragment.getContext();
        if (null == context) {
            return;
        }

        int productSkuId = simulationData.getProductSkuId();
        String productName = simulationData.getProductName();
        String productPic1 = simulationData.getProductPic1();
        String productPic2 = simulationData.getProductPic2();
        String productPic3 = simulationData.getProductPic3();
        double productHeight = simulationData.getProductHeight();
        double productOffsetRatio = simulationData.getProductOffsetRatio();

        int augmentedProductSkuId = simulationData.getAugmentedProductSkuId();
        String augmentedProductName = simulationData.getAugmentedProductName();
        String augmentedProductPic1 = simulationData.getAugmentedProductPic1();
        String augmentedProductPic2 = simulationData.getAugmentedProductPic2();
        String augmentedProductPic3 = simulationData.getAugmentedProductPic3();
        double augmentedProductHeight = simulationData.getAugmentedProductHeight();
        double augmentedProductOffsetRatio = simulationData.getAugmentedProductOffsetRatio();

        double collocationHeight = productHeight + augmentedProductHeight - augmentedProductOffsetRatio - productOffsetRatio;
        double h = new BigDecimal(collocationHeight).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

        View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_plant_info_layout, null);
        mPlantInfoPopupWindow = new PopupWindow(view, MATCH_PARENT, WRAP_CONTENT);

        ImageView closeImageView = view.findViewById(R.id.closeImageView);
        TextView collocationHeightTextView = view.findViewById(R.id.collocationHeightTextView);
        ImageView plantImageView = view.findViewById(R.id.plantImageView);
        TextView plantNameTextView = view.findViewById(R.id.plantNameTextView);
        TextView plantSerialNumberTextView = view.findViewById(R.id.plantSerialNumberTextView);
        TextView plantHeightTextView = view.findViewById(R.id.plantHeightTextView);

        RelativeLayout potInfoLayout = view.findViewById(R.id.potInfoLayout);
        ImageView potImageView = view.findViewById(R.id.potImageView);
        TextView potNameTextView = view.findViewById(R.id.potNameTextView);
        TextView potSerialNumberTextView = view.findViewById(R.id.potSerialNumberTextView);
        TextView potHeightTextView = view.findViewById(R.id.potHeightTextView);

        String collocationHeightString = context.getResources().getString(R.string.collocation_total_height) + h + context.getResources().getString(R.string.cm);
        collocationHeightTextView.setText(collocationHeightString);

        if (!TextUtils.isEmpty(productPic1)) {
            plantImageView.setBackground(null);
            ImageUtil.displayImage(context, plantImageView, productPic1);
        } else if (!TextUtils.isEmpty(productPic2)) {
            plantImageView.setBackground(null);
            ImageUtil.displayImage(context, plantImageView, productPic2);
        } else if (!TextUtils.isEmpty(productPic3)) {
            plantImageView.setBackground(null);
            ImageUtil.displayImage(context, plantImageView, productPic3);
        }
        if (!TextUtils.isEmpty(productName)) {
            plantNameTextView.setText(productName);
        }
        String plantSerialNumberString = context.getResources().getString(R.string.serial_number_colon) + productSkuId;
        plantSerialNumberTextView.setText(plantSerialNumberString);

        String plantHeightString = context.getResources().getString(R.string.height) + productHeight + context.getResources().getString(R.string.cm);
        plantHeightTextView.setText(plantHeightString);

        /**/

        if (0 != augmentedProductSkuId) {
            potInfoLayout.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(augmentedProductPic1)) {
            potImageView.setBackground(null);
            ImageUtil.displayImage(context, potImageView, augmentedProductPic1);
        } else if (!TextUtils.isEmpty(augmentedProductPic2)) {
            potImageView.setBackground(null);
            ImageUtil.displayImage(context, potImageView, augmentedProductPic2);
        } else if (!TextUtils.isEmpty(augmentedProductPic3)) {
            potImageView.setBackground(null);
            ImageUtil.displayImage(context, potImageView, augmentedProductPic3);
        }
        if (!TextUtils.isEmpty(augmentedProductName)) {
            potNameTextView.setText(augmentedProductName);
        }
        String potSerialNumberString = context.getResources().getString(R.string.serial_number_colon) + augmentedProductSkuId;
        potSerialNumberTextView.setText(potSerialNumberString);

        String potHeightString = context.getResources().getString(R.string.height) + augmentedProductHeight + context.getResources().getString(R.string.cm);
        potHeightTextView.setText(potHeightString);

        closeImageView.setOnClickListener(this);

        mPlantInfoPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPlantInfoPopupWindow.setOutsideTouchable(true);
        mPlantInfoPopupWindow.setFocusable(true);

        // 设置一个动画效果
        mPlantInfoPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);
        mPlantInfoPopupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 将保存用户的"模拟搭配产品"数据里面的"模拟图片"添加相册
     *
     * @param simulationData "模拟搭配产品"数据
     */
    @Override
    public void joinPhotoAlbum(SimulationData simulationData) {
        Fragment fragment = (Fragment) mSceneEditView;
        Context context = fragment.getContext();
        if (null == context) {
            return;
        }
        String path = FileUtil.createExternalStorageFile(PICTURES_PATH);
        String picturePath = simulationData.getPicturePath();
        if (TextUtils.isEmpty(path) || !judeFileExists(picturePath)) {
            MessageUtil.showMessage(fragment.getResources().getString(R.string.failed_to_save_image));
            return;
        }
        File file = new File(picturePath);
        try {
            FileUtil.copyFile(picturePath, path + file.getName());
            // 发送广播通知相册更新图片
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(path + file.getName()));
            intent.setData(uri);
            context.sendBroadcast(intent);
            MessageUtil.showMessage(fragment.getResources().getString(R.string.image_saved_to) + path + fragment.getResources().getString(R.string.folder));
        } catch (IOException e) {
            e.printStackTrace();
            MessageUtil.showMessage(fragment.getResources().getString(R.string.failed_to_save_image));
        }
    }

    /**
     * 将"模拟搭配产品"数据添加到报价列表数据中
     *
     * @param simulationData "模拟搭配产品"数据
     */
    @Override
    public void addQuotationData(SimulationData simulationData) {
        mSceneEditModel.addQuotationData(simulationData, this);
    }

    /**
     * 添加贴纸
     *
     * @param stickerView        贴纸View
     * @param simulationDataList 用户保存的"模拟搭配产品"数据
     */
    @Override
    public void addSticker(StickerView stickerView, List<SimulationData> simulationDataList) {
        Fragment fragment = (Fragment) mSceneEditView;
        for (int i = 0; i < simulationDataList.size(); i++) {
            SimulationData simulationData = simulationDataList.get(i);
            String finallySkuId = simulationData.getFinallySkuId();
            String picturePath = simulationData.getPicturePath();
            String productName = simulationData.getProductName();
            double productHeight = simulationData.getProductHeight();
            double productOffsetRatio = simulationData.getProductOffsetRatio();
            String augmentedProductName = simulationData.getAugmentedProductName();
            double augmentedProductHeight = simulationData.getAugmentedProductHeight();
            double augmentedProductOffsetRatio = simulationData.getAugmentedProductOffsetRatio();
            float x = simulationData.getX();
            float y = simulationData.getY();
            double xScale = simulationData.getxScale();
            double yScale = simulationData.getyScale();

            StringBuilder stringBuilder = new StringBuilder();
            if (!TextUtils.isEmpty(productName)) {
                stringBuilder.append(productName);
            }
            if (!TextUtils.isEmpty(productName) && !TextUtils.isEmpty(augmentedProductName)) {
                stringBuilder.append("+");
            }
            if (!TextUtils.isEmpty(augmentedProductName)) {
                stringBuilder.append(augmentedProductName);
            }
            String pottedName = stringBuilder.toString();
            double height = productHeight + augmentedProductHeight - augmentedProductOffsetRatio - productOffsetRatio;
            // 四舍五入
            long h = Math.round(height);
            String pottedHeight = fragment.getResources().getString(R.string.total_height_is_expected) + h + fragment.getResources().getString(R.string.cm);

            File file = new File(picturePath);
            if (file.isFile() && file.exists()) {
                Bitmap bitmap = getLocalBitmap(picturePath);
                Drawable drawable = new BitmapDrawable(fragment.getResources(), bitmap);
                DrawableSticker drawableSticker = new DrawableSticker(drawable);
                stickerView.addSticker(simulationData, drawableSticker, x, y, xScale, yScale, i, finallySkuId, pottedName, pottedHeight);
            } else {
                // 图片不存在,可能被删除了
                LogUtil.e(TAG, "图片不存在,可能被删除了");
            }
        }
    }

    /**
     * 删除贴纸信息
     *
     * @param simulationData "模拟搭配产品"信息
     */
    @Override
    public void deleteSimulationData(SimulationData simulationData) {
        if (null == simulationData) {
            return;
        }
        mSceneEditModel.deleteSimulationData(simulationData, this);
    }

    /**
     * 添加贴纸信息
     *
     * @param simulationData "模拟搭配产品"信息
     */
    @Override
    public void addSimulationData(SimulationData simulationData) {
        if (null == simulationData) {
            return;
        }
        mSceneEditModel.addSimulationData(simulationData, this);
    }

    /**
     * 更新贴纸信息
     *
     * @param simulationData 位置发生改变的"模拟搭配产品"信息
     */
    @Override
    public void updateSimulationData(SimulationData simulationData) {
        if (null == simulationData) {
            return;
        }
        mSceneEditModel.updateSimulationData(simulationData, this);
    }

    /**
     * 添加产品数据到"模拟搭配产品"数据
     *
     * @param listBean 产品数据
     */
    @Override
    public void addSimulationData(ListBean listBean) {
        Fragment fragment = (Fragment) mSceneEditView;
        Context context = fragment.getContext();
        String pic2 = listBean.getPic2();
        if (TextUtils.isEmpty(pic2)) {
            return;
        }
        ImageUtil.downloadPicture(context, new ImageUtil.DownloadCallback() {
            @Override
            public void onDownloadStarted() {

            }

            @Override
            public void onDownloadFinished(List<Bitmap> bitmapList) {
                Bitmap bitmap = bitmapList.get(0);
                if (null == bitmap) {
                    return;
                }
                mSceneEditModel.addSimulationData(listBean, bitmap, SceneEditPresenterImpl.this);
            }

            @Override
            public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

            }
        }, pic2);
    }

    /**
     * 添加产品数据到"模拟搭配产品"数据操作的结果
     *
     * @param result true操作成功,false操作失败
     */
    @Override
    public void onAddSimulationDataResult(boolean result) {
        mSceneEditView.onAddSimulationDataResult(result);
    }

    /**
     * 添加产品数据到"模拟搭配产品"数据
     *
     * @param data 用户点击植物规格获取的随机组合数据
     */
    @Override
    public void addSimulationData(SpecSuk.DataBean data) {
        Fragment fragment = (Fragment) mSceneEditView;
        Context context = fragment.getContext();
        List<SpecSuk.DataBean.ListBean> list = data.getList();
        if (null == list || list.size() == 0) {
            return;
        }
        String productPic3 = null;
        String augmentedProductPic3 = null;
        for (SpecSuk.DataBean.ListBean listBean : list) {
            String type = listBean.getType();
            if (PLANT.equals(type)) {
                productPic3 = listBean.getPic3();
            } else {
                augmentedProductPic3 = listBean.getPic3();
            }
        }
        if (TextUtils.isEmpty(productPic3) || TextUtils.isEmpty(augmentedProductPic3)) {
            return;
        }
        ImageUtil.downloadPicture(context, new ImageUtil.DownloadCallback() {
            @Override
            public void onDownloadStarted() {

            }

            @Override
            public void onDownloadFinished(List<Bitmap> bitmapList) {
                handleOnDownloadFinished(data, bitmapList);
            }

            @Override
            public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

            }
        }, productPic3, augmentedProductPic3);
    }

    /**
     * 处理在下载全部图片完成之后的一些事情
     *
     * @param data       用户点击植物规格获取的随机组合数据
     * @param bitmapList 下载完成的图片
     */
    private void handleOnDownloadFinished(SpecSuk.DataBean data, List<Bitmap> bitmapList) {
        double productHeight = 0;
        double productOffsetRatio = 0;
        double augmentedProductHeight = 0;
        double augmentedProductOffsetRatio = 0;
        List<SpecSuk.DataBean.ListBean> list = data.getList();
        for (SpecSuk.DataBean.ListBean listBean : list) {
            String type = listBean.getType();
            if (PLANT.equals(type)) {
                productHeight = listBean.getHeight();
                productOffsetRatio = listBean.getOffset_ratio();
            } else {
                augmentedProductHeight = listBean.getHeight();
                augmentedProductOffsetRatio = listBean.getOffset_ratio();
            }
        }
        // 标记是否存在下载图片失败的情况
        boolean isFailed = false;
        Bitmap[] bitmaps = new Bitmap[bitmapList.size()];
        for (int i = 0; i < bitmapList.size(); i++) {
            Bitmap bitmap = bitmapList.get(i);
            if (null == bitmap) {
                isFailed = true;
                break;
            } else {
                bitmaps[i] = bitmap;
            }
        }
        if (isFailed) {
            //  下载图片失败
            return;
        }
        Bitmap bitmap = ImageUtil.pictureSynthesis(productHeight, augmentedProductHeight, productOffsetRatio, augmentedProductOffsetRatio, bitmaps);
        mSceneEditModel.addSimulationData(data, bitmap, this);
    }

    /**
     * 添加报价数据到"模拟搭配产品"数据
     *
     * @param quotationData 报价数据
     */
    @Override
    public void addSimulationData(QuotationData quotationData) {
        Fragment fragment = (Fragment) mSceneEditView;
        Context context = fragment.getContext();
        String picturePath = quotationData.getPicturePath();
        String productPic2 = quotationData.getProductPic2();
        if (judeFileExists(picturePath)) {
            // 如果存在说明该报价数据是植物与盆器搭配组合的
            Bitmap bitmap = getLocalBitmap(picturePath);
            if (null == bitmap) {
                return;
            }
            mSceneEditModel.addSimulationData(quotationData, bitmap, this);
        } else {
            // 如果不存在说明该报价数据是植物或者盆器非组合
            ImageUtil.downloadPicture(context, new ImageUtil.DownloadCallback() {
                @Override
                public void onDownloadStarted() {

                }

                @Override
                public void onDownloadFinished(List<Bitmap> bitmapList) {
                    Bitmap bitmap = bitmapList.get(0);
                    if (null == bitmap) {
                        return;
                    }
                    mSceneEditModel.addSimulationData(quotationData, bitmap, SceneEditPresenterImpl.this);
                }

                @Override
                public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

                }
            }, productPic2);
        }
    }

    /**
     * sku加入待摆放清单
     *
     * @param simulationData "模拟搭配产品"数据
     */
    @Override
    public void addQuotation(SimulationData simulationData) {
        int productSkuId = simulationData.getProductSkuId();
        int augmentedProductSkuId = simulationData.getAugmentedProductSkuId();
        String picturePath = simulationData.getPicturePath();

        File file = new File(picturePath);
        Map<String, RequestBody> params = new HashMap<>();
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
        params.put("com_pic\"; filename=\"" + file.getName(), requestBody);

        mSceneEditModel.addQuotation(productSkuId, augmentedProductSkuId, params, this);
    }

    /**
     * 将"模拟搭配产品"数据添加到报价列表数据中的结果
     *
     * @param result true添加成功,false添加失败
     */
    @Override
    public void addQuotationDataResult(boolean result) {
        mSceneEditView.addQuotationDataResult(result);
    }

    /**
     * 在sku加入待摆放清单成功时回调
     *
     * @param addQuotation sku加入待摆放清单时服务器端返回的数据
     */
    @Override
    public void onAddQuotationSuccess(AddQuotation addQuotation) {
        mSceneEditView.onAddQuotationSuccess(addQuotation);
    }

    @Override
    public void onAddParam(List<BTCBean> bean) {
        mSceneEditView.onAddParam(bean);
    }

    @Override
    public void onSetUserPositionSuccess(UserPosition userPosition) {
        mSceneEditView.onSetUserPositionSuccess(userPosition);
    }

    @Override
    public void onGetDesignSceneCallback(ResponsePage<ItemDesignSceneModel> page) {
        mSceneEditView.onGetDesignScenes(page);
    }

    @Override
    public void onGetDesignDetailCallback(ItemDesignSceneModel model) {
        mSceneEditView.onGetDesignDetail(model);
    }

    @Override
    public void onCreateDesignCallback(CreateDesignModel model) {
        mSceneEditView.onCreateDesignResult(model.getDesing_number());
    }

    @Override
    public void getParams() {
        mSceneEditModel.addParam(this);
    }

    @Override
    public void setUserPosition(String cityCode) {
        mSceneEditModel.setUserPosition(cityCode, this);
    }

    @Override
    public void getDesignDetail(String number) {
        mSceneEditModel.getDesignDetail(number, this);
    }

    @Override
    public void createDesign() {
        mSceneEditModel.createDesign(this);
    }

    @Override
    public void onCreateSceneOrPic(CreateSceneOrPicModel model) {
        mSceneEditView.onCreateSceneOrPicCallback(model);
    }

    @Override
    public void onGetSceneInfo(List<SceneInfo> sceneInfos) {
        mSceneEditView.onGetSceneListCallback(sceneInfos);
    }

    @Override
    public void CreateSceneOrPic(String desing_number, String type, String scheme_id, String scheme_name,
                                 Map<String, RequestBody> pic, String product_sku_id) {
        mSceneEditModel.createSceneOrPic(desing_number, type, scheme_id, scheme_name, pic,
                product_sku_id, this);
    }

    @Override
    public void getSceneList() {
        mSceneEditModel.getSceneList(this);
    }

    /**
     * 设置"搭配图片布局的容器"子View的位置
     *
     * @param collocationLayout        搭配图片布局的容器
     * @param simulationDataList       用户保存的"模拟搭配图片"数据
     * @param finallySkuId             当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
     * @param productCombinationType   主产品组合模式: 1单图模式,2搭配上部,3搭配下部
     * @param augmentedCombinationType 附加产品组合模式: 1单图模式,2搭配上部,3搭配下部
     */
    @Override
    public void setCollocationLayoutPosition(RelativeLayout collocationLayout, List<SimulationData> simulationDataList,
                                             String finallySkuId, int productCombinationType, int augmentedCombinationType) {
        Fragment fragment = (Fragment) mSceneEditView;
        Context context = fragment.getContext();
        if (null == context) {
            return;
        }
        mViewPagerList.clear();
        mMatchLayoutList.clear();
        // 移除上一次动态添加的View
        collocationLayout.removeAllViews();

        // 根据当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
        // 在用户保存的"模拟搭配图片"数据找出同款的产品
        List<SimulationData> list = new ArrayList<>();
        for (SimulationData simulationData : simulationDataList) {
            String finallySkuId_ = simulationData.getFinallySkuId();
            if (TextUtils.isEmpty(finallySkuId) || TextUtils.isEmpty(finallySkuId_)) {
                continue;
            }
            if (!finallySkuId.equals(finallySkuId_)) {
                continue;
            }
            list.add(simulationData);
        }
        if (list.size() == 0) {
            return;
        }
        for (SimulationData simulationData : list) {
            float currentX = simulationData.getX();
            float currentY = simulationData.getY();
            double width = simulationData.getWidth();
            double height = simulationData.getHeight();
            double xScale = simulationData.getxScale();
            double yScale = simulationData.getyScale();
            double currentStickerWidth = width * xScale;
            double currentStickerHeight = height * yScale;

            if (1 == productCombinationType || 1 == augmentedCombinationType) {
                // 动态添加View
                HorizontalViewPager viewPager = new HorizontalViewPager(context);
                viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
                collocationLayout.addView(viewPager);
                mViewPagerList.add(viewPager);

                // 动态设置位置
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
                layoutParams.leftMargin = (int) currentX;
                layoutParams.topMargin = (int) currentY;
                layoutParams.width = (int) currentStickerWidth;
                layoutParams.height = (int) currentStickerHeight;
                viewPager.setLayoutParams(layoutParams);
            } else {
                // 动态添加View
                MatchLayout matchLayout = new MatchLayout(context);
                collocationLayout.addView(matchLayout);
                mMatchLayoutList.add(matchLayout);

                // 动态设置位置
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) matchLayout.getLayoutParams();
                layoutParams.leftMargin = (int) currentX;
                layoutParams.topMargin = (int) currentY;
                layoutParams.width = (int) currentStickerWidth;
                layoutParams.height = (int) currentStickerHeight;
                matchLayout.setLayoutParams(layoutParams);
            }
        }
        if (1 == productCombinationType || 1 == augmentedCombinationType) {
            // 单图模式
            mSceneEditView.onSetSingleCollocationLayoutPositionSucceed(mViewPagerList);
        } else {
            // 上下搭配
            mSceneEditView.onSetGroupCollocationLayoutPositionSucceed(mMatchLayoutList);
        }
    }

    /**
     * 设置"搭配图片布局的容器"子View的位置
     *
     * @param collocationLayout        搭配图片布局的容器
     * @param simulationDataList       用户保存的"模拟搭配图片"数据
     * @param finallySkuId             当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
     * @param productCombinationType   主产品组合模式: 1单图模式,2搭配上部,3搭配下部
     * @param augmentedCombinationType 附加产品组合模式: 1单图模式,2搭配上部,3搭配下部
     */
    @Override
    public void setCollocationLayoutPosition(BaseSticker currentSticker, RelativeLayout collocationLayout, List<SimulationData> simulationDataList,
                                             String finallySkuId, int productCombinationType, int augmentedCombinationType) {
        Fragment fragment = (Fragment) mSceneEditView;
        Context context = fragment.getContext();
        if (null == context) {
            return;
        }
        mViewPagerList.clear();
        mMatchLayoutList.clear();
        // 移除上一次动态添加的View
        collocationLayout.removeAllViews();

        // 根据当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
        // 在用户保存的"模拟搭配图片"数据找出同款的产品
        List<SimulationData> list = new ArrayList<>();
        for (SimulationData simulationData : simulationDataList) {
            String finallySkuId_ = simulationData.getFinallySkuId();
            if (TextUtils.isEmpty(finallySkuId) || TextUtils.isEmpty(finallySkuId_)) {
                continue;
            }
            if (!finallySkuId.equals(finallySkuId_)) {
                continue;
            }
            list.add(simulationData);
        }
        if (list.size() == 0) {
            return;
        }
        for (SimulationData simulationData : list) {
            float currentX = simulationData.getX();
            float currentY = simulationData.getY();
            double width = simulationData.getWidth();
            double height = simulationData.getHeight();
            double xScale = simulationData.getxScale();
            double yScale = simulationData.getyScale();
//            double currentStickerWidth = width * xScale;
//            double currentStickerHeight = height * yScale;
            Drawable drawable = currentSticker.getDrawable();
            float scaleFactorWidth = (float) (simulationData.getWidth() / drawable.getIntrinsicWidth());
            float scaleFactorHeight = (float) (simulationData.getHeight() / drawable.getIntrinsicHeight());
            double currentStickerWidth = width * xScale * scaleFactorWidth;
            double currentStickerHeight = height * yScale * scaleFactorHeight;

            if (1 == productCombinationType || 1 == augmentedCombinationType) {
                // 动态添加View
                HorizontalViewPager viewPager = new HorizontalViewPager(context);
                viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
                collocationLayout.addView(viewPager);
                mViewPagerList.add(viewPager);

                // 动态设置位置
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
//                layoutParams.leftMargin = (int) (currentX - (currentStickerHeight / 2 - currentStickerWidth / 2));
                layoutParams.leftMargin = (int) currentX;
                layoutParams.topMargin = (int) currentY;
                layoutParams.width = (int) currentStickerWidth;
                layoutParams.height = (int) currentStickerHeight;
                viewPager.setLayoutParams(layoutParams);
            } else {
                // 动态添加View
                MatchLayout matchLayout = new MatchLayout(context);
                collocationLayout.addView(matchLayout);
                mMatchLayoutList.add(matchLayout);

                // 动态设置位置
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) matchLayout.getLayoutParams();
//                layoutParams.leftMargin = (int) (currentX - (currentStickerHeight / 2 - currentStickerWidth / 2));
                layoutParams.leftMargin = (int) currentX;
                layoutParams.topMargin = (int) currentY;
                layoutParams.width = (int) currentStickerWidth;
                layoutParams.height = (int) currentStickerHeight;
                matchLayout.setLayoutParams(layoutParams);
            }
        }
        if (1 == productCombinationType || 1 == augmentedCombinationType) {
            // 单图模式
            mSceneEditView.onSetSingleCollocationLayoutPositionSucceed(mViewPagerList);
        } else {
            // 上下搭配
            mSceneEditView.onSetGroupCollocationLayoutPositionSucceed(mMatchLayoutList);
        }
    }

    /**
     * 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
     *
     * @param matchLayout      自定义植物与盆器互相搭配的View
     * @param plantViewPager   放置"植物自由搭配图"的ViewPager
     * @param potViewPager     放置"盆器自由搭配图"的ViewPager
     * @param plantHeight      用户当前选择的植物规格的高度
     * @param potHeight        用户当前选择的盆规格的高度
     * @param plantOffsetRatio 花盆的偏移量
     * @param potOffsetRatio   花盆的偏移量
     */
    @Override
    public void setCollocationContentParams(MatchLayout matchLayout, HorizontalViewPager plantViewPager, HorizontalViewPager potViewPager,
                                            double plantHeight, double potHeight, double plantOffsetRatio, double potOffsetRatio) {
        matchLayout.postDelayed(() -> {
            int height = matchLayout.getHeight();
            int width = matchLayout.getWidth();
            int top = matchLayout.getTop();
            double h = plantHeight + potHeight - plantOffsetRatio - potOffsetRatio;
            if (0 == h) {
                return;
            }
            // 将搭配图片布局的容器的高度/(植物高度 + 盆的高度 - 花盆的偏移量) = 每一份的高度,保留2为小数
            double portionHeight = BigDecimal.valueOf((float) height / h).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            double plantImageViewParamsHeight = BigDecimal.valueOf(portionHeight * (plantHeight)).
                    setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double viewPagerParamsHeight = BigDecimal.valueOf(portionHeight * (potHeight)).
                    setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            ViewGroup.LayoutParams viewPagerParams = potViewPager.getLayoutParams();
            viewPagerParams.height = BigDecimal.valueOf(viewPagerParamsHeight).setScale(0, BigDecimal.ROUND_UP).intValue();
            potViewPager.setLayoutParams(viewPagerParams);

            if (plantOffsetRatio > 0) {
                double plantImageViewParamsWidth = (width + ((plantOffsetRatio * portionHeight) * (width / plantHeight)));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        BigDecimal.valueOf(plantImageViewParamsWidth).setScale(0, BigDecimal.ROUND_UP).intValue(),
                        BigDecimal.valueOf(height).setScale(0, BigDecimal.ROUND_UP).intValue());

                int windowWidth = YiBaiApplication.getWindowWidth();
                params.leftMargin = BigDecimal.valueOf(((double) windowWidth - plantImageViewParamsWidth) / 2)
                        .setScale(0, BigDecimal.ROUND_UP).intValue();
                params.topMargin = (int) top;
                matchLayout.setLayoutParams(params);

                ViewGroup.LayoutParams plantImageViewParams = plantViewPager.getLayoutParams();
                plantImageViewParams.height = BigDecimal.valueOf(plantImageViewParamsHeight + (plantOffsetRatio * portionHeight))
                        .setScale(0, BigDecimal.ROUND_UP).intValue();
                plantImageViewParams.width = BigDecimal.valueOf(plantImageViewParamsWidth)
                        .setScale(0, BigDecimal.ROUND_UP).intValue();
                plantViewPager.setLayoutParams(plantImageViewParams);
            } else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                int windowWidth = YiBaiApplication.getWindowWidth();
                params.leftMargin = (int) ((windowWidth - width) / 2);
                params.topMargin = (int) top;
                matchLayout.setLayoutParams(params);

                ViewGroup.LayoutParams plantImageViewParams = plantViewPager.getLayoutParams();
                plantImageViewParams.height = BigDecimal.valueOf(plantImageViewParamsHeight)
                        .setScale(0, BigDecimal.ROUND_UP).intValue();
                plantImageViewParams.width = width;
                plantViewPager.setLayoutParams(plantImageViewParams);
            }

        }, 200);
    }

    /**
     * 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
     *
     * @param matchLayout      自定义植物与盆器互相搭配的View
     * @param plantViewPager   放置"植物自由搭配图"的ViewPager
     * @param potViewPager     放置"盆器自由搭配图"的ViewPager
     * @param plantHeight      用户当前选择的植物规格的高度
     * @param potHeight        用户当前选择的盆规格的高度
     * @param plantOffsetRatio 花盆的偏移量
     * @param potOffsetRatio   花盆的偏移量
     */
    @Override
    public void setCollocationContentPlantAndPot(MatchLayout matchLayout, HorizontalViewPager plantViewPager, HorizontalViewPager potViewPager,
                                                 double plantHeight, double potHeight, double plantOffsetRatio, double potOffsetRatio) {
        matchLayout.post(() -> {
                int height = matchLayout.getHeight();
                int width = matchLayout.getWidth();
                int top = matchLayout.getTop();
                double h = plantHeight + potHeight - plantOffsetRatio - potOffsetRatio;
                if (0 == h) {
                    return;
                }
                // 将搭配图片布局的容器的高度/(植物高度 + 盆的高度 - 花盆的偏移量) = 每一份的高度,保留2为小数
                double portionHeight = BigDecimal.valueOf((float) height / h).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                double plantImageViewParamsHeight = BigDecimal.valueOf(portionHeight * (plantHeight)).
                        setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                double viewPagerParamsHeight = BigDecimal.valueOf(portionHeight * (potHeight)).
                        setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                ViewGroup.LayoutParams viewPagerParams = potViewPager.getLayoutParams();
                viewPagerParams.height = BigDecimal.valueOf(viewPagerParamsHeight).setScale(0, BigDecimal.ROUND_UP).intValue();
                potViewPager.setLayoutParams(viewPagerParams);

                if (plantOffsetRatio > 0) {
                    double plantImageViewParamsWidth = width;
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            BigDecimal.valueOf(plantImageViewParamsWidth).setScale(0, BigDecimal.ROUND_UP).intValue(),
                            BigDecimal.valueOf(height).setScale(0, BigDecimal.ROUND_UP).intValue());

                    int windowWidth = YiBaiApplication.getWindowWidth();
                    params.leftMargin = BigDecimal.valueOf(((double) windowWidth - plantImageViewParamsWidth) / 2)
                            .setScale(0, BigDecimal.ROUND_UP).intValue();
                    params.topMargin = (int) top;
                    matchLayout.setLayoutParams(params);

                    ViewGroup.LayoutParams plantImageViewParams = plantViewPager.getLayoutParams();
                    plantImageViewParams.height = BigDecimal.valueOf(plantImageViewParamsHeight + (plantOffsetRatio * portionHeight))
                            .setScale(0, BigDecimal.ROUND_UP).intValue();
                    plantImageViewParams.width = (int) plantImageViewParamsWidth;
                    plantViewPager.setLayoutParams(plantImageViewParams);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                    int windowWidth = YiBaiApplication.getWindowWidth();
                    params.leftMargin = (int) ((windowWidth - width) / 2);
                    params.topMargin = (int) top;
                    matchLayout.setLayoutParams(params);

                    ViewGroup.LayoutParams plantImageViewParams = plantViewPager.getLayoutParams();
                    plantImageViewParams.height = BigDecimal.valueOf(plantImageViewParamsHeight)
                            .setScale(0, BigDecimal.ROUND_UP).intValue();
                    plantImageViewParams.height = width;
                    plantViewPager.setLayoutParams(plantImageViewParams);
                }
//            int height = matchLayout.getHeight();
//            double allHeight = plantHeight + potHeight;
//            double mPlant = plantHeight - plantOffsetRatio;
//            double mPot = potHeight - potOffsetRatio;
//            // 将搭配图片布局的容器的高度/(植物高度 + 盆的高度 - 花盆的偏移量) = 每一份的高度,保留2为小数
//            double portionPlantHeight = BigDecimal.valueOf((float) height / mPlant).doubleValue();
//            double portionPotHeight = BigDecimal.valueOf((float) height / mPot).doubleValue();
//            ViewGroup.LayoutParams plantImageViewParams = plantViewPager.getLayoutParams();
//            plantImageViewParams.height = (int) (portionPlantHeight * (plantHeight + plantOffsetRatio) / 2);
//            plantViewPager.setLayoutParams(plantImageViewParams);
//
//            ViewGroup.LayoutParams viewPagerParams = potViewPager.getLayoutParams();
//            viewPagerParams.height = (int) (portionPotHeight * (potHeight + potOffsetRatio) / 2);
//            potViewPager.setLayoutParams(viewPagerParams);
        });
    }

    @Override
    public void setCollocationContent(MatchLayout matchLayout, HorizontalViewPager plantViewPager, HorizontalViewPager potViewPager,
                                      double plantHeight, double potHeight, double plantOffsetRatio, double potOffsetRatio) {
        matchLayout.post(() -> {
            int height = matchLayout.getHeight();

            ViewGroup.LayoutParams plantImageViewParams = plantViewPager.getLayoutParams();
            plantImageViewParams.height = height;
            plantViewPager.setLayoutParams(plantImageViewParams);

            ViewGroup.LayoutParams viewPagerParams = potViewPager.getLayoutParams();
            viewPagerParams.height = height;
            potViewPager.setLayoutParams(viewPagerParams);
        });
    }

    /**
     * 将新搭配的产品数据添加到"模拟搭配产品"数据
     *
     * @param productData        产品相关数据
     * @param simulationDataList 用户保存的"模拟搭配图片"数据
     * @param finallySkuId       当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
     * @param single             true 单图模式/false 上下搭配
     */
    @Override
    public void addSimulationData(ProductData productData, List<SimulationData> simulationDataList, String finallySkuId, boolean single) {
        downloadAndSynthesis(productData, simulationDataList, finallySkuId, single);
    }

    /**
     * 下载图片与合成图片
     *
     * @param productData        产品相关数据
     * @param simulationDataList 用户保存的"模拟搭配图片"数据
     * @param finallySkuId       当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
     * @param single             true 单图模式/false 上下搭配
     */
    private void downloadAndSynthesis(ProductData productData, List<SimulationData> simulationDataList, String finallySkuId, boolean single) {
        Fragment fragment = (Fragment) mSceneEditView;
        Activity activity = fragment.getActivity();

        String augmentedProductPic2 = productData.getAugmentedProductPic2();
        String productPic3 = productData.getProductPic3();
        String augmentedProductPic3 = productData.getAugmentedProductPic3();

        if (single) {
            ImageUtil.downloadPicture(activity, new ImageUtil.DownloadCallback() {
                @Override
                public void onDownloadStarted() {

                }

                @Override
                public void onDownloadFinished(List<Bitmap> bitmapList) {
                    handleOnDownloadFinished(productData, simulationDataList, finallySkuId, single, bitmapList);
                }

                @Override
                public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

                }
            }, augmentedProductPic2);
        } else if (!TextUtils.isEmpty(productPic3) && !TextUtils.isEmpty(augmentedProductPic3)) {
            ImageUtil.downloadPicture(activity, new ImageUtil.DownloadCallback() {
                @Override
                public void onDownloadStarted() {

                }

                @Override
                public void onDownloadFinished(List<Bitmap> bitmapList) {
                    handleOnDownloadFinished(productData, simulationDataList, finallySkuId, single, bitmapList);
                }

                @Override
                public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

                }
            }, productPic3, augmentedProductPic3);
        } else {
            mSceneEditView.onAddSimulationDataResult(false);
        }
    }

    /**
     * 处理在下载全部图片完成之后的一些事情
     *
     * @param productData        产品相关数据
     * @param simulationDataList 用户保存的"模拟搭配图片"数据
     * @param finallySkuId       当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
     * @param single             true 单图模式/false 上下搭配
     * @param bitmapList         下载完成的图片
     */
    private void handleOnDownloadFinished(ProductData productData, List<SimulationData> simulationDataList, String finallySkuId, boolean single, List<Bitmap> bitmapList) {
        double productHeight = productData.getProductHeight();
        double productOffsetRatio = productData.getProductOffsetRatio();
        double augmentedProductHeight = productData.getAugmentedProductHeight();
        double augmentedProductOffsetRatio = productData.getAugmentedProductOffsetRatio();

        if (single) {
            Bitmap bitmap = bitmapList.get(0);
            if (null == bitmap) {
                // 下载图片失败return
                return;
            }
            mSceneEditModel.addSimulationData(productData, simulationDataList, finallySkuId, single, bitmap, this);
            return;
        }

        // 标记是否存在下载图片失败的情况
        boolean isFailed = false;
        Bitmap[] bitmaps = new Bitmap[bitmapList.size()];
        for (int i = 0; i < bitmapList.size(); i++) {
            Bitmap bitmap = bitmapList.get(i);
            if (null == bitmap) {
                isFailed = true;
                break;
            } else {
                bitmaps[i] = bitmap;
            }
        }
        if (isFailed) {
            // 下载图片失败return
            return;
        }
        Bitmap bitmap = ImageUtil.pictureSynthesis(productHeight, augmentedProductHeight, productOffsetRatio,
                augmentedProductOffsetRatio, bitmaps);
        if (null == bitmap) {
            // 合成图片失败return
            return;
        }
        mSceneEditModel.addSimulationData(productData, simulationDataList, finallySkuId, single, bitmap, this);
    }

    /**
     * 显示显示产品代码信息的PopupWindow
     *
     * @param v                       锚点
     * @param productPriceCode        主产品售价代码
     * @param productTradePriceCode   主产品批发价代码
     * @param augmentedPriceCode      附加产品售价代码
     * @param augmentedTradePriceCode 附加产品批发价代码
     * @param productPrice            主产品价格
     * @param augmentedProductPrice   附加产品价格
     */
    @Override
    public void displayProductCodePopupWindow(View v, String productPriceCode, String productTradePriceCode,
                                              String augmentedPriceCode, String augmentedTradePriceCode,
                                              double productPrice, double augmentedProductPrice) {
        Fragment fragment = (Fragment) mSceneEditView;
        Activity activity = fragment.getActivity();
        if (null == activity) {
            return;
        }

        View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_product_code_info_layout, null);
        mProductCodePopupWindow = new PopupWindow(view, MATCH_PARENT, WRAP_CONTENT);

        RelativeLayout rootLayout = view.findViewById(R.id.rootLayout);
        TextView plantProductPriceCodeTextView = view.findViewById(R.id.plantProductPriceCodeTextView);
        TextView potProductPriceCodeTextView = view.findViewById(R.id.potProductPriceCodeTextView);
        TextView plantProductTradePriceCodeTextView = view.findViewById(R.id.plantProductTradePriceCodeTextView);
        TextView potProductTradePriceCodeTextView = view.findViewById(R.id.potProductTradePriceCodeTextView);
        TextView productPriceCodeTextView = view.findViewById(R.id.productPriceCodeTextView);

        rootLayout.setOnClickListener(v1 -> mProductCodePopupWindow.dismiss());

        if (TextUtils.isEmpty(productPriceCode)) {
            plantProductPriceCodeTextView.setText(" ");
        } else {
            plantProductPriceCodeTextView.setText(productPriceCode);
        }
        if (TextUtils.isEmpty(augmentedPriceCode)) {
            potProductPriceCodeTextView.setText(" ");
        } else {
            potProductPriceCodeTextView.setText(augmentedPriceCode);
        }
        if (TextUtils.isEmpty(productTradePriceCode)) {
            plantProductTradePriceCodeTextView.setText(" ");
        } else {
            plantProductTradePriceCodeTextView.setText(productTradePriceCode);
        }
        if (TextUtils.isEmpty(augmentedTradePriceCode)) {
            potProductTradePriceCodeTextView.setText(" ");
        } else {
            potProductTradePriceCodeTextView.setText(augmentedTradePriceCode);
        }
        double totalPrice = productPrice + augmentedProductPrice;
        String s = YiBaiApplication.getCurrencySymbol() + totalPrice;
        productPriceCodeTextView.setText(s);

        mProductCodePopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mProductCodePopupWindow.setOutsideTouchable(true);
        mProductCodePopupWindow.setFocusable(true);

        // 添加PopupWindow窗口关闭事件
        mProductCodePopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mProductCodePopupWindow.showAsDropDown(v, Gravity.BOTTOM, -DensityUtil.dpToPx(activity, 56), 0);
        }
    }

    @Override
    public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

    }

    @Override
    public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

    }

    /**
     * 在RangeSeekBar值发生变化时回调
     *
     * @param view       值发生变化的RangeSeekBar
     * @param leftValue  RangeSeekBar左边的值
     * @param rightValue RangeSeekBar右边的值
     * @param isFromUser 是否来自于用户主动触发的
     */
    @Override
    public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
        mSceneEditView.onRangeChanged(view, leftValue);
    }

    @Override
    public void onClick(View v) {
        if (null != mSceneBackgroundEditPopupWindow && mSceneBackgroundEditPopupWindow.isShowing()) {
            mSceneBackgroundEditPopupWindow.dismiss();
        }
        if (null != mPlantInfoPopupWindow && mPlantInfoPopupWindow.isShowing()) {
            mPlantInfoPopupWindow.dismiss();
        }
    }
}