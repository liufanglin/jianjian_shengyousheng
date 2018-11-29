package com.ximai.savingsmore.save.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by caojian on 16/12/1.
 */
public class BusinessMessage implements Serializable {
    public List<BusinessScopes> BusinessScopes;
    public List<ChainStores> ChainStores ;
    public  String Longitude;
    public  String Latitude;
    public Area Area;
    public  City City;
    public  Country Country;
    public  Province Province;
    public  String PhotoPath;
    public  UserExtInfo UserExtInfo;
    public   String Domicile;
    public  String ApprovalDateName;
    public  String UserDisplayName;
    public  String ShowName;
    public String PhoneNumber;
    public String Id;
    public  String IMUserName;
    public String Email;
    public String UserName;
    public String Post;
    public String WeChatImageId;
    public String WeChatImagePath;

}
