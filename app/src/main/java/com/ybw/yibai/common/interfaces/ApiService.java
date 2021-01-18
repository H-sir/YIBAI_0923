package com.ybw.yibai.common.interfaces;

import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.AddQuotationLocation;
import com.ybw.yibai.common.bean.Address;
import com.ybw.yibai.common.bean.AppUpdate;
import com.ybw.yibai.common.bean.BTCBean;
import com.ybw.yibai.common.bean.BindingPhone;
import com.ybw.yibai.common.bean.BindingWechat;
import com.ybw.yibai.common.bean.Case;
import com.ybw.yibai.common.bean.CaseClassify;
import com.ybw.yibai.common.bean.CategorySimilarSKU;
import com.ybw.yibai.common.bean.CheckShareBean;
import com.ybw.yibai.common.bean.CityAreaList;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.CreateCustomers;
import com.ybw.yibai.common.bean.CreateQuotationLocation;
import com.ybw.yibai.common.bean.CreateQuotationOrder;
import com.ybw.yibai.common.bean.CustomerList;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.DeleteCustomer;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.bean.DeleteQuotationLocation;
import com.ybw.yibai.common.bean.DeleteShippingAddress;
import com.ybw.yibai.common.bean.DesignCreate;
import com.ybw.yibai.common.bean.DesignDetails;
import com.ybw.yibai.common.bean.DesignList;
import com.ybw.yibai.common.bean.DesignScheme;
import com.ybw.yibai.common.bean.EditCustomer;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.bean.FastImport;
import com.ybw.yibai.common.bean.FeedBack;
import com.ybw.yibai.common.bean.HotScheme;
import com.ybw.yibai.common.bean.HotSchemeCategory;
import com.ybw.yibai.common.bean.HotSchemes;
import com.ybw.yibai.common.bean.LoginInfo;
import com.ybw.yibai.common.bean.ModifyPassword;
import com.ybw.yibai.common.bean.PlaceBean;
import com.ybw.yibai.common.bean.PlacementQrQuotationList;
import com.ybw.yibai.common.bean.ProductDetails;
import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.bean.PurchaseOrder;
import com.ybw.yibai.common.bean.PurchaseOrderList;
import com.ybw.yibai.common.bean.QuotationAgain;
import com.ybw.yibai.common.bean.QuotationList;
import com.ybw.yibai.common.bean.QuotationLocation;
import com.ybw.yibai.common.bean.Recommend;
import com.ybw.yibai.common.bean.RecommendBean;
import com.ybw.yibai.common.bean.RecommendProduct;
import com.ybw.yibai.common.bean.RecommendProductList;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.bean.SetIncrease;
import com.ybw.yibai.common.bean.ShippingAddress;
import com.ybw.yibai.common.bean.SimilarPlantSKU;
import com.ybw.yibai.common.bean.SimilarSKU;
import com.ybw.yibai.common.bean.SkuMarketBean;
import com.ybw.yibai.common.bean.SpecSuk;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.bean.UpdateCompany;
import com.ybw.yibai.common.bean.UpdateQuotationInfo;
import com.ybw.yibai.common.bean.UpdateQuotationLocation;
import com.ybw.yibai.common.bean.UpdateQuotationPrice;
import com.ybw.yibai.common.bean.UpdateSKUUseState;
import com.ybw.yibai.common.bean.UpdateWatermark;
import com.ybw.yibai.common.bean.UserInfo;
import com.ybw.yibai.common.bean.UserPosition;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.bean.VerifyPassword;
import com.ybw.yibai.common.bean.VloeaBean;
import com.ybw.yibai.common.bean.WeChatBindingPhone;
import com.ybw.yibai.common.constants.HttpUrls;
import com.ybw.yibai.common.model.CreateDesignModel;
import com.ybw.yibai.common.model.CreateSceneOrPicModel;
import com.ybw.yibai.common.model.ItemDesignSceneModel;
import com.ybw.yibai.common.network.response.BaseResponse;
import com.ybw.yibai.common.network.response.CurAddrResponse;
import com.ybw.yibai.common.network.response.HotCityResponse;
import com.ybw.yibai.common.network.response.ResponsePage;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import static com.ybw.yibai.common.constants.HttpUrls.GET_CITY_PLACE_URL;

/**
 * 这个接口存放所有联网的方法
 *
 * @author sjl
 */
public interface ApiService {

    /**
     * 获取短信验证码
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param mobile    手机号
     * @return 获取短信验证码时服务器端返回的数据
     */
    @GET(HttpUrls.GET_CODE_URL)
    Observable<VerificationCode> getVerificationCode(@Header("timestamp") String timestamp,
                                                     @Header("sign") String sign,
                                                     @Query("mobile") String mobile);

    /**
     * 用户登录
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param username  账号
     * @param password  密码
     * @param isRandom  是否是验证码登陆(1是0否)
     * @return 登陆时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.LOGIN_URL)
    Observable<LoginInfo> login(@Header("timestamp") String timestamp,
                                @Header("sign") String sign,
                                @Field("username") String username,
                                @Field("password") String password,
                                @Field("israndom") int isRandom);

    /**
     * 微信登录
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param code      微信给的code
     * @return 微信登录时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.WECHAT_LOGIN_URL)
    Observable<LoginInfo> wechatLogin(@Header("timestamp") String timestamp,
                                      @Header("sign") String sign,
                                      @Field("code") String code);

    /**
     * 微信绑定手机
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param mobile    手机号码
     * @param code      微信给的code
     * @param unionId   当且仅当该移动应用已获得该用户的 userinfo 授权时,才会出现该字段
     * @param openid    授权用户唯一标识
     * @return 微信绑定手机时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.WECHAT_BIND_MOBILE_PHONE_URL)
    Observable<WeChatBindingPhone> weChatBindingPhone(@Header("timestamp") String timestamp,
                                                      @Header("sign") String sign,
                                                      @Field("mobile") String mobile,
                                                      @Field("code") String code,
                                                      @Field("unionid") String unionId,
                                                      @Field("openid") String openid);

    /**
     * 获取用户信息
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return 在获取用户信息时服务器端返回的数据
     */
    @GET(HttpUrls.GET_USER_INFO_URL)
    Observable<UserInfo> getUserInfo(@Header("timestamp") String timestamp,
                                     @Header("sign") String sign,
                                     @Query("uid") int uid);

    /**
     * 获取系统参数
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return 在获取系统参数时服务器端返回的数据
     */
    @GET(HttpUrls.GET_SYSTEM_PARAMETER_URL)
    Observable<SystemParameter> getSystemParameter(@Header("timestamp") String timestamp,
                                                   @Header("sign") String sign,
                                                   @Query("uid") int uid);

