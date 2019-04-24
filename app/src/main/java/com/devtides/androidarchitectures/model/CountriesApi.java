package com.devtides.androidarchitectures.model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface CountriesApi {
    @GET("all")
    Single<List<Country>> getCountries();
}
