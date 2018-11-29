package com.ximai.savingsmore.save.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ximai.savingsmore.R;


/**
 * Created by luxing on 2017/9/26 0026.
 */

public class SelectCarPopupWindow extends PopupWindow {

    private View mMenuView;
    private final Button mBtn_cancel;//取消
    private final LinearLayout ll_car1;
    private final LinearLayout ll_car2;
    private final LinearLayout ll_car3;
    private final LinearLayout ll_car4;
    private final LinearLayout ll_car5;

    public SelectCarPopupWindow(Activity context, OnClickListener itemsCarOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.selectcar_popup_window, null);

        mBtn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        ll_car1 = (LinearLayout) mMenuView.findViewById(R.id.ll_car1);
        ll_car2 = (LinearLayout) mMenuView.findViewById(R.id.ll_car2);
        ll_car3 = (LinearLayout) mMenuView.findViewById(R.id.ll_car3);
        ll_car4 = (LinearLayout) mMenuView.findViewById(R.id.ll_car4);
        ll_car5 = (LinearLayout) mMenuView.findViewById(R.id.ll_car5);

        mBtn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //设置按钮监听
        ll_car1.setOnClickListener(itemsCarOnClick);
        ll_car2.setOnClickListener(itemsCarOnClick);
        ll_car3.setOnClickListener(itemsCarOnClick);
        ll_car4.setOnClickListener(itemsCarOnClick);
        ll_car5.setOnClickListener(itemsCarOnClick);


        //设置SelectPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public void showAtLocation(View anchor) {
        showAtLocation(anchor, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }
}
