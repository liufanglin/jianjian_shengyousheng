package com.ximai.savingsmore.save.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.utils.PopuWindowsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luxing on 2018/1/4 0004.
 * 搜索商品进行地图显示
 */

public class SearchDataActivity extends BaseActivity implements LocationSource, AMap.OnCameraChangeListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener, AMapLocationListener, View.OnClickListener, GeocodeSearch.OnGeocodeSearchListener {

    private List<Goods> listGoods = new ArrayList<>();//商品数据
    private MapView mapView;
    private com.amap.api.maps2d.AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private Boolean isFirstLoc = true;
    private String city;
    private String qu;
    private String sheng;
    private String shi;
    private double Longitude;
    private double Latitude;
    private PopuWindowsUtils popuWindowsUtils;
    private List<Goods> list_gray = new ArrayList<Goods>();
    private boolean isUpdate;
    private List<Marker> mark_list = new ArrayList<>();
    private int marker_position;
    private List<Goods> list_red = new ArrayList<Goods>();
    private RelativeLayout show_popu;
    private MarkerOptions markerOption;
    private View markerimgs;
    private Button marker_button;
    private String type = "name";
    private BitmapDescriptor makerIcon;
    private RelativeLayout back;
    private TextView cen_title;
    private GeocodeSearch geocodeSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_data);

        initView();

        initEvent();
        mapView.onCreate(savedInstanceState);//必须使用

        loadMap();
        /**
         * 设置地图样式
         */
        setUpMap();

        initData();
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
    }

    /**
     * 初始化地图
     */
    private void loadMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            UiSettings settings = aMap.getUiSettings();//设置显示定位按钮 并且可以点击
            settings.setMyLocationButtonEnabled(true);// 是否显示定位按钮
            settings.setZoomControlsEnabled(false);//隐藏右下角按钮
            aMap.setLocationSource(this);//设置了定位的监听,这里要实现LocationSource接口
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
            aMap.setOnCameraChangeListener(this);
            aMap.setOnMarkerClickListener(this);//地图标注点击事件
            aMap.setOnMapClickListener(this);//地图点击事件
        }

        geocodeSearch = new GeocodeSearch(this);//地理搜索类
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 设置一些amap的属性F
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点 - 有背景
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);                                                   // 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));                                // 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)                                                          //设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);                                                          // 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);                                                               // 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);                                      // 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);                                                            // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    /**
     * view
     */
    private void initView() {
        /**
         * 将继承标记进行删除
         */
        toolbar.setVisibility(View.GONE);

        mapView = (MapView) findViewById(R.id.map);
        show_popu = (RelativeLayout) findViewById(R.id.rent_map_pop);
        back = (RelativeLayout) findViewById(R.id.back);
        cen_title = (TextView) findViewById(R.id.cen_title);
    }

    private void initData() {
//        listGoods = (List<Goods>) getIntent().getExtras().getSerializable("listGoods");
        if (null != BaseApplication.getInstance().listGoods){
            listGoods = BaseApplication.getInstance().listGoods;
            if (null != listGoods){
                list_red.addAll(listGoods);
                for (int i = 0; i < listGoods.size(); i++) {
                    markeradd(listGoods.get(i), false);
                }
            }
        }

        /**
         * 移动到经纬度
         */
        if (null != listGoods.get(0)){
            if (null != listGoods.get(0).Latitude && null != listGoods.get(0).Longitude){
                String latitude = listGoods.get(0).Latitude;
                String longitude = listGoods.get(0).Longitude;
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)), 13.0f));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            mLocationOption.setInterval(120000);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * 初始化定位
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);//显示系统小蓝点
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                BaseApplication.getInstance().Longitude = aMapLocation.getLongitude();
                BaseApplication.getInstance().Latitude = aMapLocation.getLatitude();
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息

               aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                //callBack.location(aMapLocation.getCity(), aMapLocation.getDistrict());
                //city = aMapLocation.getCity() + "-" + aMapLocation.getDistrict();

                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(12));//设置地图缩放级别
                    //将地图移动到定位点-------------------------------------------------------
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double.parseDouble(listGoods.get(0).Latitude), Double.parseDouble(listGoods.get(0).Longitude))));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getStreet() + ""
                            + aMapLocation.getStreetNum());
                    isFirstLoc = false;
                }
                cen_title.setText(aMapLocation.getCity()+ aMapLocation.getDistrict());//-----------------------------------------------------------------2018.7.10修改
                //数据本来是移动地图实时鞥更新数据 - 但是现在先凡在这里面
