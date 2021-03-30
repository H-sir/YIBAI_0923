package com.ybw.yibai.common.constants;

/**
 * 这个类存放所有的网络地址
 *
 * @author sjl
 */
public class HttpUrls {

    /**
     * 获取短信验证码
     */
    public static final String GET_CODE = "mybw.getcode";

    /**
     * 登陆的方法
     */
    public static final String LOGIN_METHOD = "mybw.login";

    /**
     * 微信登陆的方法
     */
    public static final String WECHAT_LOGIN_METHOD = "mybw.wxlogin";

    /**
     * 微信登录绑定手机的方法
     */
    public static final String WECHAT_BIND_MOBILE_PHONE_METHOD = "mybw.wxbindtel";

    /**
     * 获取用户信息的方法
     */
    public static final String GET_USER_INFO_METHOD = "mybw.getuserinfo";

    /**
     * 获取系统参数的方法
     */
    public static final String GET_SYSTEM_PARAMETER_METHOD = "mybw.sysparameter";

    /**
     * 修改用户公司信息的方法
     */
    public static final String UPDATE_COMPANY_INFO_METHOD = "mybw.upcompany";

    /**
     * 设置货源城市的方法
     */
    public static final String SET_USER_POSITION_METHOD = "mybw.setuserposition";

    /**
     * 获取方案分类的方法
     */
    public static final String GET_HOT_SCHEME_CATEGORY_METHOD = "mybw.getschemecate";

    /**
     * 获取热门方案列表的方法
     */
    public static final String GET_HOT_SCHEME_METHOD = "mybw.getscheme";

    /**
     * 获取新热门方案列表的方法
     */
    public static final String GET_HOT_SCHEMES_METHOD = "mybw.getschemes";

    /**
     * 获取推荐产品列表的方法
     */
    public static final String GET_RECOMMEND_LIST_METHOD = "mybw.getrecommendlist";

    /**
     * 获取案例分类的方法
     */
    public static final String GET_CASE_CLASSIFY_METHOD = "mybw.casecategory";

    /**
     * 获取公司案例详情的方法
     */
    public static final String GET_CASE_METHOD = "mybw.case";

    /**
     * 获取产品筛选参数的方法
     */
    public static final String GET_PRODUCT_SCREENING_PARAM_METHOD = "mybw.productparam";

    /**
     * 获取产品sku列表的方法
     */
    public static final String GET_SUK_LIST_METHOD = "mybw.getskulist";

    /**
     * 获取产品详情的方法
     */
    public static final String GET_PRODUCT_INFO_METHOD = "mybw.productinfo";
    public static final String GET_SKU_LIST_IDS_METHOD = "mybw.getskulistids";

    /**
     * 场景中产品一键创建并导入的方法
     */
    public static final String FAST_IMPORT_METHOD = "mybw.fastimport";

    /**
     * 根据大中小随机获取组合的方法
     */
    public static final String GET_SPEC_SUK_METHOD = "mybw.getspecsku";

    /**
     * 换搭配获取植物花盆列表的方法
     */
    public static final String GET_RECOMMEND_METHOD = "mybw.getrecommends";

    /**
     * 新搭配获取植物花盆列表的方法
     */
    public static final String GET_NEWRECOMMEND_METHOD = "mybw.newrecommend";

    /**
     * 获取相似类型sku列表的方法
     */
    public static final String GET_CATEGORY_SIMILAR_SUK_METHOD = "mybw.getcategorysimilarsku";

    /**
     * 获取相似推荐sku列表的方法
     */
    public static final String GET_SIMILAR_SUK_LIST_METHOD = "mybw.getsimilarsku";

    /**
     * 修改产品sku使用状态的方法
     */
    public static final String UPDATE_SKU_USE_STATE_METHOD = "mybw.upusesku";

    /**
     * 获取搭配时植物sku推荐列表的方法
     */
    public static final String GET_SIMILAR_PLANT_SUK_LIST_METHOD = "mybw.getmakesku";

    /**
     * 获取模拟场景植物花盆推荐列表的方法
     */
    public static final String GET_RECOMMEND_PRODUCT_LIST_METHOD = "mybw.getrecommend";

