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

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/22下午12:26.
 */
public class MainPresenterImpl implements MainPresenter {


    private final MainView view;
    private final MainModel model;

    public MainPresenterImpl(MainView view, MainModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        view.setLayoutContentView();
        view.setActionBar();
        String apiKey = view.getApiKey();
        model.setApiKey(apiKey);
        view.getStreetParkingInfosFromAPI();
        view.setFabLocationListener();
        view.initialGoogleApiClient();
    }

    @Override
    public List<StreetParkingInfo> getStreetParkingInfos() {

        List<StreetParkingInfo> streetParkingInfos = new ArrayList<>();

        try {
            streetParkingInfos = model.getStreetParkingInfos();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return streetParkingInfos;
    }


    @Override
    public boolean search(final String queryStr) {

        final boolean[] isHasResult = {false};
        Observable.fromCallable(() -> model.getSearchStreetParkingInfos(queryStr))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(view::showProgress)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActionSubscriber<>(
                        streetParkingInfos -> {
                            if (streetParkingInfos.size() > 0) {
                                view.setRvStreetParking(streetParkingInfos);
                                isHasResult[0] = true;

                            } else {
                                view.showNoResult(queryStr);
                            }
                        },
                        (Action1<Throwable>) throwable -> view.showNoResult(queryStr),
                        (Action0) view::hideProgressAndRefresh
                ));
        return isHasResult[0];


    }

    @Override
    public void refresh() {

        view.showProgress();

        String queryStr = model.getQueryStr();

        List<StreetParkingInfo> streetParkingInfos;

        if ("".equals(queryStr)) {
            view.getStreetParkingInfosFromAPI();

        } else {
            streetParkingInfos = model.getSearchStreetParkingInfos(queryStr);
            view.setRvStreetParking(streetParkingInfos);

        }

        view.hideProgressAndRefresh();

    }

    @Override
    public void clearQueryStr() {
        model.setQueryStr("");
    }

    @Override
    public void onStart() {
        view.connectGoogleApiClient();
    }

    @Override
    public void onStop() {
        view.disconnectGoogleApiClient();
    }

    @Override
    public String getLocation(Location location) {
        String resultStr = "";
        if (location == null) {
            view.initialGoogleApiClient();
            view.connectGoogleApiClient();


        } else {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            resultStr = model.getGeocodeRoadName(latitude, longitude);

        }

        return resultStr;
    }

    @Override
    public boolean viewIsInSearch() {
        return !"".equals(model.getQueryStr());
    }


}
