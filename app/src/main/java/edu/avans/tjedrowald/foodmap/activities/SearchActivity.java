package edu.avans.tjedrowald.foodmap.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.avans.tjedrowald.foodmap.R;
import edu.avans.tjedrowald.foodmap.adapters.YelpBusinessAdapter;
import edu.avans.tjedrowald.foodmap.interfaces.YelpBusinessAdapterOnClickHandler;
import edu.avans.tjedrowald.foodmap.interfaces.YelpQueryCallback;
import edu.avans.tjedrowald.foodmap.models.SearchResponse;
import edu.avans.tjedrowald.foodmap.sync.YelpQueryTask;
import retrofit2.Call;

/**
 * Created by tjedrowald on 1-3-18.
 */

public class SearchActivity extends BaseLocationActivity implements YelpBusinessAdapterOnClickHandler, YelpQueryCallback {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private TextView noResultsTv;
    private ImageView noResultsImg;
    private RecyclerView recyclerView;
    private ProgressBar loadingIndicator;
    private YelpBusinessAdapter yelpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        yelpAdapter = new YelpBusinessAdapter(this);

        noResultsTv = findViewById(R.id.no_results_text);
        noResultsImg = findViewById(R.id.no_results);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        recyclerView = findViewById(R.id.recyclerview_search_result);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(yelpAdapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    // This method is called when the last location is fetched.
    @Override
    public void onComplete(@NonNull Task<Location> task) {
        if (task.isSuccessful() && task.getResult() != null) {
            // we can search the API when we have a location (this is a mandatory field)
            mLastLocation = task.getResult();
            loadSearchResult();
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

    private void loadSearchResult() {
        new YelpQueryTask(yelpAPI, this).execute(mLastLocation);
    }

    private void showSearchResult() {
        recyclerView.setVisibility(View.VISIBLE);
        noResultsImg.setVisibility(View.INVISIBLE);
        noResultsTv.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        noResultsImg.setVisibility(View.VISIBLE);
        noResultsTv.setVisibility(View.VISIBLE);
        showSnackbar(R.string.error_message, android.R.string.ok,
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadSearchResult();
                }
            });
    }

    @Override
    public void onClick(String Id, String BusinessName, String BusinessImage) {
        Context context = this;
        Class destinationClass = SearchDetailActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra("businessId", Id);
        intent.putExtra("businessName", BusinessName);
        intent.putExtra("businessImage", BusinessImage);
        startActivity(intent);
    }


    @Override
    public void onPreExecute() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(SearchResponse searchResponse) {
        loadingIndicator.setVisibility(View.INVISIBLE);

        if (searchResponse != null) {
            showSearchResult();
            yelpAdapter.setSearchResult(searchResponse.getBusinesses());
        }
        else {
            showErrorMessage();
        }
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_search;
    }
}