    /**
     * 获取客户列表的方法
     */
    public static final String CLIENT_LIST_METHOD = "mybw.client";

    /**
     * 创建客户的方法
     */
    public static final String CREATE_CLIENT_METHOD = "mybw.createclient";

    /**
     * 修改客户信息的方法
     */
    public static final String EDIT_CLIENT_METHOD = "mybw.editclient";

    /**
     * 删除客户的方法
     */
    public static final String DELETE_CLIENT_METHOD = "mybw.delclient";

    /**
     * 创建报价位置的方法
     */
    public static final String CREATE_QUOTATION_LOCATION_METHOD = "mybw.creatquoteplace";

    /**
     * 获取位置明细列表的方法
     */
    public static final String GET_QUOTATION_LOCATION_METHOD = "mybw.getquoteplace";

    /**
     * 修改报价位置信息的方法
     */
    public static final String UPDATE_QUOTATION_LOCATION_METHOD = "mybw.upquoteplace";

    /**
     * 删除报价位置信息的方法
     */
    public static final String DELETE_QUOTATION_LOCATION_METHOD = "mybw.delquoteplace";

    /**
     * 修改报价位置中产品数量的方法
     */
    public static final String UPDATE_QUOTATION_INFO_METHOD = "mybw.upquoteinfo";

    /**
     * 删除设计图片的方法
     */
    public static final String DELETE_DESIGN_SCHEME_PIC_METHOD = "mybw.delschemepic";

    /**
     * 删除设计图片的URL
     */
    public static final String DELETE_DESIGN_SCHEME_PIC_URL = "?method=" + DELETE_DESIGN_SCHEME_PIC_METHOD;

    /**
     * 删除场景的URL
     */
    public static final String DELETE_DESIGN_SCHEME_URL = "?method=mybw.delscheme";

    /**
     * 删除场景的方法
     */
    public static final String DELETE_DESIGN_SCHEME_METHOD = "mybw.delscheme";

    /**
     * 获取设计详情的方法
     */
    public static final String GET_DESIGN_INFO_METHOD = "mybw.getdesigninfo";

    /**
     * 获取设计详情的URL
     */
    public static final String GET_DESIGN_INFO_URL = "?method=" + GET_DESIGN_INFO_METHOD;

    /**
     * 获取待摆放清单列表/报价清单列表的方法
     */
    public static final String GET_QUOTATION_LIST_METHOD = "mybw.getquotelist";

    /**
     * 删除待摆放清单产品的方法
     */
    public static final String DELETE_QUOTATION_METHOD = "mybw.delquote";

    /**
     * sku加入待摆放清单的方法
     */
    public static final String ADD_QUOTATION_METHOD = "mybw.addquote";
    /**
     * 添加商品到进货列表
     */
    public static final String ADD_PURCART_METHOD = "mybw.addpurcart";
    /**
     * 修改进货购物车信息
     */
    public static final String UP_CARTGATE_METHOD = "mybw.upcartgate";
    /**
     * 批量修改或删除进货购物车信息
     */
    public static final String UP_ALL_CART_METHOD = "mybw.upallcart";

    /**
     * 待摆放清单加入到报价位置的方法
     */
    public static final String ADD_QUOTATION_LOCATION_METHOD = "mybw.addquoteplace";

    /**
     * 修改清单产品价格的方法
     */
    public static final String UPDATE_QUOTATION_PRICE_METHOD = "mybw.upquoteprice";

    /**
     * 获取报价单列表的方法
     */
    public static final String QUOTATION_LIST_METHOD = "mybw.orderlist";

    /**
     * 创建报价单的方法
     */
    public static final String CREATE_QUOTATION_ORDER_METHOD = "mybw.createorder";

    /**
     * 获取采购单列表的方法
     */
    public static final String GET_PURCHASE_ORDER_LIST_METHOD = "mybw.purchaseorderlist";

    /**
     * 创建采购订单
     */
    public static final String CREATE_PURCHASE_ORDER_METHOD = "mybw.createpurchaseorder";

    /**
     * 报价单再次报价
     */
    public static final String QUOTATION_AGAIN_METHOD = "mybw.exportquote";

