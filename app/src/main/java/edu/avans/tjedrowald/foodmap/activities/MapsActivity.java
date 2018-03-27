package edu.avans.tjedrowald.foodmap.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import edu.avans.tjedrowald.foodmap.R;
import edu.avans.tjedrowald.foodmap.interfaces.YelpSearchCallback;
import edu.avans.tjedrowald.foodmap.models.SearchResponse;
import edu.avans.tjedrowald.foodmap.sync.YelpSearchTask;

public class MapsActivity extends BaseLocationActivity implements OnMapReadyCallback, YelpSearchCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    // callback when map is loaded
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    // callback when the last location is fetched.
    @Override
    public void onComplete(@NonNull Task<Location> task) {
        if (task.isSuccessful() && task.getResult() != null) {
            // we can search the API when we have a location (this is a mandatory field)
            mLastLocation = task.getResult();
            new YelpSearchTask(yelpAPI, this).execute(mLastLocation);
        } else {
            Log.w(TAG, "getLastLocation:exception", task.getException());
            showSnackbar(R.string.no_location_detected, android.R.string.ok,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // try again
                        getLastLocation();
                    }
                });
        }
    }

    // callback before executing YelpSearchTask
    @Override
    public void onPreExecuteYelpQuery() {

    }

    // callback after executing YelpSearchTask
    @Override
    public void onPostExecuteYelpQuery(SearchResponse searchResponse) {

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    int getContentViewId() {
        return R.layout.activity_map;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_map;
    }
}
