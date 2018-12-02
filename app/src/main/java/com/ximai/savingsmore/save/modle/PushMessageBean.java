package com.ximai.savingsmore.save.modle;

import java.util.List;

/**
 * Created by luck on 2018/1/12 0012.
 */

public class PushMessageBean {
    public boolean IsSuccess;
    public String Message;
    public String Id;
    public List<MainData> MainData;
    public boolean ShowData;
    public String OtherData;
    public String TotalRecordCount;
    public String TotalPageCount;
    public String AllTotalRecordCount;

    public class MainData{
        public String Id;
        public String ProductId;
        public String Name;
        public String Number;
        public String SharedCount;
        public String HitCount;
        public String FavouriteCount;
        public String CommentCount;
        public String SaleCount;
        public String Image;
        public String StoreName;
        public String Country;
        public String Province;
        public String City;
        public String Area;
        public String Address;
        public String StartTime;
        public String EndTime;
        public String PromotionType;
        public String OriginalPrice;
        public String Currency;
        public String Price;
        public String PurchaseQuantity;
        public String FreeQuantity;
        public String PreferentialPrice;
        public String Preferential;
        public String Rebate;
        public String StartTimeName;
        public String EndTimeName;
        public String PromotionTypeName;
        public String CreateTime;
        public String CreateTimeName;
        public String Longitude;
        public String Latitude;
        public List<ChainStores> ChainStores;
        public String CareCount;//关注数量
        public String StoreCount;//到店人数
    }
}
