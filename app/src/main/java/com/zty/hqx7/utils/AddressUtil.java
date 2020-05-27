package com.zty.hqx7.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressUtil {

    public static String getLocations(Context context){
        String strLocation = "0,0";
        //获取系统的服务，
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!checkPermission(context,permission.ACCESS_COARSE_LOCATION)){
            Toast.makeText(context,"定位权限关闭，无法获取地理位置",Toast.LENGTH_SHORT).show();
            return strLocation;
        }
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(context,"GPS未开启，无法获取地理位置",Toast.LENGTH_SHORT).show();
            return strLocation;
        }
        //创建一个criteria对象
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        //设置不需要获取海拔方向数据
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        //设置允许产生资费
        criteria.setCostAllowed(true);
        //要求低精度 低耗电
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        //location 如果为空那么换一下provider对象(别忘记注册GPS权限)
        String provider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER).getName();
        Log.i("Tobin", "Location Provider is "+ provider);
        Location location = locationManager.getLastKnownLocation(provider);
        if(location != null){
            strLocation = convertAddress(context, location.getLatitude(), location.getLongitude());
        } else {
            Toast.makeText(context,"无法获取地理位置",Toast.LENGTH_SHORT).show();
            System.out.println("location为null，无法获取地理位置");
        }

        return strLocation;
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */
    private final static LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {}
        public void onProviderDisabled(String provider){
            Log.i("Tobin", "Provider现在不可用..");
        }
        public void onProviderEnabled(String provider){
            Log.i("Tobin", "Provider现在可用..");
        }
        public void onStatusChanged(String provider, int status, Bundle extras){ }
    };


    /**
     * @param latitude 经度
     * @param longitude 纬度
     * @return 详细位置信息 GeoCoder是基于后台backend的服务，因此这个方法不是对每台设备都适用。
     */
    private static String convertAddress(Context context, double latitude, double longitude) {
        Geocoder mGeocoder = new Geocoder(context, Locale.getDefault());
        StringBuilder mStringBuilder = new StringBuilder();

        try {
            List<Address> mAddresses = mGeocoder.getFromLocation(latitude, longitude, 1);
            if (!mAddresses.isEmpty()) {
                Address address = mAddresses.get(0);
                //.append(address.getCountryName()).append(", ")
                mStringBuilder.append(address.getAdminArea()).append(", ").append(address.getLocality());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mStringBuilder.toString();
    }

    private static boolean checkPermission(Context context, permission permName) {
        int perm = context.checkCallingOrSelfPermission("android.permission."+permName.toString());
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private enum permission{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, MREAD_PHONE_STATE}
}
