package edu.avans.tjedrowald.foodmap.sync;

import android.os.AsyncTask;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.avans.tjedrowald.foodmap.interfaces.YelpQueryCallback;
import edu.avans.tjedrowald.foodmap.models.SearchResponse;
import edu.avans.tjedrowald.foodmap.sync.connection.YelpFusionApi;
import retrofit2.Call;

/**
 * Created by tjedrowald on 26-3-18.
 */

public class YelpQueryTask extends AsyncTask<Location, Void, SearchResponse> {

    private static final String TAG = YelpQueryTask.class.getSimpleName();

    private YelpFusionApi yelpAPI;
    private YelpQueryCallback callback;

    public YelpQueryTask(YelpFusionApi yelpAPI, YelpQueryCallback callback) {
        this.yelpAPI = yelpAPI;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onPreExecute();
    }

    @Override
    protected SearchResponse doInBackground(Location... params) {

        /* If there's no location, there's nothing to look up. */
        if (params[0] == null) {
            return null;
        }
        Location lastLocation = params[0];

        try {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("latitude", Double.toString(lastLocation.getLatitude()));
            queryParams.put("longitude", Double.toString(lastLocation.getLongitude()));
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
        callback.onPostExecute(response);
    }
}
