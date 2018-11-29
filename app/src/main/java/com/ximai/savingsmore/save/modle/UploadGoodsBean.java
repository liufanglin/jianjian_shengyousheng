package com.ximai.savingsmore.save.modle;



import java.io.Serializable;

/**
 * @author luxing
 * @Description
 */
public class UploadGoodsBean implements Serializable {
    //原来方格图片bean
    public String ImageId;
    public String  ImagePath;
    public String SortNo;
    public String Id;
    public String FilePath;


    private String url;
    private Boolean isNet;

    public UploadGoodsBean() {
        super();
    }
    public UploadGoodsBean(String url, Boolean isNet) {
        super();
        this.url = url;
        this.isNet = isNet;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Boolean getIsNet() {
        return isNet;
    }
    public void setIsNet(Boolean isNet) {
        this.isNet = isNet;
    }



}
