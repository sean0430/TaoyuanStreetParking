package com.sean.nanastudio.taoyuanstreetparking;

import android.location.Location;

import java.util.List;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/22下午12:24.
 */
public interface MainPresenter {

    void onCreate();

    List<StreetParkingInfo> getStreetParkingInfos();

    boolean search(String queryStr);

    void refresh();

    void clearQueryStr();

    void onStart();

    void onStop();

    String getLocation(Location location);

}
