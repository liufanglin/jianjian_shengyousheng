package com.ximai.savingsmore.save.modle;

import java.util.List;

/**
 * Created by luck on 2018/1/29 0029.
 * 个人奖赏中心
 */

public class PersonalRewardBean {
//
//    {
//        "IsSuccess": true,
//            "Message": "查询成功！",
//            "Id": null,
//            "MainData": [{
//        "User": null,
//                "ProvideDateName": null,
//                "OpenTimeName": null,
//                "RedPacketTypeName": "我要返利信息",
//                "StateName": "未打开",
//                "TransferTypeName": "转账方式",
//                "UserId": "be89e4e2-a646-44e6-95a3-3d798146cd86",
//                "ProvideDate": null,
//                "OpenTime": null,
//                "RedPacketType": 4,
//                "State": 1,
//                "TransferType": 0,
//                "Price": 1.88,
//                "Name": null,
//                "BankName": null,
//                "BankDeposit": null,
//                "BankCart": null,
//                "PhoneNumber": null,
//                "Remark": null,
//                "Id": "ab10c3f0-08f5-40a9-bb9c-1f5ed20dbd8b",
//                "CreateTime": "2018-01-29T14:24:51.327",
//                "CreateTimeName": "2018-01-29 14:24",
//                "CreateDateName": "2018-01-29"
//    }],
//        "ShowData": 0,//今日金额
//            "OtherData": 0,//累计金额
//            "TotalRecordCount": 1,
//            "TotalPageCount": 1,
//            "AllTotalRecordCount": 0
//    }

    public boolean IsSuccess;
    public String Message;
    public String Id;
    public List<MainData> MainData;
    public String ShowData;
    public String OtherData;
    public String TotalRecordCount;
    public String TotalPageCount;
    public String AllTotalRecordCount;

    public static class MainData{
        //自定义
        public boolean isOpen;

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            isOpen = open;
        }

        public String User;
        public String ProvideDateName;
        public String OpenTimeName;
        public String RedPacketTypeName;
        public String StateName;
        public String TransferTypeName;
        public String UserId;
        public String ProvideDate;
        public String OpenTime;
        public String RedPacketType;
        public String State;
        public String TransferType;
        public String Price;
        public String Name;
        public String BankName;
        public String BankDeposit;
        public String BankCart;
        public String PhoneNumber;
        public String Remark;
        public String Id;
        public String CreateTime;
        public String CreateTimeName;
        public String CreateDateName;


        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }
    }
}
