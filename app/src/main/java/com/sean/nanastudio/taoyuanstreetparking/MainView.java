package com.sean.nanastudio.taoyuanstreetparking;

import java.util.List;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/22下午12:23.
 */
public interface MainView {
    void setActionBar();

    void setFabLocationListener();

    void checkLocationPermission();

    void initialGoogleApiClient();

    void setLayoutContentView();

    void getStreetParkingInfosFromAPI();

    void setRvStreetParking(List<StreetParkingInfo> streetParkingInfos);

    void showProgress();

    void hideProgressAndRefresh();

    void connectGoogleApiClient();

    void disconnectGoogleApiClient();

    void showNoResult();
}
