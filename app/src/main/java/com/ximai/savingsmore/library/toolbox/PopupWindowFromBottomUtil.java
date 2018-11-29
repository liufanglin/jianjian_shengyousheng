package com.ximai.savingsmore.library.toolbox;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.view.WheelView;
import com.ximai.savingsmore.save.modle.BaseMessage;
import com.ximai.savingsmore.save.modle.GoodSalesType;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by C.ym on 2016/5/6.
 */
public class PopupWindowFromBottomUtil {

    private static String id;

    public static PopupWindow showWindow(View contentView, View parentView, final Activity context) {

        PopupWindow setIconWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);


        setIconWindow.setBackgroundDrawable(new BitmapDrawable());
        // setIconWindow.setAnimationStyle(R.style.PopupAnimation);
        setIconWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f, context);
        setIconWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失的时候恢复成原来的透明度
                backgroundAlpha(1f, context);
            }
        });

        return setIconWindow;
    }

    private static void backgroundAlpha(float bgAlpha, Activity context) {
        WindowManager.LayoutParams attr = context.getWindow().getAttributes();
        attr.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(attr);
    }


    static String wheelViewItem;

    public static PopupWindow shouWindowWithWheel(Activity context, View parentView, List<String> infoList, final Listener listener) {

        View outerView = LayoutInflater.from(context).inflate(R.layout.single_colume_wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wvType);
        final PopupWindow popupWindow = showWindow(outerView, parentView, context);

        wv.setOffset(1);


        wv.setItems(infoList);
        wv.setSeletion(0);
        wheelViewItem = wv.getSeletedItem();
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

            @Override
            public void onSelected(int selectedIndex, String item) {
                wheelViewItem = item;
            }
        });

        TextView btnCancel = (TextView) outerView.findViewById(R.id.tvCancel);
        TextView btnConfirm = (TextView) outerView.findViewById(R.id.tvConfirm);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirm(wheelViewItem, popupWindow);
            }
        });

        return popupWindow;
    }

    public static PopupWindow shouRange(Activity context, View parentView, final List<BaseMessage> info, final Listener2 listener2) {
        List<String> list = new ArrayList<String>();

        View outerView = LayoutInflater.from(context).inflate(R.layout.single_colume_wheel_view, null);
        final WheelView wv = (WheelView) outerView.findViewById(R.id.wvType);
        final PopupWindow popupWindow = showWindow(outerView, parentView, context);

        wv.setOffset(1);

        for (int i = 0; i < info.size(); i++) {
            list.add(info.get(i).Name);
        }


        wv.setItems(list);
        wv.setSeletion(0);
        wheelViewItem = wv.getSeletedItem();
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

            @Override
            public void onSelected(int selectedIndex, String item) {
                wheelViewItem = item;
            }
        });

        TextView btnCancel = (TextView) outerView.findViewById(R.id.tvCancel);
        TextView btnConfirm = (TextView) outerView.findViewById(R.id.tvConfirm);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener2.callBack(info.get(wv.getSeletedIndex()), wheelViewItem, popupWindow);
            }
        });

        return popupWindow;
    }

    public static PopupWindow shouSalesType(Activity context, View parentView, final List<GoodSalesType> info, final Listenrt3 listener3) {
        List<String> list = new ArrayList<String>();

        View outerView = LayoutInflater.from(context).inflate(R.layout.single_colume_wheel_view, null);
        final WheelView wv = (WheelView) outerView.findViewById(R.id.wvType);
        final PopupWindow popupWindow = showWindow(outerView, parentView, context);

        wv.setOffset(1);

        for (int i = 0; i < info.size(); i++) {
            list.add(info.get(i).Value);
        }


        wv.setItems(list);
        wv.setSeletion(0);
        wheelViewItem = wv.getSeletedItem();
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

            @Override
            public void onSelected(int selectedIndex, String item) {
                wheelViewItem = item;
            }
        });

        TextView btnCancel = (TextView) outerView.findViewById(R.id.tvCancel);
        TextView btnConfirm = (TextView) outerView.findViewById(R.id.tvConfirm);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener3.callback(info.get(wv.getSeletedIndex()), popupWindow);
            }
        });

        return popupWindow;
    }


    static String wheelViewItem0;
    static String wheelViewItem1;
    static String wheelViewItem2;
    static String wheelViewItem3;

    public static PopupWindow showAddress(Activity context, View parentView, final List<BaseMessage> infoList, final Listenre1 listener) {
        final List<String> country = new ArrayList<String>();
        final List<BaseMessage> country_message = new ArrayList<BaseMessage>();
        final List<String> sheng = new ArrayList<String>();
        final List<BaseMessage> sheng_message = new ArrayList<BaseMessage>();
        final List<String> shi = new ArrayList<String>();
        final List<BaseMessage> shi_message = new ArrayList<BaseMessage>();
        final List<String> qu = new ArrayList<String>();
        final List<BaseMessage> qu_message = new ArrayList<BaseMessage>();

        View outerView = LayoutInflater.from(context).inflate(R.layout.thrree_colume_wheel_view, null);
        //国家
        final WheelView wv0 = (WheelView) outerView.findViewById(R.id.wvType0);
        //省
        final WheelView wv1 = (WheelView) outerView.findViewById(R.id.wvType1);
        //市
        final WheelView wv2 = (WheelView) outerView.findViewById(R.id.wvType2);
        //区
        final WheelView wv3 = (WheelView) outerView.findViewById(R.id.wvType3);

        final PopupWindow popupWindow = showWindow(outerView, parentView, context);

        for (int i = 0; i < infoList.size(); i++) {
            if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals("4eedc104-70dc-43b6-b94b-1c3cdbad7c90")) {
                country.add(infoList.get(i).Name);
                country_message.add(infoList.get(i));
            }
        }

        for (int i = 0; i < infoList.size(); i++) {
            if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals("72183c02-e051-4f6f-8524-c5fc9a9aa419")) {
                sheng.add(infoList.get(i).Name);
                sheng_message.add(infoList.get(i));
            }
        }

        for (int i = 0; i < infoList.size(); i++) {
            if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals(sheng_message.get(0).Id)) {
                shi.add(infoList.get(i).Name);
                shi_message.add(infoList.get(i));
            }
        }

        //国家
        wv0.setOffset(1);
        wv0.setItems(country);
        wv0.setSeletion(0);
        wheelViewItem0 = wv0.getSeletedItem();

        //省
        wv1.setOffset(1);
        wv1.setItems(sheng);
        wv1.setSeletion(0);
