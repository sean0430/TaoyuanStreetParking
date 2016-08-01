package com.sean.nanastudio.taoyuanstreetparking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/22下午3:37.
 */
public class StringParkingAdapter extends RecyclerView.Adapter {
    private Context context;
    private final List<StreetParkingInfo> streetParkingInfos;

    public StringParkingAdapter(Context context, List<StreetParkingInfo> streetParkingInfos) {
        this.context = context;
        this.streetParkingInfos = streetParkingInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StreetParkingViewHolder(context, LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_street_paring, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StreetParkingInfo streetParkingInfo = streetParkingInfos.get(position);
        ((StreetParkingViewHolder) holder).bindData(streetParkingInfo);
    }

    @Override
    public int getItemCount() {
        return streetParkingInfos.size();
    }
}
