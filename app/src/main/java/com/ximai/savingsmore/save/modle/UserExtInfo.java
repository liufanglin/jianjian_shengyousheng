package com.ximai.savingsmore.save.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by caojian on 16/11/28.f
 *
 *
 */
public class UserExtInfo  implements Serializable{
    public String FoundingDateName;//公司成立时间

    public List<Images> Images;

    public String ImagesJson;

    public String StoreName;

    public String OfficePhone;
    public String PhoneNumber;

    public String WebSite;

    public String FoundingDate;

    public String BusinessLicenseId;

    public String BusinessLicensePath;

    public String BusinessLicenseNumber;

    public String LicenseKeyId;

    public String LicenseKeyPath;

    public String LicenseKeyNumber;

    public String HitCount;

    public String SharedCount;

    public String FavouriteCount;

    public String CommentCount;

    public String Score;

    public String Score2;

    public String Score3;

    public String StartHours;

    public String EndHours;

    public String Counselor;

    public String CounselorPhoneNumber;

    public String Counselor2;

    public String CounselorPhoneNumber2;

    public Boolean IsBag;

    public boolean IsRebate;

    public String Id;

    public String CreateTime;

    public String CreateTimeName;
    public String VideoId;
    public String VideoPath;

    public String CreateDateName;
    public String RebatePercent;//门店消费返利

    public String InvoiceId;//商品类别
    public String DeliveryServiceId;//送货服务
    public String PremiumId;//送货保险

    public Invoice Invoice = new Invoice();//商家发票
    public DeliveryService DeliveryService = new DeliveryService();
    public Premium Premium = new Premium();

    public BusinessDate BusinessDate = new BusinessDate();
    public String BusinessDateId;//营业日期
    public String SharedRedPack;//招聘兼职转发促销


    public FirstClass FirstClass = new FirstClass();
    public String SecondClassId;//主营商品

    public SecondClass SecondClass = new SecondClass();
    public String FirstClassId;//商品类别

}
