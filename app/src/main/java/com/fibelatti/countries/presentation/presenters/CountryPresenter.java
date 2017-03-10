package com.fibelatti.countries.presentation.presenters;

public interface CountryPresenter {
    void setUp();

    void tearDown();

    void getAll();

    void search(String query);

    void getByRegion(String region);

    void restoreData();
}
