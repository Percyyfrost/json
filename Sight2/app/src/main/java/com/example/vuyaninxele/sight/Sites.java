package com.example.vuyaninxele.sight;

/**
 * Created by vuyani.nxele on 2018/02/21.
 */

public class Sites {

    private String siteName;
    private String siteNumber;
    private String  Location;
    private String latitude;
    private String Longitude;
    private String Supervisor;
    private String SupervisorContact;

    public Sites(String siteName, String siteNumber, String location, String latitude, String lng, String supervisor, String contactDetail ) {
        this.siteName = siteName;
        this.siteNumber = siteNumber;
        Location = location;
        this.latitude = latitude;
        Longitude = lng;
        Supervisor = supervisor;
        SupervisorContact = contactDetail;
    }

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
        return Longitude;
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
        Longitude = longitude;
    }

    public void setSupervisor(String supervisor) {
        Supervisor = supervisor;
    }

    public void setContactDetail(String contactDetail) {
        SupervisorContact = contactDetail;
    }
}
