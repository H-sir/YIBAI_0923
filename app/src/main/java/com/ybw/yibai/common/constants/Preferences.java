package com.ybw.yibai.common.constants;

import com.ybw.yibai.module.pictureview.PictureViewActivity;
import com.ybw.yibai.module.quotation.QuotationFragment;

/**
 * SharedPreferences要保存或者Intent要传递的数据
 *
 * @author sjl
 */
public class Preferences {

    /*SharedPreferences*/

    /**
     * 保存与用户相关信息的
     */
    public static final String USER_INFO = "userInfo";

    /**
     * 在用户登陆时获得的账号ID
     */
    public static final String UID = "uid";

    /**
     * 用户的账号
     */
    public static final String USER_NAME = "userName";

    /**
     * 用户的昵称
     */
    public static final String NICK_NAME = "nickName";

    /**
     * 用户的真实姓名
     */
    public static final String TRUE_NAME = "trueName";

    /**
     * 角色(除了service以外,进货改价需验证密码)
     */
    public static final String ROLE_NAME = "roleName";

    /**
     * 用户的电话号码
     */
    public static final String TELEPHOTO = "telephone";

    /**
     * 用户的头像
     */
    public static final String HEAD = "head";

    /**
     * 用户的电子邮件
     */
    public static final String EMAIL = "email";

    /**
     * 用户的地址
     */
    public static final String ADDRESS = "address";

    /**
     * 用户的公司名称
     */
    public static final String COMPANY = "company";

    /**
     * 公司LOGO
     */
    public static final String COMPANY_LOGO = "companyLogo";

    /**
     * 用户的公司简介(html代码,需base64解码)
     */
    public static final String COMPANY_DETAILS = "companyDetails";

    /**
     * 用户的公司简介图片
     */
    public static final String COMPANY_DETAILS_PIC = "companyDetailsPic";

    /**
     * 是否绑定了微信0否;1是
     */
    public static final String IS_BIND_WX = "isBindWX";

    /**
     * 会员等级(1体验2专业3终生旗舰)
     */
    public static final String VIP_LEVEL = "vipLevel";

    /**
     * 租价幅度%
     */
    public static final String INCREASE_RENT = "increaseRent";

    /**
     * 售价幅度%
     */
    public static final String INCREASE_SELL = "increaseSell";

    /**
     * 结算货币符号
     */
    public static final String CURRENCY_SYMBOL = "currencySymbol";

    /**
     * 邀请的背景图
     */
    public static final String INVITE_BG = "inviteBg";

    /**
     * 邀请规则图片
     */
    public static final String INVITE_RULE_BG = "inviteRuleBg";

    /**
     * 活动邀请的背景图
     */
    public static final String ACTIVITY_INVITE_BG = "activityInviteBg";

    /**
     * 邀请注册链接
     */
    public static final String INVITE_URL = "inviteUrl";

    /**
     * 新建设计号
     */
    public static final String DESIGN_CREATE = "designCreate";


    /**
     * 设计号
     */
    public static final String DESIGN_NUMBER = "designNumber";

    /**
     * 邀请记录地址
     */
    public static final String RECORD_URL = "recordUrl";

    /**
     * 用户升级vip页面地址
     */
    public static final String USER_VIP_URL = "userVipUrl";

    /**
     * 我的钱包地址
     */
    public static final String MY_WALLET_URL = "myWalletUrl";

    /**
     * 余额提现地址
     */
    public static final String TAKE_CASE_URL = "takeCashUrl";

    /**
     * 水印图片
     */
    public static final String WATERMARK_PIC = "watermarkPic";

    /**
     * 当前货源城市名称
     */
    public static final String CITY_NAME = "cityName";

    /**
     * 在用户登陆时获得的token
     */
    public static final String TOKEN = "token";

    /**********/

    /**
     * 服务商(用户角色类型)
     */
    public static final String SERVICE = "service";

    /**********/

    /**
     * 当前用户选择服务的客户的Id
     */
    public static final String CUSTOMERS_ID = "customersId";

    /**
     * 当前用户选择服务的客户的Logo
     */
    public static final String CUSTOMERS_LOGO = "customersLogo";

    /**
     * 当前用户选择服务的客户的名称
     */
    public static final String CUSTOMERS_MANE = "customersName";

    /*Intent*/

    /**
     * 网页链接地址
     */
    public static final String URL = "url";

    /**
     * 标记该网页为本地URL类型
     */
    public static final String LOCAL_URL_TYPE = "localUrlType";