//        wheelViewItem1 = wv1.getSeletedItem();

        //市
        wv2.setItems(shi);
        wv2.setOffset(1);
        wv2.setSeletion(0);
        //区
        wv3.setOffset(1);
        wv3.setSeletion(0);
        wv3.setItems(qu);

        //国家
        wv0.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                sheng.clear();
                sheng_message.clear();

                shi.clear();
                shi_message.clear();

                qu.clear();
                qu_message.clear();
                //国家ID
                id = country_message.get(selectedIndex - 1).Id;
                wheelViewItem0 = item;
                for (int i = 0; i < infoList.size(); i++) {
                    if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals(id)) {
                        sheng.add(infoList.get(i).Name);
                        sheng_message.add(infoList.get(i));
                    }
                }

                for (int i = 0; i < infoList.size(); i++) {
                    if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals(sheng_message.get(0).Id)) {
                        shi.add(infoList.get(i).Name);
                        shi_message.add(infoList.get(i));
                    }
                }

                if (shi_message.size() > 0) {
                    for (int i = 0; i < infoList.size(); i++) {
                        if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals(shi_message.get(0).Id)) {
                            qu.add(infoList.get(i).Name);
                            qu_message.add(infoList.get(i));
                        }
                    }
                    wv1.setItems(sheng);
                    wv1.setOffset(1);
                    wv1.setSeletion(0);

                    wv2.setItems(shi);
                    wv2.setOffset(1);
                    wv2.setSeletion(0);

                    wv3.setOffset(1);
                    wv3.setSeletion(0);
                    wv3.setItems(qu);

                }
            }
        });

        //省市区监听
        wv1.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                shi.clear();
                shi_message.clear();

                qu.clear();
                qu_message.clear();

                String id = sheng_message.get(selectedIndex - 1).Id;
