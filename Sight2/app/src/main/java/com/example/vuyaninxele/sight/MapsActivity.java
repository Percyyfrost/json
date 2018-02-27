package com.example.vuyaninxele.sight;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String url2 = "http://atc-esb-mobipoc.cloudhub.io/api/api/rest/v1/site/siteexists?Country=ZA/Results";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url2, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("Results");
                    JSONArray episodes = jsonObject.getJSONArray("Site");
                    for (int i = 0; i <episodes.length() ; i++) {

                        JSONObject episode = episodes.getJSONObject(i);
                        String Location = episode.getString("siteName");
                        Log.i("Location____Object", Location);

                        String Latitude = episode.getString("latitude");
                        Log.i("Latitude____Object", String.valueOf(Latitude));

                        String Longitude = episode.getString("longitude");
                        Log.i("Longitude___Object", String.valueOf(Longitude));
                        float lat = Float.parseFloat(removeLastChar(Latitude));
                        float lng = Float.parseFloat(removeLastChar(Longitude));
                       // mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(Location));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });



        Volley.newRequestQueue(this).add(jsonObjReq);
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 2));
    }
}
