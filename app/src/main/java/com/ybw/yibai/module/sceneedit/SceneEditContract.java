package com.ybw.yibai.module.sceneedit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jaygoo.widget.RangeSeekBar;
import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.AddQuotation;
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
import com.ybw.yibai.common.widget.HorizontalViewPager;
import com.ybw.yibai.common.widget.MatchLayout;
import com.ybw.yibai.common.widget.stickerview.BaseSticker;
import com.ybw.yibai.common.widget.stickerview.StickerView;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/9/23
 */
public interface SceneEditContract {

    interface SceneEditView extends BaseView {

        /**
         * 根据场景id查找用户保存的"模拟搭配产品"数据成功时回调
         *
         * @param simulationDataList 用户保存的"模拟搭配产品"数据
         */
        void onGetSimulationDataSuccess(List<SimulationData> simulationDataList);

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

        /**
         * 将场景中产品一键创建并导入服务器中成功时回调
         *
         * @param fastImport 将场景中产品一键创建并导入服务器中成功时服务器端返回的数据
         */
        void onFastImportSuccess(FastImport fastImport);

        /**
         * 根据大中小随机获取组合成功时回调
         *
         * @param specSuk 随机获取组合
         */
        void onGetSpecSukSuccess(SpecSuk specSuk);

        /**
         * 在获取相似类型sku列表成功时回调
         *
         * @param categorySimilarSKU 相似类型sku列表
         */
        void onGetCategorySimilarSKUListSuccess(CategorySimilarSKU categorySimilarSKU);

        /**
         * 换搭配获取植物/花盆列表成功时回调
         *
         * @param recommend 植物花盆列表
         */
        void onGetRecommendSuccess(Recommend recommend);

        void onGetNewRecommendSuccess(Recommend recommend);

        /**
         * 将"模拟搭配产品"数据添加到报价列表数据中的结果
         *
         * @param result true添加成功,false添加失败
         */
        void addQuotationDataResult(boolean result);

        /**
         * 添加产品数据到"模拟搭配产品"数据操作的结果
         *
         * @param result true操作成功,false操作失败
         */
        void onAddSimulationDataResult(boolean result);

        /**
         * 在sku加入待摆放清单成功时回调
         *
         * @param addQuotation sku加入待摆放清单时服务器端返回的数据
         */
        void onAddQuotationSuccess(AddQuotation addQuotation);

        /**
         * 在RangeSeekBar值发生变化时回调
         *
         * @param view  值发生变化的RangeSeekBar
         * @param value RangeSeekBar的值
         */
        void onRangeChanged(RangeSeekBar view, float value);

        /**
         * 在设置"搭配图片布局的容器"的位置成功时回调 -> 单产品
         *
         * @param viewPagerList 存放动态添加的"ViewPager"
         */
        void onSetSingleCollocationLayoutPositionSucceed(List<HorizontalViewPager> viewPagerList);

        /**
         * 在设置"搭配图片布局的容器"的位置成功时回调 -> 组合
         *
         * @param matchLayoutList 动态添加的"植物与盆器互相搭配的View"列表
         */
        void onSetGroupCollocationLayoutPositionSucceed(List<MatchLayout> matchLayoutList);

        void onAddParam(List<BTCBean> bean);

        /**
         * 在设置货源城市成功时回调
         *
         * @param userPosition 设置货源城市时服务器端返回的数据
         */
        void onSetUserPositionSuccess(UserPosition userPosition);

        void onGetDesignScenes(ResponsePage<ItemDesignSceneModel> page);

        void onCreateDesignResult(String number);

        void onGetDesignDetail(ItemDesignSceneModel model);

        void onCreateSceneOrPicCallback(CreateSceneOrPicModel model);

        void onGetSceneListCallback(List<SceneInfo> sceneInfos);

        void updateSceneBgSuccess();

    }

    interface SceneEditPresenter extends BasePresenter<SceneEditView> {

        /**
         * 根据场景id查找保存用户的"模拟搭配图片"数据
         *
         * @param sceneId 场景id
         */
        void getSimulationData(long sceneId);

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
         * 设置场景背景
         *
         * @param imageView 要设置场景背景的ImageView
         * @param path      要设置场景背景路径
         */
        void setBackgroundImage(ImageView imageView, String path);

        /**
         * 更新当前场景的信息
         *
         * @param sceneInfo 当前场景的信息
         */
        void updateSceneInfo(SceneInfo sceneInfo);

