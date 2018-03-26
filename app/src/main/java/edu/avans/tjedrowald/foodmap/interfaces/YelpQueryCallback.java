package edu.avans.tjedrowald.foodmap.interfaces;

import edu.avans.tjedrowald.foodmap.models.SearchResponse;

/**
 * Created by tjedrowald on 26-3-18.
 */

public interface YelpQueryCallback {
    void onPreExecute();
    void onPostExecute(SearchResponse searchResponse);
}
