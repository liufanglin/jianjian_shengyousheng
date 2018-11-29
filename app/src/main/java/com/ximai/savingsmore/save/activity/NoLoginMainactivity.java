package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.fragment.MapFragment;
import com.ximai.savingsmore.save.utils.PopuWindowsUtils;

/**
 * Created by caojian on 16/11/17.
 * 退出登录后跳转此页面
 */
public class NoLoginMainactivity extends FragmentActivity implements View.OnClickListener {
    private MapView mapView;
    private com.amap.api.maps2d.AMap aMap;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private Boolean isFirstLoc = true;
    private com.amap.api.maps2d.model.MarkerOptions markerOption;
    //private MarkerOptions markerOption;
    private View markerimgs;
    private Button marker_button;
    private BitmapDescriptor makerIcon;
    private ImageView toList;
    private LinearLayout search;

    private PopuWindowsUtils popuWindowsUtils;
    private RelativeLayout show_popu;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MapFragment mapFragment;
    private ImageView login;
    private TextView location;
    private String address;
    double Longitude1 = 0;
    double Latitude1 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_login_activity);
        fragmentManager = this.getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        location = (TextView) findViewById(R.id.location);
        mapFragment = new MapFragment();
        mapFragment.setDate(NoLoginMainactivity.this, new MapFragment.CallBack() {
            @Override
            public void location(String city, double Longitude, double Latitude) {
                if (city.contains("null")){
                    String aNull = city.replace("null", "");
                    location.setText(aNull);
                    address = aNull;
                }else{
                    location.setText(city);
                    address = city;
                }
            }
        });
        fragmentTransaction.add(R.id.content, mapFragment).commit();
        login = (ImageView) findViewById(R.id.login);
        login.setOnClickListener(this);
//        mapView = (MapView) findViewById(R.id.map);
//        toList= (ImageView) findViewById(R.id.list);
        search = (LinearLayout) findViewById(R.id.search);
//        show_popu= (RelativeLayout) findViewById(R.id.rent_map_pop);
//        mapView.onCreate(savedInstanceState);
        search.setOnClickListener(this);
//        toList.setOnClickListener(this);
//        popuWindowsUtils=new PopuWindowsUtils(NoLoginMainactivity.this);
//
//        // aMapLocation=new AMapLocation();
//        if (aMap == null) {
//            aMap = mapView.getMap();
//            //设置显示定位按钮 并且可以点击
//            UiSettings settings = aMap.getUiSettings();
//
//            settings.setZoomControlsEnabled(false);
//            aMap.setLocationSource(this);//设置了定位的监听,这里要实现LocationSource接口
//            // 是否显示定位按钮
//            settings.setMyLocationButtonEnabled(true);
//            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
//
//            //初始化定位
//            mLocationClient = new AMapLocationClient(getApplicationContext());
//            //设置定位回调监听，这里要实现AMapLocationListener接口，AMapLocationListener接口只有onLocationChanged方法可以实现，用于接收异步返回的定位结果，参数是AMapLocation类型。
//            mLocationClient.setLocationListener(this);
//            //初始化定位参数
//            mLocationOption = new AMapLocationClientOption();
//            //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//            //设置是否返回地址信息（默认返回地址信息）
//            mLocationOption.setNeedAddress(true);
//            //设置是否只定位一次,默认为false
//            mLocationOption.setOnceLocation(false);
//            //设置是否强制刷新WIFI，默认为强制刷新
//            mLocationOption.setWifiActiveScan(true);
//            //设置是否允许模拟位置,默认为false，不允许模拟位置
//            mLocationOption.setMockEnable(false);
//            //设置定位间隔,单位毫秒,默认为2000ms
//            mLocationOption.setInterval(2000);
//            //给定位客户端对象设置定位参数
//            mLocationClient.setLocationOption(mLocationOption);
//            //启动定位
//            mLocationClient.startLocation();
//        }
//        markeradd();
//        aMap.setOnMarkerClickListener(this);
//        aMap.setOnMapClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.login) {
//            Intent intent = new Intent(NoLoginMainactivity.this, LoginActivity.class);
//            startActivity(intent);
//        }
//        if(R.id.search==v.getId()){
//            Intent intent = new Intent(NoLoginMainactivity.this, SearchActivity.class);
//            startActivity(intent);
//        }
//
//    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                Intent intent = new Intent(NoLoginMainactivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.list:
                if (Longitude1 != 0 && 0 != Latitude1) {
                    Intent intent1 = new Intent(NoLoginMainactivity.this, SearchResultActivity.class);
                    intent1.putExtra("long", Longitude1 + "");
                    intent1.putExtra("lat", Latitude1 + "");
                    intent1.putExtra("title", address);
                    startActivity(intent1);
                }
                break;
            case R.id.search:
                Intent intent2 = new Intent(NoLoginMainactivity.this, SearchActivitys.class);
                startActivity(intent2);
                break;
        }
    }


//    @Override
//    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        mListener = onLocationChangedListener;
//    }

//    @Override
//    public void deactivate() {
//        mListener = null;
//    }

//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null) {
//            if (aMapLocation.getErrorCode() == 0) {
//                //定位成功回调信息，设置相关消息
//                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
//                aMapLocation.getLatitude();//获取纬度
//                aMapLocation.getLongitude();//获取经度
//                aMapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(aMapLocation.getTime());
//                df.format(date);//定位时间
//                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                aMapLocation.getCountry();//国家信息
//                aMapLocation.getProvince();//省信息
//                aMapLocation.getCity();//城市信息
//                aMapLocation.getDistrict();//城区信息
//                aMapLocation.getStreet();//街道信息
//                aMapLocation.getStreetNum();//街道门牌号信息
//                aMapLocation.getCityCode();//城市编码
//                aMapLocation.getAdCode();//地区编码
//
//                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
//                if (isFirstLoc) {
//                    //设置缩放级别
//                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
//                    //将地图移动到定位点
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
//                    //点击定位按钮 能够将地图的中心移动到定位点
//                    mListener.onLocationChanged(aMapLocation);
//                    //获取定位信息
//                    StringBuffer buffer = new StringBuffer();
//                    buffer.append(aMapLocation.getCountry() + ""
//                            + aMapLocation.getProvince() + ""
//                            + aMapLocation.getCity() + ""
//                            + aMapLocation.getProvince() + ""
//                            + aMapLocation.getDistrict() + ""
//                            + aMapLocation.getStreet() + ""
//                            + aMapLocation.getStreetNum());
//                    isFirstLoc = false;
//                }
//            } else {
//                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                //Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
//            }
//        }
//
//    }
//    @Override
//    public void onMapClick(LatLng latLng) {
//
//    }latLng


//    //marker的点击事件
//    @Override
//    public boolean onMarkerClick(com.amap.api.maps2d.model.Marker marker) {
//        popuWindowsUtils.showAsDropDown(show_popu);
//        return true;
//    }
}