    /**
     * 修改用户公司信息
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param name      公司名称
     * @param details   描述
     * @return 修改用户公司信息时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.UPDATE_COMPANY_INFO_URL)
    Observable<UpdateCompany> updateCompany(@Header("timestamp") String timestamp,
                                            @Header("sign") String sign,
                                            @Part("uid") int uid,
                                            @Part("name") String name,
                                            @Part("details") String details);

    /**
     * 修改用户公司信息
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param name      公司名称
     * @param params    公司logo/介绍图
     * @param details   描述
     * @return 修改用户公司信息时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.UPDATE_COMPANY_INFO_URL)
    Observable<UpdateCompany> updateCompany(@Header("timestamp") String timestamp,
                                            @Header("sign") String sign,
                                            @Part("uid") int uid,
                                            @Part("name") String name,
                                            @PartMap Map<String, RequestBody> params,
                                            @Part("details") String details);

    /**
     * 修改用户公司信息
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param name      公司名称
     * @param param     公司logo
     * @param params    介绍图
     * @param details   描述
     * @return 修改用户公司信息时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.UPDATE_COMPANY_INFO_URL)
    Observable<UpdateCompany> updateCompany(@Header("timestamp") String timestamp,
                                            @Header("sign") String sign,
                                            @Part("uid") int uid,
                                            @Part("name") String name,
                                            @PartMap Map<String, RequestBody> param,
                                            @PartMap Map<String, RequestBody> params,
                                            @Part("details") String details);

    /**
     * 设置货源城市
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param code      城市代码
     * @return 设置货源城市时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.SET_USER_POSITION_URL)
    Observable<UserPosition> setUserPosition(@Header("timestamp") String timestamp,
                                             @Header("sign") String sign,
                                             @Field("uid") int uid,
                                             @Field("code") String code);

    @GET("?method=mybw.getdesignlist")
    Observable<BaseResponse<ResponsePage<ItemDesignSceneModel>>> getDesignScenes(
            @Header("timestamp") String timestamp,
            @Header("sign") String sign,
            @Query("uid") String uid,
            @Query("page") String page,
            @Query("pagesize") String pagesize
    );

    // 获取设计详情
    @GET("?method=mybw.getdesigninfo")
    Observable<BaseResponse<ItemDesignSceneModel>> getDesignInfo(
            @Header("timestamp") String timestamp,
            @Header("sign") String sign,
            @Query("uid") String uid,
            @Query("number") String number
    );

    /**
     * 删除设计
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param number    设计号
     * @return 删除设计时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.DELETE_DESIGN_URL)
    Observable<BaseBean> deleteDesign(@Header("timestamp") String timestamp,
                                      @Header("sign") String sign,
                                      @Field("uid") int uid,
                                      @Field("number") String number);


    @FormUrlEncoded
    @POST("?method=mybw.deldesign")
    Observable<BaseResponse<Object>> deleteDesign(
            @Header("timestamp") String timestamp,
            @Header("sign") String sign,
            @Field("uid") String uid,
            @Field("number") String number
    );

    @FormUrlEncoded
    @POST("?method=mybw.createdesign")
    Observable<BaseResponse<CreateDesignModel>> createDesign(
            @Header("timestamp") String timestamp,
            @Header("sign") String sign,
            @Field("uid") String uid
    );

    /**
     * 创建设计的方法
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return 设计编号
     */
    @POST(HttpUrls.CREATE_DESIGN_URL)
    Observable<DesignCreate> createDesign(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Query("uid") int uid);

    /**
     * 14.4.	创建场景或场景添加图片产品
     *
     * @param timestamp    时间搓
     * @param sign         签名
     * @param uid          用户的ID
     * @param desingNumber 设计编号
     * @param type         1创建场景2添加图片和产品
     * @param schemeName   场景名称(type为1时才有效,可不传)
     * @return 设计编号
     */
    @Multipart
    @POST(HttpUrls.DESIGN_SCHEME_URL)
    Observable<DesignScheme> designNewScheme(@Header("timestamp") String timestamp,
                                             @Header("sign") String sign,
                                             @Part("uid") int uid,
                                             @Part("desing_number") String desingNumber,
                                             @Part("type") int type,
                                             @Part("scheme_name") String schemeName);

    /**
     * 14.4.	创建场景或场景添加图片产品
     *
     * @param timestamp    时间搓
     * @param sign         签名
     * @param uid          用户的ID
     * @param desingNumber 设计编号
     * @param type         1创建场景2添加图片和产品
     * @param schemeName   场景名称(type为1时才有效,可不传)
     * @return 设计编号
     */
    @Multipart
    @POST(HttpUrls.DESIGN_SCHEME_URL)
    Observable<DesignScheme> designNewScheme(@Header("timestamp") String timestamp,
                                             @Header("sign") String sign,
                                             @Part("uid") int uid,
                                             @Part("desing_number") String desingNumber,
                                             @Part("type") int type,
                                             @Part("scheme_name") String schemeName,
                                             @Part MultipartBody.Part parts
    );

    /**
     * 查询设计分享的权限
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     */
    @Multipart
    @POST(HttpUrls.CHECK_SHARE_URL)
    Observable<CheckShareBean> checkShare(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Part("uid") int uid);


    /**
     * 14.4.	创建场景或场景添加图片产品
     *
     * @param timestamp    时间搓
     * @param sign         签名
     * @param uid          用户的ID
     * @param desingNumber 设计编号
     * @param type         1创建场景2添加图片和产品
     * @param schemeName   场景名称(type为1时才有效,可不传)
     * @param parts        图片,单图表单提交模式formdata(type为2时必传,为1时也可以一起传入,相当于一起创建)
     * @return 设计编号
     */
    @Multipart
    @POST(HttpUrls.DESIGN_SCHEME_URL)
    Observable<DesignScheme> designScheme(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Part("uid") int uid,
                                          @Part("desing_number") String desingNumber,
                                          @Part("type") int type,
                                          @Part("scheme_name") String schemeName,
                                          @Part MultipartBody.Part parts);

    /**
     * 14.4.	修改场景信息
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param name      场景名称
     * @param scheme_id 场景ID
     * @param parts     图片,单图表单提交模式formdata(type为2时必传,为1时也可以一起传入,相当于一起创建)
     * @return 设计编号
     */
    @Multipart
    @POST(HttpUrls.EDIT_SCHEME_URL)
    Observable<BaseBean> editScheme(@Header("timestamp") String timestamp,
                                    @Header("sign") String sign,
                                    @Part("uid") int uid,
                                    @Part("name") String name,
                                    @Part("scheme_id") String scheme_id,
                                    @Part MultipartBody.Part parts);

    /**
     * 14.4.	修改场景信息
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param name      场景名称
     * @param scheme_id 场景ID
     * @return 设计编号
     */
    @Multipart
    @POST(HttpUrls.EDIT_SCHEME_URL)
    Observable<BaseBean> editScheme(@Header("timestamp") String timestamp,
                                    @Header("sign") String sign,
                                    @Part("uid") int uid,
                                    @Part("name") String name,
                                    @Part("scheme_id") String scheme_id);


    /**
     * 14.4.	创建场景或场景添加图片产品
     *
     * @param timestamp    时间搓
     * @param sign         签名
     * @param uid          用户的ID
     * @param desingNumber 设计编号
     * @param type         1创建场景2添加图片和产品
     * @param schemeId     场景id(type为2时必传)
     * @param productSkuId 关联的产品sku_id(如果是组合产品格式为:主id,副id)
     * @return 设计编号
     */
    @Multipart
    @POST(HttpUrls.DESIGN_SCHEME_URL)
    Observable<DesignScheme> designScheme(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Part("uid") int uid,
                                          @Part("desing_number") String desingNumber,
                                          @Part("type") int type,
                                          @Part("scheme_id") String schemeId,
                                          @PartMap Map<String, RequestBody> params,
                                          @Part("product_sku_id") String productSkuId);

