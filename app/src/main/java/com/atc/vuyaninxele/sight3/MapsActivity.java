package com.atc.vuyaninxele.sight3;

import android.Manifest;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference dbref;
    private ClusterManager<MyItem> mClusterManager;
    private Button showAll;
    private Button cluster;
    private boolean clicked;
    private EditText distance;
    private Double lat,lng;
    private Location lastLocation;
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1;
    private Integer select = 0;
    private ValueEventListener valueEventListener;
    private LiveData<Location> liveData;
    private Bundle bundle;
    private Fragment mContent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }
        dbref = FirebaseDatabase.getInstance().getReference("Site");
        liveData = new LocationLiveData(getApplicationContext());
        clicked = false;
        showAll = findViewById(R.id.shwbttn);
        cluster = findViewById(R.id.cluster);
        distance = findViewById(R.id.distance_input);
        lat = 28.0145;
        lng = -26.0256;
//        lastLocation.setLatitude(28.45);
//        lastLocation.setLongitude(23.56);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (valueEventListener != null)
            dbref.removeEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueEventListener != null)
            dbref.removeEventListener(valueEventListener);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("DOES IT", "ATANYYPOINTAND");
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "myFragmentName", mContent);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered
     * when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION_CODE);
        } else {
            mMap.setMyLocationEnabled(true);
            liveData.observe(this, new Observer<Location>() {
                @Override
                public void onChanged(@Nullable Location location) {
                    if(location == null)
                    {
                        lat = 28.0145;
                        lng = -26.0256;
                        Toast.makeText(getApplicationContext(),"Location Unavailable, Default location", Toast.LENGTH_LONG).show();
                    }
                    else {
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                    }
//                    if (location != null && lastLocation.getLatitude() != location.getLatitude()) {
//                        lastLocation = location;
//                        Log.d("Long vs Long",String.valueOf(location.getLongitude()) + " " + String.valueOf(lastLocation.getLongitude()));
//                        Log.d("Lat vs Lat",String.valueOf(location.getLatitude())  + " " + String.valueOf(lastLocation.getLatitude()));
//                        if (select == 0)
//                            WithinRange(mMap);
//                        if (select == 1)
//                            ShowAll(mMap);
//                        if (select == 2)
//                            Cluster(mMap);
//                    }
                        if (select == 0)
                            WithinRange(mMap);
                        if (select == 1)
                            ShowAll(mMap);
                        if (select == 2)
                            Cluster(mMap);

                }
            });

        }




        if (getIntent().getExtras() != null &&getIntent().getExtras().getInt("CheckClicked") == 1) {
            MvCamTo(mMap);
        }

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!clicked)
                    clicked = true;
                else
                    clicked = false;
                if (!clicked || !TextUtils.isEmpty(distance.getText().toString()))
                    WithinRange(mMap);
                else
                    ShowAll(mMap);
            }
        });
            WithinRange(mMap);
        cluster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cluster(mMap);
            }
        });
    }

    public void MvCamTo(GoogleMap googleMap){

        mMap = googleMap;
        String json = getIntent().getExtras().getString("sites");
        Sites sites1 = new Gson().fromJson(json, Sites.class);
        String Latitude = sites1.getLatitude();
        String Longitude = sites1.getLongitude();

        double Lat =  DMStoDD(removeLastChar(direc(Latitude)));
        double Lng = DMStoDD(removeLastChar(direc(Longitude)));
        LatLng mvcam = new LatLng(Lat, Lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mvcam, 15));
    }
    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 2));
    }

    public static String direc(String str){

        return (str.charAt(str.length() - 1) == 'S' || str.charAt(str.length() - 1) == 'W') ? "-" + str : str;
    }

    public static Double DMStoDD (String str){

        String [] arrOfStr;
        Float d;
        Float m;
        Float s;

        arrOfStr = str.split(",", 3);
        d = Float.parseFloat(arrOfStr[0]);
        m = Float.parseFloat(arrOfStr[1]);
        s = Float.parseFloat(arrOfStr[2]);

        return (Math.signum(d) * (Math.abs(d) + (m / 60.0) + (s / 3600.0)));
    }


    public void WithinRange(GoogleMap googleMap){
        final Double dist;
        select = 0;
        if (TextUtils.isEmpty(distance.getText().toString()))
            dist = 15000.00;
        else
            dist = Integer.valueOf(distance.getText().toString()) * 1000 * 0.621;
        mMap = googleMap;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.atclogomarker);
        Bitmap b=bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, 95, 95, false);

        final LatLng myLocation = new LatLng(lat, lng);
        mMap.clear();
        valueEventListener = dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String Location = snapshot.child("siteName").getValue().toString();
                    String Latitude = snapshot.child("latitude").getValue().toString();
                    String Longitude = snapshot.child("longitude").getValue().toString();

                    LatLng sites = new LatLng(DMStoDD(removeLastChar(direc(Latitude))), DMStoDD(removeLastChar(direc(Longitude))));
                    if(computeDistanceBetween(myLocation, sites) < dist )
                        mMap.addMarker(new MarkerOptions()
                                .position(sites)
                                .title(Location)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(MapsActivity.this, databaseError.getDetails(),Toast.LENGTH_SHORT).show();
                /**
                 * add proper error handling
                 */
            }
        });
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10));

    }
    public void ShowAll(GoogleMap googleMap) {

        mMap = googleMap;
        select = 1;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.atclogomarker);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, 95, 95, false);
        mMap.clear();
        valueEventListener = dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String Location = snapshot.child("siteName").getValue().toString();
                    String Latitude = snapshot.child("latitude").getValue().toString();
                    String Longitude = snapshot.child("longitude").getValue().toString();

                    LatLng sites = new LatLng(DMStoDD(removeLastChar(direc(Latitude))), DMStoDD(removeLastChar(direc(Longitude))));

                    mMap.addMarker(new MarkerOptions()
                            .position(sites)
                            .title(Location)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(MapsActivity.this, databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                /**
                 * add proper error handling
                 */
            }
        });
    }

    public void Cluster(GoogleMap googleMap){
        mMap = googleMap;
        mMap.clear();
        select = 2;
        mClusterManager = new ClusterManager<MyItem>(getApplicationContext(), mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.atclogomarker);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, 95, 95, false);
        valueEventListener = dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String Location = snapshot.child("siteName").getValue().toString();
                    String Latitude = snapshot.child("latitude").getValue().toString();
                    String Longitude = snapshot.child("longitude").getValue().toString();
                    String SiteNumber = snapshot.child("siteNumber").getValue().toString();

                    MyItem offsetItem = new MyItem(DMStoDD(removeLastChar(direc(Latitude))), DMStoDD(removeLastChar(direc(Longitude))), Location, SiteNumber);
                    mClusterManager.addItem(offsetItem);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(MapsActivity.this, databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                /**
                 * add proper error handling
                 */
            }
        });

    }
}