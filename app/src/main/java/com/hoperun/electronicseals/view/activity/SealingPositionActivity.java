package com.hoperun.electronicseals.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

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
import com.hoperun.electronicseals.R;

import java.util.ArrayList;
import java.util.List;

public class SealingPositionActivity extends Activity {
    private MapView mMapView = null;

    private BaiduMap mMap = null;

    private LocationClient mLocationClient = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sealingposition);
        iniPermission();
        mMapView = findViewById(R.id.bmapView);
        mMap = mMapView.getMap();
        // 设置为普通地图
        mMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mMap.setMyLocationEnabled(true);
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

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_location);
        // 创建OverlayOptions的集合
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        // 构造大量坐标数据
        LatLng point1 = new LatLng(34.252405, 108.88856);
        LatLng point2 = new LatLng(34.852405, 108.438856);
        LatLng point3 = new LatLng(34.552405, 108.314977);
        // 创建OverlayOptions属性
        OverlayOptions option1 =  new MarkerOptions()
                .position(point1)
                .icon(bitmap);
        OverlayOptions option2 =  new MarkerOptions()
                .position(point2)
                .icon(bitmap);
        OverlayOptions option3 =  new MarkerOptions()
                .position(point3)
                .icon(bitmap);
        // 将OverlayOptions添加到list
        options.add(option1);
        options.add(option2);
        options.add(option3);
        // 在地图上批量添加
        mMap.addOverlays(options);

        mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            //marker被点击时回调的方法
            //若响应点击事件，返回true，否则返回false
            //默认返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                //用来构造InfoWindow的Button
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.mipmap.popup);
                button.setText("InfoWindow");

                // 构造InfoWindow
                // point 描述的位置点
                // -100 InfoWindow相对于point在y轴的偏移量
                InfoWindow mInfoWindow = new InfoWindow(button, point1, -100);

                // 使InfoWindow生效
                mMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
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
            Log. i("gongyu", "location.getRadius()  " + location.getRadius() + "   location.getDirection()  " + location.getDirection() +
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
    private void iniPermission(){
        List<String> permissionList=new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.
                permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.
                permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.
                permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions,1);
        }
    }
}
