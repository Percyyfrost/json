package com.atc.vuyaninxele.sight3;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

/**
 * Created by vuyani.nxele on 2018/03/15.
 */

public class DistanceArrange implements Comparator<Sites>
{

    LatLng myLoc;

    public DistanceArrange(Location current)
    {
        myLoc = new LatLng(current.getLatitude() * -1, current.getLongitude());
    }
    @Override
    public int compare(final Sites sites, final Sites sites1)
    {
        double lat = MapsActivity.DMStoDD(MapsActivity.removeLastChar(sites.getLatitude()));
        double lon = MapsActivity.DMStoDD(MapsActivity.removeLastChar(sites.getLongitude()));
        double lat1 = MapsActivity.DMStoDD(MapsActivity.removeLastChar(sites1.getLatitude()));
        double lon1 =  MapsActivity.DMStoDD(MapsActivity.removeLastChar(sites1.getLongitude()));

        double distance = computeDistanceBetween(myLoc, new LatLng(lat, lon));
        double distance1 = computeDistanceBetween(myLoc, new LatLng(lat1, lon1));

        return (int)(distance  - distance1 );
    }
}
