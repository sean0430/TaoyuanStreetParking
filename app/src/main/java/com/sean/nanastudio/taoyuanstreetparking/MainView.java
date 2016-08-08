/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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

    void showNoResult(String queryStr);

    String getApiKey();
}