    /**
     * 14.4 创建场景或场景添加图片产品
     *
     * @param desing_number  设计编号
     * @param type           1创建场景2添加图片和产品
     * @param scheme_id      场景id(type为2时必传)
     * @param scheme_name    场景名称(type为1时才有效,可不传)
     * @param pic            图片,单图表单提交模式(type为2时必传,为1时也可以一起传入,相当于一起创建)
     * @param product_sku_id 关联的产品sku_id(如果是组合产品格式为:主id,副id)
     * @return 数据
     */
    @Multipart
    @POST("?method=mybw.designscheme")
    Observable<BaseResponse<CreateSceneOrPicModel>> createSceneOrPic(
            @Header("sign") String sign, @Header("timestamp") String timestamp,
            @Part("uid") String uid, @Part("desing_number") String desing_number,
            @Part("type") String type, @Part("scheme_id") String scheme_id, @Part("scheme_name") String scheme_name,
            @PartMap Map<String, RequestBody> pic, @Part("product_sku_id") String product_sku_id
    );

    /**
     * 获取设计列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return 获取产品sku列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_DESIGN_LIST_URL)
    Observable<DesignList> getDesignList(@Header("timestamp") String timestamp,
                                         @Header("sign") String sign,
                                         @Query("uid") int uid);

    /**
     * 获取多货源
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param sku_id    设计变化
     * @return 获取产品详情时服务器端返回的数据
     */
    @GET(HttpUrls.GET_SKU_MARKET_URL)
    Observable<SkuMarketBean> getSkuMarket(@Header("timestamp") String timestamp,
                                           @Header("sign") String sign,
                                           @Query("uid") int uid,
                                           @Query("sku_id") int sku_id);

    /**
     * 获取方案分类
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return 在获取方案分类时服务器端返回的数据
     */
    @GET(HttpUrls.GET_HOT_SCHEME_CATEGORY_URL)
    Observable<HotSchemeCategory> getHotSchemeCategory(@Header("timestamp") String timestamp,
                                                       @Header("sign") String sign,
                                                       @Query("uid") int uid);

    /**
     * 获取热门方案列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cateId    方案分类id
     * @return 在获取方案列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_HOT_SCHEME_URL)
    Observable<HotScheme> getHotScheme(@Header("timestamp") String timestamp,
                                       @Header("sign") String sign,
                                       @Query("uid") int uid,
                                       @Query("cateid") int cateId);

    /**
     * 获取新热门方案列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cateId    方案分类id
     * @return 在获取方案列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_HOT_SCHEMES_URL)
    Observable<HotSchemes> getHotSchemes(@Header("timestamp") String timestamp,
                                         @Header("sign") String sign,
                                         @Query("uid") int uid,
                                         @Query("cateid") int cateId);

    /**
     * 获取推荐产品列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return 推荐产品列表
     */
    @GET(HttpUrls.GET_RECOMMEND_LIST_URL)
    Observable<RecommendProductList> getRecommendProductList(@Header("timestamp") String timestamp,
                                                             @Header("sign") String sign,
                                                             @Query("uid") int uid);

    /**
     * 获取公司案例分类
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return 获取公司案例分类时服务器端返回的数据
     */
    @GET(HttpUrls.GET_CASE_CLASSIFY_URL)
    Observable<CaseClassify> getCaseClassify(@Header("timestamp") String timestamp,
                                             @Header("sign") String sign,
                                             @Query("uid") int uid);

    /**
     * 获取公司案例详情
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cateId    案例分类id(用于筛选,不传则调取所有)
     * @return 获取公司案例时服务器端返回的数据
     */
    @GET(HttpUrls.GET_CASE_URL)
    Observable<Case> getCase(@Header("timestamp") String timestamp,
                             @Header("sign") String sign,
                             @Query("uid") int uid,
                             @Query("cateid") Integer cateId);

    /**
     * 获取产品筛选参数
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return 获取产品筛选参数时服务器端返回的数据
     */
    @GET(HttpUrls.GET_PRODUCT_SCREENING_PARAM_URL)
    Observable<ProductScreeningParam> getProductScreeningParam(@Header("timestamp") String timestamp,
                                                               @Header("sign") String sign,
                                                               @Query("uid") int uid);

    /**
     * 获取产品筛选参数
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return 获取产品筛选参数时服务器端返回的数据
     */
    @GET("?method=mybw.getparam")
    Observable<BaseResponse<List<BTCBean>>> getParams(@Header("timestamp") String timestamp,
                                                      @Header("sign") String sign,
                                                      @Query("uid") int uid);

    /**
     * 获取设计详情
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param number    设计变化
     * @return 获取产品详情时服务器端返回的数据
     */
    @GET(HttpUrls.GET_DESIGN_INFO_URL)
    Observable<DesignDetails> getDesignDetails(@Header("timestamp") String timestamp,
                                               @Header("sign") String sign,
                                               @Query("uid") int uid,
                                               @Query("number") String number);

    /**
     * 删除设计
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param picId     图片ID
     * @return 删除设计时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.DELETE_DESIGN_SCHEME_PIC_URL)
    Observable<BaseBean> deleteDesignSchemePic(@Header("timestamp") String timestamp,
                                               @Header("sign") String sign,
                                               @Field("uid") int uid,
                                               @Field("id") String picId);

    /**
     * 删除设计
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param schemeId  场景ID
     * @return 删除设计时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.DELETE_DESIGN_SCHEME_URL)
    Observable<BaseBean> deleteDesignScheme(@Header("timestamp") String timestamp,
                                            @Header("sign") String sign,
                                            @Field("uid") int uid,
                                            @Field("scheme_id") String schemeId);

    /**
     * 获取产品sku列表
     *
     * @param timestamp   时间搓
     * @param sign        签名
     * @param uid         用户的ID
     * @param isAll       是否获取全部:1获取全部0分页(默认为0分页)
     * @param cateCode    产品类别(产品筛选参数大类别名)默认获取植物
     * @param useState    使用状态
     * @param pcId        产品类别id(多个用逗号拼接)
     * @param curDiameter 当前口径:搭配口径+边距*2的值
     * @param diameter    搭配口径:格式同上
     * @param height      搭配高度
     * @param keyword     关键词搜索
     * @return 获取产品sku列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_SUK_LIST_URL)
    Observable<SKUList> getSKUList(@Header("timestamp") String timestamp,
                                   @Header("sign") String sign,
                                   @Query("uid") int uid,
                                   @Query("isall") int isAll,
                                   @Query("cate_code") String cateCode,
                                   @Query("usestate") Integer useState,
                                   @Query("pcid") String pcId,
                                   @Query("curdiameter") Double curDiameter,
                                   @Query("diameter") Double diameter,
                                   @Query("height") String height,
                                   @Query("keyword") String keyword,
                                   @Query("page") Integer page,
                                   @Query("pagesize") Integer pageSize);

    /**
     * 获取产品sku列表
     *
     * @param timestamp   时间搓
     * @param sign        签名
     * @param uid         用户的ID
     * @param isAll       是否获取全部:1获取全部0分页(默认为0分页)
     * @param cateCode    产品类别(产品筛选参数大类别名)默认获取植物
     * @param useState    使用状态
     * @param pcId        产品类别id(多个用逗号拼接)
     * @param curDiameter 当前口径:搭配口径+边距*2的值
     * @param diameter    搭配口径:格式同上
     * @param height      搭配高度
     * @param keyword     关键词搜索
     * @return 获取产品sku列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_SUK_LIST_URL)
    Observable<SKUList> getSKUList(@Header("timestamp") String timestamp,
                                   @Header("sign") String sign,
                                   @Query("uid") int uid,
                                   @Query("isall") int isAll,
                                   @Query("cate_code") String cateCode,
                                   @Query("usestate") Integer useState,
                                   @Query("pcid") String pcId,
                                   @Query("curdiameter") Double curDiameter,
                                   @Query("diameter") Double diameter,
                                   @Query("height") String height,
                                   @Query("keyword") String keyword,
                                   @Query("page") Integer page,
                                   @Query("pagesize") Integer pageSize,
                                   @Query("searchcate") Integer searchcate,
                                   @Query("searchmodel") Integer searchmodel);

    /**
     * 获取产品sku列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param isAll     是否获取全部:1获取全部0分页(默认为0分页)
     * @param cateCode  产品类别(产品筛选参数大类别名)默认获取植物
     * @param useState  使用状态
     * @param pcId      产品类别id(多个用逗号拼接)
     * @param keyword   关键词搜索
     * @param param     动态设置的参数
     * @return 获取产品sku列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_SUK_LIST_URL)
    Observable<SKUList> getSKUList(@Header("timestamp") String timestamp,
                                   @Header("sign") String sign,
                                   @Query("uid") int uid,
                                   @Query("isall") int isAll,
                                   @Query("cate_code") String cateCode,
                                   @Query("usestate") Integer useState,
                                   @Query("pcid") String pcId,
                                   @Query("keyword") String keyword,
                                   @QueryMap Map<String, String> param,
                                   @Query("page") Integer page,
                                   @Query("pagesize") Integer pageSize);

    /**
     * 获取产品详情
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param productId 产品ID
     * @return 获取产品详情时服务器端返回的数据
     */
    @GET(HttpUrls.GET_PRODUCT_INFO_URL)
    Observable<ProductDetails> getProductDetails(@Header("timestamp") String timestamp,
                                                 @Header("sign") String sign,
                                                 @Query("uid") int uid,
                                                 @Query("product_id") int productId);


