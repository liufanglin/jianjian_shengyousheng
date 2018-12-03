package com.ximai.savingsmore.save.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.ShareData;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareUtils implements OnClickListener, PlatformActionListener {
    private String id;
    private Context context;
    public PopupWindow shareWindow;
    private ShareData data;
    private String Remark;
    private LinearLayout wechat, qzone, wechatMoments, weibo, duanxin, youxiang;
    private final String isGoodsShark;
    private String isPeopleAndBusiness;


    public ShareUtils(ShareData data, Context context) {
        this.data = data;
        this.context = context;
        isGoodsShark = PreferencesUtils.getString(context, "isGoodsShark", "");//1是商品2是商家ffff
        //3是商家 - 2是个人
        isPeopleAndBusiness = PrefUtils.getString(context, "isPeopleAndBusiness", "");
    }

    public ShareUtils(ShareData data, Context context,String id) {
        this.data = data;
        this.context = context;
        this.id = id;
        isGoodsShark = PreferencesUtils.getString(context, "isGoodsShark", "");//1是商品2是商家
        //3是商家 - 2是个人
        isPeopleAndBusiness = PrefUtils.getString(context, "isPeopleAndBusiness", "");
    }

    /**
     * 设置弹窗的显示
     * @param parentView
     */
    public void show(View parentView) {
        if (shareWindow != null && shareWindow.isShowing()) {
            shareWindow.dismiss();
            setAlpath(1f);
        }
        View cw = ((Activity) context).getLayoutInflater().inflate(R.layout.item_shareutil_layout, null);
        wechat = (LinearLayout) cw.findViewById(R.id.wechat);
        wechat.setOnClickListener(this);
        qzone = (LinearLayout) cw.findViewById(R.id.qzone);
        weibo = (LinearLayout) cw.findViewById(R.id.weibo);
        duanxin = (LinearLayout) cw.findViewById(R.id.duanxin);
        youxiang = (LinearLayout) cw.findViewById(R.id.youxiang);
        duanxin.setOnClickListener(this);
        youxiang.setOnClickListener(this);
        weibo.setOnClickListener(this);
        qzone.setOnClickListener(this);
        wechatMoments = (LinearLayout) cw.findViewById(R.id.wechatmoments);
        wechatMoments.setOnClickListener(this);

        setAlpath(0.5f);//弹出弹框背景设置半透明

        shareWindow = new PopupWindow(cw, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        shareWindow.setFocusable(true);//点击空白区域关闭
        shareWindow.setTouchable(true);
        shareWindow.setOutsideTouchable(true);
        shareWindow.setAnimationStyle(R.style.take_photo_anim);// 设置弹出窗体显示时的动画，从底部向上弹出
        shareWindow.showAtLocation(parentView.getRootView(), Gravity.BOTTOM, 0, 0);//设置从底部进行展示
        /**
         * 设置点击其他空白区域弹框关闭的监听
         */
        shareWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpath(1f);
            }
        });

        ((LinearLayout) cw).setFocusable(true);
        ((LinearLayout) cw).setFocusableInTouchMode(true);
        //弹窗的点击事件
        cw.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return false;
            }
        });

        cw.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            dismiss();
                            break;
                        case KeyEvent.KEYCODE_MENU:
                            dismiss();
                            break;
                    }
                }
                return true;
            }
        });
    }

    /**
     * 关闭弹窗
     */
    public void dismiss() {
        if (shareWindow != null && shareWindow.isShowing()) {
            shareWindow.dismiss();
            setAlpath(1f);
        }
    }

    /**
     * 取消分享
     * @param arg0
     * @param arg1
     */
    @Override
    public void onCancel(Platform arg0, int arg1) {
        Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
        setAlpath(1f);
    }

    /**
     * 分享成功
     * @param arg0
     * @param arg1
     * @param arg2
     */
    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
        if ("1".equals(isGoodsShark)){//1是商品
//            shareProduct(id,Remark);
            shareApp(Remark);
            Log.e("tag","22222222222222222222222222222222222222222222222222222222");
        }else{//2不是商品
            shareApp(Remark);
            Log.e("tag","-2-2-2-2-2-2--2-2-2-2-2--2-2-22-2-2-2-2-2-2-2-22-2-2--");
        }
        setAlpath(1f);
    }

    /**
     * 分享错误
     * @param arg0
     * @param arg1
     * @param arg2
     */
    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        Toast.makeText(context, "分享错误", Toast.LENGTH_SHORT).show();
        setAlpath(1f);
    }

    /**
     * 点击监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        SharePlatfrom sharePlatfrom = new SharePlatfrom();
        if (v.getId() == R.id.wechat) {
            Remark = "微信";
            dismiss();
            if (isWeixinAvilible(context))
                sharePlatfrom.platfrom("wechat", data);
            else
                Toast.makeText(context, "您的微信还未安装呢！", Toast.LENGTH_SHORT).show();

        }
        if (v.getId() == R.id.wechatmoments) {
            Remark = "朋友圈";
            dismiss();
            if (isWeixinAvilible(context))
                sharePlatfrom.platfrom("wechat_circle", data);
            else
                Toast.makeText(context, "您的微信还未安装呢！", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.weibo) {
            Remark = "微博";
            dismiss();
            sharePlatfrom.platfrom("weibo", data);
        }
        if (v.getId() == R.id.qzone) {
            Remark = "qq空间";
            dismiss();
            sharePlatfrom.platfrom("qzone", data);
        }
        if (v.getId() == R.id.duanxin) {
            //短信
            if ("1".equals(isGoodsShark)){//商品链接的分享
                if (null != id){
                    PreferencesUtils.putString(context,"SMS","1");
                    dismiss();
                    Uri smsToUri = Uri.parse("smsto:");
                    Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                    String s = "http://www.savingsmore.com/Product/SharedProductDetail/" + id;
                    intent.putExtra("sms_body", data.getTitle()+"\n"+data.getText()+s);
                    context.startActivity(intent);
                }
            }else{//下载链接的分享 - 侧边栏的分享 - 判断是商家还是个人
                if ("2".equals(isPeopleAndBusiness)){//2是个人3是商家
                    //            shareApp("短信");
                    PreferencesUtils.putString(context,"SMS","1");
                    dismiss();
                    String s="http://login.savingsmore.com/Home/Download";
                    Uri smsToUri = Uri.parse("smsto:");
                    Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                    intent.putExtra("sms_body", data.getTitle()+"\n"+data.getText()+s);
                    context.startActivity(intent);
                }else if ("3".equals(isPeopleAndBusiness)){
                    //            shareApp("短信");
                    PreferencesUtils.putString(context,"SMS","1");
                    dismiss();
                    String s="http://login.savingsmore.com/Home/Download";
                    Uri smsToUri = Uri.parse("smsto:");
                    Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                    intent.putExtra("sms_body", data.getTitle()+"\n"+data.getText()+s);

//                    intent.putExtra("sms_body", "您的朋友"+MyUserInfoUtils.getInstance().myUserInfo.ShowName +"向您推荐促销商品，快去看！促销结束就无效了！http://login.savingsmore.com/Home/Download");
                    context.startActivity(intent);
                }
            }
        }

        if (v.getId() == R.id.youxiang) {
//            shareApp("邮箱");
            if ("1".equals(isGoodsShark)){//商品链接的分享
                if (null != id){
                    dismiss();
                    Intent email = new Intent(android.content.Intent.ACTION_SEND);
                    email.setType("plain/text");
                    String s = "http://www.savingsmore.com/Product/SharedProductDetail/" + id;
//                    String emailBody = "您的朋友"+ MyUserInfoUtils.getInstance().myUserInfo.ShowName +"分享了一个促销商品，快去看！促销结束就无效了！"+s;
                    email.putExtra(android.content.Intent.EXTRA_SUBJECT, data.getTitle());//邮件主题
                    email.putExtra(android.content.Intent.EXTRA_TEXT, data.getText()+s);//邮件内容
                    context.startActivity(Intent.createChooser(email, "请选择邮件发送内容"));
                }
            }else{//下载链接的分享 - 在这李进行商家或者是个人的判断
                if ("2".equals(isPeopleAndBusiness)) {//2是个人3是商家
                    dismiss();
                    Intent email = new Intent(android.content.Intent.ACTION_SEND);
                    email.setType("plain/text");
                    String s="http://login.savingsmore.com/Home/Download";
                    String emailBody = "各品牌实体门店都在促销，手机一搜，“省又省”带你去。http://login.savingsmore.com/Home/Download";
                    email.putExtra(android.content.Intent.EXTRA_SUBJECT, data.getTitle());//邮件主题
                    email.putExtra(android.content.Intent.EXTRA_TEXT, data.getText()+s);//邮件内容
                    context.startActivity(Intent.createChooser(email, "请选择邮件发送内容"));
                }else if ("3".equals(isPeopleAndBusiness)){
                    dismiss();
                    Intent email = new Intent(android.content.Intent.ACTION_SEND);
                    email.setType("plain/text");
                    String s="http://login.savingsmore.com/Home/Download";
//                    String emailBody = "您的朋友"+MyUserInfoUtils.getInstance().myUserInfo.ShowName +"向您推荐促销商品，快去看！促销结束就无效了！http://login.savingsmore.com/Home/Download";
                    email.putExtra(android.content.Intent.EXTRA_SUBJECT, data.getTitle());//邮件主题
                    email.putExtra(android.content.Intent.EXTRA_TEXT, data.getText()+s);//邮件内容
                    context.startActivity(Intent.createChooser(email, "请选择邮件发送内容"));
                }
            }

            /**
             * 发送短信
             */
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ("1".equals(isGoodsShark)){//1是商品
//                        shareProduct(id,"邮箱");
                        shareApp("邮箱");
                    }else{//2不是商品
                        shareApp("邮箱");
                    }
                }
            },40000);
        }
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 设置透明度
     * @param alpath
     */
    public void setAlpath(float alpath) {
        WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
        params.alpha = alpath;
        ((Activity) context).getWindow().setAttributes(params);
    }

    /**
     * 实现点击窗体以外的部分能关闭弹窗
     */
    public void setWindowsBackground() {
        if (shareWindow != null && shareWindow.isShowing()) {
            shareWindow.dismiss();
            setAlpath(1f);
            shareWindow = null;
        }
    }

    /**
     * 判断分享类型，设置分享数据
     */
    public class SharePlatfrom {
        private final static String PENGYOUQUAN = "wechat_circle";
        private final static String QQKONGJIAN = "qzone";
        private final static String WEIXIN = "wechat";
        private final static String WEIBO = "weibo";

        public void platfrom(String type, ShareData data) {
            Platform platform = null;
            String shareImagePath = data.getImagePath();
            String shareUrl = data.getUrl();
            String shareText = data.getText();
            String shareimageUrl = data.getImageUrl();
            String shareTitle = data.getTitle();
            String shareSite = data.getSite();
            String shareSiteUrl = data.getSiteUrl();
            String shareTitltUrl = data.getTitleurl();
            Bitmap bitmap=data.getBitmap();
            if (type == PENGYOUQUAN) {
       /*        微信分享图文：必须设置title，imageUrl（imagepath，ImageData) text为可选参数
                微信分享文本：title text都必须有
                微信网页分享:title text imageUrl	url*/

                platform = ShareSDK.getPlatform(context, WechatMoments.NAME);
                WechatMoments.ShareParams params = new WechatMoments.ShareParams();
                if (shareUrl != null) {
                    params.setShareType(Platform.SHARE_WEBPAGE);
                    params.setUrl(shareUrl);
                } else {
                    if (shareimageUrl != null || shareImagePath != null||bitmap!=null) {
                        params.setShareType(Platform.SHARE_IMAGE);
                    } else {
                        params.setShareType(Platform.SHARE_TEXT);
                    }
                }

                if (shareImagePath != null) {
                    params.setImagePath(shareImagePath);
                }
                if (shareText != null) {
                    params.setText(shareText);
                }
                if (shareimageUrl != null) {
                    params.setImageUrl(shareimageUrl);
                }
                if (bitmap!=null){
                    params.setImageData(bitmap);
                }
                if (shareTitle != null) {
                    params.setTitle(shareTitle);
                }
                platform.setPlatformActionListener(ShareUtils.this);
                platform.share(params);
            }
            if (type == WEIBO) {
                platform = ShareSDK.getPlatform(context, SinaWeibo.NAME);
                SinaWeibo.ShareParams params = new SinaWeibo.ShareParams();
                if (shareImagePath != null) {
                    params.setImagePath(shareImagePath);
                }
                if (shareText != null) {
                    params.setText(shareText);
                }
                if (shareimageUrl != null) {
                    params.setImageUrl(shareimageUrl);
                }
                if (shareTitle != null) {
                    //  params.setTitle(shareTitle);
                }
                if (shareSite != null) {
                    //  params.setSite(shareSite);
                }
                if (shareSiteUrl != null) {
                    // params.setSiteUrl(null);
                }
                if (shareTitltUrl != null) {
                    // params.setTitleUrl(shareTitltUrl);
                }
                platform.setPlatformActionListener(ShareUtils.this);
                platform.share(params);
            }
            if (type == QQKONGJIAN) {
                //qq空间图文分享必须设置四个参数不能少：setTitle(),setTitleUrl(),setText(),setImageUrl();

                // platform = ShareSDK.getPlatform(context, QZone.NAME);
                platform = ShareSDK.getPlatform(context, QQ.NAME);

                QQ.ShareParams params = new QQ.ShareParams();
                if (shareImagePath != null) {
                    params.setImagePath(shareImagePath);
                }
                if (shareText != null) {
                    params.setText(shareText);
                }
                if (shareimageUrl != null) {
                    params.setImageUrl(shareimageUrl);
                }
                if (shareTitle != null) {
                    params.setTitle(shareTitle);
                }
                if (shareSite != null) {
                    params.setSite(shareSite);
                }
                if (shareSiteUrl != null) {
                    params.setSiteUrl(null);
                }
                if (shareTitltUrl != null) {
                    params.setTitleUrl(shareTitltUrl);
                }

                platform.setPlatformActionListener(ShareUtils.this);
                platform.share(params);
            }
            if (type == WEIXIN) {
                platform = ShareSDK.getPlatform(context, Wechat.NAME);
                Wechat.ShareParams params = new Wechat.ShareParams();
                if (shareUrl != null) {
                    params.setShareType(Platform.SHARE_WEBPAGE);
                    params.setUrl(shareUrl);
                } else {
                    if (shareimageUrl != null || shareImagePath != null) {
                        params.setShareType(Platform.SHARE_IMAGE);
                    } else {
                        params.setShareType(Platform.SHARE_TEXT);
                    }
                }

                if (shareImagePath != null) {
                    params.setImagePath(shareImagePath);
                }
                if (shareText != null) {
                    params.setText(shareText);
                }
                if (shareimageUrl != null) {
                    params.setImageUrl(shareimageUrl);
                }
                if (shareTitle != null) {
                    params.setTitle(shareTitle);
                }
                platform.setPlatformActionListener(ShareUtils.this);
                platform.share(params);
            }
        }
    }

    private void shareApp(String Remark) {
        WebRequestHelper.json_post(context, URLText.SHARE_APP, RequestParamsPool.sahreApp(Remark), new MyAsyncHttpResponseHandler(context) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                //   Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                JSONObject object = new JSONObject();
                /**
                 * 更新左侧侧边栏数据
                 */
                String isPeopleAndBusiness = PrefUtils.getString(context, "isPeopleAndBusiness", "");
                if ("2".equals(isPeopleAndBusiness)){
                    NotificationCenter.defaultCenter().postNotification(Constants.PERSONAL_CEBIANLAN,"");
                    Log.e("tag","33333333333333333333333-------------333333333333333333333333333333333333");
                }else{
                    NotificationCenter.defaultCenter().postNotification(Constants.BUSINESS_CEBIANLAN,"");
                    Log.e("tag","33333333333333333333333333333333333333333333333333333333333");
                }
            }
        });
    }

    //分享商品 -之前是用这个
    private void shareProduct(String id,String Remark) {
        WebRequestHelper.json_post(context, URLText.SHARE_PRODUCT, RequestParamsPool.shareProduct(id,Remark), new MyAsyncHttpResponseHandler(context) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                //   Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                JSONObject object = new JSONObject();
                /**
                 * 更新左侧侧边栏数据
                 */
                String isPeopleAndBusiness = PrefUtils.getString(context, "isPeopleAndBusiness", "");
                if ("2".equals(isPeopleAndBusiness)){
                    NotificationCenter.defaultCenter().postNotification(Constants.PERSONAL_CEBIANLAN,"");
                }else{
                    NotificationCenter.defaultCenter().postNotification(Constants.BUSINESS_CEBIANLAN,"");
                }
            }
        });
    }
}