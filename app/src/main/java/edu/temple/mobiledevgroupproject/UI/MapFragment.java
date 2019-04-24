/*MapFragment:
*(1) Receives a list of jobs from parent activity
*(2) Displays mapview with markers using list of jobs
*(3) Displays infowindow that gives job info and allows user to view job details in seperate activity
*(4) Sends data from job from selected markers to parent activity*/

package edu.temple.mobiledevgroupproject.UI;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import edu.temple.mobiledevgroupproject.Objects.Job;
import edu.temple.mobiledevgroupproject.R;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    //layout objects
    GoogleMap googleMap;
    MapView mapView;
    View mView;

    //other objects
    ArrayList<Job> jobsToDisplay;
    MapClickInterface mapClickListener;

    public MapFragment() {
        //required empty public constructor
    }

    public interface MapClickInterface {
        void jobSelected(Job selectedJob);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mapClickListener = (MapClickInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        Bundle args = getArguments();
        if (args != null) {
            jobsToDisplay = (ArrayList<Job>) args.getSerializable("jobs_to_display");
        }
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = mView.findViewById(R.id.map_view);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        configureMap(jobsToDisplay);
    }

    /**
     * Launches a custom info window giving the user the option to view
     * the job corresponding with the param. marker.
     * @param marker The selected marker
     * @return false
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Job selectedJob = null;
                for (int i = 0; i < jobsToDisplay.size(); i++) {
                    Job thisJob = jobsToDisplay.get(i);
                    if (marker.getTitle().equals(thisJob.getJobTitle())) {
                        selectedJob = thisJob;
                    }
                }
                if (selectedJob != null) {
                    mapClickListener.jobSelected(selectedJob);
                }
            }
        });
        return false;
    }



    /**
     * Sets markers on the fragment's mapview using the location fields of the
     * Jobs in the jobsToDisplay param.
     * Also sets a marker for the user's location.
     * @param jobsToDisplay The list of Job objects to display on the mapview.
     */
    public void configureMap(ArrayList<Job> jobsToDisplay) {
        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMarkerClickListener(this);

        //TODO implement fetching of currrent position in parent activity. Currently position is hardcoded.
        LatLng userPos = new LatLng(39.981991, -75.153053);
        CameraPosition camPos = CameraPosition.builder()
                .target(userPos)
                .zoom(15)
                .bearing(0)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos));
        //add user pos. as marker
        String iconName = getResources().getResourceName(R.drawable.icons8_user_24);

        googleMap.addMarker(new MarkerOptions()
                .position(userPos)
                .title("USER POSITION")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeIcons(iconName, 60, 60))));

        iconName = getResources().getResourceName(R.drawable.marker_icon);

        if (jobsToDisplay != null) {
            for (int i = 0; i < jobsToDisplay.size(); i++) {
                Job thisJob = jobsToDisplay.get(i);
                googleMap.addMarker(new MarkerOptions()
                        .position(thisJob.getLocation())
                        .title(thisJob.getJobTitle())
                        .snippet(thisJob.getJobDescription())
                        .icon(BitmapDescriptorFactory.fromBitmap(resizeIcons(iconName, 80, 80))))
                        .setAlpha(1);
            }
        }
    }

    private Bitmap resizeIcons(String iconName, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getContext().getPackageName()));
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
}
