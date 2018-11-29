package com.ximai.savingsmore.save.modle;

import java.io.Serializable;

/**
 * Created by caojian on 17/1/4.
 */
public class OrderProducts implements Serializable{
    public String Currency;
    public String OrderId;
    public String Name;
    public String Address;
    public String ProductId;
    public String CreateTimeName;
    public String StoreName;

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }
}
