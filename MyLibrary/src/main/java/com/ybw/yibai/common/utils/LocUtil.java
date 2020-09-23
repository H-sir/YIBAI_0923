package com.ybw.yibai.common.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

public class LocUtil {
    public static Location getLastKnownLocation(Context context){
        //获取地理位置管理器
        LocationManager mLocationManager = (LocationManager)context.getSystemService(Service.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
