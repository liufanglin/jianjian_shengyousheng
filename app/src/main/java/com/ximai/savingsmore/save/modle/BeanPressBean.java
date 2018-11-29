package com.ximai.savingsmore.save.modle;

import java.util.List;

/**
 * Created by luck on 2018/3/25 0025.
 */

public class BeanPressBean {
    public boolean IsSuccess;
    public String Message;
    public String Id;
    public List<MainData> MainData;
    public ShowData ShowData;
    public String OtherData;
    public String TotalRecordCount;
    public String TotalPageCount;
    public String AllTotalRecordCount;

    public class MainData {

        //是否选中
        public Boolean IsSelect1;
        public Boolean IsSelect2;
        public Boolean IsSelect3;
        public Boolean IsSelect4;
        public Boolean IsSelect5;
        public Boolean IsSelect6;
        public String Id;
        public String Name;
        public String OnlinePay;
        public String LinePay;
        public String Favourite;
        public String Shared;
        public String Hit;

    }

    public class ShowData {
        public String Id;
        public String Name;
        public String OnlinePay;
        public String LinePay;
        public String Favourite;
        public String Shared;
        public String Hit;
    }
}