        /**
         * 显示场景背景编辑的PopupWindow
         *
         * @param rootLayout View根布局
         */
        void displaySceneBackgroundEditPopupWindow(View rootLayout);

        /**
         * 场景中产品一键创建并导入
         *
         * @param placeName         位置名称
         * @param file              位置场景图
         * @param alreadyPlacedList 已摆放"模拟搭配图片"数据
         */
        void fastImport(String placeName, File file, List<SimulationData> alreadyPlacedList);

        /**
         * 根据大中小随机获取组合
         *
         * @param type 规格大中小id
         */
        void getSpecSuk(int type);

        /**
         * 获取相似类型sku列表
         *
         * @param skuId 产品SKUid
         */
        void getCategorySimilarSKUList(int skuId);

        /**
         * 换搭配获取植物/花盆列表
         *
         * @param cateCode   类别:plant,获取植物 pot获取盆
         * @param plantSkuId 植物SkuId
         * @param potSkuId   盆SkuId
         * @param specType   大中小id(此参数仅在获取花盆时有效)
         * @param pCid       分类id(多个用逗号拼接)
         * @param param      动态设置的参数
         */
        void getRecommend(String cateCode, Integer plantSkuId, Integer potSkuId,
                          Integer specType, Integer pCid, Map<String, String> param);

        void getNewRecommend(String cateCode, Integer plantSkuId, Integer potSkuId,
                             Integer specType, Integer pCid, Map<String, String> param);

        /**
         * 显示植物信息的PopupWindow
         *
         * @param rootLayout     View根布局
         * @param simulationData "模拟搭配产品"数据
         */
        void displayPlantInfoPopupWindow(View rootLayout, SimulationData simulationData);

        /**
         * 将保存用户的"模拟搭配产品"数据里面的"模拟图片"添加相册
         *
         * @param simulationData "模拟搭配产品"数据
         */
        void joinPhotoAlbum(SimulationData simulationData);

        /**
         * 将"模拟搭配产品"数据添加到报价列表数据中
         *
         * @param simulationData "模拟搭配产品"数据
         */
        void addQuotationData(SimulationData simulationData);

        /**
         * 添加贴纸
         *
         * @param stickerView        贴纸View
         * @param simulationDataList 用户保存的"模拟搭配产品"数据
         */
        void addSticker(StickerView stickerView, List<SimulationData> simulationDataList);

        /**
         * 删除贴纸数据
         *
         * @param simulationData "模拟搭配产品"信息
         */
        void deleteSimulationData(SimulationData simulationData);

        /**
         * 添加贴纸数据
         *
         * @param simulationData "模拟搭配产品"信息
         */
        void addSimulationData(SimulationData simulationData);

        /**
         * 更新贴纸信息
         *
         * @param simulationData 位置发生改变的"模拟搭配产品"信息
         */
        void updateSimulationData(SimulationData simulationData);

        /**
         * 添加产品数据到"模拟搭配产品"数据
         *
         * @param listBean 产品数据
         */
        void addSimulationData(ListBean listBean);

        /**
         * 添加"随机组合"数据到"模拟搭配产品"数据
         *
         * @param data 用户点击植物规格获取的随机组合数据
         */
        void addSimulationData(SpecSuk.DataBean data);

        /**
         * 添加报价数据到"模拟搭配产品"数据
         *
         * @param quotationData 报价数据
         */
        void addSimulationData(QuotationData quotationData);

        /**
         * sku加入待摆放清单
         *
         * @param simulationData "模拟搭配产品"数据
         */
        void addQuotation(SimulationData simulationData);

        /**
         * 设置"搭配图片布局的容器"子View的位置
         *
         * @param collocationLayout        搭配图片布局的容器
         * @param simulationDataList       用户保存的"模拟搭配图片"数据
         * @param finallySkuId             当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
         * @param productCombinationType   主产品组合模式: 1单图模式,2搭配上部,3搭配下部
         * @param augmentedCombinationType 附加产品组合模式: 1单图模式,2搭配上部,3搭配下部
         */
        void setCollocationLayoutPosition(RelativeLayout collocationLayout, List<SimulationData> simulationDataList,
                                          String finallySkuId, int productCombinationType, int augmentedCombinationType);

        /**
         * /**
         * 设置"搭配图片布局的容器"子View的位置
         *
         * @param collocationLayout        搭配图片布局的容器
         * @param simulationDataList       用户保存的"模拟搭配图片"数据
         * @param finallySkuId             当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
         * @param productCombinationType   主产品组合模式: 1单图模式,2搭配上部,3搭配下部
         * @param augmentedCombinationType 附加产品组合模式: 1单图模式,2搭配上部,3搭配下部
         */
        void setCollocationLayoutPosition(BaseSticker currentSticker, RelativeLayout collocationLayout, List<SimulationData> simulationDataList,
                                          String finallySkuId, int productCombinationType, int augmentedCombinationType);

