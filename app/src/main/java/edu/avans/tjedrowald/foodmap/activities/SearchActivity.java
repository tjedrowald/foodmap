package edu.avans.tjedrowald.foodmap.activities;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import edu.avans.tjedrowald.foodmap.R;
import edu.avans.tjedrowald.foodmap.adapters.YelpSearchAdapter;
import edu.avans.tjedrowald.foodmap.models.SearchResponse;
import retrofit2.Call;

/**
 * Created by tjedrowald on 1-3-18.
 */

public class SearchActivity extends BaseLocationActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private ProgressBar loadingIndicator;
    private YelpSearchAdapter yelpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        yelpAdapter = new YelpSearchAdapter();

        recyclerView = findViewById(R.id.recyclerview_search_result);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(yelpAdapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        loadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_search;
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
        showSearchResult();
        new YelpQueryTask().execute("Dordrecht"); // hmm, fetch user input
    }

    private void showSearchResult() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        showSnackbar(R.string.error_message, android.R.string.ok,
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadSearchResult();
                }
            });
    }

    public class YelpQueryTask extends AsyncTask<String, Void, SearchResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected SearchResponse doInBackground(String... params) {

            /* If there's no location, there's nothing to look up. */
            if (mLastLocation == null) {
                return null;
            }

            try {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put("latitude", Double.toString(mLastLocation.getLatitude()));
                queryParams.put("longitude", Double.toString(mLastLocation.getLongitude()));
                queryParams.put("term", "food");
                queryParams.put("lang", "nl"); // hmm, this should not be hard coded.
                Call<SearchResponse> call = yelpAPI.getBusinessSearch(queryParams);
                SearchResponse response = call.execute().body();
                return response;
            } catch (IOException e) {
                Log.d(TAG, "IOException "+e);
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SearchResponse response) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (response != null) {
                showSearchResult();
                yelpAdapter.setSearchResult(response);
            }
            else {
                showErrorMessage();
            }
        }
    }
}