//                callBack.location(aMapLocation.getCity()+ aMapLocation.getDistrict(), aMapLocation.getLatitude(),aMapLocation.getLongitude());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                //Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {}

    /**
     * 移动中定位继续刷新
     * @param position
     */
    @Override
    public void onCameraChangeFinish(CameraPosition position) {
        LatLng latLng = position.target;
        Longitude = latLng.longitude;
        Latitude = latLng.latitude;
        getAddressByLatlng(latLng);
    }

    /**
     * 异步查询
     * @param latLng
     */
    private void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);//异步查询
    }

    /**
     * 得到逆地理编码异步查询结果
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String province = regeocodeAddress.getProvince();//省
        String city = regeocodeAddress.getCity();//市区
        String district = regeocodeAddress.getDistrict();//区
        cen_title.setText(province + district);//-----------------------------------------------------------------2018.7.10修改
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (null != popuWindowsUtils) {
            popuWindowsUtils.dismixss();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final int c = listGoods.size();
        for (int i = 0; i < c; i++) {
            if (!list_gray.contains(listGoods.get(i))) {
                isUpdate = true;
                break;
            }
        }
        if (isUpdate) {
            for (int i = 0; i < mark_list.size(); i++) {
                if (marker.equals(mark_list.get(i))) {
                    marker_position = i;
                    list_gray.add(listGoods.get(i));
                    break;
                }
            }
            aMap.clear();
            mark_list.clear();
            for (int i = 0; i < c; i++) {
                if (list_gray.contains(listGoods.get(i))) {
                    markeradd(list_red.get(i), true);
                } else {
                    markeradd(list_red.get(i), false);
                }
            }
        }
        if (null == popuWindowsUtils) {
            popuWindowsUtils = new PopuWindowsUtils(this, listGoods, new PopuWindowsUtils.callBack() {
                @Override
                public void call(int posiiton) {
                    if (posiiton != marker_position) {
                        aMap.clear();
                        mark_list.clear();
                        list_gray.add(listGoods.get(posiiton));
                        for (int i = 0; i < c; i++) {
                            if (list_gray.contains(listGoods.get(i))) {
                                markeradd(list_red.get(i), true);
                            } else {
                                markeradd(list_red.get(i), false);
                            }
                        }
                    }
                }
            });
        }

        if (!popuWindowsUtils.isShowing()) {
            popuWindowsUtils.showAsDropDown(show_popu, marker_position);
        }
        /**
         * 解决地图蓝点消失的问题
         */
        setUpMap();
        return true;
    }

    /**
     * 获取数据将Marker 添加到相应的经纬度
     */
    private void markeradd(Goods goods, boolean isClick) {
        com.amap.api.maps2d.model.LatLng latlng = new com.amap.api.maps2d.model.LatLng(Double.parseDouble(goods.Latitude), Double.parseDouble(goods.Longitude));
        markerOption = new MarkerOptions();
        markerimgs = FrameLayout.inflate(this, R.layout.markerimgs, null);
        marker_button = (Button) markerimgs.findViewById(R.id.marker_content);
        if (type.equals("name")) {
            marker_button.setText(goods.Name);
        } else {
//            marker_button.setText(goods.Preferential);
            if (goods.Preferential.length() > 5){
                marker_button.setText(goods.Preferential.substring(0,5)+"...");
            }else{
                marker_button.setText(goods.Preferential);
            }

        }
        marker_button.setTextSize(11);
        if (isClick) {
            marker_button.setBackgroundResource(R.mipmap.map_icon1);
        } else {
            marker_button.setBackgroundResource(R.mipmap.map_icon2);
        }
        // 显示自定义的图标
        makerIcon = BitmapDescriptorFactory.fromView(markerimgs);
        // 添加图标的经纬度
        markerOption.position(latlng);
        // 用于传输职位详情id
        // markerOption.title(mapdatainfo.getId() + "");
        markerOption.icon(makerIcon).anchor(0, 1);
        mark_list.add(aMap.addMarker(markerOption));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }


    /**
     * mainactivity进行回调
     */
//    public interface CallBack {
//        void location(String city, double Longitude, double Latitude);
//    }
}