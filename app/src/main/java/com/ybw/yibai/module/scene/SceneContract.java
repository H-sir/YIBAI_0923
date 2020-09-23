package com.ybw.yibai.module.scene;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.View;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.CreateSceneData;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.bean.DesignScheme;
import com.ybw.yibai.common.bean.DesignSchemeRequest;
import com.ybw.yibai.common.bean.PlacementQrQuotationList;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.model.CreateSceneOrPicModel;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/9/14
 */
public interface SceneContract {

    interface SceneView extends BaseView {

        /**
         * 新创建场景信息的结果
         *
         * @param result true新创建成功,false新创建失败
         */
        void addSceneInfoResult(boolean result);

        /**
         * 在查找用户的场景信息成功时回调
         *
         * @param sceneInfoList 用户场景信息列表
         */
        void onFindUserSceneInfoSuccess(List<SceneInfo> sceneInfoList);

        /**
         * 在获取摆放清单列表成功时回调
         *
         * @param placementQrQuotationList 摆放清单列表
         */
        void onGetPlacementListSuccess(PlacementQrQuotationList placementQrQuotationList);

        /**
         * 在删除待摆放清单产品成功时回调
         *
         * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
         */
        void onDeletePlacementListSuccess(DeletePlacement deletePlacement);

        /**
         * 在获取用户保存的"报价"数据成功时回调
         *
         * @param quotationDataList 用户保存的"报价"数据列表
         */
        void onGetQuotationDataSuccess(List<QuotationData> quotationDataList);

        /**
         * 更新用户场景信息列表的集合的结果
         *
         * @param result true添加成功,false添加失败
         */
        void updateSceneListInfoResult(boolean result);

        /**
         * 将"模拟搭配产品"数据添加到报价列表数据中的结果
         *
         * @param result true添加成功,false添加失败
         */
        void addQuotationResult(boolean result);

        /**
         * 申请权限的结果
         *
         * @param b true 已经获取全部权限,false 没有获取全部权限
         */
        void applyPermissionsResults(boolean b);

        /**
         * 返回从相机或相册返回的图像
         *
         * @param file 图像文件
         */
        void returnsTheImageReturnedFromTheCameraOrAlbum(File file);

        void onCreateSceneOrPicCallback(CreateSceneOrPicModel model);

        /**
         * 在场景添加图片产品成功时回调
         *
         * @param designScheme 设计详情
         */
        void onGetAddDesignSchemeSuccess(DesignScheme designScheme);

        /**
         * 点击继续添加时返回的图片地址和主副产品的值
         */
        void onAddSchemePath(String path, int productSkuId, int augmentedProductSkuId);

        void onGetDesignSchemeSuccess(DesignScheme designScheme);
    }

    interface ScenePresenter extends BasePresenter<SceneView> {

        /**
         * 动态设置"NavigationView"的宽度
         *
         * @param navigationView 要设置宽度的NavigationView
         */
        void setNavigationViewParams(NavigationView navigationView);

        /**
         * 新创建场景信息
         *
         * @param createSceneData 创建场景时需要的数据
         */
        void addSceneInfo(List<CreateSceneData> createSceneData);

        /**
         * 查找用户的场景信息
         */
        void findUserSceneListInfo();

        /**
         * 查找用户的场景信息
         *
         * @param createFlag 是否需要创建场景
         */
        void findUserSceneListInfo(boolean createFlag);

        /**
         * 获取待摆放清单列表
         */
        void getPlacementList();

        /**
         * 删除待摆放清单列表
         *
         * @param quoteIds 清单产品id,多个用英文逗号分隔
         */
        void deletePlacementList(String quoteIds);

        /**
         * 获取用户保存的"报价"数据
         */
        void getQuotationData();

        /**
         * 更新用户场景信息列表的集合
         *
         * @param sceneInfoList 用户场景信息列表的集合
         */
        void updateSceneListInfo(List<SceneInfo> sceneInfoList);

        /**
         * 将某一个场景的"模拟搭配产品"数据添加到报价列表数据中
         *
         * @param sceneId 场景id(一般为创建该场景的时间戳)
         */
        void addQuotation(long sceneId);

        /**
         * 申请权限
         *
         * @param permissions 要申请的权限列表
         */
        void applyPermissions(String[] permissions);

        /**
         * 打开相机
         */
        void openCamera();

        /**
         * 打开相册
         */
        void openPhotoAlbum();

        /**
         * 获得Activity返回的数据
         *
         * @param requestCode 请求代码
         * @param resultCode  结果代码
         * @param data        返回的数据
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);

        /**
         * 显示修改场景名称的PopupWindow
         *
         * @param rootLayout    界面Root布局
         * @param sceneInfoList 用户场景信息列表的集合
         * @param position      位置
         */
        void displayEditSceneNamePopupWindow(View rootLayout, List<SceneInfo> sceneInfoList, int position);

        /**
         * 显示删除场景信息的Dialog
         *
         * @param sceneInfoList 用户场景信息列表的集合
         */
        void displayDeleteSceneNameDialog(List<SceneInfo> sceneInfoList);

        void createSceneOrPic(String desing_number, String type, String scheme_id,
                              String scheme_name, Map<String, RequestBody> pic, String product_sku_id);

