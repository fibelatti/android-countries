package com.fibelatti.countries.presentation.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fibelatti.countries.R;
import com.fibelatti.countries.data.models.Country;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final String BASE_IMG_URL_250_PX = "https://github.com/hjnilsson/country-flags/blob/master/png250px/";

    private Context context;
    private List<Country> list;

    private boolean isLoadingVisible = false;
    private int loadingItemIndex = -1;

    class LoadingViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        LoadingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class DataViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view_country_flag)
        ImageView countryFlag;
        @BindView(R.id.text_view_country_name)
        TextView countryName;
        @BindView(R.id.text_view_country_region)
        TextView countryRegion;

        DataViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public CountryAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void clearList() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void addToList(Country item) {
        this.list.add(item);
    }

    public void addManyToList(List<Country> countries) {
        this.list.addAll(countries);
    }

    public String getCountryByIndex(int index) {
        return list.get(index).getName();
    }

    public void showLoadingItem() {
        this.isLoadingVisible = true;
        this.list.add(null);
        this.loadingItemIndex = list.size() - 1;
        notifyItemInserted(this.loadingItemIndex);
    }

    public void hideLoadingItem() {
        if (isLoadingVisible) {
            this.isLoadingVisible = false;
            this.list.remove(this.loadingItemIndex);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_country, parent, false);
            return new DataViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_loading, parent, false);
            return new LoadingViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataViewHolder) {
            DataViewHolder dataViewHolder = (DataViewHolder) holder;
            Country item = list.get(position);

            Picasso.with(context)
                    .load(BASE_IMG_URL_250_PX + item.getAlpha2Code().toLowerCase() + ".png?raw=true")
                    .into(dataViewHolder.countryFlag);

            dataViewHolder.countryName.setText(item.getName());
            dataViewHolder.countryRegion.setText(item.getRegion());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }
}
