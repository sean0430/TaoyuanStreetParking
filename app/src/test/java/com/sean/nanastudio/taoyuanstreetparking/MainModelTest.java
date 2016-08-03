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

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/8/3上午9:39.
 */
public class MainModelTest {


    @Test
    public void testSetApiKey() throws Exception {


        MainModel model = Mockito.mock(MainModel.class);

        model.setApiKey("API_KEY");

        Mockito.verify(model).setApiKey("API_KEY");

    }

    @Test
    public void testGetStreetParkingInfos() throws Exception {

        MainModel model = new MainModelImpl();

        List<StreetParkingInfo> expected = new ArrayList<>();
        List<StreetParkingInfo> actual = model.getStreetParkingInfos();

        Assert.assertEquals(expected, actual);
        Assert.assertNotNull(model.getStreetParkingInfos());

    }

    @Test
    public void testGetSearchStreetParkingInfos() throws Exception {

        MainModel model = new MainModelImpl();

        List<StreetParkingInfo> expected = new ArrayList<>();
        List<StreetParkingInfo> actual = model.getSearchStreetParkingInfos("");

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void testGetQueryStr_queryStr_is_empty() throws Exception {

        MainModel model = new MainModelImpl();

        String excepted = "";
        String actual = model.getQueryStr();

        Assert.assertEquals(excepted, actual);

    }

    @Test
    public void testGetQueryStr_queryStr_is_not_empty() throws Exception {

        MainModel model = new MainModelImpl();

        String expected = "queryStr";

        model.setQueryStr("queryStr");
        String actual = model.getQueryStr();

        Assert.assertEquals(expected, actual);

    }


    @Test
    public void testSetQueryStr() throws Exception {

        MainModel model = Mockito.mock(MainModel.class);

        model.setQueryStr(Mockito.anyString());

        Mockito.verify(model).setQueryStr(Mockito.anyString());
    }

    @Test
    public void testGetGeocodeRoadName() throws Exception {

        MainModel model = Mockito.mock(MainModel.class);
        double latitude = Mockito.anyDouble();
        double longitude = Mockito.anyDouble();

        model.getGeocodeRoadName(latitude, longitude);

        Mockito.verify(model).getGeocodeRoadName(Mockito.anyDouble(), Mockito.anyDouble());


    }
}