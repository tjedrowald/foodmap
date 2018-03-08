package edu.avans.tjedrowald.foodmap.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import edu.avans.tjedrowald.foodmap.R;
import edu.avans.tjedrowald.foodmap.models.Business;
import edu.avans.tjedrowald.foodmap.models.Category;
import edu.avans.tjedrowald.foodmap.models.SearchResponse;

/**
 * Created by tjedrowald on 2-3-18.
 */

public class YelpSearchAdapter extends RecyclerView.Adapter<YelpSearchAdapter.YelpSearchAdapterViewHolder>{

    private SearchResponse searchResult;
    private Context context;

    //Create the default constructor (we will pass in parameters in later)
    public YelpSearchAdapter(){

    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent    The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new YelpSearchAdapterViewHolder that holds the View for each list item
     */
    @Override
    public YelpSearchAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.element_search_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new YelpSearchAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the search
     * result for this particular position, using the "position" argument.
     *
     * @param holder    The ViewHolder which should be updated to represent the
     *                  contents of the item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(YelpSearchAdapterViewHolder holder, int position) {
        Business business = searchResult.getBusinesses().get(position);
        holder.mSearchItemTitle.setText(business.getName());
        holder.mSearchItemCategories.setText(makeCategoriesString(business.getCategories()));
        holder.mSearchItemState .setText((business.getIsClosed() ? R.string.closed : R.string.open));
        ImageLoader.getInstance().displayImage(business.getImageUrl(), holder.mSearchItemIcon);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == searchResult) return 0;
        return searchResult.getBusinesses().size();
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public class YelpSearchAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mSearchItemIcon;
        public final TextView mSearchItemTitle;
        public final ImageView mSearchItemRating;
        public final TextView mSearchItemCategories;
        public final TextView mSearchItemState;

        public YelpSearchAdapterViewHolder(View view) {
            super(view);
            mSearchItemIcon = (ImageView) view.findViewById(R.id.res_icon);
            mSearchItemTitle = (TextView) view.findViewById(R.id.res_title);
            mSearchItemRating = (ImageView) view.findViewById(R.id.res_rating);
            mSearchItemCategories = (TextView) view.findViewById(R.id.res_categories);
            mSearchItemState = (TextView) view.findViewById(R.id.res_state);
        }
    }

    public void setSearchResult(SearchResponse response) {
        searchResult = response;
        notifyDataSetChanged();
    }

    private String makeCategoriesString(ArrayList<Category> categories){
        String catString = "";
        for(Category cat : categories){
            catString = catString + (catString != "" ? ", " : "") + cat.getTitle();
        }
        return catString;
    }
}

