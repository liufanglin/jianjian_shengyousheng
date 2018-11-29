package com.ximai.savingsmore.save.modle;

import java.util.List;

/**
 * Created by caojian on 16/11/30.
 */
public class Business {

    //是否可编辑
    public  Boolean IsEditor=false;
    //是否选中
    public  Boolean IsSelect=false;
    public String Id;
    public String UserId;
    public  String StoreName;
    public  String PhotoPath;
    public  String Province;
    public String City;
    public  String Area;
    public  String CreateTime;
    public  String CreateTimeName;
    public  String CreateDateName;
    public List<Images> Images;

}
