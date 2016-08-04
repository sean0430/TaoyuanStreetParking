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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/22下午12:27.
 */
public class MainModelImpl implements MainModel {

    private static final String RESULT = "result";
    private static final String RECORDS = "records";
    private static final String RESULTS = "results";
    private static final String ADDRESS_COMPONENTS = "address_components";
    private static final String TYPES = "types";
    private static final String ROUTE = "[\"route\"]";
    private static final String SHORT_NAME = "short_name";
    private static final String ROAD = "路";
    private static final String STREET = "街";
    private static final String STREET_OR_ROAD_NAME_NOT_FOUND = "Street or road name not found";
    private String API_KEY;


    private static List<StreetParkingInfo> streetParkingInfos = new ArrayList<>();
    private String queryStr = "";
    private OkHttpClient okHttpClient = new OkHttpClient();
    private static final ParseTpNameToNameInfo parseTpNameToNAmeInfo = new ParseTpNameToNameInfo();

    private final static String DATA_URL = "http://data.tycg.gov.tw/api/v1/rest/datastore/" +
            "27d2edc9-890e-4a42-bcae-6ba78dd3c331?" +
            "limit=500"; // require amount

    private static final String GEOCODE_ADDRESS_URL =
            "https://maps.googleapis.com/maps/api/geocode/json?" +
                    "result_type=street_address&" +
                    "language=zh_TW&" +
                    "location_type=ROOFTOP&" +
                    "latlng=";

    @Override
    public void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }


    @Override
    public List<StreetParkingInfo> getStreetParkingInfos() {

        if (streetParkingInfos.size() <= 0) {
            return getDataFromAPI();

        } else {
            return streetParkingInfos;

        }

    }

    private List<StreetParkingInfo> getDataFromAPI() {
        final List<StreetParkingInfo> streetParkingInfos = new ArrayList<>();

        Request request = new Request.Builder()
                .url(DATA_URL)
                .get()
                .build();

        String responseStr = "";

        try {
            Response response = okHttpClient.newCall(request).execute();
            responseStr = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }


        final String finalResponseStr = responseStr;
        Observable.just(responseStr)
                .flatMap((Func1<String, Observable<?>>) s -> {
                    List<JSONObject> jsonObjects = new ArrayList<>();
                    try {
                        JSONObject responseJSON = new JSONObject(finalResponseStr);
                        String resultStr = responseJSON.getString(RESULT);
                        JSONObject resultJSON = new JSONObject(resultStr);
                        String recordsStr = resultJSON.getString(RECORDS);
                        JSONArray resultArray = new JSONArray(recordsStr);
                        jsonObjects = new ArrayList<>();
                        for (int i = 0; i < resultArray.length(); i++) {
                            jsonObjects.add(resultArray.getJSONObject(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return Observable.from(jsonObjects);
                })
                .subscribe(jsonObject -> {
                    StreetParkingInfo streetParkingInfo =
                            formatJSONToStreetParkingInfo((JSONObject) jsonObject);
                    streetParkingInfos.add(streetParkingInfo);
                });


        MainModelImpl.streetParkingInfos = streetParkingInfos;
        return streetParkingInfos;
    }

    private StreetParkingInfo formatJSONToStreetParkingInfo(JSONObject jsonObject) {
        try {
            String rd_name = jsonObject.getString("rd_name");
            rd_name = rd_name.replace("00", "");
            String rd_begin = jsonObject.getString("rd_begin");
            String rd_end = jsonObject.getString("rd_end");
            String rd_count = jsonObject.getString("rd_count");
            String use_cnt = jsonObject.getString("use_cnt");
            String last_time = jsonObject.getString("last_time");
            String tp_name = jsonObject.getString("tp_name");

            TpNameInfo tpNameInfo = parseTpNameToNAmeInfo.parse(tp_name);

            return new StreetParkingInfo(rd_name, rd_begin, rd_end, rd_count,
                    use_cnt, last_time, tp_name, tpNameInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new StreetParkingInfo("", "", "", "", "", "", "", null);
    }


    @Override
    public List<StreetParkingInfo> getSearchStreetParkingInfos(final String queryStr) {
        this.queryStr = queryStr;

        final List<StreetParkingInfo> resultInfos = new ArrayList<>();
        Observable.from(streetParkingInfos)
                .filter(streetParkingInfo -> streetParkingInfo.getRd_name().contains(queryStr))
                .subscribe(resultInfos::add);

        return resultInfos;
    }

    @Override
    public String getQueryStr() {
        return queryStr.trim();
    }

    @Override
    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    @Override
    public String getGeocodeRoadName(double latitude, double longitude) {

        String requestStr = String.format("%s%s,%s&key=%s",
                GEOCODE_ADDRESS_URL,
                latitude,
                longitude,
                API_KEY);

        Request request = new Request.Builder()
                .url(requestStr)
                .get()
                .build();


        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseStr = response.body().string();
            return parseGeocodeResponseToRoadOrStreetName(responseStr);

        } catch (IOException | JSONException e) {
            e.printStackTrace();

        }
        return STREET_OR_ROAD_NAME_NOT_FOUND;

    }

    private String parseGeocodeResponseToRoadOrStreetName(String responseStr) throws JSONException {

        JSONObject responseJSON = new JSONObject(responseStr);
        JSONArray resultJSONArray = responseJSON.getJSONArray(RESULTS);
        JSONArray addressComponentsArray =
                resultJSONArray.getJSONObject(0).getJSONArray(ADDRESS_COMPONENTS);

        if (addressComponentsArray.length() > 0) {
            for (int i = 0; i < addressComponentsArray.length(); i++) {
                JSONObject addressComponentsJSON = addressComponentsArray.getJSONObject(i);
                String routeStr = addressComponentsJSON.getString(TYPES);
                if (ROUTE.equals(routeStr.trim())) {
                    String shortNameStr = addressComponentsJSON.getString(SHORT_NAME);
                    return getSubString(shortNameStr);

                }
            }
        }

        return STREET_OR_ROAD_NAME_NOT_FOUND;
    }

    private String getSubString(String targetStr) {
        if (targetStr.indexOf(ROAD) > 0) {
            return targetStr.substring(0, targetStr.indexOf(ROAD));

        } else if (targetStr.indexOf(STREET) > 0) {
            return targetStr.substring(0, targetStr.indexOf(STREET));

        } else {
            return STREET_OR_ROAD_NAME_NOT_FOUND;
        }

    }

}