    /**
     * 标记该网页为我的钱包网页链接地址类型
     */
    public static final String WALLET_URL_TYPE = "walletUrlType";

    /**
     * 标记该网页为租摆订单分享链接地址类型
     */
    public static final String ORDER_SHARE_URL_TYPE = "orderShareUrlType";

    /**
     * 位置
     */
    public static final String POSITION = "position";

    /**
     * 文件路径
     */
    public static final String FILE_PATH = "filePath";

    /**
     *
     */
    public static final String AUTHORITY = "com.ybw.yibai.fileprovider";

    /**
     * 登录信息
     */
    public static final String LOGIN_INFO = "loginInfo";

    /**
     * 盆器
     */
    public static final String POT = "pot";

    /**
     * 植物
     */
    public static final String PLANT = "plant";

    /**
     * 盆器类别ID
     */
    public static final String POT_TYPE_ID = "potTypeId";

    /**
     * 盆器信息
     */
    public static final String POT_INFO = "potInfo";

    /**
     * 植物信息
     */
    public static final String PLANT_INFO = "plantInfo";

    /**
     * 产品的Id
     */
    public static final String PRODUCT_ID = "productId";

    /**
     * 产品的类型
     */
    public static final String PRODUCT_TYPE = "productType";

    /**
     * 产品的SKU Id
     */
    public static final String PRODUCT_SKU_ID = "skuId";
    /**
     * 产品的SKU Id
     */
    public static final String PRODUCT_SKU_ADDORSELECT = "productSkuAddOrSelect";

    /**
     * 产品类别Code
     */
    public static final String CATEGORY_CODE = "categoryCode";

    /**
     * 产品使用状态Id
     */
    public static final String PRODUCT_USE_STATE_ID = "productUseStateId";

    /**
     * 产品SKU名称
     */
    public static final String PRODUCT_SKU_NAME = "productSKUName";

    /**
     * 产品SKU数据集合
     */
    public static final String PRODUCT_SKU_LIST = "skuList";

    /**
     * 产品筛选参数
     */
    public static final String PRODUCT_SCREENING_PARAM = "productScreeningParam";

    /**
     * 报价集合
     */
    public static final String QUOTATION_LIST = "quotationList";

    /**
     * 案例分类信息
     */
    public static final String PROJECT_CLASSIFY_INFO = "projectClassifyInfo";

    /**
     * 客户列表
     */
    public static final String CUSTOMERS_LIST = "customersList";

    /**
     * 客户详细信息
     */
    public static final String CUSTOMER_INFO = "customersInfo";

    /**
     * 客户预选产品列表
     */
    public static final String CUSTOMERS_PRESELECTION_PRODUCT_LIST = "customersPreselectionProductList";

    /**
     * 场景信息
     */
    public static final String SCENE_INFO = "sceneInfo";

    /**
     * 热门场景列表
     */
    public static final String HOT_SCHEME_LIST = "hotSchemeList";

    /**
     * 热门场景信息
     */
    public static final String HOT_SCHEME_INFO = "hotSchemeInfo";

    /**
     * "模拟搭配产品"数据
     */
    public static final String SIMULATION_DATA = "simulationData";

    /**
     * 图片地址列表
     */
    public static final String PHOTO_URL_LIST = "photoUrlList";

    /**
     * 收货地址信息
     */
    public static final String SHIPPING_ADDRESS_INFO = "shippingAddressInfo";

    /**********/

    /**
     * 信息类型
     */
    public static final String TYPE = "type";

    /**
     * 搜索关键词
     */
    public static final String KEY_WORD = "keyWord";

    /**
     * 标记在{@link QuotationFragment} 点击场景图片进入{@link PictureViewActivity} 选择以模拟图片
     */
    public static final String SELECT_IMAGE = "selectImage";

    /**
     * 是否开启水印
     */
    public static final String OPEN_WATERMARK = "openWatermark";

    /**
     * 是否记住授权码
     */
    public static final String SAVE_AUTHORIZATION = "saveAuthorization";

    /**
     * 授权码
     */
    public static final String AUTHORIZATION_CODE = "authorizationCode";

    /**
     * 创建场景时需要的数据列表
     */
    public static final String CREATE_SCENE_DATA_LIST = "createSceneDataList";

    /**
     * 租摆订单的客户名称
     */
    public static final String CUSTOMER_NAME = "customerName";

    /**
     * 租摆订单的订单号
     */
    public static final String ORDER_NUMBER = "orderNumber";
}
