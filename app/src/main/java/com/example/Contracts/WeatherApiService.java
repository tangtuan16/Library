package com.example.Contracts;

import com.example.Models.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("v1/forecast.json")
    Call<Weather> getForecast(
            @Query("key") String apiKey,
            @Query("q") String location,
            @Query("days") int days
    );
}
