package edu.avans.tjedrowald.foodmap.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.avans.tjedrowald.foodmap.R;
import edu.avans.tjedrowald.foodmap.models.Business;
import edu.avans.tjedrowald.foodmap.models.Hour;
import edu.avans.tjedrowald.foodmap.models.Open;
import edu.avans.tjedrowald.foodmap.models.SearchResponse;
import retrofit2.Call;

import static edu.avans.tjedrowald.foodmap.models.Open.getHoursString;

public class SearchDetailActivity extends BaseYelpActivity {

    private static final String TAG = SearchDetailActivity.class.getSimpleName();

    //GUI
    private NestedScrollView nestedScrollView;
    private ProgressBar queryProgressBar;
    private ImageView backgroundImage;
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
        nestedScrollView.setVisibility(View.INVISIBLE);

        // init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("businessId") && intent.hasExtra("businessName") && intent.hasExtra("businessImage")) {
            businessId = intent.getStringExtra("businessId");
            toolbar.setTitle(intent.getStringExtra("businessName"));
            ImageLoader.getInstance().displayImage(intent.getStringExtra("businessImage"), backgroundImage);
        }
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadSearchResult();
    }

    private void loadSearchResult() {
        new YelpQueryTask().execute(businessId);
    }

    private void showSearchResult(final Business business) {
        businessPhoneTv.setText(business.getPhone());
        businessPhoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhoneNumber(business.getPhone());
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

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
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

    public class YelpQueryTask extends AsyncTask<String, Void, Business> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            queryProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Business doInBackground(String... params) {

            // no restaurant id, no nothing to lookup
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
            queryProgressBar.setVisibility(View.INVISIBLE);
            nestedScrollView.setVisibility(View.VISIBLE);
            if (business != null) {
                showSearchResult(business);
            }
            else {
                showErrorMessage();
            }
        }
    }
}

