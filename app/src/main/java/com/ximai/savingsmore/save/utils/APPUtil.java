package com.ximai.savingsmore.save.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import com.ximai.savingsmore.save.modle.AppInfo;
import com.ximai.savingsmore.save.modle.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class APPUtil {

    public static String[] paks = new String[]{"com.baidu.BaiduMap",
            "com.autonavi.minimap"};

    //######################################
    //通过URI API接口启调地图工具
    //######################################

    public static void startNative_Baidu(Context context, Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            return;
        }
        if (loc1.getAddress() == null || "".equals(loc1.getAddress())) {
            loc1.setAddress("我的位置");
        }
        if (loc2.getAddress() == null || "".equals(loc2.getAddress())) {
            loc2.setAddress("目的地");
        }
        try {
            Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:" + loc1.getStringLatLng() + "|name:" + loc1.getAddress() + "&destination=latlng:" + loc2.getStringLatLng() + "|name:" + loc2.getAddress() + "&mode=transit&src=重庆快易科技|CC房车-车主#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "地址解析错误", Toast.LENGTH_SHORT).show();
        }
    }

    public static void startNative_Gaode(Context context, Location loc) {
        if (loc == null) {
            return;
        }
        if (loc.getAddress() == null || "".equals(loc.getAddress())) {
            loc.setAddress("目的地");
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW",
                    android.net.Uri.parse("androidamap://navi?sourceApplication=CC房车-车主&poiname=重庆快易科技&lat=" + loc.getLat() + "&lon=" + loc.getLng() + "&dev=0&style=0"));
            intent.setPackage("com.autonavi.minimap");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "地址解析错误", Toast.LENGTH_SHORT).show();
        }
    }

    public static void startNative_Tengxun(Context context, Location loc1, Location loc2) {
        /**
         * 目前腾讯还没有可以启调的接口
         */
        return;
    }

    //######################################
    //通过SDK接口启调地图工具
    //######################################

    //public static void startNativeBySDK_Baidu(Context context, Location loc1, Location loc2){
//		double mLat1 = 39.915291;
//		double mLon1 = 116.403857;
//		double mLat2 = 40.056858;
//		double mLon2 = 116.308194;

//		double mLat1 = 30.679318;
//		double mLon1 = 104.104604;
//		double mLat2 = 29.450532;
//		double mLon2 = 105.971314;

//		loc1 = new Location(mLat1, mLon1);
//		loc2 = new Location(mLat2, mLon2);
//
//		if (loc1==null || loc2==null) {
//			return;
//		}
//		if (loc1.getAddress()==null || "".equals(loc1.getAddress())) {
//			loc1.setAddress("我的位置");
//		}
//		if (loc2.getAddress()==null || "".equals(loc2.getAddress())) {
//			loc2.setAddress("目的地");
//		}
//
//		NaviParaOption para = new NaviParaOption().startPoint(MyDistanceUtil.entity2Baidu(loc1))
//												  .startName(loc1.getAddress())
//												  .endPoint(MyDistanceUtil.entity2Baidu(loc2))
//												  .endName(loc2.getAddress());
//		try {
//			BaiduMapNavigation.openBaiduMapNavi(para, context);
//		} catch (BaiduMapAppNotSupportNaviException e) {
//			e.printStackTrace();
//			Toast.makeText(context, "地址解析错误", Toast.LENGTH_SHORT).show();
//		}
//	}
//
    //######################################
    //通启调web地图
    //######################################

    public static String getWebUrl_Baidu(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            return null;
        }
        if (loc1.getAddress() == null || "".equals(loc1.getAddress())) {
            loc1.setAddress("我的位置");
        }
        if (loc2.getAddress() == null || "".equals(loc2.getAddress())) {
            loc2.setAddress("目的地");
        }
        //http://api.map.baidu.com/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving&region=西安&output=html&src=yourCompanyName|yourAppName
        return "http://api.map.baidu.com/direction?origin=latlng:" + loc1.getStringLatLng() + "|name:" + loc1.getAddress() + "&destination=latlng:" + loc2.getStringLatLng() + "|name:" + loc2.getAddress() + "&mode=driving&src=重庆快易科技|CC房车-车主";
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);

        packageManager.getInstalledApplications(packageManager.GET_META_DATA);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    /**
     * 跳转到微信*/
    public static boolean isGoWechat(Context context){
        String packageName="com.tencent.mm";
        if (isAvilible(context,packageName)){
//            Intent intent = new Intent();
//            ComponentName cmp = new ComponentName(packageName, "com.tencent.mm.ui.LauncherUI");
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setComponent(cmp);
//            context.startActivity(intent);
            return true;
        }else {
            Toast.makeText(context,"请先安装微信",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 跳转到支付宝*/
    public static boolean isGoPay(Context context){
        String packageName="com.eg.android.AlipayGphone";
        if (isAvilible(context,packageName)){
//            PackageManager packageManager
//                    = context.getApplicationContext().getPackageManager();
//            Intent intent = packageManager.
//                    getLaunchIntentForPackage(packageName);
//            context.startActivity(intent);
            return true;
        }else {
            Toast.makeText(context,"请先安支付宝应用",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    /**
     * 通过包名获取应用信息
     * @param context
     * @param packageName
     * @return
     */
    public static AppInfo getAppInfoByPak(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfos) {
            if (packageName.equals(packageInfo.packageName)) {
                AppInfo tmpInfo = new AppInfo();
                tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
                tmpInfo.setPackageName(packageInfo.packageName);
                tmpInfo.setVersionName(packageInfo.versionName);
                tmpInfo.setVersionCode(packageInfo.versionCode);
                tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
                return tmpInfo;
            }
        }
        return null;
    }

    /**
     * 返回当前设备上的地图应用集合
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getMapApps(Context context) {
        LinkedList<AppInfo> apps = new LinkedList<AppInfo>();

        for (String pak : paks) {
            AppInfo appinfo = getAppInfoByPak(context, pak);
            if (appinfo != null) {
                apps.add(appinfo);
            }
        }
        return apps;
    }

    /**
     * 获取应用中所有浏览器集合
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getWebApps(Context context) {
        LinkedList<AppInfo> apps = new LinkedList<AppInfo>();

        String default_browser = "android.intent.category.DEFAULT";
        String browsable = "android.intent.category.BROWSABLE";
        String view = "android.intent.action.VIEW";

        Intent intent = new Intent(view);
        intent.addCategory(default_browser);
        intent.addCategory(browsable);
        Uri uri = Uri.parse("http://");
        intent.setDataAndType(uri, null);

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.setAppName(resolveInfo.loadLabel(packageManager).toString());
            tmpInfo.setAppIcon(resolveInfo.loadIcon(packageManager));
            tmpInfo.setPackageName(resolveInfo.activityInfo.packageName);
            apps.add(tmpInfo);
        }

        return apps;
    }
}