        /**
         * 新创设计号和建场景信息
         */
        void addDesignAndSceneInfo();

        void addDesignAndSceneInfo(List<CreateSceneData> mCreateSceneDataList);

        /**
         * 场景添加图片产品
         *
         * @param designSchemeRequest 创建场景或场景添加图片产品实体类
         */
        void designScheme(DesignSchemeRequest designSchemeRequest);

        /**
         * 在创建场景或场景添加图片产品成功时回调
         *
         * @param designScheme 设计详情
         */
        void onGetDesignSchemeSuccess(DesignScheme designScheme);

        /**
         * 在场景添加图片产品成功时回调
         *
         * @param designScheme 设计详情
         */
        void onGetAddDesignSchemeSuccess(DesignScheme designScheme);
    }

    interface SceneModel {
        /**
         * 新创建场景信息
         *
         * @param createSceneData 创建场景时需要的数据
         * @param callBack        回调方法
         */
        void addSceneInfo(List<CreateSceneData> createSceneData, CallBack callBack);

        /**
         * 查找用户的场景信息
         *
         * @param callBack 回调方法
         */
        void findUserSceneListInfo(CallBack callBack);


        /**
         * 查找用户的场景信息
         *
         * @param callBack 回调方法
         */
        void findUserSceneListInfo(boolean createFlag, CallBack callBack);


        /**
         * 获取待摆放清单列表
         *
         * @param callBack 回调方法
         */
        void getPlacementList(CallBack callBack);

        /**
         * 删除待摆放清单列表
         *
         * @param quoteIds 清单产品id,多个用英文逗号分隔
         * @param callBack 回调方法
         */
        void deletePlacementList(String quoteIds, CallBack callBack);

        /**
         * 获取用户保存的"报价"数据
         *
         * @param callBack 回调方法
         */
        void getQuotationData(CallBack callBack);

        /**
         * 更新用户场景信息列表的集合
         *
         * @param sceneInfoList 用户场景信息列表的集合
         * @param callBack      回调方法
         */
        void updateSceneListInfo(List<SceneInfo> sceneInfoList, CallBack callBack);

        /**
         * 将某一个场景的"模拟搭配产品"数据添加到报价列表数据中
         *
         * @param sceneId  场景id(一般为创建该场景的时间戳)
         * @param callBack 回调方法
         */
        void addQuotation(long sceneId, CallBack callBack);

        /**
         * 删除场景信息
         *
         * @param sceneInfoList 用户场景信息列表的集合
         * @param callBack      回调方法
         */
        void deleteSceneListInfo(List<SceneInfo> sceneInfoList, CallBack callBack);

        void createSceneOrPic(String desing_number, String type, String scheme_id,
                              String scheme_name, Map<String, RequestBody> pic, String product_sku_id,
                              CallBack callback);

        /**
         * 新建设计号和场景
         *
         * @param callBack 回调方法
         */
        void addDesignAndSceneInfo(CallBack callBack);

        /**
         * 新建设计号和场景
         *
         * @param callBack 回调方法
         */
        void addDesignAndSceneInfo(List<CreateSceneData> mCreateSceneDataList, CallBack callBack);

        void createDesignScheme(String desingNumber, int type, String schemeId, String schemeName, MultipartBody.Part parts, String productSkuId, CallBack callBack);

        void designScheme(String desingNumber, int type, String schemeId, String schemeName, Map<String, RequestBody> params, String productSkuId,  CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 新创建场景信息的结果
         *
         * @param result true新创建成功,false新创建失败
         */
        void addSceneInfoResult(boolean result);

        /**
         * 在查找用户的场景信息成功时回调
         *
         * @param sceneInfoList 用户场景信息列表
         */
        void onFindUserSceneInfoSuccess(List<SceneInfo> sceneInfoList);

        /**
         * 在获取摆放清单列表成功时回调
         *
         * @param placementQrQuotationList 摆放清单列表
         */
        void onGetPlacementListSuccess(PlacementQrQuotationList placementQrQuotationList);

        /**
         * 在删除待摆放清单产品成功时回调
         *
         * @param deletePlacement 删除待摆放清单产品时服务器端返回的数据
         */
        void onDeletePlacementListSuccess(DeletePlacement deletePlacement);

        /**
         * 在获取用户保存的"报价"数据成功时回调
         *
         * @param quotationDataList 用户保存的"报价"数据列表
         */
        void onGetQuotationDataSuccess(List<QuotationData> quotationDataList);

        /**
         * 更新用户场景信息列表的集合的结果
         *
         * @param result true添加成功,false添加失败
         */
        void updateSceneListInfoResult(boolean result);

        /**
         * 将"模拟搭配产品"数据添加到报价列表数据中的结果
         *
         * @param result true添加成功,false添加失败
         */
        void addQuotationResult(boolean result);

        void onCreateSceneOrPic(CreateSceneOrPicModel model);
        /**
         * 在创建场景或场景添加图片产品成功时回调
         *
         * @param designScheme 设计详情
         */
        void onGetDesignSchemeSuccess(DesignScheme designScheme);

        /**
         * 在场景添加图片产品成功时回调
         *
         * @param designScheme 设计详情
         */
        void onGetAddDesignSchemeSuccess(DesignScheme designScheme);
    }
}
