package com.example.asus_pc.mapdemo;


import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;

public class MainActivity extends Activity implements LocationSource {
    MapView mMapView = null;
    private AMap aMap;
    private LocationManager locationManager;
    private UiSettings mUiSettings;
    private Animation centerMarker;
    private AMapLocation aLocation;
    private OnLocationChangedListener mListener;

    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        init();
    }
    private  void init(){
        if(aMap==null){
            aMap=mMapView.getMap();
            CameraUpdate cu =CameraUpdateFactory.zoomTo(15);
            aMap.moveCamera(cu);
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.common_ic_googleplayservices));
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);

        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
        mUiSettings.setTiltGesturesEnabled(false);// 设置地图是否可以倾斜
        mUiSettings.setScaleControlsEnabled(true);// 设置地图默认的比例尺是否显示
        mUiSettings.setZoomControlsEnabled(false);
        initMapListener();
    }
    private void initMapListener() {
        aMap.setOnMapLoadedListener((AMap.OnMapLoadedListener) this);
        aMap.setOnCameraChangeListener((AMap.OnCameraChangeListener) this);
        aMap.setOnMarkerClickListener((AMap.OnMarkerClickListener) this);
        aMap.setOnInfoWindowClickListener((AMap.OnInfoWindowClickListener) this);
        aMap.setInfoWindowAdapter((AMap.InfoWindowAdapter) this);// 设置自定义InfoWindow样式
        aMap.setOnMapClickListener((AMap.OnMapClickListener) this);
        centerMarker.setAnimationListener((Animation.AnimationListener) this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }
    public void onLocationChanged(AMapLocation aLocation) {
        if (aLocation != null) {
            this.aLocation = aLocation;
            if (mListener != null)
                mListener.onLocationChanged(aLocation);// 显示系统小蓝点
            if (isFirst) {
                isFirst = false;
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(
                        aLocation.getLatitude(), aLocation.getLongitude())));
                CameraUpdateFactory.zoomTo(16);
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(new LatLng(aLocation.getLatitude(),
                        aLocation.getLongitude()));
                markerOption.title("上海市").snippet("上海：34.341568, 108.940174");
                markerOption.draggable(true);
                Marker marker = aMap.addMarker(markerOption);
                marker.setObject("11");//这里可以存储用户数据
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;


    }

    @Override
    public void deactivate() {

    }
}