        /**
         * 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
         *
         * @param matchLayout    自定义植物与盆器互相搭配的View
         * @param plantViewPager 放置"植物自由搭配图"的ViewPager
         * @param potViewPager   放置"盆器自由搭配图"的ViewPager
         * @param plantHeight    用户当前选择的植物规格的高度
         * @param potHeight      用户当前选择的盆规格的高度
         */
        void setCollocationContentParams(MatchLayout matchLayout, HorizontalViewPager plantViewPager,
                                         HorizontalViewPager potViewPager, double plantHeight, double potHeight, double plantOffsetRatio, double potoffsetRatio);

        /**
         * 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
         *
         * @param matchLayout    自定义植物与盆器互相搭配的View
         * @param plantViewPager 放置"植物自由搭配图"的ViewPager
         * @param potViewPager   放置"盆器自由搭配图"的ViewPager
         * @param plantHeight    用户当前选择的植物规格的高度
         * @param potHeight      用户当前选择的盆规格的高度
         */
        void setCollocationContentPlantAndPot(MatchLayout matchLayout, HorizontalViewPager plantViewPager,
                                              HorizontalViewPager potViewPager,
                                              double plantHeight, double potHeight,
                                              double plantOffsetRatio, double potoffsetRatio,
                                              double productWidth, double augmentedProductWidth);

        /**
         * 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
         *
         * @param matchLayout    自定义植物与盆器互相搭配的View
         * @param plantViewPager 放置"植物自由搭配图"的ViewPager
         * @param potViewPager   放置"盆器自由搭配图"的ViewPager
         * @param plantHeight    用户当前选择的植物规格的高度
         * @param potHeight      用户当前选择的盆规格的高度
         */
        void setCollocationContent(MatchLayout matchLayout, HorizontalViewPager plantViewPager,
                                   HorizontalViewPager potViewPager, double plantHeight, double potHeight, double plantOffsetRatio, double potoffsetRatio);

        /**
         * 将新搭配的产品数据添加到"模拟搭配产品"数据
         *
         * @param productData        产品相关数据
         * @param simulationDataList 用户保存的"模拟搭配图片"数据
         * @param finallySkuId       当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
         * @param single             true 单图模式/false 上下搭配
         */
        void addSimulationData(ProductData productData, List<SimulationData> simulationDataList, String finallySkuId, boolean single);

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
        void displayProductCodePopupWindow(View v, String productPriceCode, String productTradePriceCode,
                                           String augmentedPriceCode, String augmentedTradePriceCode,
                                           double productPrice, double augmentedProductPrice);

        void getParams();

        void setUserPosition(String cityCode);

        void getDesignDetail(String number);

        void createDesign();

        /**
         * 创建场景或场景添加图片产品
         *
         * @param design_number  设计编号
         * @param type           1创建场景2添加图片和产品
         * @param scheme_id      场景id(type为2时必传)
         * @param scheme_name    场景名称(type为1时才有效,可不传)
         * @param pic            图片,单图表单提交模式(type为2时必传,为1时也可以一起传入,相当于一起创建)
         * @param product_sku_id 关联的产品sku_id(如果是组合产品格式为:主id,副id)
         */
        void CreateSceneOrPic(String design_number, String type, String scheme_id,
                              String scheme_name, Map<String, RequestBody> pic, String product_sku_id);

        void getSceneList();

        /**
         * 点击新增按钮时获取搭配列表
         *
         * @param specTypeId 只传盆栽规格类型
         */
        void getNewRecommedByAddSpec(int specTypeId);

        /**
         * 点击改款
         */
        void getNewRecommedChangeStyle(int productSkuId, int augmentedProductSkuId);

        /**
         * 根据刷选条件获取新的推荐搭配
         */
        void getNewRecommed(String cateCode, int productSkuId, int augmentedProductSkuId, int specTypeId, List<String> specidList, List<String> attridList, Map<String, String> plantParamMap);

        /**
         * 更新背景图片
         */
        void updateSceneBg(String absolutePath, String sceneName, String scheme_id);

        void addQuotation(ProductData productData);

        void addQuotationData(ProductData productData);
    }

    interface SceneEditModel {

