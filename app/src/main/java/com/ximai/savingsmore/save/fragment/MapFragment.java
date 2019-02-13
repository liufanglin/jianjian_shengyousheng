package com.ximai.savingsmore.save.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.activity.SearchResultActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.PopuWindowsUtils;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.utils.SpeechUtils;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by caojian on 16/11/21.
 * 地图fragment
 */
public class MapFragment extends Fragment implements View.OnClickListener, LocationSource, AMapLocationListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {
    private ImageView login;
    private MapView mapView;
    private com.amap.api.maps2d.AMap aMap;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private Boolean isFirstLoc = true;
    private LocationSource.OnLocationChangedListener mListener;
    private com.amap.api.maps2d.model.MarkerOptions markerOption;
    private View markerimgs;
    private Button marker_button;
    private BitmapDescriptor makerIcon;
    private PopuWindowsUtils popuWindowsUtils;
    private RelativeLayout show_popu;
    private Context context;
    private List<Goods> list = new ArrayList<Goods>();
    private List<Goods> list_gray = new ArrayList<Goods>();
    private List<Goods> list_red = new ArrayList<Goods>();
    private GoodsList goodsList = new GoodsList();
    private CallBack callBack;
    private String city;
    private double Longitude;
    private double Latitude;
    private String qu;
    private String sheng;
    private String shi;
    private List<Marker> mark_list = new ArrayList<>();
    private int marker_position;
    private TextView cuxiao_style;
    private String type = "name";
    private boolean isUpdate;
    private TextView tv_liebiao;
    private boolean isFrist = true;
    private SpeechUtils instance;
    private Handler handler =new Handler();
    private GeocodeSearch geocodeSearch;
    private String province;
    private String city1;
    private String district;
    private String pageSize;
    private String areaId;
    private String currentAreaId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, null);
        mapView = (MapView) view.findViewById(R.id.map);
        tv_liebiao = (TextView) view.findViewById(R.id.tv_liebiao);
        show_popu = (RelativeLayout) view.findViewById(R.id.rent_map_pop);
        //必须使用
        mapView.onCreate(savedInstanceState);
        cuxiao_style = (TextView) view.findViewById(R.id.cuxiao_style);
        cuxiao_style.setOnClickListener(this);
        tv_liebiao.setOnClickListener(this);
        /**
         * 进行初始化
         */
        instance = SpeechUtils.getInstance(getActivity());
        /**
         * 注册一个监听
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.LOADING_MAPDATA);
        /**
         * 关闭popuwindow
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.CLOSE_POPUWINDOW);
        /**
         * 设置map参数
         */
        loadMap();
        /**
         * 设置地图样式
         */
        setUpMap();
        /**
         * 获取商品数据
         */
      //  getAllGoods();
        return view;
    }

    /**
     * 初始化地图
     */
    private void loadMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            //设置显示定位按钮 并且可以点击
            UiSettings settings = aMap.getUiSettings();
            // 是否显示定位按钮
            settings.setMyLocationButtonEnabled(true);
            //隐藏右下角按钮
            settings.setZoomControlsEnabled(false);
            //设置了定位的监听,这里要实现LocationSource接口
            aMap.setLocationSource(this);
            //显示定位层并且可以触发定位,默认是flase
            aMap.setMyLocationEnabled(true);
            aMap.setOnCameraChangeListener(this);
            //地图标注点击事件
            aMap.setOnMarkerClickListener(this);
            //地图点击事件
            aMap.setOnMapClickListener(this);
            //地理搜索类
            geocodeSearch = new GeocodeSearch(getActivity());
            geocodeSearch.setOnGeocodeSearchListener(this);
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点 - 有背景
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 设置小蓝点的图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location_marker));
        // 设置圆形的边框颜色
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        //设置小蓝点的锚点
        // myLocationStyle.anchor(int,int)
        // 设置圆形的边框粗细
        myLocationStyle.strokeWidth(1.0f);
        aMap.setMyLocationStyle(myLocationStyle);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
    }

    /**
     * mainactivity进行回调
     * @param context
     * @param callBack
     */
    public void setDate(Context context, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (null == instance){
            /**
             * 进行初始化
             */
            instance = SpeechUtils.getInstance(getActivity());
        }
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
        NotificationCenter.defaultCenter().removeObserver(notificationCenterObserver, Constants.LOADING_MAPDATA);
        NotificationCenter.defaultCenter().removeObserver(notificationCenterObserver, Constants.CLOSE_POPUWINDOW);
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        /**
         * 的点击事件
         */
        if (R.id.tv_liebiao == v.getId()) {
            if (null != city && Longitude != 0 && Latitude != 0) {
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra("long", Longitude + "");
                intent.putExtra("lat", Latitude + "");
                if (!TextUtils.isEmpty(province)){
                    if (province.equals("上海市") ){
                        intent.putExtra("title", "上海市"+district);
                    }else if (province.equals("天津市")){
                        intent.putExtra("title", "天津市"+district);
                    }else if (province.equals("重庆市")){
                        intent.putExtra("title", "重庆市"+district);
                    }else if (province.equals("北京市")){
                        intent.putExtra("title", "北京市"+district);
                    }else if (province.equals("中国香港")){
                        intent.putExtra("title", "中国香港"+district);
                    }else if (province.equals("中国澳门")){
                        intent.putExtra("title", "中国澳门"+district);
                    }else{
                        intent.putExtra("title", province + district);
                        intent.putExtra("qu", district);
                        intent.putExtra("shi", city1);
                        intent.putExtra("sheng", province);
                    }
                }
                startActivity(intent);
            }
        }
        /**
         * 促销形式
         */
        if (R.id.cuxiao_style == v.getId()) {
            if (null != popuWindowsUtils){
                popuWindowsUtils.dismixss();
            }
            if (cuxiao_style.getText().toString().equals("促销商品")) {
                cuxiao_style.setText("促销优惠");
                type = "name";
                aMap.clear();
                mark_list.clear();//先进行清除
                int a = list.size();
                for (int i = 0; i < a; i++) {
                    markeradd(list_red.get(i), false);
                }
                int b = list_gray.size();
                for (int i = 0; i < b; i++) {
                    markeradd(list_gray.get(i), true);
                }
            } else {
                mark_list.clear();//先进行清除
                cuxiao_style.setText("促销商品");
                type = "style";
                aMap.clear();
                int a = list_red.size();
                for (int i = 0; i < a; i++) {
                    markeradd(list_red.get(i), false);
                }
                int b = list_gray.size();
                for (int i = 0; i < b; i++) {
                    markeradd(list_gray.get(i), true);
                }
            }
        }
    }

    /**
     * 激活定位
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getContext());
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

    /**
     * 定位成功回调
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(final AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);//显示系统小蓝点
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                BaseApplication.getInstance().Longitude = aMapLocation.getLongitude();
                BaseApplication.getInstance().Latitude = aMapLocation.getLatitude();
                Longitude=aMapLocation.getLongitude();
                Latitude=aMapLocation.getLatitude();
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间

                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息

                city = aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                //callBack.location(aMapLocation.getCity(), aMapLocation.getDistrict());
                //city = aMapLocation.getCity() + "-" + aMapLocation.getDistrict();

                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                currentAreaId=aMapLocation.getAdCode();//地区编码
                //保存地址到全局 - 在非促销品返利
                BaseApplication.getInstance().userAddress = aMapLocation.getAddress()+aMapLocation.getAoiName();
                /**
                 * 判断是商家登录还是个人登录
                 */
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String isPeopleAndBusiness = PrefUtils.getString(getActivity(), "isPeopleAndBusiness", "");
                            if (!TextUtils.isEmpty(isPeopleAndBusiness) && null != isPeopleAndBusiness){
                                if ("2".equals(isPeopleAndBusiness)){
                                    //定位成功之后将数据进行上传
                                    /**
                                     * 附近促销商家
                                     */
                                    businessDistance(aMapLocation.getLongitude(),aMapLocation.getLatitude());
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },10000);

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
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
                getAllGoods();
                //数据本来是移动地图实时鞥更新数据 - 但是现在先凡在这里面
                callBack.location(aMapLocation.getCity()+ aMapLocation.getDistrict(), aMapLocation.getLatitude(),aMapLocation.getLongitude());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                //Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 停止定位
     */
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
     * 点击地图进行定位
     */
    @Override
    public void onMapClick(LatLng latLng) {
        if (null != popuWindowsUtils) {
            popuWindowsUtils.dismixss();
        }
    }

    /**
     * 地图图标点击监听
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        final int c = list.size();//商品总数量
        for (int i = 0; i < c; i++) {
            if (!list_gray.contains(list.get(i))) {//灰色的设置
                isUpdate = true;
                break;
            }
        }
        if (isUpdate) {
            for (int i = 0; i < mark_list.size(); i++) {
                if (marker.equals(mark_list.get(i))) {
                    marker_position = i;
                    list_gray.add(list.get(i));
                    break;
                }
            }
            aMap.clear();
            mark_list.clear();
            for (int i = 0; i < c; i++) {
                if (list_gray.contains(list.get(i))) {
                    markeradd(list_red.get(i), true);
                } else {
                    markeradd(list_red.get(i), false);
                }
            }
        }


        //跳转事件 跳转到列表
        if (list.get(marker_position).CoordinatesCount>1){
            if ( Longitude != 0 && Latitude != 0) {
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra("long", list.get(marker_position).Longitude + "");
                intent.putExtra("lat", list.get(marker_position).Latitude + "");
                intent.putExtra("Radius","1");
                if (!TextUtils.isEmpty(province)){
                    if (province.equals("上海市") ){
                        intent.putExtra("title", "上海市"+district);
                    }else if (province.equals("天津市")){
                        intent.putExtra("title", "天津市"+district);
                    }else if (province.equals("重庆市")){
                        intent.putExtra("title", "重庆市"+district);
                    }else if (province.equals("北京市")){
                        intent.putExtra("title", "北京市"+district);
                    }else if (province.equals("中国香港")){
                        intent.putExtra("title", "中国香港"+district);
                    }else if (province.equals("中国澳门")){
                        intent.putExtra("title", "中国澳门"+district);
                    }else{
                        intent.putExtra("title", province + district);
                        intent.putExtra("qu", district);
                        intent.putExtra("shi", city1);
                        intent.putExtra("sheng", province);
                    }
                }
                startActivity(intent);
            }
            return true;
        }

        if (null == popuWindowsUtils) {
            popuWindowsUtils = new PopuWindowsUtils(getActivity(), list, new PopuWindowsUtils.callBack() {
                @Override
                public void call(int posiiton) {
                    if (posiiton != marker_position) {
                        aMap.clear();
                        mark_list.clear();
                        if (list.size()==0){
                            return;
                        }
                        if (list.size()>posiiton) {
                            list_gray.add(list.get(posiiton));
                        }

                        for (int i = 0; i < c; i++) {
                            if (list_gray.contains(list.get(i))) {
                                markeradd(list_red.get(i), true);
                            } else {
                                markeradd(list_red.get(i), false);
                            }
                        }

//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        }).start();



                    }
                }
            });
        }else{
            popuWindowsUtils.dismixss();
        }
        if (!popuWindowsUtils.isShowing()) {
            popuWindowsUtils.showAsDropDown(show_popu, marker_position);
        }
    //    setUpMap();//解决地图蓝点消失的问题
        return true;
    }

    /**
     * 获取数据将Marker 添加到相应的经纬度
     */
    private void markeradd(Goods goods, boolean isClick) {
        com.amap.api.maps2d.model.LatLng latlng = new com.amap.api.maps2d.model.LatLng(Double.parseDouble(goods.Latitude), Double.parseDouble(goods.Longitude));
        markerOption = new MarkerOptions();
        markerimgs = FrameLayout.inflate(context, R.layout.markerimgs, null);
        marker_button = (Button) markerimgs.findViewById(R.id.marker_content);
        if (type.equals("name")) {//判断是否先促销名称和促销形式
            //跳转事件 跳转到列表
            if (goods.CoordinatesCount>1){
                marker_button.setText(goods.CoordinatesCount+"个促销品");
            }else {
                marker_button.setText(goods.Name);
            }
        } else {
            marker_button.setText(goods.Preferential);
            if (goods.Preferential.length() > 5){
                marker_button.setText(goods.Preferential.substring(0,5)+"...");
            }else{
                marker_button.setText(goods.Preferential);
            }
        }
        marker_button.setTextSize(11);
        //判断是否被点击
        if (isClick) {
            marker_button.setBackgroundResource(R.mipmap.map_icon1);
        } else {
            marker_button.setBackgroundResource(R.mipmap.map_icon2);
        }
        makerIcon = BitmapDescriptorFactory.fromView(markerimgs); // 显示自定义的图标
        markerOption.position(latlng);// 添加图标的经纬度
        // markerOption.title(mapdatainfo.getId() + "");
        markerOption.icon(makerIcon).anchor(0, 1); // 用于传输职位详情id
        mark_list.add(aMap.addMarker(markerOption));
    }

    /**
     * 得到所有的商品
     */
    private void getAllGoods() {
        aMap.clear();
        list.clear();
        list_gray.clear();
        list_red.clear();
        mark_list.clear();
        if (popuWindowsUtils!=null){
            popuWindowsUtils.dismixss();
        }
        //将弹框独享设置为空
        popuWindowsUtils = null;
        //设置数据
        String number = PreferencesUtils.getString(getActivity(), "number");
        if ("不限".equals(number)){
            pageSize = "1000";
        }else{
            pageSize = number;
        }
        WebRequestHelper.json_post(context, URLText.GET_GOODS, RequestParamsPool.getAllGoods("true", null, null, null, Longitude+"", Latitude+"", 1, Integer.parseInt(pageSize), false, false, false, false, "", "", "", "", "", "", ""), new MyAsyncHttpResponseHandler(context) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                String ss=new String(responseBody);
                if (goodsList==null) return;
                if (goodsList.IsSuccess) {
                    if (null != goodsList.MainData) {
                        list = goodsList.MainData;
                        list_red.addAll(list);
                        for (int i = 0; i < list.size(); i++) {
                            markeradd(list.get(i), false);
                        }
                    }
                }
            }
        });
    }

    /**
     * 附近促销商家
     */
    private void businessDistance(double Longitude,double Latitude) {
        WebRequestHelper.json_post(context, URLText.BUSINESS_DISTANCE, RequestParamsPool.businessDistance(Longitude,Latitude), new MyAsyncHttpResponseHandler(context) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
            }
        });
    }

    /**
     * 移动时候的回调
     * @param cameraPosition
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        LatLng latLng = cameraPosition.target;
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
     * 定位改变监听回调
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
     * 得到逆地理编码异步查询结果
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        //省
        province = regeocodeAddress.getProvince();
        //市
        city1 = regeocodeAddress.getCity();
        //区
        district = regeocodeAddress.getDistrict();
        /**
         * 数据回调到mainactivity
         */

        areaId=regeocodeAddress.getAdCode();
        if (!areaId.equals(currentAreaId)){
            currentAreaId=areaId;
            getAllGoods();
        }

        callBack.location(province + district,Latitude,Longitude);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * mainactivity进行回调
     */
    public interface CallBack {
        void location(String city, double Longitude, double Latitude);
    }

    /**
     * 观察者
     */
    NotificationCenter.NotificationCenterObserver notificationCenterObserver = new NotificationCenter.NotificationCenterObserver(){
        @Override
        public void onReceive(String eventName,Object cid) {
            try {
                if (Constants.LOADING_MAPDATA.equals(eventName)) {
                    /**
                     * 获取商品数据
                     */
                    getAllGoods();
                }else if(Constants.CLOSE_POPUWINDOW.equals(eventName)){
                    if (null != popuWindowsUtils){
                        popuWindowsUtils.dismixss();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}