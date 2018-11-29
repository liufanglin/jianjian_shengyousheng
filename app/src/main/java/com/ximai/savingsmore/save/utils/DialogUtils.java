package com.ximai.savingsmore.save.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ximai.savingsmore.R;


/**
 * Created by caojian on 16/6/6.
 */
public class DialogUtils {
    private static  AlertDialog dialog=null;
    private  static  CallBack cb;
    public static  void createDialog(Context context,String content,String title,CallBack callBack,int id){
        cb=callBack;
        dialog=new AlertDialog.Builder(context).create();
        View view= LayoutInflater.from(context).inflate(R.layout.dialog,null);
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(true);
        TextView textView= (TextView) view.findViewById(R.id.dialog_text);
        TextView textView1= (TextView) view.findViewById(R.id.dialog_text1);
        ImageView imageView= (ImageView) view.findViewById(R.id.dialog_image);
        imageView.setImageResource(id);
        textView.setText(content);
        textView1.setText(title);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=cb){
                cb.callBack();
                }
            }
        });
        dialog.show();
    }
    public static  void diamis(){
        dialog.dismiss();
    }
    public interface  CallBack{
        void callBack();
    }
}