        /**
         * 根据场景id查找保存用户的"模拟搭配产品"数据
         *
         * @param sceneId  场景id
         * @param callBack 回调方法
         */
        void getSimulationData(long sceneId, CallBack callBack);

        /**
         * 更新当前场景的信息
         *
         * @param sceneInfo 当前场景的信息
         * @param callBack  回调方法
         */
        void updateSceneInfo(SceneInfo sceneInfo, CallBack callBack);

        /**
         * 场景中产品一键创建并导入
         *
         * @param placeName  位置名称
         * @param jsonString 产品数据(jsonObj)格式
         * @param params     位置场景图,单图模式
         * @param parts      产品组合图,多图模式(注意不要打乱顺序)
         * @param callBack   回调方法
         */
        void fastImport(String placeName, String jsonString, Map<String, RequestBody> params, MultipartBody.Part[] parts, CallBack callBack);

        /**
         * 根据大中小随机获取组合
         *
         * @param type     规格大中小id
         * @param callBack 回调方法
         */
        void getSpecSuk(int type, CallBack callBack);

        /**
         * 获取相似类型sku列表
         *
         * @param skuId    产品SKUid
         * @param callBack 回调方法
         */
        void getCategorySimilarSKUList(int skuId, CallBack callBack);

        /**
         * 换搭配获取植物花盆列表
         *
         * @param cateCode   类别:plant,获取植物 pot获取盆
         * @param plantSkuId 植物SkuId
         * @param potSkuId   盆SkuId
         * @param specType   大中小id(此参数仅在获取花盆时有效)
         * @param pCid       分类id(多个用逗号拼接)
         * @param param      动态设置的参数
         * @param callBack   回调方法
         */
        void getRecommend(String cateCode, Integer plantSkuId, Integer potSkuId, Integer specType,
                          Integer pCid, Map<String, String> param, CallBack callBack);

        void getNewRecommend(String cateCode, Integer plantSkuId, Integer potSkuId, Integer specType,
                             Integer pCid, String params, Map<String, String> param, CallBack callBack);

        /**
         * 将"模拟搭配产品"数据添加到报价列表数据中
         *
         * @param simulationData "模拟搭配产品"数据
         * @param callBack       回调方法
         */
        void addQuotationData(SimulationData simulationData, CallBack callBack);

        /**
         * 删除贴纸信息
         *
         * @param simulationData "模拟搭配产品"信息
         * @param callBack       回调方法
         */
        void deleteSimulationData(SimulationData simulationData, CallBack callBack);

        /**
         * 添加贴纸信息
         *
         * @param simulationData "模拟搭配产品"信息
         * @param callBack       回调方法
         */
        void addSimulationData(SimulationData simulationData, CallBack callBack);

        /**
         * 更新贴纸信息
         *
         * @param simulationData 位置发生改变的"模拟搭配产品"信息
         * @param callBack       回调方法
         */
        void updateSimulationData(SimulationData simulationData, CallBack callBack);

        /**
         * 添加产品数据到"模拟搭配产品"数据
         *
         * @param listBean 产品数据
         * @param bitmap   产品"模拟搭图片"
         * @param callBack 回调方法
         */
        void addSimulationData(ListBean listBean, Bitmap bitmap, CallBack callBack);

        /**
         * 添加"随机组合"数据到"模拟搭配产品"数据
         *
         * @param data     产品数据
         * @param bitmap   产品"模拟搭图片"
         * @param callBack 回调方法
         */
        void addSimulationData(SpecSuk.DataBean data, Bitmap bitmap, CallBack callBack);

        /**
         * 添加"随机组合"数据到"模拟搭配产品"数据
         *
         * @param data     产品数据
         * @param bitmap   产品"模拟搭图片"
         * @param callBack 回调方法
         */
        void addSimulationData(Recommend.DataBean data, Bitmap bitmap, CallBack callBack);

        /**
         * sku加入待摆放清单
         *
         * @param firstSkuId  主产品 skuId
         * @param secondSkuId 附加产品 skuId
         * @param params      组合图片
         * @param callBack    回调方法
         */
        void addQuotation(int firstSkuId, int secondSkuId, Map<String, RequestBody> params, CallBack callBack);

        /**
         * 添加报价数据到"模拟搭配产品"数据
         *
         * @param quotationData 报价数据
         * @param bitmap        产品"模拟搭图片"
         * @param callBack      回调方法
         */
        void addSimulationData(QuotationData quotationData, Bitmap bitmap, CallBack callBack);

