package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.easeui.EaseConstant;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.cache.MyImageLoader;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.library.view.ScrollViewWithListView;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.OrderDetial;
import com.ximai.savingsmore.save.modle.OrderStateResult;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 17/1/5.
 * 原来的订单详情 - 目前废弃
 *
 */
public class OrderDetialActivity extends BaseActivity implements View.OnClickListener {
    private String OrderId;
    private List<Goods> list_good = new ArrayList<Goods>();
    private OrderDetial orderDetial;
    private TextView order_number;
    private TextView fapiao;
    private ScrollViewWithListView scrollViewWithListView;
    private TextView remark;
    private MyAdapter myAdapter;
    private TextView state;
    private LinearLayout business_submit;
    private TextView submit1, submit2, submit3, submit4;
    private TextView pay_money;
    private String type;
    private OrderStateResult orderStateResult;
    private RelativeLayout liulan_item;
    private ImageView liuyan_decrition;
    private ImageView send_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detial);
        setCenterTitle("订单详情");
        OrderId = getIntent().getStringExtra("Id");
        type = getIntent().getStringExtra("type");
        getOrderDetial(OrderId);
        setLeftBackMenuVisibility(OrderDetialActivity.this, "");
        order_number = (TextView) findViewById(R.id.order_number);
        fapiao = (TextView) findViewById(R.id.fapiao);
        remark = (TextView) findViewById(R.id.beizhu);
        state = (TextView) findViewById(R.id.state);
        submit1 = (TextView) findViewById(R.id.submit1);
        submit2 = (TextView) findViewById(R.id.submit2);
        submit3 = (TextView) findViewById(R.id.submit3);
        submit4 = (TextView) findViewById(R.id.submit4);
        liuyan_decrition = (ImageView) findViewById(R.id.liuyan_dection);
        liulan_item = (RelativeLayout) findViewById(R.id.liuyan_item);
        send_message = (ImageView) findViewById(R.id.send_message);
        if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")) {
            send_message.setVisibility(View.GONE);
        }
        send_message.setOnClickListener(this);
        liulan_item.setOnClickListener(this);
        submit1.setOnClickListener(this);
        submit2.setOnClickListener(this);
        submit3.setOnClickListener(this);
        submit4.setOnClickListener(this);
        pay_money = (TextView) findViewById(R.id.pay_money);
        business_submit = (LinearLayout) findViewById(R.id.business_submit);
        submit1.setOnClickListener(this);
        submit2.setOnClickListener(this);
        scrollViewWithListView = (ScrollViewWithListView) findViewById(R.id.list1);
        myAdapter = new MyAdapter();
        scrollViewWithListView.setAdapter(myAdapter);

    }

    //订单详情
    private void getOrderDetial(String Id) {
        String url = null;
        if (type.equals("business")) {
            url = URLText.BUSINESS_ORDER_DETIAL;
        } else {
            url = URLText.ORDER_DETIAL;
        }
        WebRequestHelper.json_post(OrderDetialActivity.this, url, RequestParamsPool.orderDetial(Id), new MyAsyncHttpResponseHandler(OrderDetialActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    orderDetial = GsonUtils.fromJson(object.optString("MainData"), OrderDetial.class);
                    if (null != orderDetial) {
                        business_submit.setVisibility(View.GONE);
                        submit3.setVisibility(View.GONE);
                        submit4.setVisibility(View.GONE);
                        list_good = orderDetial.OrderProducts;
                        order_number.setText("订单号:" + orderDetial.Number);
                        state.setText(orderDetial.OrderStateName);
                        fapiao.setText(orderDetial.InvoiceTitle);
                        remark.setText(orderDetial.Remark);
                        pay_money.setText("￥" + orderDetial.Price);
                        // pay_money.setText(orderDetial.);
                        myAdapter.notifyDataSetChanged();
                        if (orderDetial.OrderState.equals("2") && MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("3")) {
                            business_submit.setVisibility(View.VISIBLE);
                        } else if (orderDetial.OrderState.equals("3") && MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")) {
                            submit3.setVisibility(View.VISIBLE);
                        } else if (orderDetial.OrderState.equals("8") && MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")) {
                            submit4.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit1:
                deliverGoods(OrderId);
                break;
            case R.id.submit2:
                cancelOrder(OrderId);
                break;
            case R.id.submit3:
                receipteMyOrder(OrderId);
                break;
            case R.id.submit4:
                quit_moneny(OrderId);
                break;
            case R.id.liuyan_item:
                if (remark.getVisibility() == View.VISIBLE) {
                    remark.setVisibility(View.GONE);
                    liuyan_decrition.setBackgroundResource(R.mipmap.search_up3);
                } else {
                    liuyan_decrition.setBackgroundResource(R.mipmap.search_dowm3);
                    remark.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.send_message:
                Intent send = new Intent(OrderDetialActivity.this, ChatActivity.class);
                send.putExtra(EaseConstant.EXTRA_USER_ID, orderDetial.User.IMUserName);
                startActivity(send);
                break;

        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_good.size();
        }

        @Override
        public Object getItem(int position) {
            return list_good.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodel viewHodel;
            if (null == convertView) {
                viewHodel = new ViewHodel();
                convertView = (LinearLayout) LayoutInflater.from(OrderDetialActivity.this).inflate(R.layout.order_good_item, null);
                viewHodel.sales_count = (TextView) convertView.findViewById(R.id.sales_number);
                viewHodel.bianhao = (TextView) convertView.findViewById(R.id.bianhao);
                viewHodel.name = (TextView) convertView.findViewById(R.id.name);
                viewHodel.imageView = (ImageView) convertView.findViewById(R.id.image);
                viewHodel.zhekou = (TextView) convertView.findViewById(R.id.zhekou);
                viewHodel.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
                viewHodel.shou_chang = (TextView) convertView.findViewById(R.id.dainzan_number);
                viewHodel.liulan = (TextView) convertView.findViewById(R.id.clock_number);
                viewHodel.share = (TextView) convertView.findViewById(R.id.share_number);
                viewHodel.location = (TextView) convertView.findViewById(R.id.location);
                viewHodel.start_time = (TextView) convertView.findViewById(R.id.statr_time);
                viewHodel.end_time = (TextView) convertView.findViewById(R.id.end_time);
                viewHodel.price = (TextView) convertView.findViewById(R.id.price);
                viewHodel.dazhe_style = (TextView) convertView.findViewById(R.id.dazhe);
                viewHodel.high_price = (TextView) convertView.findViewById(R.id.high_price);
                viewHodel.high_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                viewHodel.number = (TextView) convertView.findViewById(R.id.number);
                convertView.setTag(viewHodel);
            }
            viewHodel = (ViewHodel) convertView.getTag();
            if (null != list_good.get(position).Number) {
                viewHodel.bianhao.setText("商品号:" + list_good.get(position).Number);
            }
            viewHodel.number.setText("" + list_good.get(position).Quantity);
            if (null != list_good.get(position).SalesCount) {
                viewHodel.sales_count.setText("销:" + list_good.get(position).SalesCount);
            }
            if (null != list_good.get(position).Image) {
                MyImageLoader.displayDefaultImage(URLText.img_url + list_good.get(position).Image, viewHodel.imageView);
            }
            if (null != list_good.get(position).Name) {
                viewHodel.name.setText(list_good.get(position).Name);
            }
            if (null != list_good.get(position).Preferential) {
                viewHodel.zhekou.setText(list_good.get(position).PromotionTypeName);
            }
            if (null != list_good.get(position).StoreName) {
                viewHodel.shop_name.setText(list_good.get(position).StoreName);
            }
            if (null != list_good.get(position).FavouriteCount) {
                viewHodel.shou_chang.setText(list_good.get(position).FavouriteCount);
            }
            viewHodel.liulan.setText(list_good.get(position).HitCount);
            viewHodel.share.setText(list_good.get(position).SharedCount);
            if (null != list_good.get(position).Address) {
                viewHodel.location.setText(list_good.get(position).Address);
            }
            if (null != list_good.get(position).StartTimeName && !TextUtils.isEmpty(list_good.get(position).StartTimeName)) {
                viewHodel.start_time.setText(list_good.get(position).StartTimeName.split(" ")[0]);
            }
            if (null != list_good.get(position).EndTimeName && !TextUtils.isEmpty(list_good.get(position).EndTimeName)) {
                viewHodel.end_time.setText(list_good.get(position).EndTimeName.split(" ")[0]);
            }
            //  viewHodel.price.setText("￥" + list_good.get(position).Price.substring(0, list_good.get(position).Price.indexOf(".")));
            viewHodel.price.setText("￥" + list_good.get(position).Price);
            if (list_good.get(position).Preferential.length() > 5){
                viewHodel.dazhe_style.setText(list_good.get(position).Preferential.substring(0,5)+"...");
            }else{
                viewHodel.dazhe_style.setText(list_good.get(position).Preferential);
            }
            //viewHodel.high_price.setText("原价:￥" + list_good.get(position).OriginalPrice.substring(0, list_good.get(position).OriginalPrice.indexOf(".")));
            viewHodel.high_price.setText("原价￥" + list_good.get(position).OriginalPrice);
            return convertView;
        }
    }


    class ViewHodel {
        public TextView sales_count;
        public TextView bianhao;
        public ImageView imageView;
        public TextView name;
        public TextView zhekou;
        public TextView shop_name;
        public TextView shou_chang;
        public TextView liulan;
        public TextView share;
        public TextView location;
        public TextView start_time;
        public TextView end_time;
        public TextView price;
        public TextView dazhe_style;
        public TextView high_price;
        public TextView number;
    }

    //确认收货
    private void receipteMyOrder(String ID) {
        WebRequestHelper.json_post(OrderDetialActivity.this, URLText.RECEIPEMY_ORDER, RequestParamsPool.queren_order(ID), new MyAsyncHttpResponseHandler(OrderDetialActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
                    getOrderDetial(OrderId);
                }
                Toast.makeText(OrderDetialActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
                LogUtils.instance.d("确认收款=" + result);
            }
        });
    }

    //申请退款
    private void quit_moneny(String Id) {
        WebRequestHelper.json_post(OrderDetialActivity.this, URLText.QUIT_MONEY, RequestParamsPool.quit_moneny(Id), new MyAsyncHttpResponseHandler(OrderDetialActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
                    getOrderDetial(OrderId);
                }
//                Toast.makeText(OrderDetialActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
                Toast.makeText(OrderDetialActivity.this,"退款申请审核中", Toast.LENGTH_SHORT).show();
                LogUtils.instance.d("退款=" + result);
            }
        });
    }

    //商家发货
    private void deliverGoods(String Id) {
        WebRequestHelper.json_post(OrderDetialActivity.this, URLText.FAHUO, RequestParamsPool.fa_huo(Id), new MyAsyncHttpResponseHandler(OrderDetialActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
                    getOrderDetial(OrderId);
                }
                Toast.makeText(OrderDetialActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
                LogUtils.instance.d("发货=" + result);
            }
        });
    }

    //取消订单
    private void cancelOrder(String Id) {
        WebRequestHelper.json_post(OrderDetialActivity.this, URLText.CANAEL, RequestParamsPool.cancelOrder(Id), new MyAsyncHttpResponseHandler(OrderDetialActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
                    getOrderDetial(OrderId);
                }
                Toast.makeText(OrderDetialActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
                LogUtils.instance.d("取消订单=" + result);
            }
        });
    }

}
