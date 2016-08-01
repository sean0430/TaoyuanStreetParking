package com.sean.nanastudio.taoyuanstreetparking;

import java.io.Serializable;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/27上午9:33.
 */
public class TpNameInfo implements Serializable {

    private String weekdaysRates;                   // 平日費率
    private String weekdaysChargingTime;            // 平日收費時間
    private WeekdaysChargeType weekdaysChargeType;  // 平日收費類型
    private String holidayRates;                    // 假日費率
    private String holidayChargingTime;             // 假日收費時間
    private HolidayChargeType holidayChargeType;    // 假日收費類型

    enum WeekdaysChargeType {
        PER_HALF_A_HOUR("平日每半小時"),
        PER_A_HOUR("平日每小時"),
        PROGRESSION("平日每小時累進"),
        PER_TIME("平日每次");

        private String description;

        WeekdaysChargeType(String description) {

            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    enum HolidayChargeType {
        NO_CHARGING("假日不計費"),
        PER_HALF_A_HOUR("假日每半小時"),
        PER_A_HOUR("假日每小時"),
        PROGRESSION("假日每小時累進"),
        PER_TIME("假日每次");

        private String description;

        HolidayChargeType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public TpNameInfo(String weekdaysRates, String weekdaysChargingTime,
                      String holidayRates, String holidayChargingTime,
                      WeekdaysChargeType weekdaysChargeType, HolidayChargeType holidayChargeType) {
        this.weekdaysRates = weekdaysRates;
        this.weekdaysChargingTime = weekdaysChargingTime;
        this.holidayRates = holidayRates;
        this.holidayChargingTime = holidayChargingTime;
        this.weekdaysChargeType = weekdaysChargeType;
        this.holidayChargeType = holidayChargeType;
    }

    public String getWeekdaysRates() {
        return weekdaysRates;
    }


    public String getWeekdaysChargingTime() {
        return weekdaysChargingTime;
    }


    public String getHolidayRates() {
        return holidayRates;
    }


    public String getHolidayChargingTime() {
        return holidayChargingTime;
    }

    public WeekdaysChargeType getWeekdaysChargeType() {
        return weekdaysChargeType;
    }

    public HolidayChargeType getHolidayChargeType() {
        return holidayChargeType;
    }
}
