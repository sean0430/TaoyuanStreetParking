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

import android.util.Log;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/27下午3:06.
 */
public class ParseTpNameToNameInfo {

    private static final String TAG = "ParseTpNameToNameInfo";

    private static final String WEEKDAYS_RATES = "平日小車費率:";
    private static final String WEEKDAYS_CHARGING_TIME = ",平日收費時段:";
    private static final String WEEKDAYS_PER_HALF_A_HOUR = "平日半小時計費,";
    private static final String WEEKDAYS_PER_A_HOUR = "平日計時計費,";
    private static final String WEEKDAYS_PROGRESSION = "平日累進計費,";
    private static final String WEEKDAYS_PER_TIME = "平日計次計費,";

    private static final String HOLIDAY_RATES = ",假日小車費率:";
    private static final String HOLIDAY_CHARGING_TIME = ",假日收費時段:";
    private static final String HOLIDAY_PER_HALF_A_HOUR = ",假日半小時計費";
    private static final String HOLIDAY_NO_CHARGE = ",假日無計費,";
    private static final String HOLIDAY_PER_HOUR = ",假日計時計費,";
    private static final String HOLIDAY_PROGRESSION = ",假日累進計費,";
    private static final String HOLIDAY_PER_TIME = ",假日計次計費,";

    public TpNameInfo parse(String tpNameStr) {

        TpNameInfo.WeekdaysChargeType weekdaysChargeType = TpNameInfo.WeekdaysChargeType.PER_HALF_A_HOUR;
        TpNameInfo.HolidayChargeType holidayChargeType = TpNameInfo.HolidayChargeType.PER_HALF_A_HOUR;

        if (tpNameStr.indexOf(WEEKDAYS_PER_HALF_A_HOUR) > 0) {
            weekdaysChargeType = TpNameInfo.WeekdaysChargeType.PER_HALF_A_HOUR;

        } else if (tpNameStr.indexOf(WEEKDAYS_PER_A_HOUR) > 0) {
            weekdaysChargeType = TpNameInfo.WeekdaysChargeType.PER_A_HOUR;

        } else if (tpNameStr.indexOf(WEEKDAYS_PROGRESSION) > 0) {
            weekdaysChargeType = TpNameInfo.WeekdaysChargeType.PROGRESSION;

        } else if (tpNameStr.indexOf(WEEKDAYS_PER_TIME) > 0) {
            weekdaysChargeType = TpNameInfo.WeekdaysChargeType.PER_TIME;

        } else {
            Log.e(TAG, "parse: weekdays charge type error," +
                    "\ntp name = " + tpNameStr, new WrongChargeTypeException(tpNameStr));
        }


        int weekdaysRatesBeginIndex = tpNameStr.indexOf(WEEKDAYS_RATES) + 7;
        int weekdaysRatesEndIndex = tpNameStr.indexOf(WEEKDAYS_CHARGING_TIME);
        String weekdaysRates = tpNameStr.substring(
                weekdaysRatesBeginIndex, weekdaysRatesEndIndex);

        int weekdaysChargingTimeBeginIndex = tpNameStr.indexOf(WEEKDAYS_CHARGING_TIME) + 8;
        int weekdaysChargingTimeEndIndex = -1;

        if (tpNameStr.indexOf(HOLIDAY_PER_HALF_A_HOUR) > 0) {
            weekdaysChargingTimeEndIndex = tpNameStr.indexOf(HOLIDAY_PER_HALF_A_HOUR);
            holidayChargeType = TpNameInfo.HolidayChargeType.PER_HALF_A_HOUR;

        } else if (tpNameStr.indexOf(HOLIDAY_NO_CHARGE) > 0) {
            weekdaysChargingTimeEndIndex = tpNameStr.indexOf(HOLIDAY_NO_CHARGE);
            holidayChargeType = TpNameInfo.HolidayChargeType.NO_CHARGING;

        } else if (tpNameStr.indexOf(HOLIDAY_PER_HOUR) > 0) {
            weekdaysChargingTimeEndIndex = tpNameStr.indexOf(HOLIDAY_PER_HOUR);
            holidayChargeType = TpNameInfo.HolidayChargeType.PER_A_HOUR;

        } else if (tpNameStr.indexOf(HOLIDAY_PROGRESSION) > 0) {
            weekdaysChargingTimeEndIndex = tpNameStr.indexOf(HOLIDAY_PROGRESSION);
            holidayChargeType = TpNameInfo.HolidayChargeType.PROGRESSION;

        } else if (tpNameStr.indexOf(HOLIDAY_PER_TIME) > 0) {
            weekdaysChargingTimeEndIndex = tpNameStr.indexOf(HOLIDAY_PER_TIME);
            holidayChargeType = TpNameInfo.HolidayChargeType.PER_TIME;

        } else {
            Log.e(TAG, "parse: holiday charge type error," +
                    "\ntp name = " + tpNameStr, new WrongChargeTypeException(tpNameStr));
        }


        String weekdaysChargingTime = tpNameStr.substring(
                weekdaysChargingTimeBeginIndex, weekdaysChargingTimeEndIndex);

        int holidayRatesBeginIndex = tpNameStr.indexOf(HOLIDAY_RATES) + 8;
        int holidayRatesEndIndex = -1;

        if (tpNameStr.indexOf(HOLIDAY_CHARGING_TIME) > 0) {
            holidayRatesEndIndex = tpNameStr.indexOf(HOLIDAY_CHARGING_TIME);

        } else if (tpNameStr.indexOf(HOLIDAY_NO_CHARGE) > 0) {
            holidayRatesEndIndex = tpNameStr.indexOf(HOLIDAY_NO_CHARGE);
        }
        String holidayRates = tpNameStr.substring(holidayRatesBeginIndex, holidayRatesEndIndex);

        int holidayChargingTimeBeginIndex = tpNameStr.indexOf(HOLIDAY_CHARGING_TIME) + 8;
        int holidayChargingTimeEndIndex = tpNameStr.length();
        String holidayChargingTime = tpNameStr.substring(
                holidayChargingTimeBeginIndex, holidayChargingTimeEndIndex
        );

        return new TpNameInfo(weekdaysRates, weekdaysChargingTime,
                holidayRates, holidayChargingTime,
                weekdaysChargeType, holidayChargeType);
    }
}
