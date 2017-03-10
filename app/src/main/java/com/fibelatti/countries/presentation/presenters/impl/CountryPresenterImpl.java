package com.fibelatti.countries.presentation.presenters.impl;

import com.fibelatti.countries.data.remote.impl.CountryRepositoryRemoteImpl;
import com.fibelatti.countries.data.repository.CountryRepository;
import com.fibelatti.countries.data.repository.impl.CountryRepositoryImpl;
import com.fibelatti.countries.helpers.AnalyticsHelper;
import com.fibelatti.countries.helpers.impl.AnalyticsHelperImpl;
import com.fibelatti.countries.presentation.presenters.CountryPresenter;
import com.fibelatti.countries.presentation.presenters.CountryPresenterView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CountryPresenterImpl
        implements CountryPresenter {

    private CountryPresenterView view;
    private CountryRepository countryRepository;
    private Subscription countrySubscription;
    private AnalyticsHelper analyticsHelper;

    private static final int OPERATION_ALL = 0;
    private static final int OPERATION_QUERY = 1;
    private static final int OPERATION_REGION = 2;

    private int currentOperation;
    private String query;
    private String region;

    private CountryPresenterImpl(CountryPresenterView view) {
        this.view = view;
    }

    public static CountryPresenterImpl createPresenter(CountryPresenterView view) {
        return new CountryPresenterImpl(view);
    }

    @Override
    public void setUp() {
        countryRepository = new CountryRepositoryImpl(new CountryRepositoryRemoteImpl());
        analyticsHelper = AnalyticsHelperImpl.getInstance();
    }

    @Override
    public void tearDown() {
        if (countrySubscription != null && !countrySubscription.isUnsubscribed())
            countrySubscription.unsubscribe();
    }

    @Override
    public void getAll() {
        this.currentOperation = OPERATION_ALL;

        analyticsHelper.fireGetAllEvent();

        if (countrySubscription != null && !countrySubscription.isUnsubscribed())
            countrySubscription.unsubscribe();

        countrySubscription = countryRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.clearListAndShowLoading())
                .doOnCompleted(() -> view.hideLoadAndNotifyChanges())
                .subscribe((countries) -> view.addResultsToList(countries), (throwable) -> view.handleError(throwable));
    }

    @Override
    public void search(String query) {
        this.currentOperation = OPERATION_QUERY;
        this.query = query;

        analyticsHelper.fireQueryEvent(query);

        if (countrySubscription != null && !countrySubscription.isUnsubscribed())
            countrySubscription.unsubscribe();

        countrySubscription = countryRepository.search(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.clearListAndShowLoading())
                .doOnCompleted(() -> view.hideLoadAndNotifyChanges())
                .subscribe((countries) -> view.addResultsToList(countries), (throwable) -> view.handleError(throwable));
    }

    @Override
    public void getByRegion(String region) {
        this.currentOperation = OPERATION_REGION;
        this.region = region;

        analyticsHelper.fireRegionEvent(region);

        if (countrySubscription != null && !countrySubscription.isUnsubscribed())
            countrySubscription.unsubscribe();

        countrySubscription = countryRepository.getByRegion(region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.clearListAndShowLoading())
                .doOnCompleted(() -> view.hideLoadAndNotifyChanges())
                .subscribe((countries) -> view.addResultsToList(countries), (throwable) -> view.handleError(throwable));
    }

    @Override
    public void restoreData() {
        switch (currentOperation) {
            case OPERATION_ALL:
                getAll();
                break;
            case OPERATION_QUERY:
                search(query);
                break;
            case OPERATION_REGION:
                getByRegion(region);
                break;
            default:
                getAll();
        }
    }
}
