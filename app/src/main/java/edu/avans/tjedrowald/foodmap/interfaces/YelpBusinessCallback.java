package edu.avans.tjedrowald.foodmap.interfaces;

import edu.avans.tjedrowald.foodmap.models.Business;

/**
 * Created by tjedrowald on 26-3-18.
 */

public interface YelpBusinessCallback {
    void onPreExecuteYelpBusiness();
    void onPostExecuteYelpBusiness(Business business);
}
