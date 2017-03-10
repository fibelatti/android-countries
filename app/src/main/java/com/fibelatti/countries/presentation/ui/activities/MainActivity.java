package com.fibelatti.countries.presentation.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.fibelatti.countries.R;
import com.fibelatti.countries.data.models.Country;
import com.fibelatti.countries.presentation.presenters.CountryPresenter;
import com.fibelatti.countries.presentation.presenters.CountryPresenterView;
import com.fibelatti.countries.presentation.presenters.impl.CountryPresenterImpl;
import com.fibelatti.countries.presentation.ui.adapters.CountryAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity
        extends AppCompatActivity
        implements CountryPresenterView {
    public static final String TAG = MainActivity.class.getSimpleName();

    private CountryPresenter countryPresenter;
    private CountryAdapter countryAdapter;

    //region layout bindings
    @BindView(R.id.root_layout)
    CoordinatorLayout rootLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_placeholder)
    RelativeLayout layoutPlaceholder;
    @BindView(R.id.layout_content)
    RelativeLayout layoutContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recycler_view_content)
    RecyclerView recyclerViewContent;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        countryPresenter = CountryPresenterImpl.createPresenter(this);
        countryAdapter = new CountryAdapter(this);

        countryPresenter.setUp();

        setUpLayout();
        setUpRecyclerView();
        setUpValues();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countryPresenter.restoreData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countryPresenter.tearDown();
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    private void setUpValues() {
        setTitle(getString(R.string.main_title));
    }

    private void setUpRecyclerView() {
        swipeRefresh.setOnRefreshListener(() -> countryPresenter.restoreData());

        recyclerViewContent.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewContent.setItemAnimator(new DefaultItemAnimator());
        recyclerViewContent.setAdapter(countryAdapter);
    }

    @Override
    public void clearListAndShowLoading() {
        countryAdapter.clearList();
        swipeRefresh.setRefreshing(false);

        showLoading();
    }

    @Override
    public void hideLoadAndNotifyChanges() {
        countryAdapter.hideLoadingItem();
        countryAdapter.notifyDataSetChanged();
    }

    @Override
    public void addResultsToList(List<Country> countries) {
        countryAdapter.addManyToList(countries);
        showContentLayout();
    }

    @Override
    public void handleError(Throwable throwable) {
        showPlaceholderLayout();

        Log.e(getString(R.string.generic_log_error, TAG, "restoreData"), throwable.getMessage());
    }

    private void showLoading() {
        countryAdapter.showLoadingItem();
    }

    private void showContentLayout() {
        layoutContent.setVisibility(View.VISIBLE);
        layoutPlaceholder.setVisibility(View.GONE);
    }

    private void showPlaceholderLayout() {
        layoutContent.setVisibility(View.GONE);
        layoutPlaceholder.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_try_again)
    public void tryReload() {
        countryPresenter.restoreData();
    }
}
