package edu.avans.tjedrowald.foodmap.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.reward.mediation.InitializableMediationRewardedVideoAdAdapter;

import java.util.ArrayList;

import edu.avans.tjedrowald.foodmap.R;
import edu.avans.tjedrowald.foodmap.adapters.YelpBusinessAdapter;
import edu.avans.tjedrowald.foodmap.data.FavoritesContract;
import edu.avans.tjedrowald.foodmap.data.FavoritesDbHelper;
import edu.avans.tjedrowald.foodmap.interfaces.YelpBusinessAdapterOnClickHandler;
import edu.avans.tjedrowald.foodmap.models.Business;
import edu.avans.tjedrowald.foodmap.models.SearchResponse;

/**
 * Created by tjedrowald on 1-3-18.
 */

public class FavoritesActivity extends BaseMenuActivity implements YelpBusinessAdapterOnClickHandler {

    // GUI
    private TextView noFavoritesTv;
    private ImageView noFavoritesImg;
    private RecyclerView recyclerView;

    private SQLiteDatabase database;
    private YelpBusinessAdapter yelpAdapter;

    private ArrayList<Business> getFavorites() {
        ArrayList<Business> favorites;
        Cursor cursor =  database.query(FavoritesContract.FavoritesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoritesContract.FavoritesEntry.COLUMN_TIMESTAMP + " DESC");

        try {
            favorites = new ArrayList<>();
            while (cursor.moveToNext()) {
                String yelpId = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_YELP_ID));
                String businessName = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_BUSINESS));
                String businessImage = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_IMAGE_URL));
                Business business = new Business();
                business.setId(yelpId);
                business.setName(businessName);
                business.setImageUrl(businessImage);
                favorites.add(business);
            }
        } finally {
            cursor.close();
        }
        return favorites;
    }

    private void showFavorites() {
        ArrayList<Business> businesses = getFavorites();
        if (businesses.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            yelpAdapter.setSearchResult(getFavorites());
            noFavoritesTv.setVisibility(View.INVISIBLE);
            noFavoritesImg.setVisibility(View.INVISIBLE);
        }else {
            recyclerView.setVisibility(View.INVISIBLE);
            noFavoritesTv.setVisibility(View.VISIBLE);
            noFavoritesImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a database helper
        FavoritesDbHelper dbHelper = new FavoritesDbHelper(this);
        // Get writable database
        database = dbHelper.getWritableDatabase();

        yelpAdapter = new YelpBusinessAdapter(this);

        noFavoritesTv = findViewById(R.id.no_favorites_text);
        noFavoritesImg = findViewById(R.id.no_favorites);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(yelpAdapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        showFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFavorites();
    }

    @Override
    public void onClick(String Id, String BusinessName, String BusinessImage) {
        Intent intent = new Intent(this, SearchDetailActivity.class);
        intent.putExtra("businessId", Id);
        intent.putExtra("businessName", BusinessName);
        intent.putExtra("businessImage", BusinessImage);
        startActivity(intent);
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_favorites;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_favorites;
    }
}
