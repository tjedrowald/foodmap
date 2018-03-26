package edu.avans.tjedrowald.foodmap.data;

import android.provider.BaseColumns;

/**x
 * Created by tjedrowald on 22-3-18.
 */

public class FavoritesContract {


    public static final class FavoritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_YELP_ID = "yelpId";
        public static final String COLUMN_BUSINESS = "businessName";
        public static final String COLUMN_IMAGE_URL = "imageUrl";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}