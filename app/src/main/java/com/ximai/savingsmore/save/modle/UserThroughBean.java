package com.ximai.savingsmore.save.modle;

import java.util.List;

/**
 * Created by Administrator on 2018/3/24 0024.
 */

public class UserThroughBean {
    public boolean IsSuccess;
    public String Message;
    public String Id;
    public List<MainData> MainData;
    public String ShowData;
    public String OtherData;
    public String TotalRecordCount;
    public String TotalPageCount;
    public String AllTotalRecordCount;

    public class MainData {
        public String Id;
        public String Name;
        public String Sex;
        public String Post;
        public String Address;
        public String JoinTime;
        public String JoinTimeName;
    }
}
