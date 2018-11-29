package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.utils.UIUtils;

/**
 * Created by luxing on 2018/1/8 0008.
 * 废弃
 */

public class PointApplyActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout back;
    private TextView tv_prices;
    private TextView tv_jifen;
    private TextView tv_bianhao;
    private Button btn_apply;
    private ImageView iv_state;
    private TextView tv_state;
    private TextView tv_pointmsg1;
    private TextView tv_pointmsg2;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_apply);
        initView();
        initEvent();
        initData();
    }

    /**
     * view
     */
    private void initView() {
        /**
         * 将继承标记进行删除
         */
        toolbar.setVisibility(View.GONE);
        back = (RelativeLayout) findViewById(R.id.back);//返回
        tv_prices = (TextView) findViewById(R.id.tv_prices); //返利金额
        tv_jifen = (TextView) findViewById(R.id.tv_jifen); //积分
        tv_bianhao = (TextView) findViewById(R.id.tv_bianhao); //编号
        btn_apply = (Button) findViewById(R.id.btn_apply); //查看积分
        iv_state = (ImageView) findViewById(R.id.iv_state);//状态图片
        tv_state = (TextView) findViewById(R.id.tv_state);//状态文字
        tv_pointmsg1 = (TextView) findViewById(R.id.tv_pointmsg1);//积分发哪里说明1
        tv_pointmsg2 = (TextView) findViewById(R.id.tv_pointmsg2);//积分发哪里说明2

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
    }

    /**
     * data
     */
    private void initData() {
        String state = getIntent().getStringExtra("State");
        String StateName = getIntent().getStringExtra("StateName");//申请的状态说明
        String Point = getIntent().getStringExtra("Point");
        String Price = getIntent().getStringExtra("Price");

        if ("1".equals(state)){//申请中
            iv_state.setBackgroundResource(R.mipmap.iv_apply);
            tv1.setText("消费金额");
            tv2.setText("返利积分");
            tv3.setText("返利编号");
            tv_state.setText("申请审核中");
            tv_prices.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(Price)));
            tv_jifen.setText(Point+"分");
            tv_bianhao.setText("正在审核中");
            tv_pointmsg1.setText("1、返利积分申请已收到，正在审核中，请等候");
            tv_pointmsg2.setText("2、申请成功后积分进入您的返利积分中心");
            btn_apply.setVisibility(View.GONE);
        }else if ("2".equals(state)){//通过
            iv_state.setBackgroundResource(R.mipmap.fanli_success);
            String Number = getIntent().getStringExtra("Number");
            tv_state.setText("返利申请成功");
            tv1.setText("返利编号");
            tv2.setText("消费金额");
            tv3.setText("本次积分");
            tv_prices.setText(Number);
            tv_jifen.setText("¥"+UIUtils.formatPrice(Double.parseDouble(Price)));
            tv_bianhao.setText(Point+"分");
            tv_pointmsg1.setText("1、本次返利积分已进入您的返利积分中心");
            tv_pointmsg2.setText("2、积分兑换现金的比例按照省又省积分返利规则执行");
            btn_apply.setVisibility(View.VISIBLE);
        }else if ("3".equals(state)){//未通过
            iv_state.setBackgroundResource(R.mipmap.fanli_over);
            String Number = getIntent().getStringExtra("Number");
            tv_state.setText("返利申请未通过");
            tv1.setText("返利编号");
            tv2.setText("消费金额");
            tv3.setText("本次积分");
            tv_prices.setText(Number);
            tv_jifen.setText("¥"+UIUtils.formatPrice(Double.parseDouble(Price)));
            tv_bianhao.setText("无");
            tv_pointmsg1.setText("1、本次申请返利积分没有通过审核，无返利积分");
            tv_pointmsg2.setText("2、积分兑换现金的比例按照省又省积分返利规则执行");
        }
    }

    /**
     * event
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back://返回
                finish();
                break;
            case R.id.btn_apply://查看积分
                startActivity(new Intent(this,PointManagerActivity.class));
                break;
        }
    }
}