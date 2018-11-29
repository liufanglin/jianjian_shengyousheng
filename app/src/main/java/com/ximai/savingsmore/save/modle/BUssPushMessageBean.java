package com.ximai.savingsmore.save.modle;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27 0027.
 */

public class BUssPushMessageBean {
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
        public String SourceId;
        public String PushRecordType;
        public String Content;
        public String Title;
        public boolean IsReaded;
        public String CreateTime;
        public String CreateTimeName;
    }
}
