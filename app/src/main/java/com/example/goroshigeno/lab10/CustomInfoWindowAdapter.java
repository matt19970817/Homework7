package com.example.goroshigeno.lab10;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by matt1 on 2018/1/5.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity context;
    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custominfowindow, null);
        TextView tvtitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvaddr = (TextView) view.findViewById(R.id.tv_addr);


        tvtitle.setText(marker.getTitle());
        tvaddr.setText(marker.getSnippet());




    return  view;

    }


}
