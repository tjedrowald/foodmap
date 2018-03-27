package edu.avans.tjedrowald.foodmap.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;

import edu.avans.tjedrowald.foodmap.R;
import edu.avans.tjedrowald.foodmap.helpers.UniversalImageLoader;
import edu.avans.tjedrowald.foodmap.sync.connection.YelpFusionApi;
import edu.avans.tjedrowald.foodmap.sync.connection.YelpFusionApiFactory;

/**
 * Created by tjedrowald on 10-3-18.
 */

public abstract class BaseYelpActivity extends AppCompatActivity {
    protected static YelpFusionApi yelpAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        YelpFusionApiFactory factory = new YelpFusionApiFactory();
        try {
            yelpAPI = factory.createAPI(getResources().getString(R.string.yelp_api_key));
        } catch (IOException e) {
            Log.d("BaseMenuActivity", "IOException "+e);
            e.printStackTrace();
        }

        ImageLoader.getInstance().init(new UniversalImageLoader(this).getConfig());
    }

    protected void showSnackbar(final int mainTextStringId, final int actionStringId,
                                View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
            getString(mainTextStringId),
            Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(actionStringId), listener).show();
    }

}
