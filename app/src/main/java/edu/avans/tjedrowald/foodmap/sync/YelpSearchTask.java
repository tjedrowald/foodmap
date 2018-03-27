package edu.avans.tjedrowald.foodmap.sync;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import edu.avans.tjedrowald.foodmap.interfaces.YelpSearchCallback;
import edu.avans.tjedrowald.foodmap.models.SearchResponse;
import edu.avans.tjedrowald.foodmap.sync.connection.YelpFusionApi;
import retrofit2.Call;

/**
 * Created by tjedrowald on 26-3-18.
 */

public class YelpSearchTask extends AsyncTask<Location, Void, SearchResponse> {

    private static final String TAG = YelpSearchTask.class.getSimpleName();

    private YelpFusionApi yelpAPI;
    private YelpSearchCallback callback;

    public YelpSearchTask(YelpFusionApi yelpAPI, YelpSearchCallback callback) {
        this.yelpAPI = yelpAPI;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onPreExecuteYelpQuery();
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
            queryParams.put("lang", Locale.getDefault().getLanguage()); // hmm, this should not be hard coded.
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
        callback.onPostExecuteYelpQuery(response);
    }
}