    /**
     * 获取收货地址的方法
     */
    public static final String GET_ADDRESS_METHOD = "mybw.getaddress";

    /**
     * 创建或修改收货地址的方法
     */
    public static final String CREATE_OR_MODIFY_SHIPPING_ADDRESS_METHOD = "mybw.puraddress";

    /**
     * 删除收货地址的方法
     */
    public static final String DELETE_SHIPPING_ADDRESS_METHOD = "mybw.deladdress";

    /**
     * 获取城市区域列表的方法
     */
    public static final String GET_CITY_AREA_LIST_METHOD = "mybw.getregion";

    /**
     * 验证修改价格密码的方法
     */
    public static final String VERIFY_PASSWORD_METHOD = "mybw.verifypwd";

    /**
     * 设置价格增幅的方法
     */
    public static final String SET_INCREASE_METHOD = "mybw.setincrease";

    /**
     * 修改用户基础信息的方法
     */
    public static final String EDIT_USER_INFO_METHOD = "mybw.edituser";
    /**
     * 绑定市场的方法
     */
    public static final String BIND_MARKET_METHOD = "mybw.bindmarket";

    /**
     * 创建设计的方法
     */
    public static final String CREATE_DESIGN_METHOD = "mybw.createdesign";


    /**
     * 修改场景信息
     */
    public static final String EDIT_SCHEME_METHOD = "mybw.editscheme";


    /**
     * 创建设计的方法
     */
    public static final String DESIGN_SCHEME_METHOD = "mybw.designscheme";
    /**
     * 查询设计的分享的权限方法
     */
    public static final String CHECK_SHARE_METHOD = "mybw.checkshare";

    /**
     * 微信绑定的方法
     */
    public static final String BINDING_WECHAT_METHOD = "mybw.bindwx";

    /**
     * 绑定更换手机的方法
     */
    public static final String BINDING_PHONE_METHOD = "mybw.bindtel";

    /**
     * 用户反馈接口的方法
     */
    public static final String FEED_BACK_METHOD = "mybw.feedback";

    /**
     * 修改水印图片的方法
     */
    public static final String UPDATE_WATERMARK_METHOD = "mybw.upwatermark";

    /**
     * 版本更新的方法
     */
    public static final String APP_UPDATE_METHOD = "mybw.version";

    /**
     * 修改密码的方法
     */
    public static final String MODIFY_PASSWORD_METHOD = "mybw.uppwd";

    /**
     * 获取城市列表的方法
     */
    public static final String GET_CITY_METHOD = "mybw.getcity";
    /**
     * 根据经纬度获取市场列表的方法
     */
    public static final String GET_MARKET_LIST_METHOD = "mybw.getmarketlist";

    /**
     * 根据经纬度获取城市的方法
     */
    public static final String GET_CITY_PLACE_METHOD = "mybw.getplace";

    /**
     * 获取进货列表
     */
    public static final String GET_PURCART_METHOD = "mybw.getpurcart";

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 获取进货列表
     */
    public static final String GET_PURCART_URL = "?method=" + GET_PURCART_METHOD;

    /**
     * 请求根地址的URL
     */
    public static final String BASE_URL = "https://api.100ybw.com/";

    /**
     * 获取短信验证码的URL
     */
    public static final String GET_CODE_URL = "?method=" + GET_CODE;

    /**
     * 用户登录的URL
     */
    public static final String LOGIN_URL = "?method=" + LOGIN_METHOD;

    /**
     * 微信登录的URL
     */
    public static final String WECHAT_LOGIN_URL = "?method=" + WECHAT_LOGIN_METHOD;

    /**
     * 微信绑定手机的URL
     */
    public static final String WECHAT_BIND_MOBILE_PHONE_URL = "?method=" + WECHAT_BIND_MOBILE_PHONE_METHOD;

    /**
     * 获取用户信息的URL
     */
    public static final String GET_USER_INFO_URL = "?method=" + GET_USER_INFO_METHOD;

