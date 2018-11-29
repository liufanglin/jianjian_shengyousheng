package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.core.CoreJob;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.PersonalRewardBean;
import com.ximai.savingsmore.save.modle.PointMessage;
import com.ximai.savingsmore.save.modle.RedPacketCount;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by caojian on 16/12/28.
 * 积分管理
 */
public class PointManagerActivity extends BaseActivity {
    private TextView share_all, share_zhekou, share_liucun;
    private TextView buy_all, buy_zhekou, buy_liucun;
    private TextView commnet_all, comment_zhekou, comment_liucun;
    private TextView heji_all, heji_zhekou, heji_liucun;

    private PointMessage pointMessage;
    private ImageView jinpai, yingpai, tongpai;
    private TextView tv_sharkTotal;
    private TextView tv_commitTotal;
    private TextView tv_ordertotal;
    private TextView tv_rebate;
    private TextView tv_rebatetotal;
    private TextView tv_spailtotal;
    private RedPacketCount redPacketCount;
    private TextView tv_spail;
    private TextView textView3;
    private ImageView iv_shang;
    private PersonalRewardBean personalRewardBean;
    private List<PersonalRewardBean.MainData> rewardList;
    private TextView tv_phone;
    private TextView tv_name;
    private KyLoadingBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_manager_activity);

        initView();

        initEvent();

        initData();
    }

    /**
     * init- view
     */
    private void initView() {
        setCenterTitle("积分返利中心");
        setLeftBackMenuVisibility(PointManagerActivity.this, "");
        share_all = (TextView) findViewById(R.id.share_all);
        share_zhekou = (TextView) findViewById(R.id.share_zhekou);
        share_liucun = (TextView) findViewById(R.id.share_liucun);
        buy_all = (TextView) findViewById(R.id.buy_all);
        buy_zhekou = (TextView) findViewById(R.id.buy_zhekou);
        buy_liucun = (TextView) findViewById(R.id.buy_liucun);
        commnet_all = (TextView) findViewById(R.id.comment_all);
        comment_zhekou = (TextView) findViewById(R.id.comment_zhekou);
        comment_liucun = (TextView) findViewById(R.id.comment_liucun);
        heji_all = (TextView) findViewById(R.id.heji_all);
        heji_zhekou = (TextView) findViewById(R.id.heji_zhekou);
        heji_liucun = (TextView) findViewById(R.id.heji_liucun);
        jinpai = (ImageView) findViewById(R.id.jinpai);
        yingpai = (ImageView) findViewById(R.id.yingpai);
        tongpai = (ImageView) findViewById(R.id.tongpai);
        tv_sharkTotal = (TextView) findViewById(R.id.tv_sharkTotal);
        tv_commitTotal = (TextView) findViewById(R.id.tv_commitTotal);
        tv_ordertotal = (TextView) findViewById(R.id.tv_ordertotal);
        tv_rebatetotal = (TextView) findViewById(R.id.tv_rebatetotal);
        tv_rebate = (TextView) findViewById(R.id.tv_rebate);
        tv_spailtotal = (TextView) findViewById(R.id.tv_spailtotal);
        tv_spail = (TextView) findViewById(R.id.tv_spail);
        textView3 = (TextView) findViewById(R.id.textView3);
        iv_shang = (ImageView) findViewById(R.id.iv_shang);
        tv_phone = (TextView) findViewById(R.id.tv_phone);//优惠促销卡
        tv_name = (TextView) findViewById(R.id.tv_name);//优惠促销卡
        //规则目前都使用本地
//        buy_guzhe = (TextView) findViewById(R.id.buy_guizhe);
//        heji_guzhe = (TextView) findViewById(R.id.heji_guizhe);
//        comment_guzhe = (TextView) findViewById(R.id.comment_guizhe);
//        share_guzhe = (TextView) findViewById(R.id.share_guizhe);
    }

    /**
     * event
     */
    private void initEvent() {
        /**
         * 赏字的点击事件
         */
        iv_shang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 首先判断是否有红包 - 没有弹出对话框
                 */
                if (true == pointMessage.PointRedPacket){
                    if (Integer.parseInt(pointMessage.Point) > 10000 && Integer.parseInt(pointMessage.OpenPointRedPacket) >= 5){
                        /**
                         * 红包对话框
                         */
                        pointDialog();
                    }else{
                        /**
                         * 调用赏字的接口
                         */
                        getPointRewardData();
                    }
                }else{
                    /**
                     * 没有奖赏
                     */
                    noPointDialog();
                }
            }
        });
    }


    /**
     * initdata -
     */
    private void initData() {
//        /**
//         * 获取积分数据
//         */
//        getPoint();
//        /**
//         * 获取红包列表 - 用来判断赏字是否显示
//         */
//        getRewardData();
        if (null == MyUserInfoUtils.getInstance().myUserInfo.RecipientsTelephone){
            tv_phone.setText("暂无");
        }else{
            if (11 == MyUserInfoUtils.getInstance().myUserInfo.RecipientsTelephone.length()){
                String s1 = MyUserInfoUtils.getInstance().myUserInfo.RecipientsTelephone.substring(0, 3);
                String s2 = MyUserInfoUtils.getInstance().myUserInfo.RecipientsTelephone.substring(7, 11);
                tv_phone.setText(s1 + "  ****  " + s2);
            }else{
                tv_phone.setText("暂无");
            }
        }

        if (null == MyUserInfoUtils.getInstance().myUserInfo.UserDisplayName){
            tv_name.setText("暂无");
        }else{
            tv_name.setText(MyUserInfoUtils.getInstance().myUserInfo.UserDisplayName);
        }
    }

    /**
     * onResume
     */
    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 获取积分数据
         */
        getPoint();
    }

    /**
     * 获取积分管理数据
     */
    private void getPoint() {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(PointManagerActivity.this, URLText.GET_POINT, RequestParamsPool.getPoint(), new MyAsyncHttpResponseHandler(PointManagerActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    pointMessage = GsonUtils.fromJson(object.optString("MainData"), PointMessage.class);
                    if (null != pointMessage) {

                        if (pointMessage.SharedLastPoint.length() > 5){
                            share_all.setText(pointMessage.SharedLastPoint.substring(0,5)+"...");
                        }else{
                            share_all.setText(pointMessage.SharedLastPoint);
                        }

                        if (pointMessage.SharedTotalPoint.length() > 5){
                            tv_sharkTotal.setText(pointMessage.SharedTotalPoint.substring(0,5)+"...");//分享总积分
                        }else{
                            tv_sharkTotal.setText(pointMessage.SharedTotalPoint);//分享总积分
                        }
                        share_liucun.setText("-");
                        share_zhekou.setText("-");

                        if (pointMessage.CommentLastPoint.length() > 5){
                            commnet_all.setText(pointMessage.CommentLastPoint.substring(0,5)+"...");
                        }else{
                            commnet_all.setText(pointMessage.CommentLastPoint);
                        }

                        if (pointMessage.CommentTotalPoint.length() > 5){
                            tv_commitTotal.setText(pointMessage.CommentTotalPoint.substring(0,5)+"...");//提交评论
                        }else{
                            tv_commitTotal.setText(pointMessage.CommentTotalPoint);//提交评论
                        }

                        comment_liucun.setText("-");
                        comment_zhekou.setText("-");

                        if (pointMessage.OrderLastPoint.length() > 5){
                            buy_all.setText(pointMessage.OrderLastPoint.substring(0,5)+"...");
                        }else{
                            buy_all.setText(pointMessage.OrderLastPoint);
                        }

                        if (pointMessage.OrderTotalPoint.length() > 5){
                            tv_ordertotal.setText(pointMessage.OrderTotalPoint.substring(0,5)+"...");//促销品消费
                        }else{
                            tv_ordertotal.setText(pointMessage.OrderTotalPoint);//促销品消费
                        }
                        buy_liucun.setText("-");

                        if (pointMessage.OrderLastPoint.length() > 5){
                            buy_zhekou.setText(pointMessage.UsedPoint.substring(0,5)+"...");//促销品消费 - 抵扣
                        }else{
                            buy_zhekou.setText(pointMessage.UsedPoint);//促销品消费 - 抵扣
                        }

                        if (pointMessage.RebateLastPoint.length() > 5){
                            tv_rebate.setText(pointMessage.RebateLastPoint.substring(0,5)+"...");
                        }else{
                            tv_rebate.setText(pointMessage.RebateLastPoint);
                        }

                        if (pointMessage.RebateTotalPoint.length() > 5){
                            tv_rebatetotal.setText(pointMessage.RebateTotalPoint.substring(0,5)+"...");//我要返利
                        }else{
                            tv_rebatetotal.setText(pointMessage.RebateTotalPoint);//我要返利
                        }

                        if (pointMessage.SpecialAwardsLastPoint.length() > 5){
                            tv_spail.setText(pointMessage.SpecialAwardsLastPoint.substring(0,5)+"...");
                        }else{
                            tv_spail.setText(pointMessage.SpecialAwardsLastPoint);
                        }

                        if (pointMessage.SpecialAwardsTotalPoint.length() > 5){
                            tv_spailtotal.setText(pointMessage.SpecialAwardsTotalPoint.substring(0,5)+"...");//特殊奖励
                        }else{
                            tv_spailtotal.setText(pointMessage.SpecialAwardsTotalPoint);//特殊奖励
                        }

                        if (pointMessage.LastTotalPoint.length() > 5){
                            textView3.setText(pointMessage.LastTotalPoint.substring(0,5)+"...");//最近积分合计
                        }else{
                            textView3.setText(pointMessage.LastTotalPoint);//最近积分合计
                        }

                        if (pointMessage.Point.length() > 5){
                            heji_liucun.setText(pointMessage.Point.substring(0,5)+"...");//合计留存
                        }else{
                            heji_liucun.setText(pointMessage.Point);//合计留存
                        }

                        if (pointMessage.UsedPoint.length() > 5){
                            heji_zhekou.setText(pointMessage.UsedPoint.substring(0,5)+"...");//合计最近抵扣
                        }else{
                            heji_zhekou.setText(pointMessage.UsedPoint);//合计最近抵扣
                        }

                        if (pointMessage.TotalPoint.length() > 5){
                            heji_all.setText(pointMessage.TotalPoint.substring(0,5)+"...");//累计积分
                        }else{
                            heji_all.setText(pointMessage.TotalPoint);//累计积分
                        }

                        if (Integer.parseInt(pointMessage.Point) > 2000 && Integer.parseInt(pointMessage.Point) <= 3000) {
                            jinpai.setVisibility(View.INVISIBLE);
                            yingpai.setVisibility(View.INVISIBLE);
                            tongpai.setVisibility(View.VISIBLE);
                        } else if (Integer.parseInt(pointMessage.Point) > 3000 && Integer.parseInt(pointMessage.Point) <= 5000 ) {
                            yingpai.setVisibility(View.VISIBLE);
                            tongpai.setVisibility(View.INVISIBLE);
                            jinpai.setVisibility(View.INVISIBLE);
                        } else if (Integer.parseInt(pointMessage.Point) > 5000){
                            yingpai.setVisibility(View.INVISIBLE);
                            tongpai.setVisibility(View.INVISIBLE);
                            jinpai.setVisibility(View.VISIBLE);
                        }

                        /**
                         * 判断赏的显示方式
                         */
                        if (true == pointMessage.PointRedPacket){
                            iv_shang.setBackgroundResource(R.mipmap.iv_shang);
                        }else{
                            iv_shang.setBackgroundResource(R.mipmap.iv_shang_default);
                        }
                    }

                    if (null != builder){
                        builder.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
    }

    /**
     * 获取积分超过1万返回的红包
     */
    private void getPointRewardData() {
        WebRequestHelper.json_post(PointManagerActivity.this, URLText.PLAY_POINTREWARD, RequestParamsPool.getPointRewardData(), new MyAsyncHttpResponseHandler(PointManagerActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String s = new String(responseBody);
                    JSONObject object = new JSONObject(s);
                    /**
                     * 跳转到奖赏中心
                     */
                    Intent intent8 = new Intent(PointManagerActivity.this,PersonalRewardActivity.class);
                    startActivity(intent8);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 将金额三位划分
     * @param s
     * @return
     */
    private String split(String s){
        String s1 = new StringBuilder(s).reverse().toString();//先将字符串颠倒顺序
        String str2 = "";
        for(int i=0;i<s1.length();i++){
            if(i*3+3>s1.length()){
                str2 += s1.substring(i*3, s1.length());
                break;
            }
            str2 += s1.substring(i*3, i*3+3)+",";
        }
        if(str2.endsWith(",")){
            str2 = str2.substring(0, str2.length()-1);
        }
        return new StringBuilder(str2).reverse().toString();//进行一个反序
    }

    /**
     * 没有红包的对话框
     */
    public void pointDialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "积分提现额度每天250分。今日已经满了，改日再提！", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 没有红包的对话框
     */
    public void noPointDialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "Ooops,还没有奖赏", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 打开loading
     */
    public void showLoading(Context context, String text){
        if (null == builder){
            builder = new KyLoadingBuilder(context);
        }
        builder.setIcon(R.mipmap.loading);
        builder.setText(text);
        builder.setOutsideTouchable(false);
        //builder.setBackTouchable(true);
        builder.show();
    }
}