    /**
     * 场景中产品一键创建并导入
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cid       客户的id
     * @param placeName 位置名称
     * @param item      产品数据(jsonObj)格式
     * @param params    位置场景图,单图模式
     * @param parts     产品组合图,多图模式(注意不要打乱顺序)
     * @return 场景中产品一键创建并导入时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.FAST_IMPORT_URL)
    Observable<FastImport> fastImport(@Header("timestamp") String timestamp,
                                      @Header("sign") String sign,
                                      @Part("uid") int uid,
                                      @Part("cid") int cid,
                                      @Part("place_name") String placeName,
                                      @Part("item") String item,
                                      @PartMap Map<String, RequestBody> params,
                                      @Part MultipartBody.Part[] parts);

    /**
     * 添加商品到进货列表
     *
     * @param timestamp           时间搓
     * @param sign                签名
     * @param uid                 用户的ID
     * @param firstSkuId          主skuid
     * @param secondSkuId         附加产品skuid
     * @param firstGateProductId  主产品档口产品id(货源中获取)
     * @param secondGateProductId 附加产品档口产品id 附加产品skuid存在时此为必填项
     * @param params              组合图片,单图表单提交formdata 模式   附加产品skuid存在时此为必填项
     * @return 场景中产品一键创建并导入时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.ADD_PURCART_URL)
    Observable<BaseBean> addPurcart(@Header("timestamp") String timestamp,
                                    @Header("sign") String sign,
                                    @Part("uid") int uid,
                                    @Part("first_sku_id") int firstSkuId,
                                    @Part("second_sku_id") int secondSkuId,
                                    @Part("first_gate_product_id") int firstGateProductId,
                                    @Part("second_gate_product_id") int secondGateProductId,
                                    @PartMap Map<String, RequestBody> params);

    /**
     * 添加商品到进货列表
     *
     * @param timestamp          时间搓
     * @param sign               签名
     * @param uid                用户的ID
     * @param firstSkuId         主skuid
     * @param firstGateProductId 主产品档口产品id(货源中获取)
     */
    @Multipart
    @POST(HttpUrls.ADD_PURCART_URL)
    Observable<BaseBean> addPurcart(@Header("timestamp") String timestamp,
                                    @Header("sign") String sign,
                                    @Part("uid") int uid,
                                    @Part("first_sku_id") int firstSkuId,
                                    @Part("first_gate_product_id") int firstGateProductId);

    /**
     * 添加商品到进货列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     */
    @Multipart
    @POST(HttpUrls.UP_CARTGATE_URL)
    Observable<BaseBean> upCartGate(@Header("timestamp") String timestamp,
                                    @Header("sign") String sign,
                                    @Part("uid") int uid,
                                    @Part("cart_id") int cartId,
                                    @Part("num") int num);

    /**
     * 添加商品到进货列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     */
    @Multipart
    @POST(HttpUrls.UP_CARTGATE_URL)
    Observable<BaseBean> updateCartGateCheck(@Header("timestamp") String timestamp,
                                             @Header("sign") String sign,
                                             @Part("uid") int uid,
                                             @Part("cart_id") int cartId,
                                             @Part("checked") int checked);

    /**
     * 添加商品到进货列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     */
    @Multipart
    @POST(HttpUrls.UP_ALL_CART_URL)
    Observable<BaseBean> upAllCart(@Header("timestamp") String timestamp,
                                   @Header("sign") String sign,
                                   @Part("uid") int uid,
                                   @Part("cart_ids") String cartIds,
                                   @Part("type") int type,
                                   @Part("checked") int checked);

    /**
     * 根据大中小随机获取组合
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param type      规格大中小id
     * @return 根据大中小随机获取组合时服务器端返回的数据
     */
    @GET(HttpUrls.GET_SPEC_SUK_URL)
    Observable<SpecSuk> getSpecSuk(@Header("timestamp") String timestamp,
                                   @Header("sign") String sign,
                                   @Query("uid") int uid,
                                   @Query("type") int type);

