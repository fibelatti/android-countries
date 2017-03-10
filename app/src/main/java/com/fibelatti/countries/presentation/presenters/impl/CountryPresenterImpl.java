package com.fibelatti.countries.presentation.presenters.impl;

import com.fibelatti.countries.data.remote.impl.CountryRepositoryRemoteImpl;
import com.fibelatti.countries.data.repository.CountryRepository;
import com.fibelatti.countries.data.repository.impl.CountryRepositoryImpl;
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

    private static final int OPERATION_ALL = 0;

    private int currentOperation;

    private CountryPresenterImpl(CountryPresenterView view) {
        this.view = view;
    }

    public static CountryPresenterImpl createPresenter(CountryPresenterView view) {
        return new CountryPresenterImpl(view);
    }

    @Override
    public void setUp() {
        countryRepository = new CountryRepositoryImpl(new CountryRepositoryRemoteImpl());
    }

    @Override
    public void tearDown() {
        if (countrySubscription != null && !countrySubscription.isUnsubscribed())
            countrySubscription.unsubscribe();
    }

    @Override
    public void getAll() {
        this.currentOperation = OPERATION_ALL;

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
    public void restoreData() {
        switch (currentOperation) {
            case OPERATION_ALL:
                getAll();
                break;
            default:
                getAll();
        }
    }
}