//                wheelViewItem1 = item;
                for (int i = 0; i < infoList.size(); i++) {
                    if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals(id)) {
                        shi.add(infoList.get(i).Name);
                        shi_message.add(infoList.get(i));
                    }
                }

                if (shi_message.size() > 0) {
                    for (int i = 0; i < infoList.size(); i++) {
                        if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals(shi_message.get(0).Id)) {
                            qu.add(infoList.get(i).Name);
                            qu_message.add(infoList.get(i));
                        }
                    }
                    wv2.setItems(shi);
                    wv2.setOffset(1);
                    wv2.setSeletion(0);
                    wv3.setOffset(1);
                    wv3.setSeletion(0);
                    wv3.setItems(qu);

                }
            }
        });
        //市
        wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                String id = shi_message.get(selectedIndex - 1).Id;
                qu.clear();
                qu_message.clear();
                for (int i = 0; i < infoList.size(); i++) {
                    if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals(id)) {
                        qu.add(infoList.get(i).Name);
                        qu_message.add(infoList.get(i));
                    }
                }
                wv3.setOffset(1);
                wv3.setSeletion(0);
                wv3.setItems(qu);

            }
        });
        //区
        wv3.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {

            }
        });

        //确定和取消
        TextView btnCancel = (TextView) outerView.findViewById(R.id.tvCancel);
        TextView btnConfirm = (TextView) outerView.findViewById(R.id.tvConfirm);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qu_message.size() == 0) {
                    listener.callBack(id,sheng_message.get(wv1.getSeletedIndex()).Id, shi_message.get(wv2.getSeletedIndex()).Id, "", wv0.getSeletedItem() + " " + wv1.getSeletedItem() + " " + wv2.getSeletedItem() + " " + wv3.getSeletedItem() + " ", popupWindow);
                } else {
                    listener.callBack(id,sheng_message.get(wv1.getSeletedIndex()).Id, shi_message.get(wv2.getSeletedIndex()).Id, qu_message.get(wv3.getSeletedIndex()).Id, wv0.getSeletedItem() + " " + wv1.getSeletedItem() + " " + wv2.getSeletedItem() + " " + wv3.getSeletedItem() + " ", popupWindow);
                }
            }
        });
        return popupWindow;
    }

    public interface Listener {
        void confirm(String content, PopupWindow window);
    }

    public interface Listenre1 {
        void callBack(String Id,String Id1, String Id2, String Id3, String content, PopupWindow popupWindow);
    }

    public interface Listenre1_1 {
        void callBack(String Id1, String Id2, String Id3,String sheng,String city, String content, PopupWindow popupWindow);
    }

    public interface Listener2 {
        void callBack(BaseMessage baseMessage, String content, PopupWindow popupWindow);
    }

    public interface Listenrt3 {
        void callback(GoodSalesType goodSalesType, PopupWindow popupWindow);
    }


    public static PopupWindow showAddresss(Activity context, View parentView, final List<BaseMessage> infoList, final Listenre1_1 listener) {
        final List<String> sheng = new ArrayList<String>();
        final List<BaseMessage> sheng_message = new ArrayList<BaseMessage>();
        final List<String> shi = new ArrayList<String>();
        final List<BaseMessage> shi_message = new ArrayList<BaseMessage>();
        final List<String> qu = new ArrayList<String>();
        final List<BaseMessage> qu_message = new ArrayList<BaseMessage>();
        String Id1;

        for (int i = 0; i < infoList.size(); i++) {
            if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals("72183c02-e051-4f6f-8524-c5fc9a9aa419")) {
                sheng.add(infoList.get(i).Name);
                sheng_message.add(infoList.get(i));
            }
        }
        View outerView = LayoutInflater.from(context).inflate(R.layout.thrree_colume_wheel_view, null);
        final WheelView wv1 = (WheelView) outerView.findViewById(R.id.wvType1);
        final WheelView wv2 = (WheelView) outerView.findViewById(R.id.wvType2);
        final WheelView wv3 = (WheelView) outerView.findViewById(R.id.wvType3);
        final PopupWindow popupWindow = showWindow(outerView, parentView, context);
        wv1.setOffset(1);
        wv1.setItems(sheng);
        wv1.setSeletion(0);
        wheelViewItem1 = wv1.getSeletedItem();
        for (int i = 0; i < infoList.size(); i++) {
            if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals(sheng_message.get(0).Id)) {
                shi.add(infoList.get(i).Name);
                shi_message.add(infoList.get(i));
            }
        }
        wv2.setItems(shi);
        wv2.setOffset(1);
        wv2.setSeletion(0);
        wv3.setOffset(1);
        wv3.setSeletion(0);
        wv3.setItems(qu);
        wv1.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                shi.clear();
                shi_message.clear();
                qu.clear();
                qu_message.clear();
                String id = sheng_message.get(selectedIndex - 1).Id;
                wheelViewItem1 = item;
                for (int i = 0; i < infoList.size(); i++) {
                    if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals(id)) {
                        shi.add(infoList.get(i).Name);
                        shi_message.add(infoList.get(i));
                    }
                }
                if (shi_message.size() > 0) {
                    for (int i = 0; i < infoList.size(); i++) {
                        if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals(shi_message.get(0).Id)) {
                            qu.add(infoList.get(i).Name);
                            qu_message.add(infoList.get(i));
                        }
                    }
                    wv2.setItems(shi);
                    wv2.setOffset(1);
                    wv2.setSeletion(0);
                    wv3.setOffset(1);
                    wv3.setSeletion(0);
                    wv3.setItems(qu);
                }
            }
        });
        wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                String id = shi_message.get(selectedIndex - 1).Id;
                qu.clear();
                qu_message.clear();
                for (int i = 0; i < infoList.size(); i++) {
                    if (null != infoList.get(i).Id && null != infoList.get(i).ParentId && infoList.get(i).ParentId.equals(id)) {
                        qu.add(infoList.get(i).Name);
                        qu_message.add(infoList.get(i));
                    }
                }
                wv3.setOffset(1);
                wv3.setSeletion(0);
                wv3.setItems(qu);

            }
        });
        wv3.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
            }
        });
        TextView btnCancel = (TextView) outerView.findViewById(R.id.tvCancel);
        TextView btnConfirm = (TextView) outerView.findViewById(R.id.tvConfirm);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qu_message.size() == 0) {
                    listener.callBack(sheng_message.get(wv1.getSeletedIndex()).Id, shi_message.get(wv2.getSeletedIndex()).Id, "",wv1.getSeletedItem(),   wv2.getSeletedItem()  , wv1.getSeletedItem() + " " + wv2.getSeletedItem() + " " + wv3.getSeletedItem() + " ", popupWindow);
                } else {
                    listener.callBack(sheng_message.get(wv1.getSeletedIndex()).Id, shi_message.get(wv2.getSeletedIndex()).Id, qu_message.get(wv3.getSeletedIndex()).Id,wv1.getSeletedItem(),wv2.getSeletedItem(), wv1.getSeletedItem() + " " + wv2.getSeletedItem() + " " + wv3.getSeletedItem() + " ", popupWindow);
                }
            }
        });
        return popupWindow;
    }
}