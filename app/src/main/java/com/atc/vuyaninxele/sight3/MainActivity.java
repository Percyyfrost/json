package com.atc.vuyaninxele.sight3;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;


public class MainActivity extends AppCompatActivity implements
        ClickListener{

    private List<Sites> sitesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SitesAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference dbref;
    private SearchView searchView;
    private ValueEventListener valueEventListener;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
//    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION = 102;
    private boolean permissionIsGranted = false;

    Location MyCurrentLoc;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbref.removeEventListener(valueEventListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        initUi();
        setSupportActionBar(toolbar);
        dbref = FirebaseDatabase.getInstance().getReference("Site");
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            LiveData<Location> liveData = new LocationLiveData(getApplicationContext());
            liveData.observe(this, new Observer<Location>() {
                @Override
                public void onChanged(@Nullable Location location) {
                    MyCurrentLoc = location;
                    if (MyCurrentLoc != null)
                        Collections.sort(sitesList, new DistanceArrange(MyCurrentLoc));
                    else
                        Toast.makeText(getApplicationContext(), "No Location, Unable to Geo Sort", Toast.LENGTH_LONG).show();
                    mAdapter.notifyDataSetChanged();
                    mAdapter.setClickListener(MainActivity.this);
                }
            });
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("floataction",2);
                startActivity(intent);
            }
        });
        load(); // fetching of data from firebase
        refreshSwipe();// fetching data from gam
    }

    private void refreshSwipe() {
        swipeRefreshLayout.setColorSchemeResources(R.color.Black, R.color.red, R.color.Black);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Sync.DataSync(getApplicationContext());
                    }
                }, 4000);
            }
        });
    }

    private void initUi() {
        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new SitesAdapter((ArrayList<Sites>) sitesList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
    }

    @Override
    public void itemClicked(List<Sites> sitesList, View view, int position) {

        Sites sites = sitesList.get(position); // getting the model
        Intent intent = new Intent(MainActivity.this, Popup.class);
        intent.putExtra("sites", new Gson().toJson(sites));
        startActivity(intent);

    }

    private void load() {

            valueEventListener = dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        String jsonString = new Gson().toJson(dataSnapshot.getValue());
                        List<Sites> siteslist = new Gson().fromJson(jsonString, new TypeToken<List<Sites>>() {
                        }.getType());
                        sitesList.addAll(siteslist);
                        if(MyCurrentLoc != null) // value check for DistanceArrange
                            Collections.sort(sitesList, new DistanceArrange(MyCurrentLoc)); // sorting using location
                    }

                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                List<Sites> filteredList = new ArrayList<>();
                for (Sites row : sitesList) {

                    //Search filter if statement
                    if (row.getSiteName().toLowerCase().contains(query.toLowerCase()) || row.getSiteNumber().contains(query)) {
                        filteredList.add(row);
                    }
                }
                mAdapter = new SitesAdapter((ArrayList<Sites>) filteredList, getApplicationContext());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mAdapter.setClickListener(MainActivity.this);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (searchView != null&& !searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        finish();
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    permissionIsGranted = true;
                } else {
                    //permission denied
                    permissionIsGranted = false;
                    Toast.makeText(getApplicationContext(), "This app requires location permission to be granted, Default location on", Toast.LENGTH_SHORT).show();
                    MyCurrentLoc.setLongitude(28.0145);
                    MyCurrentLoc.setLatitude(-26.0256);
                }
                break;

        }
    }

}
