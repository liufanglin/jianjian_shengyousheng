package com.ximai.savingsmore.library.net;

import android.text.TextUtils;
import android.util.Log;


import com.loopj.android.http.RequestParams;
import com.ximai.savingsmore.save.modle.Images;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.MyProduct;
import com.ximai.savingsmore.save.modle.ProductParamModle;
import com.ximai.savingsmore.save.modle.PushSettings;
import com.ximai.savingsmore.save.modle.SearchLocationModle;
import com.ximai.savingsmore.save.modle.UserParameter;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author wangguodong
 */
public class RequestParamsPool {


//    //统一加密处理
//    public static String encryptData(String url, String str) {
//
//        String result = "";
//
//        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(str)) {
//            LogUtils.instance.d("您请求加密的数据错误####");
//            return result;
//        }
//
//        String urlStr = url.substring(url.lastIndexOf("?")+1, url.length());
//
//        try {
//            result = DesUtils.encode(str,urlStr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//
//    }

    //登录参数
    public static RequestParams getLoginParams(String phone, String password, String pusuId, int type) {
        RequestParams params = new RequestParams();
        params.put("UserName", phone);
        params.put("Password", password);
        params.put("UserType", type);
        params.put("PushId", pusuId);
        return params;
    }
    // 发送验证码
    public static RequestParams getCodeParams(String UserName, int CodeType) {
        RequestParams params = new RequestParams();
        params.put("UserName", UserName);
        params.put("CodeType", CodeType);
        return params;
    }
    // 发送验证码
    public static RequestParams getCodeParams2(String UserName,String ImageCode, int CodeType,String DeviceId,String CountryCode) {
        RequestParams params = new RequestParams();
        params.put("UserName", UserName);
        params.put("CodeType", CodeType);
        params.put("ImageCode", ImageCode);
        params.put("DeviceId", DeviceId);
        params.put("CountryCode", CountryCode);
        return params;
    }

    // 发送验证码
    public static RequestParams getCodeParams1(String UserName, int CodeType,String ImageCode,String DeviceId,String CountryCode) {
        RequestParams params = new RequestParams();
        params.put("UserName", UserName);
        params.put("CodeType", CodeType);
        params.put("ImageCode", ImageCode);
        params.put("DeviceId", DeviceId);
        params.put("CountryCode", CountryCode);
        return params;
    }

    /**
     * 是否注册
     * @return
     */
    public static RequestParams isSignup(String PhoneNumber) {
        RequestParams params = new RequestParams();
        params.put("PhoneNumber", PhoneNumber);
        Log.e("tag",params.toString());
        return params;
    }

    //注册
    public static RequestParams getRegristes(String phone, String password, String confimPassword, String code, int type) {
        RequestParams params = new RequestParams();
        params.put("PhoneNumber", phone);
        params.put("Password", password);
        params.put("ConfirmPassword", confimPassword);
        params.put("Code", code);
        params.put("UserType", type);
        return params;
    }

    //找回密码
    public static RequestParams getResetPassword(String phone, String password, String Code) {
        RequestParams params = new RequestParams();
        params.put("PhoneNumber", phone);
        params.put("NewPassword", password);
        params.put("ConfirmPassword", password);
        params.put("Code", Code);
        return params;
    }

