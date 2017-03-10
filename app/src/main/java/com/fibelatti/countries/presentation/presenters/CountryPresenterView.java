package com.fibelatti.countries.presentation.presenters;

import com.fibelatti.countries.data.models.Country;

import java.util.List;

public interface CountryPresenterView {
    void clearListAndShowLoading();

    void hideLoadAndNotifyChanges();

    void addResultsToList(List<Country> countries);

    void handleError(Throwable throwable);
}
