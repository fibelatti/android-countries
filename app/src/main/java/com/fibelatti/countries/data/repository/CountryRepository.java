package com.fibelatti.countries.data.repository;

import com.fibelatti.countries.data.models.Country;

import java.util.List;

import rx.Observable;

public interface CountryRepository {
    Observable<List<Country>> getAll();

    Observable<List<Country>> getByName(String name);

    Observable<List<Country>> search(String query);

    Observable<List<Country>> getByRegion(String region);
}