    /**
     * 获取系统参数的URL
     */
    public static final String GET_SYSTEM_PARAMETER_URL = "?method=" + GET_SYSTEM_PARAMETER_METHOD;
    /**
     * 换搭新的配获取植物花盆列表的URL
     */
    public static final String GET_NEWRECOMMEND_URL = "?method=" + GET_NEWRECOMMEND_METHOD;


    /**
     * 删除设计的方法
     */
    public static final String DELETE_DESIGN_METHOD = "mybw.deldesign";


    /**
     * 删除设计的URL
     */
    public static final String DELETE_DESIGN_URL = "?method=" + DELETE_DESIGN_METHOD;

    /**
     * 获取城市列表的URL
     */
    public static final String GET_CITY_URL = "?method=" + GET_CITY_METHOD;

    public static final String GET_CITY_PLACE_URL = "?method=" + GET_CITY_PLACE_METHOD;

    public static final String GET_MARKET_LIST_URL = "?method=" + GET_MARKET_LIST_METHOD;

    /**
     * 修改用户公司信息的URL
     */
    public static final String UPDATE_COMPANY_INFO_URL = "?method=" + UPDATE_COMPANY_INFO_METHOD;

    /**
     * 设置货源城市的URL
     */
    public static final String SET_USER_POSITION_URL = "?method=" + SET_USER_POSITION_METHOD;

    /**
     * 获取方案分类的URL
     */
    public static final String GET_HOT_SCHEME_CATEGORY_URL = "?method=" + GET_HOT_SCHEME_CATEGORY_METHOD;

    /**
     * 获取方案列表的URL
     */
    public static final String GET_HOT_SCHEME_URL = "?method=" + GET_HOT_SCHEME_METHOD;

    /**
     * 获取新方案列表的URL
     */
    public static final String GET_HOT_SCHEMES_URL = "?method=" + GET_HOT_SCHEMES_METHOD;

    /**
     * 获取推荐产品列表的URL
     */
    public static final String GET_RECOMMEND_LIST_URL = "?method=" + GET_RECOMMEND_LIST_METHOD;

    /**
     * 获取公司案例分类的URL
     */
    public static final String GET_CASE_CLASSIFY_URL = "?method=" + GET_CASE_CLASSIFY_METHOD;

    /**
     * 获取公司案例详情的URL
     */
    public static final String GET_CASE_URL = "?method=" + GET_CASE_METHOD;

    /**
     * 获取产品筛选参数的URL
     */
    public static final String GET_PRODUCT_SCREENING_PARAM_URL = "?method=" + GET_PRODUCT_SCREENING_PARAM_METHOD;

    /**
     * 获取产品sku列表的URL
     */
    public static final String GET_SUK_LIST_URL = "?method=" + GET_SUK_LIST_METHOD;

    /**
     * 获取产品详情的URL
     */
    public static final String GET_PRODUCT_INFO_URL = "?method=" + GET_PRODUCT_INFO_METHOD;

    /**
     * 获取指定sku信息
     */
    public static final String GET_SKU_LIST_IDS_URL = "?method=" + GET_SKU_LIST_IDS_METHOD;

    /**
     * 场景中产品一键创建并导入的URL
     */
    public static final String FAST_IMPORT_URL = "?method=" + FAST_IMPORT_METHOD;

    /**
     * 根据大中小随机获取组合的URL
     */
    public static final String GET_SPEC_SUK_URL = "?method=" + GET_SPEC_SUK_METHOD;

    /**
     * 换搭配获取植物花盆列表的URL
     */
    public static final String GET_RECOMMEND_URL = "?method=" + GET_RECOMMEND_METHOD;

    /**
     * 修改场景信息
     */
    public static final String EDIT_SCHEME_URL = "?method=" + EDIT_SCHEME_METHOD;

    /**
     * 获取相似类型sku列表的URL
     */
    public static final String GET_CATEGORY_SIMILAR_SUK_URL = "?method=" + GET_CATEGORY_SIMILAR_SUK_METHOD;

    /**
     * 获取相似推荐sku列表的URL
     */
    public static final String GET_SIMILAR_SUK_LIST_URL = "?method=" + GET_SIMILAR_SUK_LIST_METHOD;

