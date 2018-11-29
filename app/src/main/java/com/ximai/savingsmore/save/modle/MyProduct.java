package com.ximai.savingsmore.save.modle;

import java.util.List;

/**
 * Created by caojian on 16/12/12.
 */
public class MyProduct {
    public  String StartTimeName;
    public String EndTimeName;
    public  String PromotionTypeName;
    public List<Images> Images;//图片
    public List<ChainStores> ChainStores;//分店
    public String Name;
//    public Brand Brand;

    public  String Number;
    public  String Brand;
//    public PromotionCause PromotionCause;
    public PromotionCause PromotionCause;
    public String PromotionCauseId;
    public  String FirstClassId;
    public String SecondClassId;
    public  String CountryId;
    public  String ProvinceId;
    public  String CityId;
    public String AreaId;
    public String Address;
    public String BrandId;
    public String StartTime;
    public String EndTime;
    public String OriginalPrice;
    public String Price;
    public String CurrencyId;
    public  String UnitId;
    public  String PromotionType;
    public  String InvoiceId;
    public  String Introduction;
    public String Description;
    public String PurchaseQuantity;//购买数量
    public String FreeQuantity;//赠送数量

    public String VideoId;//视频Id
    public String VideoPath;//视频路径
    public  String Id;
    public Unit Unit = new Unit();
    public Currency Currency;
    public Invoice Invoice;
    public DeliveryService DeliveryService;//送货服务
    public String DeliveryServiceId;

    public Premium Premium;//送货保险
    public String PremiumId;

    class Brand {
        public String Name;
    }

    class PromotionCause{
        public String Name;
        public String Id;
    }

    class Currency{
        public String Name;
    }

    class Invoice{
        public String Name;
    }

    class DeliveryService{
        public String ParentId;
        public String Name;
        public String SortNo;
        public String IsBag;
        public String Id;
    }

    class Premium{
        public String ParentId;
        public String Name;
        public String SortNo;
        public String IsBag;
        public String Id;
    }
}
