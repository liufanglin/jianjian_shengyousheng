package com.ximai.savingsmore.save.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.core.CoreJob;
import com.ximai.savingsmore.save.utils.EventCenter;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by caojian on 16/11/16.
 */
public class BaseActivity  extends RootActivity {

    public Toolbar toolbar = null;
    private TextView titleView = null;

    //左侧菜单

    private LinearLayout styleLeftMenuWithImageLayout = null;
    private LinearLayout styleLeftMenuJsutTextLayout = null;
    private LinearLayout styleLeftMenuIconLayout = null;
    private LinearLayout styleLeftMenuInChatViewLayout = null;


    private TextView styleLeftMenuWithImageTitleView = null;
    private TextView styleLeftMenuJsutTextTitleView = null;

    private ImageView styleLeftMenuInChatViewState = null;
    private TextView styleLeftMenuInChatViewTitle = null;

    //右侧菜单
    private LinearLayout styleRightMenuLayout = null;
    private ImageView styleRightMenuImageView = null;


    //右侧文字菜单

    public LinearLayout styleRightTextMenuLayout = null;
    public TextView rightTextMenuTextView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册观察者
       // EventCenter.getInstance().addObserver(this);
        //记录activity打开记录
        CoreJob.addToActivityStack(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注册观察者
        //EventCenter.getInstance().deleteObserver(this);
        //移除activity打开记录
        CoreJob.removeFormActivityStack(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(BaseActivity.this).inflate(R.layout.base_activity_root, null);
        LinearLayout container = (LinearLayout) layout.findViewById(R.id.content);
        LayoutInflater.from(this).inflate(layoutResID, container);

        toolbar = (Toolbar) layout.findViewById(R.id.id_toolbar);
        titleView = (TextView) layout.findViewById(R.id.title);

        styleLeftMenuWithImageLayout = (LinearLayout) layout.findViewById(R.id.style_left_menu_with_image);
        styleLeftMenuJsutTextLayout = (LinearLayout) layout.findViewById(R.id.style_left_menu_just_text);
        styleLeftMenuWithImageTitleView = (TextView) layout.findViewById(R.id.style_left_menu_with_image_title);
        styleLeftMenuJsutTextTitleView = (TextView) layout.findViewById(R.id.style_left_menu_just_text_title);

        styleLeftMenuIconLayout = (LinearLayout) layout.findViewById(R.id.style_left_menu_icon);
        styleLeftMenuInChatViewLayout = (LinearLayout) layout.findViewById(R.id.style_left_menu_in_chat_view);//聊天界面的左侧菜单

        styleLeftMenuInChatViewState = (ImageView) layout.findViewById(R.id.style_left_menu_in_chat_view_state);
        styleLeftMenuInChatViewTitle = (TextView) layout.findViewById(R.id.style_left_menu_in_chat_view_title);


        styleRightMenuLayout = (LinearLayout) layout.findViewById(R.id.style_right_menu);
        styleRightMenuImageView = (ImageView) layout.findViewById(R.id.right_menu_image);


        styleRightTextMenuLayout = (LinearLayout) layout.findViewById(R.id.style_right_text_menu);
        rightTextMenuTextView = (TextView) layout.findViewById(R.id.right_menu_text);


        styleRightTextMenuLayout.setVisibility(View.GONE);
        styleRightMenuLayout.setVisibility(View.GONE);

        styleLeftMenuIconLayout.setVisibility(View.GONE);
        styleLeftMenuInChatViewLayout.setVisibility(View.GONE);
        styleLeftMenuJsutTextLayout.setVisibility(View.GONE);
        styleLeftMenuWithImageLayout.setVisibility(View.GONE);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        super.setContentView(layout);


    }

    @Override
    public void setContentView(View view) {


        LinearLayout layout = (LinearLayout) LayoutInflater.from(BaseActivity.this).inflate(R.layout.base_activity_root, null);
        LinearLayout container = (LinearLayout) layout.findViewById(R.id.content);
        container.addView(view);

        toolbar = (Toolbar) layout.findViewById(R.id.id_toolbar);
        titleView = (TextView) layout.findViewById(R.id.title);

        styleLeftMenuWithImageLayout = (LinearLayout) layout.findViewById(R.id.style_left_menu_with_image);
        styleLeftMenuJsutTextLayout = (LinearLayout) layout.findViewById(R.id.style_left_menu_just_text);
        styleLeftMenuWithImageTitleView = (TextView) layout.findViewById(R.id.style_left_menu_with_image_title);
        styleLeftMenuJsutTextTitleView = (TextView) layout.findViewById(R.id.style_left_menu_just_text_title);


        styleLeftMenuIconLayout = (LinearLayout) layout.findViewById(R.id.style_left_menu_icon);
        styleLeftMenuInChatViewLayout = (LinearLayout) layout.findViewById(R.id.style_left_menu_in_chat_view);//聊天界面的左侧菜单

        styleLeftMenuInChatViewState = (ImageView) layout.findViewById(R.id.style_left_menu_in_chat_view_state);
        styleLeftMenuInChatViewTitle = (TextView) layout.findViewById(R.id.style_left_menu_in_chat_view_title);

        styleRightTextMenuLayout = (LinearLayout) layout.findViewById(R.id.style_right_text_menu);
        rightTextMenuTextView = (TextView) layout.findViewById(R.id.right_menu_text);


        styleRightTextMenuLayout.setVisibility(View.GONE);


        styleRightMenuLayout = (LinearLayout) layout.findViewById(R.id.style_right_menu);
        styleRightMenuImageView = (ImageView) layout.findViewById(R.id.right_menu_image);


        styleRightMenuLayout.setVisibility(View.GONE);
        styleLeftMenuIconLayout.setVisibility(View.GONE);
        styleLeftMenuInChatViewLayout.setVisibility(View.GONE);
        styleLeftMenuJsutTextLayout.setVisibility(View.GONE);
        styleLeftMenuWithImageLayout.setVisibility(View.GONE);

        toolbar.setTitle("");

       // setSupportActionBar(toolbar);

        super.setContentView(layout);

    }

    /**
     * 设置居中标题
     *
     * @param title
     */
    public void setCenterTitle(String title) {

        if (null != titleView) {

            if (null != toolbar)
                toolbar.setTitle("");

            titleView.setText(title);
        }
    }

    /**
     *
     *左侧显示带返回按钮的菜单 默认关闭activity
     * @param activity
     * @param title
     */
    public void setLeftBackMenuVisibility(final Activity activity, String title) {


        if (null != styleLeftMenuWithImageLayout && null != styleLeftMenuWithImageTitleView) {
            styleLeftMenuWithImageLayout.setVisibility(View.VISIBLE);
            styleLeftMenuWithImageTitleView.setText(title);

            styleLeftMenuWithImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != activity)
                        activity.finish();
                }
            });
        }

    }

    /**
     * 左侧显示返回按钮文字菜单（Webview）
     */

    public void setWebViewLeftBackMenuVisibility(String title, View.OnClickListener listener) {


        if (null != styleLeftMenuWithImageLayout && null != styleLeftMenuWithImageTitleView && !TextUtils.isEmpty(title)) {
            styleLeftMenuWithImageLayout.setVisibility(View.VISIBLE);
            styleLeftMenuWithImageTitleView.setText(title);
            styleLeftMenuWithImageLayout.setOnClickListener(listener);
        }

    }


    /**
     * 左侧显示文字菜单
     *
     * @param activity
     * @param title
     */
    public void setLeftTextMenuVisibility(final Activity activity, String title) {


        if (null != styleLeftMenuJsutTextLayout && null != styleLeftMenuJsutTextTitleView && !TextUtils.isEmpty(title)) {
            styleLeftMenuJsutTextLayout.setVisibility(View.VISIBLE);
            styleLeftMenuJsutTextTitleView.setText(title);

            styleLeftMenuJsutTextLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != activity)
                        activity.finish();
                }
            });
        }

    }


    /**
     * 左侧显示logo
     *
     * @param activity
     */
    public void setLeftLogoIconVisibility(final Activity activity) {


        if (null != styleLeftMenuIconLayout) {
            styleLeftMenuIconLayout.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 在聊天界面显示的按钮 包括状态图片 以及标题 返回箭头
     *
     * @param activity
     * @param title
     */
    public void setStyleLeftMenuInChatViewVisible(final Activity activity, int image, String title) {


        if (null != styleLeftMenuInChatViewLayout && null != styleLeftMenuInChatViewState && null != styleLeftMenuInChatViewTitle && !TextUtils.isEmpty(title)) {
            styleLeftMenuInChatViewLayout.setVisibility(View.VISIBLE);
            styleLeftMenuInChatViewTitle.setText(title);
            styleLeftMenuInChatViewState.setImageResource(image);

            styleLeftMenuInChatViewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != activity)
                        activity.finish();
                }
            });
        }

    }


    public void addRightImageMenu(int imgRes, View.OnClickListener listener) {
        if (null != styleRightMenuLayout && null != styleRightMenuImageView) {
            styleRightMenuLayout.setVisibility(View.VISIBLE);
            styleRightMenuImageView.setImageResource(imgRes);
            styleRightMenuLayout.setOnClickListener(listener);

        }
    }

    /**
     * 是否隐藏右侧二维码按钮
     */
    public void hideRightImageMenu(int hide) {
        styleRightMenuImageView.setVisibility(hide);
    }


    /**
     * 添加右侧文字菜单
     *
     * @param menu
     * @param listener
     */
    public void addRightTextMenu(String menu, View.OnClickListener listener) {
        if (null != styleRightTextMenuLayout && null != rightTextMenuTextView) {
            styleRightTextMenuLayout.setVisibility(View.VISIBLE);
            rightTextMenuTextView.setText(menu);
            styleRightTextMenuLayout.setOnClickListener(listener);

        }
    }

