package com.fibelatti.countries.presentation.presenters.impl;

import com.fibelatti.countries.data.remote.impl.CountryRepositoryRemoteImpl;
import com.fibelatti.countries.data.repository.CountryRepository;
import com.fibelatti.countries.data.repository.impl.CountryRepositoryImpl;
import com.fibelatti.countries.helpers.AnalyticsHelper;
import com.fibelatti.countries.helpers.impl.AnalyticsHelperImpl;
import com.fibelatti.countries.presentation.presenters.CountryDetailPresenter;
import com.fibelatti.countries.presentation.presenters.CountryDetailPresenterView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CountryDetailPresenterImpl
        implements CountryDetailPresenter {

    private CountryDetailPresenterView view;
    private CountryRepository countryRepository;
    private Subscription countrySubscription;
    private AnalyticsHelper analyticsHelper;

    private CountryDetailPresenterImpl(CountryDetailPresenterView view) {
        this.view = view;
    }

    public static CountryDetailPresenterImpl createPresenter(CountryDetailPresenterView view) {
        return new CountryDetailPresenterImpl(view);
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
    public void getCountryByName(String countryName) {
        analyticsHelper.fireDetailEvent(countryName);

        if (countrySubscription != null && !countrySubscription.isUnsubscribed())
            countrySubscription.unsubscribe();

        countrySubscription = countryRepository.getByName(countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((countries) -> view.displayData(countries.get(0)), (throwable) -> view.handleError(throwable));
    }
}
