package com.ximai.savingsmore.save.modle;

/**
 * Created by luck on 2017/12/15 0015.
 */

public class ProduceBean {

    private String ParentId;
    private String Id;
    private String SortNo;

    private String IsBag;
    public int image;
    public String font;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public ProduceBean(int image, String font,String ParentId,String Id,String SortNo,String IsBag) {
        this.image = image;
        this.font = font;
        this.ParentId = ParentId;
        this.Id = Id;
        this.SortNo = SortNo;
        this.IsBag = IsBag;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSortNo() {
        return SortNo;
    }

    public void setSortNo(String sortNo) {
        SortNo = sortNo;
    }

    public String getIsBag() {
        return IsBag;
    }

    public void setIsBag(String isBag) {
        IsBag = isBag;
    }
}
