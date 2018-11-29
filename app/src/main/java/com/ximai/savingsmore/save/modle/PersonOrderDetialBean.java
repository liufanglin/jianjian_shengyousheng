package com.ximai.savingsmore.save.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by caojian on 17/1/6.
 */
public class PersonOrderDetialBean implements Serializable{
    public OrderUser User;
    public List<Goods> OrderProducts;

    public String Number;
    public String SellerId;
    public String TotalPrice;
    public String Price;
    public String Currency;
    public String Recipients;
    public String PhoneNumber;
    public String ProvinceId;
    public String CityId;
    public String Address;
    public String InvoiceTitle;
    public String Remark;
    public String PayType;
    public String PayAppName;
    public String PayApp;
    public String PayOrderSn;
    public String OrderStateName;
    public String DeductionPoint;
    public String Id;
    public int OrderState;
    public Seller Seller;
    public Province Province;
    public City City;

    public class Seller implements Serializable{
        public UserExtInfo UserExtInfo;
        public String Domicile;
    }
}
