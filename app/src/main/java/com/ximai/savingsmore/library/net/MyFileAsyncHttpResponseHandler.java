package com.ximai.savingsmore.library.net;

import java.io.File;

import org.apache.http.Header;

import android.content.Context;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
/**
 * @author wangguodong
 */
public abstract class MyFileAsyncHttpResponseHandler extends
		FileAsyncHttpResponseHandler {


	public MyFileAsyncHttpResponseHandler(Context context) {
		super(context);
	}
	@Override
	public void onSuccess(int statusCode, Header[] headers, File file) {
		onResponse(statusCode, headers, file);
	}

	public abstract void onResponse(int statusCode, Header[] headers, File file) ;


}