        /**
         * 将新搭配的产品数据添加到"模拟搭配产品"数据
         *
         * @param productData        产品相关数据
         * @param simulationDataList 用户保存的"模拟搭配图片"数据
         * @param finallySkuId       当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
         * @param single             true 单图模式/false 上下搭配
         * @param bitmap             产品"模拟搭图片"
         * @param callBack           回调方法
         */
        void addSimulationData(ProductData productData, List<SimulationData> simulationDataList,
                               String finallySkuId, boolean single, Bitmap bitmap, CallBack callBack);

        void addParam(CallBack callBack);

        /**
         * 设置货源城市
         *
         * @param position 城市字符串（广东省广州市）
         * @param callBack 回调方法
         */
        void setUserPosition(String position, CallBack callBack);

        void getDesignDetail(String number, CallBack callback);

        void createDesign(CallBack callBack);

        void createSceneOrPic(String desing_number, String type, String scheme_id,
                              String scheme_name, Map<String, RequestBody> pic, String product_sku_id,
                              CallBack callback);

        void getSceneList(CallBack callBack);

        /**
         * 点击新增按钮时获取搭配列表
         *
         * @param specTypeId 只传盆栽规格类型
         */
        void getNewRecommedByAddSpec(int specTypeId, CallBack callBack);

        /**
         * 点击改款获取搭配列表
         */
        void getNewRecommedChangeStyle(int productSkuId, int augmentedProductSkuId, CallBack callBack);

        /**
         * 点击下方的二级分类获取搭配数据
         */
        void getNewRecommed(String cateCode, int productSkuId, int augmentedProductSkuId, int specTypeId, String specid,
                            String attrid, Map<String, String> plantParamMap, CallBack callBack);

        /**
         * 更新背景图片
         */
        void updateSceneBg(String absolutePath, String sceneName, String scheme_id, CallBack callBack);

        void addQuotationData(int productSkuId, int augmentedProductSkuId, Map<String, RequestBody> params, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 根据场景id查找用户保存的"模拟搭配产品"数据成功时回调
         *
         * @param simulationDataList 用户保存的"模拟搭配产品"数据
         */
        void onGetSimulationDataSuccess(List<SimulationData> simulationDataList);

        /**
         * 将场景中产品一键创建并导入服务器中成功时回调
         *
         * @param fastImport 将场景中产品一键创建并导入服务器中成功时服务器端返回的数据
         */
        void onFastImportSuccess(FastImport fastImport);

        /**
         * 根据大中小随机获取组合成功时回调
         *
         * @param specSuk 随机获取组合
         */
        void onGetSpecSukSuccess(SpecSuk specSuk);

        /**
         * 在获取相似类型sku列表成功时回调
         *
         * @param categorySimilarSKU 相似类型sku列表
         */
        void onGetCategorySimilarSKUListSuccess(CategorySimilarSKU categorySimilarSKU);

        /**
         * 在换搭配获取植物/花盆列表成功时回调
         *
         * @param recommend 植物花盆列表
         */
        void onGetRecommendSuccess(Recommend recommend);

        void onGetNewRecommendSuccess(Recommend model);

        /**
         * 将"模拟搭配产品"数据添加到报价列表数据中的结果
         *
         * @param result true添加成功,false添加失败
         */
        void addQuotationDataResult(boolean result);

        /**
         * 添加产品数据到"模拟搭配产品"数据操作的结果
         *
         * @param result true操作成功,false操作失败
         */
        void onAddSimulationDataResult(boolean result);

        /**
         * 在sku加入待摆放清单成功时回调
         *
         * @param addQuotation sku加入待摆放清单时服务器端返回的数据
         */
        void onAddQuotationSuccess(AddQuotation addQuotation);

        void onAddParam(List<BTCBean> bean);

        /**
         * 在设置货源城市成功时回调
         *
         * @param userPosition 设置货源城市时服务器端返回的数据
         */
        void onSetUserPositionSuccess(UserPosition userPosition);

        void onGetDesignSceneCallback(ResponsePage<ItemDesignSceneModel> page);

        void onGetDesignDetailCallback(ItemDesignSceneModel model);

        void onCreateDesignCallback(CreateDesignModel model);

        void onCreateSceneOrPic(CreateSceneOrPicModel model);

        void onGetSceneInfo(List<SceneInfo> sceneInfos);

        /**
         * 只传规格时的返回
         */
        void onGetRecommendByAddSpecSuccess(Recommend recommend);

        void updateSceneBgSuccess();

    }
}
