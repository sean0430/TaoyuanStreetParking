package com.sean.nanastudio.taoyuanstreetparking;

import android.location.Location;

import java.util.List;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/22下午12:26.
 */
public interface MainModel {

    List<StreetParkingInfo> getStreetParkingInfos();

    List<StreetParkingInfo> getSearchStreetParkingInfos(String queryStr);

    String getQueryStr();

    void setQueryStr(String queryStr);

    String getGeocodeRoadName(Location location);
}

