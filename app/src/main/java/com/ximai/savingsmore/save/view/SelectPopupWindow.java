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
import android.widget.PopupWindow;

import com.ximai.savingsmore.R;


/**
 * Created by luxing on 2017/9/26 0026.
 */

public class SelectPopupWindow extends PopupWindow {

    private View mMenuView;
    private final Button btn_one;//照相
    private final Button btn_two;//从相册选
    private final Button mBtn_cancel;//取消

    public SelectPopupWindow(Activity context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_popup_window, null);

        btn_one = (Button) mMenuView.findViewById(R.id.btn_one);
        btn_two = (Button) mMenuView.findViewById(R.id.btn_two);
        mBtn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);

        mBtn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //设置按钮监听
        btn_one.setOnClickListener(itemsOnClick);
        btn_two.setOnClickListener(itemsOnClick);
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

    public void showAtLocation(View anchor, String itemStr, String itemStr2) {
        if (TextUtils.isEmpty(itemStr)) {
            throw new RuntimeException("first item must not be empty");
        }
        if (TextUtils.isEmpty(itemStr2)) {
            btn_two.setVisibility(View.GONE);
        } else {
            btn_two.setText(itemStr2);
        }
        btn_one.setText(itemStr);
        showAtLocation(anchor, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }
}
