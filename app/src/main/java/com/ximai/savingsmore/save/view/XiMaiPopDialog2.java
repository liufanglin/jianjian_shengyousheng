package com.ximai.savingsmore.save.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;

public class XiMaiPopDialog2 extends Dialog implements DialogInterface {

    private String title_text;
    	private String content_text;
    	private String ok_text;
    	private String cancel_text;
        private String ok_text_color;
        private String no_text_color;
    	private DialogCallBack callback;
    	private int index;

    public XiMaiPopDialog2(Context context) {
        super(context);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_shape);
    }

        public XiMaiPopDialog2(Context context, String title_text, String content_text, String ok_text, String cancel_text, int theme, DialogCallBack dialogcallback, int index) {
            super(context, theme);
            this.title_text = title_text;
            this.content_text = content_text;
            this.ok_text = ok_text;
            this.cancel_text = cancel_text;
            this.callback = dialogcallback;
            this.index = index;

            getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_shape);
        }
    public XiMaiPopDialog2(Context context, String title_text, String content_text, String ok_text, String cancel_text, String ok_text_color, String no_text_color, int theme, DialogCallBack dialogcallback, int index) {
        super(context, theme);
        this.title_text = title_text;
        this.content_text = content_text;
        this.ok_text = ok_text;
        this.cancel_text = cancel_text;
        this.callback = dialogcallback;
        this.ok_text_color = ok_text_color;
        this.no_text_color = no_text_color;
        this.index = index;

        getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_shape);
    }
        public XiMaiPopDialog2(Context context, String title_text, String content_text, String ok_text, int theme, DialogCallBack dialogcallback, int index) {
            super(context, theme);
            this.title_text = title_text;
            this.content_text = content_text;
            this.ok_text = ok_text;
            this.callback = dialogcallback;
            this.index = index;

            getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_shape);
        }


		@Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_pop1);
            TextView title = (TextView) findViewById(R.id.title);
            TextView content = (TextView) findViewById(R.id.content);
            Button ok = (Button) findViewById(R.id.ok);
            Button cancel = (Button) findViewById(R.id.cancel);


            View line = findViewById(R.id.line);
            if(TextUtils.isEmpty(title_text)){
                title.setVisibility(View.GONE);
                content.setTextSize(15);
            }else {
                title.setText(title_text);
            }

            if(TextUtils.isEmpty(content_text)){
                content.setVisibility(View.GONE);
            }else {
                content.setText(content_text);
            }

            ok.setText(ok_text);
            if(!TextUtils.isEmpty(ok_text_color)) {
                ok.setTextColor(Color.parseColor(ok_text_color));
            }
            if(!TextUtils.isEmpty(no_text_color)) {
                cancel.setTextColor(Color.parseColor(no_text_color));
            }
            if (!TextUtils.isEmpty(cancel_text)){
                cancel.setText(cancel_text);
            }

            if(index == 1){
            	cancel.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                ok.setBackgroundResource(R.drawable.base_item_dialog_ok_selector_2);
            }else{
	            cancel.setOnClickListener(new View.OnClickListener() {
	                public void onClick(View v) {
                        XiMaiPopDialog2.this.dismiss();
	                    callback.CancleDown(XiMaiPopDialog2.this);
	                }
	            });
            }
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XiMaiPopDialog2.this.dismiss();
                    callback.OkDown(XiMaiPopDialog2.this);
                }
            });
        }
}