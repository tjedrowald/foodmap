package edu.avans.tjedrowald.foodmap.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import edu.avans.tjedrowald.foodmap.R;
import edu.avans.tjedrowald.foodmap.data.FavoritesContract;
import edu.avans.tjedrowald.foodmap.data.FavoritesDbHelper;
import edu.avans.tjedrowald.foodmap.interfaces.YelpBusinessCallback;
import edu.avans.tjedrowald.foodmap.models.Business;
import edu.avans.tjedrowald.foodmap.models.Open;
import edu.avans.tjedrowald.foodmap.sync.YelpBusinessTask;

import static edu.avans.tjedrowald.foodmap.models.Open.getHoursString;

public class SearchDetailActivity extends BaseYelpActivity implements YelpBusinessCallback {

    //region private vars
    private static final String TAG = SearchDetailActivity.class.getSimpleName();

    private SQLiteDatabase database;

    //GUI
    private NestedScrollView nestedScrollView;
    private ProgressBar queryProgressBar;
    private ImageView backgroundImage;
    private FloatingActionButton fab;
    private TextView businessHoursTv;
    private TextView businessHoursMondayTv;
    private TextView businessHoursTuesdayTv;
    private TextView businessHoursWednesdayTv;
    private TextView businessHoursThursdayTv;
    private TextView businessHoursFridayTv;
    private TextView businessHoursSaturdayTv;
    private TextView businessHoursSundayTv;
    private TextView businessPhoneTv;
    private TextView businessAddress1Tv;
    private TextView businessZipCodeTv;
    private TextView businessCityTv;

    private String businessId;
    //endregion


    //region private methods
    private void loadSearchResult() {
        new YelpBusinessTask(yelpAPI, this).execute(businessId);
    }

    private void showSearchResult(final Business business) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int duration = Toast.LENGTH_LONG;

                if (removeFavorite(business)) {
                    Toast toast = Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.remove_from_favorites), business.getName()), duration);
                    toast.show();
                } else if (addFavorite(business)) {
                    Toast toast = Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.add_to_favorites), business.getName()), duration);
                    toast.show();
                }
            }
        });

        businessPhoneTv.setText(business.getPhone());
        businessPhoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + business.getPhone()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        businessHoursTv.setText((business.getIsClosed() ? R.string.closed : R.string.open));
        if (business.getHours() != null) {
            for (Open open : business.getHours().get(0).getOpen()) {
                switch (open.getDay()){
                    case 0 :
                        businessHoursMondayTv.setText(getHoursString(open));
                        break;
                    case 1 :
                        businessHoursTuesdayTv.setText(getHoursString(open));
                        break;
                    case 2 :
                        businessHoursWednesdayTv.setText(getHoursString(open));
                        break;
                    case 3 :
                        businessHoursThursdayTv.setText(getHoursString(open));
                        break;
                    case 4 :
                        businessHoursFridayTv.setText(getHoursString(open));
                        break;
                    case 5 :
                        businessHoursSaturdayTv.setText(getHoursString(open));
                        break;
                    case 6 :
                        businessHoursSundayTv.setText(getHoursString(open));
                        break;
                }
            }
        }
        businessAddress1Tv.setText(business.getLocation().getAddress1());
        businessZipCodeTv.setText(business.getLocation().getZipCode());
        businessCityTv.setText(business.getLocation().getCity());
    }

    private void showErrorMessage() {
        showSnackbar(R.string.error_message, android.R.string.ok,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadSearchResult();
                    }
                });
    }

    private boolean addFavorite(Business business) {
        ContentValues values = new ContentValues();
        // add values to record keys
        values.put(FavoritesContract.FavoritesEntry.COLUMN_YELP_ID, business.getId());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_BUSINESS, business.getName());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_IMAGE_URL, business.getImageUrl());
        return database.insert(FavoritesContract.FavoritesEntry.TABLE_NAME, null, values) > 0;
    }
    private boolean removeFavorite(Business business) {
        return database.delete(FavoritesContract.FavoritesEntry.TABLE_NAME,
                FavoritesContract.FavoritesEntry.COLUMN_YELP_ID + " = '" + business.getId() + "'",
                null) > 0;
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);

        // Get references to GUI
        nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scrollview);
        queryProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        backgroundImage = (ImageView) findViewById(R.id.background_image);
        businessPhoneTv = (TextView) findViewById(R.id.phone_title);
        businessHoursTv = (TextView) findViewById(R.id.hours_title);
        businessHoursMondayTv = (TextView) findViewById(R.id.monday_hours);
        businessHoursTuesdayTv = (TextView) findViewById(R.id.tuesday_hours);
        businessHoursWednesdayTv = (TextView) findViewById(R.id.wednesday_hours);
        businessHoursThursdayTv = (TextView) findViewById(R.id.thursday_hours);
        businessHoursFridayTv = (TextView) findViewById(R.id.friday_hours);
        businessHoursSaturdayTv = (TextView) findViewById(R.id.saturday_hours);
        businessHoursSundayTv = (TextView) findViewById(R.id.sunday_hours);
        businessAddress1Tv = (TextView) findViewById(R.id.address_1);
        businessZipCodeTv = (TextView) findViewById(R.id.zip_code);
        businessCityTv = (TextView) findViewById(R.id.city);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        nestedScrollView.setVisibility(View.INVISIBLE);

        // init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("businessId") && intent.hasExtra("businessName") && intent.hasExtra("businessImage")) {
            businessId = intent.getStringExtra("businessId");
            toolbar.setTitle(intent.getStringExtra("businessName"));
            ImageLoader.getInstance().displayImage(intent.getStringExtra("businessImage"), backgroundImage);

            // load more business info from API
            loadSearchResult();
        }
        setSupportActionBar(toolbar);

        // Create a database helper
        FavoritesDbHelper dbHelper = new FavoritesDbHelper(this);
        // Get writable database
        database = dbHelper.getWritableDatabase();

    }

    //region public methods
    @Override
    public void onPreExecuteYelpBusiness() {
        queryProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecuteYelpBusiness(Business business) {
        queryProgressBar.setVisibility(View.INVISIBLE);
        nestedScrollView.setVisibility(View.VISIBLE);
        if (business != null) {
            showSearchResult(business);
        }
        else {
            showErrorMessage();
        }
    }
    //endregion
}