    /**
     * 换搭配获取植物花盆列表
     *
     * @param timestamp  时间搓
     * @param sign       签名
     * @param uid        用户的ID
     * @param cateCode   类别:plant,获取植物 pot获取盆
     * @param plantSkuId 植物SkuId
     * @param potSkuId   盆SkuId
     * @param specType   大中小id(此参数仅在获取花盆时有效)
     * @param pCid       分类id(多个用逗号拼接)
     * @param specId     规格id(同类别规格用逗号分隔,不同类别用|分隔) 例:specId=11,12|15,16
     * @param attrId     属性id,(格式同规格)
     * @param height     搭配高度范围,用|分隔 例:height=5|20
     * @param diameter   搭配口径范围,格式同上
     * @param isRandom   是否随机(0/1),随机获取三条记录
     * @return 换搭配获取植物花盆列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_RECOMMEND_URL)
    Observable<Recommend> getRecommend(@Header("timestamp") String timestamp,
                                       @Header("sign") String sign,
                                       @Query("uid") int uid,
                                       @Query("cate_code") String cateCode,
                                       @Query("plantskuid") Integer plantSkuId,
                                       @Query("potskuid") Integer potSkuId,
                                       @Query("spectype") Integer specType,
                                       @Query("pcid") Integer pCid,
                                       @Query("specid") String specId,
                                       @Query("attrid") String attrId,
                                       @Query("height") String height,
                                       @Query("diameter") String diameter,
                                       @Query("issj") Integer isRandom);

    @GET("?method=mybw.newrecommend")
    Observable<Recommend> getNewRecommend(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Query("uid") int uid,
                                          @Query("cate_code") String cateCode,
                                          @Query("plantskuid") Integer plantSkuId,
                                          @Query("potskuid") Integer potSkuId,
                                          @Query("spectype") Integer specType,
                                          @Query("specid") String specId,
                                          @Query("attrid") String attrId,
                                          @QueryMap Map<String, String> param);

    /**
     * 新搭配首次获取植物花盆列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param specType  大中小id(此参数仅在获取花盆时有效)
     * @return 换搭配获取植物花盆列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_NEWRECOMMEND_URL)
    Observable<Recommend> getNewRecommend(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Query("uid") int uid,
                                          @Query("spectype") Integer specType,
                                          @Query("apiver") String apiver,
                                          @Query("check") String check
    );

    /**
     * 新搭配首次获取植物花盆列表
     *
     * @param timestamp  时间搓
     * @param sign       签名
     * @param uid        用户的ID
     * @param plantSkuId 植物
     * @param specType   大中小id(此参数仅在获取花盆时有效)
     * @return 换搭配获取植物花盆列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_NEWRECOMMEND_URL)
    Observable<Recommend> getNewRecommedChangeStyle(@Header("timestamp") String timestamp,
                                                    @Header("sign") String sign,
                                                    @Query("uid") int uid,
                                                    @Query("plantskuid") Integer plantSkuId,
                                                    @Query("potskuid") Integer specType,
                                                    @Query("apiver") String apiver,
                                                    @Query("check") String check
    );

    /**
     * 新搭配获取植物花盆列表
     *
     * @param timestamp  时间搓
     * @param sign       签名
     * @param uid        用户的ID
     * @param cateCode   类别:plant,获取植物 pot获取盆
     * @param plantSkuId 植物SkuId
     * @param specType   大中小id(此参数仅在获取花盆时有效)
     * @return 换搭配获取植物花盆列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_NEWRECOMMEND_URL)
    Observable<Recommend> getNewRecommend(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Query("uid") int uid,
                                          @Query("cate_code") String cateCode,
                                          @Query("plantskuid") Integer plantSkuId,
                                          @Query("potskuid") Integer potskuid,
                                          @Query("spectype") Integer specType,
                                          @Query("apiver") String apiver,
                                          @Query("check") String check
    );

    /**
     * 新搭配获取植物花盆列表
     *
     * @param timestamp  时间搓
     * @param sign       签名
     * @param uid        用户的ID
     * @param cateCode   类别:plant,获取植物 pot获取盆
     * @param plantSkuId 植物SkuId
     * @param specType   大中小id(此参数仅在获取花盆时有效)
     * @param specid     规格id(多个用逗号拼接)
     * @param attrid     属性id(多个用逗号拼接)
     * @return 换搭配获取植物花盆列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_NEWRECOMMEND_URL)
    Observable<Recommend> getNewRecommend(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Query("uid") int uid,
                                          @Query("cate_code") String cateCode,
                                          @Query("plantskuid") Integer plantSkuId,
                                          @Query("spectype") Integer specType,
                                          @Query("specid") String specid,
                                          @Query("attrid") String attrid,
                                          @Query("apiver") String apiver,
                                          @Query("check") String check
    );


    /**
     * 换搭配获取植物花盆列表
     *
     * @param timestamp  时间搓
     * @param sign       签名
     * @param uid        用户的ID
     * @param cateCode   类别:plant,获取植物 pot获取盆
     * @param plantSkuId 植物SkuId
     * @param potSkuId   盆SkuId
     * @param specType   大中小id(此参数仅在获取花盆时有效)
     * @param pCid       分类id(多个用逗号拼接)
     * @param param      动态设置的参数
     * @param isRandom   是否随机(0/1),随机获取三条记录
     * @return 换搭配获取植物花盆列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_RECOMMEND_URL)
    Observable<Recommend> getRecommend(@Header("timestamp") String timestamp,
                                       @Header("sign") String sign,
                                       @Query("uid") int uid,
                                       @Query("cate_code") String cateCode,
                                       @Query("plantskuid") Integer plantSkuId,
                                       @Query("potskuid") Integer potSkuId,
                                       @Query("spectype") Integer specType,
                                       @Query("pcid") Integer pCid,
                                       @QueryMap Map<String, String> param,
                                       @Query("issj") Integer isRandom);

    /**
     * 获取相似类型sku列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param skuId     产品SKUid
     * @return 获取相似类型sku列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_CATEGORY_SIMILAR_SUK_URL)
    Observable<CategorySimilarSKU> getCategorySimilarSKUList(@Header("timestamp") String timestamp,
                                                             @Header("sign") String sign,
                                                             @Query("uid") int uid,
                                                             @Query("sku_id") int skuId);

    /**
     * 获取相似推荐sku列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param skuId     产品SKUid
     * @return 获取相似推荐sku列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_SIMILAR_SUK_LIST_URL)
    Observable<SimilarSKU> getSimilarSKUList(@Header("timestamp") String timestamp,
                                             @Header("sign") String sign,
                                             @Query("uid") int uid,
                                             @Query("sku_id") int skuId);

    /**
     * 修改产品sku使用状态
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param skuId     产品SKUid
     * @return 修改产品sku使用状态时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.UPDATE_SKU_USE_STATE_URL)
    Observable<UpdateSKUUseState> updateSKUUseState(@Header("timestamp") String timestamp,
                                                    @Header("sign") String sign,
                                                    @Field("uid") int uid,
                                                    @Field("sku_id") int skuId);

    /**
     * 获取搭配时植物sku推荐列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param skuId     植物SKUid
     * @param page      第几页
     * @param pageSize  每页返回条数
     * @return 获取搭配时植物sku推荐列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_SIMILAR_PLANT_SUK_LIST_URL)
    Observable<SimilarPlantSKU> getSimilarPlantSKUList(@Header("timestamp") String timestamp,
                                                       @Header("sign") String sign,
                                                       @Query("uid") int uid,
                                                       @Query("sku_id") int skuId,
                                                       @Query("page") Integer page,
                                                       @Query("pagesize") Integer pageSize);

    /**
     * 获取模拟场景植物花盆推荐列表
     *
     * @param timestamp  时间搓
     * @param sign       签名
     * @param uid        用户的ID
     * @param isAll      是否获取全部:1获取全部0分页
     * @param cateCode   类别:plant,只获取植物,pot只获取盆不传则植物和盆都会返回
     * @param plantSkuId 植物skuId
     * @param potSkuId   盆skuId
     * @return 获取模拟场景植物花盆推荐列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_RECOMMEND_PRODUCT_LIST_URL)
    Observable<RecommendProduct> getRecommendProductList(@Header("timestamp") String timestamp,
                                                         @Header("sign") String sign,
                                                         @Query("uid") int uid,
                                                         @Query("isall") int isAll,
                                                         @Query("cate_code") String cateCode,
                                                         @Query("plantskuid") int plantSkuId,
                                                         @Query("potskuid") Integer potSkuId);

    /**
     * 获取客户列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return 获取客户列表时服务器端返回的数据
     */
    @GET(HttpUrls.CLIENT_LIST_URL)
    Observable<CustomerList> getCustomerList(@Header("timestamp") String timestamp,
                                             @Header("sign") String sign,
                                             @Query("uid") int uid);

