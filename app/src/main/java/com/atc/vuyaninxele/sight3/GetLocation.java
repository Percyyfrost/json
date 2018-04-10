package com.atc.vuyaninxele.sight3;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by vuyani.nxele on 2018/03/08.
 */

public class GetLocation  {
//
//    Intent intentThatCalled;
//    public double latitude;
//    public double longitude;
//    public LocationManager locationManager;
//    public Criteria criteria;
//    public String bestProvider;
//
//    String voice2text; //added
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        intentThatCalled = getIntent();
//        voice2text = intentThatCalled.getStringExtra("v2txt");
//        getLocation();
//    }
//
//    public static boolean isLocationEnabled(Context context)
//    {
//        //...............
//        return true;
//    }
//
//    protected void getLocation() {
//        if (isLocationEnabled(MainActivity.this)) {
//            locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
//            criteria = new Criteria();
//            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
//
//            //You can still do this if you like, you might get lucky:
//            Location location = locationManager.getLastKnownLocation(bestProvider);
//            if (location != null) {
//                Log.e("TAG", "GPS is on");
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//                Toast.makeText(MainActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
//                searchNearestPlace(voice2text);
//            }
//            else{
//                //This is what you need:
//                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
//            }
//        }
//        else
//        {
//            //prompt user to enable location....
//            //.................
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        locationManager.removeUpdates(this);
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        //Hey, a non null location! Sweet!
//
//        //remove location callback:
//        locationManager.removeUpdates(this);
//
//        //open the map:
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//        Toast.makeText(MainActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
//        searchNearestPlace(voice2text);
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
//
//    public void searchNearestPlace(String v2txt) {
//        //.....
//    }
}