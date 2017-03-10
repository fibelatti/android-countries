package com.fibelatti.countries.data.remote;

import com.fibelatti.countries.data.models.Country;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface CountryRepositoryRemote {
    String BASE_URL = "https://restcountries.eu/rest/v2/";

    @GET("all")
    Observable<List<Country>> getAll();

    @GET("name/{name}?fullText=true")
    Observable<List<Country>> getByName(
            @Path("name") String name
    );

    @GET("name/{name}")
    Observable<List<Country>> search(
            @Path("name") String name
    );

    @GET("region/{region}")
    Observable<List<Country>> getByRegion(
            @Path("region") String region
    );
}