    /**
     * 创建客户
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param name      名称
     * @param tel       电话
     * @param address   详细地址
     * @return 创建客户时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.CREATE_CLIENT_URL)
    Observable<CreateCustomers> createCustomer(@Header("timestamp") String timestamp,
                                               @Header("sign") String sign,
                                               @Part("uid") int uid,
                                               @Part("name") String name,
                                               @Part("tel") String tel,
                                               @Part("address") String address);

    /**
     * 创建客户
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param name      名称
     * @param tel       电话
     * @param address   详细地址
     * @param params    Logo图片
     * @return 创建客户时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.CREATE_CLIENT_URL)
    Observable<CreateCustomers> createCustomer(@Header("timestamp") String timestamp,
                                               @Header("sign") String sign,
                                               @Part("uid") int uid,
                                               @Part("name") String name,
                                               @Part("tel") String tel,
                                               @Part("address") String address,
                                               @PartMap Map<String, RequestBody> params);

    /**
     * 修改客户
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cid       客户的ID
     * @param name      名称
     * @param tel       电话
     * @param address   详细地址
     * @return 修改客户时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.EDIT_CLIENT_URL)
    Observable<EditCustomer> editCustomer(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Part("uid") int uid,
                                          @Part("cid") int cid,
                                          @Part("name") String name,
                                          @Part("tel") String tel,
                                          @Part("address") String address);

    /**
     * 修改客户
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cid       客户的ID
     * @param name      名称
     * @param tel       电话
     * @param address   详细地址
     * @param params    Logo图片
     * @return 修改客户时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.EDIT_CLIENT_URL)
    Observable<EditCustomer> editCustomer(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Part("uid") int uid,
                                          @Part("cid") int cid,
                                          @Part("name") String name,
                                          @Part("tel") String tel,
                                          @Part("address") String address,
                                          @PartMap Map<String, RequestBody> params);

    /**
     * 删除客户
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cid       客户的ID
     * @return 删除客户时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.DELETE_CLIENT_URL)
    Observable<DeleteCustomer> deleteCustomer(@Header("timestamp") String timestamp,
                                              @Header("sign") String sign,
                                              @Field("uid") int uid,
                                              @Field("cid") int cid);

    /**
     * 创建报价位置
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cid       客户的ID
     * @param name      位置名称
     * @return 创建报价位置时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.CREATE_QUOTATION_LOCATION_URL)
    Observable<CreateQuotationLocation> createQuotationLocation(@Header("timestamp") String timestamp,
                                                                @Header("sign") String sign,
                                                                @Field("uid") int uid,
                                                                @Field("cid") int cid,
                                                                @Field("name") String name);

    /**
     * 创建报价位置
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cid       客户的ID
     * @param name      位置名称
     * @param params    设计图,单图表单提交模式
     * @return 创建报价位置时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.CREATE_QUOTATION_LOCATION_URL)
    Observable<CreateQuotationLocation> createQuotationLocation(@Header("timestamp") String timestamp,
                                                                @Header("sign") String sign,
                                                                @Part("uid") int uid,
                                                                @Part("cid") int cid,
                                                                @Part("name") String name,
                                                                @PartMap Map<String, RequestBody> params);

    /**
     * 获取位置明细列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cid       客户的ID
     * @return 获取位置明细列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_QUOTATION_LOCATION_URL)
    Observable<QuotationLocation> getQuotationLocation(@Header("timestamp") String timestamp,
                                                       @Header("sign") String sign,
                                                       @Query("uid") int uid,
                                                       @Query("cid") int cid);

    /**
     * 修改报价位置信息
     *
     * @param timestamp    时间搓
     * @param sign         签名
     * @param uid          用户的ID
     * @param quotePlaceId 报价位置id
     * @param name         位置名称
     * @return 修改报价位置信息时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.UPDATE_QUOTATION_LOCATION_URL)
    Observable<UpdateQuotationLocation> updateQuotationLocation(@Header("timestamp") String timestamp,
                                                                @Header("sign") String sign,
                                                                @Field("uid") int uid,
                                                                @Field("quote_place_id") int quotePlaceId,
                                                                @Field("name") String name);

    /**
     * 修改报价位置信息
     *
     * @param timestamp    时间搓
     * @param sign         签名
     * @param uid          用户的ID
     * @param quotePlaceId 报价位置id
     * @param name         位置名称
     * @param params       设计图,单图表单提交模式
     * @return 修改报价位置信息时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.UPDATE_QUOTATION_LOCATION_URL)
    Observable<UpdateQuotationLocation> updateQuotationLocation(@Header("timestamp") String timestamp,
                                                                @Header("sign") String sign,
                                                                @Part("uid") int uid,
                                                                @Part("quote_place_id") int quotePlaceId,
                                                                @Part("name") String name,
                                                                @PartMap Map<String, RequestBody> params);

    /**
     * 删除报价位置信息
     *
     * @param timestamp    时间搓
     * @param sign         签名
     * @param uid          用户的ID
     * @param quotePlaceId 报价位置id
     * @return 删除报价位置信息时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.DELETE_QUOTATION_LOCATION_URL)
    Observable<DeleteQuotationLocation> deleteQuotationLocation(@Header("timestamp") String timestamp,
                                                                @Header("sign") String sign,
                                                                @Field("uid") int uid,
                                                                @Field("quote_place_id") int quotePlaceId);

    /**
     * 修改报价位置中产品数量
     *
     * @param timestamp    时间搓
     * @param sign         签名
     * @param uid          用户的ID
     * @param quotePlaceId 报价位置id
     * @param quoteId      清单产品id
     * @param number       产品数量
     * @return 修改报价位置中产品数量时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.UPDATE_QUOTATION_INFO_URL)
    Observable<UpdateQuotationInfo> updateQuotationInfo(@Header("timestamp") String timestamp,
                                                        @Header("sign") String sign,
                                                        @Field("uid") int uid,
                                                        @Field("quote_place_id") int quotePlaceId,
                                                        @Field("quote_id") int quoteId,
                                                        @Field("num") int number);

    /**
     * 获取摆放清单列表/报价清单列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cid       客户的ID
     * @param type      1摆放清单列表2报价清单列表
     * @return 获取摆放清单列表/报价清单列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_QUOTATION_LIST_URL)
    Observable<PlacementQrQuotationList> getPlacementQrQuotationList(@Header("timestamp") String timestamp,
                                                                     @Header("sign") String sign,
                                                                     @Query("uid") int uid,
                                                                     @Query("cid") int cid,
                                                                     @Query("type") int type);

    /**
     * 删除待摆放清单产品
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param cid       客户的ID
     * @param quoteIds  清单产品id,多个用英文逗号分隔
     * @return 删除待摆放清单产品时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.DELETE_QUOTATION_URL)
    Observable<DeletePlacement> deletePlacement(@Header("timestamp") String timestamp,
                                                @Header("sign") String sign,
                                                @Field("uid") int uid,
                                                @Field("cid") int cid,
                                                @Field("quote_ids") String quoteIds);

    /**
     * sku加入待摆放清单
     *
     * @param timestamp   时间搓
     * @param sign        签名
     * @param uid         用户的ID
     * @param cid         客户的ID
     * @param firstSkuId  主产品 skuId
     * @param secondSkuId 附加产品 skuId
     * @return sku加入待摆放清单时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.ADD_QUOTATION_URL)
    Observable<AddQuotation> addQuotation(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Field("uid") int uid,
                                          @Field("cid") int cid,
                                          @Field("first_sku_id") int firstSkuId,
                                          @Field("second_sku_id") Integer secondSkuId);

    /**
     * sku加入待摆放清单
     *
     * @param timestamp   时间搓
     * @param sign        签名
     * @param uid         用户的ID
     * @param cid         客户的ID
     * @param firstSkuId  主产品 skuId
     * @param secondSkuId 附加产品 skuId
     * @param params      组合图片,单图表单提交模式
     * @return sku加入待摆放清单时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.ADD_QUOTATION_URL)
    Observable<AddQuotation> addQuotation(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Part("uid") int uid,
                                          @Part("cid") int cid,
                                          @Part("first_sku_id") Integer firstSkuId,
                                          @Part("second_sku_id") Integer secondSkuId,
                                          @PartMap Map<String, RequestBody> params);

    /**
     * 待摆放清单加入到报价位置
     *
     * @param timestamp    时间搓
     * @param sign         签名
     * @param uid          用户的ID
     * @param quoteId      清单id
     * @param quotePlaceId 报价位置id
     * @return 待摆放清单加入到报价位置时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.ADD_QUOTATION_LOCATION_URL)
    Observable<AddQuotationLocation> addQuotationLocation(@Header("timestamp") String timestamp,
                                                          @Header("sign") String sign,
                                                          @Field("uid") int uid,
                                                          @Field("quote_id") int quoteId,
                                                          @Field("quote_place_id") int quotePlaceId);

    /**
     * 修改清单产品价格
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param quoteId   清单id
     * @param price     新价格
     * @param type      1售价2月租价
     * @return 修改清单产品价格时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.UPDATE_QUOTATION_PRICE_URL)
    Observable<UpdateQuotationPrice> updateQuotationPrice(@Header("timestamp") String timestamp,
                                                          @Header("sign") String sign,
                                                          @Field("uid") int uid,
                                                          @Field("quote_id") int quoteId,
                                                          @Field("price") double price,
                                                          @Field("type") int type);

    /**
     * 获取报价单列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param type      0/空获取所有,1待客户确定,2待跟进3已成交4已失效
     * @param isAll     0分页1不分页默认为1
     * @return 获取订单列表时服务器端返回的数据
     */
    @GET(HttpUrls.QUOTATION_LIST_URL)
    Observable<QuotationList> getQuotationList(@Header("timestamp") String timestamp,
                                               @Header("sign") String sign,
                                               @Query("uid") int uid,
                                               @Query("type") int type,
                                               @Query("isall") int isAll);