    /**
     * 修改产品sku使用状态的URL
     */
    public static final String UPDATE_SKU_USE_STATE_URL = "?method=" + UPDATE_SKU_USE_STATE_METHOD;

    /**
     * 获取搭配时植物sku推荐列表的URL
     */
    public static final String GET_SIMILAR_PLANT_SUK_LIST_URL = "?method=" + GET_SIMILAR_PLANT_SUK_LIST_METHOD;

    /**
     * 获取模拟场景植物花盆推荐列表的URL
     */
    public static final String GET_RECOMMEND_PRODUCT_LIST_URL = "?method=" + GET_RECOMMEND_PRODUCT_LIST_METHOD;

    /**
     * 获取客户列表的URL
     */
    public static final String CLIENT_LIST_URL = "?method=" + CLIENT_LIST_METHOD;

    /**
     * 创建客户的URL
     */
    public static final String CREATE_CLIENT_URL = "?method=" + CREATE_CLIENT_METHOD;

    /**
     * 修改客户信息的URL
     */
    public static final String EDIT_CLIENT_URL = "?method=" + EDIT_CLIENT_METHOD;

    /**
     * 删除客户的URL
     */
    public static final String DELETE_CLIENT_URL = "?method=" + DELETE_CLIENT_METHOD;

    /**
     * 创建报价位置的URL
     */
    public static final String CREATE_QUOTATION_LOCATION_URL = "?method=" + CREATE_QUOTATION_LOCATION_METHOD;

    /**
     * 获取位置明细列表的URL
     */
    public static final String GET_QUOTATION_LOCATION_URL = "?method=" + GET_QUOTATION_LOCATION_METHOD;

    /**
     * 修改报价位置信息的URL
     */
    public static final String UPDATE_QUOTATION_LOCATION_URL = "?method=" + UPDATE_QUOTATION_LOCATION_METHOD;

    /**
     * 删除报价位置信息的URL
     */
    public static final String DELETE_QUOTATION_LOCATION_URL = "?method=" + DELETE_QUOTATION_LOCATION_METHOD;

    /**
     * 修改报价位置中产品数量的URL
     */
    public static final String UPDATE_QUOTATION_INFO_URL = "?method=" + UPDATE_QUOTATION_INFO_METHOD;

    /**
     * 获取待摆放清单列表/报价清单列表的URL
     */
    public static final String GET_QUOTATION_LIST_URL = "?method=" + GET_QUOTATION_LIST_METHOD;

    /**
     * 删除待摆放清单产品的URL
     */
    public static final String DELETE_QUOTATION_URL = "?method=" + DELETE_QUOTATION_METHOD;
    /**
     * 创建设计
     */
    public static final String CREATE_DESIGN_URL = "?method=" + CREATE_DESIGN_METHOD;
    /**
     * 创建场景或场景添加图片产品
     */
    public static final String DESIGN_SCHEME_URL = "?method=" + DESIGN_SCHEME_METHOD;
    /**
     * 查询设计分享的权限
     */
    public static final String CHECK_SHARE_URL = "?method=" + CHECK_SHARE_METHOD;

    /**
     * 获取设计列表的方法
     */
    public static final String GET_DESIGN_LIST_METHOD = "mybw.getdesignlist";

    /**
     * 获取设计列表的URL
     */
    public static final String GET_DESIGN_LIST_URL = "?method=" + GET_DESIGN_LIST_METHOD;

    /**
     * 获取多货源的URL
     */
    public static final String GET_SKU_MARKET_URL = "?method=mybw.getskumarket";

    /**
     * 获取更多货源
     */
    public static final String GET_SKU_MARKET_METHOD = "mybw.getskumarket";
    /**
     * sku加入待摆放清单的URL
     */
    public static final String ADD_QUOTATION_URL = "?method=" + ADD_QUOTATION_METHOD;
    /**
     * 添加商品到进货列表
     */
    public static final String ADD_PURCART_URL = "?method=" + ADD_PURCART_METHOD;

    /**
     * 修改进货购物车信息
     */
    public static final String UP_CARTGATE_URL = "?method=" + UP_CARTGATE_METHOD;
    /**
     * 批量修改或删除进货购物车信息
     */
    public static final String UP_ALL_CART_URL = "?method=" + UP_ALL_CART_METHOD;

