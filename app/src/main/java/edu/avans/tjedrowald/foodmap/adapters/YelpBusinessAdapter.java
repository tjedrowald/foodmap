package edu.avans.tjedrowald.foodmap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import edu.avans.tjedrowald.foodmap.R;
import edu.avans.tjedrowald.foodmap.interfaces.YelpBusinessAdapterOnClickHandler;
import edu.avans.tjedrowald.foodmap.models.Business;
import edu.avans.tjedrowald.foodmap.models.SearchResponse;

/**
 * Created by tjedrowald on 2-3-18.
 */

public class YelpBusinessAdapter extends RecyclerView.Adapter<YelpBusinessAdapter.YelpSearchAdapterViewHolder>{

    private ArrayList<Business> searchResult;
    private Context context;
    private YelpBusinessAdapterOnClickHandler mClickHandler;


    public YelpBusinessAdapter(YelpBusinessAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
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
        Business business = searchResult.get(position);

        holder.mSearchItemTitle.setText(business.getName());
        ImageLoader.getInstance().displayImage(business.getImageUrl(), holder.mSearchItemIcon);
        if (business.getCategories() != null) {
            holder.mSearchItemCategories.setText((business.getCategoriesString()));
            holder.mSearchItemState .setText((business.getIsClosed() ? R.string.closed : R.string.open));
        }
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
        return searchResult.size();
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public class YelpSearchAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mSearchItemIcon;
        public final TextView mSearchItemTitle;
        public final TextView mSearchItemCategories;
        public final TextView mSearchItemState;

        public YelpSearchAdapterViewHolder(View view) {
            super(view);
            mSearchItemIcon = (ImageView) view.findViewById(R.id.res_icon);
            mSearchItemTitle = (TextView) view.findViewById(R.id.res_title);
            mSearchItemCategories = (TextView) view.findViewById(R.id.res_categories);
            mSearchItemState = (TextView) view.findViewById(R.id.res_state);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Business business = searchResult.get(adapterPosition);
            mClickHandler.onClick(business.getId(), business.getName(), business.getImageUrl());
        }
    }

    public void setSearchResult(ArrayList<Business> response) {
        searchResult = response;
        notifyDataSetChanged();
    }
}

