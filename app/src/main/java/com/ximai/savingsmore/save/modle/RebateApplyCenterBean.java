package com.ximai.savingsmore.save.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luxing on 2018/1/9 0009.
 */

public class RebateApplyCenterBean implements Serializable{
    public boolean IsSuccess;
   public String Message;
   public String Id;
   public List<MainData> MainData;
   public String ShowData;
   public String OtherData;
   public int TotalRecordCount;
   public int TotalPageCount;
   public int AllTotalRecordCount;

    public class MainData implements Serializable{
        public String User;
        public String StateName;//123 ---申请通过驳回
        public String RebateWayName;//123 ---申请通过驳回
        public String Price;
        public String Point;
        public String Address;
        public String State;
        public String Number;
        public List<Images> ProductImages;
        public List<Images> ReceiptImages;
        public String Id;
        public String CreateTime;
        public String CreateTimeName;
        public String CreateDateName;
    }
}
