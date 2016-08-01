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

import java.io.Serializable;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/22上午11:49.
 */
public class StreetParkingInfo implements Serializable {
    private String rd_name;  // 路段名稱
    private String rd_begin; // 路段起點
    private String rd_end;   // 路段迄點
    private String rd_count; // 路段格位數
    private String use_cnt;  // 已停格位數
    private String last_time;// 最近更新時間
    private String tp_name;  // 路段收費標準

    private TpNameInfo tpNameInfo;

    public StreetParkingInfo(String rd_name, String rd_begin, String rd_end,
                             String rd_count, String use_cnt, String last_time, String tp_name, TpNameInfo tpNameInfo) {
        this.rd_name = rd_name;
        this.rd_begin = rd_begin;
        this.rd_end = rd_end;
        this.rd_count = rd_count;
        this.use_cnt = use_cnt;
        this.last_time = last_time;
        this.tp_name = tp_name;
        this.tpNameInfo = tpNameInfo;
    }

    public String getRd_name() {
        return rd_name;
    }

    public String getRd_begin() {
        return rd_begin;
    }

    public String getRd_end() {
        return rd_end;
    }

    public String getRd_count() {
        return rd_count;
    }

    public String getUse_cnt() {
        return use_cnt;
    }

    public String getLast_time() {
        return last_time;
    }

    public String getTp_name() {
        return tp_name;
    }

    public TpNameInfo getTpNameInfo() {
        return tpNameInfo;
    }
}
