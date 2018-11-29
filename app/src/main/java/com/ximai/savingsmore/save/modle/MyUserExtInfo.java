package com.ximai.savingsmore.save.modle;

import java.util.List;

/**
 * Created by caojian on 16/12/9.
 */
public class MyUserExtInfo {
    public  String FoundingDateName;
    public List<Images> Images;
    public String ImagesJson;
    public  String StoreName;//
    public String OfficePhone;
    public String PhoneNumber;
    public String RebatePercent;//门店消费返利
    public String WebSite;
    public String FoundingDate;
    public String  BusinessLicenseId;
    public String BusinessLicensePath;
    public String BusinessLicenseNumber;
    public String LicenseKeyId;
    public String LicenseKeyPath;
    public String LicenseKeyNumber;
    public String HitCount;
    public String StartHours;
    public String EndHours;
    public boolean IsBag;
    public boolean IsRebate;//促销品消费返利
    public String BusinessDateId;//营业日期
    public String FirstClassId;//商品类别
    public String InvoiceId;//商品发票
    public String DeliveryServiceId;//送货服务
    public String PremiumId;//送货保险
    public String VideoId;//视频Id
    public String VideoPath;//视频地址
    public String SharedRedPack;

    public BusinessDate BusinessDate = new BusinessDate();
    public Invoice Invoice = new Invoice();
    public DeliveryService DeliveryService = new DeliveryService();
    public Premium Premium = new Premium();
    public FirstClass FirstClass = new FirstClass();
    public SecondClass SecondClass = new SecondClass();
}