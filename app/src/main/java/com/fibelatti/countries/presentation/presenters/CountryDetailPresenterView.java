package com.fibelatti.countries.presentation.presenters;

import com.fibelatti.countries.data.models.Country;

public interface CountryDetailPresenterView {
    void displayData(Country country);

    void handleError(Throwable throwable);
}
