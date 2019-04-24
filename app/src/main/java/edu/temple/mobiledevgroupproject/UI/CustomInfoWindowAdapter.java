package edu.temple.mobiledevgroupproject.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import edu.temple.mobiledevgroupproject.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context context;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.marker_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView titleView = mWindow.findViewById(R.id.info_window_title);
        TextView descView = mWindow.findViewById(R.id.info_window_desc);

        titleView.setText(marker.getTitle());
        descView.setText(marker.getSnippet());
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView titleView = mWindow.findViewById(R.id.info_window_title);
        TextView descView = mWindow.findViewById(R.id.info_window_desc);

        titleView.setText(marker.getTitle());
        descView.setText(marker.getSnippet());
        return null;
    }
}
