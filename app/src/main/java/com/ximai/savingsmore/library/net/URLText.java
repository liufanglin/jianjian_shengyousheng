package com.ximai.savingsmore.library.net;


import com.ximai.savingsmore.library.constants.AppConstants;

/**
 * @author wangguodong
 */
public interface URLText {
    public static final String img_url = "http://fileupload.savingsmore.com/ServerFiles/";
    public static final String upLoad = "http://fileupload.savingsmore.com/api/";

    public static final String baseUrl = AppConstants.BASEURL;
    //登录接口
    public static final String LOGIN_URL = baseUrl + "Account/Signin";
    //发送验证码
    public static final String SEND_CODE = baseUrl + "SMS/SendCode";
    //是否注册
    public static final String IS_SIGNUP = baseUrl + "Account/IsSignup";
    //注册
    public static final String REGISTER_CODE = baseUrl + "Account/Signup";
    //找回密码
    public static final String RESET_PASSWORD = baseUrl + "Account/ForgetPassword";
    //得到商品
    public static final String GET_GOODS = baseUrl + "Product/QueryProductList";
    //得到商品详情
    public static final String GET_GOOD_DETIAL = baseUrl + "Product/Detail";
    //得到用户的信息
    public static final String GET_USERINFO = baseUrl + "User/QueryMyInfo";
    //    //得打商店促销的商品
    public static final String GET_SALES_GOODS = baseUrl + "Product/QueryMyProduct";
    //添加收藏商品
    public static final String ADD_COLLECT = baseUrl + "Product/Favourite";
    //取消收藏商品
    public static final String CANCEL_COLLECT = baseUrl + "Product/RemoveFavourite";
    // 添加收藏店铺
    public static final String ADD_COLLECT_BUSINESS = baseUrl + "User/Favourite";
    //取消收藏店铺
    public static final String CANCEL_COLLECT_BUSINESS = baseUrl + "User/RemoveFavourite";
    //收藏的商品
    public static final String COLLECT_GOODS = baseUrl + "Product/QueryFavourite";
    //收藏的店铺
    public static final String COLLECT_BUSINESS = baseUrl + "User/QueryFavourite";
    //获取店铺 的信息
    public static final String USER_DETIAL = baseUrl + "User/Detail";
    //商品评论
    public static final String GOODS_COMMENT = baseUrl + "Comment/QueryComment";
    //发表评论
    public static final String SUBMIT_COMMENT = baseUrl + "Comment/SubmitComment";
    //上传图片
    public static final String UP_LOAD = baseUrl + "FileManager/UploadFile";
    //查询基础字典
    public static final String QUERYDICNODE = baseUrl + "DictNode/QueryDictNode";
    //查询基础字典3
    public static final String QUERYDICNODE3 = baseUrl + "DictNode/QueryDictNode2";
    //查询基础字典2
    public static final String QUERYDICNODE2 = baseUrl + "DictNode/QueryDictNode3";
    //获取我的资料
    public static final String OUERY_MYINFO = baseUrl + "User/QueryMyInfo";
    //上传图片
    public static final String UPLOAD_IMAGE = upLoad + "FileManager/UploadFile";
    //保存个人信息
    public static final String SAVE_MESSAGE = baseUrl + "User/SaveMyInfo";
    //保存商品信息
    public static final String SAVEMYPRODUCT = baseUrl + "Product/SaveMyProduct";
    //申请分类
    public static final String APPLY_CLASSITY = baseUrl + "Product/SaveProductClass";
    //申请品牌
    public static final String SAVEBRAND = baseUrl + "Product/SaveBrand";
    //商家列表
    public static final String BUSINESS_LIST = baseUrl + "User/QuerySellerList";
    //得到环信
    public static final String USERBYIM = baseUrl + "User/QueryUserByIM";
    //发表留言
    public static final String SEND_MESSAGE = baseUrl + "Message/SendMessage";
    //退出登录
    public static final String LOGIN_OUT = baseUrl + "Account/Signout";
    //保存推送设置
    public static final String SET_PUSH = baseUrl + "User/SavePush";
    //推送商品
    public static final String PUSH_GOODS = baseUrl + "Product/QueryPush";
    //移除推送的商品
    public static final String REMOVE_PUSH_GOODS = baseUrl + "Product/RemovePush";
    //查询注册人数
    public static final String REGISTER_NUMBER = baseUrl + "Account/QueryRegisterStatistics";
    //侧栏统计
    public static final String MENU_NUMBER = baseUrl + "User/QueryUserStatistics";
    //移除商品
    public static final String REMOVE_MYGOODS = baseUrl + "Product/RemoveMyProduct";
    //第三方登录
    public static final String THIRD_LOGIN = baseUrl + "Account/ExternalSignin";
    //积分统计
    public static final String GET_POINT = baseUrl + "PointRecord/QueryPointStatistics";
    //我的订单
    public static final String GET_ORDER = baseUrl + "Order/QueryMyOrder";

