package com.ximai.savingsmore.save.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by caojian on 16/11/21.
 */
public class Goods implements Serializable {


    public int Quantity;
    //是否可编辑
    public Boolean IsEditor = false;
    //是否选中
    public Boolean IsSelect = false;
    //是否已读
    public Boolean IsRead = false;

    public String HitCount;

    public String Number;

    public String SharedCount;

    public String Score;

    public String Area;

    public String IsFavourite;

    public String OriginalPrice;

    public String IsShared;

    public String Rebate;

    public String PromotionType;

    public String PurchaseQuantity;

    public Double PreferentialPrice;

    public String StoreName;

    public List<ChainStores> ChainStores;

    public String Image;

    public String Latitude;

    public String StartTime;

    public String Distance;

    public String FavouriteCount;

    public String CommentCount;

    public String SaleCount;

    public String SalesCount;

    public String StartTimeName;

    public String Country;

    public String Province;

    public String City;

    public String Name;

    public String Preferential;

    public String FreeQuantity;

    public String EndTimeName;

    public String Price ;

    public String Currency ;

    public String Address;

    public String Longitude;

    public String EndTime;

    public String ProductId;
    public String Id;//现在这边是购物车的id

    public String PromotionTypeName;
    public String ImageId;
    public String CreateTimeName;
    public String CareCount;//关注数量
    public String StoreCount;//到店人数
}
