package com.fibelatti.countries.presentation.presenters;

public interface CountryDetailPresenter {
    void setUp();

    void tearDown();

    void getCountryByName(String countryName);
}
