package com.ximai.savingsmore.save.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.fragment.FinishSalesGoodFragmentCe;
import com.ximai.savingsmore.save.fragment.SalesGoodFraments;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * Created by caojian on 16/12/9.
 */
public class IssuGoodActivity extends BaseActivity implements View.OnClickListener {
    private TextView isBag, noBag;
    private View introduce, sales_good;
    private FragmentManager fragmentManager;
    private SalesGoodFraments isBagGoodFragment;
    private FinishSalesGoodFragmentCe noBagGoodFragment;
    private RelativeLayout back;
    private RelativeLayout rl_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_good_activity);

        initView();

        initData();

        initEvent();
    }

    /**
     * init - view
     */
    private void initView() {
        /**
         * 将继承标记进行删除
         */
        toolbar.setVisibility(View.GONE);
        introduce = findViewById(R.id.introduce);
        sales_good = findViewById(R.id.cuxiao_goog);
        isBag = (TextView) findViewById(R.id.is_bag);
        noBag = (TextView) findViewById(R.id.no_bag);
        back = (RelativeLayout) findViewById(R.id.back);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
    }

    private void initData() {
        isBagGoodFragment = new SalesGoodFraments();
        noBagGoodFragment = new FinishSalesGoodFragmentCe();
        Bundle bundle = new Bundle();
        bundle.putString("isComment", "false");
        isBagGoodFragment.setArguments(bundle);
        noBagGoodFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment, isBagGoodFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment, noBagGoodFragment).commit();
        fragmentManager.beginTransaction().show(isBagGoodFragment).commit();
        fragmentManager.beginTransaction().hide(noBagGoodFragment).commit();
    }

    private void initEvent() {
        isBag.setOnClickListener(this);
        noBag.setOnClickListener(this);
        back.setOnClickListener(this);
        rl_add.setOnClickListener(this);
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.is_bag:
                introduce.setVisibility(View.VISIBLE);
                sales_good.setVisibility(View.INVISIBLE);
                fragmentManager.beginTransaction().hide(noBagGoodFragment).commit();
                fragmentManager.beginTransaction().show(isBagGoodFragment).commit();
                break;
            case R.id.no_bag:
                introduce.setVisibility(View.INVISIBLE);
                sales_good.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().hide(isBagGoodFragment).commit();
                fragmentManager.beginTransaction().show(noBagGoodFragment).commit();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.rl_add:
                if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("1")){
                    Onedialog();
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("2")){
                    Twodialog();
                } else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("3")){
                    Intent intent = new Intent(IssuGoodActivity.this, AddGoodsAcitivyt.class);
                    startActivity(intent);
//                    TwodialogFabu();
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("4")){
                    /**
                     * 打电话
                     */
                    dialog();
                }
                break;
                default:
                    break;
        }
    }

    /**
     * 未通过审核 - 进行一个弹框操作
     */
    public void dialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                call("02158366991");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "您的入驻申请未通过审核，请联系我们 021-58366991。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 打电话
     *
     * @param phoneNumber
     */
    public void initCallPhone(final String phoneNumber) {
        AndPermission.with(this)
                .requestCode(300)
                .permission(Manifest.permission.CALL_PHONE)
                .rationale(new RationaleListener() {

                    @Override
                    public void showRequestPermissionRationale(int i, final Rationale rationale) {
                        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
//                        AndPermission.rationaleDialog(mActivity, rationale).show();
                        com.yanzhenjie.alertdialog.AlertDialog.newBuilder(IssuGoodActivity.this)
                                .setTitle("友好提醒")
                                .setMessage("你已拒绝过定位权限，沒有定位定位权限无法为你推荐附近的商品，你看着办！")
                                .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        rationale.resume();
                                    }
                                })
                                .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        rationale.cancel();
                                    }
                                }).show();
                    }
                })
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        if (requestCode == 300) { // Successfully.
                            intentToCall(phoneNumber);
                        }
                    }
                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(IssuGoodActivity.this, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(IssuGoodActivity.this, 400)
                                    .setTitle("权限申请失败")
                                    .setMessage("您拒绝了我们必要的一些权限，请去设置打开拨打电话权限")
                                    .setPositiveButton("好，去设置")
                                    .show();
                        }
                        if (requestCode == 200) {// Failure.
                            Toast.makeText(IssuGoodActivity.this, "请去设置打开拨打电话权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .start();
    }

    /**
     * 打电话
     * @param phoneNumber
     */
    private void intentToCall(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri data = Uri.parse("tel:" + phoneNumber);
            intent.setData(data);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        } catch (Exception e) {
            System.out.println("call phone error");
            e.printStackTrace();
        }
    }

    /**
     * 未申请
     */
    public void Onedialog(){
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
        Dialog dialog = new XiMaiPopDialog(IssuGoodActivity.this, "温馨提示", "请完成注册商家信息，再发布促销。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 待审核
     */
    public void Twodialog(){
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
        Dialog dialog = new XiMaiPopDialog(IssuGoodActivity.this, "温馨提示", "您提交的入驻申请正在审核，请耐心等待。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 待审核
     */
    public void TwodialogFabu(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();

                //去我的个人中心
                Intent intent2 = new Intent(IssuGoodActivity.this, BusinessMyCenterActivity.class);
                intent2.putExtra("title", "我的中心");
                startActivity(intent2);
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
                //继续发布
                Intent intent = new Intent(IssuGoodActivity.this, AddGoodsAcitivyt.class);
                startActivity(intent);
            }
        };
        Dialog dialog = new XiMaiPopDialog(IssuGoodActivity.this, "温馨提示", "请确认您发布的促销与贵公司“主营商品”种类一致!可以在“我的中心”选择“主营商品”调整!", "知道了","取消", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}