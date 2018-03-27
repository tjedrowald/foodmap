package edu.avans.tjedrowald.foodmap.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import edu.avans.tjedrowald.foodmap.R;
import edu.avans.tjedrowald.foodmap.interfaces.YelpSearchCallback;
import edu.avans.tjedrowald.foodmap.models.Business;
import edu.avans.tjedrowald.foodmap.models.SearchResponse;
import edu.avans.tjedrowald.foodmap.sync.YelpSearchTask;

public class MapsActivity extends BaseLocationActivity implements OnMapReadyCallback, YelpSearchCallback, GoogleMap.OnInfoWindowClickListener {

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
        mMap.setOnInfoWindowClickListener(this);
    }


    // callback when the last location is fetched.
    @Override
    public void onComplete(@NonNull Task<Location> task) {
        if (task.isSuccessful() && task.getResult() != null) {
            // we can search the API when we have a location (this is a mandatory field)
            mLastLocation = task.getResult();

            CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude()))      // Sets the center of the map to the current location
                .zoom(5)                                    // Sets the zoom to current country
                .build();                                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // get nearby restaurants
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
        //nothing right now
    }

    // callback after executing YelpSearchTask
    @Override
    public void onPostExecuteYelpQuery(SearchResponse searchResponse) {
        for (Business business : searchResponse.getBusinesses()) {
			LatLng location = new LatLng(
                business.getCoordinates().getLatitude(),
                business.getCoordinates().getLongitude()
            );

            // add marker for all fetched restaurants
            mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(business.getName())
                .snippet(business.getCategoriesString())
            ).setTag(business);
        }

        // construct a CameraPosition focusing on the current location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude()))      // Sets the center of the map to the current location
                .zoom(13)                                   // Sets the zoom to the current city
                .build();                                   // Creates a CameraPosition from the builder

        // animate the camera to that position.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // set map preferences
        mMap.setMinZoomPreference(12.0f);
        mMap.setMaxZoomPreference(18.0f);
    }

    // callback for info window events
    @Override
    public void onInfoWindowClick(Marker marker) {
        Business business = (Business) marker.getTag();
        Intent intent = new Intent(this, SearchDetailActivity.class);
        intent.putExtra("businessId", business.getId());
        intent.putExtra("businessName", business.getName());
        intent.putExtra("businessImage", business.getImageUrl());
        startActivity(intent);
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
