package com.ximai.savingsmore.save.modle;

import android.graphics.Bitmap;

//分享的数据
public class ShareData {
    public String dialogTitle;
	private String imagepath;
	private String text;
	private String url;
	private String imageUrl;
	private String title;
	private String site;
	private String siteurl;
	private String titleurl;
	private Bitmap bitmap;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImagePath() {
		return imagepath;
	}
	public void setImagePath(String imagepath) {
		this.imagepath = imagepath;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getSite() {
		return site;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getSiteUrl() {
		return siteurl;
	}
	public void setSiteUrl(String siteUrl) {
		this.siteurl = siteUrl;
	}

	public String getTitleurl() {
		return titleurl;
	}
	public void setTitleUrl(String titleurl) {
		this.titleurl = titleurl;
	}

}
