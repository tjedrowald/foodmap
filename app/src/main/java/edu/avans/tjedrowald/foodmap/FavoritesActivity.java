package edu.avans.tjedrowald.foodmap;

/**
 * Created by tjedrowald on 1-3-18.
 */

public class FavoritesActivity extends BaseActivity {
    @Override
    int getContentViewId() {
        return R.layout.activity_favorites;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_favorites;
    }
}