    /**
     * 创建报价单
     *
     * @param timestamp     时间搓
     * @param sign          签名
     * @param uid           用户的ID
     * @param cid           客户的id
     * @param payType       支付模式id(支付模式0购买1月租2年租)默认为1
     * @param tax           税点(默认为0)
     * @param districtMoney 优惠金额(默认为0)
     * @return 创建报价单时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.CREATE_QUOTATION_ORDER_URL)
    Observable<CreateQuotationOrder> createQuotationOrder(@Header("timestamp") String timestamp,
                                                          @Header("sign") String sign,
                                                          @Part("uid") int uid,
                                                          @Part("cid") int cid,
                                                          @Part("pay_type") int payType,
                                                          @Part("tax") double tax,
                                                          @Part("district_money") double districtMoney);

    /**
     * 获取采购单列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户ID
     * @param isAll     0分页1不分页默认为1
     * @param state     状态类型: 0/空获取所有,1待付款,2待配送3待收货4已完成
     * @return 获取采购单列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_PURCHASE_ORDER_LIST_URL)
    Observable<PurchaseOrderList> getPurchaseOrderList(@Header("timestamp") String timestamp,
                                                       @Header("sign") String sign,
                                                       @Query("uid") int uid,
                                                       @Query("isall") int isAll,
                                                       @Query("state") int state);

    /**
     * 创建采购订单
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param item      商品信息(json)
     * @param addressId 地址id
     * @return 创建采购订单时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.CREATE_PURCHASE_ORDER_URL)
    Observable<PurchaseOrder> createPurchaseOrder(@Header("timestamp") String timestamp,
                                                  @Header("sign") String sign,
                                                  @Field("uid") int uid,
                                                  @Field("item") String item,
                                                  @Field("addr_id") int addressId);

    /**
     * 报价单再次报价
     *
     * @param timestamp   时间搓
     * @param sign        签名
     * @param uid         用户的ID
     * @param cid         客户的ID
     * @param orderNumber 订单编号
     * @param clearOk     是否清除未完成报价
     * @return 报价单再次报价时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.QUOTATION_AGAIN_URL)
    Observable<QuotationAgain> quotationAgain(@Header("timestamp") String timestamp,
                                              @Header("sign") String sign,
                                              @Field("uid") int uid,
                                              @Field("cid") int cid,
                                              @Field("order_number") String orderNumber,
                                              @Field("clearok") int clearOk);

    /**
     * 获取收货地址
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return 获取收货地址时服务器端返回的数据
     */
    @GET(HttpUrls.GET_ADDRESS_URL)
    Observable<Address> getReceiptAddress(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Query("uid") int uid);

    /**
     * 创建或修改收货地址
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户ID
     * @param addressId 收货地址id(有此字段表示修改)
     * @param areaId    区id
     * @param consignee 收货人
     * @param mobile    联系电话
     * @param address   详细地址
     * @param isDefault 是否默认(1是0否)
     * @return 创建或修改收货地址时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.CREATE_OR_MODIFY_SHIPPING_ADDRESS_URL)
    Observable<ShippingAddress> createOrModifyTheShippingAddress(@Header("timestamp") String timestamp,
                                                                 @Header("sign") String sign,
                                                                 @Field("uid") int uid,
                                                                 @Field("addr_id") Integer addressId,
                                                                 @Field("area_id") int areaId,
                                                                 @Field("consignee") String consignee,
                                                                 @Field("mobile") String mobile,
                                                                 @Field("address") String address,
                                                                 @Field("isdefault") int isDefault);

    /**
     * 删除收货地址
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户ID
     * @param addressId 收货地址id
     * @return 删除收货地址时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.DELETE_SHIPPING_ADDRESS_URL)
    Observable<DeleteShippingAddress> deleteShippingAddress(@Header("timestamp") String timestamp,
                                                            @Header("sign") String sign,
                                                            @Field("uid") int uid,
                                                            @Field("addr_id") int addressId);

    /**
     * 获取城市区域列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户ID
     * @return 获取城市区域列表时服务器端返回的数据
     */
    @GET(HttpUrls.GET_CITY_AREA_LIST_URL)
    Observable<CityAreaList> getCityAreaList(@Header("timestamp") String timestamp,
                                             @Header("sign") String sign,
                                             @Query("uid") int uid);