//    @Override
//    public void update(Observable observable, Object data) {
//        if (EventCenter.isContainsEnent(EventCenter.FRAGMENT_DIALOG)) {
//            if(JpushReceiver.mdf_type.equals("10")) {
//                DialogUtils.createDialog(BaseActivity.this, JpushReceiver.desc, "我知道了", new DialogUtils.CallBack() {
//                    @Override
//                    public void callBack() {
//                        CoreJob.exitApplication(true,0,false);
//                        DialogUtils.diamis();
//                    }
//                }, R.mipmap.manager);
//            }
//            else if(JpushReceiver.mdf_type.equals("11")) {
//                DialogUtils.createDialog(BaseActivity.this, JpushReceiver.desc, "我知道了", new DialogUtils.CallBack() {
//                    @Override
//                    public void callBack() {
//                        CoreJob.exitApplication(true,0,false);
//                        DialogUtils.diamis();
//                    }
//                }, R.mipmap.assiistant);
//            }
//            else if(JpushReceiver.mdf_type.equals("2")) {
//                DialogUtils.createDialog(BaseActivity.this, JpushReceiver.desc, "我知道了", new DialogUtils.CallBack() {
//                    @Override
//                    public void callBack() {
//                        CoreJob.exitApplication(true,0,false);
//                        DialogUtils.diamis();
//                    }
//                }, R.mipmap.shop_change);
//            }
//        }
//    }

}
