package com.ximai.savingsmore.save.modle;

import java.io.Serializable;

/**
 * Created by caojian on 16/12/5.
 */
public class BaseMessage  implements Serializable{
    public  boolean isSelect;
    public  String ParentId;
    public String Name;
    public String SortNo;
    public  String IsBag;
    public String Id;
}