    //商铺订单
    public static final String BUSINESS_GET_ORDER = baseUrl + "Order/QuerySellerOrder";
    //分享App
    public static final String SHARE_APP = baseUrl + "Account/SharedApp";
    //分享商品
    public static final String SHARE_PRODUCT = baseUrl + "Product/Shared";
    //更新购物车
    public static final String UPDATE_CAR = baseUrl + "Cart/UpdateProduct";
    //得到购物车
    public static final String GET_MYCAR = baseUrl + "Cart/QueryMyCart";
    //提交订单
    public static final String SUBMIT_ORDER = baseUrl + "Order/SubmitOrder";
    //支付宝支付签名
    public static final String ALIPAY_SIGN = baseUrl + "Order/AlipaySign";
    //查看支付结果
    public static final String PAY_RESULT = baseUrl + "Order/PayResult";
    //积分抵扣
    public static final String JIFEN_DIKOU = baseUrl + "Order/PreviewOrder";
    //订单详情
    public static final String ORDER_DETIAL = baseUrl + "Order/MyOrderDetail";
    //商家订单详情
    public static final String BUSINESS_ORDER_DETIAL = baseUrl + "Order/SellerOrderDetail";
    //微信生成交易单
    public static final String WECHAT_SIGN = baseUrl + "Order/WeChatPayUnifiedOrder";
    //客服列表
    public static final String SERVICE_LIST = baseUrl + "DictNode/QueryService";
    //确认订单
    public static final String RECEIPEMY_ORDER = baseUrl + "Order/ReceipteMyOrder";
    //申请退款
    public static final String QUIT_MONEY = baseUrl + "Order/RefundMyOrder";
    //待支付的订单进行取消
    public static final String CANCEL_MY_MONEY = baseUrl + "Order/CancelMyOrder";
    //发货
    public static final String FAHUO = baseUrl + "Order/ShippeSellerOrder";
    //商家的取消订单
    public static final String CANAEL = baseUrl + "Order/SellerCancelOrder";
    //清空购物车f
    public static final String CLEAR_CAR = baseUrl + "Cart/ClearMyCart";
    //绑定邮箱
    public static final String bindemail = baseUrl + "User/BindEmail";
    //回复评论
    public static final String replyComment = baseUrl + "Comment/ReplyComment";
    //得到红包个数
    public static final String redPacket = baseUrl + "User/QueryReadPacketCount";
    //获取返利申请中心数据
    public static final String REBATEAPPLY = baseUrl + "RebateRecord/QueryMyRebate";
    //非促销品申请返利
    public static final String REBATEAPPLYDATA = baseUrl + "RebateRecord/SubmitRebate";
    //线下支付
    public static final String LINE_PAY = baseUrl + "Order/LinePayOrder";
    //我要促销价
    public static final String NEED_CUXIAOJIA = baseUrl + "Cart/UpdateNonPromotionProduct";
    //删除返利中心数据
    public static final String DELETE_REBATEDATA = baseUrl + "RebateRecord/Remove";
    //获取红包列表
    public static final String GET_REWADRDATA = baseUrl + "RedPacketRecord/QueryRedPacked";
    //打开红包
    public static final String PLAY_REWARD = baseUrl + "RedPacketRecord/OpenRedPacked";
    //获取积分返回的红包
    public static final String PLAY_POINTREWARD = baseUrl + "PointRecord/ExchangeRedPacket";
    //提现红包
    public static final String PLAY_TRANSFER = baseUrl + "RedPacketRecord/Withdrawals";
    //个人删除订单
    public static final String PERSONAL_DETELEORDER = baseUrl + "Order/RemoveMyOrder";
    //商家删除订单
    public static final String BUSINESS_DETELEORDER = baseUrl + "Order/RemoveSellerOrder";
    //附近促销商家
    public static final String BUSINESS_DISTANCE = baseUrl + "User/NearSeller";
    //用户群数据
    public static final String BUSINESS_USERGROUP = baseUrl + "User/QueryUserGroup";
    //用户表现
    public static final String USER_EXPRESS = baseUrl + "User/QueryUserCollect";
    //商户表现
    public static final String BUSS_EXPRESS = baseUrl + "Product/QueryProductCollect";
    //商户推送消息
    public static final String BUSS_PUSH = baseUrl + "PushRecord/QueryPushList";
    //移除商家推送消息
    public static final String REMOVE_PUSH_BUSS = baseUrl + "PushRecord/RemovePush";
    //图片验证码
    public static final String PHOTO_CODE = "http://api.savingsmore.com/Home/VerificationCode?deviceId=";
    //商品地址列表
    public static final String SEARCH_LOCATION_DATA = baseUrl + "Product/QueryPlace";
    //商家地址列表
    public static final String SEARCH_BUSINESS_LOCATION_DATA = baseUrl + "User/QueryPlace";
}
