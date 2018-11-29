package com.ximai.savingsmore.save.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by luxing on 17/5/21.
 */

public class MyScrollView extends ScrollView {
    private int downX;

    private int downY;

    private int mTouchSlop;

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();

    }

    public MyScrollView(Context context) {
        super(context);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();

    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction();

        switch (action) {

            case MotionEvent.ACTION_DOWN:

                downX = (int) ev.getRawX();

                downY = (int) ev.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:

                int moveY = (int) ev.getRawY();

                if (Math.abs(moveY - downY) > mTouchSlop) {

                    return true;

                }

        }

        return super.onInterceptTouchEvent(ev);
    }
}
