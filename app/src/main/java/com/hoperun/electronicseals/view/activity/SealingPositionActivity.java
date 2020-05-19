package com.hoperun.electronicseals.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.bean.DeviceEventResp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SealingPositionActivity extends Activity {
    private MapView mMapView = null;

    private BaiduMap mMap = null;

    private LocationClient mLocationClient = null;

    private SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sealingposition);

        initView();

        initBaiduMap();

        locationInitialization();

        addMark();


        mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            //marker被点击时回调的方法
            //若响应点击事件，返回true，否则返回false
            //默认返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                //用来构造InfoWindow的view
                View mapInfoView = LayoutInflater.from(SealingPositionActivity.this).inflate(R.layout.item_exception, null);
                TextView tsealIdTV = mapInfoView.findViewById(R.id.box_seal_id_tv);
                TextView sealTimeTV = mapInfoView.findViewById(R.id.box_seal_time_tv);
                TextView sealAddressTV = mapInfoView.findViewById(R.id.box_seal_addresss_tv);
                TextView sealInfoTV = mapInfoView.findViewById(R.id.box_seal_info_tv);
                tsealIdTV.setText("864480040662891");
                sealTimeTV.setText(format.format(1586795195000l));
                sealAddressTV.setText("莲湖区丰庆路188号");
                sealInfoTV.setText("设备被移动");
                // 构造InfoWindow
                // point 描述的位置点TextView
                // -100 InfoWindow相对于point在y轴的偏移量
                InfoWindow mInfoWindow = new InfoWindow(mapInfoView, marker.getPosition(), -100);

                // 使InfoWindow生效
                mMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }
    private void addMark() {
//        DeviceEventResp mapinfo = (DeviceEventResp) getIntent().getSerializableExtra("mapinfo");
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_gcoding);
        // 创建OverlayOptions的集合
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        // 构造大量坐标数据
        LatLng point1 = new LatLng(34.203415, 108.846846);
        LatLng point2 = new LatLng(34.239294, 108.981088);
        LatLng point3 = new LatLng(34.249082, 108.922591);
        LatLng point4 = new LatLng(34.302538, 108.946162);
        LatLng point5 = new LatLng(34.269371, 108.892408);
        LatLng point6 = new LatLng(34.247889, 108.960248);
        LatLng point7 = new LatLng(34.256423, 108.92834);
        LatLng point8 = new LatLng(39.901493, 116.364686);
        LatLng point9 = new LatLng(29.674411, 91.040807);
        LatLng point10 = new LatLng(43.838416, 87.361348);
        final List<LatLng> latLngs = new ArrayList<>();
        latLngs.add(point1);
        latLngs.add(point2);
        latLngs.add(point3);
        latLngs.add(point4);
        latLngs.add(point5);
        latLngs.add(point6);
        latLngs.add(point7);
        latLngs.add(point8);
        latLngs.add(point9);
        latLngs.add(point10);
        mMap.setOnMapLoadedCallback(() -> {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(LatLng latLng : latLngs){
                builder = builder.include(latLng);
            }
            LatLngBounds latlngBounds = builder.build();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds,mMapView.getWidth(),mMapView.getHeight());
            mMap.animateMapStatus(u);
        });

        // 创建OverlayOptions属性
        OverlayOptions option1 = new MarkerOptions()
                .position(point1)
                .icon(bitmap);
        OverlayOptions option2 = new MarkerOptions()
                .position(point2)
                .icon(bitmap);
        OverlayOptions option3 = new MarkerOptions()
                .position(point3)
                .icon(bitmap);
        OverlayOptions option4 = new MarkerOptions()
                .position(point4)
                .icon(bitmap);
        OverlayOptions option5 = new MarkerOptions()
                .position(point5)
                .icon(bitmap);
        OverlayOptions option6 = new MarkerOptions()
                .position(point6)
                .icon(bitmap);
        OverlayOptions option7 = new MarkerOptions()
                .position(point7)
                .icon(bitmap);
        OverlayOptions option8 = new MarkerOptions()
                .position(point8)
                .icon(bitmap);
        OverlayOptions option9 = new MarkerOptions()
                .position(point9)
                .icon(bitmap);
        OverlayOptions option10 = new MarkerOptions()
                .position(point10)
                .icon(bitmap);
        // 将OverlayOptions添加到list
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);
        options.add(option5);
        options.add(option6);
        options.add(option7);
        options.add(option8);
        options.add(option9);
        options.add(option10);
        // 在地图上批量添加
        mMap.addOverlays(options);
    }

    private void locationInitialization() {
        // 定位初始化
        mLocationClient = new LocationClient(this);

        // 通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        // 设置locationClientOption
        mLocationClient.setLocOption(option);

        // 注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        // 开启地图定位图层
        mLocationClient.start();
    }

    private void initBaiduMap() {
        mMap = mMapView.getMap();
        // 设置为普通地图
        mMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mMap.setMyLocationEnabled(true);
    }

    private void initView() {
        mMapView = findViewById(R.id.bmapView);
    }


    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    class MyLocationListener extends BDAbstractLocationListener {
        boolean isFirst = true;

        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            Log.i("gongyu", "location.getRadius()  " + location.getRadius() + "   location.getDirection()  " + location.getDirection() +
                    "   location.getLatitude()   " + location.getLatitude() + "  location.getLongitude()  " + location.getLongitude());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mMap.setMyLocationData(locData);
            if (isFirst) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
                mMap.animateMapStatus(msu);
                isFirst = false;
            }
        }
    }
}
