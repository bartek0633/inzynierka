package com.example.inzynierka;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class LocationTracker implements LocationListener {

    private Context context;

    LocationTracker(Context context){
        super();
        this.context = context;
    }

    Location getLocation(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("gps", "Application does not have permission to use GPS.");
            return null;
        }
        try {
            LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0, 0, this);
            boolean isGPSenabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(isGPSenabled){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        6000, 10, this);
                return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else {
                Log.e("gps", "GPS in not enabled.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

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
}