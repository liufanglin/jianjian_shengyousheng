package com.ximai.savingsmore.library.toolbox;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

/**
 * NetWork Utils
 * <ul>
 * <strong>Attentions</strong>
 * <li>You should add <strong>android.permission.ACCESS_NETWORK_STATE</strong> in manifest, to get network status.</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-11-03
 */
public class NetWorkUtils {

    public static final String NETWORK_TYPE_WIFI       = "wifi";
    public static final String NETWORK_TYPE_3G         = "eg";
    public static final String NETWORK_TYPE_2G         = "2g";
    public static final String NETWORK_TYPE_WAP        = "wap";
    public static final String NETWORK_TYPE_UNKNOWN    = "unknown";
    public static final String NETWORK_TYPE_DISCONNECT = "disconnect";

    /**
     * Get network type
     * 
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? -1 : networkInfo.getType();
    }

    /**
     * Get network type name
     * 
     * @param context
     * @return
     */
    public static String getNetworkTypeName(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        String type = NETWORK_TYPE_DISCONNECT;
        if (manager == null || (networkInfo = manager.getActiveNetworkInfo()) == null) {
            return type;
        };

        if (networkInfo.isConnected()) {
            String typeName = networkInfo.getTypeName();
            if ("WIFI".equalsIgnoreCase(typeName)) {
                type = NETWORK_TYPE_WIFI;
            } else if ("MOBILE".equalsIgnoreCase(typeName)) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                type = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORK_TYPE_3G : NETWORK_TYPE_2G)
                        : NETWORK_TYPE_WAP;
            } else {
                type = NETWORK_TYPE_UNKNOWN;
            }
        }
        return type;
    }

    /**
     * Whether is fast mobile network
     * 
     * @param context
     * @return
     */
    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return false;
        }

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    public enum NetworkConnectType {
        MOBILE, WIFI
    }


    // 判断是否有网络连接
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    // 判断wifi是否可用
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    // 判断手机网络是否可用
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {

        boolean result = false;
        if(context==null)
            return false;
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            final NetworkInfo[] networkInfos = connectivityManager
                    .getAllNetworkInfo();
            if (networkInfos != null) {
                final int length = networkInfos.length;
                for (int i = 0; i < length; i++) {
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return result;
    }

    public static boolean isWIFIor3GNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo == null
                || !connectivityManager.getBackgroundDataSetting()) {
            return false;
        }
        final int networkType = networkInfo.getType();
        final int networkSubType = networkInfo.getSubtype();
        // Only update if WiFi or 3G is connected
        if (networkType == ConnectivityManager.TYPE_WIFI) {
            return networkInfo.isConnected();
        } else if (networkType == ConnectivityManager.TYPE_MOBILE
                && networkSubType == TelephonyManager.NETWORK_TYPE_UMTS) {
            return networkInfo.isConnected();
        } else {
            return false;
        }
    }

    public static boolean isWIFIAvailable(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo == null
                || !connectivityManager.getBackgroundDataSetting()) {
            return false;
        }
        final int networkType = networkInfo.getType();
        // Only update if WiFi or 3G is connected
        if (networkType == ConnectivityManager.TYPE_WIFI) {
            return networkInfo.isConnected();
        } else {
            return false;
        }
    }

    /**
     * Returns whether the network is roaming
     */
    public static boolean isNetworkRoaming(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null
                    && info.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null && tm.isNetworkRoaming()) {
                    return true;
                } else {
                }
            } else {
            }
        }
        return false;
    }

    public static NetworkConnectType getNetworkConnectType(Context context) {
        NetworkConnectType networkConnectType = null;

        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // mobile
        NetworkInfo.State mobile = NetworkInfo.State.DISCONNECTED;
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null)
            mobile = connectivityManager.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE).getState();

        // wifi
        NetworkInfo.State wifi = NetworkInfo.State.DISCONNECTED;
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null)
            wifi = connectivityManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState();
        if (mobile == NetworkInfo.State.CONNECTED
                || mobile == NetworkInfo.State.CONNECTING) {
            // mobile
            networkConnectType = NetworkConnectType.MOBILE;
        } else if (wifi == NetworkInfo.State.CONNECTED
                || wifi == NetworkInfo.State.CONNECTING) {
            // wifi
            networkConnectType = NetworkConnectType.WIFI;
        }
        return networkConnectType;
    }

    public static boolean isUsingWap(Context context) {
        boolean result = false;
        if (isNetworkAvailable(context)
                && getNetworkConnectType(context) == NetworkConnectType.MOBILE) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (networkInfo != null) {
                final String netExtraInfo = networkInfo.getExtraInfo();
                if (!TextUtils.isEmpty(netExtraInfo)
                        && netExtraInfo.toLowerCase().contains("wap")) {
                    result = true;
                }
            }
        }
        return result;
    }


    public static HttpURLConnection setupNetwork(Context context, URL url)
            throws IOException {
        HttpURLConnection httpURLConnection = null;

        if (isUsingWap(context)) {

            final String host = android.net.Proxy.getDefaultHost();
            final int port = android.net.Proxy.getDefaultPort();

            if (!TextUtils.isEmpty(host) && port != -1) {
                httpURLConnection = (HttpURLConnection) url
                        .openConnection(new java.net.Proxy(
                                java.net.Proxy.Type.HTTP,
                                new InetSocketAddress(host, port)));
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            }
        } else {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        }

        return httpURLConnection;
    }

    public static HttpURLConnection setupNetwork(Context context, String urlStr)
            throws IOException {
        return setupNetwork(context, new URL(urlStr));
    }
}
