package com.atc.vuyaninxele.sight3;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by vuyani.nxele on 2018/02/21.
 */

public class Sites implements ClusterItem {

    private String siteName;
    private String siteNumber;
    private String  Location;
    private String latitude;
    private String longitude;
    private String Supervisor;
    private String SupervisorContact;
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;

    public Sites(String siteName, String siteNumber, String location, String latitude, String longitude, String supervisor, String contactDetail ) {
        this.siteName = siteName;
        this.siteNumber = siteNumber;
        Location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        Supervisor = supervisor;
        SupervisorContact = contactDetail;
    }


    public Sites(Double lat, Double lng, String title) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = null;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() { return mTitle; }

    @Override
    public String getSnippet() { return mSnippet; }

    public String getSiteName() {
        return siteName;
    }

    public String getSiteNumber() {
        return siteNumber;
    }

    public String getLocation() {
        return Location;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getSupervisor() {
        return Supervisor;
    }

    public String getContactDetail() {
        return SupervisorContact;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setSiteNumber(String siteNumber) {
        this.siteNumber = siteNumber;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setSupervisor(String supervisor) {
        Supervisor = supervisor;
    }

    public void setContactDetail(String contactDetail) {
        SupervisorContact = contactDetail;
    }
}
