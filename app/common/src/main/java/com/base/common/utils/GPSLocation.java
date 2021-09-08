package com.base.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

/**
 * 获取上一次定位结果
 */
public class GPSLocation implements LocationListener {
    private static final String TAG = "GPSLocation";
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 20; // 0 meters
    // The minimum time between updates in milliseconds  1000 * 60 * 28800;  8 hours 28800
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 10; // 8 hours

    private Context context;
    private Double longitude;
    private Double latitude;

    @SuppressLint("MissingPermission")
    public GPSLocation(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    /**
     * Return latitude and longitude
     */
    private Double[] getLocationLatLong() {
        Double[] latlong = new Double[2];
        latlong[0] = 0.0;
        latlong[1] = 0.0;

        try {
            // Get the location manager
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                // getting GPS status
                Boolean isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                // ——–Gps provider—
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this, Looper.getMainLooper());

                if (isGPSEnabled) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (location != null) {
                        latlong[0] = location.getLatitude();
                        latlong[1] = location.getLongitude();
                        locationManager.removeUpdates(this);
                        return latlong;
                    }
                }

                if (isNetworkEnabled) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (location != null) {
                        latlong[0] = location.getLatitude();
                        latlong[1] = location.getLongitude();
                        locationManager.removeUpdates(this);
                        return latlong;
                    }
                }

                // use passive provider
                Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (location != null) {
                    latlong[0] = location.getLatitude();
                    latlong[1] = location.getLongitude();
                    locationManager.removeUpdates(this);
                    return latlong;
                }
            } else {
                Log.e("GPSLocation", "Location manager is null");
            }
        } catch (Exception e) {
            Log.e("GPSLocation", e.getMessage());
            e.printStackTrace();
        }
        return latlong;
    }


    /**
     * Function which will return device location to calling method
     *
     * @return DeviceLocation structure
     */
    public Double[] getLocation() {
        Double[] latlong = getLocationLatLong();
        latitude = latlong[0];
        longitude = latlong[1];
        return latlong;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("GPSLocation", "Lat: " + location.getLatitude());
        Log.e("GPSLocation", "Long: " + location.getLongitude());

        // only latitude longitude to set up
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private GsmCellLocation getGsmCellLocation(Context context) {
        final TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony != null && telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
            @SuppressLint("MissingPermission") final GsmCellLocation location = (GsmCellLocation) telephony.getCellLocation();
            return location;
        }
        return null;
    }

    public int getLac(Context context) {
        GsmCellLocation gsmCellLocation = getGsmCellLocation(context);
        return gsmCellLocation != null ? gsmCellLocation.getLac() : 0;
    }

    public int getCid(Context context) {
        GsmCellLocation gsmCellLocation = getGsmCellLocation(context);
        return gsmCellLocation != null ? gsmCellLocation.getCid() : 0;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

}
