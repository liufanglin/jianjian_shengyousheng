package com.ximai.savingsmore.save.view;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;

/**
 * Created by luck on 2017/11/13.
 */

public class PopupAnimation {
    private View animationView;
    private AnimationSet set;
    private AnimationSet set1;
    PopupWindow window;

    /*
    * 先设置Popupwindow与最外面的LinearLayout
    * */
    public void setPopWindow(PopupWindow window, View pop_layout){
        this.window = window;
        this.animationView = pop_layout;
    }

    public void initExitAnimation() {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        animationView.measure(w, h);
        int width =animationView.getMeasuredWidth();
        int height =animationView.getMeasuredHeight();


        set1 = new AnimationSet(true);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -height);
        set1.setDuration(300);
        set1.addAnimation(animation);
        set1.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                window.dismiss();
                if(myPopwindowListener!=null){
                    myPopwindowListener.onDismissListener();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void initEnterAnimation() {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        animationView.measure(w, h);
        int width =animationView.getMeasuredWidth();
        int height =animationView.getMeasuredHeight();

        animationView.setVisibility(View.GONE);
        set = new AnimationSet(true);
        TranslateAnimation animation = new TranslateAnimation(0, 0, -height, 0);
        set.setDuration(500);
        set.addAnimation(animation);
        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                animationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
    }


    public void startEnterAnimation() {
        animationView.startAnimation(set);
    }

    public void startExitAnimation() {
        animationView.startAnimation(set1);
    }

    public void showWindow(View v) {
        if(Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            v.getGlobalVisibleRect(rect);
            int h = v.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            window.setHeight(h);
        }
//        int[] location = new int[2];
//        v.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
//        window.showAtLocation(v, Gravity.NO_GRAVITY, 0, y + v.getHeight());
        window.showAsDropDown(v);
        startEnterAnimation();
    }

    public boolean isShowing() {
        return window.isShowing();
    }

    public void dismiss() {
        startExitAnimation();
    }
    PopListener myPopwindowListener;
    public void setPopWindowListener(PopListener myPopwindowListener){
        this.myPopwindowListener = myPopwindowListener;
    }

    public interface PopListener{
        void onDismissListener();
    }
}