    /**
     * 验证修改价格密码
     *
     * @param timestamp    时间搓
     * @param sign         签名
     * @param uid          用户ID
     * @param viewPassword 查看密码
     * @return 验证修改价格密码时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.VERIFY_PASSWORD_URL)
    Observable<VerifyPassword> verifyPassword(@Header("timestamp") String timestamp,
                                              @Header("sign") String sign,
                                              @Field("uid") int uid,
                                              @Field("viewpwd") String viewPassword);

    /**
     * 设置价格增幅
     *
     * @param timestamp    时间搓
     * @param sign         签名
     * @param uid          用户ID
     * @param increaseRent 租价幅度%
     * @param increaseSell 售价幅度%
     * @return 设置价格增幅时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.SET_INCREASE_URL)
    Observable<SetIncrease> setIncrease(@Header("timestamp") String timestamp,
                                        @Header("sign") String sign,
                                        @Field("uid") int uid,
                                        @Field("increase_rent") String increaseRent,
                                        @Field("increase_sell") String increaseSell);

    /**
     * 修改用户基础信息
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param nickname  昵称
     * @return 修改用户基础信息时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.EDIT_USER_INFO_URL)
    Observable<EditUserInfo> editUserInfo(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Part("uid") int uid,
                                          @Part("nickname") String nickname);

    /**
     * 修改用户基础信息
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param com_open  全局货源筛选开关：1仅展示供货的2全部
     * @return 修改用户基础信息时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.EDIT_USER_INFO_URL)
    Observable<EditUserInfo> editUserProductInfo(@Header("timestamp") String timestamp,
                                                 @Header("sign") String sign,
                                                 @Part("uid") int uid,
                                                 @Part("com_open") int com_open);


    /**
     * 修改用户基础信息
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param params    头像
     * @return 修改用户基础信息时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.EDIT_USER_INFO_URL)
    Observable<EditUserInfo> editUserInfo(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Part("uid") int uid,
                                          @PartMap Map<String, RequestBody> params);

    /**
     * 绑定手机号码
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param tel       手机号码
     * @param code      验证码
     * @return 绑定手机号码时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.BINDING_PHONE_URL)
    Observable<BindingPhone> bindingPhone(@Header("timestamp") String timestamp,
                                          @Header("sign") String sign,
                                          @Field("uid") int uid,
                                          @Field("tel") String tel,
                                          @Field("code") String code);

    /**
     * 绑定微信
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param code      微信给的code
     * @return 绑定微信时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.BINDING_WECHAT_URL)
    Observable<BindingWechat> bindingWechat(@Header("timestamp") String timestamp,
                                            @Header("sign") String sign,
                                            @Field("uid") int uid,
                                            @Field("code") String code);

    /**
     * 用户反馈
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param desc      建议或问题描述
     * @param contact   联系手机或者联系邮箱
     * @param parts     描述图片
     * @return 用户反馈时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.FEED_BACK_URL)
    Observable<FeedBack> feedBack(@Header("timestamp") String timestamp,
                                  @Header("sign") String sign,
                                  @Part("uid") int uid,
                                  @Part("desc") String desc,
                                  @Part("contact") String contact,
                                  @Part MultipartBody.Part[] parts);

    /**
     * 修改水印图片
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param params    水印图片(单图上传模式)
     * @return 修改水印图片时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.UPDATE_WATERMARK_URL)
    Observable<UpdateWatermark> updateWatermark(@Header("timestamp") String timestamp,
                                                @Header("sign") String sign,
                                                @Part("uid") int uid,
                                                @PartMap Map<String, RequestBody> params);

    /**
     * 版本更新
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return APP更新时服务器端返回的数据
     */
    @GET(HttpUrls.APP_UPDATE_URL)
    Observable<AppUpdate> appUpdate(@Header("timestamp") String timestamp,
                                    @Header("sign") String sign,
                                    @Query("uid") int uid);

    /**
     * 获取进货列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return APP更新时服务器端返回的数据
     */
    @GET(HttpUrls.GET_PURCART_URL)
    Observable<PurCartBean> getPurCart(@Header("timestamp") String timestamp,
                                       @Header("sign") String sign,
                                       @Query("uid") int uid);

    /**
     * 版本更新
     *
     * @return APP更新时服务器端返回的数据
     */
    @GET("http://43.226.73.21:8081/RoadHouse/ybvloea.json")
    Observable<String> getDataByGet();

    /**
     * 修改密码
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param oldpwd    旧密码
     * @param newpwd    新密码
     * @param type      模式1普通模式2验证码模式(默认为1)
     * @param code      验证码(验证码模式时必填)
     * @return 修改密码时服务器端返回的数据
     */
    @FormUrlEncoded
    @POST(HttpUrls.MODIFY_PASSWORD_URL)
    Observable<ModifyPassword> modifyPassword(@Header("timestamp") String timestamp,
                                              @Header("sign") String sign,
                                              @Field("uid") int uid,
                                              @Field("oldpwd") String oldpwd,
                                              @Field("newpwd") String newpwd,
                                              @Field("type") int type,
                                              @Field("code") String code);

    /**
     * 获取城市列表
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @return
     */
    @GET(HttpUrls.GET_CITY_URL)
    Observable<CityListBean> getCity(@Header("timestamp") String timestamp,
                                     @Header("sign") String sign,
                                     @Query("uid") int uid);

    /**
     * 经纬度获取城市地区信息
     */
    @GET(GET_CITY_PLACE_URL)
    Observable<PlaceBean> getPlace(
            @Header("timestamp") String timestamp,
            @Header("sign") String sign,
            @Query("uid") int uid,
            @Query("longitude") String longitude,
            @Query("latitude") String latitude);

    /**
     * 设置货源城市
     *
     * @param timestamp 时间搓
     * @param sign      签名
     * @param uid       用户的ID
     * @param code      城市字符串（广东省广州市）
     * @return 设置货源城市时服务器端返回的数据
     */
    @Multipart
    @POST(HttpUrls.SET_USER_POSITION_URL)
    Observable<UserPosition> setCity(@Header("timestamp") String timestamp,
                                     @Header("sign") String sign,
                                     @Part("uid") int uid,
                                     @Part("code") String code);

    @GET("?method=mybw.getplace")
    Observable<BaseResponse<CurAddrResponse>> getLocation(
            @Header("timestamp") String timestamp,
            @Header("sign") String sign,
            @Query("data") String data);


    @GET("?method=mybw.getcity")
    Observable<BaseResponse<HotCityResponse>> getAllCities(
            @Header("timestamp") String timestamp,
            @Header("sign") String sign,
            @Query("uid") String uid);

    @FormUrlEncoded
    @POST("?method=mybw.setuserposition")
    Observable<BaseResponse<Object>> setCity(
            @Header("timestamp") String timestamp,
            @Header("sign") String sign,
            @Field("uid") String uid,
            @Field("code") String code);

    @FormUrlEncoded
    @POST("?method=mybw.delscheme")
    Observable<BaseResponse<Object>> deleteScene(
            @Header("timestamp") String timestamp,
            @Header("sign") String sign,
            @Field("uid") String uid,
            @Field("scheme_id") String code);

    @FormUrlEncoded
    @POST("?method=mybw.delschemepic")
    Observable<BaseResponse<Object>> deleteSchemePic(
            @Header("timestamp") String timestamp,
            @Header("sign") String sign,
            @Field("uid") String uid,
            @Field("id") String id);
}
