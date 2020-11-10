package com.ybw.yibai.module.imagecontrast;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.adapter.ImageContrastAdapter;
import com.ybw.yibai.common.bean.ImageContrastSelectedProduct;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.Recommend;
import com.ybw.yibai.common.bean.Recommend.DataBean;
import com.ybw.yibai.common.bean.RecommendBean;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.imagecontrast.ImageContrastContract.ImageContrastPresenter;
import com.ybw.yibai.module.imagecontrast.ImageContrastContract.ImageContrastView;
import com.ybw.yibai.module.sceneedit.SceneEditFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.FILE_PATH;
import static com.ybw.yibai.common.constants.Preferences.PLANT;
import static com.ybw.yibai.common.constants.Preferences.PLANT_INFO;
import static com.ybw.yibai.common.constants.Preferences.POT;
import static com.ybw.yibai.common.constants.Preferences.POT_INFO;
import static com.ybw.yibai.common.constants.Preferences.POT_TYPE_ID;
import static com.ybw.yibai.common.constants.Preferences.PRODUCT_TYPE;
import static com.ybw.yibai.common.constants.Preferences.SIMULATION_DATA;

/**
 * 多张搭配图片模拟效果对比
 *
 * @author sjl
 * @date 2019/11/8
 */
public class ImageContrastActivity extends BaseActivity implements
        ImageContrastView, View.OnClickListener, ImageContrastAdapter.OnItemClickListener {

    public static final String TAG = "ImageContrastActivity";

    /**
     * 场景背景图片文件的绝对路径
     */
    private String filePath;

    /**
     * 主产品名称的款名Id
     */
    private int productSkuId;

    /**
     * 附加产品的款名Id
     */
    private int augmentedProductSkuId;

    /**
     * 用户点击"选择分类"获取到的盆器类别ID
     */
    private int potTypeId;

    /**
     * 盆栽大中小类别
     */
    private int spec;

    /**
     *
     */
    private String cateCode;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 盆器对比/植物对比
     */
    private TextView mComparisonTextView;

    /**
     * 显示搭配图片模拟效果
     */
    private RecyclerView mRecyclerView;

    /**
     * 换一批
     */
    private TextView mChangeBatchTextView;

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
     * 植物信息
     */
    private ListBean mPlantInfo;

    /**
     * 盆器信息
     */
    private ListBean mPotInfo;

    /**
     * 贴纸数据
     */
    private SimulationData mSimulationData;

    /**
     * 推荐植物/花盆
     */
    private List<ListBean> mList;

    /**
     * 多张搭配图片模拟效果对比适配器
     */
    private ImageContrastAdapter mAdapter;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private ImageContrastPresenter mImageContrastPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_image_contrast;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mComparisonTextView = findViewById(R.id.comparisonTextView);
        mRecyclerView = findViewById(R.id.recyclerView);
        mChangeBatchTextView = findViewById(R.id.changeBatchTextView);
        mWaitDialog = new WaitDialog(this);

        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 给RecyclerView设置布局管理器(必须设置)
        mRecyclerView.setLayoutManager(manager);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();

        Intent intent = getIntent();
        filePath = intent.getStringExtra(FILE_PATH);
        mPlantInfo = (ListBean) intent.getSerializableExtra(PLANT_INFO);
        mPotInfo = (ListBean) intent.getSerializableExtra(POT_INFO);
        mSimulationData = (SimulationData) intent.getSerializableExtra(SIMULATION_DATA);
        potTypeId = intent.getIntExtra(POT_TYPE_ID, 0);
        cateCode = intent.getStringExtra(PRODUCT_TYPE);
        if (null != mPlantInfo) {
            productSkuId = mPlantInfo.getSku_id();
        }
        if (null != mPotInfo) {
            augmentedProductSkuId = mPotInfo.getSku_id();
        }
        if (PLANT.equals(cateCode)) {
            mComparisonTextView.setText(getResources().getString(R.string.plant_comparison));
        } else {
            mComparisonTextView.setText(getResources().getString(R.string.pot_comparison));
        }

        mAdapter = new ImageContrastAdapter(this, filePath, mSimulationData, mList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initEvent() {
        mImageContrastPresenter = new ImageContrastPresenterImpl(this);
        mImageContrastPresenter.getNewRecommed(cateCode, productSkuId, augmentedProductSkuId,
                potTypeId);
//        mImageContrastPresenter.getRecommend(cateCode, productSkuId, augmentedProductSkuId,
//                spec, potTypeId, null, null, null, null);

        mBackImageView.setOnClickListener(this);
        mComparisonTextView.setOnClickListener(this);
        mChangeBatchTextView.setOnClickListener(this);
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

        // 盆器对比/植物对比
        if (id == R.id.comparisonTextView) {
            mImageContrastPresenter.displayComparisonPopupWindow(mComparisonTextView);
        }

        // 换一批
        if (id == R.id.changeBatchTextView) {
            mImageContrastPresenter.getRecommend(cateCode, productSkuId, augmentedProductSkuId,
                    spec, potTypeId, null, null, null, null);
        }
    }

    /**
     * 在PopupWindow里面的RadioButton被选中时回调
     *
     * @param group     被点击的RadioGroup
     * @param checkedId 被选中的RadioButton ID
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 盆器对比
        if (checkedId == R.id.potComparisonRadioButton) {
            mComparisonTextView.setText(getResources().getString(R.string.pot_comparison));
            mImageContrastPresenter.getRecommend(POT, productSkuId, augmentedProductSkuId,
                    spec, null, null, null, null, null);
        }

        // 植物对比
        if (checkedId == R.id.plantComparisonRadioButton) {
            mComparisonTextView.setText(getResources().getString(R.string.plant_comparison));
            mImageContrastPresenter.getRecommend(PLANT, productSkuId, augmentedProductSkuId,
                    spec, null, null, null, null, null);
        }
    }

    /**
     * 在RecyclerView Item 点击时回调
     *
     * @param position    被点击的Item位置
     * @param picturePath 组合图片在本地保存的路径
     */
    @Override
    public void onItemClick(int position, String picturePath) {
        ListBean listBean = mList.get(position);
        String categoryCode = listBean.getCategoryCode();
        if (PLANT.equals(categoryCode)) {
            productSkuId = listBean.getSku_id();
            augmentedProductSkuId = mSimulationData.getAugmentedProductSkuId();
        } else {
            productSkuId = mSimulationData.getAugmentedProductSkuId();
            augmentedProductSkuId = listBean.getSku_id();
        }
        /**
         * 发送数据到{@link SceneEditFragment#imageContrastActivitySelectedProduct(ImageContrastSelectedProduct)}
         */
        EventBus.getDefault().post(new ImageContrastSelectedProduct(productSkuId, augmentedProductSkuId));
        finish();
        // mImageContrastPresenter.addSimulationData(mSimulationData, listBean, picturePath);
    }

    @Override
    public void onGetRecommendSuccess(RecommendBean recommend) {

    }

    /**
     * 换搭配获取植物花盆列表成功时回调
     *
     * @param recommend 植物花盆列表
     */
    @Override
    public void onGetRecommendSuccess(Recommend recommend) {
        if (CODE_SUCCEED != recommend.getCode()) {
            MessageUtil.showMessage(recommend.getMsg());
            return;
        }
        DataBean data = recommend.getData();
        if (null == data) {
            return;
        }
        List<ListBean> list = new ArrayList<>();
        if (data.getPot() != null) {
            list = data.getPot().getList();
            cateCode = POT;
        } else {
            if (data.getPlant() != null) {
                list = data.getPlant().getList();
                cateCode = PLANT;
            }
        }

        if (null == list || list.size() == 0) {
            return;
        }
        mList.clear();
        if (PLANT.equals(cateCode)) {
            mPlantInfo.setCategoryCode(PLANT);
            mList.add(mPlantInfo);
        } else {
            mPotInfo.setCategoryCode(POT);
            mList.add(mPotInfo);
        }
        for (ListBean listBean : list) {
            if (PLANT.equals(cateCode)) {
                listBean.setCategoryCode(PLANT);
            } else {
                listBean.setCategoryCode(POT);
            }
        }
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
//        if (CODE_SUCCEED != recommend.getCode()) {
//            MessageUtil.showMessage(recommend.getMsg());
//            return;
//        }
//        DataBean data = recommend.getData();
//        if (null == data) {
//            return;
//        }
//        List<ListBean> list = data.getList();
//        if (null == list || list.size() == 0) {
//            return;
//        }
//        mList.clear();
//        cateCode = data.getCateCode();
//        if (PLANT.equals(cateCode)) {
//            mPlantInfo.setCategoryCode(PLANT);
//            mList.add(mPlantInfo);
//        } else {
//            mPotInfo.setCategoryCode(POT);
//            mList.add(mPotInfo);
//        }
//        for (ListBean listBean : list) {
//            if (PLANT.equals(cateCode)) {
//                listBean.setCategoryCode(PLANT);
//            } else {
//                listBean.setCategoryCode(POT);
//            }
//        }
//        mList.addAll(list);
//        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加到模拟列表操作的结果
     *
     * @param result true操作成功,false操作失败
     */
    @Override
    public void onAddSimulationDataResult(boolean result) {

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
    public void onDestroy() {
        super.onDestroy();
        if (null != mImageContrastPresenter) {
            mImageContrastPresenter.onDetachView();
            mImageContrastPresenter = null;
        }
    }
}
