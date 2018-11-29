package com.ximai.savingsmore.save.utils;

import com.ximai.savingsmore.library.toolbox.LogUtils;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;


/**
 * 界面刷新事件总线
 * 如果界面需要刷新，请先实现Observer接口  然后注册监听
 *
 * @author wangguodong
 */
public class EventCenter extends Observable {

    public static final int FRAGMENT_DISCOVERY_TAG = 10001;
    public static final int FRAGMENT_FOCUS_TAG = 10002;
    public static final int FRAGMENT_MECENTER_TAG = 1003;
    public static final int FRAGMENT_NEW_TAG = 10004;
    public static final int FRAGMENT_ALL_TAG = 10005;

    public static final int FRAGMENT_DIALOG = 10016;
    //显示红点
    public static final int FRAGMENT_CUSTOMER_TAG = 10006;

    //public static final int UPDATE_LOCAL_CONTACTS = 10007;
    //要求刷新会话列表
    public static final int FRAGMENT_CONVERSITON_TAG = 10008;

    public static final int UPDATESEX_TAG = 10009;

    //要求刷新客户界面
    public static final int FORCE_UPDATE_CONTACTS_AND_CACHE = 10010;

    //在发现tab添加红点
    public static final int ADD_RED_DOT_ON_DISCOVERYTAB = 10011;

    //在个人中心tab添加红点
    public static final int ADD_RED_DOT_ON_ME_CENTERTAB = 10012;

    ////在个人中心tab删除红点
    public static final int DELETE_RED_DOT_ON_ME_CENTERTAB = 10013;

    //订单管理item添加红点
    public static  final int ADD_RED_DOT_ON_ORDER_ITEM = 100014;

    //订单管理item删除红点
    public static  final int DELETE_RED_DOT_ON_ORDER_ITEM = 100015;

    private static EventCenter instance = null;
    private static Set<Integer> eventTag = new HashSet<Integer>();

    public static EventCenter getInstance() {

        if (null == instance)
            instance = new EventCenter();
        return instance;
    }


    public void addEvent(int event) {

        eventTag.add(event);
        setChanged();
        notifyObservers();
      
        eventTag.remove(event);
        LogUtils.instance.d("通知观察者刷新界面并移除事件");
    }

    public static boolean isContainsEnent(int tag) {
        return eventTag.contains(tag);

    }


}
