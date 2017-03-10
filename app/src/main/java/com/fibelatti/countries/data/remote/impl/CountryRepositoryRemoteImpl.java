package com.fibelatti.countries.data.remote.impl;

import com.fibelatti.countries.data.models.Country;
import com.fibelatti.countries.data.remote.CountryRepositoryRemote;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class CountryRepositoryRemoteImpl
        implements CountryRepositoryRemote {

    private static Retrofit retrofit;
    private static CountryRepositoryRemote countryService;

    public CountryRepositoryRemoteImpl() {
        setUpRetrofit();
        createServices();
    }

    @Override
    public Observable<List<Country>> getAll() {
        return countryService.getAll();
    }

    @Override
    public Observable<List<Country>> getByName(String name) {
        return countryService.getByName(name);
    }

    @Override
    public Observable<List<Country>> search(String name) {
        return countryService.search(name);
    }

    @Override
    public Observable<List<Country>> getByRegion(String region) {
        return countryService.getByRegion(region);
    }

    private void setUpRetrofit() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(CountryRepositoryRemote.BASE_URL)
                .build();
    }

    private void createServices() {
        countryService = retrofit.create(CountryRepositoryRemote.class);
    }
}