    /**
     * 待摆放清单加入到报价位置的URL
     */
    public static final String ADD_QUOTATION_LOCATION_URL = "?method=" + ADD_QUOTATION_LOCATION_METHOD;

    /**
     * 修改清单产品价格的URL
     */
    public static final String UPDATE_QUOTATION_PRICE_URL = "?method=" + UPDATE_QUOTATION_PRICE_METHOD;

    /**
     * 获取报价单列表的URL
     */
    public static final String QUOTATION_LIST_URL = "?method=" + QUOTATION_LIST_METHOD;

    /**
     * 创建报价单的URL
     */
    public static final String CREATE_QUOTATION_ORDER_URL = "?method=" + CREATE_QUOTATION_ORDER_METHOD;

    /**
     * 获取采购单列表的URL
     */
    public static final String GET_PURCHASE_ORDER_LIST_URL = "?method=" + GET_PURCHASE_ORDER_LIST_METHOD;

    /**
     * 创建采购订单的URL
     */
    public static final String CREATE_PURCHASE_ORDER_URL = "?method=" + CREATE_PURCHASE_ORDER_METHOD;

    /**
     * 报价单再次报价的URL
     */
    public static final String QUOTATION_AGAIN_URL = "?method=" + QUOTATION_AGAIN_METHOD;

    /**
     * 获取收货地址的URL
     */
    public static final String GET_ADDRESS_URL = "?method=" + GET_ADDRESS_METHOD;

    /**
     * 创建或修改收货地址的URL
     */
    public static final String CREATE_OR_MODIFY_SHIPPING_ADDRESS_URL = "?method=" + CREATE_OR_MODIFY_SHIPPING_ADDRESS_METHOD;

    /**
     * 删除收货地址的URL
     */
    public static final String DELETE_SHIPPING_ADDRESS_URL = "?method=" + DELETE_SHIPPING_ADDRESS_METHOD;

    /**
     * 获取城市区域列表的URL
     */
    public static final String GET_CITY_AREA_LIST_URL = "?method=" + GET_CITY_AREA_LIST_METHOD;

    /**
     * 验证修改价格密码的URL
     */
    public static final String VERIFY_PASSWORD_URL = "?method=" + VERIFY_PASSWORD_METHOD;

    /**
     * 设置价格增幅的URL
     */
    public static final String SET_INCREASE_URL = "?method=" + SET_INCREASE_METHOD;

    /**
     * 修改用户基础信息的URL
     */
    public static final String EDIT_USER_INFO_URL = "?method=" + EDIT_USER_INFO_METHOD;
    /**
     * 修改用户基础信息的URL
     */
    public static final String BIND_MARKET_URL = "?method=" + BIND_MARKET_METHOD;

    /**
     * 绑定更换手机的URL
     */
    public static final String BINDING_PHONE_URL = "?method=" + BINDING_PHONE_METHOD;

    /**
     * 微信绑定的URL
     */
    public static final String BINDING_WECHAT_URL = "?method=" + BINDING_WECHAT_METHOD;

    /**
     * 用户反馈接口的URL
     */
    public static final String FEED_BACK_URL = "?method=" + FEED_BACK_METHOD;

    /**
     * 修改水印图片的URL
     */
    public static final String UPDATE_WATERMARK_URL = "?method=" + UPDATE_WATERMARK_METHOD;

    /**
     * 版本更新的URL
     */
    public static final String APP_UPDATE_URL = "?method=" + APP_UPDATE_METHOD;

    /**
     * 修改密码的URL
     */
    public static final String MODIFY_PASSWORD_URL = "?method=" + MODIFY_PASSWORD_METHOD;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 公共令牌
     */
    public static final String PUBLIC_TOKEN = "100YBWIN1l1QvmxqCFp9g23/yJNRwIby3qcCAwEAAQ==";

    /**
     * 服务协议的URL
     */
    public static final String AGREEMENT_URL = "s";

    /**
     * 使用帮助的URL
     */
    public static final String USING_HELP_URL = "https://www.showdoc.com.cn/yibaiapp";
}
