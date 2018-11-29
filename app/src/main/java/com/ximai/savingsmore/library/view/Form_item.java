package com.ximai.savingsmore.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;


/**
 * 打卡异常的自定义的item
 * Created by cx on 16/4/25.
 */
public class Form_item extends LinearLayout{

    private TextView mTvLeft;//item中最左边的选项
    public TextView mTvRight;//item中最右边的提示信息(这里是请选择)
    private ImageView mIvIn;//点击进入的图标(这里是向右的箭头)



    public Form_item(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.form_unit);
        mTvLeft.setText(ta.getString(R.styleable.form_unit_titleleft));
        mTvRight.setText(ta.getString(R.styleable.form_unit_text));
        ta.recycle();
    }


    private void initView() {
        View form_item = LayoutInflater.from(getContext()).inflate(R.layout.view_form_item,this);
        mTvLeft = (TextView) form_item.findViewById(R.id.tv_left);
        mTvRight = (TextView) form_item.findViewById(R.id.tv_right);
        mIvIn = (ImageView) form_item.findViewById(R.id.iv_reports_right);
    }
    public void setmTvRight(String s){
        mTvRight.setText(s);
    }
    public String getmTvRightText(){
        return mTvRight.getText().toString();
    }
    public TextView getmTvRight_(){
        return mTvRight;
    }
    public ImageView getImage(){
        return mIvIn;
    }
}
