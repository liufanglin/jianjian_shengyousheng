package com.ximai.savingsmore.save.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class submitOrderResults implements Serializable {
    public boolean IsSuccess;
    public String Message;
    public String Id;
    public List<MainData> MainData;
    public String ShowData;
    public String OtherData;
    public int TotalRecordCount;
    public int TotalPageCount;
    public int AllTotalRecordCount;


    public class MainData implements Serializable{
        public String Number;
        public String SellerId;
        public String TotalPrice;
        public String PreferentialPrice;
        public String SellerPreferentialPrice;
        public double PointPreferentialPrice;
        public String DeductionPoint;
        public double Price;
        public String Recipients;
        public String PhoneNumber;
        public String ProvinceId;
        public String CityId;
        public String AreaId;
        public String Address;
        public String InvoiceTitle;
        public String Remark;
        public String OrderState;
        public String PayType;
        public String PayApp;
        public String PayOrderSn;
        public String RefundSn;
        public String IsNormalApplyRefund;
        public String DeliveryName;
        public String DeliverySn;
        public boolean IsBag;
        public boolean IsDeleted;
        public boolean IsDeletedBySeller;
        public String PaySecurityStamp;
        public String RebatePercent;
        public List<OrderDynamics> OrderDynamics;
        public List<OrderProducts> OrderProducts;
        public User User;
        public Seller Seller;
        public Province Province;
        public City City;
        public Area Area;
        public String OrderStateName;
        public String PayAppName;
        public String Id;
        public String CreateTime;
        public String CreateTimeName;
        public String CreateDateName;
    }

    public class Seller implements Serializable{
        public Area Area;
        public City City;
        public Province Province;
        public UserExtInfo UserExtInfo;
        public String UserDisplayName;
        public String PhotoId;
        public String PhotoPath;
        public String NickName;
        public boolean Sex;
        public String Birthday;
        public String BirthPlace;
        public String Domicile;
        public String QQ;
        public String Weibo;
        public String WeChat;
        public double Longitude;
        public double Latitude;
        public String IMId;
        public String IMUserName;
        public String ApprovalDate;
        public String Post;
        public String ProvinceId;
        public String CityId;
        public String AreaId;
        public String Point;
        public boolean IsVIP;
        public String RecipientsTelephone;
        public String ShowName;
        public String ApprovalDateName;
        public String VipLevel;
        public String VipLevelName;
        public String Email;
        public boolean EmailConfirmed;
        public String PhoneNumber;
        public boolean PhoneNumberConfirmed;
        public String Id;
        public String UserName;
    }
}
