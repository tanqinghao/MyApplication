package com.example.trans.myapplication.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.trans.myapplication.WeatherActivity;


public class GPSUtils {

    private static final String TAG = "GPSUtils";
    private static GPSUtils instance;
    private Context mContext;
    private LocationManager locationManager;
    private final int REFRESH_TIME = 1000;//间隔刷新时间
    private final int MIN_DISTANCE = 1;//位置刷新距离，单位m
    private final int ACCESS_FINE_LOCATION_COMMANDS_REQUEST_CODE = 0x99;


    private GPSUtils(Context context) {
        this.mContext = context;
    }

    public static GPSUtils getInstance(Context context) {
        if (instance == null) {
            instance = new GPSUtils(context);
        }
        return instance;
    }


    public boolean checkPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 没有权限。
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 用户拒绝过这个权限，应该提示用户。
                Log.d(TAG, "checkPermission: 拒绝过这个权限，提示，或者提示用户重新开启 ");
            } else {
                // 申请授权。
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_COMMANDS_REQUEST_CODE);
            }
        } else {
            Log.d(TAG, "checkPermission: ");
            return true;
        }
        return false;
    }


    /**
     * 获取经纬度
     *
     * @return 经纬度字符串 ，分割
     */
    @SuppressLint("MissingPermission")
    public void getLngAndLat(OnLocationResultListener onLocationResultListener) {
        String gpslocationProvider = null;
        String netlocationProvider = null;
        mOnLocationListener = onLocationResultListener;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //GPS
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpslocationProvider = LocationManager.GPS_PROVIDER;
        }   //Network
        else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            netlocationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            //跳转启动权限
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            Log.d(TAG, "getLngAndLat: ");
            return;
        }

        //获取Location 优先gps
        @SuppressLint("MissingPermission")
        Location location = locationManager.getLastKnownLocation(gpslocationProvider);
        if (location != null) {
            if (mOnLocationListener != null) {
                mOnLocationListener.onLocationResult(location);
            }
            Log.d(TAG, "getLngAndLat: netlocation：" + location);
            locationManager.requestLocationUpdates(gpslocationProvider, REFRESH_TIME, MIN_DISTANCE, locationListener);
        } else {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                netlocationProvider = LocationManager.NETWORK_PROVIDER;
            }
            Log.d(TAG, "getLngAndLat: netlocationProvider:" + netlocationProvider);
            if (netlocationProvider != null) {
                location = locationManager.getLastKnownLocation(netlocationProvider);
                Log.d(TAG, "getLngAndLat: netlocation：" + location + "----" + netlocationProvider);
                if (location != null) {
                    if (mOnLocationListener != null) {
                        mOnLocationListener.onLocationResult(location);
                    }
                }
                locationManager.requestLocationUpdates(netlocationProvider, REFRESH_TIME, MIN_DISTANCE, locationListener);
            }else{
                Log.d(TAG, "getLngAndLat: netWork is null ");
            }

        }
    }


    public LocationListener locationListener = new LocationListener() {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: ");
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: ");
        }

        // Provider被disable时触，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: ");
        }

        //当坐标改变时触发
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: ");
            if (mOnLocationListener != null) {
                mOnLocationListener.OnLocationChange(location);
            }
        }
    };

    //移出监听事件
    public void removeListener() {
        locationManager.removeUpdates(locationListener);
    }


    private OnLocationResultListener mOnLocationListener;

    //gps监听接口
    public interface OnLocationResultListener {

        void onLocationResult(Location location);

        void OnLocationChange(Location location);
    }
}
