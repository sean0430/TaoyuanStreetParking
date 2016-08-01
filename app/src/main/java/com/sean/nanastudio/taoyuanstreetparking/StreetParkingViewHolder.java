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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/22下午3:39.
 */
public class StreetParkingViewHolder extends RecyclerView.ViewHolder {
    private final Context context;

    public StreetParkingViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
    }

    public void bindData(StreetParkingInfo streetParkingInfo) {

        getTvRdName().setText(streetParkingInfo.getRd_name());
        getTvRdBegin().setText(String.format("%s: %s",
                context.getResources().getString(R.string.rd_begin), streetParkingInfo.getRd_begin()));
        getTvRdEnd().setText(String.format("%s: %s",
                context.getResources().getString(R.string.rd_end), streetParkingInfo.getRd_end()));

        TpNameInfo tpNameInfo = streetParkingInfo.getTpNameInfo();
        getWeekdaysChargingType().setText(tpNameInfo.getWeekdaysChargeType().getDescription());
        getTvWeekDaysRates().setText(String.format(" %s %s ",
                tpNameInfo.getWeekdaysRates(), context.getResources().getString(R.string.dollar)));
        getTvWeekDaysChargingTime().setText(tpNameInfo.getWeekdaysChargingTime());
        getTvHolidayChargingType().setText(tpNameInfo.getHolidayChargeType().getDescription());
        getTvHolidayChargingRates().setText(String.format(" %s %s ",
                tpNameInfo.getHolidayRates(), context.getResources().getString(R.string.dollar)));
        getTvHolidayChargingTime().setText(tpNameInfo.getHolidayChargingTime());

        getLastTime().setText(String.format("%s%s",
                context.getResources().getString(R.string.last_time), streetParkingInfo.getLast_time()));

    }

    private TextView getTvRdName() {
        return (TextView) itemView.findViewById(R.id.tvRdName);
    }

    private TextView getTvRdBegin() {
        return (TextView) itemView.findViewById(R.id.tvRdBegin);
    }

    private TextView getTvRdEnd() {
        return (TextView) itemView.findViewById(R.id.tvRdEnd);
    }

    private TextView getWeekdaysChargingType() {
        return (TextView) itemView.findViewById(R.id.tvWeekdaysChargingType);
    }

    private TextView getTvWeekDaysRates() {
        return (TextView) itemView.findViewById(R.id.tvWeekdaysRates);
    }

    private TextView getTvWeekDaysChargingTime() {
        return (TextView) itemView.findViewById(R.id.tvWeekdaysChargingTime);
    }

    private TextView getTvHolidayChargingType() {
        return (TextView) itemView.findViewById(R.id.tvHolidayChargingType);
    }

    private TextView getTvHolidayChargingRates() {
        return (TextView) itemView.findViewById(R.id.tvHolidayRates);
    }

    private TextView getTvHolidayChargingTime() {
        return (TextView) itemView.findViewById(R.id.tvHolidayChargingTime);
    }

    private TextView getLastTime() {
        return (TextView) itemView.findViewById(R.id.tvLastTime);
    }
}
