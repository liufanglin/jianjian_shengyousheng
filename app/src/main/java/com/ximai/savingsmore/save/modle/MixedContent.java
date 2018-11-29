package com.ximai.savingsmore.save.modle;

/**
 * Created by wangqiang on 2017/8/16.
 */

public class MixedContent {
    String url;
    int resId;
    int type;//0 图片 1 视频

    public int getBioaji() {
        return bioaji;
    }

    public void setBioaji(int bioaji) {
        this.bioaji = bioaji;
    }

    private int bioaji;//标记索引

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MixedContent(String url) {
        this.url = url;
    }


    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public MixedContent(int resId, int type) {
        this.resId = resId;
        this.type = type;
    }

    public MixedContent(String url, int type) {
        this.url = url;
        this.type = type;
    }

    public MixedContent(String url, int type,int bioaji) {
        this.url = url;
        this.type = type;
        this.bioaji = bioaji;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
