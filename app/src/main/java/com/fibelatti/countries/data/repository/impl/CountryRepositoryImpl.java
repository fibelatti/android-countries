package com.fibelatti.countries.data.repository.impl;

import com.fibelatti.countries.data.models.Country;
import com.fibelatti.countries.data.remote.CountryRepositoryRemote;
import com.fibelatti.countries.data.repository.CountryRepository;

import java.util.List;

import rx.Observable;

public class CountryRepositoryImpl
        implements CountryRepository {

    private CountryRepositoryRemote countryRepositoryRemote;

    public CountryRepositoryImpl(CountryRepositoryRemote countryRepositoryRemote) {
        this.countryRepositoryRemote = countryRepositoryRemote;
    }

    @Override
    public Observable<List<Country>> getAll() {
        return countryRepositoryRemote.getAll();
    }

    @Override
    public Observable<List<Country>> getByName(String name) {
        return countryRepositoryRemote.getByName(name);
    }

    @Override
    public Observable<List<Country>> search(String query) {
        return countryRepositoryRemote.search(query);
    }

    @Override
    public Observable<List<Country>> getByRegion(String region) {
        return countryRepositoryRemote.getByRegion(region);
    }
}
