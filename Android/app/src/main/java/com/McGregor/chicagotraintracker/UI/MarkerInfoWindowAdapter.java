package com.McGregor.chicagotraintracker.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.McGregor.chicagotraintracker.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View window;
    private final Context context;

    @SuppressLint("InflateParams")
    public MarkerInfoWindowAdapter(Context context) {
        this.context = context;
        window = LayoutInflater.from(context).inflate(R.layout.marker_info_window, null);
    }


    private void renderWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.titleTextView);
        tvTitle.setTextColor(context.getResources().getColor(R.color.black, null));
        TextView snippetBody = view.findViewById(R.id.snippetTextView);
        tvTitle.setText(title);
        String snippet = marker.getSnippet();
        snippetBody.setText(snippet);

    }

    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindowText(marker, window);
        return window;
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {
        renderWindowText(marker, window);
        return window;
    }
}