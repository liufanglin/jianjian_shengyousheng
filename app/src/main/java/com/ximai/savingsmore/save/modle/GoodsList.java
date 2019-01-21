package com.ximai.savingsmore.save.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by caojian on 16/11/23.
 */
public class GoodsList implements Serializable {
    public  Boolean IsSuccess;
    public  String Message;
    public int TotalPageCount;
    public  List<Goods> MainData;
    public  List<Goods> ShowData;
}
