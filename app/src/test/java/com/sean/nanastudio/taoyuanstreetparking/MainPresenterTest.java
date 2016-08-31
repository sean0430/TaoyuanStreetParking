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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/26上午9:46.
 */
public class MainPresenterTest {

    private MainModel model;
    private MainView view;
    private MainPresenter presenter;


    @Before
    public void setUp() throws Exception {

        model = Mockito.mock(MainModel.class);
        view = Mockito.mock(MainView.class);
        presenter = new MainPresenterImpl(view, model);

    }

    @Test
    public void testOnCreate() throws Exception {

        presenter.onCreate();

        Mockito.verify(view).setLayoutContentView();
        Mockito.verify(view).setActionBar();
        String apiKey = view.getApiKey();
        Mockito.verify(model).setApiKey(apiKey);
        Mockito.verify(view).getStreetParkingInfosFromAPI();

    }

    @Test
    public void testGetStreetParkingInfos() throws Exception {

        presenter.getStreetParkingInfos();

        Mockito.verify(model).getStreetParkingInfos();

    }

    @Test
    public void testSearch() throws Exception {

        List<StreetParkingInfo> expected = new ArrayList<>();

        List<StreetParkingInfo> actual = model.getSearchStreetParkingInfos("");

        Assert.assertEquals(expected, actual);

    }


    @Test
    public void testRefresh() throws Exception {

        presenter.refresh();

        Mockito.verify(view).setRvStreetParking(Mockito.anyListOf(StreetParkingInfo.class));

    }

    @Test
    public void testClearQueryStr() throws Exception {

        presenter.clearQueryStr();

        Mockito.verify(model).setQueryStr(Mockito.anyString());

    }

    @Test
    public void testOnStart() throws Exception {

        presenter.onStart();

        Mockito.verify(view).connectGoogleApiClient();

    }

    @Test
    public void testOnStop() throws Exception {

        presenter.onStop();

        Mockito.verify(view).disconnectGoogleApiClient();

    }

    @Test
    public void testGetLocationWithLocation() throws Exception {


        Location location = Mockito.mock(Location.class);
        presenter.getLocation(location);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Mockito.verify(model).getGeocodeRoadName(latitude, longitude);

    }

    @Test
    public void testGetLocationViewNoLocation() throws Exception {

        presenter.getLocation(null);

//        Mockito.verify(view).requestLocationPermission();
        Mockito.verify(view).initialGoogleApiClient();
        Mockito.verify(view).connectGoogleApiClient();

    }

    @Test
    public void testViewIsInSearch() throws Exception {

        presenter.search("test");
        Assert.assertTrue(presenter.viewIsInSearch());

    }


}