    //得到所有商品
    public static StringEntity getAllGoods(String IsPromotion, String Provice, String City, String Area, String Longitude, String Latitude, int PageNo, int PageSize, Boolean IsRebateDesc, Boolean IsPriceDesc, Boolean IsStartTimeDesc, Boolean IsDistanceDesc, String Keyword,
                                           String isBag, String isState, String class1, String class2, String brand, String type) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("PageNo", PageNo);
            object.put("PageSize", PageSize);
            object1.put("PageParameter", object);
            object1.put("IsPromotion", true);//是否是在促销日期中的
            if (null != Longitude && !TextUtils.isEmpty(Longitude)) {
                object1.put("Longitude", Longitude);
            }
            if (null != IsPromotion) {
                object1.put("IsPromotion", IsPromotion);
            }
            if (null != Area && !TextUtils.isEmpty(Area)) {
                object1.put("AreaId", Area);
            }
            if (null != Provice && !TextUtils.isEmpty(Provice)) {
                object1.put("ProvinceId", Provice);
            }
            if (null != City && !TextUtils.isEmpty(City)) {
                object1.put("CityId", City);
            }
            if (null != Latitude && !TextUtils.isEmpty(Latitude)) {
                object1.put("Latitude", Latitude);
            }
            if (null != class1 && !TextUtils.isEmpty(class1)) {
                object1.put("FirstClassId", class1);
            }
            if (null != class2 && !TextUtils.isEmpty(class2)) {
                object1.put("SecondClassId", class2);
            }
            if (null != brand && !TextUtils.isEmpty(brand)) {
                object1.put("BrandId", brand);
            }
            if (null != type && !TextUtils.isEmpty(type)) {
                object1.put("PromotionType", type);
            }
            if (null != isBag && !TextUtils.isEmpty(isBag)) {
                object1.put("IsBag", isBag);
            }
//            if (null != isState && !TextUtils.isEmpty(isState)) {
//                object1.put("IsPromotion", isState);
//            }
            if (null != IsRebateDesc) {
                object1.put("IsSaleCountDesc", IsRebateDesc);
            }
            if (null != IsPriceDesc) {
                object1.put("IsPriceDesc", IsPriceDesc);
            }
            if (null != IsStartTimeDesc) {
                object1.put("IsStartTimeDesc", IsStartTimeDesc);
            }
            if (null != IsDistanceDesc) {
                object1.put("IsDistanceDesc", IsDistanceDesc);
            }
            if (!TextUtils.isEmpty(Keyword)) {
                object1.put("Keyword", Keyword);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity = null;
        try {
            // stringEntity  = new StringEntity("{\"Keyword\":\"你\",\"PageParameter\":{\"PageSize\":10,\"PageNo\":1}}");
            stringEntity = new StringEntity(object1.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //得到所有商品
    public static StringEntity getAllGoods(String IsPromotion, String Provice, String City, String Area, String Longitude, String Latitude, int PageNo, int PageSize, Boolean IsRebateDesc, Boolean IsPriceDesc, Boolean IsStartTimeDesc, Boolean IsDistanceDesc,Boolean IsCareCountDesc, String Keyword,
                                           String isBag, String isState, String class1, String class2, String brand, String type) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("PageNo", PageNo);
            object.put("PageSize", PageSize);
            object1.put("PageParameter", object);
            object1.put("IsPromotion", true);//是否是在促销日期中的
            if (null != Longitude && !TextUtils.isEmpty(Longitude)) {
                object1.put("Longitude", Longitude);
            }
            if (null != IsPromotion) {
                object1.put("IsPromotion", IsPromotion);
            }
            if (null != Area && !TextUtils.isEmpty(Area)) {
                object1.put("AreaId", Area);
            }
            if (null != Provice && !TextUtils.isEmpty(Provice)) {
                object1.put("ProvinceId", Provice);
            }
            if (null != City && !TextUtils.isEmpty(City)) {
                object1.put("CityId", City);
            }
            if (null != Latitude && !TextUtils.isEmpty(Latitude)) {
                object1.put("Latitude", Latitude);
            }
            if (null != class1 && !TextUtils.isEmpty(class1)) {
                object1.put("FirstClassId", class1);
            }
            if (null != class2 && !TextUtils.isEmpty(class2)) {
                object1.put("SecondClassId", class2);
            }
            if (null != brand && !TextUtils.isEmpty(brand)) {
                object1.put("BrandId", brand);
            }
            if (null != type && !TextUtils.isEmpty(type)) {
                object1.put("PromotionType", type);
            }
            if (null != isBag && !TextUtils.isEmpty(isBag)) {
                object1.put("IsBag", isBag);
            }
//            if (null != isState && !TextUtils.isEmpty(isState)) {
//                object1.put("IsPromotion", isState);
//            }
            if (null != IsCareCountDesc) {
                object1.put("IsCareCountDesc", IsCareCountDesc);
            }
            if (null != IsPriceDesc) {
                object1.put("IsPriceDesc", IsPriceDesc);
            }
            if (null != IsStartTimeDesc) {
                object1.put("IsStartTimeDesc", IsStartTimeDesc);
            }
            if (null != IsDistanceDesc) {
                object1.put("IsDistanceDesc", IsDistanceDesc);
            }
            if (!TextUtils.isEmpty(Keyword)) {
                object1.put("Keyword", Keyword);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity = null;
        try {
            // stringEntity  = new StringEntity("{\"Keyword\":\"你\",\"PageParameter\":{\"PageSize\":10,\"PageNo\":1}}");
            stringEntity = new StringEntity(object1.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }
    //得到所有商品
    public static StringEntity getAllGoods(String IsPromotion, String Provice, String City, String Area, String Longitude, String Latitude, int PageNo, int PageSize, Boolean IsRebateDesc, Boolean IsPriceDesc, Boolean IsStartTimeDesc, Boolean IsDistanceDesc,Boolean IsCareCountDesc, String Keyword,
                                           String isBag, String isState, String class1, String class2, String brand, String type,String Radius) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("PageNo", PageNo);
            object.put("PageSize", PageSize);
            object1.put("PageParameter", object);
            object1.put("IsPromotion", true);//是否是在促销日期中的
            if (null != Longitude && !TextUtils.isEmpty(Longitude)) {
                object1.put("Longitude", Longitude);
            }
            if (null != IsPromotion) {
                object1.put("IsPromotion", IsPromotion);
            }
            if (null != Area && !TextUtils.isEmpty(Area)) {
                object1.put("AreaId", Area);
            }
            if (null != Provice && !TextUtils.isEmpty(Provice)) {
                object1.put("ProvinceId", Provice);
            }
            if (null != City && !TextUtils.isEmpty(City)) {
                object1.put("CityId", City);
            }
            if (null != Latitude && !TextUtils.isEmpty(Latitude)) {
                object1.put("Latitude", Latitude);
            }
            if (null != class1 && !TextUtils.isEmpty(class1)) {
                object1.put("FirstClassId", class1);
            }
            if (null != class2 && !TextUtils.isEmpty(class2)) {
                object1.put("SecondClassId", class2);
            }
            if (null != brand && !TextUtils.isEmpty(brand)) {
                object1.put("BrandId", brand);
            }
            if (null != type && !TextUtils.isEmpty(type)) {
                object1.put("PromotionType", type);
            }
            if (null != isBag && !TextUtils.isEmpty(isBag)) {
                object1.put("IsBag", isBag);
            }
//            if (null != isState && !TextUtils.isEmpty(isState)) {
//                object1.put("IsPromotion", isState);
//            }
            if (null != IsCareCountDesc) {
                object1.put("IsCareCountDesc", IsCareCountDesc);
            }
            if (null != IsPriceDesc) {
                object1.put("IsPriceDesc", IsPriceDesc);
            }
            if (null != IsStartTimeDesc) {
                object1.put("IsStartTimeDesc", IsStartTimeDesc);
            }
            if (null != IsDistanceDesc) {
                object1.put("IsDistanceDesc", IsDistanceDesc);
            }
            if (!TextUtils.isEmpty(Keyword)) {
                object1.put("Keyword", Keyword);
            }
            if (!TextUtils.isEmpty(Radius)){
                object1.put("Radius", Radius);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity = null;
        try {
            // stringEntity  = new StringEntity("{\"Keyword\":\"你\",\"PageParameter\":{\"PageSize\":10,\"PageNo\":1}}");
            stringEntity = new StringEntity(object1.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }
    /**
     * 得到所有商品 - 目前搜索商品
     * @return
     */
    public static StringEntity getAllGoods(String Provice,String Keyword,boolean isBag ,String FirstClassId) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("PageNo", 1);
            object.put("PageSize", 1000);
            object1.put("PageParameter", object);
            object1.put("IsPromotion", true);//是否是在促销日期中的
            //省份
            if (null != Provice && !TextUtils.isEmpty(Provice)) {
                object1.put("ProvinceId", Provice);
            }
            //名称
            if (!TextUtils.isEmpty(Keyword)) {
                object1.put("Keyword", Keyword);
            }
            //是否是产品还是服务
            object1.put("IsBag", isBag);
            object1.put("FirstClassId", FirstClassId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity = null;
        try {
            // stringEntity  = new StringEntity("{\"Keyword\":\"你\",\"PageParameter\":{\"PageSize\":10,\"PageNo\":1}}");
            stringEntity = new StringEntity(object1.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 得到所有商品 - 目前搜索商品
     * @return
     */
    public static StringEntity getAllGoods(String Radius,String Longitude, String Latitude) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("PageNo", 1);
            object.put("PageSize", 1000);
            object1.put("PageParameter", object);
            object1.put("IsPromotion", true);//是否是在促销日期中的
            object1.put("Radius", Radius);
            object1.put("Longitude", Longitude);
            //是否是产品还是服务
            object1.put("Latitude", Latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity = null;
        try {
            // stringEntity  = new StringEntity("{\"Keyword\":\"你\",\"PageParameter\":{\"PageSize\":10,\"PageNo\":1}}");
            stringEntity = new StringEntity(object1.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }


    /**
     * 得到所有商品 - 目前搜索商品
     * @return
     */
    public static StringEntity getAllGoodsNoIsBag(String Provice,String CityId,String Keyword ,String FirstClassId) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("PageNo", 1);
            object.put("PageSize", 1000);
            object1.put("PageParameter", object);
            object1.put("IsPromotion", true);//是否是在促销日期中的
            //省份
            if (null != Provice && !TextUtils.isEmpty(Provice)) {
                object1.put("ProvinceId", Provice);
            }
            if (null != CityId && !TextUtils.isEmpty(CityId)) {
                object1.put("CityId", CityId);
            }
            //名称
            if (!TextUtils.isEmpty(Keyword)) {
                object1.put("Keyword", Keyword);
            }
            object1.put("FirstClassId", FirstClassId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity stringEntity = null;
        try {
            // stringEntity  = new StringEntity("{\"Keyword\":\"你\",\"PageParameter\":{\"PageSize\":10,\"PageNo\":1}}");
            stringEntity = new StringEntity(object1.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 商品搜索 - 18.10.29
     * @return
     */
    public static StringEntity getAllGoodsNoIsBag1(String Provice,String CityId,String Area,String Keyword, String Address,String FirstClassId) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("PageNo", 1);
            object.put("PageSize", 1000);
            object1.put("PageParameter", object);
            //省份
            if (null != Provice && !TextUtils.isEmpty(Provice)) {
                object1.put("ProvinceId", Provice);
            }
            if (null != CityId && !TextUtils.isEmpty(CityId)) {
                object1.put("CityId", CityId);
            }
            if (null != CityId && !TextUtils.isEmpty(Area)) {
                object1.put("AreaId", Area);
            }
            //名称
            if (!TextUtils.isEmpty(Keyword)) {
                object1.put("Keyword", Keyword);
            }
            if (!TextUtils.isEmpty(Address)) {
                object1.put("Address", Address);
            }
            if (!TextUtils.isEmpty(FirstClassId)){
                object1.put("FirstClassId", FirstClassId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity stringEntity = null;
        try {
            // stringEntity  = new StringEntity("{\"Keyword\":\"你\",\"PageParameter\":{\"PageSize\":10,\"PageNo\":1}}");
            stringEntity = new StringEntity(object1.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 得到商品详情 - 添加金纬度进行测试
     */
    public static StringEntity getGoodDetial(String id,String Longitude,String Latitude) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", id);
            object1.put("Longitude", Longitude);
            object1.put("Latitude", Latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;

    }

    //得到用户信息
    public static StringEntity getUserInfo() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    // 得到促销商品
    public static StringEntity getSalesGoods(String SellerId) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("PageNo", 1);
            object.put("PageSize", 1000);
            object1.put("PageParameter", object);
            object1.put("SellerId", SellerId);
            object1.put("IsPromotion", true);//是否是在促销日期中的
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity = null;
        try {
            // stringEntity  = new StringEntity("{\"Keyword\":\"你\",\"PageParameter\":{\"PageSize\":10,\"PageNo\":1}}");
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //得到热门商品
    public static StringEntity getHotGoods(Boolean isBag) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("PageNo", 1);
            object.put("PageSize", 1000);
            object1.put("PageParameter", object);
            // object1.put("IsTop",true);
            object1.put("IsSaleCountDesc", true);
            object1.put("IsPromotion", true);//是否是在促销日期中的
            object1.put("IsBag", isBag);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    public static StringEntity getMySalesGoods(boolean isEnd, boolean isComment, boolean IsHited, boolean IsFavourited) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject object3 = new JSONObject();
        try {
            object.put("PageNo", 1);
            object.put("PageSize", 1000);
            object2.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object3.put("IsEnd", isEnd);
            if (isComment) {
                object3.put("IsCommented", isComment);
            }
            if (IsFavourited) {
                object3.put("IsFavourited", IsFavourited);
            }
            if (IsHited) {
                object3.put("IsHited", IsHited);
            }
            object1.put("SearchParameter", object3);
            object1.put("SecurityStampParameter", object2);
            object1.put("PageParameter", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 商家获取分享的汇总数据
     */
    public static StringEntity getMySharkGoods(boolean isEnd, boolean isComment, boolean IsHited, boolean IsFavourited,boolean IsShared) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject object3 = new JSONObject();
        try {
            object.put("PageNo", 1);
            object.put("PageSize", 1000);
            object2.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object3.put("IsEnd", isEnd);
            if (isComment) {
                object3.put("IsCommented", isComment);
            }
            if (IsFavourited) {
                object3.put("IsFavourited", IsFavourited);
            }
            if (IsHited) {
                object3.put("IsHited", IsHited);
            }
            if (IsShared) {
                object3.put("IsShared", IsShared);
            }
            object1.put("SearchParameter", object3);
            object1.put("SecurityStampParameter", object2);
            object1.put("PageParameter", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 商家获取分享的汇总数据
     */
    public static StringEntity getMyLookThroughGoods(boolean isEnd, boolean isComment, boolean IsHited, boolean IsFavourited,boolean IsShared) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject object3 = new JSONObject();
        try {
            object.put("PageNo", 1);
            object.put("PageSize", 1000);
            object2.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object3.put("IsEnd", isEnd);
            object3.put("IsHited", IsHited);
            if (isComment) {
                object3.put("IsCommented", isComment);
            }
            if (IsFavourited) {
                object3.put("IsFavourited", IsFavourited);
            }
            if (IsShared) {
                object3.put("IsShared", IsShared);
            }
            object1.put("SearchParameter", object3);
            object1.put("SecurityStampParameter", object2);
            object1.put("PageParameter", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }


    // 添加收藏商品
    public static StringEntity addColect(String id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("Id", id);
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 删除返利中心的数据
     * @param id
     * @return
     */
    public static StringEntity deleteRebateData(String id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("Id", id);
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //取消收藏
    public static StringEntity cancelColect(String id, List<String> list) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONArray array = new JSONArray();
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
        }
        try {
            if (null != list && list.size() != 0) {
                object.put("IdList", array);
            }
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            if (null != id && !TextUtils.isEmpty(id)) {
                object.put("IdListJson", id);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //收藏店铺
    public static StringEntity focusBusiness(String id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("Id", id);
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //移除收藏店铺
    public static StringEntity cancelBusiness(String id, List<String> list) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONArray array = new JSONArray();
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
        }
        try {
            if (null != list && list.size() != 0) {
                object.put("IdList", array);
            }
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            if (null != id && !TextUtils.isEmpty(id)) {
                object.put("IdListJson", id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //得到收藏的商品列表
    public static StringEntity collectGoods() {
        StringEntity stringEntity = null;
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object2.put("PageNo", 1);
            object2.put("PageSize", 1000);
            object.put("PageParameter", object2);
            object.put("SecurityStampParameter", object1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 获取返利中心的数据
     * @return
     */
    public static StringEntity getRebateApplyData() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject object3 = new JSONObject();
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object2.put("PageNo", 1);
            object2.put("PageSize", 1000);
            object.put("SecurityStampParameter", object1);//参数1
            object.put("PageParameter", object2);//参数2
            object.put("SearchParameter", object3);//参数3
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //收藏的店铺的烈表
    public static StringEntity collectBusiness() {
        StringEntity stringEntity = null;
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object2.put("PageNo", 1);
            object2.put("PageSize", 1000);
            object.put("PageParameter", object2);
            object.put("SecurityStampParameter", object1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //店铺信息
    public static StringEntity getBusinessMessage(String id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            object.put("Id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return stringEntity;
    }

    //得到评论
    public static StringEntity getGoodsComment(String id) {
        StringEntity stringEntity = null;
        JSONObject object2 = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object2.put("PageNo", 1);
            object2.put("PageSize", 1000);
            object.put("PageParameter", object2);
            object.put("Id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //提交评论
    public static StringEntity submitComment(String Id, String ProductScore, String SellerScore, String SellerScore2, String SellerScore3, String Content
            , List<Images> list, boolean IsAnonymous) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("Id", Id);
            object.put("ProductScore", ProductScore);
            object.put("SellerScore", "0");
            object.put("SellerScore2", "0");
            object.put("SellerScore3", "0");
            object.put("Content", Content);
            object.put("SecurityStampParameter", object1);
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject object2 = new JSONObject();
                object2.put("Id", list.get(i).ImageId);
                object2.put("FilePath", list.get(i).ImagePath);
                object2.put("SortNo", i);
                array.put(object2);
            }
            object.put("Images", array);
            object.put("IsAnonymous", IsAnonymous);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }


    //得到我的信息
    public static StringEntity getMyUserInfo() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //上传图片
    public static StringEntity upLoadImage(File file, String type) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        try {
            object.put("file", file);
            object.put("FileType", type);
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("Password", "15047754A79842C784E56355FC691E6A");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    public static RequestParams upLoad(File file, String type) {
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put("file", file);
            requestParams.put("FileType", type);
            requestParams.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            requestParams.put("Password", "15047754A79842C784E56355FC691E6A");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return requestParams;
    }


    //保存用户信息(商户)
    public static StringEntity saveMessage(UserParameter userParameter) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject object3 = new JSONObject();
        try {
            object2.put("UserDisplayName", userParameter.UserDisplayName);
            object2.put("PhotoId", userParameter.PhotoId);
            object2.put("PhotoPath", userParameter.PhotoPath);
            object2.put("Sex", userParameter.Sex);
            object2.put("Domicile", userParameter.Domicile);
            object2.put("WeChat", userParameter.WeChat);
            object2.put("Post", userParameter.Post);
            object2.put("ProvinceId", userParameter.ProvinceId);
            object2.put("CountryId", userParameter.CountryId);
            object2.put("CityId", userParameter.CityId);
            object2.put("AreaId", userParameter.AreaId);

            object2.put("WeChatImageId", userParameter.WeChatImageId);
            object2.put("WeChatImagePath", userParameter.WeChatImagePath);

            object3.put("FoundingDateName", userParameter.UserExtInfo.FoundingDateName);
            object3.put("StoreName", userParameter.UserExtInfo.StoreName);
            object3.put("OfficePhone", userParameter.UserExtInfo.OfficePhone);
            object3.put("WebSite", userParameter.UserExtInfo.WebSite);
            object3.put("FoundingDate", userParameter.UserExtInfo.FoundingDate);
            object3.put("BusinessLicenseId", userParameter.UserExtInfo.BusinessLicenseId);
            object3.put("BusinessLicensePath", userParameter.UserExtInfo.BusinessLicensePath);
            object3.put("BusinessLicenseNumber", userParameter.UserExtInfo.BusinessLicenseNumber);
            object3.put("LicenseKeyId", userParameter.UserExtInfo.LicenseKeyId);
            object3.put("LicenseKeyPath", userParameter.UserExtInfo.LicenseKeyPath);
            object3.put("LicenseKeyNumber", userParameter.UserExtInfo.LicenseKeyNumber);
            object3.put("EndHours", userParameter.UserExtInfo.EndHours);
            object3.put("StartHours", userParameter.UserExtInfo.StartHours);
            object3.put("IsBag", userParameter.UserExtInfo.IsBag);//是否是产品类还是服务类
            object3.put("FirstClassId", userParameter.UserExtInfo.FirstClassId);//主营商品
            object3.put("IsRebate", userParameter.UserExtInfo.IsRebate);
            object3.put("PhoneNumber", userParameter.UserExtInfo.PhoneNumber);
            object3.put("BusinessDateId", userParameter.UserExtInfo.BusinessDateId);//商家营业日期
            object3.put("RebatePercent", userParameter.UserExtInfo.RebatePercent);//门店消费返利
            object3.put("InvoiceId", userParameter.UserExtInfo.InvoiceId);//商品发票
            object3.put("DeliveryServiceId", userParameter.UserExtInfo.DeliveryServiceId);//送货服务
            object3.put("PremiumId", userParameter.UserExtInfo.PremiumId);//送货保险
            object3.put("VideoId", userParameter.UserExtInfo.VideoId);//视频Id
            object3.put("VideoPath", userParameter.UserExtInfo.VideoPath);//视频路劲
            object3.put("SharedRedPack", userParameter.UserExtInfo.SharedRedPack);//招聘兼职转发促销
            JSONArray jsonArray = new JSONArray();
            JSONObject object4 = new JSONObject();
            JSONObject object5 = new JSONObject();
            if (null != userParameter.BusinessScopes) {
                if (null != userParameter.BusinessScopes.get(0) && userParameter.BusinessScopes.size() > 0) {
//                object5.put("Name", userParameter.BusinessScopes.get(0).DictNode.Name);
//                object5.put("SortNo", userParameter.BusinessScopes.get(0).DictNode.SortNo);
//                object5.put("Id", userParameter.BusinessScopes.get(0).DictNode.Id);
                    object4.put("BusinessScopeType", 1);
                    object4.put("DictNodeId", userParameter.BusinessScopes.get(0).DictNodeId);
                }
            }
            jsonArray.put(object4);
            JSONArray jsonArray1 = new JSONArray();
            if (null != userParameter.UserExtInfo.Images) {
                for (int i = 0; i < userParameter.UserExtInfo.Images.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("ImageId", userParameter.UserExtInfo.Images.get(i).ImageId);
                    jsonObject.put("ImagePath", userParameter.UserExtInfo.Images.get(i).ImagePath);
                    jsonArray1.put(jsonObject);
                }
            }
            object3.put("Images", jsonArray1);
            object2.put("UserExtInfo", object3);
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object2.put("BusinessScopes", jsonArray);
            object.put("SecurityStampParameter", object1);
            object.put("UserParameter", object2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // String newStr = new String(object.toString().getBytes(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET).trim();
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringEntity;
    }

    //保存用户信息(个人)
    public static StringEntity savePersonMessage(UserParameter userParameter) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject object3 = new JSONObject();
        try {
            object2.put("UserDisplayName", userParameter.UserDisplayName);
            object2.put("PhotoId", userParameter.PhotoId);
            object2.put("PhotoPath", userParameter.PhotoPath);
            object2.put("Sex", userParameter.Sex);
            object2.put("WeChat", userParameter.WeChat);
            object2.put("Domicile", userParameter.Domicile);
            object2.put("Post", userParameter.Post);
            object2.put("ProvinceId", userParameter.ProvinceId);
            object2.put("CityId", userParameter.CityId);
            object2.put("AreaId", userParameter.AreaId);
            object2.put("RecipientsTelephone", userParameter.RecipientsTelephone);
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            object.put("UserParameter", object2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // String newStr = new String(object.toString().getBytes(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET).trim();
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //保存商品信息
    public static StringEntity getMyProduct(MyProduct myProduct) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            JSONArray jsonArray = new JSONArray();
            if (myProduct.Images.size() > 0) {
                for (int i = 0; i < myProduct.Images.size(); i++) {
                    JSONObject images = new JSONObject();
                    images.put("ImageId", myProduct.Images.get(i).ImageId);
                    images.put("ImagePath", myProduct.Images.get(i).ImagePath);
                    images.put("SortNo", myProduct.Images.get(i).SortNo);
                    jsonArray.put(images);
                }
            }
            if (null != myProduct.Id) {
                object2.put("Id", myProduct.Id);
            }

            JSONArray jsonArray1 = new JSONArray();
            if (myProduct.ChainStores.size() > 0) {
                for (int i = 0; i < myProduct.ChainStores.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Name", myProduct.ChainStores.get(i).Name);
                    jsonObject.put("LinkMan", myProduct.ChainStores.get(i).LinkMan);
                    jsonObject.put("ContactWay", myProduct.ChainStores.get(i).ContactWay);
                    jsonObject.put("Address", myProduct.ChainStores.get(i).Address);
                    jsonObject.put("SortNo", "1");
                    jsonArray1.put(jsonObject);
                }
            }
            object2.put("ChainStores", jsonArray1);//分店

            object2.put("Images", jsonArray);

            object2.put("Name", myProduct.Name);
            object2.put("Brand", myProduct.Brand);
            //object2.put("Number", myProduct.Number);
            object2.put("FirstClassId", myProduct.FirstClassId);
            object2.put("CountryId", myProduct.CountryId);
            object2.put("ProvinceId", myProduct.ProvinceId);
            object2.put("CityId", myProduct.CityId);
            object2.put("AreaId", myProduct.AreaId);
            object2.put("Address", myProduct.Address);
            object2.put("BrandId", myProduct.BrandId);
            object2.put("StartTime", myProduct.StartTime);
            object2.put("EndTime", myProduct.EndTime);
            object2.put("OriginalPrice", myProduct.OriginalPrice);
            object2.put("PromotionCauseId", myProduct.PromotionCauseId);
            object2.put("DeliveryServiceId", myProduct.DeliveryServiceId);
            object2.put("PremiumId", myProduct.PremiumId);
            object2.put("Price", myProduct.Price);

            if (null != myProduct.PurchaseQuantity && null != myProduct.FreeQuantity){
                object2.put("PurchaseQuantity", myProduct.PurchaseQuantity);
                object2.put("FreeQuantity", myProduct.FreeQuantity);
            }
            object2.put("IsProcurementService", true);
            object2.put("CurrencyId", myProduct.CurrencyId);
            object2.put("UnitId", myProduct.UnitId);
            object2.put("PromotionType", myProduct.PromotionType);
            object2.put("InvoiceId", myProduct.InvoiceId);
            object2.put("Introduction", myProduct.Introduction);
            object2.put("Description", myProduct.Description);
            object2.put("VideoId", myProduct.VideoId);//视频Id
            object2.put("VideoPath", myProduct.VideoPath);//视频path
            object.put("ProductParameter", object2);
            object.put("SecurityStampParameter", object1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    public static StringEntity apply_classity(String name, String ParentId) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            object.put("Name", name);
            object.put("ParentId", ParentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    public static StringEntity save_brand(String name) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            object.put("Name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //商家列表
    public static StringEntity businessList(String keyWord, String ProviceId, String CityId, String AreaId) {
        StringEntity stringEntity = null;
        JSONObject object2 = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object2.put("PageNo", 1);
            object2.put("PageSize", 1000);
            object.put("PageParameter", object2);
            object.put("Keyword", keyWord);
            if (null != ProviceId) {
                object.put("ProvinceId", ProviceId);
            }
            if (null != CityId) {
                object.put("CityId", CityId);
            }
            if (null != AreaId) {
                object.put("AreaId", AreaId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }
    //商家列表
    public static StringEntity businessList1(String ProviceId, String CityId, String AreaId,String keyWord,String Address) {
        StringEntity stringEntity = null;
        JSONObject object2 = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object2.put("PageNo", 1);
            object2.put("PageSize", 1000);
            object.put("PageParameter", object2);
            if (null != keyWord) {
                object.put("Keyword", keyWord);
            }
            if (null != ProviceId) {
                object.put("ProvinceId", ProviceId);
            }
            if (null != CityId) {
                object.put("CityId", CityId);
            }
            if (null != AreaId) {
                object.put("AreaId", AreaId);
            }
            if (!TextUtils.isEmpty(Address)){
                object.put("Address", Address);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //根据环信的到用户信息
    public static StringEntity getUserByIM(String userName) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            array.put(userName);
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            object.put("IMUserNameList", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //发表留言
    public static StringEntity sendMessage(String Id, String name, String city, String number, String email, String qq, String content) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            // object.put("ReceiverId", Id);
            object.put("Name", name);
            object.put("City", city);
            object.put("PhoneNumber", number);
            object.put("Email", email);
            object.put("QQOrWeChat", qq);
            object.put("Content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    public static StringEntity loginOut() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //保存推送
    public static StringEntity savePush(boolean IsPush, List<PushSettings> list) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            object.put("IsPush", IsPush);
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject object2 = new JSONObject();
                object2.put("PushType", list.get(i).PushType);
                object2.put("PushValue", list.get(i).PushValue);
                array.put(object2);
            }
            object.put("PushSettings", array);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //推送商品
    public static StringEntity getPushGood() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            object2.put("PageNo", 1);
            object2.put("PageSize", 1000);
            object.put("PageParameter", object2);
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //移除推送商品
    public static StringEntity removePushGoods(String id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(id);
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            object.put("IdList", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    public static StringEntity getMenuNumber() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        try {
            if (null != LoginUser.getInstance() && null != LoginUser.getInstance().userInfo) {
                object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    public static StringEntity removeGoods(String id) {
        StringEntity stringEntity = null;
        JSONObject jsonObject = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            jsonObject.put("Id", id);
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            jsonObject.put("SecurityStampParameter", object1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //第三方登录
    public static RequestParams thirldLogin(String OpenId, String NickName, String ExternalSigninType,String PushId, String UserType) {
        RequestParams params = new RequestParams();
        params.put("OpenId", OpenId);
        params.put("NickName", NickName);
        params.put("ExternalSigninType", ExternalSigninType);
        params.put("PushId", PushId);
        params.put("UserType", UserType);
        return params;
    }

    //得到积分
    public static StringEntity getPoint() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //得到订单
    public static StringEntity getOrder() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object2.put("PageNo", 1);
            object2.put("PageSize", 1000);
            object1.put("SecurityStampParameter", object);
            object1.put("PageParameter", object2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //分享App
    public static StringEntity sahreApp(String Remark) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", "00000000-0000-0000-0000-000000000000");
            object1.put("Remark", Remark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }


    //分享商品
    public static StringEntity shareProduct(String Id,String Remark) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
            object1.put("Remark", Remark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //分享商品
    public static StringEntity shareProduct(String Id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //更新购物车
    public static StringEntity update_car(String ProductId, String Quantity, String CartOperaType) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("ProductId", ProductId);
            object1.put("Quantity", Quantity);
            object1.put("CartOperaType", CartOperaType);
            object1.put("SecurityStampParameter", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //得到购物车
    public static StringEntity get_car(String isBag) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("IsBag", isBag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 提交订单
     */
    public static StringEntity submit_order(boolean IsUsePoint, String Recipients, String PhoneNumber, String ProvinceId, String CityId, String AreaId, String Address, String InvoiceTitle, String Remark, List<String> list) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("IsUsePoint", IsUsePoint);
            object1.put("Recipients", Recipients);
            object1.put("PhoneNumber", PhoneNumber);
            object1.put("ProvinceId", ProvinceId);
            object1.put("CityId", CityId);
            object1.put("AreaId", AreaId);
            object1.put("Address", Address);
            object1.put("InvoiceTitle", InvoiceTitle);
            object1.put("Remark", Remark);
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
            object1.put("CartIdList", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //支付宝签名
    public static StringEntity alipaySign(String Id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //支付结果
    public static StringEntity payResult(String Id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //积分抵扣
    public static StringEntity jifenDikou(List<String> CartIdList) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            JSONArray array = new JSONArray();
            for (int i = 0; i < CartIdList.size(); i++) {
                array.put(CartIdList.get(i));
            }
            object1.put("CartIdList", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //订单详情
    public static StringEntity orderDetial(String Id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //微信生成预付单
    public static StringEntity weChatSign(String Id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //确认收货
    public static StringEntity queren_order(String Id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //申请退款
    public static StringEntity quit_moneny(String Id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //发货
    public static StringEntity fa_huo(String Id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
            object1.put("DeliveryName", "顺风");
            object1.put("DeliverySn", "111111");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //取消订单
    public static StringEntity cancelOrder(String Id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //清空购物车
    public static StringEntity clearCar() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //绑定邮箱
    public static StringEntity bindEmail(String email, String password) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Password", password);
            object1.put("Email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //回复留言
    public static StringEntity replyComment(String Id, String Content) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
            object1.put("Content", Content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }


    /**
     * 保存非促销品返利申请
     * @param price
     * @return
     */
    public static StringEntity rebateApply(String price,List<Images> ProductImages,List<Images> ReceiptImages,String Address,String RebateWay) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();//全部封装
        JSONObject object1 = new JSONObject();//安全标记
        JSONObject object2 = new JSONObject();//图片
        JSONObject object3 = new JSONObject();//图片
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);

            JSONArray jsonArray = new JSONArray();
            if (ProductImages.size() > 0) {
                for (int i = 0; i < ProductImages.size(); i++) {
                    JSONObject images = new JSONObject();
                    images.put("Id", ProductImages.get(i).Id);
                    images.put("FilePath", ProductImages.get(i).FilePath);
                    images.put("SortNo", i);
                    jsonArray.put(images);
                }
            }
//            object2.put("ProductImages", jsonArray);//图片提交

            JSONArray jsonArray2 = new JSONArray();
            if (ReceiptImages.size() > 0) {
                for (int i = 0; i < ReceiptImages.size(); i++) {
                    JSONObject images = new JSONObject();
                    images.put("Id", ReceiptImages.get(i).Id);
                    images.put("FilePath", ReceiptImages.get(i).FilePath);
                    images.put("SortNo", i);
                    jsonArray2.put(images);
                }
            }
//            object3.put("ReceiptImages", jsonArray2);//图片提交ffff

            object.put("SecurityStampParameter", object1);//将安全标记进行添加
            object.put("ProductImages",jsonArray);//图片进行添加
            object.put("ReceiptImages",jsonArray2);//图片进行添加
            object.put("Price",price);//价格
            object.put("Address",Address);//地址
            object.put("RebateWay",RebateWay);//返利方式

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 线下支付
     * @param Id
     * @return
     */
    public static StringEntity line_pay(String Id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", Id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 我要促销价
     */
    public static StringEntity needCuXiaoPrice(String SellerId,String CartId, String ImageId,String ImagePath,String Price,String Quantity, String CartOperaType) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SellerId", SellerId);
            object1.put("CartId", CartId);
            object1.put("ImageId", ImageId);
            object1.put("ImagePath", ImagePath);
            object1.put("Price", Price);
            object1.put("Quantity", Quantity);
            object1.put("CartOperaType", CartOperaType);
            object1.put("SecurityStampParameter", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }


    /**
     * 获取红包列表
     * @return
     */
    public static StringEntity getRewardData() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject object3 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);//安全参数

            object2.put("PageNo", 1);//pageparameter参数
            object2.put("PageSize", 1000);
            object1.put("PageParameter", object2);

            object3.put("IsEnd","");//搜索参数
            object3.put("IsBag","");
            object3.put("ClassId","");
            object3.put("IsCommented","");
            object3.put("IsHited","");
            object3.put("IsFavourited","");
            object3.put("IsShared","");
            object1.put("SearchParameter", object3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 打开红包
     * @return
     */
    public static StringEntity playReward(String Id,String Remark) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);//安全参数
            object1.put("Id",Id);//红包Id
            object1.put("Remark",Remark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 获取积分返回的红包
     * @return
     */
    public static StringEntity getPointRewardData() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 提现
     * @return
     */
    public static StringEntity withDrawMoney(String TransferType,String Name,String BankCart,String BankName,String BankDeposit,String PhoneNumber) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);//安全参数

            object1.put("TransferType",TransferType);
            object1.put("Name",Name);//姓名
            object1.put("BankCart",BankCart);//银行卡号
            object1.put("BankName",BankName);//银行名称
            object1.put("BankDeposit",BankDeposit);//开户行
            object1.put("PhoneNumber",PhoneNumber);//手机号
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 删除订单
     * @return
     */
    public static StringEntity deleteOrder(String id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object);
            object1.put("Id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }


    /**
     * 靠近商家进行推送
     */
    public static StringEntity businessDistance(double Longitude,double Latitude) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            if (null != LoginUser.getInstance().userInfo.SecurityStamp){
                object.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
                object1.put("SecurityStampParameter", object);
                object1.put("Longitude", Longitude);
                object1.put("Latitude", Latitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object1.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }


    /**
     * 商家获取用户群数据
     */
    public static StringEntity getUserGroup(int PageNo,int PageSize,Boolean IsNumberDesc, Boolean IsSexDesc, Boolean IsAddressDesc, Boolean IsJoinTimeDesc,String Keyword) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            object.put("PageNo", PageNo);
            object.put("PageSize", PageSize);
            object2.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object2);
            object1.put("PageParameter", object);

            object1.put("IsNumberDesc", IsNumberDesc);
            object1.put("IsSexDesc", IsSexDesc);
            object1.put("IsAddressDesc", IsAddressDesc);
            object1.put("IsJoinTimeDesc", IsJoinTimeDesc);
            object1.put("Keyword", Keyword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(object1.toString(),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 用户表现
     */
    public static StringEntity getUserExpressData(int PageNo,int PageSize,Boolean IsNameDesc,Boolean IsOnlinePayDesc,Boolean IsLinePayDesc,Boolean IsFavouriteDesc,Boolean IsSharedDesc,Boolean IsHitDesc,String Keyword) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            object.put("PageNo", PageNo);
            object.put("PageSize", PageSize);
            object1.put("PageParameter", object);

            object2.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object2);

            object1.put("IsNameDesc", IsNameDesc);
            object1.put("IsOnlinePayDesc", IsOnlinePayDesc);
            object1.put("IsLinePayDesc", IsLinePayDesc);
            object1.put("IsFavouriteDesc", IsFavouriteDesc);
            object1.put("IsSharedDesc", IsSharedDesc);
            object1.put("IsHitDesc", IsHitDesc);
            object1.put("Keyword", Keyword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(object1.toString(),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 商户表现
     */
    public static StringEntity getBussExpressData(int PageNo,int PageSize,Boolean IsNameDesc,Boolean IsOnlinePayDesc,Boolean IsLinePayDesc,Boolean IsFavouriteDesc,Boolean IsSharedDesc,Boolean IsHitDesc,String Keyword) {
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            object.put("PageNo", PageNo);
            object.put("PageSize", PageSize);
            object1.put("PageParameter", object);

            object2.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object1.put("SecurityStampParameter", object2);

            object1.put("IsNameDesc", IsNameDesc);
            object1.put("IsOnlinePayDesc", IsOnlinePayDesc);
            object1.put("IsLinePayDesc", IsLinePayDesc);
            object1.put("IsFavouriteDesc", IsFavouriteDesc);
            object1.put("IsSharedDesc", IsSharedDesc);
            object1.put("IsHitDesc", IsHitDesc);
            object1.put("Keyword",Keyword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(object1.toString(),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //推送商品
    public static StringEntity getPushData() {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.put(7);
            object2.put("PageNo", 1);
            object2.put("PageSize", 1000);
            object.put("PageParameter", object2);
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            object.put("TypeList", jsonArray2);
//            object.put("TypeListJson", jsonArray2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }


    //移除推送商品
    public static StringEntity removeBussPushGoods(String id) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(id);
        try {
            object1.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
            object.put("SecurityStampParameter", object1);
            object.put("IdList", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //通过地址获取数据
    public static StringEntity getBussinessLocationSearch(String address) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            if (!TextUtils.isEmpty(address)) {
                object.put("Keywords", address);
            }
            object1.put("PageNo", 1);
            object1.put("PageSize", 1000);
            object2.put("PageParameter",object1);
            object.put("SellerParam",object2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    /**
     * 获取商品搜索的地址结果
     */
    public static StringEntity getGoodAddressSearch(String address) {
        StringEntity stringEntity = null;
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            if (!TextUtils.isEmpty(address)) {
                object.put("Keywords", address);
            }
            object1.put("PageNo", 1);
            object1.put("PageSize", 1000);
            object2.put("PageParameter",object1);
            object.put("ProductParam",object2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            stringEntity = new StringEntity(object.toString(), MyAsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringEntity;
    }
}
