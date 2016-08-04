# TaoyuanStreetParking

一個簡單的app

用於顯示桃園市路邊停車格相關資訊

[PLAY商店聯結](https://play.google.com/store/apps/details?id=com.sean.nanastudio.taoyuanstreetparking)


</br>
</br>


##第一版只有基本功能

先由[桃園市政府資料開放平台](http://data.tycg.gov.tw/TYCG_OPD/)
中的[路邊停車資訊 API](http://data.tycg.gov.tw/TYCG_OPD/opendata/datalist/datasetMeta/outboundDesc?id=6ba44925-43ed-4bc7-8243-c22ab10ca4ff&rid=27d2edc9-890e-4a42-bcae-6ba78dd3c331)取得資料

經過整理之後呈現於首頁RecyclerView上

</br>

另有加入搜尋功能

使用者可以點擊上方放大鏡圖示進入搜尋

搜尋結果會立即呈現在畫面上

</br>
</br>


##第二版加入定位功能

加入新的元件FloationActionButton

當使用者點擊時

會先請求定位權限

權限確認沒有問題後開始定位

定位成功後使用相關資料經由[Google Maps Geoceoding API](https://developers.google.com/maps/documentation/geocoding/intro?hl=zh-tw)取得地址

再拿該地址使用原本的搜尋功能

</br>
</br>


##第三版修改程式碼

使用 RxJava 和 Retrolambda

使程式碼更為簡潔乾靜

</br>
</br>
##License


   Copyright (C) 2016 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 

