package edu.avans.tjedrowald.foodmap.sync;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.avans.tjedrowald.foodmap.interfaces.YelpBusinessCallback;
import edu.avans.tjedrowald.foodmap.models.Business;
import edu.avans.tjedrowald.foodmap.sync.connection.YelpFusionApi;
import retrofit2.Call;

/**
 * Created by tjedrowald on 26-3-18.
 */
public class YelpBusinessTask extends AsyncTask<String, Void, Business> {

    private YelpFusionApi yelpAPI;
    private YelpBusinessCallback callback;

    public YelpBusinessTask(YelpFusionApi yelpAPI, YelpBusinessCallback callback) {
        this.yelpAPI = yelpAPI;
        this.callback = callback;
    }

    private static final String TAG = YelpBusinessTask.class.getSimpleName();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onPreExecuteYelpBusiness();
    }

    @Override
    protected Business doInBackground(String... params) {

        // no restaurant id, so nothing to lookup
        if(params[0] == null) return null;

        try {
            Call<Business> call = yelpAPI.getBusiness(params[0]);
            Business business= call.execute().body();
            return business;
        } catch (IOException e) {
            Log.d(TAG, "IOException "+e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Business business) {
        callback.onPostExecuteYelpBusiness(business);
    }
}
