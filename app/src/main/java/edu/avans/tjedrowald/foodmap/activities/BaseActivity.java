package edu.avans.tjedrowald.foodmap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;

import edu.avans.tjedrowald.foodmap.R;
import edu.avans.tjedrowald.foodmap.helpers.UniversalImageLoader;
import edu.avans.tjedrowald.foodmap.sync.connection.YelpFusionApi;
import edu.avans.tjedrowald.foodmap.sync.connection.YelpFusionApiFactory;

/**
 * Created by tjedrowald on 1-3-18.
 */
public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected static YelpFusionApi yelpAPI;
    protected BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        YelpFusionApiFactory factory = new YelpFusionApiFactory();
        try {
            yelpAPI = factory.createAPI(getResources().getString(R.string.yelp_api_key));
        } catch (IOException e) {
            Log.d("BaseActivity", "IOException "+e);
            e.printStackTrace();
        }

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        ImageLoader.getInstance().init(new UniversalImageLoader(this).getConfig());
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.navigation_map:
                startActivity(new Intent(this, MapsActivity.class));
                return true;
            case R.id.navigation_favorites:
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
        }
        return false;
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    protected void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    protected void showSnackbar(final int mainTextStringId, final int actionStringId,
                                View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();

}